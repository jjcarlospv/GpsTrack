package smoothcombtt.xpo.com.gpstrack.TrackingModule.common;

/**
 * Created by jose.paucar on 14/12/2015.
 */
public class GpsConstants {


    public static final String SHARE_PACKAGE_APP = "com.xpo.smoothcombtt";

    // Flags para guardar las coordenadas
    public static final String SHARE_POSITION_TRACK = SHARE_PACKAGE_APP + "/" + "PositionTrack";
    public static final String SHARE_POSITION_TRACK_GPS = SHARE_POSITION_TRACK + "/"+"TrackGps";
    public static final String SHARE_POSITION_TRACK_LAT = SHARE_POSITION_TRACK + "/"+"TrackLat";
    public static final String SHARE_POSITION_TRACK_LNG = SHARE_POSITION_TRACK + "/"+"TrackLng";
    public static final String SHARE_POSITION_TRACK_ACY = SHARE_POSITION_TRACK + "/"+"TrackAcy";
    public static final String SHARE_POSITION_TRACK_PROV = SHARE_POSITION_TRACK + "/"+"TrackProv";
    public static final String SHARE_POSITION_TRACK_SPEED = SHARE_POSITION_TRACK + "/"+"TrackSpeed";

    public static final String POSITION_ACTION = SHARE_PACKAGE_APP + "/" + "Action";
    public static final String PROGRESS_LONGITUDE = POSITION_ACTION + "/" + "Longitude";
    public static final String PROGRESS_LATITUDE = POSITION_ACTION + "/" + "Latitude";
    public static final String PROGRESS_PROV = POSITION_ACTION + "/" + "Prov";
    public static final String PROGRESS_SPEED = POSITION_ACTION + "/" + "Speed";
    public static final String PROGRESS_DISTANCE = POSITION_ACTION + "/" + "Distance";

    public static final String SPEED_ACTION = "com.example.jean.SPEED";
    public static final String DISTANCE_ACTION = "com.example.jean.DISTANCE";

    public static final String GPS_STOPPED = "GpsStopped";

    // Variables para guardar la ultima posicion de la ruta recorrida
    public static final String SHARE_TRACKING = "SmoothComBttTracking";
    public static final String SHARE_TRACKING_LAST_WAYPOINT = "SmoothComBttTrackingLastWaypoint";

    public static int SWEET_PATH_GROUP = 10;

    // Radio por defecto
    public final static Double RADIO_DISTANCE = 50.0;

    // Parametros para los calculos de distancia y direccion entre puntos
    public final static Double SHORT_DISTANCE = 10.0;
    public final static Double MEDIUM_DISTANCE = 90.0;

    // Distancia hacia cada parte de ruta(m)
    public final static Double ROAD_DISTANCE = 10.0;

    //public final static Double DELTA_DIRECTION = 20.0;//0.34907; // 20° en radianes
    public final static Double DELTA_DIRECTION = 70.0;//0.8728; // 50° en radianes

    public final static int WAYPOINTS_GROUP = 100;

    public final static Double PI_VALUE = 3.1416;
    public final static Double ROUND_NUMBER = 10000.0;
    public final static Double IN_GRADOS_PARAM = 111195.0;
    public final static Double DIVIDER = 2.0;

    // Estados para cada WayPoint
    public final static int STATUS_ROUTE_LOCATION_NORMAL = 0;
    public final static int STATUS_ROUTE_LOCATION_IN_PROCESS = 1;
    public final static int STATUS_ROUTE_LOCATION_NAVIGATED = 2;
    public final static int STATUS_ROUTE_LOCATION_REFUSED = 3;

    // Estados para la posicion actual del driver

    public final static int STATUS_DRIVER_FAR_ROUTE = 0; // Alejado de la ruta
    public final static int STATUS_DRIVER_NEAR_WAYPOINT = 1; // Cerca de un waypoint - filtro 1
    public final static int STATUS_DRIVER_SAME_DIRECTION = 2; // En la misma direccion del waypoint - filtro 2
    public final static int STATUS_DRIVER_ON_SECTION = 3; // En la ruta - filtro 1
    public final static int STATUS_DRIVER_ON_ROAD = 4; // Cerca un waypoint con la misma direction
    // de la porcion de ruta pero alejado de la secióm de ruta.
    public final static int STATUS_DRIVER_GO_BACK = 5;


    // Sending SMS

    //public final static String SMS_CELPHONE_NUMBER = "9685811792"; // Celular de pruebas
    public final static String SMS_CELPHONE_NUMBER = "954781512"; // Celular de pruebas

    public final static String EXTRA_TRACKING_NOTIFICATION = "extraTrackingNotification";
    public final static String SMS_GPS_OFF = "smsGpsOff";
    public final static String SMS_GPS_ON = "smsGpsOn";
    public final static String SMS_DRIVER_ON_ROAD = "smsDriverOnRoute";
    public final static String SMS_DRIVER_FAR_ROUTE = "smsDriverFarRoute";
    public final static String SMS_DRIVER_FAR_ROUTE_2 = "smsDriverFarRoute2";
    public final static String SMS_DRIVER_FAR_ROUTE_3 = "smsDriverFarRoute3";


    // Cantidad de informacion para analizar la desviación
    public final static int TRACKING_QUANTITY = 6;
    public final static int TRACKING_QUANTITY_NOTIFICATION = 4;
    public final static int TRACKING_QUANTITY_NOTIFICATION_2 = 3;

    // Parámetros para almacenar una ruta específica en el celular
    public static final String SHARE_SAVE_ROUTE = SHARE_PACKAGE_APP + "/" + "SaveRoute";
    public static final String SHARE_SAVE_ROUTE_ID = SHARE_SAVE_ROUTE + "/" + "Id";
    public static final String SHARE_SAVE_ROUTE_DATE_MODIFIED = SHARE_SAVE_ROUTE + "/" + "DateModified";
    public static final String SHARE_SAVE_ROUTE_NULL = SHARE_SAVE_ROUTE + "/" + "Null";
/*
    //public static final String SHARE_PACKAGE_APP = "smoothcombtt.xpo.com.gpstrack";
    public static final String SHARE_PACKAGE_APP = "com.xpo.smoothcombtt";
    // Flags para guardar las coordenadas
    public static final String SHARE_POSITION_TRACK = SHARE_PACKAGE_APP + "/" + "PositionTrack";
    public static final String SHARE_POSITION_TRACK_GPS = SHARE_POSITION_TRACK + "/"+"TrackGps";
    public static final String SHARE_POSITION_TRACK_LAT = SHARE_POSITION_TRACK + "/"+"TrackLat";
    public static final String SHARE_POSITION_TRACK_LNG = SHARE_POSITION_TRACK + "/"+"TrackLng";
    public static final String SHARE_POSITION_TRACK_ACY = SHARE_POSITION_TRACK + "/"+"TrackAcy";
    public static final String SHARE_POSITION_TRACK_PROV = SHARE_POSITION_TRACK + "/"+"TrackProv";
    public static final String SHARE_POSITION_TRACK_SPEED = SHARE_POSITION_TRACK + "/"+"TrackSpeed";

    public static final String POSITION_ACTION = SHARE_PACKAGE_APP + "/" + "Action";
    public static final String PROGRESS_LONGITUDE = POSITION_ACTION + "/" +"Longitude";
    public static final String PROGRESS_LATITUDE = POSITION_ACTION + "/" +"Latitude";
    public static final String PROGRESS_PROV = POSITION_ACTION + "/" +"Prov";
    public static final String PROGRESS_SPEED = POSITION_ACTION + "/" +"Speed";
    public static final String PROGRESS_DISTANCE = POSITION_ACTION + "/" +"Distance";

    public static final String SPEED_ACTION = "com.example.jean.SPEED";
    public static final String DISTANCE_ACTION = "com.example.jean.DISTANCE";

    public static final String GPS_STOPPED = "GpsStopped";

    // Variables para guardar la ultima posicion de la ruta recorrida
    public static final String SHARE_TRACKING = "SmoothComBttTracking";
    public static final String SHARE_TRACKING_LAST_WAYPOINT = "SmoothComBttTrackingLastWaypoint";

    public static int SWEET_PATH_GROUP = 10;


    // Parametros para los calculos de distancia y direccion entre puntos
    public final static Double SHORT_DISTANCE = 40.0;
    public final static Double MEDIUM_DISTANCE = 90.0;

    // Distancia hacia cada parte de ruta(m)
    public final static Double ROAD_DISTANCE = 4.0;

    //public final static Double DELTA_DIRECTION = 20.0;//0.34907; // 20° en radianes
    public final static Double DELTA_DIRECTION = 50.0;//0.8728; // 50° en radianes

    public final static int WAYPOINTS_GROUP = 100;

    public final static Double PI_VALUE = 3.1416;
    public final static Double ROUND_NUMBER = 10000.0;
    public final static Double IN_GRADOS_PARAM = 111195.0;
    public final static Double DIVIDER = 2.0;

    public final static int STATUS_ROUTE_LOCATION_NORMAL = 0;
    public final static int STATUS_ROUTE_LOCATION_IN_PROCESS = 1;
    public final static int STATUS_ROUTE_LOCATION_NAVIGATED = 2;
    public final static int STATUS_ROUTE_LOCATION_REFUSED = 3;

    // Estados para la posicion actual del driver

    public final static int STATUS_DRIVER_FAR_ROUTE = 0; // Alejado de la ruta
    public final static int STATUS_DRIVER_NEAR_WAYPOINT = 1; // Cerca de un waypoint - filtro 1
    public final static int STATUS_DRIVER_SAME_DIRECTION = 2; // En la misma direccion del waypoint - filtro 2
    public final static int STATUS_DRIVER_ON_SECTION = 3; // En la ruta - filtro 1
    public final static int STATUS_DRIVER_ON_ROAD = 4; // Cerca un waypoint con la misma direction
    // de la porcion de ruta pero alejado de la secióm de ruta.
    public final static int STATUS_DRIVER_GO_BACK = 5;*/
}
