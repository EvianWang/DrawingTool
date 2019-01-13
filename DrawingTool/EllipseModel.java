import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.*;

public class EllipseModel extends ShapeModel {
    public EllipseModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        Rectangle rect = new java.awt.Rectangle(startPoint);
        rect.add(endPoint);
        this.shape = new Ellipse2D.Double(rect.x, rect.y, rect.width, rect.height);
        this.width = rect.width;
        this.height = rect.height;
        
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
    
    public EllipseModel(EllipseModel em) {
        super(em);
        Rectangle boundingBox = em.getShape().getBounds();
        Ellipse2D.Double ell = new Ellipse2D.Double((double) boundingBox.x, (double) boundingBox.y, (double) boundingBox.width, (double) boundingBox.height);
        this.shape = ell;
        this.width = em.width;
        this.height = em.height;
        
        this.scaleRec = em.scaleRec;
        this.rotateCir = em.rotateCir;
        this.center = em.center;
        this.translateStart = em.translateStart;
        
    }

    @Override
    public EllipseModel clone() {
        return new EllipseModel(this);
    }

}
