/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel; 
import java.awt.Shape;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ziyulin
 */
public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    private JPanel firstLine, secondLine, topPanel;
    // create the widgets for the firstLine Panel.
    private JButton undoButton, clearButton, color1Button, color2Button;
    private JComboBox<String> shapeComboBox;
    //create the widgets for the secondLine Panel.
    private JCheckBox filledCheckBox, gradientCheckBox, dashedCheckBox;
    private JSpinner lineWidthSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 99, 1));
    private JSpinner dashLengthSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 99, 1));
    // Variables for drawPanel.
    private DrawPanel drawPanel;
    // add status label
    private JLabel statusLabel;
    private Color color1 = Color.BLACK;  // You can initialize it to any color you want
    private Color color2 = Color.WHITE; 
    private boolean filled = false;
    private boolean useGradient = false;
    private boolean dashed = false;
    private float dashLength = 5.0f;
    private String selectedShapeType = "Line";  // Default shape type
    private MyShapes currentShape;
    //private ArrayList<MyShapes> shapes = new ArrayList<>(); 
    
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        // add widgets to panels
        super("Java 2D Drawings");
        // firstLine widgets
        undoButton = new JButton("Undo");
        clearButton = new JButton("Clear");
        shapeComboBox = new JComboBox<>(new String[]{"Line", "Oval", "Rectangle"});
        color1Button = new JButton("1st Color");
        color2Button = new JButton("2nd Color");
        JPanel firstLine = new JPanel(new FlowLayout());
        firstLine.add(color1Button);
        firstLine.add(color2Button);
        firstLine.add(shapeComboBox);
        firstLine.add(undoButton);
        firstLine.add(clearButton);
        // secondLine widgets
        filledCheckBox = new JCheckBox("Filled");
        gradientCheckBox = new JCheckBox("Use Gradient");
        dashedCheckBox = new JCheckBox("Dashed");
        lineWidthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        dashLengthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JPanel secondLine = new JPanel(new FlowLayout());
        secondLine.add(filledCheckBox);
        secondLine.add(gradientCheckBox);
        secondLine.add(dashedCheckBox);
        secondLine.add(new JLabel("Line Width"));
        secondLine.add(lineWidthSpinner);
        secondLine.add(new JLabel("Dash Length"));
        secondLine.add(dashLengthSpinner);
        // add top panel of two panels
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(firstLine);
        topPanel.add(secondLine);
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        drawPanel = new DrawPanel();
        drawPanel.setBackground(Color.WHITE);

        statusLabel = new JLabel("Mouse outside panel");

        add(topPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        //add listeners and event handlers
        color1Button.addActionListener(e -> color1 = JColorChooser.showDialog(this, "Choose a color", color1));
        color2Button.addActionListener(e -> color2 = JColorChooser.showDialog(this, "Choose a color", color2));
        shapeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedShapeType = (String) shapeComboBox.getSelectedItem();
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawPanel.shapes.size() > 0) {
                    drawPanel.shapes.remove(drawPanel.shapes.size() - 1);
                    drawPanel.repaint();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.shapes.clear();
                drawPanel.repaint();
            }
        });

        filledCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filled = filledCheckBox.isSelected();
            }
        });
        
        gradientCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useGradient = gradientCheckBox.isSelected();
            }
        });

        dashedCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashed = dashedCheckBox.isSelected();
            }
        });
        
        lineWidthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int lineWidth = (Integer) lineWidthSpinner.getValue();
            }
        });

        dashLengthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                dashLength = ((SpinnerNumberModel) dashLengthSpinner.getModel()).getNumber().floatValue();
            }
        });        

    }

    // Create event handlers, if needed

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {
        private MyShapes currentShape;
        private final ArrayList<MyShapes> shapes = new ArrayList<>(); // Change Shape to your custom shape class
        private Point startPoint, endPoint;

        public DrawPanel()
        {
            MouseHandler handler = new MouseHandler();
            addMouseListener(handler);
            addMouseMotionListener(handler);
        }
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            for (MyShapes shape : shapes) {
                shape.draw(g2d);
            }

            // Draw the shape that is currently being drawn by the user
            if (currentShape != null) {
                currentShape.draw(g2d);
            }


            //loop through and draw each shape in the shapes arraylist

        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {
            @Override
            public void mousePressed(MouseEvent event)
            {
                startPoint = event.getPoint();
                
                int lineWidth = (Integer)lineWidthSpinner.getValue();
                
                float dashLength = ((Integer) dashLengthSpinner.getValue()).floatValue();
                BasicStroke stroke;
                if (dashed) {
                    stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[]{dashLength}, 0);
                } else {
                    stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }

                Paint paint;
                if (useGradient) {
                    paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
                } else {
                    paint = color1;
                }

                if (null != selectedShapeType) switch (selectedShapeType) {
                    case "Line":
                        currentShape = new MyLine(startPoint, startPoint, color1, new BasicStroke());
                        break;
                // add more cases for other shapes
                    case "Oval":
                        currentShape = new MyOval(startPoint, startPoint, color1, new BasicStroke(), filled);
                        break;
                    case "Rectangle":
                        currentShape = new MyRectangle(startPoint, startPoint, color1, new BasicStroke(), filled);
                        break;
                    default:
                        break;
                }
            }

                
            
            @Override
            public void mouseReleased(MouseEvent event)
            {
                endPoint = event.getPoint();
                
                if (currentShape != null) {
                    currentShape.setEndPoint(endPoint);
                    shapes.add(currentShape);
                    currentShape = null;  // Reset currentShape
                    repaint();  // This will call paintComponent to redraw the panel
                }
                
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                endPoint = event.getPoint();
                
                if (currentShape != null) {
                    currentShape.setEndPoint(endPoint);
                }

                //currentShape.setEndPoint(endPoint);

                repaint();  
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusLabel.setText(String.format("Mouse at (%d, %d)", event.getX(), event.getY()));
            }
        }
    }
}

    

