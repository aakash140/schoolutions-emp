package com.school.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.log4j.Logger;

public class PasswordUtil {

	private static Logger logger = Logger.getLogger(PasswordUtil.class);

	public static String encryptPassword(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		logger.info("Initiating Password Encryption.");

		int iterations = 1000;
		byte[] salt = getSalt();

		PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, 16 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(keySpec).getEncoded();

		// byte arrays of salt and hash contain values in decimal format.
		// Convert them to hexadecimal before returning encrypted password
		// string.
		String encryptedPassword = iterations + ":" + toHexadecimal(salt) + ":" + toHexadecimal(hash);

		maskPassword(password);

		logger.info("Password encrypted successfully.");
		return encryptedPassword;
	}

	public static boolean isvalidPassword(String storedPassword, char[] enteredPassword)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		logger.info("Comparing and Validating passwords");
		String[] elements = storedPassword.split(":");
		int iterations = Integer.parseInt(elements[0]);
		byte[] salt = fromHexadecimal(elements[1]);
		byte[] hash = fromHexadecimal(elements[2]);

		PBEKeySpec keySpec = new PBEKeySpec(enteredPassword, salt, iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] tempHash = skf.generateSecret(keySpec).getEncoded();

		// Compare length equality bitwise.diff will become 1 if any of the bit
		// is unequal.
		int diff = hash.length ^ tempHash.length;

		// Compare each bit of password.diff will become 1 if any of the bit is
		// unequal.
		for (int i = 0; i < hash.length && i < tempHash.length; i++)
			diff |= hash[i] ^ tempHash[i];

		maskPassword(enteredPassword);
		return diff == 0;
	}

	private static byte[] getSalt() throws NoSuchAlgorithmException {
		logger.info("Generating a salt");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

	private static String toHexadecimal(byte[] decimal) {
		logger.info("Converting byte[] decimal to hexadecimal string");
		// Convert byte array having decimal values to BigInteger.
		BigInteger bi = new BigInteger(1, decimal);
		// Convert to hexadecimal
		String hex = bi.toString(16);
		int paddingLength = (decimal.length * 2) - hex.length();

		if (paddingLength > 0) {
			// If there is difference in lengths, then pad remaining bits with
			// 0s.
			return (String.format("%0" + paddingLength + "d", 0) + hex);
		}

		return hex;
	}

	private static byte[] fromHexadecimal(String hex) {
		logger.info("Converting Hexadecimal String to byte[] decimal");
		byte[] bytes = new byte[hex.length() / 2];

		// Convert String containing hexadecimal bits to decimal bits.
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);

		return bytes;
	}

	private static void maskPassword(char[] password) {
		logger.info("Masking the entered password");
		for (int i = 0; i < password.length; i++)
			password[i] = '#';
	}
}