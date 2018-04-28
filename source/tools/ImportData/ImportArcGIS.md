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

　　执行导入ArcGIS数据时，大部分参数可参考[通用数据](GeneraParameters.html)页面，不同格式的ArcGIS数据需设置的个别参数不同，具体说明如下：

 - \*.shp数据
    - 导入空数据：勾选该复选框，若导入数据集为空数据集时，默认导入一个无对象的空数据集。否则，程序会提示导入失败。
 - \*.e00、\*.shp数据
    - 忽略属性信息：勾选该参数，表示导入空间数据的同时，不导入属性信息，只导入数据的空间信息。
 - \*.grd、\*.txt、\*.dem 数据
    - 建立影像金字塔：勾选该复选框，在导入影像数据时，将对导入数据创建影像金字塔。

### 相关主题

![](img/smalltitle.png) [关于缓冲区分析](BufferTheory.html)

![](img/smalltitle.png) [缓冲区分析应用实例](BufferAnalyst_Example.html)


