package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.PrjCoordSys;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.PrjCoordSysUtilities;
import com.supermap.desktop.utilities.PropertyManagerUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author caolp
 */
public class MetaProcessSetProjection extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "SetProjectionResult";
	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset parameterSingleDataset;
	private ParameterTextField coordSysName;
	private ParameterTextField coordUnit;
	private ParameterTextArea textAreaCoordInfo;
	private ParameterButton parameterButton;
	private ParameterCombine parameterCombineSourceData;
	private ParameterCombine parameterCombineCoordInfo;
	private PrjCoordSys prj;

	@Override
	protected String getCOMPLETED_MESSAGE() {
		return ProcessProperties.getString("String_setProjectionSuccessed");
	}

	@Override
	protected String getFAILED_MESSAGE() {
		return ProcessProperties.getString("String_setProjectionFailed");
	}

	public MetaProcessSetProjection() {
		initParameters();
		initComponentState();
		registerEvents();

	}

	private void initParameters() {
		this.datasource = new ParameterDatasourceConstrained();
		// 不支持可读
		this.datasource.setReadOnlyNeeded(false);
		this.parameterSingleDataset = new ParameterSingleDataset();
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset();
		if (defaultDataset != null) {
			this.parameterSingleDataset.setSelectedItem(defaultDataset);
			this.datasource.setSelectedItem(defaultDataset.getDatasource());
		}
		this.datasource.setDescribe(CommonProperties.getString("String_Label_Datasource"));
		this.parameterCombineSourceData = new ParameterCombine();
		this.parameterCombineSourceData.addParameters(this.datasource, this.parameterSingleDataset);
		this.parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		this.coordSysName = new ParameterTextField(ControlsProperties.getString("String_Message_CoordSysName"));
		this.coordSysName.setEnabled(false);
		this.coordUnit = new ParameterTextField(ControlsProperties.getString("String_ProjectionInfoControl_LabelGeographyUnit"));
		this.coordUnit.setEnabled(false);
		this.textAreaCoordInfo = new ParameterTextArea();
		this.textAreaCoordInfo.setEnabled(false);
		this.parameterButton = new ParameterButton(ControlsProperties.getString("String_ProjectionInfoControl_ButtonResetProjectionInfo"));
		this.parameterCombineCoordInfo = new ParameterCombine();
		this.parameterCombineCoordInfo.setDescribe(ControlsProperties.getString("String_ProjectionInfoControl_LabelProjectionInfo"));
		this.parameterCombineCoordInfo.addParameters(this.coordSysName, this.coordUnit, this.textAreaCoordInfo, new ParameterCombine(ParameterCombine.HORIZONTAL).addParameters(this.parameterButton));
		this.parameters.setParameters(this.parameterCombineSourceData, this.parameterCombineCoordInfo);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.DATASET, this.parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_ResetProjectionResult"),
				DatasetTypes.DATASET, this.parameterSingleDataset);
		//Add Constraint
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.parameterSingleDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

	}

	private void initComponentState() {
		PrjCoordSys prjCoordSys = null;
		Datasource dataSource = null;
		if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof Dataset) {
				prjCoordSys = ((Dataset) this.getParameters().getInputs().getData(INPUT_DATA).getValue()).getPrjCoordSys();
			} else if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
				if (Application.getActiveApplication().getActiveDatasets().length > 0) {
					prjCoordSys = Application.getActiveApplication().getActiveDatasets()[0].getPrjCoordSys();
				} else {
					dataSource = Application.getActiveApplication().getActiveDatasources()[0];
					if (dataSource.getDatasets().getCount() > 0) {
						prjCoordSys = dataSource.getDatasets().get(0).getPrjCoordSys();
					} else {
						dataSource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
						if (dataSource.getDatasets().getCount() > 0) {
							prjCoordSys = dataSource.getDatasets().get(0).getPrjCoordSys();
						} else {
							prjCoordSys = dataSource.getPrjCoordSys();
						}
					}
				}
			} else {
				dataSource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
				if (dataSource.getDatasets().getCount() > 0) {
					prjCoordSys = dataSource.getDatasets().get(0).getPrjCoordSys();
				} else {
					prjCoordSys = dataSource.getPrjCoordSys();
				}
			}
			if (null != prjCoordSys) {
				this.coordSysName.setSelectedItem(prjCoordSys.getName());
				this.coordUnit.setSelectedItem(prjCoordSys.getCoordUnit());
				this.textAreaCoordInfo.setSelectedItem(PrjCoordSysUtilities.getDescription(prjCoordSys));
			}
		} else {
			this.coordSysName.setSelectedItem("");
			this.coordUnit.setSelectedItem("");
			this.textAreaCoordInfo.setSelectedItem("");
		}
	}


	public ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialogPrjCoordSysSettings dialogPrjCoordSysSettings = new JDialogPrjCoordSysSettings();
			if (dialogPrjCoordSysSettings.showDialog() == DialogResult.OK) {
				prj = dialogPrjCoordSysSettings.getPrjCoordSys();
				coordSysName.setSelectedItem(prj.getName());
				coordUnit.setSelectedItem(prj.getCoordUnit());
				textAreaCoordInfo.setSelectedItem(PrjCoordSysUtilities.getDescription(prj));
			}
		}
	};

	public PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			PrjCoordSys prjCoordSys1 = null;
			if (parameterSingleDataset.getSelectedItem() != null && evt.getNewValue() instanceof Dataset) {
				prjCoordSys1 = ((Dataset) evt.getNewValue()).getPrjCoordSys();
				coordSysName.setSelectedItem(prjCoordSys1.getName());
				coordUnit.setSelectedItem(prjCoordSys1.getCoordUnit());
				textAreaCoordInfo.setSelectedItem(PrjCoordSysUtilities.getDescription(prjCoordSys1));
			}
		}
	};


	private void registerEvents() {
		this.parameterSingleDataset.addPropertyListener(this.propertyChangeListener);
		this.parameterButton.setActionListener(this.actionListener);
	}


	@Override
	public IParameterPanel getComponent() {
		return this.parameters.getPanel();
	}

	@Override
	public IParameters getParameters() {
		return this.parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.SET_PROJECTION;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_setProjection");
	}


	@Override
	public boolean execute() {
		boolean ret = true;

		Dataset src;
		if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof Dataset) {
			src = (Dataset) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
		} else {
			src = this.parameterSingleDataset.getSelectedItem();
		}
		try {
			fireRunning(new RunningEvent(this, 0, "Start set geoCoorSys"));
			if (this.prj != null) {
				src.setPrjCoordSys(this.prj);
				String prjCoorSysInfo = PrjCoordSysUtilities.getDescription(this.prj);
				this.textAreaCoordInfo.setSelectedItem(prjCoorSysInfo);
				this.parameters.getOutputs().getData(OUTPUT_DATA).setValue(src);
				PropertyManagerUtilities.refreshPropertyManager();
			} else {
				// 没有主动选择，则视为维持原投影不变
				this.parameters.getOutputs().getData(OUTPUT_DATA).setValue(src);
			}
		} catch (Exception e) {
			ret = false;
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public boolean isChangeSourceData() {
		return true;
	}

}