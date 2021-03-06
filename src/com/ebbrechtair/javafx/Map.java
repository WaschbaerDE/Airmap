package com.ebbrechtair.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map extends Canvas {
    private final double[] middlepoint;//This is the 500|500 point / the middlepoint corner of the map/canvas in km
    private double zoomFaktor;//This is the faktor between pixel to distance in km 1000px = 100km BASIC 
                                // VERDOPPELT SICH ALLE 4 RAD KLICKS
    private double lastX;
    private double lastY;


    private final GraphicsContext context = this.getGraphicsContext2D();

    public Map() {
        super(1000, 1000);
        this.zoomFaktor = 0.1;
        this.middlepoint = new double[]{0,0};

        drawGrit();

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
        });
    }

    //gets coords of leftuppercorner in km
    public double[] getLeftUpperCorner() {
        return new double[]{(this.middlepoint[0]-500)*zoomFaktor,(this.middlepoint[1]+500)*zoomFaktor};
    }

    //gets coords of rigthbottomcorner in km
    public double[] getRigthBottomCorner() {
        return new double[]{(this.middlepoint[0]+500)*zoomFaktor,(this.middlepoint[1]-500)*zoomFaktor};
    }

    //draws basic grid on map
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

}
