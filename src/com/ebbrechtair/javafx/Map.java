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
    private double zoomFaktor;//This is the faktor between pixel to distance in nauticmiles 1000px 200seemeilen
                                // VERDOPPELT SICH ALLE 4 RAD KLICKS
    private double lastX;
    private double lastY;

    private Image imageAirportVFR;
    private Image imageAirportIFR;
    private Image image2;
    private Image imageNavaidDME;
    private Image imageNavaidNDB;
    private Image imageNavaidVOR;
    private Image iamgeNavaidVORDME;

    private final GraphicsContext context = this.getGraphicsContext2D();

    public Map() {
        super(1000, 1000);

        this.zoomFaktor = 0.2;
        this.middlepoint = new double[]{8.566084*60,50.038312*60};

        loadImages();
        populateGeoCoordinates();

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
            populateGeoCoordinates();

        });
        //This event is triggered on press of mouse
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
            populateGeoCoordinates();

            System.out.println("Dragevent: "+this.middlepoint[0]+" "+this.middlepoint[1]+" Zoom: 1000px ="+this.zoomFaktor*1000+"km");

        });
    }

    //gets coords of leftuppercorner in nauticmiles
    public double[] getLeftUpperCorner() {
        return new double[]{this.middlepoint[0] - (500 * this.zoomFaktor),this.middlepoint[1] + (500 * this.zoomFaktor)};
    }

    //gets coords of rigthbottomcorner in nauticmiles
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
            if(((Airport) geoCoordinate).isIfr()){
                currentIcon = this.imageAirportIFR;
            }else{
                currentIcon = this.imageAirportVFR;

            }
        }else if(geoCoordinate instanceof Fix){
            //Fix
            currentIcon = this.image2;
        }else if(geoCoordinate instanceof Navaid){
            if(geoCoordinate instanceof Navaid.DME){
                //DME
                currentIcon = this.imageNavaidDME;
            }else if(geoCoordinate instanceof Navaid.NDB){
                //NDB
                currentIcon = this.imageNavaidNDB;
            }else if(geoCoordinate instanceof Navaid.VOR){
                //VOR
                currentIcon = this.imageNavaidVOR;
            }else if(geoCoordinate instanceof Navaid.VOR_DME){
                //VOR_DME
                currentIcon = this.iamgeNavaidVORDME;
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

    /**
     * These following Methodes are used to populate all the GeoCoordinates of the map
     *
     *
     */

    private void populateGeoCoordinates(){
        populateAirport();
        //populateFix();
        populateNavaid();
        //populateRunway();
        populateAirway();

    }
    private void populateAirport(){
        double[] leftUpperCorner = getLeftUpperCorner();
        double[] rigthBootomCorner = getRigthBottomCorner();
        Converter converter = new Converter();
        String sqlstatement = null;
        ResultSet resultSet = null;

        double maxLat = converter.ConvertYInLat(leftUpperCorner[1]);
        double minLat = converter.ConvertYInLat(rigthBootomCorner[1]);
        double minLon = converter.ConvertXInLon(leftUpperCorner[0],leftUpperCorner[1]);
        double maxLon = converter.ConvertXInLon(rigthBootomCorner[0],rigthBootomCorner[1]);

        try {
            Connection connection = SqlConnector.getSQLConnection();
            Statement statement = connection.createStatement();

            sqlstatement = "SELECT * FROM db_Airport WHERE Lat > " + minLat + " AND Lat < " + maxLat + " AND Lon > " + minLon + " AND Lon < " + maxLon + ";";
            resultSet = statement.executeQuery(sqlstatement);

            while(resultSet.next()){
                drawGeoCoordinate(new Airport(resultSet.getString("ICAOCode"),resultSet.getString("AirportName"),resultSet.getDouble("Lat"),resultSet.getDouble("Lon"),resultSet.getInt("AltitudeAirportInFeet"),resultSet.getString("a01"),resultSet.getString("a02"),resultSet.getInt("MaxRunwayLength"),resultSet.getString("b01"),resultSet.getInt("IFR")));
            }
        connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void populateFix(){
        double[] leftUpperCorner = getLeftUpperCorner();
        double[] rigthBootomCorner = getRigthBottomCorner();
        Converter converter = new Converter();
        String sqlstatement = null;
        ResultSet resultSet = null;

        double maxLat = converter.ConvertYInLat(leftUpperCorner[1]);
        double minLat = converter.ConvertYInLat(rigthBootomCorner[1]);
        double minLon = converter.ConvertXInLon(leftUpperCorner[0],leftUpperCorner[1]);
        double maxLon = converter.ConvertXInLon(rigthBootomCorner[0],rigthBootomCorner[1]);

        try {
            Connection connection = SqlConnector.getSQLConnection();
            Statement statement = connection.createStatement();

            sqlstatement = "SELECT * FROM db_Fix WHERE Lat > " + minLat + " AND Lat < " + maxLat + " AND Lon > " + minLon + " AND Lon < " + maxLon + ";";
            resultSet = statement.executeQuery(sqlstatement);

            while(resultSet.next()){
                drawGeoCoordinate(new Fix(resultSet.getString("FixID"),resultSet.getDouble("Lat"),resultSet.getDouble("Lon"),resultSet.getString("Areacode")));
            }
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateNavaid(){
        populateDME();
        populateVOR();
        populateVORDME();
        populateNDB();
    }

    private void populateDME(){
        double[] leftUpperCorner = getLeftUpperCorner();
        double[] rigthBootomCorner = getRigthBottomCorner();
        Converter converter = new Converter();
        String sqlstatement = null;
        ResultSet resultSet = null;

        double maxLat = converter.ConvertYInLat(leftUpperCorner[1]);
        double minLat = converter.ConvertYInLat(rigthBootomCorner[1]);
        double minLon = converter.ConvertXInLon(leftUpperCorner[0],leftUpperCorner[1]);
        double maxLon = converter.ConvertXInLon(rigthBootomCorner[0],rigthBootomCorner[1]);

        try {
            Connection connection = SqlConnector.getSQLConnection();
            Statement statement = connection.createStatement();

            sqlstatement = "SELECT * FROM db_Navaid WHERE Lat > " + minLat + " AND Lat < " + maxLat + " AND Lon > " + minLon + " AND Lon < " + maxLon + " AND DMECapability = 1 AND RadialCapability = 0;";
            resultSet = statement.executeQuery(sqlstatement);

            while(resultSet.next()){
                drawGeoCoordinate(new Navaid.DME(resultSet.getDouble("Lat"),resultSet.getDouble("Lon"),resultSet.getString("NavaidID"),resultSet.getString("NavaidName"),resultSet.getDouble("Frequency"),resultSet.getInt("radialCapability"),resultSet.getInt("DMECapability"),resultSet.getString ("a01"),resultSet.getInt("Altitude"),resultSet.getString("AreaCode"),resultSet.getString("a02")));
            }
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateVOR(){
        double[] leftUpperCorner = getLeftUpperCorner();
        double[] rigthBootomCorner = getRigthBottomCorner();
        Converter converter = new Converter();
        String sqlstatement = null;
        ResultSet resultSet = null;

        double maxLat = converter.ConvertYInLat(leftUpperCorner[1]);
        double minLat = converter.ConvertYInLat(rigthBootomCorner[1]);
        double minLon = converter.ConvertXInLon(leftUpperCorner[0],leftUpperCorner[1]);
        double maxLon = converter.ConvertXInLon(rigthBootomCorner[0],rigthBootomCorner[1]);

        try {
            Connection connection = SqlConnector.getSQLConnection();
            Statement statement = connection.createStatement();

            sqlstatement = "SELECT * FROM db_Navaid WHERE Lat > " + minLat + " AND Lat < " + maxLat + " AND Lon > " + minLon + " AND Lon < " + maxLon + " AND DMECapability = 0 AND RadialCapability = 1;";
            resultSet = statement.executeQuery(sqlstatement);

            while(resultSet.next()){
                drawGeoCoordinate(new Navaid.VOR(resultSet.getDouble("Lat"),resultSet.getDouble("Lon"),resultSet.getString("NavaidID"),resultSet.getString("NavaidName"),resultSet.getDouble("Frequency"),resultSet.getInt("radialCapability"),resultSet.getInt("DMECapability"),resultSet.getString ("a01"),resultSet.getInt("Altitude"),resultSet.getString("AreaCode"),resultSet.getString("a02")));
            }
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateVORDME(){
        double[] leftUpperCorner = getLeftUpperCorner();
        double[] rigthBootomCorner = getRigthBottomCorner();
        Converter converter = new Converter();
        String sqlstatement = null;
        ResultSet resultSet = null;

        double maxLat = converter.ConvertYInLat(leftUpperCorner[1]);
        double minLat = converter.ConvertYInLat(rigthBootomCorner[1]);
        double minLon = converter.ConvertXInLon(leftUpperCorner[0],leftUpperCorner[1]);
        double maxLon = converter.ConvertXInLon(rigthBootomCorner[0],rigthBootomCorner[1]);

        try {
            Connection connection = SqlConnector.getSQLConnection();
            Statement statement = connection.createStatement();

            sqlstatement = "SELECT * FROM db_Navaid WHERE Lat > " + minLat + " AND Lat < " + maxLat + " AND Lon > " + minLon + " AND Lon < " + maxLon + " AND DMECapability = 1 AND RadialCapability = 1;";
            resultSet = statement.executeQuery(sqlstatement);

            while(resultSet.next()){
                drawGeoCoordinate(new Navaid.VOR_DME(resultSet.getDouble("Lat"),resultSet.getDouble("Lon"),resultSet.getString("NavaidID"),resultSet.getString("NavaidName"),resultSet.getDouble("Frequency"),resultSet.getInt("radialCapability"),resultSet.getInt("DMECapability"),resultSet.getString ("a01"),resultSet.getInt("Altitude"),resultSet.getString("AreaCode"),resultSet.getString("a02")));
            }
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateNDB(){
        double[] leftUpperCorner = getLeftUpperCorner();
        double[] rigthBootomCorner = getRigthBottomCorner();
        Converter converter = new Converter();
        String sqlstatement = null;
        ResultSet resultSet = null;

        double maxLat = converter.ConvertYInLat(leftUpperCorner[1]);
        double minLat = converter.ConvertYInLat(rigthBootomCorner[1]);
        double minLon = converter.ConvertXInLon(leftUpperCorner[0],leftUpperCorner[1]);
        double maxLon = converter.ConvertXInLon(rigthBootomCorner[0],rigthBootomCorner[1]);

        try {
            Connection connection = SqlConnector.getSQLConnection();
            Statement statement = connection.createStatement();

            sqlstatement = "SELECT * FROM db_Navaid WHERE Lat > " + minLat + " AND Lat < " + maxLat + " AND Lon > " + minLon + " AND Lon < " + maxLon + " AND DMECapability = 0 AND RadialCapability = 0;";
            resultSet = statement.executeQuery(sqlstatement);

            while(resultSet.next()){
                drawGeoCoordinate(new Navaid.NDB(resultSet.getDouble("Lat"),resultSet.getDouble("Lon"),resultSet.getString("NavaidID"),resultSet.getString("NavaidName"),resultSet.getDouble("Frequency"),resultSet.getInt("radialCapability"),resultSet.getInt("DMECapability"),resultSet.getString ("a01"),resultSet.getInt("Altitude"),resultSet.getString("AreaCode"),resultSet.getString("a02")));
            }
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateAirway(){
        double[] leftUpperCorner = getLeftUpperCorner();
        double[] rigthBootomCorner = getRigthBottomCorner();
        Converter converter = new Converter();
        String sqlstatement = null;
        ResultSet resultSet = null;

        double maxLat = converter.ConvertYInLat(leftUpperCorner[1]);
        double minLat = converter.ConvertYInLat(rigthBootomCorner[1]);
        double minLon = converter.ConvertXInLon(leftUpperCorner[0],leftUpperCorner[1]);
        double maxLon = converter.ConvertXInLon(rigthBootomCorner[0],rigthBootomCorner[1]);

        try {
            Connection connection = SqlConnector.getSQLConnection();
            Statement statement = connection.createStatement();

            sqlstatement = "SELECT * FROM db_Airway WHERE Lat > " + minLat + " AND Lat < " + maxLat + " AND Lon > " + minLon + " AND Lon < " + maxLon + " OR LatNext > " + minLat + " AND LatNext < " + maxLat + " AND LonNext > " + minLon + " AND LonNext < " + maxLon + ";";
            resultSet = statement.executeQuery(sqlstatement);

            while(resultSet.next()){
                drawAirway(new Airway(resultSet.getString("AirwayID"),resultSet.getInt("AirwayPartition"),resultSet.getString("FixID"),resultSet.getDouble("Lon"),resultSet.getDouble("Lat"),resultSet.getString("IDOfNextFix"),resultSet.getDouble("LonNext"),resultSet.getDouble("LatNext"),resultSet.getInt("InboundCourse"),resultSet.getInt("OutboundCourse"),resultSet.getDouble("LegLength")));
            }
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImages(){
        this.imageAirportVFR =new Image("com\\ebbrechtair\\ressources\\blackicons\\20px_Airport_VFR.png");
        this.imageAirportIFR =new Image("com\\ebbrechtair\\ressources\\blackicons\\20px_Airport_IFR.png");
        this.image2 =new Image("com\\ebbrechtair\\ressources\\icons\\Transparent_Number_20px_2.png");
        this.imageNavaidDME =new Image("com\\ebbrechtair\\ressources\\blackicons\\20px_Navaid_DME.png");
        this.imageNavaidNDB =new Image("com\\ebbrechtair\\ressources\\blackicons\\20px_Navaid_NDB.png");
        this.imageNavaidVOR =new Image("com\\ebbrechtair\\ressources\\blackicons\\20px_Navaid_VOR.png");
        this.iamgeNavaidVORDME =new Image("com\\ebbrechtair\\ressources\\blackicons\\20px_Navaid_VORDME.png");
    }

    private void setMiddlepoint(GeoCoordinate geoCoordinate){
        this.middlepoint[0]= geoCoordinate.getXValue()*60;
        this.middlepoint[1]= geoCoordinate.getYValue()*60;
    }

    private void drawAirway(GeoCoordinate geoCoordinate){
        Converter converter = new Converter();

        Double starty = (-geoCoordinate.getYValue()+getLeftUpperCorner()[1])/this.zoomFaktor;
        Double startx = (geoCoordinate.getXValue()-getLeftUpperCorner()[0])/this.zoomFaktor;
        Double endy = (-((Airway)geoCoordinate).getYNext()+getLeftUpperCorner()[1])/this.zoomFaktor;
        Double endx = (((Airway)geoCoordinate).getXNext()-getLeftUpperCorner()[0])/this.zoomFaktor;

        context.setStroke(Color.BLUE);
        context.setLineWidth(3.0);
        context.strokeLine(startx,starty,endx,endy);

    }

}
