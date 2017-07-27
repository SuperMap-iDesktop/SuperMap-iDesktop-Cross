package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.PrjCoordSys;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.PrjCoordSysUtilities;

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


	public MetaProcessSetProjection() {
		initParameters();
		initComponentState();
		registerEvents();
	}

	private void initParameters() {
		datasource = new ParameterDatasourceConstrained();
		parameterSingleDataset = new ParameterSingleDataset();
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset();
		if (defaultDataset != null) {
			parameterSingleDataset.setSelectedItem(defaultDataset);
			datasource.setSelectedItem(defaultDataset.getDatasource());
		}
		datasource.setDescribe(CommonProperties.getString("String_Label_Datasource"));
		parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(datasource, parameterSingleDataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		coordSysName = new ParameterTextField(ControlsProperties.getString("String_Message_CoordSysName"));
		coordSysName.setEnabled(false);
		coordUnit = new ParameterTextField(ControlsProperties.getString("String_ProjectionInfoControl_LabelGeographyUnit"));
		coordUnit.setEnabled(false);
		textAreaCoordInfo = new ParameterTextArea();
		textAreaCoordInfo.setEnabled(false);
		parameterButton = new ParameterButton(ControlsProperties.getString("String_ProjectionInfoControl_ButtonResetProjectionInfo"));
		parameterCombineCoordInfo = new ParameterCombine();
		parameterCombineCoordInfo.setDescribe(ControlsProperties.getString("String_ProjectionInfoControl_LabelProjectionInfo"));
		parameterCombineCoordInfo.addParameters(coordSysName, coordUnit, textAreaCoordInfo, new ParameterCombine(ParameterCombine.HORIZONTAL).addParameters(parameterButton));
		this.parameters.setParameters(parameterCombineSourceData, parameterCombineCoordInfo);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.DATASET, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.DATASET, parameterSingleDataset);
		//Add Constraint
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(parameterSingleDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

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
				coordSysName.setSelectedItem(prjCoordSys.getName());
				coordUnit.setSelectedItem(prjCoordSys.getCoordUnit());
				textAreaCoordInfo.setSelectedItem(PrjCoordSysUtilities.getDescription(prjCoordSys));
			}
		} else {
			coordSysName.setSelectedItem("");
			coordUnit.setSelectedItem("");
			textAreaCoordInfo.setSelectedItem("");
		}
	}


	public ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialogPrjCoordSysSettings dialogPrjCoordSysSettings = new JDialogPrjCoordSysSettings();
			if (dialogPrjCoordSysSettings.showDialog() == DialogResult.OK) {
				prj = dialogPrjCoordSysSettings.getPrjCoordSys();
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
		return parameters.getPanel();
	}

	@Override
	public IParameters getParameters() {
		return parameters;
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

		try {
			Dataset src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof Dataset) {
				src = (Dataset) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (Dataset) this.parameterSingleDataset.getSelectedItem();
			}
			fireRunning(new RunningEvent(this, 0, "Start set geoCoorSys"));
			if (prj != null) {
				src.setPrjCoordSys(prj);
				String prjCoorSysInfo = PrjCoordSysUtilities.getDescription(prj);
				textAreaCoordInfo.setSelectedItem(prjCoorSysInfo);
				fireRunning(new RunningEvent(this, 100, "重设投影坐标系成功。"));
				this.parameters.getOutputs().getData(OUTPUT_DATA).setValue(src);
			}
		} catch (Exception e) {
			ret = false;
		}

		return ret;
	}
}