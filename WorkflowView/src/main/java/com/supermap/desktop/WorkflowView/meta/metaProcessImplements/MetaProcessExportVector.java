package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Charset;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics.MetaProcessAbstractExport;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/6/28.
 */
public class MetaProcessExportVector extends MetaProcessAbstractExport {

	private ParameterCombine vectorCombine;
	private ParameterCheckBox externalData;
	private ParameterCheckBox externalRecord;
	private ParameterCharset charset;
	private ParameterComboBox cadVersion;
	private ParameterTextArea expression;
	private ParameterSQLExpression sqlExpression;

	private ExportSteppedListener exportListener = new ExportSteppedListener() {
		@Override
		public void stepped(ExportSteppedEvent e) {
			RunningEvent event = new RunningEvent(MetaProcessExportVector.this, e.getSubPercent(), "");
			fireRunning(event);
			if (event.isCancel()) {
				e.setCancel(true);
			}
		}
	};
	private PropertyChangeListener sqlExpressionChangedListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!isSelectChanged && null != evt.getNewValue()) {
				isSelectChanged = true;
				expression.setSelectedItem(sqlExpression.getSelectedItem());
				isSelectChanged = false;
			}
		}
	};

	public MetaProcessExportVector() {
		this.OUTPUT_DATA_TYPE = ControlsProperties.getString("String_Vector");
		initParameters();
		registEvents();
	}

	protected void initParameters() {
		super.initParameters();
		this.dataset.setDatasetTypes(DatasetTypeUtilities.getDatasetTypeVector());
		this.dataset.setSelectedItem(DatasetUtilities.getDefaultDatasetVector());
		String module = "ExportVector_OutPutDirectories";
		if (!SmFileChoose.isModuleExist(module)) {
			SmFileChoose.addNewNode("", System.getProperty("user.dir"), ProcessProperties.getString("String_Export"),
					module, "GetDirectories");
		}
		exportPath.setModuleName(module);
		this.vectorCombine = new ParameterCombine();
		this.vectorCombine.setDescribe(ProcessProperties.getString("String_ParamSet"));
		this.externalData = new ParameterCheckBox(ProcessProperties.getString("String_ExportExternalData"));
		this.externalData.setEnabled(false);
		this.externalRecord = new ParameterCheckBox(ProcessProperties.getString("String_ExportExternalRecord"));
		this.externalRecord.setEnabled(false);
		this.charset = new ParameterCharset();
		this.charset.setEnabled(false);
		this.cadVersion = new ParameterComboBox(ProcessProperties.getString("String_CADVersion"));
		this.cadVersion.setItems(new ParameterDataNode("CAD2007", CADVersion.CAD2007)
				, new ParameterDataNode("CAD2004", CADVersion.CAD2004), new ParameterDataNode("CAD2000", CADVersion.CAD2000)
				, new ParameterDataNode("CAD12", CADVersion.CAD12), new ParameterDataNode("CAD14", CADVersion.CAD14),
				new ParameterDataNode("CAD13", CADVersion.CAD13));
		this.cadVersion.setEnabled(false);
		this.expression = new ParameterTextArea(ProcessProperties.getString("String_ExpressionForTextArea"));
		this.expression.setEnabled(false);
		this.sqlExpression = new ParameterSQLExpression(ProcessProperties.getString("String_Expression"));
		this.sqlExpression.setEnabled(false);
		this.sqlExpression.setAnchor(GridBagConstraints.EAST);

		ParameterCombine emptyCombine = new ParameterCombine(ParameterCombine.HORIZONTAL);
		emptyCombine.addParameters(new ParameterLabel(), new ParameterLabel(), this.sqlExpression);

		this.vectorCombine.addParameters(this.externalData, this.externalRecord, this.charset
				, this.cadVersion, this.expression, emptyCombine);
		this.parameters.setParameters(this.sourceInfo, this.basicCombine, this.vectorCombine);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, this.sourceInfo);
		resetDataset();
	}

	protected void resetDataset() {
		super.resetDataset();
		if (null != this.selectDataset) {
			this.sqlExpression.setSelectDataset(this.selectDataset);
		}
		initComponentsState(exportSetting);
	}

	protected void resetExportSetting() {
		super.resetExportSetting();
		initComponentsState(exportSetting);
	}

	private void initComponentsState(ExportSetting newExportSetting) {
		this.externalData.setEnabled(false);
		this.externalRecord.setEnabled(false);
		this.cadVersion.setEnabled(false);
		if (newExportSetting instanceof ExportSettingDWG || newExportSetting instanceof ExportSettingDXF) {
			this.externalData.setEnabled(true);
			this.externalRecord.setEnabled(true);
			this.cadVersion.setEnabled(true);
			if (newExportSetting instanceof ExportSettingDWG) {
				this.externalData.setSelectedItem(String.valueOf(((ExportSettingDWG) newExportSetting).isExportingExternalData()));
				this.externalRecord.setSelectedItem(String.valueOf(((ExportSettingDWG) newExportSetting).isExportingXRecord()));
				this.cadVersion.setSelectedItem(((ExportSettingDWG) newExportSetting).getVersion());
			} else {
				this.externalData.setSelectedItem(String.valueOf(((ExportSettingDXF) newExportSetting).isExportingExternalData()));
				this.externalRecord.setSelectedItem(String.valueOf(((ExportSettingDXF) newExportSetting).isExportingXRecord()));
				this.cadVersion.setSelectedItem(((ExportSettingDXF) newExportSetting).getVersion());
			}
		}
		if (null != newExportSetting) {
			this.charset.setEnabled(true);
			this.charset.setSelectedItem(newExportSetting.getTargetFileCharset());
			this.expression.setEnabled(true);
			this.sqlExpression.setEnabled(true);
			this.expression.setSelectedItem(newExportSetting.getFilter());
		}
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_ExportVector");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		boolean isOverwrite = Boolean.valueOf(cover.getSelectedItem().toString());
		String targetPath = getFilePath();
		if (StringUtilities.isNullOrEmpty(targetPath)) {
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_ExportPathIsNull"));
			return false;
		}
		if (new File(targetPath).exists() && !isOverwrite) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_DuplicateFileError"), targetPath));
		} else if (!StringUtilities.isNullOrEmpty(targetPath)) {
			setExportSettingInfo(isOverwrite);
			isSuccessful = printResultInfo(isSuccessful, targetPath, this.exportListener);
			removeEvents();
		}
		return isSuccessful;
	}

	@Override
	protected void setExportSettingInfo(boolean isOverwrite) {
		super.setExportSettingInfo(isOverwrite);
		this.exportSetting.setTargetFileCharset((Charset) charset.getSelectedData());
		this.exportSetting.setFilter(expression.getSelectedItem().toString());
		if (exportSetting instanceof ExportSettingDWG) {
			((ExportSettingDWG) exportSetting).setExportingExternalData(Boolean.valueOf(externalData.getSelectedItem().toString()));
			((ExportSettingDWG) exportSetting).setExportingXRecord(Boolean.valueOf(externalRecord.getSelectedItem().toString()));
			((ExportSettingDWG) exportSetting).setVersion((CADVersion) cadVersion.getSelectedData());
		}
		if (exportSetting instanceof ExportSettingDXF) {
			((ExportSettingDXF) exportSetting).setExportingExternalData(Boolean.valueOf(externalData.getSelectedItem().toString()));
			((ExportSettingDXF) exportSetting).setExportingXRecord(Boolean.valueOf(externalRecord.getSelectedItem().toString()));
			((ExportSettingDXF) exportSetting).setVersion((CADVersion) cadVersion.getSelectedData());
		}
	}

	@Override
	protected void registEvents() {
		super.registEvents();
		this.sqlExpression.addPropertyListener(this.sqlExpressionChangedListener);
	}

	@Override
	protected void removeEvents() {
		super.removeEvents();
		this.sqlExpression.removePropertyListener(this.sqlExpressionChangedListener);
	}

	@Override
	public String getKey() {
		return MetaKeys.EXPORTVECTOR;
	}
}
