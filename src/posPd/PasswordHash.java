package posPd;

import java.security.*;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class PasswordHash {
    private static final int ITERATIONS = 120000;
    private PasswordHash() {}
    static String create(String password) {
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password is required.");
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return "pbkdf2$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt)
            + "$" + Base64.getEncoder().encodeToString(derive(password, salt, ITERATIONS));
    }
    static boolean verify(String password, String encoded) {
        if (password == null || encoded == null) return false;
        try {
            String[] parts = encoded.split("\\$");
            int iterations = Integer.parseInt(parts[1]);
            if (parts.length != 4 || iterations < 10000 || iterations > 1000000) return false;
            return MessageDigest.isEqual(Base64.getDecoder().decode(parts[3]),
                derive(password, Base64.getDecoder().decode(parts[2]), iterations));
        } catch (RuntimeException ex) { return false; }
    }
    private static byte[] derive(String password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch (GeneralSecurityException ex) { throw new IllegalStateException("Password hashing unavailable.", ex); }
        finally { spec.clearPassword(); }
    }
}
