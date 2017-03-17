package com.supermap.desktop.dialog;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.ui.controls.borderPanel.PanelGroupBoxViewBounds;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.text.MessageFormat;

/**
 * @author YuanR
 *         地图输出为图片窗体
 */
public class DiglogMapOutputPicture extends SmDialog {

	private JPanel mainPanel;
	private JPanel outputSetPanel;
	private PanelGroupBoxViewBounds panelGroupBoxViewBounds;
	private PanelButton panelButton;

	private JLabel fileNameLabel;
	// 带有路径选择按钮的textFile
	private FileChooserControl fileChooserControlExportPath;

	private JLabel resolutionLabel;
	// 带有警告提示的textFile
	private WaringTextField resolutionTextField;
	private JLabel DPILabel;

	private JLabel widthLabel;
	private JLabel heightLabel;
	private JTextField widthTextField;
	private JTextField heightTextField;

	private JLabel expectedMemoryLabel;
	private WaringTextField occupiedMemoryTextField;
	private JLabel memoryUnitLabel;


	// 预计占用内存label，模块暂不实现
	private JCheckBox backTransparent;

	private SmFileChoose exportPathFileChoose;
	private String fileName;

	private static final int DEFAULT_LABELSIZE = 80;
	private static final int DEFAULT_GAP = 23;


	/**
	 * 默认构造方法
	 */
	public DiglogMapOutputPicture() {
		super();
		initComponents();
		initResources();
		initLayout();
		// 初始化各个控件的默认设置
		registEvents();
		// 初始化各个控件的默认设置
		initParameter();
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setSize(new Dimension(680, this.getPreferredSize().height));

	}

	/**
	 * 初始化界面控件
	 */
	private void initComponents() {

		this.mainPanel = new JPanel();
		this.outputSetPanel = new JPanel();
		this.panelGroupBoxViewBounds = new PanelGroupBoxViewBounds();
		this.panelButton = new PanelButton();

		this.fileNameLabel = new JLabel("FileName");
		this.fileChooserControlExportPath = new FileChooserControl();

		this.resolutionLabel = new JLabel("resolution");
		this.resolutionTextField = new WaringTextField();
		this.DPILabel = new JLabel("DPI");

		this.widthLabel = new JLabel("width");
		this.heightLabel = new JLabel("height");
		this.widthTextField = new JTextField();
		this.widthTextField.setEditable(false);
		this.widthTextField.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		this.heightTextField = new JTextField();
		this.heightTextField.setEditable(false);
		this.heightTextField.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);

		this.expectedMemoryLabel = new JLabel("expectedMemory");
		this.occupiedMemoryTextField = new WaringTextField();
		this.occupiedMemoryTextField.getTextField().setEditable(false);
		this.occupiedMemoryTextField.getTextField().setBorder(new EmptyBorder(0, 0, 0, 0));
		this.memoryUnitLabel = new JLabel("memoryUnit");

		this.backTransparent = new JCheckBox("backTransparent");
	}

	/**
	 * 初始化界面布局
	 */
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
						.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.fileNameLabel, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.resolutionLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.widthLabel, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.heightLabel, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(outputSetPanelLayout.createSequentialGroup().addGap(DEFAULT_GAP).addComponent(fileChooserControlExportPath,GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, Short.MAX_VALUE))
								.addGroup(outputSetPanelLayout.createSequentialGroup()
										.addComponent(resolutionTextField,GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, Short.MAX_VALUE).addGap(7)
										.addComponent(DPILabel))
								.addGroup(outputSetPanelLayout.createSequentialGroup().addGap(DEFAULT_GAP).addComponent(this.widthTextField,GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, Short.MAX_VALUE ))
								.addGroup(outputSetPanelLayout.createSequentialGroup().addGap(DEFAULT_GAP).addComponent(this.heightTextField,GroupLayout.PREFERRED_SIZE,DEFAULT_LABELSIZE, Short.MAX_VALUE))))
				.addGroup(outputSetPanelLayout.createSequentialGroup()
						.addComponent(this.expectedMemoryLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.occupiedMemoryTextField,50,50,50)
						.addComponent(this.memoryUnitLabel,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(outputSetPanelLayout.createSequentialGroup()
						.addComponent(this.backTransparent)));

		outputSetPanelLayout.setVerticalGroup(outputSetPanelLayout.createSequentialGroup()
				.addGroup(outputSetPanelLayout.createSequentialGroup()
					.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.fileNameLabel)
						.addComponent(this.fileChooserControlExportPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.resolutionLabel)
						.addComponent(this.resolutionTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.DPILabel))
					.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.widthLabel)
						.addComponent(this.widthTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.heightLabel)
						.addComponent(this.heightTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addGroup(outputSetPanelLayout.createSequentialGroup()
						.addGroup(outputSetPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.expectedMemoryLabel)
						.addComponent(this.occupiedMemoryTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.memoryUnitLabel))
				.addGroup(outputSetPanelLayout.createSequentialGroup()
						.addComponent(this.backTransparent))
				.addGap(5,5,Short.MAX_VALUE)));
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
	    				.addComponent(this.outputSetPanel,200,200,200)
	    				.addComponent(this.panelGroupBoxViewBounds,200,200,200)));
         //@formatter:on


	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_OutputPicture"));
		this.fileNameLabel.setText(MapViewProperties.getString("String_FileName"));
		this.resolutionLabel.setText(CommonProperties.getString("String_Resolution"));
		this.DPILabel.setText("DPI");
		this.widthLabel.setText(MapViewProperties.getString("String_FormSavePicture_Width"));
		this.heightLabel.setText(MapViewProperties.getString("String_FormSavePicture_Height"));
		this.expectedMemoryLabel.setText(MapViewProperties.getString("String_ExpectedOccupyMemory"));
		this.memoryUnitLabel.setText("123132");

		this.backTransparent.setText(MapViewProperties.getString("String_PNG_BackTransparent"));
	}

	/**
	 * 添加监听事件
	 */
	private void registEvents() {
		removeEvents();
		this.fileChooserControlExportPath.getButton().addActionListener(this.exportPathLitener);

		this.resolutionTextField.registEvents();
		this.resolutionTextField.getTextField().addCaretListener(this.resolutionCareListener);

		this.occupiedMemoryTextField.registEvents();
		this.occupiedMemoryTextField.getTextField().addCaretListener(this.memoryCareListener);
	}

	/**
	 * 移除监听事件
	 */
	private void removeEvents() {
		this.fileChooserControlExportPath.getButton().removeActionListener(this.exportPathLitener);

		this.resolutionTextField.removeEvents();
		this.resolutionTextField.getTextField().removeCaretListener(this.resolutionCareListener);

		this.occupiedMemoryTextField.removeEvents();
		this.occupiedMemoryTextField.getTextField().removeCaretListener(this.memoryCareListener);
	}

	/**
	 * 路径设置按钮监听事件
	 */
	private ActionListener exportPathLitener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//设置文件选择器默认文件名为filed中的内容
				// TODO yuanR
				// 此处希望实现：当初次打开时，设置选择框中得文件名称格不为空
				// 当手动输入过一次文件名后，下次打开时就会，文件名称格会显示上次输入的内容，需要了解其机制，反正跟文本框的输入无关！！！
//				if (!StringUtilities.isNullOrEmpty(fileChooserControlExportPath.getEditor().getText())) {
//					exportPathFileChoose.setCurrentDirectory(new File(fileChooserControlExportPath.getEditor().getText()));
//				}
				int state = exportPathFileChoose.showSaveDialog(null);
				if (state == JFileChooser.APPROVE_OPTION) {
					String path = exportPathFileChoose.getFilePath();
					fileChooserControlExportPath.getEditor().setText(path);
					fileName = exportPathFileChoose.getFileName();
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} finally {
				CursorUtilities.setDefaultCursor();
			}
		}
	};

	/**
	 * 分辨率textFiled，内容改变监听，主要用于：高、宽、内存值的联动
	 */
	private CaretListener resolutionCareListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			// 当textFiled为空时不做任何联动效果
			if (!StringUtilities.isNullOrEmpty(resolutionTextField.getTextField().getText())) {
				// TODO 当分辨率改变时，需要让高度、宽度和内存随之改变
				setWidthANDHeight();
				setExpectedMemory();
			}
		}
	};

	/**
	 * 设置图片的宽度和高度，跟随分辨率和范围的变化而变化
	 */
	private void setWidthANDHeight() {
		this.widthTextField.setText(resolutionTextField.getTextField().getText());
		this.heightTextField.setText(resolutionTextField.getTextField().getText());

	}

	/**
	 * 设置消耗内存的值，跟随分辨率的变化而变化
	 */
	private void setExpectedMemory() {
		// 先获得预计占用的内存值，根据内存占用值的大小，需要给予相应的内存单位(B,KB,MB,GB)
		// TODO 统计需要占用的内存值


		String value = resolutionTextField.getTextField().getText();
		if (value.equals("50")) {
			this.memoryUnitLabel.setText("KB");

		} else if (value.equals("100")) {
			this.memoryUnitLabel.setText("MB");

		} else if (value.equals("150")) {
			this.memoryUnitLabel.setText("GB");

		}


		this.occupiedMemoryTextField.setText(resolutionTextField.getTextField().getText());
	}


	/**
	 * 当前内存textFiled，内容改变监听
	 */
	private CaretListener memoryCareListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			// 当前内存textFiled是动态变化的，所以当其值改变时，动态设置其参数范围
			initOccupiedMemory();
		}
	};

	/**
	 * 初始化参数，刷新面板
	 */
	private void initParameter() {
		initFileChoose();
		initResolution();
//		initOccupiedMemory();
	}

	/**
	 * 初始化文件选择器、文件路径Filed默认路径
	 */
	private void initFileChoose() {
		String moduleName = "MapOutputPicture";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			// 文件过滤器
			String fileFilters = SmFileChoose.bulidFileFilters(
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_PNG_FileFilter"),
							ControlsProperties.getString("String_PNG_Filters")),
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_JPG_FileFilter"),
							ControlsProperties.getString("String_JPG_Filters")),
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_Graphic_FileFilter"),
							ControlsProperties.getString("String_BMP_Filters")),
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_GIF_FileFilter"),
							ControlsProperties.getString("String_GIF_Filters")),
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_EPS_FileFilter"),
							ControlsProperties.getString("String_EPS_Filters")),
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_TIFF_FileFilter"),
							ControlsProperties.getString("String_TIFF_Filters")));

			SmFileChoose.addNewNode(fileFilters, MapViewProperties.getString("String_MapOutputPictureCurrentDirectory"),
					ControlsProperties.getString("String_Save"), moduleName, "SaveOne");
		}
		this.exportPathFileChoose = new SmFileChoose(moduleName);
		// 设置选择模式为：仅为文件夹
		this.exportPathFileChoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
		String lastPath = this.exportPathFileChoose.getModuleLastPath() + "\\" + "ExportImage.png";
		this.fileChooserControlExportPath.setText(lastPath);
	}

	/**
	 * 初始化分辨率
	 */
	private void initResolution() {
		// 给其初始的分辨率值
		this.resolutionTextField.setText("96");
		// 分辨率的范围为：(1,65535)
		this.resolutionTextField.setInitInfo(1, 65535, WaringTextField.INTEGER_TYPE, "null");
	}

	/**
	 * 初始化内存值情况的textFiled
	 * 当占用内存改变时，进行此方法设置
	 */
	private void initOccupiedMemory() {
		// 设置内存文本框相关参数，因为内存是不断变化的，所以需要动态设置
		//1、判断文件路径是否正确，获得所存文件的根目录，以获取磁盘剩余信息
		String filePath = this.fileChooserControlExportPath.getEditor().getText();
		if (!StringUtilities.isNullOrEmpty(filePath) && filePath.indexOf("\\") > 0) {
			//获得文件所在的文件夹目录
			filePath = filePath.substring(0, filePath.lastIndexOf('\\'));
			File file = new File(filePath);
			if (file.exists()) {
				double constm = 1024 * 1024 * 1024;
				double endValue = file.getFreeSpace() / constm;
				// 将磁盘剩余空间的一半作为内存限制,可做修改--yuanR 2017.3.15
				DecimalFormat df = new DecimalFormat("0");
				// 设置内存TextField错误提示信息
				this.occupiedMemoryTextField.setCustomLabelToolTipText(MessageFormat.format(MapViewProperties.getString("String_Insufficient_Disk_Space"), df.format(endValue)));
				this.occupiedMemoryTextField.setInitInfo(0, endValue, WaringTextField.MAXVALUE_TYPE, "null");
			}
		}
	}
}
