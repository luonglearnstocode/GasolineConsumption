package com.example.android.gasolineconsumption;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class History extends AppCompatActivity {
    private static final String PREF = "TestPref";
    private Set<String> set = new HashSet<String>();
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SharedPreferences prefGet = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        set = prefGet.getStringSet("data", null);
        data = new ArrayList<String>();
        data.addAll(set);
        Collections.sort(data);
        Collections.reverse(data);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);

    }


    public void clear(View view) {
        SharedPreferences prefPut = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        set = new HashSet<String>();
        prefEditor.clear();
        prefEditor.putStringSet("data", set);
        prefEditor.commit();

        Intent backToMain = new Intent(this, MainActivity.class);
        startActivity(backToMain);
    }
}
