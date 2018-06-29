---
title: Export Raster Dataset
---

### Instructions

　　SuperMap iDesktop Cross supports the export of raster datasets and image datasets, the supportive types are: 
  
- Supported raster dataset types: Erdas Image files, TIFF files, ArcGIS Grid files, Telecom raster files.
- Supported image dataset types: bitmap files, JPG, GIF, PNG, SIT image file, Erdas Image file, TIFF file, ArcGIS Grid file, Telecom raster files.


### Basic steps

1. In the Data Export dialog box, click Add image button to add one or more raster/image datasets to export. (For toolbox, only one dataset can be exported at a time)
2. Following parameters are required to set.

 - **Export Type**: Specify what format the result dataset will be.
 - **Coverage**: Whether to export a selected dataset when there is a namesake dataset under the result path, checking it means the namesake dataset will be replaced, in contrast, the export operation will be canceled.
 - **State**: Before exporting your data, the state is "Unconverted". If your data has been exported successfully, the state is "Succeed", otherwise, the state is "Failed". (Only existed in "Data Export" dialog box.)
 - **Result File**: The name of file to be exported which is the same with the original name, you can name a new name for the result file through the way: click "Result File" item or F2, then enter a new name. 
 - **Export Path**: Set a path to save the result file.
3. Select one or more datasets (in "Data Export" dialog box), and then set following parameters:
  - **Compression(%)**:  Specifies the compression ratio of image files. This item is only available for datasets in jpg format.
  - **Reference File**: Specify a path to save the coordinate reference files of raster datasets to be exported. It is active when the files result in the format: JPG, PNG, BMP. GIF.
  - **Affine transformation info exported to tfw file**: Specifies whether you want to export the mapping information of the image coordinates and geographic coordinates, to an external .tfw file. The item is only available when exporting a dataset resulting in .tiff format. By default, the box is checked, indicating that the affine information will be exported to an external .tfw file. If you do not check the box, the affine information will be exported to a .tiff file. 
  - **Password**: You can set a password for your SIT data. **Note**: After setting a password for your SIT data, you must enter the password when you want to export it, open it, add it to a scene or generate a cache, etc..
4. Click "Export" button (Run image button in toolbox) to export your data.

### Note 

-   The largest amount of TIFF image data which can be exported through the third party TIF library used in SuperMap iDesktop Cross 9D is 4G. If your data is larger than 4G, it is advised you split them into smaller files, or export them resulting in SIT format.
-   When importing or exporting telecommunication data, following points should be noted:    
	- Only data in GRID format can be exported or imported.   
	- Only data that pixel bit depth is 16 can be exported or imported. When the pixel bit depth of a Grid dataset is lower than 16, a conversion to 16 for pixel bit depth will be done during exporting the Grid dataset.
	- Only the Grid dataset whose resolution is an integer and X resolution and Y resolution are the same. 

 


