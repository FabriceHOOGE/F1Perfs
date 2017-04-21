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
        result = result && (Integer.parseInt(toCheck)<60);
        return result;
    }
}
