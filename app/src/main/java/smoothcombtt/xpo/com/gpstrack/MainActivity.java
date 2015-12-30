package smoothcombtt.xpo.com.gpstrack;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;


import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import smoothcombtt.xpo.com.gpstrack.TrackingModule.common.GpsConstants;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.provider.TrackingProvider;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.service.TrackingService;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.fragment.MapFragment;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.ProxyImpl;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.WebServiceFactory;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.tos.ResultTO;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button act_main_stop;
    private Button act_main_start;
    private Button act_main_show_markers;
    private Button act_main_clean_data;

    private FrameLayout container;
    private MapFragment mapFragment;
    private getPositionReceiver getPositionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act_main_start  = (Button)findViewById(R.id.act_main_start);
        act_main_stop  = (Button)findViewById(R.id.act_main_stop);
        act_main_show_markers = (Button)findViewById(R.id.act_main_show_markers);
        act_main_clean_data = (Button)findViewById(R.id.act_main_clean_data);

        act_main_start.setOnClickListener(this);
        act_main_stop.setOnClickListener(this);
        act_main_show_markers.setOnClickListener(this);
        act_main_clean_data.setOnClickListener(this);


        container = (FrameLayout)findViewById(R.id.container);
        mapFragment = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.container,mapFragment).commit();

        mapFragment.setInterfaceMapStatus(new MapFragment.InterfaceMapStatus() {
            @Override
            public void getMapStatus(int i) {

                    switch(i){
                        case 1:
                            ShowMarkers();
                            break;
                    }
            }
        });
    }

    @Override
    protected void onResume() {
        getPositionReceiver = new getPositionReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GpsConstants.POSITION_ACTION);
        intentFilter.addAction(GpsConstants.GPS_STOPPED);
        registerReceiver(getPositionReceiver, intentFilter);
        Log.e("BROADCAST_FRAGM", "REGISTER");
        super.onResume();
    }

    @Override
    protected void onPause() {
        //unregisterReceiver(getPositionReceiver);
        //Log.e("BROADCAST_FRAGM", "UNREGISTER");
        super.onPause();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.act_main_start:
                //startService(new Intent(MainActivity.this, TrackingService.class));

                final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                    startService(new Intent(MainActivity.this, TrackingService.class));
                }
                else{
                    AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                    final AlertDialog create = b.create();
                    create.setTitle(getResources().getString(R.string.GPS_error_title));
                    create.setCancelable(true);
                    create.setMessage(getResources().getString(R.string.GPS_error));
                    create.setButton(getResources().getString(R.string.lblOK),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    create.cancel();
                                }
                            });
                    create.show();
                }

                break;

            case R.id.act_main_stop:

                stopService(new Intent(MainActivity.this, TrackingService.class));

                break;

            case R.id.act_main_show_markers:
                ShowMarkers();
                break;

            case R.id.act_main_clean_data:
                getContentResolver().delete(TrackingProvider.URI_SAVE_POSITION, null, null);
                ShowMarkers();
                break;
        }
    }

    private void ShowMarkers(){

        Cursor cursor = getContentResolver().query(TrackingProvider.URI_SAVE_POSITION, null, null, null, null);
        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();

        if(cursor != null){

            mapFragment.CleanMap();

            while(cursor.moveToNext()){

                Double tempLat = cursor.getDouble(1);
                Double tempLng = cursor.getDouble(2);
                mapFragment.addMarker(tempLat, tempLng);
                latLngs.add(new LatLng(tempLat, tempLng));
            }

            if(latLngs.size() > 2){
                DrawRouteWithList(latLngs);
                setInterfacePointsList(new InterfacePointsList() {
                    @Override
                    public void getPointsList(List<LatLng> latLngs) {
                        Log.e("ArrayList", latLngs.toString());
                    }
                });
            }

        }
    }

    /**
     * Metodo que lee la posición enviada por el Broadcast
     */
    public class getPositionReceiver extends BroadcastReceiver {

        String broadCLatitude;
        String broadCLongitude;
        int posTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {

            switch(intent.getAction()){

                case GpsConstants.POSITION_ACTION:
                    broadCLatitude = intent.getStringExtra(GpsConstants.PROGRESS_LATITUDE);
                    broadCLongitude = intent.getStringExtra(GpsConstants.PROGRESS_LONGITUDE);

                    //mapFragment.moveTo(Double.valueOf(broadCLatitude), Double.valueOf(broadCLongitude), true);
                    mapFragment.addMarker(Double.valueOf(broadCLatitude), Double.valueOf(broadCLongitude));
                    break;

                case GpsConstants.GPS_STOPPED:

                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                        final AlertDialog create = b.create();
                        create.setTitle(getResources().getString(R.string.GPS_error_title));
                        create.setCancelable(true);
                        create.setMessage(getResources().getString(R.string.GPS_error));
                        create.setButton(getResources().getString(R.string.lblOK),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        create.cancel();
                                    }
                                });
                        create.show();


                    break;
            }

            Log.e("BROADCAST _CONTENT", "RECEIVED");
        }
    }



    private ArrayList<Polyline> polylines;
    ArrayList<Polyline> newPolylines;

    private Marker tempOrigin;
    private Marker tempDestino;
    List<LatLng> tempPoints;

    public void DrawRouteWithList(final ArrayList<LatLng> latLngs2 ) {

       final ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(-12.121029, -77.036852));
        latLngs.add(new LatLng(-12.120748, -77.037030));
        latLngs.add(new LatLng(-12.120249, -77.036852));
        latLngs.add(new LatLng(-12.119207, -77.036839));
        latLngs.add(new LatLng(-12.116800, -77.035665));
        latLngs.add(new LatLng(-12.115914, -77.032909));
        latLngs.add(new LatLng(-12.113999, -77.032361));
        latLngs.add(new LatLng(-12.112677, -77.029885));


        final ProgressDialog dialog;
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Por favor espere ...");
        dialog.show();

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(new RoutingListener() {
                    @Override
                    public void onRoutingFailure() {
                    }

                    @Override
                    public void onRoutingStart() {
                    }

                    @Override
                    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

                        mapFragment.googleMap.clear();
                        newPolylines = new ArrayList<Polyline>();

                        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                        boundsBuilder.include(latLngs.get(0));
                        boundsBuilder.include(latLngs.get(latLngs.size()-1));

                        LatLngBounds bounds = boundsBuilder.build();
                        mapFragment.googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));

                        MarkerOptions options = new MarkerOptions();
                        options.position(latLngs.get(0));
                        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
                        tempOrigin = mapFragment.googleMap.addMarker(options);

                        // End marker
                        options = new MarkerOptions();
                        options.position(latLngs.get(latLngs.size()-1));
                        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                        tempDestino = mapFragment.googleMap.addMarker(options);

                        if (newPolylines.size() > 0) {
                            for (Polyline poly : polylines) {
                                poly.remove();
                            }
                        }

                        newPolylines = new ArrayList<>();
                        //add route(s) to the map.
                        for (int ii = 0; ii < arrayList.size(); ii++) {

                            PolylineOptions polyOptions = new PolylineOptions();
                            polyOptions.color(Color.rgb(150, 40, 27));
                            polyOptions.width(9);
                            polyOptions.addAll(arrayList.get(ii).getPoints());

                            tempPoints = new ArrayList<LatLng>();
                            tempPoints = arrayList.get(ii).getPoints();

                            Polyline polyline = mapFragment.googleMap.addPolyline(polyOptions);
                            newPolylines.add(polyline);
                        }
                        interfacePointsList.getPointsList(tempPoints);
                        dialog.dismiss();
                    }

                    @Override
                    public void onRoutingCancelled() {
                        dialog.dismiss();
                    }
                })
                .waypoints(latLngs)
                .build();
        routing.execute();

    }


    /**
     * Interface
     */

    private InterfacePointsList interfacePointsList;

    public interface InterfacePointsList{
        void getPointsList(List<LatLng> latLngs);
    }

    public InterfacePointsList getInterfacePointsList() {
        return interfacePointsList;
    }

    public void setInterfacePointsList(InterfacePointsList interfacePointsList) {
        this.interfacePointsList = interfacePointsList;
    }
}
