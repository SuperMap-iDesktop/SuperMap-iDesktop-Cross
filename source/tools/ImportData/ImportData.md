---
title: 数据导入
---

### 使用说明

　　我们在进行 GIS 处理时，除了使用 SuperMap 自己的数据格式外，还需要对多种外部的数据格式进行操作。数据转换功能提供了不同数据格式之间的互转，很好地支持了多种数据格式。

　　数据导入工具箱提供了应用程序支持的所有格式数据的导入功能，支持导入的数据格式有40多种，支持导入的数据类型丰富，便于用户将其他软件的GIS或非GIS数据导入到桌面中应用。支持的数据类型有：[AutoCAD数据](ImportAutoCAD.html)、[ArcGis数据](ImportArcGIS.html)、[MapGIS数据](ImportMapGIS.html)、[MapInfo数据](ImportMapInfo.html)、[电子表格](ImportTable.html)、[影像栅格数据](ImportIMG.html)、[三维模型数据](ImportModel.html)、[Lidar数据](ImportLidar.html)、[Google数据](ImportKML.html)、[矢量文件](ImportVectorFiles.html)、[GeoJson](ImportFolder.html)、[SimpleJson](ImportFolder.html)、[GJB](ImportFolder.html)、[电信数据](ImportFolder.html)等。
 运算符           | 含义             |  示例           
 :-------------- | :--------------- | :---------------
 ＋ | 加法 | RENT + UTILITIES &lt;= 800

### 备注

 1. 每个导入项的默认目标数据源为工作空间管理器中选中的数据源集合的第一个非只读数据源；若用户没有选中工作空间管理器中的任何数据源，则每个导入项的默认目标数据源为数据源集合中的第一个非只读数据源。 
 2. 若当前工作空间管理器中的所有数据源都是只读的，则导入功能区显示为灰色，为不可用状态。 
 3. 若用户在列表框中同时选中了多个栅格文件或矢量文件，则“数据导入”区域的文件参数设置区域按照最后选中的数据格式显示参数设置项。 
 4. 若用户在列表框中同时选中了栅格数据和矢量数据，则“数据导入”区域的文件参数设置区域仅显示公共参数。 
 5. 数据导入后，数据集的坐标系默认与所在数据源坐标系一致。 
 6. 关于导入模式的说明：在导入矢量和栅格数据的时候导入模式结果稍有不同。

   - 强制覆盖模式：两者都是将原有的同名数据集删除，替换为新导入的数据。 
   - 追加模式：对矢量数据集而言，追加是直接将要导入的数据添加到已存在的同名数据集中；对栅格或者影像数据集，追加实际上是进行两个同名数据的重合区域的更新。关于数据集的追加的详细说明，请参见数据集追加。 

　　特别强调，追加模式和强制覆盖模式在存在同名数据集的情况下使用。在实际的操作过程中，请区别使用。 


### 相关主题

![](img/smalltitle.png) [AutoCAD数据](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis数据](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS数据](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo数据](ImportMapInfo.html)

![](img/smalltitle.png) [电子表格](ImportTable.html)

![](img/smalltitle.png) [影像栅格数据](ImportIMG.html)

![](img/smalltitle.png) [三维模型数据](ImportModel.html)

![](img/smalltitle.png) [Lidar数据](ImportLidar.html)

![](img/smalltitle.png) [Google数据](ImportKML.html)

![](img/smalltitle.png) [矢量文件](ImportVectorFiles.html)

![](img/smalltitle.png) [导入文件夹](ImportFolder.html)




