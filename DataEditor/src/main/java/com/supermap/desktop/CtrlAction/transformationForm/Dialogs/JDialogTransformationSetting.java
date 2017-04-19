package com.supermap.desktop.CtrlAction.transformationForm.Dialogs;

import com.supermap.data.TransformationMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.TransformationModeUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by SillyB on 2016/9/17.
 */
public class JDialogTransformationSetting extends SmDialog {
	private JLabel labelSelectedColor = new JLabel();
	private ComponentDropDown buttonSelectedColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
	private JLabel labelUnSelectedColor = new JLabel();
	private ComponentDropDown buttonUnSelectedColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
	private JLabel labelUnUseColor = new JLabel();
	private ComponentDropDown buttonUnUseColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);

	private JLabel labelTransformationMode = new JLabel();

	private JComboBox<TransformationMode> comboBoxTransformationMode = new JComboBox<>(new TransformationMode[]{
			TransformationMode.OFFSET,
			TransformationMode.RECT,
			TransformationMode.LINEAR,
			TransformationMode.SQUARE
	});
	private SmButton buttonOK = new SmButton();
	private SmButton buttonCancel = new SmButton();

	public JDialogTransformationSetting() {
		setTitle(DataEditorProperties.getString("String_TransformationSetting"));
		init();
		pack();
		this.setLocationRelativeTo((Component) Application.getActiveApplication().getActiveForm());
	}

	private void init() {
		initComponent();
		setComponentName();
		initListener();
		initResources();
		initLayout();
		initComponentState();
	}
	private void setComponentName() {
		ComponentUIUtilities.setName(this.labelSelectedColor, "JDialogTransformationSetting_labelSelectedColor");
		ComponentUIUtilities.setName(this.buttonSelectedColor, "JDialogTransformationSetting_buttonSelectedColor");
		ComponentUIUtilities.setName(this.labelUnSelectedColor, "JDialogTransformationSetting_labelUnSelectedColor");
		ComponentUIUtilities.setName(this.buttonUnSelectedColor, "JDialogTransformationSetting_buttonUnSelectedColor");
		ComponentUIUtilities.setName(this.labelUnUseColor, "JDialogTransformationSetting_labelUnUseColor");
		ComponentUIUtilities.setName(this.buttonUnUseColor, "JDialogTransformationSetting_buttonUnUseColor");
		ComponentUIUtilities.setName(this.labelTransformationMode, "JDialogTransformationSetting_labelTransformationMode");
		ComponentUIUtilities.setName(this.comboBoxTransformationMode, "JDialogTransformationSetting_comboBoxTransformationMode");
		ComponentUIUtilities.setName(this.buttonOK, "JDialogTransformationSetting_buttonOK");
		ComponentUIUtilities.setName(this.buttonCancel, "JDialogTransformationSetting_buttonCancel");
	}
	private void initComponent() {
		comboBoxTransformationMode.setRenderer(new ListCellRenderer<TransformationMode>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends TransformationMode> list, TransformationMode value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel result = new JLabel();
				result.setText(TransformationModeUtilities.toString(value));
				result.setOpaque(true);
				result.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
				return result;
			}
		});
		comboBoxTransformationMode.setPreferredSize(new Dimension(250, 23));
		buttonSelectedColor.setColor(Color.blue);
		buttonUnSelectedColor.setColor(Color.red);
		buttonUnUseColor.setColor(Color.gray);
	}

	private void initListener() {
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IForm activeForm = Application.getActiveApplication().getActiveForm();
				if (activeForm instanceof IFormTransformation) {
					((IFormTransformation) activeForm).setSelectedColor(buttonSelectedColor.getColor());
					((IFormTransformation) activeForm).setUnSelectedColor(buttonUnSelectedColor.getColor());
					((IFormTransformation) activeForm).setUnUseColor(buttonUnUseColor.getColor());
					((IFormTransformation) activeForm).setTransformationMode((TransformationMode) comboBoxTransformationMode.getSelectedItem());
				}
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
	}

	private void initResources() {
		labelSelectedColor.setText(DataEditorProperties.getString("String_SelectedColor"));
		labelUnSelectedColor.setText(DataEditorProperties.getString("String_UnSelectedColor"));
		labelUnUseColor.setText(DataEditorProperties.getString("String_UnUseColor"));
		labelTransformationMode.setText(DataEditorProperties.getString("String_TransformationMode"));
		buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelSelectedColor, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		this.add(buttonSelectedColor, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		this.add(labelUnSelectedColor, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(buttonUnSelectedColor, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		this.add(labelUnUseColor, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(buttonUnUseColor, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		this.add(labelTransformationMode, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(comboBoxTransformationMode, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		this.add(new JPanel(), new GridBagConstraintsHelper(0, 4, 2, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));

		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST));
		panelButton.add(buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST));

		this.add(panelButton, new GridBagConstraintsHelper(0, 10, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 10, 10));
	}

	private void initComponentState() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormTransformation) {
			buttonSelectedColor.setColor(((IFormTransformation) activeForm).getSelectedColor());
			buttonUnSelectedColor.setColor(((IFormTransformation) activeForm).getUnSelectedColor());
			buttonUnUseColor.setColor(((IFormTransformation) activeForm).getUnUseColor());
			comboBoxTransformationMode.setSelectedItem(((IFormTransformation) activeForm).getTransformationMode());
		}
	}
}
