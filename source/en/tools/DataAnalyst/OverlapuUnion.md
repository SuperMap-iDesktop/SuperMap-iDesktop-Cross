title: Union
---

### Instructions  

Union is an operation merging two datasets. Both the source dataset and the overlay dataset must be region datasets. 
After a union operation, the polygons are split where the two region datasets intersect. Topology relationship is rebuilt, and the geometric and attribute information of the two datasets are all retained in the result dataset.
  
![](img/unionbuttonoperation.png)  
  
The attribute table of the result dataset comes from the attribute tables of both input datasets. During a union operation, you can select attribute fields that need to be reserved from the attribute tables of dataset A and B. 
  
![](img/unionbuttonproperty.png) 

### Basic steps   
  
1. In the Toolbox, click "Vector Analysis" > "Overlay Analysis" > "Union" to open the "Union" dialog box.
2. Specify the source dataset. 
3. Specify the overlap dataset.
4. Set the name of result dataset and the datasource where the result dataset will be saved in.
5. Set the attribute fields of result dataset. Click "Field Settings...", then in the pop-up dialog, you can select the fields as the fields of result dataset.
6. **Tolerance**: According to the datasets involved in the operation give a tolerance automatically. After the operation, if the distance between two points is less than the value, they will be merged. The default value is the tolerance value of source dataset, but if no tolerance value is set for the source dataset, the tolerance value here is related to the source dataset's coordinate system.
7. Click Run image button to start the operation.