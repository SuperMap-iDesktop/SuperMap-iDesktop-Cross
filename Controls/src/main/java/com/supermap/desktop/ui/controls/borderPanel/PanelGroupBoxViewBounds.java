package com.supermap.desktop.ui.controls.borderPanel;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author YuanR
 *         当前视图范围panel
 */
public class PanelGroupBoxViewBounds extends JPanel {
	private MapControl mapControl;
	private JLabel labelCurrentViewLeft;
	private SMFormattedTextField textFieldCurrentViewLeft;
	private JLabel labelCurrentViewTop;
	private SMFormattedTextField textFieldCurrentViewTop;
	private JLabel labelCurrentViewRight;
	private SMFormattedTextField textFieldCurrentViewRight;
	private JLabel labelCurrentViewBottom;
	private SMFormattedTextField textFieldCurrentViewBottom;

	private JPanel mainPanel;

	// 整幅地图范围
	private JButton mapViewBoundsButton;
	// 当前窗口地图范围
	private JButton currentViewBoundsButton;
	// 自定义范围：选择对象范围/绘制范围
	private JComboBox customBoundsComboBox;

	// 复制
	private JButton copyButton;
	// 粘贴
	private JButton pasteButton;

	private double mapViewL = 0.0;
	private double mapViewT = 0.0;
	private double mapViewR = 0.0;
	private double mapViewB = 0.0;

	private double currentViewLeft = 0.0;
	private double currentViewTop = 0.0;
	private double currentViewRight = 0.0;
	private double currentViewBottom = 0.0;

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
		registEvents();
		initValue();
	}

	private void initCompont() {

		this.mainPanel = new JPanel();

		this.labelCurrentViewLeft = new JLabel("Left:");
		this.textFieldCurrentViewLeft = new SMFormattedTextField();
		this.labelCurrentViewTop = new JLabel("Top:");
		this.textFieldCurrentViewTop = new SMFormattedTextField();
		this.labelCurrentViewRight = new JLabel("Right:");
		this.textFieldCurrentViewRight = new SMFormattedTextField();
		this.labelCurrentViewBottom = new JLabel("Bottom:");
		this.textFieldCurrentViewBottom = new SMFormattedTextField();

		this.mapViewBoundsButton = new JButton("WholeMapBoundsButton");
		this.currentViewBoundsButton = new JButton("ViewMapBoundsButton");

		this.customBoundsComboBox = new JComboBox();
		// 通过渲染器，设置comboBox中的项显示在ComboBox中间
		this.customBoundsComboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
				return component;
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

		this.mapViewBoundsButton.setText(ControlsProperties.getString("String_MapView"));
		this.currentViewBoundsButton.setText(ControlsProperties.getString("String_CurrentView"));
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
						.addComponent(this.mapViewBoundsButton, DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE,GroupLayout.PREFERRED_SIZE)
						.addComponent(this.currentViewBoundsButton, DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE,GroupLayout.PREFERRED_SIZE)
						.addComponent(this.customBoundsComboBox, DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.copyButton,DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.pasteButton, DEFAULT_BUTTONSIZE, DEFAULT_BUTTONSIZE, GroupLayout.PREFERRED_SIZE)));

		viewPanelLayout.setVerticalGroup(viewPanelLayout.createSequentialGroup()
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewLeft)
						.addComponent(this.textFieldCurrentViewLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.mapViewBoundsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewTop)
						.addComponent(this.textFieldCurrentViewTop, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.currentViewBoundsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
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

	private void registEvents() {
		removeEvents();
		this.mapViewBoundsButton.addActionListener(actionButtonListener);
		this.currentViewBoundsButton.addActionListener(actionButtonListener);
	}

	private void removeEvents() {
		this.mapViewBoundsButton.removeActionListener(actionButtonListener);
		this.currentViewBoundsButton.removeActionListener(actionButtonListener);
	}

	/**
	 * 按钮事件枢纽站
	 */
	private ActionListener actionButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(mapViewBoundsButton)) {
				setAsMapViewBounds();
			} else if (e.getSource().equals(currentViewBoundsButton)) {
				setAsCurrentViewBounds();
			}
		}
	};

	/**
	 * 初始化地图范围的值
	 */
	private void initValue() {
		this.mapControl = MapUtilities.getMapControl();
		this.mapViewL = this.mapControl.getMap().getBounds().getLeft();
		this.mapViewT = this.mapControl.getMap().getBounds().getTop();
		this.mapViewR = this.mapControl.getMap().getBounds().getRight();
		this.mapViewB = this.mapControl.getMap().getBounds().getBottom();

		this.currentViewLeft = this.mapControl.getMap().getViewBounds().getLeft();
		this.currentViewTop = this.mapControl.getMap().getViewBounds().getTop();
		this.currentViewRight = this.mapControl.getMap().getViewBounds().getRight();
		this.currentViewBottom = this.mapControl.getMap().getViewBounds().getBottom();

		setAsMapViewBounds();
	}

	/**
	 * 设置范围为地图范围
	 */
	private void setAsMapViewBounds() {
		this.textFieldCurrentViewLeft.setValue(this.mapViewL);
		this.textFieldCurrentViewTop.setValue(this.mapViewT);
		this.textFieldCurrentViewRight.setValue(this.mapViewR);
		this.textFieldCurrentViewBottom.setValue(this.mapViewB);
	}

	/**
	 * 设置范围为当前可视范围
	 */
	private void setAsCurrentViewBounds() {
		this.textFieldCurrentViewLeft.setValue(this.currentViewLeft);
		this.textFieldCurrentViewTop.setValue(this.currentViewTop);
		this.textFieldCurrentViewRight.setValue(this.currentViewRight);
		this.textFieldCurrentViewBottom.setValue(this.currentViewBottom);
	}
}
