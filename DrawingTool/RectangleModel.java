import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;

public class RectangleModel extends ShapeModel {
    public RectangleModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        
        Point fixedStartPoint = new Point(Math.min(startPoint.x, endPoint.x), Math.min(startPoint.y, endPoint.y));
        int width = Math.abs(endPoint.x - startPoint.x);
        int height = Math.abs(endPoint.y - startPoint.y);
        Rectangle2D rect = new Rectangle2D.Double(fixedStartPoint.x,fixedStartPoint.y, width, height);

        this.shape = rect;
        this.width = width;
        this.height = height;
 
        Point temp = new Point();
        temp.setLocation(startPoint.getX()+(this.width/2),startPoint.getY()+(this.height/2));
        this.center = temp;
        Point temp1 = new Point();
        temp1.setLocation(startPoint.getX()+this.width,startPoint.getY()+this.height);
        this.scaleRec = temp1;
        Point temp2 = new Point();
        temp2.setLocation(startPoint.getX()+(this.width/2),startPoint.getY()-10);
        this.rotateCir = temp2;
    }
    
    //copy constructor
    public RectangleModel(RectangleModel rm) {
        super(rm);
        Rectangle boundingBox = rm.getShape().getBounds();
        Rectangle2D rect = new Rectangle2D.Double(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        this.shape = rect;
        this.width = rm.width;
        this.height = rm.height;
   
        this.scaleRec = rm.scaleRec;
        this.rotateCir = rm.rotateCir;
        this.center = rm.center;
        this.translateStart = rm.translateStart;
        
    }

    @Override
    public RectangleModel clone() {
        return new RectangleModel(this);
    }
    
}
