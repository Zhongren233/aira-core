package moe.aira.core.entity.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiResult {
    private Integer code;
    private String message;
    private Object data;


    public static ApiResult success() {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(200);
        return apiResult;
    }

    public static ApiResult success(Object data) {
        return success().setData(data);
    }


}
