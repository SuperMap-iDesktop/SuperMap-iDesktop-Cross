package com.supermap.desktop;

import com.supermap.data.Charset;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.EngineInfo;
import com.supermap.data.EngineType;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3Ds;
import com.supermap.realspace.Scene;
import com.supermap.realspace.TerrainLayers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommonToolkit {

	private CommonToolkit() {
		// 默认实现
	}

	public static class ApplicationInfoWrap {
		private ApplicationInfoWrap() {
			// 不提供构造函数
		}

		// #region Variable
		// private static LicenseType DesktopType = LicenseType.Desktop_None;
		// private static LicenseMode g_LicenseMode;
		// private static DateTime g_ExpiredDate;
		// private static int LicenseTipMaxDays = 10;
		//
		// private static boolean isMainFormFirstLoad;
		// #endregion
		//
		// #region Property
		// /// <summary>
		// /// 当前Desktop应用的许可类型
		// /// </summary>
		// public static LicenseType DesktopType
		// {
		// get
		// {
		// return DesktopType;
		// }
		// }
		//
		// /// <summary>
		// /// 当前Desktop应用的许可方式
		// /// </summary>
		// public static LicenseMode LicenseMode
		// {
		// get
		// {
		// return g_LicenseMode;
		// }
		// }
		//
		// /// <summary>
		// /// 许可过期日期
		// /// </summary>
		// public static DateTime ExpiredDate
		// {
		// get
		// {
		// return g_ExpiredDate;
		// }
		// }
		//
		// /// <summary>
		// /// 许可提示显示最大天数
		// /// </summary>
		// public static int LicenseTipMaxDays
		// {
		// get
		// {
		// return LicenseTipMaxDays;
		// }
		// }
		//
		// /// <summary>
		// /// 指示桌面的主窗体是否第一次加载
		// /// </summary>
		// public static boolean IsMainFormFirstLoad
		// {
		// get
		// {
		// return isMainFormFirstLoad;
		// }
		// set
		// {
		// isMainFormFirstLoad = value;
		// }
		// }
		// #endregion
		//
		// #region Construct
		//
		// #endregion
		//

		/**
		 * 验证当前Desktop应用的许可信息
		 */
		public static void validateDesktopType() {
			try {
				// boolean isConnected = false;
				// SuperMap.Data.License license = new SuperMap.Data.License();
				// LicenseMode licenseMode = LicenseMode.NoLicense;
				// DateTime dateTime = new DateTime();
				// if (license.Connect(ProductType.iDesktopAdvanced) == 0)
				// {
				// DesktopType = LicenseType.Desktop_iDesktopAdvance;
				// isConnected = true;
				// }
				// else if (license.Connect(ProductType.iDesktopProfessional) ==
				// 0)
				// {
				// DesktopType = LicenseType.Desktop_iDesktopProfessional;
				// isConnected = true;
				// }
				// else if (license.Connect(ProductType.iDesktopStandard) == 0)
				// {
				// DesktopType = LicenseType.Desktop_iDesktopStandard;
				// isConnected = true;
				// }
				//
				// if (isConnected)
				// {
				// LicenseFeatureInfo info = license.getFeatureInfo();
				// if (info.IsTrial)
				// {
				// licenseMode = LicenseMode.TrialVersion;
				// }
				// else
				// {
				// licenseMode = LicenseMode.FormalVersion;
				// }
				// dateTime = (DateTime)info.ExpiredTime;
				//
				// ProductType[] types = Toolkit.getTrialLicenses();
				// boolean isTrialLicensesAvailable = false;
				// license.Disconnect();
				// //如果用户同时正在用正式许可（同时不带一些子功能的许可），和试用许可，那么这时把试用许可允许用的功能也加上。
				// foreach (ProductType item in types)
				// {
				// if (license.Connect(item) == 0)
				// {
				// isTrialLicensesAvailable = true;
				// break;
				// }
				// }
				// if (isTrialLicensesAvailable)
				// {
				// DesktopType |= LicenseType.Desktop_Topology;
				// DesktopType |= LicenseType.Desktop_TrafficNetwork;
				// DesktopType |= LicenseType.Desktop_FacilityNetwork;
				// DesktopType |= LicenseType.Desktop_Spatial;
				// DesktopType |= LicenseType.Desktop_Chart;
				// DesktopType |= LicenseType.Desktop_GeoProcesser;
				// DesktopType |= LicenseType.Desktop_RealspaceSpatialAnalyst;
				// DesktopType |= LicenseType.Desktop_RealspaceEffect;
				// DesktopType |= LicenseType.Desktop_RealspaceNetworkAnalyst;
				// }
				// else
				// {
				// if (license.Connect(ProductType.iDesktopTopology) == 0)
				// {
				// DesktopType |= LicenseType.Desktop_Topology;
				// }
				// if (license.Connect(ProductType.iDesktopTrafficNetwork) == 0)
				// {
				// DesktopType |= LicenseType.Desktop_TrafficNetwork;
				// }
				// if (license.Connect(ProductType.iDesktopFacilityNetwork) ==
				// 0)
				// {
				// DesktopType |= LicenseType.Desktop_FacilityNetwork;
				// }
				// if (license.Connect(ProductType.iDesktopSpatial) == 0)
				// {
				// DesktopType |= LicenseType.Desktop_Spatial;
				// }
				// if (license.Connect(ProductType.iDesktopChart) == 0)
				// {
				// DesktopType |= LicenseType.Desktop_Chart;
				// }
				// if (license.Connect(ProductType.iDesktopGeoProcesser) == 0)
				// {
				// DesktopType |= LicenseType.Desktop_GeoProcesser;
				// }
				// if
				// (license.Connect(ProductType.iDesktopRealspaceSpatialAnalyst)
				// == 0)
				// {
				// DesktopType |= LicenseType.Desktop_RealspaceSpatialAnalyst;
				// }
				// if (license.Connect(ProductType.iDesktopRealspaceEffect) ==
				// 0)
				// {
				// DesktopType |= LicenseType.Desktop_RealspaceEffect;
				// }
				// if (license.Connect(516) == 0)
				// {
				// DesktopType |= LicenseType.Desktop_RealspaceNetworkAnalyst;
				// }
				// }
				// }
				// else
				// {
				// ProductType[] types = Toolkit.getTrialLicenses();
				// foreach (ProductType item in types)
				// {
				// if (license.Connect(item) == 0)
				// {
				// license.Disconnect();//对于安装完桌面后首次访问组件返回的过期时间是有误，为确保正确，必须得重新Connect一下。
				// license.Connect(item);
				// isConnected = true;
				// DesktopType = LicenseType.Desktop_iDesktopProfessional;
				// licenseMode = LicenseMode.TrialVersion;
				// LicenseFeatureInfo info = license.getFeatureInfo();
				// dateTime = info.ExpiredTime;
				//
				// DesktopType |= LicenseType.Desktop_Topology;
				// DesktopType |= LicenseType.Desktop_TrafficNetwork;
				// DesktopType |= LicenseType.Desktop_FacilityNetwork;
				// DesktopType |= LicenseType.Desktop_Spatial;
				// DesktopType |= LicenseType.Desktop_Chart;
				// DesktopType |= LicenseType.Desktop_GeoProcesser;
				// DesktopType |= LicenseType.Desktop_RealspaceSpatialAnalyst;
				// DesktopType |= LicenseType.Desktop_RealspaceEffect;
				// DesktopType |= LicenseType.Desktop_RealspaceNetworkAnalyst;
				// break;
				// }
				// }
				// }
				// if (isConnected)
				// {
				// g_LicenseMode = licenseMode;
				// g_ExpiredDate = dateTime;
				// }
				// else
				// {
				// g_LicenseMode = LicenseMode.NoLicense;
				// }
				// license.Disconnect();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
		//
		// /// <summary>
		// /// 重新验证全套的许可，包括正在使用的和未使用的，然后累加过期时间
		// /// </summary>
		// /// <returns></returns>
		// public static boolean ReFullCheckUpLicenses()
		// {
		// boolean result = false;
		// try
		// {
		// String licenseInfo = String.Empty;
		// AdminStatus status = AdminAPIWrap.AdminConnect();
		// if (status == AdminStatus.OK)
		// {
		// status = AdminAPIWrap.Adminget(AdminAPIWrap.Scope,
		// AdminAPIWrap.getAllFeature, ref licenseInfo);
		// if (status == AdminStatus.OK)
		// {
		// result = true;
		// }
		// }
		//
		// if (result)
		// {
		// XmlDocument doc = new XmlDocument();
		// doc.LoadXml(licenseInfo);
		// XmlNode root = doc.SelectSingleNode("admin_response");
		// XmlNode lmNode = null;
		//
		// List<LicenseFeature> featureList = new List<LicenseFeature>();
		//
		// for (int i = 0; i < root.ChildNodes.Count; i++)
		// {
		// lmNode = root.ChildNodes.get(i);
		// if (lmNode.Name == "feature")
		// {
		// List<LicenseFeature> productList = new List<LicenseFeature>();
		// LicenseFeature feature = LicenseFeature.FormXml(lmNode.OuterXml);
		// if (feature != null)
		// {
		// featureList.Add(feature);
		// }
		// }
		// }
		//
		// TimeSpan remainTime = TimeSpan.Zero;
		// foreach (LicenseFeature item in featureList)
		// {
		// // 如果已经开始使用了
		// if (!item.StartTime.Equals(DateTime.MinValue))
		// {
		// remainTime += item.EndTime - DateTime.Now;
		// }
		// else
		// {
		// remainTime += item.LastDesTimeSpan;
		// }
		// }
		//
		// g_ExpiredDate = DateTime.Now + remainTime;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return result;
		// }
		//
		// //判断当前版本桌面是否支持加载外部插件，暂定所有扩展系列的产品都支持加载外部插件
		// public static boolean AllowExternalAssemblyRun()
		// {
		// return IsDesktop();
		// }
		//
		// //判断当前应用许可是否属于桌面Desktop系列，iMapEditor系列的dll不应能在Desktop系列上运行。
		// public static boolean IsDesktop()
		// {
		// return (((CommonToolkit.ApplicationInfoWrap.DesktopType &
		// LicenseType.Desktop_iDesktopProfessional) ==
		// LicenseType.Desktop_iDesktopProfessional)
		// || ((CommonToolkit.ApplicationInfoWrap.DesktopType &
		// LicenseType.Desktop_iDesktopStandard) ==
		// LicenseType.Desktop_iDesktopStandard)
		// || ((CommonToolkit.ApplicationInfoWrap.DesktopType &
		// LicenseType.Desktop_iDesktopAdvance) ==
		// LicenseType.Desktop_iDesktopAdvance));
		// //|| ((CommonToolkit.ApplicationInfoWrap.DesktopType &
		// LicenseType.Desktop_iMapEditor) == LicenseType.Desktop_iMapEditor)
		// }
		//
		// public static boolean IsLicenseAvailable()
		// {
		// return (CommonToolkit.ApplicationInfoWrap.IsDesktop()
		// && (CommonToolkit.ApplicationInfoWrap.ExpiredDate -
		// DateTime.Today).TotalDays > 0);
		// }
		//
		// public static String getLicenseRemainTimeTip(LicenseMode mode,
		// TimeSpan remainTime)
		// {
		// String result = CoreResources.String_Label_InvalidationSuggestion;
		// try
		// {
		// if (mode == LicenseMode.TrialVersion)
		// {
		// if (remainTime != null)
		// {
		// Double timeCount = remainTime.TotalDays;
		// if (timeCount > 90 || timeCount < -365000)//days <
		// -365000是针对安装完桌面第一次打开时，曾经访问到组件的过期时间是0001/01/01，这时其实是刚开始试用，应该提示剩余90天。
		// {
		// timeCount = 90;
		// }
		//
		// if (timeCount >= 1)
		// {
		// result = String.format(CoreResources.String_Label_SuggestionDay,
		// (int)timeCount);
		// }
		// else if (timeCount > 0)
		// {
		// timeCount = remainTime.TotalHours;
		// if (timeCount >= 1)
		// {
		// result = String.format(CoreResources.String_Label_SuggestionHour,
		// (int)timeCount);
		// }
		// else
		// {
		// result = String.format(CoreResources.String_Label_SuggestionHour,
		// Math.Round(timeCount, 1));
		// }
		// }
		// else
		// {
		// result = CoreResources.String_Label_InvalidationSuggestion;
		// }
		// }
		// }
		// else if (mode == LicenseMode.NoLicense)
		// {
		// result = CoreResources.String_Label_NoLicenseSuggestion;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return result;
		// }
	}

	public static class AssemblyWrap {
		private AssemblyWrap() {
			// 默认私有构造器
		}

		// #region Variable
		// const String g_strDesktopPublicKeyToken = "7cd7dcc384397b6e";
		// const String g_strTestPublicKeyToken = "23d8e753f5ab9fa4";
		// #endregion
		//
		// #region Property
		//
		// #endregion
		//
		// #region Construct
		//
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 根据程序集的显示名称判断是否SuperMap程序集
		// /// </summary>
		// public static boolean IsSuperMapAssembly(String strFullName)
		// {
		// boolean bIsSuperMapAssembly = false;
		// try
		// {
		// String[] s = strFullName.Split(',');
		// for (int i = 0; i < s.Length; i++)
		// {
		// String str = s.get(i).TrimStart();
		// if (str.StartsWith("PublicKeyToken"))
		// {
		// String strPublicKeyToken = str.Replace("PublicKeyToken=", "");
		// if (strPublicKeyToken.Equals(g_strDesktopPublicKeyToken) ||
		// strPublicKeyToken.Equals(g_strTestPublicKeyToken))
		// {
		// bIsSuperMapAssembly = true;
		// }
		// break;
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return bIsSuperMapAssembly;
		// }
		//
		// /// <summary>
		// /// 根据插件信息判断插件对应的程序集是否SuperMap程序集
		// /// </summary>
		// public static boolean IsSuperMapAssembly(PluginInfo info)
		// {
		// boolean bIsSuperMapAssembly = false;
		// try
		// {
		// String path =
		// CommonToolkit.PathWrap.getFullPathName(info.AssemblyName);
		// if (System.IO.File.Exists(path))
		// {
		// System.Reflection.Assembly assembly =
		// System.Reflection.Assembly.LoadFrom(path);
		// bIsSuperMapAssembly = IsSuperMapAssembly(assembly.FullName);
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return bIsSuperMapAssembly;
		// }
		// #endregion
	}

	public static class CharsetWrap {
		private CharsetWrap() {
			// 不提供构造函数
		}

		// #region Variable
		// private static Dictionary<Charset, String> dictionaryCharset;
		// #endregion
		//
		// #region Property
		// /// <summary>
		// /// 字符集类型与引用资源字典
		// /// </summary>
		// public static Dictionary<Charset, String> DictionaryCharset
		// {
		// get
		// {
		// return dictionaryCharset;
		// }
		// }
		// #endregion
		//
		// #region Construct
		// static CharsetWrap()
		// {
		// Initialize();
		// }
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 初始化字符集封装类
		// /// </summary>
		// private static void Initialize()
		// {
		// try
		// {
		// dictionaryCharset = new Dictionary<Charset, string>();
		// dictionaryCharset.Add(Charset.ANSI,
		// CoreResources.String_Charset_ANSI);
		// dictionaryCharset.Add(Charset.Arabic,
		// CoreResources.String_Charset_Arabic);
		// dictionaryCharset.Add(Charset.Baltic,
		// CoreResources.String_Charset_Baltic);
		// dictionaryCharset.Add(Charset.ChineseBIG5,
		// CoreResources.String_Charset_ChineseBIG5);
		// dictionaryCharset.Add(Charset.Cyrillic,
		// CoreResources.String_Charset_Cyrillic);
		// dictionaryCharset.Add(Charset.Default,
		// CoreResources.String_Charset_Default);
		// dictionaryCharset.Add(Charset.EastEurope,
		// CoreResources.String_Charset_EastEurope);
		// dictionaryCharset.Add(Charset.GB18030,
		// CoreResources.String_Charset_GB18030);
		// dictionaryCharset.Add(Charset.Greek,
		// CoreResources.String_Charset_Greek);
		// dictionaryCharset.Add(Charset.Hangeul,
		// CoreResources.String_Charset_Hangeul);
		// dictionaryCharset.Add(Charset.Hebrew,
		// CoreResources.String_Charset_Hebrew);
		// dictionaryCharset.Add(Charset.ISO2022JP2,
		// CoreResources.String_Charset_ISO2022JP2);
		// dictionaryCharset.Add(Charset.Johab,
		// CoreResources.String_Charset_Johab);
		// dictionaryCharset.Add(Charset.Korean,
		// CoreResources.String_Charset_Korean);
		// dictionaryCharset.Add(Charset.MAC,
		// CoreResources.String_Charset_MAC);
		// dictionaryCharset.Add(Charset.OEM,
		// CoreResources.String_Charset_OEM);
		// dictionaryCharset.Add(Charset.Russian,
		// CoreResources.String_Charset_Russian);
		// dictionaryCharset.Add(Charset.ShiftJIS,
		// CoreResources.String_Charset_ShiftJIS);
		// dictionaryCharset.Add(Charset.Symbol,
		// CoreResources.String_Charset_Symbol);
		// dictionaryCharset.Add(Charset.Thai,
		// CoreResources.String_Charset_Thai);
		// dictionaryCharset.Add(Charset.Turkish,
		// CoreResources.String_Charset_Turkish);
		// dictionaryCharset.Add(Charset.Unicode,
		// CoreResources.String_Charset_Unicode);
		// //dictionaryCharset.Add(Charset.UTF32,
		// CoreResources.String_Charset_UTF32);
		// dictionaryCharset.Add(Charset.UTF7,
		// CoreResources.String_Charset_UTF7);
		// dictionaryCharset.Add(Charset.UTF8,
		// CoreResources.String_Charset_UTF8);
		// dictionaryCharset.Add(Charset.Vietnamese,
		// CoreResources.String_Charset_Vietnamese);
		// dictionaryCharset.Add(Charset.Windows1252,
		// CoreResources.String_Charset_Windows1252);
		// dictionaryCharset.Add(Charset.xIA5,
		// CoreResources.String_Charset_XIA5);
		// dictionaryCharset.Add(Charset.xIA5German,
		// CoreResources.String_Charset_XIA5German);
		// dictionaryCharset.Add(Charset.xIA5Norwegian,
		// CoreResources.String_Charset_XIA5Norwegian);
		// dictionaryCharset.Add(Charset.xIA5Swedish,
		// CoreResources.String_Charset_XIA5Swedish);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		//

		/**
		 * 根据字符集类型获取对应的资源字符串
		 *
		 * @param charset
		 * @return
		 */
		public static String getCharsetName(Charset charset) {
			String charsetInfo = "";
			try {
				// charsetInfo = dictionaryCharset[charset];
				charsetInfo = charset.toString();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return charsetInfo;
		}

		/**
		 * 根据字符集资源字符串获取对应的字符集类型
		 *
		 * @param charsetString
		 *            字符集名称
		 * @return 字符集类型
		 */
		public static Charset getCharset(String charsetString) {
			Charset charset = Charset.UTF8;
			return charset;
		}
	}

	public static class ColorWrap {
		private ColorWrap() {
			// 默认实现
		}

		// #region Variable
		//
		// #endregion
		//
		// #region Property
		//
		// #endregion
		//
		// #region Construct
		//
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 将RGB中的A分量转化为百分比的透明度
		// /// </summary>
		// public static int getOpaqueRateFromAlpha(int alpha)
		// {
		// return (int)(Math.Round(alpha / 255.0 * 100.0,
		// MidpointRounding.AwayFromZero));
		// }
		//
		// /// <summary>
		// /// 将百分比的透明度转化为RGB中的A分量
		// /// </summary>
		// public static int getAlphaFromOpaqueRate(int opaqueRate)
		// {
		// return (int)(Math.Round(opaqueRate / 100.0 * 255.0,
		// MidpointRounding.AwayFromZero));
		// }
		// #endregion
		//
		// #region Function_Event
		//
		// #endregion
		//
		// #region Function_Private
		//
		// #endregion
		//
		// #region Event
		//
		// #endregion
		//
		// #region InterfaceMembers
		//
		// #endregion
		//
		// #region NestedTypes
		//
		// #endregion
	}

	public static class DatasetTypeWrap {

		private static HashMap<DatasetType, String> datasetTypes = null;

		/**
		 * 初始化数据集类型字典
		 */
		private DatasetTypeWrap() {
			// 不提供构造函数
		}

		private static void initialize() {
			if (datasetTypes != null) {
				return;
			}

			datasetTypes = new HashMap<DatasetType, String>(22);

			datasetTypes.put(DatasetType.TABULAR, CommonProperties.getString("String_DatasetType_Tabular"));
			datasetTypes.put(DatasetType.POINT, CommonProperties.getString("String_DatasetType_Point"));
			datasetTypes.put(DatasetType.LINE, CommonProperties.getString("String_DatasetType_Line"));
			datasetTypes.put(DatasetType.NETWORK, CommonProperties.getString("String_DatasetType_Network"));
			datasetTypes.put(DatasetType.REGION, CommonProperties.getString("String_DatasetType_Region"));
			datasetTypes.put(DatasetType.TEXT, CommonProperties.getString("String_DatasetType_Text"));
			datasetTypes.put(DatasetType.PARAMETRICLINE, CommonProperties.getString("String_DatasetType_ParametricLine"));
			datasetTypes.put(DatasetType.PARAMETRICREGION, CommonProperties.getString("String_DatasetType_ParametricRegion"));
			datasetTypes.put(DatasetType.IMAGE, CommonProperties.getString("String_DatasetType_Image"));
			datasetTypes.put(DatasetType.GRID, CommonProperties.getString("String_DatasetType_Grid"));
			datasetTypes.put(DatasetType.WMS, CommonProperties.getString("String_DatasetType_WMS"));
			datasetTypes.put(DatasetType.WCS, CommonProperties.getString("String_DatasetType_WCS"));
			datasetTypes.put(DatasetType.POINT3D, CommonProperties.getString("String_DatasetType_Point3D"));
			datasetTypes.put(DatasetType.LINE3D, CommonProperties.getString("String_DatasetType_Line3D"));
			datasetTypes.put(DatasetType.LINEM, CommonProperties.getString("String_DatasetType_LineM"));
			datasetTypes.put(DatasetType.REGION3D, CommonProperties.getString("String_DatasetType_Region3D"));
			datasetTypes.put(DatasetType.CAD, CommonProperties.getString("String_DatasetType_CAD"));
			datasetTypes.put(DatasetType.LINKTABLE, CommonProperties.getString("String_DatasetType_LinkTable"));
			datasetTypes.put(DatasetType.TOPOLOGY, CommonProperties.getString("String_DatasetType_Topology"));
			datasetTypes.put(DatasetType.GRIDCOLLECTION, CommonProperties.getString("String_DatasetType_GridCollection"));
			datasetTypes.put(DatasetType.IMAGECOLLECTION, CommonProperties.getString("String_DatasetType_ImageCollection"));
			datasetTypes.put(DatasetType.MODEL, CommonProperties.getString("String_DatasetType_Model"));
			datasetTypes.put(DatasetType.IMAGECOLLECTION, CommonProperties.getString("String_DatasetType_ImageCollection"));
			datasetTypes.put(DatasetType.NETWORK3D, CommonProperties.getString("String_DatasetType_Network3D"));
		}

		/**
		 * 根据数据集类型资源字符串获取对应的数据集类型
		 *
		 * @param typeName
		 *            数据集类型字符串
		 * @return 数据集类型
		 */
		public static DatasetType findType(String typeName) {
			DatasetType result = null;
			try {
				initialize();
				Iterator iter = datasetTypes.entrySet().iterator();
				while (iter.hasNext()) {
					java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
					String value = (String) entry.getValue();
					if (value.equals(typeName)) {
						result = (DatasetType) entry.getKey();
						break;
					}
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return result;
		}

		/**
		 * 根据数据集类型获取对应的资源字符串
		 *
		 * @param type
		 *            数据集类型
		 * @return 数据集类型字符串
		 */
		public static String findName(DatasetType type) {
			String result = "";
			try {
				initialize();
				if (datasetTypes.get(type) == null) {
					result = CommonProperties.getString("String_DatasetType_Unknown");
				} else {
					result = datasetTypes.get(type);
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return result;
		}

		/**
		 * 验证数据集类型是否存在于枚举中
		 *
		 * @param type
		 *            数据集类型
		 * @return
		 */
		public static boolean isValidType(DatasetType type) {
			initialize();
			boolean result = true;
			if (datasetTypes.get(type) == null) {
				result = false;
			}
			// return Enum.IsDefined(typeof(DatasetType), type);
			return result;
		}

		public static boolean isPoint(DatasetType type) {
			boolean result = false;
			try {
				if (type == DatasetType.POINT || type == DatasetType.POINT3D) {
					result = true;
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return result;
		}

		public static boolean isLine(DatasetType type) {
			boolean result = false;
			try {
				if (type == DatasetType.LINE || type == DatasetType.LINEM || type == DatasetType.LINE3D || type == DatasetType.NETWORK
						|| type == DatasetType.NETWORK3D) {
					result = true;
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return result;
		}

		public static boolean isRegion(DatasetType type) {
			boolean result = false;
			try {
				if (type == DatasetType.REGION || type == DatasetType.REGION3D) {
					result = true;
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return result;
		}
	}

	public static class DatasourceWrap {

		// // 关闭相应的数据源
		// public static void CloseDatasources(Datasource[] datasources)
		// {
		// try
		// {
		// String strdatasourceAlias = String.Empty;
		// String message = String.Empty;
		// if (datasources.Length == 1)
		// {
		// strdatasourceAlias = datasources[0].Alias;
		// }
		// else
		// {
		// for (int i = 0; i < datasources.Length; i++)
		// {
		// if (i == datasources.Length - 1)
		// {
		// strdatasourceAlias +=
		// Properties.CoreResources.String_QuotationMarkHead +
		// datasources[i].Alias +
		// Properties.CoreResources.String_QuotationMarkEnd;
		// }
		// else
		// {
		// strdatasourceAlias +=
		// Properties.CoreResources.String_QuotationMarkHead +
		// datasources[i].Alias +
		// Properties.CoreResources.String_QuotationMarkEnd +
		// Properties.CoreResources.String_IntervalMark;
		// }
		// }
		// }
		// Application.ActiveApplication.UserInfoManage.FunctionRun(FunctionID.CloseDatasource);//加入用户体验计划
		// message =
		// String.Format(Properties.CoreResources.String_MessageBox_CloseDatasource,
		// strdatasourceAlias);
		//
		// if (MessageBox.Show(message, UI.CommonToolkit.GetMessageBoxTitle(),
		// MessageBoxButtons.OKCancel, MessageBoxIcon.Information) ==
		// DialogResult.OK)
		// {
		// for (int i = datasources.Length - 1; i >= 0; i--)
		// {
		// Application.ActiveApplication.UserInfoManage.FunctionStart(FunctionID.CloseDatasource);
		// try
		// {
		// boolean result =
		// Application.ActiveApplication.Workspace.Datasources.Close(datasources[i].Alias);
		//
		// if (result)
		// {
		// Application.ActiveApplication.UserInfoManage.FunctionSuccess(FunctionID.CloseDatasource);
		// }
		// else
		// {
		// Application.ActiveApplication.UserInfoManage.FunctionFailed(FunctionID.CloseDatasource);
		// }
		// }
		// catch (Exception ex)
		// {
		// Application.ActiveApplication.Output.Output(ex, InfoType.Exception,
		// FunctionID.CloseDatasource);
		// }
		// }
		// }
		// }
		// catch
		// {
		//
		// }
		// }
		//

		//
		// #region Property
		// /// <summary>
		// /// 获取本地网络中SQL Server实例的信息列表 [1/6/2012 zhoujt]
		// /// </summary>
		// public static DataTable CurrentDataTable
		// {
		// get
		// {
		// if (CurrentDataTable == null)
		// {
		// getCurrentDataTable();
		// }
		// return CurrentDataTable;
		// }
		// set
		// {
		// CurrentDataTable = value;
		// }
		// }
		// #endregion
		//
		// #region Construct
		// static DatasourceWrap()
		// {
		// Initialize();
		// }
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 获取本地网络中SQL Server实例的信息列表 [1/6/2012 zhoujt]
		// /// </summary>
		// public static void getCurrentDataTable()
		// {
		// SqlDataSourceEnumerator enumerator =
		// SqlDataSourceEnumerator.Instance;
		// CurrentDataTable = enumerator.getDataSources();
		// }
		//
		// /// <summary>
		// /// 判断指定的文件后缀是否支持的文件引擎类型
		// /// </summary>
		// public static boolean IsSuportedFileEngineExt(String fileExt)
		// {
		// boolean result = false;
		//
		// EngineInfo[] loadedFileEngines = getLoadedFileEngines();
		// if (loadedFileEngines != null)
		// {
		// foreach (EngineInfo engInfo in loadedFileEngines)
		// {
		// foreach (String ext in engInfo.SupportExtensions)
		// {
		// if (String.Compare(fileExt, ext, true) == 0)
		// {
		// result = true;
		// break;
		// }
		// }
		// if (result)
		// {
		// break;
		// }
		// }
		// }
		// return result;
		// }

		// /// <summary>
		// /// 获取当前组件支持的某种数据源的所有引擎类型集合
		// /// </summary>
		// public static void Initialize()
		// {
		// List<EngineType> fileEngines = new List<EngineType>();
		// List<EngineType> databaseEngines = new List<EngineType>();
		// List<EngineType> webEngines = new List<EngineType>();
		// try
		// {
		// for (int i = 0; i <
		// SuperMap.Data.Environment.CurrentLoadedEngineInfos.Length; i++)
		// {
		// if
		// (SuperMap.Data.Environment.CurrentLoadedEngineInfos.get(i).EngineFamily
		// == EngineFamilyType.File)
		// {
		// fileEngines.Add(SuperMap.Data.Environment.CurrentLoadedEngineInfos.get(i).getType());
		// }
		// else if
		// (SuperMap.Data.Environment.CurrentLoadedEngineInfos.get(i).EngineFamily
		// == EngineFamilyType.Database)
		// {
		// databaseEngines.Add(SuperMap.Data.Environment.CurrentLoadedEngineInfos.get(i).getType());
		// }
		// else if
		// (SuperMap.Data.Environment.CurrentLoadedEngineInfos.get(i).EngineFamily
		// == EngineFamilyType.Web)
		// {
		// webEngines.Add(SuperMap.Data.Environment.CurrentLoadedEngineInfos.get(i).getType());
		// }
		// }
		// fileEngineTypes = fileEngines.ToArray();
		// databaseEngineTypes = databaseEngines.ToArray();
		// webEngineTypes = webEngines.ToArray();
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		//
		// /// <summary>
		// /// 判断指定数据源是否文件数据源
		// /// </summary>
		// public static boolean IsFileDatasource(Datasource datasource)
		// {
		// boolean result = false;
		//
		// try
		// {
		// EngineInfo[] loadedFileEngines = getLoadedFileEngines();
		// if (loadedFileEngines != null)
		// {
		// foreach (EngineInfo engInfo in loadedFileEngines)
		// {
		// if (engInfo.getType() == datasource.EngineType)
		// {
		// result = true;
		// break;
		// }
		// }
		// }
		// }
		// catch
		// {
		//
		// }
		// return result;
		// }
		//
		// /// <summary>
		// /// 判断指定数据源是Web型数据源
		// /// </summary>
		// public static boolean IsWebDatasource(EngineType engineType)
		// {
		// boolean result = false;
		// try
		// {
		// for (int i = 0; i < webEngineTypes.Length ; i++)
		// {
		// if (webEngineTypes.get(i) == engineType)
		// {
		// result = true;
		// break;
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return result;
		// }

		//
		// /// <summary>
		// /// 将组件的文件引擎信息集合转化为桌面的文件引擎信息封装类，主要用于构造打开数据源对话框的过滤条件
		// /// </summary>
		// public static FileEngineInfo[] BuildFileEngineFilter(EngineInfo[]
		// loadedFileEngines)
		// {
		// List<FileEngineInfo> fileEngineInfos = new List<FileEngineInfo>();
		//
		// try
		// {
		// String fileExts = String.Empty;
		// String fileInfos = String.Empty;
		//
		// for (int i = loadedFileEngines.Length - 1; i >= 0; i--)
		// {
		// // 此处组件会增加引擎名称
		// // 另外组件还会增加判断是否是数据库型引擎的属性，所以到时此处也要增加判断才好
		//
		// EngineInfo engInfo = loadedFileEngines.get(i);
		// String fileExtTemp = String.Empty;
		//
		// FileEngineInfo fileEngineInfo = new FileEngineInfo();
		// fileEngineInfo.engineType = engInfo.getType();
		// fileEngineInfo.name = engInfo.Name;
		//
		// foreach (String fileExt in engInfo.SupportExtensions)
		// {
		// if (fileExt.Length > 0)
		// {
		// if (fileExt.toLowerCase().Equals(CoreResources.String_udd) ||
		// fileExt.toLowerCase().Equals(CoreResources.String_sdd))
		// {
		// }
		// else if (engInfo.getType() != EngineType.ImagePlugins)
		// {
		// if (fileExt.StartsWith(CoreResources.String_BitAndDot))
		// {
		// fileExtTemp += fileExt;
		// }
		// else if (fileExt.StartsWith(CoreResources.String_Dot))
		// {
		// fileExtTemp += String.format(CoreResources.String_Bit0, fileExt);
		// }
		// else
		// {
		// fileExtTemp += String.format(CoreResources.String_Bit0, fileExt);
		// }
		//
		// fileExtTemp += CoreResources.String_Separate;// ";";
		// }
		// else
		// {
		// String temp = String.Empty;
		// if (fileExt.StartsWith(CoreResources.String_BitAndDot))
		// {
		// temp = fileExt;
		// }
		// else if (fileExt.StartsWith(CoreResources.String_Dot))
		// {
		// temp = String.format(CoreResources.String_Bit0, fileExt);
		// }
		// else
		// {
		// temp = String.format(CoreResources.String_Bit0, fileExt);
		// }
		//
		// String strname =
		// CommonToolkit.UndefinedWrap.getImagePluginsName(temp);
		// fileInfos += strname;
		// fileExts = fileExts + temp + CoreResources.String_Separate;
		// fileInfos += String.format(CoreResources.String_MutiSeparate,
		// fileExtTemp);
		//
		// fileExtTemp = fileExts;
		// }
		// }
		// }
		//
		// if (fileExtTemp.Length > 0)
		// {
		// fileEngineInfo.fileExt = fileExtTemp;
		// fileEngineInfo.fileExt =
		// fileEngineInfo.fileExt.TrimEnd(CoreResources.String_Separate.ToCharArray());
		// fileEngineInfos.Add(fileEngineInfo);
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return fileEngineInfos.ToArray();
		// }
		//
		// /// <summary>
		// /// 根据桌面的文件引擎信息封装类构造打开数据源对话框的过滤条件
		// /// </summary>
		// public static String BuildLoadedFileEngineFilter(FileEngineInfo[]
		// fileEngineInfos)
		// {
		// String filter = String.Empty;
		//
		// try
		// {
		// String fileExts = String.Empty;
		// String fileInfos = String.Empty;
		// String allFilesFilter = String.Empty;
		//
		// foreach (FileEngineInfo fileEngineInfo in fileEngineInfos)
		// {
		// String temp = fileEngineInfo.fileExt.Replace(";*.sci3d", "");
		// temp = temp.Replace(";*.gdb", "");
		// fileExts += temp;
		// fileExts += CoreResources.String_Separate;
		// filter += fileEngineInfo.name;
		// filter += String.format(CoreResources.String_MutiSeparate, temp);
		// }
		//
		// allFilesFilter = String.format(CoreResources.String_AllFiles,
		// fileExts);
		//
		// filter = allFilesFilter + filter;
		// filter += "|";
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return filter;
		// }
		//
		// //static private String BuildLoadedFileEngineFilter()
		// //{
		// // String filter = String.Empty;
		// // EngineInfo[] loadedFileEngines = getLoadedFileEngines();
		// // if (loadedFileEngines != null)
		// // {
		// // FileEngineInfo[] fileEngineInfos =
		// BuildFileEngineFilter(loadedFileEngines);
		// // filter = BuildLoadedFileEngineFilter(fileEngineInfos);
		// // }
		//
		// // return filter;
		// //}
		// #endregion
		//
		// #region Function_Event
		//
		// #endregion
		//
		// #region Function_Private
		//
		// #endregion
		//
		// #region Event
		//
		// #endregion
		//
		// #region InterfaceMembers
		//
		// #endregion
		//
		// #region NestedTypes
		//
		// #endregion
		// }
		// }
		//
		// public class FileEngineInfo
		// {
		// public EngineType engineType;
		// public String fileExt;
		// public String name;
		// }
	}

	public static class DirectoryWrap {
		private DirectoryWrap() {
			// 默认实现
		}

		// public static Int64 getDirectorySize(String directoryPath)
		// {
		// Int64 size = 0;
		// if (!String.IsNullOrEmpty(directoryPath)
		// && Directory.Exists(directoryPath))
		// {
		// DirectoryInfo directoryInfo = new DirectoryInfo(directoryPath);
		// size = getDirectorySize(directoryInfo);
		// }
		//
		// return size;
		// }
		//
		// public static Int64 getDirectorySize(DirectoryInfo directoryInfo)
		// {
		// Int64 size = 0;
		// if (directoryInfo != null
		// && directoryInfo.Exists)
		// {
		// DirectoryInfo[] directoryInfos = directoryInfo.getDirectories();
		// for (int i = 0; i < directoryInfos.Length; i++)
		// {
		// size += getDirectorySize(directoryInfos.get(i));
		// }
		// FileInfo[] fileInfos = directoryInfo.getFiles();
		// for (int i = 0; i < fileInfos.Length; i++)
		// {
		// size += fileInfos.get(i).Length;
		// }
		// }
		//
		// return size;
		// }
		//
		// //将sourceDirectory下的所有文件以及文件夹复制到targetDirectory处
		// public static boolean Copy(String sourceDirectory, String
		// targetDirectory)
		// {
		// boolean result = false;
		// try
		// {
		// if (!String.IsNullOrEmpty(sourceDirectory)
		// && !String.IsNullOrEmpty(targetDirectory)
		// && Directory.Exists(sourceDirectory))
		// {
		// DirectoryInfo targetInfo = new DirectoryInfo(targetDirectory);
		// if (!targetInfo.Exists)
		// {
		// targetInfo.Create();
		// }
		//
		// String[] directorys = Directory.getDirectories(sourceDirectory);
		// for (int i = 0; i < directorys.Length; i++)
		// {
		// String directory = directorys.get(i);
		// DirectoryInfo directoryInfo = new DirectoryInfo(directory);
		// if ((directoryInfo.Attributes & FileAttributes.System) !=
		// FileAttributes.System
		// && (directoryInfo.Attributes & FileAttributes.Hidden) !=
		// FileAttributes.Hidden)
		// {
		// String subName = directoryInfo.Name;
		// DirectoryInfo targetSubInfo = targetInfo.CreateSubdirectory(subName);
		// if (!CommonToolkit.DirectoryWrap.Copy(directoryInfo.FullName,
		// targetSubInfo.FullName))
		// {
		// return false;
		// }
		// }
		// }
		// String[] files = Directory.getFiles(sourceDirectory);
		// for (int i = 0; i < files.Length; i++)
		// {
		// String sourceFile = files.get(i);
		// String fileName = Path.getFileName(sourceFile);
		// String targetFileName = targetDirectory + Path.DirectorySeparatorChar
		// + fileName;
		// File.Copy(sourceFile, targetFileName, true);
		// }
		// result = true;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		//
		// return result;
		// }
		//
		// public static String getDirectoryName(String fullDirectoryName)
		// {
		// String directoryName = String.Empty;
		// if (!String.IsNullOrEmpty(fullDirectoryName))
		// {
		// try
		// {
		// DirectoryInfo directoryInfo = new DirectoryInfo(fullDirectoryName);
		// directoryName = directoryInfo.Name;
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		//
		// return directoryName;
		// }
	}

	public static class EncodeTypeWrap {

		static HashMap<EncodeType, String> encodeTypes = null;

		private EncodeTypeWrap() {
			// 不提供构造函数
		}

		private static void initialize() {
			if (encodeTypes != null) {
				return;
			}
			encodeTypes = new HashMap<EncodeType, String>(10);

			encodeTypes.put(EncodeType.NONE, CommonProperties.getString("String_EncodeType_None"));
			encodeTypes.put(EncodeType.BYTE, CommonProperties.getString("String_EncodeType_Byte"));
			encodeTypes.put(EncodeType.INT16, CommonProperties.getString("String_EncodeType_Int16"));
			encodeTypes.put(EncodeType.INT24, CommonProperties.getString("String_EncodeType_Int24"));
			encodeTypes.put(EncodeType.INT32, CommonProperties.getString("String_EncodeType_Int32"));
			encodeTypes.put(EncodeType.DCT, CommonProperties.getString("String_EncodeType_DCT"));
			encodeTypes.put(EncodeType.SGL, CommonProperties.getString("String_EncodeType_SGL"));
			encodeTypes.put(EncodeType.LZW, CommonProperties.getString("String_EncodeType_LZW"));
			encodeTypes.put(EncodeType.PNG, CommonProperties.getString("String_EncodeType_PNG"));
			encodeTypes.put(EncodeType.COMPOUND, CommonProperties.getString("String_EncodeType_Compound"));
		}

		/**
		 * 根据编码类型资源字符串获取对应的编码类型
		 *
		 * @param typeName
		 *            编码类型
		 * @return 指定的编码
		 */
		public static EncodeType findType(String typeName) {
			EncodeType result = EncodeType.NONE;
			try {
				initialize();
				Iterator<?> iter = encodeTypes.entrySet().iterator();
				while (iter.hasNext()) {
					java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iter.next();
					String value = (String) entry.getValue();
					if (value.equals(typeName)) {
						result = (EncodeType) entry.getKey();
						break;
					}
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return result;
		}

		/**
		 * 根据编码类型获取对应的资源字符串
		 *
		 * @param type
		 *            编码类型
		 * @return 编码名称
		 */
		public static String findName(EncodeType type) {
			String result = "";
			try {
				initialize();
				if (encodeTypes.get(type) == null) {
					result = CommonProperties.getString("String_EncodeType_None");
				} else {
					result = encodeTypes.get(type);
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return result;
		}

		/**
		 * 验证编码类型是否存在于枚举中
		 *
		 * @param type
		 * @return
		 */
		public static boolean isValidType(EncodeType type) {
			boolean result = true;
			initialize();
			if (encodeTypes.get(type) == null) {
				result = false;
			}
			// return Enum.IsDefined(typeof(EncodeType), type);
			return result;
		}
	}

	public static class EngineTypeWrap {

		// / <summary>
		// /
		// / </summary>

		/**
		 * 根据指定的文件后缀获取相应的文件引擎类型
		 *
		 * @param fileExt
		 *            文件的扩展名
		 * @return 对应的引擎类型
		 */
		private EngineTypeWrap() {
			// 不提供构造函数
		}

		public static EngineType getEngineType(String fileExt) {
			EngineType engType = EngineType.UDB;
			EngineInfo[] loadedFileEngines = DatasourceUtilities.getLoadedFileEngines();

			if (loadedFileEngines != null) {
				for (EngineInfo engInfo : loadedFileEngines) {
					boolean find = false;
					for (String ext : engInfo.getSupportExtensions()) {
						if (fileExt.equalsIgnoreCase(ext)) {
							engType = engInfo.getType();
							find = true;
							break;
						}
					}

					if (find) {
						break;
					}
				}
			}
			return engType;
		}

		// /// <summary>
		// /// 根据引擎类型获得对应的资源信息
		// /// </summary>
		// public static String getEngineTypeName(EngineType engineType)
		// {
		// String engineTypeInfo = String.Empty;
		// try
		// {
		// if (engineType == EngineType.ImagePlugins)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_ImagePlugins;
		// }
		// else if (engineType == EngineType.OraclePlus)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_OraclePlus;
		// }
		// else if (engineType == EngineType.OracleSpatial)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_OracleSpatial;
		// }
		// else if (engineType == EngineType.SQLPlus)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_SQLPlus;
		// }
		// else if (engineType == EngineType.DMPlus)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_DMPlus;
		// }
		// else if (engineType == EngineType.DB2)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_DB2;
		// }
		// else if (engineType == EngineType.Kingbase)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_KingBase;
		// }
		// else if (engineType == EngineType.OGC)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_OGC;
		// }
		// else if (engineType == EngineType.VectorFile)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_VectorFile;
		// }
		// else if (engineType == EngineType.UDB)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_UDB;
		// }
		// else if (engineType == EngineType.PostgreSQL)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_PostgreSQL;
		// }
		// else if (engineType == EngineType.GoogleMaps)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_GoogleMaps;
		// }
		// else if (engineType == EngineType.SuperMapCloud)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_SuperMapCloud;
		// }
		// else if (engineType == EngineType.iServerRest)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_Rest;
		// }
		// else if (engineType == EngineType.MapWorld)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_MapWorld;
		// }
		// else if (engineType == EngineType.BaiduMaps)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_BaiduMaps;
		// }
		// else if (engineType == EngineType.OpenStreetMaps)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_OpenStreetMaps;
		// }
		// else if (engineType == EngineType.PCI)
		// {
		// engineTypeInfo = CoreResources.String_EngineType_PCI;
		// }
		// }
		// catch
		// {
		//
		// }
		// return engineTypeInfo;
		// }
		// #endregion
		//
		// #region Function_Event
		//
		// #endregion
		//
		// #region Function_Private
		//
		// #endregion
		//
		// #region Event
		//
		// #endregion
		//
		// #region InterfaceMembers
		//
		// #endregion
		//
		// #region NestedTypes
		//
		// #endregion
	}

	public static class FieldTypeWrap {
		private FieldTypeWrap() {
			// 默认私有构造器
		}

		// #region Variable
		//
		// #endregion
		//
		// #region Property
		//
		// #endregion
		//
		// #region Construct
		//
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 根据字段类型获取对应的资源字符串
		// /// </summary>
		// public static String getFieldTypeName(FieldType fieldType)
		// {
		// String type = CoreResources.String_FieldType_Unknown;
		// try
		// {
		// if (fieldType == FieldType.boolean)
		// {
		// type = CoreResources.String_FieldType_boolean;
		// }
		// else if (fieldType == FieldType.DateTime)
		// {
		// type = CoreResources.String_FieldType_Date;
		// }
		// else if (fieldType == FieldType.Double)
		// {
		// type = CoreResources.String_FieldType_Double;
		// }
		// else if (fieldType == FieldType.Byte)
		// {
		// type = CoreResources.String_FieldType_Byte;
		// }
		// else if (fieldType == FieldType.Int16)
		// {
		// type = CoreResources.String_FieldType_Int16;
		// }
		// else if (fieldType == FieldType.int)
		// {
		// type = CoreResources.String_FieldType_int;
		// }
		// else if (fieldType == FieldType.Int64)
		// {
		// type = CoreResources.String_FieldType_Int64;
		// }
		// else if (fieldType == FieldType.LongBinary)
		// {
		// type = CoreResources.String_FieldType_LongBinary;
		// }
		// else if (fieldType == FieldType.Char)
		// {
		// type = CoreResources.String_FieldType_Char;
		// }
		// else if (fieldType == FieldType.Single)
		// {
		// type = CoreResources.String_FieldType_Single;
		// }
		// else if (fieldType == FieldType.Text)
		// {
		// type = CoreResources.String_FieldType_Text;
		// }
		// else if (fieldType == FieldType.WText)
		// {
		// type = CoreResources.String_FieldType_WText;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return type;
		// }
		//
		// /// <summary>
		// /// 根据字段类型资源字符串获取对应的字段类型
		// /// </summary>
		// public static FieldType getFieldType(String strFieldType)
		// {
		// FieldType type = FieldType.Text;
		// try
		// {
		// if (strFieldType == CoreResources.String_FieldType_boolean)
		// {
		// type = FieldType.boolean;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_Date)
		// {
		// type = FieldType.DateTime;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_Double)
		// {
		// type = FieldType.Double;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_Byte)
		// {
		// type = FieldType.Byte;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_Int16)
		// {
		// type = FieldType.Int16;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_int)
		// {
		// type = FieldType.int;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_Int64)
		// {
		// type = FieldType.Int64;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_LongBinary)
		// {
		// type = FieldType.LongBinary;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_Char)
		// {
		// type = FieldType.Char;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_Single)
		// {
		// type = FieldType.Single;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_Text)
		// {
		// type = FieldType.Text;
		// }
		// else if (strFieldType == CoreResources.String_FieldType_WText)
		// {
		// type = FieldType.WText;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return type;
		// }
		// #endregion
		//
		// #region Function_Event
		//
		// #endregion
		//
		// #region Function_Private
		//
		// #endregion
		//
		// #region Event
		//
		// #endregion
		//
		// #region InterfaceMembers
		//
		// #endregion
		//
		// #region NestedTypes
		//
		// #endregion
	}

	public static class FilePathConverterWrap {
		private FilePathConverterWrap() {
			// 默认实现
		}

		// public static String getFileName(String filePath)
		// {
		// return Path.getFileName(filePath);
		// }
		//
		// public static String getRecentFilePath(String filePath)
		// {
		// String result = filePath.Replace("/", " > ");
		// result = result.Replace("\\", " > ");
		// return result;
		// }
	}

	public static class FillMode3DWrap {
		private FillMode3DWrap() {
			// 默认实现
		}

		// #region Variable
		//
		// #endregion
		//
		// #region Property
		// static Dictionary<FillMode3D, String> sceneFillMode3Ds = null;
		// /// <summary>
		// /// 三维面填充方式与引用资源字典
		// /// </summary>
		// private static Dictionary<FillMode3D, String> SceneFillMode3Ds
		// {
		// get
		// {
		// if (sceneFillMode3Ds == null)
		// {
		// sceneFillMode3Ds = new Dictionary<FillMode3D, String>();
		// sceneFillMode3Ds.Add(FillMode3D.Fill,
		// CoreResources.String_FillMode3D_Fill);
		// sceneFillMode3Ds.Add(FillMode3D.Line,
		// CoreResources.String_FillMode3D_Line);
		// sceneFillMode3Ds.Add(FillMode3D.LineAndFill,
		// CoreResources.String_FillMode3D_LineAndFill);
		// }
		// return sceneFillMode3Ds;
		// }
		// }
		// #endregion
		//
		// #region Construct
		//
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 根据资源字符串获取对应的三维面填充方式
		// /// </summary>
		// /// <param name="name"></param>
		// /// <returns></returns>
		// public static FillMode3D getFillMode3D(String name)
		// {
		// FillMode3D result = FillMode3D.LineAndFill;
		//
		// try
		// {
		// foreach (KeyValuePair<FillMode3D, String> kvp in SceneFillMode3Ds)
		// {
		// if (kvp.Value.Equals(name))
		// {
		// result = kvp.Key;
		// break;
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return result;
		// }
		//
		// /// <summary>
		// /// 根据三维面填充方式获取对应的资源字符串
		// /// </summary>
		// /// <param name="fillMode3D"></param>
		// /// <returns></returns>
		// public static String getFillMode3DName(FillMode3D fillMode3D) {
		// String strName = CoreResources.String_Unknown; // "Unknown";
		// try {
		// strName = SceneFillMode3Ds[fillMode3D];
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return strName;
		// }
		// #endregion
		//
		// #region Function_Event
		//
		// #endregion
		//
		// #region Function_Private
		//
		// #endregion
		//
		// #region Event
		//
		// #endregion
		//
		// #region InterfaceMembers
		//
		// #endregion
		//
		// #region NestedTypes
		//
		// #endregion
	}

	public static class FormWrap {

		// private static SymbolDialog symbolDialog;
		private static transient CopyOnWriteArrayList<NewWindowListener> newWindowListeners = new CopyOnWriteArrayList<NewWindowListener>();

		private FormWrap() {
			// 不提供构造函数
		}

		public static synchronized void addNewWindowListener(NewWindowListener listener) {
			if (newWindowListeners == null) {
				newWindowListeners = new CopyOnWriteArrayList<NewWindowListener>();
			}

			if (!newWindowListeners.contains(listener)) {
				newWindowListeners.add(listener);
			}
		}

		public static void removeNewWindowListener(NewWindowListener listener) {
			if (newWindowListeners != null && newWindowListeners.contains(listener)) {
				newWindowListeners.remove(listener);
			}
		}

		/**
		 * 发送创建子窗体的事件
		 *
		 * @param windowType
		 *            子窗口类型
		 * @return
		 */
		public static IForm fireNewWindowEvent(WindowType windowType) {
			return fireNewWindowEvent(windowType, "");
		}

		/**
		 * 发送创建子窗体的事件
		 *
		 * @param windowType
		 *            子窗口类型
		 * @param name
		 *            指定的窗口名称，如果是地图、场景或者布局子窗体，则默认打开指定名称的地图、布局或者场景。
		 * @return
		 */
		public static IForm fireNewWindowEvent(WindowType windowType, String name) {
			IForm childForm = null;
			try {
				NewWindowEvent event = new NewWindowEvent(Application.getActiveApplication().getMainFrame(), windowType);
				event.setNewWindowName(name);
				fireNewWindowEvent(event);
				childForm = event.getNewWindow();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
			return childForm;
		}

		private static void fireNewWindowEvent(NewWindowEvent event) {
			if (newWindowListeners != null) {
				CopyOnWriteArrayList<NewWindowListener> listeners = newWindowListeners;
				Iterator<NewWindowListener> iter = listeners.iterator();
				while (iter.hasNext()) {
					NewWindowListener listener = iter.next();
					listener.newWindow(event);
				}
			}
		}

		public static WindowType getWindowType(IForm form) {
			WindowType type = WindowType.MAP;
			try {
				if (form instanceof IFormLayout) {
					type = WindowType.LAYOUT;
				} else if (form instanceof IFormScene) {
					type = WindowType.SCENE;
				} else if (form instanceof IFormTabular) {
					type = WindowType.TABULAR;
				}else if(form instanceof IFormLBSControl){
					type = WindowType.LBSCONTROL;
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}

			return type;
		}

		public static IForm getForm(String title, WindowType WindowType) {
			IForm form = null;
			try {
				for (int i = 0; i < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); i++) {
					form = Application.getActiveApplication().getMainFrame().getFormManager().get(i);
					if (title.equalsIgnoreCase(form.getText()) && WindowType == getWindowType(form)) {
						break;
					} else {
						form = null;
					}
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}

			return form;
		}

		// /// <summary>
		// /// 触发创建新窗口事件
		// /// </summary>
		// /// <param name="NewWindowEventArgs">创建新窗口参数类</param>
		// /// <returns></returns>
		// public static JFrame SendNewWindowEvent(NewWindowEventArgs args)
		// {
		// JFrame jFrame = null;
		// try
		// {
		// if (NewWindowEvent != null && args != null)
		// {
		// NewWindowEvent(null, args);
		// jFrame = args.NewForm;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return jFrame;
		// }
		//
		// /// <summary>
		// /// 将所有的打开的窗口置为需要保存状态
		// /// </summary>
		// public static void SetWindowsNeedSaveState(boolean bNeedSave)
		// {
		// try
		// {
		// for (int i = 0; i <
		// Application.getActiveApplication().getMainFrame().getFormManager().Count;
		// i++)
		// {
		// IForm form =
		// Application.getActiveApplication().getMainFrame().getFormManager().get(i)
		// as IForm;
		// form.NeedSave = bNeedSave;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		//
		// / <summary>
		// /
		// / </summary>

		/**
		 * 保存所有打开的窗口内容
		 *
		 * @return 保存成功返回 true，否则返回 false
		 */
		public static boolean saveAllOpenedWindows() {
			boolean result = false;
			try {
				result = Application.getActiveApplication().getMainFrame().getFormManager().saveAll(GlobalParameters.isShowFormClosingInfo());
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}

			return result;
		}

		// /// <summary>
		// /// 根据参数初始化符号对话框。
		// /// </summary>
		// public static SymbolDialog InitSymbolDialog(Resources resource,
		// SymbolType symbolType, Object style, boolean useApplyButton)
		// {
		// SymbolDialog result = null;
		// try
		// {
		// //var timer = new System.Diagnostics.Stopwatch();
		// //timer.Start();
		// if (symbolDialog == null)
		// {
		// symbolDialog = new SymbolDialog();
		// Application.getActiveApplication().getWorkspace().Closed += new
		// WorkspaceClosedEventHandler(Workspace_Closed);
		// }
		// symbolDialog.UseApplyButton = useApplyButton;
		// symbolDialog.Resources = resource;
		// symbolDialog.getType() = symbolType;
		// Symbol symbol = null;
		// if (style is GeoStyle)
		// {
		// symbolDialog.Style = (style as GeoStyle);
		// switch (symbolType)
		// {
		// case SymbolType.Fill:
		// case SymbolType.Fill3D:
		// symbol = resource.FillLibrary.FindSymbol((style as
		// GeoStyle).FillSymbolID);
		// break;
		// case SymbolType.Line:
		// case SymbolType.Line3D:
		// symbol = resource.LineLibrary.FindSymbol((style as
		// GeoStyle).LineSymbolID);
		// break;
		// case SymbolType.Marker:
		// case SymbolType.Marker3D:
		// symbol = resource.MarkerLibrary.FindSymbol((style as
		// GeoStyle).MarkerSymbolID);
		// break;
		// default:
		// break;
		// }
		// }
		// else if (style is GeoStyle3D)
		// {
		// symbolDialog.Style3D = (style as GeoStyle3D);
		// switch (symbolType)
		// {
		// case SymbolType.Fill:
		// case SymbolType.Fill3D:
		// symbol = resource.FillLibrary.FindSymbol((style as
		// GeoStyle3D).FillSymbolID);
		// break;
		// case SymbolType.Line:
		// case SymbolType.Line3D:
		// symbol = resource.LineLibrary.FindSymbol((style as
		// GeoStyle3D).LineSymbolID);
		// break;
		// case SymbolType.Marker:
		// case SymbolType.Marker3D:
		// symbol = resource.MarkerLibrary.FindSymbol((style as
		// GeoStyle3D).MarkerSymbolID);
		// break;
		// default:
		// break;
		// }
		// }
		// else
		// {
		// symbolDialog.Style = new GeoStyle();
		// }
		// if (symbol != null)
		// {
		// symbolDialog.getType() = symbol.getType();
		// }
		// else
		// {
		// symbolDialog.getType() = symbolType;
		// }
		// result = symbolDialog;
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return result;
		// }
		//
		// static void Workspace_Closed(object sender, WorkspaceClosedEventArgs
		// args)
		// {
		// try
		// {
		// args.Workspace.Closed -= new
		// WorkspaceClosedEventHandler(Workspace_Closed);
		// if (symbolDialog != null)
		// {
		// symbolDialog.Dispose();
		// symbolDialog = null;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		// }

		// public static event NewWindowEventHandler NewWindowEvent;
		// public delegate void NewWindowEventHandler(Object sender,
		// NewWindowEventArgs e);
		// public class NewWindowEventArgs : EventArgs
		// {
		// public NewWindowEventArgs(NewWindowType newWindowType, Form form)
		// {
		// newWindowType = newWindowType;
		// newForm = form;
		// }
		//
		// private Form newForm;
		// public Form NewForm
		// {
		// get
		// {
		// return newForm;
		// }
		// set
		// {
		// newForm = value;
		// }
		// }
		//
		// private NewWindowType newWindowType;
		// public NewWindowType NewWindowType
		// {
		// get
		// {
		// return newWindowType;
		// }
		// }
		//
		// private Object param;
		// public Object Param
		// {
		// get
		// {
		// return param;
		// }
		// set
		// {
		// param = value;
		// }
		// }
		// }
	}

	public static class GeometryTypeWrap {
		private GeometryTypeWrap() {
			// 默认实现
		}

		// /// <summary>
		// /// 根据几何对象类型资源字符串获取对应的几何对象类型
		// /// </summary>

	}

	public static class GeometryWrap {
		private GeometryWrap() {
			// 默认实现
		}

		// /// <summary>
		// /// 释放对象
		// /// </summary>
		// /// <param name="geometry">被释放的对象</param>
		// public static void ReleaseGeometry(ref Geometry geometry)
		// {
		// try
		// {
		// if (geometry != null)
		// {
		// geometry.Dispose();
		// geometry = null;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		//
		// public static void ReleaseGeometry(ref GeoPoint geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoLine geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoRegion geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoText geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoRectangle geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoRoundRectangle geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoCircle geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoEllipse geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoPie geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoChord geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoArc geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoEllipticArc geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoCardinal geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoCurve geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoBSpline geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoLineM geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoPoint3D geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoLine3D geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoRegion3D geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoText3D geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoPlacemark geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoCompound geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoPicture geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoModel geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoPicture3D geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoSphere geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoHemiSphere geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoBox geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoCylinder geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoCone geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoPyramid geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoPie3D geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoCircle3D geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoPieCylinder geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoEllipsoid geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoParticle geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoMap geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoMapScale geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoNorthArrow geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoMapBorder geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static void ReleaseGeometry(ref GeoLegend geometry)
		// {
		// Geometry geoBase = geometry as Geometry;
		// ReleaseGeometry(ref geoBase);
		// geometry = null;
		// }
		//
		// public static class GeometryWrap
		// {
		// public static GeoRegion ConvertToRegion(Geometry geometry, int
		// segmentCount)
		// {
		// if (geometry != null)
		// {
		// return geometry.ToRegion(segmentCount);
		// }
		// return null;
		// }
		// }
	}

	public static class KeyboardWrap {
		private KeyboardWrap() {
			// 默认实现
		}

		// #region Variable
		// /// <summary>
		// /// （钩子监控到键盘操作时发出的）HookKeyboard事件对应的委托
		// /// </summary>
		// public delegate IntPtr HookKeyboardEventHandler(int code, KeyMessage
		// message, KBDLLHOOKSTRUCT param);
		//
		// /// <summary>
		// /// 设置Window钩子的回调函数委托
		// /// </summary>
		// private delegate IntPtr HookKeyboardDelegate(int code, IntPtr wParam,
		// IntPtr lParam);
		// private static HookKeyboardDelegate hookKeyboardProc;
		// private static IntPtr hookKeyboardHandle;
		//
		// private static Keys[] keys;
		// #endregion
		//
		// #region Property
		// /// <summary>
		// /// Keys枚举所有成员的集合
		// /// </summary>
		// public static Keys[] AllKeys
		// {
		// get
		// {
		// if (keys == null)
		// {
		// List<Keys> keys = new List<Keys>();
		// foreach (int i in Enum.getValues(typeof(Keys)))
		// {
		// keys.Add((Keys)i);
		// }
		// keys = keys.ToArray();
		// }
		// return keys;
		// }
		// }
		// #endregion
		//
		// #region Construct
		//
		// #endregion
		//
		// #region Function_Public
		// [DllImport("user32.dll", CharSet = CharSet.Auto, CallingConvention =
		// CallingConvention.StdCall)]
		// private static extern IntPtr SetWindowsHookEx(int idHook,
		// HookKeyboardDelegate lpfn, IntPtr hMod, int dwThreadId);
		//
		// [DllImport("user32.dll", CharSet = CharSet.Auto, CallingConvention =
		// CallingConvention.StdCall)]
		// private static extern boolean UnhookWindowsHookEx(IntPtr hhk);
		//
		// [DllImport("user32.dll", CharSet = CharSet.Auto, CallingConvention =
		// CallingConvention.StdCall)]
		// private static extern IntPtr CallNextHookEx(IntPtr hhk, int nCode,
		// IntPtr wParam, IntPtr lParam);
		//
		// /// <summary>
		// /// 注册键盘监控
		// /// </summary>
		// public static void HookWindowsHookEx()
		// {
		// hookKeyboardProc = new HookKeyboardDelegate(HookKeyboardProc);
		// IntPtr handle = Process.getCurrentProcess().MainModule.BaseAddress;
		// hookKeyboardHandle = SetWindowsHookEx((int)HookType.WH_KEYBOARD_LL,
		// hookKeyboardProc, handle, 0);
		// }
		//
		// /// <summary>
		// /// 反注册键盘监控
		// /// </summary>
		// public static void UnhookWindowsHookEx()
		// {
		// UnhookWindowsHookEx(hookKeyboardHandle);
		// }
		//
		// #endregion
		//
		// #region Function_Event
		//
		// #endregion
		//
		// #region Function_Private
		// /// <summary>
		// /// 根据Keys枚举成员的字符串表示，转换得到Keys枚举
		// /// </summary>
		// /// <param name="name"></param>
		// /// <returns></returns>
		// public static Keys getKeysByName(String name)
		// {
		// Keys result = Keys.None;
		// if
		// (name.toLowerCase().Equals(CoreResources.String_ShortCut_Ctrl.toLowerCase()))
		// {
		// result = Keys.Control;
		// }
		// else
		// {
		// foreach (Keys key in AllKeys)
		// {
		// if (key.ToString().Equals(name))
		// {
		// result = key;
		// break;
		// }
		// }
		// }
		// return result;
		// }
		//
		// /// <summary>
		// /// 键盘监控回调函数，里面激活键盘事件
		// /// </summary>
		// /// <param name="code"></param>
		// /// <param name="wParam"></param>
		// /// <param name="lParam"></param>
		// /// <returns></returns>
		// private static IntPtr HookKeyboardProc(int code, IntPtr wParam,
		// IntPtr lParam)
		// {
		// if (HookKeyboard != null && code >= 0)
		// {
		// KBDLLHOOKSTRUCT param =
		// (KBDLLHOOKSTRUCT)Marshal.PtrToStructure(lParam,
		// typeof(KBDLLHOOKSTRUCT));
		// HookKeyboard(code, (KeyMessage)wParam, param);
		// }
		//
		// return CallNextHookEx(hookKeyboardHandle, code, wParam, lParam);
		// }
		// #endregion
		//
		// #region Event
		// /// <summary>
		// /// 钩子监控到键盘操作时触发
		// /// </summary>
		// public static event HookKeyboardEventHandler HookKeyboard;
		// #endregion
		//
		// #region InterfaceMembers
		//
		// #endregion
		//
		// #region NestedTypes
		//
		// #endregion
	}

	public static class ListWrap {
		private ListWrap() {
			// 默认私有构造器
		}

		// public static boolean IsListOnlyContain<T>(List<T> list, params T[]
		// checkItems)
		// {
		// boolean result = false;
		// if (list.Count > 0 && checkItems.Length > 0)
		// {
		// result = list.TrueForAll(item =>
		// {
		// boolean equal = false;
		// foreach (T checkItem in checkItems)
		// {
		// if (checkItem.Equals(item))
		// {
		// equal = true;
		// break;
		// }
		// }
		// return equal;
		// });
		// }
		// return result;
		// }
		//
		// public static boolean IsListContainAny<T>(List<T> list, params T[]
		// checkItems)
		// {
		// boolean result = false;
		// if (list.Count > 0 && checkItems.Length > 0)
		// {
		// result = list.Exists(item =>
		// {
		// boolean equal = false;
		// foreach (T checkItem in checkItems)
		// {
		// if (checkItem.Equals(item))
		// {
		// equal = true;
		// break;
		// }
		// }
		// return equal;
		// });
		// }
		// return result;
		// }
	}

	public static class MappingSpecificationWrap {
		private MappingSpecificationWrap() {
			// 默认实现
		}

		// public static String PathFeatureDic =
		// CoreResources.String_AutoMapMaking_DictionaryPathRoot +
		// "FeatureDictionary.xml";
		// public static String PathDisplayDic =
		// CoreResources.String_AutoMapMaking_DictionaryPathRoot +
		// "EmapDisplayStandard.xml";
		// public static String pathspecialSymbolDic =
		// CoreResources.String_AutoMapMaking_DictionaryPathRoot +
		// "SpecialSymbolDictionary.xml";
		// private static XmlDocument featureDictionay;
		// private static XmlDocument emapDisplayStandard;
		// private static XmlDocument specialSymbolDictionay;
		//
		// public static XmlDocument FeatureDictionay
		// {
		// get
		// {
		// if (featureDictionay == null)
		// {
		// featureDictionay = new XmlDocument();
		// featureDictionay.Load(PathFeatureDic);
		// }
		//
		// return featureDictionay;
		// }
		// }
		//
		// public static XmlDocument EmapDisplayStandard
		// {
		// get
		// {
		// if (emapDisplayStandard == null)
		// {
		// emapDisplayStandard = new XmlDocument();
		// emapDisplayStandard.Load(PathDisplayDic);
		// }
		//
		// return emapDisplayStandard;
		// }
		// }
		//
		// public static XmlDocument SpecialSymbolDictionay
		// {
		// get
		// {
		// if (specialSymbolDictionay == null)
		// {
		// specialSymbolDictionay = new XmlDocument();
		// specialSymbolDictionay.Load(pathspecialSymbolDic);
		// }
		// return specialSymbolDictionay;
		// }
		// }
		//
		// public static void ReloadFeatureDictionay()
		// {
		// if (featureDictionay != null)
		// {
		// featureDictionay.Load(PathFeatureDic);
		// }
		// }
		//
		// public static void ReloadEmapDisplayStandard()
		// {
		// if (emapDisplayStandard != null)
		// {
		// emapDisplayStandard.Load(PathDisplayDic);
		// }
		// }
		//
		// public static void ReloadSpecialSymbolDictionay()
		// {
		// if (specialSymbolDictionay != null)
		// {
		// specialSymbolDictionay.Load(pathspecialSymbolDic);
		// }
		// }
	}

	public static class PixelformatWrap {
		private PixelformatWrap() {
			// 默认实现
		}

		// #region Variable
		// static Dictionary<Pixelformat, String> pixelformats = null;
		// #endregion
		//
		// #region Property
		// #endregion
		//
		// #region Construct
		// static PixelformatWrap()
		// {
		// Initialize();
		// }
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 获取数据集是否支持指定的像素格式
		// /// </summary>
		// public static boolean IsSupportedPixelformat(DatasetType datasetType,
		// Pixelformat pixelformat)
		// {
		// boolean result = true;
		//
		// if (datasetType == DatasetType.Grid || datasetType ==
		// DatasetType.GridCollection)
		// {
		// if (pixelformat == Pixelformat.RGB || pixelformat ==
		// Pixelformat.RGBA)
		// {
		// result = false;
		// }
		// }
		// return result;
		// }
		//
		// /// <summary>
		// /// 根据数据集类型和像素类型获取对应的资源字符串
		// /// </summary>
		// public static String getPixelformatName(Pixelformat pixelformat)
		// {
		// String strName = CoreResources.String_Unknown;
		// if (pixelformats.ContainsKey(pixelformat))
		// {
		// strName = pixelformats[pixelformat];
		// }
		// return strName;
		// }
		// #endregion
		//
		// #region Function_Event
		//
		// #endregion
		//
		// #region Function_Private
		// private static void Initialize()
		// {
		// pixelformats = new Dictionary<Pixelformat, String>();
		// pixelformats.Add(Pixelformat.UBit1,
		// CommonResources.String_Pixelformat_UBit1);
		// pixelformats.Add(Pixelformat.UBit4,
		// CommonResources.String_Pixelformat_UBit4);
		// pixelformats.Add(Pixelformat.Bit8,
		// CommonResources.String_Pixelformat_Bit8);
		// pixelformats.Add(Pixelformat.UBit8,
		// CommonResources.String_Pixelformat_UBit8);
		// pixelformats.Add(Pixelformat.Bit16,
		// CommonResources.String_Pixelformat_Bit16);
		// pixelformats.Add(Pixelformat.UBit16,
		// CommonResources.String_Pixelformat_UBit16);
		// pixelformats.Add(Pixelformat.RGB,
		// CommonResources.String_Pixelformat_RGB);
		// pixelformats.Add(Pixelformat.RGBA,
		// CommonResources.String_Pixelformat_RGBA);
		// pixelformats.Add(Pixelformat.Bit32,
		// CommonResources.String_Pixelformat_Bit32);
		// pixelformats.Add(Pixelformat.UBit32,
		// CommonResources.String_Pixelformat_UBit32);
		// pixelformats.Add(Pixelformat.Bit64,
		// CommonResources.String_Pixelformat_Bit64);
		// pixelformats.Add(Pixelformat.Single,
		// CommonResources.String_Pixelformat_Single);
		// pixelformats.Add(Pixelformat.Double,
		// CommonResources.String_Pixelformat_Double);
		// }
		//
	}

	public static class RecordsetWrap {
		private RecordsetWrap() {
			// 默认实现
		}

		// /// <summary>
		// /// 获得一个矢量图层对应数据集的记录集，包含连接信息
		// /// </summary>
		// public static Recordset getJoinRecordset(Object layer, boolean
		// onlyFieldInfos)
		// {
		// Recordset recordset = null;
		// try
		// {
		// DatasetVector dataset = null;
		// if (layer is Layer)
		// {
		// dataset = (layer as Layer).Dataset as DatasetVector;
		// }
		// else if (layer is Layer3DDataset)
		// {
		// dataset = (layer as Layer3DDataset).Dataset as DatasetVector;
		// }
		// JoinItems joinItems = CommonToolkit.LayerWrap.getJoinItems(layer);
		// recordset = getJoinRecordset(dataset, onlyFieldInfos, joinItems);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return recordset;
		// }
		//
		// //public static FieldInfos getJoinFieldInfos(Object layer)
		// //{
		// // FieldInfos fieldInfos = null;
		// // try
		// // {
		// // DatasetVector dataset = null;
		// // if (layer is Layer)
		// // {
		// // dataset = (layer as Layer).Dataset as DatasetVector;
		// // }
		// // else if (layer is Layer3DDataset)
		// // {
		// // dataset = (layer as Layer3DDataset).Dataset as DatasetVector;
		// // }
		// // JoinItems joinItems = CommonToolkit.LayerWrap.getJoinItems(layer);
		// // fieldInfos = getJoinFieldInfos(dataset, joinItems);
		// // }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// // return fieldInfos;
		// //}
		//
		// ///// <summary>
		// ///// 获得一个矢量数据集带指定连接信息的记录集
		// ///// </summary>
		// //public static Recordset getJoinRecordset(DatasetVector dataset,
		// boolean onlyFieldInfos, JoinItems joinItems)
		// //{
		// // Recordset recordset = null;
		// // try
		// // {
		// // QueryParameter queryparameter = new QueryParameter();
		// // queryparameter.CursorType = CursorType.Static;
		// // queryparameter.HasGeometry = false;
		// // queryparameter.JoinItems.AddRange(joinItems.ToArray());
		// // List<DatasetVector> datasetList = new List<DatasetVector>();
		// // datasetList.Add(dataset);
		// // for (int i = 0; i < joinItems.Count; i++)
		// // {
		// // JoinItem joinItem = joinItems.get(i);
		// // String joinDatasetName = joinItem.ForeignTable;
		// // DatasetVector joinDataset =
		// dataset.Datasource.Datasets[joinDatasetName] as DatasetVector;
		// // if (joinDataset != null)
		// // {
		// // datasetList.Add(joinDataset);
		// // }
		// // }
		//
		// // List<String> resultFields = new List<String>();
		// // foreach (DatasetVector data in datasetList)
		// // {
		// // QueryParameter para = new QueryParameter();
		// // para.CursorType = CursorType.Static;
		// // para.HasGeometry = false;
		// // para.AttributeFilter = "1<0";
		// // Recordset cord = null;
		// // try
		// // {
		// // cord = data.Query(para);
		// // FieldInfos fieldInfos = cord.getFieldInfos();
		// // foreach (SuperMap.Data.FieldInfo info in fieldInfos)
		// // {
		// // resultFields.Add(data.TableName + "." + info.Name);
		// // }
		// // cord.Dispose();
		// // cord = null;
		// // }
		// // catch
		// // {
		// // if (cord != null)
		// // {
		// // cord.Dispose();
		// // }
		// // }
		// // }
		// // queryparameter.ResultFields = resultFields.ToArray();
		// // if (onlyFieldInfos)
		// // {
		// // queryparameter.AttributeFilter = "1<0";
		// // }
		// // recordset = dataset.Query(queryparameter);
		// // }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// // return recordset;
		// //}
		//
		// /// <summary>
		// /// 获得一个矢量数据集带指定连接信息的记录集
		// /// </summary>
		// public static Recordset getJoinRecordset(DatasetVector dataset,
		// boolean onlyFieldInfos, JoinItems joinItems)
		// {
		// Recordset recordset = null;
		// try
		// {
		// QueryParameter queryparameter = new QueryParameter();
		// queryparameter.CursorType = CursorType.Static;
		// queryparameter.HasGeometry = false;
		// queryparameter.JoinItems.AddRange(joinItems.ToArray());
		// List<String> resultFields = getJoinFieldNames(dataset, joinItems);
		// queryparameter.ResultFields = resultFields.ToArray();
		// if (onlyFieldInfos)
		// {
		// queryparameter.AttributeFilter = "1<0";
		// }
		// recordset = dataset.Query(queryparameter);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return recordset;
		// }
		//
		// /// <summary>
		// /// 获得一个矢量数据集带指定连接信息的记录集,同时输出带表名的字段名列表
		// /// </summary>
		// public static Recordset getJoinRecordset(DatasetVector dataset,
		// boolean onlyFieldInfos, JoinItems joinItems, out List<String>
		// resultFields)
		// {
		// Recordset recordset = null;
		// resultFields = null;
		// try
		// {
		// QueryParameter queryparameter = new QueryParameter();
		// queryparameter.CursorType = CursorType.Static;
		// queryparameter.HasGeometry = false;
		// queryparameter.JoinItems.AddRange(joinItems.ToArray());
		// resultFields = getJoinFieldNames(dataset, joinItems);
		// queryparameter.ResultFields = resultFields.ToArray();
		// if (onlyFieldInfos)
		// {
		// queryparameter.AttributeFilter = "1<0";
		// }
		// recordset = dataset.Query(queryparameter);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return recordset;
		// }
		//
		// /// <summary>
		// /// 获取图层的字段集合，带连接信息
		// /// </summary>
		// /// <param name="layer"></param>
		// /// <returns></returns>
		// public static List<FieldInfo> getJoinFieldInfos(Object layer)
		// {
		// List<FieldInfo> fieldInfos = null;
		// try
		// {
		// DatasetVector dataset = null;
		// if (layer is Layer)
		// {
		// dataset = (layer as Layer).Dataset as DatasetVector;
		// }
		// else if (layer is Layer3DDataset)
		// {
		// dataset = (layer as Layer3DDataset).Dataset as DatasetVector;
		// }
		//
		// JoinItems joinItems = CommonToolkit.LayerWrap.getJoinItems(layer);
		// fieldInfos = getJoinFieldInfos(dataset, joinItems);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		//
		// return fieldInfos;
		// }
		//
		// /// <summary>
		// /// 获取指定图层的字段名集合，字段带表名
		// /// </summary>
		// /// <param name="layer"></param>
		// /// <returns></returns>
		// public static List<String> getJoinFieldNames(Object layer)
		// {
		// List<String> fields = null;
		// try
		// {
		// DatasetVector dataset = null;
		// if (layer is Layer)
		// {
		// dataset = (layer as Layer).Dataset as DatasetVector;
		// }
		// else if (layer is Layer3DDataset)
		// {
		// dataset = (layer as Layer3DDataset).Dataset as DatasetVector;
		// }
		//
		// JoinItems joinItems = CommonToolkit.LayerWrap.getJoinItems(layer);
		// fields = getJoinFieldNames(dataset, joinItems);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		//
		// return fields;
		// }
		//
		// /// <summary>
		// /// 获取指定图层的字段名集合，字段不带表名
		// /// </summary>
		// /// <param name="layer"></param>
		// /// <returns></returns>
		// public static List<String> getJoinFieldNamesWithoutDatasetName(Object
		// layer)
		// {
		// List<String> fields = null;
		// try
		// {
		// DatasetVector dataset = null;
		// if (layer is Layer)
		// {
		// dataset = (layer as Layer).Dataset as DatasetVector;
		// }
		// else if (layer is Layer3DDataset)
		// {
		// dataset = (layer as Layer3DDataset).Dataset as DatasetVector;
		// }
		//
		// JoinItems joinItems = CommonToolkit.LayerWrap.getJoinItems(layer);
		// fields = getJoinFieldNamesWithoutDatasetName(dataset, joinItems);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return fields;
		// }
		//
		// /// <summary>
		// /// 获取指定图层的字段名以及所属数据集
		// /// 字段名带表名
		// /// </summary>
		// /// <param name="layer"></param>
		// /// <returns></returns>
		// public static Dictionary<String, DatasetVector>
		// getJoinFieldNamesAndDataset(Object layer)
		// {
		// Dictionary<String, DatasetVector> result = new Dictionary<String,
		// DatasetVector>();
		// try
		// {
		// List<DatasetVector> datasetList = getJoinDatasets(layer);
		// for (int i = 0; i < datasetList.Count; i++)
		// {
		// try
		// {
		// FieldInfos fieldInfos = datasetList.get(i).FieldInfos;
		// String datasetName =
		// String.IsNullOrEmpty(datasetList.get(i).TableName) ? "" :
		// datasetList.get(i).Name + ".";
		// for (int fieldIndex = 0; fieldIndex < fieldInfos.Count; fieldIndex++)
		// {
		// String fullFieldName = datasetName + fieldInfos[fieldIndex].Name;
		// result[fullFieldName] = datasetList.get(i);
		// }
		// }
		// catch
		// {
		//
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		//
		// return result;
		// }
		//
		// /// <summary>
		// /// 获取指定图层的字段名以及对应的字段信息
		// /// 字段名带表名
		// /// </summary>
		// /// <param name="layer"></param>
		// /// <returns></returns>
		// public static Dictionary<String, FieldInfo>
		// getJoinFieldNamesAndFieldInfo(Object layer)
		// {
		// Dictionary<String, FieldInfo> result = new Dictionary<String,
		// FieldInfo>();
		// try
		// {
		// List<DatasetVector> datasetList = getJoinDatasets(layer);
		// for (int i = 0; i < datasetList.Count; i++)
		// {
		// try
		// {
		// FieldInfos fieldInfos = datasetList.get(i).FieldInfos;
		// String datasetName =
		// String.IsNullOrEmpty(datasetList.get(i).TableName) ? "" :
		// datasetList.get(i).Name + ".";
		// for (int fieldIndex = 0; fieldIndex < fieldInfos.Count; fieldIndex++)
		// {
		// String fullFieldName = datasetName + fieldInfos[fieldIndex].Name;
		// result[fullFieldName] = fieldInfos[fieldIndex];
		// }
		// }
		// catch
		// {
		//
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		//
		// return result;
		// }
		//
		// /// <summary>
		// /// 获取指定图层关联的数据集集合
		// /// 数据集集合含外部连接表对应的数据集
		// /// </summary>
		// /// <param name="layer"></param>
		// /// <returns></returns>
		// public static List<DatasetVector> getJoinDatasets(Object layer)
		// {
		// List<DatasetVector> datasets = new List<DatasetVector>();
		// try
		// {
		// DatasetVector dataset = null;
		// if (layer is Layer)
		// {
		// dataset = (layer as Layer).Dataset as DatasetVector;
		// }
		// else if (layer is Layer3DDataset)
		// {
		// dataset = (layer as Layer3DDataset).Dataset as DatasetVector;
		// }
		// JoinItems joinItems = CommonToolkit.LayerWrap.getJoinItems(layer);
		// datasets = getJoinDatasets(dataset, joinItems);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		//
		// return datasets;
		// }
		//
		// /// <summary>
		// /// 获取指定图层关联的数据集集合
		// /// 数据集集合含外部连接表对应的数据集
		// /// </summary>
		// /// <param name="layer"></param>
		// /// <returns></returns>
		// public static List<DatasetVector> getJoinDatasets(DatasetVector
		// dataset, JoinItems joinItems)
		// {
		// List<DatasetVector> datasets = new List<DatasetVector>();
		// try
		// {
		// datasets.Add(dataset);
		//
		// for (int i = 0; joinItems != null && i < joinItems.Count; i++)
		// {
		// JoinItem joinItem = joinItems.get(i);
		// String joinDatasetName = joinItem.ForeignTable;
		// DatasetVector joinDataset =
		// dataset.Datasource.Datasets[joinDatasetName] as DatasetVector;
		// if (joinDataset != null)
		// {
		// datasets.Add(joinDataset);
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		//
		// return datasets;
		// }
		//
		// /// <summary>
		// /// 获得一个矢量数据集带指定连接信息的记录集,同时输出带表名的字段名列表
		// /// </summary>
		// public static Recordset getJoinRecordset(DatasetVector dataset,
		// boolean onlyFieldInfos, JoinItems joinItems, out Dictionary<String,
		// String> resultFields)
		// {
		// Recordset recordset = null;
		// resultFields = null;
		// try
		// {
		// QueryParameter queryparameter = new QueryParameter();
		// queryparameter.CursorType = CursorType.Static;
		// queryparameter.HasGeometry = false;
		// queryparameter.JoinItems.AddRange(joinItems.ToArray());
		// resultFields = getJoinFieldNamesDic(dataset, joinItems);
		// int index = 0;
		// String[] resultFieldsName = new String[resultFields.Count];
		// queryparameter.ResultFields = new String[resultFields.Count];
		// foreach (KeyValuePair<String, String> item in resultFields)
		// {
		// resultFieldsName[index] = item.Value;
		// index++;
		// }
		// queryparameter.ResultFields = resultFieldsName;
		// if (onlyFieldInfos)
		// {
		// queryparameter.AttributeFilter = "1<0";
		// }
		// recordset = dataset.Query(queryparameter);
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return recordset;
		// }
		//
		//
		// /// <summary>
		// /// 获得一个带指定连接信息的矢量数据集的字段名集合，字段名包含表名
		// /// </summary>
		// public static List<String> getJoinFieldNames(DatasetVector dataset,
		// JoinItems joinItems)
		// {
		// List<String> resultFields = new List<String>();
		// try
		// {
		// List<DatasetVector> datasetList = getJoinDatasets(dataset,
		// joinItems);
		// //这里使用for循环，为了保证结果的顺序与获取字段集合的方法一致
		// for (int i = 0; i < datasetList.Count; i++)
		// {
		// try
		// {
		// FieldInfos fieldInfos = datasetList.get(i).FieldInfos;
		// String datasetName =
		// String.IsNullOrEmpty(datasetList.get(i).TableName) ? "" :
		// datasetList.get(i).Name + ".";
		// for (int fieldIndex = 0; fieldIndex < fieldInfos.Count; fieldIndex++)
		// {
		// //还原以前的表达式表示方法，用数据集名称 + 字段名
		// resultFields.Add(datasetName + fieldInfos[fieldIndex].Name);
		// }
		// }
		// catch
		// {
		//
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return resultFields;
		// }
		//
		// /// <summary>
		// /// 获得一个带指定连接信息的矢量数据集的字段名集合，字段名不包含表名
		// /// </summary>
		// /// <param name="dataset"></param>
		// /// <param name="joinItems"></param>
		// /// <returns></returns>
		// public static List<String>
		// getJoinFieldNamesWithoutDatasetName(DatasetVector dataset, JoinItems
		// joinItems)
		// {
		// List<String> resultFields = new List<String>();
		// try
		// {
		// List<DatasetVector> datasetList = getJoinDatasets(dataset,
		// joinItems);
		// //这里使用for循环，为了保证结果的顺序与获取字段集合的方法一致
		// for (int i = 0; i < datasetList.Count; i++)
		// {
		// try
		// {
		// FieldInfos fieldInfos = datasetList.get(i).FieldInfos;
		// for (int fieldIndex = 0; fieldIndex < fieldInfos.Count; fieldIndex++)
		// {
		// resultFields.Add(fieldInfos[fieldIndex].Name);
		// }
		// }
		// catch
		// {
		//
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return resultFields;
		// }
		//
		// public static Dictionary<String, String>
		// getJoinFieldNamesDic(DatasetVector dataset, JoinItems joinItems)
		// {
		// Dictionary<String, String> resultFields = new Dictionary<String,
		// String>();
		// try
		// {
		// List<DatasetVector> datasetList = getJoinDatasets(dataset,
		// joinItems);
		// //这里使用for循环，为了保证结果的顺序与获取字段集合的方法一致
		// for (int i = 0; i < datasetList.Count; i++)
		// {
		// try
		// {
		// FieldInfos fieldInfos = datasetList.get(i).FieldInfos;
		// String datasetName =
		// String.IsNullOrEmpty(datasetList.get(i).TableName) ? "" :
		// datasetList.get(i).Name + ".";
		// for (int fieldIndex = 0; fieldIndex < fieldInfos.Count; fieldIndex++)
		// {
		// //还原以前的表达式表示方法，用数据集名称 + 字段名
		// resultFields.Add(fieldInfos[fieldIndex].Name, datasetName +
		// fieldInfos[fieldIndex].Name);
		// }
		// }
		// catch
		// {
		//
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return resultFields;
		// }
		//
		// /// <summary>
		// /// 获取带指定连接信息的矢量数据集的字段集合
		// /// </summary>
		// /// <param name="dataset"></param>
		// /// <param name="joinItems"></param>
		// /// <returns></returns>
		// public static List<FieldInfo> getJoinFieldInfos(DatasetVector
		// dataset, JoinItems joinItems)
		// {
		// List<FieldInfo> resultFieldInfos = new List<FieldInfo>();
		// try
		// {
		// List<DatasetVector> datasetList = getJoinDatasets(dataset,
		// joinItems);
		// //这里使用for循环，为了保证结果的顺序与获取字段名集合的方法一致
		// for (int i = 0; i < datasetList.Count; i++)
		// {
		// try
		// {
		// FieldInfos dataFieldInfos = datasetList.get(i).FieldInfos;
		// for (int fieldIndex = 0; dataFieldInfos != null && fieldIndex <
		// dataFieldInfos.Count; fieldIndex++)
		// {
		// resultFieldInfos.Add(dataFieldInfos[fieldIndex]);
		// }
		// }
		// catch
		// {
		//
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return resultFieldInfos;
		// }

	}

	public static class RibbonWrap {
		private RibbonWrap() {
			// 默认实现
		}

		// public static int ConvertStringToInt(String lab)
		// {
		// int index = int.MinValue;
		// try
		// {
		// if (lab != null && !lab.Equals(_XMLTag.g_Empty))
		// {
		// index = Convert.Toint(lab);
		// }
		// else
		// {
		// index = 0;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// if (index == int.MinValue)
		// {
		// index = int.MaxValue;
		// }
		// return index;
		// }
		//
		// public static Bitmap getBitmap(String path)
		// {
		// Bitmap tempMap = null;
		// try
		// {
		// if (path != null && !path.Equals(_XMLTag.g_Empty))
		// {
		// String fullPath = CommonToolkit.PathWrap.getFullPathName(path);
		// if (System.IO.File.Exists(fullPath))
		// {
		// tempMap = new Bitmap(fullPath);
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return tempMap;
		// }

	}

	public static class ScaleWrap {
		private ScaleWrap() {
			// 默认实现
		}

		// /// <summary>
		// /// 根据传入的字符串转换为正确的比例尺Double值
		// /// 例如：5000->0.0002；0.0005->0.0005；非数字->-1；-1000->-1
		// /// 1:5000->0.0002
		// /// </summary>
		// /// <param name="scaleString"></param>
		// /// <returns>-1表示转换失败</returns>
		// public static Double ConvertStringToScaleValue(String scaleString)
		// {
		// Double scaleValue = 0.0;
		// try
		// {
		// if (!Double.TryParse(scaleString, out scaleValue))
		// {
		// if (String.Compare(scaleString.Substring(0, 2), "1:",
		// StringComparison.CurrentCultureIgnoreCase) == 0)
		// {
		// scaleString = scaleString.Substring(2);
		// try
		// {
		// Double scaleTemp = Double.Parse(scaleString);
		// if (scaleTemp < 0)
		// {
		// scaleValue = -1;
		// }
		// else
		// {
		// scaleValue = 1 / scaleTemp;
		// }
		// }
		// catch
		// {
		// scaleValue = -1;
		// }
		// }
		// else
		// {
		// scaleValue = -1;
		// }
		// }
		// else
		// {
		// if (scaleValue < 0)
		// {
		// scaleValue = -1;
		// }
		// else if (scaleValue > 1)
		// {
		// scaleValue = 1 / scaleValue;
		// }
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// scaleValue = -1;
		// }
		//
		// return scaleValue;
		// }
		//
		// /// <summary>
		// /// 根据传入的数值转换为正确的比例尺数值
		// /// 规则：大于1，返回倒数值；小于0，返回-1；其他，保持原样。
		// /// </summary>
		// /// <param name="scale"></param>
		// /// <returns></returns>
		// public static Double ConvertStringToScaleValue(Double scale)
		// {
		// Double scaleValue = 0.0;
		// try
		// {
		// if (scaleValue < 0)
		// {
		// scaleValue = -1;
		// }
		// else if (scaleValue > 1)
		// {
		// scaleValue = 1 / scaleValue;
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// scaleValue = -1;
		// }
		//
		// return scaleValue;
		// }
		//
		// /// <summary>
		// /// 根据一定规则将传入的字符串转换为比例尺表示形式
		// ///
		// 规则：1:1000->1:1000；5000->1:0.0002；0.5->1:2；-1->String.Empty；asd->String.Empty
		// /// </summary>
		// /// <param name="scaleString"></param>
		// /// <returns></returns>
		// public static String ConvertStringToScale(String scaleString)
		// {
		// String scale = String.Empty;
		// try
		// {
		// if (!String.IsNullOrEmpty(scaleString))
		// {
		// Double scaleValue = 0;
		// if (!Double.TryParse(scaleString, out scaleValue))
		// {
		// if (String.Compare(scaleString.Substring(0, 2), "1:",
		// StringComparison.CurrentCultureIgnoreCase) == 0)
		// {
		// String scaleStringTemp = scaleString.Substring(3);
		// try
		// {
		// Double scaleTemp = Double.Parse(scaleStringTemp);
		// if (scaleTemp < 0)
		// {
		// scale = String.Empty;
		// }
		// else
		// {
		// scale = scaleString;
		// }
		// }
		// catch
		// {
		// scale = String.Empty;
		// }
		// }
		// else
		// {
		// scale = String.Empty;
		// }
		// }
		// else
		// {
		// if (scaleValue < 0)
		// {
		// scale = String.Empty;
		// }
		// else if (scaleValue < Math.Pow(Math.E, -10))
		// {
		// scale = "0";
		// }
		// else if (scaleValue > 1)
		// {
		// scale = String.format("1:{0}", scaleString);
		// }
		// else
		// {
		// scale = String.format("1:{0}", (1 / scaleValue).ToString());
		// }
		// }
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// scale = String.Empty;
		// }
		//
		// return scale;
		// }
		//
		// public static String ConvertStringToScale(Double scaleValue)
		// {
		// String scale = String.Empty;
		// try
		// {
		// if (scaleValue < 0)
		// {
		// scale = String.Empty;
		// }
		// else if (scaleValue < Math.Pow(10, -10))
		// {
		// scale = "0";
		// }
		// else if (scaleValue > 1)
		// {
		// scale = String.format("1:{0}", scaleValue.ToString());
		// }
		// else
		// {
		// scale = String.format("1:{0}", (1 / scaleValue).ToString());
		// }
		// }catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		//
		// return scale;
		// }

	}

	public static class SpatialIndexTypeWrap {
		private SpatialIndexTypeWrap() {
			// 默认实现
		}

		// /// <summary>
		// /// 根据指定的扩展名判断文件类型是否被桌面支持
		// /// </summary>
		// public static boolean IsSuportedFileExt(String fileExt)
		// {
		// boolean result = false;
		// if (fileExt.Equals(".sxw") ||
		// fileExt.Equals(".smw") ||
		// fileExt.Equals(".smwu") ||
		// fileExt.Equals(".sxwu"))
		// {
		// result = true;
		// }
		// else
		// {
		// result = DatasourceWrap.IsSuportedFileEngineExt(fileExt);
		// }
		// return result;
		// }
		//
		// /// <summary>
		// /// 根据指定的栅格文件扩展名获取对应的栅格扩展名描述
		// /// </summary>
		// public static String getImagePluginsName(String fileExt)
		// {
		// String strname = "Image Plugin Data Engine";
		// try
		// {
		// switch (fileExt)
		// {
		// case "*.sit":
		// {
		// strname = "Image Tower";
		// }
		// break;
		// case "*.bmp":
		// {
		// strname = "BMP Image";
		// }
		// break;
		// case "*.jpg":
		// {
		// strname = "JPG Image";
		// }
		// break;
		// case "*.gif":
		// {
		// strname = "GIF Image";
		// }
		// break;
		// case "*.png":
		// {
		// strname = "PNG Image";
		// }
		// break;
		// case "*.tif":
		// {
		// strname = "TIF Image";
		// }
		// break;
		// case "*.img":
		// {
		// strname = "IMG Image";
		// }
		// break;
		// case "*.sci":
		// {
		// strname = "Cache Image";
		// }
		// break;
		// }
		// }
		// catch
		// {
		//
		// }
		// return strname;
		// }
		//
		// /// <summary>
		// /// 根据指定的几何对象类型获取对应的模板风格资源字符串表示
		// /// </summary>
		// public static String getStyleTemplateString(GeometryType
		// geometryType)
		// {
		// String strTitle = String.Empty;
		// try
		// {
		// switch (geometryType)
		// {
		// case GeometryType.GeoLine:
		// {
		// strTitle = CoreResources.String_LineStyleTemplateTitle;
		// }
		// break;
		// case GeometryType.GeoPoint:
		// {
		// strTitle = CoreResources.String_PointStyleTemplateTitle;
		// }
		// break;
		// case GeometryType.GeoRegion:
		// {
		// strTitle = CoreResources.String_RegionStyleTemplateTitle;
		// }
		// break;
		// case GeometryType.GeoText:
		// {
		// strTitle = CoreResources.String_TextStyleTemplateTitle;
		// }
		// break;
		//
		// default:
		// {
		// }
		// break;
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return strTitle;
		// }
	}

	public static class SymbolLibraryWrap {

		private SymbolLibraryWrap() {
			// 默认实现
		}
		//
		// private static String chartMarkerFile =
		// "./Resource/Chart/Presentation/S52/SymbolLib/Marker.sym";
		// private static String chartLineFile =
		// "./Resource/Chart/Presentation/S52/SymbolLib/Line.lsl";
		// private static String chartFillFile =
		// "./Resource/Chart/Presentation/S52/SymbolLib/Fill.bru";

		// public static String getLibPath(SymbolLibrary symbolLibray)
		// {
		// return symbolLibray.getLibPath();
		// }
		//
		// /// <summary>
		// /// 加载海图符号库
		// /// </summary>
		// public static void LoadChartSymbolLib()
		// {
		// try
		// {
		// String fileName =
		// CommonToolkit.PathWrap.getFullPathName(chartMarkerFile);
		// if (File.Exists(fileName))
		// {
		// ImportSymbolLibrary(Application.getActiveApplication().getWorkspace().Resources.MarkerLibrary,
		// fileName);
		// }
		//
		// fileName = CommonToolkit.PathWrap.getFullPathName(chartLineFile);
		// if (File.Exists(fileName))
		// {
		// ImportSymbolLibrary(Application.getActiveApplication().getWorkspace().Resources.LineLibrary,
		// fileName);
		// }
		//
		// fileName = CommonToolkit.PathWrap.getFullPathName(chartFillFile);
		// if (File.Exists(fileName))
		// {
		// ImportSymbolLibrary(Application.getActiveApplication().getWorkspace().Resources.FillLibrary,
		// fileName);
		// }
		//
		// //Application.getActiveApplication().Output.Output(CoreResources.String_LoadChartResourcesSuccess);
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		//
		// /// <summary>
		// /// 导入指定文件路径的符号库
		// /// </summary>
		// private static void ImportSymbolLibrary(SymbolLibrary currentLib,
		// String fileName)
		// {
		// try
		// {
		// SymbolGroup currentGroup = currentLib.RootGroup;
		// Resources resouces = new Resources();
		// SymbolLibrary lib = null;
		// if (currentLib is SymbolMarkerLibrary)
		// {
		// lib = resouces.MarkerLibrary;
		// }
		// else if (currentLib is SymbolLineLibrary)
		// {
		// lib = resouces.LineLibrary;
		// }
		// else if (currentLib is SymbolFillLibrary)
		// {
		// lib = resouces.FillLibrary;
		// }
		//
		// if (lib != null &&
		// lib.FromFile(fileName))
		// {
		// if (currentLib is SymbolLineLibrary)
		// {
		// //CheckLineLibUsedInlineMarkers(lib.RootGroup, new Dictionary<int,
		// int>(), resouces.LineLibrary.InlineMarkerLib, (lib as
		// SymbolLineLibrary).InlineMarkerLib);
		// }
		// else if (currentLib is SymbolFillLibrary)
		// {
		// //CheckFillLibUsedInlineMarkers(lib.RootGroup, new Dictionary<int,
		// int>(), resouces.FillLibrary.InlineMarkerLib, (lib as
		// SymbolFillLibrary).InlineMarkerLib);
		// }
		//
		// if (lib.RootGroup.ChildGroups.Count > 0)
		// {
		// AppendSymbolGroup(currentGroup, lib.RootGroup.ChildGroups[0]);
		// }
		// else
		// {
		// AppendSymbolGroup(currentGroup, lib.RootGroup);
		// }
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		//
		// /// <summary>
		// /// 附加指定的符号库分组到某符号库父目录下。
		// /// </summary>
		// private static SymbolGroup AppendSymbolGroup(SymbolGroup
		// symbolGroupParent, SymbolGroup symbolGroupSrc)
		// {
		// SymbolGroup symbolGroup = null;
		// try
		// {
		// String groupName = symbolGroupSrc.Name;
		// if (groupName.Length == 0)
		// {
		// //groupName = CoreResources.String_ChildGroup;
		// }
		//
		// if (symbolGroupParent.ChildGroups.Contains(groupName))
		// {
		// symbolGroup = symbolGroupParent.ChildGroups[groupName];
		// }
		// else
		// {
		// symbolGroup = symbolGroupParent.ChildGroups.Create(groupName);
		// }
		//
		// for (int index = 0; index < symbolGroupSrc.Count; index++)
		// {
		// int idSrc = symbolGroupSrc[index].ID;
		// if (symbolGroup.Library.Contains(idSrc))
		// {
		// symbolGroup.Library.Remove(idSrc);
		// }
		// symbolGroup.Library.Add(symbolGroupSrc[index], symbolGroup);
		// }
		//
		// for (int index = 0; index < symbolGroupSrc.ChildGroups.Count;
		// index++)
		// {
		// AppendSymbolGroup(symbolGroup, symbolGroupSrc.ChildGroups[index]);
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return symbolGroup;
		// }
		//
		// /// <summary>
		// /// 获取某符号库父目录下的可用符号库名
		// /// </summary>
		// private static void getAvailableGroupName(SymbolGroup
		// symbolGroupParent, ref String strName)
		// {
		// try
		// {
		// SymbolGroups symbolGroups = symbolGroupParent.ChildGroups;
		// String availableName = strName;
		// String name = strName;
		// int nCount = 0;
		// int nIndex = 0;
		//
		// while (symbolGroups != null && symbolGroups.Contains(strName))
		// {
		// int i = strName.LastIndexOf("_");
		//
		// if (i > -1)
		// {
		// if (i == strName.Length - 1)
		// {
		// availableName = strName + "1";
		// }
		// else
		// {
		// String endStr = strName.Substring(i + 1);
		// String beginStr = strName.Substring(0, i + 1);
		// int nValue = 0;
		// if (int.TryParse(endStr, out nValue))
		// {
		// nValue++;
		// endStr = nValue.ToString();
		// }
		// else
		// {
		// endStr = endStr + "_1";
		// }
		//
		// availableName = beginStr + endStr;
		// }
		// }
		// else
		// {
		// availableName = strName + "_1";
		// }
		//
		// nCount++;
		//
		// // 用于处理名称超长的情况
		// if (nCount > 100)
		// {
		// nIndex++; // 记录进入此if的次数
		//
		// if (nIndex < name.Length - 1)
		// {
		// availableName = name.Substring(0, name.Length - 1 - nIndex);
		// nCount = 0;
		// }
		// else
		// {
		// //availableName = CoreResources.String_ChildGroup;
		// }
		// }
		//
		// strName = availableName;
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		//
		// /// <summary>
		// /// 判断一个符号对象是否栅格符号。
		// /// </summary>
		// public static boolean IsRasterMarker(Symbol symbol)
		// {
		// boolean isRasterMarker = false;
		// SymbolMarker marker = symbol as SymbolMarker;
		// if ((symbol.getType() != SymbolType.Marker3D) //三维符号实现和栅格符号相同，所以排除
		// && marker.Count == 1 // 栅格符号只有一个笔划为Bitmap或者Icon类型
		// && (marker[0].getType() == StrokeType.Bitmap || marker[0].getType()
		// ==
		// StrokeType.Icon))
		// {
		// isRasterMarker = true;
		// }
		// return isRasterMarker;
		// }
	}

	public static class ThemeStyleWrap {
		private ThemeStyleWrap() {
			// 默认实现
		}
		// public static event ThemeStyleChangedEventHandler
		// ThemeStyleChangedEvent;
		// #region Variable
		// private static ThemeStyle currentThemeStyle;
		// #endregion
		// /// <summary>
		// /// 获取或设置桌面当前的主题风格
		// /// </summary>
		// public static ThemeStyle CurrentThemeStyle
		// {
		// get
		// {
		// return currentThemeStyle;
		// }
		// set
		// {
		// currentThemeStyle = value;
		// if (ThemeStyleChangedEvent != null)
		// {
		// ThemeStyleChangedEvent(null, new
		// ThemeStyleChangedEventArgs(currentThemeStyle, value));
		// }
		// }
		// }
		// #region Property
		//
		// #endregion
		//
		// #region Construct
		//
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 获取桌面当前主题风格的活动窗体字体
		// /// </summary>
		// public static Font getRibbonFont()
		// {
		// Font result = SystemFonts.DefaultFont;
		//
		// try
		// {
		// if (CurrentThemeStyle != null)
		// {
		// if (CurrentThemeStyle.PanelStyle.InnerAreaStyleType !=
		// InnerAreaStyleType.UseInnerStyle)
		// {
		// result = SystemFonts.DefaultFont;
		// }
		// else
		// {
		// result = CurrentThemeStyle.PanelStyle.InnerAreaStyle.Font;
		// }
		// //result = CurrentThemeStyle.RibbonStyle.Font;
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return result;
		// }
		//
		// /// <summary>
		// /// 将Desktop的UIInnerThemeStyle转为Janus的UIformatStyle
		// /// </summary>
		// public static void
		// SetUIPanelInnerStyle(Janus.Windows.UI.UIformatStyle style,
		// UIInnerThemeStyle theme)
		// {
		// try
		// {
		// if (null == style || null == theme)
		// {
		// return;
		// }
		// style.Alpha = CommonToolkit.ColorWrap.getAlphaFromOpaqueRate(0);
		// style.BackColor = theme.BackColor;
		// style.BackColorAlphaMode = Janus.Windows.UI.AlphaMode.UseAlpha;
		// style.BackColorGradient = theme.BackColor;
		// style.BackgroundGradientMode =
		// (Janus.Windows.UI.BackgroundGradientMode)theme.BackgroundGradientMode;
		// style.BackgroundImage = theme.BackgroundImage;
		// style.BackgroundImageDrawMode =
		// (Janus.Windows.UI.BackgroundImageDrawMode)theme.BackgroundImageDrawMode;
		// style.Blend = theme.Blend;
		// style.BlendGradient = theme.BlendGradient;
		// style.Font = theme.Font;
		// style.ForeColor = theme.ForeColor;
		// style.ForeColorAlphaMode = Janus.Windows.UI.AlphaMode.UseAlpha;
		// }
		// catch
		// {
		//
		// }
		// }
		//
		// /// <summary>
		// /// 将Desktop的UIWindowTabThemeStyle转为Janus的UIformatStyle
		// /// </summary>
		// public static void SetUIWindowTabStyle(Janus.Windows.UI.UIformatStyle
		// style, UITabThemeStyle source)
		// {
		// if (null == source || null == style)
		// {
		// return;
		// }
		// style.BackColor = source.BackColor;
		// style.BackColorGradient = source.BackColorGradient;
		// style.Blend = source.Blend;
		// style.BlendGradient = source.BlendGradient;
		// style.Font = source.Font;
		// style.ForeColor = source.ForeColor;
		// }
	}

	public static class ThreadWrap {
		private ThreadWrap() {
			// 默认实现
		}

		// public static Thread CreateThread(ThreadStart threadStart)
		// {
		// try
		// {
		// Thread thread = new Thread(threadStart);
		// try
		// {
		// //以下2个值必须保持一致，不然会出现错误，比如 土耳其+en 的组合，原因不在此详述
		// //设置当前线程的语言文化环境，用以正确处理字符串、数值转换，字符串比较等
		// thread.CurrentCulture =
		// CultureInfo.getCultureInfo(_XMLTag.g_strCurrentCulture);
		// //设置应用程序显示区域语言，用以加载对应的资源文件
		// thread.CurrentUICulture =
		// CultureInfo.getCultureInfo(_XMLTag.g_strCurrentUICulture);
		// }
		// catch
		// {
		// thread.CurrentCulture = CultureInfo.getCultureInfo("zh-CN");
		// thread.CurrentUICulture = CultureInfo.getCultureInfo("zh-CN");
		// }
		//
		// return thread;
		// }
		// catch (Exception ex)
		// {
		// return null;
		// throw ex;
		// }
		// }
		//
		// public static Thread CreateThread(ParameterizedThreadStart
		// threadStart)
		// {
		// try
		// {
		// Thread thread = new Thread(threadStart);
		// try
		// {
		// //以下2个值必须保持一致，不然会出现错误，比如 土耳其+en 的组合，原因不在此详述
		// //设置当前线程的语言文化环境，用以正确处理字符串、数值转换，字符串比较等
		// thread.CurrentCulture =
		// CultureInfo.getCultureInfo(_XMLTag.g_strCurrentCulture);
		// //设置应用程序显示区域语言，用以加载对应的资源文件
		// thread.CurrentUICulture =
		// CultureInfo.getCultureInfo(_XMLTag.g_strCurrentUICulture);
		// }
		// catch
		// {
		// thread.CurrentCulture = CultureInfo.getCultureInfo("zh-CN");
		// thread.CurrentUICulture = CultureInfo.getCultureInfo("zh-CN");
		// }
		//
		// return thread;
		// }
		// catch (Exception ex)
		// {
		// return null;
		// throw ex;
		// }
		// }
	}

	public static class UndefinedWrap {
		private UndefinedWrap() {
			// 默认实现
		}

		// /// <summary>
		// /// 根据指定的扩展名判断文件类型是否被桌面支持
		// /// </summary>
		// public static boolean IsSuportedFileExt(String fileExt)
		// {
		// boolean result = false;
		// if (fileExt.Equals(".sxw") ||
		// fileExt.Equals(".smw") ||
		// fileExt.Equals(".smwu") ||
		// fileExt.Equals(".sxwu"))
		// {
		// result = true;
		// }
		// else
		// {
		// result = DatasourceWrap.IsSuportedFileEngineExt(fileExt);
		// }
		// return result;
		// }
		//
		// /// <summary>
		// /// 根据指定的栅格文件扩展名获取对应的栅格扩展名描述
		// /// </summary>
		// public static String getImagePluginsName(String fileExt)
		// {
		// String strname = "Image Plugin Data Engine";
		// try
		// {
		// switch (fileExt)
		// {
		// case "*.sit":
		// {
		// strname = "Image Tower";
		// }
		// break;
		// case "*.bmp":
		// {
		// strname = "BMP Image";
		// }
		// break;
		// case "*.jpg":
		// {
		// strname = "JPG Image";
		// }
		// break;
		// case "*.gif":
		// {
		// strname = "GIF Image";
		// }
		// break;
		// case "*.png":
		// {
		// strname = "PNG Image";
		// }
		// break;
		// case "*.tif":
		// {
		// strname = "TIF Image";
		// }
		// break;
		// case "*.img":
		// {
		// strname = "IMG Image";
		// }
		// break;
		// case "*.sci":
		// {
		// strname = "Cache Image";
		// }
		// break;
		// }
		// }
		// catch
		// {
		//
		// }
		// return strname;
		// }
		//
		// /// <summary>
		// /// 根据指定的几何对象类型获取对应的模板风格资源字符串表示
		// /// </summary>
		// public static String getStyleTemplateString(GeometryType
		// geometryType)
		// {
		// String strTitle = String.Empty;
		// try
		// {
		// switch (geometryType)
		// {
		// case GeometryType.GeoLine:
		// {
		// strTitle = CoreResources.String_LineStyleTemplateTitle;
		// }
		// break;
		// case GeometryType.GeoPoint:
		// {
		// strTitle = CoreResources.String_PointStyleTemplateTitle;
		// }
		// break;
		// case GeometryType.GeoRegion:
		// {
		// strTitle = CoreResources.String_RegionStyleTemplateTitle;
		// }
		// break;
		// case GeometryType.GeoText:
		// {
		// strTitle = CoreResources.String_TextStyleTemplateTitle;
		// }
		// break;
		//
		// default:
		// {
		// }
		// break;
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return strTitle;
		// }
	}

	public static class WorkEnvironmentWrap {

		private WorkEnvironmentWrap() {
			// 默认实现
		}
		// /// <summary>
		// /// 发送工作环境
		// /// </summary>
		// public static void SendLoadWorkEnvironmentEvent(String
		// workEnvironment)
		// {
		// try
		// {
		// if (LoadWorkEnvironmentEvent != null)
		// {
		// LoadWorkEnvironmentEventArgs arg = new
		// LoadWorkEnvironmentEventArgs(workEnvironment);
		// LoadWorkEnvironmentEvent(null, arg);
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// }
		// }
		//
		// public static event LoadWorkEnvironmentEventHandler
		// LoadWorkEnvironmentEvent;
		// public delegate void LoadWorkEnvironmentEventHandler(Object sender,
		// LoadWorkEnvironmentEventArgs e);
		// public class LoadWorkEnvironmentEventArgs : EventArgs
		// {
		// public LoadWorkEnvironmentEventArgs(String workEnvironment)
		// {
		// workEnvironment = workEnvironment;
		// }
		//
		// private String workEnvironment;
		// public String workEnvironment
		// {
		// get
		// {
		// return workEnvironment;
		// }
		// }
		// }
	}

	public static class WorkspaceTypeWrap {
		private WorkspaceTypeWrap() {
			// 默认实现
		}

		// #region Variable
		//
		// #endregion
		//
		// #region Property
		//
		// #endregion
		//
		// #region Construct
		//
		// #endregion
		//
		// #region Function_Public
		// /// <summary>
		// /// 根据工作空间类型获取对应的资源字符串
		// /// </summary>
		// public static String getWorkspaceTypeName(WorkspaceType
		// workspaceType)
		// {
		// String workspaceTypeInfo = String.Empty;
		// try
		// {
		// if (workspaceType == WorkspaceType.Default)
		// {
		// workspaceTypeInfo = CoreResources.String_WorkspaceType_Default;
		// }
		// else if (workspaceType == WorkspaceType.Oracle)
		// {
		// workspaceTypeInfo = CoreResources.String_WorkspaceType_Oracle;
		// }
		// else if (workspaceType == WorkspaceType.SQL)
		// {
		// workspaceTypeInfo = CoreResources.String_WorkspaceType_SQL;
		// }
		// else if (workspaceType == WorkspaceType.SMW)
		// {
		// workspaceTypeInfo = CoreResources.String_WorkspaceType_SMW;
		// }
		// else if (workspaceType == WorkspaceType.SXW)
		// {
		// workspaceTypeInfo = CoreResources.String_WorkspaceType_SXW;
		// }
		// else if (workspaceType == WorkspaceType.SMWU)
		// {
		// workspaceTypeInfo = CoreResources.String_WorkspaceType_SMWU;
		// }
		// else if (workspaceType == WorkspaceType.SXWU)
		// {
		// workspaceTypeInfo = CoreResources.String_WorkspaceType_SXWU;
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// }
		// return workspaceTypeInfo;
		// }
		//
		// /// <summary>
		// /// 根据文件的扩展名获取对应的工作空间类型
		// /// </summary>
		// public static WorkspaceType getWorkspaceTypeByFileExt(String fileExt)
		// {
		// WorkspaceType workspaceType = WorkspaceType.Default;
		// fileExt = fileExt.toLowerCase();
		//
		// if (fileExt.Equals(".sxw"))
		// {
		// workspaceType = WorkspaceType.SXW;
		// }
		// else if (fileExt.Equals(".smw"))
		// {
		// workspaceType = WorkspaceType.SMW;
		// }
		// else if (fileExt.Equals(".smwu"))
		// {
		// workspaceType = WorkspaceType.SMWU;
		// }
		// else if (fileExt.Equals(".sxwu"))
		// {
		// workspaceType = WorkspaceType.SXWU;
		// }
		// else
		// {
		// workspaceType = WorkspaceType.Default;
		// }
		//
		// return workspaceType;
		// }
		// #endregion
		//
		// #region Function_Event
		//
		// #endregion
		//
		// #region Function_Private
		//
		// #endregion
		//
		// #region Event
		//
		// #endregion
		//
		// #region InterfaceMembers
		//
		// #endregion
		//
		// #region NestedTypes
		//
		// #endregion
	}

	/**
	 * 数据集图标路径对应工具类
	 *
	 * @author xie
	 */
	public static class DatasetImageWrap {
		private static HashMap<DatasetType, String> datasetImage = null;

		private DatasetImageWrap() {
			// 不提供构造函数
		}

		private static void initialize() {
			if (null != datasetImage) {
				return;
			}
			datasetImage = new HashMap<DatasetType, String>();
			String fileParentPath = "/com/supermap/desktop/controlsresources/WorkspaceManager/Dataset/";
			datasetImage.put(DatasetType.CAD, fileParentPath + "Image_DatasetCAD_Normal.png");
			datasetImage.put(DatasetType.GRID, fileParentPath + "Image_DatasetGrid_Normal.png");
			datasetImage.put(DatasetType.GRIDCOLLECTION, fileParentPath + "Image_DatasetGridCollection_Normal.png");
			datasetImage.put(DatasetType.IMAGE, fileParentPath + "Image_DatasetImage_Normal.png");
			datasetImage.put(DatasetType.IMAGECOLLECTION, fileParentPath + "Image_DatasetImageCollection_Normal.png");
			datasetImage.put(DatasetType.LINE, fileParentPath + "Image_DatasetLine_Normal.png");
			datasetImage.put(DatasetType.LINE3D, fileParentPath + "Image_DatasetLine3D_Normal.png");
			datasetImage.put(DatasetType.LINEM, fileParentPath + "Image_DatasetLineM_Normal.png");
			datasetImage.put(DatasetType.LINKTABLE, fileParentPath + "Image_DatasetLinkTable_Normal.png");
			datasetImage.put(DatasetType.MODEL, fileParentPath + "Image_DatasetModel_Normal.png");
			datasetImage.put(DatasetType.NETWORK, fileParentPath + "Image_DatasetNetwork_Normal.png");
			datasetImage.put(DatasetType.NETWORK3D, fileParentPath + "Image_DatasetNetwork3D_Normal.png");
			datasetImage.put(DatasetType.PARAMETRICLINE, fileParentPath + "Image_DatasetParametricLine_Normal.png");
			datasetImage.put(DatasetType.PARAMETRICREGION, fileParentPath + "Image_DatasetParametricRegion_Normal.png");
			datasetImage.put(DatasetType.POINT, fileParentPath + "Image_DatasetPoint_Normal.png");
			datasetImage.put(DatasetType.POINT3D, fileParentPath + "Image_DatasetPoint3D_Normal.png");
			datasetImage.put(DatasetType.REGION, fileParentPath + "Image_DatasetRegion_Normal.png");
			datasetImage.put(DatasetType.REGION3D, fileParentPath + "Image_DatasetRegion3D_Normal.png");
			datasetImage.put(DatasetType.TABULAR, fileParentPath + "Image_DatasetTabular_Normal.png");
			datasetImage.put(DatasetType.TEXT, fileParentPath + "Image_DatasetText_Normal.png");
			datasetImage.put(DatasetType.TOPOLOGY, fileParentPath + "Image_DatasetTopology_Normal.png");
			datasetImage.put(DatasetType.WCS, fileParentPath + "Image_DatasetWCS_Normal.png");
			datasetImage.put(DatasetType.WMS, fileParentPath + "Image_DatasetWMS_Normal.png");
			datasetImage.put(DatasetType.TEXTURE, fileParentPath + "Image_DataseSimpleDataset_Normal.png");
			// datasetImage.put(DatasetType.LINKTABLE)
			String filePathTopo = fileParentPath + "TopologyDatasetRelations/Image_TopologyDatasetRelations_Normal.png";
			datasetImage.put(DatasetType.TOPOLOGY, filePathTopo);
		}

		/**
		 * 根据数据集类型返回对应的图标所在的绝对路径
		 *
		 * @param type
		 * @return
		 */
		public static String getImageIconPath(DatasetType type) {
			String resultIcon = "";
			try {
				initialize();
				if (null == type) {
					return "";
				} else {
					Iterator<?> iterator = datasetImage.entrySet().iterator();
					while (iterator.hasNext()) {
						java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
						if (type == entry.getKey()) {
							resultIcon = (String) entry.getValue();
						}
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			return resultIcon;
		}

		/**
		 * 根据数据集中文名得到数据集图标所在的相对路径
		 *
		 * @param datasetName
		 * @return
		 */
		public static String getImageIconPath(String datasetTypeName) {
			DatasetType type = DatasetTypeWrap.findType(datasetTypeName);
			return getImageIconPath(type);
		}
	}

	public static class DatasourceImageWrap {
		private static HashMap<EngineType, String> datasourceImage = null;

		private DatasourceImageWrap() {
			// 不提供构造函数
		}

		private static void initialize() {
			if (null != datasourceImage) {
				return;
			}
			datasourceImage = new HashMap<EngineType, String>();
			String fileParentPath = "/com/supermap/desktop/controlsresources/WorkspaceManager/Datasource/";
			datasourceImage.put(EngineType.DB2, fileParentPath + "Image_DatasourceDB2_Normal.png");
			datasourceImage.put(EngineType.GOOGLEMAPS, fileParentPath + "Image_DatasourceGoogleMaps_Normal.png");
			datasourceImage.put(EngineType.IMAGEPLUGINS, fileParentPath + "Image_DatasourceImagePlugins_Normal.png");
			datasourceImage.put(EngineType.OGC, fileParentPath + "Image_DatasourceOGC_Normal.png");
			datasourceImage.put(EngineType.ORACLEPLUS, fileParentPath + "Image_DatasourceOraclePlus_Normal.png");
			datasourceImage.put(EngineType.ORACLESPATIAL, fileParentPath + "Image_DatasourceOracleSpatial_Normal.png");
			datasourceImage.put(EngineType.POSTGRESQL, fileParentPath + "Image_DatasourcePostgreSQL_Normal.png");
			datasourceImage.put(EngineType.SQLPLUS, fileParentPath + "Image_DatasourceSQLPlus_Normal.png");
			datasourceImage.put(EngineType.UDB, fileParentPath + "Image_DatasourceUDB_Normal.png");
			datasourceImage.put(EngineType.DM, fileParentPath + "Image_Datasource_DMPlus_Normal.png");
			datasourceImage.put(EngineType.KINGBASE, fileParentPath + "Image_Datasource_Kingbase_Normal.png");
			datasourceImage.put(EngineType.MYSQL, fileParentPath + "Image_MySQL.png");
			datasourceImage.put(null, fileParentPath + "Image_Datasources_Normal.png");
		}

		/**
		 * 根据数据源引擎类型返回对应的图标所在的相对路径
		 *
		 * @param type
		 * @return
		 */
		public static String getImageIconPath(EngineType type) {
			String resultIcon = "";
			try {
				initialize();
				Iterator<?> iterator = datasourceImage.entrySet().iterator();
				while (iterator.hasNext()) {
					java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
					if (type == entry.getKey()) {
						resultIcon = (String) entry.getValue();
					}
				}

			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			return resultIcon;
		}
	}

	public static class LayerImageWrap {
		private static HashMap<DatasetType, String> layerImage = null;

		private LayerImageWrap() {
			// 不提供构造函数
		}

		private static void initialize() {
			if (null != layerImage) {
				return;
			}
			layerImage = new HashMap<DatasetType, String>();
			String fileParentPath = "/com/supermap/desktop/coreresources/Layer/";
			layerImage.put(DatasetType.CAD, fileParentPath + "Image_Layer_CAD.png");
			layerImage.put(DatasetType.LINEM, fileParentPath + "Image_Layer_LineM.png");
			layerImage.put(DatasetType.NETWORK, fileParentPath + "Image_Layer_Net.png");
			layerImage.put(DatasetType.TEXT, fileParentPath + "Image_Layer_TextLayer.png");
		}

		/**
		 * 根据数据集类型返回对应的图标所在的相对路径
		 *
		 * @param type
		 * @return
		 */
		public static String getImageIconPath(DatasetType type) {
			String resultIcon = "";
			try {
				initialize();
				if (null == type) {
					return "";
				} else {
					Iterator<?> iterator = layerImage.entrySet().iterator();
					while (iterator.hasNext()) {
						java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
						if (type == entry.getKey()) {
							resultIcon = (String) entry.getValue();
						}
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			return resultIcon;
		}
	}
}
