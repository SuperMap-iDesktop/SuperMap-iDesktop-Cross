---
title: Append Column
---

　　Append a new field which is from the attribute table of source dataset to the target dataset.

　　During the appending procedure, a pair of connection fields with the same values are required, of them, one is from the source dataset and another one is from the target dataset.


### Basic steps

 1. In the toolbox, click "Data Processing" > "Vector" > "Append Column" to open the "Append Column" dialog box.
 2. Specify the target dataset and the connection field.
 3. Specify the source dataset and the connection field which corresponds with the connection field in target dataset.
 4. Check the fields you want to append to the target dataset, and you can click the name in the New Field column to modify the name of new field. 
 5. Click "Run" image button to perform the operation.

  ![](img/AddtoList.png)

### Note

  - The source dataset must contain one or more fields that the target dataset don't have. 
  - The names of the two connection fields can be different, but the field types must be the same and they contain the same field value. 
  - The system will filter out the fields in the source dataset which are also in the target dataset, the system field and SmUserID field, and other fields will be listed in the Add Field list box. 
  - The connection field type can not be binary. 
  - The user fields must be non-system fields and the SmUserID field. 
  - For SQLPlus datasource, if the target dataset contains data and has created tile index, it is recommended to delete the tile index and create it again for the new dataset after the operation. 


### Related topics

![](img/smalltitle.png) [Dissolve](Datafuse.html)

