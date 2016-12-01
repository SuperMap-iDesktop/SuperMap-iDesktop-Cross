package com.supermap.desktop.controls.property.datasource;

import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IPasswordCheck;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.dialog.JDialogChangePassword;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DatasourcePropertyControl extends AbstractPropertyControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension DEFAULT_BUTTON_PREFERREDSIZE = new Dimension(75, 23);


	private JLabel labelServer;
	private JLabel labelDatasourceType;
	private JLabel labelOpenType;
	private JLabel labelDescription;

	private JTextField textFieldServer;
	private JTextField textFieldDatasourceType;
	private JTextField textFieldOpenType;
	private JTextArea textFieldDescription;

	private SmButton buttonChangePassword;
	private SmButton buttonApply;
	private SmButton buttonReset;

	private String description = "";
	private String newPassword = "";

	private transient Datasource datasource = null;


	private transient DocumentListener textFieldDescriptionDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldDescriptionTextChanged();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldDescriptionTextChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			throw new UnsupportedOperationException();
		}
	};

	private transient ActionListener buttonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonChangePassword) {
				buttonChangePasswordClicked();
			} else if (e.getSource() == buttonApply) {
				buttonApplyClicked();
			} else if (e.getSource() == buttonReset) {
				buttonResetClicked();
			}
		}
	};

	/**
	 * Create the panel.
	 */
	public DatasourcePropertyControl(Datasource datasource) {
		super(ControlsProperties.getString("String_DatasourceProperty"));


		initializeComponents();
		initializeResources();
		setDatasource(datasource);
	}

	public Datasource getDatasource() {
		return this.datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
		this.newPassword = datasource.getConnectionInfo().getPassword();
		unregisterEvents();
		reset();
		fillComponents();
		registerEvents();
		this.buttonChangePassword.setEnabled(isChangePasswordEnabled());
		setButtonApplyEnabledInEDT(checkChange());
		setButtonResetEnabledInEDT(checkChange());
	}

	@Override
	public void refreshData() {
		setDatasource(this.datasource);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.DATASOURCE;
	}


	private void initializeComponents() {
		textFieldServer = new JTextField();
		textFieldServer.setEditable(false);
		textFieldServer.setColumns(10);
		labelServer = new JLabel("Server:");
		textFieldDatasourceType = new JTextField();
		textFieldDatasourceType.setEditable(false);
		textFieldDatasourceType.setColumns(10);
		labelDatasourceType = new JLabel("DatasourceType:");
		textFieldOpenType = new JTextField();
		textFieldOpenType.setEditable(false);
		textFieldOpenType.setColumns(10);
		labelOpenType = new JLabel("OpenType:");
		textFieldDescription = new JTextArea();
		textFieldDescription.setBorder(MetalBorders.getTextFieldBorder());
		labelDescription = new JLabel("Description:");
		buttonChangePassword = new SmButton();
		buttonChangePassword.setUseDefaultSize(false);
		buttonChangePassword.setText("ChangePassword...");
		buttonApply = new SmButton("Apply");
		buttonApply.setPreferredSize(DEFAULT_BUTTON_PREFERREDSIZE);
		buttonReset = new SmButton("OK");
		buttonReset.setPreferredSize(DEFAULT_BUTTON_PREFERREDSIZE);

		this.setLayout(new GridBagLayout());
		this.add(labelServer, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		this.add(textFieldServer, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 0, 10));

		this.add(labelDatasourceType, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(textFieldDatasourceType, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));

		this.add(labelOpenType, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(textFieldOpenType, new GridBagConstraintsHelper(0, 5, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));

		this.add(labelDescription, new GridBagConstraintsHelper(0, 6, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(textFieldDescription, new GridBagConstraintsHelper(0, 7, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));

		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new GridBagLayout());
//		panelButtons.setBorder(BorderFactory.createLineBorder(Color.black));
		panelButtons.add(buttonChangePassword, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 10, 0));
		panelButtons.add(buttonApply, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 10, 0));
		panelButtons.add(buttonReset, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 10, 10));

		this.add(panelButtons, new GridBagConstraintsHelper(0, 99, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));
//		JPanel panelDatasourceInfo = new JPanel();
//		panelDatasourceInfo.setBorder(new TitledBorder(null, ControlsProperties.getString("String_DatasourceInfo"), TitledBorder.LEADING, TitledBorder.TOP,
//				null, null));
//		GroupLayout gl_panelDatasourceInfo = new GroupLayout(panelDatasourceInfo);
//		gl_panelDatasourceInfo.setAutoCreateContainerGaps(true);
//		gl_panelDatasourceInfo.setAutoCreateGaps(true);
//		panelDatasourceInfo.setLayout(gl_panelDatasourceInfo);
//
//		// @formatter:off
//		gl_panelDatasourceInfo.setHorizontalGroup(gl_panelDatasourceInfo.createSequentialGroup()
//				.addGroup(gl_panelDatasourceInfo.createParallelGroup(Alignment.LEADING)
//						.addComponent(this.labelServer)
//						.addComponent(this.labelDatasourceType)
//						.addComponent(this.labelOpenType)
//						.addComponent(this.labelDescription))
//				.addGroup(gl_panelDatasourceInfo.createParallelGroup(Alignment.LEADING)
//						.addComponent(this.textFieldServer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//						.addComponent(this.textFieldDatasourceType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//						.addComponent(this.textFieldOpenType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//						.addComponent(this.textFieldDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
//
//		gl_panelDatasourceInfo.setVerticalGroup(gl_panelDatasourceInfo.createSequentialGroup()
//				.addGroup(gl_panelDatasourceInfo.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.labelServer)
//						.addComponent(this.textFieldServer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//				.addGroup(gl_panelDatasourceInfo.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.labelDatasourceType)
//						.addComponent(this.textFieldDatasourceType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//				.addGroup(gl_panelDatasourceInfo.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.labelOpenType)
//						.addComponent(this.textFieldOpenType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//				.addGroup(gl_panelDatasourceInfo.createParallelGroup(Alignment.LEADING)
//						.addComponent(this.labelDescription)
//						.addComponent(this.textFieldDescription, 80, 150, Short.MAX_VALUE)));
//		// @formatter:on
//
//		GroupLayout gl_mainContent = new GroupLayout(this);
//		gl_mainContent.setAutoCreateContainerGaps(true);
//		gl_mainContent.setAutoCreateGaps(true);
//		this.setLayout(gl_mainContent);
//
//		// @formatter:off
//		gl_mainContent.setHorizontalGroup(gl_mainContent.createSequentialGroup()
//				.addGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
//						.addComponent(panelDatasourceInfo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
//						.addComponent(this.buttonChangePassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//				.addGroup(gl_mainContent.createParallelGroup(Alignment.TRAILING)
////						.addComponent(scrollPaneStatisticValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE+30)
//						.addGroup(gl_mainContent.createSequentialGroup()
//								.addComponent(this.buttonReset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//								.addComponent(this.buttonApply, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))));
//
//		gl_mainContent.setVerticalGroup(gl_mainContent.createSequentialGroup()
//				.addGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
//						.addComponent(panelDatasourceInfo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
////						.addComponent(scrollPaneStatisticValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.buttonChangePassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//						.addComponent(this.buttonReset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//						.addComponent(this.buttonApply, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
//		// @formatter:on
	}

	private void fillComponents() {
		if (this.datasource != null) {
			this.textFieldServer.setText(this.datasource.getConnectionInfo().getServer());
			this.textFieldDatasourceType.setText(this.datasource.getEngineType().toString());
			String message;
			if (this.datasource.getConnectionInfo().isReadOnly()) {
				message = CoreProperties.getString(CoreProperties.ReadOnly);
			} else {
				message = CoreProperties.getString(CoreProperties.Exclusive);
			}
			this.textFieldOpenType.setText(message);
			this.textFieldDescription.setText(this.datasource.getDescription());

		}
	}


	private void reset() {
		this.newPassword = this.datasource.getConnectionInfo().getPassword();
		this.description = this.datasource.getDescription();
		this.textFieldDescription.setText(this.description);

		setButtonApplyEnabledInEDT(checkChange());
		setButtonResetEnabledInEDT(checkChange());
	}

	private void registerEvents() {
		this.buttonChangePassword.addActionListener(buttonActionListener);
		this.buttonApply.addActionListener(buttonActionListener);
		this.buttonReset.addActionListener(buttonActionListener);
		this.textFieldDescription.getDocument().addDocumentListener(textFieldDescriptionDocumentListener);
	}

	private void unregisterEvents() {
		this.buttonChangePassword.removeActionListener(buttonActionListener);
		this.buttonApply.removeActionListener(buttonActionListener);
		this.buttonReset.removeActionListener(buttonActionListener);
		this.textFieldDescription.getDocument().removeDocumentListener(textFieldDescriptionDocumentListener);
	}

	private void initializeResources() {

		this.labelServer.setText(ControlsProperties.getString("String_LabelServer"));
		this.labelDatasourceType.setText(ControlsProperties.getString("String_LabelEngineType"));
		this.labelOpenType.setText(ControlsProperties.getString("String_OpenMode"));
		this.labelDescription.setText(ControlsProperties.getString("String_labelDescription"));
		this.buttonChangePassword.setText(ControlsProperties.getString("String_ButtonChangePassword"));
		this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
		this.buttonReset.setText(CommonProperties.getString("String_Button_Reset"));

	}

	private void buttonChangePasswordClicked() {
		JDialogChangePassword dialog = new JDialogChangePassword("");

		dialog.setPasswordCheck(new IPasswordCheck() {

			@Override
			public boolean checkPassword(String password) {
				return StringUtilities.stringEquals(newPassword, password);
			}
		});
		dialog.setVisible(true);

		if (dialog.getDialogResult() == DialogResult.OK) {
			this.newPassword = dialog.getNewPassword();
			setButtonApplyEnabledInEDT(checkChange());
			setButtonResetEnabledInEDT(checkChange());
		}
	}

	private void buttonApplyClicked() {
		try {
			this.datasource.setDescription(this.description);
			if (!this.datasource.getConnectionInfo().getPassword().equals(this.newPassword)) {
				this.datasource.changePassword(this.datasource.getConnectionInfo().getPassword(), this.newPassword);
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
		setButtonApplyEnabledInEDT(false);
		setButtonResetEnabledInEDT(false);
	}

	private void buttonResetClicked() {
		reset();
	}

	private void textFieldDescriptionTextChanged() {
		this.description = this.textFieldDescription.getText();
		setButtonApplyEnabledInEDT(checkChange());
		setButtonResetEnabledInEDT(checkChange());
	}

	private void setButtonApplyEnabledInEDT(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				buttonApply.setEnabled(enabled);
			}
		});
	}

	private void setButtonResetEnabledInEDT(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				buttonReset.setEnabled(enabled);
			}
		});
	}

	private boolean checkChange() {
		boolean isChange = false;

		try {
			if (!this.datasource.getDescription().equals(this.description) || !this.datasource.getConnectionInfo().getPassword().equals(this.newPassword)) {
				isChange = true;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return isChange;
	}

	private boolean isChangePasswordEnabled() {

		EngineType tempDatasourceEngineType = datasource.getEngineType();
		return this.datasource != null && tempDatasourceEngineType != EngineType.BAIDUMAPS && tempDatasourceEngineType != EngineType.BEYONDB
				&& tempDatasourceEngineType != EngineType.DB2 && tempDatasourceEngineType != EngineType.GOOGLEMAPS
				&& tempDatasourceEngineType != EngineType.GBASE && tempDatasourceEngineType != EngineType.HIGHGODB
				&& tempDatasourceEngineType != EngineType.KINGBASE && tempDatasourceEngineType != EngineType.MONGODB
				&& tempDatasourceEngineType != EngineType.MYSQL && tempDatasourceEngineType != EngineType.ORACLEPLUS
				&& tempDatasourceEngineType != EngineType.ORACLESPATIAL && tempDatasourceEngineType != EngineType.POSTGRESQL
				&& tempDatasourceEngineType != EngineType.SQLPLUS && tempDatasourceEngineType != EngineType.SUPERMAPCLOUD && !datasource.isReadOnly();
	}
}
