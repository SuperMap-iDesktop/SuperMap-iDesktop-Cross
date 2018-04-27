---
title: 导入文件夹
---

### 使用说明

　　导入文件夹功能可将指定文件夹中的文件批量导入，可通过添加文件夹方式导入的数据有：电信Vector Line、电信Building Vector Region、File GeoDataBase Vector、GJB、SimpleJson。

 文件夹数据类型           | 文件说明      | 导入数据集类型                
 :-------------- | :--------------- 
 电信Vector Line | 信行业数据矢量线数据。 | 矢量线数据集
 电信 Building Vector | 电信行业数据矢量面数据。  | 矢量面数据集
 File GeoDatabase Vector  |  ESRI Geodatabase 矢量数据文件，可导入为点、线、面、文本以及纯属性表数据集，导入结果为单个矢量数据集。| 简单数据集
 GJB | GJB 文件是军用数字地图的矢量数据模型文件，通过 ASCII 明码方式保存。  | 简单数据集
 SimpleJson | SimpleJson(\*.json) 是SuperMap 软件支持的特有数据格式，跟GeoJson相类似,是一种键值对应的数据类型，SimpleJson 数据包含两个文件：(\*.json) 和 (\*.meta)。(\*.meta )文件中存储数据的元数据信息,(\*.json)文件中存储数据中成员信息。 | 点、线、面、CAD

### 操作说明

　　1. 在数据导入对话框中，单击工具栏中的![](img/AddFolder.png)按钮，在下拉选项中选择待导入的数据类型。
　　2. 导入矢量文件对话框中的结果设置和源文件信息可参考[通用参数](GeneraParameters.html)页面。
　　2. SimpleJson文件有两种导入方式，一种是导入单个\*.json文件，一种是导入指定文件夹中的所有\*.json文件，单个文件导入的操作可参见[导入矢量文件](ImportVectorFiles.html)页面。


### 注意




### 相关主题

![](img/smalltitle.png) [关于缓冲区分析](BufferTheory.html)

![](img/smalltitle.png) [缓冲区分析应用实例](BufferAnalyst_Example.html)


