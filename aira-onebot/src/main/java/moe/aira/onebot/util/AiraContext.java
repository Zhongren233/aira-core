package moe.aira.onebot.util;


import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.onebot.entity.AiraUser;

@Slf4j
public class AiraContext {
    static final ThreadLocal<AiraUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    static final ThreadLocal<EventConfig> EVENT_CONFIG_THREAD_LOCAL = new ThreadLocal<>();

    private AiraContext() {
    }

    public static AiraUser currentUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void setUser(AiraUser user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static EventConfig getEventConfig() {
        return EVENT_CONFIG_THREAD_LOCAL.get();
    }

    public static void setEventConfig(EventConfig eventConfig) {
        EVENT_CONFIG_THREAD_LOCAL.set(eventConfig);
    }

    public static void clear() {
        if (log.isDebugEnabled()) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                log.debug("thread stack {} . {} :{}", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getLineNumber());
            }
            log.debug("Thread {} Clearing ", Thread.currentThread().getName());
        }

        EVENT_CONFIG_THREAD_LOCAL.remove();
        USER_THREAD_LOCAL.remove();
    }
}
