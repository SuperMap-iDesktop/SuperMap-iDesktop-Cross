---
title: Object Drawing Overview
---


　　The most common geometry objects in map drawing are points, lines, regions and texts. The &quot;Object Operations&quot; tab provides the functionalities for creating geometry object on map, including drawing point, drawing line, drawing region and drawing text. 

　　All the drawing of the object is performed on a editable layer, you can set several layers as editable, but when creating point, line, region or text objects, the objects will be drew on the selected layer. So if you want to create new object on a layer, it is needed to click the layer in the layer manager and set it as the current layer.

　　The concepts of object drawing is introduced below:

### Object overview

 -   Object: Geometry object is a abstract representing of the discrete spatial entity in GIS. A object has it's own attribute and action. You can create object in SuperMap directly or by converting.
 -   Single Object: A simple object, complex object, subobject is called a single object.
 -   part: Part is unit that composes simple object and complex object. A simple object is composed of one part, i.e., the simple object itself; a complex object is composed of two or more parts. 
 -   Simple Object: An object that has only one part.
 -   Complex Object: A complex object is an object with two or more parts of the same type.
 -   Compound Object: The object created by compound computing in CAD layer. It has no concept of parts.
 -   Parameterized Object: Draw thw geomatric objects through the main parameters, and it is continuous.

### The angles in object drawing.

-   Angle direction: The angle direction in the application are all anti-clockwise direction.
-   Start angle: The start angle of the drawing object is an angle between starting edge (true north) and another edge.



### About the Shift key

　　Using the Shift key when drawing some objects can make the operation more convenient.

-   Drawing line: When drawing line by inputting length and angle, press the Shift key can only drawing horizontal, vertical or 45° line.
-   Drawing rectangle (rounded rectangle): When drawing rectangle by inputting width, height, press the Shift key will get a square.
-   Drawing ellipse: When drawing ellipse by inputting the width and height of the bounding box, press the Shift key will get a circle.

### Use A on your keyboard

　　When you are in the process of drawing an object, you can press A to switch the status of your mouse to "Pan"

　　To switch the status of operation back to drawing, you can right-click mouse.



