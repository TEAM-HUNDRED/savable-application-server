package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.ParticipationChallenge;
import net.app.savable.domain.challenge.VerificationState;
import net.app.savable.domain.challenge.dto.VerificationDetailDto;
import net.app.savable.domain.challenge.dto.VerificationRequestDto;
import net.app.savable.domain.member.Member;
import net.app.savable.global.common.S3UploadService;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.global.error.exception.ErrorCode;
import net.app.savable.service.MemberService;
import net.app.savable.service.ParticipationChallengeService;
import net.app.savable.service.VerificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/participations")
@RequiredArgsConstructor
public class VerificationController {

    private final MemberService memberService;
    private final VerificationService verificationService;
    private final ParticipationChallengeService participationChallengeService;
    private final S3UploadService s3UploadService;

    @PostMapping("/{participationId}/verification")
    public ApiResponse<String> verificationAdd(@RequestParam("image")MultipartFile file, @PathVariable Long participationId, @LoginMember SessionMember sessionMember) {

        log.info("verificationService.verificationAdd");
        Member member = memberService.findById(sessionMember.getId());
        ParticipationChallenge participationChallenge = participationChallengeService.findParticipationChallengeById(participationId);

        // 자신의 챌린지인지 확인
        if (!participationChallenge.getMemberId().equals(member.getId())) {
            return ApiResponse.fail(ErrorCode.FORBIDDEN, "사용자 본인의 챌린지가 아닌 경우 인증할 수 없습니다.");
        }

        String saveFileName; // S3에 저장된 파일 이름
        try {
            log.info("S3에 이미지 업로드");
            String fileName = generateFileName(member.getId(), participationId);
            saveFileName = s3UploadService.saveFile(file, fileName);
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, "S3에 이미지 업로드를 실패했습니다.");
        }

        VerificationRequestDto verificationRequestDto = VerificationRequestDto.builder()
                .participationChallenge(participationChallenge)
                .image(saveFileName)
                .state(VerificationState.SUCCESS)
                .member(member)
                .build();

        verificationService.addVerification(verificationRequestDto);
        return ApiResponse.success("인증이 완료되었습니다.");
    }

    public static String generateFileName(Long memberId, Long participationId) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return String.format("verification/member_%d/participation_%d_%s.jpg",
                memberId, participationId, uuid);
    }

    @GetMapping("/{participationId}/verification")
    public ApiResponse<VerificationDetailDto> verificationDetails(@PathVariable Long participationId, @LoginMember SessionMember sessionMember){
        log.info("verificationDetails participationId : {}", participationId);
        VerificationDetailDto verificationDetail = verificationService.findVerificationDetail(participationId);
        return ApiResponse.success(verificationDetail);
    }
}
