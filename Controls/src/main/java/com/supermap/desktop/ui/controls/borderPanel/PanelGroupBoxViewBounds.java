package com.supermap.desktop.ui.controls.borderPanel;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author YuanR
 *         地图输出为图片设置范围panel
 */
public class PanelGroupBoxViewBounds extends JPanel {

	public Rectangle2D rectangle2D;

	private MapControl mapControl;

	private JLabel labelCurrentViewLeft;
	public WaringTextField textFieldCurrentViewLeft;
	private JLabel labelCurrentViewBottom;
	public WaringTextField textFieldCurrentViewBottom;
	private JLabel labelCurrentViewRight;
	public WaringTextField textFieldCurrentViewRight;
	private JLabel labelCurrentViewTop;
	public WaringTextField textFieldCurrentViewTop;

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

	//	// 当前值
	private double valueLeft = 0.0;
	private double valueTop = 0.0;
	private double valueRight = 0.0;
	private double valueBottom = 0.0;


	private static final int DEFAULT_LABELSIZE = 20;
	private static final int DEFAULT_BUTTONSIZE = 85;

	/**
	 * 默认构造方法
	 */
	public PanelGroupBoxViewBounds() {
		super();
		initCompont();
		initResource();
		initLayout();
		// 先初始化，然后添加监听，然后再设置一遍范围
		// 目的：文本框的值域需要其他文本框中值作为参考，为保证初始化时每个文本框都设置值域，先初始化文本框中的值，再触发一次监听事件完成文本框的初始化
		initValue();
		registEvents();
		setAsMapViewBounds();
	}

	private void initCompont() {


		this.mainPanel = new JPanel();

		this.labelCurrentViewLeft = new JLabel("Left:");
		this.textFieldCurrentViewLeft = new WaringTextField();
		this.labelCurrentViewBottom = new JLabel("Bottom:");
		this.textFieldCurrentViewBottom = new WaringTextField();
		this.labelCurrentViewRight = new JLabel("Right:");
		this.textFieldCurrentViewRight = new WaringTextField();
		this.labelCurrentViewTop = new JLabel("Top:");
		this.textFieldCurrentViewTop = new WaringTextField();

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
		this.labelCurrentViewBottom.setText(ControlsProperties.getString("String_LabelBottom"));
		this.labelCurrentViewRight.setText(ControlsProperties.getString("String_LabelRight"));
		this.labelCurrentViewTop.setText(ControlsProperties.getString("String_LabelTop"));

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
						.addComponent(this.labelCurrentViewBottom)
						.addComponent(this.textFieldCurrentViewBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.currentViewBoundsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewRight)
						.addComponent(this.textFieldCurrentViewRight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.customBoundsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(viewPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewTop)
						.addComponent(this.textFieldCurrentViewTop, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
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
		// 将文本框的监听事件的注册放到外面，当“整幅地图”、“当前窗口”时，去除对文本框的监听
		registTextFieldEvents();
	}

	private void removeEvents() {
		this.mapViewBoundsButton.removeActionListener(actionButtonListener);
		this.currentViewBoundsButton.removeActionListener(actionButtonListener);
		removeTextFieldEvents();
	}

	private void registTextFieldEvents() {
		this.textFieldCurrentViewLeft.getTextField().addCaretListener(textFieldLeftCaretListener);
		this.textFieldCurrentViewBottom.getTextField().addCaretListener(textFieldButtomCaretListener);
		this.textFieldCurrentViewRight.getTextField().addCaretListener(textFieldRightCaretListener);
		this.textFieldCurrentViewTop.getTextField().addCaretListener(textFieldTopCaretListener);

		this.textFieldCurrentViewLeft.registEvents();
		this.textFieldCurrentViewBottom.registEvents();
		this.textFieldCurrentViewRight.registEvents();
		this.textFieldCurrentViewTop.registEvents();
	}


	private void removeTextFieldEvents() {
		this.textFieldCurrentViewLeft.getTextField().removeCaretListener(textFieldLeftCaretListener);
		this.textFieldCurrentViewBottom.getTextField().removeCaretListener(textFieldButtomCaretListener);
		this.textFieldCurrentViewRight.getTextField().removeCaretListener(textFieldRightCaretListener);
		this.textFieldCurrentViewTop.getTextField().removeCaretListener(textFieldTopCaretListener);

		this.textFieldCurrentViewLeft.removeEvents();
		this.textFieldCurrentViewBottom.removeEvents();
		this.textFieldCurrentViewRight.removeEvents();
		this.textFieldCurrentViewTop.removeEvents();
	}


	/**
	 * 按钮事件枢纽站
	 */
	private ActionListener actionButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(mapViewBoundsButton)) {
				// 设置范围为整个地图范围
				setAsMapViewBounds();
				setAsMapViewBounds();
			} else if (e.getSource().equals(currentViewBoundsButton)) {
				// 设置范围为当前窗口
				setAsCurrentViewBounds();
				setAsCurrentViewBounds();
			} else if (e.getSource().equals(copyButton)) {
				// 复制
				copyParameter();

			} else if (e.getSource().equals(pasteButton)) {
				// 粘贴
				pasteParameter();
			}
		}
	};

	private CaretListener textFieldLeftCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldLValueChange();
			// 当文本框中的值改变时，设置当前值为其对应文本框中的值
//			setCurrentBoundVaule();
		}
	};
	private CaretListener textFieldTopCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldTValueChange();
			// 当文本框中的值改变时，设置当前值为其对应文本框中的值
//			setCurrentBoundVaule();
		}
	};
	private CaretListener textFieldRightCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldRValueChange();
			// 当文本框中的值改变时，设置当前值为其对应文本框中的值
//			setCurrentBoundVaule();
		}
	};
	private CaretListener textFieldButtomCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldBValueChange();
			// 当文本框中的值改变时，设置当前值为其对应文本框中的值
//			setCurrentBoundVaule();
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
		this.textFieldCurrentViewLeft.getTextField().setText(DoubleUtilities.getFormatString(this.mapViewL));
		this.textFieldCurrentViewBottom.getTextField().setText(DoubleUtilities.getFormatString(this.mapViewB));
		this.textFieldCurrentViewRight.getTextField().setText(DoubleUtilities.getFormatString(this.mapViewR));
		this.textFieldCurrentViewTop.getTextField().setText(DoubleUtilities.getFormatString(this.mapViewT));
	}

	/**
	 * 设置范围为当前可视范围
	 */
	private void setAsCurrentViewBounds() {
		this.textFieldCurrentViewLeft.getTextField().setText(DoubleUtilities.getFormatString(this.currentViewLeft));
		this.textFieldCurrentViewBottom.getTextField().setText(DoubleUtilities.getFormatString(this.currentViewBottom));
		this.textFieldCurrentViewRight.getTextField().setText(DoubleUtilities.getFormatString(this.currentViewRight));
		this.textFieldCurrentViewTop.getTextField().setText(DoubleUtilities.getFormatString(this.currentViewTop));
	}

	/**
	 * 复制参数
	 */
	private void copyParameter() {

	}

	/**
	 * 粘贴参数
	 */
	private void pasteParameter() {

	}

	/**
	 * 范围文本框——左——改变事件
	 */
	private void textFieldLValueChange() {
		String str = this.textFieldCurrentViewRight.getTextField().getText();
		if (!StringUtilities.isNullOrEmpty(str) && DoubleUtilities.isDouble(str)) {
			this.textFieldCurrentViewLeft.setInitInfo(
					(-Double.MAX_VALUE),
					DoubleUtilities.stringToValue(textFieldCurrentViewRight.getTextField().getText()),
					WaringTextField.FLOAT_TYPE,
					"15");
		} else {
			this.textFieldCurrentViewLeft.setInitInfo(
					(-Double.MAX_VALUE),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"15");
		}
	}

	/**
	 * 范围文本框——下——改变事件
	 */
	private void textFieldBValueChange() {
		String str = this.textFieldCurrentViewTop.getTextField().getText();
		if (!StringUtilities.isNullOrEmpty(str) && DoubleUtilities.isDouble(str)) {
			this.textFieldCurrentViewBottom.setInitInfo(
					(-Double.MAX_VALUE),
					DoubleUtilities.stringToValue(this.textFieldCurrentViewTop.getTextField().getText()),
					WaringTextField.FLOAT_TYPE,
					"15");
		} else {
			this.textFieldCurrentViewBottom.setInitInfo(
					(-Double.MAX_VALUE),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"15");
		}
	}

	/**
	 * 范围文本框——右——改变事件
	 */
	private void textFieldRValueChange() {
		String str = this.textFieldCurrentViewLeft.getTextField().getText();
		if (!StringUtilities.isNullOrEmpty(str) && DoubleUtilities.isDouble(str)) {
			this.textFieldCurrentViewRight.setInitInfo(
					DoubleUtilities.stringToValue(this.textFieldCurrentViewLeft.getTextField().getText()),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"15");
		} else {
			this.textFieldCurrentViewRight.setInitInfo(
					(-Double.MAX_VALUE),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"15");
		}
	}


	/**
	 * 范围文本框——上——改变事件
	 */
	private void textFieldTValueChange() {
		String str = this.textFieldCurrentViewBottom.getTextField().getText();
		if (!StringUtilities.isNullOrEmpty(str) && DoubleUtilities.isDouble(str)) {
			this.textFieldCurrentViewTop.setInitInfo(
					DoubleUtilities.stringToValue(this.textFieldCurrentViewBottom.getTextField().getText()),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"15");
		} else {
			this.textFieldCurrentViewTop.setInitInfo(
					(-Double.MAX_VALUE),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"15");

		}
	}

	/**
	 * 设置当前
	 */
	private void setCurrentBoundVaule() {
		String valueLeft = this.textFieldCurrentViewLeft.getTextField().getText();
		String valueTop = this.textFieldCurrentViewTop.getTextField().getText();
		String valueRight = this.textFieldCurrentViewRight.getTextField().getText();
		String valueBottom = this.textFieldCurrentViewBottom.getTextField().getText();

		// 当四个文本框都不为空时
		if (!StringUtilities.isNullOrEmpty(valueLeft) && !StringUtilities.isNullOrEmpty(valueTop) && !StringUtilities.isNullOrEmpty(valueRight) && !StringUtilities.isNullOrEmpty(valueBottom)) {
			// 当四个文本框参数都为double型时
			if (DoubleUtilities.isDouble(valueLeft) && DoubleUtilities.isDouble(valueTop) && DoubleUtilities.isDouble(valueRight) && DoubleUtilities.isDouble(valueBottom)) {
				this.valueLeft = DoubleUtilities.stringToValue(valueLeft);
				this.valueTop = DoubleUtilities.stringToValue(valueTop);
				this.valueRight = DoubleUtilities.stringToValue(valueRight);
				this.valueBottom = DoubleUtilities.stringToValue(valueBottom);
				return;
			}
		}
		this.valueLeft = 0.0;
		this.valueTop = 0.0;
		this.valueRight = 0.0;
		this.valueBottom = 0.0;
	}

	/**
	 * 获得当前面板设置参数下的地图范围
	 *
	 * @return
	 */
	public Rectangle2D getRangeBound() {
		// 当要获得范围矩形框时，获得当前文本框的值，并判断是否构成矩形框
		setCurrentBoundVaule();
		if ((valueLeft < valueRight) && (valueTop > valueBottom)) {
			this.rectangle2D = new Rectangle2D();
			this.rectangle2D.setLeft(valueLeft);
			this.rectangle2D.setRight(valueRight);
			this.rectangle2D.setTop(valueTop);
			this.rectangle2D.setBottom(valueBottom);
			return this.rectangle2D;
		}
		return null;
	}

	// 获得范围文本框
	public WaringTextField getTextFieldCurrentViewLeft() {
		return this.textFieldCurrentViewLeft;
	}

	public WaringTextField getTextFieldCurrentViewTop() {
		return this.textFieldCurrentViewTop;
	}

	public WaringTextField getTextFieldCurrentViewRight() {
		return this.textFieldCurrentViewRight;
	}

	public WaringTextField getTextFieldCurrentViewBottom() {
		return this.textFieldCurrentViewBottom;
	}
}
