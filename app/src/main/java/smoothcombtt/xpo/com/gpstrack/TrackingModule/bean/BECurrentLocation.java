package smoothcombtt.xpo.com.gpstrack.TrackingModule.bean;

/**
 * Created by jose.paucar on 13/01/2016.
 */
public class BECurrentLocation implements Persistable {

    private Double latitude;
    private Double longitude;
    private Double direccion;
    private Double distance;
    private Double geofenceWidth;
    private Double geofenceHeight;
    private int status;


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDireccion() {
        return direccion;
    }

    public void setDireccion(Double direccion) {
        this.direccion = direccion;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getGeofenceWidth() {
        return geofenceWidth;
    }

    public void setGeofenceWidth(Double geofenceWidth) {
        this.geofenceWidth = geofenceWidth;
    }

    public Double getGeofenceHeight() {
        return geofenceHeight;
    }

    public void setGeofenceHeight(Double geofenceHeight) {
        this.geofenceHeight = geofenceHeight;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
