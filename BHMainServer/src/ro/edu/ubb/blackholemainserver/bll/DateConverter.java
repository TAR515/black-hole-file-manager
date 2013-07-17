package ro.edu.ubb.blackholemainserver.bll;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {

	private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Converts a calendar to String. (Format: yyyy-MM-dd)
	 * 
	 * @param calendar
	 * @return
	 */
	public static String convertCalendarToString(Calendar calendar) {
		Date date = new Date(calendar.getTimeInMillis());

		return DateConverter.dateFormatter.format(date);
	}

	/**
	 * Converts String to calendar. (Format: yyyy-MM-dd)
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 *             If the given string is not formatted correct.
	 */
	public static Calendar convertStringToCalendar(String str) throws ParseException {
		Date date = DateConverter.dateFormatter.parse(str);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

}
