title: Clip
---
   
### Instructions    
 
Clip is a process of extracting a set of features from a clipped dataset using a clip dataset. The set of polygons in the clip dataset define the clipping region. The features or feature parts that fall outside of these polygons are to be erased, and the ones that fall within the polygons will be output to the result dataset. The dataset which will be used to clip another dataset must be a region dataset, however the dataset which will be clipped can be a point dataset, a line dataset or a region dataset.

![](img/clipbuttonoperation.png)  
  
The attribute table of result dataset comes from the attribute table of the dataset clipped, and the two attribute table have the same table structure. Only fields such as area, perimeter, and length need to be recalculated. All the other field values in the result are the same with the dataset clipped.
  
![](img/clipbuttonproperty.png)  

### Basic steps   
  
1. In the Toolbox, click "Vector Analysis" > "Overlay Analysis" > "Clip" to open the dialog box "Clip".  
2. Specify the source dataset which will be clipped. 
3. Specify the overlay dataset which will be used to clipping the source dataset.
4. Set up the result dataset's name and the datasource where the result dataset will be saved in.
5. **Tolerance**: After an overlay analysis, if the distance between two nodes is less than this value, they will be merged. The default value is the node tolerance value of the clipped dataset (this value is set in the node tolerance item in the dataset tolerance setting under the Vector Dataset tab in the Dataset Attributes dialog box). If no tolerance value is set in the clipped dataset, the default tolerance value here is related to the dataset's coordinate system.
6. Click the Run image button to start the Clip operation.



