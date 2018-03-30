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
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import static javafx.scene.shape.CullFace.BACK;
import static javafx.scene.shape.CullFace.FRONT;
import static javafx.scene.shape.CullFace.NONE;
import static javafx.scene.shape.DrawMode.LINE;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Space_Craft_Trajectories_042
 */
public class Viz3D implements Visualizer{

    private final String name = "3DVizBoii";
    
    //private static int growFlip = 1;
    
    //private static Double radiusMultiplier;
    
    private Rectangle clip;
    
    private Integer numShapes;
    private AnchorPane vizPane;
    
    private Double height;
    private Double width;
    
    private double shapeHeight = 20, shapeWidth = 20, shapeDepth = 20;
        
    private final Double hueStart = 260.0;
    
    private final String vizPaneInitialStyle = "";

    private final double widthOffset = 100;
    
    private static double timer = 0;
    private double value = 0;
    
    private Box[] shapes;
    
    private PhongMaterial redMaterial;
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane, MenuBar menu) {
        end();
        
        numShapes = numBands;
        this.vizPane = vizPane;
        
        
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        
        clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane.setClip(clip);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        
        //shapeWidth = width/numShapes;
        shapeWidth = width/numShapes;
        //shapeHeight = 360/numShapes;
        
        //System.out.println(height);

        shapes = new Box[numShapes];
        int i;
        
        double x, y;
        
        for(i = 0; i < numShapes; i++)  {
            
            shapes[i] = new Box();
            this.vizPane.getChildren().add(shapes[i]);
            shapes[i].setHeight(shapeHeight);
            shapes[i].setWidth(shapeWidth);
            shapes[i].setDepth(shapeDepth);
            //shapes[i].setArcWidth(10.0);
            //shapes[i].setArcHeight(10.0);
            //shapes[i].setEffect(dropShadow);
            
            redMaterial = new PhongMaterial();
            redMaterial.setDiffuseColor(Color.hsb(260.0, 1.0, 1.0, 1.0));
            redMaterial.setSpecularColor(Color.hsb(260.0, 1.0, 1.0, 1.0));
           
            shapes[i].setMaterial(redMaterial);
            /*
            double z = i;
            
            double input = (z/numBands) * 2 * Math.PI * -1;
            double rotate = input * (180 / Math.PI);
            
            
            x = 150 * Math.cos(1 * input) + (width/2);
            y = 150 * Math.sin(1 * input) + (height/2);
            */
            y = height/2;
            x = shapeWidth * i + 5 + widthOffset;
            x = shapeWidth * i + 5;
            
           
            shapes[i].relocate(x, y);
            
            
            shapes[i].setRotationAxis(new Point3D(shapeWidth/2, shapeHeight/2, shapeDepth/2));
            shapes[i].setRotate(10);
            
            //shapes[i].setDrawMode(LINE);
            shapes[i].setCullFace(BACK);
        }
        
        

    }

    @Override
    public void end() {
        if (shapes != null) {
             for(Box box : shapes) {
                 vizPane.getChildren().remove(box);
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
       
        //vizPane.setClip(null);
        
        timer++;
        
        if (shapes == null) {
            return;
        }
        
        //reset the timer if it gets too big
        if(timer > 10000)  {
            timer = 0;
        }
       
        Integer num = min(shapes.length, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            
            double z = (i + timer/10);
            double input = ((z/numShapes) * 2 * Math.PI);
            double rotate = input * (180 / Math.PI);
            
            double x = 150 * Math.cos(1 * input) + (width/2);
            double y = 150 * Math.sin(1 * input) + (height/2);
                    
            //shapes[i].relocate(x, y);

            
            
            shapes[i].setHeight(((60.0 + magnitudes[i])/60.0) * 400 + shapeHeight);
            shapes[i].setDepth(((60.0 + magnitudes[i])/60.0) * 400 + shapeDepth);
            shapes[i].setWidth(((60.0 + magnitudes[i])/60.0) * 400 + shapeWidth);
            
            shapes[i].setRotationAxis(new Point3D(x, y, z));
            shapes[i].setRotate(rotate * 2 * Math.PI);
            
            PhongMaterial material = new PhongMaterial();
            
            material.setDiffuseColor(Color.hsb(hueStart - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
            material.setSpecularColor(Color.hsb(hueStart - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
            
            shapes[i].setMaterial(material);

        }
        
        //vizPane.setClip(clip);

    }
    
}
