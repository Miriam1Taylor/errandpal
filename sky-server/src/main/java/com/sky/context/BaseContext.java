//package com.sky.context;
//
//public class BaseContext {
//    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();
//
//    // 設置當前登錄用戶id
//    public static void setCurrentId(Long id) {
//        THREAD_LOCAL.set(id);
//    }
//
//    // 獲取當前登錄用戶id
//    public static Long getCurrentId() {
//        return THREAD_LOCAL.get();
//    }
//
//    // 清除ThreadLocal中的數據（防止內存洩漏）
//    public static void removeCurrentId() {
//        THREAD_LOCAL.remove();
//    }
//}