---
title: Supported Data Formats 
---

　　We may deal with SuperMap formats and external data formats during GIS processing. The conversion function provides the converting ability among different file formats.

　　Following table lists the supported files formats for importing data.


### ![](../img/read.gif)Format of supported vector file 

 File  |Format     |Imported Dataset Type 
 :----------- | :--------- | :------------
 AutoCAD File | \*.dwg, \*.dxf      | CAD, simple dataset 
 ArcGis Data   | \*.e00 , \*.shp, \*.gdb, \*.txt     | simple dataset 
 MapInfo Data | \*.tab, \*.mif, \*.wor | CAD, simple dataset 
 MapGIS Data | \*.wat, \*.wal, \*.wap, \*.wan | CAD, simple dataset 
 Microsoft File| \*.csv | attribute table dataset
 Image Bitmap | \*.sit, \*.img, \*.tif, \*.tiff, \*.bmp, \*.png, \*.gif, \*.jpg, \*.jpeg | image, raster dataset 
 Raster Data | \*.dem, \*.bil, \*.raw, \*.bsq, \*.bip, \*.sid, \*.b | raster dataset
 Google KML File | \*.kml, \*.kmz | CAD, simple dataset    
 Lidar Data | \*.txt | 2D, 3D dataset 
 Chinese standard vector format | \*.vct | vector dataset 
 dBasseFoxPro database file | \*.dbf | attribute table dataset 
 3D model data | \*.scv, \*.osgb , \*.3ds, \*.x, \*.dxf | CAD, model dataset 


### ![](../img/read.gif)Instruction about importing data
　　There are two function entrances for importing data, one is in the drop-down list "Dataset" of the menu bar; for another one, you can right-click mouse in the datasource node of workspace manager, and then click on "Import Dataset...". In the pop-up dialog box "Import Data", adding the file that you want to import, and setting related parameters. Parameters set for different data could not be the same. The common parameters are described below:

* **Target Datasource**: Used to set datasource where the imported dataset will be saved in.
* **Result Dataset**: Used to set name for the imported dataset, the name is the same with source dataset name by default. 
* **Encode Type**: Specifies whether to compress data when saving it, which can save disk space. For different datasets, the system will determine appropriate encode mode automatically and display it in drop-down menu.
* **Import Mode**: The import modes are: None, Force to Cover, Append. If selecting Force to Cover, the existed dataset which has the same name with imported data will be covered, however, for the function Append, imported data will be appended to the existed data.
* **Source File Path**: Displays original data path which can be copied.

