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

import smoothcombtt.xpo.com.gpstrack.TrackingModule.bean.BECurrentLocation;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.bean.BEResponseRoute;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.bean.BERouteDetails;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.bean.BERouteLocation;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.common.GpsConstants;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.common.Point;

/**
 * @author rodolfo.burlando
 */
public class Helper {

    public static final int EQUALS = 0;
    public static final int STARTS_WITH = 1;

    public Helper() {
    }

    public static boolean findInArray(String[] data, String toFind, int findingMode) {
        if (data != null && data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                if (data[i].startsWith(toFind)) {
                    return true;
                }
            }
        }

        return false;
    }

    static public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String sCurrentDate;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        sCurrentDate = dateFormat.format(calendar.getTime());

        return sCurrentDate;
    }

    static public String getCurrentDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String sCurrentDate;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        sCurrentDate = dateFormat.format(calendar.getTime());
        return sCurrentDate;
    }

    public static int compareVersions(String version, String newestVersion) {
        version = version.trim();
        newestVersion = newestVersion.trim();
        Vector disasembledVersion = StringHelper.split(version, '.');
        Vector disasembledNewestVersion = StringHelper.split(newestVersion, '.');
        int versionInt = 0;
        int newestVersionInt = 0;
        int mayorSize = disasembledVersion.size();
        if (disasembledNewestVersion.size() > mayorSize) {
            mayorSize = disasembledNewestVersion.size();
        }
        for (int i = mayorSize - 1, j = 0; i >= 0; i--, j++) {
            if (i < disasembledVersion.size()) {
                double num = Integer.parseInt(disasembledVersion.elementAt(i).toString()) * Math.pow(10, j);
                versionInt += num;
            }
        }
        for (int i = mayorSize - 1, j = 0; i >= 0; i--, j++) {
            if (i < disasembledNewestVersion.size()) {
                newestVersionInt += (Integer.parseInt(disasembledNewestVersion.elementAt(i).toString()) * Math.pow(10, j));
            }
        }

        LogHelper.logDebug("older:" + versionInt);
        LogHelper.logDebug("newer:" + newestVersionInt);
        if (versionInt < newestVersionInt) {
            return -1;
        } else if (versionInt == newestVersionInt) {
            return 0;
        } else if (versionInt > newestVersionInt) {
            return 1;
        } else {
            throw new RuntimeException("Unespected Error");
        }
    }

    public static String removeAllChars(String version, char c) {
        char[] versionChar = version.toCharArray();
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < versionChar.length; i++) {
            if (versionChar[i] != c) {
                ret.append(versionChar[i]);
            }
        }
        return ret.toString().trim();
    }

    public static Point toPoint(Double[] coordinates) {
        Point ret = new Point();
        ret.latitude = coordinates[0].doubleValue();
        ret.longitude = coordinates[1].doubleValue();
        return ret;
    }

    public static Point[] toPointArray(Double[] fence) {
        Point[] ret = new Point[fence.length / 2];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new Point();
            ret[i].latitude = fence[2 * i].doubleValue();
            ret[i].longitude = fence[2 * i + 1].doubleValue();
        }
        return ret;
    }

    public static void deleteArrayItem(Object[] array, Object item) {
        Vector ret = new Vector();
        for (int i = 0; i < array.length; i++) {
            if (!array[i].equals(item)) {
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
    public static String getCurrentTime() {
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

        if (tzname.equals("Eastern Standard Time")) {
            tzname = "(GMT-05:00) Eastern Time (US & Canada)";
        } else {
            if (tzname.equals("Central Standard Time")) {
                tzname = "(GMT-06:00) Central Time (US & Canada)";
            } else {
                if (tzname.equals("Mountain Standard Time")) {
                    tzname = "(GMT-07:00) Mountain Time (US & Canada)";
                } else {
                    if (tzname.equals("Pacific Standard Time")) {
                        tzname = "(GMT-08:00) Pacific Time (US & Canada)";
                    } else {
                        if (tzname.equals("Alaska Standard Time")) {
                            tzname = "(GMT-09:00) Alaska";
                        } else {
                            if (tzname.equals("Hawaii-Aleutian Standard Time")) {
                                tzname = "(GMT-10:00) Hawaii";
                            } else {

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

    public static void startNotification() {
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

    public static void stopNotification() {
        // if (Alert.isVibrateSupported())
        // Alert.stopVibrate();
        // if (Alert.isAudioSupported())
        // Alert.stopAudio();
        // if (Alert.isBuzzerSupported())
        // Alert.stopBuzzer();
        // LED.setState(LED.STATE_OFF);
    }


    public static Hashtable getRemoteJad(String jadUrl) {
        throw new RuntimeException("Not implemented");
    }

    public static TimeZone parseTimezone(String tz) {
        if (tz.equals("CD"))// Central Daylight Time
        {
            return TimeZone.getTimeZone("America/Chicago");
        } else if (tz.equals("CS"))// Central Standard Time
        {
            return TimeZone.getTimeZone("America/Chicago");
        } else if (tz.equals("ED"))// Eastern Daylight Time
        {
            return TimeZone.getTimeZone("America/New_York");
        } else if (tz.equals("ES"))// Eastern Standard Time
        {
            return TimeZone.getTimeZone("America/New_York");
        } else if (tz.equals("MD"))// Mountain Daylight Time
        {
            return TimeZone.getTimeZone("America/Denver");
        } else if (tz.equals("MS"))// Mountain Standard Time
        {
            return TimeZone.getTimeZone("America/Phoenix");
        } else if (tz.equals("PD"))// Pacific Daylight Time
        {
            return TimeZone.getTimeZone("America/Los_Angeles");
        } else if (tz.equals("PS"))// Pacific Standard Time
        {
            return TimeZone.getTimeZone("America/Los_Angeles");
        } else if (tz.equals("UT"))// Universal Time
        {
            return TimeZone.getTimeZone("GMT");
        } else {
            return TimeZone.getTimeZone("GMT");
        }
    }

    public static String toPegasusTimezoneAsString(TimeZone tz) {
        String[] pegasusTz = {"CST", "CDT", "EST", "EDT", "MST", "MDT", "PST", "PDT", "UTC", "UTC"};
        String[] blackberryTz = {
                "America/Chicago", "America/Chicago", "America/New_York", "America/New_York",
                "America/Denver", "America/Phoenix", "America/Los_Angeles", "America/Los_Angeles", "GMT"
        };
        for (int i = 0; i < blackberryTz.length; i++) {
            TimeZone timeZone = TimeZone.getTimeZone(blackberryTz[i]);
            if (tz.equals(timeZone)) {
                if (tz.useDaylightTime()) {
                    return pegasusTz[i + 1];
                } else {
                    return pegasusTz[i];
                }
            } else if (tz.getID().equals("(GMT)") || tz.getID().equals("GMT")) {
                return "UT";
            }
        }

        return tz.toString();
    }
    /////////////////////////////////// Nuevos metodos para GPS /////////////////////////////////////////////

    /**
     * Metodo para calcular distancia entre dos puntos. Requiere 2 Locations
     *
     * @param one
     * @param two
     * @return
     */
    public static Double distance(Location one, Location two) {

        Double d = 0.0;

        if ((one == null) || (two == null)) {
            return null;
        }

        d = Double.valueOf(one.distanceTo(two));

        /*float [] tempResults = new float[10];
        Location.distanceBetween(one.getLatitude(), one.getLongitude(), two.getLatitude(), two.getLongitude(), tempResults);
        tempResult = Double.valueOf(tempResults[0]);*/

/*
        int R = 6371000;
        Double dLat = toRad(two.getLatitude() - one.getLatitude());
        Double dLon = toRad(two.getLongitude() - one.getLongitude());
        Double lat1 = toRad(one.getLatitude());
        Double lat2 = toRad(two.getLatitude());
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        d = R * c;
*/
        // Redondear a 3 decimales
        int dec = 3;
        d = Math.round(d * Math.pow(10, 3)) / Math.pow(10, 3);

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
        d = Math.round(d * Math.pow(10, 3)) / Math.pow(10, 3);
        return d;
    }

    private static double toRad(Double d) {
        return d * Math.PI / 180;
    }

    public static Double directionBetweenPoints(Location loc1, Location loc2) {
        Double tempDirection = 0.0;
/*
        boolean isRigthSide = true;
        boolean isUpSide = true;

        Double deltaLat = 0.0;
        Double deltaLng = 0.0;
        Double tempDirection = 0.0;

        Double tempLat1 = loc1.latitude;
        Double tempLng1 = loc1.longitude;
        Double tempLat2 = loc2.latitude;
        Double tempLng2 = loc2.longitude;

        deltaLat = Math.abs(tempLat2 - tempLat1);
        deltaLng = Math.abs(tempLng2 - tempLng1);

        if (tempLat2 > tempLat1) {
            isUpSide = true;
        } else {
            isUpSide = false;
        }

        if (tempLng2 > tempLat1) {
            isRigthSide = true;
        } else {
            isRigthSide = false;
        }

        tempDirection = Math.atan2(deltaLat, deltaLng);

        // Buscando la posición del punto destino
        if (isUpSide) {
            if (isRigthSide) {
                tempDirection = tempDirection;
            } else {
                tempDirection = Math.PI - tempDirection;
            }
        } else {
            if (isRigthSide) {
                tempDirection = 2 * Math.PI - tempDirection;
            } else {
                tempDirection = Math.PI + tempDirection;
            }
        }

        */
        tempDirection = Double.valueOf(loc1.bearingTo(loc2));
        tempDirection = Math.round(tempDirection * GpsConstants.ROUND_NUMBER) / GpsConstants.ROUND_NUMBER;

        return tempDirection;
    }

    /**
     * Metodo para calcular el area de una region mediante coordenadas
     *
     * @param points
     * @return
     */
    public static Double AreaPolygon(Point[] points) {

        Double tempD = 0.0;
        Double tempd = 0.0;
        Point[] tempPoints = new Point[points.length + 1];

        for (int k = 0; k < points.length; k++) {
            tempPoints[k] = points[k];
        }

        tempPoints[tempPoints.length - 1] = points[0];


        for (int i = 0; i < tempPoints.length - 1; i++) {

            tempD = tempD + tempPoints[i].latitude * tempPoints[i + 1].longitude;
        }

        for (int j = 1; j < tempPoints.length; j++) {

            tempd = tempd + tempPoints[j].latitude * tempPoints[j - 1].longitude;
        }
        return Math.abs(tempD - tempd) * 0.5;
    }

    /**
     * Metodo para verificar el ingreso de un punto al interior de un geofence
     *
     * @param geofence
     * @param position
     * @return
     */
    public static boolean isInsideGeofence(Point[] geofence, Point position) {

        Double tempAreaGeo = AreaPolygon(geofence);
        Double tempSumSmallArea = 0.0;
        Point[] tempPoint = new Point[3];

        for (int k = 0; k < geofence.length; k++) {


            if (k == geofence.length - 1) {
                tempPoint[0] = geofence[k];
                tempPoint[1] = geofence[0];
                tempPoint[2] = position;
            } else {
                tempPoint[0] = geofence[k];
                tempPoint[1] = geofence[k + 1];
                tempPoint[2] = position;

            }

            tempSumSmallArea = tempSumSmallArea + AreaPolygon(tempPoint);
        }

        Log.e("AREA POLY", String.valueOf(tempAreaGeo));
        Log.e("AREA SUM", String.valueOf(tempSumSmallArea));

        if (tempSumSmallArea > tempAreaGeo) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * Metodo para identificar si un punto se desvía del camino asignado
     *
     * @param arrayPathLoc
     * @param currentLoc
     * @return
     */
    public static boolean isNearPath(ArrayList<LatLng> arrayPathLoc, Location currentLoc) {

        boolean isInsideLat = false;
        boolean isInsideLng = false;

        Double[] latPoly = new Double[4];
        Double[] lngPoly = new Double[4];
        Double widthPoly = 0.0005;
        Double heightPoly = 0.0005;

        latPoly[0] = currentLoc.getLatitude() - (heightPoly / 2.0);
        latPoly[1] = currentLoc.getLatitude() + (heightPoly / 2.0);
        latPoly[2] = currentLoc.getLatitude() + (heightPoly / 2.0);
        latPoly[3] = currentLoc.getLatitude() - (heightPoly / 2.0);

        lngPoly[0] = currentLoc.getLongitude() + (widthPoly / 2.0);
        lngPoly[1] = currentLoc.getLongitude() + (widthPoly / 2.0);
        lngPoly[2] = currentLoc.getLongitude() - (widthPoly / 2.0);
        lngPoly[3] = currentLoc.getLongitude() - (widthPoly / 2.0);

        for (int i = 0; i < arrayPathLoc.size(); i++) {

            if ((latPoly[0] < arrayPathLoc.get(i).latitude) && (arrayPathLoc.get(i).latitude < latPoly[1])) {
                isInsideLat = true;

                if ((lngPoly[2] < arrayPathLoc.get(i).longitude) && (arrayPathLoc.get(i).longitude < lngPoly[1])) {
                    isInsideLng = true;
                    break;
                } else {
                    isInsideLng = false;
                }
            } else {
                isInsideLat = false;
            }

        }


        return isInsideLat && isInsideLng;
    }

    /**
     * Metodo que calcula la cercanía de una posicion actual y un punto determinado
     *
     * @param pathPointLoc
     * @param geolength
     * @param currentLoc
     * @return
     */
    public static boolean isNearCurrentPosition(Location pathPointLoc, Double geolength, Location currentLoc) {

        boolean isInsideLat = false;
        boolean isInsideLng = false;

        Double[] latPoly = new Double[4];
        Double[] lngPoly = new Double[4];
        Double widthPoly = geolength;
        Double heightPoly = geolength;

        latPoly[0] = currentLoc.getLatitude() - (heightPoly / GpsConstants.DIVIDER);
        latPoly[1] = currentLoc.getLatitude() + (heightPoly / GpsConstants.DIVIDER);
        latPoly[2] = currentLoc.getLatitude() + (heightPoly / GpsConstants.DIVIDER);
        latPoly[3] = currentLoc.getLatitude() - (heightPoly / GpsConstants.DIVIDER);

        lngPoly[0] = currentLoc.getLongitude() + (widthPoly / GpsConstants.DIVIDER);
        lngPoly[1] = currentLoc.getLongitude() + (widthPoly / GpsConstants.DIVIDER);
        lngPoly[2] = currentLoc.getLongitude() - (widthPoly / GpsConstants.DIVIDER);
        lngPoly[3] = currentLoc.getLongitude() - (widthPoly / GpsConstants.DIVIDER);


        if ((latPoly[0] < pathPointLoc.getLatitude()) && (pathPointLoc.getLatitude() < latPoly[1])) {
            isInsideLat = true;

            if ((lngPoly[2] < pathPointLoc.getLongitude()) && (pathPointLoc.getLongitude() < lngPoly[1])) {
                isInsideLng = true;
            } else {
                isInsideLng = false;
            }
        } else {
            isInsideLat = false;
        }

        return isInsideLat && isInsideLng;
    }

    public static boolean isSameDirection(Location pathPointLoc, Double direction, Location currentLoc) {

        boolean temIsSameDirection = false;

        Double tempDirection = 0.0;
        tempDirection = directionBetweenPoints(pathPointLoc, currentLoc);

        Double tempDeltaPosi = direction + GpsConstants.DELTA_DIRECTION;
        Double tempDeltaNega = direction - GpsConstants.DELTA_DIRECTION;

        if ((tempDeltaNega < tempDirection) && (tempDirection < tempDeltaPosi)) {

            temIsSameDirection = true;
        }

        return temIsSameDirection;
    }

    /**
     * Metodo para calcular la distancia hacia un Geofence
     *
     * @param tempGeofence
     * @param position
     * @return
     */
    public static Double distanceToGeofence(Point[] tempGeofence, Point position) {

        Double[] tempDistance = new Double[tempGeofence.length];
        int[] tempPositionDistance = new int[tempGeofence.length];
        Location tempLoc1 = new Location(LocationManager.GPS_PROVIDER);
        Location tempLoc2 = new Location(LocationManager.GPS_PROVIDER);
        int tempIndex = 0;
        Point[] tempPointTriangule = new Point[3];

        Double tempDistanceToGeofence = 0.0;

        if ((tempGeofence == null) && (position == null)) {

            Log.e("distanceToGeofence", "0.0");
            return 0.0;
        }

        //Calculamos las distancias hacia cada punto del geofence
        for (int i = 0; i < tempGeofence.length; i++) {

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
        for (int i = 0; i < tempDistance.length - 1; i++) {
            for (int j = i + 1; j < tempDistance.length; j++) {
                if (tempDistance[i] > tempDistance[j]) {

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
        Double tempBaseLengthCoord = tempBaseLength * 0.00000899321012635446; //111195 m.


        tempPointTriangule[0] = tempGeofence[tempPositionDistance[0]];
        tempPointTriangule[1] = tempGeofence[tempPositionDistance[1]];
        tempPointTriangule[2] = position;

        Double tempAreaTriangule = AreaPolygon(tempPointTriangule);

        if (tempBaseLength != 0.0) {
            tempDistanceToGeofence = tempAreaTriangule / tempBaseLengthCoord;
        }

        Log.e("distanceToGeofence", String.valueOf(ConvCoordToMetros(tempDistanceToGeofence)));
        return ConvCoordToMetros(tempDistanceToGeofence);

    }

    /**
     * Metodo para actualizar las direccion de un punto hacia otro
     *
     * @param beRouteLocation
     * @return
     */
    public static BERouteDetails[] GetDirectionBetweenPoints(BERouteDetails[] beRouteDetails) {

        Double tempLat1 = 0.0;
        Double tempLng1 = 0.0;
        Double tempLat2 = 0.0;
        Double tempLng2 = 0.0;

        Double tempDirection = 0.0;

        for (int l = 0; l < beRouteDetails.length - 1; l++) {

            tempLat1 = Double.valueOf(beRouteDetails[l].getLatitude());
            tempLng1 = Double.valueOf(beRouteDetails[l].getLongitude());

            tempLat2 = Double.valueOf(beRouteDetails[l + 1].getLatitude());
            tempLng2 = Double.valueOf(beRouteDetails[l + 1].getLongitude());

            Location tempLoc1 = new Location(LocationManager.GPS_PROVIDER);
            Location tempLoc2 = new Location(LocationManager.GPS_PROVIDER);

            tempLoc1.setLatitude(tempLat1);
            tempLoc1.setLongitude(tempLng1);
            tempLoc2.setLatitude(tempLat2);
            tempLoc2.setLongitude(tempLng2);

            tempDirection = directionBetweenPoints(tempLoc1, tempLoc2);

            beRouteDetails[l].setDirectionNextPoint(tempDirection);
        }

        return beRouteDetails;
    }

    /**
     * Metodo para conseguir los parametros de cada punto de la ruta
     *
     * @param beRouteLocation
     * @return
     */
    public static BERouteDetails[] GetParamBetweenPoints(BERouteDetails[] beRouteDetails) {


        ArrayList<BERouteDetails> beRouteDetailses = new ArrayList<BERouteDetails>();
        Location locationOrigin = new Location(LocationManager.GPS_PROVIDER);
        Location locationDestination = new Location(LocationManager.GPS_PROVIDER);
        boolean isEqualPoint1 = false;
        boolean isEqualPoint2 = false;

        // Retirando los puntos iguales que vienen en la lista
        BERouteDetails tempBeRoute;

        int indexList = 0;

        tempBeRoute = new BERouteDetails();
        tempBeRoute.setRoutePointId(beRouteDetails[0].getRoutePointId());
        tempBeRoute.setLatitude(beRouteDetails[0].getLatitude());
        tempBeRoute.setLongitude(beRouteDetails[0].getLongitude());
        tempBeRoute.setOrder(beRouteDetails[0].getOrder());
        tempBeRoute.setIsWaypoint(beRouteDetails[0].getIsWaypoint());
        tempBeRoute.setRouteId(beRouteDetails[0].getRouteId());
        beRouteDetailses.add(tempBeRoute);
        indexList++;

        for (int k = 1; k < beRouteDetails.length; k++) {

            isEqualPoint1 = !beRouteDetailses.get(indexList - 1).getLatitude().equals(beRouteDetails[k].getLatitude());
            isEqualPoint2 = !beRouteDetailses.get(indexList - 1).getLongitude().equals(beRouteDetails[k].getLongitude());

            if (isEqualPoint1 || isEqualPoint2) {

                tempBeRoute = new BERouteDetails();
                tempBeRoute.setRoutePointId(beRouteDetails[k].getRoutePointId());
                tempBeRoute.setLatitude(beRouteDetails[k].getLatitude());
                tempBeRoute.setLongitude(beRouteDetails[k].getLongitude());
                tempBeRoute.setOrder(beRouteDetails[k].getOrder());
                tempBeRoute.setIsWaypoint(beRouteDetails[k].getIsWaypoint());
                tempBeRoute.setRouteId(beRouteDetails[k].getRouteId());
                beRouteDetailses.add(tempBeRoute);
                indexList++;
            }
        }

        BERouteDetails[] tempBeRouteDetailsReduction = new BERouteDetails[beRouteDetailses.size()];

        for (int l = 0; l < beRouteDetailses.size(); l++) {
            tempBeRoute = new BERouteDetails();
            tempBeRoute.setRoutePointId(beRouteDetailses.get(l).getRoutePointId());
            tempBeRoute.setLatitude(beRouteDetailses.get(l).getLatitude());
            tempBeRoute.setLongitude(beRouteDetailses.get(l).getLongitude());
            tempBeRoute.setOrder(beRouteDetailses.get(l).getOrder());
            tempBeRoute.setIsWaypoint(beRouteDetailses.get(l).getIsWaypoint());
            tempBeRoute.setRouteId(beRouteDetailses.get(l).getRouteId());

            tempBeRouteDetailsReduction[l] = tempBeRoute;
        }

        BERouteDetails[] tempBeRouteDetails = tempBeRouteDetailsReduction;
        // Calculando la distancia entre puntos

        for (int i = 0; i < tempBeRouteDetails.length - 1; i++) {

            locationOrigin.setLatitude(Double.valueOf(tempBeRouteDetails[i].getLatitude()));
            locationOrigin.setLongitude(Double.valueOf(tempBeRouteDetails[i].getLongitude()));

            locationDestination.setLatitude(Double.valueOf(tempBeRouteDetails[i + 1].getLatitude()));
            locationDestination.setLongitude(Double.valueOf(tempBeRouteDetails[i + 1].getLongitude()));

            tempBeRouteDetails[i].setDistanceNextPoint(Helper.distance(locationOrigin, locationDestination));
        }

        // Agrupando de acuerdo a la longitud de separación de cada punto
        for (int k = 0; k < tempBeRouteDetails.length - 1; k++) {

            if (tempBeRouteDetails[k].getDistanceNextPoint() > GpsConstants.SHORT_DISTANCE) {
                tempBeRouteDetails[k].setGroupType(BERouteDetails.LONG_GROUP);
            } else {
                tempBeRouteDetails[k].setGroupType(BERouteDetails.SHORT_GROUP);
            }
        }

        // Calculando la direccion entre puntos (en radianes)
        // Consideramos el destino como punto para analizar respecto al origen
        tempBeRouteDetails = Helper.GetDirectionBetweenPoints(tempBeRouteDetails);


        // Calculo de los grupos para la consulta del tracking
        int tempResLengthRoute = 0;
        int tempLengthRoute = 0;
        int tempGroupNumber = 0;
        int tempGroupNumberLabel = 0;

        tempLengthRoute = tempBeRouteDetails.length;

        if (tempLengthRoute > GpsConstants.WAYPOINTS_GROUP) {

            tempResLengthRoute = tempLengthRoute % GpsConstants.WAYPOINTS_GROUP;
            tempGroupNumber = (tempLengthRoute - tempResLengthRoute) / GpsConstants.WAYPOINTS_GROUP;

            if (tempResLengthRoute > 0) {
                tempGroupNumber++;
            }
        } else {
            tempGroupNumber++;
        }

        for (int m = 0; m < tempBeRouteDetails.length; m++) {

            if (m < GpsConstants.WAYPOINTS_GROUP * (tempGroupNumberLabel + 1)) {
                tempBeRouteDetails[m].setGroupNumber(tempGroupNumberLabel);
            } else {
                tempGroupNumberLabel++;
                tempBeRouteDetails[m].setGroupNumber(tempGroupNumberLabel);
            }

            // Completamos el estado de cada punto s NORMAL
            tempBeRouteDetails[m].setStatus(GpsConstants.STATUS_ROUTE_LOCATION_NORMAL);
        }

        tempBeRouteDetails[tempBeRouteDetails.length - 1].setStatus(GpsConstants.STATUS_ROUTE_LOCATION_NORMAL);

        return tempBeRouteDetails;
    }

    /**
     * Metodo para calcular la distancia de un punto actual hacia una recta definida por dos puntos. Este metodo
     * utiliza el area de una region triangular para calcular la distancia
     *
     * @param origin
     * @param destination
     * @param currentPos
     * @return
     */
    public static Double distancePointToRoad(Location origin, Location destination, Location currentPos) {

        Point[] tempPointTriangule = new Point[3];
        Point tempPointOrigin = new Point();
        Point tempPointDestination = new Point();
        Point tempPointCurrentPos = new Point();
        Double tempBaseLength = 0.0;
        Double tempDistanceToRoad = 0.0;

        tempPointOrigin.latitude = origin.getLatitude();
        tempPointOrigin.longitude = origin.getLongitude();
        tempPointDestination.latitude = destination.getLatitude();
        tempPointDestination.longitude = destination.getLongitude();
        tempPointCurrentPos.latitude = currentPos.getLatitude();
        tempPointCurrentPos.longitude = currentPos.getLongitude();


        tempPointTriangule[0] = tempPointOrigin;
        tempPointTriangule[1] = tempPointDestination;
        tempPointTriangule[2] = tempPointCurrentPos;

        Double tempAreaTriangule = AreaPolygon(tempPointTriangule);// en grados

        tempBaseLength = distance(origin, destination);
        Double tempBaseLengthCoord = tempBaseLength * 0.00000899321012635446; //111195 m.

        if (tempBaseLength != 0.0) {
            tempDistanceToRoad = tempAreaTriangule / tempBaseLengthCoord;
        }

        Log.e("DistanceRoad", String.valueOf(ConvCoordToMetros(tempDistanceToRoad)));
        return ConvCoordToMetros(tempDistanceToRoad);
    }

    static String currentWaypoint = "-1"; ///////

    /**
     * Metodo principal que contiene el algoritmo de verificación de ruta (Tracking)
     *
     * @param beRouteLocations
     * @param waypointsGroup
     * @param currentLoc
     * @return
     */
    public static BEResponseRoute isNearPathMichelin(BERouteDetails[] beRouteDetailses, int waypointsGroup, Location currentLoc) {

        BERouteDetails[] tempBeRouteLocations1 = beRouteDetailses;
        BECurrentLocation beCurrentLocation = new BECurrentLocation();
        BEResponseRoute beResponseRoute = new BEResponseRoute();

        int quantGroups = 0;
        Location tempWaypointLoc = new Location(LocationManager.GPS_PROVIDER);
        Location tempWaypointLocNext = new Location(LocationManager.GPS_PROVIDER);
        Double tempDistanciaWaypointCurrLoc = 0.0;
        Double tempDistanciaWaypointCurrLocNext = 0.0;
        Double tempDirectionBetweenPoint = 0.0;
        Double tempDistanciaWaypointCurrLocGrados = 0.0;
        boolean tempIsNearCurrPos = false;
        boolean tempIsNearCurrPosNext = false;
        boolean tempIsSameDirection = false;
        boolean tempIsNearPathMichelin = false;
        boolean tempGetReferenceWaypoint = false;
        Double tempDistanceToRoad = 0.0;


        quantGroups = tempBeRouteLocations1[tempBeRouteLocations1.length - 1].getGroupNumber() + 1;

        // Busqueda en todos los grupos
        for (int i = 0; i < quantGroups; i++) {

            //If encontramos una referencia, terminamos el bucle
            if (tempGetReferenceWaypoint) {
                break;
            }

            //Busqueda dentro de cada group
            for (int j = waypointsGroup * i; j < waypointsGroup * (i + 1); j++) {

                if (j < tempBeRouteLocations1.length - 1) {

                    tempWaypointLoc.setLatitude(Double.valueOf(tempBeRouteLocations1[j].getLatitude()));
                    tempWaypointLoc.setLongitude(Double.valueOf(tempBeRouteLocations1[j].getLongitude()));

                    beCurrentLocation.setLatitude(Double.valueOf(tempBeRouteLocations1[j].getLatitude()));
                    beCurrentLocation.setLongitude(Double.valueOf(tempBeRouteLocations1[j].getLongitude()));

                    // Calculamos la distancia entre el punto de la ruta y la posicion actual
                    tempDistanciaWaypointCurrLoc = distance(tempWaypointLoc, currentLoc);
                    beCurrentLocation.setDistance(tempDistanciaWaypointCurrLoc);

                    if (tempDistanciaWaypointCurrLoc <= tempBeRouteLocations1[j].getDistanceNextPoint()) {

                        // Calculamos la direccion del punto actual con la direcion de dos puntos consecutivos
                        tempIsSameDirection = isSameDirection(tempWaypointLoc, tempBeRouteLocations1[j].getDirectionNextPoint(), currentLoc);
                        beCurrentLocation.setDireccion(Helper.directionBetweenPoints(tempWaypointLoc, currentLoc));

                        if (tempIsSameDirection) {

                            // Solo tomamos los puntos cuyo estado sea NORMAL
                            if ((tempBeRouteLocations1[j].getStatus() == GpsConstants.STATUS_ROUTE_LOCATION_NORMAL)
                                    || (tempBeRouteLocations1[j].getStatus() == GpsConstants.STATUS_ROUTE_LOCATION_IN_PROCESS)) {

                                tempWaypointLocNext.setLatitude(Double.valueOf(tempBeRouteLocations1[j + 1].getLatitude()));
                                tempWaypointLocNext.setLongitude(Double.valueOf(tempBeRouteLocations1[j + 1].getLongitude()));

                                tempDistanceToRoad = distancePointToRoad(tempWaypointLoc, tempWaypointLocNext, currentLoc);

                                if (tempDistanceToRoad <= GpsConstants.ROAD_DISTANCE) {

                                    tempIsNearPathMichelin = true;
                                    tempBeRouteLocations1[j].setStatus(GpsConstants.STATUS_ROUTE_LOCATION_IN_PROCESS);

                                    if (!currentWaypoint.equals("-1")) {

                                        if (!currentWaypoint.equals(String.valueOf(j))) {

                                            // Cambiamos los estados anteriores a la ruta recorrida, con un valor REFUSED en caso no hyan sido seteados a OK
                                            for (int k = j - 1; 0 <= k; k--) {
                                                if (tempBeRouteLocations1[k].getStatus() == GpsConstants.STATUS_ROUTE_LOCATION_IN_PROCESS) {
                                                    tempBeRouteLocations1[k].setStatus(GpsConstants.STATUS_ROUTE_LOCATION_NAVIGATED);
                                                    Log.e("WAYPOINT " + String.valueOf(k), "NAVIGATED");
                                                } else {
                                                    if (tempBeRouteLocations1[k].getStatus() == GpsConstants.STATUS_ROUTE_LOCATION_NORMAL) {
                                                        tempBeRouteLocations1[k].setStatus(GpsConstants.STATUS_ROUTE_LOCATION_REFUSED);
                                                        Log.e("WAYPOINT " + String.valueOf(k), "REFUSED");
                                                    }
                                                }
                                            }

                                            beResponseRoute.setBeRouteDetailses(tempBeRouteLocations1);
                                        }
                                    }

                                    // Almacenamos la posicion del actualwaypoint
                                    currentWaypoint = String.valueOf(j);
                                    beResponseRoute.setInProgressWaypoint(j);
                                    beResponseRoute.setWaypointStatus(GpsConstants.STATUS_ROUTE_LOCATION_IN_PROCESS);
                                    beCurrentLocation.setStatus(GpsConstants.STATUS_DRIVER_ON_ROAD);
                                    Log.e("WAYPOINT " + String.valueOf(j), "IN PROGRESS");

                                } else {
                                    beCurrentLocation.setStatus(GpsConstants.STATUS_DRIVER_ON_SECTION);
                                    Log.e("WAYPOINT ON SECTION " + String.valueOf(j), "NORMAL");
                                }

                                tempGetReferenceWaypoint = true;
                                break;

                            } else {
                                if ((tempBeRouteLocations1[j].getStatus() == GpsConstants.STATUS_ROUTE_LOCATION_REFUSED) || (tempBeRouteLocations1[j].getStatus() == GpsConstants.STATUS_ROUTE_LOCATION_NAVIGATED)) {
                                    beCurrentLocation.setStatus(GpsConstants.STATUS_DRIVER_GO_BACK);
                                    tempGetReferenceWaypoint = true;
                                    break;
                                }

                            }


                        } else {
                            beCurrentLocation.setStatus(GpsConstants.STATUS_DRIVER_NEAR_WAYPOINT);
                        }

                    } else {
                        beCurrentLocation.setStatus(GpsConstants.STATUS_DRIVER_FAR_ROUTE);
                        Log.e("WAYPOINT FAR" + String.valueOf(j), "");
                    }

                }

            }

        }

        beResponseRoute.setBeCurrentLocation(beCurrentLocation);
        return beResponseRoute;
    }

}
