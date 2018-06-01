---
title: Data Organization Structure in SuperMap
---

　　SuperMap GIS organizes data through workspace, datasources, datasets, maps, scenes and layouts. SuperMap GIS manages data through a hierarchical tree in the Workspace Manager. A workspace consists of the datasource collection, the map collection, the scene collection, the symbol resource collection.

## Workspace

　　A workspace is where you work with your data. A workspace must be created first before any operations. Once you start the application, the system will create an empty workspace and you can perform any operations based on it, also you can open an existed workspace. All results in the work environment will be saved in the opened workspace.



### Workspace types

　　Workspaces in SuperMap fall into two categories: file-based workspace and database-based datasource. A file-based workspace can be saved in *.smwu or *.sxwu. For a database-based workspace, it means the workspace will be saved into a database, (only Oracle and SQL Server are supported, but in Linux OS, only Oracle is supported.)

### Managements of Workspaces

　　All of maps, 3D scenes and symbol resources in a workspace are saved in the workspace, so when you delete it, maps, 3D scenes and symbol resources will be deleted too. However, datasources in the workspace can not be deleted and only the relevant relationships between them are deleted. The managements of workspaces contain: Open, Save as, Save, Close and Delete workspaces.

* **Open**: In the Start tab on the Workspace group, if you want to open a file-based workspace, you can click File and then choose the file you want to open; but if you want to open a database-based workspace, you can click "Database" drop-down button and then select the Database type you want to open, and then in the pop-up dialog box, enter required information to open it.

* **Save/Save as**: Save the work environment and results, meanwhile, you can set whether to save maps or scenes in the workspace. The "Save as" command allows you to save your workspace as a file-based or database-based workspace.

* **Close**: If you want to close your workspace, you can right-click your workspace node and then select "Close Workspace" in the context menu. 

## Datasource

　　Spatial data from SuperMap iDesktop Cross can be saved in files or databases, namely datasources can be saved in files or databases. Datasources fall into 4 categories: file-based datasources, database-based datasources, Web datasources and memory datasources.

### Datasource types

* **File-based datasource**: namely, the UDB datasources. They are stored in *.udb or *.udd files. When creating an UDB datasource, an .udb file and a corresponding .udd file are generated with the same name but the suffix. GIS spatial data contains not only geometric objects (in *.udb files), but also their properties (in *.udd files). The UDB datasource has features of cross platform and accessing big data efficiently. A UDB datasource can save up to 128T data.

* **Database-based datasource**: Save your workspace into a database. Such as Oracle Plus database, SQL Server Plus database and so on. Both sptial geometry information and properties are saved in a database, if you want do some operations on spatial data, you must open datasources first, and all results will be saved in the datasources but workspaces. Since datasources are saved independently, when you delete workspaces, datasources in these workspaces will not be deleted.

* **Web Datasource**: Datasources saved in web servers, such as OGC datasources, iServerRest datasources, SuperMap Clound datasources, GoogleMaps datasources, BaiduMap datasources, MapWorld datasources. You can access datasources through URL.

* **Memory datasource**: The memory datasource belongs to one kind of temporary datasoures, that means you can not save it. But the efficiency of processing data is higher.

### Managemnet of datasources

　　YOu can save different kinds of spatial data into different datasources to manage and process data uniformly. Managements of data provided by SuperMap iDesktop Cross contain: Open, New, View Properties, Rename, Close, etc..

* **New a datasource**: The feature of creating a new datasource is provided both in ribbon and in the context menu of datasource node.

* **Open a datasoure**: The feature of opening a new datasource is provided both in ribbon and in the context menu of datasource node. You are allowed to open file-based datasources, database-based datasources or web datasources.

* **Datasource properties**: You can view your datasource's properties information and projection information in the "Properties" panel through clicking "Properties" in the context menu of you datasource's node.

## Dataset

　　The dataset is used to save geometric spatial objects. The managements of datasets contain New, Copy, Import, Export, Delete, Close, Rename, View properties, Set projections for datasources and so on. Now dataset types supported by SuperMap contain: 2D&3D point, line, region, tabular, network, CAD, text, route, image/raster, mosaic datasets etc..  

* **New a dataset**: In the "New Dataset" dialog box, you are required to set a series of parameters to create a new dataset such as: the target datasource, dataset type, dataset name, encode type, and so on.

* **Copy datasets**: Copy one or more datasets then paste them to target datasources.

* **Import&Export datasets**: Besides data types of SuperMap, external data also can be imported and data in SuperMap can be exported in other formats. 
* **Image Pyramid**: After creating image pyramids for raster or image datasets, the speed of browsing data is improved greatly. For more details, please refer to [Image Pyramid](Pyramid.html).

