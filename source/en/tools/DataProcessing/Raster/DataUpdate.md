---
title: Data Update
---
  
### Instructions  

　　Update contents of your raster dataset through other raster dataset(s). The following pictures shows an raster dataset updated.
 　　 ![](img/Append.png)   
   
Following requirements must be met to update a raster dataset successfully:
  
- The pixel formats of the target dataset which will be updated and the update dataset(s) which will be used to update the target dataset must be same. 
- There must be some overlaps between the target dataset and the update dataset, then the target dataset can be updated.
### Basic steps

1. In the toolbox, click "Data Processing" > "Raster" > "Data Update" to open the "Data Update" dialog box.
2. Specify the raster dataset you want to update.
3. In the "Source Dataset" area, you can add one or more datasets as the update datasets   
4. Click "Run" image button to execute the operation.

**Note**  
  
When update image data, if the encode mode of the source data used is DCT, the result will has deckle edge. It is because the DCT encode type is a lossy compression, and the boundary value of the image will be changed after the encoding. For the introduction of dataset encoding, see Encoding Modes for Dataset Compression. When using the data updating functionality, it is not recommended to use the image in DCT encoding, to avoid the deckle edge problem.
















