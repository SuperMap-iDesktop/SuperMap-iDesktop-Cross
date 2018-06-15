title: Thiessen Polygons
---
   
### Instructions
 
Thiessen polygons can be used for qualitative analysis, statistical analysis, proximity analysis, etc. For example, the area within a Thiessen polygon can be characterized with the properties of the corresponding discrete data point; the attribute value within a Thiessen polygon can be calculated based on the value of the discrete point in that polygon; the point in a Thiessen polygon is closer to its associated points than to other points. If the Thiessen polygon is an n-sided polygon, the point in it is adjacent to n discrete points.
  
This application creates Thiessen polygons for meteorological stations in China and then creates a Ranges Thematic map to show the ranges of average precipitation for each polygon area.
  
### Basic steps  

1. In the tool box, click "Vector Analysis" > "Proximity Analysis" > "Thiessen Polygon" to open the "Thiessen Polygon" dialog box. 
2. Specify the "WeatherStation_P" dataset as the source dataset. Set the name of result dataset and specify the datasource to save the result dataset.
3. Click "Run" to start the operation.
4. Create a range thematic map based on the result dataset, and then set the Expression as "Precipitation" and set the Range Count as 15 to get the picture as following shown.
  
![](img/TSResult.png)  
  
 