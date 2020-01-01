package com.ddcorp.mi_17helicopterperfomance;

import android.app.Application;
import android.content.Context;





/**
 * Created by d on 23.10.2016.
 */

public class application extends Application {
    private static Context context;
    private static DBHandler dbHandler;



    @Override
    public void onCreate()
    {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        context = this.getApplicationContext();
       // dbHandler = new DBHandler();
      //  DatabaseAccess.initializeInstance(dbHandler);


    }

    public static Context getContext(){
        return context;
    }
}
