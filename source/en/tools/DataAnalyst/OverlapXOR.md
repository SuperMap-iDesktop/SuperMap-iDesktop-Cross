title: XOR
---

### Instructions  

The objects or portions of objects in the source dataset and overlay dataset that do not overlap will be written into the result dataset.

![](img/xorbuttonoperation.png)  
  
The result attribute table contains all non-system fields of the source and overlay datasets. As follows:

![](img/xorbuttonproperty.png)          

### Basic steps   
  
1. In the Toolbox, click "Vector Analysis" > "Overlay Analysis" > "XOR" to open the "XOR" dialog box.
2. Specify the source dataset. 
3. Specify the overlap dataset.
4. Set the name of result dataset and the datasource where the result dataset will be saved in.
5. Set the attribute fields of result dataset. Click "Field Settings...", then in the pop-up dialog, you can select the fields as the fields of result dataset.
5. **Tolerance**: According to the datasets involved in the operation give a tolerance automatically. After the operation, if the distance between two points is less than the value, they will be merged. The default value is the tolerance value of source dataset.
　　But if no tolerance value is set for the source dataset, the tolerance value here is related to the source dataset's coordinate system. For the projection coordinate system, the default value is 1m; for the geography coordinate system, the default value is 0.00001°; for the planar coordinate system, the default value is 1 in the same unit with the coordinate system.
7. Click Run image button to start the operation.