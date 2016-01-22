package smoothcombtt.xpo.com.gpstrack.TrackingModule.bean;


import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapField;

/**
 * Created by jose.paucar on 13/01/2016.
 */
public class BERouteDetails extends RouteLocationBean implements Persistable {

    private Integer routePointId;
    private String latitude;
    private String longitude;
    private Integer order;
    private Boolean isWaypoint;
    private Integer routeId;


    public Integer getRoutePointId() {
        return routePointId;
    }

    @KSoapField("RoutePointId")
    public void setRoutePointId(Integer routePointId) {
        this.routePointId = routePointId;
    }

    public String getLatitude() {
        return latitude;
    }

    @KSoapField("Latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @KSoapField("Longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getOrder() {
        return order;
    }

    @KSoapField("Order")
    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getIsWaypoint() {
        return isWaypoint;
    }

    @KSoapField("IsWaypoint")
    public void setIsWaypoint(Boolean isWaypoint) {
        this.isWaypoint = isWaypoint;
    }

    public Integer getRouteId() {
        return routeId;
    }

    @KSoapField("RouteId")
    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }
}
