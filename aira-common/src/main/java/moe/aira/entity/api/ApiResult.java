package moe.aira.entity.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiResult<T> {
    private Integer code;
    private String message = "";
    private T data;

    private ApiResult() {
    }

    private ApiResult(T data) {
        code = 0;
        this.data = data;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(data);
    }

    public static ApiResult<Void> success() {
        return new ApiResult<>(null);
    }

    public static ApiResult<Void> fail(Integer code) {
        ApiResult<Void> result = new ApiResult<>();
        result.setCode(code);
        result.setMessage("");
        return result;
    }

    public static ApiResult<Void> fail(String message) {
        ApiResult<Void> result = new ApiResult<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static ApiResult<Void> fail(Integer code, String message) {
        ApiResult<Void> result = new ApiResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
