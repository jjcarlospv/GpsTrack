package smoothcombtt.xpo.com.gpstrack.TrackingModule.fragment;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import smoothcombtt.xpo.com.gpstrack.R;

/**
 * Created by jose.paucar on 02/12/2015.
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMyLocationButtonClickListener {


    public static final String POSITION_ACTION = "com.example.jean.POSITION";
    public static final String PROGRESS_LONGITUDE = "longitude";
    public static final String PROGRESS_LATITUDE = "latitude";

    private GoogleMap googleMap;
    private MapFragmentInterface mapFragmentInterface;
    private static boolean currPos = true;
    private InterfaceMapStatus interfaceMapStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
        Log.e("MapFragment", "onCreate");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationChangeListener(this);


        interfaceMapStatus.getMapStatus(1);

    }


    /*
    En este laboratorio no vamos a utilizar este método, sin embargo
    sirve para mover el mapa hacia unas coordenadas deseadas.
     */
    public void moveTo(final double latitude, final double longitude, final boolean animate) {
        final LatLng latLng = new LatLng(latitude, longitude);
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(13)
                .build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        if (animate) {
            googleMap.animateCamera(cameraUpdate);
        } else {
            googleMap.moveCamera(cameraUpdate);
        }
    }

    public Marker addMarker(double lat, double lng){

        LatLng latLng1 = new LatLng(lat,lng);

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng1)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
        return marker;
    }

    public void drawLine(final LatLng origin, final LatLng destination){

        Polyline line = googleMap.addPolyline(new PolylineOptions().
                add(origin, destination)
                .width(10).color(Color.rgb(150, 40, 27)));
    }

    public void setMapFragmentInterface(MapFragmentInterface mapFragmentInterface) {
        this.mapFragmentInterface = mapFragmentInterface;
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (mapFragmentInterface != null) {

            mapFragmentInterface.onLocationChange(location);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    public static interface MapFragmentInterface {

        public void onLocationChange(Location location);

    }

    public interface InterfaceMapStatus{
        void getMapStatus(int i);
    }

    public void setInterfaceMapStatus(InterfaceMapStatus interfaceMapStatus){
        this.interfaceMapStatus = interfaceMapStatus;
    }
}
