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
    private final Double minEllipseRadius = 10.0;  // 10.0
    
    private Double height = 630.0;
    private Double width = 800.0;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private final Double startHue = 260.0;
    
    private Rectangle[] shapes;
    
    public MirrorViz() {
    }
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane, MenuBar menu) {
        end();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        /*
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        */
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        shapes = new Rectangle[(numBands * 2)];
        int i = 0;
        for (i = 0; i < numBands; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(bandWidth / 2 + bandWidth * i);
            rectangle.setY(0);
            rectangle.setWidth(bandWidth);
            rectangle.setHeight(minEllipseRadius);
            rectangle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rectangle);
            shapes[i] = rectangle;
            System.out.println(i);
        }
                    System.out.println("hey");
                    
        i--;
        for (int j = 0; j < numBands; j++)  {
            i++;
            System.out.println(i);
            System.out.println(j);
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(bandWidth);
            rectangle.setHeight(minEllipseRadius);
            rectangle.setX(width - (bandWidth / 2 + bandWidth * j));
            rectangle.setY(height - minEllipseRadius);
            rectangle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rectangle);
            shapes[i] = rectangle;
            
            
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
            
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
    if (shapes == null) {
            return;
        }
        Integer num = min(shapes.length, magnitudes.length);
        int i;
        for (i = 0; i < num; i++) {
            
            shapes[i].setHeight( ((60.0 + magnitudes[i])/60.0) * halfBandHeight + minEllipseRadius);
            shapes[i].setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
        }
        
        i--;
        
        for (int j = 0; j < num; j++)  {
            i++;
            Double h = ((60.0 + magnitudes[j])/60.0) * halfBandHeight + minEllipseRadius;
            shapes[i].setHeight(h);
            shapes[i].setY(height - h);
            shapes[i].setFill(Color.hsb(startHue - (magnitudes[j] * -6.0), 1.0, 1.0, 1.0));
            
            
        }
    }
    
}
