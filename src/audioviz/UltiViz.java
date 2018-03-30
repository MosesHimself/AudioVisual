/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import static java.lang.Integer.min;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Space_Craft_Trajectories_042
 */
public class UltiViz  implements Visualizer{
    
    private final String name = "UltiViz";

    
    private Integer numShapes;
    private AnchorPane vizPane;
    
    private double height;
    private double width;
    
    private int shapeHeight, shapeWidth;
        
    private final Double hueStart = 260.0;
    
    private final String vizPaneInitialStyle = "";

    private static double timer = 0;
    private double value = 0;
    
    private Rectangle[] shapes;
    
    private MenuBar menu;
    
    public UltiViz()  {
        
    }

    @Override
    public void start(Integer numBands, AnchorPane vizPane, MenuBar menu) {
        end();
        
        numShapes = numBands;
        this.vizPane = vizPane;
        this.menu = menu;
        
        this.height = vizPane.getHeight();
        this.width = vizPane.getWidth();
        
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane.setClip(clip);
        
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
            
            //shapes[i].setEffect(new Glow(1));
            
            //shapes[i].getStyleClass().add("my-rect"); 
            
            shapes[i].setFill(Color.hsb(hueStart, 1.0, 1.0, 1.0));
            
            
            //System.out.println(i);

            double x = i;
            
            double input = (x/numBands) * 4 * Math.PI * -1;
            double rotate = input * (180 / Math.PI);
            
            
            shapes[i].setX((150 * Math.cos(1 * input) + (width/2)));
            shapes[i].setY((150 * Math.sin(1 * input) + (height/2)));
            //System.out.println(i);
            //shapes[i].getTransforms().add(new Rotate(rotate));
            //shapes[i].setRotationAxis(Point3D.ZERO);
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
        
        if(shapes == null)  {
            return;
        }
        
        //find which array to take from
        Integer num = min(shapes.length, magnitudes.length);
        
        
        for (int i = 0; i < num; i++) {
            
                
                double x = (i + timer/10);
            
                
            
                //double x = i;
            
                double input = (x/numShapes) * 4 * Math.PI * -1;
                double rotate = input * (180 / Math.PI);
            
            
                //shapes[i].setX((150 * Math.cos(input) + (width/2)) - (shapes[i].getWidth()/2));
                
                
                //shapes[i].setRotate(rotate + 270);
                //shapes[i].setRotationAxis(new Point3D(0, 0, 0));
                
                shapes[i].setHeight( ((60.0 + magnitudes[i])/60.0) * 50 + 5);
                shapes[i].setWidth( ((60.0 + magnitudes[i])/60.0) * 150 + 20);
                shapes[i].setFill(Color.hsb(hueStart - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
                shapes[i].setRotate(rotate);
                
                
                
                double radius = (((60.0 + magnitudes[1])/60.0) * 50) + 200;
                //System.out.println(radius);
                shapes[i].setX((radius * Math.cos(3 * input) + (width/2)));
                shapes[i].setY((radius * Math.sin(2 * input) + (height/2)));
                
                
                
            
        }
        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        hue = Math.floor(hue);
        
        //this makes the background go purple when bass hits
        //menu.setStyle("-fx-background-color: hsb(" + hue + ", 75%, " + hue + "%)" );
    }
    
    
    
}
