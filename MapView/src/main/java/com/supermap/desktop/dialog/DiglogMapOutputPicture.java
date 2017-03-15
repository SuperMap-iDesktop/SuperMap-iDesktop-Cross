package com.supermap.desktop.dialog;

import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.ui.controls.borderPanel.PanelGroupBoxViewBounds;

import javax.swing.*;
import java.awt.*;

/**
 * @author YuanR
 *         地图输出为图片窗体
 */
public class DiglogMapOutputPicture extends SmDialog {

	private JPanel mainPanel;
	private JPanel outputSetPanel;
	private PanelGroupBoxViewBounds panelGroupBoxViewBounds;
	private PanelButton panelButton;

	private JLabel FileNameLabel;
	// 带有路径选择按钮的textFile
	private FileChooserControl fileChooserControlExportPath;

	private JLabel resolutionLabel;
	// 带有警告提示的textFile
	private WaringTextField waringTextField;
	private JLabel DPILabel;

	private JLabel widthLabel;
	private JLabel heightLabel;
	private JTextField widthTextField;
	private JTextField heightTextField;

	// 预计占用内存label，模块暂不实现
	private JCheckBox backTransparent;

	private static final int DEFAULT_LABELSIZE = 60;


	/**
	 * 默认构造方法
	 */
	public DiglogMapOutputPicture() {
		super();
		initComponents();
		initResources();
		initLayout();
		addListeners();

		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setSize(new Dimension(620, this.getPreferredSize().height));
		this.setVisible(true);

	}

	private void initComponents() {

		this.mainPanel = new JPanel();
		this.outputSetPanel = new JPanel();
		this.panelGroupBoxViewBounds = new PanelGroupBoxViewBounds();
		this.panelButton = new PanelButton();

		this.FileNameLabel = new JLabel("FileName");
		this.fileChooserControlExportPath = new FileChooserControl();
		this.resolutionLabel = new JLabel("resolution");
		this.waringTextField = new WaringTextField();
		this.DPILabel = new JLabel("DPI");
		this.widthLabel = new JLabel("width");
		this.heightLabel = new JLabel("height");
		this.widthTextField = new JTextField();
		this.widthTextField.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.heightTextField = new JTextField();
		this.heightTextField.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);

		this.backTransparent = new JCheckBox("backTransparent");
	}

	private void initLayout() {
		intiOutputSetPanelLayout();
		initMainPanelLayout();

		this.setLayout(new BorderLayout());
		this.add(this.mainPanel, BorderLayout.CENTER);
		this.add(this.panelButton, BorderLayout.SOUTH);
	}

	/**
	 * 导出设置面板布局设计
	 */
	private void intiOutputSetPanelLayout() {

		this.outputSetPanel.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_ExportPropertySet")));
		GroupLayout outputSetPanelLayout = new GroupLayout(this.outputSetPanel);
		outputSetPanelLayout.setAutoCreateContainerGaps(true);
		outputSetPanelLayout.setAutoCreateGaps(true);
		this.outputSetPanel.setLayout(outputSetPanelLayout);

		// @formatter:off
		outputSetPanelLayout.setHorizontalGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(outputSetPanelLayout.createSequentialGroup()
						.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(this.FileNameLabel, GroupLayout.PREFERRED_SIZE, DEFAULT_LABELSIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.resolutionLabel, GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.widthLabel, GroupLayout.PREFERRED_SIZE, DEFAULT_LABELSIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.heightLabel, GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(outputSetPanelLayout.createSequentialGroup().addGap(6).addComponent(fileChooserControlExportPath,GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, Short.MAX_VALUE))
								.addGroup(outputSetPanelLayout.createSequentialGroup()
										.addComponent(waringTextField,GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, Short.MAX_VALUE).addGap(7)
										.addComponent(DPILabel))
								.addGroup(outputSetPanelLayout.createSequentialGroup().addGap(6).addComponent(this.widthTextField,GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, Short.MAX_VALUE ))
								.addGroup(outputSetPanelLayout.createSequentialGroup().addGap(6).addComponent(this.heightTextField,GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, Short.MAX_VALUE))))
				.addGroup(outputSetPanelLayout.createSequentialGroup()
						.addComponent(this.backTransparent)));

		outputSetPanelLayout.setVerticalGroup(outputSetPanelLayout.createSequentialGroup()
				.addGroup(outputSetPanelLayout.createSequentialGroup()
					.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.FileNameLabel)
						.addComponent(this.fileChooserControlExportPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.resolutionLabel)
						.addComponent(this.waringTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.DPILabel))
					.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.widthLabel)
						.addComponent(this.widthTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.heightLabel)
						.addComponent(this.heightTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addGroup(outputSetPanelLayout.createSequentialGroup()
						.addComponent(this.backTransparent))
				.addGap(5,5,Short.MAX_VALUE));
		// @formatter:on
	}

	/**
	 * 主面板布局设计
	 */
	private void initMainPanelLayout() {
		GroupLayout mainPanelLayout = new GroupLayout(this.mainPanel);
		mainPanelLayout.setAutoCreateContainerGaps(true);
		mainPanelLayout.setAutoCreateGaps(true);
		this.mainPanel.setLayout(mainPanelLayout);

		//@formatter:off
         mainPanelLayout.setHorizontalGroup(mainPanelLayout.createSequentialGroup()
                   .addComponent(this.outputSetPanel,0,120,Short.MAX_VALUE)
                   .addComponent(this.panelGroupBoxViewBounds,0,180,Short.MAX_VALUE));
         mainPanelLayout.setVerticalGroup(mainPanelLayout.createSequentialGroup()
                   .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	    				.addComponent(this.outputSetPanel,180,180,180)
	    				.addComponent(this.panelGroupBoxViewBounds,180,180,180)));
         //@formatter:on


	}

	private void initResources() {
		this.FileNameLabel.setText(MapViewProperties.getString("String_FileName"));
		this.resolutionLabel.setText(CommonProperties.getString("String_Resolution"));
		this.DPILabel.setText("DPI");
		this.widthLabel.setText(MapViewProperties.getString("String_FormSavePicture_Width"));
		this.heightLabel.setText(MapViewProperties.getString("String_FormSavePicture_Height"));
		this.backTransparent.setText(MapViewProperties.getString("String_PNG_BackTransparent"));
	}

	private void addListeners() {

	}


}
