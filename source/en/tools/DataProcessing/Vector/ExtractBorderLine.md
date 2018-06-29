---
title: Extract Border Line
---

　　Extract all boundaries of regions into a result dataset. The common boundary of two regions will be extracted only once. if a border line has either left or right region polygon, then the border line will be the outline of the region outline.


### Basic steps

 1. In the toolbox, click "Data Processing" > "Vector" > "Extract Border Line" to open the "Extract Border Line" dialog box.
 2. Specify the region dataset.
 3. **Topology Preprocessing**: Check the option, these operations: splitting by intersection, adjusting polygon directions, inserting nodes between nodes and line segments, and snapping to nodes, will be performed on the the region dataset to avoid the generation of error data like pseudo-nodes, duplicated node, dangling line segments, duplicated lines, etc.
 4. Click "Run" to start the operation.

  ![](img/BorderLine.png)


### Relate topics

![](img/smalltitle.png) [Dissolve](Datafuse.html)



