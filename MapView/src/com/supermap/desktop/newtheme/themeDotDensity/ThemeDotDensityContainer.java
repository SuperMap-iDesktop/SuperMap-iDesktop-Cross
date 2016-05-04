package com.supermap.desktop.newtheme.themeDotDensity;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeDotDensity;
import com.supermap.mapping.ThemeType;
import com.supermap.ui.MapControl;

public class ThemeDotDensityContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPaneInfo;

	private JLabel labelExpression;
	private JLabel labelValue;
	private JLabel labelDotDensityValue;
	private JLabel labelDotDensityStyle;
	private JComboBox<String> comboBoxExpression;// 表达式
	private JTextField textFieldValue;// 单点代表的值
	private JSpinner spinnerDotDensityValue;// 最大值包含的点个数
	private JButton buttonDotDensityStyle;// 点风格

	private ThemeDotDensity themeDotDensity;
	private Layer themeDotDensityLayer;
	private DatasetVector datasetVector;
	private boolean isRefreshAtOnce;
	private Map map;
	private String layerName;

	public ThemeDotDensityContainer(DatasetVector datasetVector, ThemeDotDensity themeDotDensity, Layer themeDotDensityLayer) {
		this.datasetVector = datasetVector;
		this.themeDotDensity = new ThemeDotDensity(themeDotDensity);
		this.map = initCurrentTheme(datasetVector, themeDotDensityLayer);
		initComponents();
	}

	private void initComponents() {

	}

	public ThemeDotDensityContainer(Layer layer) {
		this.themeDotDensityLayer = layer;
		this.themeDotDensity = new ThemeDotDensity((ThemeDotDensity) layer.getTheme());
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.map = ThemeGuideFactory.getMapControl().getMap();
	}

	private Map initCurrentTheme(DatasetVector datasetVector, Layer dotDensityLayer) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeDotDensityLayer = mapControl.getMap().getLayers().add(datasetVector, themeDotDensity, true);
			// 复制关联表信息到新图层中
			this.themeDotDensityLayer.setDisplayFilter(dotDensityLayer.getDisplayFilter());
			this.layerName = this.themeDotDensityLayer.getName();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
		}
		return mapControl.getMap();
	}

	@Override
	public Theme getCurrentTheme() {
		return this.themeDotDensity;
	}

	@Override
	public Layer getCurrentLayer() {
		return this.themeDotDensityLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeDotDensityLayer = layer;
	}

	@Override
	public void registActionListener() {

	}

	@Override
	public void unregistActionListener() {

	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	@Override
	public void refreshMapAndLayer() {
		if (null != ThemeGuideFactory.getMapControl()) {
			this.map = ThemeGuideFactory.getMapControl().getMap();
		}
		this.themeDotDensityLayer = MapUtilties.findLayerByName(map, layerName);
		if (null != themeDotDensityLayer && null != themeDotDensityLayer.getTheme() && themeDotDensityLayer.getTheme().getType() == ThemeType.DOTDENSITY) {
			ThemeDotDensity nowThemeDotDensity = ((ThemeDotDensity) this.themeDotDensityLayer.getTheme());
			nowThemeDotDensity.fromXML(themeDotDensity.toXML());
			this.map.refresh();
		}
	}

}
