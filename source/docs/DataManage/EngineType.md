---
title: 支持的数据引擎类型
---
    
  
空间数据库引擎是在常规数据库管理系统之上的，除具备常规数据库管理系统所必备的功能之外，还提供特定的针对空间数据的存储和管理能力。SuperMap SDX+ 是 SuperMap 的空间数据库技术，也是 SuperMap GIS 软件数据模型的重要组成部分，各种空间几何对象和影像数据都可以通过 SDX+ 引擎，存放到关系型数据库中，形成空间数据和属性数据一体化的空间数据库。对不同类型的空间数据源，需要不同的空间数据库引擎来存储和管理，如对 Oracle 数据源，需要 SDX+ for Oracle，其为 Oracle 引擎类型。  
  
**支持数据源引擎类型**  
  
数据集类型|描述 
-|-
UDB|跨平台文件引擎类型，针对 UDB 数据源。    
OraclePlus|OraclePlus 引擎类型。针对 Oracle 数据源。 
Oracle Spatial|Oracle Spatial 引擎类型。针对 Oracle Spatial 数据源。  
SQL|SQL Server 引擎类型。针对 SQL Server 数据源。 
PGSQL|PostgreSQL 引擎类型。针对 PostgreSQL 数据源。
MySQL|MySQL 引擎类型。目前仅在Window 64位提供该引擎类型，不支持数据集集合。  
DB2|DB2 引擎类型。针对 IBM DB2 数据库的 SDX+ 数据源。
OGC|OGC 引擎类型，针对 Web 数据源。目前支持的类型有 WMS，WFS，WCS，WMTS。该引擎为只读引擎，且不能创建。
Kingbase|Kingbase 引擎类型，针对 Kingbase 数据源，不支持多波段数据。  
ImagePlugins|影像只读引擎类型，针对通用影像格式如 BMP，JPG，TIFF，以及超图自定义的影像格式 SIT 等。
GoogleMaps|GoogleMaps 引擎类型。针对 GoogleMaps 数据源。该引擎为只读引擎，且不能创建。
SuperMapCloud|SuperMap 云服务引擎类型。该引擎为只读引擎，且不能创建。  
iServerRest|iServer Rest 服务引擎类型。该引擎为只读引擎，且不能创建。
OpenStreetMaps|又称 OSM 引擎类型。该引擎为只读引擎，且不能创建。


