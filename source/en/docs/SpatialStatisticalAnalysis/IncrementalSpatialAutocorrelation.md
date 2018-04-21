title: Incremental Spatial Autocorrelation
---

　　Measures the spatial autocorrelation of distance between features. Z-scores reflect the intensity of spatial clustering, and statistically significant peak z-scores indicate distances where spatial processes promoting clustering are most pronounced. These peak distances are often appropriate values to use for tools with a Distance Band or Distance Radius parameter. When doing a similar hot spot analysis or density analysis, choose a suitable distance, very important thing. At this time, through the incremental space autocorrelation, the analysis gets a suitable distance.

　　For example, there is a login data of weibo in Beijing that wants to study the hotspots and aggregation of the spatial distribution of log-in points. And the number of logins in each location was used as the evaluation field, and the clustering was conducted from two aspects of space and number of people. The results can be obtained by hot spot analysis or kernel density analysis. Prior to this, the appropriate distance value was obtained through "incremental spatial autocorrelation" analysis.​

##### 　　Operating Instructions

　　Cross provides two functional entrances, as follows:

- Click "Spatial Analysis" tab > "Spatial Statistical Analysis" group > "Analyzing Mode" drop-down button > "Incremental Spatial Autocorrelation".
- Click the "Model Builder" > "New" to open the "Toolbox" panel, then double-click the "Spatial Statistical Analysis"-"Analyzing Mode"-"Incremental Spatial Autocorrelation", and the "Incremental Spatial Autocorrelation" dialog box will pop up.

##### 　　Main Parameters
- **Source Dataset**: sets up the vector data sets to be analyzed, supports points, lines, and regions three types of datasets. 
- **Assessment Field**: Select the property field for the analysis variable. The value of this field should be multiple values, and if all of the properties of the object have a value of 1, then it cannot be solved. Only numeric fields are supported.
- **Begin Distance**: The initial distance of the Incremental Spatial Autocorrelation Analysis can be determined based on the aggregation of data. If the beginning distance is not given, the default value is the minimum distance. At that distance, each feature of the dataset has at least one adjacent feature. If you have a location exception in your dataset, this distance may not be the most appropriate start distance.
- **Incremental Distance**: The distance from each analysis of the Incremental Spatial Autocorrelation is analyzed by the starting distance plus the distance increment. If the Distance Increment is not given , the smaller one is used in the average nearest neighbor distance or (Md - B)/C (the Md is maximum threshold distance, B is beginning distance, C is the number of distance). Ensures that the algorithm has to perform the calculation of the number of according to a specified distance, to ensure maximum distance not too much so that some features to all the other features or almost all other features as its adjacent features.
- **Incremental Distances Segments**: The number of the Incremental Spatial Autocorrelation has been assigned to analyze the dataset. The numerical range is: 2 ~ 30.
- **Measure Distance Method**: Currently, only Euclidean distance calculation is supported, namely the linear distance between two elements.
- **Spatial Weights Matrix Standardization**:When the distribution of features is likely to deviate due to sampling design or the aggregation scheme imposed, the use of the row standardization is recommended. After selecting the row normalization, each weight is divided by the sum of the rows (the sum of the weights of all adjacent features). The normalized weights are usually used in combination with the fixed distance adjacent features and are almost always used for the adjacent features of the adjacent side. This can reduce the deviation caused by the different number of adjacent features. Row standardization takes ownership weights, making them between 0 and 1, creating relative (rather than absolute) weight solutions. Whenever you want to handle the region features that represent administrative boundaries, you might want to choose the "Row Standardization" option.

##### 　　Results Output

　　After setting up the above parameters, click the "Run" button in the dialog box to perform the Incremental Spatial Autocorrelation Analysis. The results of the analysis will be shown in the "Results Display" text box.
　　
　　The results of the Incremental Spatial Autocorrelation are six values: distance increment, Moran‘s I, expectation index, variance, z-score, p-value. The z-score reflects the degree of spatial clustering, and the peak z-score with statistical significance indicates the most obvious distance of the clustering of the spatial process. The peak distance of Z is usually the appropriate value for the use of "range" or "distance radius" parameters. As shown in the figure above, the z-value is the largest when the increment is 500, indicating that 500 is suitable for the hot spot analysis of the weibo login data for the distance radius.

### ![](../img/seealso.png) Related Topics

![](../img/smalltitle.png)[Spatial autocorrelation](SpatialAutocorrelation.html)

![](../img/smalltitle.png)[High/Low clustering](HighLowClustering.html)

![](../img/smalltitle.png)[Average nearest neighbor](AverageNearestNeighbor.html)
