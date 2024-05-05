package br.com.jaya.exchangerates.converter.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class EncryptUtil {

    public static String encryptWithRandomSault(String text) {
        return encrypt(text, UUID.randomUUID().toString());
    }

    public static String encrypt(String firstText, String secondText) {
        String combined = firstText + secondText;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(combined.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.substring(0, 32);
        } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException("Error encrypt text");
        }
    }
}
