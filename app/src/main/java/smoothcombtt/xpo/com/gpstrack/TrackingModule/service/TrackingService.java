package smoothcombtt.xpo.com.gpstrack.TrackingModule.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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
    private static final int SAMPLE_TIME = 1000 * 15;
    private static final int GET_INFO_TIME = 1000 * 4;

    private static final int MULTIPLICATOR = 100000000;
    private static final Double DIVIDER = 100000000.0;

    private LocationManager locationManager;

    private CustomSensorEventListener customSensorEventListener;
    private static Location currentLocation = null;
    private static Location newLocation;
    private static Location currentNETLocation = null;
    private static Location newNETLocation;
    private ArrayList<Location> arrayLocations;
    private ArrayList<Location> arrayNETLocations;

    private int timesLoc = 0;
    private Double tempLat = 0.0;
    private Double tempLng = 0.0;

    private Handler handlerSampleLocation;
    private Runnable runnableSampleLocation;

    private Handler handlerGetDataPeriod;
    private Runnable runnableGetDataPeriod;

    private SensorManager sensorManager;

    private boolean firstTime = false;
    private boolean firstTimeNetwork = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handlerSampleLocation = new Handler();
        handlerGetDataPeriod = new Handler();
        startAccelerometer(); // Iniciamos el Sensor Manager

        arrayLocations = new ArrayList<Location>();
        arrayNETLocations = new ArrayList<Location>();
        currentLocation = new Location(LocationManager.GPS_PROVIDER);
        newLocation = new Location(LocationManager.GPS_PROVIDER);


        runnableSampleLocation = new Runnable() {
            @Override
            public void run() {
                Log.e("SamplePeriod", "Stop");
                //handlerSampleLocation.removeCallbacks(runnableSampleLocation);

                StartlocationService();

                handlerGetDataPeriod.postDelayed(runnableGetDataPeriod, GET_INFO_TIME);
                Log.e("GetDataPeriod", "Start");
            }
        };

        runnableGetDataPeriod = new Runnable() {
            @Override
            public void run() {
                Log.e("GetDataPeriod", "Stop");
                //handlerGetDataPeriod.removeCallbacks(runnableGetDataPeriod);
                StopLocationService();

                handlerSampleLocation.postDelayed(runnableSampleLocation, SAMPLE_TIME);
                Log.e("SamplePeriod", "Start");
            }
        };

        firstTime = true;
        firstTimeNetwork = true;
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

            Log.e("GetSampleLocation", "Stop - Destroy");
        }

        if (handlerGetDataPeriod != null) {

            Log.e("GetDataPeriod", "Stop - Destroy");
        }

        handlerSampleLocation.removeCallbacks(runnableSampleLocation);
        handlerGetDataPeriod.removeCallbacks(runnableGetDataPeriod);

        StopLocationService();
        // Reset de variables utilizadas
        timesLoc = 0;
        arrayLocations = null;
        tempLat = 0.0;
        tempLng = 0.0;
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

    private LocationListener locationListenerNetwork = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (firstTimeNetwork) {
                currentNETLocation = location;
                SaveLocation(location);
                firstTimeNetwork = false;
            }

            if (isBetterLocation(location, currentNETLocation)) {

                if (isNearToNextPosition(currentNETLocation, location)) {

                    Double tempSaveLastLat = 0.0;
                    Double tempSaveLastLng = 0.0;
                    Double deltaLat = 0.0;
                    Double deltaLng = 0.0;

                    tempSaveLastLat = Double.valueOf(getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                            .getString(GpsConstants.SHARE_POSITION_TRACK_LAT, "0.0"));

                    tempSaveLastLng = Double.valueOf(getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                            .getString(GpsConstants.SHARE_POSITION_TRACK_LNG, "0.0"));

                    deltaLat = Math.abs(location.getLatitude() - tempSaveLastLat);
                    deltaLng = Math.abs(location.getLongitude() - tempSaveLastLng);

                    if ((deltaLat > 0.00002) || (deltaLng > 0.00002)) {

                        if (isMoving) {
                            Log.e("isMoving", "TRUE");
                            currentNETLocation = location;
                            SaveLocation(location);
                            Log.e("NETWORK", String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "/" + String.valueOf(location.getAccuracy()));
                        } else {
                            Log.e("isMoving", "FALSE");
                        }
                    }

                } else {
                    Log.e("Distance", "NETWORK" + "/" + "FAR:" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "/" + String.valueOf(location.getAccuracy()));
                }

            } else {
                Log.e("DiscardLoc", "NETWORK" + "/" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "/" + String.valueOf(location.getAccuracy()));
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

        }
    };

    private LocationListener locationListenerGps = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (firstTime) {
                currentLocation = location;
                SaveLocation(location);
                firstTime = false;
            }

            if (isBetterLocation(location, currentLocation)) {

                if (isNearToNextPosition(currentLocation, location)) {

                    Double tempSaveLastLat = 0.0;
                    Double tempSaveLastLng = 0.0;
                    Double deltaLat = 0.0;
                    Double deltaLng = 0.0;

                    tempSaveLastLat = Double.valueOf(getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                            .getString(GpsConstants.SHARE_POSITION_TRACK_LAT, "0.0"));

                    tempSaveLastLng = Double.valueOf(getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                            .getString(GpsConstants.SHARE_POSITION_TRACK_LNG, "0.0"));

                    deltaLat = Math.abs(location.getLatitude() - tempSaveLastLat);
                    deltaLng = Math.abs(location.getLongitude() - tempSaveLastLng);

                    if ((deltaLat > 0.00001) || (deltaLng > 0.00001)) {

                        if (isMoving) {
                            Log.e("isMoving", "TRUE");
                            currentLocation = location;
                            SaveLocation(location);
                            Log.e("GPS", String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "/" + String.valueOf(location.getAccuracy()));
                        } else {
                            Log.e("isMoving", "FALSE");
                        }
                    }


                } else {
                    Log.e("Distance", "GPS" + "/" + "FAR:" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "/" + String.valueOf(location.getAccuracy()));
                }

            } else {
                Log.e("DiscardLoc", "GPS" + "/" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "/" + String.valueOf(location.getAccuracy()));
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

        }
    };

    /**
     * Metodo para iniciar el servicio de Localizacion
     */
    private void StartlocationService() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //locationManager.getProvider(locationManager.getBestProvider(createCoarseCriteria(), true));

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        Log.e("LocService", "StartService");
    }

    /**
     * Criteria para escoger un proveedor
     *
     * @return
     */
    public static Criteria createCoarseCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;
    }

    /**
     * this criteria needs high accuracy, high power, and cost
     */
    public static Criteria createFineCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;
    }


    /**
     * Metodo para cancelar el servicio de Localizacion
     */
    private void StopLocationService() {
        if (locationManager != null) {
            Log.e("LocService", "StopService");
            locationManager.removeUpdates(locationListenerNetwork);
            locationManager.removeUpdates(locationListenerGps);
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

        //Log.e("Best", String.valueOf(newLocation.getLatitude()) + "," + String.valueOf(newLocation.getLongitude()) + "//" + dateString);

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
        boolean isLessAccurate = (0 < accuracyDelta) && (accuracyDelta < 45);
        boolean isMoreAccurate = (-15 < accuracyDelta) && (accuracyDelta < 0);
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

        if ((1E-4 > deltaLat) || (1E-4 > deltaLng)) {

            return true;
        }

        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////
}



    /*
    private void GetArraypositions(Location newLocationTemp, Location oldLocationTemp, ArrayList<Location> arrayLocTemp, String Name) {

        if (firstTime) {
            oldLocationTemp = newLocationTemp;
            SaveLocation(newLocationTemp);
            firstTime = false;
        }

        if (isBetterLocation(newLocationTemp, oldLocationTemp)) {

            if (isNearToNextPosition(oldLocationTemp, newLocationTemp)) {

                if (arrayLocTemp != null) {

                    arrayLocTemp.add(newLocationTemp);
                    timesLoc++;

                    oldLocationTemp = newLocationTemp;
                    Log.e(Name, String.valueOf(newLocationTemp.getLatitude()) + "," + String.valueOf(newLocationTemp.getLongitude()) + "/" + String.valueOf(newLocationTemp.getAccuracy()));
                    Log.e("Cuantity", String.valueOf(timesLoc));
                    Log.e("Distance", "NEAR");

                } else {
                    arrayLocTemp = new ArrayList<Location>();
                }




                // Guardamos si el punto es diferente al anterior guardado

                Double tempSaveLastLat = 0.0;
                Double tempSaveLastLng = 0.0;
                Double deltaLat = 0.0;
                Double deltaLng = 0.0;

                tempSaveLastLat = Double.valueOf(getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                        .getString(GpsConstants.SHARE_POSITION_TRACK_LAT, "0.0"));

                tempSaveLastLng = Double.valueOf(getSharedPreferences(GpsConstants.SHARE_POSITION_TRACK, MODE_PRIVATE)
                        .getString(GpsConstants.SHARE_POSITION_TRACK_LNG, "0.0"));

                deltaLat = Math.abs(newLocationTemp.getLatitude() - tempSaveLastLat);
                deltaLng = Math.abs(newLocationTemp.getLongitude() - tempSaveLastLng);

                if ((deltaLat > 0.0001) && (deltaLng > 0.0001)) {

                    if (isMoving) {
                        Log.e("isMoving", "TRUE");
                        oldLocationTemp = newLocationTemp;
                        SaveLocation(newLocationTemp);
                        Log.e(Name, String.valueOf(newLocationTemp.getLatitude()) + "," + String.valueOf(newLocationTemp.getLongitude()) + "/" + String.valueOf(newLocationTemp.getAccuracy()));
                    } else {
                        Log.e("isMoving", "FALSE");
                    }
                }


            } else {
                Log.e("Distance", Name + "/" + "FAR:" + String.valueOf(newLocationTemp.getLatitude()) + "," + String.valueOf(newLocationTemp.getLongitude()) + "/" + String.valueOf(newLocationTemp.getAccuracy()));
            }

        } else {
            Log.e("DiscardLoc", Name + "/" +  String.valueOf(newLocationTemp.getLatitude()) + "," + String.valueOf(newLocationTemp.getLongitude()) + "/" + String.valueOf(newLocationTemp.getAccuracy()));
        }
    }
*/


/**
 * Metodo para almacenar una posicion aceptable
 */
/*
    private void SaveGoodPosition(final ArrayList<Location> arrayLoc) {

        Double tempSaveLastLat = 0.0;
        Double tempSaveLastLng = 0.0;
        Double deltaLat = 0.0;
        Double DeltaLng = 0.0;

        if ((arrayLoc != null) && (timesLoc > 0)) {

            tempLat = 0.0;
            tempLng = 0.0;

            for (int i = 0; i < arrayLoc.size(); i++) {

                tempLat = tempLat + arrayLoc.get(i).getLatitude();
                tempLng = tempLng + arrayLoc.get(i).getLongitude();
            }

            tempLat = tempLat / arrayLoc.size();
            tempLng = tempLng / arrayLoc.size();

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

                if (isMoving) {
                    Log.e("isMoving", "TRUE");
                    SaveLocation(newLocation);
                } else {
                    Log.e("isMoving", "FALSE");
                }
            }

            timesLoc = 0;
            //arrayLoc = null;

        }
    }
*/