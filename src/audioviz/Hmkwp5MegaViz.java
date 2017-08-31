/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import static java.lang.Integer.min;
import javafx.geometry.Point3D;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 *
 * @author HG King ayy
 */
public class Hmkwp5MegaViz implements Visualizer{
    
    private final String name = "Hmkwp5MegaVizFamGod";
    
    private static int growFlip = 1;
    
    private static Double radiusMultiplier;
    private MenuBar menu;
    
    private Integer numShapes;
    private AnchorPane vizPane;
    
    private final Double height = 630.0;
    private final Double width = 1400.0;
    
    private int shapeHeight, shapeWidth;
        
    private final Double hueStart = 260.0;
    
    private final String vizPaneInitialStyle = "";

    private static double timer = 0;
    private double value = 0;
    
    private Rectangle[] shapes;
    
    public Hmkwp5MegaViz()  {
    }

    @Override
    public void start(Integer numBands, AnchorPane vizPane, MenuBar menu) {
        end();
        
        numShapes = numBands;
        this.vizPane = vizPane;
        this.menu = menu;
        
        /*
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        */
        radiusMultiplier = 150.0;
        
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane.setClip(clip);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        
        //shapeWidth = width/numShapes;
        shapeWidth = 40;
        shapeHeight = 360/numShapes;
        
        //System.out.println(height);

        shapes = new Rectangle[numShapes];
        int i;
        
        for(i = 0; i < numShapes; i++)  {
            
            shapes[i] = new Rectangle();
            this.vizPane.getChildren().add(shapes[i]);
            shapes[i].setHeight(shapeHeight);
            shapes[i].setWidth(shapeWidth);
            shapes[i].setArcWidth(10.0);
            shapes[i].setArcHeight(10.0);
            shapes[i].setEffect(dropShadow);
           
            shapes[i].setFill(Color.hsb(hueStart, 1.0, 1.0, 1.0));
            
            double x = i;
            
            double input = (x/numBands) * 2 * Math.PI * -1;
            double rotate = input * (180 / Math.PI);
            
            shapes[i].setX((radiusMultiplier * Math.cos(input) + (width/2)));
            shapes[i].setY((radiusMultiplier * Math.sin(input) + (height/2)));
            shapes[i].setRotate(rotate);
        }
    }

    @Override
    public void end() {
        if (shapes != null) {
             for(Rectangle rect : shapes) {
                 vizPane.getChildren().remove(rect);
             }
            shapes = null;
            vizPane.setClip(null);
            vizPane.setStyle(vizPaneInitialStyle);
            timer = 0;
            growFlip = 1;
        }   
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        
        timer++;
        
        //reset the timer if it gets too big
        if(timer > 10000)  {
            timer = 0;
        }
        
        //every 150 frames, flip the direction of the circle grow
        if(timer % 150 == 0)  {
            growFlip *= -1;
            System.out.println("ayy");
        }

        radiusMultiplier += (double)(2 * growFlip);
        
        if(shapes == null)  {
            return;
        }
        
        //find which array to take from
        Integer num = min(shapes.length, magnitudes.length);
               
        for (int i = 0; i < num; i++) {
                           
                double x = (i + timer/10);
                double input = ((x/numShapes) * 2 * Math.PI);
                double rotate = input * (180 / Math.PI);

                shapes[i].setHeight( ((60.0 + magnitudes[i])/60.0) * 300 + 40);
                shapes[i].setWidth( ((60.0 + magnitudes[i])/60.0) * 300 + 40);
                shapes[i].setFill(Color.hsb(hueStart - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
                shapes[i].setRotate(rotate * 2 * Math.PI);               
                
                if(growFlip > 0)  {
                    shapes[i].setX((radiusMultiplier * Math.cos(1 * input) + (width/2)));
                    shapes[i].setY((radiusMultiplier * Math.sin(2 * input) + (height/2)));
                }
                else  {
                    shapes[i].setX((radiusMultiplier * Math.cos(3 * input) + (width/2)));
                    shapes[i].setY((radiusMultiplier * Math.sin(2 * input) + (height/2)));
                }
        }
        
        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        hue = Math.floor(hue);
        
        //this makes the background go purple when bass hits
        vizPane.setStyle("-fx-background-color: hsb(" + hue + ", 75%, " + hue + "%)" );
        
        
        
        //this makes the background go purple when bass hits
        menu.setStyle("-fx-background-color: hsb(" + hue + ", 75%, " + hue + "%)" );
        
    }
    
}
