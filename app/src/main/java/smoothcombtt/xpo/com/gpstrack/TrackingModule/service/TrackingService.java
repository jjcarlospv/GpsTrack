package smoothcombtt.xpo.com.gpstrack.TrackingModule.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import smoothcombtt.xpo.com.gpstrack.TrackingModule.common.GpsConstants;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.database.DataBaseHelper;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.fragment.MapFragment;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.provider.TrackingProvider;

/**
 * Created by jose.paucar on 02/12/2015.
 */
public class TrackingService extends Service {

    private static final int ONE_MINUTES = 1000 * 60 * 1;
    private static final int INIT_TIME = 5000;
    private static final int SAMPLE_TIME = 1000 * 30;
    private static final int GET_INFO_TIME = 100 * 35;

    private static final int MULTIPLICATOR = 100000000;
    private static final Double DIVIDER = 100000000.0;

    private LocationManager locationManager;
    private CustomLocationListener customLocationListener;
    private CustomSensorEventListener customSensorEventListener;
    private Location currentLocation;
    private Location newLocation;
    private ArrayList<Location> arrayLocations;
    private int timesLoc = 0;
    private Double tempLat = 0.0;
    private Double tempLng = 0.0;

    private Handler handlerSampleLocation;
    private Runnable runnableSampleLocation;

    private Handler handlerGetDataPeriod;
    private Runnable runnableGetDataPeriod;

    private SensorManager sensorManager;

    private boolean firstTime = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        handlerSampleLocation = new Handler();
        handlerGetDataPeriod = new Handler();
        startAccelerometer(); // Iniciamos el Sensor Manager
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        arrayLocations = new ArrayList<Location>();
        currentLocation = new Location(LocationManager.GPS_PROVIDER);
        newLocation = new Location(LocationManager.GPS_PROVIDER);


        runnableSampleLocation = new Runnable() {
            @Override
            public void run() {
                Log.e("SamplePeriod", "Stop");
                handlerSampleLocation.removeCallbacks(runnableSampleLocation);

                firstTime = true;
                StartlocationService();

                handlerGetDataPeriod.postDelayed(runnableGetDataPeriod, GET_INFO_TIME);
                Log.e("GetDataPeriod", "Start");
            }
        };

        runnableGetDataPeriod = new Runnable() {
            @Override
            public void run() {
                Log.e("GetDataPeriod", "Stop");
                handlerGetDataPeriod.removeCallbacks(runnableGetDataPeriod);

                StopLocationService();
                SaveGoodPosition();

                handlerSampleLocation.postDelayed(runnableSampleLocation, SAMPLE_TIME);
                Log.e("SamplePeriod", "Start");
            }
        };

        firstTime = true;
        StartlocationService();
        handlerGetDataPeriod.postDelayed(runnableGetDataPeriod, GET_INFO_TIME);
        Log.e("GetDataPeriod", "Start");

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isMoving = false;

        if (handlerSampleLocation != null) {
            handlerSampleLocation.removeCallbacks(runnableSampleLocation);

            handlerSampleLocation = null;
            runnableSampleLocation = null;
            Log.e("GetSampleLocation", "Stop - Destroy");
        }

        if (handlerGetDataPeriod != null) {
            handlerGetDataPeriod.removeCallbacks(runnableGetDataPeriod);
            handlerGetDataPeriod = null;
            runnableGetDataPeriod = null;
            Log.e("GetDataPeriod", "Stop - Destroy");
        }

        // Reset de variables utilizadas
        timesLoc = 0;
        arrayLocations = null;
        tempLat = 0.0;
        tempLng = 0.0;
        StopLocationService();
        stopAccelerometer();


    }

    private class CustomLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            if(firstTime){
                currentLocation = location;
                SaveLocation(location);
                firstTime = false;
            }

            if (isBetterLocation(location, currentLocation)) {

                if (isNearToNextPosition(currentLocation, location)) {

                    if (arrayLocations != null) {

                        arrayLocations.add(location);
                        timesLoc++;
                        Log.e("Cuantity", String.valueOf(timesLoc));
                        Log.e("Distance", "NEAR");

                    } else {
                        arrayLocations = new ArrayList<Location>();
                    }

                } else {
                    /*
                    if ((currentLocation.getLatitude() == 0.0) || (currentLocation.getLongitude() == 0.0)) {
                        currentLocation.setLatitude(location.getLatitude());
                        currentLocation.setLongitude(location.getLongitude());
                        Log.e("SetCurrLoc", String.valueOf(currentLocation.getLatitude()) + "," + String.valueOf(currentLocation.getLongitude()));
                    }*/

                    Log.e("Distance", "FAR");
                    Log.e("LocRead", String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
                }


            } else {
                Log.e("DiscardLoc", String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
             /*Notification.messageError(getApplicationContext(),"Provider",s, false, null);

            if(s.equals("gps")){
                Toast.makeText(getApplicationContext(), "GPS is off", Toast.LENGTH_LONG).show();

                //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
            Log.i("lm_disabled", s);


           if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                //Notification.messageError(null,"Provider","Gps Disable", false, null);
            }
            else {
                if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    //Notification.messageError(null,"Provider","Network Provider", false, null);
                }
            }*/
        }
    }


    /**
     * Metodos para capturar el estado del sensor
     */
    private void startAccelerometer() {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        customSensorEventListener = new CustomSensorEventListener();
        sensorManager.registerListener(customSensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

    }

    private void stopAccelerometer() {

        if (customSensorEventListener != null) {
            sensorManager.unregisterListener(customSensorEventListener);
        }
    }

    static float tempX = 0;
    static float tempY = 0;
    static float tempZ = 0;

    static boolean isMoving = false;

    private class CustomSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            if ((Math.abs(tempX - sensorEvent.values[0]) > 0.5) || (Math.abs(tempY - sensorEvent.values[1]) > 0.5) || (Math.abs(tempZ - sensorEvent.values[2]) > 0.5)) {
                tempX = sensorEvent.values[0];
                tempY = sensorEvent.values[1];
                tempZ = sensorEvent.values[2];

                Log.e("X:", "X:" + String.valueOf(tempX) + "/" +
                        "Y:" + String.valueOf(tempY) + "/" +
                        "Z:" + String.valueOf(tempZ));
                isMoving = true;

            } else {
                isMoving = false;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }


    /**
     * Metodo para almacenar una posicion aceptable
     */

    private void SaveGoodPosition() {

        Double tempSaveLastLat = 0.0;
        Double tempSaveLastLng = 0.0;
        Double deltaLat = 0.0;
        Double DeltaLng = 0.0;

        if ((arrayLocations != null) && (timesLoc > 0)) {

            tempLat = 0.0;
            tempLng = 0.0;

            for (int i = 0; i < arrayLocations.size(); i++) {

                tempLat = tempLat + arrayLocations.get(i).getLatitude();
                tempLng = tempLng + arrayLocations.get(i).getLongitude();
            }

            tempLat = tempLat / arrayLocations.size();
            tempLng = tempLng / arrayLocations.size();

            tempLat = Math.round(tempLat * MULTIPLICATOR) / DIVIDER;
            tempLng = Math.round(tempLng * MULTIPLICATOR) / DIVIDER;

            newLocation.setLatitude(tempLat);
            newLocation.setLongitude(tempLng);

            // Guardamos si el punto es diferente al anterior guardado


            tempSaveLastLat = Double.valueOf(getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                    .getString(GpsConstants.SHARE_POSITION_TRACK_LAT, "0.0"));

            tempSaveLastLng = Double.valueOf(getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                    .getString(GpsConstants.SHARE_POSITION_TRACK_LNG, "0.0"));

            deltaLat = Math.abs(newLocation.getLatitude() - tempSaveLastLat);
            DeltaLng = Math.abs(newLocation.getLongitude() - tempSaveLastLng);

            if ((deltaLat > 0.00001) && (DeltaLng > 0.00001)) {

                if(isMoving){
                    Log.e("isMoving", "TRUE");
                    SaveLocation(newLocation);
                }
                else{
                    Log.e("isMoving", "FALSE");
                }
            }

            timesLoc = 0;
            arrayLocations = null;

        }
    }

    /**
     * Metodo para iniciar el servicio de Localizacion
     */

    private void StartlocationService() {

        Log.e("LocService", "StartService");
        customLocationListener = new CustomLocationListener();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, customLocationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, customLocationListener);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, customLocationListener);
    }

    /**
     * Metodo para cancelar el servicio de Localizacion
     */
    private void StopLocationService() {
        if (locationManager != null) {
            Log.e("LocService", "StopService");
            locationManager.removeUpdates(customLocationListener);
            locationManager = null;
        }
    }

    /**
     * Metodo para guardar la position en base de datos
     *
     * @param newLocation
     * @return
     */
    private Location SaveLocation(Location newLocation) {

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String dateString = df.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.LATITUDE, newLocation.getLatitude());
        contentValues.put(DataBaseHelper.LONGITUDE, newLocation.getLongitude());
        contentValues.put(DataBaseHelper.DATE, tempLng);
        getContentResolver().insert(TrackingProvider.URI_SAVE_POSITION, contentValues);

        // Guardamos la última posición en variables

        getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                .edit().putString(GpsConstants.SHARE_POSITION_TRACK_LAT, String.valueOf(newLocation.getLatitude())).commit();

        getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                .edit().putString(GpsConstants.SHARE_POSITION_TRACK_LNG, String.valueOf(newLocation.getLongitude())).commit();


        Intent intentTestService = new Intent(MapFragment.POSITION_ACTION);
        intentTestService.putExtra(MapFragment.PROGRESS_LATITUDE, String.valueOf(newLocation.getLatitude()));
        intentTestService.putExtra(MapFragment.PROGRESS_LONGITUDE, String.valueOf(newLocation.getLongitude()));
        intentTestService.addFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intentTestService);

        Log.e("Best", String.valueOf(newLocation.getLatitude()) + "," + String.valueOf(newLocation.getLongitude()) + "//" + dateString);

        return newLocation;
    }

    /**
     * Métodos para conseguir una mejor posicion
     *
     * @param location
     * @param currentBestLocation
     * @return
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > ONE_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -ONE_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 100;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     *
     * @param provider1
     * @param provider2
     * @return
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /**
     * Metodo para hacer evaluar el alejamiento de dos posiciones
     *
     * @param origin
     * @param destino
     * @return
     */

    private boolean isNearToNextPosition(Location origin, Location destino) {

        Double tempOriginLat = 0.0;
        Double tempOriginLng = 0.0;
        Double tempDestinoLat = 0.0;
        Double tempDestinoLng = 0.0;

        Double deltaLat = 0.0;
        Double deltaLng = 0.0;

        tempOriginLat = Math.round(origin.getLatitude() * MULTIPLICATOR) / DIVIDER;
        tempOriginLng = Math.round(origin.getLongitude() * MULTIPLICATOR) / DIVIDER;

        tempDestinoLat = Math.round(destino.getLatitude() * MULTIPLICATOR) / DIVIDER;
        tempDestinoLng = Math.round(destino.getLongitude() * MULTIPLICATOR) / DIVIDER;

        deltaLat = Math.abs(tempOriginLat - tempDestinoLat);
        deltaLng = Math.abs(tempOriginLng - tempDestinoLng);

        if ((1E-5 > deltaLat) || (deltaLng < 1E-5)) {
            return true;
        }

        return false;
    }
}
