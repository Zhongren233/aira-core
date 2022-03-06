package moe.aira.aspect;

import moe.aira.entity.api.ApiResult;
import moe.aira.exception.AiraException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AiraControllerAdvice {

    @ExceptionHandler(AiraException.class)
    public ApiResult<Void> apiResult(AiraException e) {
        return ApiResult.fail(e.errorCode(), e.getMessage());
    }
}
