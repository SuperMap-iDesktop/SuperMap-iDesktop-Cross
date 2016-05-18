package com.supermap.desktop.geometryoperation.control;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentListener;
import javax.swing.SwingUtilities;

import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxDataset;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxDatasource;

public class JDialogGeometryConvert extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel labelDesDatasource;
	private JLabel labelDesDataset;
	private DatasourceComboBox comboBoxDatasource;
	private DatasetComboBox comboBoxDataset;
	private JCheckBox checkBoxNewDataset;
	private SmTextFieldLegit textFieldNewDataset;
	private JCheckBox checkBoxRemoveSrc;
	private JButton buttonOK;
	private JButton buttonCancel;

	private Datasource desDatasource;
	private DatasetVector desDataset;
	private String newDatasetName;
	private boolean isNewDataset;
	private boolean isRemoveSrc;

	private ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub

		}
	};

	public JDialogGeometryConvert(String title) {
		initializeComponents(title);
	}

	private void initializeComponents(String title) {
		setTitle(title);
		this.labelDesDatasource = new JLabel(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.labelDesDataset = new JLabel(ControlsProperties.getString("String_Label_TargetDataset"));
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxDataset = new DatasetComboBox();
		this.checkBoxNewDataset = new JCheckBox(ControlsProperties.getString("String_Label_NewDataset"));
		this.textFieldNewDataset = new SmTextFieldLegit();
		this.checkBoxRemoveSrc = new JCheckBox(MapEditorProperties.getString("String_RemoveSrcObj"));
		this.buttonOK = new JButton(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel = new JButton(CommonProperties.getString(CommonProperties.Cancel));

		GroupLayout gl = new GroupLayout(getContentPane());
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		getContentPane().setLayout(gl);

		// @formatter:off
		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelDesDatasource)
								.addComponent(this.labelDesDataset)
								.addComponent(this.checkBoxNewDataset)
								.addComponent(this.checkBoxRemoveSrc))
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxDatasource, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
								.addComponent(this.comboBoxDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldNewDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)))
				.addGroup(gl.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDesDatasource)
						.addComponent(this.comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDesDataset)
						.addComponent(this.comboBoxDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxNewDataset)
						.addComponent(this.textFieldNewDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(this.checkBoxRemoveSrc)
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	public static void main(String[] args) {
		final JDialogGeometryConvert dialog = new JDialogGeometryConvert("test");
		dialog.setSize(400, 200);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dialog.setVisible(true);
			}
		});
	}
}
