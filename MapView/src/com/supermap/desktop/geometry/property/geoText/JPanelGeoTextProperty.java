package com.supermap.desktop.geometry.property.geoText;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

import javax.swing.*;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.TextPartType;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.textStyle.*;
import com.supermap.desktop.utilties.*;

public class JPanelGeoTextProperty extends JPanel implements IGeoTextProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ITextStyle panelBasicSet;
	private IPreview panelPreview;
	private ITextPart panelTextPart;
	private JCheckBox checkboxApplyForTextPart;
	private Geometry geometry;
	private TextStyle textStyle;
	private TextStyleChangeListener basicSetListener;
	private TextPartChangeListener textpartListener;
	private Vector<GeoInfoChangeListener> geoTextChangeListeners;
	private ItemListener checkboxItemListener;

	public JPanelGeoTextProperty(Geometry geometry) {
		this.geometry = geometry;
		if (this.geometry instanceof GeoText) {
			this.textStyle = ((GeoText) this.geometry).getTextStyle().clone();
		}
		if (this.geometry instanceof GeoText3D) {
			this.textStyle = ((GeoText3D) this.geometry).getTextStyle().clone();
		}
		initComponents();
		registEvents();
		initResources();
	}

	private void initResources() {
		this.checkboxApplyForTextPart.setText(ControlsProperties.getString("String_GeometryPropertyTextControl_CheckBoxApplyRotationOnCurrentChildPart"));
	}

	private void registEvents() {
		this.basicSetListener = new TextStyleChangeListener() {

			@Override
			public void modify(TextStyleType newValue) {
				Object newTextStyleValue = panelBasicSet.getResultMap().get(newValue);
				String text = ((JTextField) panelTextPart.getComponentsMap().get(TextPartType.TEXT)).getText();
				if (null == newTextStyleValue || StringUtilties.isNullOrEmptyString(text)) {
					return;
				}
				if (newValue.equals(TextStyleType.FIXEDSIZE)) {
					// 设置固定大小时不刷新预览界面，但需要设置计算出的字高
					textStyle.setSizeFixed((boolean) panelBasicSet.getResultMap().get(newValue));
					textStyle.setFontHeight((double) panelBasicSet.getResultMap().get(TextStyleType.FONTHEIGHT));
					fireGeoTextChanged(true);
					return;
				}
				if (!newValue.equals(TextStyleType.FIXEDSIZE)) {

					ResetTextStyleUtil.resetTextStyle(newValue, textStyle, newTextStyleValue);
					fireGeoTextChanged(true);
					double rotation = 0.0;
					if (((GeoText) geometry).getPartCount() > 0) {
						rotation = ((GeoText) geometry).getPart(0).getRotation();
					}
					panelPreview.refresh(text, textStyle, rotation);
				}
			}
		};
		this.textpartListener = new TextPartChangeListener() {
			double newRotation = ((GeoText) geometry).getPart((int) panelTextPart.getResultMap().get(TextPartType.INFO)).getRotation();

			@Override
			public void modify(TextPartType newValue) {
				Object newTextPartValue = panelTextPart.getResultMap().get(newValue);
				String text = ((JTextField) panelTextPart.getComponentsMap().get(TextPartType.TEXT)).getText();

				if (null == newTextPartValue || StringUtilties.isNullOrEmptyString(text)) {
					return;
				}
				boolean isApply = checkboxApplyForTextPart.isSelected();
				if (newValue.equals(TextPartType.INFO)) {
					panelPreview.refresh(text, textStyle, newRotation);
					return;
				}

				if (newValue.equals(TextPartType.ROTATION) && geometry instanceof GeoText) {
					newRotation = (double) newTextPartValue;
					panelPreview.refresh(text, textStyle, (double) newTextPartValue);
					fireGeoTextChanged(true);
					return;
				}
				if (newValue.equals(TextPartType.TEXT) && geometry instanceof GeoText && isApply) {
					panelPreview.refresh(text, textStyle, newRotation);
					fireGeoTextChanged(true);
					return;
				}
				if (newValue.equals(TextPartType.TEXT) && geometry instanceof GeoText3D && isApply) {
					panelPreview.refresh(text, textStyle, 0.0);
					fireGeoTextChanged(true);
					return;
				}
			}
		};
		this.checkboxItemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				panelTextPart.enabled(checkboxApplyForTextPart.isSelected());
				panelTextPart.setSubobjectEnabled(checkboxApplyForTextPart.isSelected());
				panelTextPart.enabled(checkboxApplyForTextPart.isSelected());
			}
		};
		removeEvents();
		this.panelBasicSet.addTextStyleChangeListener(basicSetListener);
		this.panelTextPart.addTextPartChangeListener(textpartListener);
		this.checkboxApplyForTextPart.addItemListener(checkboxItemListener);
	}

	private void removeEvents() {
		this.panelBasicSet.removeTextStyleChangeListener(basicSetListener);
		this.panelTextPart.removeTextPartChangeListener(textpartListener);
		this.checkboxApplyForTextPart.removeItemListener(checkboxItemListener);
	}

	private void initComponents() {
		this.removeAll();
		panelPreview = new PreviewPanel(geometry);
		panelTextPart = new TextPartPanel(geometry);
		if (geometry instanceof GeoText || geometry instanceof GeoText3D) {
			panelBasicSet = new TextBasicPanel();
			panelBasicSet.setTextStyle(textStyle);
			panelBasicSet.setProperty(true);
			panelBasicSet.setUnityVisible(false);
			panelBasicSet.initTextBasicPanel();
			this.setLayout(new GridBagLayout());
			this.add(initLeftPanel(), new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH)
					.setInsets(2).setWeight(1, 1));
			this.add(initRightPanel(),
					new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setInsets(2).setWeight(1, 1));
		}
	}

	private JPanel initRightPanel() {
		JPanel rightPanel = new JPanel();
		this.checkboxApplyForTextPart = new JCheckBox();
		this.checkboxApplyForTextPart.setSelected(true);
		rightPanel.setLayout(new GridBagLayout());
		rightPanel.add(this.panelBasicSet.getEffectPanel(),
				new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(1).setWeight(0, 1));
		rightPanel.add(this.checkboxApplyForTextPart, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(1).setWeight(1, 1));
		rightPanel.add(this.panelTextPart.getPanel(),
				new GridBagConstraintsHelper(0, 2, 1, 2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(1).setWeight(0, 2));
		return rightPanel;
	}

	private JPanel initLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.add(this.panelBasicSet.getBasicsetPanel(),
				new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(1)
						.setWeight(1, 0));
		leftPanel.add(this.panelPreview.getPanel(),
				new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setInsets(1).setWeight(1, 1));
		leftPanel.setMinimumSize(new Dimension(260, 200));
		return leftPanel;
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void reset(Recordset recordset) {
		this.geometry = recordset.getGeometry();
		if (this.geometry instanceof GeoText) {
			this.textStyle = ((GeoText) this.geometry).getTextStyle().clone();
		}
		if (this.geometry instanceof GeoText3D) {
			this.textStyle = ((GeoText3D) this.geometry).getTextStyle().clone();
		}
		initComponents();
		initResources();
		removeEvents();
		registEvents();
		this.updateUI();
	}

	@Override
	public void apply(Recordset recordset) {
		// 由于没有做显示树，当前默认只设置最后一个recordset
		// 设置属性
		recordset.moveLast();
		Geometry geometry = recordset.getGeometry().clone();
		// 设置子对象参数
		if (geometry instanceof GeoText && checkboxApplyForTextPart.isSelected()) {
			((GeoText) geometry).setTextStyle(textStyle);
			TextPart textPart = ((GeoText) geometry).getPart((int) panelTextPart.getResultMap().get(TextPartType.INFO));
			if (null != panelTextPart.getResultMap().get(TextPartType.ROTATION)) {
				textPart.setRotation((double) panelTextPart.getResultMap().get(TextPartType.ROTATION));
			}
			if (null != panelTextPart.getResultMap().get(TextPartType.TEXT)) {
				textPart.setText((String) panelTextPart.getResultMap().get(TextPartType.TEXT));
			}
		}
		if (geometry instanceof GeoText && !checkboxApplyForTextPart.isSelected()) {
			((GeoText) geometry).setTextStyle(textStyle);
			if (null != panelTextPart.getResultMap().get(TextPartType.ROTATION)) {
				for (int i = 0; i < ((GeoText) geometry).getPartCount(); i++) {
					((GeoText) geometry).getPart(i).setRotation((double) panelTextPart.getResultMap().get(TextPartType.ROTATION));
				}
			}
		}
		if (geometry instanceof GeoText3D && checkboxApplyForTextPart.isSelected()) {
			((GeoText3D) geometry).setTextStyle(textStyle);
			TextPart3D textPart = ((GeoText3D) geometry).getPart((int) panelTextPart.getResultMap().get(TextPartType.INFO));
			if (null != panelTextPart.getResultMap().get(TextPartType.TEXT)) {
				textPart.setText((String) panelTextPart.getResultMap().get(TextPartType.TEXT));
			}
		}
		recordset.edit();
		recordset.setGeometry(geometry);
		recordset.update();
		MapUtilties.getActiveMap().refresh();
	}

	@Override
	public void enabled(boolean enabled) {
		this.panelBasicSet.enabled(enabled);
		this.panelTextPart.enabled(enabled);
		this.panelTextPart.setRotationEnabled(enabled);
		if (checkboxApplyForTextPart.isSelected() && enabled) {
			this.panelTextPart.enabled(true);
		} else {
			this.panelTextPart.enabled(false);
		}
		this.checkboxApplyForTextPart.setEnabled(enabled);
	}

	@Override
	public void addGeoTextChangeListener(GeoInfoChangeListener l) {
		if (geoTextChangeListeners == null) {
			geoTextChangeListeners = new Vector<GeoInfoChangeListener>();
		}
		if (!geoTextChangeListeners.contains(l)) {
			geoTextChangeListeners.add(l);
		}
	}

	@Override
	public void removeGeoTextChangeListener(GeoInfoChangeListener l) {
		if (null != geoTextChangeListeners && geoTextChangeListeners.contains(l)) {
			geoTextChangeListeners.remove(l);
		}
	}

	@Override
	public void fireGeoTextChanged(boolean isModified) {
		if (null != geoTextChangeListeners) {
			Vector<GeoInfoChangeListener> listeners = geoTextChangeListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++) {
				listeners.elementAt(i).modify(isModified);
			}
		}
	}

}
