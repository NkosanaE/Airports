package com.dvt_airpots.Model;

public class Departure {

    private String iataCode;
    private String icaoCode;
    private String terminal;
    private String gate;
    private float delay;
    private String scheduledTime;
    private String estimatedTime;
    private String actualTime;
    private String estimatedRunway;
    private String actualRunway;


    // Getter Methods

    public String getIataCode() {
        return iataCode;
    }

    public String getIcaoCode() {
        return icaoCode;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getGate() {
        return gate;
    }

    public float getDelay() {
        return delay;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public String getActualTime() {
        return actualTime;
    }

    public String getEstimatedRunway() {
        return estimatedRunway;
    }

    public String getActualRunway() {
        return actualRunway;
    }

    // Setter Methods

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public void setEstimatedRunway(String estimatedRunway) {
        this.estimatedRunway = estimatedRunway;
    }

    public void setActualRunway(String actualRunway) {
        this.actualRunway = actualRunway;
    }
}
