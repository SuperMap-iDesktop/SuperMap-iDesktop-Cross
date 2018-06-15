title: Erase
---

### Instructions  

Erase is a process of erasing the overlapped parts from source datasets. The source dataset can be a point, a line or a region dataset, but the erasing dataset must be a region dataset. Overlap the erasing dataset and the source dataset, and the features that fall in these polygons are to be erased, and the features that fall outside of the polygons will be output to the result dataset. The Erase operation works similarly to the Clip operation. The only difference is that the content reserved in the result is different.
 
![](img/erasebuttonoperation.png)  
  
The attribute table of result dataset comes from the attribute table of the source dataset. The attribute table types are the same. As demonstrated in the figure below, all the non-system fields in dataset A are added automatically.

![](img/erasebuttonproperty.png)   

### Basic Steps   
  
1. In the Toolbox, click "Vector Analysis" > "Overlay Analysis" > "Erase" to open the "Erase" dialog box.  
2. Specify the source dataset which will be erased by another dataset.
3. Specify the overlay dataset which will be used to erase the source dataset.
4. Set the name of result dataset, and the datasource where the dataset will be saved in.
5. **Tolerance**: According to the datasets involved in the operation give a tolerance automatically. After the Erase operation, if the distance between two points is less than the value, they will be merged. The default value is the tolerance value of source dataset, but if no tolerance value is set for the source dataset, the tolerance value here is related to the source dataset's coordinate system.
6. Click Run image button to start the operation.