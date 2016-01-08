/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import smoothcombtt.xpo.com.gpstrack.TrackingModule.common.Point;

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

    // Nuevos metodos para el geofence


    /**
     * Metodo para calcular distancia entre dos puntos. Requiere 2 Locations
     *
     * @param one
     * @param two
     * @return
     */
    private static Double distance(Location one, Location two) {
        int R = 6371000;
        Double dLat = toRad(two.getLatitude() - one.getLatitude());
        Double dLon = toRad(two.getLongitude() - one.getLongitude());
        Double lat1 = toRad(one.getLatitude());
        Double lat2 = toRad(two.getLatitude());
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double d = R * c;

        // Redondear a 3 decimales
        int dec = 3;
        d = Math.round(d*Math.pow(10,3))/ Math.pow(10, 3);
        return d;
    }

    private static Double ConvCoordToMetros(Double latLng) {
        int R = 6371000;
        Double dLat = toRad(latLng);
        Double dLon = toRad(0.0);
        Double lat1 = toRad(0.0);
        Double lat2 = toRad(latLng);
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double d = R * c;

        // Redondear a 3 decimales
        int dec = 3;
        d = Math.round(d*Math.pow(10,3))/ Math.pow(10, 3);
        return d;
    }

    private static double toRad(Double d) {
        return d * Math.PI / 180;
    }


    /**
     * Metodo para calcular el area de una region mediante coordenadas
     * @param points
     * @return
     */
    public static Double AreaPolygon(Point [] points){

        Double tempD = 0.0;
        Double tempd = 0.0;
        Point[] tempPoints = new Point[points.length + 1];

        for(int k = 0; k < points.length;k++){
            tempPoints[k] = points[k];
        }

        tempPoints[tempPoints.length - 1] = points[0];


        for(int i = 0; i < tempPoints.length-1; i++){

            tempD = tempD + tempPoints[i].latitude * tempPoints[i+1].longitude;
        }

        for(int j = 0; j < tempPoints.length-1; j++){

            tempd = tempd + tempPoints[j+1].latitude * tempPoints[j].longitude;
        }
        return Math.abs(tempD - tempd)*0.5;
    }

    /**
     * Metodo para verificar el ingreso de un punto al interior de un geofence
     * @param geofence
     * @param position
     * @return
     */
    public static boolean isInsideGeofence(Point [] geofence, Point position){

        Double tempAreaGeo =  AreaPolygon(geofence);
        Double tempSumSmallArea = 0.0;
        Point[] tempPoint = new Point[3];

        for(int k = 0; k < geofence.length; k++){


            if(k == geofence.length-1){
                tempPoint[0] = geofence[k];
                tempPoint[1] = geofence[0];
                tempPoint[2] = position;
            }
            else{
                tempPoint[0] = geofence[k];
                tempPoint[1] = geofence[k+1];
                tempPoint[2] = position;

            }

            tempSumSmallArea = tempSumSmallArea + AreaPolygon(tempPoint);
        }

        Log.e("AREA POLY",String.valueOf(tempAreaGeo));
        Log.e("AREA SUM",String.valueOf(tempSumSmallArea));

        if(tempSumSmallArea > tempAreaGeo){
            return false;
        }
        else{
            return true;
        }

    }

    /**
     * Metodo para identificar si un punto se desvía del camino asignado
     * @param arrayPathLoc
     * @param currentLoc
     * @return
     */
    public static boolean isNearPath(ArrayList<LatLng> arrayPathLoc, Location currentLoc){

        boolean isInsideLat = false;
        boolean isInsideLng = false;

        Double [] latPoly = new Double[4];
        Double [] lngPoly = new Double[4];
        Double widthPoly = 0.0005;
        Double heightPoly = 0.0005;

        latPoly[0] = currentLoc.getLatitude() - (heightPoly/2.0);
        latPoly[1] = currentLoc.getLatitude() + (heightPoly/2.0);
        latPoly[2] = currentLoc.getLatitude() + (heightPoly/2.0);
        latPoly[3] = currentLoc.getLatitude() - (heightPoly/2.0);

        lngPoly[0] = currentLoc.getLongitude() + (widthPoly/2.0);
        lngPoly[1] = currentLoc.getLongitude() + (widthPoly/2.0);
        lngPoly[2] = currentLoc.getLongitude() - (widthPoly/2.0);
        lngPoly[3] = currentLoc.getLongitude() - (widthPoly/2.0);

        for(int i = 0; i < arrayPathLoc.size(); i++){

            if((latPoly[0] < arrayPathLoc.get(i).latitude)&&(arrayPathLoc.get(i).latitude < latPoly[1])){
                isInsideLat = true;

                if((lngPoly[2] < arrayPathLoc.get(i).longitude)&&(arrayPathLoc.get(i).longitude < lngPoly[1])){
                    isInsideLng = true;
                    break;
                }
                else{
                    isInsideLng = false;
                }
            }else{
                isInsideLat = false;
            }

        }


        return isInsideLat && isInsideLng;
    }

    /**
     * Metodo para calcular la distancia hacia un Geofence
     * @param tempGeofence
     * @param position
     * @return
     */
    public static Double distanceToGeofence(Point[] tempGeofence, Point position){

        Double[] tempDistance = new Double[tempGeofence.length];
        int[] tempPositionDistance = new int[tempGeofence.length];
        Location tempLoc1 = new Location(LocationManager.GPS_PROVIDER);
        Location tempLoc2 = new Location(LocationManager.GPS_PROVIDER);
        int tempIndex = 0;
        Point[] tempPointTriangule = new Point[3];

        Double tempDistanceToGeofence = 0.0;

        if((tempGeofence == null)&&(position == null)){

            Log.e("distanceToGeofence", "0.0");
            return 0.0;
        }

        //Calculamos las distancias hacia cada punto del geofence
        for(int i = 0; i < tempGeofence.length; i++){

            tempLoc1.setLatitude(tempGeofence[i].latitude);
            tempLoc1.setLongitude(tempGeofence[i].longitude);

            tempLoc2.setLatitude(position.latitude);
            tempLoc2.setLongitude(position.longitude);

            tempDistance[i] = distance(tempLoc1, tempLoc2); // Distancia en Metros
            tempPositionDistance[i] = i;
        }

        int tempChange = 0;
        Double tempChangeDistance = 0.0;

        // Realizamos el ordenamineto de las distancias
        for(int i = 0; i < tempDistance.length - 1; i++){
            for(int j = i +1; j < tempDistance.length; j++){
                if(tempDistance[i] > tempDistance[j]){

                    tempChangeDistance = tempDistance[i];
                    tempDistance[i] = tempDistance[j];
                    tempDistance[j] = tempChangeDistance;

                    tempChange = tempPositionDistance[i];
                    tempPositionDistance[i] = tempPositionDistance[j];
                    tempPositionDistance[j] = tempChange;
                }
            }
        }

        // Tomamos las medidas mas pequeñas para generar un triangulo que nos ayudara
        // con el cálculo de distancia


        tempLoc1.setLatitude(tempGeofence[tempPositionDistance[0]].latitude);
        tempLoc1.setLongitude(tempGeofence[tempPositionDistance[0]].longitude);

        tempLoc2.setLatitude(tempGeofence[tempPositionDistance[1]].latitude);
        tempLoc2.setLongitude(tempGeofence[tempPositionDistance[1]].longitude);

        Double tempBaseLength = distance(tempLoc1, tempLoc2); // Distancia en Metros
        //Double tempBaseLengthCoord = tempBaseLength * 0.000008998719243599958;
        Double tempBaseLengthCoord = tempBaseLength *   0.00000899321012635446; //111195 m.


        tempPointTriangule[0] = tempGeofence[tempPositionDistance[0]];
        tempPointTriangule[1] = tempGeofence[tempPositionDistance[1]];
        tempPointTriangule[2]= position;

        Double tempAreaTriangule = AreaPolygon(tempPointTriangule);

        if(tempBaseLength != 0.0){
            tempDistanceToGeofence = tempAreaTriangule/tempBaseLengthCoord;
        }

        Log.e("distanceToGeofence", String.valueOf(ConvCoordToMetros(tempDistanceToGeofence)));
        return ConvCoordToMetros(tempDistanceToGeofence);

    }

}
