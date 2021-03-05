package com.ebbrechtair.classes;

public class Navaid extends GeoCoordinate {
    private String navaidID;
    private String navaidName;
    private Double frequency;
    private int radialCapability;
    private int dmeCapability;
    private String a01;
    private int altitude;
    private String areaCode;
    private String a02;

    public Navaid(Double lat, Double lon, String navaidID, String navaidName, Double frequency, int radialCapability, int dmeCapability, String a01, int altitude, String areaCode, String a02) {
        super(lat, lon);
        this.navaidID = navaidID;
        this.navaidName = navaidName;
        this.frequency = frequency;
        this.radialCapability = radialCapability;
        this.dmeCapability = dmeCapability;
        this.a01 = a01;
        this.altitude = altitude;
        this.areaCode = areaCode;
        this.a02 = a02;
    }

    public class DME{

    }

    public class VOR{

    }

    public class VOR_DME{

    }

    public class NDB{

    }
}