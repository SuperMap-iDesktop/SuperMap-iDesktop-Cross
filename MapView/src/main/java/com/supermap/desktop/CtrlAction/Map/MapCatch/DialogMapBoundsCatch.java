package com.supermap.desktop.CtrlAction.Map.MapCatch;

import com.supermap.data.GeoRegion;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

/**
 * Created by yuanR on 2017/8/24 0024.
 * 地图范围选取面板
 */
public class DialogMapBoundsCatch extends SmDialog {

	private static final long serialVersionUID = 1L;

	public GeoRegion getGeoRegion() {
		return geoRegion;
	}

	private GeoRegion geoRegion;

	private JLabel labelSourcePointX;
	public JTextField textFieldSourcePointX;
	private JLabel labelSourcePointY;
	public JTextField textFieldSourcePointY;
	private JLabel labelTargetPointX;
	public JTextField textFieldTargetPointX;
	private JLabel labelTargetPointY;
	public JTextField textFieldTargetPointY;

	// 复制
	private JButton copyButton;
	// 粘贴
	private JButton pasteButton;

	// 确定取消按钮
	private PanelButton panelButton;

	public static final int DEFAULT_BUTTON_WIDTH = 75;
	public static final int DEFAULT_TEXTFILED_WIDTH = 75;


	/**
	 * 按钮事件枢纽站
	 */
	private ActionListener actionButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(copyButton)) {
				// 复制
				copyParameter();
			} else if (e.getSource().equals(pasteButton)) {
				// 粘贴
				pasteParameter();
			} else if (e.getSource().equals(panelButton.getButtonOk())) {
				copyParameter();
				dispose();
			} else if (e.getSource().equals(panelButton.getButtonCancel())) {
				dispose();
			}
		}
	};


	/**
	 * 文本框获得、失去焦点监听
	 * 用于实现对于千分位的处理，当编辑时去除千分位，结束编辑时获得千分位
	 */
	private FocusListener textFieldFocusListener = new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			// 当获得焦点时，将文本框中数字做去除千分位处理
			if (e.getSource().equals(textFieldSourcePointX)) {
				String temp = textFieldSourcePointX.getText();
				temp = temp.replace(",", "");
				textFieldSourcePointX.setText(temp);
			} else if (e.getSource().equals(textFieldSourcePointY)) {
				String temp = textFieldSourcePointY.getText();
				temp = temp.replace(",", "");
				textFieldSourcePointY.setText(temp);
			} else if (e.getSource().equals(textFieldTargetPointX)) {
				String temp = textFieldTargetPointX.getText();
				temp = temp.replace(",", "");
				textFieldTargetPointX.setText(temp);
			} else if (e.getSource().equals(textFieldTargetPointY)) {
				String temp = textFieldTargetPointY.getText();
				temp = temp.replace(",", "");
				textFieldTargetPointY.setText(temp);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			// 当失去焦点时，将文本框中数字设置千分位
			if (!textFieldSourcePointX.getText().isEmpty() && e.getSource().equals(textFieldSourcePointX)) {
				String temp = textFieldSourcePointX.getText();
				temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(temp));
				textFieldSourcePointX.setText(temp);
			} else if (!textFieldSourcePointY.getText().isEmpty() && e.getSource().equals(textFieldSourcePointY)) {
				String temp = textFieldSourcePointY.getText();
				temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(temp));
				textFieldSourcePointY.setText(temp);
			} else if (!textFieldTargetPointX.getText().isEmpty() && e.getSource().equals(textFieldTargetPointX)) {
				String temp = textFieldTargetPointX.getText();
				temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(temp));
				textFieldTargetPointX.setText(temp);
			} else if (!textFieldTargetPointY.getText().isEmpty() && e.getSource().equals(textFieldTargetPointY)) {
				String temp = textFieldTargetPointY.getText();
				temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(temp));
				textFieldTargetPointY.setText(temp);
			}
		}
	};

	public DialogMapBoundsCatch(GeoRegion geoRegion) {
		super();
		this.geoRegion = geoRegion;
		initComponents();
		initLayout();
		initStates(this.geoRegion);
		registerEvent();
		this.setTitle(MapViewProperties.getString("String_GroupBox_MapBounds"));
		this.setSize(new Dimension(400, 180));
		this.setLocationRelativeTo(null);
	}


	/**
	 * 复制参数
	 */
	private void copyParameter() {

		if (this.textFieldSourcePointX.getText() != null && this.textFieldSourcePointY.getText() != null && this.textFieldTargetPointX.getText() != null && this.textFieldTargetPointY.getText() != null) {
			String clipBoardTextLeft = ControlsProperties.getString("String_SourcePointX") + StringUtilities.getNumber(this.textFieldSourcePointX.getText().replace(",", ""));
			String clipBoardTextBottom = ControlsProperties.getString("String_SourcePointY") + StringUtilities.getNumber(this.textFieldSourcePointY.getText().replace(",", ""));
			String clipBoardTextRight = ControlsProperties.getString("String_TargetPointX") + StringUtilities.getNumber(this.textFieldTargetPointX.getText().replace(",", ""));
			String clipBoardTextTop = ControlsProperties.getString("String_TargetPointY") + StringUtilities.getNumber(this.textFieldTargetPointY.getText().replace(",", ""));
			// 调用windows复制功能，将值复制到剪贴板
			setSysClipboardText(clipBoardTextLeft + "," + clipBoardTextBottom + "," + clipBoardTextRight + "," + clipBoardTextTop);
			Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_MapBounds_Has_Been_Copied"));
		}
	}

	/**
	 * 调用windows的剪贴板
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
		String sourcePointX = ControlsProperties.getString("String_SourcePointX");
		String sourcePointY = ControlsProperties.getString("String_SourcePointY");
		String targetPointX = ControlsProperties.getString("String_TargetPointX");
		String targetPointY = ControlsProperties.getString("String_TargetPointY");

		if (clipBoard.contains(sourcePointX) && clipBoard.contains(sourcePointY) && clipBoard.contains(targetPointX) && clipBoard.contains(targetPointY)) {
			String sourceX = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_SourcePointX")), clipBoard.indexOf(ControlsProperties.getString("String_SourcePointY")));
			String sourceY = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_SourcePointY")), clipBoard.indexOf(ControlsProperties.getString("String_TargetPointX")));
			String targetX = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_TargetPointX")), clipBoard.indexOf(ControlsProperties.getString("String_TargetPointY")));
			String targetY = clipBoard.substring(clipBoard.indexOf(ControlsProperties.getString("String_TargetPointY")));

			sourceX = (sourceX.replace(sourcePointX, "")).replace(",", "");
			sourceY = (sourceY.replace(sourcePointY, "")).replace(",", "");
			targetX = (targetX.replace(targetPointX, "")).replace(",", "");
			targetY = (targetY.replace(targetPointY, "")).replace(",", "");

			if (StringUtilities.isNumber(sourceX) && StringUtilities.isNumber(sourceY) && StringUtilities.isNumber(targetX) && StringUtilities.isNumber(targetY)) {
				this.textFieldSourcePointX.setText(sourceX);
				this.textFieldSourcePointY.setText(sourceY);
				this.textFieldTargetPointX.setText(targetX);
				this.textFieldTargetPointY.setText(targetY);
			}
		}
	}

	private void initComponents() {
		this.labelSourcePointX = new JLabel(ControlsProperties.getString("String_SourcePointX"));
		this.textFieldSourcePointX = new JTextField();
		this.labelSourcePointY = new JLabel(ControlsProperties.getString("String_SourcePointY"));
		this.textFieldSourcePointY = new JTextField();
		this.labelTargetPointX = new JLabel(ControlsProperties.getString("String_TargetPointX"));
		this.textFieldTargetPointX = new JTextField();
		this.labelTargetPointY = new JLabel(ControlsProperties.getString("String_TargetPointY"));
		this.textFieldTargetPointY = new JTextField();

		this.copyButton = new JButton("CopyButton");
		this.pasteButton = new JButton("PasteButton");

		this.copyButton.setText(CoreProperties.getString("String_CopySymbolOrGroup"));
		this.pasteButton.setText(CoreProperties.getString("String_PasteSymbolOrGroup"));

		this.panelButton = new PanelButton();
	}

	private void initLayout() {
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(groupLayout);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelSourcePointX)
						.addComponent(this.labelSourcePointY)
						.addComponent(this.labelTargetPointX)
						.addComponent(this.labelTargetPointY))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.textFieldSourcePointX,  DEFAULT_TEXTFILED_WIDTH,DEFAULT_TEXTFILED_WIDTH, Short.MAX_VALUE)
						.addComponent(this.textFieldSourcePointY,DEFAULT_TEXTFILED_WIDTH,DEFAULT_TEXTFILED_WIDTH, Short.MAX_VALUE)
						.addComponent(this.textFieldTargetPointX,DEFAULT_TEXTFILED_WIDTH,DEFAULT_TEXTFILED_WIDTH, Short.MAX_VALUE)
						.addComponent(this.textFieldTargetPointY,DEFAULT_TEXTFILED_WIDTH,DEFAULT_TEXTFILED_WIDTH, Short.MAX_VALUE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.copyButton,DEFAULT_BUTTON_WIDTH,DEFAULT_BUTTON_WIDTH,DEFAULT_BUTTON_WIDTH)
						.addComponent(this.pasteButton,DEFAULT_BUTTON_WIDTH,DEFAULT_BUTTON_WIDTH,DEFAULT_BUTTON_WIDTH)
						.addComponent(this.panelButton.getButtonOk(),DEFAULT_BUTTON_WIDTH,DEFAULT_BUTTON_WIDTH,DEFAULT_BUTTON_WIDTH)
						.addComponent(this.panelButton.getButtonCancel(),DEFAULT_BUTTON_WIDTH,DEFAULT_BUTTON_WIDTH,DEFAULT_BUTTON_WIDTH))
		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelSourcePointX)
						.addComponent(this.textFieldSourcePointX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.copyButton))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelSourcePointY)
						.addComponent(this.textFieldSourcePointY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.pasteButton))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelTargetPointX)
						.addComponent(this.textFieldTargetPointX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.panelButton.getButtonOk()))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelTargetPointY)
						.addComponent(this.textFieldTargetPointY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.panelButton.getButtonCancel()))
				.addGap(20, 20, Short.MAX_VALUE)
		);
		//@formatter:on
	}

	/**
	 * 根据矩形框初始化值
	 *
	 * @param geoRegion
	 */
	public void initStates(GeoRegion geoRegion) {
		textFieldSourcePointX.setText(String.valueOf(geoRegion.getBounds().getLeft()));
		textFieldSourcePointY.setText(String.valueOf(geoRegion.getBounds().getTop()));
		textFieldTargetPointX.setText(String.valueOf(geoRegion.getBounds().getRight()));
		textFieldTargetPointY.setText(String.valueOf(geoRegion.getBounds().getBottom()));

	}

	private void registerEvent() {
		this.copyButton.addActionListener(actionButtonListener);
		this.pasteButton.addActionListener(actionButtonListener);
		this.panelButton.getButtonOk().addActionListener(actionButtonListener);
		this.panelButton.getButtonCancel().addActionListener(actionButtonListener);
		this.textFieldSourcePointX.addFocusListener(textFieldFocusListener);
		this.textFieldSourcePointY.addFocusListener(textFieldFocusListener);
		this.textFieldTargetPointX.addFocusListener(textFieldFocusListener);
		this.textFieldTargetPointY.addFocusListener(textFieldFocusListener);
	}
}
