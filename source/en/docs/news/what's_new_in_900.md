---
title: SuperMap iDesktop Cross 9D New Features
---


SuperMap iDesktop Cross 9D added BigData, Model Builder, Spatial Statistical Analysis and other related modules based on the previous version, and optimized and enriched Data Management, Data Editing, Data Processing, Mapping, Data Analysis module. Where Mapping module added functions such as Map Clip, Multiple process Map Cache and Terrain Color Connection, etc. Data Editing module added Object Split and Modify function; Data Processing module provided Dataset Dissolve, Resample, Extraction Center Line, type conversion and other functions. Data Analysis module provides Spatial Statistics Analysis to analyze and forecast the statistic value which is associated with spatial and time-space phenomenon. Abundant features was provided for Spatial Analysis, including Hydrology Analysis, Distance Raster, Build DEM and so on. The management and analysis of big data have been supported as well. Customizing data process flow through the Model Builder has been provided, that achieves the unmanned and continuous operations. 

**SuperMap iDesktop Cross 9D includes the following new features and optimizations:**

### Data Management

-   Supports to import GPS data into point datasets, *.GPX files, improves data compatibility for applications, and supports the export of point datasets with SmX, Smy, Ele, and Time property fields to GPS data.
-   Supports to import GeoJson Data while supports to export the vector data (point, line, region, etc.) as a GeoJson file. 
-   Supports to import and export of Simplejson data, that is, *.json format files. It also supports two forms of single file and folder import.
-   Supports to import Excel data into a attribute table dataset while supporting the export of attribute table information for datasets to Excel data.
-   Optimized import *.csv file operations, supports previewing CSV files, and supports direct import into point datasets
-   Supports for UDB, Oracleplus, PostgreSQL datasource to create a Vector Collection, can be a non-UDB datasource of the same type of Vector Collection, add to the vector collection of unified management, suitable for the large data volume of data divided into multiple child datasets for management, easy to unified display, property queries, and so on.
-   Added SDX SuperMap BigDataStore type of Web datasource, can be configured iServer service address and port, access to the server BigDataStore datasources, making it easy for users to use BigDataStore data directly perform the function of data processing and analysis of the dataset.

### Data Editing

-   Optimized the operation of the split line object, if the selected breakpoint is the intersection point of multiple objects, it supports setting the object to be interrupted, and the selected object in the list is highlighted in the map.

### Data Processing

-   Added Points Aggregate function, according to the spatial distribution, the point that satisfies the clustering condition is divided into one class.
-   Added Rarefy Points function, according to the specified radius, to a point in the dataset as the center dilute all the points in the circle. And then randomly use a point to represent all the points, the point after the diluting is not necessarily the center points.
-   Supports to vector dataset resample, When the object node is too dense, the dataset node can be resampled according to certain rules to simplify the node.
-   Supports to smooth dataset, when the line or the boundaries of the polygon too many segments, by increasing the way the node to use the curve or line segment to replace the polyline, so that the line edge smooth.
-   Added Dataset Dissolve function, can be the specified property field value of the next adjacent object, dissolved into an object, eliminate redundant data.
-   Supports eliminate tiny polygons, which combines a polygon in a dataset smaller than a specified area into an adjacent polygon, or deletes an orphaned polygon to simplify the data.
-   Added Edge Match function, the edges of the adjacent map can be connected.
-   Supports to extract border line，the boundary line of the region object can be extracted as a line dataset.
-   Supports to extract center line from double lines，the center line of the non-closed double line is extracted according to the given width, which is used for route data.
-   Added Extract Center Line from Region function, the center line of all the objects in the dataset is extracted as a line dataset, which is generally used to extract the center line of the road region.
-   Added Extract Main Center Line from Region function, can extract the main region is the center of the object as the longest distance in front or specified starting point and end point is extracted, commonly used in extraction of rivers, the backbone of the centerline.
-   Added Append Row function, used to append object and property information from one or more datasets to the target dataset.
-   Added Append Column function, used to add new field to the attribute table of the target dataset. 
-   Added Raster Resample function, assign the cell value or deduction value of original raster dataset to the pixel of new raster dataset.
-   Added Image Composite function, It is to combine three single band image data into a RGB image. Users can choose the best band according to the needs for combining to improve the image display speed and accuracy.
-   Added Raster Algebraic Operation function. This function is to operate the mathematics and functional operation for one or more raster data and meets the multiple raster analysis needs. 
-   Added Raster Reclassify function，redefine every pixel value of the raster dataset. 
-   Added Data Update function，uses the contents of one or more raster datasets to update the contents of the corresponding portions of another raster dataset.
-   Adds Data Conversion function, supports point and line data , attribute and spatial data, CAD and simple data, 2D and 3D conversion:
	-   Points, Lines and Regions Conversion： Conversion among point, line, and region datasets include Line to Region, Region to Line, Line to Point, and Region to Point. 
	-   Attribute and Spatial Data Conversion：Supports five modes of conversion of attribute to text, text to attribute, text to point data, attribute to point data, point attribute to region attribute.
	-   CAD and Simple Conversion：Conversion between CAD and Simple data includes CAD to Simple and Simple to CAD. 
	-   2D and 3D Conversion: a 2D dataset (point, line, region) into a 3D dataset (point, line, region). Convert a 3D dataset (point, line, region) into a 2D dataset (point, line, region).
-   Set the SQL expressions for obtain the value of the specified field, the text box displays all the only value in this field, the user double-click any one of the only value, convenient to the value of the input expression in the edit box.

### Attribute Table

-   Browsing the dataset attributes table, support structure and function through the right mouse button to provide attributes to open the current dataset the attributes of the panel, can quickly see the information such as field type, length, the default value, improve the usability of dataset attribute field management.
-   Supports to save the attributes table as Excel, and the field information of the specified attributes table can be exported to Excel.
-   Supports to save a attributes table as a attribute dataset, which can save the attribute table of a Vector dataset attribute table or attribute dataset.
-   Added Add Record function to add a new record to the attribute table.
-   Added Delete Record function, You can delete a vector dataset's attribute table or a pure attribute dataset, one or more rows of selected attribute records.
### Mapping
-   Supports to make a heat map for the event point data. Through a certain radius and color table, intuitively display point data heat distribution in the map. Such as making the microblogging login user's distribution map.
-   Supports to make a Grid Aggregation Map for the event point data. Uses the grid of different colors to represent the aggregation degree of the point data, it can be used to indicate the incidence of crime and traffic accidents in a region. 
-   Optimized the Map Clip function, redesigned the parametes settings mode to simplify the user operation. 
	-   The settings of clipping mode, erasing, and exact clipping have been adjusted to the layer list. The users set the clipping parameters for every layer easily, the different result will be shown in the window when using the different clipping modes. 
	-   Adjusted the setting mode about the layers, the layers in the list are involved in clipping, and the other layers are not involved in clipping. 
	-   Supports batch clipping multi objects, you can get the data of the regions that every object corresponds to at a time. 
-   Added Generate Map Cache function，Including Single Process and Multiple processes Map Cache，where the multiple processes operating simultaneously. Reasonable use of computer resources, effectively shortening the time to generate map cache. 	
    -   Before generating the cache, splits the task, so that each process can obtain the task to generate map cache. 
	-   Supports one computer to perform multiple processes opration, while also supporting multiple computers to perform multiple processes operations at the same time. 
	-   In the process of generating the map cache, you can increase or reduce the numbers of processes to facilitate the rational use of computer resources. 
	-   Supports to view the overall progress and every process's progress. 
-   Added Cache Update capabilities can be updated to specify the cache of specified areas and scale according to the specified cache scope.
-   Added the Cache Checking function and the Cache Supplementing function. You can check if there are missing tiles or white blocks in the cache and regenerate the missing cache. 
-   Added the Raser Color Balance function. The multiple raster layers which are added to the map window are displayed with a uniform color table and a uniform value field to . To achieve the effect of the entire map color display evenly. 
-   Added the function of map export to image dataset, the user can render map to image dataset quickly.
-   Added map export to picture function, you can specify the scope of the map export as a picture, can be used to print maps and other applications.
-   Optimizes the default range in the window when opening a empty map, the default range is the drawing range, and avoids the situation that the objects can't be drawn in the default range of the window. 
-   Optimizes the color table setting in the raster layer properties, enables editing of the selected color scheme, and makes the display of raster layers more consistent with user needs.
-   Added Associated Browse function.when you associate with the attribute table, to select all objects on the map when setting the list of selected attribute table.
-   Supports the import of pictures into a symbol library, which is converted to raster symbols for use in map maps.


### Spatial Analysis

-   The setting type of environment parameters of raster analysis has been optimized, setting a global analysis parameter is allowed, before performing raster analysis, a global parameter is used by default.
-   Added vector-rasterization which can turn vector data into raster data based on a specified property value.
-   Added grid-vectorization which can turn a raster dataset into a point, a line, or a region dataset.
-   Added Thin Raster feature which can be used when raster data is turned into vector line data to improve the speed and precision of vectorization.
-   Added the feature of constructing a Thiessen polygon, the created polygon elements can divide space and assign them to the closest point elements, so that a group of measure value can be applied to the proximal area.
-   Added the feature of calculating distance, which is used for calculating the shortest distance from a point object to another point, line or region object.
-   Supports the feature of constructing DEM, a point or line dataset with altitude information can be constructed by interpolation as terrain data. Also the lake region dataset and altitude value can be specified to add DEM data of lake information for constructed terrain.
-   Added DEM Dig Lake function, the raster value of lake region data can be changed to specified value according to existed lake data.
-   Added Kernel Density Analysis feature, which is used for calculating the density of point and line feature measurement values in the neighboring range to vividly reflect distribution of dispersed measurement values in the continuous range.
-   Added Simple Density Analysis feature, which is used for calculating the density of point features around each output raster cell.
-   Added Generate Distance Raster feature, which is used for calculating the distance from each cell to the closet source, and assigning the distance to a raster cell to generate a distance dataset, a direction dataset and a distribution dataset.
-   Added Calculate Shortes Path feature, according to a generated distance raster dataset, the shortest path from each target point to the closest source can be calculated out.
-   Supports for calculating the closest surface path from a source point to a target point along the raster surface.
-   Supports for calculating the least cost path from a specified source point to a target point along the raster surface.
-   Added Raster Statistics feature, which can help user grasp the distribution of raster value quickly. There are 5 ways provided: Basic Statistics, Zonal Statistics, Common Statistics, Elevation Statistics, Neighbour Statistics.
-   Added Sun Radiation feature, which enables you to map and analyze the effects of the sun over a geographic area for specific time periods to get a raster map about sun radiation.
-   Supports Multi-buffer Zone Analysis feature, which can create multiple buffers within a specified distance around geometric objects.

### Hydrology Analysis

-   Added Fill Spurious Sink feature, the spurious sink in DEM raster data can be filled.
-   Added Calculate Flow Direction feature for calculating hydrographic surface water flow direction, and then providing data for other analysis.
-   Added Calculate Flow Length to calculate the distance that each cell travels to the starting point or the end point along the flow, including upstream length and downstream length.
-   Added Calculate Flow Accumulation features to calculate water-collecting amount of each cell 
-   Added Calculate Catchment Point feature to generate catchment points raster data according to flow raster and cumulative water-collecting raster.
-   Added Watershed Segmentation feature to divide a basin into several sub basins.
-   Added Calculate Drainage Basin feature to create a raster dataset for description of drainage basins.
-   Added Extract Rater Water System feature to extract cells that accumulated water-collecting amount is greater than a specified threshold, thereby getting a raster system.
-   Added Stream Order feature to classify rivers based on some factors of these rivers.
-   Added Water System Vectorization feature which is used for converting a raster system to a vector water system, while saving grades of rivers into a property table of a result dataset.
-   Added Connect Water System feature to assign a unique value for every river in a water system based on a raster water system and a flow raster.

### Spatial Statistics Analysis

-   Added Spatial Statistical Analysis feature to analyze and forecast the statistic value which is associated with spatial and time-space phenomenon, including: Measuring Geographic Analysis, Analyzing Mode, Cluster Distributions, Modeling Spatial Relationship, Utilities Tools.
-   Added Measuring Geographic Analysis function which is used to analyzing distributional characteristic of elements, the provided features include: Central Element, Mean Center, Median Center, Directional Distribution, Linear Directional Mean, Standard Distance.
	-   Provided Central Element feature for finding out the element which is in the center of an input element class, the result is an object of an input element.
	-   Provided Mean Center feature to analyze the geographic center for a group of elements, the result data is new generated points.
	-   Provided Median Center feature which is used for finding a point that the overall distance to objects in a dataset is minimum.
	-   Provided Directional Distribution feature which can be used for creating standard deviational ellipses to summarize the spatial characteristic of geographic elements, eg. center trend, dispersion degree and direction trend.
	-   Provided Linear Directional Mean feature to analyze mean direction, length and geographic center of a group of line objects.
	-   Provided Standard Distance feature to measure the degree that object elements are concentrates or scatter around geometric mean center.
-   Added Analyzing Mode function to evaluate what mode (cluster spatial mode, discrete spatial mode or random spatial mode) is generated by objects, the supported features include: Spatial Autocorrelation, High/Low Clustering, Incremental Spatial Autocorrelation, Average Nearest Neighbor.
	-   Supports Spatial Autocorrelation analysis to evaluate whether the expressed mode is cluster mode, discrete mode or random mode.
	-   Supports High/Low Clustering analysis to measure the degree of clustering for high values or low values. 
	-   Supports Incremental Spatial Autocorrelation analysis to choose an appropriate distance threshold or distance range for the methods with the same parameters.
	-   Supports Average Nearest Neighbor analysis to calculate a nearest neighbor index according to an average distance from each object to its nearest neighboring object.
-   Added Cluster Distributions analysis to identify statistically significant hot points, cold points or spatial outliers, the provided features include: Cluster and Outlier Analysis, Hotspot Analysis, Optimized Hotspot Analysis.
	-   Supports Cluster and Outlier Analysis which is used for identifying the statistically significant hot points, cold points or spatial outliers.
	-   Supports Hotspot Analysis which can be used for indentifying statistically significant hot points, cold points.
	-   Supports Optimized Hotspot Analysis feature to count and create a map with statistically significant hot point and cold point and generate optimizable result by evaluating characteristic of input elements.
-   Added Modeling Spatial Relationship feature which can construct spatial weight matrices or data relationship models using regression analysis. Geographically Weighted Regression Analysis is provided for a local form of linear regression which is used to model spatially varying relationships.
-   Supports to generate spatial matrix files. A spatial weight matrix file can be constructed based on specified data to represent spatial relationships between elements in a dataset.

### Online Analysis

-   Suppports to manage big amount of data. You can manage own data by accessing a configured and accessible Hadoop address, such as: uploading data, downloading data, managing folders, managing tasks, etc.
-   The ability of analyzing and processing big data based on Spark is provided, users can make a map quickly, such as a kernel density analysis map.
-   Supports simple point density analysis feature to calculate the density of point features around each output raster cell.
-   Supports kernel density analysis feature to calculate the density of point and line feature measurement values in the neighboring range to vividly reflect distribution of dispersed measurement values in the continuous range.
-   Supports Single Query feature, you can construct filter conditions by spatial position relationships between geometric objects.
-   Two ways for aggregation analysis are provided, Grid Region Aggergation and Polygon Aggregation. Point elements in a map can be partitioned by grid regions or polygons. The count of points within every region can be calculated, the degree of aggregation can be reflected by taking the calculated result as the statistics values of regions.
-   Supports Vector Clip feature to clip a vector dataset. There are two ways: Internal Clip, External Clip.
-    Supports Summary Region feature to summarize the count of points, the length of lines, and the area of regions within a specified range, the statistic data of property fields of objects can be computed as well.

### Visually Building a Model

-   The Model Builder supports for building a model for importing data, processing data, and analyzing data, etc., you can design a workflow which can execute many operations to data continuously.
-   You can add, delete, modify a model in a canvas conveniently.
-   Hundreds of features are provided in Model Builder, including: kernel density analysis, big data analysis, measuring geographic analysis, and so on.
-    One node or whole model can be run, you can control and manage the process.
-   The constructed model can be save into a workspace for further usage or modification.
-   In the workspace manager, the Model Builder node has been added for renaming, deleting, opening an existed workflow.
-   The created model can be exported as a template, you can create a new model easily by loading a template.
-   Supporting to check whether a created model exists some errors, such as: a free node, endless loop, no data imported and so on.
-   Supports for searching a feature, so that you can locate the feature you want.

### Usability

-   Added user experience feature to collect some information from users, such as how to use iDesktop, Hot Features and the encountered problems. The information will help iDesktop team know and improve the features used commonly.
