---
title: Import Image/Raster Data
---

### Instructions

　　Image data formats supported by SuperMap iDesktop Cross contain: \*.img, \*.tiff, \*.tif, \*.sit, \*.bmp, \*.png, \*.gif, \*.jpg, \*.jpeg, \*.jp2, \*.jpk, \*.sid, \*.ecw.

 Image data formats           | Description             |  Result data types          
 :-------------- | :--------------- | :---------------
 Erdas Image file (\*.img)  | File format using for remote sensing analysis under Erdas platform. | Image, raster
 TIFF image data (\*.tif, \*.tiff)  | TIFF (Tagged Image File Format) | Image, raster
 Bitmap file (\*.bmp)  | Standard image format in Window OS without being compressed | Image, raster
 PNG file (\*.png) | Storage format of network images  | Image, raster
 GIF file (\*.gif) | GIF| Image, raster
 JPG file (\*.jpg; \*.jpeg) | JPG| Image, raster 
 JPEG2000 file (\*.jp2, \*.jpk) | JPEG2000| Image, raster   
 ECW file (\*.ecw) | Erdas ECW | Image, raster     
 SIT image data (\*.sit) | Special storage format of raster and image data owned by SuperMap | Image dataset

　　Supported raster data formats: \*.dem, \*.bil, \*.raw, \*.bsq, \*.bip, \*.b.

 Raster data formats           | Description             |  Result data types             
 :-------------- | :--------------- | :---------------
 Erdas Image file (\*.img)  | File format using for remote sensing analysis under Erdas platform. | Image, raster
 USGDEM and GBDEM raster file (\*.dem) | Exchange formats of spatial data.| Raster dataset    
 BIL file (\*.bil) | BIL | Raster dataset    
 BIP file (\*.bip) | BIP| Raster dataset     
 BSQ file (\*.bsq) | BSQ| Raster dataset    
 RAW file (\*.raw) | RAW| Image dataset   
 MrSID file (\*.sid) | MrSID| Image dataset, raster dataset
 Telecom raster file (\*.b，\*.bin) | Telecom Raster File | Raster dataset  



### Basic steps

　　Takes the parameter settings of importing a CSV file as an example, on the common parameters please refer to [Common Parameter](GeneraParameters.html).

　　1. **Band Import**: When importing multi-bands image data like \*.img、\*.tif、\*.tiff、\*.sid、\*.ecw, you can specify the band mode of result dataset. Three ways are provided including Single Bands, Multi-band and composite band. 

  -  **Single Bands**:  To import a multiple single-band images resulting in a multi-band dataset.
  -  **Multi-band**: To import a multi-band images resulting in a multi-band dataset.
  -  **Composite Band**: To import a multiple single-band images resulting in a single band dataset. The result dataset only has one band. Note: the way only apply to the import of a 8-bit three-bands or four-bands data.

　　2. **Create Image Pyramid**: Checking it to create a image pyramid for the result dataset.

　　3. **Reference File**: When importing tif files, coordinate reference files (\*.tfw) can be imported too.


### Related topics

![](img/smalltitle.png) [AutoCAD data](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis data](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS data](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo data](ImportMapInfo.html)

![](img/smalltitle.png) [Spreadsheet](ImportTable.html)

![](img/smalltitle.png) [3D model data](ImportModel.html)

![](img/smalltitle.png) [Lidar data](ImportLidar.html)

![](img/smalltitle.png) [Google data](ImportKML.html)

![](img/smalltitle.png) [Vector file](ImportVectorFiles.html)

![](img/smalltitle.png) [Import folder](ImportFolder.html)




