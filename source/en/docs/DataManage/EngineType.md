---
title: Datasource types and data engines
---

　　Spatial data is saved in a datasource but a workspace, hence, to operate on data, you must open or acquire a datasource. As your needs, you can save different spatial data into a datasource to manage and operate uniformly. For different types of datasources, different spatial data engines are required.
　　  
　　SuperMap SDX+ is the spatial database engine of SuperMap. All kinds of spatial geometric objects and images can be stored in relational database via SDX+ engine to become a spatial database which can implement incorporate storage of spatial data and attribute data.

   
### File datasources  
Save spatial data and property data into a file that extension is *.udb. For a small amount of data, a file datasource is more better in displaying and transmitting data.
   
  
**File engine**: UDB engine (can read and write) customized by SuperMap, image plugin engine（to access image data）and vector file engine (to access external vector files). 
  
Datasource type|Description 
-|-
UDB|cross-platform file engine, focus on UDB datasources.
ImagePlugins|read-only image engine, focus on common image formats, such as BMP, JPG, TIFF, SIT(customized by SuperMap), etc..
Vector file| Vector file engine (can edit and save), focus on common vector formats, such as: shp, tab, Acad, etc..
  
  
### Database datasources  
  
Save a datasource into a database, ten more databases are supported including: OraclePlus, Oracle Spatial, SQLPlus, PostgreSQL, DB2, KingBase, MySQL, BeyonDB, HighGoDB, KDB and MongoDB. Database datasources are generally used for saving a big amount of data to facilitate data management and access. The support of concurrent operations is convenient for modifying and syncing data. To access a database, you must install a corresponding client and configure environment.
   
**Database engine**: SuperMap spatial database takes large-scale relational databases (Oracle, SQL Server, Sybase and DM3) as storage containers to saving integrative spatial data and property data.
      
Datasource type|Description 
-|-
SQLPlus|SQL Server engine, focus on SQL Server datasources.
SQLSpatial|SQLSpatial engine. 
OraclePlus|OraclePlus engine, focus on Oracle datasources, and the installation of a client is necessary. 
Oracle Spatial|Oracle Spatial engine, focus on Oracle Spatial datasources, a client is necessary.  
PostgreSQL|PostgreSQL engine, focus on PostgreSQL datasources.
MySQL|MySQL engine provided only for Window 64, dataset collections are not supported. 
DB2|DB2 engine, focus on SDX+ datasources in IBM DB2 database and the installation of client is necessary.
Kingbase|Kingbase engine, focus on Kingbase datasources with no support to multi-band data. It only is used for projects.  
BeyonDB| BeyonDB engine.
PGGIS|PostGIS engine extended from PostgreSQL spatial data.
KDB|Inspur KDB engine.
DMPlus|The third generation DM engine, the installation of a client is necessary.
HighGoDB|HighGoDB engine.
GBase|GBase engine.  
MongoDB|MongoDB engine. 
   
### Web datasources   

Save datasources into web servers, such as OGC, GoogleMaps, SuperMap Cloud, REST map service and MapWorld.
 
**Web engine**  

Wed engine can directly access Web services served by WFS, WMS, WCS. This type of engine take Web servers meeting OGC standard as SuperMap datasources. With it, maps and data published by network can be connected with them from SuperMap perfectly, and applications of WFS and WMS can be integrated into the technical architecture of SuperMap. Please have a notice that web engine is read-only.

Datasource type|Description 
-|-  
OGC|OGC engine. Focus on Web datasources, currently supportive types: WMS, WFS, WCS and WMTS.
GoogleMaps|GoogleMaps engine. Focus on Google digital map services.
SuperMapCloud|SuperMap Cloud engine. Focus on cloud services published by SuperMap.  
iServerRest|iServer Rest service engine. Focus on map services published based on REST protocol.
OpenStreetMaps|Also called OSM engine. Free and open-source map services.  
MapWorld| MapWorld engines. Focus on map services published by MapWorld.
   
 
### Memory datasources  
  
All data in datasources is temporarily saved into memory and can't be saved permanently. You can save some middle result data to improve analysis efficiency. Memory data can be exported as local data.
The engine corresponding with memory data is memory engine.
  