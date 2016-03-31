package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JDialogSymbolsChange;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.MessageFormat;

public class JDialogTabularGoTo extends SmDialog {

	private static final Dimension JTEXTFIELD_SIZE = new Dimension(200, 50);

	private int minAbsolatePlace = 1;
	private int maxAbsolatePlace = 0;

	private int selectRow = 1;

	private int minRelativePlace = 0;
	private int maxRelativePlace = 0;

	private JPanel jPanelMain;

	private ButtonGroup buttonGroupPlaceType;
	private JLabel jLabelNowPlace;
	private JLabel jLabelRelativePlace;
	private JLabel jLabelAbsolutePlace;

	private JTextField jTextFieldNowPlace;
	private JTextField jTextFieldRelativePlace;
	private JTextField jTextFieldAbsolutePlace;
	private JTextField jTextFieldRelativeSpan;
	private JTextField jTextFieldAbsoluteSpan;

	private SmButton jButtonGoTo;
	private SmButton jButtonClose;

	private JRadioButton jRadioButtonRelative;
	private JRadioButton jRadioButtonAbsolute;

	public JDialogTabularGoTo() {
		super();
		initComponent();
		setListener();
		init();
		initResources();
	}

	/**
	 * 鎺т欢鍒濆鍖�
	 */
	private void initComponent() {
		this.setSize(400, 220);
		this.setTitle("GoTo");
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		jPanelMain = new JPanel();

		jLabelNowPlace = new JLabel("Now Place:");
		jLabelRelativePlace = new JLabel("RelativePlace:");
		jLabelAbsolutePlace = new JLabel("AbsolutePlace");

		jTextFieldNowPlace = new JTextField();
		jTextFieldRelativePlace = new JTextField();
		jTextFieldAbsolutePlace = new JTextField();
		jTextFieldRelativeSpan = new JTextField();
		jTextFieldAbsoluteSpan = new JTextField();

		jTextFieldRelativeSpan.setPreferredSize(JTEXTFIELD_SIZE);
		jTextFieldAbsoluteSpan.setPreferredSize(JTEXTFIELD_SIZE);
		jTextFieldAbsolutePlace.setPreferredSize(JTEXTFIELD_SIZE);
		jTextFieldRelativePlace.setPreferredSize(JTEXTFIELD_SIZE);

		jButtonClose = new SmButton("Close");
		jButtonGoTo = new SmButton("GoTo");
		getRootPane().setDefaultButton(this.jButtonGoTo);
		buttonGroupPlaceType = new ButtonGroup();
		jRadioButtonAbsolute = new JRadioButton();
		jRadioButtonRelative = new JRadioButton();
		buttonGroupPlaceType.add(jRadioButtonAbsolute);
		buttonGroupPlaceType.add(jRadioButtonRelative);

		jPanelMain.setLayout(new GridBagLayout());
		jPanelMain.add(jLabelNowPlace, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 1).setInsets(10, 10, 10, 5).setFill(GridBagConstraints.BOTH));
		jPanelMain.add(jTextFieldNowPlace, new GridBagConstraintsHelper(2, 0, 2, 1).setWeight(0, 1).setInsets(10, 0, 10, 10).setFill(GridBagConstraints.BOTH));

		jPanelMain.add(jLabelRelativePlace, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setInsets(10, 10, 10, 0).setFill(GridBagConstraints.BOTH));
		jPanelMain.add(jRadioButtonRelative, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		jPanelMain.add(jTextFieldRelativePlace,
				new GridBagConstraintsHelper(2, 1, 1, 1).setWeight(1, 1).setInsets(10, 0, 10, 5).setFill(GridBagConstraints.BOTH).setIpad(80, 0));//
		jPanelMain.add(jTextFieldRelativeSpan,
				new GridBagConstraintsHelper(3, 1, 1, 1).setWeight(1, 1).setInsets(10, 5, 10, 10).setFill(GridBagConstraints.BOTH).setIpad(80, 0));//

		jPanelMain.add(jLabelAbsolutePlace, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setInsets(10, 10, 10, 0).setFill(GridBagConstraints.BOTH));
		jPanelMain.add(jRadioButtonAbsolute, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		jPanelMain.add(jTextFieldAbsolutePlace,
				new GridBagConstraintsHelper(2, 2, 1, 1).setWeight(1, 1).setInsets(10, 0, 10, 5).setFill(GridBagConstraints.BOTH));
		jPanelMain.add(jTextFieldAbsoluteSpan,
				new GridBagConstraintsHelper(3, 2, 1, 1).setWeight(1, 1).setInsets(10, 5, 10, 10).setFill(GridBagConstraints.BOTH));

		jPanelMain.add(jButtonGoTo, new GridBagConstraintsHelper(2, 3, 1, 1).setWeight(1, 1).setInsets(10, 0, 10, 5).setFill(GridBagConstraints.BOTH));
		jPanelMain.add(jButtonClose, new GridBagConstraintsHelper(3, 3, 1, 1).setWeight(1, 1).setInsets(10, 5, 10, 10).setFill(GridBagConstraints.BOTH));

		this.add(jPanelMain);
	}

	/**
	 * 璧勬簮鍖�
	 */
	private void initResources() {
		this.setTitle(TabularViewProperties.getString("String_FormTabularGoTo_Title"));
		jLabelNowPlace.setText(TabularViewProperties.getString("String_FormTabularGoTo_labelCurrentRow"));
		jLabelRelativePlace.setText(TabularViewProperties.getString("String_FormTabularGoTo_radioButtonRelativeRow"));
		jLabelAbsolutePlace.setText(TabularViewProperties.getString("String_FormTabularGoTo_radioButtonAbsoluteRow"));
		jButtonGoTo.setText(TabularViewProperties.getString("String_FormTabularGoTo_buttonYes"));
		jButtonClose.setText(TabularViewProperties.getString("String_FormTabularGoTo_buttonCancel"));
	}

	/**
	 * 鍒濆鍖栧睘鎬�
	 */
	private void init() {
		jTextFieldNowPlace.setEditable(false);
		jTextFieldAbsoluteSpan.setEnabled(false);
		jTextFieldRelativeSpan.setEnabled(false);

		jRadioButtonAbsolute.setSelected(true);
		resetValue();
	}

	/**
	 * 閲嶆柊璁＄畻鏄剧ず鐨勫��
	 */
	private void resetValue() {
		IFormTabular formTabular = (IFormTabular) Application.getActiveApplication().getActiveForm();

		if (formTabular != null) {
			selectRow = formTabular.getSelectedRow() == -1 ? 1 : formTabular.getSelectedRow() + 1;
			maxAbsolatePlace = formTabular.getRowCount();
			minRelativePlace = minAbsolatePlace - selectRow;
			maxRelativePlace = maxAbsolatePlace - selectRow;
			jTextFieldAbsoluteSpan.setText(MessageFormat.format(TabularViewProperties.getString("String_FormTabularGoTo_Span"),
					String.valueOf(minAbsolatePlace), String.valueOf(maxAbsolatePlace)));
			jTextFieldRelativeSpan.setText(MessageFormat.format(TabularViewProperties.getString("String_FormTabularGoTo_Span"),
					String.valueOf(minRelativePlace), String.valueOf(maxRelativePlace)));
			jTextFieldNowPlace.setText(Integer.toString(selectRow));
			jTextFieldAbsolutePlace.setText(Integer.toString(selectRow));
			jTextFieldRelativePlace.setText("0");
		}
	}

	private void setListener() {

		this.jRadioButtonAbsolute.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (jRadioButtonAbsolute.isSelected()) {
					jTextFieldAbsolutePlace.setEnabled(true);
					jTextFieldRelativePlace.setEnabled(false);
				} else {
					jTextFieldAbsolutePlace.setEnabled(false);
					jTextFieldRelativePlace.setEnabled(true);
				}
			}
		});

		this.jButtonClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		this.jButtonGoTo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				goToRow();
			}
		});

		KeyListener rowInputListener = new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				int keyCode = e.getKeyChar();
				if (keyCode < KeyEvent.VK_0 || keyCode > KeyEvent.VK_9) {
					if (keyCode == KeyEvent.VK_ENTER) {
						goToRow();
					} else if (keyCode == KeyEvent.VK_MINUS) {
						// do nothing
					} else {
						e.consume();
					}
				}
			}
		};

		this.jTextFieldAbsolutePlace.addKeyListener(rowInputListener);
		this.jTextFieldRelativePlace.addKeyListener(rowInputListener);
	}

	private void goToRow() {
		try {
			int goToRow = 0;
			if (jRadioButtonAbsolute.isSelected()) {
				// 缁濆
				goToRow = Integer.parseInt(jTextFieldAbsolutePlace.getText());
			} else {
				// 鐩稿
				goToRow = selectRow + Integer.parseInt(jTextFieldRelativePlace.getText());
			}
			if (goToRow == selectRow) {
				return;
			}
			if (goToRow > maxAbsolatePlace) {
				goToRow = maxAbsolatePlace;
			}
			if (goToRow < minAbsolatePlace) {
				goToRow = minAbsolatePlace;
			}
			((IFormTabular) Application.getActiveApplication().getActiveForm()).goToRow(goToRow - 1);
		} catch (Exception e) {
			// do nothing

		} finally {
			resetValue();
		}
	}

	@Override
	public void escapePressed() {
		dispose();
	}

	@Override
	public void enterPressed() {
		if (this.getRootPane().getDefaultButton() == this.jButtonGoTo) {
			goToRow();
		}
		if (this.getRootPane().getDefaultButton() == this.jButtonClose) {
			dispose();
		}
	}

}
