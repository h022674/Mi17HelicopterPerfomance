package com.ddcorp.mi_17helicopterperfomance;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ddcorp.mi_17helicopterperfomance.application;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by d on 18.09.2016.
 */
public class DBHandler extends SQLiteAssetHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "perdata.db";
    //private static final String TAG = DBHandler.class.getSimpleName().toString();


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}




