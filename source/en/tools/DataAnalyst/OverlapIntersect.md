title: Intersect
---

### Instructions  

An Intersect operation finds the intersection between two datasets. The feature objects (except point objects) in the intersected dataset are split where they intersect with the polygons in the intersect dataset. The spatial information of a result dataset from an Intersect operation is the same with that from a Clip operation. But the clip operation does not process attribute information, whereas an Intersect operation allows you to select attribute fields to be reserved.
 
![](img/intersectbuttonoperation.png)  
  
In addition to its own attribute fields, the result dataset contains all fileds of the two datasets involved in the operation. You can select the fields you want to keep as your needs.

![](img/intersectbuttonproperty.png)       

### Basic Steps   
  
1. In the Toolbox, click "Vector Analysis" > "Overlay Analysis" > "Intersect" to open the "Intersect" dialog box.
2. Specify the source dataset which will be intersected with another dataset to get the intersection parts.  
3. Specify the overlay dataset which will be used to intersect with the source dataset.
4. Specify the name of result dataset and the datasource where the result dataset will be saved in.
5. Set the fields of the result dataset. Click the "Field Settings..." button, and choose fields from the source dataset and the overlay dataset to constitute the fields of the result dataset. 
5. **Tolerance**: According to the datasets involved in the operation give a tolerance automatically. After the Intersect operation, if the distance between two points is less than the value, they will be merged. The default value is the tolerance value of source dataset, but if no tolerance value is set for the source dataset, the tolerance value here is related to the source dataset's coordinate system.
6. Click the Run image button to start the operation.