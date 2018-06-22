---
title: Integrate
---

### Instructions

　　The nodes within the tolerance range will be considered as the same object or overlap objects. The operation can be used for processing the problems that there are gaps or overlaps between common borders.

 - Applicable for point, line, region datasets.
 - The source dataset will be changed after the operation, hence backing up it is better.
 - The nodes being snapped together will be with the same 2D coordinates after Integrate operation.


### Basic steps

 1. In the toolbox, click "Data Processing" > "Vector" > "Integrate" to open the "Integrate" dialog box.
 2. Specify the dataset to be integrated.
 3. **Integrate Tolerance**: All nodes within the tolerance range will be snapped together. If the value is too great, some polygons or lines will be deleted, or some nodes which you do not expect to be moved will be moved.
 4. Unit: Multiple units are provided for you to choose from. Meter is by default.
 5. Click "Run" to execute the operation.

  ![](img/Intergrate.png)

### Related topics

![](img/smalltitle.png) [Dissolve](Datafuse.html)



