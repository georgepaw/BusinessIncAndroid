package company.businessinc.bathtouch;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Louis on 04/02/2015.
 */
public class DateFormatter {

    private Calendar today;

    public DateFormatter() {
        today = Calendar.getInstance();
    }

    public String format(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String formattedDate = "null";

        int day = cal.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR);
        int year = cal.get(Calendar.YEAR) - today.get(Calendar.YEAR);

        if(year == 0) {
            if(day == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
                formattedDate = sdf.format(date);
            } else if (day == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
                formattedDate = "Tomorrow, " + sdf.format(date);
            } else if (day == -1) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
                formattedDate = "Yesterday, " + sdf.format(date);
            } else if (day > 1 && day < 7) {
                SimpleDateFormat sdf = new SimpleDateFormat("ccc, HH:mm a");
                formattedDate = sdf.format(date);
            } else if (day < -1) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
                formattedDate = sdf.format(date);
            } else if (day >= 7) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, HH:mm a");
                formattedDate = sdf.format(date);
            }
        } else if (year == -1) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            formattedDate = sdf.format(date);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy, HH:mm a");
            formattedDate = sdf.format(date);
        }
        return formattedDate;
    }
}
