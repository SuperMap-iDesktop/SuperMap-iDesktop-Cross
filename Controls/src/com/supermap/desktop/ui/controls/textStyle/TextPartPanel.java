package com.supermap.desktop.ui.controls.textStyle;

import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.TextPartType;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.StringUtilties;

/**
 * 子对象面板
 * 
 * @author xie
 *
 */
public class TextPartPanel extends JPanel implements ITextPart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelRotation;// 旋转角度
	private JSpinner spinnerRotation;
	private JLabel labelSubobject;// 子对象
	private JComboBox<String> comboBoxSubobject;
	private JLabel labelText;// 文本内容
	private JTextArea textArea;
	private Geometry geomerty;
	private HashMap<TextPartType, Object> textPartTypeMap;
	private HashMap<TextPartType, JComponent> componentsMap;
	private HashMap<Integer, Object> enumMap;
	private ItemListener subObjectListener;
	private ChangeListener rotationListener;
	private Vector<TextPartChangeListener> textPartChangedListeners;
	private DocumentListener textAreaListener;

	public TextPartPanel(Geometry geometry) {
		this.geomerty = geometry;
		initComponents();
		initResources();
		registEvents();
	}

	private void registEvents() {
		this.subObjectListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int selectItem = comboBoxSubobject.getSelectedIndex();
					if (selectItem >= 0 && geomerty instanceof GeoText) {
						spinnerRotation.setValue(((TextPart) enumMap.get(selectItem)).getRotation());
						textArea.setText(((TextPart) enumMap.get(selectItem)).getText());
					}
					if (selectItem >= 0 && geomerty instanceof GeoText3D) {
						textArea.setText(((TextPart3D) enumMap.get(selectItem)).getText());
					}
					textPartTypeMap.put(TextPartType.INFO, selectItem);
					fireTextPartChanged(TextPartType.INFO);
				}
			}
		};
		this.rotationListener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				double rotation = (double) spinnerRotation.getValue();
				if (Double.compare(rotation, ((TextPart) enumMap.get(comboBoxSubobject.getSelectedIndex())).getRotation()) != 0) {
					((TextPart) enumMap.get(comboBoxSubobject.getSelectedIndex())).setRotation(rotation);
					textPartTypeMap.put(TextPartType.ROTATION, rotation);
					fireTextPartChanged(TextPartType.ROTATION);
				}
			}
		};
		this.textAreaListener = new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				resetText();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				resetText();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				resetText();
			}
		};
		removeEvents();
		this.comboBoxSubobject.addItemListener(subObjectListener);
		this.textArea.getDocument().addDocumentListener(textAreaListener);
		this.spinnerRotation.addChangeListener(rotationListener);
	}

	private void initResources() {
		this.labelRotation.setText(ControlsProperties.getString("String_Label_SymbolAngle"));
		this.labelSubobject.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelChildPart"));
		this.labelText.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_LabelText"));
	}

	private void initComponents() {
		this.removeAll();
		this.setLayout(new GridBagLayout());
		this.setBorder(new LineBorder(Color.lightGray));
		initTextPartTypeMap();
		//@formatter:off
		this.add(this.labelRotation,   new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2, 10, 2, 10));
		this.add(this.spinnerRotation, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setIpad(60, 0).setWeight(1, 0).setInsets(2, 10, 2, 10));
		this.add(initPanelPartInfo(),  new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(2, 0).setInsets(2, 10, 2, 10));
		//@formatter:on
	}

	private JPanel initPanelPartInfo() {
		//@formatter:off
		JPanel panelPartInfo = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		panelPartInfo.setBorder(new TitledBorder(ControlsProperties.getString("String_GeometryPropertyTextControl_GroupBoxChildPart")));
		panelPartInfo.setLayout(new GridBagLayout());
		panelPartInfo.add(this.labelSubobject,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelPartInfo.add(this.comboBoxSubobject,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(2,10,2,10));
		panelPartInfo.add(this.labelText,           new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelPartInfo.add(scrollPane, new GridBagConstraintsHelper(0, 2, 2, 2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(2, 0).setIpad(0, 40).setInsets(2,10,2,10));
		//@formatter:on
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(textArea);
		return panelPartInfo;
	}

	private void initTextPartTypeMap() {
		this.textPartTypeMap = new HashMap<TextPartType, Object>();
		this.componentsMap = new HashMap<TextPartType, JComponent>();
		this.enumMap = new HashMap<Integer, Object>();
		this.labelRotation = new JLabel();
		this.spinnerRotation = new JSpinner();
		this.spinnerRotation.setModel(new SpinnerNumberModel(new Double(0.0), new Double(0.0), new Double(360.0), new Double(1.0)));
		this.labelSubobject = new JLabel();
		this.comboBoxSubobject = new JComboBox<String>();
		this.labelText = new JLabel();
		this.textArea = new JTextArea();
		this.componentsMap.put(TextPartType.TEXT, this.textArea);
		if (geomerty instanceof GeoText && ((GeoText) geomerty).getPartCount() > 0) {
			for (int i = 0; i < ((GeoText) geomerty).getPartCount(); i++) {
				this.comboBoxSubobject.addItem(MessageFormat.format(ControlsProperties.getString("String_TheNumberSubObject"), i + 1));
				this.enumMap.put(i, ((GeoText) geomerty).getPart(i));
			}
			textPartTypeMap.put(TextPartType.INFO, 0);
			textPartTypeMap.put(TextPartType.ROTATION, ((GeoText) geomerty).getPart(0).getRotation());
			this.spinnerRotation.setValue(((GeoText) geomerty).getPart(0).getRotation());
			this.textArea.setText(((GeoText) geomerty).getPart(0).getText());
		}
		if (geomerty instanceof GeoText3D && ((GeoText3D) geomerty).getPartCount() > 0) {
			for (int i = 0; i < ((GeoText3D) geomerty).getPartCount(); i++) {
				this.comboBoxSubobject.addItem(MessageFormat.format(ControlsProperties.getString("String_TheNumberSubObject"), i + 1));
				this.enumMap.put(i, ((GeoText3D) geomerty).getPart(i));
			}
			textPartTypeMap.put(TextPartType.INFO, 0);
			this.spinnerRotation.setEnabled(false);
			this.textArea.setText(((GeoText3D) geomerty).getPart(0).getText());
		}

	}

	@Override
	public HashMap<TextPartType, Object> getResultMap() {
		return this.textPartTypeMap;
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void removeEvents() {
		this.comboBoxSubobject.removeItemListener(subObjectListener);
		this.spinnerRotation.removeChangeListener(rotationListener);
		this.textArea.getDocument().removeDocumentListener(textAreaListener);
	}

	@Override
	public void addTextPartChangeListener(TextPartChangeListener l) {
		if (textPartChangedListeners == null) {
			textPartChangedListeners = new Vector<TextPartChangeListener>();
		}
		if (!textPartChangedListeners.contains(l)) {
			textPartChangedListeners.add(l);
		}
	}

	@Override
	public void removeTextPartChangeListener(TextPartChangeListener l) {
		if (textPartChangedListeners != null && textPartChangedListeners.contains(l)) {
			textPartChangedListeners.remove(l);
		}
	}

	@Override
	public void fireTextPartChanged(TextPartType newValue) {
		if (textPartChangedListeners != null) {
			Vector<TextPartChangeListener> listeners = textPartChangedListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++) {
				listeners.elementAt(i).modify(newValue);
			}
		}
	}

	@Override
	public HashMap<TextPartType, JComponent> getComponentsMap() {
		return this.componentsMap;
	}

	private void resetText() {
		String text = textArea.getText();
		String textPartText = "";
		if (this.enumMap.get(comboBoxSubobject.getSelectedIndex()) instanceof TextPart) {
			textPartText = ((TextPart) this.enumMap.get(comboBoxSubobject.getSelectedIndex())).getText();
		} else {
			textPartText = ((TextPart3D) this.enumMap.get(comboBoxSubobject.getSelectedIndex())).getText();
		}
		if (!StringUtilties.isNullOrEmptyString(text) && !text.equals(textPartText)) {
			if (!StringUtilties.isNullOrEmptyString(text) && !text.equals(textPartText)) {
				if (this.enumMap.get(comboBoxSubobject.getSelectedIndex()) instanceof TextPart) {
					((TextPart) this.enumMap.get(comboBoxSubobject.getSelectedIndex())).setText(text);
				} else {
					((TextPart3D) this.enumMap.get(comboBoxSubobject.getSelectedIndex())).setText(text);
				}
			}
			textPartTypeMap.put(TextPartType.TEXT, text);
			fireTextPartChanged(TextPartType.TEXT);
		}
	}

	@Override
	public void enabled(boolean enabled) {
		this.labelText.setEnabled(enabled);
		this.textArea.setEnabled(enabled);
	}

	@Override
	public void setSubobjectEnabled(boolean enabled) {
		this.labelSubobject.setEnabled(enabled);
		this.comboBoxSubobject.setEnabled(enabled);
	}

	@Override
	public void setRotationEnabled(boolean enabled) {
		this.labelRotation.setEnabled(enabled);
		this.spinnerRotation.setEnabled(enabled);
	}

	@Override
	public HashMap<Integer, Object> getTextPartInfo() {
		return enumMap;
	}

	@Override
	public void resetGeometry(Geometry geometry) {
		this.geomerty = geometry;
		this.enumMap.clear();
		if (geomerty instanceof GeoText && ((GeoText) geomerty).getPartCount() > 0) {
			for (int i = 0; i < ((GeoText) geomerty).getPartCount(); i++) {
				this.enumMap.put(i, ((GeoText) geomerty).getPart(i));
			}
		}
		if (geomerty instanceof GeoText3D && ((GeoText3D) geomerty).getPartCount() > 0) {
			for (int i = 0; i < ((GeoText3D) geomerty).getPartCount(); i++) {
				this.enumMap.put(i, ((GeoText3D) geomerty).getPart(i));
			}
		}
	}

}
