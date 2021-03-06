package com.ebbrechtair.javafx;

import com.ebbrechtair.classes.GeoCoordinate;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.beans.EventHandler;

public class Map extends Canvas {
    private double[] middlepoint;//This is the 500|500 point / the middlepoint corner of the map/canvas in km
    private double zoomFaktor;//This is the faktor between pixel to distance in km 1000px = 100km BASIC // VERDOPPELT SICH ALLE 4 RAD KLICKS
    private double lastX;
    private double lastY;


    private GraphicsContext context = this.getGraphicsContext2D();

    public Map() {
        super(1000, 1000);
        this.zoomFaktor = 0.1;
        this.middlepoint = new double[]{0,0};


        drawGrit();
        //drawTestAirways();

        //This event get triggered on Scrolling with Mouse3-button
        //the new zoomfaktor is the old one times the scroll /40 * the faktor to zoom x2 with 4 wheel clicks
        setOnScroll(event -> {
            if(event.getDeltaY()>0){
                this.zoomFaktor = this.zoomFaktor*(event.getDeltaY()/40)/Math.pow(2,0.25);
            }
            if(event.getDeltaY()<0){
                this.zoomFaktor = this.zoomFaktor*(-event.getDeltaY()/40)*Math.pow(2,0.25);
            }
        });

        //This event get triggered while dragging(holding Mouse1 und dragging the Mouse)
        setOnMouseDragged(event -> {
            if(this.lastX==0&&this.lastY==0) {
                this.lastX = event.getX();
                this.lastY = event.getY();
            }
            this.middlepoint[0] = middlepoint[0]+(event.getX()-this.lastX)*zoomFaktor;
            this.middlepoint[1] = middlepoint[1]+(this.lastY-event.getY())*zoomFaktor;
            this.lastX = event.getX();
            this.lastY = event.getY();

            System.out.println(this.middlepoint[0]+" "+this.middlepoint[1]);
        });

    }



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

    private void drawGritLines(int x1, int y1, int x2, int y2) {
        context.setLineWidth(0.5);
        context.setStroke(Color.BLACK);
        context.strokeLine(x1, y1, x2, y2);
    }



//    public void drawTestAirways(){
//        for (int i = 1; i < 30; i++) {
//            lineCoordinates[0] = (i*10);
//            lineCoordinates[1] = (i*20);
//            lineCoordinates[2] = (i*30);
//            lineCoordinates[3] = (i*40);
//            drawAirwayLines(lineCoordinates[0],lineCoordinates[1],lineCoordinates[2],lineCoordinates[3]);
//        }
//        lineCoordinates[0] = (30*10);
//        lineCoordinates[1] = (30*20);
//        lineCoordinates[2] = (30*30);
//        lineCoordinates[3] = (30*40);
////        colorAirwayRoutes(lineCoordinates[0],lineCoordinates[1],lineCoordinates[2],lineCoordinates[3]);
//    }

//    public void drawAirwayLines(int x1, int y1, int x2, int y2) {
//        context.setLineWidth(2);
//        context.setStroke(Color.GREEN);
//        context.strokeLine(x1, y1, x2, y2);
//    }

//    public void colorAirwayRoutes(int x1, int y1, int x2, int y2) {
//        context.setLineWidth(5);
//        context.setStroke(Color.MAGENTA);
//        context.strokeLine(x1, y1, x2, y2);
//    }
}

