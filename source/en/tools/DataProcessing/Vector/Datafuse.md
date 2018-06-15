---
title: Dissolve
---

### Instructions

　　Dissolves objects meeting some conditions to one object. Dissolve is appropriate for the 2D line/region dataset, 3D line dantaset and text dataset.

　　The following conditions are needed for dataset dissolving:

 - The values of a field of objects are the same.
 - The line objects must be have the same endpoints.  
 - The region objects must intersect with each other or be adjacent mutually (have common borders). 

　　The Dissolve feature contains three process ways: Dissolve, Group and Dissolved Combination. Note, only Group is supported to a text dataset.

### Basic steps

 1. In the toolbox, click "Data Processing" > "Vector" > "Dissolve" to open the "Dissolve" dialog box.
 2. Select the dataset to process in the Source Dataset. 
 3. **Dissolve Mode**: Following three modes are provided. **Note**: Only the "Group" mode is worked for a text dataset.
 
  - **Dissolve**: Dissolves the objects which have the same attribute fields and intersects with each other (or the distance between them are within the tolerance) to one object. 

 ![](img/DatafusePolygon.png)

   - **Group**: Groups the objects with the same attribute fields to one object, and deletes the overlaps. 

  ![](img/Datagroup.png)

   - **Dissolved Combination**: Dissolves the objects which have the same attribute fields and intersect or are tangent with each other to one object, if the dissolving fields of objects are the same after dissolving, groups the objects to a complex object. 

 4.**Dissolve Tolerance**: If the distance between two nodes is less than or equal the value, the nodes will be merged to one node. The default value is one millionth of the dataset border (the maximum tolerance is 100 times of the default tolerance), the unit is the original unit of the dataset. **Note**: Setting the value for a text dataset is meaningless.
 5.**Filter Expression**: Only the objects meeting this condition will be used in the calculation.
 6.Process objects that the values of dissolved fields are null: If this check box is checked, the objects that the dissolved filed as null will be used in the dissolving operation.
 7.Result Data: Name and save the result dataset and select a datasource to save it.
 8.Dissolve Field: The fields that have the same value. Dissolves or groups objects according to this field value.
 9.Statistics Field: Select a statistic method and create a new field to save results, the statistic method can be Max, Min, Sum, Average, First Object and Last Object.
    - Max: The maximum value of field of dissolve/group objects, only available for numeric and time fields. 
    - Min: The minimum value of field of dissolve/group objects, only available for numeric and time fields. 
    - Sum: The sum of field of dissolve/group objects, only available for numeric fields.  
    - Average: The average of field of dissolve/group objects, only available for numeric fields. 
    - First: The field value of the object with the minimum SmID in the dissolve/group objects. 
    - Last:  The field value of the object with the maximum SmID in the dissolve/group objects. 

 10.Set a name for result dataset and specify a datasource to save the result. Click "Run" image button to perform the operation. As the following picture shows: object 1, 2, 3 are dissolved to a line object, and object 4, 5 are dissolved another line object.

  ![](img/DatafuseResult.png)

### Relate topics

![](img/smalltitle.png) [Integrate](Integrate.html)



