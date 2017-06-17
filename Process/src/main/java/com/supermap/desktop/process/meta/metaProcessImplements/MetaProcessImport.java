package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.Charset;
import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjFileType;
import com.supermap.data.conversion.DataImport;
import com.supermap.data.conversion.ImportResult;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingBIL;
import com.supermap.data.conversion.ImportSettingBIP;
import com.supermap.data.conversion.ImportSettingBMP;
import com.supermap.data.conversion.ImportSettingBSQ;
import com.supermap.data.conversion.ImportSettingCSV;
import com.supermap.data.conversion.ImportSettingDBF;
import com.supermap.data.conversion.ImportSettingDGN;
import com.supermap.data.conversion.ImportSettingDWG;
import com.supermap.data.conversion.ImportSettingDXF;
import com.supermap.data.conversion.ImportSettingE00;
import com.supermap.data.conversion.ImportSettingECW;
import com.supermap.data.conversion.ImportSettingFileGDBVector;
import com.supermap.data.conversion.ImportSettingGBDEM;
import com.supermap.data.conversion.ImportSettingGIF;
import com.supermap.data.conversion.ImportSettingGJB;
import com.supermap.data.conversion.ImportSettingGRD;
import com.supermap.data.conversion.ImportSettingIMG;
import com.supermap.data.conversion.ImportSettingJP2;
import com.supermap.data.conversion.ImportSettingJPG;
import com.supermap.data.conversion.ImportSettingKML;
import com.supermap.data.conversion.ImportSettingKMZ;
import com.supermap.data.conversion.ImportSettingMAPGIS;
import com.supermap.data.conversion.ImportSettingMIF;
import com.supermap.data.conversion.ImportSettingModel3DS;
import com.supermap.data.conversion.ImportSettingModelDXF;
import com.supermap.data.conversion.ImportSettingModelFBX;
import com.supermap.data.conversion.ImportSettingModelFLT;
import com.supermap.data.conversion.ImportSettingModelOSG;
import com.supermap.data.conversion.ImportSettingModelX;
import com.supermap.data.conversion.ImportSettingMrSID;
import com.supermap.data.conversion.ImportSettingPNG;
import com.supermap.data.conversion.ImportSettingRAW;
import com.supermap.data.conversion.ImportSettingSHP;
import com.supermap.data.conversion.ImportSettingSIT;
import com.supermap.data.conversion.ImportSettingTAB;
import com.supermap.data.conversion.ImportSettingTEMSBuildingVector;
import com.supermap.data.conversion.ImportSettingTEMSVector;
import com.supermap.data.conversion.ImportSettingTIF;
import com.supermap.data.conversion.ImportSettingVCT;
import com.supermap.data.conversion.ImportSettingWOR;
import com.supermap.data.conversion.ImportSteppedEvent;
import com.supermap.data.conversion.ImportSteppedListener;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.dataconversion.IParameterCreator;
import com.supermap.desktop.process.dataconversion.ImportParameterCreator;
import com.supermap.desktop.process.dataconversion.ImportSettingSetter;
import com.supermap.desktop.process.dataconversion.ReflectInfo;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.ParameterButton;
import com.supermap.desktop.process.parameter.implement.ParameterCharset;
import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.process.parameter.implement.ParameterRadioButton;
import com.supermap.desktop.process.parameter.implement.ParameterTextArea;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.PrjCoordSysUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.MessageFormat;
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
	private ParameterFile parameterFile;
	private ParameterCharset parameterCharset;
	private ParameterFile parameterChooseFile;
	private ParameterButton parameterButton;
	private ParameterTextArea parameterTextArea;
	private ParameterRadioButton parameterRadioButton;
	private ParameterTextField datasetName;
	private boolean isSelectingChange = false;
	private boolean isSelectingFile = false;
	private PropertyChangeListener fileListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!isSelectingFile && evt.getNewValue() instanceof String) {
				try {
					isSelectingFile = true;
					String fileName = (String) evt.getNewValue();
					//set dataset name
					String fileAlis = FileUtilities.getFileAlias(fileName);
					//文件选择器编辑过程中会不断响应，所以未修改到正确的路径时不变。JFileChooserControl是否需要一个编辑提交listener
					if (fileAlis != null) {
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

		this.getParameters().addOutputParameters(OUTPUT_DATA, types, parameterCreator.getParameterCombineResultSet());
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
	public void run() {
		boolean isSuccessful = false;
		DataImport dataImport = ImportSettingSetter.setParameter(importSetting, sourceImportParameters, resultImportParameters, paramParameters);

		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			dataImport.addImportSteppedListener(this.importStepListener);
			ImportResult result = dataImport.run();
			ImportSetting[] succeedSettings = result.getSucceedSettings();
			if (succeedSettings.length > 0) {
				final Datasource datasource = succeedSettings[0].getTargetDatasource();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (null != datasource) {
							UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
						}
					}
				});
				Dataset dataset = datasource.getDatasets().get(succeedSettings[0].getTargetDatasetName());
				this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(dataset);
				isSuccessful = dataset != null;
				fireRunning(new RunningEvent(this, 100, "finished"));
			} else {
				fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ImportFailed")));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}finally {
			dataImport.removeImportSteppedListener(this.importStepListener);
		}

		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.IMPORT + importType;
	}

}
