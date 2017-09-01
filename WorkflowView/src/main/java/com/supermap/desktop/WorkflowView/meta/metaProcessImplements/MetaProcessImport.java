package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.*;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.WorkflowView.meta.dataconversion.*;
import com.supermap.desktop.WorkflowView.meta.loader.ImportProcessLoader;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.implement.UserDefineType.ImportSettingExcel;
import com.supermap.desktop.implement.UserDefineType.ImportSettingGPX;
import com.supermap.desktop.implement.UserDefineType.UserDefineImportResult;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.PrjCoordSysUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author XiaJT
 */
public class MetaProcessImport extends MetaProcess {

	private final static String OUTPUT_DATA = "ImportResult";
	protected ImportSetting importSetting;
	private CopyOnWriteArrayList<ReflectInfo> sourceImportParameters;
	private CopyOnWriteArrayList<ReflectInfo> resultImportParameters;
	private CopyOnWriteArrayList<ReflectInfo> paramParameters;
	private String importType = "";
	private IParameterCreator parameterCreator;
	private ImportSteppedListener importStepListener = new ImportSteppedListener() {
		@Override
		public void stepped(ImportSteppedEvent e) {
			RunningEvent event = new RunningEvent(MetaProcessImport.this, e.getSubPercent(), "");
			fireRunning(event);

			if (event.isCancel()) {
				e.setCancel(true);
			}
		}
	};
	// 针对SimpleJson，以文件夹和文件形式选择文件-yuanR2017.9.1
	private ParameterRadioButton parameterRadioButtonFileSelectType;
	private ParameterFile parameterFile;
	private ParameterFile parameterFileFolder;
	private ParameterCharset parameterCharset;
	private ParameterFile parameterChooseFile;
	private ParameterButton parameterButton;
	private ParameterTextArea parameterTextArea;
	private ParameterRadioButton parameterRadioButton;
	private ParameterDatasource parameterResultDatasource;
	private ParameterTextField datasetName;
	private boolean isSelectingChange = false;
	private boolean isSelectingFile = false;
	private PropertyChangeListener fileListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!isSelectingFile && evt.getNewValue() instanceof String && evt.getSource().equals(parameterFile)) {
				try {
					isSelectingFile = true;
					String fileName = (String) evt.getNewValue();
					//set dataset name
					String fileAlis = FileUtilities.getFileAlias(fileName);
					//文件选择器编辑过程中会不断响应，所以未修改到正确的路径时不变。JFileChooserControl是否需要一个编辑提交listener
					if (fileAlis != null) {
						if (parameterResultDatasource != null && parameterResultDatasource.getSelectedItem() != null) {
							fileAlis = parameterResultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(fileAlis);
						}
						datasetName.setSelectedItem(fileAlis);
					}
					//set charset
					if (importSetting instanceof ImportSettingTAB || importSetting instanceof ImportSettingMIF) {
						if (fileName != null && new File(fileName).exists()) {
							importSetting.setSourceFilePath(fileName);
							Charset charset = importSetting.getSourceFileCharset();
							parameterCharset.setSelectedItem(charset);
						}
					}

				} finally {
					isSelectingFile = false;
				}
			} else if (!isSelectingFile && evt.getNewValue() instanceof String && evt.getSource().equals(parameterFileFolder)) {
				try {
					isSelectingFile = true;
					String fileName = (String) evt.getNewValue();
					//set dataset name
					String fileAlis = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.length());
					if (fileAlis != null) {
						if (parameterResultDatasource != null && parameterResultDatasource.getSelectedItem() != null) {
							fileAlis = parameterResultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(fileAlis);
						}
						datasetName.setSelectedItem(fileAlis);
					}
				} finally {
					isSelectingFile = false;
				}
			}
		}
	};

	private PropertyChangeListener fileValueListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (parameterChooseFile.getSelectedItem() != null) {
				String filePath = parameterChooseFile.getSelectedItem().toString();

				// 设置投影信息
				if (!StringUtilities.isNullOrEmpty(filePath)) {
					PrjCoordSys newPrjCoorSys = new PrjCoordSys();
					String fileType = FileUtilities.getFileType(filePath);
					boolean isPrjFile;
					if (fileType.equalsIgnoreCase(".prj")) {
						isPrjFile = newPrjCoorSys.fromFile(filePath, PrjFileType.ESRI);
					} else {
						isPrjFile = newPrjCoorSys.fromFile(filePath, PrjFileType.SUPERMAP);
					}
					if (isPrjFile) {
						String prjCoorSysInfo = PrjCoordSysUtilities.getDescription(newPrjCoorSys);
						parameterTextArea.setSelectedItem(prjCoorSysInfo);
					}
				}
			}
		}
	};


	public ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialogPrjCoordSysSettings dialogPrjCoordSysSettings = new JDialogPrjCoordSysSettings();
			if (dialogPrjCoordSysSettings.showDialog() == DialogResult.OK) {
				PrjCoordSys newPrjCoordSys = dialogPrjCoordSysSettings.getPrjCoordSys();
				String prjCoorSysInfo = PrjCoordSysUtilities.getDescription(newPrjCoordSys);
				parameterTextArea.setSelectedItem(prjCoorSysInfo);
			}
		}
	};

	public MetaProcessImport(ImportSetting importSetting, String importType) {
		this.importSetting = importSetting;
		this.importType = importType;
		initParameters();
	}

	public MetaProcessImport() {
	}

	public void initParameters() {
		parameterCreator = new ImportParameterCreator();
		setResultImportParameters(parameterCreator.createResult(importSetting, this.importType));
		setSourceImportParameters(parameterCreator.createSourceInfo(importSetting, this.importType));
		setParamParameters(parameterCreator.create(importSetting));
		updateParameters();
	}

	public void updateParameters() {
		parameterResultDatasource = parameterCreator.getParameterResultDatasource();
		parameterFile = parameterCreator.getParameterFile();
		datasetName = parameterCreator.getParameterDataset();
		parameterCharset = parameterCreator.getParameterCharset();
		if (null != parameterCreator.getParameterCombineSourceInfoSet()) {
			parameters.addParameters(parameterCreator.getParameterCombineSourceInfoSet());
		}
		if (null != parameterCreator.getParameterCombineResultSet()) {
			parameters.addParameters(parameterCreator.getParameterCombineResultSet());
		}
		if (null != parameterCreator.getParameterCombineParamSet()) {
			parameters.addParameters(parameterCreator.getParameterCombineParamSet());
		}
		addOutPutParameters();
		parameterFile.addPropertyListener(this.fileListener);

		// 给文件选择类型单选框，增加监听-yuanR2017.9.1
		if (importSetting instanceof ImportSettingSimpleJson) {
			parameterFileFolder = parameterCreator.getParameterFileFolder();
			parameterFileFolder.addPropertyListener(this.fileListener);
			parameterRadioButtonFileSelectType = parameterCreator.getParameterRadioButtonFolderOrFile();
			parameterRadioButtonFileSelectType.addPropertyListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					parameterFile.setEnabled(parameterRadioButtonFileSelectType.getSelectedItem().equals(parameterRadioButtonFileSelectType.getItemAt(1)));
					parameterFileFolder.setEnabled(parameterRadioButtonFileSelectType.getSelectedItem().equals(parameterRadioButtonFileSelectType.getItemAt(0)));
					if (!parameterFile.isEnabled) {
						parameterFile.removePropertyListener(fileListener);
						parameterFile.setSelectedItem("");
						parameterFile.addPropertyListener(fileListener);
					}
					if (!parameterFileFolder.isEnabled) {
						parameterFileFolder.removePropertyListener(fileListener);
						parameterFileFolder.setSelectedItem("");
						parameterFileFolder.addPropertyListener(fileListener);
					}
				}
			});
		}

		if (importSetting instanceof ImportSettingModelOSG || importSetting instanceof ImportSettingModelX
				|| importSetting instanceof ImportSettingModelDXF || importSetting instanceof ImportSettingModelFBX
				|| importSetting instanceof ImportSettingModelFLT || importSetting instanceof ImportSettingModel3DS) {
			parameterButton = parameterCreator.getParameterButton();
			parameterButton.setActionListener(this.actionListener);
			parameterChooseFile = parameterCreator.getParameterChooseFile();
			parameterChooseFile.addPropertyListener(this.fileValueListener);
			parameterTextArea = parameterCreator.getParameterTextArea();
			parameterRadioButton = parameterCreator.getParameterSetRadioButton();
			parameterRadioButton.addPropertyListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (!isSelectingChange) {
						isSelectingChange = true;
						ParameterDataNode node = (ParameterDataNode) evt.getNewValue();
						boolean select = (boolean) node.getData();
						if (select) {
							parameterButton.setEnabled(select);
							parameterChooseFile.setEnabled(!select);
						} else {
							parameterButton.setEnabled(select);
							parameterChooseFile.setEnabled(!select);
						}
						isSelectingChange = false;
					}
				}
			});
		}
	}

	private void addOutPutParameters() {
		// TODO: 2017/6/16
		DatasetTypes types = DatasetTypes.DATASET;
		if (importSetting instanceof ImportSettingSHP || importSetting instanceof ImportSettingE00
				|| importSetting instanceof ImportSettingDWG || importSetting instanceof ImportSettingDXF
				|| importSetting instanceof ImportSettingTAB || importSetting instanceof ImportSettingMIF
				|| importSetting instanceof ImportSettingMAPGIS || importSetting instanceof ImportSettingSIT
				|| importSetting instanceof ImportSettingModelOSG || importSetting instanceof ImportSettingModel3DS
				|| importSetting instanceof ImportSettingModelX || importSetting instanceof ImportSettingKML
				|| importSetting instanceof ImportSettingKMZ || importSetting instanceof ImportSettingDGN
				|| importSetting instanceof ImportSettingVCT || importSetting instanceof ImportSettingGJB
				|| importSetting instanceof ImportSettingFileGDBVector) {
			types = DatasetTypes.SIMPLE_VECTOR;
		} else if (importSetting instanceof ImportSettingGRD || importSetting instanceof ImportSettingGBDEM
				|| importSetting instanceof ImportSettingBIL || importSetting instanceof ImportSettingRAW
				|| importSetting instanceof ImportSettingBSQ || importSetting instanceof ImportSettingBIP) {
			types = DatasetTypes.GRID;
		} else if (importSetting instanceof ImportSettingDBF || importSetting instanceof ImportSettingCSV) {
			types = DatasetTypes.TABULAR;
		} else if (importSetting instanceof ImportSettingWOR) {
			types = DatasetTypes.DATASET;
		} else if (importSetting instanceof ImportSettingIMG || importSetting instanceof ImportSettingTIF
				|| importSetting instanceof ImportSettingBMP || importSetting instanceof ImportSettingPNG
				|| importSetting instanceof ImportSettingGIF || importSetting instanceof ImportSettingJPG
				|| importSetting instanceof ImportSettingJP2 || importSetting instanceof ImportSettingMrSID
				|| importSetting instanceof ImportSettingECW) {
			// 类型可选
//			types = new DatasetTypes("gridAndImage",DatasetTypes.GRID.getValue() | DatasetTypes.IMAGE.getValue());
			types = DatasetTypes.IMAGE;
		} else if (importSetting instanceof ImportSettingTEMSVector) {
			types = DatasetTypes.LINE;
		} else if (importSetting instanceof ImportSettingTEMSBuildingVector) {
			types = DatasetTypes.REGION;
		}

		this.getParameters().addOutputParameters(OUTPUT_DATA,
				MessageFormat.format(ProcessOutputResultProperties.getString("String_InputResult"), importType),
				types, parameterCreator.getParameterCombineResultSet());
	}

	public void setImportSetting(ImportSetting importSetting) {
		this.importSetting = importSetting;
	}

	public void setResultImportParameters(CopyOnWriteArrayList<ReflectInfo> resultImportParameters) {
		this.resultImportParameters = resultImportParameters;
	}

	public void setSourceImportParameters(CopyOnWriteArrayList<ReflectInfo> sourceImportParameters) {
		this.sourceImportParameters = sourceImportParameters;
	}

	public void setParamParameters(CopyOnWriteArrayList<ReflectInfo> paramParameters) {
		this.paramParameters = paramParameters;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public String getTitle() {
		if (importType.equalsIgnoreCase("GBDEM")) {
			return MessageFormat.format(ProcessProperties.getString("String_ImportTitle"), "ArcGIS DEM");
		} else if (importType.equalsIgnoreCase("GRD_DEM")) {
			return MessageFormat.format(ProcessProperties.getString("String_ImportTitle"), ProcessProperties.getString("String_Grid") + "DEM");
		}
		return MessageFormat.format(ProcessProperties.getString("String_ImportTitle"), importType);
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		String datasetName = importSetting.getTargetDatasetName();
		Dataset dataset = DatasourceUtilities.getDataset(datasetName, importSetting.getTargetDatasource());
		if (importSetting.getImportMode().equals(ImportMode.OVERWRITE) && dataset != null) {
			ArrayList<Dataset> datasets = new ArrayList<>();
			datasets.add(dataset);
			java.util.List<Dataset> closedDatasets = DatasetUIUtilities.sureDatasetClosed(datasets);
			if (closedDatasets.size() > 0) {
				isSuccessful = doImport();
			}
		} else {
			isSuccessful = doImport();
		}

		return isSuccessful;
	}

	@Override
	public Class<? extends IProcessLoader> getLoader() {
		return ImportProcessLoader.class;
	}

	private boolean doImport() {
		boolean isSuccessful = false;
		long startTime = System.currentTimeMillis();
		long endTime;
		long time;
		if (null == ((ParameterFile) (sourceImportParameters.get(0)).parameter).getSelectedItem()) {
			fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ImportFailed")));
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_ImportFailed"));
			return isSuccessful;
		}

		if (importSetting instanceof ImportSettingGPX) {
			fireRunning(new RunningEvent(this, 0, "start"));
			importSetting.setSourceFilePath(((ParameterFile) (sourceImportParameters.get(0)).parameter).getSelectedItem().toString());
			final Datasource datasource = ((ParameterDatasource) resultImportParameters.get(0).parameter).getSelectedItem();
			importSetting.setTargetDatasource(datasource);
			importSetting.setTargetDatasetName(((ParameterTextField) resultImportParameters.get(1).parameter).getSelectedItem().toString());
			((ImportSettingGPX) importSetting).addImportSteppedListener(this.importStepListener);
			UserDefineImportResult result = ((ImportSettingGPX) importSetting).run();
			if (null != result) {
				isSuccessful = true;
				updateDatasource(result.getSuccess());
				endTime = System.currentTimeMillis(); // 获取结束时间
				time = endTime - startTime;
				printMessage(result, time);
			} else {
				fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ImportFailed")));
				Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_ImportFailed"));
			}
			((ImportSettingGPX) importSetting).removeImportSteppedListener(this.importStepListener);
		} else if (importSetting instanceof ImportSettingExcel) {
			fireRunning(new RunningEvent(this, 0, "start"));
			importSetting.setSourceFilePath(((ParameterFile) (sourceImportParameters.get(0)).parameter).getSelectedItem().toString());
			final Datasource datasource = ((ParameterDatasource) resultImportParameters.get(0).parameter).getSelectedItem();
			importSetting.setTargetDatasource(datasource);
			importSetting.setTargetDatasetName(((ParameterTextField) resultImportParameters.get(1).parameter).getSelectedItem().toString());
			((ImportSettingExcel) importSetting).setFirstRowIsField(Boolean.valueOf(((ParameterCheckBox) paramParameters.get(0).parameter).getSelectedItem()));
			((ImportSettingExcel) importSetting).addImportSteppedListener(this.importStepListener);
			startTime = System.currentTimeMillis(); // 获取开始时间
			UserDefineImportResult[] result = ((ImportSettingExcel) importSetting).run();
			if (null != result) {
				isSuccessful = true;
				endTime = System.currentTimeMillis(); // 获取结束时间
				time = endTime - startTime;
				for (UserDefineImportResult tempResult : result) {
					if (null != tempResult && null != tempResult.getSuccess()) {
						isSuccessful = true;
						updateDatasource(tempResult.getSuccess());
						printMessage(tempResult, time);
					} else {
						fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ImportFailed")));
						Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_ImportFailed"));
					}
				}
			}
			((ImportSettingExcel) importSetting).removeImportSteppedListener(importStepListener);

//		} else if (importSetting instanceof ImportSettingSimpleJson) {
//			// SimpleJson有两种导入模式，因此单开一个else if-yuanR2017.9.1
//			fireRunning(new RunningEvent(this, 0, "start"));
//			if (parameterRadioButtonFileSelectType.getSelectedItem().equals(parameterRadioButtonFileSelectType.getItemAt(1))) {
//				importSetting.setSourceFilePath(((ParameterFile) (sourceImportParameters.get(0)).parameter).getSelectedItem().toString());
//			} else {
//				importSetting.setSourceFilePath(((ParameterFile) (sourceImportParameters.get(1)).parameter).getSelectedItem().toString());
//			}
//			importSetting.setSourceFileCharset((Charset) ((ParameterCharset) sourceImportParameters.get(2).parameter).getSelectedItem());
//			final Datasource datasource = ((ParameterDatasource) resultImportParameters.get(0).parameter).getSelectedItem();
//			importSetting.setTargetDatasource(datasource);
//			importSetting.setTargetDatasetName(((ParameterTextField) resultImportParameters.get(1).parameter).getSelectedItem().toString());
//
//			startTime = System.currentTimeMillis(); // 获取开始时间
//			UserDefineImportResult[] result = ((ImportSettingExcel) importSetting).run();
//			if (null != result) {
//				isSuccessful = true;
//				endTime = System.currentTimeMillis(); // 获取结束时间
//				time = endTime - startTime;
//				for (UserDefineImportResult tempResult : result) {
//					if (null != tempResult.getSuccess()) {
//						isSuccessful = true;
//						updateDatasource(tempResult.getSuccess());
//						printMessage(tempResult, time);
//					} else {
//						fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ImportFailed")));
//						Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_ImportFailed"));
//					}
//				}
//			}
		} else {
			ImportSetting newImportSetting = new ImportSettingCreator().create(importType);
			DataImport dataImport = ImportSettingSetter.setParameter(newImportSetting, sourceImportParameters, resultImportParameters, paramParameters);
			try {
				fireRunning(new RunningEvent(this, 0, "start"));
				dataImport.addImportSteppedListener(this.importStepListener);
				ImportResult result = dataImport.run();
				ImportSetting[] succeedSettings = result.getSucceedSettings();
				if (succeedSettings.length > 0) {
					isSuccessful = true;
					updateDatasource(succeedSettings[0]);
					endTime = System.currentTimeMillis(); // 获取结束时间
					time = endTime - startTime;
					printMessage(result, time);
				} else {
					fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ImportFailed")));
					Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_ImportFailed"));
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				dataImport.removeImportSteppedListener(this.importStepListener);
			}
		}
		return isSuccessful;
	}

	private void printMessage(ImportResult result, long time) {
		ImportSetting[] successImportSettings = result.getSucceedSettings();
		ImportSetting[] failImportSettings = result.getFailedSettings();
		String successImportInfo = ProcessProperties.getString("String_FormImport_OutPutInfoOne");
		String failImportInfo = ProcessProperties.getString("String_FormImport_OutPutInfoTwo");
		if (null != successImportSettings && 0 < successImportSettings.length) {
			String[] names = result.getSucceedDatasetNames(successImportSettings[0]);
			// 创建空间索引，字段索引
			ImportSetting sucessSetting = successImportSettings[0];
			if (null != names && names.length > 0) {
				for (int j = 0; j < names.length; j++) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(successImportInfo, sucessSetting.getSourceFilePath(), "->", names[j], sucessSetting
							.getTargetDatasource().getAlias(), String.valueOf((time / names.length) / 1000.0)));
				}
			}
		} else if (null != failImportSettings && 0 < failImportSettings.length) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(failImportInfo, failImportSettings[0].getSourceFilePath(), "->", ""));
		}
	}

	private void printMessage(UserDefineImportResult result, long time) {
		if (null != result.getSuccess()) {
			String successImportInfo = ProcessProperties.getString("String_FormImport_OutPutInfoOne");
			Application.getActiveApplication().getOutput().output(MessageFormat.format(successImportInfo, result.getSuccess().getSourceFilePath(), "->", result.getSuccess().getTargetDatasetName(), result.getSuccess()
					.getTargetDatasource().getAlias(), String.valueOf(time / 1000.0)));
		}
	}

	private void updateDatasource(ImportSetting succeedSetting) {
		final Datasource datasource = succeedSetting.getTargetDatasource();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (null != datasource) {
					UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
				}
			}
		});
		if (importSetting instanceof ImportSettingWOR) {
			// 刷新地图节点
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
					DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
					MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
					UICommonToolkit.getWorkspaceManager().getWorkspaceTree().refreshNode((DefaultMutableTreeNode) treeNode.getChildAt(1));
				}
			});
		}
		Dataset dataset = datasource.getDatasets().get(succeedSetting.getTargetDatasetName());
		this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(dataset);
		fireRunning(new RunningEvent(this, 100, "finished"));
	}

	@Override
	public String getKey() {
		return MetaKeys.IMPORT + importType;
	}

}
