package moe.aira.onebot.client.skyland;

/**
 * 登录状态失效（如服务端返回"用户未登录"），此时应清除已保存的 token 并提示重新绑定。
 */
public class SkylandLoginExpiredException extends SkylandApiException {

    public SkylandLoginExpiredException(String message) {
        super(message);
    }
}
