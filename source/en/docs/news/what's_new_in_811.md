---
title: SuperMap iDesktop Cross 8C(2017) New Features
---


SuperMap iDesktop Cross 8C(2017)
On the basis of the previous version, the functions related with modules mapping, map edit, data process, data analysis are added and optimized. Among them, the functions creating and modifying level symbol, point density and custom thematic map are added for mapping module, the management and edit of color scheme are suported, also style settings of text object is provided. For object edit module, the features: node edit, smooth, resample, donut, erase, convert type of object are supplied. At the same time, the editing function of point, line and surface object is improved. Data process module adds these functions: registration, attribute table updating column, custom projection, spatial query, and so on. Meanwhile, improving the usability of program, such as development framework, tools of deployment, restore workspace automatically, and dragging toolbar, etc.

**Following details the added and optimized features on SuperMap iDesktop Cross 9D:**

### Mapping

-   Enriched thematic map type, added level symbol, point density, the functions creating and modifying custom thematic map to meet the needs for user to apply multiple types thematic.
-   Management function of color scheme is added, it supplies abundant color scheme which can be applied to any kinds of thematic map, also it is supported that creating, editing, importing, exporting the color scheme and adding it to "my favorite scheme".
-   Supports setting the display style of the selected text object, including the parameters: font name, font size, color, display effect.
-   Supports the associated browse between layer and its attribute table, which is achieved the linkage operation between attribute records and the corresponding objects highlighted in map.
-   Added CAD layer object style setting function, you can batch set the selected object style to optimize the rendering effect of the layer.

### Object Edit

-   Added many functions for point, line, region object, including: add node, edit node, stretch, erase, trim, connect line, resample, smooth, change direction, select object to clip, donut polygon, zoom, line converts to point, region converts to lines, lines converts region, explode, positioning copy, mirroring, rotate, and so on.

### Data Management

-   Added the function of viewing a selected object node attribute which is convenient for users to view object type, sum number of nodes and coordinate of node.
-   Supports for viewing attribute information of a selected text object, if status of the layer where the selected object locates is editable, you can set style for text object.
-   Supports for switching the opened mode of UDB datasource as read-only or exclusive by one key, for the read-only mode, you can convenient to compare map or data by opening the same datasource in multiple applications at the same time.
-   Supports for reopening UDB datasource which has some occupied problems by refreshing. If the datasource occupied by original program is closed, you can reopen it through right-clicking mouse and then clicking "Refresh" in datasource or workspace opened newly; if the opened mode of datasource is switched to read-only, the datasource will be opened in read-only mode by refreshing again, you don't need to re-specify datasource path.
-   Supports the settings of global parameter from application program, you can make personalized desktop options configuration (parameters: common, environment, edit, etc.), meanwhile, you can view the version information of application program.

### Data Process

-   Added single registration and bulk registration. Through specified algorithm and control point, according to reference data to correct and change the spatial position of registration data.
-   Added function "Attribute Table Updating Column". By certain conditions or rules, unified modifying field values of multiple records in attribute table, which makes it convenient for users to enter or modify attribute table data.
-   Supports for setting custom coordinate system, user can customize a coordinate system and save it according to related geography or projection coordinate system parameters.
-   Added function "Spatial Query". Through spatial position relation between geometric objects to construct the filter condition to query objects who meet the condition from existing data.

### Data Analysis

-   Added function "Overlay Analysis". Through processing and analyzing spatial data, extracting new spatial geometric information required by users, such as: clip, union, intersect, erase, etc.

### Usability

-   Added function "Restore Workspace Automatically", application will save current workspace regularly. If the application exits suddenly, you can restore data according to the saved workspace to avoid missing data (map, scene, and so on).
-   Optimized the drag and float of toolbar, supported multiple rows of toolbar parallel display, can be adaptive program interface width.
-   Using new implementation framework "Felix" from OSGI.
-   Using IDEA and Maven to do development deploy, configuring auto management and deploying dependency of Java and OSGI Bundle  by pom, at the same time, re-organize code and standardize the structure of the project code.
-   Using IDEA which can greatly improve the development efficiency, and supporting Java IDE tools, such as: Eclipse, NetBeans.
