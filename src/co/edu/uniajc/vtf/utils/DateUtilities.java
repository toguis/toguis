package co.edu.uniajc.vtf.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtilities {
    public static String getDateOffset(Calendar pDate)
    {
        Calendar loDate = pDate;
        TimeZone loTimezone = loDate.getTimeZone();
        int loOffset = loTimezone.getRawOffset();
        if(loTimezone.inDaylightTime(new Date())){
            loOffset = loOffset + loTimezone.getDSTSavings();
        }
        int liOffsetHrs = loOffset / 1000 / 60 / 60;
        int liOffsetMins = loOffset / 1000 / 60 % 60;
        
        String lsOffSetHrs = "";
        
        if(liOffsetHrs < 0 && liOffsetHrs > -10){
            liOffsetHrs *= -1;
            lsOffSetHrs = "-0" +  liOffsetHrs;
         }
         else if(liOffsetHrs > 0 && liOffsetHrs < 10){
            lsOffSetHrs = "+0" +  liOffsetHrs;            
         }
         else if(liOffsetHrs == 0){
             lsOffSetHrs = ""; 
         }
         else{
             lsOffSetHrs = liOffsetHrs + "";
         }
        
        String lsOffSetMins = "";      
        if(liOffsetMins < 10 ){
           lsOffSetMins = "0" +  liOffsetMins;
        }
        else{
            lsOffSetMins = liOffsetMins + "";
        }         
        return  lsOffSetHrs +  lsOffSetMins;
    }
    
    public static long getDateMilliseconds(Calendar pDate){
    	return pDate.getTimeInMillis();
    }
    
    public static String getFormattedDate(Calendar pDate){
    	return  "\\/Date(" +  getDateMilliseconds(pDate) + getDateOffset(pDate) + ")\\/";
    }
}
