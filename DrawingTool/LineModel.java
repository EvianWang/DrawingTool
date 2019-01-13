import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;

public class LineModel extends ShapeModel {

    Point a;
    Point b;
    public LineModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        this.a = startPoint;
        this.b = endPoint;

        Path2D path = new Path2D.Double();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(endPoint.x, endPoint.y);
        this.shape = path;

        this.width = Math.abs(a.x - b.x);
        this.height = Math.abs(a.y - b.y);
   
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
    //some getter
    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }
    //copy constructor
    public LineModel(LineModel lm) {
        super(lm);
        this.a = lm.getA();
        this.b = lm.getB();

        Path2D path = new Path2D.Double();
        path.moveTo(a.x,a.y);
        path.lineTo(b.x,b.y);
        this.shape = path;
        this.width = lm.width;
        this.height = lm.height;
      
        this.scaleRec = lm.scaleRec;
        this.rotateCir = lm.rotateCir;
        this.center = lm.center;
        this.translateStart = lm.translateStart;
       
    }
    
    @Override
    public void rotateShape(Point lastMouse) {
        Point origin = new Point(this.rotateCir);
        Point destination = new Point(lastMouse);
        double theta = Math.atan2(destination.getY() - this.center.getY(), destination.getX() - this.center.getX()) - Math.atan2(origin.getY() - this.center.getY(), origin.getX() - this.center.getX());
        Path2D.Double path = new Path2D.Double();
        path.append(this.getShape(), false);
        AffineTransform at =  AffineTransform.getRotateInstance(theta,this.center.getX(),this.center.getY());
        this.shape = path.createTransformedShape(at);

        this.rotated();
        this.angle = theta;

        Point temp = new Point();
        at.transform(this.scaleRec, temp);
        this.scaleRec = temp;
        Point temp1 = new Point();
        at.transform(this.rotateCir, temp1);
        this.rotateCir = temp1;
        Point temp2 = new Point();
        at.transform(this.a,temp2);
        this.a = temp2;
        Point temp3 = new Point();
        at.transform(this.b,temp3);
        this.b = temp3;
        

    }

    @Override
    public LineModel clone() {
        return new LineModel(this);
    }

    @Override
    public void scaleTo(Point endPoint) {
        Path2D.Double path = new Path2D.Double();
        path.append(this.getShape(), false);
        double sx,sy;
     
        sx = (endPoint.getX() - this.center.getX()) / (this.scaleRec.getX() - this.center.getX());
        sy = (endPoint.getY() - this.center.getY()) / (this.scaleRec.getY() - this.center.getY());
        AffineTransform at =  new AffineTransform();
       
        at.translate(this.center.x,this.center.y);
        at.scale(sx,sy);
        at.translate(-this.center.x,-this.center.y);
      
        this.shape = path.createTransformedShape(at);

        Point temp = new Point();
        at.transform(this.scaleRec, temp);
        this.scaleRec = temp;
        Point temp1 = new Point();
        at.transform(this.rotateCir, temp1);
        this.rotateCir = temp1;
        Point temp2 = new Point();
        at.transform(this.a,temp2);
        this.a = temp2;
        Point temp3 = new Point();
        at.transform(this.b,temp3);
        this.b = temp3;
        
        int tempInt = this.width;
        this.width = (int) (tempInt * sx);
        tempInt = this.height;
        this.height = (int) (tempInt * sy);
        
    }
    
    @Override
    public void translateShape(Point endPoint) {
        double tx = endPoint.getX() - this.translateStart.getX();
        double ty = endPoint.getY() - this.translateStart.getY();
      
        Path2D.Double path = new Path2D.Double();
        path.append(this.getShape(), false);

     
        AffineTransform at = new AffineTransform();
        at.translate(tx,ty);
        this.shape = path.createTransformedShape(at);
        Point temp = new Point();
        at.transform(this.scaleRec, temp);
        this.scaleRec = temp;
        Point temp1 = new Point();
        at.transform(this.rotateCir, temp1);
        this.rotateCir = temp1;
        Point temp2 = new Point();
        at.transform(this.a,temp2);
        this.a = temp2;
        Point temp3 = new Point();
        at.transform(this.b,temp3);
        this.b = temp3;
        Point temp4 = new Point();
        at.transform(this.center,temp4);
        this.center = temp4;
        
        this.translateStart = endPoint;
    }

    @Override
    public boolean hitTest(Point2D p) {
        return pointToLineDistance(a,b,(Point) p) < 10;
    }

    public double pointToLineDistance(Point A, Point B, Point P) {
        double normalLength = Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.y-A.y)*(B.y-A.y));
        return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
    }
}
