---
title: Validate Line Topology
---

### ![](img/read.gif)Instructions

　　Validate and repair line dataset and network dataset in topology.

### ![](img/read.gif)Basic Steps

1\.  Click "Data" > "Topology" > "Validate Line Topology", and then set related parameters in the pop-up dialog box.

2\.  Select the needed line dataset or network dataset.

3\.  **Topology Error Handling Options**

　　Including 7 rules: clean pseudo nodes, clean redundant vertices, clean duplicate lines, clean overshoots, clean undershoots, merge adjacent ends and intersect arcs. you can select the proper rules according to your needs. For more details about the topology processing rules, please refer to [Topology Processing Rules](TopoProcess.html). When perform topology processing, the system will validate the line dataset with the selected topology rules and repair the topology errors.

4\. Click "Advanced" button, the "Advanced Settings" dialog box appears, you can set the nobreak line and the tolerance.


-   **Intersect Arcs**: set nobreak line parameter, the lines meet the parameters will not be broken.
    -   NoBreak Object: After setting the filter expression, the system will not break the line objects that meet the expression. Click the "..." button on the right, the SQL expression dialog box appears, you can enter an expression in the dialog box.
    -   NoBreak Position: Choose the point dataset from the dropdown list appeared by clicking the dropdown arrow to determine the NoBreak location. Whether a line object will be broken by checking whether the distance between a point object from the selected point dataset and its adjacent line object is within the tolerance.

　　If you do not set NoBreak Object, all the line objects will be used in the intersect arcs operation; if you do not set NoBreak Position, all the line objects will be used in the intersect arcs operation; when both of them are set, the system will process the union.

-   **Tolerance Settings**: Overshoot Tolerance is used for cleaning overshoots, Undershoot Tolerance is used for cleaning undershoot and Fuzzy is used for other topology processing rules that need to check the distance between nodes. The default value for Fuzzy is related to the coordiante system of the dataset; But for the overshoot and undershoot tolerance, the default value is the tolerance of the dataset, if a tolerance was not set for a dataset, the default value is 10 times Fuzzy value.

　　If the topology error options are not list, the system will skip the error without any operations.
    

### Note

* To perform Clean Overshoots for line data, you must check the "Intersect Arcs" option, to ensure the accuracy of the operation.



