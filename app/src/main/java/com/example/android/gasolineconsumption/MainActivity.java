package com.example.android.gasolineconsumption;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String PREF = "TestPref";
    private final int GET_SECACT_RES = 6;
    private ArrayList<Integer> odometerValue = new ArrayList<Integer>();
    private ArrayList<Integer> distances = new ArrayList<Integer>();
    private ArrayList<Integer> fuelAmount = new ArrayList<Integer>();
    private ArrayList<Double> consumption = new ArrayList<Double>();
    private String unit;
    private String unit_distance;
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
    private double sum_hourly;
    private int sum_hourly_km;
    private double sum_hourly_mile;
    private double sum_minute;
    private int sum_minute_km;
    private double sum_minute_mile;
    private String advise;
    private ArrayList<String> data = new ArrayList<String>();
    private Set<String> set = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //default unit
        unit = "lkm";
        unit_distance = "km";

        //initial value
        averageLkm = 0;
        maxLkm = 0;
        minLkm = 0;
        lastLkm = 0;
        averageMpg = 0;
        maxMpg = 0;
        minMpg = 0;
        lastMpg = 0;
        sum_hourly_km = 0;
        sum_hourly_mile = 0;
        sum_minute_km = 0;
        sum_minute_mile = 0;

        SharedPreferences prefGet = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        set = prefGet.getStringSet("data", null);

        data = new ArrayList<String>();
        if (set != null) {
            data.addAll(set);
            Collections.sort(data);
        }
        calculate();
    }

    public void translate() {
        if (unit.equals("lkm")) {
            average = averageLkm;
            max = maxLkm;
            min = minLkm;
            last = lastLkm;
            unit_distance = "km";
            sum_hourly = sum_hourly_km;
            sum_minute = sum_minute_km;
        } else {
            average = averageMpg;
            max = maxMpg;
            min = minMpg;
            last = lastMpg;
            unit_distance = "miles";
            sum_hourly = sum_hourly_mile;
            sum_minute = sum_minute_mile;
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

        sum_hourly = Math.round(sum_hourly * 100.0) / 100.0;
        TextView this_hour_distance = (TextView) findViewById(R.id.hourly);
        this_hour_distance.setText(sum_hourly + " " + unit_distance);

        sum_minute = Math.round(sum_minute * 100.0) / 100.0;
        TextView this_minute_distance = (TextView) findViewById(R.id.minute);
        this_minute_distance.setText(sum_minute + " " + unit_distance);
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
        if (odometerValue.size() > 0) {
            //pass the lower limit of the input for odometer value in the next activity
            //new value should not be lower than the latest value
            getNewData.putExtra("lim", odometerValue.get(odometerValue.size() - 1));
        } else {
            getNewData.putExtra("lim", 0);
        }
        startActivityForResult(getNewData, GET_SECACT_RES);
    }

    public void calculate() {
        TextView average_text_view = (TextView) findViewById(R.id.average);
        TextView max_text_view = (TextView) findViewById(R.id.max);
        TextView min_text_view = (TextView) findViewById(R.id.min);
        TextView last_text_view = (TextView) findViewById(R.id.last);
        TextView this_hour_distance = (TextView) findViewById(R.id.hourly);
        TextView advise_text_view = (TextView) findViewById(R.id.advise);

        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.dd.MM 'at' hh:mm:ss");
        String this_hour = ft.format(date).substring(0, 17);
        String this_minute = ft.format(date).substring(0, 20);

        if (data.size() < 2) {
            average_text_view.setText("0.00");
            max_text_view.setText("0.00");
            min_text_view.setText("0.00");
            last_text_view.setText("0.00");
            this_hour_distance.setText("0.00");
        } else {
            odometerValue = new ArrayList<Integer>();
            distances = new ArrayList<Integer>();
            fuelAmount = new ArrayList<Integer>();
            consumption = new ArrayList<Double>();

            for (String datum : data) {
                String[] datumsplitted = datum.split("\t\t\t\t\t\t");
                odometerValue.add(Integer.parseInt(datumsplitted[1]));
                fuelAmount.add(Integer.parseInt(datumsplitted[2]));
            }

            //add value to distances list - difference of odometer value
            for (int i = 1; i < odometerValue.size(); i++) {
                distances.add(odometerValue.get(i) - odometerValue.get(i - 1));
            }

            //calculate fuel consumption for each distance
            for (int i = 0; i < distances.size(); i++) {
                consumption.add((double) fuelAmount.get(i + 1) / distances.get(i) * 100);
            }
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

            if (averageLkm > 20) {
                advise = "You should consider buying a new car!";
            } else if (averageLkm > 10) {
                advise = "Your car is a gas-guzzler!";
            } else if (averageLkm > 6) {
                advise = "Your car is pretty fuel efficient!";
            } else if (averageLkm > 2) {
                advise = "Your car is very fuel efficient!";
            } else {
                advise = "Your fuel efficiency is incredible!";
            }
            advise_text_view.setText(advise);

            sum_hourly_km = 0;
            sum_minute_km = 0;
            for (String datum : data) {
                String[] datumsplitted = datum.split("\t\t\t\t\t\t");
                if (datumsplitted[0].substring(0, 17).equals(this_hour)) {
                    int i = odometerValue.indexOf(Integer.parseInt(datumsplitted[1])) - 1;
                    Log.i("i", String.valueOf(i));
                    if (i >= 0) {
                        sum_hourly_km += distances.get(i);
                    }
                }
                if (datumsplitted[0].substring(0, 20).equals(this_minute)) {
                    int i = odometerValue.indexOf(Integer.parseInt(datumsplitted[1])) - 1;
                    if (i >= 0) {
                        sum_minute_km += distances.get(i);
                    }
                }
            }
            sum_hourly_mile = sum_hourly_km * 0.621371;
            sum_minute_mile = sum_minute_km * 0.621371;
        }
        translate();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent getData) {
        if (requestCode == GET_SECACT_RES) {
            if (resultCode == Activity.RESULT_OK) {
                String newData = getData.getStringExtra("newdata");
                data.add(newData);
                String[] newDataSplitted = newData.split("\t\t\t\t\t\t");
                odometerValue.add(Integer.parseInt(newDataSplitted[1]));

                calculate();
            }
        }
    }

    public void showHistory(View view) {
        SharedPreferences prefPut = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        set = new HashSet<String>();
        set.addAll(data);
        prefEditor.clear();
        prefEditor.putStringSet("data", set);
        prefEditor.commit();

        Intent showHistory = new Intent(this, History.class);
        startActivity(showHistory);

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefPut = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        set = new HashSet<String>();
        set.addAll(data);
        prefEditor.clear();
        prefEditor.putStringSet("data", set);
        prefEditor.commit();
    }

}