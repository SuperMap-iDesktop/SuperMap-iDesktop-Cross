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
	public static final String HydrologicalAnalysis = "HydrologicalAnalysis";
	public static final String Empty = "empty";

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
	public static final String CentralElement = "CentralElement";
	public static final String MeanCenter = "MeanCenter";
	public static final String MedianCenter = "MedianCenter";
	public static final String Directional = "Directional";
	public static final String LinearDirectionalMean = "LinearDirectionalMean";
	public static final String StandardDistance = "StandardDistance";
	public static final String autoCorrelation = "autoCorrelation";
	public static final String highOrLowClustering = "highOrLowClustering";
	public static final String clusterOutlierAnalyst = "clusterOutlierAnalyst";
	public static final String hotSpotAnalyst = "hotSpotAnalyst";
	public static final String geographicWeightedRegression = "geographicWeightedRegression";
	public static final String incrementalAutoCorrelation = "incrementalAutoCorrelation";
	public static final String AverageNearestNeighbor = "averageNearestNeighbor";
	public static final String optimizedHotSpotAnalyst = "optimizedHotSpotAnalyst";


	// 大数据
	public static final String HEAT_MAP = "HeatMap";
	public static final String SIMPLE_DENSITY = "SimpleDensity";
	public static final String KERNEL_DENSITY = "KernelDensity";
	public static final String OVERLAYANALYSTGEO = "overlayanalystgeo";
	public static final String SINGLE_QUERY = "singleQuery";

	public static final String USER_DEFINE_PROJECTION = "UserDefineProjection";
}
