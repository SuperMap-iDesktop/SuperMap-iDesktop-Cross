title: Identity
---

### Instructions  

The result dataset has the same extent as the source dataset layer, but it contains geometric shapes and attributes data from the overlay dataset layer. An Identity operation is a process of performing an Intersect operation on the source dataset and the overlay dataset and then a Union operation on the intersection result and the source dataset. If the source dataset is a point dataset, all its objects will be retained in the newly generated dataset; if the source dataset is a line dataset, all its objects will also be retained in the newly generated dataset, but the objects intersecting with the overlapped dataset will be split at the intersections; if the source dataset is a region dataset, all polygons will be retained in the result dataset, but the objects from the source dataset will be divided into multiple objects at the intersections. 



 
![](img/identitybuttonoperation.png)  
  
Except for the system fields, other attribute fields of the result dataset comes from the attribute fields of both input datasets. You can select attribute fields that need to be reserved from the attribute tables of the source dataset and the overlay dataset, as shown below.

![](img/identitybuttonproperty.png)     

### Basic Steps   
  
1. In the Toolbox, click "Vector Analysis" > "Overlay Analysis" > "Identity" to open the "Identity" dialog box.    
2. Specify the source dataset.
3. Specify the overlap dataset.
4. Set the name of result dataset and the datasource where the result dataset will be saved in.
5. Set the attribute fields of result dataset. Click "Field Settings...", then in the pop-up dialog, you can select the fields as the fields of result dataset.
6. **Tolerance**: According to the datasets involved in the operation give a tolerance automatically. After the operation, if the distance between two points is less than the value, they will be merged. The default value is the tolerance value of source dataset, but if no tolerance value is set for the source dataset, the tolerance value here is related to the source dataset's coordinate system.
7. Click Run image button to start the operation.