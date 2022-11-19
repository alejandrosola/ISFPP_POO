package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validation {

	public static Double isDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static LocalDate isDate(String dateStr) {

		try {
			return LocalDate.parse(dateStr);
		} catch (DateTimeParseException e) {
			return null;
		}
	}
}
