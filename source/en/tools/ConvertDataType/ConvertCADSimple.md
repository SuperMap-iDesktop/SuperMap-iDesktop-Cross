---
title: Conversions between CAD and simple data
---

　　This article describes how to convert between CAD and simple data formats.

## CAD to simple data
  
　　Converting CAD data to simple data means a CAD dataset will be divided into several simple datasets and each kinds of objects will be converted to a simple dataset, such as all polygons in the CAD dataset will be output into a region dataset. For parameterized geometric objects like circles, sectors and rectangles, their parameter information will not be retained in associated simple datasets. These objects will be regarded as polylines with some adjacent vertexes which will be recorded.

### Basic steps  
  
 1. In the tool box, click "Conversion" > "CAD and Simple" > "CAD to Simple", and then in the dialog box specify the CAD dataset you want to convert.
 2. Select the result dataset type and set its name.
### Note

　　Only 2D or 3D point, line, region objects in a CAD dataset can be converted.

## Simple data to CAD

　　Convert several simple datasets into a CAD dataset. Such as, when you express rivers in different levels with lines and regions, you can convert these lines and regions into a CAD dataset.

### Basic steps

 1. In the tool box, click "Conversion" > "CAD and Simple" > "Simple to CAD".
 2. Click the "Add" image button to add several simple datasets.
 3. Specify a datasource to save the result dataset and its name, then click "Run" image button to execute the operation.

## EPS to simple data

　　Points, lines, regions of EPS can be converted into simple points, lines, and regions thereby view spatial information and vertex information of objects.

　　In the tool box, click "Conversion" > "CAD and Simple" > "EPS to Simple", then select EPS data and set the result dataset name and specify a datasource to save it. Finally, click "Run" image button to execute the operation.


