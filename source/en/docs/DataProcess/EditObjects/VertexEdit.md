---
title: Vertex Edit
---


　　Vertexes is used for indicating coordinate points of geometry object (except text objects) in SuperMap. Shape and position of geometry object can be changed through the functions Add Vertex and Edit Vertex.

　　Vertex Edit can be used for line or region object only but not for ellipse object and arc object. You can see what the type of a selected object is in the property dialog box which pops up when right-clicking at the selected object and then selecting "Property".

### Edit Vertex

　　Vertexes of a selected object can be moved or deleted by using Edit Vertex function in an editable layer.

Basic Steps:

1.  Select a geometry object whose vertexes will be edited in an editable layer.
2.  Click on "Object Operations" > "Object Editing" gallery > "Edit Node" group > "Edit Node", after that, all vertexes of the selected object are highlighted on the map window.
3.  The vertexes can be moved or deleted, detail operation is as follow:
 - Move Vertex: Select a vertex by left-clicking mouse, then hold the left mouse button down and drag the mouse to move the vertex, at an appropriate position release the left mouse button to finish the moving.
 -  Delete Vertex: Select one or more vertexes, and then press Delete key to delete all selected vertexes.
4.  During the operation, you can select other geometry object to perform the Vertex Operation. You can finish the operation by clicking right mouse button.


### Add Vertex

　　A new vertex can be added for a selected object in an editable layer.

Basic Steps:

1.  Select a geometry object to  in an editable layer.
2.  Click on "Object Operations" > "Object Editing" gallery > "Edit Node" group > "Add Node", after that, all vertexes of the selected object are highlighted on the map window.
3.  Click on the boundary of the geometric object to add a new vertex for it.
4.  Go on adding vertices for other geometric objects with the same method.
  
### Modify

    Through using the "Modify" function, following operations can be done automatically.
  
+ When dragging or deleting a vertex of a region object, the adjacent region objects will automatically maintain the border avoiding modification of nodes and topological problems such as existing gaps or overlaps to improve work efficiency.
+ When doing the dragging or deleting operation to a common point, related lines can adjust their shapes as the position of vertices automatically.

Basic Steps:
  
1. Set the layer where the modified region objects locate editable.
2. Select a region object to be modified.
3. Click on "Object Operations" > "Object Editing" gallery > "Edit Node" group > "Modify" and all the vertices of the selected object will be shown in the current window.
4. Move the mouse to the vertex which you want to modify and hold down the left mouse button and move the mouse. Or click Delete to delete the vertex. After modifying the vertex, the geometry shape of the adjacent region object changes but always keeps the adjacency.</li>
5. In the course of operation, you can select other region or line objects, and continue the operation. You can end the operation by canceling the selected state of "Modify" or right-clicking the mouse.</li>
