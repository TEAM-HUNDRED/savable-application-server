package net.app.savable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.member.AccountState;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.dto.ChallengeInfoResponseDto;
import net.app.savable.domain.member.dto.MemberInfoResponseDto;
import net.app.savable.domain.member.dto.MemberSignUpRequestDto;
import net.app.savable.global.common.S3UploadService;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.global.error.exception.ErrorCode;
import net.app.savable.service.MemberService;
import net.app.savable.service.ParticipationChallengeService;
import net.app.savable.service.VerificationService;
import org.joda.time.DateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class MemberController {

    private final MemberService memberService;
    private final VerificationService verificationService;
    private final ParticipationChallengeService participationChallengeService;
    private final S3UploadService s3UploadService;

    @GetMapping("/my-page")
    public ApiResponse<MemberInfoResponseDto> myPageDetails(@LoginMember SessionMember sessionMember,
                                                            HttpServletRequest request) {

        log.info("MemberController.myPageDetails() 실행");
        System.out.printf("\n\n////////마이페이지 API 호출////////\n");
        System.out.printf("호출 시간: %s\n", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("*** request headers ***\n");
        printRequestInfo(request);

        Long memberId = sessionMember.getId();
        Member member = memberService.findById(memberId);
        Long verificationCount = verificationService.findTotalVerificationCount(memberId); // 지금까지 총 인증 횟수
        Long scheduledReward = participationChallengeService.findScheduledReward(memberId); // 예정된 리워드

        if (scheduledReward == null) {
            scheduledReward = 0L;
        }

        ChallengeInfoResponseDto challengeInfoResponseDto = participationChallengeService.findChallengeSummary(member.getId());
        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.builder()
                .username(member.getUsername())
                .profileImage(member.getProfileImage())
                .phoneNumber(member.getPhoneNumber())
                .totalSavings(member.getSavings())
                .totalReward(member.getReward())
                .scheduledReward(scheduledReward)
                .verificationCount(verificationCount)
                .challengeInfoResponseDto(challengeInfoResponseDto)
                .build();

        return ApiResponse.success(memberInfoResponseDto);
    }

    @PatchMapping("/member/deletion")
    public ApiResponse<String> memberRemove(@LoginMember SessionMember sessionMember, HttpServletRequest request) {
        log.info("MemberController.deleteMember() 실행");

        memberService.deleteMember(memberService.findById(sessionMember.getId()));

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        return ApiResponse.success("회원 탈퇴가 완료되었습니다.");
    }

    @PatchMapping("/member/settings") // 유저 정보 수정(프로필이 FormData인 경우)
    public ApiResponse<String> memberProfileUpdate(
            @LoginMember SessionMember sessionMember,
            @RequestParam("image") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("phoneNumber") String phoneNumber) {

        log.info("MemberController.memberProfileUpdate() 실행");

        // 유효성 검사
        String usernamePattern = "^[ 가-힣a-zA-Z0-9]*$"; // 한글, 영문, 숫자만 입력 가능
        String phoneNumberPattern = "^010[0-9]{8}$"; // 01012345678 형식

        if(!isValid(username, usernamePattern)) {
            return ApiResponse.fail(ErrorCode.DATA_INTEGRITY_VIOLATION, "닉네임은 한글, 영문, 숫자만 입력 가능합니다.");
        } else if(!isValid(phoneNumber, phoneNumberPattern)) {
            return ApiResponse.fail(ErrorCode.DATA_INTEGRITY_VIOLATION, "전화번호 형식이 올바르지 않습니다.");
        } else if (username.length() > 10 || username.length() < 2) {
            return ApiResponse.fail(ErrorCode.DATA_INTEGRITY_VIOLATION, "닉네임은 2자 이상 10자 이하로 입력해주세요.");
        }

        Member memberByPhoneNumber = memberService.findByPhoneNumberAndAccountStateNot(phoneNumber, AccountState.DELETED);
        if (memberByPhoneNumber != null && !(memberByPhoneNumber.getId().equals(sessionMember.getId()))) { // 이미 존재하는 phoneNumber
            return ApiResponse.fail(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 전화번호입니다.");
        }

        // 이미지 다운로드
        String saveFileName; // S3에 저장된 파일 이름
        try { // S3에 프로필 이미지 업로드
            log.info("S3에 이미지 업로드");
            String fileName = generateFileName(sessionMember.getId());
            saveFileName = s3UploadService.saveFile(file, fileName);
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, "S3에 이미지 업로드를 실패했습니다.");
        }

        Member member = memberService.findById(sessionMember.getId());
        try {
            memberService.updateMember(member, username, saveFileName, phoneNumber);
        } catch (DataIntegrityViolationException ex) {
            return ApiResponse.fail(ErrorCode.DATA_INTEGRITY_VIOLATION, ex.getMessage()); // 예외 메시지를 반환
        }

        return ApiResponse.success("회원 정보 수정이 완료되었습니다.");
    }

    @PatchMapping("/member/sign-up") // 유저 정보 수정(프로필이 URL인 경우)
    public ApiResponse<String> memberSignUp(
            @LoginMember SessionMember sessionMember,
            @RequestBody MemberSignUpRequestDto memberSignUpRequestDto,
            HttpServletRequest request) {

        log.info("MemberController.memberProfileUpdate() 실행");
        System.out.printf("\n\n////////회원가입 API 호출////////\n");
        System.out.printf("호출 시간: %s\n", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("*** request headers ***\n");
        printRequestInfo(request);

        String username = memberSignUpRequestDto.getUsername();
        String phoneNumber = memberSignUpRequestDto.getPhoneNumber();
        String imageUrl = memberSignUpRequestDto.getImageUrl();

        // 유효성 검사
        String usernamePattern = "^[ 가-힣a-zA-Z0-9]*$"; // 한글, 영문, 숫자만 입력 가능
        String phoneNumberPattern = "^010[0-9]{8}$"; // 01012345678 형식

        if(!isValid(username, usernamePattern)) {
            return ApiResponse.fail(ErrorCode.DATA_INTEGRITY_VIOLATION, "닉네임은 한글, 영문, 숫자만 입력 가능합니다.");
        } else if(!isValid(phoneNumber, phoneNumberPattern)) {
            return ApiResponse.fail(ErrorCode.DATA_INTEGRITY_VIOLATION, "전화번호 형식이 올바르지 않습니다.");
        } else if (username.length() > 10 || username.length() < 2) {
            return ApiResponse.fail(ErrorCode.DATA_INTEGRITY_VIOLATION, "닉네임은 2자 이상 10자 이하로 입력해주세요.");
        }

        Member memberByPhoneNumber = memberService.findByPhoneNumberAndAccountStateNot(phoneNumber, AccountState.DELETED);
        if (memberByPhoneNumber != null && !(memberByPhoneNumber.getId().equals(sessionMember.getId()))) { // 이미 존재하는 phoneNumber
            return ApiResponse.fail(ErrorCode.ALREADY_EXIST_PHONENUMBER, "이미 존재하는 전화번호입니다.");
        }

        // 이미지 다운로드
        String saveFileName; // S3에 저장된 파일 이름
        try { // S3에 프로필 이미지 업로드
            log.info("S3에 이미지 업로드");

            URL url = new URL(imageUrl);
            String fileName = generateFileName(sessionMember.getId());

            Path tempFile = Files.createTempFile("temp", ".jpg");
            Files.copy(url.openStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            saveFileName = s3UploadService.saveImageUrl(tempFile, fileName);
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, "S3에 이미지 업로드를 실패했습니다.");
        }

        Member member = memberService.findById(sessionMember.getId());
        try {
            memberService.updateMember(member, username, saveFileName, phoneNumber);
        } catch (DataIntegrityViolationException ex) {
            return ApiResponse.fail(ErrorCode.DATA_INTEGRITY_VIOLATION, ex.getMessage()); // 예외 메시지를 반환
        }

        return ApiResponse.success("회원 정보 수정이 완료되었습니다.");
    }

    public static String generateFileName(Long memberId) {
        String uuid = UUID.randomUUID().toString().replace("-", "");

        return String.format("profile/member_%d_%s.jpg",
                memberId, uuid);
    }

    private boolean isValid(String input, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    @GetMapping("/member/logout")
    public ApiResponse<String> memberLogout(HttpSession session, @LoginMember SessionMember sessionMember
            , HttpServletRequest request){
        System.out.printf("\n\n////////로그아 API 호출////////\n");
        System.out.printf("호출 시간: %s\n", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("*** request headers ***\n");
        printRequestInfo(request);

        session.invalidate();
        return ApiResponse.success("로그아웃이 완료되었습니다.");
    }

    public void printRequestInfo(HttpServletRequest request) {
        // 요청 헤더 출력
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("\n");
        }

        // 요청 URL 출력
        String requestURL = request.getRequestURL().toString();

        System.out.printf("[URL]\nURL: %s\n\n[Request Headers]\n%s", requestURL, headers.toString());
        System.out.printf("*********************\n\n");
    }
}
