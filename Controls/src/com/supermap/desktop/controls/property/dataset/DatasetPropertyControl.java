package com.supermap.desktop.controls.property.dataset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalBorders;

import com.supermap.data.Dataset;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilties.DatasetTypeUtilties;
import com.supermap.desktop.utilties.EncodeTypeUtilties;
import com.supermap.desktop.utilties.StringUtilties;

public class DatasetPropertyControl extends AbstractPropertyControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_LABEL_WIDTH = 100;

	private JLabel labelName;
	private JTextField textFieldName;
	private JLabel labelDatasetType;
	private JTextField textFieldDatasetType;
	private JLabel labelRecordsetName;
	private JTextField textFieldRecordsetName;
	private JLabel labelEncoding;
	private JTextField textFieldEncoding;

	private JLabel labelLeft;
	private JTextField textFieldLeft;
	private JLabel labelTop;
	private JTextField textFieldTop;
	private JLabel labelRight;
	private JTextField textFieldRight;
	private JLabel labelBottom;
	private JTextField textFieldBottom;

	private JLabel labelDescription;
	private JTextArea textAreaDescription;

	private JButton buttonReset;
	private JButton buttonApply;

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
		this.labelName = new JLabel("DatasetName:");
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
		GroupLayout gl_panelBase = new GroupLayout(panelBase);
		gl_panelBase.setAutoCreateContainerGaps(true);
		gl_panelBase.setAutoCreateGaps(true);
		panelBase.setLayout(gl_panelBase);
		// @formatter:off
		gl_panelBase.setHorizontalGroup(gl_panelBase.createSequentialGroup()
				.addGroup(gl_panelBase.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelName, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelDatasetType, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelRecordsetName, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
						.addComponent(this.labelEncoding, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH))
				.addGroup(gl_panelBase.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldDatasetType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldRecordsetName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldEncoding, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		gl_panelBase.setVerticalGroup(gl_panelBase.createSequentialGroup()
				.addGroup(gl_panelBase.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelName)
						.addComponent(this.textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelBase.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDatasetType)
						.addComponent(this.textFieldDatasetType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelBase.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelRecordsetName)
						.addComponent(this.textFieldRecordsetName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelBase.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelEncoding)
						.addComponent(this.textFieldEncoding, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on

		this.labelLeft = new JLabel("Left:");
		this.textFieldLeft = new JTextField();
		this.textFieldLeft.setEditable(false);
		this.labelTop = new JLabel("Top:");
		this.textFieldTop = new JTextField();
		this.textFieldTop.setEditable(false);
		this.labelRight = new JLabel("Right");
		this.textFieldRight = new JTextField();
		this.textFieldRight.setEditable(false);
		this.labelBottom = new JLabel("Bottom:");
		this.textFieldBottom = new JTextField();
		this.textFieldBottom.setEditable(false);

		JPanel panelBounds = new JPanel();
		panelBounds.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_DatasetBounds")));
		GroupLayout gl_panelBounds = new GroupLayout(panelBounds);
		gl_panelBounds.setAutoCreateContainerGaps(true);
		gl_panelBounds.setAutoCreateGaps(true);
		panelBounds.setLayout(gl_panelBounds);
		// @formatter:off
		gl_panelBounds.setHorizontalGroup(gl_panelBounds.createSequentialGroup()
				.addGroup(gl_panelBounds.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelLeft, DEFAULT_LABEL_WIDTH/2, DEFAULT_LABEL_WIDTH/2, DEFAULT_LABEL_WIDTH/2)
						.addComponent(this.labelTop, DEFAULT_LABEL_WIDTH/2, DEFAULT_LABEL_WIDTH/2, DEFAULT_LABEL_WIDTH/2)
						.addComponent(this.labelRight, DEFAULT_LABEL_WIDTH/2, DEFAULT_LABEL_WIDTH/2, DEFAULT_LABEL_WIDTH/2)
						.addComponent(this.labelBottom, DEFAULT_LABEL_WIDTH/2, DEFAULT_LABEL_WIDTH/2, DEFAULT_LABEL_WIDTH/2))
				.addGroup(gl_panelBounds.createParallelGroup(Alignment.LEADING)
						.addComponent(this.textFieldLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldTop, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldRight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		gl_panelBounds.setVerticalGroup(gl_panelBounds.createSequentialGroup()
				.addGroup(gl_panelBounds.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelLeft)
						.addComponent(this.textFieldLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelBounds.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelTop)
						.addComponent(this.textFieldTop, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelBounds.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelRight)
						.addComponent(this.textFieldRight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelBounds.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelBottom)
						.addComponent(this.textFieldBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on

		this.labelDescription = new JLabel("Description:");
		this.textAreaDescription = new JTextArea();
		this.textAreaDescription.setBorder(MetalBorders.getTextFieldBorder());

		JPanel panelDescription = new JPanel();
		panelDescription.setBorder(BorderFactory.createTitledBorder(""));
		GroupLayout gl_panelDescription = new GroupLayout(panelDescription);
		gl_panelDescription.setAutoCreateContainerGaps(true);
		gl_panelDescription.setAutoCreateGaps(true);
		panelDescription.setLayout(gl_panelDescription);
		// @formatter:off
		gl_panelDescription.setHorizontalGroup(gl_panelDescription.createSequentialGroup()
				.addComponent(this.labelDescription, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH)
				.addComponent(this.textAreaDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		
		gl_panelDescription.setVerticalGroup(gl_panelDescription.createParallelGroup(Alignment.LEADING)
				.addComponent(this.labelDescription)
				.addComponent(this.textAreaDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		// @formatter:on

		this.buttonReset = new JButton("Reset");
		this.buttonApply = new JButton("Apply");

		JPanel panelButtons = new JPanel();
		GroupLayout gl_panelButtons = new GroupLayout(panelButtons);
		gl_panelButtons.setAutoCreateGaps(true);
		panelButtons.setLayout(gl_panelButtons);
		// @formatter:off
		gl_panelButtons.setHorizontalGroup(gl_panelButtons.createSequentialGroup()
				.addGap(10, 10, Short.MAX_VALUE)
				.addComponent(this.buttonReset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.buttonApply, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap());
		
		gl_panelButtons.setVerticalGroup(gl_panelButtons.createParallelGroup(Alignment.CENTER)
				.addComponent(this.buttonReset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.buttonApply, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		// @formatter:on

		GroupLayout gl_mainContent = new GroupLayout(this);
		gl_mainContent.setAutoCreateContainerGaps(true);
		gl_mainContent.setAutoCreateGaps(true);
		setLayout(gl_mainContent);
		// @formatter:off
		gl_mainContent.setHorizontalGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainContent.createSequentialGroup()
						.addComponent(panelBase, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
						.addComponent(panelBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
				.addComponent(panelDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		
		gl_mainContent.setVerticalGroup(gl_mainContent.createSequentialGroup()
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(panelBase, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(panelDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		// @formatter:on
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
		this.textFieldDatasetType.setText(DatasetTypeUtilties.toString(this.dataset.getType()));
		this.textFieldRecordsetName.setText(this.dataset.getTableName());
		this.textFieldEncoding.setText(EncodeTypeUtilties.toString(this.dataset.getEncodeType()));
		this.textFieldLeft.setText(BigDecimal.valueOf(this.dataset.getBounds().getLeft()).toString());
		this.textFieldTop.setText(BigDecimal.valueOf(this.dataset.getBounds().getTop()).toString());
		this.textFieldRight.setText(BigDecimal.valueOf(this.dataset.getBounds().getRight()).toString());
		this.textFieldBottom.setText(BigDecimal.valueOf(this.dataset.getBounds().getBottom()).toString());
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
		if (StringUtilties.isNullOrEmpty(this.description) && StringUtilties.isNullOrEmpty(this.dataset.getDescription())) {
			return false;
		}

		if (!StringUtilties.isNullOrEmpty(this.description) && StringUtilties.isNullOrEmpty(this.dataset.getDescription())) {
			return true;
		}

		if (StringUtilties.isNullOrEmpty(this.description) && !StringUtilties.isNullOrEmpty(this.dataset.getDescription())) {
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
