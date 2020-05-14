package com.ddcorp.mi_17helicopterperformance;

/**
 * Created by d on 22.10.2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class DatabaseAccess {

    private static DatabaseAccess instance;
    private Context context;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DBHandler(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public Integer get_IGE_OGE_TABLE_value(Integer takeoff_alt_meter, int fat, String tablename) {
        open();
        int returnvalue = 0;
        int alt = (Math.round(takeoff_alt_meter / 50) * 50);
        if (fat >49) {
            fat=49;
        }
        if (fat <-50) {
            fat=-50;
        }
        Cursor cursor = database.rawQuery("Select  \"" + String.valueOf(alt) + "\" FROM " +
                tablename +
                " WHERE fat=" + String.valueOf(fat) + " ", null);

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                returnvalue = Math.round(Float.valueOf(cursor.getString(0)));
            }
        } finally {
            cursor.close();
            close();
        }


        return returnvalue;
    }

    public Integer getIGE_OGE_WING_value(Integer takeoff_wind_msec, String winddirection, String tablename) {
        open();
        int returnvalue = 0;
        if (takeoff_wind_msec > 12) {
            takeoff_wind_msec = 12;
        }
        if (winddirection == null) {
            winddirection = "Head";
        }
        Cursor cursor = database.rawQuery("Select  \"" + String.valueOf(winddirection) + "\" FROM " +
                tablename +
                " WHERE ruzgarhizi=" + String.valueOf(takeoff_wind_msec) + " ", null);

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                returnvalue = Math.round(Float.valueOf(cursor.getString(0)));
            }
        } finally {
            cursor.close();
            close();
        }


        return returnvalue;


    }

    public Integer get_v_speeds(Integer takeoff_alt_meter, String coloumn) {
        open();
        int returnvalue = 0;
        String querystring;
        int alt = (Math.round(takeoff_alt_meter / 100) * 100);
        querystring = "SELECT " +
                coloumn +
                " FROM airspeed WHERE irtifa=" +
                String.valueOf(alt);

        Cursor cursor = database.rawQuery(querystring, null);

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                returnvalue = cursor.getInt(0);
            }
        } finally {
            cursor.close();
            close();
        }

        return returnvalue;
    }

    public Integer get_singleeng(Integer takeoff_alt_meter, Integer fat, String tablename) {
        open();
        int returnvalue = 0;
        int alt = (Math.round(takeoff_alt_meter / 100) * 100);
        Cursor cursor = database.rawQuery("Select  \"" + String.valueOf(alt) + "\" FROM " +
                tablename +
                " WHERE SICAKLIK=" + String.valueOf(fat) + " ", null);

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                returnvalue = Math.round(Float.valueOf(cursor.getString(0)));
            }
        } finally {
            cursor.close();
            close();
        }


        return returnvalue;


    }

    public Integer get_fuelconsumption(Integer gw, Integer cruise_alt_meter) {

        open();
        int returnvalue = 0;
        gw = (Math.round(gw / 100) * 100);
        if (gw < 9000) {
            gw = 9000;
        }
        cruise_alt_meter = (Math.round(cruise_alt_meter / 100) * 100);
        Cursor cursor = database.rawQuery("Select  \"" + String.valueOf(gw) + "\" FROM " +
                "fuel" +
                " WHERE Column0=" + String.valueOf(cruise_alt_meter) + " ", null);

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                returnvalue = Math.round(Float.valueOf(cursor.getString(0)));
            }
        } finally {
            cursor.close();
            close();
        }

        return returnvalue;
    }

    public double get_apu_press(Integer altitude_meter, Integer fat) {
        open();
        List<Float> p_t2 = new ArrayList<>();
        List<Float> p_t1 = new ArrayList<>();

        String querystring;
        String coloumn;

        if (fat > 0) {
            coloumn = "sifir,arti";
        } else if (fat == 0) coloumn = "sifir";
        else coloumn = "eksi,sifir";

        int alt0 = (Math.round(altitude_meter / 100) * 100);
        if (alt0 > 3800) {
            alt0=3800;
        }
        int alt1;
        if ((alt0 / 100) % 2 == 0) {
            alt1 = alt0;
        } else {
            alt1 = alt0 + 100;
            alt0 = alt0 - 100;
        }


        querystring = "SELECT " +
                coloumn +
                " FROM apu WHERE " +
                " irtifa BETWEEN " +
                alt0 +
                " and " +
                alt1;

        Cursor cursor = database.rawQuery(querystring, null);

        try {
            cursor.moveToFirst();
                do {
                    if (cursor.getCount() > 0) {
                        if (cursor.getColumnCount() == 2) {
                            p_t1.add(cursor.getFloat(0));// 0 derece
                            p_t2.add(cursor.getFloat(1));// 50 derece
                        }else p_t1.add(cursor.getFloat(0));// 0 derece
                    }
                }while (cursor.moveToNext());

        } finally {
            cursor.close();
            close();
        }

//C3+(C4-C3)*(I8-B3)/(B4-B3)

        Float p_t1_pol = 0f;
        Float p_t2_pol  =0f;
        double p_return=0f;
        if (alt0 != alt1) {

              p_t1_pol=p_t1.get(0)+(p_t1.get(1)-p_t1.get(0))*(altitude_meter-alt0)/(alt1-alt0);
            if (!p_t2.isEmpty()) {
                p_t2_pol=p_t2.get(0)+(p_t2.get(1)-p_t2.get(0))*(altitude_meter-alt0)/(alt1-alt0);
            }

        }else {
           p_t1_pol=p_t1.get(0);
            if (!p_t2.isEmpty()) {
                p_t2_pol=p_t2.get(0);
            }
        }

        if (fat ==0) {
            p_return=p_t1_pol;
        }else if(fat<0){
            p_return=(Float) p_t2_pol+(p_t1_pol-p_t2_pol)*  (Math.abs(fat))/(50);
            //p_return=(Float) p_t1_pol+(p_t2_pol-p_t1_pol)*  (Math.abs(fat))/(50);
        }else {
            p_return=(Float) p_t1_pol+(p_t2_pol-p_t1_pol)*  (Math.abs(fat))/(50);

        }


        return p_return;
    }


    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
   /* public List<String> getQuotes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM quotes", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }*/
}

