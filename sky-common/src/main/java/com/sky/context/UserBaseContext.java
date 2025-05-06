package com.sky.context;

public class UserBaseContext {
    private static final ThreadLocal<Long> currentId = new ThreadLocal<>();
    public static void setCurrentId(Long id) {
        currentId.set(id);
    }
    public static Long getCurrentId() {
        return currentId.get();
    }
    public static void remove() {
        currentId.remove();
    }
}