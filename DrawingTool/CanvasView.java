import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.lang.Math;
import java.awt.geom.Path2D;
import java.awt.geom.*;
import java.awt.Color;


public class CanvasView extends JPanel implements Observer {
    DrawingModel model;
    Point2D lastMouse;
    Point2D startMouse;
    // added field
    boolean scaleMode;
    boolean rotateMode;
    boolean translateMode;
    ShapeModel transformedShape;

    public CanvasView(DrawingModel model) {
        super();
        this.model = model;
        this.scaleMode = false;
        this.rotateMode = false;

        MouseAdapter mouseListener = new MouseAdapter() {

            // added mouseClicked for select mode
            @Override
            public void mouseClicked(MouseEvent e) {
                lastMouse = e.getPoint();
                for(ShapeModel sm : model.shapes) {
                    sm.unselected();
                }
		        for(ShapeModel sm : model.shapes) {
                    // check if in a shape
                    if (sm.hitTest(lastMouse) == true) {
                        sm.selected();
			break;
                    }
		}
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lastMouse = e.getPoint();
                startMouse = e.getPoint();
                // flag the right mode to process the according shapemodel
                for(int i = 0; i < model.shapes.size(); i++) {
                    ShapeModel sm = model.shapes.get(i);
                    Rectangle scaleRec = new Rectangle(sm.scaleRec.x-5,sm.scaleRec.y-5,10,10);
                    Ellipse2D.Double rotateCir = new Ellipse2D.Double(sm.rotateCir.x-5,sm.rotateCir.y-5,10,10);
                    if(sm.selected && scaleRec.contains(startMouse)) {
                        scaleMode = true;
                        transformedShape = sm.clone();
                        model.shapes.remove(sm);
                    }
                    if(sm.selected && rotateCir.contains(startMouse)) {
                        rotateMode = true;
                        transformedShape = sm.clone();
                        model.shapes.remove(sm);        
                        
                    }
                    if(sm.selected && sm.hitTest(startMouse)) {
                        translateMode = true;
                        sm.translateStart = (Point) startMouse;
                        transformedShape = sm.clone();
                        model.shapes.remove(sm);
                    }
                }
                repaint();
                
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lastMouse = e.getPoint();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                // add the shapemodel accordingly
                if(scaleMode) {
                    ShapeModel shape = transformedShape;
                    shape.scaleTo((Point) lastMouse);
                    model.addShape(shape);
                    transformedShape = shape;
                    shape.selected();
                } else if(rotateMode) {
                    ShapeModel shape = transformedShape;
                    shape.rotateShape((Point) lastMouse);
                    shape.rotated();
                    transformedShape = shape;

                    model.addShape(shape);
                    shape.selected();
                } else if(translateMode) {
                    ShapeModel shape = transformedShape;
                    shape.translateShape((Point) lastMouse);
                    transformedShape = shape;
                    model.addShape(shape);
                    shape.selected();
                } else {
                    ShapeModel shape = new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse);
                    model.addShape(shape);
                }
                
                // turn all the flags off
                scaleMode = false;
                rotateMode = false;
                translateMode = false;
                startMouse = null;
                lastMouse = null;
                repaint();
            }
        };

        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);

        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        setBackground(Color.WHITE);

        drawAllShapes(g2);
        drawCurrentShape(g2);
    }

    private void drawAllShapes(Graphics2D g2) {
        //g2.setColor(new Color(66,66,66));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for(ShapeModel shape : model.getShapes()) {
            // draw scaleRec & rotateCir for selected shapemodel
            if(shape.selected == true){
                g2.setColor(Color.BLUE);
                Rectangle scaleRec = new Rectangle(shape.scaleRec.x-5,shape.scaleRec.y-5,10,10);
                Ellipse2D.Double rotateCir = new Ellipse2D.Double(shape.rotateCir.x-5,shape.rotateCir.y-5,10,10);
                g2.fill(scaleRec);
                g2.fill(rotateCir);
                g2.setColor(new Color(66,66,66));
            }
            g2.setColor(shape.getColor());
            g2.draw(shape.getShape());
        }
    }

    private void drawCurrentShape(Graphics2D g2) {
        if (startMouse == null) {
            return;
        }

        g2.setColor(new Color(66,66,66));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        //draw correct current shape
        if (scaleMode) {
            ShapeModel tempShape = transformedShape;
            g2.draw(tempShape.getShape());
            // call corresponding transform function
            tempShape.scaleTo((Point) lastMouse);
            Rectangle scaleRec = new Rectangle(transformedShape.scaleRec.x-5,transformedShape.scaleRec.y-5,10,10);
            Ellipse2D.Double rotateCir = new Ellipse2D.Double(transformedShape.rotateCir.x-5,transformedShape.rotateCir.y-5,10,10);
            g2.setColor(Color.BLUE);
            g2.fill(scaleRec);
            g2.fill(rotateCir);
            g2.setColor(new Color(66,66,66));

        } else if(rotateMode) {
            ShapeModel tempShape = transformedShape;
            g2.draw(tempShape.getShape());
            // call corresponding transform function
            tempShape.rotateShape((Point) lastMouse);
            tempShape.rotated();
            Rectangle scaleRec = new Rectangle(transformedShape.scaleRec.x-5,transformedShape.scaleRec.y-5,10,10);
            Ellipse2D.Double rotateCir = new Ellipse2D.Double(transformedShape.rotateCir.x-5,transformedShape.rotateCir.y-5,10,10);
            g2.setColor(Color.BLUE);
            g2.fill(scaleRec);
            g2.fill(rotateCir);
            g2.setColor(new Color(66,66,66));
        } else if(translateMode){
            ShapeModel tempShape =  transformedShape;
            //call corresponding transform function
            tempShape.translateShape((Point) lastMouse);
            Rectangle scaleRec = new Rectangle(transformedShape.scaleRec.x-5,transformedShape.scaleRec.y-5,10,10);
            Ellipse2D.Double rotateCir = new Ellipse2D.Double(transformedShape.rotateCir.x-5,transformedShape.rotateCir.y-5,10,10);
            g2.draw(tempShape.getShape());
            g2.setColor(Color.BLUE);
            g2.fill(scaleRec);
            g2.fill(rotateCir);
            g2.setColor(new Color(66,66,66)); 
        } else {
            g2.draw(new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse).getShape());
        }
        
    }
}
