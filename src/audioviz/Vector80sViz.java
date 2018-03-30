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
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Space_Craft_Trajectories_042
 */
public class Vector80sViz implements Visualizer{

    private final String name = "80's Vector Viz";
    
    private Integer numBands;
    private AnchorPane vizPane;
    
    private final Double bandHeightPercentage = 1.0;
    private final Double minRectHeight = 10.0;  // 10.0
    
    private Double height;
    private Double width;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private final Double startHue = 131.0;
    
    private Rectangle[] rect;
    
    public MenuBar menu;
    
    public Vector80sViz()  {
        
    }
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane, MenuBar menu) {
        end();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        this.menu = menu;
                
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        rect = new Rectangle[numBands];
        
        for (int i = 0; i < numBands; i++) {
            
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(bandWidth);
            rectangle.setHeight(minRectHeight);
            rectangle.setX( bandWidth * i);
            rectangle.setY(height - rectangle.getHeight());
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.hsb(startHue, 1.0, 1.0, 1.0));
            //rectangle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rectangle);
            rect[i] = rectangle;
            
            
            
        }
        
        
    }

    @Override
    public void end() {
        if (rect != null) {
             for(Rectangle rects : rect) {
                 vizPane.getChildren().remove(rects);
             }
            rect = null;
            
            //vizPane.setStyle(vizPaneInitialStyle);
            //timer = 0;
            vizPane.setClip(null);
        }
       
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {

        
        
        if(rect == null)  {
            return;
        }
        
        //find which array to take from
        Integer num = min(rect.length, magnitudes.length);
        
        
        
        
        
        for (int i = 0; i < numBands; i++) {
            
            rect[i].setHeight(rect[i].getHeight() - 15);
            
            if(((60.0 + magnitudes[i])/60.0) * 500 + 10 > rect[i].getHeight())  {
                
                rect[i].setHeight( ((60.0 + magnitudes[i])/60.0) * 500 + 10);
            
            }
        
        rect[i].setY(height - rect[i].getHeight());
            
        
        }

        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        hue = Math.floor(hue);
        
        //this makes the background go purple when bass hits
        //menu.setStyle("-fx-background-color: hsb(" + hue + ", 75%, " + hue + "%)" );
    }
    
}
