package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.Dataset;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.EncodeTypeUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DatasetPropertyControl extends AbstractPropertyControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel labelName;
	private JTextField textFieldName;
	private JLabel labelDatasetType;
	private JTextField textFieldDatasetType;
	private JLabel labelRecordsetName;
	private JTextField textFieldRecordsetName;
	private JLabel labelEncoding;
	private JTextField textFieldEncoding;

	private JLabel labelLeft;
	private SMFormattedTextField textFieldLeft;
	private JLabel labelTop;
	private SMFormattedTextField textFieldTop;
	private JLabel labelRight;
	private SMFormattedTextField textFieldRight;
	private JLabel labelBottom;
	private SMFormattedTextField textFieldBottom;

	private JLabel labelDescription;
	private JTextArea textAreaDescription;

	private SmButton buttonReset;
	private SmButton buttonApply;

	private Dataset dataset;
	private String description = "";

	private DocumentListener textAreaDescriptionDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textAreaDocumentChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textAreaDocumentChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			throw new UnsupportedOperationException();
		}
	};
	private ActionListener buttonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonReset) {
				buttonResetClicked();
			} else if (e.getSource() == buttonApply) {
				buttonApplyClicked();
			}
		}
	};

	public DatasetPropertyControl(Dataset dataset) {
		super(ControlsProperties.getString("String_DatasetProperty"));
		initializeComponents();
		initializeResources();
		setDataset(dataset);
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
		unregisterEvents();
		reset();
		fillComponents();
		setButtonsEnabled();
		registerEvents();
	}

	@Override
	public void refreshData() {
		setDataset(this.dataset);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.DATASET;
	}

	private void initializeComponents() {
		Dimension labelSize = new Dimension(40, 23);
		this.labelName = new JLabel("DatasetName:");
		labelName.setPreferredSize(labelSize);
		this.textFieldName = new JTextField();
		this.textFieldName.setEditable(false);
		this.labelDatasetType = new JLabel("DatasetType:");
		this.textFieldDatasetType = new JTextField();
		this.textFieldDatasetType.setEditable(false);
		this.labelRecordsetName = new JLabel("RecordsetName:");
		this.textFieldRecordsetName = new JTextField();
		this.textFieldRecordsetName.setEditable(false);
		this.labelEncoding = new JLabel("Encoding:");
		this.textFieldEncoding = new JTextField();
		this.textFieldEncoding.setEditable(false);

		JPanel panelBase = new JPanel();
		panelBase.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_BasicInfo")));
		panelBase.setLayout(new GridBagLayout());
		panelBase.add(labelName, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setAnchor(GridBagConstraints.WEST));
		panelBase.add(textFieldName, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));

		panelBase.add(labelDatasetType, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0).setAnchor(GridBagConstraints.WEST));
		panelBase.add(textFieldDatasetType, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelBase.add(labelRecordsetName, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0).setAnchor(GridBagConstraints.WEST));
		panelBase.add(textFieldRecordsetName, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		panelBase.add(labelEncoding, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0).setAnchor(GridBagConstraints.WEST));
		panelBase.add(textFieldEncoding, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10));


		this.labelLeft = new JLabel("Left:");
		labelLeft.setPreferredSize(labelSize);
		this.textFieldLeft = new SMFormattedTextField();
		this.textFieldLeft.setEditable(false);
		this.labelTop = new JLabel("Top:");
		this.textFieldTop = new SMFormattedTextField();
		this.textFieldTop.setEditable(false);
		this.labelRight = new JLabel("Right");
		this.textFieldRight = new SMFormattedTextField();
		this.textFieldRight.setEditable(false);
		this.labelBottom = new JLabel("Bottom:");
		this.textFieldBottom = new SMFormattedTextField();
		this.textFieldBottom.setEditable(false);

		JPanel panelBounds = new JPanel();
		panelBounds.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_DatasetBounds")));
		panelBounds.setLayout(new GridBagLayout());
		panelBounds.add(labelLeft, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setAnchor(GridBagConstraints.WEST));
		panelBounds.add(textFieldLeft, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setAnchor(GridBagConstraints.CENTER));

		panelBounds.add(labelTop, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0).setAnchor(GridBagConstraints.WEST));
		panelBounds.add(textFieldTop, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10).setAnchor(GridBagConstraints.CENTER));

		panelBounds.add(labelRight, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0).setAnchor(GridBagConstraints.WEST));
		panelBounds.add(textFieldRight, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10).setAnchor(GridBagConstraints.CENTER));

		panelBounds.add(labelBottom, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 10, 0).setAnchor(GridBagConstraints.WEST));
		panelBounds.add(textFieldBottom, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10).setAnchor(GridBagConstraints.CENTER));

		this.labelDescription = new JLabel("Description:");
		this.textAreaDescription = new JTextArea();
		this.textAreaDescription.setBorder(MetalBorders.getTextFieldBorder());

		JPanel panelDescription = new JPanel();
		panelDescription.setBorder(BorderFactory.createTitledBorder(""));
		panelDescription.setLayout(new GridBagLayout());
		panelDescription.add(labelDescription, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 10));
		panelDescription.add(textAreaDescription, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 10, 10));

		this.buttonReset = new SmButton("Reset");
		this.buttonApply = new SmButton("Apply");

		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(buttonReset, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 10, 0));
		panelButtons.add(buttonApply, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 10, 10));

		JScrollPane jScrollPane = new JScrollPane();
		JPanel panelMain = new JPanel();
		panelMain.setLayout(new GridBagLayout());
		panelMain.add(panelBase, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 10));
		panelMain.add(panelBounds, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));
		panelMain.add(panelDescription, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
		panelMain.add(panelButtons, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));

		jScrollPane.setViewportView(panelMain);
		this.setLayout(new GridBagLayout());
		this.add(jScrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));

	}

	private void initializeResources() {
		this.labelName.setText(ControlsProperties.getString("String_LabelDatasetName"));
		this.labelName.setToolTipText(this.labelName.getText());
		this.labelDatasetType.setText(ControlsProperties.getString("String_LabelDatasetType"));
		this.labelDatasetType.setToolTipText(this.labelDatasetType.getText());
		this.labelRecordsetName.setText(ControlsProperties.getString("String_LabelTableName"));
		this.labelRecordsetName.setToolTipText(this.labelRecordsetName.getText());
		this.labelEncoding.setText(ControlsProperties.getString("String_LabelEncodeType"));
		this.labelEncoding.setToolTipText(this.labelEncoding.getText());
		this.labelLeft.setText(ControlsProperties.getString(ControlsProperties.Label_Left));
		this.labelTop.setText(ControlsProperties.getString(ControlsProperties.Label_Top));
		this.labelRight.setText(ControlsProperties.getString(ControlsProperties.Label_Right));
		this.labelBottom.setText(ControlsProperties.getString(ControlsProperties.Label_Bottom));
		this.labelDescription.setText(ControlsProperties.getString("String_LabelDescription"));
		this.labelDescription.setToolTipText(this.labelDescription.getText());
		this.buttonReset.setText(CommonProperties.getString(CommonProperties.Reset));
		this.buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
	}

	private void registerEvents() {
		this.textAreaDescription.getDocument().addDocumentListener(this.textAreaDescriptionDocumentListener);
		this.buttonReset.addActionListener(this.buttonActionListener);
		this.buttonApply.addActionListener(this.buttonActionListener);
	}

	private void unregisterEvents() {
		this.textAreaDescription.getDocument().removeDocumentListener(this.textAreaDescriptionDocumentListener);
		this.buttonReset.removeActionListener(this.buttonActionListener);
		this.buttonApply.removeActionListener(this.buttonActionListener);
	}

	private void reset() {
		this.description = this.dataset.getDescription();
	}

	private void fillComponents() {
		this.textFieldName.setText(this.dataset.getName());
		this.textFieldDatasetType.setText(DatasetTypeUtilities.toString(this.dataset.getType()));
		this.textFieldRecordsetName.setText(this.dataset.getTableName());
		this.textFieldEncoding.setText(EncodeTypeUtilities.toString(this.dataset.getEncodeType()));
		this.textFieldLeft.setText(DoubleUtilities.getFormatString(this.dataset.getBounds().getLeft()));
		this.textFieldTop.setText(DoubleUtilities.getFormatString(this.dataset.getBounds().getTop()));
		this.textFieldRight.setText(DoubleUtilities.getFormatString(this.dataset.getBounds().getRight()));
		this.textFieldBottom.setText(DoubleUtilities.getFormatString(this.dataset.getBounds().getBottom()));
		this.textAreaDescription.setText(this.description);
	}

	private void setButtonsEnabled() {
		boolean isDatasetReadOnly = this.dataset.isReadOnly();
		this.textAreaDescription.setEditable(!isDatasetReadOnly);
		boolean enabled = verifyChange();
		this.buttonApply.setEnabled(enabled);
		this.buttonReset.setEnabled(enabled);
	}

	private boolean verifyChange() {
		if (StringUtilities.isNullOrEmpty(this.description) && StringUtilities.isNullOrEmpty(this.dataset.getDescription())) {
			return false;
		}

		if (!StringUtilities.isNullOrEmpty(this.description) && StringUtilities.isNullOrEmpty(this.dataset.getDescription())) {
			return true;
		}

		if (StringUtilities.isNullOrEmpty(this.description) && !StringUtilities.isNullOrEmpty(this.dataset.getDescription())) {
			return true;
		}

		return !this.description.equals(this.dataset.getDescription());
	}

	private void textAreaDocumentChange() {
		this.description = this.textAreaDescription.getText();
		setButtonsEnabled();
	}

	private void buttonResetClicked() {
		reset();
		fillComponents();
		setButtonsEnabled();
	}

	private void buttonApplyClicked() {
		this.dataset.setDescription(this.description);
		setButtonsEnabled();
	}
}
