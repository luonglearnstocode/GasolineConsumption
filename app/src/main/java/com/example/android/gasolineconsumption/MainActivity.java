package com.example.android.gasolineconsumption;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String PREF = "TestPref";
    private String unit;
    private double averageLkm;
    private double maxLkm;
    private double minLkm;
    private double lastLkm;
    private double averageMpg;
    private double maxMpg;
    private double minMpg;
    private double lastMpg;
    private double average;
    private double max;
    private double min;
    private double last;
    private ArrayList<String> data;
    private Set<String> set = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences prefGet = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
//        set = prefGet.getStringSet("key", null);

//        if (set != null) {
//            data = new ArrayList<String>();
//            data.addAll(set);
//        } else {
//            data = new ArrayList<String>();
////            data.add("16.12.2015\t120567\t42");
////            data.add("2.1.2016\t120869\t38");
////            data.add("9.1.2016\t121146\t43");
////            data.add("13.1.2016\t121459\t39");
//        }


        ArrayList<Integer> odometerValue = new ArrayList<Integer>();
        ArrayList<Integer> distances = new ArrayList<Integer>();
        ArrayList<Integer> fuelAmount = new ArrayList<Integer>();
        ArrayList<Double> consumption = new ArrayList<Double>();

        //list of initial data
        //ArrayList<String> data = new ArrayList<String>();
        data = new ArrayList<String>();
        data.add("16.12.2015\t120567\t42");
        data.add("2.1.2016\t120869\t38");
        data.add("9.1.2016\t121146\t43");
        data.add("13.1.2016\t121459\t39");

        //for each string in data, split, add value to odometerValue and fuelAmount
        for (String datum : data) {
            String[] datumsplitted = datum.split("\t");
            odometerValue.add(Integer.parseInt(datumsplitted[1]));
            fuelAmount.add(Integer.parseInt(datumsplitted[2]));
        }

        //add value to distances list - difference of odometer value
        for (int i = 1; i < odometerValue.size(); i++){
            distances.add(odometerValue.get(i)-odometerValue.get(i-1));
        }

        //calculate fuel consumption for each distance
        for (int i = 0; i < distances.size(); i++){
            consumption.add((double) fuelAmount.get(i)/distances.get(i)*100);
        }

        //result for L/100km unit
        double sum = 0;
        averageLkm = 0;
        maxLkm = 0;
        minLkm = consumption.get(0);
        //minLkm = 0;
        lastLkm = 0;
        //calculate the average of consumption, max and min
        for (double value : consumption) {
            sum += value;
            averageLkm = sum / consumption.size();
            if (value > maxLkm) {
                maxLkm = value;
            }
            if (value < minLkm) {
                minLkm = value;
            }
            lastLkm = value;
        }

        //result for Mpg unit
        averageMpg = 235.214 / averageLkm;
        maxMpg = 235.214 / maxLkm;
        minMpg = 235.214 / minLkm;
        lastMpg = 235.214 / lastLkm;

        unit = "lkm";

        if (unit.equals("lkm")) {
            average = averageLkm;
            max = maxLkm;
            min = minLkm;
            last = lastLkm;
        } else {
            average = averageMpg;
            max = maxMpg;
            min = minMpg;
            last = lastMpg;
        }

        translate();
    }

    public void translate() {
        if (unit.equals("lkm")) {
            average = averageLkm;
            max = maxLkm;
            min = minLkm;
            last = lastLkm;
        } else {
            average = averageMpg;
            max = maxMpg;
            min = minMpg;
            last = lastMpg;
        }

        //and text to textviews
        average = Math.round(average * 100.0) / 100.0;//round up to 2 decimal numbers
        TextView average_text_view = (TextView) findViewById(R.id.average);
        average_text_view.setText(Double.toString(average));

        max = Math.round(max * 100.0) / 100.0;
        TextView max_text_view = (TextView) findViewById(R.id.max);
        max_text_view.setText(Double.toString(max));

        min = Math.round(min * 100.0) / 100.0;
        TextView min_text_view = (TextView) findViewById(R.id.min);
        min_text_view.setText(Double.toString(min));

        last = Math.round(last * 100.0) / 100.0;
        TextView last_text_view = (TextView) findViewById(R.id.last);
        last_text_view.setText(Double.toString(last));
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_l100km:
                if (checked) {
                    unit = "lkm";
                }
                break;
            case R.id.radio_mpg:
                if (checked) {
                    unit = "mpg";
                }
                break;
        }
        translate();
    }

    public void add(View view) {
        Intent getNewData = new Intent(this, Second.class);
        startActivity(getNewData);

        SharedPreferences prefPut = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();

        Set<String> set = new HashSet<String>();
        set.addAll(data);
        prefEditor.putStringSet("key", set);
        prefEditor.commit();
        //translate();
    }

    public void showHistory(View view) {
    }
}