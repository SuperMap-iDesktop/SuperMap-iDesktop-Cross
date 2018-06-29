---
title: Elevation Statistics
---
　　Get the altitude information of the 2D point data according to the altitude information of the raster data and then export the result as a 3D point dataset.
    
   
### Basic steps


 1. Two function entrances are provided:
 - Under the "Spatial Analysis" tab on the "Raster Analysis" group, click "Raster Statistics" then select "Elevation Statistics".
  - In the toolbox, click "Raster Analysis" > "Raster Statistics" > "Elevation Statistics" or drag the item into the model panel.
 2. Specify the point dataset (2D) for counting elevation information.
 3. Specify the raster dataset that is source of elevation information.
 4. Enter a name for the result dataset and specify a datasource to save the result.
 5. Click "Run" image button to execute the operation.  
   
### Note  
  
- The result dataset is a 3D point dataset whose attribute information has a integration of system fields of the 2D point dataset and adds a new field named SMZ (elevation field) for recording elevation information of every point.
- Points in result dataset may be less than in the 2D point dataset because of some points are located out of the raster regions thereby leading to these points do not have elevation information.


   

### Related topics  

![](img/smalltitle.png) [Basic statistics](BasicStatistic.html)  
![](img/smalltitle.png) [Common statistics](CommonStatistic.html)  
![](img/smalltitle.png) [Neighbor statistics](NeighbourStatistic.html)    
![](img/smalltitle.png) [Zonal statistics](ZonalStatistic.html)    
  



