/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import static java.lang.Integer.min;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;

/**
 *
 * @author Space_Craft_Trajectories_042
 */
public class SphereViz implements Visualizer{
    
    private final String name = "Sphere Viz";
    
    private Integer numBands;
    private AnchorPane vizPane;
    
    private final Double bandHeightPercentage = 1.0;
    private final Double minRectHeight = 10.0;  // 10.0
    
    private Double height = 630.0;
    private Double width = 1400.0;
    
    private MenuBar menu;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private PhongMaterial redMaterial;
    
    private final Double startHue = 131.0;
    
    private Sphere[] spheres;
    
    private static double offset;

    @Override
    public void start(Integer numBands, AnchorPane vizPane, MenuBar menu) {
        end();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        this.menu = menu;
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane.setClip(clip);
        
        /*
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        */
        offset = 0;
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        if(numBands < 8)  {
            bandWidth = bandWidth/2;
            offset = width/2;
        }
        spheres = new Sphere[numBands];
        
        for (int i = 0; i < numBands; i++) {
            Sphere sphere = new Sphere(bandWidth/2);
            
            redMaterial = new PhongMaterial();
            redMaterial.setDiffuseColor(Color.hsb(260.0, 1.0, 1.0, 1.0));
            redMaterial.setSpecularColor(Color.hsb(260.0, 1.0, 1.0, 1.0));
           
            
            
            sphere.setLayoutX((bandWidth / 2 + bandWidth * i) + offset);
            sphere.setLayoutY(height / 2);
            
            vizPane.getChildren().add(sphere);
            spheres[i] = sphere;
            spheres[i].setMaterial(redMaterial);
        }

    }
    
    @Override
    public void end() {
         if (spheres != null) {
             for (Sphere sphere : spheres) {
                 vizPane.getChildren().remove(sphere);
             }
            spheres = null;
        } 
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        
        if (spheres == null) {
            return;
        }
        
        Integer num = min(spheres.length, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            spheres[i].setRadius(bandWidth / 2);
            PhongMaterial material = new PhongMaterial();
            
            material.setDiffuseColor(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
            material.setSpecularColor(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
            if(magnitudes[i] == -60.0)  {
                material.setDiffuseColor(Color.hsb(0, 0, 0.40, 1.0));
                material.setSpecularColor(Color.hsb(0, 0, 0.40, 1.0));
            }
            spheres[i].setMaterial(material);
            if(numBands != 1)   {
                if(numBands == 2)  {
                    spheres[i].setLayoutY(height - ((spheres[i].getRadius() + ((60.0 + magnitudes[i])/60.0) * 500 + 10)/2));
                }
                else
                spheres[i].setLayoutY(height - (spheres[i].getRadius() + ((60.0 + magnitudes[i])/60.0) * 500 + 10));
            }
        }
        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        hue = Math.floor(hue);
        
        //this makes the background go purple when bass hits
        //menu.setStyle("-fx-background-color: hsb(" + hue + ", 75%, " + hue + "%)" );
    }
    
}
