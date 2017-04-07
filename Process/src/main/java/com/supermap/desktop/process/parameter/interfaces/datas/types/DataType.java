package com.supermap.desktop.process.parameter.interfaces.datas.types;

/**
 * Created by highsad on 2017/3/30.
 */
public class DataType {
	public final static int DATASET_POINT = 0x01;
	public final static int DATASET_LINE = 0x02;
	public final static int DATASET_REGION = 0x04;
	public final static int DATASET_CAD = 0x08;
	public final static int DATASET_TEXT = 0x10;
	public final static int DATASET_SIMPLE = DATASET_POINT | DATASET_LINE | DATASET_REGION | DATASET_TEXT;
	public final static int DATASET_VECTOR = DATASET_CAD | DATASET_SIMPLE;
	public final static int DATASET_GRID = 0x20;
	public final static int DATASET_IMAGE = 0x40;
	public final static int DATASET = DATASET_VECTOR | DATASET_GRID | DATASET_IMAGE;
	public final static int DATASOURCE = 0x80;
	public final static int WORKSPACE = 0x0100;
}
