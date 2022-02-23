package com.benjaminwan.chinesettstflite.utils;

public class FloatArrayToByteArrayUtils {
    public static byte[] convertTo16Bit(float[] data) {
        byte[] byte16bit = new byte[data.length << 1];
        for (int i = 0; i < data.length; i++) {
            short temp = (short) (32768 * data[i]);
            byte16bit[i * 2] = (byte) (temp);
            byte16bit[i * 2 + 1] = (byte) (temp >> 8);
        }
        return byte16bit;
    }
}
