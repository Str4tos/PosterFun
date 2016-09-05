package com.example.stratos.posterfun.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBContent extends SQLiteOpenHelper {

    private static final String DB_NAME = "dbFunEvents.sqlite";
    private static final String DB_FOLDER = "/data/data/com.example.stratos.posterfun/databases/";
    private static final String DB_PATH = DB_FOLDER + DB_NAME;
    private static final int DB_VERSION = 5;
    private static final String URL_DATABASE = "http://posterfun.at.ua/dbFunEvents.sqlite";


    public DBContent(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static String getDB_PATH(){
        return DB_PATH;
    }

    public static String getUrlDatabase() {
        return URL_DATABASE;
    }

    public static boolean isInitialized() {
        SQLiteDatabase checkDB = null;
        Boolean correctVersion = false;

        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            correctVersion = checkDB.getVersion() == DB_VERSION;
        } catch (SQLiteException e) {
            Log.w("Stratos", e.getMessage());
        } finally {
            if (checkDB != null)
                checkDB.close();
        }

        return checkDB != null && correctVersion;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
