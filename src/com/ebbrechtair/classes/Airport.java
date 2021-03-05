package com.ebbrechtair.classes;

public class Airport extends GeoCoordinate {
    private String icaoCode;
    private String airportName;
    private int altitudeAirportInFeet;
    private String a01;
    private String a02;
    private int maxRunwayLength;
    private String b01;

    public Airport(Double lat, Double lon, String icaoCode, String airportName, int altitudeAirportInFeet, String a01, String a02, int maxRunwayLength, String b01) {
        super(lat, lon);
        this.icaoCode = icaoCode;
        this.airportName = airportName;
        this.altitudeAirportInFeet = altitudeAirportInFeet;
        this.a01 = a01;
        this.a02 = a02;
        this.maxRunwayLength = maxRunwayLength;
        this.b01 = b01;
    }
}
