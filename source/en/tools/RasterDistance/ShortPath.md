---
title: Calculate The Shortest Path
---

　　Calculate the shortest path from each target point to its nearest source based on the distance raster dataset, for example, the shortest path from the points in the suburbs to their nearest markets (source).

　　About source data, distance data and direction data: The distance raster and direction raster input must match with each other, that is, they must be created with the Generate distance raster functionality.

　　Path types:

 - Cell path: A path is generated for each grid cell, connecting that cell with the closest source.
 - Zone path: A path is generated for each cell zone. A cell zone is composed of contiguous cells with equal values. A path for a target zone is the least-cost path from the zone to the closest source. 
 - Single path: Only one path is generated for all grid cells. This path is the one with the least cost among all the paths connecting the entire target area dataset. 

### Basic steps

1. Two function entrances are provided:
 - Under the "Spatial Analysis" tab on the "Raster Analysis" group, click "Distance Raster" then select "Calculate Shortest Path".
  - In the toolbox, click "Raster Analysis" > "Distance Raster" > "Calculate Shortest Path" or drag the item into the model panel.

2. Target Dataset: Specify your dataset which can be a point, line, region or raster dataset.
3. Distance Data: Select the datasource and dataset. It is the distance dataset created by the Create Distance Raster function.  
4. Direction Data: Select the datasource and dataset. It is the direction dataset created by the Create Distance Raster function.
5. Select the path type. Three types are provided: cell path, single path or zone path. 
6. Result Data: Enter a name for the result dataset and specify a datasource to save the dataset.
7. Click "Run" image button to perform the operation.


### Related topics  
 
　　![](../img/smalltitle.png) [Distance raster overview](AboutRasterDistance.html)  

　　![](../img/smalltitle.png) [Generate distance raster](CreateRasterDistance.html)
   
　　![](../img/smalltitle.png) [The shortest surface path between two points](TwoPointDis.html) 
   
　　![](../img/smalltitle.png) [The shortest cost path between two points](TwoPointCostDis.html)    
 

