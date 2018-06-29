---
title: Import 3D Model File
---

### Instructions

　　3D model files in various formats are supported by SuperMap iDesktop Cross like: \*.dxf, \*.x, \*.3ds, \*.osgb, etc..

 Data format           | Description             |  Result dataset type          
 :-------------- | :--------------- | :---------------
 \*.3ds | 3DS 3D model data. | Model dataset  
 \*.dxf | DXF 3D model file. | CAD, Simple dataset 
 \*.x | DirectX 3D model file. | Model dataset  
 \*.osgb | OSGB model file| Model dataset 

### Basic steps

　　1.On the parameter settings of Result Settings and Source File Info in the "Import Data" dialog box, please refer to [Common Parameter](GeneraParameters.html).

　　2.Description of conversion parameters are:

 - **Rotation Type**: Only when the imported data is in \*.obj format, the drop-down button is available. Three rotation ways are provided including: Non-rotation, Rotate 90 degrees clockwise along X axis, Rotate 90 degrees anticlockwise along Y axis.
 - **Decompose to multiple model objects**: If you check it, models to be imported will be divided into multiple objects with a field named ModelName will be added to record objects' name.
 - **Coordinate System Settings**: Set a projection for the result dataset. Click "Settings..." to open the "Coordinate System Settings" dialog box where you can specify a projection. For more information about the settings of projection, please refer to [Projection Settings](docs/DataProcess/Projection/SetPrjCoordSys.html).
 - **Import Projection File**: Select the "Import Projection File" radio button, and then click Folder image button to open the "Import Projection File" dialog box where you can select the projection file. The supportive files contain: *.shp, *.prj, *.mif, *.tab, *.tif, *.img, *.sit, *.xml. 
 - **Model Position**: The position of the model after importing, represented by a 3D point object. The default point is (0,0,0).

　　**Note**: The coordinate system of the imported model dataset is the same with the datasource's by default. If the dataset uses geographical coordinate system, the position will be set to the latitude and longitude. If the dataset is set to the plane or the projection coordinate system, the position should be set to X, Z and Y coordinates.

### Note

　　It is necessary to set up the coordinates of relative points according to the datasource's coordinate system when setting model position points. Otherwise, the data will be displayed in the scene in a wrong way. For example, the model is imported into a datasource with the plane coordinate system, but the coordinates of the model position are set to the latitude and longitude, and it will not be displayed when loading the model data to a scene. 

### Related topics

![](img/smalltitle.png) [AutoCAD data](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis data](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS data](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo data](ImportMapInfo.html)

![](img/smalltitle.png) [Spreadsheet](ImportTable.html)

![](img/smalltitle.png) [Image/raster data](ImportIMG.html)

![](img/smalltitle.png) [Lidar data](ImportLidar.html)

![](img/smalltitle.png) [Google data](ImportKML.html)

![](img/smalltitle.png) [Vector file](ImportVectorFiles.html)

![](img/smalltitle.png) [Import folder](ImportFolder.html)




