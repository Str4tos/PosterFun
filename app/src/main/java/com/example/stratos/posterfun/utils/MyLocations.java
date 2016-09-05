package com.example.stratos.posterfun.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

public class MyLocations {

    private LocationManager locationManager;
    private Activity myContext;
    private PrefStor prefStor;
    private static Location currLocation;

    public MyLocations(Activity context) {
        this.myContext = context;
        prefStor = new PrefStor(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (currLocation == null)
            currLocation = prefStor.loadLocation();
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, locationListener);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);

    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (location == null)
                return;
            currLocation = location;
            locationManager.removeUpdates(locationListener);
            prefStor.saveLocation(currLocation);
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (locationManager.getLastKnownLocation(provider) != null) {
                currLocation = locationManager.getLastKnownLocation(provider);
                locationManager.removeUpdates(locationListener);
                prefStor.saveLocation(currLocation);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Stratos", "LocationListener.onProviderDisabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Stratos", "LocationListener.onStatusChanged");
        }
    };

    public static void setItemsDistance(List<GenContent> mITEMS) {
        if (currLocation == null || mITEMS == null) return;
        for (GenContent item : mITEMS)
            if (item.getDistance() == 0) {
                Location temp = new Location("temp_location");
                temp.setLatitude(item.getLoclat());
                temp.setLongitude(item.getLoclng());

                //double result = 111.2 * Math.sqrt((lng - lngItem) * (lng - lngItem) + (lat - latItem) * Math.cos(Math.PI * lng / 180) * (lat - latItem) * Math.cos(Math.PI * lng / 180));
                float result = currLocation.distanceTo(temp) / 1000;
                item.setDistance(result);
            }
    }


}
