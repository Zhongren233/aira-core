package moe.aira.resp.hekk;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccessTokenResponse extends ServerResponse {
    private String accessToken;
}
