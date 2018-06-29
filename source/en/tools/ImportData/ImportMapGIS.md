---
title: Import MapGIS Data
---

### Instructions

　　In MapGIS file system, the project files (\*.MPJ) consist of the point file(\*.WT), line file(\*.WL), region file(\*.WP), network file(\*.WN).  Before performing the import function, MapGIS drawing file format must be converted to MapGIS file exchange format. The code files are: point file(*.wat), line file(*.wal), region file(*.wap), network file(*.wan).

　　SuperMap iDesktop Cross supports importing MapGIS one-dimensional data that is the four code files mentioned above. Besides, you need to convert the color index file to the file in \*.wat format.

**Note**: The *.wat file's format is the same with point exchange format, but they represent different meanings.

　　Code files supported by SuperMap including:

  - Point file(*.wat): WMAP6022, WMAP7022,  WMAP8022, WMAP9022
  - Line file (*.wal): WMAP6021, WMAP7021,  WMAP8021, WMAP9021
  - Region file (*.wap): WMAP6023, WMAP7023,  WMAP8023, WMAP9023
  - Network file (*.wan): WMAP6024, WMAP7024,  WMAP8024, WMAP9024


### Basic steps

　　1.MapGIS files can be imported into SuperMap resulting in simple data or complex data.

　　2.On the parameter settings in the "Data Import" dialog box, please refer to [Common Parameters](GeneraParameters.html)

　　3.Color Index File: Set the path of color index file when importing data. The default is MapGISColor.wat.




### Related topics

![](img/smalltitle.png) [AutoCAD data](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis data](ImportArcGIS.html)

![](img/smalltitle.png) [MapInfo data](ImportMapInfo.html)

![](img/smalltitle.png) [Spreadsheet](ImportTable.html)

![](img/smalltitle.png) [Image/raster data](ImportIMG.html)

![](img/smalltitle.png) [3D model data](ImportModel.html)

![](img/smalltitle.png) [Lidar data](ImportLidar.html)

![](img/smalltitle.png) [Google data](ImportKML.html)

![](img/smalltitle.png) [Vector file](ImportVectorFiles.html)

![](img/smalltitle.png) [Import folder](ImportFolder.html)


