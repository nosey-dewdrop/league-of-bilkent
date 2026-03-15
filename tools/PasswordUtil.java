package tools;

import model.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/*
 * ┌──────────────────────────────────────────────────────────────┐
 * │                  <<class>> PasswordUtil                      │
 * │              SHA-256 password hashing with salt              │
 * ├──────────────────────────────────────────────────────────────┤
 * │ + generateSalt(): String -> random 16-byte salt (Base64)    │
 * │ + hashPassword(password, salt): String -> SHA-256 hash      │
 * │ + verifyPassword(password, hash, salt): boolean             │
 * ├──────────────────────────────────────────────────────────────┤
 * │ USED BY: User (convenience constructor), LoginScreen,       │
 * │          RegisterScreen, ForgotPasswordDialog, SampleData   │
 * └──────────────────────────────────────────────────────────────┘
 */
public class PasswordUtil {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random 16-byte salt.
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Creates a SHA-256 hash from password + salt.
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashed = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported!", e);
        }
    }

    /**
     * Verifies the entered password against the stored hash.
     */
    public static boolean verifyPassword(String password, String storedHash, String storedSalt) {
        String newHash = hashPassword(password, storedSalt);
        return newHash.equals(storedHash);
    }
}
