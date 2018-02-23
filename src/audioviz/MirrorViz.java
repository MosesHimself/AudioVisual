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
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Space_Craft_Trajectories_042
 */
public class MirrorViz implements Visualizer{

    private final String name = "MirrorViz";
    
    private Integer numBands;
    private AnchorPane vizPane;
    
    private final Double bandHeightPercentage = 1.0;
    private final Double minRectHeight = 10.0;  // 10.0
    
    private Double height = 630.0;
    private Double width = 1400.0;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private final Double startHue = 260.0;
    
    private Rectangle[] shapes;
    private Rectangle[] shapes2;
    
    private MenuBar menu;
    
    public MirrorViz() {
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane, MenuBar menu) {
        end();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        this.menu = menu;
        /*
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        */
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        shapes = new Rectangle[numBands];
        shapes2 = new Rectangle[numBands];
        
        for (int i = 0; i < numBands; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(bandWidth);
            rectangle.setHeight(minRectHeight);
            rectangle.setX( bandWidth * i);
            rectangle.setY(height - rectangle.getHeight());
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.WHITE);
            //rectangle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rectangle);
            shapes[i] = rectangle;
        }
        
        for (int j = numBands - 1; j >= 0; j--) {
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(bandWidth);
            rectangle.setHeight(minRectHeight);
            rectangle.setX( bandWidth * j);
            rectangle.setY(minRectHeight);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.WHITE);
            //rectangle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rectangle);
            shapes2[j] = rectangle;
        }

    }
    
    @Override
    public void end() {
        if (shapes != null) {
             for (Rectangle rect : shapes) {
                 vizPane.getChildren().remove(rect);
             }
            shapes = null;
        } 
        if (shapes2 != null) {
             for (Rectangle rect : shapes2) {
                 vizPane.getChildren().remove(rect);
             }
            shapes2 = null;
        } 
    }
    
    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (shapes == null) {
            return;
        }
        
        if (shapes2 == null) {
            return;
        }
        
        Integer num = min(shapes.length, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            //shapes2[numBands - 1 - i].setHeight(shapes2[numBands - 1 - i].getHeight() - 15);
            //if(((60.0 + magnitudes[numBands - 1 - i])/60.0) * 500 + 10 > shapes2[numBands - 1 - i].getHeight())  {
                shapes2[numBands - 1 - i].setHeight( ((60.0 + magnitudes[i])/60.0) * 500 + 10);
            //}
            //shapes2[numBands - 1 - i].setY(shapes[numBands - 1 - i].getHeight());
            shapes2[numBands - 1 - i].setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
                
            //shapes[i].setHeight(shapes[i].getHeight() - 15);
            //if(((60.0 + magnitudes[i])/60.0) * 500 + 10 > shapes[i].getHeight())  {
                shapes[i].setHeight( ((60.0 + magnitudes[i])/60.0) * 500 + 10);
            //}
            shapes[i].setY(height - shapes[i].getHeight());
            shapes[i].setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
        }
        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        hue = Math.floor(hue);
        
        //this makes the background go purple when bass hits
        //menu.setStyle("-fx-background-color: hsb(" + hue + ", 75%, " + hue + "%)" );
    }
    
    
}
