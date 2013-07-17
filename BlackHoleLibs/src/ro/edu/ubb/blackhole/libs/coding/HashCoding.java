package ro.edu.ubb.blackhole.libs.coding;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This object can code a string to byte array.
 * 
 * @author Wikipedia and Turdean Arnold Robert
 * @version 1.0
 */
public class HashCoding {

	/**
	 * Used algorithm.
	 */
	private static final String ALGORITHM = "SHA-1";

	/**
	 * The name of the character set.
	 */
	private static final String CHARSET = "UTF-8";

	/**
	 * Code a String to byte array.
	 * 
	 * @param str
	 *            The string which you want to code.
	 * @return The coded byte array.
	 * @throws NoSuchAlgorithmException
	 *             If some error were occured.
	 * @throws UnsupportedEncodingException
	 *             If some error were occured.
	 */
	public static byte[] hashString(String str) throws Exception {
		MessageDigest digest = MessageDigest.getInstance(HashCoding.ALGORITHM);
		digest.reset();

		return digest.digest(str.getBytes(HashCoding.CHARSET));
	}
}
