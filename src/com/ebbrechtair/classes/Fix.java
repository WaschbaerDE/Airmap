package com.ebbrechtair.classes;

public class Fix extends GeoCoordinate {
    private String fixID;
    private String areacode;

    public Fix(Double lat, Double lon, String fixID, String areacode) {
        super(lat, lon);
        this.fixID = fixID;
        this.areacode = areacode;
    }
}