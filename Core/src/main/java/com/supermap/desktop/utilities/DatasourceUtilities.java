package com.supermap.desktop.utilities;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineInfo;
import com.supermap.data.EngineType;
import com.supermap.data.ErrorInfo;
import com.supermap.data.Toolkit;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 数据源工具类
 *
 * @author highsad
 */
public class DatasourceUtilities {
	// #region Variable
	// private static DataTable CurrentDataTable;

	private static ArrayList<EngineInfo> loadedFileEngines = null;

	/**
	 * 刚刚打开失败的数据源，是不是因为密码错误
	 */
	private static boolean passwordWrong = false;

	public static boolean isPasswordWrong() {
		return passwordWrong;
	}

	// private static EngineType[] fileEngineTypes;
	// private static EngineType[] databaseEngineTypes;
	// private static EngineType[] webEngineTypes;
	// #endregion
	private static EngineType[] webEngineTypes = {
			EngineType.OGC, EngineType.ISERVERREST, EngineType.SUPERMAPCLOUD, EngineType.GOOGLEMAPS, EngineType.BAIDUMAPS,
			EngineType.OPENSTREETMAPS};

	/**
	 * 是否为web型数据源
	 *
	 * @param engineType
	 * @return
	 */
	public static boolean isWebDatasource(EngineType engineType) {
		boolean result = false;
		try {
			for (int i = 0; i < webEngineTypes.length; i++) {
				if (webEngineTypes[i] == engineType) {
					result = true;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	/**
	 * 根据指定名称获取指定数据源下的数据集
	 *
	 * @return
	 */
	private DatasourceUtilities() {
		// 工具类不提供构造函数

	}

	public static Dataset getDataset(String datasetName, Datasource datasource) {
		Dataset result = null;

		try {
			if (null == datasetName || null == datasource) {
				return null;
			}

			result = datasource.getDatasets().get(datasetName);

			// 如果到这里数据集还是 null，那么就扫描网络数据集等的子数据集进行匹配
			if (result == null) {
				for (int i = 0; i < datasource.getDatasets().getCount(); i++) {
					Dataset dataset = datasource.getDatasets().get(i);

					if (dataset instanceof DatasetVector) {
						DatasetVector datasetVector = (DatasetVector) dataset;
						if (datasetVector.getChildDataset() != null && datasetName.equals(datasetVector.getChildDataset().getName())) {
							result = datasetVector.getChildDataset();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/**
	 * 关闭所有内存数据源
	 */
	public static void closeMemoryDatasource() {
		for (int i = 0; i < Application.getActiveApplication().getWorkspace().getDatasources().getCount(); ) {
			Datasource item = Application.getActiveApplication().getWorkspace().getDatasources().get(i);
			if (":memory:".equalsIgnoreCase(item.getConnectionInfo().getServer())) {
				closeDatasource(item);
				// 关闭内存数据源时不保存工作空间
				// Application.getActiveApplication().getWorkspace().Save();
			} else {
				i++;
			}
		}
	}

	/**
	 * 关闭数据源
	 *
	 * @param datasource
	 * @return
	 */
	public static boolean closeDatasource(Datasource datasource) {
		// 关闭选中的数据源下的数据集
		Datasets datasets = datasource.getDatasets();
		DatasetUtilities.closeDataset(datasets);
		// 关闭数据源
		boolean flag = Application.getActiveApplication().getWorkspace().getDatasources().close(datasource.getAlias());
		return flag;
	}

	/**
	 * 判断工作空间中是否存在内存数据源
	 *
	 * @param workspace
	 * @return
	 */
	public static boolean isContainMemoryDatasource(Workspace workspace) {
		boolean isContian = false;
		try {
			for (int i = 0; i < workspace.getDatasources().getCount(); i++) {
				Datasource datasource = workspace.getDatasources().get(i);
				if (":memory:".equalsIgnoreCase(datasource.getConnectionInfo().getServer())) {
					isContian = true;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return isContian;
	}

	/**
	 * 获取工作空间中的内存数据源
	 *
	 * @param workspace
	 * @return
	 */
	public static String[] getMemoryDatasources(Workspace workspace) {
		ArrayList<String> datasources = new ArrayList<String>();
		try {
			for (int i = 0; i < workspace.getDatasources().getCount(); i++) {
				Datasource datasource = workspace.getDatasources().get(i);
				if (":memory:".equalsIgnoreCase(datasource.getConnectionInfo().getServer())) {
					datasources.add(datasource.getAlias());
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return datasources.toArray(new String[datasources.size()]);
	}

	/**
	 * 获取当前组件支持加载打开的文件引擎信息集合
	 *
	 * @return 文件引擎信息集合
	 */
	public static EngineInfo[] getLoadedFileEngines() {
		EngineInfo[] result = null;
		try {
			if (loadedFileEngines == null) {
				loadedFileEngines = new ArrayList<EngineInfo>();
				// modify by huchenpu 2015-07-09
				// 组件有问题，下面这行代码直接抛异常
				// EngineInfo[] infos =
				// Environment.getCurrentLoadedEngineInfos();
				// int length = infos.length;
				// for (EngineInfo engInfo :
				// Environment.getCurrentLoadedEngineInfos()) {
				// if (engInfo.getEngineFamily() == EngineFamilyType.FILE) {
				// loadedFileEngines.add(engInfo);
				// }
				// }

				result = loadedFileEngines.toArray(new EngineInfo[loadedFileEngines.size()]);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	public static String getAvailableDatasourceAlias(String alias, int index) {
		int indexTemp = index;
		String availableName = alias;
		try {
			Datasource datasource = null;
			if (indexTemp == 0) {
				datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(availableName);
			} else {
				availableName += "_" + String.valueOf(indexTemp);
				datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(availableName);
			}

			if (datasource != null) {
				indexTemp++;

				availableName = getAvailableDatasourceAlias(alias, indexTemp);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return availableName;
	}

	/**
	 * 打开指定路径与密码的文件型数据源
	 *
	 * @param fileName
	 * @param passWord
	 * @param isReadOnly
	 * @return
	 */
	public static Datasource openFileDatasource(String fileName, String passWord, boolean isReadOnly) {
		Datasource datasource = null;
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		try {
			((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.WAIT_CURSOR);
			DatasourceConnectionInfo info = new DatasourceConnectionInfo();

			// 看看该文件是否已经打开了
			boolean alreadyOpen = false;
			String server = "";
			String fileNameTemp = fileName.toLowerCase();
			fileNameTemp = fileNameTemp.replace("\\", "/");
			for (int index = 0; index < workspace.getDatasources().getCount(); index++) {
				server = workspace.getDatasources().get(index).getConnectionInfo().getServer().toLowerCase();
				server = server.replace("\\", "/");
				if (server.indexOf(".") > -1
						&& (server.equals(fileNameTemp) || (server.substring(0, server.lastIndexOf(".")) + ".udd").equals(fileNameTemp) || (server.substring(0,
						server.lastIndexOf(".")) + ".udb").equals(fileNameTemp))) {
					datasource = workspace.getDatasources().get(index);
					alreadyOpen = true;
					break;
				}
			}

			if (!alreadyOpen) {
				File file = new File(fileName);
				String alias = file.getName();
				String fileExt = "udb";
				int dot = alias.lastIndexOf('.');
				if ((dot > -1) && (dot < (alias.length()))) {
					fileExt = alias.substring(dot, alias.length());
					alias = alias.substring(0, dot);
				}

				// 找一个合适的别名
				alias = getAvailableDatasourceAlias(alias, 0);

				info.setAlias(alias);
				info.setServer(fileName);
				info.setPassword(passWord);
				info.setEngineType(CommonToolkit.EngineTypeWrap.getEngineType(fileExt));
				info.setReadOnly(!file.canWrite() || isReadOnly || info.getEngineType() == EngineType.VECTORFILE);

				try {
					datasource = workspace.getDatasources().open(info);
					if (datasource != null) {
						// 数据源打开成功后将其添加到最近文件列表中 [12/12/2011 zhoujt]
						RecentFileUtilties.addDatasourceToRecentFile(datasource);
					}
				} catch (Exception ex) {
					if ("supermap_license_error_wronglicensedata".equalsIgnoreCase(ex.getMessage())) {
						Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_Wronglicensedata"));
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.DEFAULT_CURSOR);
		}

		return datasource;
	}

	public static Datasource openFileDatasource(String fileName, String passWord, boolean isReadOnly, boolean isFirst) {
		DatasourceUtilities.passwordWrong = false;
		Datasource datasource = null;
		try {
			((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.WAIT_CURSOR);
			Toolkit.clearErrors();
			datasource = openFileDatasource(fileName, passWord, isReadOnly);
			if (datasource == null) {
				ErrorInfo[] errorInfos = Toolkit.getLastErrors(1);

				// 首先判断一下是不是已知的错误，目前已知的就是密码错误
				for (int i = 0; i < errorInfos.length; i++) {
					if (errorInfos[i].getMarker().equals(CoreProperties.getString("String_UGS_PASSWORD"))
							|| errorInfos[i].getMarker().equals(CoreProperties.getString("String_UGS_PASSWORDError"))) {
						DatasourceUtilities.passwordWrong = true;
						break;
					}
				}

				// 不是第一次打开，输出错误信息
				if (!isFirst) {
					if (DatasourceUtilities.passwordWrong) {
						Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_CurrentPasswordWrong"));
					} else {
						for (int i = 0; i < errorInfos.length; i++) {
							Application.getActiveApplication().getOutput().output(errorInfos[i].getMessage());
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.DEFAULT_CURSOR);
		}

		return datasource;
	}

	private static EngineType[] WEB_TYPE = new EngineType[]{EngineType.OGC, EngineType.ISERVERREST, EngineType.SUPERMAPCLOUD, EngineType.GOOGLEMAPS,
			EngineType.BAIDUMAPS, EngineType.OPENSTREETMAPS};

	/**
	 * 根据引擎类型判断是否为web类型的数据源
	 *
	 * @param engineType
	 * @return
	 */
	public static boolean isWebType(EngineType engineType) {
		boolean result = false;

		for (EngineType type : WEB_TYPE) {
			if (engineType == type) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 根据数据源判断是否为web类型的数据源
	 *
	 * @param datasource
	 * @return
	 */
	public static boolean isWebType(Datasource datasource) {
		return isWebType(datasource.getEngineType());
	}


	public static boolean isFileType(EngineType engineType) {
		return engineType == EngineType.IMAGEPLUGINS || engineType == EngineType.UDB || engineType == EngineType.VECTORFILE;
	}

	/*
	* 复制指定的 DatasourceConnectionInfo
	* */
	public static DatasourceConnectionInfo cloneInfo(DatasourceConnectionInfo srcInfo) {
		DatasourceConnectionInfo info = new DatasourceConnectionInfo();
		info.setAlias(srcInfo.getAlias());
		info.setAutoConnect(srcInfo.isAutoConnect());
		info.setServer(srcInfo.getServer());
		info.setDatabase(srcInfo.getDatabase());
		info.setDriver(srcInfo.getDriver());
		info.setEngineType(srcInfo.getEngineType());
		info.setOpenLinkTable(srcInfo.isOpenLinkTable());
		info.setReadOnly(srcInfo.isReadOnly());
		info.setUser(srcInfo.getUser());
		info.setPassword(srcInfo.getPassword());
		return info;
	}

	/**
	 * 刷新指定的数据源，返回结果，对于打开失败的文件型数据源刷新之后会返回一个新的数据源对象
	 *
	 * @param datasource 要刷新的数据源
	 * @return 刷新结果
	 */
	public static Datasource refreshDatasource(Datasource datasource) {
		Datasource result = datasource;

		// 内存数据源不支持刷新
		if (datasource != null
				&& !isMemoryDatasource(datasource)) {

			// 因为被占用而打开失败的文件型数据源的 reopen 组件不支持，自行实现
			if (isFileType(datasource.getEngineType())) {
				result = refreshDatasourceFile(datasource);
			} else {
				datasource.refresh();
			}
		}
		return result;
	}

	/**
	 * 刷新文件型数据源
	 *
	 * @param datasource
	 * @return
	 */
	public static Datasource refreshDatasourceFile(Datasource datasource) {
		Datasource result = datasource;

		if (datasource != null && !datasource.isOpened()) {
			DatasourceConnectionInfo srcInfo = datasource.getConnectionInfo();

			// 组件不提供判断占用的方法，使用内存工作空间打开来判断是否可以成功打开工作空间
			// 如果只读打开可以成功则表明必然是可以刷新的，否则就什么都不做，输出被占用打开失败
			DatasourceConnectionInfo tempInfo = cloneInfo(srcInfo);
			tempInfo.setReadOnly(true);
			if (attemptToOpenDatasource(tempInfo)) {
				Workspace workspace = datasource.getWorkspace();
				DatasourceConnectionInfo info = DatasourceUtilities.cloneInfo(srcInfo);
				String datasourceName = datasource.getAlias();
				boolean isReadOnlyMode = info.isReadOnly(); // 当前是否只读打开，如果独占打开失败，后面会尝试只读打开

				datasource.close();
				try {
					result = workspace.getDatasources().open(info);
				} catch (Exception e) {
					result = null;
				}

				if (result == null && !isReadOnlyMode) {
					// 如果独占打开失败，就再次尝试只读打开
					DatasourceConnectionInfo infoReadOnly = DatasourceUtilities.cloneInfo(info);
					infoReadOnly.setReadOnly(true);
					try {
						result = workspace.getDatasources().open(infoReadOnly);
					} catch (Exception e) {
						result = null;
					}
				}

				if (result == null) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(CoreProperties.getString("String_RefreshDatasouce_Failed"), datasourceName));
				}
			} else {
				Application.getActiveApplication().getOutput().output(MessageFormat.format(CoreProperties.getString("String_RefreshDatasouce_Failed"), srcInfo.getAlias()));
			}
		} else {
			datasource.refresh();
		}

		return result;
	}

	/**
	 * 判断指定的数据源是否内存数据源
	 *
	 * @param datasource
	 * @return
	 */
	public static boolean isMemoryDatasource(Datasource datasource) {
		return datasource.getConnectionInfo().getServer().equalsIgnoreCase(CoreProperties.getString("String_DatasourceServer_Memory"));
	}

	/**
	 * 判断指定的数据源是否被占用，仅 windows 有用，Linux 没用
	 *
	 * @param datasourcePath
	 * @return
	 */
	public static boolean isDatasourceOccupied(String datasourcePath) {
		File file = new File(datasourcePath);
		return !file.renameTo(file);
	}

	/**
	 * 尝试打开一下数据源，并返回是否能成功打开
	 *
	 * @param info
	 * @return
	 */
	public static boolean attemptToOpenDatasource(DatasourceConnectionInfo info) {
		boolean result = true;
		Workspace workspace = null;

		try {
			workspace = new Workspace();
			workspace.getDatasources().open(info);
			result = workspace.getDatasources().getCount() > 0;
		} catch (Exception e) {
			result = false;
		} finally {
			if (workspace != null) {
				workspace.close();
				workspace.dispose();
			}
		}
		return result;
	}

	public static Datasource getDefaultResultDatasource() {
		Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
		if (activeDatasources != null) {
			for (Datasource activeDatasource : activeDatasources) {
				if (!activeDatasource.isReadOnly()) {
					return activeDatasource;
				}
			}
		}
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (!datasources.get(i).isReadOnly()) {
				return datasources.get(i);
			}
		}
		return null;
	}

	/**
	 * 根据数据源链接信息,从当前工作空间中获取数据源
	 *
	 * @param connectionInfo
	 * @return
	 */
	public static Datasource getDatasource(DatasourceConnectionInfo connectionInfo) {
		Datasource result = null;
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (null != datasources) {
			for (int i = 0; i < datasources.getCount(); i++) {
				DatasourceConnectionInfo tempInfo = datasources.get(i).getConnectionInfo();
				if (connectionInfo.getServer().equals(tempInfo.getServer()) &&
						connectionInfo.getEngineType().equals(tempInfo.getEngineType())
						&& connectionInfo.getDatabase().equals(tempInfo.getDatabase())
						&& connectionInfo.getUser().equals(tempInfo.getUser())
						&& connectionInfo.getPassword().equals(tempInfo.getPassword())) {
					result = datasources.get(i);
					break;
				}
			}
		}
		return result;
	}
}
