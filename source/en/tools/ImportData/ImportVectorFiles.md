---
title: Import Vector Files
---

### Instructions

　　Varieties of vector files can be imported into SuperMap iDesktop Cross like \*.vct, \*.json, \*.gpx.

 Vector files           | Description      | Import dataset types                
 :-------------- | :--------------- 
 VCT | VCT is the Chinese standard vector exchange format. | Vector dataset
 SimpleJson | SimpleJson(*.json) is a special data format for SuperMap. SimpleJson is a type of key-value that is similar to GeoJson. The supported geometric types includes points, lines, regions. SimpleJson consists of two files (*.json) and (*.meta), among them meta data information is saved in the (*.meta) file such as, geometric types, projection information and field information, and member information in data is saved into the (*.json) file.| Point, line,region, CAD
 GeoJson files | GeoJson is a format which encodes all kinds of geographic structures. It is a data exchange format of geographic spatial information based on JavaScript objects. |  Point, line,region, CAD
 GPS |  \*.gpx is one kind of files on GPS tracing points recorded by GPS devices.| Point dataset


### Basic steps

　　1. On the parameter settings of Result Settings and Source File Info in the "Import Data" dialog box, please refer to [Common Parameter](GeneraParameters.html).
　　2.  Two ways to import SimpleJson files, one is importing single \*.json files, and another one is importing all \*.json files in a specified folder. For detail operations, please refer to [Import Folder](ImportFolder.html).


### Note

　　For a SimpleJson(*.json) file, only one (*.json) file and one (*.meta) file are contained. But for a SimpleJson folder data, a folder corresponds to a (*.meta) file. A (*.json) file and a folder are chose by default to open data. 



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

![](img/smalltitle.png) [Import folder](ImportFolder.html)




