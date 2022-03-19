package moe.aira.onebot.util;


import moe.aira.onebot.config.EventConfig;
import moe.aira.onebot.entity.AiraUser;

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
        EVENT_CONFIG_THREAD_LOCAL.remove();
        USER_THREAD_LOCAL.remove();
    }
}
