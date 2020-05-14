package com.ddcorp.mi_17helicopterperformance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

class fragment_weights_recyler extends RecyclerView.Adapter<fragment_weights_recyler.ViewHolder> {
    public FragmentManager fragmentManager;
    public Integer dustprotect = 0, hoist = 0, hirss = 0, antiice = 0, internaltank = 0, bambibucket = 0, armour = 0;
    public GrossWeight_listener grossWeight_listener;
    DatabaseAccess databaseAccess;
    Integer operating = 0, crew, fuel, load = 0;
    Context context;
    card0_ViewHolder card0 = null;
    boolean firststart = true;
    private int card_0 = 0;
    private int card_1 = 1;
    private int card_2 = 2;
    private Integer custom_weights = 0;
    private Integer GW = 0;
    private Integer static_weights = 0;


    public fragment_weights_recyler(Context context, GrossWeight_listener grossWeight_listener) {
        this.context = context;
        // updateGrossWeight(GW, operating, crew, fuel, load, hoist, antiice, internaltank, bambibucket, armour);
        databaseAccess = DatabaseAccess.getInstance(context);
        this.grossWeight_listener = grossWeight_listener;


    }

    public Animation getBlinkAnimation() {
        Animation animation = new AlphaAnimation(1, 0);         // Change alpha from fully visible to invisible
        animation.setDuration(300);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(5);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in

        return animation;
    }

    @NonNull
    @Override
    public fragment_weights_recyler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        if (viewType == card_0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_weight_card0, parent, false);

            return new card0_ViewHolder(v);
        }
        if (viewType == card_1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_weight_card1, parent, false);

            return new card1_ViewHolder(v);
        }
        if (viewType == card_2) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_weight_card2, parent, false);

            return new card2_ViewHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull fragment_weights_recyler.ViewHolder holder, int position) {

        if (holder.getItemViewType() == card_0) {
            card0 = (card0_ViewHolder) holder;
            update_GW(card0);
        }
        if (holder.getItemViewType() == card_1) {
            final card1_ViewHolder card = (card1_ViewHolder) holder;

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updater_custom_weights(card);
                    hidesoftkeyboard(v);
                    notifyDataSetChanged();
                }
            };
            card.operating_TextView.setOnClickListener(onClickListener);
            card.crew_TextView.setOnClickListener(onClickListener);
            card.fuel_TextView.setOnClickListener(onClickListener);
            card.load_TextView.setOnClickListener(onClickListener);

            if (card.checkBox.isChecked()) {
                card.operating_TextView.setText("7500");
            }
            card.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        card.operating_TextView.setText("");
                    }
                    updater_custom_weights(card);
                    notifyDataSetChanged();
                }
            });
            if (card.checkBox2.isChecked()) {
                card.fuel_TextView.setText("2490");
            }
            card.checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        card.fuel_TextView.setText("");
                    }
                    updater_custom_weights(card);
                    notifyDataSetChanged();

                }
            });
            if (card.checkBox3.isChecked()) {
                card.crew_TextView.setText("400");
            }

            card.checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        card.crew_TextView.setText("");
                    }
                    updater_custom_weights(card);
                    notifyDataSetChanged();


                }
            });
            updater_custom_weights(card);

            card.custom_weight_Textview.setText(custom_weights.toString() + " Kg.");


        }
        if (holder.getItemViewType() == card_2) {
            final card2_ViewHolder card = (card2_ViewHolder) holder;
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    update_static_weights(card);
                    notifyDataSetChanged();
                }
            };
/*
            card.Switch_dustprotect.setOnCheckedChangeListener(onCheckedChangeListener);
            card.Switch_hirss.setOnCheckedChangeListener(onCheckedChangeListener);
            card.Switch_antiice.setOnCheckedChangeListener(onCheckedChangeListener);*/
            card.Switch_armour.setOnCheckedChangeListener(onCheckedChangeListener);
            card.Switch_hoist.setOnCheckedChangeListener(onCheckedChangeListener);

            //card.Switch_internaltank.setOnCheckedChangeListener(onCheckedChangeListener);
            card.Switch_bambibucket.setOnCheckedChangeListener(onCheckedChangeListener);
            card.Switch_internaltank.setOnCheckedChangeListener(onCheckedChangeListener);
            //card.Checkbox_bambifull.setOnCheckedChangeListener(onCheckedChangeListener);
            if (card.Switch_bambibucket.isChecked()) {
                card.Checkbox_bambifull.setEnabled(true);
            } else card.Checkbox_bambifull.setEnabled(false);
            card.Checkbox_bambifull.setOnCheckedChangeListener(onCheckedChangeListener);

            if (card.Switch_internaltank.isChecked()) {
                card.CheckBox_Aux_Full.setEnabled(true);
            } else card.CheckBox_Aux_Full.setEnabled(false);
            card.CheckBox_Aux_Full.setOnCheckedChangeListener(onCheckedChangeListener);

            update_static_weights(card);
            if (card0 != null && firststart) {
                firststart = false;
                update_GW(card0);
            }


        }

    }

    private void update_static_weights(card2_ViewHolder card) {
       /* dustprotect = Switch_to_integer(card.Switch_dustprotect.isChecked(), 200);
        hirss = Switch_to_integer(card.Switch_hirss.isChecked(), 300);
        antiice = Switch_to_integer(card.Switch_antiice.isChecked(), 800);
        */
        hoist = Switch_to_integer(card.Switch_hoist.isChecked(), 47);

        internaltank = Switch_to_integer(card.Switch_internaltank.isChecked(), 48);
        armour = Switch_to_integer(card.Switch_armour.isChecked(), 250);
        if (card.Switch_bambibucket.isChecked()) {
            if (card.Checkbox_bambifull.isChecked()) {
                bambibucket = 2590;
            } else bambibucket = 90;
        } else bambibucket = 0;

        if (card.Switch_internaltank.isChecked()) {
            if (card.CheckBox_Aux_Full.isChecked()) {
                internaltank = 943;
            } else internaltank = 48;
        } else internaltank = 0;

        static_weights = hoist +  internaltank + bambibucket + armour ;
//        static_weights = hoist + antiice + internaltank + bambibucket + armour + hirss + dustprotect;
        card.static_weight_Textview.setText(static_weights.toString() + " Kg.");
        update_GW(card0);

    }

    private void update_GW(card0_ViewHolder card) {
        GW = custom_weights + static_weights;
        if (GW <11101) {
            card.GW_TextView.setTextColor(context.getResources().getColor(R.color.text_Green));;
        }else if(GW<13000){
            card.GW_TextView.setTextColor(context.getResources().getColor(R.color.AccentOrange));;
        }else
            card.GW_TextView.setTextColor(context.getResources().getColor(R.color.text_Red));;

        card.GW_TextView.setText(GW.toString());
        grossWeight_listener.GrossWeght_listener(GW);
    }

    private void updater_custom_weights(card1_ViewHolder card) {
        operating = Textview_to_integer(card.operating_TextView.getText().toString());
        crew = Textview_to_integer(card.crew_TextView.getText().toString());
        fuel = (int) (0.79 * Textview_to_integer(card.fuel_TextView.getText().toString()));
        card.textInputLayout_fuel.setHint("Lt. (" + fuel.toString() + " Kg. JP8)");
        load = Textview_to_integer(card.load_TextView.getText().toString());
        custom_weights = operating + crew + fuel + load;
        update_GW(card0);


    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void updateGrossWeight(Integer armour, Integer operating, Integer crew, Integer fuel, Integer load, Integer hoist, Integer antiice, Integer internaltank, Integer bambibucket, Integer GW, Integer dustprotect) {
        this.GW = GW;
        this.operating = operating;
        this.crew = crew;
        this.fuel = fuel;
        this.load = load;
        this.hoist = hoist;
        this.antiice = antiice;
        this.internaltank = internaltank;
        this.bambibucket = bambibucket;
        this.armour = armour;
        this.dustprotect = dustprotect;
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
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        view.clearFocus();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public interface GrossWeight_listener {
        public void GrossWeght_listener(int GW);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    private class card0_ViewHolder extends ViewHolder {

        TextView GW_TextView;

        public card0_ViewHolder(View v) {
            super(v);

            GW_TextView = v.findViewById(R.id.GW_TextView);
            GW_TextView.setText("-");


        }
    }

    private class card1_ViewHolder extends ViewHolder {


        TextView custom_weight_Textview,
                operating_TextView,
                crew_TextView,
                fuel_TextView,
                load_TextView;

        CheckBox checkBox, checkBox2, checkBox3;
        TextInputLayout textInputLayout_fuel;


        public card1_ViewHolder(View v) {
            super(v);
            operating_TextView = v.findViewById(R.id.operatingtext);
            crew_TextView = v.findViewById(R.id.crewtext);
            fuel_TextView = v.findViewById(R.id.fueltext);
            load_TextView = v.findViewById(R.id.loadtext);
            textInputLayout_fuel = v.findViewById(R.id.textInputLayout_fuel);
            custom_weight_Textview = v.findViewById(R.id.custom_weight_Textview);
            checkBox = v.findViewById(R.id.checkBox);
            checkBox2 = v.findViewById(R.id.checkBox2);
            checkBox3 = v.findViewById(R.id.checkBox3);


            operating = Textview_to_integer(operating_TextView.getText().toString());
            crew = Textview_to_integer(crew_TextView.getText().toString());
            fuel = Textview_to_integer(fuel_TextView.getText().toString());
            load = Textview_to_integer(load_TextView.getText().toString());


        }
    }

    private class card2_ViewHolder extends ViewHolder {


        TextView static_weight_Textview;

        Switch
                Switch_hoist,
                Switch_internaltank,
                Switch_bambibucket,
                Switch_armour;


        CheckBox Checkbox_bambifull;
        CheckBox CheckBox_Aux_Full;

        public card2_ViewHolder(View v) {
            super(v);


            Switch_hoist = v.findViewById(R.id.SwitchHoist);
            Switch_internaltank = v.findViewById(R.id.SwitchTank);
            Switch_bambibucket = v.findViewById(R.id.SwitchBambi);
            Switch_armour = v.findViewById(R.id.SwitchArmour);
            Checkbox_bambifull = v.findViewById(R.id.checkBoxBambi_Full);
            static_weight_Textview = v.findViewById(R.id.static_weight_Textview);
            CheckBox_Aux_Full = v.findViewById(R.id.checkBox_Aux_Full);


        }
    }

}
