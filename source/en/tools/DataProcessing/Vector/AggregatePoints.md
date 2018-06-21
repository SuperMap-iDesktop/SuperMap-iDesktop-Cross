---
title: Points Aggregate
---

　　The function of aggregating points can aggregate dense points or construct a polygon for the same cluster of points according to density-based clustering. After aggregation, a field named "ResultType" will be generated for aggregation type information statistics.


**Theory**

　　With DBSCAN algorithm (Density-based spatial clustering of applications with noise), groups a set of points in spatial. DBSCAN can divide the points in high-density regions into clusters, and can find out any shapes of clusters from spatial data with noise. The generation of clusters is controlled through the threshold of point number and cluster radius(e).If the e-neighborhood (the circular area that a given object is its center and e is its radius) of a given point contains at least minPts points, then the point is considered a core point. If a given point P is within the e-neighborhood of a given core point Q, P is reachable directly from Q. The algorithm DBSCAN find continually cluster through checking the e-neighborhood of every point until no more points can be added. If the e-neighborhood of a point P contains more than MinPts points, a new cluster will be created with P is the core center.


**Applications scenarios**

　　This function is applicable to massive data for classification according to the spatial density of points. Also, you can construct region objects around clusters of proximate points.

 1. Remove noisy points from point cloud data  

　　While acquiring point cloud data, it will be affected or disturbed by some factors such as human actions, instruments, environment, measurement methods, etc. Therefore, point cloud data acquired will contain some noisy points. Point cloud acquired for measured features is often distributed continuously along the surface and the noisy points are often randomly distributed far way from features. Therefore, you can remove noises through the Point Aggregate function. 

 2. Create regions where the signal is weak based on monitored data:

　　According to the signal strength monitored by communication monitoring system, with "Point Aggregate" function, creates a polygon around the weak-signal points to help you find an appropriate place to build a signal tower.
 


### Basic steps

 1. In the toolbox, click "Data Processing" > "Vector" > "Points Aggregate" to open the "Points Aggregate" dialog box.
 2. Specify your dataset which you want to perform the operation on.
 3. Radius: Used to set radius for point aggregation. When number of points within the specified radius is bigger than the point count threshold, these points belong to one class. The unit for radius can be set through the drop-down list. 
 4. Point Count Threshold: Used to set and display the least number of points for a cluster. The value should be greater than or equal 2. The bigger the value is, the harder creating a cluster is. 4 is recommended.
 5. Set a name for result dataset and specify a datasource to save the result. A region will be created around the points that belong to the same cluster. 
 6. Click OK button to perform the operation. After successful analysis, the output window will have a corresponding prompt. Clustering results obtained are shown below. 

  ![](img/AggregatePoints.png)

### Related topics

![](img/smalltitle.png) [Dissolve](Datafuse.html)



