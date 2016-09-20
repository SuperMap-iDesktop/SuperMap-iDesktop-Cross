package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.data.Point2Ds;
import com.supermap.data.Transformation;
import com.supermap.data.TransformationError;
import com.supermap.data.TransformationMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.FormTransformationTableModel;
import com.supermap.desktop.CtrlAction.transformationForm.beans.FormTransformationSubFormType;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author XiaJT
 */
public class CtrlActionCalculateError extends CtrlAction {
	public CtrlActionCalculateError(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IFormTransformation activeForm = (IFormTransformation) Application.getActiveApplication().getActiveForm();
		TransformationMode transformationMode = activeForm.getTransformationMode();
		FormTransformationTableModel model = ((FormTransformationTableModel) activeForm.getTable().getModel());
		if (model.getEnableRowCount() == 1) {
			transformationMode = TransformationMode.OFFSET;
		} else if (model.getEnableRowCount() == 2) {
			transformationMode = TransformationMode.RECT;
		} else if (model.getEnableRowCount() >= 4 && model.getEnableRowCount() < 7) {
			transformationMode = TransformationMode.LINEAR;
		} else if (model.getEnableRowCount() >= 7) {
			if (transformationMode == TransformationMode.OFFSET || transformationMode == TransformationMode.RECT) {
				JDialogChooseMode jDialogChooseMode = new JDialogChooseMode();
				if (jDialogChooseMode.showDialog() == DialogResult.OK) {
					transformationMode = jDialogChooseMode.getTransformationMode();
					activeForm.setTransformationMode(transformationMode);
				} else {
					return;
				}
			}
		}
		Transformation transformation = new Transformation();
		transformation.setTransformMode(transformationMode);
		Point2Ds targetPoint2Ds = new Point2Ds();
		Point2Ds referPoint2Ds = new Point2Ds();
		for (int i = 0; i < model.getEnableRowCount(); i++) {
			targetPoint2Ds.add(model.getOriginalPoint(model.getEnableRow(i)));
			referPoint2Ds.add(model.getReferPoint(model.getEnableRow(i)));
		}
		transformation.setTargetControlPoints(referPoint2Ds);
		transformation.setOriginalControlPoints(targetPoint2Ds);
		TransformationError error = transformation.getError();
		double[] residualX = error.getResidualX();
		double[] residualY = error.getResidualY();
		double[] residualTotle = error.getRMS();
		for (int i = 0; i < model.getEnableRowCount(); i++) {
			model.setResidualX(model.getEnableRow(i), residualX[i]);
			model.setResidualY(model.getEnableRow(i), residualY[i]);
			model.setResidualTotal(model.getEnableRow(i), residualTotle[i]);
		}
		activeForm.setTransformation(transformation);
		error.dispose();
		ToolbarUIUtilities.updataToolbarsState();
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormTransformation) {
			FormTransformationTableModel model = (FormTransformationTableModel) ((IFormTransformation) activeForm).getTable().getModel();
			if (model.getEnableRowCount() <= 0 || model.getEnableRowCount() == 3) {
				return false;
			}
			if (model.getEnablePointCount(FormTransformationSubFormType.Target) != model.getEnablePointCount(FormTransformationSubFormType.Reference)) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	class JDialogChooseMode extends SmDialog {
		JLabel label = new JLabel(DataEditorProperties.getString("String_ChooseTransformationMode"));
		//		JComboBox<TransformationMode> comboBoxTransformationMode = new JComboBox<>(new TransformationMode[]{
//				TransformationMode.LINEAR,
//				TransformationMode.SQUARE
//		});
		ButtonGroup buttonGroup = new ButtonGroup();
		JRadioButton radioButtonLinear = new JRadioButton(DataEditorProperties.getString("String_TransformationModeLINEAR"));
		JRadioButton radioButtonSQUARE = new JRadioButton(DataEditorProperties.getString("String_TransformationModeSQUARE"));

		SmButton buttonOk = new SmButton(CommonProperties.getString(CommonProperties.OK));
		SmButton buttonCancel = new SmButton(CommonProperties.getString(CommonProperties.Cancel));


		JDialogChooseMode() {
			radioButtonLinear.setSelected(true);
			buttonGroup.add(radioButtonLinear);
			buttonGroup.add(radioButtonSQUARE);
			this.setTitle(DataEditorProperties.getString("String_CalculateError"));
			buttonOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.OK;
					dispose();
				}
			});
			buttonCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					dispose();
				}
			});

			this.setLayout(new GridBagLayout());
			this.add(label, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
			this.add(radioButtonLinear, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0.5, 0).setInsets(5, 10, 0, 0));
			this.add(radioButtonSQUARE, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0.5, 0).setInsets(5, 10, 0, 10));
			this.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));

			JPanel jPanel = new JPanel(new GridBagLayout());
			jPanel.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 10, 10, 0));
			jPanel.add(buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(5, 5, 10, 10));

			this.add(jPanel, new GridBagConstraintsHelper(0, 10, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
			this.setLocationRelativeTo(null);
			pack();
		}

		TransformationMode getTransformationMode() {
			return radioButtonLinear.isSelected() ? TransformationMode.LINEAR : TransformationMode.SQUARE;
		}
	}

}
