package com.app.superdistributor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String formatDate(String inputDate) {
        try {
            // Define input and output date formats
            DateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            // Parse input date string to Date object
            Date date = inputFormat.parse(inputDate);

            // Format the date to the desired output format
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}