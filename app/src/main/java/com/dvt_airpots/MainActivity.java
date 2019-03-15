package com.dvt_airpots;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.dvt_airpots.Helpers.FlightRequestHelper;
import com.dvt_airpots.Helpers.VolleyRequestHelper;
import com.dvt_airpots.Model.APIUrl;
import com.dvt_airpots.Model.Airport;
import com.dvt_airpots.Model.FlightDeparture;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String TAG = MainActivity.class.toString();
    public GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    public double latitude,longitude;

    String url;

    private RequestQueue requestQueue = null;

    public ArrayList<Airport> airports;
   public static ArrayList<FlightDeparture> flightDepartures;
   public HashMap<MarkerOptions,Airport> markerAirportHashMap;

    public String airportName,airportCity;

    FlightRequestHelper flightRequestHelper;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Nearby Flights");

        flightRequestHelper = new FlightRequestHelper(this);
        markerAirportHashMap = new HashMap<>();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);


        requestQueue = VolleyRequestHelper.getInstance(this).getRequestQueue();

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkPermissions();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }



    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                mLastLocation = location;

                //zoom to current location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                flightRequestHelper.doNearbyRequest();

                mGoogleMap.addCircle( new CircleOptions()
                        .center(latLng)   //set center
                        .radius(10000)   //set radius in meters
                        .fillColor(0x551e90ff)  //default
                        .strokeColor(Color.TRANSPARENT));

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));



            }
        }
    };

    protected void checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Do something, when permissions not granted
                if ( ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // If we should give explanation of requested permissions

                    // Show an alert dialog here with request explanation
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Location" +
                            " permission is required.");
                    builder.setTitle("Please grant this permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                    },
                                    MY_PERMISSIONS_REQUEST_LOCATION
                            );
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Directly request for required permissions, without explanation
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            },
                            MY_PERMISSIONS_REQUEST_LOCATION
                    );
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        for (Airport airport : markerAirportHashMap.values() ){

            if (marker.getTitle().equals(airport.getNameAirport())){

                String iataCode = airport.getCodeIataAirport();

                airportName =airport.getNameAirport();
                airportCity = getCity(Double.parseDouble(airport.getLatitudeAirport()),Double.parseDouble(airport.getLongitudeAirport()));
                flightRequestHelper.doFlightsRequest(iataCode);

            }

        }
        return true;

    }

    private String getCity(double lat,double lng){
        try {
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
               return addresses.get(0).getLocality();
            }
            else {
                return "Unavailable";

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
       return "Unavailable";
    }


    public void createAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}





