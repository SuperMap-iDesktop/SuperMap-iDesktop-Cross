package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.Charset;
import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.conversion.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.dataconversion.IParameterCreator;
import com.supermap.desktop.process.dataconversion.ImportParameterCreator;
import com.supermap.desktop.process.dataconversion.ImportSettingSetter;
import com.supermap.desktop.process.dataconversion.ReflectInfo;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterCharset;
import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.FileUtilities;

import javax.swing.*;
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
    private ParameterTextField datasetName;
	private ParameterCharset parameterCharset;

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

	public MetaProcessImport(ImportSetting importSetting, String importType) {
		this.importSetting = importSetting;
		this.importType = importType;
		initParameters();
	}

	public MetaProcessImport() {
		parameters = new DefaultParameters();
	}

	public void initParameters() {
		parameters = new DefaultParameters();

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
	    }
	    parameters.addParameters(parameterCreator.getParameterCombineParamSet());
	    this.getParameters().addOutputParameters(OUTPUT_DATA, DatasetTypes.DATASET, parameterCreator.getParameterCombineResultSet());
	    parameterFile.addPropertyListener(this.fileListener);
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
        fireRunning(new RunningEvent(this, 0, "start"));
	    DataImport dataImport = ImportSettingSetter.setParameter(importSetting, sourceImportParameters, resultImportParameters, paramParameters);
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
	        fireRunning(new RunningEvent(this, 100, "finished"));
            setFinished(true);
        } else {
            fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ImportFailed")));
            setFinished(true);
        }
        dataImport.removeImportSteppedListener(this.importStepListener);
    }

	@Override
	public String getKey() {
		return MetaKeys.IMPORT + importType;
	}

}
