package net.app.savable.global.error;

import net.app.savable.global.error.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiResponseSampleController { // 예외 처리를 위한 컨트롤러(샘플)

    @GetMapping("/failExample")
    public ApiResponse<String> failExample() { // ApiResponse.fail() 메서드를 사용하여 실패 응답을 생성
        return ApiResponse.fail(ErrorCode.BAD_REQUEST);
    }

    @GetMapping("/successExample")
    public ApiResponse<String> successExample() { // ApiResponse.success() 메서드를 사용하여 성공 응답을 생성
        return ApiResponse.success("success");
    }
}
