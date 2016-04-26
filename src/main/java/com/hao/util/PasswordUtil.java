package com.hao.util;

import com.hao.util.password.ShaEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;

/**
 * Created by user on 2016/2/24.
 */
public class PasswordUtil {

    private PasswordUtil(){}

    /**
     * 密码加密的工具类 用来生成一个不可逆的加密字符串和盐值
     * @param rawPassword  未加密的密码
     * @return  加密后的密码
     */
    public static Pair<String,String> encodePassword(String rawPassword) {
        String salt = generateSalt(null);
        return ImmutablePair.of(encodePassword(rawPassword,salt),salt);
    }


    public static boolean isPasswordVaild(String rawPassword, String encryptedPassword, String salt) {
        return encodePassword(rawPassword,salt).equals(encryptedPassword);
    }

    /**
     * 用来生成一个不可逆的加密字符串
     * @param rawPassword  未加密的密码
     * @param salt   盐值
     * @return  加密后的密码
     */
    public static String encodePassword(String rawPassword,String salt) {
        ShaEncoder encoder = new ShaEncoder(512);
        return encoder.encodePassword(checkNotNull(emptyToNull(rawPassword),"待加密的密码"),checkNotNull(emptyToNull(salt),"加密盐值"));
    }

    /**
     * 按照给定长度生成指定的随机字符串作为盐值
     * @param length
     * @return
     */
    public static String generateSalt(Integer length) {
        return RandomStringUtils.randomAlphanumeric(length == null ? 32 : length);
    }


}
