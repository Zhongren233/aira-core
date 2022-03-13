package moe.aira.onebot.util;


import moe.aira.onebot.entity.AiraUser;

public class AiraUserContext {
    static final ThreadLocal<AiraUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    private AiraUserContext() {
    }

    public static AiraUser currentUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void set(AiraUser user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}
