package com.supermap.desktop.newtheme.themeLabel;

import com.supermap.data.DatasetVector;
import com.supermap.data.TextStyle;
import com.supermap.desktop.enums.UnitValue;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.TextStyleContainer;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeType;
import com.supermap.ui.MapControl;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 标签统一风格专题图
 * 
 * @author xie
 *
 */
public class ThemeLabelUniformContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane = new JTabbedPane();
	private transient ThemeLabelPropertyPanel panelProperty;
	private transient ThemeLabelAdvancePanel panelAdvance;

	private transient Map map;
	private transient ThemeLabel themeLabel;
	private transient Layer themeLabelLayer;
	private transient TextStyle textStyle;
	private transient TextStyleContainer textStyleContainer;

	private boolean isRefreshAtOnece = true;

	private transient LocalPropertyChangeListener propertyChangeListener = new LocalPropertyChangeListener();

	private ItemListener unityListener;

	public ThemeLabelUniformContainer(Layer layer) {
		this.themeLabelLayer = layer;
		this.themeLabel = new ThemeLabel((ThemeLabel) layer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.textStyle = ((ThemeLabel) themeLabelLayer.getTheme()).getUniformStyle();
		initComponents();
		registActionListener();
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		this.setLayout(new GridBagLayout());
		this.panelProperty = new ThemeLabelPropertyPanel(themeLabelLayer);
		this.panelAdvance = new ThemeLabelAdvancePanel(themeLabelLayer);
		this.textStyleContainer = new TextStyleContainer(textStyle, map, themeLabelLayer);
		this.textStyleContainer.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.panelProperty.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.panelAdvance.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.textStyleContainer.setUniformStyle(true);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Style"), textStyleContainer);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Advanced"), this.panelAdvance);
		this.add(this.tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH)
				.setWeight(1, 1));
	}

	public boolean isRefreshAtOnece() {
		return isRefreshAtOnece;
	}

	public void setRefreshAtOnece(boolean isRefreshAtOnece) {
		this.isRefreshAtOnece = isRefreshAtOnece;
	}

	public Layer getThemeLabelLayer() {
		return themeLabelLayer;
	}

	public void setThemeLabelLayer(Layer themeLabelLayer) {
		this.themeLabelLayer = themeLabelLayer;
	}

	@Override
	public Theme getCurrentTheme() {
		return themeLabel;
	}

	@Override
	public void registActionListener() {
		this.unityListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int itemCount = panelProperty.getComboBoxOffsetUnity().getSelectedIndex();
					switch (itemCount) {
					case 0:
						panelAdvance.getLabelHorizontalUnity().setText(MapViewProperties.getString("String_DistanceUnit_Millimeter"));
						panelAdvance.getLabelVerticalUnity().setText(MapViewProperties.getString("String_DistanceUnit_Millimeter"));
						break;
					case 1:
						panelAdvance.getLabelHorizontalUnity().setText(UnitValue.parseToString(map.getCoordUnit()));
						panelAdvance.getLabelVerticalUnity().setText(UnitValue.parseToString(map.getCoordUnit()));
						break;
					default:
						break;
					}
				}
			}
		};
		unregistActionListener();
		this.panelProperty.getComboBoxOffsetUnity().addItemListener(this.unityListener);
		panelProperty.registActionListener();
		panelAdvance.registActionListener();
		textStyleContainer.registActionListener();
	}

	@Override
	public void unregistActionListener() {
		this.panelProperty.getComboBoxOffsetUnity().removeItemListener(this.unityListener);
		panelProperty.unregistActionListener();
		panelAdvance.unregistActionListener();
		textStyleContainer.unregistActionListener();
	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.textStyleContainer.setRefreshAtOnce(isRefreshAtOnce);
		this.panelProperty.setRefreshAtOnce(isRefreshAtOnce);
		this.panelAdvance.setRefreshAtOnce(isRefreshAtOnce);
	}

	@Override
	public void refreshMapAndLayer() {
		if (null != themeLabelLayer && null != themeLabelLayer.getTheme() && themeLabelLayer.getTheme().getType() == ThemeType.LABEL) {
			this.textStyleContainer.refreshMapAndLayer();
			this.panelProperty.refreshMapAndLayer();
			this.panelAdvance.refreshMapAndLayer();
		}
	}

	class LocalPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ThemeLabelUniformContainer.this.firePropertyChange("ThemeChange", null, null);
			map = ThemeGuideFactory.getMapControl().getMap();
			map.refresh();
		}
	}

	@Override
	public Layer getCurrentLayer() {
		return themeLabelLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeLabelLayer = layer;
	}
}
