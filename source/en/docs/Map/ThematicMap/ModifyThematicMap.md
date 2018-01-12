---
title: Modify Themaitc Maps
---

　　Creating a thematic map only through a template can not meet users' needs, the settings of parameters to change maps have been provided in "Thematic Mapping" panel. For different types of thematic maps, the settings of parameters are different.

#### Common Parameters

- **Expression**

　　Expressions are used for creating fields or field expressions of thematic maps. For a vector data, the expression can be either a single field or a mathematical expression of one or more fields constructed by SQL Expressions. Such as you can take the expression "Pop_2006||ten thousands" as the thematic variable of the map you want to create, among the expression, Pop_2006 is the field indicating the population in 2006, and the unit is ten thousands.

　　Note: When creating thematic maps for the data saved in Oracle Plus and SQL Plus, the field expression only can be field's value (SmID) or field's operation (SmID + 1). If you set "SmID > 100", then the thematic map can not be created successfully.

- **Items of Thematic Maps**

　　The items are different for different thematic maps. Features in a item is taken as a body, when setting styles for a item, all features in the item will change at the same time. There are items in these kinds of thematic maps: Unique Values Map, Ranges Map, Label Map, Graph Map, Grid Unique Values Map, Grid Ranges Map. 

  - For a Unique Values Map, an item is a class in which the thematic values of features are the same. For example,  in a unique map, there is a field Name (the thematic variable) indicating names of provinces, the values of the field Name of all features are the same in an item of the map.
  - For a Ranges Map, the thematic values will be divided into several ranges, and all features will be distributed to a range based on their thematic values. And a range is an item.
  - For a range label thematic map, a range is an item.
  - Multiple thematic variables can be shown in one Graph Map. The number of items is the number of thematic variables. Such as in a Country-City Population Statistic Map, two thematic variables about the population can be shown at the same time, one is for countries, another is for cities, and so there are two items in the map. 
  - A Grid Ranges Map is similar with a Ranges Map, the difference is that the operated object for the former is vector data, but for the latter is raster data, accordingly, the items of a Grid Ranges Map are the different ranges.
  - For a Grid Unique Values Map, all features in the map with the same attribute value will categorized as a class, each class is an item. 

- **Color Scheme**

　　Provide color schemes for rendering all items of thematic maps and assign a rendering color for every item according to the number of items. You can customize a color scheme, also you can choose a color scheme from every color category provided by system. Based on the types and application of thematic maps, the color schemes have been categorized as: For DEM, For Aggregation Map, For Graph Map, For Ranges Map, For Unique Values Map,	For Range Label Map, For Unique Label Map.

- **Item Style**

　　You can select multiple items to modify styles for marker symbols, line symbols, and filled symbols during creating a thematic map.

　　**Marker Symbol**

 Parameter         | Description           
 :-------------- | :---------------
 Type | The type of the marker used 
 Display Size | The size of the marker used when displayed on a map
 Color | The color of the marker
 Icon | An icon which replaces a point symbol
 Lock Aspect Ratio | Lock the ratio between the height and width of a marker symbol
 Rotation | Set the rotation angle for a marker symbol when it is a 3D symbol. The unit is degree(°)

　　**Line Symbol**

 Parameter         | Description           
 :-------------- | :---------------
 Style | The style of the line symbol or boundary line of a filled symbol
 Width | The width of the line symbol
 Line Color | The color of the line symbol 

　　**Filled Symbol**

 Parameter         | Description           
 :-------------- | :---------------
 Style | The style of the filled symbol
 Foreground | The color of filled contents
 Background | The color of unfilled contents
 Transparency | An integer from 0 to 100 is used for setting the transparency effect of the filled symbol.
 Type | The gradiently filled type when enabling Gradient Fill, including Linear, Radial and Square.
 Rotation | Set the rotation angle
 Offset X | The relative horizontal offset percent from the center of gradiently filled symbol to the center of the filled range. 
 Offset Y | The relative vertical offset percent from the center of gradiently filled symbol to the center of the filled range.


　　**Instantly Refresh**

　　When changing the settings in the Thematic Mapping window, all changes for the thematic map are instantly shown on the map. To prevent instant refresh, uncheck the Enable Instant Refresh box. If the thematic map is not instantly refreshed during the modification, click Apply at the bottom to apply all changes to the thematic map when the modification is done.


　　[Unique Values Map](/SuperMap-iDesktop-Cross/docs/Map/ThematicMap/UniqueValueProperties.html)

　　[Ranges Map](/SuperMap-iDesktop-Cross/docs/Map/ThematicMap/RangesMapGroupDia.html)

　　[Label Map](/SuperMap-iDesktop-Cross/docs/Map/ThematicMap/LabelProperties.html)

　　[Graph Map](/SuperMap-iDesktop-Cross/docs/Map/ThematicMap/GraphProperties.html)

　　[Dot Density Map](/SuperMap-iDesktop-Cross/docs/Map/ThematicMap/DotDensityProperties.html)

　　[Graduated Symbols Map](/SuperMap-iDesktop-Cross/docs/Map/ThematicMap/GraduatedSymbolProperties.html)

　　[Custom Map](/SuperMap-iDesktop-Cross/docs/Map/ThematicMap/CustomizeProperties.html)
