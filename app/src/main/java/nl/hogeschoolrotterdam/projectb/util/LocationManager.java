package nl.hogeschoolrotterdam.projectb.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import nl.hogeschoolrotterdam.projectb.R;

import java.util.ArrayList;


public class LocationManager {
    private static final int REQUEST_LOCATION = 2;
    private static final int REQUEST_CHECK_SETTINGS = 32;
    private static LocationManager ourInstance = new LocationManager();
    @Nullable
    public Location lastLocation = null;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private ArrayList<OnLocationResultListener> locationResultListeners = new ArrayList<>();
    private LocationCallback locationCallback = new LocationCallback() {


        @Override
        public void onLocationResult(LocationResult locationResult) {
            lastLocation = locationResult.getLastLocation();

            for (OnLocationResultListener listener : locationResultListeners) {
                listener.onLocationResult(locationResult.getLastLocation());
            }
            locationResultListeners.clear();
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            if (!locationAvailability.isLocationAvailable()) {
                lastLocation = null;
                for (OnLocationResultListener listener : locationResultListeners) {
                    listener.onLocationResult(null);
                }
                locationResultListeners.clear();
            }
        }
    };
    private boolean isInitialized = false;

    @NonNull
    public static LocationManager getInstance() {
        return ourInstance;
    }

    public LocationManager initialize(Context context) {
        if (isInitialized) return this;
        isInitialized = true;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        settingsClient = LocationServices.getSettingsClient(context);

        locationRequest = LocationRequest.create()
                .setInterval(5 * 60 * 1000) // 5*60 seconds (5min)
                .setFastestInterval(60 * 1000) // 60 seconds
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Accurate within a few meters

        locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();

        return this;
    }

    private void startUpdatingLocation(final Activity activity) {
        stopUpdatingLocation();
        if (!hasLocationPermission(activity)) {
            requestLocationPermission(activity);
            return;
        }

        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (((ApiException) e).getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                ((ResolvableApiException) e).startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void requestLocationPermission(final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.warn_enable_permissions)
                    .setMessage(R.string.warn_enable_permissions_message)
                    .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            actualRequestLocationPermission(activity);
                        }
                    })
                    .show();
        } else {
            actualRequestLocationPermission(activity);
        }
    }

    private void actualRequestLocationPermission(final Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }

    private void stopUpdatingLocation() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean hasLocationPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void updateLocation(Activity activity, OnLocationResultListener listener) {
        if (!hasLocationPermission(activity)) {
            requestLocationPermission(activity);
            listener.onLocationResult(null);
        } else {
            locationResultListeners.add(listener);
            startUpdatingLocation(activity);
        }
    }

    public interface OnLocationResultListener {
        /**
         * If location is null, that means that a location was not recieved.
         *
         * @param location the current user location, or null.
         */
        void onLocationResult(@Nullable Location location);
    }
}