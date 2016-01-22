package smoothcombtt.xpo.com.gpstrack.TrackingModule.bean;

/**
 * Created by jose.paucar on 13/01/2016.
 */
public class BERouteLocation extends RouteLocationBean implements Persistable {

    private String latitude;
    private String longitude;
    private Boolean isMandatory;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }
}
