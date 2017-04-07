package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.conversion.ImportResult;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.dataconversion.ImportSettingSetter;
import com.supermap.desktop.process.dataconversion.ReflectInfo;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;

import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;
import com.supermap.desktop.ui.UICommonToolkit;

import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author XiaJT
 */
public class MetaProcessImport extends MetaProcess {

	private final static String OUTPUT_DATA = "ImportResult";
	private ImportSetting importSetting;
	private CopyOnWriteArrayList<ReflectInfo> defaultImportParameters;
	private CopyOnWriteArrayList<ReflectInfo> paramParameters;

	public MetaProcessImport() {
		parameters = new DefaultParameters();
		this.outputs.addData(OUTPUT_DATA, DataType.DATASET);
	}

	public void updateParameters() {
		if (null != defaultImportParameters && null != paramParameters) {
			IParameter[] tempParameters = new IParameter[defaultImportParameters.size() + paramParameters.size()];
			for (int i = 0; i < defaultImportParameters.size(); i++) {
				tempParameters[i] = defaultImportParameters.get(i).parameter;
			}
			for (int i = 0; i < paramParameters.size(); i++) {
				tempParameters[defaultImportParameters.size() + i] = paramParameters.get(i).parameter;
			}
			parameters.setParameters(tempParameters);
		} else if (null != defaultImportParameters && null == paramParameters) {
			IParameter[] tempParameters = new IParameter[defaultImportParameters.size()];
			for (int i = 0; i < defaultImportParameters.size(); i++) {
				tempParameters[i] = defaultImportParameters.get(i).parameter;
			}
			parameters.setParameters(tempParameters);
		}
	}

	public void setImportSetting(ImportSetting importSetting) {
		this.importSetting = importSetting;
	}

	public void setDefaultImportParameters(CopyOnWriteArrayList<ReflectInfo> defaultImportParameters) {
		this.defaultImportParameters = defaultImportParameters;
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
		return ProcessProperties.getString("String_Import");
	}

	@Override
	public void run() {
		ImportResult run = ImportSettingSetter.setParameter(importSetting, defaultImportParameters, paramParameters).run();
		ImportSetting[] succeedSettings = run.getSucceedSettings();
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
			this.outputs.getData(OUTPUT_DATA).setValue(dataset);
			fireRunning(new RunningEvent(this, 100, "finished"));
			setFinished(true);
		}
	}

	@Override
	public String getKey() {
		return MetaKeys.IMPORT;
	}

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/import.png");
	}
}
