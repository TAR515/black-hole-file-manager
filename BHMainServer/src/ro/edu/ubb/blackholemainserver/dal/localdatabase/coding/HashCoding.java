package ro.edu.ubb.blackholemainserver.dal.localdatabase.coding;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackholemainserver.dal.localdatabase.jdbc.JdbcDaoFactory;

// ASK hogy irjam, h a Wikipediarol van ?
/**
 * 
 * @author Wikipedia
 * 
 */
public class HashCoding {
	private static final String TAG = HashCoding.class.getSimpleName();

	private static final Logger logger = Logger.getLogger(JdbcDaoFactory.class);

	public static byte[] hashString(String str) throws NoSuchAlgorithmException {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			return digest.digest(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.debug(TAG + " hashString(String str) -> NO_SUCH_ALGORITHM_EXCEPTION");
			throw new NoSuchAlgorithmException(
					TAG + " hashString(String str) -> NO_SUCH_ALGORITHM_EXCEPTION", e);
		}
	}
}
