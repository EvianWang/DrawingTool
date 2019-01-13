import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;
import java.io.*;
import java.awt.Color;

public class ShapeModel {
    Shape shape;
    // added
    boolean selected;
    boolean scaled;
    boolean rotated;
    Point scaleRec;
    Point rotateCir;
    Point center;
    Point translateStart;
    double angle;
    Color color;
    
    int width;
    int height;
    
    

    public ShapeModel(Point startPoint, Point endPoint) { 
        this.selected = false;
        this.rotated = false;
        this.angle = 0;
        this.color = Color.BLACK;
        
    }

    public ShapeModel(ShapeModel sm) { }

    public ShapeModel clone() {
        return new ShapeModel(this);
    }

 
    public void scaled() {
        this.scaled = true;
    }

    public void rotated(){
        this.rotated = true;
    }

    public void scaleFinished() {
        this.scaled = false;
    }

    public void changeColor(Color color){
        this.color = color;
    }

    public void scaleTo(Point endPoint) {
        Path2D.Double path = new Path2D.Double();
        path.append(this.getShape(), false);

        double sx = (endPoint.getX() - this.center.getX()) / (this.scaleRec.getX() - this.center.getX());
        double sy = (endPoint.getY() - this.center.getY()) / (this.scaleRec.getY() - this.center.getY());
        AffineTransform at =  new AffineTransform();
        if(this.rotated == false) {
            at.translate(this.center.x, this.center.y);
            at.scale(sx, sy);
            at.translate(-this.center.x, -this.center.y);
        }else{
            at.translate(this.center.x, this.center.y);
            at.rotate(-angle,this.center.x,this.center.y);
            at.scale(sx,sy);
            at.rotate(angle,this.center.x,this.center.y);
            at.translate(this.center.x, this.center.y);
        }
        this.shape = path.createTransformedShape(at);

        Point temp = new Point();
        at.transform(this.scaleRec, temp);
        this.scaleRec = temp;
        Point temp1 = new Point();
        at.transform(this.rotateCir, temp1);
        this.rotateCir = temp1;
    }
    
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
        Point temp4 = new Point();
        at.transform(this.center,temp4);
        this.center = temp4;

        this.translateStart = endPoint;
    
    }

    public void unselected() {
        this.selected = false;
    }

    public void selected() {
	this.selected = true;
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
    
    public void changeShape(Shape s) {
        this.shape = s;
    }

    public void rotateShape(Point lastMouse) {
        Point origin = this.rotateCir;
        Point destination = new Point(lastMouse);
        // theta is the rotate angle
        double theta = Math.atan2(destination.y - center.y, destination.x - center.x) - Math.atan2(origin.y - center.y, origin.x - center.x);
        this.angle = theta;
        Path2D.Double path = new Path2D.Double();
        path.append(this.getShape(), false);
        AffineTransform at = AffineTransform.getRotateInstance(theta,this.center.getX(),this.center.getY());
        this.shape = path.createTransformedShape(at);
        // rotate other field accordingly
        this.rotated = true;
        Point temp = new Point();
        at.transform(this.scaleRec, temp);
        this.scaleRec = temp;
        Point temp1 = new Point();
        at.transform(this.rotateCir, temp1);
        this.rotateCir = temp1;
    }

    // You will need to change the hittest to account for transformations.
    public boolean hitTest(Point2D p) {
        return this.getShape().contains(p);
    }

    /**
     * Given a ShapeType and the start and end point of the shape, ShapeFactory constructs a new ShapeModel
     * using the class reference in the ShapeType enum and returns it.
     */
    public static class ShapeFactory {
        public ShapeModel getShape(ShapeType shapeType, Point startPoint, Point endPoint) {
            try {
                Class<? extends ShapeModel> clazz = shapeType.shape;
                Constructor<? extends ShapeModel> constructor = clazz.getConstructor(Point.class, Point.class);

                return constructor.newInstance(startPoint, endPoint);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public enum ShapeType {
        Ellipse(EllipseModel.class),
        Rectangle(RectangleModel.class),
        Line(LineModel.class);

        public final Class<? extends ShapeModel> shape;
        ShapeType(Class<? extends ShapeModel> shape) {
            this.shape = shape;
        }
    }
}
