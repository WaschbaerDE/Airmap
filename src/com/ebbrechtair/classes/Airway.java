package com.ebbrechtair.classes;

public class Airway extends GeoCoordinate{
    private String atsID;
    private int atsPartition;
    private String fixID;
    private Double lon;
    private Double lat;
    private String iDOfNextFix;
    private Double lonNext;
    private Double latNext;
    private int inBoundCourse;
    private int outBoundCourse;
    private Double legLength;

    public Airway(String atsID, int atsPartition, String fixID, Double lon, Double lat, String iDOfNextFix, Double lonNext, Double latNext, int inBoundCourse, int outBoundCourse, Double legLength) {
        super(lat,lon);
        this.atsID = atsID;
        this.atsPartition = atsPartition;
        this.fixID = fixID;
        this.iDOfNextFix = iDOfNextFix;
        this.lonNext = lonNext;
        this.latNext = latNext;
        this.inBoundCourse = inBoundCourse;
        this.outBoundCourse = outBoundCourse;
        this.legLength = legLength;
    }
}
