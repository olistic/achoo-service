package uy.achoo.util;

import com.sun.jersey.core.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 *
 * Auxiliary class to hash passwords
 */
public class DigestUtils {
    private final static int ITERATION_NUMBER = 1000;

    /**
     * Hash a password with a salt
     *
     * @param password
     * @param salt
     * @return The hashed password
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String digestPassword(String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bDigest = getHash(ITERATION_NUMBER, password, salt);
        return byteToBase64(bDigest);
    }

    /**
     * Generates a random salt
     *
     * @return The generated salt
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // Salt generation 64 bits long
        byte[] bSalt = new byte[8];
        random.nextBytes(bSalt);
        return bSalt;
    }


    /**
     * Get hash from a password, a number of iterations and a salt,
     *
     * @param iterationNb int The number of iterations of the algorithm
     * @param password    String The password to encrypt
     * @param salt        byte[] The salt
     * @return byte[] The digested password
     * @throws NoSuchAlgorithmException If the algorithm doesn't exist
     */
    private static byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNb; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }


    /**
     * From a byte[] returns a base 64 representation
     *
     * @param data byte[]
     * @return String
     * @throws IOException
     */
    public static String byteToBase64(byte[] data) {
        return new String(Base64.encode(data));
    }

    /**
     * From a base 64returns a byte[]
     *
     * @param data String
     * @return byte[]
     * @throws IOException
     */
    public static byte[] base64toBytes(String data) {
        return Base64.decode(data.getBytes());
    }
}
