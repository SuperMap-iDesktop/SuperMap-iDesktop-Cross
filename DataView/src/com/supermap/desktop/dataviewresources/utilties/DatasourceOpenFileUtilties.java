package com.supermap.desktop.dataviewresources.utilties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineFamilyType;
import com.supermap.data.EngineInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Environment;
import com.supermap.data.ErrorInfo;
import com.supermap.data.Toolkit;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit.DatasourceWrap;
import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.dialog.JDialogGetPassword;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilties.CursorUtilties;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

/**
 * 打开文件型数据源
 * 
 * @author X
 *
 */
public class DatasourceOpenFileUtilties {
	// 用到了很多controls下的类 所以不能写在core下，先放在这里吧。
	private static final String WINDOWS_FILE_CHOOSE_UI = "com.sun.java.swing.plaf.windows.WindowsFileChooserUI";
	private static final String GTK_FILE_CHOOSE_UI = "com.sun.java.swing.plaf.gtk.GTKFileChooserUI";
	private static final String MEYAL_FILE_CHOOSE_UI = "javax.swing.plaf.metal.MetalFileChooserUI";
	/**
	 * 是否不询问直接使用只读参数
	 */
	private static boolean isDontAskReadOnlyOpen = false;
	/**
	 * 是否只读打开
	 */
	private static boolean isReadOnlyOpen = false;

	/**
	 * 打开文件型数据源。 主要获取文件路径，然后调用DatasourceOpenFile方法打开
	 * 
	 * @return 成功数目
	 */

	private DatasourceOpenFileUtilties() {
		// 工具类不提供构造
	}

	public static int datasourceOpenFile() {
		resetReadOnlyProperties();

		if (!SmFileChoose.isModuleExist("DatasourceOpenFile")) {

			String fileFilterAll = SmFileChoose.createFileFilter(DataViewProperties.getString("String_FileFilters_SupportType"), getAllSupportFileExtensions());

			String UDBDescribe = DataViewProperties.getString("String_FileFilters_Datasourse");
			String fileFilterUdb = getTypedSupportFileExtensions(EngineType.UDB) == null || getTypedSupportFileExtensions(EngineType.UDB).length <= 0 ? null
					: SmFileChoose.createFileFilter(UDBDescribe, "udb");

			String ImageDescribe = SystemPropertyUtilties.isWindows() ? DataViewProperties.getString("String_FileFilters_ImagePlugins_Win")
					: DataViewProperties.getString("String_FileFilters_ImagePlugins_Linux");
			String fileFilterImagePlugin = getTypedSupportFileExtensions(EngineType.IMAGEPLUGINS) == null
					|| getTypedSupportFileExtensions(EngineType.IMAGEPLUGINS).length <= 0 ? null : SmFileChoose.createFileFilter(ImageDescribe,
					getTypedSupportFileExtensions(EngineType.IMAGEPLUGINS));

			String VectorDescribe = SystemPropertyUtilties.isWindows() ? DataViewProperties.getString("String_FileFilters_Vector_Win") : DataViewProperties
					.getString("String_FileFilters_Vector_Linux");
			String fileFilterVector = getTypedSupportFileExtensions(EngineType.VECTORFILE) == null
					|| getTypedSupportFileExtensions(EngineType.VECTORFILE).length <= 0 ? null : SmFileChoose.createFileFilter(VectorDescribe,
					getTypedSupportFileExtensions(EngineType.VECTORFILE));

			String fileFiltersString = SmFileChoose.bulidFileFilters(fileFilterAll, fileFilterUdb, fileFilterImagePlugin, fileFilterVector);
			SmFileChoose.addNewNode(fileFiltersString, CommonProperties.getString("String_DefaultFilePath"),
					DataViewProperties.getString("String_Title_DatasoursesOpenFile"), "DatasourceOpenFile", "OpenMany");
		}

		SmFileChoose openFileDlg = new SmFileChoose("DatasourceOpenFile");

		// 只读打开
		JCheckBox checkBoxReadOnlyOpen = new JCheckBox(DataViewProperties.getString("String_ReadOnlyOpen"));
		addReadOnlyCheckBox(openFileDlg, checkBoxReadOnlyOpen);

		int successCount = 0;
		// 选择文件不为空
		if (openFileDlg.showDefaultDialog() == JFileChooser.APPROVE_OPTION) {
			boolean readOnlyOpen = false;
			if (checkBoxReadOnlyOpen.isSelected()) {
				DatasourceOpenFileUtilties.setIsDontAskReadOnlyOpen(true);
				DatasourceOpenFileUtilties.setIsReadOnlyOpen(true);
				readOnlyOpen = true;
			}
			File[] files = openFileDlg.getSelectFiles();
			if (files != null) {
				for (File file : files) {
					Datasource datasource = DatasourceOpenFileUtilties.openDatasourceFile(file, readOnlyOpen);
					if (datasource != null) {
						successCount++;
					}
				}
				if (files.length > 1) {
					String message = String.format(CoreProperties.getString("String_OpenDatasourceResultMsg"), files.length, successCount, files.length
							- successCount);
					Application.getActiveApplication().getOutput().output(message);
				}
			}
		}
		ToolbarUtilties.updataToolbarsState();
		return successCount;
	}

	/**
	 * 添加只读打开选项
	 * 
	 * @param openFileDlg
	 * @param isReadOpen
	 */
	private static void addReadOnlyCheckBox(SmFileChoose openFileDlg, JCheckBox isReadOpen) {
		if (openFileDlg.getUI().getClass().getName().equals(DatasourceOpenFileUtilties.WINDOWS_FILE_CHOOSE_UI)) {
			((JPanel) ((JPanel) openFileDlg.getComponent(2)).getComponent(1)).add(isReadOpen, BorderLayout.AFTER_LAST_LINE);
		} else if (openFileDlg.getUI().getClass().getName().equals(DatasourceOpenFileUtilties.GTK_FILE_CHOOSE_UI)) {
			((JPanel) openFileDlg.getComponent(1)).add(isReadOpen, BorderLayout.AFTER_LAST_LINE);
		} else if (openFileDlg.getUI().getClass().getName().equals(DatasourceOpenFileUtilties.MEYAL_FILE_CHOOSE_UI)) {
			((JPanel) openFileDlg.getComponent(2)).add(isReadOpen, BorderLayout.AFTER_LAST_LINE);
		}
	}

	/**
	 * 根据文件打开数据源，失败时返回null。 如果单独使用此方法需要先调用resetReadOnlyProperties方法。
	 * 
	 * @param file 数据源文件
	 * @return
	 */
	public static Datasource openFileDatasource(File file) {
		Datasource datasource = openDatasourceFile(file, false);
		ToolbarUtilties.updataToolbarsState();
		return datasource;
	}

	/**
	 * 根据文件打开数据源，失败时返回null。 如果单独使用此方法需要先调用resetReadOnlyProperties方法。
	 * 
	 * @param file 数据源文件
	 * @return
	 */
	public static Datasource openDatasourceFile(File file, boolean isReadOnlyOpen) {

		String message = null;
		// 以后有需求时可在各部分修改对应的Message，暂时先统一输出吧。
		String openFailedMessage = MessageFormat.format(DataViewProperties.getString("String_DataSource_Openfail"), file.getPath());
		String[] pluginString = getTypedSupportFileExtensions(EngineType.IMAGEPLUGINS);
		String[] vectorString = getTypedSupportFileExtensions(EngineType.VECTORFILE);
		Set<String> pluginSet = new HashSet<String>();
		Set<String> vectorSet = new HashSet<String>();
		if (pluginString != null && pluginString.length > 0) {
			for (int i = 0; i < pluginString.length; i++) {
				pluginSet.add(pluginString[i]);
				pluginSet.add(pluginString[i].toUpperCase());
			}
		}
		if (vectorString != null && vectorString.length > 0) {
			for (int i = 0; i < vectorString.length; i++) {
				vectorSet.add(vectorString[i]);
				vectorSet.add(vectorString[i].toUpperCase());
			}
		}
		String filePath = file.getPath();
		if (-1 != filePath.lastIndexOf(".")) {
			// 根据文件类型判断使用引擎，以及是否文件可写。

			EngineType engineType = null;
			boolean isMustReadOnlyOpen = false;
			boolean isReadOnlyOpenTemp = isReadOnlyOpen;
			String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase();// 注：linux上文件名区分大小写，此处暂没做处理。

			if ("udb".equals(fileType)) {
				engineType = EngineType.UDB;

				// 不知道组件是按文件关系还是文件名找udd文件,所以此处大小写后缀都判断吧
				String uddLowerFilePath = filePath.substring(0, filePath.length() - 1) + "d";
				String uddUpperFilePath = filePath.substring(0, filePath.length() - 1) + "D";
				isMustReadOnlyOpen = (new File(uddLowerFilePath).exists() && !new File(uddLowerFilePath).canWrite())
						|| (new File(uddUpperFilePath).exists() && !new File(uddUpperFilePath).canWrite());

			} else if (pluginSet.contains(fileType)) {
				engineType = EngineType.IMAGEPLUGINS;
			} else if (vectorSet.contains(fileType)) {
				engineType = EngineType.VECTORFILE;
			}

			isMustReadOnlyOpen = isMustReadOnlyOpen || !file.canWrite();

			// VECTORFILE直接只读打开
			if (engineType == null) {
				return null;
			} else if (engineType == EngineType.VECTORFILE || engineType == EngineType.IMAGEPLUGINS) {
				isMustReadOnlyOpen = false;
				isReadOnlyOpenTemp = true;
			}
			// 如果文件为只读
			if (isMustReadOnlyOpen) {
				// 如果需要询问
				if (!DatasourceOpenFileUtilties.getIsDontAskReadOnlyOpen()) {
					message = MessageFormat.format(CoreProperties.getString("String_OpenReadOnlyDatasourceWarning"), filePath);
					int result = showIsReadOpenJOpition(message);
					if (result == JOptionPane.YES_OPTION) {
						isReadOnlyOpenTemp = DatasourceOpenFileUtilties.getIsReadOnlyOpen();
					} else if (result == JOptionPane.NO_OPTION) {
						Application.getActiveApplication().getOutput().output(openFailedMessage);
						return null;
					} else {
						return null;
					}
				} else if (DatasourceOpenFileUtilties.getIsReadOnlyOpen()) {
					// 不询问直接已读打开
					isReadOnlyOpenTemp = true;
				} else {
					// 不询问且统一不打开
					Application.getActiveApplication().getOutput().output(openFailedMessage);
					return null;
				}
			}

			Datasource resultDatasource = DatasourceOpenFileUtilties.isDatasourceOpened(filePath);
			if (null != resultDatasource) {
				UICommonToolkit.refreshSelectedDatasourceNode(resultDatasource.getAlias());
				DatasourceWrap.addDatasourceToRecentFile(resultDatasource);
				return resultDatasource;
			} else {
				// 数据源没打开，老实自己打开吧。
				final DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
				connectionInfo.setServer(filePath);
				connectionInfo.setAlias(DatasourceWrap.getAvailableDatasourceAlias(file.getName().substring(0, file.getName().lastIndexOf(".")), 0));
				connectionInfo.setEngineType(engineType);
				connectionInfo.setReadOnly(isReadOnlyOpenTemp);
				try {
					CursorUtilties.setWaitCursor();
					resultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().open(connectionInfo);
				} catch (Exception e) {
					// 有可能密码错误，这里先不输出
				} finally {
					CursorUtilties.setDefaultCursor();
				}
				if (resultDatasource != null) {
					UICommonToolkit.refreshSelectedDatasourceNode(resultDatasource.getAlias());
					DatasourceWrap.addDatasourceToRecentFile(resultDatasource);
					return resultDatasource;
				} else {
					// 判断是否为密码错误
					boolean isPasswordWrong = false;

					// TODO 先不判断 密码错误返回的代码不一致,后面如果有其他错误方式再改，此处代码先不删除
					ErrorInfo[] errorInfos = Toolkit.getLastErrors(1);

					for (int i = 0; i < errorInfos.length; i++) {
						if (errorInfos[i].getMarker().equals(CoreProperties.getString("String_UGS_PASSWORD"))
								|| errorInfos[i].getMarker().equals(CoreProperties.getString("String_UGS_PASSWORDError"))) {
							isPasswordWrong = true;
							break;
						} else if ("-100".equalsIgnoreCase(errorInfos[i].getMarker()) && "sit".equalsIgnoreCase(fileType)) {
							isPasswordWrong = true;
							break;
						}
					}
					if (isPasswordWrong) {
						JDialogGetPassword dialogGetPassword = new JDialogGetPassword(MessageFormat.format(
								DataViewProperties.getString("String_InputDatasourcePassword"), filePath)) {
							private static final long serialVersionUID = 1L;

							@Override
							public boolean isRightPassword(String password) {
								return abstractIsRightPassword(connectionInfo, password);
							}

							private boolean abstractIsRightPassword(final DatasourceConnectionInfo connectionInfo, String password) {
								connectionInfo.setPassword(password);
								Datasource datasource = null;

								try {
									datasource = Application.getActiveApplication().getWorkspace().getDatasources().open(connectionInfo);
								} catch (Exception e) {
									// 密码错误 不处理
								}
								if (datasource == null) {
									return false;
								} else {
									UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
									DatasourceWrap.addDatasourceToRecentFile(datasource);
									return true;
								}
							}
						};

						if (DialogResult.OK == dialogGetPassword.showDialog()) {
							resultDatasource = UICommonToolkit.getDatasource(UICommonToolkit.getWorkspaceManager().getWorkspaceTree());
							return resultDatasource;
						} else {
							Application.getActiveApplication().getOutput().output(openFailedMessage);
							return null;
						}
					} else {
						Application.getActiveApplication().getOutput().output(openFailedMessage);
						return null;
					}
				}
			}
		} else {
			// 文件无后缀名，不能判断类型
			Application.getActiveApplication().getOutput().output(openFailedMessage);
			return null;
		}
	}

	/**
	 * 重置只读打开属性
	 */
	public static void resetReadOnlyProperties() {
		DatasourceOpenFileUtilties.setIsDontAskReadOnlyOpen(false);
		DatasourceOpenFileUtilties.setIsReadOnlyOpen(false);
	}

	/**
	 * 判断当前数据源是否已经打开
	 * 
	 * @param filePath 数据源的文件路径
	 * @return
	 */
	public static Datasource isDatasourceOpened(String filePath) {
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).getConnectionInfo().getServer().equals(filePath)) {
				return datasources.get(i);
			}
		}
		return null;
	}

	/**
	 * 数据源只读时弹出提示，并根据选项设置对应的属性
	 * 
	 * @param message
	 * @return
	 */
	private static int showIsReadOpenJOpition(String message) {
		JCheckBox dontAskAgain = new JCheckBox(DataViewProperties.getString("String_DontAskAgain"));
		Object[] params = { message, dontAskAgain };
		int result = JOptionPane.showConfirmDialog((Component) Application.getActiveApplication().getMainFrame(), params,
				CoreProperties.getString("String_MessageBox_Title"), JOptionPane.INFORMATION_MESSAGE);
		boolean checkBoxState = dontAskAgain.isSelected();
		if (checkBoxState) {
			DatasourceOpenFileUtilties.setIsDontAskReadOnlyOpen(true);
		}
		if (result == JOptionPane.YES_OPTION) {
			DatasourceOpenFileUtilties.setIsReadOnlyOpen(true);
		} else if (result == JOptionPane.NO_OPTION) {
			DatasourceOpenFileUtilties.setIsReadOnlyOpen(false);
		}
		return result;
	}

	// get set方法
	public static void setIsDontAskReadOnlyOpen(boolean isDontAskReadOnlyOpen) {
		DatasourceOpenFileUtilties.isDontAskReadOnlyOpen = isDontAskReadOnlyOpen;
	}

	public static void setIsReadOnlyOpen(boolean isReadOnlyOpen) {
		DatasourceOpenFileUtilties.isReadOnlyOpen = isReadOnlyOpen;
	}

	public static boolean getIsDontAskReadOnlyOpen() {
		return DatasourceOpenFileUtilties.isDontAskReadOnlyOpen;
	}

	public static boolean getIsReadOnlyOpen() {
		return DatasourceOpenFileUtilties.isReadOnlyOpen;
	}

	/**
	 * 支持的文件类型引擎
	 * 
	 * @return
	 */
	private static EngineInfo[] getFileInfos() {
		EngineInfo[] engineInfos = Environment.getCurrentLoadedEngineInfos();
		CopyOnWriteArrayList<EngineInfo> list = new CopyOnWriteArrayList<EngineInfo>();
		for (EngineInfo engineInfo : engineInfos) {
			if (EngineFamilyType.FILE == engineInfo.getEngineFamily()) {
				list.add(engineInfo);
			}
		}
		return (EngineInfo[]) list.toArray(new EngineInfo[list.size()]);
	}

	/**
	 * 去掉文件后缀中的.符号
	 * 
	 * @param engineInfo
	 * @return
	 */
	public static String[] getEngineSupportFileExtensions(EngineInfo engineInfo) {
		String[] fileExtensions = engineInfo.getSupportExtensions();
		for (int i = 0; i < fileExtensions.length; i++) {
			fileExtensions[i] = fileExtensions[i].substring(1);
		}
		return fileExtensions;
	}

	/**
	 * 所有支持的文件类型
	 * 
	 * @return
	 */
	public static String[] getAllSupportFileExtensions() {
		EngineInfo[] fileEngineInfo = DatasourceOpenFileUtilties.getFileInfos();

		CopyOnWriteArrayList<String> arrayListAllSupportType = new CopyOnWriteArrayList<>();
		for (int i = 0; i < fileEngineInfo.length; i++) {
			for (String fileExtensions : getEngineSupportFileExtensions(fileEngineInfo[i])) {
				// 云维英说不干，我们也不干！
				if (!"udd".equals(fileExtensions) && !"gci".equals(fileExtensions) && !"sci3d".equals(fileExtensions) && !"sct".equals(fileExtensions)) {
					arrayListAllSupportType.add(fileExtensions);
				}
			}
		}
		return (String[]) arrayListAllSupportType.toArray(new String[arrayListAllSupportType.size()]);
	}

	/**
	 * UDB引擎
	 * 
	 * @return
	 */
	public static String[] getTypedSupportFileExtensions(EngineType engineType) {
		EngineInfo[] fileEngineInfo = DatasourceOpenFileUtilties.getFileInfos();
		for (int i = 0; i < fileEngineInfo.length; i++) {
			if (engineType == fileEngineInfo[i].getType()) {
				if (engineType.equals(EngineType.IMAGEPLUGINS)) {
					List<String> strings = new ArrayList<>();
					for (String s : fileEngineInfo[i].getSupportExtensions()) {
						// 云维英说不干，我们也不干！
						if (!".gci".equals(s) && !".sci3d".equals(s) && !".sct".equals(s)) {
							strings.add(s.substring(1));
						}
					}
					return (String[]) strings.toArray(new String[strings.size()]);
				} else {
					return getEngineSupportFileExtensions(fileEngineInfo[i]);
				}
			}
		}
		return new String[0];
	}
}
