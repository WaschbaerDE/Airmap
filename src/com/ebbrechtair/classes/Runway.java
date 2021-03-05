package com.ebbrechtair.classes;


public class Runway extends GeoCoordinate {
    private String airportcode;
    private String runwayIdentifier;
    private int heading;
    private int maxTakeoffLength;
    private int runwayWidthFeet;
    private String locils;
    private Double iLSFrequency1;
    private Double iLSFrequency2;
    private int touchdownAltitude;
    private Double approachGlideslope;
    private String a01;
    private String a02;
    private String a03;

    public Runway(Double lat, Double lon, String airportcode, String runwayIdentifier, int heading, int maxTakeoffLength, int runwayWidthFeet, String locils, Double iLSFrequency1, Double iLSFrequency2, int touchdownAltitude, Double approachGlideslope, String a01, String a02, String a03) {
        super(lat, lon);
        this.airportcode = airportcode;
        this.runwayIdentifier = runwayIdentifier;
        this.heading = heading;
        this.maxTakeoffLength = maxTakeoffLength;
        this.runwayWidthFeet = runwayWidthFeet;
        this.locils = locils;
        this.iLSFrequency1 = iLSFrequency1;
        this.iLSFrequency2 = iLSFrequency2;
        this.touchdownAltitude = touchdownAltitude;
        this.approachGlideslope = approachGlideslope;
        this.a01 = a01;
        this.a02 = a02;
        this.a03 = a03;
    }
}

