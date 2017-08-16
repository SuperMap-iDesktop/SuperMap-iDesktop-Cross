package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.BlockSizeOption;
import com.supermap.data.PixelFormat;
import com.supermap.data.Rectangle2D;

/**
 * Created by yuanR on 2017/8/15 0015.
 * 新建栅格/影像数据集额外的参数信息
 */
public class GridImageExtraDatasetBean {
	// 栅格/影像分块的类型常量
	private BlockSizeOption blockSizeOption;
	// 栅格/影像存储的像素格式
	private PixelFormat pixelFormat;
	// 栅格/影像地理范围
	private Rectangle2D rectangle;
	// 栅格/影像数据集的高宽
	private Integer height;
	private Integer width;

	// 栅格数据集
	private Double noValue;
	private Double maxValue;
	private Double minValue;
	// 影像数据集的波段个数
	private Integer bandCount;
}
