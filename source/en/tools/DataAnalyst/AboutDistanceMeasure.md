title: Theory on Distance Calculation
---
   
### Instructions
 
Calculate Distances can be used to calculate the distances from points to other points, lines or regions. The result is saved in a new tabular dataset. When calculating distances, please notice:
     
-  The source and reference datasets must have the same coordinate system. The result distance is in the same unit with the dataset coordinate system.  
-  There can be more than one reference features that are closest to a source point, in that case, all their IDs and distance will be recorded in the tabular dataset.
-  The distance will be considered as 0 if the source and reference features contain one another or overlap.  
-  If the reference dataset is a region dataset, then the boundary of the region is used in the calculation. If the source point is within a region, then their distance is 0. 
 
  
### Theory on Distance Calculation  
  
**1.The distance between two points is the length of straight line connecting them**  
  
To calculate the closest distance, it will calculate the distance from the source point to the reference features, and save the closest one or more features as the result.
 
　　![](img//DistanceMeasure1.png)   
  
**2.Distance from a point to a line is either the length of perpendicular or the distance between the point and the closest vertex**  
  
The shortest distance from a point to a line segment is the length of perpendicular to the line segment. If the foot of source point in the line is not in the line segment, then the distance to the closest end point is the shortest distance. The distance between a point and a polyline should be the distance between the point and the line segment which is closest to the point.


　　![](img//DistanceMeasure2.png)   
    
  
**3.The distance between a point to a region is the distance from the point to one boundary of the region**  
  
Because a polygon is an area enclosed by a series of line segments, calculating the distance from a point to a polygon involves identifying the closest line segment to the point, and then calculate the distance from the point to the line segment. The distance is positive only when the point is outside of the polygon; otherwise, it is zero.

　　![](img//DistanceMeasure3.png)   
     