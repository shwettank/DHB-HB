package com.dhb.utils;

import android.annotation.SuppressLint;

import com.dhb.request_model.AgeModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class DateUtils {
    private static DateUtils instance = null;

    protected DateUtils() {
        // Exists only to defeat instantiation.
    }

    public static DateUtils getInstance() {
        if (instance == null){
            instance = new DateUtils();
        }
        return instance;
    }

    public String UTC_DATE_FORMAT = "yyyy-mm-dd hh:MM:ss";
    public long MILLISECONDS_IN_SECONDS = 1000;
    public String MONTH_DATE_FORMAT = "MMM dd";
    public String CHALLENGES_END_DATE_FORMAT = "MMM dd, yyyy";

    public Date dateFromUTCLong(long time) {
        return null;

    }

    public Date dateFromUTCString(String dateStr) {

        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat(UTC_DATE_FORMAT);

        try {

            date = format.parse(dateStr);

        } catch (ParseException e){

            e.printStackTrace();
            Logger.debug("Parsing Faied : " + e);
        }

        return date;
    }

    public Date dateFromString(String dateStr, SimpleDateFormat dateFormat) {

        Date date = null;

        try {

            date = dateFormat.parse(dateStr);

        } catch (ParseException e){

            e.printStackTrace();
            Logger.debug("Parsing Faied : " + e);
        }

        return date;
    }

    public Date dateFromString(String dateStr, String dateFormatStr, String timezone) {

        Date date = null;

        SimpleDateFormat format = getDateFormatFromString(dateFormatStr);
        format.setTimeZone(TimeZone.getTimeZone(timezone));

        date = dateFromString(dateStr, format);

        return date;
    }

    public String fromDate(Date date, String timezone, SimpleDateFormat dateFormat) {

        String dateStr = "";

        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        dateStr = dateFormat.format(date);

        return dateStr;
    }

    public String dateWithClientTimezone(Date date, SimpleDateFormat dateFormat) {

        String dateStr = "";

        dateFormat.setTimeZone(TimeZone.getDefault());
        dateStr = dateFormat.format(date);

        return dateStr;
    }

    public SimpleDateFormat getDateFormatFromString(String dateFormatStr) {

        return new SimpleDateFormat(dateFormatStr);
    }

    public static String getDateFromFromLong(long milliseconds, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());
    }

    public long getDateFromDateString(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy hh:MM:ss");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Logger.debug("" + date.getTime());
        return date.getTime();
    }

    @SuppressLint("SimpleDateFormat")
    public String getDateDiff(long milliSeconds) {
        String diff = null;
        long diffDays = 0, diffMinutes = 0, diffHours = 0;

        long commentTime = (milliSeconds) * 1000L;
        DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = format.format(new Date(commentTime));

        DateFormat todayDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");

        todayDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String strTodayDate = todayDateFormat.format(new Date());
        Logger.debug(strTodayDate);
        Date d1 = null;
        Date d2 = null;

        try {
            DateFormat convertDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
            d1 = convertDateFormat.parse(strDate);

            d2 = convertDateFormat.parse(strTodayDate);

        } catch (ParseException e1){
            e1.printStackTrace();
        }

        commentTime = d1.getTime();
        long currentTime = d2.getTime();

        try {
            long diffmill = currentTime - commentTime;
            diffDays = diffmill / (24 * 60 * 60 * 1000);
            diffMinutes = diffmill / (60 * 1000) % 60;
            diffHours = diffmill / (60 * 60 * 1000) % 24;
            Logger.debug("diffDay : " + diffDays + "diffHours : " + diffHours + "   diffMinutes: " + diffHours);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (diffDays > 30){
            Date date = new Date(commentTime);

            DateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
            diff = format1.format(date);
            return diff;
        } else if (diffDays > 1 && diffDays < 30){
            diff = String.valueOf(diffDays);
            return diff + " days ago";
        } else if (diffDays == 1){
            diff = String.valueOf(diffDays);
            return diff + " day ago";
        } else if (diffHours > 1){
            diff = String.valueOf(diffHours);
            return diff + " hours ago";
        } else if (diffHours == 1){
            diff = String.valueOf(diffHours);
            return diff + " hour ago";
        } else if (diffMinutes > 1){
            diff = String.valueOf(diffMinutes);
            return diff + " minutes ago";
        } else {
            diff = "Just Now";
            return diff;
        }

    }

    @SuppressLint("SimpleDateFormat")
    public String getDateDiffForJudge(long milliSeconds) {
        String diff = null;

        long diffmill = 0;
        long commentTime = (milliSeconds) * 1000L;
        DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = format.format(new Date(commentTime));

        DateFormat todayDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");

        todayDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String strTodayDate = todayDateFormat.format(new Date());
        Logger.debug(strTodayDate);
        Date d1 = null;
        Date d2 = null;

        try {
            DateFormat convertDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
            d1 = convertDateFormat.parse(strDate);

            d2 = convertDateFormat.parse(strTodayDate);

        } catch (ParseException e1){
            e1.printStackTrace();
        }

        commentTime = d1.getTime();
        long currentTime = d2.getTime();

        try {
            diffmill = currentTime - commentTime;
        } catch (Exception e){
            e.printStackTrace();
        }
        if (diffmill < 0){
            return "00:00:00";
        } else {
            diff = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(diffmill), TimeUnit.MILLISECONDS.toMinutes(diffmill) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffmill)), TimeUnit.MILLISECONDS.toSeconds(diffmill) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffmill)));
            return diff;
        }

    }

    public String getDateDiffCounter(long commentTime) {
        String diff = null;
        long diffDays = 0;
        long currentTime = new Date().getTime();

        long diffmill = commentTime - currentTime;
        diffDays = diffmill / (24 * 60 * 60 * 1000);

        if (diffDays > 0){
            diff = String.valueOf(diffDays);
            if (diffDays == 1){
                diff = diff + " Day";
            } else {
                diff = diff + " Days";
            }
            return diff;
        } else if (diffmill > 0){
            return null;
        } else if (diffmill < 0){
            return "00:00:00";
        } else {
            return null;
        }

    }

    public String getDateDiffCounter(String commentTime) {
        String diff = null;
        int diffDays = 0;
        if (commentTime != null && !commentTime.equals("")){
            String[] separated = commentTime.split(":");

            diffDays = Integer.parseInt(separated[0]);
            int diffHours = Integer.parseInt(separated[1]);
            int diffMin = Integer.parseInt(separated[2]);
            int diffSec = Integer.parseInt(separated[3]);

            long diffmill = ((diffHours * 60 + diffMin) * 60 + diffSec) * 1000;

            if (diffDays > 0){
                diff = String.valueOf(diffDays);
                if (diffDays == 1){
                    diff = diff + " Day";
                } else {
                    diff = diff + " Days";
                }
                return diff;
            } else if (diffmill > 0){
                return null;
            } else {
                return "00:00:00";
            }
        } else {
            return "00:00:00";
        }

    }

    public String getDateDownCounter(long commentTime) {
        String diff = null;
        long diffDays = 0, diffHours = 0;
        long currentTime = new Date().getTime();

        long diffmill = currentTime - commentTime;
        diffDays = diffmill / (24 * 60 * 60 * 1000);
        diffHours = diffmill / (60 * 60 * 1000) % 24;
        if (diffDays > 0){
            diff = String.valueOf(diffDays);
            return diff + " Days";
        } else if (diffHours > 0 && diffHours <= 24){
            return null;
        } else {
            return "0 Days";
        }

    }

    public String getTimeInMin(long milliSeconds) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)), TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
    }

    public long getTimeInMillies(String timeRemaining) {
        if (timeRemaining != null && !timeRemaining.equals("")){
            String[] separated = timeRemaining.split(":");

            int hours = Integer.parseInt(separated[1]);
            int min = Integer.parseInt(separated[2]);
            int sec = Integer.parseInt(separated[3]);

            long millies = ((hours * 60 + min) * 60 + sec) * 1000;
            return millies;
        } else {
            return 0;
        }
    }

    public long getDateInmilliesLocal(String date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        Date d1 = null;
        long startDateMillisec = 0;
        try {
            d1 = format.parse(date);

            startDateMillisec = d1.getTime() / 1000;
        } catch (Exception e){
            e.printStackTrace();
        }

        return startDateMillisec;
    }

    public long getDateInmillies(String date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        TimeZone obj = TimeZone.getTimeZone("UTC");
        format.setTimeZone(obj);
        Date d1 = null;
        long startDateMillisec = 0;
        try {
            d1 = format.parse(date);

            startDateMillisec = d1.getTime() / 1000;
        } catch (Exception e){
            e.printStackTrace();
        }

        return startDateMillisec;
    }

    public String getDateWithPrefix(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("d");
        format.setTimeZone(TimeZone.getDefault());
        String strDate = format.format(new Date(timestamp));

        if (strDate.endsWith("1") && !strDate.endsWith("11"))
            format = new SimpleDateFormat("d'st' MMMM yyyy");
        else if (strDate.endsWith("2") && !strDate.endsWith("12"))
            format = new SimpleDateFormat("d'nd' MMMM yyyy");
        else if (strDate.endsWith("3") && !strDate.endsWith("13"))
            format = new SimpleDateFormat("d'rd' MMMM yyyy");
        else
            format = new SimpleDateFormat("d'th' MMMM yyyy");

        return format.format(new Date(timestamp));

    }

    public static String getDateWithShortPrefix(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("d");
        format.setTimeZone(TimeZone.getDefault());
        String strDate = format.format(new Date(timestamp));

        if (strDate.endsWith("1") && !strDate.endsWith("11"))
            format = new SimpleDateFormat("d'st' MMM yyyy");
        else if (strDate.endsWith("2") && !strDate.endsWith("12"))
            format = new SimpleDateFormat("d'nd' MMM yyyy");
        else if (strDate.endsWith("3") && !strDate.endsWith("13"))
            format = new SimpleDateFormat("d'rd' MMM yyyy");
        else
            format = new SimpleDateFormat("d'th' MMM yyyy");

        return format.format(new Date(timestamp));

    }

    public String getDateWithShortPrefixs(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("d");
        format.setTimeZone(TimeZone.getDefault());
        String strDate = format.format(new Date(timestamp));

        if (strDate.endsWith("1") && !strDate.endsWith("11"))
            format = new SimpleDateFormat("d'st' MMM yy");
        else if (strDate.endsWith("2") && !strDate.endsWith("12"))
            format = new SimpleDateFormat("d'nd' MMM yy");
        else if (strDate.endsWith("3") && !strDate.endsWith("13"))
            format = new SimpleDateFormat("d'rd' MMM yy");
        else
            format = new SimpleDateFormat("d'th' MMM yy");

        return format.format(new Date(timestamp));

    }

    public String getDateWithPrefixForCreateChallenge(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("d");
        format.setTimeZone(TimeZone.getDefault());
        String strDate = format.format(new Date(timestamp));

        if (strDate.endsWith("1") && !strDate.endsWith("11"))
            format = new SimpleDateFormat("MMM d'st' ");
        else if (strDate.endsWith("2") && !strDate.endsWith("12"))
            format = new SimpleDateFormat("MMM d'nd' ");
        else if (strDate.endsWith("3") && !strDate.endsWith("13"))
            format = new SimpleDateFormat("MMM d'rd' ");
        else
            // format = new SimpleDateFormat("MMM d'th' yyyy");
            format = new SimpleDateFormat("MMM d'th' ");
        return format.format(new Date(timestamp));

    }

    @SuppressLint("SimpleDateFormat")
    public boolean getDateDiff(String firstDate, String secondDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");

        Date d1 = null;
        Date d2 = null;
        long diffDays = 0;
        try {
            d1 = format.parse(firstDate);
            d2 = format.parse(secondDate);
            Logger.debug("first date : " + d1);
            Logger.debug("second date date : " + d2);

            long diff = d2.getTime() - d1.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
            // birthDateMillisec = d1.getTime() / 1000;
            Logger.debug(diffDays + " days, ");

        } catch (Exception e){
            e.printStackTrace();
        }

        if (diffDays > 6573L){
            return true;
        } else {
            return false;
        }

    }

    public String getChallengesEndDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat(CHALLENGES_END_DATE_FORMAT);
        format.setTimeZone(TimeZone.getDefault());
        String strDate = format.format(new Date(timestamp));

        return strDate;

    }

    public List<String> getBetweenYears(long oldTimestamp) {
        List<String> listYears = new ArrayList<String>();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        int currentYear = Integer.parseInt(simpleDateformat.format(new Date()));
        int registeredYear = Integer.parseInt(simpleDateformat.format(new Date(oldTimestamp)));
        while (registeredYear <= currentYear){
            listYears.add("" + currentYear);
            --currentYear;
        }
        return listYears;
    }

    public static String getDateInDefault(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timestamp);
        String date = android.text.format.DateFormat.format("dd MMM yyyy", cal).toString();
        cal.getTimeInMillis();
        return date;
    }

    public static String getDateInDefaultForCalender(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timestamp);
        String date = android.text.format.DateFormat.format("dd-MMM-yyyy", cal).toString();
        cal.getTimeInMillis();
        return date;
    }

    public static Calendar getDateInNewDefaultFormatCalender(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timestamp);
        String date = android.text.format.DateFormat.format("dd-MM-yyyy", cal).toString();
        cal.getTimeInMillis();
        return cal;
    }

    public static String getTimeInDefaultTimeZone(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timestamp);
        String time = android.text.format.DateFormat.format("h:mm A", cal).toString();
        cal.getTimeInMillis();
        return time.toUpperCase();
    }

    public static long getMilliSecondsInDefaultTimeZone(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timestamp);
        return cal.getTimeInMillis();
    }

    public static Date getDayInDefaultTimeZone(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timestamp);
        return cal.getTime();
    }

    public static String getDateInUTC(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timestamp);
        String date = android.text.format.DateFormat.format("MMM dd", cal).toString();
        cal.getTimeInMillis();
        return date;
    }

    public static Long getTimeStampInUTC() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        return cal.getTimeInMillis();
    }

    public static String getDateInForCalenderUTC(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timestamp);
        String date = android.text.format.DateFormat.format("MMM-dd-yyyy", cal).toString();
        cal.getTimeInMillis();
        return date;
    }

    public static String getCommentDate(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timestamp);
        String date = android.text.format.DateFormat.format("MM/dd/yyyy", cal).toString();
        return date;
    }

    public static String getReceivedDate(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timestamp);
        String date = android.text.format.DateFormat.format("MM/dd", cal).toString();
        return date;
    }

    public static String getTimeInUTC(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timestamp);
        String time = android.text.format.DateFormat.format("h:mm A", cal).toString();
        return time.toUpperCase();
    }

    public static String getTime(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timestamp);
        String time = android.text.format.DateFormat.format("h:mm A", cal).toString();
        return time.toUpperCase();
    }

    public static Date getDay(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timestamp);
        return cal.getTime();
    }

/*
    public long getCurrentTimestamp()
    {
        return  System.currentTimeMillis();
    }*/

    public static int getDateDiffString(long TimeDiff) {
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (TimeDiff) / oneDay;

        if (delta > 0){
            return Integer.parseInt(delta + "");
        }
        if (delta < 0){
            return -1;
        }

        return 0;
    }

    public static int getTimeDiffString(long TimeDiff) {
        long oneMin = 1000 * 60 * 60;
        long delta = (TimeDiff) / oneMin;

        if (delta > 0){
            return Integer.parseInt(delta + "");
        }
        return 0;
    }

    public static int getMinDiffString(long TimeDiff) {
        long oneMin = 1000 * 60;
        long delta = TimeDiff / oneMin;

        if (delta > 0){
            return Integer.parseInt(delta + "");
        }
        return 0;
    }

    public static String getAge(int year, int month, int day) {


//Now access the values as below
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        int m = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        int d = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
        Logger.debug("YEAR - " + age);
        Logger.debug("M - " + m);
        Logger.debug("D - " + d);
		/*  if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
		              age--;
		                           }*/

		/*if ((month < today.get(Calendar.MONTH))
		|| ((month == today.get(Calendar.MONTH)) && (day < today
		        .get(Calendar.DAY_OF_MONTH)))) {
		    --age;
		    --m;
		    --d;
		   }*/

        if (month > today
                .get(Calendar.MONTH)){
            --age;

        } else if (month == today
                .get(Calendar.MONTH)){
            if (day > today
                    .get(Calendar.DAY_OF_MONTH)){
                --age;
            }
        }


        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        Logger.debug("Year-" + year);
        Logger.debug("Month-" + m);
        Logger.debug("dat-" + d);


        return ageS;
    }

    public static Calendar getDOBFromAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeZone(TimeZone.getDefault());
        Calendar calculatedDate = Calendar.getInstance();
        calculatedDate.setTimeZone(TimeZone.getDefault());
        Calendar today = Calendar.getInstance();
        today.setTimeZone(TimeZone.getDefault());
        dob.set(year, month, day);

        today.add(Calendar.DAY_OF_MONTH, -day);
        today.add(Calendar.MONTH, -month);
        today.add(Calendar.YEAR, -year);

        int d = today.get(Calendar.DAY_OF_MONTH);
        int m = today.get(Calendar.MONTH);
        int y = today.get(Calendar.YEAR);

        calculatedDate.set(y, m, d);
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatted = format1.format(today.getTime());
        return today;
    }

    public static AgeModel calculateAge(long birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;

        if (birthDate != 0){
            //create calendar object for birth day
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTimeInMillis(birthDate);

            //create calendar object for current day
            Calendar now = Calendar.getInstance();
            Calendar current = Calendar.getInstance();
            //Get difference between years
            years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);

            //get months
            int currMonth = now.get(Calendar.MONTH) + 1;
            int birthMonth = birthDay.get(Calendar.MONTH) + 1;

            //Get difference between months
            months = currMonth - birthMonth;

            //if month difference is in negative then reduce years by one and calculate the number of months.
            if (months < 0){
                years--;
                months = 12 - birthMonth + currMonth;
//                if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
//                    months--;
            } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)){
                years--;
                months = 11;
            }

            //Calculate the days
            if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
                days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
            else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)){
                int today = now.get(Calendar.DAY_OF_MONTH);
                now.add(Calendar.MONTH, -1);
                days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;

                if (days < current.getActualMaximum(Calendar.DAY_OF_MONTH) && months > 0){
//                    months = months - 1;
                }
            } else {
                days = 0;
                if (months == 12){
                    years++;
                    months = 0;
                }
            }
        }

        //Create new Age object
        return new AgeModel(days, months, years);
    }

    public static Date convertToDate(String dateString) {
        //String dateString = "03/26/2012 ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static long getMillisecondsFromDateString(String dob) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = sdf.parse(dob);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getMillisecondsFromManualDateString(String dob) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = sdf.parse(dob);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getCurrentTimeInDefault() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());

//		Logger.debug("Local : " + calendar.getTimeInMillis());
//
//		SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		//Current Date Time in Local Timezone
//		System.out.println("Current Date and Time in local timezone: " + localDateFormat.format( new Date(calendar.getTimeInMillis())));
//
//		SimpleDateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//		//Current Date Time in GMT
//		System.out.println("Current Date and Time in GMT time zone: " + gmtDateFormat.format(new Date(calendar.getTimeInMillis())));


        return calendar.getTimeInMillis();
    }

    public static long getEndOfDay(Date date) {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeZone(tz);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static long getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeZone(tz);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getStartOfDayInDefaultTimeZone() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfDayInDefaultTimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static boolean isBetweenDates(Date dateOne, Date dateTwo, Date dateSelected) {
        if (dateSelected.equals(dateOne) || dateSelected.equals(dateTwo)){
            return true;
        }
        if (dateSelected.after(dateOne) && dateSelected.before(dateTwo)){
            return true;
        }
        return false;
    }

    public static long getFiveDaysAgoStartOfDayInDefaultTimeZone() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.add(Calendar.DAY_OF_YEAR, -5);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

}
