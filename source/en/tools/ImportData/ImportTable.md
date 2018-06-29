---
title: Import Spreadsheet
---

### Instructions

　　In data acquisition, the spatial information of geographic data is often stored in Excel or csv files to improve the utilization of data.

　　About Excel: 

  - The versions of Microsoft Excel files which can be imported into iDesktop must be Office 2007 and above that is files in *.xlsx format.
  - The first record in a Excel file is fields' names.
  - Import result dataset name, normally is the Excel file name plus “_” plus the sheet name. 
  - It will import all sheets with data by default but the empty ones. 
  - When there are merged cells, only the first cell in the merged cell area has data. 

　　About CSV: 

  - The head of the file can't be empty, and every record is a line. 
  - The text can only contain separators and field values. 
  - By default, comma is used as the separator, but customized separator is also allowed by SuperMap iDesktop 9D . 
  - The first record can be the field name. 


### Basic steps

　　On the settings of common parameters, please refer to [Common Parameters](GeneraParameters.html).

　　1. Separator: Specify the separator in your CSV file. "," is by default. Also you can select ".", "Tab", or a space. Also you are allowed to customize a readable character.

　　2. First Row as Field Info: Check it to import the first row in your CSV file as the field name otherwise the first row will be the attribute information. If the first row of the CSV file specified the field information, the system will read it automatically. 

　　3. Data Preview: You can preview the effect of result attribute table after your CSV file is imported. 

　　4. Import Spatial Data 
　
   - WKT Field: Obtain spatial information of your data through the specified WKT way. WKT is a text markup language for representing vector geometry object, spatial reference systems and transformations between spatial reference systems.
   - Coordinate Field: Specify the spatial information for the CSV data by setting the longitude, latitude, and elevation fields. 



### Related topics

![](img/smalltitle.png) [AutoCAD data](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis data](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS data](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo data](ImportMapInfo.html)

![](img/smalltitle.png) [Image/raster data](ImportIMG.html)

![](img/smalltitle.png) [3D model data](ImportModel.html)

![](img/smalltitle.png) [Lidar data](ImportLidar.html)

![](img/smalltitle.png) [Google data](ImportKML.html)

![](img/smalltitle.png) [Vector file](ImportVectorFiles.html)

![](img/smalltitle.png) [Import folder](ImportFolder.html)



