---
title: Manage Datasources
---

### Open Datasources  

Three ways are provided for opening a datasource: 
  
+   Click Cross icon and select "Open", then you can open a file datasource,a database datasource or a web datasource. 
+   In the "Start" tab, on the "Datasource" group, you can click the "file", "Database" or "Web" to open different kind of datasource.
+    Right-click the "Datasource" node in your workspace and then you can select relative buttons to open different datasources.
   
**Open a file datasource**       
1. After any operation mentioned above to open a file datasource, the "Open File Datasource" dialog box appears.
2. Navigate to the datasource you want to open then select it and Click "Open".
  
-  **Note**: The opened datasource could be either a *.udb or a image file or an external vector file.
-  The supportive image formats are *sit, *.bmp, *.jpg, *.jpeg, *.*.png, *.tif, *.tiff, *.img, *.sci, *.gif, *.gci, *.sct, *.xml, *.ecw, *.sid, *.bil, *.jp2, *.j2k, *.egc, etc.. When you open an image data file, a datasource node with the same name with the image data file will be added to the Datasource node in Workspace Manager and the image data file will be added to the newly created datasource as an image dataset.  
-  The supportive formats of external vector files are *.shp, *.sde, *.mif, *.tab, *.dwg, *.dxf, *.dcf, *.dgn, *.kml, *.kmz, *.gml, *.wal, *.wan, *.wap, *.wat, *.csv, *.e00, *.xlsx, etc.. When opening an external vector file, a datasource with the same name with the vector file will be built in your workspace, and the file will be added into the datasource as a CAD dataset.
 

   
**Open a database datasource**       
1. After any operation mentioned above to open a database datasource, the dialog box "Open Database Datasource" will appear.
2. In the dialog box, you can choose a database type from the list on the left, and type the necessary information of the selected datasource, then click OK.  

No.|Database type|Instance name|Server name|Database name|Account|Max pool size|Supported version|Note
-|-|-|-|-|-|-|-|-  
1|OraclePlus|v|x|Optional|v|v|9i/10g/11g/12c|A client is required with correct configurations of environment parameters. You can connect a server via a client or through "easy connect naming method", the format of instance name is host[:port][/service_name]   
2|OracleSpatial|v|x|Optional|v|x|recommended 10g, 11g, 12c|A client is required with correct configurations of environment parameters. You can connect a server via a client or through "easy connect naming method", the format of instance name is host[:port][/service_name]  
3|SQLPlus|x|v|v|v|x|2000/2005/2008/ 2012/2016|A client is required with correct configurations of environment parameters.
4|MySQL|x|v|v|v|x|5.6.16 and above|Your machine must have permission to access the remote server, if not, you can reference the command "grant all privileges on *.* to user@'%' identified by pwd; flush privileges", and replace % with your IP.  
5|PostgreSQL|x|v|v|v|v|9.0 and above|Your machine must have permission to access the remote server, if not, please add "host all all 0.0.0.0/0 trust" into the /data/pg_hba.conf file under the storage path of server data, and then restart the server.  
6|DB2|x|x|v|v|x|9.7 and above|A client is required with correct configurations of environment parameters.  
7|HighGoDB|x|v|v|v|x|Similar to PostgreSQL
8|SinoDB|x|v|v|v|x| |A client is required with correct configurations of environment parameters.    
9|KingBase|x|v|v|v|x|V7|A client is required with correct configurations of environment parameters. 
10|ArcSDE|x|v|v|v|x| |Three dynamic libraries are needed including (sg\sde\pe).  
11|BeyonDB|x|v|v|v|x|2015|A client is required with correct configurations of environment parameters.   
12|KDB|v|x|v|v|x| |A client is required with correct configurations of environment parameters.   
13|MongoDB|x|v|v|v|x|2.4 and above|Supported dataset types include: point, line, region, 3D point, 3D line, 3D region and tabular datasets.  
14|DMPlus|x|Optional|v|v|x|DM7|A client is required with correct configurations of environment parameters. 
 
 
-  **Max Pool Size**: When opening an OraclePlus or PostgreSQL datasource, you can set the max pool size. A pool is used for assigning, managing and releasing connections of databases. Setting max pool size means you can specify how many connections at most can be requested by database, if the real number of requesting is greater than it, the latter requests will be added into a waiting queue. 
-  **Load data link info**: Provides a load and save the database data source link information to facilitate the user to save and load the open database-type data source information. 

**Open a Web datasource**   
      
1. After any operation mentioned above to open a web datasource, the dialog box "Open Web Datasource" will appear.
2. In the dialog box, select the web type you want to open, and then enter requirement information. Following contents detail relative parameters opening a web datasource.
 - **OGC**: Type the service address and service type. SuperMap GIS 9D supports 4 service types: WMS, WFS, WMTS and WCS with the first three types are only-read datasources while the last one can be done a little simple edit. For WMTS server, there is a cache folder generated in local machine after opening it. The path is SuperMap iDesktop 9D\Bin\Cache\WebCache\WMTS\, under the path, to save tile files and request files (*.xml), folders will be built according to addresses of published service.
 - **iServerREST**: You need to enter a service address to open a datasource. 
 - **GoogleMaps**: You don't need to set the default parameters, such as service address, service type and open mode, etc.. 
 -  **SuperMapCloud**: You don't need to set the default parameters, such as service address, service type, username, key and open mode, etc.. 
 -  **OpenStreetMaps**: The settings of the system default parameters are unnecessary, and you can click "Open" directly to open it.

  
### New a datasource
  
Three kinds of datasources can be produced through three ways:
  
+   Click Cross icon > "New" and then you can select one kind of datasources you want to create.	
+   In the "Start" tab, on the "Datasource" group, you can click "File" drop-down button or "Database" drop-down button to select relative button to create datasources you need.
+   Right click "Datasource" in the Workspace Manager, then select relative buttons to create datasources you want.
  
**New a file datasource**  

Based on the operations mentioned above, and then select "New File Datasource..." to open the "New File Datasource" dialog box, and then navigate to related folder where you want to save your datasource and set a name for your datasource (a *.udb file). 
  
**New a database datasource**    
  
Based on the operations mentioned above, then select "New Database Datasource...", and then in the pop-up dialog box, you can choose a database type from the left list and enter necessary information at right. For more details on parameters, please refer to "Open a database datasource".  

**New a memory datasource**  
 Based on the operations mentioned above, then select "New Memory Datasource..." to create a temporary datasource which can not be saved. 
  
**Note**   
The datasource newly created will be opened in the current workspace. 

 ### View properties of your datasource 

Select a datasource then right click, and select "Properties" to open the "Properties" dialog box. Three kinds of property information you can view: "Datasource", "Statistic", "Coordinate System".   

    
+   **Datasource**: Connection path, Engine type, open mode on your datasource are shown, also you can add and modify description of your datasource and change its password.
+   **Statistic**: All statistics of datasets included in your datasource are shown. Such as "Sum" is total number of datasets. Besides, all supportive types of datasets and corresponding number are listed in the table.
+   **Coordinate System**: The coordinate system adopted by your datasource, the geography unit and information on the coordinate system are displayed. You can re-specify a coordinate system or perform projection transformation.


**Note**: The "Properties" window is float, that means it can display not only datasources' properties but also properties of workspaces and datasets. You can select related data to view related property information when the "Properties" window is open. 
  
### Compress datasources    
For file datasources in the UDB format, to reduce data quantity thus occupying less disk space, you can right click your datasource and select "Compress" to compress it. If there is an open dataset in your datasource, you must close it first.

### Rename datasources  
Modify your datasource's name.
1. Select a datasource and then press F2 in your keyboard, or right click and select "Rename".
2. After entering a new name, you can click anywhere to finish the operation.

**Note**: The layer based on the renamed dataset in your map will not be shown normally.
  
### Close datasources
Select the datasource you want to close, then right click and select "Close". 
 
