---
title: Manage Workspaces
---
  


　　A workspace is where you operate on your data. A workspace must be created first before any operations. All of your work, such as the datasources opened, maps, layouts and scenes you configured, etc., can be saved in a workspace for future use.
  
### Workspace types
  
According to how workspaces are stored, there are two types: file workspace and database workspace.

* **File workspace**: Your workspace is saved as two files that extensions are *.sxw/*.smw or *.sxwu/*.smwu.
* **Database workspace**: Your workspace is saved into a database. Currently, the supportive database workspaces include: SQL Server, Oracle, PostgreSQL, MySQL, MongoDB, DM.
    
### Workspace structure
  
In SuperMap iDesktop Cross, one workspace corresponds with one workspace consisting of a tree structure, and the datasource collection, map collection, layout collection, scene collection, symbol library collection in a datasource are unique.
 
Datasource collections are used for managing all datasources opened in your workspace. Map/Layout collections are used for saving maps/layouts in your workspace. 3D scene collections are used for saving 3D scenes and the symbol library collections are used for managing symbol libraries.
Existing of maps, layouts, 3D scenes and symbol resources depends on workspaces, that means all of them are saved into workspaces. When you delete a workspace, maps, layouts, scenes and symbol resources are deleted as well in the workspace but datasources which are saves independently, and only the interrelationships between them are deleted.

### Open a workspace
   

Three ways are provided to open a workspace:   
  
+   Click Cross icon > "Open", and then you can select any type of workspace you want to open.
+   Click "Start" tab > "Workspace" group, and then you can select "File" or "Database" drop-down button to open a workspace.
+    Right-click the "Untitled Workspace" node in the "Workspace Manager" and then select "Open File Workspce..." or "Open Database Workspace...". 
      
**Note**:  
 
You can open only one workspace in the product. Therefore, the current open workspace must be closed first before opening another workspace. When closing the current workspace, the save prompt will pop up if there are unsaved changes. 

### Save workspaces  
  
 **Save** button is used to save the currently opened workspace and operation results in it, to use the operation results when open the workspace again, you must save them into the current workspace first then save the workspace.
  
1. **Save operation results first** If there are some unsaved contents, the Save button is available and click it to open the "Save" dialog box where you can specify which items will be saved. 
2. **Save workspaces** After specifying the items you want to saved, click "Save" button to save the current workspace. 
3. **Save as workspace** You can click Cross icon then select "Save as..." to save the current workspace as a file datasource or a database workspace.


### Close workspaces  
**Close Workspace** is used for closing your workspace. 
1. Right-click your workspace node and then select "Close Workspace".
2. If there are some contents unsaved, a dialog box reminding you to save contents will pop up.
3. If you click "No", the changed and unsaved contents can not be saved into your workspace, but if you click "Yes", they will be saved into your workspace.
  








