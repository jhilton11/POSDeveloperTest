package com.appify.javaposdevelopertest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilClass {

    public static String getDate(String date) {
        String formattedDate = "";
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
// This could be MM/dd/yyyy, you original value is ambiguous
            Date dateValue = input.parse(date);
            SimpleDateFormat output = new SimpleDateFormat("dd, MMM yyyy, HH:mm:ss a");
            formattedDate = output.format(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
