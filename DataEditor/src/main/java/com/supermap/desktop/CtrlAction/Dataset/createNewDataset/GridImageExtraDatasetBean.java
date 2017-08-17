package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.BlockSizeOption;
import com.supermap.data.PixelFormat;
import com.supermap.data.Rectangle2D;

/**
 * Created by yuanR on 2017/8/15 0015.
 * 新建栅格/影像数据集额外的参数信息
 */
public class GridImageExtraDatasetBean {
	public void setBlockSizeOption(BlockSizeOption blockSizeOption) {
		this.blockSizeOption = blockSizeOption;
	}

	public void setPixelFormatImage(PixelFormat pixelFormatImage) {
		this.pixelFormatImage = pixelFormatImage;
	}

	public void setPixelFormatGrid(PixelFormat pixelFormatGrid) {
		this.pixelFormatGrid = pixelFormatGrid;
	}

	public void setRectangle(Rectangle2D rectangle) {
		this.rectangle = rectangle;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setBandCount(Integer bandCount) {
		this.bandCount = bandCount;
	}

	public void setNoValue(Double noValue) {
		this.noValue = noValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public BlockSizeOption getBlockSizeOption() {
		return blockSizeOption;
	}

	public PixelFormat getPixelFormatImage() {
		return pixelFormatImage;
	}

	public PixelFormat getPixelFormatGrid() {
		return pixelFormatGrid;
	}

	public Rectangle2D getRectangle() {
		return rectangle;
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getBandCount() {
		return bandCount;
	}

	public Double getNoValue() {
		return noValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	// 栅格/影像分块的类型常量
	private BlockSizeOption blockSizeOption;
	// 栅格/影像存储的像素格式
	private PixelFormat pixelFormatImage;
	private PixelFormat pixelFormatGrid;
	// 栅格/影像地理范围
	private Rectangle2D rectangle;
	// 栅格/影像数据集的高宽
	private Integer height;
	private Integer width;

	//栅格
	private Double noValue;
	private Double maxValue;
	private Double minValue;

	// 影像数据集的波段个数
	private Integer bandCount;

	public GridImageExtraDatasetBean() {
		this.blockSizeOption = BlockSizeOption.BS_64;
		this.pixelFormatImage = PixelFormat.RGBA;
		this.pixelFormatGrid = PixelFormat.DOUBLE;
		this.rectangle = new Rectangle2D(-200, -200, 200, 200);
		this.height = 800;
		this.width = 800;

		this.noValue = -9999.0;
		this.maxValue = 10000.0;
		this.minValue = -1000.0;

		this.bandCount = 1;
	}
}
