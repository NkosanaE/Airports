package com.dvt_airpots.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.dvt_airpots.FlightDetailsActivity;
import com.dvt_airpots.MainActivity;
import com.dvt_airpots.Model.Airport;
import com.dvt_airpots.Model.FlightAirportError;
import com.dvt_airpots.Model.FlightDeparture;
import com.dvt_airpots.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FlightRequestHelper {

    private RequestQueue requestQueue = null;

    String url;

    String TAG =this.getClass().getName();

    MainActivity mainActivity;

    public FlightRequestHelper(MainActivity mainActivity){
        this.mainActivity =mainActivity;
        requestQueue = VolleyRequestHelper.getInstance(mainActivity).getRequestQueue();
    }

    public void doNearbyRequest() {

        mainActivity.mGoogleMap.clear();
        url = mainActivity.getResources().getString(R.string.apiBaseUrl)
                +mainActivity.getResources().getString(R.string.apiKey)
                +"lat="+mainActivity.latitude
                +"&lng="+mainActivity.longitude
                +mainActivity.getResources().getString(R.string.apiDistance);

        StringRequest registerConsumer = new StringRequest(Request.Method.GET, url, // URL
                createNearbyReqSuccessListener(),
                createReqErrorListener());
        //createProgress(R.string.nfc_customer_registration);
        requestQueue.add(registerConsumer); // make sure that this is not null
    }

    public void doFlightsRequest(String aitaCode) {

        url = "http://aviation-edge.com/v2/public/timetable?key=021988-a0dbb9&iataCode="+aitaCode+"&type=departure";

        StringRequest registerConsumer = new StringRequest(Request.Method.GET, url, // URL
                createFlightsReqSuccessListener(),
                createReqErrorListener());
        //createProgress(R.string.nfc_customer_registration);
        requestQueue.add(registerConsumer); // make sure that this is not null
    }



    public Response.Listener<String> createNearbyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);
                //JSONArray jsonArray = new JSONArray(response);

                try {
                    Type listType = new TypeToken<ArrayList<Airport>>() {
                    }.getType();
                    mainActivity.airports = new Gson().fromJson(response.toString(), listType);
                    if (mainActivity.airports != null && mainActivity.airports.size() > 0) {

                        handleResponseMessage(true, "Successful");
                    } else { // failed

                        handleResponseMessage(false, "Something went wrong. Please try again");
                    }
                }catch (Exception ex){
                    try {
                        Log.d(TAG, "onResponse: "+ex);
                        FlightAirportError error = new Gson().fromJson(response,FlightAirportError.class);
                        if (error.getError().getText().toLowerCase().contains("no record")) {
                            mainActivity.createAlertDialog(mainActivity.airportName, "There are currently no airports located in your area");
                        }else{
                            mainActivity.createAlertDialog(mainActivity.airportName, error.getError().getText());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    public Response.Listener<String> createFlightsReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Type listType = new TypeToken<ArrayList<FlightDeparture>>() {}.getType();
                    mainActivity.flightDepartures = new Gson().fromJson(response.toString(), listType);
                    if (mainActivity.flightDepartures != null && mainActivity.flightDepartures.size() > 0) {

                        handleFlightsResponseMessage(true, "Successful");
                    }
                }catch (Exception ex){
                    try {
                        FlightAirportError error = new Gson().fromJson(response, FlightAirportError.class);
                        if (error.getError().getText().toLowerCase().contains("no record")) {
                            mainActivity.createAlertDialog(mainActivity.airportName, "There are currently no flights in this airport");
                        } else {
                            mainActivity.createAlertDialog(mainActivity.airportName, error.getError().getText());
                        }
                        Log.d(TAG, "onResponse: " + ex);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    //----------------------------------------------------------------------------------------------
    public Response.ErrorListener createReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("error: " + error);
                String message = new String();

                VolleyLog.e("error.getMessage(): " + error.getMessage());
                VolleyLog.e("error.networkResponse: " + error.networkResponse);
                String errorMessage = new String();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        errorMessage = new String(error.networkResponse.data, "UTF-8"); // for UTF-8 encoding
                        VolleyLog.e("errorMessage: " + errorMessage);
                        Error error1 = new Gson().fromJson(errorMessage, Error.class);
                        message = error1.getMessage();
                    } catch (Exception e) {

                        e.printStackTrace();
                        message = "An unknown error occurred";
                    }
                } else {
                    message = "Could not connect";
                }


                handleResponseMessage(false, message); // propagate the server error message
            }

        };
    }

    void handleResponseMessage(final boolean responseIsSuccessful, String message) {

      //  Bitmap sd = Bitmap.createBitmap(R.drawable.ic_pin)

        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.ic_pin);


        if (mainActivity.airports.size()>0) {

            for (Airport airport : mainActivity.airports) {

                mainActivity.mGoogleMap.setOnMarkerClickListener(mainActivity);

                LatLng latLng = new LatLng(Double.parseDouble(airport.getLatitudeAirport()), Double.parseDouble(airport.getLongitudeAirport()));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(airport.getNameAirport());
                markerOptions.icon(markerIcon);
                mainActivity.mGoogleMap.addMarker(markerOptions);



                mainActivity.markerAirportHashMap.put(markerOptions, airport);


            }
        }


    }

    void handleFlightsResponseMessage(final boolean responseIsSuccessful, String message) {

        if (mainActivity.flightDepartures.size()>0){

            Intent i = new Intent(mainActivity.getApplicationContext(), FlightDetailsActivity.class);
            i.putExtra("airport",mainActivity.airportName);
            i.putExtra("city",mainActivity.airportCity);
             mainActivity.startActivity(i);

        }

    }


    private BitmapDescriptor vectorToBitmap(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(mainActivity.getResources(), id, null);
        assert vectorDrawable != null;
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        //DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
