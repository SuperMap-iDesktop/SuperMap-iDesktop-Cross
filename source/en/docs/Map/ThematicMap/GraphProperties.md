---
title: Modify Graph Maps
---


Optimize display effect for a Graph Map through setting related parameters in the Properties and Advance panel, following are the parameters in details.

### Properties


**Graph Type**: 13 graphs are provided for people to choose: Area, Step, Line, Point, Bar, 3D Bar, Pie, 3D Pie, Rose, 3D Rose, Stacked Bar, 3D Stacked Bar, and Ring.
 
**Graduate By**: The method is used to decide the size of symbols and the proportion of each thematic variable value. Three options are provided: Constant, Logarithm, and Square Root. For fields with negative values, only the Constant method can be used.
- Constant: Graduated by the attribute value.
- Logarithm: Graduated by the logarithm of the attribute value.
- Square Root: Graduated by the square root of the attribute value. 

**Add or Delete fields**: You can add or delete one or more fields from the list by clicking the Add and Delete image buttons provided on the toolbar above the list. With the changing of the thematic fields, the thematic map will be instantly reconfigured and refreshed.
- Add fields: Click the Add image button on the toolbar to display the field list , check the box before a certain field (you can check more boxes), or click Expression... at the bottom to display the SQL Expression dialog box and construct a field expression, and then click OK to add to the fields or field expressions to the list as thematic variables.

- Delete fields: Select the thematic variables you want to remove from the list and click the Remove image button on the toolbar to remove them.
- Up and Down:  You can move the thematic variables up or down with the buttons. The position of variables will affect the result of graph maps.

**Style**: You can set styles for variables in the list by clicking "Style" image button in the toolbar. Or you can double click the style in every field to change the style.


### Advanced


 "**Option**" area: To adjust and control the display of statistical symbols.
 
 - **Flow**: Check the box to display graph symbols in floating mode. If checked, the graph symbols will change positions in the map window for optimal effects when zooming map in or out. When you opened the Flow, please don't use the partly refreshing function, or the display will be incorrect.
 - **No Overlap**: Check the box to display graph symbols as much as possible without overlap.
 - **Show Negative**: Show graph symbols for negative values.
 - **Scale with Map Zoom**:  Check the box to scale the graph symbols with map zoom. Otherwise, the sizes of all statistical symbols are fixed.
 - **Show Leader Line**: Check the box to show lines connecting the graph symbols and the objects if the graph symbols are placed away from the objects. You can set the styles for the leader lines through the Style Settings dialog box that shows up by clicking the Line Style button to the right.

 "**Visible Size Limited**" area: The maximum and minimum sizes of the graph symbols. The sizes of all symbols will vary during the maximum and minimum sizes. If "Scale with Map Zoom" is checked, the unit for Visible Size Range is map units, if not, the unit is 0.01mm. 

- **Max Size**: The maximum size of the graph symbols. When the thematic maps are pie, 3D pie, rose, 3D rose and ring, the Graph diameter is the quarter of the max size. The height of other ThemeGraphs, such as the columnar and area, is the quarter of the maximum display. Other graph symbols are drawn according to the maximum and minimum symbols.
- **Min Size**: The Minimum size of the graph symbols. When the thematic maps are pie, 3D pie, rose, 3D rose and ring, the Graph diameter is the quarter of the min size. The height of other ThemeGraphs, such as the columnar and area, is the quarter of the maximum display. Other graph symbols are drawn according to the maximum and minimum symbols.

 "**Offset Settings**" area: Controls the distances the graph symbols are placed from the objects.

- **False Easting**:  The horizontal graph symbol offset. You can directly type a value in the combo box or select a field from the list.
- **False Northing**: The vertical graph symbol offset. You can directly type a value in the combo box or select a field from the list.
- **Offset Unit**: Set the unit in which the offsets will be measured. The graph symbol offset distance can be measured in map units or 0.1 millimeters.

 "**Graph Labels**" area: Set whether to display texts in a graph map or not. If the check-box is checked, Label Format and Label Style will be enabled for people to set the text format and the text style.

- **Label Format**: The options provided include: Percent, Value, Caption, Label + Percentage, and Label + Value.
- **Label Style**:  The style of the graph symbol labels. Click the button at right to display the label style settings dialog box and set the graph symbol label style in it.

 "**Axis Options**" area: Specifies whether and how to display the axes. The Axis Options settings are applicable to Area, Step, Line, 3D Line, Bar, 3D Bar, Stacked Bar, 3D Stacked Bar, etc.

- **Axis Color**: The color of the axes. Click the button at right to display the color panel and specify a color for the axes.
- **Labeling Mode**: How the axes will be labeled. You can choose to label both axes, Y axis only, or none. By default, both axes are labeled.
- **Label Style**: The style of the text labels for axes. Click the button to the right to to display the color panel and specify a color for the labels of axes.
- **Show Axis Grid**: Check the box to show axis gridlines.

**Bar Styles**

- **Bar Width Factor**: The width of each bar. The default is half the max width for the graph map. You can specify a value ranging from 0 to the max width. The unit is map units. The Bar Settings are applicable to Bar, 3D Bar, Stacked Bar, and 3D Stacked Bar graphs.

**Rose and Pie Styles**: Set angles.

- **Start Angle**: The start angel of the Pie/Rose graph. The default is 0, the positive direction of the X axis. The Start Angle setting is applicable to Pie, 3d Pie, Rose, and 3D Rose graphs. 
- **Rose Angle**: The angle of each sector. The default is 0. If you set Rose Angle to 0 or greater than 360 degrees, the angle of each sector will be 360/Number of sectors.
When changing the settings in the Thematic Mapping window, all changes for the thematic map are instantly shown on the map. To prevent instant refresh, uncheck the Enable Instant Refresh box. If the thematic map is not instantly refreshed during the modification, click Apply at the bottom to apply all changes to the thematic map when the modification is done.

