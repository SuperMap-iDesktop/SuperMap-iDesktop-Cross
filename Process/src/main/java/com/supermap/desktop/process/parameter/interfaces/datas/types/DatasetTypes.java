package com.supermap.desktop.process.parameter.interfaces.datas.types;

/**
 * Created by highsad on 2017/4/13.
 */
public final class DatasetTypes extends Type {
	public final static DatasetTypes POINT = new DatasetTypes("point", 0x01);
	public final static DatasetTypes LINE = new DatasetTypes("line", 0x02);
	public final static DatasetTypes REGION = new DatasetTypes("region", 0x04);
	public final static DatasetTypes TEXT = new DatasetTypes("text", 0x08);
	public final static DatasetTypes CAD = new DatasetTypes("cad", 0x10);
	public static final DatasetTypes LINKTABLE = new DatasetTypes("LinkTable", 0x20);
	public static final DatasetTypes NETWORK = new DatasetTypes("network", 0x40);
	public static final DatasetTypes NETWORK3D = new DatasetTypes("network3D", 0x80);
	public static final DatasetTypes LINEM = new DatasetTypes("lineM", 0x0100);
	public static final DatasetTypes PARAMETRICLINE = new DatasetTypes("ParametericLine", 0x0200);
	public static final DatasetTypes PARAMETRICREGION = new DatasetTypes("ParametericRegion", 0x0400);
	public static final DatasetTypes GRIDCOLLECTION = new DatasetTypes("GridCollection", 0x0800);
	public static final DatasetTypes IMAGECOLLECTION = new DatasetTypes("ImageCollection", 0x1000);
	public static final DatasetTypes MODEL = new DatasetTypes("model", 0x2000);
	public static final DatasetTypes TEXTURE = new DatasetTypes("texture", 0x4000);
	public static final DatasetTypes IMAGE = new DatasetTypes("image", 0x8000);
	public static final DatasetTypes WMS = new DatasetTypes("WMS", 0x010000);
	public static final DatasetTypes WCS = new DatasetTypes("WCS", 0x020000);
	public static final DatasetTypes GRID = new DatasetTypes("grid", 0x040000);
	public static final DatasetTypes VOLUME = new DatasetTypes("volume", 0x080000);
	public static final DatasetTypes TOPOLOGY = new DatasetTypes("topology", 0x100000);
	public static final DatasetTypes POINT3D = new DatasetTypes("point3D", 0x200000);
	public static final DatasetTypes LINE3D = new DatasetTypes("line3D", 0x400000);
	public static final DatasetTypes REGION3D = new DatasetTypes("region3D", 0x800000);
	public static final DatasetTypes DEM = new DatasetTypes("DEM", 0x01000000);
	public static final DatasetTypes POINTEPS = new DatasetTypes("pointEPS", 0x20000000);
	public static final DatasetTypes LINEEPS = new DatasetTypes("lineEPS", 0x40000000);
	public static final DatasetTypes REGIONEPS = new DatasetTypes("regionEPS", 0x80000000);
	public static final DatasetTypes TEXTEPS = new DatasetTypes("textEPS", 0x0100000000L);
	public final static DatasetTypes TABULAR = new DatasetTypes("tabular", 0x0200000000L);

	public final static DatasetTypes SIMPLE_VECTOR = new DatasetTypes("SimpleVector",
			POINT.getValue() | LINE.getValue() | REGION.getValue());
	public final static DatasetTypes VECTOR = new DatasetTypes("vector",
			TABULAR.getValue() | SIMPLE_VECTOR.getValue() | TEXT.getValue() | CAD.getValue());

	public final static DatasetTypes DATASET = new DatasetTypes("dataset", VECTOR.getValue() | GRID.getValue() | IMAGE.getValue());

	public DatasetTypes(String name, long value) {
		super(name, value);
	}
}
