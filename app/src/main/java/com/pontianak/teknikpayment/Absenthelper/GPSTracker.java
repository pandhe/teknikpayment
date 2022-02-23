package com.pontianak.teknikpayment.Absenthelper;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.pontianak.teknikpayment.Service.Service_Connector;
import com.pontianak.teknikpayment.Service.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    LatLng myLocation;

    GoogleMap mMap = null;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters

    // The minimum time between updates in milliseconds
    //private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final long MIN_TIME_BW_UPDATES = 15000; // 1 minute
    // Declaring a Location Manager
    protected LocationManager locationManager;
    private Service_Connector service_connector;
    private SharedPrefManager sharedPrefManager;


    public GPSTracker(Context context) {
        this.mContext = context;
        this.service_connector=new Service_Connector();
        this.sharedPrefManager=new SharedPrefManager(context);
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.d("disabled","1");
            } else {
                Log.d("disabled","0");
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    Log.d("network","ok");
                    Log.d("ijin", String.valueOf(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("network123","tak ada ijin");

                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }else{
                        Log.d("ijin network","ok");
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network234", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            myLocation = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            if(mMap != null){
                                mapPanTo();
                            }
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    Log.d("gps","ok");
                    if (location != null) {
                        Log.d("location null","ok");
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                myLocation = new LatLng(location.getLatitude(),
                                        location.getLongitude());
                                if(mMap != null){
                                    mapPanTo();
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
        }
        if(location!=null) {
            Log.i("ez tracker", location.toString()+location.getLatitude()+" "+location.getLongitude());
            if (location.isFromMockProvider()) {
                Log.i("ez tracer mock", "sepertinya koordinat " +" dan akal sehatnya mungkin (/`_`)/ fake");
                sendmockwarning(location);
            }
        }
        else{
            Log.i("ez tracker", "tracking gagal");
        }
        return location;
    }




    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app.
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public void setMap(GoogleMap map){
        this.mMap = map;
        this.mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getLocation();
                return false;

            }
        });
       // mMap.setMy
        mapPanTo();
    }
    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */

    public void mapPanTo(){
        Log.d("mapPanTo","1");
        if(myLocation != null) {
            Log.d("mapPanTo","myLocation is not null");
            if (mMap != null) {
                Log.d("mapPanTo","map not null");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        myLocation, 17));
            }else{
                Log.d("mapPanTo","map null");
            }
        }else{
            Log.d("mapPanTo","myLocation is null");
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting DialogHelp Title
        alertDialog.setTitle("GPS is settings");

        // Setting DialogHelp Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChangedTrck","pindah");
        float bestAccuracy = -1f;

        if (location.getAccuracy() != 0.0f
                && (location.getAccuracy() < bestAccuracy) || bestAccuracy == -1f) {
            locationManager.removeUpdates(this);
        }

        myLocation = new LatLng(location.getLatitude(),
                location.getLongitude());
        mapPanTo();

        bestAccuracy = location.getAccuracy();

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public float getAccurecy()
    {
        if(location != null) {
            return location.getAccuracy();
        }else{
            return 0.0f;
        }
    }

    private void sendmockwarning(Location location){
        Map<String,String> map=new HashMap();
        map.put("locationstring",location.toString());
        service_connector.sendpostrequestwithsession_v3(this.mContext, sharedPrefManager.getSpCookie(), "service/mockwarning", map, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onResult() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {

            }

            @Override
            public void onNoConnection(String message) {

            }

            @Override
            public void OnServerError(String message) {

            }

            @Override
            public void OnTimeOut() {

            }
        });
    }

}