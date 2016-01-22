package smoothcombtt.xpo.com.gpstrack.TrackingModule.bean;

/**
 * Created by jose.paucar on 13/01/2016.
 */
public class RouteLocationBean implements Persistable {


    public static final String SHORT_GROUP = "ShortGroup";
    public static final String LONG_GROUP = "LongGroup";

    private String name;
    private String address;
    private Double distanceNextPoint;
    private Double directionNextPoint;
    private Double[] geofence;
    private Integer status;
    private String groupType;
    private Integer groupNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getDistanceNextPoint() {
        return distanceNextPoint;
    }

    public void setDistanceNextPoint(Double distanceNextPoint) {
        this.distanceNextPoint = distanceNextPoint;
    }

    public Double getDirectionNextPoint() {
        return directionNextPoint;
    }

    public void setDirectionNextPoint(Double directionNextPoint) {
        this.directionNextPoint = directionNextPoint;
    }

    public Double[] getGeofence() {
        return geofence;
    }

    public void setGeofence(Double[] geofence) {
        this.geofence = geofence;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }
}
