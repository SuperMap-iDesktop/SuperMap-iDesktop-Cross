package com.supermap.desktop.ui.controls;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.ui.controls.textStyle.*;
import com.supermap.desktop.utilities.MapUtilities;

/**
 * 文本风格对话框
 * 
 * @author xie 2016-6-3
 */
public class TextStyleDialog extends SmDialog {

	private static final long serialVersionUID = 1L;

	private transient JButton buttonClose;

	private transient ITextStyle textBasicPanel;

	private Geometry geometry;
	private String text;
	private TextStyle tempTextStyle;// 用于预览的TextStyle
	private Double rotation;
	private Recordset recordset;
	private transient TextStyleChangeListener textStyleChangeListener;
	private ActionListener buttonCloseListener;
	private boolean isDisposed;

	private static volatile TextStyleDialog dialog;

	public static TextStyleDialog createInstance() {
		if (null == dialog) {
			dialog = new TextStyleDialog();
			dialog.setVisible(true);
		}
		return dialog;
	}

	private TextStyleDialog() {
		super();
		setModal(false);
		isDisposed = false;
		this.setSize(360, 480);
		this.setTitle(ControlsProperties.getString("String_TextStyleSet"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	public void showDialog(Recordset recordset) {
		if (null != this.recordset) {
			this.recordset.dispose();
		}
		this.recordset = recordset;
		this.recordset.moveFirst();
		this.geometry = this.recordset.getGeometry();
		if (geometry instanceof GeoText) {
			text = ((GeoText) geometry).getText();
			tempTextStyle = ((GeoText) geometry).getTextStyle();
			rotation = ((GeoText) geometry).getPart(0).getRotation();
		} else {
			text = ((GeoText3D) geometry).getText();
			tempTextStyle = ((GeoText3D) geometry).getTextStyle();
		}
		initMainPanel();
		registEvents();
	}

	private void initMainPanel() {
		JPanel panel = (JPanel) this.getContentPane();
		panel.removeAll();
		this.textBasicPanel = new TextBasicPanel();
		this.textBasicPanel.setTextStyle(tempTextStyle);
		this.textBasicPanel.setOutLineWidth(false);
		this.textBasicPanel.setUnityVisible(false);
		this.textBasicPanel.setProperty(false);
		this.textBasicPanel.initTextBasicPanel();
		this.textBasicPanel.enabled(true);
		this.getContentPane().setLayout(new GridBagLayout());
		this.buttonClose = ComponentFactory.createButtonClose();
		//@formatter:off
		panel.add(textBasicPanel.getBasicsetPanel(), new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setInsets(10, 10, 0, 10).setWeight(1, 1));
		panel.add(textBasicPanel.getEffectPanel(),   new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setInsets(10, 10, 0, 10).setWeight(1, 1));
		panel.add(this.buttonClose,                  new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(5, 10, 10, 10));
		//@formatter:on
		panel.updateUI();
	}

	public void registEvents() {
		this.textStyleChangeListener = new TextStyleChangeListener() {

			@Override
			public void modify(TextStyleType newValue) {
				updateGeometries(newValue);
			}
		};

		this.buttonCloseListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isDisposed) {
					disposeInfo();
				}
			}
		};
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (!isDisposed) {
					disposeInfo();
				}
			}

		});
		removeEvents();
		this.textBasicPanel.addTextStyleChangeListener(this.textStyleChangeListener);
		this.buttonClose.addActionListener(this.buttonCloseListener);
	};

	private void disposeInfo() {
		removeEvents();
		dialog.dispose();
		dialog = null;
		recordset.dispose();
		isDisposed = true;
	}

	private void updateGeometries(TextStyleType newValue) {
		recordset.moveFirst();
		while (!recordset.isEOF()) {
			recordset.edit();
			Geometry tempGeometry = recordset.getGeometry();
			if (tempGeometry instanceof GeoText && !newValue.equals(TextStyleType.FIXEDSIZE)) {
				ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText) tempGeometry).getTextStyle(), textBasicPanel.getResultMap().get(newValue));
			}
			if (tempGeometry instanceof GeoText && newValue.equals(TextStyleType.FIXEDSIZE)) {
				ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText) tempGeometry).getTextStyle(), textBasicPanel.getResultMap().get(newValue));
				ResetTextStyleUtil.resetTextStyle(TextStyleType.FONTHEIGHT, ((GeoText) tempGeometry).getTextStyle(),
						textBasicPanel.getResultMap().get(TextStyleType.FONTHEIGHT));
			}
			if (tempGeometry instanceof GeoText3D && !newValue.equals(TextStyleType.FIXEDSIZE)) {
				ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText3D) tempGeometry).getTextStyle(), textBasicPanel.getResultMap().get(newValue));
			}
			if (tempGeometry instanceof GeoText3D && newValue.equals(TextStyleType.FIXEDSIZE)) {
				ResetTextStyleUtil.resetTextStyle(newValue, ((GeoText3D) tempGeometry).getTextStyle(), textBasicPanel.getResultMap().get(newValue));
				ResetTextStyleUtil.resetTextStyle(TextStyleType.FONTHEIGHT, ((GeoText3D) tempGeometry).getTextStyle(),
						textBasicPanel.getResultMap().get(TextStyleType.FONTHEIGHT));
			}
			recordset.setGeometry(tempGeometry);
			recordset.update();
			recordset.moveNext();
		}

		recordset.dispose();
		MapUtilities.getActiveMap().refresh();
		TextStyleDialog.this.dispose();
	}

	private void removeEvents() {
		this.buttonClose.removeActionListener(this.buttonCloseListener);
		this.textBasicPanel.removeTextStyleChangeListener(this.textStyleChangeListener);
	}

}
