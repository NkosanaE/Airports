package com.dvt_airpots.Model;

public class APIUrl {

    String base;
    String key;
    String  latLong;
    String distance;

    public APIUrl(String base, String key, String latlong,String distance) {
        this.base = base;
        this.key = key;
        this.latLong =latlong;
        this.distance = distance;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return  base  + ", key='" + key +  distance;
    }
}
