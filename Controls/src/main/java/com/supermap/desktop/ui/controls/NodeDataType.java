package com.supermap.desktop.ui.controls;

import com.supermap.data.Enum;

/**
 * 节点类型枚举类型
 *
 * @author xuzw
 */
public class NodeDataType extends Enum {
    private NodeDataType(int value, int ugcValue) {
        super(value, ugcValue);
    }

    public static final NodeDataType UNKNOWN = new NodeDataType(-1, -1);

    public static final NodeDataType WORKSPACE = new NodeDataType(1001, 1001);

    public static final NodeDataType DATASOURCES = new NodeDataType(1002, 1002);

    public static final NodeDataType DATASOURCE = new NodeDataType(1003, 1003);

    public static final NodeDataType DATASET_VECTOR = new NodeDataType(1004, 1004);

    public static final NodeDataType DATASET_IMAGE = new NodeDataType(1005, 1005);

    public static final NodeDataType DATASET_GRID = new NodeDataType(1006, 1006);

    public static final NodeDataType DATASET_TOPOLOGY = new NodeDataType(1007, 1007);

    public static final NodeDataType DATASET_VOLUME = new NodeDataType(1008, 1008);

    public static final NodeDataType TOPOLOGY_DATASET_RELATIONS = new NodeDataType(1009, 1009);

    public static final NodeDataType TOPOLOGY_ERROR_DATASETS = new NodeDataType(1010, 1010);

    public static final NodeDataType MAP_NAME = new NodeDataType(1011, 1011);

    public static final NodeDataType SCENE_NAME = new NodeDataType(1012, 1012);

    public static final NodeDataType LAYOUT_NAME = new NodeDataType(1013, 1013);

    public static final NodeDataType MAPS = new NodeDataType(1014, 1014);

    public static final NodeDataType SCENES = new NodeDataType(1015, 1015);

	public static final NodeDataType LAYOUTS = new NodeDataType(1016, 1016);

	public static final NodeDataType RESOURCES = new NodeDataType(1017, 1017);

    public static final NodeDataType SYMBOL_MARKER_LIBRARY = new NodeDataType(1018, 1018);

    public static final NodeDataType SYMBOL_LINE_LIBRARY = new NodeDataType(1019, 1019);

    public static final NodeDataType SYMBOL_FILL_LIBRARY = new NodeDataType(1020, 1020);

    public static final NodeDataType DATASET_RELATION_SHIP = new NodeDataType(1021, 1021);

	public static final NodeDataType WORKFLOWS = new NodeDataType(1022, 1022);

	public static final NodeDataType WORKFLOW = new NodeDataType(1023, 1023);

    public static final NodeDataType DATASET_VECTOR_ITEM = new NodeDataType(1024, 1024);


	public static final NodeDataType SCREEN_LAYER3D = new NodeDataType(2001, 2001);

    public static final NodeDataType TERRAIN_LAYERS = new NodeDataType(2002, 2002);

    public static final NodeDataType LAYER3D_DATASET = new NodeDataType(2003, 2003);

    public static final NodeDataType LAYER3D_KML = new NodeDataType(2004, 2004);

    public static final NodeDataType LAYER3D_MODEL = new NodeDataType(2005, 2005);

    public static final NodeDataType LAYER3D_MAP = new NodeDataType(2006, 2006);

    public static final NodeDataType TERRAIN_LAYER = new NodeDataType(2007, 2007);

    public static final NodeDataType LAYER3D_IMAGE_FILE = new NodeDataType(2008, 2008);

    public static final NodeDataType LAYER3D_VECTOR_FILE = new NodeDataType(2009, 2009);

    public static final NodeDataType FEATURE3DS = new NodeDataType(2010, 2010);

    public static final NodeDataType FEATURE3D = new NodeDataType(2011, 2011);

    public static final NodeDataType LAYER3DS = new NodeDataType(2012, 2012);

    public static final NodeDataType THEME3D_RANGE_ITEM = new NodeDataType(2013, 2013);

    public static final NodeDataType THEME3D_UNIQUE_ITEM = new NodeDataType(2014, 2014);

    public static final NodeDataType SCREENLAYER3D_GEOMETRY_TAG = new NodeDataType(2015, 2015);

    public static final NodeDataType LAYER = new NodeDataType(3001, 3001);

    public static final NodeDataType THEME_GRAPH_ITEM = new NodeDataType(3003, 3003);

    public static final NodeDataType THEME_GRID_RANGE_ITEM = new NodeDataType(3004, 3004);

    public static final NodeDataType THEME_GRID_UNIQUE_ITEM = new NodeDataType(3005, 3005);

    public static final NodeDataType THEME_LABEL_ITEM = new NodeDataType(3006, 3006);
    public static final NodeDataType THEME_RANGE_ITEM = new NodeDataType(3007, 3007);
    public static final NodeDataType THEME_UNIQUE_ITEM = new NodeDataType(3008, 3008);

    public static final NodeDataType WMSSUB_LAYER = new NodeDataType(3009, 3009);

    // add by xuzw 2010-09-28
    public static final NodeDataType LAYER_GRID = new NodeDataType(3010, 3010);
    public static final NodeDataType LAYER_IMAGE = new NodeDataType(3011, 3011);
    public static final NodeDataType LAYER_THEME = new NodeDataType(3012, 3012);

    // add by gouyu 2010-12-22
    public static final NodeDataType THEME_RANGE = new NodeDataType(3013, 3013);
    public static final NodeDataType THEME_UNIQUE = new NodeDataType(3014, 3014);
    public static final NodeDataType THEME_CUSTOM = new NodeDataType(3015, 3015);

    // add by gouyu 2012-12-24
    public static final NodeDataType DATASET_IMAGE_COLLECTION = new NodeDataType(3016, 3016);
    public static final NodeDataType DATASET_GRID_COLLECTION = new NodeDataType(3017, 3017);
    public static final NodeDataType DATASET_IMAGE_COLLECTION_ITEM = new NodeDataType(3018, 3018);
    public static final NodeDataType DATASET_GRID_COLLECTION_ITEM = new NodeDataType(3019, 3019);

    // add by gouyu 2014-7-23
    public static final NodeDataType LAYER_GROUP = new NodeDataType(3020, 3020);
    public static final NodeDataType Layer3D_OSGBFile = new NodeDataType(3021, 3021);
    public static final NodeDataType LAYER_WMS = new NodeDataType(3022, 3022);
}
