package moe.aira.onebot.util;

import moe.aira.onebot.entity.AiraGroup;

public class AiraGroupContext {

    private static final ThreadLocal<AiraGroup> GROUP_THREAD_LOCAL = new ThreadLocal<>();

    private AiraGroupContext() {
    }

    public static AiraGroup currentUser() {
        return GROUP_THREAD_LOCAL.get();
    }

    public static void set(AiraGroup user) {
        GROUP_THREAD_LOCAL.set(user);
    }

    public static void clear() {
        GROUP_THREAD_LOCAL.remove();
    }
}
