---
title: Export Vector Dataset
---

### Instructions

　　The vector data formats supported by SuperMap iDesktop Cross contain: CSV, GeoJson, SimpleJson, Chinese standard vector exchange format, AutoCAD DWG, AutoCAD DXF, ArcGIS Shape, Google KML, Goole KML, Telecom Building vector, ArcInfo Export, MapInFo MIF, MapInfo TAB, Microsoft Excel. 

### Basic steps

1. In the Data Export dialog box, click Add image button to add one or more vector datasets to export. (For toolbox, only one dataset can be exported at a time)
2. Following parameters are required to set.

 - **Export Type**: Specify what format the result dataset will be.
 - **Coverage**: Whether to export a selected dataset when there is a namesake dataset under the result path, checking it means the namesake dataset will be replaced, in contrast, the export operation will be canceled.
 - **State**: Before exporting your data, the state is "Unconverted". If your data has been exported successfully, the state is "Succeed", otherwise, the state is "Failed". (Only existed in "Data Export" dialog box.)
 - **Result File**: The name of file to be exported which is the same with the original name, you can name a new name for the result file through the way: click "Result File" item or F2, then enter a new name. 
 - **Export Path**: Set a path to save the result file.
3. Select one or more datasets (in "Data Export" dialog box), and then set following parameters:
  - **Export Extended Field**: Specifies whether to export the extended fields of AutoCAD files.
  - **Export Extendsion Record**: Specifies whether to export the extended records of AutoCAD files.   
  - **Export Point as WKT**: Specifies whether to export points as WKT fields. 
  - **Export Header**: Specifies whether to export header information of files.
  - **Charset**: Specify the datasets resulting in what character encoding. ASCII (Default) is by default.
  - **CAD Version**: You can choose which CAD file (*.dwg ) or file's version (*.dxf ) the exported files result in from the drop-down list. The supportive versios contain: AutoCAD R12, AutoCAD R13, AutoCAD R14, AutoCAD 2000, AutoCAD 2004, AutoCAD 2007, and AutoCAD 2007 is by default.    
  - **SQL Expression**: You can set a SQL expression, and the objects which meet the condition will not be exported.

4. Click "Export" button (Run image button in toolbox) to export your data.

###Note

　　Only attribute tables with records no more than 100,000 will be exported to a Microsoft Excel. If the number of records is 0, the exporting operation will fail. 
