package com.supermap.desktop.process.meta;

/**
 * Created by highsad on 2017/1/5.
 */
public class MetaKeys {
	public static final String SPATIAL_INDEX = "SpatialIndex";
	public static final String BUFFER = "Buffer";
	public static final String IMPORT = "Import";
	public static final String PROJECTION = "Projection";
	public static final String SET_PROJECTION = "SetProjection";
	public static final String EXPORTGRID = "ExportGrid";
	public static final String EXPORTVECTOR = "ExportVector";

	public static final String GRIDREGION_AGGREGATION = "GridRegionAggregation";
	public static final String POLYGON_AGGREGATION = "PolygonAggregation";
	public static final String OVERLAY_ANALYST_CLIP = "OverlayAnalyst_CLIP";
	public static final String OVERLAY_ANALYST_UNION = "OverlayAnalyst_UNION";
	public static final String OVERLAY_ANALYST_ERASE = "OverlayAnalyst_ERASE";
	public static final String OVERLAY_ANALYST_IDENTITY = "OverlayAnalyst_IDENTITY";
	public static final String OVERLAY_ANALYST_INTERSECT = "OverlayAnalyst_INTERSECT";
	public static final String OVERLAY_ANALYST_UPDATE = "OverlayAnalyst_UPDATE";
	public static final String OVERLAY_ANALYST_XOR = "OverlayAnalyst_XOR";
	public static final String INTERPOLATOR_IDW = "Interpolator_IDW";
	public static final String INTERPOLATOR_RBF = "Interpolator_RBF";
	public static final String INTERPOLATOR_UniversalKRIGING = "Interpolator_UniversalKRIGING";
	public static final String INTERPOLATOR_SimpleKRIGING = "Interpolator_SimpleKRIGING";
	public static final String INTERPOLATOR_KRIGING = "Interpolator_KRIGING";
	public static final String ISOLINE = "ISOLine";
	public static final String ISOREGION = "ISORegion";
	public static final String ISOPOINT = "ISOPoint";
	public static final String DEMBUILD = "DEMBuild";
	public static final String DEMLAKE = "DEMLake";
	public static final String VECTORTOGRID = "VectorToGrid";
	public static final String GRIDTOVECTOR = "GridToVector";
	public static final String COMPUTEDISTANCE = "ComputeDistance";
	public static final String THIESSENPOLYGON = "ThiessenPolygon";
	public static final String THINRASTER = "ThinRaster";
	public static final String PROCESS_GROUP = "ProcessGroup";
	public static final String SQL_QUERY = "SqlQuery";
	public static final String HYDROLOGICAL_ANALYSIS = "HYDROLOGICAL_ANALYSIS";
	public static final String EMPTY = "empty";

	// 水文分析
	public static final String FillingPseudoDepressions = "FillingPseudoDepressions";
	public static final String CalculatedFlowDirection = "CalculatedFlowDirection";
	public static final String ComputationalFlowLength = "ComputationalFlowLength";
	public static final String CalculatedWaterFlow = "CalculatedWaterFlow";
	public static final String CatchmentPointCalculation = "CatchmentPointCalculation";
	public static final String WatershedSegmentation = "WatershedSegmentation";
	public static final String WatershedBasin = "WatershedBasin";
	public static final String ExtractingGridWaterSystem = "ExtractingGridWaterSystem";
	public static final String RiverClassification = "RiverClassification";
	public static final String DrainageVectorization = "DrainageVectorization";
	public static final String ConnectedDrainage = "ConnectedDrainage";

	// 空间统计分析
	public static final String CENTRAL_ELEMENT = "CENTRAL_ELEMENT";
	public static final String MEAN_CENTER = "MEAN_CENTER";
	public static final String MEDIAN_CENTER = "MEDIAN_CENTER";
	public static final String DIRECTIONAL = "DIRECTIONAL";
	public static final String LINEAR_DIRECTIONAL_MEAN = "LINEAR_DIRECTIONAL_MEAN";
	public static final String STANDARD_DISTANCE = "STANDARD_DISTANCE";
	public static final String AUTO_CORRELATION = "AUTO_CORRELATION";
	public static final String HIGH_OR_LOW_CLUSTERING = "HIGH_OR_LOW_CLUSTERING";
	public static final String CLUSTER_OUTLIER_ANALYST = "CLUSTER_OUTLIER_ANALYST";
	public static final String HOT_SPOT_ANALYST = "HOT_SPOT_ANALYST";
	public static final String GEOGRAPHIC_WEIGHTED_REGRESSION = "GEOGRAPHIC_WEIGHTED_REGRESSION";
	public static final String INCREMENTAL_AUTO_CORRELATION = "INCREMENTAL_AUTO_CORRELATION";
	public static final String AVERAGE_NEAREST_NEIGHBOR = "averageNearestNeighbor";
	public static final String OPTIMIZED_HOT_SPOT_ANALYST = "OPTIMIZED_HOT_SPOT_ANALYST";

	//数据处理
	public static final String AGGREGATE_POINTS = "AggregatePoints";
	public static final String VECTOR_RESAMPLE = "VectorResample";
	public static final String EDGE_MATCH = "EdgeMatch";
	public static final String LINE_POLYGON_SMOOTH = "LinePolygonSmooth";
	public static final String PICKUP_BORDER ="PickupBorder";
	public static final String DUALLINE_TO_CENTERLINE ="DualLineToCenterLine";
	public static final String REGION_TO_CENTERLINE ="RegionToCenterLine";
	public static final String REGION_TRUNK_TO_CENTERLINE ="RegionTrunkToCenterLine";
	public static final String RAREFY_POINTS ="RarefyPoints";

	//类型转换
	public static final String CONVERSION_POINT_TO_LINE = "CONVERSION_POINT_TO_LINE";
	public static final String CONVERSION_LINE_TO_POINT = "CONVERSION_LINE_TO_POINT";
	public static final String CONVERSION_LINE_TO_REGION = "CONVERSION_LINE_TO_REGION";
	public static final String CONVERSION_REGION_TO_LINE = "CONVERSION_REGION_TO_LINE";
	public static final String CONVERSION_REGION_TO_POINT = "CONVERSION_REGION_TO_POINT";
	public static final String CONVERSION_CAD_TO_SIMPLE = "CONVERSION_CAD_TO_SIMPLE";
	public static final String CONVERSION_CAD_TO_MODEL = "CONVERSION_CAD_TO_MODEL";
	public static final String CONVERSION_SIMPLE_TO_CAD = "CONVERSION_SIMPLE_TO_CAD";
	public static final String CONVERSION_EPS_TO_SIMPLE = "CONVERSION_EPS_TO_SIMPLE";
	public static final String CONVERSION_FIELD_TO_TEXT = "CONVERSION_FIELD_TO_TEXT";
	public static final String CONVERSION_TEXT_TO_FILED = "CONVERSION_TEXT_TO_FILED";
	public static final String CONVERSION_TEXT_TO_POINT = "CONVERSION_TEXT_TO_POINT";
	public static final String CONVERSION_TABULAR_TO_POINT = "CONVERSION_TABULAR_TO_POINT";
	public static final String CONVERSION_TABULARPOINT_TO_REGION = "CONVERSION_TABULARPOINT_TO_REGION";

	// 大数据
	public static final String HEAT_MAP = "HeatMap";
	public static final String SIMPLE_DENSITY = "SimpleDensity";
	public static final String KERNEL_DENSITY = "KernelDensity";
	public static final String OVERLAYANALYSTGEO = "overlayanalystgeo";
	public static final String SINGLE_QUERY = "singleQuery";

	public static final String USER_DEFINE_PROJECTION = "UserDefineProjection";
}
