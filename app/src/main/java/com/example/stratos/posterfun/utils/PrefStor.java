package com.example.stratos.posterfun.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

public class PrefStor {

    private SharedPreferences sharedPreferences;
    private final String SAVED_LAT = "Location_Latitude";
    private final String SAVED_LGN = "Location_Longitude";
    private final String SAVED_CITY = "City_name";


    public PrefStor(Activity activity) {
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveLocation(Location curLoc) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putFloat(SAVED_LAT, (float) curLoc.getLatitude());
        ed.putFloat(SAVED_LGN, (float) curLoc.getLongitude());
        ed.apply();
    }

    public Location loadLocation() {
            Location curLoc = new Location("Current location device");
            curLoc.setLatitude(sharedPreferences.getFloat(SAVED_LAT, 0));
            curLoc.setLongitude(sharedPreferences.getFloat(SAVED_LGN, 0));
        return curLoc;
    }

    public void saveCityName(String city){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(SAVED_CITY, city);
        ed.apply();

    }

    public String getCityName(){
        return sharedPreferences.getString(SAVED_CITY,PreCont.SELECT_ALL);
    }



}
