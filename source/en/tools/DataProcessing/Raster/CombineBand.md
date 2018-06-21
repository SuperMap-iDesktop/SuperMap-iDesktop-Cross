---
title: Image Composite
---
  
### Instructions  

　　Combine three single-band images into a RGB image. You can select optimally composite bands to improve the display speed and accuracy of images. According to the theory of additive color and given three single band images make up a colorized image.

　　The highlighted features in the result image are different by combining different bands. Take the 7 bands of the TM image as an example:

 -  **321 band**: that is true color synthesis, the band 3, 2, and 1 are given red, green, blue, to obtain a natural color composite image. The color of the image is consistent with the actual scene. It is suitable for shallow water exploration and mapping, at the same time, it is suitable for non remote sensing application professionals. 
 -  **432 band**: standard false color synthesis, the band 4, 3, 2 are given red, green, blue, and the vegetation appears red, so that it can highlight the characteristics of vegetation, often used in vegetation information extraction. It is the most commonly used band combination in vegetation, crop, land use and wetland analysis. 
 -  **453 band**: the most informative combination. In the TM data, the band 5 is the most informative. The band 4, 5, 3 are given red, green and blue to make the color contrast is very obvious, and the color display rule of various types of features is similar to the conventional synthesis image, commonly used in visual interpretation, but also applied to determine the boundary of the land and water. 
 -  **741 band**: the band combination image is compatible with middle infrared, near infrared and visible spectral band information, and the color information is rich. It has abundant geological information and earth environmental information, and there is no much interference information, geological interpretation level is high, it can display various structural features (folds and faults) clearly, and it can display different types of rocks with clear boundary. 

### Basic steps

1. In the tool box, click Data Processing > Raster > Image Composite to open the dialog box "Image Composite".
2. Specify each band dataset:
  
  - Red Band: Specify a dataset which will be loaded through the red channel and rendered with red as the base color.  
  - Green Band: Specify a dataset which will be loaded through the green channel and rendered with green as the base color.  
  - Blue Band: Specify a dataset which will be loaded through the blue channel and rendered with blue as the base color.  
        
3. Set the result dataset name and specify a datasource to save the dataset. Click "Run" image button to execute the operation. Following illustrates a standard false color created by 432 bands.
 
　　　　 ![](img/CombineBandResult.png) 



