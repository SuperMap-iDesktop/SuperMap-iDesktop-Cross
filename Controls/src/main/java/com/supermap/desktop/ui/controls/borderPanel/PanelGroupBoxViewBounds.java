package com.supermap.desktop.ui.controls.borderPanel;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.SMFormattedTextField;

import javax.swing.*;
import java.awt.*;

/**
 * @author YuanR
 *         当前视图范围panel
 */
public class PanelGroupBoxViewBounds extends JPanel {

	private JLabel labelCurrentViewLeft;
	private SMFormattedTextField textFieldCurrentViewLeft;
	private JLabel labelCurrentViewTop;
	private SMFormattedTextField textFieldCurrentViewTop;
	private JLabel labelCurrentViewRight;
	private SMFormattedTextField textFieldCurrentViewRight;
	private JLabel labelCurrentViewBottom;
	private SMFormattedTextField textFieldCurrentViewBottom;

	private JPanel mainPanel;
	private JPanel viewPanel;
	private JPanel setPanel;

	// 整幅地图范围
	private JButton wholeMapBoundsButton;
	// 当前窗口地图范围
	private JButton CurrentMapBoundsButton;
	// 自定义范围：选择对象范围/绘制范围
//	private JButton drewMapBoundsButton;
	private JComboBox customBoundsComboBox;


	// 复制
	private JButton copyButton;
	// 粘贴
	private JButton pasteButton;

	private static final int DEFAULT_LABELSIZE = 50;
	private static final int DEFAULT_BUTTONSIZE = 85;


	/**
	 * 默认构造方法
	 */
	public PanelGroupBoxViewBounds() {
		super();
		initCompont();
		initResource();
		initLayout();
		initListeners();
	}

	private void initCompont() {

		this.mainPanel = new JPanel();

		this.viewPanel = new JPanel();
		this.labelCurrentViewLeft = new JLabel("Left:");
		this.textFieldCurrentViewLeft = new SMFormattedTextField();
		this.labelCurrentViewTop = new JLabel("Top:");
		this.textFieldCurrentViewTop = new SMFormattedTextField();
		this.labelCurrentViewRight = new JLabel("Right:");
		this.textFieldCurrentViewRight = new SMFormattedTextField();
		this.labelCurrentViewBottom = new JLabel("Bottom:");
		this.textFieldCurrentViewBottom = new SMFormattedTextField();

		this.setPanel = new JPanel();
		this.wholeMapBoundsButton = new JButton("WholeMapBoundsButton");
		this.CurrentMapBoundsButton = new JButton("ViewMapBoundsButton");

		this.customBoundsComboBox = new JComboBox();
		// 通过渲染器，设置comboBox中的项显示在ComboBox中间
		this.customBoundsComboBox.setRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label=new JLabel();

				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			}
		});
		this.customBoundsComboBox.addItem(ControlsProperties.getString("String_DrewBounds"));
		this.customBoundsComboBox.addItem(ControlsProperties.getString("String_SelectObject"));

		this.copyButton = new JButton("CopyButton");
		this.pasteButton = new JButton("PasteButton");


	}

	private void initResource() {
		this.labelCurrentViewLeft.setText(ControlsProperties.getString("String_LabelLeft"));
		this.labelCurrentViewTop.setText(ControlsProperties.getString("String_LabelTop"));
		this.labelCurrentViewRight.setText(ControlsProperties.getString("String_LabelRight"));
		this.labelCurrentViewBottom.setText(ControlsProperties.getString("String_LabelBottom"));

		this.wholeMapBoundsButton.setText(ControlsProperties.getString("String_WholeMap"));
		this.CurrentMapBoundsButton.setText(ControlsProperties.getString("String_CurrentWindow"));
		this.copyButton.setText(CoreProperties.getString("String_CopySymbolOrGroup"));
		this.pasteButton.setText(CoreProperties.getString("String_PasteSymbolOrGroup"));
	}

	private void initLayout() {
		initMainPanelLayout();
		this.setLayout(new BorderLayout());
		this.add(this.mainPanel);
	}

	/**
	 * 输出范围面板左半部分：范围显示面板
	 */
	private void initMainPanelLayout() {

		this.mainPanel.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_MapOutputBounds")));
		GroupLayout viewPanelLayout = new GroupLayout(this.mainPanel);
		viewPanelLayout.setAutoCreateContainerGaps(true);
		viewPanelLayout.setAutoCreateGaps(true);
		this.mainPanel.setLayout(viewPanelLayout);

		// @formatter:off
		viewPanelLayout.setHorizontalGroup(viewPanelLayout.createSequentialGroup()
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelCurrentViewLeft, GroupLayout.PREFERRED_SIZE, DEFAULT_LABELSIZE, DEFAULT_LABELSIZE)
						.addComponent(this.labelCurrentViewTop, GroupLayout.PREFERRED_SIZE, DEFAULT_LABELSIZE, DEFAULT_LABELSIZE)
						.addComponent(this.labelCurrentViewRight, GroupLayout.PREFERRED_SIZE, DEFAULT_LABELSIZE, DEFAULT_LABELSIZE)
						.addComponent(this.labelCurrentViewBottom, GroupLayout.PREFERRED_SIZE, DEFAULT_LABELSIZE, DEFAULT_LABELSIZE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.textFieldCurrentViewLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldCurrentViewTop, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldCurrentViewRight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.textFieldCurrentViewBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.wholeMapBoundsButton, DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE,GroupLayout.PREFERRED_SIZE)
						.addComponent(this.CurrentMapBoundsButton, DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE,GroupLayout.PREFERRED_SIZE)
						.addComponent(this.customBoundsComboBox, DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.copyButton,DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.pasteButton, DEFAULT_BUTTONSIZE, DEFAULT_BUTTONSIZE, GroupLayout.PREFERRED_SIZE)));

		viewPanelLayout.setVerticalGroup(viewPanelLayout.createSequentialGroup()
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewLeft)
						.addComponent(this.textFieldCurrentViewLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.wholeMapBoundsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewTop)
						.addComponent(this.textFieldCurrentViewTop, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.CurrentMapBoundsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewRight)
						.addComponent(this.textFieldCurrentViewRight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.customBoundsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewBottom)
						.addComponent(this.textFieldCurrentViewBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.copyButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.pasteButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(5,5,Short.MAX_VALUE));

		// @formatter:on
	}

	private void initListeners() {
	}
}
