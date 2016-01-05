package com.supermap.desktop.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;

public class FileProperty extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblFileType = new JLabel();
	private JLabel lblFileLocation = new JLabel();
	private JLabel lblFileSize = new JLabel();
	private JLabel lblFileModify = new JLabel();
	private JLabel lblProperty = new JLabel();
	private JCheckBox chckbxHidden = new JCheckBox();
	private JLabel lblDate = new JLabel("date");
	private JLabel lblSize = new JLabel("size");
	private JLabel lblLocation = new JLabel("location");
	private JLabel lblType = new JLabel("type");
	private JButton btnSure = new JButton();
	private JButton btnQuit = new JButton();

	public FileProperty(Dialog owner,  ImportFileInfo fileInfo) {
		super(owner,true);
		initComponent();
		setFileInfo(fileInfo);
		setLocationRelativeTo(owner);
	}

	public FileProperty() {
		initComponent();
	}

	private void setFileInfo(ImportFileInfo tempFileInfo) {
		if (null != tempFileInfo) {
			String fileName = tempFileInfo.getFileName();
			String fileType = fileName.substring(fileName.lastIndexOf("."),
					fileName.length());
			this.lblType.setText("(" + fileType + ")");
			this.lblLocation.setText(tempFileInfo.getFilePath());
			File file = new File(tempFileInfo.getFilePath());
			this.lblSize.setText(parseFileSize(file.length()));
			String dateStr = new String(
					DataConversionProperties.getString("string_dataFormat_ch"));
			SimpleDateFormat sdf = new SimpleDateFormat(dateStr);
			this.lblDate.setText(sdf.format(file.lastModified()));
			this.chckbxHidden.setSelected(file.isHidden());
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
		btnSure.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnQuit.addActionListener(new ActionListener() {

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
							.addComponent(btnSure)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnQuit)
							.addGap(22))))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSure)
						.addComponent(btnQuit))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(lblProperty))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblFileType)
								.addComponent(lblFileLocation)
								.addComponent(lblFileSize)
								.addComponent(lblFileModify))))
					.addGap(25)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDate)
						.addComponent(lblSize)
						.addComponent(lblLocation)
						.addComponent(lblType)
						.addComponent(chckbxHidden))
					.addContainerGap(218, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblFileType)
								.addComponent(lblType))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblFileLocation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblFileSize)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblFileModify)
							.addGap(10))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblLocation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblSize)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblDate)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblProperty)
						.addComponent(chckbxHidden))
					.addContainerGap(44, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPanel.setLayout(gl_contentPanel);
	}

	private void initResource() {
		setTitle(DataConversionProperties.getString("string_fileProperty"));
		this.lblFileLocation.setText(DataConversionProperties.getString("string_label_lblFileLocation"));
		this.lblFileType.setText(DataConversionProperties.getString("string_label_lblFileType"));
		this.lblFileModify.setText(DataConversionProperties.getString("string_label_lblFileLastModify"));
		this.lblFileSize.setText(DataConversionProperties.getString("string_label_lblFileSize"));
		this.lblProperty.setText(DataConversionProperties.getString("string_label_lblFileProperty"));
		this.chckbxHidden.setText(DataConversionProperties.getString("string_chcekbox_hidden"));
		this.btnSure.setText(DataConversionProperties.getString("string_button_sure"));
		this.btnQuit.setText(DataConversionProperties.getString("string_button_quit"));
	}
}
