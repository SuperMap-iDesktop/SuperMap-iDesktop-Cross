---
title: 导入MapInfo数据
---

### 使用说明

　　MapGIS明码数据格式，是ASCII码的明码文件，其文件结构由文件头和数据区两部分组成。在MAPGIS文件系统中，其工程文件（后缀名为\*.MPJ）一般包括点文件（\*.WT）、线文件（\*.WL）、面文件（\*.WP）、网络文件（\*.WN）。在执行导入功能之前，必须将MapGIS图形文件格式转换为MAPGIS交换文件格式，即MapGIS明码格式，之后再进行导入。上述四种文件格式，转化为明码格式后，文件名分别为：点明码文件（*.wat）、线明码文件（*.wal）、区明码文件（*.wap）、网络明码文件（*.wan）。

　　SuperMap iDesktop Cross 支持导入MapGIS一维数据，即上述的四种明码文件格式。另外，用户需要在MapGIS平台下将颜色索引表文件转换成明码格式（后缀名为\*.wat）。**注意**:此*.wat文件与点明码文件格式相同，但是表达的含义并不相同。

　　SuperMap支持对MapGIS 6系列产品的明码文件，其中包括：

  - 点文件（*.wat）数据版本号：WMAP6022、WMAP7022、 WMAP8022 、WMAP9022；
  - 线文件（*.wal）数据版本号：WMAP6021、WMAP7021、 WMAP8021 、WMAP9021；
  - 区文件（*.wap）数据版本号：WMAP6023、WMAP7023、 WMAP8023 、WMAP9023；
  - 网络文件（*.wan）数据版本号：WMAP6024、WMAP7024、 WMAP8024 、WMAP9024。


### 操作说明

　　1.MapGIS文件支持导入为简单数据或复合数据；

　　2.导入界面中的参数设置可参考[通用参数](GeneraParameters.html)页面

　　3.颜色索引文件：支持设置MAPGIS导入数据时的颜色索引表文件路径，默认为应用程序自带的MapGISColor.wat。




### 相关主题

![](img/smalltitle.png) [关于缓冲区分析](BufferTheory.html)

![](img/smalltitle.png) [缓冲区分析应用实例](BufferAnalyst_Example.html)


