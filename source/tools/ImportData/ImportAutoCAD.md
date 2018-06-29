---
title: 导入AutoCAD数据
---

### 使用说明

　　SuperMap iDesktop Cross 支持导入 AutoCAD 的3种格式，DWG、 DXF和DGN。
  - DXF：Drawing Interchange Format（图形交换格式）的缩写形式，是 Autodesk 公司开发的 AutoCAD 与其他软件格式数据交换的文件格式。这是图形文件的 ASCII 或二进制文件格式，用于向其他应用程序输出图形和从其他应用程序输入图形。
  - DWG：是 AutoCAD 的图形文件，专门用于保存矢量图形的标准文件格式。
  - DGN：是一种 CAD 文件格式，为美国 Bentley System 公司的 MicroStation 和 Intergraph 公司的 Interactive Graphics Design System(IGDS) CAD 程序所支持。


### 操作说明

1. 添加CAD数据：不同的功能入口，添加待导入数据的方式不同，可参考[通用参数](GeneraParameters.html)页面。
2. 导入AutoCAD对话框中的结果设置和源文件信息可参考[通用参数](GeneraParameters.html)页面。
3. 设置AutoCAD DXF(*.dxf)文件和 ArtoCAD Drawing(*.dwg) 文件的转换参数，具体说明如下：
  - 曲线拟合精度：用来设置导入数据时的线对象或面对象的边线精度，系统默认为73。 
  - 字体库设置：若导入的源文件有相关的字体文件(*.shx)，需要在此处添加一并导入，避免出现 CAD 数据中的字体缺失、显示不全的情况。 
  - 合并图层：选中此复选框，则在导入 DWG/DXF/DGN 文件时，将所有图层合并为一个图层导入。导入时，默认将多个 CAD 图层合并为一个图层。 
  - 导入符号块：该复选框用于设置是否导入块对象，或将其视为点对象导入。默认导入符号块。 
  - 导入扩展数据：用于在导入 CAD 格式的图形数据时，同时导入 AutoCAD 制图中类似属性表的数据。外部扩展数据导入后格式为一些额外的字段，追加在默认字段后面。 
  - 导入不可见图层：选中此复选框，即可导入 AutoCAD 图形文件中不可见的图层。在 AutoCAD 中，图层不可见比较实用，当存在多个图层时，不同的图层保存不同类型的对象，当不需要显示该类对象时，直接将该层设置为不可见，下次使用时设置为可见。 
  - 导入块属性：勾选该复选框，则在导入时会导入数据的块属性；不勾选该复选框，则导入时会忽略块属性。 
  - 导入扩展记录：勾选该复选框，则会将用户自定义的字段及属性字段作为扩展记录导入；不勾选该复选框则表示不导入用户自定义的字段和属性字段。 
  - 保留对象高度：在导入 CAD 包含三维对象（如三维多段线），选中该项，则导入时会保留三维对象的高度信息（对象的 Z 坐标值）；不勾选该项，则导入后不保留高度信息。成功导入 DWG/DXF 后，在生成的数据集的属性表中会生成 Elevation 字段，存储对象的高度信息。 
  - 保持参数化对象：在导入 CAD 包含参数化对象，选中该项，导入后生成的 CAD 数据集中包含带有参数化对象的复合面或者复合线对象；不勾选该项，则导入后不包含参数化对象。 
  - 保留多义线宽度：选中此复选框，即可保留 AutoCAD 图形文件中的多义线（Polyline）的宽度。在 CAD 里面，多义线是由一系列直线和弧线组的多段线，各段线可以单独定义线宽。默认保留原始定义的多义线宽度。 
  - 设置缩放因子：当导入的数据为*.dwg数据时，支持设置数据的 X、Y、Z 方向的缩放比例值。 

4. 设置导入 AutoCAD DGN 数据的转换参数：

  - 单元对象导入为点对象：默认不勾选，即将原有的单元对象以除 cell header 外的所有要素对象的形式导入；若勾选则在单元对象的位置用点对象代替。


### 备注

　　若导入的数据中包含参数化对象，导入时勾选“保持参数化对象”复选框，根据所选择的数据集类型不同，分为以下两种情况： 

- 复合数据集：若导入时选择数据集类型为“复合数据集”，导入后生成的 CAD 数据集中包含带有参数化对象的复合面或者复合线对象。 
- 简单数据集：若导入时选择数据集类型为“简单数据集”，导入后会单独生成复合面或者复合线数据集用于存储参数化对象。

### 相关主题

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




