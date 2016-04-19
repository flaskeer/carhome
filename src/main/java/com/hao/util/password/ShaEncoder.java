package com.hao.util.password;

import com.hao.util.password.codec.Base64;
import com.hao.util.password.codec.Hex;
import com.hao.util.password.codec.Utf8;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by user on 2016/4/19.
 */
public class ShaEncoder {

    private final String algorithm;
    private int iterations = 1;
    private boolean encodeHashAsBase64 = false;

    public ShaEncoder(int strength) {
        this("SHA-" + strength);
    }

    public ShaEncoder(String algorithm) {
        this(algorithm,false);
    }

    public ShaEncoder(String algorithm,boolean encodeHashAsBase64) {
        this.algorithm = algorithm;
        setEncodeHashAsBase64(encodeHashAsBase64);
        getMessageDigst();
    }

    private final MessageDigest getMessageDigst() throws IllegalArgumentException{
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
        }
    }

    public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
        this.encodeHashAsBase64 = encodeHashAsBase64;
    }

    public boolean isEncodeHashAsBase64() {
        return encodeHashAsBase64;
    }

    public String encodePassword(String rawPass, Object salt) {
        String saltedPass = mergePasswordAndSalt(rawPass,salt,false);
        MessageDigest messageDigest = getMessageDigst();
        byte[] digest = messageDigest.digest(Utf8.encode(saltedPass));
        for (int i = 0; i < iterations; i++) {
            digest = messageDigest.digest(digest);
        }
        if (isEncodeHashAsBase64()) {
            return Utf8.decode(Base64.encode(digest));
        } else {
            return new String(Hex.encode(digest));
        }
    }

    public String mergePasswordAndSalt(String password,Object salt,boolean strict) {
        if (password == null) {
            password = "";
        }
        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("can not use { or } in salt.toString()");
            }
        }
        if ((salt == null) || "".equals(salt)) {
            return password;
        }else {
            return password + "(^_^)" + salt.toString();
        }

    }
}
