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
	private JTextField textFieldText;
	private Geometry geomerty;
	private HashMap<TextPartType, Object> textPartTypeMap;
	private HashMap<TextPartType, JComponent> componentsMap;
	private HashMap<Integer, Object> enumMap;
	private ItemListener subObjectListener;
	private ChangeListener rotationListener;
	private Vector<TextPartChangeListener> textPartChangedListeners;
	private KeyAdapter textFieldKeyListener = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			if (KeyEvent.VK_ENTER == e.getKeyChar()) {
				resetText();
			}
		}

	};
	private FocusAdapter textFieldFocusLostListener = new FocusAdapter() {

		@Override
		public void focusLost(FocusEvent e) {
			resetText();
		}

	};

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
						textFieldText.setText(((TextPart) enumMap.get(selectItem)).getText());
					}
					if (selectItem >= 0 && geomerty instanceof GeoText3D) {
						textFieldText.setText(((TextPart3D) enumMap.get(selectItem)).getText());
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
				if (enumMap.get(comboBoxSubobject.getSelectedIndex()) instanceof TextPart) {
					((TextPart) enumMap.get(comboBoxSubobject.getSelectedIndex())).setRotation(rotation);
				}
				textPartTypeMap.put(TextPartType.ROTATION, rotation);
				fireTextPartChanged(TextPartType.ROTATION);
			}
		};
		removeEvents();
		this.comboBoxSubobject.addItemListener(subObjectListener);
		this.textFieldText.addKeyListener(textFieldKeyListener);
		this.spinnerRotation.addChangeListener(rotationListener);
		this.textFieldText.addFocusListener(textFieldFocusLostListener);
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
		this.add(this.spinnerRotation, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setIpad(50, 0).setWeight(1, 0).setInsets(2, 10, 2, 10));
		this.add(initPanelPartInfo(),  new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(2, 0).setInsets(2, 10, 2, 10));
		//@formatter:on
	}

	private JPanel initPanelPartInfo() {
		//@formatter:off
		JPanel panelPartInfo = new JPanel();
		panelPartInfo.setBorder(new TitledBorder(ControlsProperties.getString("String_GeometryPropertyTextControl_GroupBoxChildPart")));
		panelPartInfo.setLayout(new GridBagLayout());
		panelPartInfo.add(this.labelSubobject,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelPartInfo.add(this.comboBoxSubobject,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(2,10,2,10));
		panelPartInfo.add(this.labelText,           new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setInsets(2,10,2,10));
		panelPartInfo.add(textFieldText, new GridBagConstraintsHelper(0, 2, 2, 2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setInsets(2,10,2,10));
		//@formatter:on
		return panelPartInfo;
	}

	private void initTextPartTypeMap() {
		this.textPartTypeMap = new HashMap<TextPartType, Object>();
		this.componentsMap = new HashMap<TextPartType, JComponent>();
		this.enumMap = new HashMap<Integer, Object>();
		this.labelRotation = new JLabel();
		this.spinnerRotation = new JSpinner();
		this.spinnerRotation.setModel(new SpinnerNumberModel(new Double(0.0), null, null, new Double(0.1)));
		this.labelSubobject = new JLabel();
		this.comboBoxSubobject = new JComboBox<String>();
		this.labelText = new JLabel();
		this.textFieldText = new JTextField();
		this.componentsMap.put(TextPartType.TEXT, this.textFieldText);
		if (geomerty instanceof GeoText && ((GeoText) geomerty).getPartCount() > 0) {
			for (int i = 0; i < ((GeoText) geomerty).getPartCount(); i++) {
				this.comboBoxSubobject.addItem(MessageFormat.format(ControlsProperties.getString("String_TheNumberSubObject"), i + 1));
				this.enumMap.put(i, ((GeoText) geomerty).getPart(i));
			}
			textPartTypeMap.put(TextPartType.INFO, 0);
			textPartTypeMap.put(TextPartType.ROTATION, ((GeoText) geomerty).getPart(0).getRotation());
			this.spinnerRotation.setValue(((GeoText) geomerty).getPart(0).getRotation());
			this.textFieldText.setText(((GeoText) geomerty).getPart(0).getText());
		}
		if (geomerty instanceof GeoText3D && ((GeoText3D) geomerty).getPartCount() > 0) {
			for (int i = 0; i < ((GeoText3D) geomerty).getPartCount(); i++) {
				this.comboBoxSubobject.addItem(MessageFormat.format(ControlsProperties.getString("String_TheNumberSubObject"), i + 1));
				this.enumMap.put(i, ((GeoText3D) geomerty).getPart(i));
			}
			textPartTypeMap.put(TextPartType.INFO, 0);
			this.spinnerRotation.setEnabled(false);
			this.textFieldText.setText(((GeoText3D) geomerty).getPart(0).getText());
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
		this.textFieldText.removeKeyListener(textFieldKeyListener);
		this.textFieldText.removeFocusListener(textFieldFocusLostListener);
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
		String text = textFieldText.getText();
		String textPartText = "";
		if (this.enumMap.get(comboBoxSubobject.getSelectedIndex()) instanceof TextPart) {
			textPartText = ((TextPart) this.enumMap.get(comboBoxSubobject.getSelectedIndex())).getText();
		} else {
			textPartText = ((TextPart3D) this.enumMap.get(comboBoxSubobject.getSelectedIndex())).getText();
		}
		if (!StringUtilties.isNullOrEmptyString(text) && !text.equals(textPartText)) {
			textPartTypeMap.put(TextPartType.TEXT, text);
			fireTextPartChanged(TextPartType.TEXT);
		}
	}

	@Override
	public void enabled(boolean enabled) {
		this.labelText.setEnabled(enabled);
		this.textFieldText.setEnabled(enabled);
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

}
