package com.dvt_airpots;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.dvt_airpots.Adapters.FlightListAdapter;
import com.dvt_airpots.Model.FlightDeparture;

import java.util.ArrayList;

public class FlightDetailsActivity extends AppCompatActivity {

    FlightListAdapter flightListAdapter;

    TextView txtAirportName;
    TextView txtAirportCity;

    private ListView listView;

    private String airportName,airportCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);
        txtAirportName =(TextView) findViewById(R.id.airPortNameTextView);
        txtAirportCity =(TextView) findViewById(R.id.airPortLocationTextView);
        listView = (ListView) findViewById(R.id.flightsList);


        Intent i =getIntent();
        airportName = i.getStringExtra("airport");
        airportCity = i.getStringExtra("city");


        txtAirportName.setText(airportName);
        txtAirportCity.setText(airportCity);
        populateFligths();

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void populateFligths(){

        flightListAdapter = new FlightListAdapter(getApplicationContext(),R.layout.flight_row_item,MainActivity.flightDepartures);
        listView.setAdapter(flightListAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;
    }

}
