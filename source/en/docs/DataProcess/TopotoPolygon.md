---
title: Construct Region by Topology
---

### ![](img/read.gif)Instructions

　　Builds the region dataset with line dataset or network dataset by the topology processing.

### ![](img/read.gif)Basic Steps
1\.  Click "Data" > "Topology" > "Region by Topology", and set related parameters in the pop-up dialog box.

2\.  In the Source Data area, select a line dataset or a network dataset which needs processing in topology.


3\.  **Topology Processing**: Before perform the construct region operation, it is recommended to perform topology processing operation for the dataset. Repair the problem line objects (for example, have pseudo, redundant point, overshoots, duplicated lines, not merged adjacent end points) within the tolerance by topology processing. Split the intersection line objects at the intersection point. By topology processing, you can avoid creating redundant objects when constructing regions.

*  The following topology rules are enabled when checked Topology Processing, including clean pseudo nodes, clean redundant vertices, clean duplicate lines, clean overshoots, clean undershoots, merge adjacent ends and intersect arcs. You can select the proper rules according to your needs, for more details about topology rules, please refer to [Topology Rules](TopoProcess.html).

*  Click the Advanced button, the Advanced Settings dialog box appears, you can set the nobreak line and the tolerance.


　　When the Topology Processing is checked, the system will perform the topology processing operation before constructing region, including check and repair the topology error of the selected line dataset.


