---
title: Import AutoCAD Data
---

### Instructions

　　AutoCAD in 3 formats can be imported into SuperMap iDesktop Cross: DWG,  DXF and DGN.
  - DXF: short for Drawing Interchange Format. It is a file format that AutoCAD data and data in other formats exchange each other. It is an ASCII or binary file format for outputting or inputting images.
  - DWG: It is a AutoCAD drawing file for saving vector pictures.
  - DGN: one kind of CAD file formats supported by MicroStation and Intergraph's Interactive Graphics Design System(IGDS).


### Basic steps

1. Add CAD data: the ways of adding CAD data through different function entrances are different. For function entrances, please refer to [Common Parameters for Importing Data](GeneraParameters.html).
2. On the result settings and source information, you can refer to [Common Parameters for Importing Data](GeneraParameters.html).
3. Following content introduce the conversion parameters of AutoCAD DXF file(*.dxf) and ArtoCAD Drawing(*.dwg) file: 
  - Curve Accuracy: Curve Accuracy: Sets the edge accuracy of line or region objects. By default, the curve fitting accuracy is 73.
  - Unite Layer: Checking it means when importing DWG/DXF files, all layers will be merged into one layer. It is checked by default. 
  - Import Symbol Block: Specify whether to import block objects or import block objects as points. Importing symbol blocks is by default.
  - Import Extended Field: Used for importing data in AutoCAD drawing files like attribute tables when exporting drawing files in CAD formats. The extended data will be imported as some extra fields being appended next to default fields.
  - Import Invisible Layers: Specify whether to import the invisible layers in AutoCAD drawing files.
  - Import Block Attribute: Specify whther to import the block attributes of data.
  - Import Extended Records: Specify whether to import your custom fields and attribute fields as extended records. If you do not check it, the fields will not be imported.
  - Keep Object Height: When the imported CAD includes 3D objects and you selected this item, it will remain the height information of the 3D objects (the Z coordinates of the object). If you don't check this box, the height information will not be kept. After imported  DWG/DXF  successfully, a field Elevation will be created in the attribute table of generated dataset for storing object height information. 
  - Keep Parametric Object: Specify whether to import parametric objects when importing CAD data.
  - Keep Line Width: Checking it means the polyline width in AutoCAD drawing files will be retained, In CAD, polylines consist of a series of straight lines and arcs, and of them, each line can be given a line width. The original polyline width is retained by default.

4. Set the conversion parameters for importing AutoCAD DGN data: 

  - Unit object is imported as the point object: Specify whether to import all unit objects as point objects. If you do not check it, all unit objects will be imported in the formats of all features but cell header.


### Note

　　If the imported data includes the parametric object and you have checked this box, you should note following two situations.

- CAD: If you set the CAD Dataset as the dataset type. The created CAD dataset contains the CAD region and line objects with the parametric objects. 
- Simple dataset: If you set the Simple Dataset as the dataset type. It will create the CAD region or line for saving the parametric objects. 

### Related topics

![](img/smalltitle.png) [ArcGis data](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS data](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo data](ImportMapInfo.html)

![](img/smalltitle.png) [Spreadsheet](ImportTable.html)

![](img/smalltitle.png) [Image/raster data](ImportIMG.html)

![](img/smalltitle.png) [3D model data](ImportModel.html)

![](img/smalltitle.png) [Lidar data](ImportLidar.html)

![](img/smalltitle.png) [Google data](ImportKML.html)

![](img/smalltitle.png) [Vector file](ImportVectorFiles.html)

![](img/smalltitle.png) [Import folder](ImportFolder.html)




