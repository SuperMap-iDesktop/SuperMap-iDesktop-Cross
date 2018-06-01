title: Theories of Overlay Analysis
---

　　An overlay analysis is a procedure of generating new spatial information through processing or analyzing spatial data. For example, you can perform the overlay analysis on the national soil map dataset and the administrative district map dataset to get the desired result. Overlay analyses can also be used to process various attribute information for datasets.

  
　　Overlay analyses are widely applied in these domains: resource management, urban development assessment, land management, agriculture, forestry and animal husbandry, statistics, etc.

 
　　Spatial overlay analyses may involve logical operations such as And, Or, Not, and XOR. The table below describes the properties and rules of Boolean logical operations on layers. For simplicity, layers A, B, and C in a Euclidean space are defined to be binary images here.
  
   ![](img/LogicOperation.png)

The figure below is an illustration of Boolean logical operations including Contain, Intersect, Union, Difference, and XOR.

   ![](img/Overlay_17.png)

### Instructions  

If there is overlap during region objects in a region dataset, the dataset is invalid and the analysis results may be wrong, so performing topology check is suggested before doing the overlay analysis.

### Notes  
   
1. Overlay analyses can be performed on point/line/region objects. There are input dataset(in SuperMap GIS, it is called the 1st dataset) and overlay dataset(in SuperMap GIS, it is called the 2nd dataset).  
2. Note that the analysis may be error if there is overlap in the region dataset.  
3. The input and overlay datasets must have the same geometric reference data.  
4. It is advised to build a spatial index on the result dataset if the data size is large to improve the display efficiency of data.  



### ![](img/seealso.png) Related topics

![](img/smalltitle.png)[Generate single buffer](SingleBuffer.html)

![](img/smalltitle.png)[Examples on buffer analysis](BufferAnalyst_Example.html)


