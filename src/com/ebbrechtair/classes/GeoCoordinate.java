package com.ebbrechtair.classes;

public abstract class GeoCoordinate {
    private Double lat;
    private Double lon;

    public GeoCoordinate(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return this.lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public Double getYValue(){ return 111.3 * this.lat;}

    public Double getXValue(){ return 111.3 * Math.cos(this.lat) * this.lon;}

}
