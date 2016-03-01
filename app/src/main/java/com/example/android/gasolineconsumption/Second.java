package com.example.android.gasolineconsumption;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Second extends AppCompatActivity {
    private String dataAdded;
    private ArrayList<String> data;
    private int odometerValue = 0;
    private int fuelAmount = 0;
    private int lim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }

    public void confirm(View view) {
        lim = getIntent().getIntExtra("lim", 0);//last odometer input value, if not exist, set to 0
        EditText odometer = (EditText) findViewById(R.id.odometer_value);
        odometerValue = Integer.parseInt(odometer.getText().toString());

        if (odometerValue <= lim) { // new odometer value is not greater than the last value
            odometer.setError("please check your input");
        } else {
            EditText fuel = (EditText) findViewById(R.id.fuel_amount);
            fuelAmount = Integer.parseInt(fuel.getText().toString());
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy.dd.MM 'at' hh:mm:ss");
            dataAdded = ft.format(date) + "\t\t\t\t\t\t" + odometerValue + "\t\t\t\t\t\t" + fuelAmount;
            Intent back = new Intent();
            back.putExtra("newdata", dataAdded);
            setResult(RESULT_OK, back);
            finish();
        }
    }
}
