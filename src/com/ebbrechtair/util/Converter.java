package com.ebbrechtair.util;

public class Converter {

    public Double ConvertLatToYInKm(double lat){ return 111.3 * lat;}

    public Double ConvertLonToXInKm(double lat, double lon){ return 111.3 * Math.cos(lat*Math.PI/180) * lon;}
//von km
    //public Double ConvertYInLat(double y){return y / 111.3;}

    //public Double ConvertXInLon(double x, double y){return x / (111.3 * Math.cos(ConvertYInLat(y)*Math.PI/180));}
//von seemeilen
    public Double ConvertYInLat(double y){return y / 60;}
    public Double ConvertXInLon(double x, double y){return x /60;}
    public Double ConvertLatInY(double lat){return lat *60;}
    public Double ConvertLonInX(double lon){return lon *60;}



}
