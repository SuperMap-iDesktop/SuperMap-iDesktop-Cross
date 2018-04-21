title: High/Low Clustering
---

　　High/Low Clustering is the degree of clustering of high or low values using the Getis-Ord General G statistics. The General G index is also a corollary statistic that USES limited data to estimate the overall situation. When the p-value returned by this tool is small and statistically significant, the null hypothesis can be rejected. If the z-score value is positive, the observed General G index is larger than the expected General G index, indicating high values for the attribute are clustered in the study area. If the z-score value is negative, the observed General G index is smaller than the expected index, indicating that low values are clustered in the study area. 

##### 　　Applications

- Look for unexpected spikes in the number of emergency room visits, which might indicate an outbreak of a local or regional health problem.
- Comparing the spatial pattern of different types of retail within a city to see which types cluster with competition to take advantage of comparison shopping (automobile dealerships, for example) and which types repel competition (fitness centers/gyms, for example). 
- Summarizing the level at which spatial phenomena cluster to examine changes at different times or in different locations. For example, it is known that cities and their populations cluster. Using High/Low Clustering analysis, you can compare the level of population clustering within a single city over time (analysis of urban growth and density). 

##### 　　Operating Instructions

　　Cross provides two functional entrances, as follows:

- Click "Spatial Analysis" tab > "Spatial Statistical Analysis" group > "Analyzing Mode" - "High/Low Clustering".
- Click the "Model Builder" > "New" button to open the "Toolbox" panel, then double-click the "Spatial Statistical Analysis" > "Analyzing Mode" > "High/Low Clustering".

##### 　　Main Parameters
- **Concept Model**:Your choice for the Conceptualized Model should reflect inherent relationships among the features you are analyzing. The more realistically you can model how features interact with each other in space, the more accurate your results will be. 
  - Fixed Distance: applicable to point and region with large changes in region size.
  - Region Adjacent (Common Edges or Intersect): applicable to the data of adjacent side and intersection.
  - Region Adjacent (Adjacency Point, Common Edges or Intersect): applicable to the region data with adjacent points,adjacent sides and intersecting.
  - Inverse Distance: all features are regarded as adjacent features of all other features. All features affect the target features, but as distance increases, the effect is smaller, and the weight between the elements is one over the distance, which is applicable to continuous data.
  - Inverse Distance Square: similar to the "Inverse Distance Model", with the increase of distance, the influence decreases faster, and the weight between the features is one over the square of the distance.
  - k Nearest: The K features closest to the target features are contained in the calculation of the target features (the weight is 1), and the remaining features will be excluded from the target feature calculation (the weight is 0). This option is very effective if you want to ensure that you have a minimum number of contiguous features for analysis. This approach works well when the distribution of data changes in the study area so that some features are removed from all other features. When the proportion of fixed analysis is not as important as the number of fixed adjacent objects, k-nearest neighbor method is suitable.
  - Spatial Weight Matrix File: space weight matrix file is required. The spatial weight is a number that reflects the distance, time, or other cost of each feature and any other feature in the dataset. If you want to model the accessibility of city services, for example, to look for areas where urban crime is concentrated, it is a good idea to use the network to model spatial relationships. Before analyzing, create a spatial weight matrix file (.swm) using the generated network space weight tool, and then specify the full path of the SWM file created.
  - Undifferentiated Regional:The model is a combination of "Inverse Distance Model" and "Fixed Distance Model". Each feature is regarded as an adjacent feature of other features. This option is not suitable for large datasets. The features within the specified fixed distance range have equal weights (weights 1);In addition to the specified distance of fixed distance, the effect will be smaller as distance increases.
- **Break Distance Tolerance**:"-1" means to calculate and apply the default distance, which is to ensure that each element has at least one adjacent feature;" 0 "means that no distance is applied, and each feature is an adjacent feature. Non-zero positive values are adjacent features when the distance between the features is smaller than this value.
- **Inverse Distance Power Exponent**:The higher the exponent, the higher the power value, the smaller the exponential effect.
- **Number of Adjacent Features**:Set a positive integer, indicating that the nearest K features around the target features are adjacent features.
- **Measure Distance Method**:Currently, Euclidean distance is supported by the linear distance between two elements.
- **Spatial Weights Matrix Standardization**:When the distribution of elements is likely to deviate due to sampling design or the aggregation scheme imposed, the use of the row standardization is recommended. After selecting the row normalization, each weight is divided by the sum of the rows (the sum of the weights of all adjacent features). The normalized weights are usually used in combination with the fixed distance adjacent features and are almost always used for the adjacent features of the adjacent side. This can reduce the deviation caused by the different number of adjacent features. Row standardization takes ownership weights, making them between 0 and 1, creating relative (rather than absolute) weight solutions. Whenever you want to handle the region features that represent administrative boundaries, you might want to choose the "Row Standardization" option.

##### 　　Results Output

　　After setting up the above parameters, click the "Run" button in the dialog box to perform the High/Low Clustering Analysis. The results of the analysis will be shown in the "Output Window" text box.

    ![](img/HighOrLowClustering.png)　　
　　
　　The High/Low Clustering (Getis-Ord General G) tool is an inferential statistic, which means that the results of the analysis are interpreted within the context of the null hypothesis. When the p-value returned by this tool is small and statistically significant, the null hypothesis can be rejected. If the null hypothesis is rejected, then the sign of the z-score becomes important. If the z-score value is positive, the observed General G index is larger than the expected General G index, indicating high values for the attribute are clustered in the study area. If the z-score value is negative, the observed General G index is smaller than the expected index, indicating that low values are clustered in the study area. 

- The z-score value is positive —— The observed General G index is larger than the expected General G index —— High values are clustered in the study area.
- The z-score value is negative —— The expected General G index is larger than the observed General G index —— Low values are clustered in the study area.

　　The High/Low Clustering analysis is most appropriate when you have a fairly even distribution of values and are looking for unexpected spatial spikes of high values. when the observed General G index is equal the expected General G index ，both the high and low values cluster, they tend to cancel each other out. If you are interested in measuring spatial clustering when both the high values and the low values cluster, use the Spatial Autocorrelation tool.

| Result                           | High/Low Clustering                                    | Spatial Autocorrelation                                    |
| ---------------------------- | ---------------------------------------- | ---------------------------------------- |
| The p-value is **not** statistically significant.       | You can't reject the null hypothesis. It is quite possible that the spatial distribution of feature attribute values is the result of random spatial processes. Said another way, the observed spatial pattern of values could well be one of many, many possible versions of complete spatial randomness. |                                          |
| The p-value **is** statistically significant, and the z-score is positive. | You may reject the null hypothesis. The spatial distribution of **high** values in the dataset is more spatially clustered than would be expected if underlying spatial processes were truly random.  | You may reject the null hypothesis. The spatial distribution of high values and/or low values in the dataset is more spatially clustered than would be expected if underlying spatial processes were truly random.|
| The p-value **is** statistically significant, and the z-score is negative. | You may reject the null hypothesis. The spatial distribution of **low** values in the dataset is more spatially clustered than would be expected if underlying spatial processes were truly random. | You may reject the null hypothesis. The spatial distribution of high values and low values in the dataset is more spatially **dispersed** than would be expected if underlying spatial processes were truly random. A dispersed spatial pattern often reflects some type of competitive process: a feature with a high value repels other features with high values; similarly, a feature with a low value repels other features with low values. |

### ![](../img/seealso.png) Related Topics

![](../img/smalltitle.png)[Spatial autocorrelation](SpatialAutocorrelation.html)

![](../img/smalltitle.png)[Incremental spatial autocorrelation](IncrementalSpatialAutocorrelation.html)

![](../img/smalltitle.png)[Average nearest neighbor](AverageNearestNeighbor.html)