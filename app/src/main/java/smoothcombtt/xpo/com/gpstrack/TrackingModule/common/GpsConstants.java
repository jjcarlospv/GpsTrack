package smoothcombtt.xpo.com.gpstrack.TrackingModule.common;

/**
 * Created by jose.paucar on 14/12/2015.
 */
public class GpsConstants {

    // Flags para guardar las coordenadas
    public static final String SHARE_POSITION_TRACK = "smoothcombtt.xpo.com.gpstrack";
    public static final String SHARE_POSITION_TRACK_GPS = SHARE_POSITION_TRACK + "/"+"TrackGps";
    public static final String SHARE_POSITION_TRACK_LAT = SHARE_POSITION_TRACK + "/"+"TrackLat";
    public static final String SHARE_POSITION_TRACK_LNG = SHARE_POSITION_TRACK + "/"+"TrackLng";
    public static final String SHARE_POSITION_TRACK_ACY = SHARE_POSITION_TRACK + "/"+"TrackAcy";
    public static final String SHARE_POSITION_TRACK_PROV = SHARE_POSITION_TRACK + "/"+"TrackProv";
    public static final String SHARE_POSITION_TRACK_SPEED = SHARE_POSITION_TRACK + "/"+"TrackSpeed";

    public static final String POSITION_ACTION = "com.example.jean.POSITION";
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
    public final static Double ROAD_DISTANCE = 5.0;

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

}
