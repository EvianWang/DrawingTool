import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Color;

public class ToolbarView extends JToolBar implements Observer {
    public JButton delete = new JButton("Delete");
    public JButton clear = new JButton("Clear");
    public JButton duplicate = new JButton("Duplicate");
    public ButtonGroup colors = new ButtonGroup();

    private DrawingModel model;

    ToolbarView(DrawingModel model) {
        super();
        this.model = model;
        model.addObserver(this);

        setFloatable(false);

        String[] shapes = {
                "Rectangle",
                "Line",
                "Ellipse"
        };
        JComboBox b1 = new JComboBox(shapes);
        b1.setMaximumSize(b1.getPreferredSize());
        b1.addActionListener(
                new java.awt.event.ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent e){
                        JComboBox combo = (JComboBox)e.getSource();
                        String currentShape = (String)combo.getSelectedItem();
                        model.setShape(ShapeModel.ShapeType.valueOf(currentShape));
                    }
                }
        );
        add(b1);

        String[] colors = {
                "Black",
                "Red",
                "Green",
                "Blue"
        };
        JComboBox b2 = new JComboBox(colors);
        b2.setMaximumSize(b2.preferredSize());
        b2.addActionListener(
                new java.awt.event.ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent e){
                        JComboBox combo = (JComboBox)e.getSource();
                        String currentColor = (String)combo.getSelectedItem();
                        if (currentColor == "Black"){
                            model.setColor(Color.BLACK);
                        }
                        if (currentColor == "Red"){
                            model.setColor(Color.RED);
                        }
                        if (currentColor == "Green"){
                            model.setColor(Color.GREEN);
                        }
                        if(currentColor == "Blue") {
                            model.setColor(Color.BLUE);
                        }
                    }
                }
        );
        //add(b2);

        add(duplicate);
        add(delete);
        add(clear);

        duplicate.addActionListener(buttonListener);
        delete.addActionListener(
                new java.awt.event.ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent e){
                        JButton b = (JButton)e.getSource();
                        if(b.getText() == "Delete"){
                            model.removeShape();
                        }
                    }
                }
        );
        clear.addActionListener(
                new java.awt.event.ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent e){
                        JButton b = (JButton)e.getSource();
                        if(b.getText() == "Clear"){
                            model.clearShapes();
                        }
                    }
                }
        );

        /*
        ActionListener drawingActionListener = e -> model.setShape(ShapeModel.ShapeType.valueOf(((JButton) e.getSource()).getText()));

        for(ShapeModel.ShapeType mode : ShapeModel.ShapeType.values()) {
            JMenuItem mi = new JMenuItem(mode.toString());
            mi.addActionListener(drawingActionListener);
            shape.add(mi);
        }
        */

        this.update(null, null);
    }

    ButtonListener buttonListener = new ButtonListener();
        class ButtonListener implements java.awt.event.ActionListener {
                public void actionPerformed(java.awt.event.ActionEvent e){
                        JButton b = (JButton)e.getSource();
                        if(b.getText() == "Duplicate"){
                                model.duplicate();
                                //model.notifyObserver();
                        }
                }
        }
   

    @Override
    public void update(Observable o, Object arg) {

    }
}
