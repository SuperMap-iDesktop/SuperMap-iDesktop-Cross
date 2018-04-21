---
title: New a datasource
---


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

  ![](img/Propertise.png)    
    
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
 