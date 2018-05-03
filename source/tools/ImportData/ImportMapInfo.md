---
title: 导入MapInfo数据
---

### 使用说明

　　SuperMap iDesktop Cross 支持导入 MapInfo 的\*.mif、\*.wor、\*.tab三种格式的数据。
  - MIF：是 MapInfo 用来对外交换数据的一种中间交换文件。当用户用 MapInfo 打开一种地图表并以 *.mif 格式转出，MapInfo 会生成两个文件（*.mif 和*.mid）。其中*.mif 文件保存了 MapInfo 的表结构及对象的空间信息，包括了对象的符号样式、填充模式等风格信息。而*.mid 文件则按照对象的记录顺序保存了每个对象的属性信息
  - WOR：是 MapInfo 工作空间文件。工作空间文件用于保存表、窗口和窗口位置、地图、布局、资源符号、MapInfo 环境设置等信息。
  - TAB：是属性数据的表结构文件，以表的形式存储信息，TAB 文件定义了地图属性数据的表结构


### 操作说明

　　1.导入MapInfo文件时，可参考[通用参数](GeneraParameters.html)页面进行参数设置。

　　2.\*.mif和\*.tab数据可导入为简单数据或复合数据。

　　3.导入\*.mif和\*.tab数据，可设置是否忽略属性，若勾选该复选框，则只导入空间信息，而没有属性信息。




### 相关主题

![](img/smalltitle.png) [AutoCAD数据](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis数据](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS数据](ImportMapGIS.html)

![](img/smalltitle.png) [电子表格](ImportTable.html)

![](img/smalltitle.png) [影像栅格数据](ImportIMG.html)

![](img/smalltitle.png) [三维模型数据](ImportModel.html)

![](img/smalltitle.png) [Lidar数据](ImportLidar.html)

![](img/smalltitle.png) [Google数据](ImportKML.html)

![](img/smalltitle.png) [矢量文件](ImportVectorFiles.html)

![](img/smalltitle.png) [导入文件夹](ImportFolder.html)




