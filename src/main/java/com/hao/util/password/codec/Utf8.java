package com.hao.util.password.codec;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * Created by user on 2016/4/19.
 */
public class Utf8 {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * Get the bytes of the String in UTF-8 encoded form.
     */
    public static byte[] encode(CharSequence string) {
        try {
            ByteBuffer byteBuffer = CHARSET.newEncoder().encode(CharBuffer.wrap(string));
            byte[] byteCopy = new byte[byteBuffer.limit()];
            System.arraycopy(byteBuffer.array(),0,byteCopy,0,byteBuffer.limit());
            return byteCopy;
        }  catch (CharacterCodingException e) {
             throw new IllegalArgumentException("Encoding failed",e);
        }
    }

    /**
     * decode the bytes in UTF-8 from into a string
     * @param bytes
     * @return
     */
    public static String decode(byte[] bytes) {
        try {
            return CHARSET.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) {
            throw new IllegalArgumentException("Decoding failed",e);
        }
    }

}
