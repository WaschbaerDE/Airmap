package com.ebbrechtair.javafx;

import com.ebbrechtair.classes.*;
import com.ebbrechtair.util.Converter;
import com.ebbrechtair.util.SqlConnector;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Map extends Canvas {

    //Koordinaten sind immer als X | Y gespeichert!!!!! -> Lon | Lat
    private final double[] middlepoint;//This is the 500|500 point / the middlepoint corner of the map/canvas in km
    private double zoomFaktor;//This is the faktor between pixel to distance in km 1000px = 200km BASIC // 1000px 200seemeilen
                                // VERDOPPELT SICH ALLE 4 RAD KLICKS
    private double lastX;
    private double lastY;

    private final Image image0;
    private final Image image1;
    private final Image image2;
    private final Image image3;
    private final Image image4;
    private final Image image5;
    private final Image image6;

    private final GraphicsContext context = this.getGraphicsContext2D();

    public Map() {
        super(1000, 1000);
        this.zoomFaktor = 0.02;
        this.middlepoint = new double[]{612.728065,5568.679578};

        this.image0 =new Image("com\\ebbrechtair\\ressources\\icons\\Transparent_Number_20px_0.png");
        this.image1 =new Image("com\\ebbrechtair\\ressources\\icons\\Transparent_Number_20px_1.png");
        this.image2 =new Image("com\\ebbrechtair\\ressources\\icons\\Transparent_Number_20px_2.png");
        this.image3 =new Image("com\\ebbrechtair\\ressources\\icons\\Transparent_Number_20px_3.png");
        this.image4 =new Image("com\\ebbrechtair\\ressources\\icons\\Transparent_Number_20px_4.png");
        this.image5 =new Image("com\\ebbrechtair\\ressources\\icons\\Transparent_Number_20px_5.png");
        this.image6 =new Image("com\\ebbrechtair\\ressources\\icons\\Transparent_Number_20px_6.png");

        drawGrit();
        populateAirports();

        //This event get triggered on Scrolling with Mouse3-button
        //the new zoomfaktor is the old one times the scroll /40 * the faktor to zoom x2 with 4 wheel clicks
        setOnScroll(event -> {
            if(event.getDeltaY()>0){
                for(int i=0;i<Math.abs(event.getDeltaY()/40);i++) {
                    this.zoomFaktor = this.zoomFaktor / Math.pow(2, 0.25);
                }

                this.middlepoint[0] = this.middlepoint[0]+((event.getX()-500)*this.zoomFaktor/4);
                this.middlepoint[1] =  this.middlepoint[1]+((500-event.getY())*this.zoomFaktor/4);
                System.out.println("Zoom-In-Event: "+this.middlepoint[0]+" "+ this.middlepoint[1]+" Zoom: 1000px ="+this.zoomFaktor*1000+"km");

            }
            if(event.getDeltaY()<0){
                for(int i=0;i<Math.abs(event.getDeltaY()/40);i++) {
                    this.zoomFaktor = this.zoomFaktor * Math.pow(2, 0.25);
                }

                this.middlepoint[0] = this.middlepoint[0]-((event.getX()-500)*this.zoomFaktor/4);
                this.middlepoint[1] =  this.middlepoint[1]-((500-event.getY())*this.zoomFaktor/4);
                System.out.println("Zoom-Out-Event: X: "+this.middlepoint[0]+" Y: "+ this.middlepoint[1]+" Zoom: 1000px ="+this.zoomFaktor*1000+"km");
            }
            clearMap();
            drawGrit();
            populateAirports();

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
            populateAirports();

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
        Image currentIcon = null;
        if(geoCoordinate instanceof Airport){
            //Airport
            if(((Airport) geoCoordinate).getIcaoCode().equals("EDDF")){
                currentIcon = this.image0;
            }else{
                currentIcon = this.image1;

            }
        }else if(geoCoordinate instanceof Fix){
            //Fix
            currentIcon = this.image2;
        }else if(geoCoordinate instanceof Navaid){
            if(1==1){
                //DME
                currentIcon = this.image3;
            }else if(1==1){
                //NDB
                currentIcon = this.image4;
            }else if(1==1){
                //VOR
                currentIcon = this.image5;
            }else if(1==1){
                //VOR_DME
                currentIcon = this.image6;
            }

        }

        double x = (geoCoordinate.getXValue()-getLeftUpperCorner()[0])/this.zoomFaktor;
        double y = (-geoCoordinate.getYValue()+getLeftUpperCorner()[1])/this.zoomFaktor;

        double iconsizeX = 20.0;
        double iconsizeY = 20.0;

        Converter converter = new Converter();
        context.drawImage(currentIcon,x-(iconsizeX/2),y-(iconsizeY/2),iconsizeX,iconsizeY);
    }

    private void clearMap(){
        context.clearRect(0,0,1000,1000);
    }

    private void populateAirports(){
        double[] leftUpperCorner = getLeftUpperCorner();
        double[] rigthBootomCorner = getRigthBottomCorner();
        Converter converter = new Converter();

        double maxLat = converter.ConvertYInLat(leftUpperCorner[1]);
        double minLat = converter.ConvertYInLat(rigthBootomCorner[1]);
        double minLon = converter.ConvertXInLon(leftUpperCorner[0],leftUpperCorner[1]);
        double maxLon = converter.ConvertXInLon(rigthBootomCorner[0],rigthBootomCorner[1]);
        System.out.println("LO: "+leftUpperCorner[0]+" "+leftUpperCorner[1]);

        String sqlstatement = "SELECT * FROM db_Airport WHERE Lat > " + minLat + " AND Lat < " + maxLat + " AND Lon > " + minLon + " AND Lon < " + maxLon + ";";

        try {
            SqlConnector sqlConnector = new SqlConnector();
            Connection connection = SqlConnector.getSQLConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlstatement);

            while(resultSet.next()){
                drawGeoCoordinate(new Airport(resultSet.getString(2),resultSet.getString(3),resultSet.getDouble(4),resultSet.getDouble(5),resultSet.getInt(6),resultSet.getString(7),resultSet.getString(8),resultSet.getInt(9),resultSet.getString(10)));
            }
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
