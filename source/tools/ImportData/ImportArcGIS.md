---
title: 导入ArcGIS数据
---

### 使用说明

　　SuperMap iDesktop Cross 支持导入多种 ArcGIS 数据格式，例如：\*.shp、\*.grd、\*.txt、\*.e00、\*.dem、\*.dbf。

  - *.shp：ArcView Shape（简称 Shp）是 ArcView GIS 软件特有的数据格式，用于存储空间数据和属性数据，是常用的一种矢量数据格式。
  - \*.grd/*.txt：网格数据，是 ArcInfo Grid 的一种栅格数据存储格式，分块存储着像元的空间位置和像元值信息。
  - \*.e00：是 ESRI ArcInfo 的通用交换格式文件。通过 ASCII 明码方式，E00 包含了几乎所有的矢量格式以及属性信息。
  - \*.dem：分为 USGDEM 和 GBDEM 两种栅格文件，是空间数据的交换格式，导入为栅格数据。
  - \*.dbf：ShapeFile数据的表数据。


### 操作说明

1. 添加ArcGIS数据：不同的功能入口，添加待导入数据的方式不同，可参考[通用参数](GeneraParameters.html)页面。
2. 执行导入ArcGIS数据时，大部分参数可参考[通用数据](GeneraParameters.html)页面，不同格式的ArcGIS数据需设置的个别参数不同，具体说明如下：
  - 忽略属性数据：导入SHP和E00数据时支持选择是否忽略属性数据。
  - 创建金字塔：导入GRD、TXT、DEM、ASC数据时，支持设置是否创建金字塔。

### 相关主题

![](img/smalltitle.png) [AutoCAD数据](ImportAutoCAD.html)

![](img/smalltitle.png) [MapGIS数据](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo数据](ImportMapInfo.html)

![](img/smalltitle.png) [电子表格](ImportTable.html)

![](img/smalltitle.png) [影像栅格数据](ImportIMG.html)

![](img/smalltitle.png) [三维模型数据](ImportModel.html)

![](img/smalltitle.png) [Lidar数据](ImportLidar.html)

![](img/smalltitle.png) [Google数据](ImportKML.html)

![](img/smalltitle.png) [矢量文件](ImportVectorFiles.html)

![](img/smalltitle.png) [导入文件夹](ImportFolder.html)



