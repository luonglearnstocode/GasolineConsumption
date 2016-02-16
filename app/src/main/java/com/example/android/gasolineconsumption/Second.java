package com.example.android.gasolineconsumption;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Second extends AppCompatActivity {
    private static final String PREF = "TestPref";
    private Set<String> set;
    private String dataAdded;
    private ArrayList<String> data;
    private int odometerValue = 0;
    private int fuelAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //data = new ArrayList<String>();
//        data.add("16.12.2015\t120567\t42");
//        data.add("2.1.2016\t120869\t38");
//        data.add("9.1.2016\t121146\t43");
//        data.add("13.1.2016\t121459\t39");


        SharedPreferences prefGet = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        set = prefGet.getStringSet("key", null);
        data = new ArrayList<String>();
        data.addAll(set);
//        EditText odometer = (EditText) findViewById(R.id.odometer_value);
//        odometerValue = Integer.parseInt(odometer.getText().toString());
//
//        EditText fuel = (EditText) findViewById(R.id.fuel_amount);
//        fuelAmount = Integer.parseInt(fuel.getText().toString());
//
//        dataAdded = "date/t" + odometerValue + "/t" + fuelAmount;


    }

    public void confirm(View view) {
        EditText odometer = (EditText) findViewById(R.id.odometer_value);
        odometerValue = Integer.parseInt(odometer.getText().toString());

        EditText fuel = (EditText) findViewById(R.id.fuel_amount);
        fuelAmount = Integer.parseInt(fuel.getText().toString());

        dataAdded = "16.2.2016" + "/t" + odometerValue + "/t" + fuelAmount;

        SharedPreferences prefPut = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();

        data.add(dataAdded);
        set = new HashSet<String>();
        set.addAll(data);

        prefEditor.putStringSet("key", set);
        prefEditor.commit();

//        Intent main = new Intent(this, MainActivity.class);
//        startActivity(main);
        finish();
    }
}
