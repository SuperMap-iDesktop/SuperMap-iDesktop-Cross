---
title: Data Import
---

### Instruction

　　Besides SuperMap's data formats, varieties of external data formats need being operated. Data conversion function provides the conversion between different data formats to supports various data formats.

　　Through the Data Import dialog box, abundant data types can be imported into Cross to operate.
The supportive data types contain: [AutoCAD data](ImportAutoCAD.html), [ArcGis data](ImportArcGIS.html), [MapGIS data](ImportMapGIS.html), [MapInfo data](ImportMapInfo.html), [Spreadsheet](ImportTable.html), [Image/raster data](ImportIMG.html), [3D model data](ImportModel.html), [Lidar data](ImportLidar.html), [Google data](ImportKML.html), [Vector file](ImportVectorFiles.html), [GeoJson](ImportFolder.html), [SimpleJson](ImportFolder.html), [GJB](ImportFolder.html), [Telecom data](ImportFolder.html), etc..


### Note

 1. By default, the files will be imported into the first non-readonly datasource displayed in Workspace Manager if no datasource is selected.
 2. If all the datasources in the current workspace manager are read-only, the Import button is disabled.
 3. If you select several vector and raster files, the parameter settings is based on the last file you selected. 
 4. If you select both raster and vector data, the common parameters are displayed only. 
 5.  After the data is imported, the coordinate system of the result dataset is the same with that of the datasource by default. 
 6. There are some differences between vector import and raster import. 
   - Replace: Replace the namesake dataset with the newly imported data.
   - Append: For vector data, the imported data will be added to the namesake dataset. For raster data, the overlap region will be updated for the two datasets with the same name.


### Related topics

![](img/smalltitle.png) [AutoCAD data](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis data](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS data](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo data](ImportMapInfo.html)

![](img/smalltitle.png) [Spreadsheet](ImportTable.html)

![](img/smalltitle.png) [Image/raster data](ImportIMG.html)

![](img/smalltitle.png) [3D model data](ImportModel.html)

![](img/smalltitle.png) [Lidar data](ImportLidar.html)

![](img/smalltitle.png) [Google data](ImportKML.html)

![](img/smalltitle.png) [Vector file](ImportVectorFiles.html)

![](img/smalltitle.png) [Import folder](ImportFolder.html)




