package com.ddcorp.mi_17helicopterperfomance;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements fragment_cruise.send_fuelconsumption,fragment_weights.sendGW,fragment_startcard.send_env_cond,fragment_takeoff.send_env_cond {
    public static final String LAST_VERSION = "LAST_VERSION";
    public static final String TAG = "Main Activity";
    private DatabaseAccess databaseAccess;
    ViewPager viewPager;
    fragment_arrival fragment_arrival;
    fragment_takeoff fragment_takeoff;
    fragment_cruise fragment_cruise;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
                builder.setTitle("About");
                builder.setMessage(R.string.abouttext);

                // add a button
                builder.setPositiveButton("OK", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                // User chose the "Settings" item, show the app settings UI...
                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Locate the viewpager in activity_main.xml
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tablayout);

        // Set the ViewPagerAdapter into ViewPager
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new viewpageradapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
               /* if (tab.getPosition() == 4) {
                   // sendGW_to_arrival_and_takeoff(gw);
                   // send_fuelconsumption_to_arivalFragment(fuelconsumption);
                }*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*RecyclerView recyclerView=findViewById(R.id.recylerview);
        adapter=new mRecylerAdapter(this);
        adapter.fragmentManager=getSupportFragmentManager();

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

*/
      //  init_WeightCard();

    }


    private int Switch_to_integer(Boolean bool, int i) {
        if (bool) {
            return i;
        } else return 0;
    }

    private Integer Textview_to_integer(String Text) {
        Integer returnvalue = 0;
        try {
            if (Text != null) {
                returnvalue = Integer.parseInt(Text);
            }
        } catch (NumberFormatException e) {
            returnvalue = 0;
        }
        return returnvalue;
    }

    private void hidesoftkeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        view.clearFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       try {
           if (databaseAccess != null) {
               databaseAccess.close();
           }

       }finally {
           finish();
       }


    }

    @Override
    public void send_fuelconsumption_to_arivalFragment(int fuelconsumption) {
        if (fragment_arrival == null) {
            fragment_arrival =(fragment_arrival) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 4);

        }
        fragment_arrival.getfuelconsumption(fuelconsumption);

    }

    @Override
    public void sendGW_to_arrival_and_takeoff(int GW) {

        if (fragment_arrival == null) {
            fragment_arrival =(fragment_arrival) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 4);

        }

            fragment_arrival.getGW(GW);

        if (fragment_takeoff == null) {
            fragment_takeoff=(fragment_takeoff) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 2);
        }
        fragment_takeoff.getGW(GW);


    }

    @Override
    public void send_env_cond_to_takeoff(int altitude, int fat, int wind_kt, int wind_direction, boolean usemeter, boolean usemsec) {
        if (fragment_takeoff == null) {
            fragment_takeoff=(fragment_takeoff) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 2);
        }
        fragment_takeoff.getEnvironmental_Conditions(  altitude,  fat, wind_kt, wind_direction,usemeter,usemsec);

         }

    @Override
    public void send_env_cond_to_cruise(int altitude, int fat, boolean usemeter) {
        if (fragment_cruise == null) {
            fragment_cruise=(fragment_cruise) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 3);
        }
        fragment_cruise.getEnvironmental_Conditions(  altitude,  fat,usemeter);

    }

    @Override
    public void send_env_cond_to_arrival(int altitude, int fat, boolean usemeter, int wind_kt, int wind_direction, boolean usemsec) {
        if (fragment_arrival == null) {
            fragment_arrival=(fragment_arrival) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 4);
        }
        fragment_arrival.getEnvironmental_Conditions(  altitude,  fat, wind_kt, wind_direction,usemeter,usemsec);

    }
}
