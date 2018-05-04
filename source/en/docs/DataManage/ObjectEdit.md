---
title: Object Operations
---

### Select Objects

　　Before you select objects, you must make the layers where the objects are located in visible.
  
#### Make your layers visible  

Following several methods are provided:   
  
**Visible button**: In the Layer Manager, click the Show button, when the status of the button is ![](img/VisibleButton.png), the relative layer is visible; but if the status of the button is ![](img/UnvisibleButton.png), the relative layer is invisible.
  
**Context-menu**: Right click layers and then select "Visible".
  
**Visible check-box in the Layer Properties panel**: Right click your layer and select "Layer Properties", then in the pop-up panel, check "Visible" in the "Display Control" group.
  
**Note**   
   
The reasons why the layer still is invisible even though you have set it visible may be:
  
+ Some filter settings have been set so the layer was filtered. Solution: Remove other filtering settings.
+ The layer has been covered by other layers. Solution: Move up the layer until it is visible.
  
#### Make your layers selectable  

"Selectable" command is used to control whether your layer can be selected. To make a layer selectable, following several methods are provided:

**Selectable button**: In the Layer Manager, click the Selectable button, when the status of the button is ![](img/select.png), the relative layer can be selected; but if its status is ![](img/unselect.png), the relative layer can not be selected.
  
**Context-menu**: Right click layers and then select "Selectable".
  
**Selectable check-box in the Layer Properties panel**: Right click your layer and select "Layer Properties", then in the pop-up panel, check "Selectable" in the "Display Control" group. 
  
#### The ways of selecting objects  
  
Four ways are provided to select objects in selectable layers. In the "Object Operations" tab on the "Map Editing" group, click "Select" drop-down button and then you can choose a way to select objects.
  
**Select**  
  
Click "Select" to turn the operation status into a Select-by-clicking status, and the status of mouse is ![](img/pointSelect.png).
  
+ Select-by-clicking: You can click the object you want to select. You can hold Shift, then click more objects to select all of them.  
+ Select-by-drawing-rectangle: You can hold left-key of your mouse, then drag your mouse to draw a temporary rectangle, and then release the left key in an appropriate place, so all of objects whose centroids are located in the rectangle will be selected. You can hold Shift and continue to select more objects.
  
**Circle**  

By drawing a temporary circle to select objects. Click "Circle" and then the status of your mouse in the map window turns into ![](img/circleSelect.png).  

+ Left click your mouse at an appropriate place to determine the center and then drag your mouse to draw a circle. 
+ In an appropriate place, left click your mouse again to complete the drawing of the circle, and then all of objects whose centroids are located in the circle will be selected.   
+ You can hold Shift and continue to raw circles to select more objects.
  
**Polygon**  

By drawing a temporary polygon to select objects. Click "Polygon" and then the status of your mouse in the map window turns into ![](img/regionSelect.png).
  
+ Left click your mouse to begin draw a polygon, and then right click to finish the drawing. All of objects whose centroids are located in the polygon will be selected. 
+ You can hold Shift and continue to draw polygons to select more objects.   
  
**Line**  

By drawing a temporary polyline to select objects. Click "Line" and then the status of your mouse in the map window turns into ![](img/polylineSelect.png). 
  
+ Left click your mouse to begin draw a polygon, and then right click to finish the drawing. All of objects intersecting with the line will be selected.
+ You can hold Shift and continue to draw polylines to select more objects.   

You can use of a mix of all above four ways and hold "Shift" to select more objects. You can select objects cross layers, hence, all objects meeting the selection conditions will be selected and highlighted.
  
**Note** When your layers are selectable and the focus of your mouse is in the map window, by clicking "Ctrl+A", all objects will be selected in the map. Click "ESC" to cancel the selectable status of objects.

 
  




 
 





  
  











 


　