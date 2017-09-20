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

	private PrjCoordSys prjCoordSys = null;
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
		this.parameterDatasource = new ParameterDatasourceConstrained();
		this.parameterDataset = new ParameterSingleDataset();
		this.parameterDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		// 不支持可读
		this.parameterDatasource.setReadOnlyNeeded(false);

		ParameterCombine parameterCombineSource = new ParameterCombine();
		parameterCombineSource.setDescribe(SOURCE_PANEL_DESCRIPTION);
		parameterCombineSource.addParameters(this.parameterDatasource, this.parameterDataset);

		this.parameterMode.setItems(
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
		parameterCombine.addParameters(this.parameterMode, this.parameterProjection);
		parameterCombine.setWeightIndex(0);
		parameterCombineSetting.addParameters(parameterCombine, this.parameterScaleDifference);

		ParameterCombine parameterCombineRotation = new ParameterCombine();
		parameterCombineRotation.setDescribe(ControlsProperties.getString("String_Rotation"));
		parameterCombineRotation.addParameters(this.parameterTextFieldAngleX, this.parameterTextFieldAngleY, this.parameterTextFieldAngleZ);

		ParameterCombine parameterOffset = new ParameterCombine();
		parameterOffset.setDescribe(ControlsProperties.getString("String_Offset"));
		parameterOffset.addParameters(this.parameterTextFieldOffsetX, this.parameterTextFieldOffsetY, this.parameterTextFieldOffsetZ);

		parameters.setParameters(parameterCombineSource, parameterCombineSetting, parameterCombineRotation, parameterOffset);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.DATASET, parameterCombineSource);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_TransParamsSettingResult"),
				DatasetTypes.DATASET, this.parameterDataset);
	}

	private void initParameterState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset();
		if (defaultDataset != null) {
			this.parameterDatasource.setSelectedItem(defaultDataset.getDatasource());
			this.parameterDataset.setSelectedItem(defaultDataset);
		}
		this.parameterScaleDifference.setEnabled(false);
		this.parameterTextFieldAngleX.setEnabled(false);
		this.parameterTextFieldAngleY.setEnabled(false);
		this.parameterTextFieldAngleZ.setEnabled(false);

		this.parameterScaleDifference.setSelectedItem("0");
		this.parameterTextFieldOffsetX.setSelectedItem("0");
		this.parameterTextFieldOffsetY.setSelectedItem("0");
		this.parameterTextFieldOffsetZ.setSelectedItem("0");
		this.parameterTextFieldAngleX.setSelectedItem("0");
		this.parameterTextFieldAngleY.setSelectedItem("0");
		this.parameterTextFieldAngleZ.setSelectedItem("0");
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.parameterDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.parameterDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

	}

	private void initParameterListeners() {
		this.parameterMode.addPropertyListener(new PropertyChangeListener() {
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
		this.parameterProjection.setActionListener(new ActionListener() {
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
		Dataset src;
		if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() instanceof Dataset) {
			src = (Dataset) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
		} else {
			src = this.parameterDataset.getSelectedItem();
		}
		// 当未设置投影时，给定原数据集投影,防止参数为空报错-yuanR2017.9.6
		if (this.prjCoordSys == null) {
			this.prjCoordSys = src.getPrjCoordSys();
		}
		try {
			fireRunning(new RunningEvent(this, 0, "Start set geoCoorSys"));
			CoordSysTransMethod method = (CoordSysTransMethod) this.parameterMode.getSelectedData();

			CoordSysTransParameter coordSysTransParameter = new CoordSysTransParameter();
			coordSysTransParameter.setScaleDifference(Double.valueOf(this.parameterScaleDifference.getSelectedItem()));
			coordSysTransParameter.setRotateX(Double.valueOf(this.parameterTextFieldAngleX.getSelectedItem()) / 60 / 60 / 180 * Math.PI);
			coordSysTransParameter.setRotateY(Double.valueOf(this.parameterTextFieldAngleY.getSelectedItem()) / 60 / 60 / 180 * Math.PI);
			coordSysTransParameter.setRotateZ(Double.valueOf(this.parameterTextFieldAngleZ.getSelectedItem()) / 60 / 60 / 180 * Math.PI);
			coordSysTransParameter.setTranslateX(Double.valueOf(this.parameterTextFieldOffsetX.getSelectedItem()));
			coordSysTransParameter.setTranslateY(Double.valueOf(this.parameterTextFieldOffsetY.getSelectedItem()));
			coordSysTransParameter.setTranslateZ(Double.valueOf(this.parameterTextFieldOffsetZ.getSelectedItem()));
			isSuccessful = CoordSysTranslator.convert(src, this.prjCoordSys, coordSysTransParameter, method);
			if (isSuccessful) {
				getParameters().getOutputs().getData(OUTPUT_DATA).setValue(src);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
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
