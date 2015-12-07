package smoothcombtt.xpo.com.gpstrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;



import smoothcombtt.xpo.com.gpstrack.TrackingModule.service.TrackingService;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.fragment.MapFragment;


public class MainActivity extends AppCompatActivity {

    private Button act_main_stop;
    private Button act_main_start;
    private FrameLayout container;
    private MapFragment mapFragment;
    private getPositionReceiver getPositionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act_main_start  = (Button)findViewById(R.id.act_main_start);
        act_main_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, TrackingService.class));
            }
        });

        act_main_stop  = (Button)findViewById(R.id.act_main_stop);
        act_main_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(MainActivity.this, TrackingService.class));
            }
        });

        container = (FrameLayout)findViewById(R.id.container);

        mapFragment = new MapFragment();

        getFragmentManager().beginTransaction().replace(R.id.container,mapFragment).commit();

        mapFragment.setInterfaceMapStatus(new MapFragment.InterfaceMapStatus() {
            @Override
            public void getMapStatus(int i) {

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
