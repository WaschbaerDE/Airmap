package com.ebbrechtair.classes;

public abstract class Navaid extends GeoCoordinate {
    private String navaidID;
    private String navaidName;
    private Double frequency;
    private boolean radialCapability;
    private boolean dmeCapability;
    private String a01;
    private int altitude;
    private String areaCode;
    private String a02;

    public  Navaid( String navaidID, String navaidName, Double lat, Double lon, Double frequency, int radialCapability, int dmeCapability, String a01, int altitude, String areaCode, String a02) {
        super(lat, lon);
        this.navaidID = navaidID;
        this.navaidName = navaidName;
        this.frequency = frequency;
        if(radialCapability==1){
            this.radialCapability = true;
        }else{
            this.dmeCapability = false;
        }
        if(dmeCapability==1){
            this.dmeCapability = true;
        }else{
            this.dmeCapability = false;
        }
        this.a01 = a01;
        this.altitude = altitude;
        this.areaCode = areaCode;
        this.a02 = a02;
    }

    public static class DME extends Navaid{

        public DME(Double lat, Double lon, String navaidID, String navaidName, Double frequency, int radialCapability, int dmeCapability, String a01, int altitude, String areaCode, String a02) {
            super( navaidID, navaidName,lat, lon, frequency, radialCapability, dmeCapability, a01, altitude, areaCode, a02);
        }
    }

    public static class VOR extends Navaid{
        public VOR(Double lat, Double lon,String navaidID, String navaidName,  Double frequency, int radialCapability, int dmeCapability, String a01, int altitude, String areaCode, String a02) {
            super(navaidID, navaidName, lat, lon, frequency, radialCapability, dmeCapability, a01, altitude, areaCode, a02);
        }
    }

    public static class VOR_DME extends Navaid{
        public VOR_DME(Double lat, Double lon, String navaidID, String navaidName, Double frequency, int radialCapability, int dmeCapability, String a01, int altitude, String areaCode, String a02) {
            super(navaidID, navaidName, lat, lon, frequency, radialCapability, dmeCapability, a01, altitude, areaCode, a02);
        }
    }

    public static class NDB extends Navaid{
        public NDB(Double lat, Double lon, String navaidID, String navaidName, Double frequency, int radialCapability, int dmeCapability, String a01, int altitude, String areaCode, String a02) {
            super(navaidID, navaidName, lat, lon, frequency, radialCapability, dmeCapability, a01, altitude, areaCode, a02);
        }
    }
}