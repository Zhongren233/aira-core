package moe.aira.api.aspect;

import moe.aira.entity.api.ApiResult;
import moe.aira.exception.AiraException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AiraControllerAdvice {
    @ResponseBody
    @ExceptionHandler(AiraException.class)
    public ApiResult<Void> apiResult(AiraException e) {
        return ApiResult.fail(e.errorCode(), e.getMessage());
    }
}
