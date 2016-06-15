package com.supermap.desktop.ui.controls;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.JButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilties.ComponentFactory;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.ui.controls.textStyle.*;
import com.supermap.desktop.utilties.MapUtilties;

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
	private List<Geometry> geometries;
	private Recordset recordset;
	private transient TextStyleChangeListener textStyleChangeListener;
	private ActionListener buttonCloseListener;

	public TextStyleDialog() {
		super();
	}

	public void showDialog(Recordset recordset, List<Geometry> geometries) {
		this.recordset = recordset;
		this.geometries = geometries;
		this.geometry = geometries.get(0);
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
		this.setVisible(true);
	}
	
	private void initMainPanel() {
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
		this.getContentPane().add(textBasicPanel.getBasicsetPanel(), new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setInsets(10, 10, 0, 10).setWeight(1, 1));
		this.getContentPane().add(textBasicPanel.getEffectPanel(),   new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTH).setInsets(10, 10, 0, 10).setWeight(1, 1));
		this.getContentPane().add(this.buttonClose,                  new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 1).setInsets(5, 10, 10, 10));
		//@formatter:on
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

	public void registEvents() {
		this.textStyleChangeListener = new TextStyleChangeListener() {

			@Override
			public void modify(TextStyleType newValue) {
				if (!newValue.equals(TextStyleType.FIXEDSIZE) && !newValue.equals(TextStyleType.FONTWIDTH)) {
					ResetTextStyleUtil.resetTextStyle(newValue, tempTextStyle, textBasicPanel.getResultMap().get(newValue));
				}
			}
		};

		this.buttonCloseListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TextStyleDialog.this.dispose();
			}
		};
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent e) {
				removeEvents();
			}
			
		});
		removeEvents();
		this.textBasicPanel.addTextStyleChangeListener(this.textStyleChangeListener);
	};

	private void buttonConfirmClicked() {
		HashMap<TextStyleType, Object> resultMap = textBasicPanel.getResultMap();
		if (geometry instanceof GeoText) {
			for (int i = 0; i < geometries.size(); i++) {
				TextStyle tempStyle = ((GeoText) geometries.get(i)).getTextStyle();
				resetTextStyle(resultMap, tempStyle);
			}
		} else {
			for (int i = 0; i < geometries.size(); i++) {
				TextStyle tempStyle = ((GeoText3D) geometries.get(i)).getTextStyle();
				resetTextStyle(resultMap, tempStyle);
			}
		}
		int count = 0;
		recordset.moveFirst();
		while (!recordset.isEOF()) {
			recordset.edit();
			recordset.setGeometry(geometries.get(count));
			recordset.update();
			geometries.get(count).dispose();
			count++;
			recordset.moveNext();
		}
		recordset.dispose();
		MapUtilties.getActiveMap().refresh();
		TextStyleDialog.this.dispose();
	}

	private void resetTextStyle(HashMap<TextStyleType, Object> resultMap, TextStyle tempStyle) {
		Iterator<?> iterator = resultMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<TextStyleType, Object> entry = (Entry<TextStyleType, Object>) iterator.next();
			ResetTextStyleUtil.resetTextStyle(entry.getKey(), tempStyle, entry.getValue());
		}
	}
	
	private void removeEvents(){
		this.buttonClose.removeActionListener(this.buttonCloseListener);
		this.textBasicPanel.removeTextStyleChangeListener(this.textStyleChangeListener);
	}

}
