title: Analyzing Patterns
---

　　The Analyzing Patterns uses inferential statistics. The null hypothesis will be established in advance of the statistical test, assuming that the features or the values associated with the features are represented as spatial random patterns. The result of the analysis is a p-value, which is used to represent the correct probability of null hypothesis, to determine whether to accept the null hypothesis or reject the null hypothesis. The results of the analysis will also be obtained by the z-value, which is used to represent the multiple of the standard deviation to determine whether the data is clustered, discrete or random. Calculating a probability may be important if you need to have a high level of confidence in a particular decision. If there are public safety or legal implications associated with your decision, for example, you may need to justify your decision using statistical evidence.

　　The data model can be quantified by Analyzing Patterns. The data is first analyzed and then analyzed further. The Analyzing Patterns provides the Spatial Autocorrelation, High/Low clustering, Incremental Spatial Autocorrelation, and the Average Nearest Neighbor, which are described as follows:

- **[Spatial Autocorrelation]** (SpatialAutocorrelation.html): Measures spatial autocorrelation based on feature locations and attribute values using the Global Moran's I statistic.
- **[High/Low Clustering]** (HighLowClustering.html) :Measures the degree of clustering for either high values or low values.
- **[Incremental Spatial Autocorrelation]** (IncrementalSpatialAutocorrelation.html):Measures spatial autocorrelation for a series of distances and optionally creates a line graph of those distances and their corresponding z-scores. Z-scores reflect the intensity of spatial clustering, and statistically significant peak z-scores indicate distances where spatial processes promoting clustering are most pronounced. These peak distances are often appropriate values to use for tools with a Distance Band or Distance Radius parameter.
- **[Average Nearest Neighbor]** (AverageNearestNeighbor.html):Calculates a nearest neighbor index based on the average distance from each feature to its nearest neighboring feature.

### ![](../img/seealso.png) Related Topics

![](../img/smalltitle.png)[Basic vocabulary](BasicVocabulary.html)

![](../img/smalltitle.png)[Measuring geographic analysis](MeasureGeographicDistributions.html)

![](../img/smalltitle.png)[Cluster analysis](Clusters.html)

![](../img/smalltitle.png)[Modeling spatial relationships](SpatialRelationshipModeling.html)
