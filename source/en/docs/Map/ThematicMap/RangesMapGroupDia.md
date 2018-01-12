---
title: Modify Ranges Map
---

　　Right-click a ranges map in the Layer Manager and click "Modify Thematic Map...", then all setting information is shown in the pop-up "Thematic Mapping" panel. The settings for thematic maps which were created based on different types of vector layers are different.


- **Method:** You can use the following classification methods to classify your quantitative data. You can click the Method drop-down arrow to choose a method.

  -   **Equal Interval:** This classification method allows you to specify the number, then divides the range of attribute values into equal-sized sub-ranges (classes).
  -   **Square Root Interval:** This classification method first divides the range of the square roots of all attribute values into equal-sized classes and get data-breaks, then calculates the squares of the data-breaks to get the thematic-breaks to classify the attribute values.
  -   **Standard Deviation Interval:** This classification method shows you how much an object's attribute value varies from the mean. First, the mean value and the standard deviation are calculated. Class ranges are then created using these values. The length of each range is one standard deviation and the range at the middle is 0.5 standard deviation above or below the mean. With this method, the application calculates the number of classes.
  -   **Logarithmic Interval:** This classification method first divides the range of the logarithms of all attribute values to base 10 into equal-sized sub-ranges, then calculates the exponents of all breaks and uses the exponents to classify the attribute values.
  -   **Quantile Interval:** Each class contains an equal number of objects. A quantile classification is well suited to linearly distributed data. Because objects are grouped by the number in each class, the resulting map can be misleading. Similar objects can be placed in adjacent classes, or objects with widely different values can be put in the same class. You can minimize this distortion by increasing the number of classes.
  -   **Custom Interval:** This classification method allows you to specify an interval by which to equally divide a range of attribute values. Rather than specifying the number of intervals as in the equal interval classification method, with this method, you specify the interval value. SuperMap Desktop automatically determines the number of classes based on the interval.

- **Range Count:** How many sub-ranges (classes) you want to classify the range of attribute values into.

- **Interval:** The interval by which to equally divide a range of attribute values. This item is only active if you select the custom interval classification method to classify the attribute values.

- **Caption Format:** The format of the caption expressing the value field of each sub-range.



- **Union or Spllit:** Merge and split sub-ranges according to your needs.

  -   Union: ![](img/mergeButton.png) button is used to select multiple continuous ranges (can be used together with Ctrl or Shift) into a range. If there is only one selected range or the selected ranges are not continuous, this button is not available.
  -   Split: ![](img/splitButton.png) button is used to split one range into two.

- **Visible:**

  -   Click ![](img/seeButton.png) button to show or hide the objects that thematic values are within the selected sub-ranges.
  -   You can also click the icon ![](img/see.png) to control the visibility of the corresponding objects.

- **Style:**

　　The image button "Style" is used to set display styles of objects that attribute values are within corresponding range. Also you can click the style of the related item in the list to change corresponding objects' styles. For different kinds of objects, the functions for setting style are different.

　　You can choose one or more items from the list by holding Shift or Ctrl key and then click "Style" image button to view the "Modify Style in Bulk" dialog box. In the dialog box, you can modify the styles for the selected items. For details operation, please refer to [Modify Thematic Map](CreatThematicMap.html).

- **Ranges:** The end value of a sub-range. You can directly type a value and press Enter to see the refreshed ranges map.

- **Caption:**  The Caption is used to display and set title of each range.

