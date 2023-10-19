package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.member.AccountState;
import net.app.savable.domain.member.Member;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.global.error.exception.ErrorCode;
import net.app.savable.service.MemberService;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PhoneNumberVerificationController {

    private final MemberService memberService;
    private final DefaultMessageService messageService;
    private final String apiKey;
    private final String apiSecret;

    @Autowired
    public PhoneNumberVerificationController(
            MemberService memberService,
            @Value("${sms.api.key}") String apiKey,
            @Value("${sms.api.secret}") String apiSecret) {
        this.memberService = memberService;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    @PostMapping("/send-sms")
    public ApiResponse<String> smsSend(@RequestBody HashMap<String, String> phoneNumber) {
        log.info("PhoneNumberVerificationController.sendSms() 실행");

        String number = phoneNumber.get("phoneNumber");
        if (memberService.findByPhoneNumberAndAccountStateNot(number, AccountState.DELETED) != null) { // 이미 가입된 번호라면
            return ApiResponse.fail(ErrorCode.ALREADY_EXIST_PHONENUMBER, "이미 가입된 번호입니다.");
        }

        Message message = new Message();
        String randomNum = generateRandomNumber();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01082081162");
        message.setTo(number);
        message.setText("[Savable] 인증번호는 [" + randomNum + "] 입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return ApiResponse.success(randomNum);
    }

    public String generateRandomNumber() { // 핸드폰 인증 번호 생성
        Random rand = new Random();
        String numberStr = "";
        for (int i = 0; i < 6; i++) {
            numberStr += Integer.toString(rand.nextInt(10));
        }
        return numberStr;
    }
}
