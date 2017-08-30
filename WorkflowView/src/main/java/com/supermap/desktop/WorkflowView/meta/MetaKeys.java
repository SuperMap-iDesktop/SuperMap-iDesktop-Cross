package com.supermap.desktop.WorkflowView.meta;

/**
 * Created by highsad on 2017/1/5.
 */
public class MetaKeys {
	public static final String SPATIAL_INDEX = "SpatialIndex";
	public static final String BUFFER = "Buffer";
	public static final String MULTIBUFFER = "MultiBuffer";
	public static final String IMPORT = "Import";
	public static final String PROJECTION = "Projection";
	public static final String SET_PROJECTION = "SetProjection";
	public static final String EXPORTGRID = "ExportGrid";
	public static final String EXPORTVECTOR = "ExportVector";
	public static final String FIELD_INDEX = "FieldIndex";

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
	public static final String HYDROLOGICAL_ANALYST = "HydrologicalAnalyst";
	public static final String EMPTY = "Empty";
	public static final String COST_PATH_LINE = "CostPathLine";
	public static final String ELIMINATE = "Eliminate";
	public static final String SIMPLE_DENSITY_OFFLINE = "SimpleDensityOffline";
	public static final String KERNEL_DENSITY_OFFLINE = "KernelDensityOffline";

	//栅格统计
	public static final String GRID_ANALYST_SETTING = "GridAnalystSetting";
	public static final String BASIC_STATISTIC = "BasicStatistics";
	public static final String COMMON_STATISTIC = "CommonStatistics";
	public static final String NEIGHBOUR_STATISTIC = "NeighbourStatistics";
	public static final String ZONAL_STATISTICS_ON_RASTER_VALUE = "ZonalStatisticsOnRasterValue";
	public static final String HEIGHT_VALUE_STATISTIC = "HeightValueStatistics";

	// 距离栅格
	public static final String SHORTEST_PATH = "ShortestPath";
	public static final String SURFACE_PATH_LINE = "SurfacePathLine";
	public static final String CREATE_DISTANCE_RASTER="CreateDistanceRaster";

	// 水文分析
	public static final String FILL_SINK = "FillSink";
	public static final String FLOW_DIRECTION = "FlowDirection";
	public static final String FLOW_LENGTH = "FlowLength";
	public static final String STREAM_GRID = "StreamGrid";
	public static final String POUR_POINTS = "PourPoint";
	public static final String WATERSHED = "Watershed";
	public static final String BASIN = "Basin";
	public static final String FLOW_ACCUMULATION = "FlowAccumulation";
	public static final String STREAM_ORDER = "StreamOrder";
	public static final String STREAM_TO_LINE = "StreamToLine";
	public static final String STREAM_LINK = "StreamLink";

	// 空间统计分析
	public static final String CENTRAL_ELEMENT = "CentralElement";
	public static final String MEAN_CENTER = "MeanCenter";
	public static final String MEDIAN_CENTER = "MedianCenter";
	public static final String DIRECTIONAL = "Directional";
	public static final String LINEAR_DIRECTIONAL_MEAN = "LinearDirectionalMean";
	public static final String STANDARD_DISTANCE = "StandardDistance";
	public static final String AUTO_CORRELATION = "AutoCorrelation";
	public static final String HIGH_OR_LOW_CLUSTERING = "HighOrLowClustering";
	public static final String CLUSTER_OUTLIER_ANALYST = "ClusterOutlierAnalyst";
	public static final String HOT_SPOT_ANALYST = "HotSpotAnalyst";
	public static final String GEOGRAPHIC_WEIGHTED_REGRESSION = "GeographicWeightedRegression";
	public static final String INCREMENTAL_AUTO_CORRELATION = "IncrementalAutoCorrelation";
	public static final String AVERAGE_NEAREST_NEIGHBOR = "AverageNearestNeighbor";
	public static final String OPTIMIZED_HOT_SPOT_ANALYST = "OptimizedHotSpotAnalyst";

	//数据处理
	public static final String AGGREGATE_POINTS = "AggregatePoints";
	public static final String VECTOR_RESAMPLE = "VectorResample";
	public static final String EDGE_MATCH = "EdgeMatch";
	public static final String LINE_POLYGON_SMOOTH = "LinePolygonSmooth";
	public static final String PICKUP_BORDER = "PickupBorder";
	public static final String DUALLINE_TO_CENTERLINE = "DualLineToCenterLine";
	public static final String REGION_TO_CENTERLINE = "RegionToCenterLine";
	public static final String REGION_TRUNK_TO_CENTERLINE = "RegionTrunkToCenterLine";
	public static final String RAREFY_POINTS = "RarefyPoints";
	public static final String DISSOLVE="Dissolve";
	public static final String GRID_RESAMPLE="GridResample";
	public static final String IMAGE_COMBINE="ImageCombine";

	//类型转换
	public static final String CONVERSION_POINT_TO_LINE = "Conversion_PointToLine";
	public static final String CONVERSION_LINE_TO_POINT = "Conversion_LineToPoint";
	public static final String CONVERSION_LINE_TO_REGION = "Conversion_LineToRegion";
	public static final String CONVERSION_REGION_TO_POINT = "Conversion_RegionToPoint";
	public static final String CONVERSION_REGION_TO_LINE = "Conversion_RegionToLine";
	public static final String CONVERSION_CAD_TO_SIMPLE = "Conversion_CADToSimple";
	public static final String CONVERSION_CAD_TO_MODEL = "Conversion_CADToModel";
	public static final String CONVERSION_SIMPLE_TO_CAD = "Conversion_SimpleToCAD";
	public static final String CONVERSION_EPS_TO_SIMPLE = "Conversion_EPSToSimple";
	public static final String CONVERSION_FIELD_TO_TEXT = "Conversion_FieldToText";
	public static final String CONVERSION_TEXT_TO_FILED = "Conversion_TextToField";
	public static final String CONVERSION_TEXT_TO_POINT = "Conversion_TextToPoint";
	public static final String CONVERSION_TABULAR_TO_POINT = "Conversion_TabularToPoint";
	public static final String CONVERSION_TABULARPOINT_TO_REGION = "Conversion_TabularPointToRegion";
	public static final String CONVERSION_POINT3D_TO_2D = "Conversion_Point3DTo2D";
	public static final String CONVERSION_LINE3D_TO_2D = "Conversion_Line3DTo2D";
	public static final String CONVERSION_REGION3D_TO_2D = "Conversion_Region3DTo2D";
	public static final String CONVERSION_POINT2D_TO_3D = "Conversion_Point2DTo3D";
	public static final String CONVERSION_LINE2D_TO_3D = "Conversion_Line2DTo3D";
	public static final String CONVERSION_REGION2D_TO_3D = "Conversion_Region2DTo3D";
	public static final String CONVERSION_NETWORK_TO_POINT2D = "Conversion_NetWorkToPoint2D";
	public static final String CONVERSION_NETWORK_TO_LINE = "Conversion_NetWorkToLine";
	public static final String CONVERSION_LINEM_TO_LINE = "Conversion_LineMToLine";


	public static final String APPEND_FIELDS = "AppendFields";
	public static final String APPEND_ROWS="AppendRows";

	// 大数据
	public static final String HEAT_MAP = "HeatMap";
	public static final String SIMPLE_DENSITY = "SimpleDensity";
	public static final String KERNEL_DENSITY = "KernelDensity";
	public static final String GRIDREGION_AGGREGATION = "GridRegionAggregation";
	public static final String POLYGON_AGGREGATION = "PolygonAggregation";
	public static final String OVERLAYANALYSTGEO = "overlayanalystgeo";
	public static final String SINGLE_QUERY = "SingleQuery";
	public static final String SUMMARY_REGION="SummaryRegion";

	public static final String USER_DEFINE_PROJECTION = "UserDefineProjection";
}
