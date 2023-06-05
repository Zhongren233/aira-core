package moe.aira.entity.hekk;

import lombok.Data;
import moe.aira.enums.AppStatusCode;
@Data
public class ServerResponse {

    protected AppStatusCode appStatusCode;
    protected Long serverVersion;
    protected String currentTime;


}
