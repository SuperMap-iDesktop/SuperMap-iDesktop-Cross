package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.data.Geometry;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import static com.supermap.desktop.controls.ControlDefaultValues.*;

/**
 * @author YuanR
 *         地图输出为图片设置范围panel
 */
public class PanelGroupBoxViewBounds extends JPanel {

	public Rectangle2D rangeRectangle = Rectangle2D.getEMPTY();

	private MapControl mapControl;
	private Map map;

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

	private JButton customBoundsButton;
	private JPopupMenuBounds popupMenuCustomBounds;

	private SmDialog dialog;
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

	// 当前值
	private double valueLeft = 0.0;
	private double valueTop = 0.0;
	private double valueRight = 0.0;
	private double valueBottom = 0.0;

	private static final int DEFAULT_LABELSIZE = 20;
	private static final int DEFAULT_BUTTONSIZE = 95;

	private String borderName = ControlsProperties.getString("String_MapOutputBounds");

	/**
	 * 按钮事件枢纽站
	 */
	private ActionListener actionButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(mapViewBoundsButton)) {
				// 设置范围为整个地图范围
				setAsMapViewBounds();
				// 因为每个TextField的范围受其他约束，第一次设置是赋值，第二次设置为初始化值域范围
				setAsMapViewBounds();
			} else if (e.getSource().equals(currentViewBoundsButton)) {
				// 设置范围为当前窗口
				setAsCurrentViewBounds();
				// 因为每个TextField的范围受其他约束，第一次设置是赋值，第二次设置为初始化值域范围
				setAsCurrentViewBounds();
			} else if (e.getSource().equals(customBoundsButton)) {
				// 当点击自定义按钮时，show出自定义右键菜单栏
				popupMenuCustomBounds.show(customBoundsButton, 0, customBoundsButton.getHeight());
			} else if (e.getSource().equals(copyButton)) {
				// 复制
				copyParameter();

			} else if (e.getSource().equals(pasteButton)) {
				// 粘贴
				pasteParameter();
				// 因为每个TextField的范围受其他约束，第一次设置是赋值，第二次设置为初始化值域范围
				pasteParameter();
			}
		}
	};

	/**
	 * 当自定义范围结束，获得绘制的矩形范围/选择对象的最小矩形框
	 */
	private transient PropertyChangeListener boundsPropertyChangeListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(JPopupMenuBounds.CUSTOM_BOUNDS)) {
				// 通过给予的矩形框，设置范围文本框的数值
				setAsRectangleBounds(popupMenuCustomBounds.getRectangle2D());
				// 因为每个TextField的范围受其他约束，第一次设置是赋值，第二次设置为初始化值域范围
				setAsRectangleBounds(popupMenuCustomBounds.getRectangle2D());
			}
		}
	};


	private CaretListener textFieldLeftCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldLValueChange();
		}
	};
	private CaretListener textFieldTopCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldTValueChange();
		}
	};
	private CaretListener textFieldRightCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldRValueChange();
		}
	};
	private CaretListener textFieldButtomCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldBValueChange();
		}
	};

	public PanelGroupBoxViewBounds(SmDialog smDialog) {
		super();
		this.dialog = smDialog;
		init();
	}

	public PanelGroupBoxViewBounds(SmDialog smDialog, String borderName) {
		super();
		this.dialog = smDialog;
		this.borderName = borderName;
		init();

	}

	public PanelGroupBoxViewBounds(SmDialog smDialog, String borderName, Map inputMap) {
		super();
		this.dialog = smDialog;
		this.borderName = borderName;
		this.map=inputMap;
		init();

	}

	public void init(){
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
		this.textFieldCurrentViewLeft = new WaringTextField(true);
		this.labelCurrentViewBottom = new JLabel("Bottom:");
		this.textFieldCurrentViewBottom = new WaringTextField(true);
		this.labelCurrentViewRight = new JLabel("Right:");
		this.textFieldCurrentViewRight = new WaringTextField(true);
		this.labelCurrentViewTop = new JLabel("Top:");
		this.textFieldCurrentViewTop = new WaringTextField(true);

		this.mapViewBoundsButton = new JButton("WholeMapBoundsButton");
		this.currentViewBoundsButton = new JButton("ViewMapBoundsButton");

		this.customBoundsButton = new JButton("CustomBounds");
		// 初始化自定义范围下拉列表对象
		this.popupMenuCustomBounds = new JPopupMenuBounds(dialog, JPopupMenuBounds.CUSTOM_BOUNDS, Rectangle2D.getEMPTY());
		// 通过字符串列表，控制popupMenuCustomBounds显示的项
		ArrayList arrayList = new ArrayList();
		arrayList.add(MapViewProperties.getString("String_Button_SelectObject"));
		arrayList.add(MapViewProperties.getString("String_Button_DrawRectangle"));
		this.popupMenuCustomBounds.setPopupItems(arrayList);

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
		this.customBoundsButton.setText(ControlsProperties.getString("String_CustomBounds"));
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

		this.mainPanel.setBorder(BorderFactory.createTitledBorder(borderName));
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
						.addComponent(this.customBoundsButton, DEFAULT_BUTTONSIZE,  DEFAULT_BUTTONSIZE, GroupLayout.PREFERRED_SIZE)
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
						.addComponent(this.customBoundsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
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
		this.copyButton.addActionListener(actionButtonListener);
		this.pasteButton.addActionListener(actionButtonListener);
		this.customBoundsButton.addActionListener(actionButtonListener);
		registTextFieldEvents();
		this.popupMenuCustomBounds.addPropertyChangeListeners(boundsPropertyChangeListener);
	}

	private void removeEvents() {
		this.mapViewBoundsButton.removeActionListener(actionButtonListener);
		this.currentViewBoundsButton.removeActionListener(actionButtonListener);
		this.copyButton.removeActionListener(actionButtonListener);
		this.pasteButton.removeActionListener(actionButtonListener);
		this.customBoundsButton.removeActionListener(actionButtonListener);
		removeTextFieldEvents();
		this.popupMenuCustomBounds.removePropertyChangeListener(boundsPropertyChangeListener);
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
	 * 初始化地图范围的值
	 */
	private void initValue() {
		this.mapControl = MapUtilities.getMapControl();
		if (this.mapControl!=null) {
			this.mapViewL = this.mapControl.getMap().getBounds().getLeft();
			this.mapViewT = this.mapControl.getMap().getBounds().getTop();
			this.mapViewR = this.mapControl.getMap().getBounds().getRight();
			this.mapViewB = this.mapControl.getMap().getBounds().getBottom();
		}else if (this.map!=null){
			this.mapViewL = this.map.getBounds().getLeft();
			this.mapViewT = this.map.getBounds().getTop();
			this.mapViewR = this.map.getBounds().getRight();
			this.mapViewB = this.map.getBounds().getBottom();
			this.customBoundsButton.setEnabled(false);
			this.currentViewBoundsButton.setEnabled(false);
		}
		setAsMapViewBounds();
	}

	/**
	 * 设置范围为地图范围
	 */
	private void setAsMapViewBounds() {

		String mapLeft = DoubleUtilities.getFormatString(this.mapViewL);
		String mapBottom = DoubleUtilities.getFormatString(this.mapViewB);
		String mapRight = DoubleUtilities.getFormatString(this.mapViewR);
		String mapTop = DoubleUtilities.getFormatString(this.mapViewT);

		this.textFieldCurrentViewLeft.getTextField().setText(mapLeft);
		this.textFieldCurrentViewBottom.getTextField().setText(mapBottom);
		this.textFieldCurrentViewRight.getTextField().setText(mapRight);
		this.textFieldCurrentViewTop.getTextField().setText(mapTop);
	}

	/**
	 * 设置范围为当前可视范围
	 */
	private void setAsCurrentViewBounds() {
		// 当前视图范围可改变，每次设置之前都需要重新取值，并且做去除千分位的处理
		this.currentViewLeft = this.mapControl.getMap().getViewBounds().getLeft();
		this.currentViewBottom = this.mapControl.getMap().getViewBounds().getBottom();
		this.currentViewRight = this.mapControl.getMap().getViewBounds().getRight();
		this.currentViewTop = this.mapControl.getMap().getViewBounds().getTop();

		this.textFieldCurrentViewLeft.getTextField().setText(DoubleUtilities.getFormatString(this.currentViewLeft));
		this.textFieldCurrentViewBottom.getTextField().setText(DoubleUtilities.getFormatString(this.currentViewBottom));
		this.textFieldCurrentViewRight.getTextField().setText(DoubleUtilities.getFormatString(this.currentViewRight));
		this.textFieldCurrentViewTop.getTextField().setText(DoubleUtilities.getFormatString(this.currentViewTop));
	}

	/**
	 * 设置范围为给予的矩形框范围
	 */
	private void setAsRectangleBounds(Rectangle2D rectangleBounds) {

		this.textFieldCurrentViewLeft.getTextField().setText(DoubleUtilities.getFormatString(rectangleBounds.getLeft()));
		this.textFieldCurrentViewBottom.getTextField().setText(DoubleUtilities.getFormatString(rectangleBounds.getBottom()));
		this.textFieldCurrentViewRight.getTextField().setText(DoubleUtilities.getFormatString(rectangleBounds.getRight()));
		this.textFieldCurrentViewTop.getTextField().setText(DoubleUtilities.getFormatString(rectangleBounds.getTop()));
	}


	/**
	 * 范围文本框——左——改变事件
	 */
	private void textFieldLValueChange() {
		// 当左范围文本框改变时，设置右范围文本框的限制范围
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
		// 当上范围文本框改变时，设置下范围文本框的限制范围
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
	 * 范围文本框——下——改变事件
	 */
	private void textFieldBValueChange() {
		// 当下范围文本框改变时，设置上范围文本框的限制范围
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
	 * 范围文本框——右——改变事件
	 */
	private void textFieldRValueChange() {
		// 当右范围文本框改变时，设置左范围文本框的限制范围
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
	 * 复制参数
	 */
	private void copyParameter() {
		ControlDefaultValues.setCopyCurrentMapboundsLeft(DoubleUtilities.stringToValue(this.textFieldCurrentViewLeft.getTextField().getText()));
		ControlDefaultValues.setCopyCurrentMapboundsBottom(DoubleUtilities.stringToValue(this.textFieldCurrentViewBottom.getTextField().getText()));
		ControlDefaultValues.setCopyCurrentMapboundsRight(DoubleUtilities.stringToValue(this.textFieldCurrentViewRight.getTextField().getText()));
		ControlDefaultValues.setCopyCurrentMapboundsTop(DoubleUtilities.stringToValue(this.textFieldCurrentViewTop.getTextField().getText()));

		String clipBoardTextLeft = ControlsProperties.getString("String_LabelLeft") + this.textFieldCurrentViewLeft.getTextField().getText().replace(",", "");
		String clipBoardTextBottom = ControlsProperties.getString("String_LabelBottom") + this.textFieldCurrentViewBottom.getTextField().getText().replace(",", "");
		String clipBoardTextRight = ControlsProperties.getString("String_LabelRight") + this.textFieldCurrentViewRight.getTextField().getText().replace(",", "");
		String clipBoardTextTop = ControlsProperties.getString("String_LabelTop") + this.textFieldCurrentViewTop.getTextField().getText().replace(",", "");
		// 调用windows复制功能，将值复制到剪贴板
		setSysClipboardText(clipBoardTextLeft + "," + clipBoardTextBottom + "," + clipBoardTextRight + "," + clipBoardTextTop);
		Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_MapBounds_Has_Been_Copied"));
	}

	/**
	 * 调用windows的剪贴板
	 * yuanR
	 *
	 * @param coypText
	 */
	public static void setSysClipboardText(String coypText) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable Text = new StringSelection(coypText);
		clip.setContents(Text, null);
	}


	/**
	 * 调用windows的剪贴板
	 * 获得系统剪贴板内
	 * yuanR 2017.3.24
	 *
	 * @return
	 */
	public static String getSysClipboardText() {
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipTf = sysClip.getContents(null);
		if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				String ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * 粘贴参数
	 * 从系统粘贴板获得最新的文本，当文本内容符合范围条件时，才进行粘贴，当不符合时，不做任何操作
	 */
	private void pasteParameter() {
		String clipBoard = getSysClipboardText();
		// 判断剪切板中是否包含“左：”“下：”“右：”“上：”等“分隔符”
		String left = ControlsProperties.getString("String_LabelLeft");
		String bottom = ControlsProperties.getString("String_LabelBottom");
		String right = ControlsProperties.getString("String_LabelRight");
		String top = ControlsProperties.getString("String_LabelTop");

		if (clipBoard.contains(left) && clipBoard.contains(bottom) && clipBoard.contains(right) && clipBoard.contains(top)) {
			String clipBoardLeft = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_LabelLeft")), clipBoard.indexOf(ControlsProperties.getString("String_LabelBottom")));
			String clipBoardBottom = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_LabelBottom")), clipBoard.indexOf(ControlsProperties.getString("String_LabelRight")));
			String clipBoardRight = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_LabelRight")), clipBoard.indexOf(ControlsProperties.getString("String_LabelTop")));
			String clipBoardTop = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_LabelTop")));

			clipBoardLeft = (clipBoardLeft.replace(left, "")).replace(",", "");
			clipBoardBottom = (clipBoardBottom.replace(bottom, "")).replace(",", "");
			clipBoardRight = (clipBoardRight.replace(right, "")).replace(",", "");
			clipBoardTop = (clipBoardTop.replace(top, "")).replace(",", "");

			if (DoubleUtilities.isDouble(clipBoardLeft) && DoubleUtilities.isDouble(clipBoardBottom) && DoubleUtilities.isDouble(clipBoardRight) && DoubleUtilities.isDouble(clipBoardTop)) {
				// 获得系统粘贴板内的数字符合规范，设置为千分位，存入桌面
				ControlDefaultValues.setCopyCurrentMapboundsLeft(DoubleUtilities.stringToValue(clipBoardLeft));
				ControlDefaultValues.setCopyCurrentMapboundsBottom(DoubleUtilities.stringToValue(clipBoardBottom));
				ControlDefaultValues.setCopyCurrentMapboundsRight(DoubleUtilities.stringToValue(clipBoardRight));
				ControlDefaultValues.setCopyCurrentMapboundsTop(DoubleUtilities.stringToValue(clipBoardTop));

				String mapLeft = DoubleUtilities.getFormatString(getCopyCurrentMapboundsLeft());
				String mapBottom = DoubleUtilities.getFormatString(getCopyCurrentMapboundsBottom());
				String mapRight = DoubleUtilities.getFormatString(getCopyCurrentMapboundsRight());
				String mapTop = DoubleUtilities.getFormatString(getCopyCurrentMapboundsTop());

				this.textFieldCurrentViewLeft.getTextField().setText(mapLeft);
				this.textFieldCurrentViewBottom.getTextField().setText(mapBottom);
				this.textFieldCurrentViewRight.getTextField().setText(mapRight);
				this.textFieldCurrentViewTop.getTextField().setText(mapTop);
			}
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
			this.rangeRectangle.setLeft(valueLeft);
			this.rangeRectangle.setRight(valueRight);
			this.rangeRectangle.setTop(valueTop);
			this.rangeRectangle.setBottom(valueBottom);
			return this.rangeRectangle;
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

	public JButton getCopyButton() {
		return copyButton;
	}

	public java.util.Map<Layer, java.util.List<Geometry>> getSelectedGeometryAndLayer(){
		return this.popupMenuCustomBounds.getSelectedGeometryAndLayer();
	}
}
