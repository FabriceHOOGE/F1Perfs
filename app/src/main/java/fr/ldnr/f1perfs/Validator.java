package fr.ldnr.f1perfs;

/**
 * Created by fabri on 20/04/2017.
 */

public class Validator {
    public static boolean isValidNewTimeEvent(String toCheck)
    {
        boolean result = true;
        result = result && !toCheck.isEmpty();
        result = result && Validator.isValidMaxLength(toCheck, 50);

        return result;
    }

    public static boolean isValidNewTimeRace(String toCheck)
    {
        boolean result = true;
        result = result && !toCheck.isEmpty();
        result = result && Validator.isValidMaxLength(toCheck, 50);

        return result;
    }

    public static boolean isValidNewTimePilot(String toCheck)
    {
        boolean result = true;
        result = result && !toCheck.isEmpty();
        result = result && Validator.isValidMaxLength(toCheck, 50);

        return result;
    }

    public static boolean isValidNewTimeLapTime(String toCheckMinute, String toCheckSecond, String toCheckMillisecond)
    {
        boolean result = true;
        result = result && !toCheckMinute.isEmpty();
        result = result && !toCheckSecond.isEmpty();
        result = result && !toCheckMillisecond.isEmpty();
        result = result && Validator.isValidMinutesSecond(toCheckMinute);
        result = result && Validator.isValidMinutesSecond(toCheckSecond);
        return result;
    }

    //Méthodes pour vérifier certaines caractérisques des chaines de caractère
    private static boolean isValidMaxLength(String toCheck,int length)
    {
        return !(toCheck.length() > length);
    }

    private static boolean isValidMinutesSecond(String toCheck)
    {
        boolean result = true;
        if(toCheck.isEmpty())
            return false;
        
        result = result && (Integer.parseInt(toCheck)<60);
        return result;
    }

    public static String millisecondToTime(int millisecond)
    {
        String sMinute,sSecond,sMillisecond;
        int iMinute, iSecond, iMillisecond;

        iMillisecond = millisecond%1000;
        millisecond = millisecond - iMillisecond;
        iSecond = (millisecond/1000)%60;
        millisecond = millisecond - iSecond*1000;
        iMinute = millisecond/60000;

        sMinute = new Integer(iMinute).toString();
        sSecond = new Integer(iSecond).toString();
        sMillisecond = new Integer(iMillisecond).toString();
        return Validator.formatTime(sMinute,sSecond,sMillisecond);
    }

    public static String formatTime(String minute,String second, String millisecond)
    {
        String result = "";
        if(minute.length()<2)
            result = result +"0";
        result = result + minute+":";

        if(second.length()<2)
            result=result + "0";
        result = result + second +".";

        if(millisecond.length()<3)
        {
            if(millisecond.length()<2)
                result = result+"0";
            result = result+"0";
        }
        result = result+millisecond;
        return result;
    }

    public static int timeToMillisecond(String minute, String second, String millisecond)
    {
        if(!Validator.isValidMinutesSecond(minute))
            minute = "0";
        if(!Validator.isValidMinutesSecond(second))
            second = "0";
        return((Integer.parseInt(minute)*60000) + (Integer.parseInt(second)*1000) + Integer.parseInt(millisecond));
    }
}
