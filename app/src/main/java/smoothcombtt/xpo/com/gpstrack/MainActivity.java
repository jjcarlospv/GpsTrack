package smoothcombtt.xpo.com.gpstrack;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;


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
        intentFilter.addAction(MapFragment.POSITION_ACTION);
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

/*
                AsyncTask<ResultTO, Integer, Void> asyncTask = new AsyncTask<ResultTO, Integer, Void>() {
                    @Override
                    protected Void doInBackground(ResultTO... resultTOs) {

                        ResultTO result = new ResultTO();
                        result = WebServiceFactory.getFactory().getDotNetSoapWebservice(ProxyImpl.class)
                                .authentification("Test","test321","AGHKJKHJJJD");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                    }
                };

                asyncTask.execute(new ResultTO());
*/

                break;
        }
    }

    private void ShowMarkers(){

        Cursor cursor = getContentResolver().query(TrackingProvider.URI_SAVE_POSITION, null, null, null, null);

        if(cursor != null){

            mapFragment.CleanMap();

            while(cursor.moveToNext()){

                Double tempLat = cursor.getDouble(1);
                Double tempLng = cursor.getDouble(2);
                mapFragment.addMarker(tempLat, tempLng);
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
            if (intent.getAction() == MapFragment.POSITION_ACTION) {

                broadCLatitude = intent.getStringExtra(MapFragment.PROGRESS_LATITUDE);
                broadCLongitude = intent.getStringExtra(MapFragment.PROGRESS_LONGITUDE);

                mapFragment.moveTo(Double.valueOf(broadCLatitude), Double.valueOf(broadCLongitude), true);
                mapFragment.addMarker(Double.valueOf(broadCLatitude), Double.valueOf(broadCLongitude));

            }
            Log.e("BROADCAST _CONTENT", "RECEIVED");
        }
    }
}
