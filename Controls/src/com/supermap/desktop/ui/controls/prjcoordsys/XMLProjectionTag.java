package com.supermap.desktop.ui.controls.prjcoordsys;

public class XMLProjectionTag {
	public static final String FILE_STARTUP_XML = "../Configuration/SuperMap.Desktop.Startup.xml";
	public static final String PROJECTION = "projection";
	public static final String DEFAULT = "default";
	public static final String PROJECTION_XML = "Templates/Projection/Projection.xml";

	public static final String NAMESPACEURL = "http://www.supermap.com/sml";
	// 1
	public static final String PROJECTION_ROOT = "SuperMapProjectionDefine";
	// {{ 隶属于Root
	public static final String PRJCOORDSYS_DEFINES = "sml:PJCoordSysDefines";
	// {{隶属于g_PJCoorSysDefines
	public static final String PRJGROUP_CAPTION_DEFAULT = "sml:DefaultPJGroupCaption";
	public static final String PRJCOORDSYS_CAPTION_DEFAULT = "sml:DefaultPJCoordSysCaption";
	public static final String PRJCOORDSYS_DEFINE = "sml:PJCoordSysDefine";
	// {{隶属于g_PJCoordSysDefine
	public static final String PRJGROUP_CAPTION = "sml:PJGroupCaption";
	public static final String PRJCOORDSYS_CAPTION = "sml:PJCoordSysCaption";
	public static final String PRJCOORDSYS_TYPE = "sml:PJCoordSysType";
	public static final String COORDINATE_REFERENCE_SYSTEM = "sml:CoordinateReferenceSystem";
	// {{隶属于g_CoordinateReferenceSystem
	public static final String NAMESET = "sml:Nameset";
	public static final String NAME = "sml:name"; // 隶属于g_Nameset
	public static final String TYPE = "sml:Type";
	public static final String UNITS = "sml:Units";
	public static final String GEOGRAPHIC_COORDINATE_SYSTEM = "sml:GeographicCoordinateSystem";
	public static final String MAP_PROJECTION = "sml:MapProjection";
	public static final String PARAMETERS = "sml:Parameters";
	public static final String HORIZONAL_GEODETIC_DATUM = "sml:HorizonalGeodeticDatum";
	public static final String PRIME_MERIDIAN = "sml:PrimeMeridian";
	public static final String ELLIPSOID = "sml:Ellipsoid";
	public static final String SEMI_MAJOR_AXIS = "sml:SemiMajorAxis";
	public static final String INVERSE_FLATTENING = "sml:InverseFlattening";
	public static final String FALSE_EASTING = "sml:FalseEasting";
	public static final String FALSE_NORTHING = "sml:FalseNorthing";
	public static final String CENTRAL_MERIDIAN = "sml:CentralMeridian";
	public static final String STANDARD_PARALLEL1 = "sml:StandardParallel1";
	public static final String STANDARD_PARALLEL2 = "sml:StandardParallel2";
	public static final String SCALE_FACTOR = "sml:ScaleFactor";
	public static final String CENTRAL_PARALLEL = "sml:CentralParallel";
	public static final String AZIMUTH = "sml:Azimuth";
	public static final String FIRSTPOINT_LONGITUDE = "sml:FirstPointLongitude";
	public static final String SECONDPOINT_LONGITUDE = "sml:SecondPointLongitude";

	// 隶属于 Root
	public static final String GEOCOORDSYS_DEFINES = "sml:PJGeoCoordSysDefines";
	public static final String GEOCOORDSYS_CAPTION_DEFAULT = "sml:DefaultPJGeoCoordSysCaption";
	public static final String GEOCOORDSYS_DEFINE = "sml:PJGeoCoordSysDefine";
	public static final String GEOGROUP_CATION = "sml:PJGeoGroupCaption"; // 新增内容
	public static final String GEOCOORDSYS_CAPTION = "sml:PJGeoCoordSysCaption";
	public static final String GEOCOORDSYS_TYPE = "sml:PJGeoCoordSysType";

	private XMLProjectionTag() {
		// 工具类不提供构造函数
	}
}
