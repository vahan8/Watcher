package gevorgyan.vahan.newsfeed.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {

    private static final String DATE_TIME_FORMAT_READABLE = "dd MMM yy HH:mm";

    private DateUtils() {
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_READABLE, Locale.getDefault());
        return formatter.format(date);
    }

}
