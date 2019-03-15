package com.dvt_airpots.Model;

public class AirLine {

    private String name;
    private String iataCode;
    private String icaoCode;


    // Getter Methods

    public String getName() {
        return name;
    }

    public String getIataCode() {
        return iataCode;
    }

    public String getIcaoCode() {
        return icaoCode;
    }

    // Setter Methods

    public void setName(String name) {
        this.name = name;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }
}
