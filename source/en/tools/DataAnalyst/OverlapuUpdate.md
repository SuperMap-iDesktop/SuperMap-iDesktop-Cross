title: Update
---

### Instructions  


Replace the overlapped parts from the source dataset with the updated dataset. The result dataset retains the geometry shapes and attribute information of the source dataset. The source dataset and the update dataset are must be of region datasets.

![](img/updatebuttonoperation.png)  
  
The attribute table of result dataset are as follows:

![](img/updatebuttonproperty.png)            

### Basic steps   
  
1. In the Toolbox, click "Vector Analysis" > "Overlay Analysis" > "Update" to open the "Update" dialog box.
2. Specify the source dataset. 
3. Specify the overlap dataset.
4. Set the name of result dataset and the datasource where the result dataset will be saved in.
5. **Tolerance**: According to the datasets involved in the operation give a tolerance automatically. After the operation, if the distance between two points is less than the value, they will be merged. The default value is the tolerance value of source dataset, but if no tolerance value is set for the source dataset, the tolerance value here is related to the source dataset's coordinate system.
6. Click Run image button to start the operation.