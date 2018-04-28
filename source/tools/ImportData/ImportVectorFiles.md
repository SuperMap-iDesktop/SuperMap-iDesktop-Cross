---
title: 导入矢量文件
---

### 使用说明

　　SuperMap iDesktop Cross 支持导入多种矢量数据，此处的矢量文件包括\*.vct、\*.json、\*.gpx三种。

 矢量文件           | 文件说明      | 导入数据集类型                
 :-------------- | :--------------- 
 VCT | VCT是中国标准矢量交换格式，广泛应用于国土资源部门土地利用现状调查成果汇交、土地利用规划成果汇交和地籍调查数据库成果交换。关于 VCT 参数的具体描述，请参见 [VCT 文件](VCTConfig.html)页面。 | 矢量数据集
 SimpleJson | SimpleJson(\*.json) 是SuperMap 软件支持的特有数据格式，跟GeoJson,相类似,是一种键值对应的数据类型，支持的几何类型包含点、线、面等类型，SimpleJson里的特征包含几何对象和其属性信息。SimpleJson 数据包含两个文件：(\*.json) 和 (\*.meta)。(\*.meta )文件中存储数据的元数据信息，其中包含该文件的几何类型、投影信息以及所包含的字段信息；(\*.json)文件中存储数据中成员信息。 | 点、线、面、CAD
 GeoJson文件 | GeoJson 是一种对各种地理数据结构进行编码的格式，基于JavaScript对象表示的地理空间信息数据交换格式。  | 点、线、面、CAD
 GPS |  \*.gpx 文件是GPS设备记录下来的GPS轨迹点文件，是一种轻量级的XML数据交换格式。| 点数据集


### 操作说明

　　1. 导入矢量文件对话框中的结果设置和源文件信息可参考[通用参数](GeneraParameters.html)页面。
　　2. SimpleJson文件有两种导入方式，一种是导入单个\*.json文件，一种是 导入指定文件夹中的所有\*.json文件，具体操作可参见[导入文件夹](ImportFolder.html)页面。


### 注意

　　对于一个单文件的SimpleJson(*.json) ，同时包含一个(*.json) 和一个 (*.meta)。而一个SimpleJson文件夹数据，是一个文件夹对应一个(*.meta)文件。在选择文件导入时，程序默认选择的是(*.json)文件和文件夹来打开数据。



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

![](img/smalltitle.png) [导入文件夹](ImportFolder.html)




