---
title: Common Parameters for Importing Data
---

### Function entrances

 - In the workspace manager, right click your datasource and then in the context menu, select "Import Dataset...".
 - In the "Start" tab, click "Data Import" button on the "Data Processing" group.

### About parameters

　　Because of the data types imported are different, the parameters set are different, but some base parameters are the same. Following contents details these common parameters.

##### Data list

 - **Original Data**: The names of files to be imported.
 - **File Type**: The types of files to be imported.
 - **State**: Before importing your data, the state is "Unconverted". If your data has been imported successfully, the state is "Succeed", otherwise, the state is "Failed". (Only existed in "Data Export" dialog box.)

##### Toolbar

　　Five image buttons are provided, including: add file, add folder, delete, select all, inverse.

##### Result settings

 - **Encode Type**: Specify the character encoding after your data is imported. The imported dataset does not use any encoding by default. Various encoding are provided, such as Byte, Int 16, Int 24, Int 32.
 - **Dataset Type**: Specify the dataset type after your data is imported. Such as: CAD, simple dataset, raster dataset.
 - **Import Mode**: Following three modes are provided to process the problem that there is a namesake dataset existed under your datasource.
    -  None: The system will modify your data's name and import it into your datasource automatically.
    -  Replace: The existed dataset will be replaced by the newly imported dataset.
    -  Append: If the type and structure of data to be imported is the same with the namesake dataset's, append the newly imported data into the existed dataset, otherwise, the data will not be imported, and the value of State in the Data Import dialog is Failed.
 - **Import Null Dataset**: If the data to be imported is empty, when you check the check box, an empty dataset will be imported without any object, in contrast, the system will prompt you failure importing. 

##### Source file information
 
  -  **Source File Path**: Display where your data to be imported is located in.
  -  **Properties**: Click it to view attributes of your data.

### Related topics

![](img/smalltitle.png) [Buffer analysis](BufferTheory.html)

![](img/smalltitle.png) [Sample application on buffer analysis](BufferAnalyst_Example.html)


