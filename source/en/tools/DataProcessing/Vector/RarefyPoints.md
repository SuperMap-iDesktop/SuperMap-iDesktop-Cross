---
title: Rarefy Points
---

　　Take a point in the dataset as the center and according to the given radius draw a circle, and then randomly use a point to represent all the points within the circle.

**Application scenarios**

　　If points are too dense, there are a lot of overlaps appeared in your dataset in a smaller scale, hence you can rarefy points with the Rarefy Points function to improve the display performance and effect of your map.

### Basic steps

 1. In the toolbox, click "Data Processing" > "Vector" > "Rarefy Points" to open the "Integrate" dialog box.
 2. Specify the point dataset which will be rarefied.
 3. **Rarefy Radius**: Set a rarefied radius.
 4. **Statistic Type**: Select a statistic type to calculate values of original fields of result points retained within the rarefied radius and save these values into a newly statistic fields. 8 types are supported:
   - Average: The value of statistics field is the average value of attribute values of all points within the range of radius. 
   - Max: The value of statistics field is the maximum value in attribute values of all points within the range of radius.
   - Min: The value of statistics field is the minimum value in attribute values of all points within the range of radius. 
   - Sample Standard Deviation: The value of statistics field is the sample standard deviation of attribute values of all points within the range of radius
   - Sample Variance:  The value of statistics field is the sample standard deviation of attribute values of all points within the range of radius.  
   - Standard Deviation: The value of statistics field is the standard deviation of attribute values of all points within the range of radius. 
   - Variance: The value of statistics field is the variance of attribute values of all points within the range of radius. 
   - Sum: The value of statistics field is the sum of attribute values of all points within the range of radius. 
 5. **Random Save Rarefy Points**: Randomly save a point within the range of radius when checking "Randomly Save Rare Points". 
 6. **Keep Source Field**: Check it to save all field information of original data, if do not check it, only the selected fields in Statistic Filed will be retained.
 7. After setting the above parameters, click "OK" button to rarefy the specified point data set. After successful execution, the output window will have a corresponding prompt. 

  ![](img/RarefyPoints.png)

### Related topics

![](img/smalltitle.png) [Dissolve](Datafuse.html)



