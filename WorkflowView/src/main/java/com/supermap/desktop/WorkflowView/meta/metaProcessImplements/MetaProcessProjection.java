package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoordSysTransMethodProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class MetaProcessProjection extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "ProjectionResult";

	private PrjCoordSys prjCoordSys;
	private ParameterDatasourceConstrained parameterDatasource;
	private ParameterSingleDataset parameterDataset;

	private ParameterButton parameterProjection = new ParameterButton(ProcessProperties.getString("String_chooseProjection"));

	private ParameterComboBox parameterMode = new ParameterComboBox(ControlsProperties.getString("String_TransMethod"));
	private ParameterNumber parameterScaleDifference = new ParameterNumber(ControlsProperties.getString("String_ScaleDifference"));
	private ParameterNumber parameterTextFieldAngleX = new ParameterNumber("X:");
	private ParameterNumber parameterTextFieldAngleY = new ParameterNumber("Y:");
	private ParameterNumber parameterTextFieldAngleZ = new ParameterNumber("Z:");

	private ParameterNumber parameterTextFieldOffsetX = new ParameterNumber("X:");
	private ParameterNumber parameterTextFieldOffsetY = new ParameterNumber("Y:");
	private ParameterNumber parameterTextFieldOffsetZ = new ParameterNumber("Z:");


	public MetaProcessProjection() {
		initParameters();
		initParameterState();
		initParameterConstraint();
		initParameterListeners();
	}

	private void initParameters() {
		parameterDatasource = new ParameterDatasourceConstrained();
		parameterDataset = new ParameterSingleDataset();
		parameterDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));


		ParameterCombine parameterCombineSource = new ParameterCombine();
		parameterCombineSource.setDescribe(SOURCE_PANEL_DESCRIPTION);
		parameterCombineSource.addParameters(parameterDatasource, parameterDataset);

		parameterMode.setItems(
				new ParameterDataNode(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.GeocentricTranslation), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION),
				new ParameterDataNode(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.Molodensky), CoordSysTransMethod.MTH_MOLODENSKY),
				new ParameterDataNode(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.MolodenskyAbridged), CoordSysTransMethod.MTH_MOLODENSKY_ABRIDGED),
				new ParameterDataNode(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.PositionVector), CoordSysTransMethod.MTH_POSITION_VECTOR),
				new ParameterDataNode(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.CoordinateFrame), CoordSysTransMethod.MTH_COORDINATE_FRAME),
				new ParameterDataNode(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.BursaWolf), CoordSysTransMethod.MTH_BURSA_WOLF)
		);
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(SETTING_PANEL_DESCRIPTION);
		ParameterCombine parameterCombine = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombine.addParameters(parameterMode, parameterProjection);
		parameterCombine.setWeightIndex(0);
		parameterCombineSetting.addParameters(parameterCombine, parameterScaleDifference);

		ParameterCombine parameterCombineRotation = new ParameterCombine();
		parameterCombineRotation.setDescribe(ControlsProperties.getString("String_Rotation"));
		parameterCombineRotation.addParameters(parameterTextFieldAngleX, parameterTextFieldAngleY, parameterTextFieldAngleZ);

		ParameterCombine parameterOffset = new ParameterCombine();
		parameterOffset.setDescribe(ControlsProperties.getString("String_Offset"));
		parameterOffset.addParameters(parameterTextFieldOffsetX, parameterTextFieldOffsetY, parameterTextFieldOffsetZ);

		parameters.setParameters(parameterCombineSource, parameterCombineSetting, parameterCombineRotation, parameterOffset);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.DATASET, parameterCombineSource);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_TransParamsSettingResult"),
				DatasetTypes.DATASET, parameterDataset);
	}

	private void initParameterState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset();
		if (defaultDataset != null) {
			parameterDatasource.setSelectedItem(defaultDataset.getDatasource());
			parameterDataset.setSelectedItem(defaultDataset);
		}
		parameterScaleDifference.setEnabled(false);
		parameterTextFieldAngleX.setEnabled(false);
		parameterTextFieldAngleY.setEnabled(false);
		parameterTextFieldAngleZ.setEnabled(false);

		parameterScaleDifference.setSelectedItem("0");
		parameterTextFieldOffsetX.setSelectedItem("0");
		parameterTextFieldOffsetY.setSelectedItem("0");
		parameterTextFieldOffsetZ.setSelectedItem("0");
		parameterTextFieldAngleX.setSelectedItem("0");
		parameterTextFieldAngleY.setSelectedItem("0");
		parameterTextFieldAngleZ.setSelectedItem("0");
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(parameterDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(parameterDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

	}

	private void initParameterListeners() {
		parameterMode.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					boolean isEnable = parameterMode.getSelectedIndex() > 2;
					parameterScaleDifference.setEnabled(isEnable);
					parameterTextFieldAngleX.setEnabled(isEnable);
					parameterTextFieldAngleY.setEnabled(isEnable);
					parameterTextFieldAngleZ.setEnabled(isEnable);
				}
			}
		});
		parameterProjection.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogPrjCoordSysSettings jDialogPrjCoordSysSettings = new JDialogPrjCoordSysSettings();
				if (jDialogPrjCoordSysSettings.showDialog() == DialogResult.OK) {
					prjCoordSys = jDialogPrjCoordSysSettings.getPrjCoordSys();
				}
			}
		});
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		try {
			Dataset src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof Dataset) {
				src = (Dataset) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (Dataset) this.parameterDataset.getSelectedItem();
			}
			fireRunning(new RunningEvent(this, 0, "Start set geoCoorSys"));

			CoordSysTransMethod method = (CoordSysTransMethod) parameterMode.getSelectedData();

			CoordSysTransParameter coordSysTransParameter = new CoordSysTransParameter();
			coordSysTransParameter.setScaleDifference(Double.valueOf((String) parameterScaleDifference.getSelectedItem()));
			coordSysTransParameter.setRotateX(Double.valueOf(this.parameterTextFieldAngleX.getSelectedItem().toString()) / 60 / 60 / 180 * Math.PI);
			coordSysTransParameter.setRotateY(Double.valueOf(this.parameterTextFieldAngleY.getSelectedItem().toString()) / 60 / 60 / 180 * Math.PI);
			coordSysTransParameter.setRotateZ(Double.valueOf(this.parameterTextFieldAngleZ.getSelectedItem().toString()) / 60 / 60 / 180 * Math.PI);
			coordSysTransParameter.setTranslateX(Double.valueOf((String) parameterTextFieldOffsetX.getSelectedItem()));
			coordSysTransParameter.setTranslateY(Double.valueOf((String) parameterTextFieldOffsetY.getSelectedItem()));
			coordSysTransParameter.setTranslateZ(Double.valueOf((String) parameterTextFieldOffsetZ.getSelectedItem()));
			CoordSysTranslator.convert(src, prjCoordSys, coordSysTransParameter, method);
			fireRunning(new RunningEvent(this, 100, "set geoCoorSys finished"));
			isSuccessful = true;
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(src);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {

		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.PROJECTION;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Projection");
	}

	@Override
	public boolean isChangeSourceData() {
		return true;
	}
}
