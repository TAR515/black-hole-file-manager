package ro.edu.ubb.blackhole.libs.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides a runtime Date parsing framework for client applications.
 * 
 * @author Administrator
 * @version 1.0
 */
public class DateParser {

	/**
	 * Default formatter.
	 */
	private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Converts a calendar to String. (Format: yyyy-MM-dd)
	 * 
	 * @param calendar
	 *            Calendar.
	 * @return A 'yyyy-MM-dd' formated string.
	 */
	public static String convertCalendarToString(Calendar calendar) {
		Date date = new Date(calendar.getTimeInMillis());

		return DateParser.dateFormatter.format(date);
	}

	/**
	 * Converts String to calendar. (Format: yyyy-MM-dd)
	 * 
	 * @param str
	 *            A 'yyyy-MM-dd' formated string.
	 * @return Calendar
	 * @throws ParseException
	 *             If the given string isn't correct formated.
	 */
	public static Calendar convertStringToCalendar(String str) throws ParseException {
		Date date = DateParser.dateFormatter.parse(str);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

}
