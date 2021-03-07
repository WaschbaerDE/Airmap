package com.ebbrechtair.javafx;

import com.ebbrechtair.classes.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;

public class Map extends Canvas {
    private final double[] middlepoint;//This is the 500|500 point / the middlepoint corner of the map/canvas in km
    private double zoomFaktor;//This is the faktor between pixel to distance in km 1000px = 200km BASIC
                                // VERDOPPELT SICH ALLE 4 RAD KLICKS
    private double lastX;
    private double lastY;

    private final GraphicsContext context = this.getGraphicsContext2D();

    public Map() {
        super(1000, 1000);
        this.zoomFaktor = 0.2;
        this.middlepoint = new double[]{0,0};

        drawGrit();
        drawGeoCoordinate(new Airport(0.0,0.0,"1","1",1,"1","1",1,"1"));
        drawGeoCoordinate(new Fix(0.1,0.1,"name","ac"));

        //This event get triggered on Scrolling with Mouse3-button
        //the new zoomfaktor is the old one times the scroll /40 * the faktor to zoom x2 with 4 wheel clicks
        setOnScroll(event -> {
            if(event.getDeltaY()>0){
                this.zoomFaktor = this.zoomFaktor*(event.getDeltaY()/40)/Math.pow(2,0.25);

                this.middlepoint[0] = this.middlepoint[0]+((event.getX()-500)*this.zoomFaktor/4);
                this.middlepoint[1] =  this.middlepoint[1]+((500-event.getY())*this.zoomFaktor/4);
                System.out.println("Zoom-In-Event: "+this.middlepoint[0]+" "+ this.middlepoint[1]+" Zoom: 1000px ="+this.zoomFaktor*1000+"km");

            }
            if(event.getDeltaY()<0){
                this.zoomFaktor = -this.zoomFaktor/((event.getDeltaY()/40)/Math.pow(2,0.25));

                this.middlepoint[0] = this.middlepoint[0]-((event.getX()-500)*this.zoomFaktor/4);
                this.middlepoint[1] =  this.middlepoint[1]-((500-event.getY())*this.zoomFaktor/4);
                System.out.println("Zoom-Out-Event: X: "+this.middlepoint[0]+" Y: "+ this.middlepoint[1]+" Zoom: 1000px ="+this.zoomFaktor*1000+"km");
            }
            clearMap();
            drawGrit();


            drawGeoCoordinate(new Airport(0.0,0.0,"1","1",1,"1","1",1,"1"));
            drawGeoCoordinate(new Fix(0.1,0.1,"name","ac"));


        });
        setOnMousePressed (event -> {
            this.lastX = event.getX();
            this.lastY = event.getY();
        });
        //This event get triggered while dragging(holding Mouse1 und dragging the Mouse)
        setOnMouseDragged(event -> {

            this.middlepoint[0] = this.middlepoint[0] - (event.getX() - this.lastX) * this.zoomFaktor;
            this.middlepoint[1] = this.middlepoint[1] - (this.lastY - event.getY()) * this.zoomFaktor;
            this.lastX = event.getX();
            this.lastY = event.getY();

            clearMap();
            drawGrit();


            drawGeoCoordinate(new Airport(0.0,0.0,"1","1",1,"1","1",1,"1"));
            drawGeoCoordinate(new Fix(0.1,0.1,"name","ac"));


            System.out.println("Dragevent: "+this.middlepoint[0]+" "+this.middlepoint[1]+" Zoom: 1000px ="+this.zoomFaktor*1000+"km");
        });
    }

    //gets coords of leftuppercorner in km
    public double[] getLeftUpperCorner() {
        return new double[]{this.middlepoint[0] - (500 * this.zoomFaktor),this.middlepoint[1] + (500 * this.zoomFaktor)};
    }

    //gets coords of rigthbottomcorner in km
    public double[] getRigthBottomCorner() {
        return new double[]{this.middlepoint[0] + (500 * this.zoomFaktor),this.middlepoint[1] - (500 * this.zoomFaktor)};
    }

    //draws basic grid on map (iterates many drawGRitLine methodes)
    private void drawGrit(){
        for(int i = 0; i < 10000; i = i+100) {
            drawGritLines(i, 0, i, 9000);
            drawGritLines(0, i, 9000, i);
        }
        context.setLineWidth(2);
        context.setStroke(Color.LIGHTSALMON);
        context.strokeLine(500,0, 500, 1000);
        context.strokeLine(0,500, 1000, 500);
    }

    //draws a line from point x1 y1 to x2 y2
    private void drawGritLines(int x1, int y1, int x2, int y2) {
        context.setLineWidth(0.5);
        context.setStroke(Color.BLACK);
        context.strokeLine(x1, y1, x2, y2);
    }

    //Drag and drop funktioniert bei ersten mal super nach dem los lassen geht es nicht
//pfad muss relativ gestalten werden!
    private void drawGeoCoordinate(GeoCoordinate geoCoordinate){
        String url ="file:\\E:\\Dokumente\\IntelliJ\\Airmap\\src\\com\\ebbrechtair\\icons\\Transparent_Number_SansSerif_Black_";
        if(geoCoordinate instanceof Airport){
            //Airport
            url += "1";
        }else if(geoCoordinate instanceof Fix){
            //Fix
            url += "2";
        }else if(geoCoordinate instanceof Navaid){
            if(1==1){
                //DME
                url += "3";
            }else if(1==1){
                //NDB
                url += "4";
            }else if(1==1){
                //VOR
                url += "5";
            }else if(1==1){
                //VOR_DME
                url += "6";
            }

        }

        Double x = (geoCoordinate.getXValue()-this.middlepoint[0])/this.zoomFaktor;
        Double y = (geoCoordinate.getYValue()+this.middlepoint[1])/this.zoomFaktor;
        Double iconsizeX = 20.0;
        Double iconsizeY = 20.0;

        context.drawImage(new Image(url),x-(iconsizeX/2)+(-this.middlepoint[0]/4*this.zoomFaktor+500),y-(iconsizeY/2)+(this.middlepoint[1]/4*this.zoomFaktor+500),iconsizeX,iconsizeY);


    }

    private void clearMap(){
        context.clearRect(0,0,1000,1000);
    }


}
