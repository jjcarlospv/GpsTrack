/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

/**
 * @author rodolfo.burlando
 */
public class Helper
{

    public static final int EQUALS = 0;
    public static final int STARTS_WITH = 1;

    public Helper()
    {
    }

    public static boolean findInArray(String[] data, String toFind, int findingMode)
    {
        if (data != null && data.length > 0)
        {
            for (int i = 0; i < data.length; i++)
            {
                if (data[i].startsWith(toFind))
                {
                    return true;
                }
            }
        }

        return false;
    }

    static public String getCurrentDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String sCurrentDate;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        sCurrentDate = dateFormat.format(calendar.getTime());

        return sCurrentDate;
    }

    static public String getCurrentDate(String format)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String sCurrentDate;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        sCurrentDate = dateFormat.format(calendar.getTime());
        return sCurrentDate;
    }

    public static int compareVersions(String version, String newestVersion)
    {
        version = version.trim();
        newestVersion = newestVersion.trim();
        Vector disasembledVersion = StringHelper.split(version, '.');
        Vector disasembledNewestVersion = StringHelper.split(newestVersion, '.');
        int versionInt = 0;
        int newestVersionInt = 0;
        int mayorSize = disasembledVersion.size();
        if (disasembledNewestVersion.size() > mayorSize)
        {
            mayorSize = disasembledNewestVersion.size();
        }
        for (int i = mayorSize - 1, j = 0; i >= 0; i--, j++)
        {
            if (i < disasembledVersion.size())
            {
                double num = Integer.parseInt(disasembledVersion.elementAt(i).toString()) * Math.pow(10, j);
                versionInt += num;
            }
        }
        for (int i = mayorSize - 1, j = 0; i >= 0; i--, j++)
        {
            if (i < disasembledNewestVersion.size())
            {
                newestVersionInt += (Integer.parseInt(disasembledNewestVersion.elementAt(i).toString()) * Math.pow(10, j));
            }
        }

        LogHelper.logDebug("older:" + versionInt);
        LogHelper.logDebug("newer:" + newestVersionInt);
        if (versionInt < newestVersionInt)
        {
            return -1;
        }
        else if (versionInt == newestVersionInt)
        {
            return 0;
        }
        else if (versionInt > newestVersionInt)
        {
            return 1;
        }
        else
        {
            throw new RuntimeException("Unespected Error");
        }
    }

    public static String removeAllChars(String version, char c)
    {
        char[] versionChar = version.toCharArray();
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < versionChar.length; i++)
        {
            if (versionChar[i] != c)
            {
                ret.append(versionChar[i]);
            }
        }
        return ret.toString().trim();
    }
/*
    public static Point toPoint(Double[] coordinates)
    {
        Point ret = new Point();
        ret.latitude = coordinates[0].doubleValue();
        ret.longitude = coordinates[1].doubleValue();
        return ret;
    }

    public static Point[] toPointArray(Double[] fence)
    {
        Point[] ret = new Point[fence.length / 2];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = new Point();
            ret[i].latitude = fence[2 * i].doubleValue();
            ret[i].longitude = fence[2 * i + 1].doubleValue();
        }
        return ret;
    }
*/
    public static void deleteArrayItem(Object[] array, Object item)
    {
        Vector ret = new Vector();
        for (int i = 0; i < array.length; i++)
        {
            if (!array[i].equals(item))
            {
                ret.addElement(array[i]);
            }
        }
        array = new Object[ret.size()];
        ret.copyInto(array);
    }

    /**
     * Method to get the Timezone
     *
     * @return
     */
    public static String getCurrentTime()
    {
        SimpleDateFormat formatUTC = new SimpleDateFormat("yyyyMMdd HH:mm");
        formatUTC.setTimeZone(TimeZone.getDefault());
        String date_long = (new Date()).getTime() + "";

        return formatUTC.format(new Date(Long.parseLong(date_long)));
    }

    public static String getTimezone() {

        /////////// Codigo de prueba /////////////////////////////////////////
        Calendar cal2 = Calendar.getInstance();
        TimeZone tz2 = cal2.getTimeZone();
        Date date2 = new Date();
        boolean daylightTime = tz2.useDaylightTime();
        String tzname2 = tz2.getDisplayName(daylightTime, TimeZone.LONG, Locale.getDefault());
        tz2.inDaylightTime(date2);
        Log.e("DayLight", tzname2);
        ////////////////////////////////////////////////////////////////////////

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        String tzname = tz.getDisplayName();
        Log.e("DayLightNOT", tzname);

        String timezoneStr = new SimpleDateFormat("Z").format(cal.getTime());
        StringBuffer hours_timezone = new StringBuffer(timezoneStr).insert(3, ":");
        String hours_timezonestring = hours_timezone.toString();

        if (tzname.equals("Eastern Standard Time"))
        {
            tzname = "(GMT-05:00) Eastern Time (US & Canada)";
        }
        else
        {
            if (tzname.equals("Central Standard Time"))
            {
                tzname = "(GMT-06:00) Central Time (US & Canada)";
            }
            else
            {
                if (tzname.equals("Mountain Standard Time"))
                {
                    tzname = "(GMT-07:00) Mountain Time (US & Canada)";
                }
                else
                {
                    if (tzname.equals("Pacific Standard Time"))
                    {
                        tzname = "(GMT-08:00) Pacific Time (US & Canada)";
                    }
                    else
                    {
                        if (tzname.equals("Alaska Standard Time"))
                        {
                            tzname = "(GMT-09:00) Alaska";
                        }
                        else
                        {
                            if (tzname.equals("Hawaii-Aleutian Standard Time"))
                            {
                                tzname = "(GMT-10:00) Hawaii";
                            }
                            else
                            {

                                SimpleDateFormat simpleFormat = new SimpleDateFormat("Z");
                                String dateResult = simpleFormat.format(new Date());
                                tzname = "GMT" + dateResult.substring(0, 3) + ":00";
                            }
                        }
                    }
                }
            }
        }


        return tzname;
    }

    public static void startNotification()
    {
        // The TUNE (bar 1 and 2 of Islamey by Balakirev).
        final short BFlat = 466; // 466.16
        final short AFlat = 415; // 415.30
        final short A = 440; // 440.00
        final short GFlat = 370; // 369.99
        final short DFlat = 554; // 554.37
        final short C = 523; // 523.25
        final short F = 349; // 349.32
        final short TEMPO = 125;
        final short d16 = 1 * TEMPO; // Duration of a 16th note, arbitrary, in
        // ms.
        final short d8 = d16 << 1; // Duration of an eigth note, arbitrary, in
        // ms.
        final short dpause = 10; // 10 ms pause
        final short pause = 0; // Zero frequency pause

        final short[] TUNE = new short[]{
                BFlat, d16, pause, dpause, BFlat, d16, pause, dpause, BFlat, d16, pause, dpause,
                BFlat, d16, pause, dpause, A, d16, pause, dpause, BFlat, d16, pause, dpause, GFlat, d16, pause, dpause, GFlat,
                d16, pause, dpause, A, d16, pause, dpause, BFlat, d16, pause, dpause, DFlat, d16, pause, dpause, C,
                d16,
                pause,
                dpause, // Bar 1
                AFlat, d16, pause, dpause, AFlat, d16, pause, dpause, AFlat, d16, pause, dpause, AFlat, d16, pause, dpause, F,
                d16, pause, dpause, GFlat, d16, pause, dpause, AFlat, d16, pause, dpause, BFlat, d16, pause, dpause, AFlat,
                d16, pause, dpause, F, d8 + d16 // Bar 2
        };
        final int VOLUME = 80; // % volume

        // LED.setConfiguration(500, 250, LED.BRIGHTNESS_50);
        // LED.setState(LED.STATE_BLINKING);
        // if (Alert.isVibrateSupported())
        // Alert.startVibrate(2000);
        // if (Alert.isAudioSupported())
        // Alert.startAudio(TUNE, VOLUME);
        // if (Alert.isBuzzerSupported())
        // Alert.startBuzzer(TUNE, VOLUME);
    }

    public static void stopNotification()
    {
        // if (Alert.isVibrateSupported())
        // Alert.stopVibrate();
        // if (Alert.isAudioSupported())
        // Alert.stopAudio();
        // if (Alert.isBuzzerSupported())
        // Alert.stopBuzzer();
        // LED.setState(LED.STATE_OFF);
    }


    public static Hashtable getRemoteJad(String jadUrl)
    {
        throw new RuntimeException("Not implemented");
    }

    public static TimeZone parseTimezone(String tz)
    {
        if (tz.equals("CD"))// Central Daylight Time
        {
            return TimeZone.getTimeZone("America/Chicago");
        }
        else if (tz.equals("CS"))// Central Standard Time
        {
            return TimeZone.getTimeZone("America/Chicago");
        }
        else if (tz.equals("ED"))// Eastern Daylight Time
        {
            return TimeZone.getTimeZone("America/New_York");
        }
        else if (tz.equals("ES"))// Eastern Standard Time
        {
            return TimeZone.getTimeZone("America/New_York");
        }
        else if (tz.equals("MD"))// Mountain Daylight Time
        {
            return TimeZone.getTimeZone("America/Denver");
        }
        else if (tz.equals("MS"))// Mountain Standard Time
        {
            return TimeZone.getTimeZone("America/Phoenix");
        }
        else if (tz.equals("PD"))// Pacific Daylight Time
        {
            return TimeZone.getTimeZone("America/Los_Angeles");
        }
        else if (tz.equals("PS"))// Pacific Standard Time
        {
            return TimeZone.getTimeZone("America/Los_Angeles");
        }
        else if (tz.equals("UT"))// Universal Time
        {
            return TimeZone.getTimeZone("GMT");
        }
        else
        {
            return TimeZone.getTimeZone("GMT");
        }
    }

    public static String toPegasusTimezoneAsString(TimeZone tz)
    {
        String[] pegasusTz = {"CST", "CDT", "EST", "EDT", "MST", "MDT", "PST", "PDT", "UTC", "UTC"};
        String[] blackberryTz = {
                "America/Chicago", "America/Chicago", "America/New_York", "America/New_York",
                "America/Denver", "America/Phoenix", "America/Los_Angeles", "America/Los_Angeles", "GMT"
        };
        for (int i = 0; i < blackberryTz.length; i++)
        {
            TimeZone timeZone = TimeZone.getTimeZone(blackberryTz[i]);
            if (tz.equals(timeZone))
            {
                if (tz.useDaylightTime())
                {
                    return pegasusTz[i + 1];
                }
                else
                {
                    return pegasusTz[i];
                }
            }
            else if (tz.getID().equals("(GMT)") || tz.getID().equals("GMT"))
            {
                return "UT";
            }
        }

        return tz.toString();
    }

}
