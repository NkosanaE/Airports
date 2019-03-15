package com.dvt_airpots.Model;

public class Flight {

    private String number;
    private String iataNumber;
    private String icaoNumber;


    // Getter Methods

    public String getNumber() {
        return number;
    }

    public String getIataNumber() {
        return iataNumber;
    }

    public String getIcaoNumber() {
        return icaoNumber;
    }

    // Setter Methods

    public void setNumber(String number) {
        this.number = number;
    }

    public void setIataNumber(String iataNumber) {
        this.iataNumber = iataNumber;
    }

    public void setIcaoNumber(String icaoNumber) {
        this.icaoNumber = icaoNumber;
    }
}
