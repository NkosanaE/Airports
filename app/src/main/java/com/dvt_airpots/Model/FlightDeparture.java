package com.dvt_airpots.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightDeparture  {

    private String type;
    private String status;
    Departure departure;
    public Arrival arrival;
    public AirLine airline;
    Flight flight;

    public FlightDeparture(String type, String status, Departure departureObject, Arrival arrivalObject, AirLine airlineObject, Flight flightObject) {
        this.type = type;
        this.status = status;
        this.departure = departureObject;
        this.arrival = arrivalObject;
        this.airline = airlineObject;
        this.flight = flightObject;
    }


    // Getter Methods

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Departure getDeparture() {
        return departure;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public AirLine getAirline() {
        return airline;
    }

    public Flight getFlight() {
        return flight;
    }

    // Setter Methods

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeparture(Departure departureObject) {
        this.departure = departureObject;
    }

    public void setArrival(Arrival arrivalObject) {
        this.arrival = arrivalObject;
    }

    public void setAirline(AirLine airlineObject) {
        this.airline = airlineObject;
    }

    public void setFlight(Flight flightObject) {
        this.flight = flightObject;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//    }
}
