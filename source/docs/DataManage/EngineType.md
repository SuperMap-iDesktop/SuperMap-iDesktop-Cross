---
title: 数据源类型及数据引擎
---

　　数据源（Datasource）是存储空间数据的场所。所有的空间数据都是存储于数据源而不是工作空间，任何对空间数据的操作都需要打开或获取数据源，用户可以按照数据的用途，将不同的空间数据保存于数据源中，对这些数据统一进行管理和操作。 对不同类型的数据源，需要不同的空间数据引擎来存储和管理。　　
　　  
　　SuperMap SDX+ 是 SuperMap 的空间引擎技术，它提供了一种通用的访问机制（或模式）来访问存储在不同引擎里的数据。 各种空间几何对象和影像数据都可以通过 SDX+ 引擎，存放到关系型数据库中，形成空间数据和属性数据一体化的空间数据库。  
　　不同类型的空间数据源对应不同的数据引擎。SuperMap 产品中提供了多种数据源类型，分为文件型数据源、数据库型数据源、 Web 数据源以及内存数据源。因此，对应的引擎类型有文件引擎、数据库引擎和 Web 引擎。　      
   
### 文件型数据源  
将空间数据和属性数据直接存储到文件中。存储扩展名为 *.udb 的文件。在小数据量情况下使用文件型数据源地图的显示更快，且数据迁移方便。 
   
  
**文件引擎**：包含有三类：SuperMap 自定义的 UDB 引擎（可读写）、影像插件引擎（直接访问一些影像数据）和矢量文件引擎（直接访问外部矢量文件）。 
  
数据源类型|描述 
-|-
UDB|跨平台文件引擎类型，针对 UDB 数据源。   
ImagePlugins|影像只读引擎类型，针对通用影像格式如 BMP，JPG，TIFF，以及超图自定义的影像格式 SIT 等。  
矢量文件| 矢量文件引擎类，针对通用矢量格式如 shp，tab，Acad等,支持矢量文件的编辑和保存。
  
  
### 数据库型数据源  
  
将数据源存储在数据库中，目前桌面产品提供 OraclePlus、Oracle Spatial、SQLPlus、PostgreSQL、DB2、KingBase、MySQL、BeyonDB、HighGoDB、KDB 和 MongoDB 十余种数据库型数据源功能。一般常用于大数据量的数据存储，便于大数据量的管理和访问，且支持并发操作便于修改和数据同步。用户在于访问数据库需要本地配置相关的数据库环境和客户端。
   
**数据库引擎**：SuperMap 空间数据库以大型关系型数据库为存储容器，通过SuperMap SDX+进行管理和操作，将空间数据和属性数据一体化存储到大型关系型数据库中，如Oracle、SQL Server、Sybase 和 DM3 等。如下表所示的空间数据库类型：
      
数据源类型|描述 
-|-
SQLPlus|SQL Server 引擎类型，针对 SQL Server 数据源。  
SQLSpatial|SQLSpatial 引擎类型。  
OraclePlus|OraclePlus 引擎类型，针对 Oracle 数据源，必须安装客户端。 
Oracle Spatial|Oracle Spatial 引擎类型，针对 Oracle Spatial 数据源，必须安装客户端。  
PostgreSQL|PostgreSQL 引擎类型，针对 PostgreSQL 数据源。
MySQL|MySQL 引擎类型，目前仅在Window 64位提供该引擎类型，不支持数据集集合。  
DB2|DB2 引擎类型，针对 IBM DB2 数据库的 SDX+ 数据源，必须安装客户端。
Kingbase|Kingbase 引擎类型，针对 Kingbase 数据源，不支持多波段数据。该引擎目前仅供项目使用。   
BeyonDB| BeyonDB 引擎类型。
PGGIS|PostgreSQL的空间数据扩展PostGIS 引擎类型。
KDB|浪潮KDB 引擎类型。
DMPlus|第三代DM 引擎类型，必须安装客户端。
HighGoDB|HighGoDB 引擎类型。  
GBase|GBase 引擎类型。  
MongoDB|MongoDB 引擎类型。   
   
### Web 数据源   

将数据源存储在网络服务器中，OGC、GoogleMaps、超图云服务、REST 地图服务和天地图地图服务数据源属于 Web 数据源。  
 
**Web 引擎**  
  
Web引擎可以直接访问WFS、WMS、WCS等所提供的Web服务，这类引擎就是把网络上符合OGC标准的Web服务器，作为 SuperMap 的数据源来处理，通过它可以把网络发布的地图和数据与 SuperMap 的地图和数据完全结合，将WFS和WMS的应用融入到 SuperMap 的技术体系，拓展了 SuperMap 数据引擎的应用领域。Web引擎为只读引擎。

数据集类型|描述 
-|-  
OGC|OGC 引擎类型，针对于 Web 数据源，目前支持的类型有 WMS，WFS，WCS 和 WMTS。
GoogleMaps|GoogleMaps 引擎类型。针对谷歌电子地图服务。
SuperMapCloud|SuperMap 云服务引擎类型，针对超图发布的云服务。  
iServerRest|iServer Rest 服务引擎类型，基于 REST 协议发布的地图服务。
OpenStreetMaps|又称 OSM 引擎类型，是由网络大众共同打造的免费开源的地图服务。  
MapWorld| 天地图服务引擎类型，针对天地图发布的地图服务。 
   
 
### 内存数据源  
  
数据源中的数据都保存在内存中，为临时数据源，不支持保存。一些分析的中间结果可以存储在该数据源中，有利于提高分析的效率，当得到最终数据时可从内存数据源导出为本地数据。   
内存数据源对应的数据引擎为内存引擎。 
  