package com.supermap.desktop.process.parameter.interfaces.datas.types;

/**
 * Created by highsad on 2017/4/13.
 */
public final class DatasetTypes extends Type {
	public final static DatasetTypes TABULAR = new DatasetTypes("tabular", 0x08);
	public final static DatasetTypes POINT = new DatasetTypes("point", 0x01);
	public final static DatasetTypes LINE = new DatasetTypes("line", 0x02);
	public final static DatasetTypes REGION = new DatasetTypes("region", 0x02);
	public final static DatasetTypes TEXT = new DatasetTypes("text", 0x04);
	public final static DatasetTypes CAD = new DatasetTypes("cad", 0x08);
	public final static DatasetTypes GRID = new DatasetTypes("grid", 0x10);
	public final static DatasetTypes IMAGE = new DatasetTypes("image", 0x20);

	public final static DatasetTypes SIMPLE_VECTOR = new DatasetTypes("SimpleVector",
			POINT.getValue() | LINE.getValue() | REGION.getValue());
	public final static DatasetTypes VECTOR = new DatasetTypes("vector",
			TABULAR.getValue() | SIMPLE_VECTOR.getValue() | TEXT.getValue() | CAD.getValue());

	public final static DatasetTypes DATASET = new DatasetTypes("dataset", VECTOR.getValue() | GRID.getValue() | IMAGE.getValue());

	public DatasetTypes(String name, int value) {
		super(name, value);
	}
}
