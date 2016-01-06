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

    public static int SWEET_PATH_GROUP = 10;

}
