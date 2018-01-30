---
title: Modify Custom Maps
---

"Thematic Mapping" panel is used for setting the filling style, line style and symbol style for a thematic map. The filling style can work only for region objects from region vector thematic maps, the line style apply to the line objects from line vector layer and boundaries of region objects from region vector thematic map, and the symbol style works for point objects from point vector thematic maps.

### Fill

Set rendering styles for region objects in a custom map.

- **Fill Symbol**: A numeric field or field expression holding the symbol ID. The system symbol with the corresponding ID will be called in to render the object.
- **Transparency**: A numeric field or field expression holding the opacity if the symbol used to render the object. The opacity ranges from 0 to 100, with 0 being fully transparent and 100 being fully opaque. If you set the Opacity to a value beyond the range, the symbol would be opaque.
- **Foreground**: A numeric field or field expression holding the color code. The color is used to fill the foreground of the symbol. The field value is firstly converted into a hex value, and then the value is picked according to the hex value. For instance, if the field value is 1000, it will be firstly converted into 3E8, then 0s are added ahead to form a 6-digit number. After that, the number is split into three parts:00, 03, and E8 (232 in decimal), representing the G, B, and R values.
- **Background**: A numeric field or field expression holding the color code. The color is used to fill the background of the symbol. The field value is firstly converted into a hex value, and then the value is picked according to the hex value. For instance, if the field value is 1000, it will be firstly converted into 3E8, then 0s are added ahead to form a 6-digit number. After that, the number is split into three parts:00, 03, and E8 (232 in decimal), representing the the G, B, and R values.
- **Gradient Mode**: A numeric field or field expression holding the value representing the gradient mode. 1, 2, 3, 4 respectively represent linear, circular, conical, square, and other values represent none.
- **Gradient Angle**: A numeric field or field expression holding the value representing the gradient angle in degrees. The default is 0. A positive value indicates counterclockwise rotation while a negative value indicates clockwise rotation.
- **Offset X**:  The percentage of the gradient center to the fill center in the horizontal direction. Applicable to conical, square and circular gradient fills. A positive value indicates left offset while a negative value indicates right offset.
- **Offset Y**: The percentage of the gradient center to the fill center in the vertical direction. A positive value indicates up offset while a negative value indicates down offset.

### Line Type

Set rendering styles for line objects or the boundaries of region objects in a custom map.

- **Line Symbol**: A numeric field or field expression holding the symbol ID. The system symbol with the corresponding ID will be called in to render the object.
- **Line Color**: A numeric field or field expression holding the color code. The color is used to render the line. The field value is firstly converted into a hex value, and then the value is picked according to the hex value. For instance, if the field value is 1000, it will be firstly converted into 3E8, then 0s are added ahead to form a 6-digit number. After that, the number is split into three parts:00, 03, and E8 (232 in decimal), representing the the G, B, and R values.
- **Width**: A numeric field or field expression holding the value representing the the line width in 0.1 millimeters.

### Symbol

Set rendering styles for point objects in a custom map. 

- **Marker Symbol**: A numeric field or field expression holding the symbol ID. The system symbol with the corresponding ID will be called in to render the object.
- **Color**: A numeric field or field expression holding the color code. The color is used to render the point. The field value is firstly converted into a hex value, and then the value is picked according to the hex value. For instance, if the field value is 1000, it will be firstly converted into 3E8, then 0s are added ahead to form a 6-digit number. After that, the number is split into three parts:00, 03, and E8 (232 in decimal), representing the the G, B, and R values.
- **Marker Size**: A numeric field or field expression holding the value representing the the marker size in 0.1 millimeters. 0 indicates that the original size will be employed. A negative value indicates that the marker will not be displayed.
- **Rotation**: A numeric field or field expression holding the value representing the rotation angle of the marker symbol in degrees. A positive value indicates counterclockwise rotation while a negative value indicates clockwise rotation. The unit is degrees.