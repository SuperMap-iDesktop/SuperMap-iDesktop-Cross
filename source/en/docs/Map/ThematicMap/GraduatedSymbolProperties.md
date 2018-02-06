---
title: Modify Graduated Symbols Maps
---


Optimize display effect for a Graduated Symbols Map through setting related parameters in the Properties panel. The parameters are detailed below.


**Graduated by**: The size of symbols can be specified by clicking the button beside the label. You can choose an appropriate method to calculate the graduated value.

- **Constant**: Graduates the symbol by the attribute value.
- **Square Root**: Graduates the symbol by the square root of the attribute value.
- **Logarithm**: Graduates the symbol by the logarithm of the attribute value.

**Datum Value**:  The attribute value the default size of the specified symbol represents. The other sizes will be calculated with the specified method based on this value. The greater the value, the smaller the graduated symbols. The datum value has the same range with the thematic variable.

**Symbol Style Settings**: Set symbol styles for positive values, 0, and negative values. To set symbol style for 0 and negative values, you must check the boxes before Show Zero and Show Negative first. The symbols styles for the positive values, 0, and negative values are independent to each other.

- **Positive**: Set symbol style for positive values. Click the button at right of the label, set the symbol style for positive values in the dialog box (for details, please refer to Marker Symbol Style Settings).
- **Show Zero**: Check the box to show symbols for 0s and render the symbols with the specified style. Click the button at right of the label, set the symbol style for 0 in the dialog box (for details, please refer to Marker Symbol Style Settings).
- **Show Negative**: Check the box to show symbols for negative values and render the symbols with the specified style. Click the button at right of the label, set the symbol style for negative values in the dialog box (for details, please refer to Marker Symbol Style Settings).
- **Flow**: Check the box to display labels in floating mode. If checked, the labels move with the map window for optimal effects. When you check Flow, please don't use the partly refreshing function, otherwise the display will be incorrect.

**Offset Settings**:  

- **Offset Unit**: Set the unit of the offsets. There are two units for people to choose in the drop-down menu. "0.1mm" means the offset unit is 0.1 millimeter, another alternative unit "Unit" means the offset unit is the same with the unit of coordinate system of the map.
- **False Easting**:  The horizontal label offset. You can directly type in a value or select a numeric field from the drop-down list.
- **False Northing**: The vertical label offset. You can directly type in a value or select a numeric field from the drop-down list.
- **Show Leader Line**: Check the box to show lines connecting the graduated symbols and the objects if the graduated symbols are placed away from the objects. You can set the styles for the leader lines through the Line Symbol Selector dialog box that shows up by clicking the Line Style button. To know more about how to set line styles, please refer to Line Symbol Style Settings.
