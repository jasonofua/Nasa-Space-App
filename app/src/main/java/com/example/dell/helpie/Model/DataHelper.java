package com.example.dell.helpie.Model;

import android.content.Context;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

public class DataHelper {

    // database helper used to store data offline

    public static final String FIRE = "fire";
    public static final String FLOOD = "flood";
    public static final String EARTHQUAKE = "earthquake";
    public static final String HURRICANE = "hurricane";
    public static final String TORNADO = "tornado";
    public static final String USERDATA = "userdata";

    public static Database getDatabase(Context context, String databaseName){
        try {
            AndroidContext androidContext = new AndroidContext(context);
            Manager dbManager = new Manager(androidContext, Manager.DEFAULT_OPTIONS);
            return dbManager.getDatabase(databaseName);
        } catch (CouchbaseLiteException | IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Cannot get access to required database!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }}
