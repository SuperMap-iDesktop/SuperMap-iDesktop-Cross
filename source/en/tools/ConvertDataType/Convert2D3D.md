---
title: Conversion between 2D and 3D data
---

　　This artical introduces how to convert between 2D data and 3D data.

### 2D data to 3D data
  
　　To convert 2D data to 3D data, you must set a corresponding elevation field. Fields and attributes of the 2D dataset will be retained in the converted 3D dataset meanwhile the elevation information will be added.


**Basic steps** 
  
 1. In the toolbox, click "Conversion" > "2D and 3D", then in the list select the feature as your needs.

   - For the conversion from 2D points/regions to 3D points/regions, you must set an elevation field which will be saved as the z-coordinate value of every object.
   - For the conversion from 2D lines to 3D lines, you are required to set a starting elevation and an ending elevation.

 2. Specify the dataset you want to convert and the result dataset name and a datasource to save the result dataset.



### 3D data to 2D data

　　Convert 3D points to 2D points, at the same time, the SMZ field and Z-coordinate information will not be retained in 2D datasets. Convert 3D lines/regions to 2D lines/regions, z-coordinate system will not be kept in 2D datasets.

**Basic steps**

 1. In the toolbox, click "Conversion" > "2D and 3D", then in the list select the feature as your needs.
 2. Specify the dataset you want to convert and the result dataset name and a datasource to save the result dataset.



