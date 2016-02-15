package com.example.android.gasolineconsumption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Integer> odometerValue = new ArrayList<Integer>();
        ArrayList<Integer> distances = new ArrayList<Integer>();
        ArrayList<Integer> fuelAmount = new ArrayList<Integer>();
        ArrayList<Double> consumption = new ArrayList<Double>();

        //list of initial data
        ArrayList<String> data = new ArrayList<String>();
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

        double sum = 0;
        double average = 0;
        double max = 0;
        double min = consumption.get(0);
        double last = consumption.get(0);
        //calculate the average of consumption, max and min
        for (double value : consumption) {
            sum += value;
            average = sum/consumption.size();
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
            last = value;
        }

        //and text to textviews
        average = Math.round(average*100.0)/100.0;//round up to 2 decimal numbers
        TextView average_text_view = (TextView) findViewById(R.id.average);
        average_text_view.setText(Double.toString(average));

        max = Math.round(max*100.0)/100.0;
        TextView max_text_view = (TextView) findViewById(R.id.max);
        max_text_view.setText(Double.toString(max));

        min = Math.round(min*100.0)/100.0;
        TextView min_text_view = (TextView) findViewById(R.id.min);
        min_text_view.setText(Double.toString(min));

        last = Math.round(last*100.0)/100.0;
        TextView last_text_view = (TextView) findViewById(R.id.last);
        last_text_view.setText(Double.toString(last));

    }

}