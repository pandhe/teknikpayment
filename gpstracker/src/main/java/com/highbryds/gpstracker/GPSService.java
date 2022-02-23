package com.highbryds.gpstracker;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.highbryds.gpstracker.Utils.NotificationClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by tahir.raza on 11/23/2017.
 */

public class GPSService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    // Binder given to clients
    // Registered callbacks
    private static final String LOGSERVICE = "#######";
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    Context c = this;
    NotificationClass nc = new NotificationClass();
    public static int PacketSize;
    public static int LocationInterval;
    public static int LocationFastestInterval;
    /// private static final int drawableIcon = R.drawable.cast_ic_notification_small_icon;
    public static PendingIntent contentIntent;
    public static String NotificationTxt;
    public static String NotificationTitle;
    public static int drawable_small;
    public static String urlServer;
    public static String idDosen;
    int mNotificationId = 001;
    int mNotificationIdWarning = 002;
    int mNotificationGPSEnabled = 003;

    NotificationManager mNotificationManager;
    dbAbsen myDbAbsen;
    BroadcastReceiver mGpsSwitchStateReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        // building Google Api Client at the startup
        buildGoogleApiClient();

        //calling and building notification for ANDROID O within 5 seconds of the Service Startup.
        ForegroundServiceInitialize();

        myDbAbsen = new dbAbsen(getApplicationContext());

        mGpsSwitchStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    Boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if(!isGpsEnabled){
                        final Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        String warningHeader = "GPS anda tidak aktif";
                        String warningText = "Segera aktifkan GPS anda agar presensi anda terdata. ";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //Creating Channel
                            nc.createMainNotificationChannel(getApplicationContext());
                            //building Notification.
                            Notification.Builder notifi = new Notification.Builder(getApplicationContext(), nc.getMainNotificationId());
                            notifi.setSmallIcon(drawable_small);
                            notifi.setContentTitle(warningHeader);
                            notifi.setContentText(warningText);
                            notifi.setContentIntent(contentIntent);
                            notifi.setOngoing(true);

                            //getting notification object from notification builder.
                            Notification n = notifi.build();

                            mNotificationManager =
                                    (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(mNotificationGPSEnabled, n);


                        } else {
                            //for devices less than API Level 26
                            Notification notification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle(warningHeader)
                                    .setContentText(warningText)
                                    .setSmallIcon(drawable_small)
                                    .setContentIntent(contentIntent)
                                    .setSound(notifSound)
                                    .build();

                            mNotificationManager =
                                    (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(mNotificationGPSEnabled, notification);


                        }
                    }else{
                        try {
                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.cancel(mNotificationGPSEnabled);
                        }catch(Exception e){

                        }
                    }
                    // Make an action or refresh an already managed state.
                }
            }
        };

        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //Inntizlizing ArrayList
//        TrackLoc = new ArrayList < > ();
        // We already have taken the permission from the user so this is redundant here...
        @SuppressLint("MissingPermission") Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        startLocationUpdate();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Has been Suspended..", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            Intent broadcastIntent = new Intent("com.highbryds.tracker");
            //  broadcastIntent.setAction(MainActivity.mBroadcastStringAction);
            broadcastIntent.putExtra("latitude", String.valueOf(location.getLatitude()));
            broadcastIntent.putExtra("longitude", String.valueOf(location.getLongitude()));
            broadcastIntent.putExtra("speed", String.valueOf(location.getSpeed()));
            broadcastIntent.putExtra("time", String.valueOf(location.getTime()));
            broadcastIntent.putExtra("altitude", String.valueOf(location.getAltitude()));
            sendToInternet(location.getLatitude(),location.getLongitude(), location.getAccuracy());

            sendBroadcast(broadcastIntent);

        } catch (NullPointerException e) {
            Toast.makeText(GPSService.this, "Location has been disabled...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        Log.d("GPSService", "destroyed");
        super.onDestroy();
        stopThisService();
        if (mGpsSwitchStateReceiver != null){
            unregisterReceiver(mGpsSwitchStateReceiver);
        }

        Log.d("GPSService","destroyed");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void initLocationRequest() {
        //these are the parameters defined.
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LocationInterval);
        mLocationRequest.setFastestInterval(LocationFastestInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        //Getting all the paramters.
        initLocationRequest();
        //Requesting Location Updates..
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    private void stopLocationUpdate() {
        //TO Remove Location Udpates
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    public void stopThisService(){
        this.cancelNotification();
        this.stopForeground(true);
        this.stopSelf();
    }

    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(new Date());
    }

    private void postTempTracking(List<tempTracking> tempData){

        Gson gson = new Gson();
        String element = gson.toJson(
                tempData,
                new TypeToken<ArrayList<tempTracking>>() {}.getType());

        //Log.d("data gagal",element);

        String urlPostTracking = urlServer + "/postTrackDataGagal.php";

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest request = new StringRequest(Request.Method.POST, urlPostTracking,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDbAbsen.truncateTabelTracking();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", idDosen);
                params.put("dataTrack",element);

                return params;
            }
        };

        request.setRetryPolicy(policy);

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(request);
        //mRequestQueue.start();



        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", idDosen);
        params.put("dataTrack",element);

    }

    public void sendToInternet(final double latitude, final double longitude, final double accuracy) {
        long unixTime = System.currentTimeMillis() / 1000L;
        final String timestamp = String.valueOf(unixTime);

        String urlTrack = urlServer + "/postTrack.php";
        Log.d("sendToInternet", "start");
        int socketTimeout = 15000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        final Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        StringRequest request = new StringRequest(Request.Method.POST, urlTrack,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sendToInternet",String.valueOf(latitude)+ " - " + String.valueOf(longitude) + String.valueOf(accuracy));
                        try{
                            JSONObject responseObject = new JSONObject(response);
                            String status= responseObject.getString("status");
                            if(status.equals("STOP SERVICE")){
                                stopThisService();
                            }
                        }catch (Exception e){

                        }

                        try{
                            List<tempTracking> listTracking =  myDbAbsen.getDataListTracking();
                            if(listTracking.size() > 0){
                                postTempTracking(listTracking);
                            }else{

                            }
                        }catch(Exception e){

                        }

                        try {
                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.cancel(mNotificationIdWarning);
                        }catch(Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("postTrackError", String.valueOf(error));
                myDbAbsen.insertData(String.valueOf(latitude),String.valueOf(longitude),String.valueOf(accuracy),timestamp);


                String warningHeader = "Gagal mengirimkan data lokasi";
                String warningText = "Cek koneksi internet dan pastikan lokasi anda aktif. ";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //Creating Channel
                    nc.createMainNotificationChannel(getApplicationContext());
                    //building Notification.
                    Notification.Builder notifi = new Notification.Builder(getApplicationContext(), nc.getMainNotificationId());
                    notifi.setSmallIcon(drawable_small);
                    notifi.setContentTitle(warningHeader);
                    notifi.setContentText(warningText);
                    notifi.setContentIntent(contentIntent);

                    //getting notification object from notification builder.
                    Notification n = notifi.build();

                    mNotificationManager =
                            (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(mNotificationIdWarning, n);


                } else {
                    //for devices less than API Level 26
                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle(warningHeader)
                            .setContentText(warningText)
                            .setSmallIcon(drawable_small)
                            .setContentIntent(contentIntent)
                            .setSound(notifSound)
                            .build();

                    mNotificationManager =
                            (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(mNotificationIdWarning, notification);
                }
            }
        }) {
            @Override
            public HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", idDosen);
                params.put("lat", String.valueOf(latitude));
                params.put("lng", String.valueOf(longitude));
                params.put("akurasi", String.valueOf(accuracy));

                return params;
            }
        };

        request.setRetryPolicy(policy);
        /**
         SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, urlTrack, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        Log.d("sendToInternet",String.valueOf(latitude)+ " - " + String.valueOf(longitude) + String.valueOf(accuracy));
        }
        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        Log.d("VolleyError", String.valueOf(error));
        }
        });


         request.addMultipartParam("id", "text/plain", idDosen);
         request.addMultipartParam("lat", "text/plain", String.valueOf(latitude));
         request.addMultipartParam("lng", "text/plain", String.valueOf(longitude));
         request.addMultipartParam("akurasi","text/plain",String.valueOf(accuracy));

         request.setRetryPolicy(policy);
         */
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(request);
        //mRequestQueue.start();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void ForegroundServiceInitialize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Creating Channel
            nc.createMainNotificationChannel(this);
            //building Notification.
            Notification.Builder notifi = new Notification.Builder(getApplicationContext(), nc.getMainNotificationId());
            notifi.setSmallIcon(drawable_small);
            notifi.setContentTitle(NotificationTitle);
            notifi.setContentText(NotificationTxt);
            notifi.setContentIntent(contentIntent);
            notifi.setOngoing(true);

            //getting notification object from notification builder.
            Notification n = notifi.build();

            mNotificationManager =
                    (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(mNotificationId, n);

            //  startting foreground
            startForeground(1, n);


        } else {
            //for devices less than API Level 26
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(NotificationTitle)
                    .setContentText(NotificationTxt)
                    .setSmallIcon(drawable_small)
                    .setContentIntent(contentIntent)
                    .setOngoing(true).build();
            startForeground(1, notification);

        }


    }


    public void cancelNotification() {
        try{
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(mNotificationId); // Notification ID to cancel
            //}else{

            //}
        }catch(Exception e){

        }

    }
}