package org.hz.realtime.common.sequence.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * ClassName: DateTimeUtils
 * 
 * @description
 * @author framework
 * @Date 2013-4-1
 * 
 */
public final class DateTimeUtils {

    public static void main(String[] argvs) {
        // System.out.println(DateTimeUtils.formatStrToDate("20101212101010", FULLSECONDS));
    }

    /** the day time of the format date: yyyyMMddHHmmss **/
    public final static int FULLSECONDS = 0;
    /** the day of the format date: yyyyMMdd **/
    public final static int FULLDATE = 1;
    /** the day of the format date: yyyyMM **/
    public final static int FULLMONTH = 2;
    /** the day of the format date: yyMMdd **/
    public final static int SIMPLEDATE = 3;
    
    private final static SimpleDateFormat[] DATE_FORMATTER = new SimpleDateFormat[] { 
    		new SimpleDateFormat("yyyyMMddHHmmss"),
    		new SimpleDateFormat("yyyyMMdd"), 
    		new SimpleDateFormat("yyyyMM"), 
    		new SimpleDateFormat("yyMMdd") };


    /**
     * 获取指定时间差的时间对象
     * 
     * @param date
     * @param days
     * @return
     */
    public final static Date getDiffDate(Date date, int days) {
    	Date diffDay = new Date();
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.DAY_OF_MONTH, days); 
    	diffDay = calendar.getTime();   
    	return diffDay;
    }
    
    
    /**
     * Do format date with specified type.
     * 
     * @param date
     *            the date
     * @param format
     *            the format type
     * @return the formatted date
     */
    public final static String formatDateToString(java.util.Date date, int format) {
        String rel = null;
        if (date != null) {
            if (format >= DATE_FORMATTER.length || format < 0) {
                format = 0;
            }
            rel = DATE_FORMATTER[format].format(date);
        }
        return rel;
    }

    public final static String formatDateToString(java.util.Date date, String format) {
        String rel = null;
        if (date != null) {
            rel = new SimpleDateFormat(format).format(date);
        }
        return rel;
    }
    
    public final static java.util.Date formatStrToDate(String strDate, int format) {
        java.util.Date rel = null;
        if (strDate != null && !strDate.equals("")) {
            if (format >= DATE_FORMATTER.length || format < 0) {
                format = 0;
            }
            try {
                rel = DATE_FORMATTER[format].parse(strDate);
            } catch (ParseException e) {
                rel = null;
            }
        }
        return rel;
    }
}
