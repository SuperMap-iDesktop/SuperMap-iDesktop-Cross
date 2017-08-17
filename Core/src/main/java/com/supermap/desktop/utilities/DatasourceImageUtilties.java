package com.supermap.desktop.utilities;

import com.supermap.data.EngineType;
import com.supermap.desktop.Application;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author XiaJT
 */
public class DatasourceImageUtilties {
	private static HashMap<EngineType, String> datasourceImage = null;
	private static final String fileParentPath = "/controlsresources/WorkspaceManager/Datasource/";

	private DatasourceImageUtilties() {
		// 不提供构造函数
	}

	private static void initialize() {
		if (null != datasourceImage) {
			return;
		}
		datasourceImage = new HashMap<>();

		datasourceImage.put(EngineType.DB2, fileParentPath + "Datasource_DB2.png");
		datasourceImage.put(EngineType.IMAGEPLUGINS, fileParentPath + "Image_DatasourceImagePlugins_Normal.png");
		datasourceImage.put(EngineType.OGC, fileParentPath + "Image_DatasourceOGC_Normal.png");
		datasourceImage.put(EngineType.ORACLEPLUS, fileParentPath + "Datasource_OraclePlus.png");
		datasourceImage.put(EngineType.ORACLESPATIAL, fileParentPath + "Datasource_ORS.png");
		datasourceImage.put(EngineType.POSTGRESQL, fileParentPath + "Datasource-PG.png");
		datasourceImage.put(EngineType.SQLPLUS, fileParentPath + "Datasource_Sql.png");
		datasourceImage.put(EngineType.UDB, fileParentPath + "Image_DatasourceUDB_Normal.png");
		datasourceImage.put(EngineType.DM, fileParentPath + "Image_Datasource_DMPlus_Normal.png");
		datasourceImage.put(EngineType.KINGBASE, fileParentPath + "Datasource_Kingbase.png");
		datasourceImage.put(EngineType.MYSQL, fileParentPath + "Datasource_MY.png");
		datasourceImage.put(EngineType.BAIDUMAPS, fileParentPath + "Image_Baidu.png");
		datasourceImage.put(EngineType.ISERVERREST, fileParentPath + "Datasource_is.png");
		datasourceImage.put(EngineType.SUPERMAPCLOUD, fileParentPath + "Datasource_SMC.png");
		datasourceImage.put(EngineType.GOOGLEMAPS, fileParentPath + "Datasource_Google.png");
		datasourceImage.put(EngineType.OPENSTREETMAPS, fileParentPath + "Datasource_OSM.png");
		datasourceImage.put(EngineType.MEMORY, fileParentPath + "Image_Memory.png");
		datasourceImage.put(EngineType.DATASERVER, fileParentPath + "Datasource_BigDataStore.png");
		datasourceImage.put(EngineType.MYSQLPlus, fileParentPath + "Image_MySQLPlus.png");
		datasourceImage.put(EngineType.BEYONDB, fileParentPath + "Image_BeyonDB.png");
		datasourceImage.put(EngineType.HIGHGODB, fileParentPath + "Image_HiGoDB.png");
		datasourceImage.put(EngineType.KDB, fileParentPath + "Image_kdb.png");
		datasourceImage.put(EngineType.SDE, fileParentPath + "Image_ArcSDE.png");
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

	public static String getBigImageIconPath(EngineType engineType) {
		if (engineType == EngineType.OGC) {
			return fileParentPath + "Image_OGC_24.png";
		} else if (engineType == EngineType.ISERVERREST) {
			return fileParentPath + "Image_IServerRest_24.png";
		} else if (engineType == EngineType.SUPERMAPCLOUD) {
			return fileParentPath + "Image_SuperMapCloud_24.png";
		} else if (engineType == EngineType.GOOGLEMAPS) {
			return fileParentPath + "Image_GoogleMaps_24.png";
		} else if (engineType == EngineType.BAIDUMAPS) {
			return fileParentPath + "Baidu-24.png";
		} else if (engineType == EngineType.SQLPLUS) {
			return fileParentPath + "Image_DatasourceSQLPlus_Normal_24.png";
		} else if (engineType == EngineType.ORACLEPLUS) {
			return fileParentPath + "Image_DatasourceOraclePlus_Normal_24.png";
		} else if (engineType == EngineType.ORACLESPATIAL) {
			return fileParentPath + "Image_DatasourceOracleSpatial_Normal_24.png";
		} else if (engineType == EngineType.POSTGRESQL) {
			return fileParentPath + "Image_DatasourcePostgreSQL_Normal_24.png";
		} else if (engineType == EngineType.DB2) {
			return fileParentPath + "Image_DatasourceDB2_Normal_24.png";
		} else if (engineType == EngineType.DM) {
			return fileParentPath + "MD+_24.png";
		} else if (engineType == EngineType.KINGBASE) {
			return fileParentPath + "Image_Datasource_Kingbase_Normal_24.png";
		} else if (engineType == EngineType.MYSQL) {
			return fileParentPath + "Image_MySQL_24.png";
		} else if (engineType == EngineType.OPENSTREETMAPS) {
			return fileParentPath + "Image_OpenStreetMaps_24.png";
		} else if (engineType == EngineType.DATASERVER) {
			return fileParentPath + "Image_BigDataStore_24.png";
		} else if (engineType == EngineType.MYSQLPlus) {
			return fileParentPath + "Image_MySQLPlus_24.png";
		} else if (engineType == EngineType.BEYONDB) {
			return fileParentPath + "Image_BeyonDB_24.png";
		} else if (engineType == EngineType.HIGHGODB) {
			return fileParentPath + "Image_HiGoDB_24.png";
		} else if (engineType == EngineType.KDB) {
			return fileParentPath + "Image_kdb_24.png";
		} else if (engineType == EngineType.SDE) {
			return fileParentPath + "Image_ArcSDE_24.png";
		}
		return "";
	}
}
