---
title: Extract Center Line from Region
---

　　Extract the center line for each polygon in a region dataset and save results in a line dataset. In general, the function is used for extracting center lines for street polygons.


### Basic steps

 1. In the toolbox, click "Data Processing" > "Vector" > "Extract Center Line from Region" to open the "Extract Center Line from Region" dialog box.
 2. Specify the region dataset in which center lines of polygons will be extracted.
 3. **Max Width**:  the maximum distance between two boundaries from the same polygon. 30 is by default. Its unit is the same with the source dataset. You can use the Map Measure tool to calculate the widest distance between two boundaries of a region. 
 4. **Min Width**: the minimum distance between two boundaries from the same polygon. When the distance between two boundaries is less than the value, no center line is extracted for them. 0 is by default and its unit is the same with the source dataset.
 5. Click "Run" to perform the operation.

  ![](img/RegionToCenterLine.png)

### Note

　　Max Width must be set and greater than 0. When the distance between two boundaries of a region is larger than the max width, the two boundaries will be extracted, but if it is less than the min width, no line will be extracted.

### Related topics

![](img/smalltitle.png) [Dissolve](Datafuse.html)

