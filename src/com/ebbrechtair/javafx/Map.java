package com.ebbrechtair.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map extends Canvas {

    private GraphicsContext context = this.getGraphicsContext2D();
    private int[] lineCoordinates = new int[4];

    public Map() {
        super(1000, 1000);

        drawGrit();
        drawTestAirways();
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

    public void drawTestAirways(){
        for (int i = 1; i < 30; i++) {
            lineCoordinates[0] = (i*10);
            lineCoordinates[1] = (i*20);
            lineCoordinates[2] = (i*30);
            lineCoordinates[3] = (i*40);
            drawAirwayLines(lineCoordinates[0],lineCoordinates[1],lineCoordinates[2],lineCoordinates[3]);
        }
        lineCoordinates[0] = (30*10);
        lineCoordinates[1] = (30*20);
        lineCoordinates[2] = (30*30);
        lineCoordinates[3] = (30*40);
        colorAirwayRoutes(lineCoordinates[0],lineCoordinates[1],lineCoordinates[2],lineCoordinates[3]);
    }

    public void drawAirwayLines(int x1, int y1, int x2, int y2) {
        context.setLineWidth(2);
        context.setStroke(Color.GREEN);
        context.strokeLine(x1, y1, x2, y2);
    }

    public void colorAirwayRoutes(int x1, int y1, int x2, int y2) {
        context.setLineWidth(5);
        context.setStroke(Color.MAGENTA);
        context.strokeLine(x1, y1, x2, y2);
    }
}

