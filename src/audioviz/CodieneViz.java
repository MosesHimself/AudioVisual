/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import static java.lang.Integer.min;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 *
 * @author Space_Craft_Trajectories_042
 */
public class CodieneViz implements Visualizer{
    
    private final String name = "CodeineVizBoii";
    
    private Integer numBands;
    private AnchorPane vizPane;
    
    private final Double bandHeightPercentage = 1.0;
    private final Double minEllipseRadius = 10.0;  // 10.0
    
    private Double height;
    private Double width;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private final Double startHue = 289.0;
    
    static double timer;
    
    private Ellipse[] ellipses;
    
    private ImageView backgroundImageView1;
    
    private MenuBar menu;
    
    public CodieneViz()  {
        
        
    }

    @Override
    public void start(Integer numBands, AnchorPane vizPane, MenuBar menu) {
        end();
        timer = 0;
        this.numBands = numBands;
        this.vizPane = vizPane;
        this.menu = menu;
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        ellipses = new Ellipse[numBands + 1];
        backgroundImageView1 = new ImageView();
        int j = 0;
        for (int i = 0; i < numBands; i++) {
            Ellipse ellipse = new Ellipse();
            
            double x = (i + timer/90);
            double input = (x/numBands) * 2 * Math.PI;
            double rotate = input * (180 / Math.PI);
            
            
            ellipse.setCenterX(150 * Math.cos(1 * input) + (width/2));
            ellipse.setCenterY(150 * Math.sin(1 * input) + (height/2));
            
            ellipse.setRadiusX(bandWidth / 2);
            ellipse.setRadiusY(minEllipseRadius);
            ellipse.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            ellipse.setRotate(rotate);
            
            this.vizPane.getChildren().add(ellipse);
            ellipses[i] = ellipse;
            j = i;
        
        }
        System.out.println(j++);
        ellipses[j]= new Ellipse();
        ellipses[j].setCenterX(width/2);
        ellipses[j].setCenterY(height/2);
            
        ellipses[j].setRadiusX(150);
        ellipses[j].setRadiusY(150);
        ellipses[j].setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
        
        this.vizPane.getChildren().add(ellipses[j]);
        
        this.vizPane.getChildren().add(backgroundImageView1);
        
        
        
        Image backgroundImage1 = new Image(getClass().getResourceAsStream("goku_symbol.png"));
        backgroundImageView1.setImage(backgroundImage1);
        
        backgroundImageView1.setX(width/2 -backgroundImage1.getWidth()/2);
        backgroundImageView1.setY(height/2 -backgroundImage1.getHeight()/2);
        
        
        
    }

    @Override
    public void end() {
        if (ellipses != null) {
             for (Ellipse ellipse : ellipses) {
                 vizPane.getChildren().remove(ellipse);
             }
            ellipses = null;
        } 
        if(backgroundImageView1 != null)  {
            vizPane.getChildren().remove(backgroundImageView1);
            backgroundImageView1 = null;
        }
        timer = 0;
    
    }

    @Override
    public String getName() {
        return name;
        
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        timer++;
        
        if(timer > 10000)  {
            timer = 0;
        }
        
        if (ellipses == null) {
            return;
        }
        
        Integer num = min(ellipses.length - 1, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            
            double x = (i + timer/90);
            double input = (x/numBands) * 2 * Math.PI;
            double rotate = input * (180 / Math.PI);
            
            ellipses[i].setRadiusX( ((60.0 + magnitudes[i])/60.0) * 300 + minEllipseRadius);
            
            ellipses[i].setCenterX(150 * Math.cos(1 * input) + (width/2));
            ellipses[i].setCenterY(150 * Math.sin(1 * input) + (height/2));
            ellipses[i].setRotate(rotate);
            
                
           
        }
        double x = (1 + timer/100);
        double input = (x/numBands) * 2 * Math.PI;
        double rotate = input * (180 / Math.PI);
        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        hue = Math.floor(hue);
        
        //this makes the background go purple when bass hits
        //menu.setStyle("-fx-background-color: hsb(" + hue + ", 75%, " + hue + "%)" );
        backgroundImageView1.setRotate(rotate);
    }
    

}
