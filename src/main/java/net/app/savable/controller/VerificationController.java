package net.app.savable.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.Challenge;
import net.app.savable.domain.challenge.ParticipationChallenge;
import net.app.savable.domain.challenge.Verification;
import net.app.savable.domain.challenge.VerificationState;
import net.app.savable.domain.challenge.dto.VerificationDetailDto;
import net.app.savable.domain.challenge.dto.VerificationRequestDto;
import net.app.savable.domain.member.Member;
import net.app.savable.global.common.S3UploadService;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.global.error.exception.ErrorCode;
import net.app.savable.service.AsyncService;
import net.app.savable.service.MemberService;
import net.app.savable.service.ParticipationChallengeService;
import net.app.savable.service.VerificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/participations")
@RequiredArgsConstructor
public class VerificationController {

    private final MemberService memberService;
    private final VerificationService verificationService;
    private final ParticipationChallengeService participationChallengeService;
    private final S3UploadService s3UploadService;
    private final AsyncService asyncService;

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
                .state(VerificationState.WAITING)
                .member(member)
                .aiState(VerificationState.WAITING)
                .build();

        Verification savedVerification = verificationService.addVerification(verificationRequestDto);

        // 비동기 처리
        Challenge challenge = participationChallenge.getChallenge();
        final CompletableFuture<Boolean> certResult;
        if (challenge.getIsOcrNeeded()){ // OCR이 필요한 경우
            certResult = asyncService.callFlaskOcrApiAsync(saveFileName, challenge.getVerificationPrompt());
        } else { // OCR이 필요하지 않은 경우
            certResult = asyncService.callFlaskImageCaptioningApiAsync(saveFileName, challenge.getVerificationPrompt());
        }
        certResult.thenAccept(result -> {
            if (result) {
                verificationService.updateVerificationState(savedVerification.getId(), VerificationState.SUCCESS);
            } else {
                verificationService.updateVerificationState(savedVerification.getId(), VerificationState.FAIL);
            }
        });

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
