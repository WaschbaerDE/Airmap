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

        //This event get triggered on Scrolling with Mouse3-button
        //the new zoomfaktor is the old one times the scroll /40 * the faktor to zoom x2 with 4 wheel clicks
        setOnScroll(event -> {
            if(event.getDeltaY()>0){
                this.zoomFaktor = this.zoomFaktor*(event.getDeltaY()/40)/Math.pow(2,0.25);

                this.middlepoint[0] = this.middlepoint[0]+((event.getX()-500)*this.zoomFaktor/4);
                this.middlepoint[1] =  this.middlepoint[1]+((500-event.getY())*this.zoomFaktor/4);
                System.out.println("Zoom-In: "+this.middlepoint[0]+" "+ this.middlepoint[1]);

            }
            if(event.getDeltaY()<0){
                this.middlepoint[0] = this.middlepoint[0]-((event.getX()-500)*this.zoomFaktor/4);
                this.middlepoint[1] =  this.middlepoint[1]-((500-event.getY())*this.zoomFaktor/4);
                System.out.println("Zoom-Out: "+this.middlepoint[0]+" "+ this.middlepoint[1]);
            }
            context.setFill(Color.WHITE);

            drawGeoCoordinate(new Airport(0.0,0.0,"1","1",1,"1","1",1,"1"));

        });

        //This event get triggered while dragging(holding Mouse1 und dragging the Mouse)
        setOnMouseDragged(event -> {
            if(this.lastX==0&&this.lastY==0) {
                this.lastX = event.getX();
                this.lastY = event.getY();
            }
            this.middlepoint[0] = this.middlepoint[0]-(event.getX()-this.lastX)*this.zoomFaktor;
            this.middlepoint[1] = this.middlepoint[1]-(this.lastY-event.getY())*this.zoomFaktor;
            this.lastX = event.getX();
            this.lastY = event.getY();

            context.clearRect(0,0,1000,1000);

            drawGeoCoordinate(new Airport(0.0,0.0,"1","1",1,"1","1",1,"1"));

            System.out.println(this.middlepoint[0]+" "+this.middlepoint[1]);
        });
    }

    //gets coords of leftuppercorner in km

    //WRONG
    public double[] getLeftUpperCorner() {
        return new double[]{(this.middlepoint[0]-500)*zoomFaktor,(this.middlepoint[1]+500)*zoomFaktor};
    }

    //gets coords of rigthbottomcorner in km

    //WRONG
    public double[] getRigthBottomCorner() {
        return new double[]{(this.middlepoint[0]+500)*zoomFaktor,(this.middlepoint[1]-500)*zoomFaktor};
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

    //Die Methode drawAirport nimmt einen Airport und malt ein bild von einem absoluten pfad auf die map
//pfad muss relativ gestalten werden!
    private void drawAirport(Airport airport){
        context.drawImage(new Image("file:\\E:\\Dokumente\\IntelliJ\\Airmap\\src\\com\\ebbrechtair\\icons\\Transparent_Number_1.png"),490,490,20,20);

    }

    //Drag and drop funktioniert bei ersten mal super nach dem los lassen geht es nicht
    private void drawGeoCoordinate(GeoCoordinate geoCoordinate){
        String url ="";
        if(geoCoordinate instanceof Airport){
            url = "file:\\E:\\Dokumente\\IntelliJ\\Airmap\\src\\com\\ebbrechtair\\icons\\Transparent_Number_1.png";
        }else if(geoCoordinate instanceof Fix){
            url = "file:\\E:\\Dokumente\\IntelliJ\\Airmap\\src\\com\\ebbrechtair\\icons\\Transparent_Number_2.png";
        }else if(geoCoordinate instanceof Navaid){
            url = "file:\\E:\\Dokumente\\IntelliJ\\Airmap\\src\\com\\ebbrechtair\\icons\\Transparent_Number_3.png";
        }

        Double x = (geoCoordinate.getXValue()-this.middlepoint[0])/this.zoomFaktor;
        Double y = (geoCoordinate.getYValue()+this.middlepoint[1])/this.zoomFaktor;
        Double iconsizeX = 20.0;
        Double iconsizeY = 20.0;

        context.drawImage(new Image(url),x-(iconsizeX/2)+(-this.middlepoint[0]/4*this.zoomFaktor+500),y-(iconsizeY/2)+(this.middlepoint[1]/4*this.zoomFaktor+500),iconsizeX,iconsizeY);


    }

}
