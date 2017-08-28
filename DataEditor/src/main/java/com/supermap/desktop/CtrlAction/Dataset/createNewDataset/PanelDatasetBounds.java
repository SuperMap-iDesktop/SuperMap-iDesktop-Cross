package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.utilities.ClipBoardUtilties;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

/**
 * Created by yuanR on 2017/8/15 0015.
 * 新建影像数据-数据集范围
 */
public class PanelDatasetBounds extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel labelCurrentViewLeft;

	public WaringTextField getTextFieldCurrentViewLeft() {
		return textFieldCurrentViewLeft;
	}

	public WaringTextField getTextFieldCurrentViewBottom() {
		return textFieldCurrentViewBottom;
	}

	public WaringTextField getTextFieldCurrentViewRight() {
		return textFieldCurrentViewRight;
	}

	public WaringTextField getTextFieldCurrentViewTop() {
		return textFieldCurrentViewTop;
	}

	public WaringTextField textFieldCurrentViewLeft;
	private JLabel labelCurrentViewBottom;
	public WaringTextField textFieldCurrentViewBottom;
	private JLabel labelCurrentViewRight;
	public WaringTextField textFieldCurrentViewRight;
	private JLabel labelCurrentViewTop;
	public WaringTextField textFieldCurrentViewTop;

	// 整幅地图范围
	private JButton mapViewBoundsButton;
	// 当前窗口地图范围
	private JButton currentViewBoundsButton;
	// 复制
	private JButton copyButton;
	// 粘贴
	private JButton pasteButton;

	public Rectangle2D rangeRectangle = Rectangle2D.getEMPTY();

	private MapControl mapControl;

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

	private static final int DEFULT_RECTANGLE_VALUE = 200;


	/**
	 * 按钮事件枢纽站
	 */
	private ActionListener actionButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(mapViewBoundsButton)) {
				// 设置范围为整个地图范围
				setAsMapViewBounds();
			} else if (e.getSource().equals(currentViewBoundsButton)) {
				// 设置范围为当前窗口
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
	 * 文本框获得、失去焦点监听
	 * 用于实现对于千分位的处理，当编辑时去除千分位，结束编辑时获得千分位
	 */
	private FocusListener textFieldFocusListener = new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			// 当获得焦点时，将文本框中数字做去除千分位处理
			if (e.getSource().equals(textFieldCurrentViewLeft.getTextField())) {
				String temp = textFieldCurrentViewLeft.getTextField().getText();
				temp = temp.replace(",", "");
				textFieldCurrentViewLeft.getTextField().setText(temp);
			} else if (e.getSource().equals(textFieldCurrentViewBottom.getTextField())) {
				String temp = textFieldCurrentViewBottom.getTextField().getText();
				temp = temp.replace(",", "");
				textFieldCurrentViewBottom.getTextField().setText(temp);
			} else if (e.getSource().equals(textFieldCurrentViewRight.getTextField())) {
				String temp = textFieldCurrentViewRight.getTextField().getText();
				temp = temp.replace(",", "");
				textFieldCurrentViewRight.getTextField().setText(temp);
			} else if (e.getSource().equals(textFieldCurrentViewTop.getTextField())) {
				String temp = textFieldCurrentViewTop.getTextField().getText();
				temp = temp.replace(",", "");
				textFieldCurrentViewTop.getTextField().setText(temp);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			// 当失去焦点时，将文本框中数字设置千分位
			if (!textFieldCurrentViewLeft.getTextField().getText().isEmpty() && e.getSource().equals(textFieldCurrentViewLeft.getTextField())) {
				String temp = textFieldCurrentViewLeft.getTextField().getText();
				temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(temp));
				textFieldCurrentViewLeft.getTextField().setText(temp);
			} else if (!textFieldCurrentViewBottom.getTextField().getText().isEmpty() && e.getSource().equals(textFieldCurrentViewBottom.getTextField())) {
				String temp = textFieldCurrentViewBottom.getTextField().getText();
				temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(temp));
				textFieldCurrentViewBottom.getTextField().setText(temp);
			} else if (!textFieldCurrentViewRight.getTextField().getText().isEmpty() && e.getSource().equals(textFieldCurrentViewRight.getTextField())) {
				String temp = textFieldCurrentViewRight.getTextField().getText();
				temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(temp));
				textFieldCurrentViewRight.getTextField().setText(temp);
			} else if (!textFieldCurrentViewTop.getTextField().getText().isEmpty() && e.getSource().equals(textFieldCurrentViewTop.getTextField())) {
				String temp = textFieldCurrentViewTop.getTextField().getText();
				temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(temp));
				textFieldCurrentViewTop.getTextField().setText(temp);
			}
		}
	};

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
	public void setAsCurrentViewBounds() {

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
	 * 复制参数
	 */
	private void copyParameter() {
		ClipBoardUtilties.setBounds(new Rectangle2D(
				DoubleUtilities.stringToValue(this.textFieldCurrentViewLeft.getTextField().getText()),
				DoubleUtilities.stringToValue(this.textFieldCurrentViewBottom.getTextField().getText()),
				DoubleUtilities.stringToValue(this.textFieldCurrentViewRight.getTextField().getText()),
				DoubleUtilities.stringToValue(this.textFieldCurrentViewTop.getTextField().getText())));
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
	 * yuanR 2017.8.15
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
		Rectangle2D bounds = ClipBoardUtilties.getBounds();
		if (bounds != null) {
			this.textFieldCurrentViewLeft.getTextField().setText(DoubleUtilities.getFormatString(bounds.getLeft()));
			this.textFieldCurrentViewBottom.getTextField().setText(DoubleUtilities.getFormatString(bounds.getBottom()));
			this.textFieldCurrentViewRight.getTextField().setText(DoubleUtilities.getFormatString(bounds.getRight()));
			this.textFieldCurrentViewTop.getTextField().setText(DoubleUtilities.getFormatString(bounds.getTop()));
		}
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
		// 当无法构成矩形时，给于默认值矩形
//		Rectangle2D rectangle2D = new Rectangle2D(-DEFULT_RECTANGLE_VALUE, -DEFULT_RECTANGLE_VALUE,
//				DEFULT_RECTANGLE_VALUE, DEFULT_RECTANGLE_VALUE);
//		return rectangle2D;
		return null;
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


	public PanelDatasetBounds() {
		initComponents();
		initLayout();
		registerEvent();
	}

	private void initComponents() {

		this.labelCurrentViewLeft = new JLabel(ControlsProperties.getString("String_LabelLeft"));
		this.textFieldCurrentViewLeft = new WaringTextField(true);
		this.labelCurrentViewBottom = new JLabel(ControlsProperties.getString("String_LabelBottom"));
		this.textFieldCurrentViewBottom = new WaringTextField(true);
		this.labelCurrentViewRight = new JLabel(ControlsProperties.getString("String_LabelRight"));
		this.textFieldCurrentViewRight = new WaringTextField(true);
		this.labelCurrentViewTop = new JLabel(ControlsProperties.getString("String_LabelTop"));
		this.textFieldCurrentViewTop = new WaringTextField(true);

		this.mapViewBoundsButton = new JButton("WholeMapBoundsButton");
		this.currentViewBoundsButton = new JButton("ViewMapBoundsButton");
		this.copyButton = new JButton("CopyButton");
		this.pasteButton = new JButton("PasteButton");

		this.mapViewBoundsButton.setText(ControlsProperties.getString("String_MapView"));
		this.currentViewBoundsButton.setText(ControlsProperties.getString("String_CurrentView"));
		this.copyButton.setText(CoreProperties.getString("String_CopySymbolOrGroup"));
		this.pasteButton.setText(CoreProperties.getString("String_PasteSymbolOrGroup"));
	}

	private void initLayout() {

		this.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDatasetBounds")));

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelCurrentViewLeft)
						.addComponent(this.labelCurrentViewTop)
						.addComponent(this.labelCurrentViewRight)
						.addComponent(this.labelCurrentViewBottom))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.textFieldCurrentViewLeft, 150, 150, Short.MAX_VALUE)
						.addComponent(this.textFieldCurrentViewTop, 150, 150, Short.MAX_VALUE)
						.addComponent(this.textFieldCurrentViewRight, 150, 150, Short.MAX_VALUE)
						.addComponent(this.textFieldCurrentViewBottom, 150, 150, Short.MAX_VALUE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.mapViewBoundsButton, 100, 100, 100)
						.addComponent(this.currentViewBoundsButton, 100, 100, 100)
						.addComponent(this.copyButton, 100, 100, 100)
						.addComponent(this.pasteButton, 100, 100, 100))
		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewLeft)
						.addComponent(this.textFieldCurrentViewLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.mapViewBoundsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewTop)
						.addComponent(this.textFieldCurrentViewTop, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.currentViewBoundsButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewRight)
						.addComponent(this.textFieldCurrentViewRight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.copyButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelCurrentViewBottom)
						.addComponent(this.textFieldCurrentViewBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.pasteButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(20, 20, Short.MAX_VALUE)
		);
		//@formatter:on

//		this.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDatasetBounds")));
//		this.setLayout(new GridBagLayout());
//		this.add(this.labelCurrentViewLeft, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10).setWeight(0, 0));
//		this.add(this.textFieldCurrentViewLeft, new GridBagConstraintsHelper(1, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(3, 0));
//		this.add(this.mapViewBoundsButton, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(0, 0));
//		this.add(this.labelCurrentViewTop, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10).setWeight(0, 0));
//		this.add(this.textFieldCurrentViewTop, new GridBagConstraintsHelper(1, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(3, 0));
//		this.add(this.currentViewBoundsButton, new GridBagConstraintsHelper(4, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(0, 0));
//		this.add(this.labelCurrentViewRight, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10).setWeight(0, 0));
//		this.add(this.textFieldCurrentViewRight, new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(3, 0));
//		this.add(this.copyButton, new GridBagConstraintsHelper(4, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(0, 0));
//		this.add(this.labelCurrentViewBottom, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10).setWeight(0, 0));
//		this.add(this.textFieldCurrentViewBottom, new GridBagConstraintsHelper(1, 3, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(3, 0));
//		this.add(this.pasteButton, new GridBagConstraintsHelper(4, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(0, 0));

	}

	/**
	 * 根据矩形框初始化值
	 *
	 * @param rectangle
	 */
	public void initStates(Rectangle2D rectangle) {
		textFieldCurrentViewLeft.setText(String.valueOf(rectangle.getLeft()));
		textFieldCurrentViewTop.setText(String.valueOf(rectangle.getTop()));
		textFieldCurrentViewRight.setText(String.valueOf(rectangle.getRight()));
		textFieldCurrentViewBottom.setText(String.valueOf(rectangle.getBottom()));
		// 判断是否有数据集打开
		currentViewBoundsButton.setEnabled(false);
		mapViewBoundsButton.setEnabled(false);

		this.mapControl = MapUtilities.getMapControl();
		if (this.mapControl != null) {
			this.mapViewL = this.mapControl.getMap().getBounds().getLeft();
			this.mapViewT = this.mapControl.getMap().getBounds().getTop();
			this.mapViewR = this.mapControl.getMap().getBounds().getRight();
			this.mapViewB = this.mapControl.getMap().getBounds().getBottom();
			currentViewBoundsButton.setEnabled(true);
			mapViewBoundsButton.setEnabled(true);
		}
	}

	private void registerEvent() {
		this.mapViewBoundsButton.addActionListener(actionButtonListener);
		this.currentViewBoundsButton.addActionListener(actionButtonListener);
		this.copyButton.addActionListener(actionButtonListener);
		this.pasteButton.addActionListener(actionButtonListener);

		this.textFieldCurrentViewLeft.getTextField().addCaretListener(textFieldLeftCaretListener);
		this.textFieldCurrentViewBottom.getTextField().addCaretListener(textFieldButtomCaretListener);
		this.textFieldCurrentViewRight.getTextField().addCaretListener(textFieldRightCaretListener);
		this.textFieldCurrentViewTop.getTextField().addCaretListener(textFieldTopCaretListener);

		this.textFieldCurrentViewLeft.getTextField().addFocusListener(textFieldFocusListener);
		this.textFieldCurrentViewBottom.getTextField().addFocusListener(textFieldFocusListener);
		this.textFieldCurrentViewRight.getTextField().addFocusListener(textFieldFocusListener);
		this.textFieldCurrentViewTop.getTextField().addFocusListener(textFieldFocusListener);

		this.textFieldCurrentViewLeft.registEvents();
		this.textFieldCurrentViewBottom.registEvents();
		this.textFieldCurrentViewRight.registEvents();
		this.textFieldCurrentViewTop.registEvents();
	}
}
