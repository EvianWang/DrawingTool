import java.util.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.*;
import java.awt.Color;

public class DrawingModel extends Observable {

    public List<ShapeModel> shapes = new ArrayList<>();

    ShapeModel.ShapeType shapeType = ShapeModel.ShapeType.Rectangle;


    public ShapeModel.ShapeType getShape() {
        return shapeType;
    }

    public void duplicate() {
        for(int i = 0; i < this.shapes.size(); i++) {
            ShapeModel sm = this.shapes.get(i);
            if(sm.selected) {
                ShapeModel newSm = sm.clone();
                newSm.translateStart = newSm.center;
                Point endPoint = new Point((int) newSm.center.getX(),(int) newSm.center.getY()+10);
                newSm.translateShape(endPoint);
                sm.unselected();
                //newSm.selected();
                this.shapes.add(newSm);
                //sm.unselected();
                newSm.selected();
                this.setChanged();
                this.notifyObservers();
                break; 
                
            }
        }
    }

    public void setShape(ShapeModel.ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void setColor(Color color){
        for(int i = 0; i < this.shapes.size(); i++) {
            ShapeModel sm = this.shapes.get(i);
            if(sm.selected) {
                //change the color of selected shape model
                sm.changeColor(color);
                this.setChanged();
                this.notifyObservers();
                break;
            }
        }
    }

    public DrawingModel() { }

    public List<ShapeModel> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    public void addShape(ShapeModel shape) {
        this.shapes.add(shape);
        this.setChanged();
        this.notifyObservers();
    }

    public void removeShape(){
        for(int i = 0; i < this.shapes.size(); i++) {
            ShapeModel sm = this.shapes.get(i);
            if(sm.selected) {
                //remove the shape
                this.shapes.remove(sm);
                this.setChanged();
                this.notifyObservers();
                break;
            }
        }
    }

    public void clearShapes(){
        this.shapes.clear();
        this.setChanged();
        this.notifyObservers();
    }
}
