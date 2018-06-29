---
title: Extract Center Lines from Dual Lines
---

　　Generates center lines from non-closed dual lines and save them as a line dataset. This function is usually used to get center lines for streets comprising of two lines. The function works better for parallel lines. 


### Basic steps

 1. In the toolbox, click "Data Processing" > "Vector" > "Extract Center Line from Double Lines" to open the "Extract Center Line from Double Lines" dialog box.
 2. Specify the source line dataset. To ensure the result correct, first you need to process the closed lines to be non-closed, then perform the operation.
 3. **Max Width**: the maximum distance between the double lines. The unit of this parameter is the same with the source dataset. To determine the max distance, you can click "Maps" > "Operation" > "Map Measure" > "Distance" to manually measure the widest part of the double line. It is advised that you set the max width parameter a bit larger than the measured value. 
 4. **Max Width**: the minimum distance between the double line. If the distance is less than the min width, then no center line will be extracted for that part of the double line. The unit of this parameter is the same with the source dataset. 
 5. Click "Run" to perform the operation.

  ![](img/DualLineToCenterLine.png)

### Notes

　The units of maximum and minimum width should be the same with source dataset. The maximum width should be larger than 0. Center lines will only be extracted from double lines that distances are between Max and Min Width. If the distance between double lines is larger than the max width, the border line will be extracted.

### Related topics

![](img/smalltitle.png) [Dissolve](Datafuse.html)

