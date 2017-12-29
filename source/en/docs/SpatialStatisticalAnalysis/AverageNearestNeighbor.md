title: Average Nearest Neighbor
---

　　The Average Nearest Neighbor tool measures the distance between each feature centroid and its nearest neighbor's centroid location. It then averages all these nearest neighbor distances, and you get nearest neighbor index. If the index is less than 1, the pattern exhibits clustering. If the index is greater than 1, the trend is toward dispersion. If the average distance is less than the average for a hypothetical random distribution, the distribution of the features being analyzed is considered clustered. If the average distance is greater than a hypothetical random distribution, the features are considered dispersed. The average nearest neighbor can come up with an index of the specific concentration of a data, which can be used to compare different data with the largest concentration of data.

　　The average nearest neighbor method is very sensitive to the Area value. Consequently, the Average Nearest Neighbor tool is most effective for comparing different features in a fixed study area. If an Area parameter value is not specified, then the area of the minimum enclosing rectangle around the input features is used.


##### 　　Applications

- Evaluate competition or territory: Quantify and compare the spatial distribution of a variety of plant or animal species within a fixed study area; compare average nearest neighbor distances for different types of businesses within a city.
- Monitor changes over time: Evaluate changes in spatial clustering for a single type of business within a fixed study area over time.
- Compare an observed distribution to a control distribution: In a timber analysis, you may want to compare the pattern of harvested areas to the pattern of harvestable areas to determine if cut areas are more clustered than you would expect, given the distribution of harvestable timber overall.

##### 　　Operating Instructions

　　Cross provides two functional entrances, as follows:

- Click "Toolbox" - "Spatial Statistical Analysis" - "Analyzing Patterns" - "Average Nearest Neighbor" in the menu bar, and the "Average Nearest Neighbor" dialog box will pop up.
- In the "Visual Modeling" panel, double-click the "Spatial Statistical Analysis"-"Analyzing Patterns"-"Average Nearest Neighbor", and the "Average Nearest Neighbor" dialog box will pop up.

##### 　　Main Parameters
- **Input Feature**: Set up the vector data sets to be analyzed, supports points, lines, and regions three types of datasets. 
- **Study area area**: Set up the size of the area of study area, the unit is square meter, the area value is greater than or equal to 0;If the area of the study area is 0, the minimum external rectangular area of the source data set will be automatically calculated as the area of study area.
- **Distance Method**: Currently, only Euclidean distance calculation is supported, namely the linear distance between two elements.

##### 　　Results Output

　　After setting up the above parameters, click the "Run" button in the dialog box to perform the Average Nearest Neighbor Analysis. The results of the analysis include the following five parameters: the nearest neighbor index, the expected mean distance, the average observation distance, z-score and P-value, as shown in the figure below.

　　The nearest neighbor index is the ratio of average observation distance to the expected average distance.  If the nearest neighbor index is less than 1, the mode of expression is clustering. If the nearest neighbor index is greater than 1, the pattern of performance tends to spread. 



### ![](../img/seealso.png) Related Topics


![](../img/smalltitle.png)[Spatial autocorrelation](SpatialAutocorrelation.html)

![](../img/smalltitle.png)[High/Low clustering](HighLowClustering.html)

![](../img/smalltitle.png)[Incremental spatial autocorrelation](IncrementalSpatialAutocorrelation.html)

