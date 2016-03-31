package com.hao.util;

/**
 * 辅助工具类  增加preconditions没有的功能
 * Created by user on 2016/3/30.
 */
public class Asserts {

    public static String checkNotBlank(String reference) {
        return checkNotBlank(reference,null);
    }

    public static String checkNotBlank(String reference, Object errorMessage) {
        if (reference == null || reference.trim().length() == 0) {
            throwIllegalArgumentException(errorMessage);
        }
        return reference;
    }

    public static String checkNotEmpty(String reference) {
        return checkNotEmpty(reference,null);
    }

    private static String checkNotEmpty(String reference, Object errorMessage) {
        if (reference == null || reference.length() == 0) {
            throwIllegalArgumentException(errorMessage);
        }
        return reference;
    }


    private static void throwIllegalArgumentException(Object errorMessage) {
        throw errorMessage == null ? new IllegalArgumentException() : new IllegalArgumentException(String.valueOf(errorMessage));
    }
}
