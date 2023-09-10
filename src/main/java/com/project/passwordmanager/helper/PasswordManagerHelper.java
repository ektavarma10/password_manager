package com.project.passwordmanager.helper;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.ValidationException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Slf4j
public class PasswordManagerHelper {

    public static final String ALGO = "AES/CBC/PKCS5Padding";
    public static final String PBKDF_2_WITH_HMAC_SHA_256 = "PBKDF2WithHmacSHA256";

    public static SecretKey generateSecretKey(SecretKeyFactory secretKeyFactory, String password, byte[] salt) {
        SecretKey secretKey;
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(spec).getEncoded(), "AES");
        } catch (Exception e) {
            log.error("Error while :: generateSecretKey ", e);
            throw new RuntimeException(e);
        }
        return secretKey;
    }

    public static String decryptPassword(SecretKeyFactory secretKeyFactory, byte[] encryptedPassword, byte[] salt, byte[] iv, String hashedString) {
        final SecretKey secretKey = generateSecretKey(secretKeyFactory, hashedString, salt);
        final IvParameterSpec ivSpec = PasswordHelper.generateIV(iv);
        byte[] decryptedPassword;
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            decryptedPassword = cipher.doFinal(encryptedPassword);
        } catch (Exception e) {
            log.error("Exception during :: decryptPassword", e);
            throw new RuntimeException(e);
        }
        return new String(decryptedPassword);
    }

    public static byte[] encryptPassword(SecretKeyFactory secretKeyFactory, String password, byte[] salt, byte[] iv, String hashedString) {
        final SecretKey secretKey = generateSecretKey(secretKeyFactory, hashedString, salt);
        final IvParameterSpec ivSpec = PasswordHelper.generateIV(iv);
        byte[] encryptedPassword;
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            encryptedPassword = cipher.doFinal(password.getBytes());
        } catch (Exception e) {
            log.error("Exception during :: encryptPassword", e);
            throw new RuntimeException(e);
        }
        return encryptedPassword;
    }

    public static void validateLoginPassword(SecretKeyFactory secretKeyFactory, String password, String hashedPassword) {
        String currPasswordHash = new String(PasswordManagerHelper.generateSecretKey(secretKeyFactory, password, new byte[16]).getEncoded());
        if(!currPasswordHash.equals(hashedPassword)) {
            throw new ValidationException("Invalid login password");
        }
    }
}
