---
title: Basic Statistics
---

### Instructions

　　Calculates the basic statistics information of cell based on the input raster data, and show the statistics result and gray information with histogram visually. The contents include: maximum value, minimum value, mean value, standard deviation and variance.
### Basic steps


 1. Two function entrances are provided:
 - Under the "Spatial Analysis" tab on the "Raster Analysis" group, click "Raster Statistics" then select "Basic Statistics".
  - In the toolbox, click "Raster Analysis" > "Raster Statistics" > "Basic Statistics" or drag the item into the model panel.
 2. Specify the raster data for statistics.   
 3. Contents of basic statistics will be shown in the "Statistics Result" area including: 

  - **Maximum**: Finds out the maximum value.
  - **Minimum**: Finds out the minimum value.
  - **Mean**: Count the average value of all cell values.
  - **Standard Deviation**: Calculate the standard deviation of all the cells in the raster data. Standard deviation is the mean value of the differences between the data and the mean value, and it reflects the dispersion of the data. Standard deviation is the square root of variance. As shown below, x1,x2,x3,......xn is a group of sample data, µ is the mean value of them. The formula to calculate is as the following:  
  　　![](img/StandardDev.png)  
  - **Variance**: Calculate the variance of all the cells. Variance is the sum of all the squares of the difference between the cell values and the mean value. 

  4.**Whether to generate a histogram**: Check the box to generate a histogram to show the frequency distribution of raster values in raster data. Each column represents the relative frequency in particular area or a particular group. The abscissa axis is groups of value ranges and the vertical axis represents number of cells falling into groups.

  - **Range Count**: Enter a integer as the number of ranges.
  - **Function**: It is the function to convert the field value. The system provides NONE (null function which is not to convert the values), LOG (logarithmic function) and ARCSIN (arc-sin function). When the sample points are interpolated by the transform functions, some interpolation methods assume that the data are normally distributed. If the sample points do not follow a normal distribution, you need to transform the data to make the data normally distributed. The three functions are illustrated as follows:     
    - **None**: The values uses the raw data to interpolate without any conversion.
    - **Log**: Logarithmic function is used to convert the raw data using the natural logarithm as the bottom of the logarithm transform. The logarithm transform is applicable to the data which are positively skewed and whose peak value is in the left of the plot (i.e., the sample point whose median is greater than the middle value). The domain of the logarithmic function is the real number greater than 0, so you need to ensure it meets the condition during the logarithm transform. 
    - **Arcsin**: Arc-sin function is to perform the arc-sin transform for the raw data. It is applicable to the data that is proportional or distributed by percentage. The domain of the function is [-1,1], so you need to pay attention on the value range of the statistical field during the arc-sin transform. 

    
  5. Click "Run" button to execute the operation.
　　![](img/BasicResult.png)

### Related topics  

![](img/smalltitle.png) [Common statistics](CommonStatistic.html)  
![](img/smalltitle.png) [Neighbor statistics](NeighbourStatistic.html)    
![](img/smalltitle.png) [Zonal statistics](ZonalStatistic.html) 
![](img/smalltitle.png) [Elevation statistics](AltitudeStatistic.html)    



