# 2D-DrawingApplication
**Java 2D Drawing Application**

![Java2DDrawingApplication](https://github.com/zbl5332/2D-DrawingApplication/blob/6751296408f74e966d5bf0f843bec828309ab6ac/Java2DDrawingApplication.png)

The application contains the following elements:

 a combo box for selecting the shape to draw, a line, oval, or rectangle.
 two JButtons that each show a JColorChooser dialog to allow the user to choose the first and second color in the gradient.
 an Undo button to undo the last shape drawn.
 a Clear button to clear all shapes from the drawing.
 a checkbox which specifies if the shape should be filled or unfilled.
 a checkbox to specify whether to paint using a gradient.
 a checkbox for specifying whether to draw a dashed or solid line.
 a JSpinner for entering the Stroke width.
 a JSpinner for entering the Stroke dash length.
 a JPanel on which the shapes are drawn.
 a status bar JLabel at the bottom of the frame that displays the current location of the mouse on the draw panel.

 
If the user selects to draw with a gradient, set the Paint on the shape to be a gradient of the two colors chosen by the user. If the user does not chose to draw with a gradient, then Paint with a solid color of the 1st Color. 

Set the stroke for a line to be drawn, where the first stroke line creates a dashed line and dashLength is a one element float array with the dash length in the first element. The second stroke line creates an undashed line with the line width specified from the GUI.

Note: When dragging the mouse to create a new shape, the shape should be drawn as the mouse is dragged.

In the paintComponent(Graphics g) method of the DrawPanel, to loop through and draw each shape created by the user, you will loop through an ArrayList of MyShapes, that you built, and call the draw(Graphics2D g2d) method for each shape. The draw method is already implemented in the MyShapes hierarchy.

