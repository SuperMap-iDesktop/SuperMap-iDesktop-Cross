package com.supermap.desktop.newtheme.themeCustom;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

/**
 * 自定义专题图
 * 
 * @author xie
 *
 */
public class ThemeCustomContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	private ThemeCustom themeCustom;
	private Layer themeCustomLayer;
	private Map map;
	private DatasetVector datasetVector;

	private JTabbedPane tabbedPane;
	private JPanel panelProperty;
	private JPanel panelFill;
	private JPanel panelLine;
	private JPanel panelMarker;
	// 填充
	private JLabel labelFillStyle;
	private JComboBox<String> comboBoxFillStyle;// 填充风格
	private JLabel labelFillOpaque;
	private JComboBox<String> comboBoxFillOpaque;// 透明度
	private JLabel labelFillForeColor;
	private JComboBox<String> comboBoxFillForeColor;// 填充前景色
	private JLabel labelFillBackColor;
	private JComboBox<String> comboBoxFillBackColor;// 填充背景色
	private JLabel labelFillGradientMode;
	private JComboBox<String> comboBoxFillGradientMode;// 渐变类型
	private JLabel labelFillGradientAngle;
	private JComboBox<String> comboBoxFillGradientAngle;// 渐变角度
	private JLabel labelFillGradientOffsetRatioX;
	private JComboBox<String> comboBoxFillGradientOffsetRatioX;// 渐变水平偏移
	private JLabel labelFillGradientOffsetRatioY;
	private JComboBox<String> comboBoxFillGradientOffsetRatioY;// 渐变垂直偏移
	// 线型
	private JLabel labelLineSymbol;
	private JComboBox<String> comboBoxLineSymbol;// 线风格
	private JLabel labelLineColor;
	private JComboBox<String> comboBoxLineColor;// 线颜色
	private JLabel labelLineWidth;
	private JComboBox<String> comboBoxLineWidth;// 线宽
	// 符号
	private JLabel labelMarkerSymbol;
	private JComboBox<String> comboBoxMarkerSymbol;// 符号风格
	private JLabel labelMarkerColor;
	private JComboBox<String> comboBoxMarkerColor;// 符号颜色
	private JLabel labelMarkerSize;
	private JComboBox<String> comboBoxMarkerSize;// 符号大小
	private JLabel labelMarkerAngle;
	private JComboBox<String> comboBoxMarkerAngle;// 符号旋转角度

	private boolean isRefreshAtOnce;
	private String layerName;

	public ThemeCustomContainer(DatasetVector datasetVector, ThemeCustom themeCustom, Layer layer) {
		this.datasetVector = datasetVector;
		this.themeCustom = new ThemeCustom(themeCustom);
		this.map = initCurrentTheme(datasetVector, layer);
		initComponents();
		initResources();
	}

	public ThemeCustomContainer(Layer layer) {
		this.themeCustomLayer = layer;
		this.layerName = layer.getName();
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.themeCustom = new ThemeCustom((ThemeCustom) layer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
	}

	private Map initCurrentTheme(DatasetVector datasetVector2, Layer layer) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeCustomLayer = mapControl.getMap().getLayers().add(datasetVector, themeCustom, true);
			// 复制关联表信息到新图层中
			this.themeCustomLayer.setDisplayFilter(layer.getDisplayFilter());
			this.layerName = this.themeCustomLayer.getName();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
		}
		return mapControl.getMap();
	}

	private void initComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		this.tabbedPane = new JTabbedPane();
		this.panelProperty = new JPanel();
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.add(tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		initPanelProperty();	
	}

	private void initPanelProperty() {
		initPanelFill();
		initPanelLine();
		initPanelMarker();
		//@formatter:off
		JPanel panelContent = new JPanel();
		panelContent.setLayout(new GridBagLayout());
		panelContent.add(panelFill,  new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,10,2,10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(panelLine,  new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,10,2,10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(panelMarker, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,10,2,10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.setLayout(new GridBagLayout());
		this.panelProperty.add(panelContent,  new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
		//@formatter:on
	}

	private void initPanelFill() {
		//@formatter:off
		this.panelFill = new JPanel();
		this.labelFillStyle = new JLabel();
		this.comboBoxFillStyle = new JComboBox<String>();
		this.labelFillOpaque = new JLabel();
		this.comboBoxFillOpaque = new JComboBox<String>();
		this.labelFillForeColor = new JLabel();
		this.comboBoxFillForeColor = new JComboBox<String>();
		this.labelFillBackColor = new JLabel();
		this.comboBoxFillBackColor = new JComboBox<String>();
		this.labelFillGradientMode = new JLabel();
		this.comboBoxFillGradientMode = new JComboBox<String>();
		this.labelFillGradientAngle = new JLabel();
		this.comboBoxFillGradientAngle = new JComboBox<String>();
		this.labelFillGradientOffsetRatioX = new JLabel();
		this.comboBoxFillGradientOffsetRatioX = new JComboBox<String>();
		this.labelFillGradientOffsetRatioY = new JLabel();
		this.comboBoxFillGradientOffsetRatioY = new JComboBox<String>();
		this.panelFill.setLayout(new GridBagLayout());
		this.panelFill.add(labelFillStyle,                  new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelFill.add(comboBoxFillStyle,               new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillOpaque,                 new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelFill.add(comboBoxFillOpaque,              new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillForeColor,              new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelFill.add(comboBoxFillForeColor,           new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillBackColor,              new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelFill.add(comboBoxFillBackColor,           new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillGradientMode,           new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelFill.add(comboBoxFillGradientMode,        new GridBagConstraintsHelper(1, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillGradientAngle,          new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelFill.add(comboBoxFillGradientAngle,       new GridBagConstraintsHelper(1, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillGradientOffsetRatioX,   new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelFill.add(comboBoxFillGradientOffsetRatioX,new GridBagConstraintsHelper(1, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillGradientOffsetRatioY,   new GridBagConstraintsHelper(0, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelFill.add(comboBoxFillGradientOffsetRatioY,new GridBagConstraintsHelper(1, 7, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void initPanelLine() {
		this.panelLine = new JPanel();
		this.labelLineSymbol = new JLabel();
		this.comboBoxLineSymbol = new JComboBox<String>();
		this.labelLineColor = new JLabel();
		this.comboBoxLineColor = new JComboBox<String>();
		this.labelLineWidth = new JLabel();
		this.comboBoxLineWidth = new JComboBox<String>();
		//@formatter:off
		this.panelLine.setLayout(new GridBagLayout());
		this.panelLine.add(labelLineSymbol,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelLine.add(comboBoxLineSymbol, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelLine.add(labelLineColor,     new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelLine.add(comboBoxLineColor,  new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelLine.add(labelLineWidth,     new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelLine.add(comboBoxLineWidth,  new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}
	
	private void initPanelMarker() {
		this.panelMarker = new JPanel();
		this.labelMarkerSymbol = new JLabel();
		this.comboBoxMarkerSymbol = new JComboBox<String>();
		this.labelMarkerColor = new JLabel();
		this.comboBoxMarkerColor = new JComboBox<String>();
		this.labelMarkerSize = new JLabel();
		this.comboBoxMarkerSize  = new JComboBox<String>();
		this.labelMarkerAngle = new JLabel();
		this.comboBoxMarkerAngle  = new JComboBox<String>();
		//@formatter:off
		this.panelMarker.setLayout(new GridBagLayout());
		this.panelMarker.add(labelMarkerSymbol,   new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelMarker.add(comboBoxMarkerSymbol,new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelMarker.add(labelMarkerColor,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelMarker.add(comboBoxMarkerColor, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelMarker.add(labelMarkerSize,     new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelMarker.add(comboBoxMarkerSize,  new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelMarker.add(labelMarkerAngle,    new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelMarker.add(comboBoxMarkerAngle, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	
	private void initResources() {
		this.labelFillStyle.setText(MapViewProperties.getString("String_ThemeCustom_FillSymbolID"));
		this.labelFillOpaque.setText(MapViewProperties.getString("String_Opaque"));
		this.labelFillForeColor.setText(MapViewProperties.getString("String_ThemeCustom_ForeColor"));
		this.labelFillBackColor.setText(MapViewProperties.getString("String_ThemeCustom_BackColor"));
		this.labelFillGradientMode.setText(MapViewProperties.getString("String_ThemeCustom_FillGradientMode"));
		this.labelFillGradientAngle.setText(MapViewProperties.getString("String_ThemeCustom_FillGradientAngle"));
		this.labelFillGradientOffsetRatioX.setText(MapViewProperties.getString("String_ThemeCustom_FillOffsetX"));
		this.labelFillGradientOffsetRatioY.setText(MapViewProperties.getString("String_ThemeCustom_FillOffsetY"));
		this.labelLineSymbol.setText(MapViewProperties.getString("String_ThemeCustom_LineSymbolID"));
		this.labelLineColor.setText(MapViewProperties.getString("String_ThemeCustom_LineColor"));
		this.labelLineWidth.setText(MapViewProperties.getString("String_ThemeCustom_LineWidth"));
		this.labelMarkerSymbol.setText(MapViewProperties.getString("String_ThemeCustom_MarkerSymbolID"));
		this.labelMarkerColor.setText(MapViewProperties.getString("String_ThemeCustom_MarkerColor"));
		this.labelMarkerSize.setText(MapViewProperties.getString("String_ThemeCustom_MarkerSize"));
		this.labelMarkerAngle.setText(MapViewProperties.getString("String_ThemeCustom_MarkerAngle"));
		this.panelFill.setBorder(new TitledBorder(MapViewProperties.getString("String_ThemeCustom_Fill")));
		this.panelLine.setBorder(new TitledBorder(MapViewProperties.getString("String_ThemeCustom_Line")));
		this.panelMarker.setBorder(new TitledBorder(MapViewProperties.getString("String_ThemeCustom_Symbol")));
	}

	@Override
	public Theme getCurrentTheme() {
		return this.themeCustom;
	}

	@Override
	public Layer getCurrentLayer() {
		return this.themeCustomLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeCustomLayer = layer;
	}

	@Override
	public void registActionListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregistActionListener() {
		// TODO Auto-generated method stub

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
		this.themeCustomLayer = MapUtilties.findLayerByName(map, layerName);
		if (null != themeCustomLayer && null != themeCustomLayer.getTheme() && themeCustomLayer.getTheme().getType() == ThemeType.CUSTOM) {
			ThemeCustom nowThemecustom = ((ThemeCustom) this.themeCustomLayer.getTheme());
			nowThemecustom.fromXML(themeCustom.toXML());
			this.map.refresh();
		}
	}

}
