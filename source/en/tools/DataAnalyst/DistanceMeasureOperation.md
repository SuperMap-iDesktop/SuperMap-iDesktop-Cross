title: Distance Calculation
---
   
### Instructions

Calculate Distances can be used to calculate the distances between points and points/lines/regions, and the distances from the points/lines/regions within certain extents to the specified points. The result is saved in a new tabular dataset. The records in the tabular dataset includes: source point ID, adjacent element ID(points/lines/regions), and distances between them. 

### Application  
  
This function is used to check the proximity relationship between two groups of features. You can calculate distances from companies, shops or restaurants etc. to the areas existing community problems, thereby place dustbins or arrange policemen in appropriate positions

Another use scenario is: Find all the wells close to and its distances to the polluted well within a specified extent.

### Basic Steps


1. In the Toolbox, click Vector Analysis > Proximity Analysis > Measure Distance to open the "Measure Distance" dialog box.
2. Set the source data: Set the datasource and the dataset (point or network dataset). For a network dataset, the vertices in it will be involved in the calculation. 
3. Proximity Data: Select the datasource and dataset taking part in the distance calculation. The type of the selected dataset could be point, line, region or network dataset.
4. Ways of calculation: Select a way of calculation.
  
	- **Closest Distance**: According to the given range, calculate distances between objects in the source dataset and objects in the proximity dataset then record one or more objects' IDs with the closest distances and distances value.
	- Meanwhile, you can check the "Set Min Distance" or "Set Max Distance" or both of them to set the max distance and the min distance, after that, these points whose distances to source points are less than the min distance or bigger than the max value will be ignored.
 	- **Distance in Range**: You are required to set the max and min distances in the same unit with your dataset. According to the given range, calculate distances between objects in the source dataset and objects in the proximity dataset then record IDS and distance values of these point objects whose distances to source point objects are within the range of max value and min value.
 　　![](img//Distance.png)   
5. Set the result dataset name and the datasource where the result dataset will be saved in. Click "Run" image button to perform the operation.
 

### An instance on the distance calculation   
  
**Theme**: The closest distances from schools to hospitals in Changchun city.   
**Data preparation**: A point dataset on schools and a point dataset on hospitals. Set the school point dataset as the source dataset and another one as the proximity dataset. The calculation way adopts "Closest Distance".
**Result**:   
  
1.  The result is a tabular dataset recording school IDs, hospital ID's and related distances.

　　![](img//DistanceResult1.png)    
2. Through the "Append Column" feature append records of Hospital_ID and Distance into the school point dataset. Then according to hospotals' IDs, append hospitals' name into the school point dataset.
  
   ![](img//AppendColums.png)    
4. Create a label thematic map to intuitively show the closest hospitals and distances.
　　![](img//DistanceApplication4.png)      

