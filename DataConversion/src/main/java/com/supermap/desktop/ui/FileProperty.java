package com.supermap.desktop.ui;

import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ImportFileInfo;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;

public class FileProperty extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel labelFileType = new JLabel();
	private JLabel labelFileLocation = new JLabel();
	private JLabel labelFileSize = new JLabel();
	private JLabel labelFileModify = new JLabel();
	private JLabel labelProperty = new JLabel();
	private JCheckBox checkboxHidden = new JCheckBox();
	private JLabel labelDate = new JLabel("date");
	private JLabel labelSize = new JLabel("size");
	private JLabel labelLocation = new JLabel("location");
	private JLabel labelType = new JLabel("type");
	private SmButton buttonSure = new SmButton();
	private SmButton buttonQuit = new SmButton();

	public FileProperty(JDialog owner,  ImportFileInfo fileInfo) {
		super(owner,true);
		initComponent();
		setFileInfo(fileInfo);
		setLocationRelativeTo(owner);
		this.componentList.add(this.buttonSure);
		this.componentList.add(this.buttonQuit);
		this.setFocusTraversalPolicy(policy);
	}

	public FileProperty() {
		initComponent();
	}

	private void setFileInfo(ImportFileInfo tempFileInfo) {
		if (null != tempFileInfo) {
			String fileName = tempFileInfo.getFileName();
			if (fileName.lastIndexOf(".")>0) {
				String fileType = fileName.substring(fileName.lastIndexOf("."),
						fileName.length());
				this.labelType.setText("(" + fileType + ")");
			}else {
				this.labelType.setText(DataConversionProperties.getString("String_Dir"));
			}
			this.labelLocation.setText(tempFileInfo.getFilePath());
			File file = new File(tempFileInfo.getFilePath());
			this.labelSize.setText(parseFileSize(file.length()));
			String dateStr = new String(
					DataConversionProperties.getString("string_dataFormat_ch"));
			SimpleDateFormat sdf = new SimpleDateFormat(dateStr);
			this.labelDate.setText(sdf.format(file.lastModified()));
			this.checkboxHidden.setSelected(file.isHidden());
		}
	}

	private String parseFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size > kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else {
			return String.format("%d B", size);
		}
	}

	private void initComponent() {
		setBounds(100, 100, 600, 250);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), DataConversionProperties
				.getString("string_border_panelBase"), TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		buttonSure.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonQuit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		initResource();
		//@formatter:off
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
							.addGap(14))
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
							.addComponent(buttonSure)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonQuit)
							.addGap(22))))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonSure)
						.addComponent(buttonQuit))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(labelProperty))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(labelFileType)
								.addComponent(labelFileLocation)
								.addComponent(labelFileSize)
								.addComponent(labelFileModify))))
					.addGap(25)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(labelDate)
						.addComponent(labelSize)
						.addComponent(labelLocation)
						.addComponent(labelType)
						.addComponent(checkboxHidden))
					.addContainerGap(218, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(labelFileType)
								.addComponent(labelType))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(labelFileLocation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(labelFileSize)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(labelFileModify)
							.addGap(10))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(labelLocation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(labelSize)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(labelDate)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(labelProperty)
						.addComponent(checkboxHidden))
					.addContainerGap(44, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPanel.setLayout(gl_contentPanel);
	}

	private void initResource() {
		setTitle(DataConversionProperties.getString("string_fileProperty"));
		this.labelFileLocation.setText(DataConversionProperties.getString("string_label_lblFileLocation"));
		this.labelFileType.setText(DataConversionProperties.getString("string_label_lblFileType"));
		this.labelFileModify.setText(DataConversionProperties.getString("string_label_lblFileLastModify"));
		this.labelFileSize.setText(DataConversionProperties.getString("string_label_lblFileSize"));
		this.labelProperty.setText(DataConversionProperties.getString("string_label_lblFileProperty"));
		this.checkboxHidden.setText(DataConversionProperties.getString("string_chcekbox_hidden"));
		this.buttonSure.setText(DataConversionProperties.getString("string_button_sure"));
		this.buttonQuit.setText(DataConversionProperties.getString("string_button_quit"));
	}
}
