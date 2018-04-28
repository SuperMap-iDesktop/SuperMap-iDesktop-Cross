---
title: Dataset Encoding Types 
---
  
Setting proper encoding modes for GIS data using in different applications is very important to improve the efficiency of the system and saving storage space. 
  
Encoding|Description  
-|-
None|No encoding mode is employed.   
SGL|SGL (SuperMap Grid LZW) is defined by SuperMap. SGL is a modified encoding mode based on LZW and has higher efficiently. As a lossless encoding mode, the SGL is extensively used for the compressed storage of Grid and DEM datasets by SuperMap.
DCT|DCT (Discrete Cosine Transform) is a widely used transform in data compression. Providing a balance between high performance and complexity, the DCT has become a most widely used compression encoding mode all over the world. The DCT decorrelates the image data through the discrete cosine transform, representing the information more compactly.  The DCT is a lossy encoding mode with high compression ratio and performance. As a lossy encoding mode, DCT is applicable to image datasets because image datasets are seldom used for accurate analysis.
LZW|LZW ( Lempel&ndash;Ziv&ndash;Welch) is a widely used dictionary compression method. LZW uses a code to represent a string. Therefore, you can compress not only duplicate data, but also non-duplicate data. The LZW is a lossless compression encoding mode applicable to raster and image datasets.
PNG|PNG is a lossless compression encoding mode applicable to image datasets.
JPEG|JPEG is a lossy mode of operation, in the case of no difference in visual effects, the compression ratio can reach 1/20 to 1/40, the degree of compression is higher, suitable for the background image as the image data.
Four-byte|4 bytes are used to store a coordinate value.  This encoding mode is applicable to vector data, with 2D point, tabular, CAD datasets and 3D vector dataset excluded.
Three-byte|3 bytes are used to store a coordinate value.  This encoding mode is applicable to vector data, with 2D point, tabular, CAD datasets and 3D vector dataset excluded.  
Double-byte|Two bytes are used to store a coordinate value.  This encoding mode is applicable to vector data, with 2D point, tabular, CAD datasets and 3D vector dataset excluded.  
Single-byte|A single byte is used to store a coordinate value.  This encoding mode is applicable to vector data, with 2D point, tabular, CAD datasets and 3D vector dataset excluded.  

The encoding modes of different types of datasets are shown in the table bellow:

Dataset Type|Encoding  
:-:|:-:
Vector dataset|Single-byte, Double-byte, Three-byte and 4 byte
DEM/Grid dataset|SGL, LZW
Image dataset|LZW, DCT, PNG




    
