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
import android.widget.TextView;


import com.cardiomood.android.controls.gauge.SpeedometerGauge;
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
import smoothcombtt.xpo.com.gpstrack.TrackingModule.common.Polygon;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.provider.TrackingProvider;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.service.TrackingService;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.fragment.MapFragment;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.ProxyImpl;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.WebServiceFactory;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons.Helper;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.tos.ResultTO;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button act_main_stop;
    private Button act_main_start;
    private Button act_main_show_markers;
    private Button act_main_new_ruta;
    private Button act_main_clean_data;
    private TextView act_main_speed;
    private TextView act_main_distance;

    private FrameLayout container;
    private MapFragment mapFragment;
    private getPositionReceiver getPositionReceiver;

    private SpeedometerGauge speedometer;


    // Prueba Geofence
    Polygon fence = new Polygon();

    boolean insideFence = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act_main_start  = (Button)findViewById(R.id.act_main_start);
        act_main_stop  = (Button)findViewById(R.id.act_main_stop);
        act_main_show_markers = (Button)findViewById(R.id.act_main_show_markers);
        act_main_new_ruta  = (Button)findViewById(R.id.act_main_new_ruta);
        act_main_clean_data = (Button)findViewById(R.id.act_main_clean_data);
        act_main_speed = (TextView)findViewById(R.id.act_main_speed);
        act_main_distance = (TextView)findViewById(R.id.act_main_distance);

        act_main_start.setOnClickListener(this);
        act_main_stop.setOnClickListener(this);
        act_main_show_markers.setOnClickListener(this);
        act_main_new_ruta.setOnClickListener(this);
        act_main_clean_data.setOnClickListener(this);


        container = (FrameLayout)findViewById(R.id.container);
        mapFragment = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.container,mapFragment).commit();

        mapFragment.setInterfaceMapStatus(new MapFragment.InterfaceMapStatus() {
            @Override
            public void getMapStatus(int i) {

                switch (i) {
                    case 1:
                        ShowMarkers();
                        ShowGeofence();
                        break;
                }
            }
        });

        speedometer = (SpeedometerGauge)findViewById(R.id.speedometer);
        // configure value range and ticks
        speedometer.setMaxSpeed(300);
        speedometer.setMajorTickStep(30);
        speedometer.setMinorTicks(2);

        // Configure value range colors
        speedometer.addColoredRange(30, 140, Color.GREEN);
        speedometer.addColoredRange(140, 180, Color.YELLOW);
        speedometer.addColoredRange(180, 400, Color.RED);


        /*speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                Log.e("SPEED", String.valueOf(progress));
                return String.valueOf((int) Math.round(progress));
            }
        });*/

    }

    public void ShowGeofence(){
        Double[] geofence = new Double[16];
        Double[] coordinates = new Double[2];

        geofence[0] = -12.119762;
        geofence[1] = -77.036591;
        geofence[2] = -12.119410;
        geofence[3] = -77.036209;
        geofence[4] = -12.118487;
        geofence[5] = -77.035980;
        geofence[6] = -12.118101;
        geofence[7] = -77.036311;
        geofence[8] = -12.118088;
        geofence[9] = -77.037217;
        geofence[10] = -12.118487;
        geofence[11] = -77.037804;
        geofence[12] = -12.119311;
        geofence[13] = -77.037817;
        geofence[14] = -12.119847;
        geofence[15] = -77.037511;

        //coordinates[0] = -12.120695;
        //coordinates[1] = -77.036445;
        //coordinates[0] = -12.119095;
        //coordinates[1] = -77.036994;

        coordinates[0] = -12.118165;
        coordinates[1] = -77.037838;

         insideFence = fence.contains(Helper.toPointArray(geofence), Helper.toPoint(coordinates));


        Double[] poly = new Double[10];
        poly[0] = 9.0;
        poly[1] = 4.0;
        poly[2] = 1.0;
        poly[3] = 2.0;
        poly[4] = 4.0;
        poly[5] = 5.0;
        poly[6] = 2.0;
        poly[7] = 8.0;
        poly[8] = 8.0;
        poly[9] = 9.0;

        Double[] tri = new Double[6];
        tri[0] = 3.0;
        tri[1] = 4.0;
        tri[2] = 7.0;
        tri[3] = 6.0;
        tri[4] = 6.0;
        tri[5] = 1.0;



        Log.e("AREA", String.valueOf(Helper.AreaPolygon(Helper.toPointArray(tri))));
        Helper.isInsideGeofence(Helper.toPointArray(geofence), Helper.toPoint(coordinates));

        for(int i = 0; i<geofence.length-1; i+=2){
            mapFragment.addMarkerWithImageSource(geofence[i], geofence[i+1], R.mipmap.ic_marker_geofence);
        }

        mapFragment.addMarkerWithImageSource(coordinates[0], coordinates[1], R.mipmap.ic_marker_geoposition);
    }

    @Override
    protected void onResume() {
        getPositionReceiver = new getPositionReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GpsConstants.POSITION_ACTION);
        intentFilter.addAction(GpsConstants.GPS_STOPPED);
        intentFilter.addAction(GpsConstants.SPEED_ACTION);
        intentFilter.addAction(GpsConstants.DISTANCE_ACTION);
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
    public void onBackPressed() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        //builder.setTitle(getResources().getString(R.string.push_title));
        builder.setMessage(getResources().getString(R.string.backpressed_message));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.btn_acept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopService(new Intent(MainActivity.this, TrackingService.class));
                finish();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

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

            case R.id.act_main_new_ruta:
                ShowNewRuta();
                break;

            case R.id.act_main_clean_data:
                getContentResolver().delete(TrackingProvider.URI_SAVE_POSITION, null, null);
                ShowMarkers();
                break;
        }
    }

    private void ShowMarkers(){

        Cursor cursor = getContentResolver().query(TrackingProvider.URI_SAVE_POSITION, null, null, null, null);
        final ArrayList<LatLng> latLngsMain = new ArrayList<LatLng>();

        if(cursor != null){

            mapFragment.CleanMap();

            while(cursor.moveToNext()){

                Double tempLat = cursor.getDouble(1);
                Double tempLng = cursor.getDouble(2);
                mapFragment.addMarker(tempLat, tempLng);
                latLngsMain.add(new LatLng(tempLat, tempLng));
            }

            for(int i = 0; i < latLngsMain.size(); i++){
                mapFragment.addMarker(latLngsMain.get(i).latitude, latLngsMain.get(i).longitude);
            }
        }
    }

    private void ShowNewRuta(){
        Cursor cursor = getContentResolver().query(TrackingProvider.URI_SAVE_POSITION, null, null, null, null);
        final ArrayList<LatLng> latLngsMain = new ArrayList<LatLng>();

        if(cursor != null){

            mapFragment.CleanMap();

            while(cursor.moveToNext()){

                Double tempLat = cursor.getDouble(1);
                Double tempLng = cursor.getDouble(2);
                mapFragment.addMarker(tempLat, tempLng);
                latLngsMain.add(new LatLng(tempLat, tempLng));
            }

            if((latLngsMain.size() > 1)&&(latLngsMain != null)){
                DrawRouteWithList(latLngsMain);
                setInterfacePointsList(new InterfacePointsList() {
                    @Override
                    public void getPointsList(List<LatLng> latLngs) {

                        if((latLngs != null)&&(latLngs.size() > 0)){

                            for(int j = 0; j<latLngs.size(); j++){

                                mapFragment.googleMap.addMarker(new MarkerOptions()
                                        .position(latLngs.get(j))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sweet_marker)));
                            }
                        }
                    }
                });
            }

        }

        cursor.close();
    }

    /**
     * Metodo que lee la posición enviada por el Broadcast
     */
    public class getPositionReceiver extends BroadcastReceiver {

        String broadCLatitude;
        String broadCLongitude;
        String broadCSpeed;
        String braodCSpeedAction;
        String braodCDistance;
        int posTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {

            switch(intent.getAction()){

                case GpsConstants.POSITION_ACTION:
                    broadCLatitude = intent.getStringExtra(GpsConstants.PROGRESS_LATITUDE);
                    broadCLongitude = intent.getStringExtra(GpsConstants.PROGRESS_LONGITUDE);
                    //broadCSpeed = intent.getStringExtra(GpsConstants.PROGRESS_SPEED);
                    //mapFragment.moveTo(Double.valueOf(broadCLatitude), Double.valueOf(broadCLongitude), true);
                    mapFragment.addMarker(Double.valueOf(broadCLatitude), Double.valueOf(broadCLongitude));

                    //if(speedometer != null){
                    //    speedometer.setSpeed(Double.valueOf(broadCSpeed),true);
                    //}

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

                case GpsConstants.SPEED_ACTION:
                    braodCSpeedAction = intent.getStringExtra(GpsConstants.PROGRESS_SPEED);
                    act_main_speed.setText(braodCSpeedAction);
                    break;

                case GpsConstants.DISTANCE_ACTION:
                    braodCDistance = intent.getStringExtra(GpsConstants.PROGRESS_DISTANCE);
                    act_main_distance.setText(braodCDistance);
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
    private ArrayList<LatLng> tempLatLngList;

    private static int rest = 0;
    private static int groups = 0;
    private Routing routing;
    private ProgressDialog dialog;

    public void DrawRouteWithList(final ArrayList<LatLng> latLngs ) {

     /*  final ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(-12.121029, -77.036852));
        latLngs.add(new LatLng(-12.120748, -77.037030));
        latLngs.add(new LatLng(-12.120249, -77.036852));
        latLngs.add(new LatLng(-12.119207, -77.036839));
        latLngs.add(new LatLng(-12.116800, -77.035665));
        latLngs.add(new LatLng(-12.115914, -77.032909));
        latLngs.add(new LatLng(-12.113999, -77.032361));
        latLngs.add(new LatLng(-12.112677, -77.029885));
*/

        final int groupEl = GpsConstants.SWEET_PATH_GROUP;
        final Double elements = Double.valueOf(groupEl);

        rest = (latLngs.size())% groupEl;
        groups = (latLngs.size()- rest)/groupEl;



        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Por favor espere ...");
        dialog.setCancelable(false);
        dialog.show();

        tempPoints = new ArrayList<LatLng>();

        for(int g = 0; g < groups; g++){

            tempLatLngList = new ArrayList<LatLng>();

            for(int item = groupEl*g; item < groupEl* (g +1); item ++){
                tempLatLngList.add(latLngs.get(item));
            }

            if((tempLatLngList != null) && (tempLatLngList.size() > 1)){
                routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .optimize(true)
                        .withListener(routingListener)
                        .waypoints(tempLatLngList)
                        .build();

                routing.execute();
            }
        }

        tempLatLngList = new ArrayList<LatLng>();

        for(int g = groupEl*groups; g < groupEl*groups + rest; g++){
            tempLatLngList.add(latLngs.get(g));
        }

        if((tempLatLngList != null) && (tempLatLngList.size() > 1)){
            routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(routingListener)
                    .waypoints(tempLatLngList)
                    .build();

            routing.execute();
        }
    }

    private RoutingListener routingListener = new RoutingListener() {
        @Override
        public void onRoutingFailure() {

        }

        @Override
        public void onRoutingStart() {

        }

        @Override
        public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

/*
                            newPolylines = new ArrayList<Polyline>();
                          mapFragment.googleMap.clear();
                            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                            boundsBuilder.include(tempLatLngList.get(0));
                            boundsBuilder.include(tempLatLngList.get(tempLatLngList.size()-1));

                            LatLngBounds bounds = boundsBuilder.build();
                            //mapFragment.googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));

                            MarkerOptions options = new MarkerOptions();
                            options.position(latLngs.get(0));
                            options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
                            tempOrigin = mapFragment.googleMap.addMarker(options);

                            // End marker
                            options = new MarkerOptions();
                            options.position(tempLatLngList.get(latLngs.size()-1));
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


                                tempPoints = arrayList.get(ii).getPoints();

                                Polyline polyline = mapFragment.googleMap.addPolyline(polyOptions);
                                newPolylines.add(polyline);
                            }
                            finishGroup = true;
*/

            for (int ii = 0; ii < arrayList.size(); ii++) {

                tempPoints = arrayList.get(ii).getPoints();
            }

            interfacePointsList.getPointsList(tempPoints);
            if(dialog != null){
                dialog.dismiss();
            }

        }

        @Override
        public void onRoutingCancelled() {

        }
    };


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
