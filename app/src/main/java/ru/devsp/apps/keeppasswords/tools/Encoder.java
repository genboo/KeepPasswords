package ru.devsp.apps.keeppasswords.tools;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Кодирование\декодирование строк
 * Created by gen on 22.10.2017.
 */

public class Encoder {

    private String mPassword;

    private byte[] mSalt = {
            (byte) 0xA1, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x34, (byte) 0xE2, (byte) 0xE3
    };
    private int mPswdIterations = 32;
    private int mKeySize = 128;
    private byte[] mIvBytes = {0x00, (byte) 0xA1, 0x00, 0x00, 0x00, 0x00, (byte) 0x31, 0x00,
            0x00, 0x00, (byte) 0x07, 0x00, (byte) 0x10, (byte) 0xE0, (byte) 0xBE, 0x00};

    private static Encoder inst;

    public static Encoder getInstance(String password) {
        if (inst == null || !inst.mPassword.equals(password)) {
            //Создаем новый экземпляр или пересоздаем, если пароль поменялся
            inst = new Encoder(password);
        }
        return inst;
    }

    private Encoder(String password) {
        mPassword = password;
    }

    public String enc(String plainText) {
        try {
            // Derive the key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(
                    mPassword.toCharArray(),
                    mSalt,
                    mPswdIterations,
                    mKeySize
            );

            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(mIvBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);


            byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Base64.encodeToString(encryptedTextBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException |
                InvalidKeySpecException |
                NoSuchPaddingException |
                InvalidKeyException |
                UnsupportedEncodingException |
                BadPaddingException |
                IllegalBlockSizeException |
                InvalidAlgorithmParameterException ex) {
            Logger.e(ex);
        }
        return null;
    }

    public String dec(String encryptedText) {
        if (encryptedText == null) {
            return null;
        }
        try {
            byte[] encryptedTextBytes = Base64.decode(encryptedText, Base64.DEFAULT);

            // Derive the key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(
                    mPassword.toCharArray(),
                    mSalt,
                    mPswdIterations,
                    mKeySize
            );

            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(mIvBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);

            byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
            return new String(decryptedTextBytes, "UTF-8");
        } catch (NoSuchAlgorithmException |
                InvalidKeySpecException |
                NoSuchPaddingException |
                InvalidKeyException |
                UnsupportedEncodingException |
                BadPaddingException |
                IllegalBlockSizeException |
                InvalidAlgorithmParameterException ex) {
            Logger.e(ex);
        }
        return null;

    }
}
