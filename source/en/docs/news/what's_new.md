---
 title: SuperMap iDesktop Cross 9D SP1 New Features
---


SuperMap iDesktop Cross 9D Service Pack 1 desktop product on the basis of the previous version, adds and optimizes these modules: Mapping, Object Edit, Data Processing, Spatial Analysis, Model Builder related functions, etc. Where Mapping module add and optimize grouping layer and layer setting, support generated MongoDB multi-version map cache and support load and play version, while add map measurement function, and the color scheme has been added and optimized; For Object Edit, add fill gap function; For Spatial Analysis, adds cut fill、cut and fill of region、calculating DEM curvature function, and optimize the function of spatial statistical analysis; For Data Processing, adds batch projection transformation、coordinate transform function, optimize CSV import、VCT export、dissolve datasets function; At the same time, the use of a new Ribbon interface style, so that the functional organization clearer and intuitive, more fitting user habits, and support the interface skin-changing function, etc.

**Following details the added and optimized features on SuperMap iDesktop Cross 9D SP1：**

### Mapping

-   Adds layer grouping function, the layer can be grouped management, to achieve the same group of unified control; At the same time, grouped layers can be nested, that is, the layer grouping can also contain groupings.
-   Supports in layer manager by clicking on the icon on the layer node to direct layer visibility, selectable batch settings.
-   Optimizes layer visible scale set constraint form, when the user set the minimum scale is greater than or equal to the maximum scale or the maximum scale is less than equal to the maximum scale, the program will give warning messages to remind users.
-   Optimizes the layer node display, increasing visible scale state display.The user can see whether the layer is within the visible scale range by using the line frame status of the button in the layer node.
-   Supports to generate MongoDB multi-version map cache. Cache version information can be set when the cache is generated. It is mainly used to generate multiple map caches in different periods and different states of the same region, so as to facilitate the unified management and comparison. A multi-version cache is stored in the same database with the same cache name and different version information.
-   Supports to add MongoDB map cache in the Map window, users can set playback parameters, playback of multiple versions of the cache, the dynamic rendering of data changes occurred.
-   When generating a map cache, supports generates vector tile formats. Vector tiles occupy less server resources, transfer more convenient and more flexible display.
-   Optimizes the cache check function，support for checking solid and checking block，You can check if there are empty blocks in the cache and have block more than 100 pixels. 
-   Optimizes color scheme grouping, split "For point, line and label" into "For Lable Unique Value Map" and "For label ranges map" to suit different usage scenarios.
-   Adds 16 color schemes for unique values map, Graph thematic map, unique label thematic map and the range label thematic map, all of which are composed of more than 8 key colors.
-   Optimizes the random colors of the map, the map will only appear in the color scheme of the key color, making the color effect becomes more controllable.


### Object Edit

-   Adds fill gap function, user can expands the current region object to fill the gap between the object and the object around it.

### Spatial Analysis

-   Adds collect events function in the spatial statistic analysis, the user can use collect events tool to remove the overlapping elements in the input elements, and statistics the number of overlapping elements, and apply this statistic value as the weight value in the subsequent analysis.
-   Optimizes the output of the analyzing mode in the spatial statistical analysis, the analysis results can be directly presented and the results will be output.
-   Adds function for cutting and filling, This function is used to calculate the region and volume of surface and provides the basis and reference for construction or soil and water conservation decision-making. 
-   Adds function for cutting and filling of region，it can be applied when a region with ridges and valleys is to be flattened. The user can specify the region to be flattened and the target elevation. This method can be used to calculate the filled area, the cut area, the fill volume, and the cut volume. 
-   Adds function for inverse cutting and filling, it used to get the elevation after cut/fill based on the specified cut/fill dataset extent and cut/fill volume. 
-   Adds function for calculating DEM curvature. Used to calculate the surface curvature for a DEM raster, including average curvature, profile curvature, and plan curvature.

### Data Processing

-   Optimizes the import function of CSV data and support the import as vector dataset. 
-   Optimizes importing CSV, EXCEL data, and supports importing strings longer than 255.
-   Optimizes export VCT data function, supports exporting multiple datasets to one VCT file.
-   Optimizes dissolve datasets function, supports the dissolve of the text dataset.
-   Supports modifying dataset field types for Oracle, SQL Server, MySQL, PostgreSQL, kingbase five database engines.
-   Optimizes coordinate system settings related functions and operation mode: Support import, export, favorite, new, delete coordinate system, improve the ease of use of coordinate system settings. and supports the creation of coordinate systems through EPSG encoding.
-   Adds batch projection transformation function, projection transformation in batch allows you to transform projection for multiple datsets in the datsource at the same time. 
-   Adds coordinate transform function, that allows the user to convert a point of coordinates to a coordinate in another coordinate system.
-   Supports to save and open the workspace of MySQL, PostgreSQL, DM, etc.
-   Adds vector data integration function, users can set the integrate tolerance value, the tolerance range of nodes captured together.
-   Adds the measurement function of surface distance, surface area and surface volume for raster data.
-   Adds raster split function, the user sets the raster grading number, the program calculates the discontinuity point by Jenks the algorithm of the natural discontinuity point, and the raster value of each segment is based on the minimum raster value set by the user, and each level plus 1 is the new raster value.
-   Adds hidden row and hidden column functions, it can hide a row/column or multi-row/multi-column record in the property table.
Model

###  Model Builder

-   In the process of performing visual modeling, supports to stop the current operation by clicking the stop button.
-   Optimizes Canvas node and Task Manager feature display, when there are multiple identical function nodes in the canvas, the program is distinguished by different names, and also in Task Manager with the same name, so that the different nodes of the same function have a clear correspondence between the canvas and the task Manager.


### Interface Upgrade

-  In this version, the new Ribbon interface style is adopted, which replaces the traditional menu bar mode, makes the function organization clearer and intuitive, and synchronizes the function of the modeling toolbox with the functions of the Ribbon interface, which is convenient for users.
-  Supports interface for skin change, currently provide Black, Default two different theme style switch. 
-  Optimizes the modeling toolbox display effect.


### Ease of Use

-   The subwindow tab supports right-click menus to close all windows and close other windows.


