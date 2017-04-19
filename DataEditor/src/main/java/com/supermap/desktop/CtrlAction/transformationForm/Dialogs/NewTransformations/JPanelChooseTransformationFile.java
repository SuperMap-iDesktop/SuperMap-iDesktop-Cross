package com.supermap.desktop.CtrlAction.transformationForm.Dialogs.NewTransformations;

import com.supermap.data.TransformationMode;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationUtilties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.TransformationModeUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author XiaJT
 */
public class JPanelChooseTransformationFile extends JPanelNewTransformationBase {

	private JPanel panelCenter;
	private JLabel labelTransformationFile;
	private FileChooserControl fileChooserControl;
	private JLabel labelTransformationMode;
	private JComboBox<TransformationMode> comboBox;

	private JLabel labelSelectedColor;
	private ComponentDropDown buttonSelectedColor;
	private JLabel labelUnSelectedColor;
	private ComponentDropDown buttonUnSelectedColor;
	private JLabel labelUnUseColor;
	private ComponentDropDown buttonUnUseColor;

	@Override
	protected String getPanelTitle() {
		return DataEditorProperties.getString("String_ChooseTransformationFile");
	}

	@Override
	protected String getDescribeText() {
		return DataEditorProperties.getString("String_ChooseTransformationFileDescribe");
	}

	@Override
	protected JPanel getCenterPanel() {
		if (panelCenter == null) {
			init();
		}
		return panelCenter;
	}

	private void init() {
		initComponents();
		initLayouts();
		initListeners();
		initComponentStates();
		setComponentName();
	}

	private void initComponents() {
		panelCenter = new JPanel();
		labelTransformationFile = new JLabel();
		fileChooserControl = new FileChooserControl();
		labelTransformationMode = new JLabel();
		labelTransformationFile.setText(DataEditorProperties.getString("String_Transformation_LabelConfigFile"));
		labelTransformationMode.setText(DataEditorProperties.getString("String_TransformationMode"));
		comboBox = new JComboBox<>(new TransformationMode[]{
				TransformationMode.OFFSET,
				TransformationMode.RECT,
				TransformationMode.LINEAR,
				TransformationMode.SQUARE
		});
		comboBox.setRenderer(new ListCellRenderer<TransformationMode>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends TransformationMode> list, TransformationMode value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel result = new JLabel();
				result.setText(TransformationModeUtilities.toString(value));
				result.setOpaque(true);
				result.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
				return result;
			}
		});
		labelSelectedColor = new JLabel();
		buttonSelectedColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
		buttonSelectedColor.setColor(Color.blue);
		labelUnSelectedColor = new JLabel();
		buttonUnSelectedColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
		buttonUnSelectedColor.setColor(Color.red);
		labelUnUseColor = new JLabel();
		buttonUnUseColor = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
		buttonUnUseColor.setColor(Color.gray);
		labelSelectedColor.setText(DataEditorProperties.getString("String_SelectedColor"));
		labelUnSelectedColor.setText(DataEditorProperties.getString("String_UnSelectedColor"));
		labelUnUseColor.setText(DataEditorProperties.getString("String_UnUseColor"));
	}
	private void setComponentName() {
		ComponentUIUtilities.setName(this.panelCenter, "JPanelChooseTransformationFile_panelCenter");
		ComponentUIUtilities.setName(this.labelTransformationFile, "JPanelChooseTransformationFile_labelTransformationFile");
		ComponentUIUtilities.setName(this.fileChooserControl, "JPanelChooseTransformationFile_fileChooserControl");
		ComponentUIUtilities.setName(this.labelTransformationMode, "JPanelChooseTransformationFile_labelTransformationMode");
		ComponentUIUtilities.setName(this.comboBox, "JPanelChooseTransformationFile_comboBox");
		ComponentUIUtilities.setName(this.labelSelectedColor, "JPanelChooseTransformationFile_labelSelectedColor");
		ComponentUIUtilities.setName(this.buttonSelectedColor, "JPanelChooseTransformationFile_buttonSelectedColor");
		ComponentUIUtilities.setName(this.labelUnSelectedColor, "JPanelChooseTransformationFile_labelUnSelectedColor");
		ComponentUIUtilities.setName(this.buttonUnSelectedColor, "JPanelChooseTransformationFile_buttonUnSelectedColor");
		ComponentUIUtilities.setName(this.labelUnUseColor, "JPanelChooseTransformationFile_labelUnUseColor");
		ComponentUIUtilities.setName(this.buttonUnUseColor, "JPanelChooseTransformationFile_buttonUnUseColor");
	}
	private void initLayouts() {
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.add(labelTransformationFile, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setInsets(40, 10, 0, 0));
		panelCenter.add(fileChooserControl, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(40, 5, 0, 10));

		panelCenter.add(labelTransformationMode, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelCenter.add(comboBox, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelCenter.add(labelSelectedColor, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelCenter.add(buttonSelectedColor, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelCenter.add(labelUnSelectedColor, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelCenter.add(buttonUnSelectedColor, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelCenter.add(labelUnUseColor, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelCenter.add(buttonUnUseColor, new GridBagConstraintsHelper(1, 4, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelCenter.add(new JPanel(), new GridBagConstraintsHelper(0, 5, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));

	}

	private void initListeners() {
		fileChooserControl.getButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String moduleName = "transformOpen";
				if (!SmFileChoose.isModuleExist(moduleName)) {
					String fileFilters = SmFileChoose.createFileFilter(DataEditorProperties.getString("String_TransformationFileFilter"), "drfu");
					SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"), CommonProperties.getString(CommonProperties.open)
							, moduleName, "OpenOne");
				}
				SmFileChoose fileChoose = new SmFileChoose(moduleName);
				if (fileChoose.showDefaultDialog() == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChoose.getSelectedFile();
					Document document = XmlUtilities.getDocument(selectedFile.getAbsolutePath());
					TransformationMode transformationMode = TransformationUtilties.getTransformationMode(document);
					if (transformationMode == null) {
						UICommonToolkit.showErrorMessageDialog(DataEditorProperties.getString("String_Transformation_FromXMLFailed"));
					} else {
						fileChooserControl.setText(selectedFile.getPath());
						comboBox.setSelectedItem(transformationMode);
					}
				}
			}
		});
	}

	private void initComponentStates() {
		comboBox.setSelectedItem(TransformationMode.LINEAR);
		fileChooserControl.getEditor().setEditable(false);
	}

	@Override
	protected void setButtonNext(SmButton buttonNext) {

	}

	public TransformationMode getTransformationMode() {
		return (TransformationMode) comboBox.getSelectedItem();
	}

	public boolean isSelectedTransformationFile() {
		return !StringUtilities.isNullOrEmpty(fileChooserControl.getEditor().getText());
	}

	public String getSelectedTransformationFilePath() {
		return fileChooserControl.getEditor().getText();
	}

	public Color getSelectedColor() {
		return buttonSelectedColor.getColor();
	}

	public Color getUnSelectedColor() {
		return buttonUnSelectedColor.getColor();
	}

	public Color getUnUseColor() {
		return buttonUnUseColor.getColor();
	}
}
