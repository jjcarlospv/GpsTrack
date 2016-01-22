package smoothcombtt.xpo.com.gpstrack.TrackingModule.bean;



/**
 * Created by jose.paucar on 22/01/2016.
 */
public class BEResponseRoute {

    private int waypointStatus;
    private int inProgressWaypoint;
    private BERouteDetails[] beRouteDetailses;
    private String message;
    private BECurrentLocation beCurrentLocation;


    public int getWaypointStatus() {
        return waypointStatus;
    }

    public void setWaypointStatus(int waypointStatus) {
        this.waypointStatus = waypointStatus;
    }

    public int getInProgressWaypoint() {
        return inProgressWaypoint;
    }

    public void setInProgressWaypoint(int inProgressWaypoint) {
        this.inProgressWaypoint = inProgressWaypoint;
    }

    public BERouteDetails[] getBeRouteDetailses() {
        return beRouteDetailses;
    }

    public void setBeRouteDetailses(BERouteDetails[] beRouteDetailses) {
        this.beRouteDetailses = beRouteDetailses;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BECurrentLocation getBeCurrentLocation() {
        return beCurrentLocation;
    }

    public void setBeCurrentLocation(BECurrentLocation beCurrentLocation) {
        this.beCurrentLocation = beCurrentLocation;
    }
}
