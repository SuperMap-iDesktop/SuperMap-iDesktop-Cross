package com.supermap.desktop.newtheme.themeCustom;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.apache.http.entity.FileEntity;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
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
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();

	private JTabbedPane tabbedPane;
	private JPanel panelProperty;
	private JPanel panelFill;
	private JPanel panelLine;
	private JPanel panelMarker;
	// 填充
	private JLabel labelFillStyle;
	private JComboBox<String> comboBoxFillSymbol;// 填充风格
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
	// private JLabel labelMarkerColor;
	// private JComboBox<String> comboBoxMarkerColor;// 符号颜色
	private JLabel labelMarkerSize;
	private JComboBox<String> comboBoxMarkerSize;// 符号大小
	private JLabel labelMarkerAngle;
	private JComboBox<String> comboBoxMarkerAngle;// 符号旋转角度

	private ArrayList<String>[] comboBoxLists = new ArrayList[15];
	private final int FILLSYMBOLID = 0;
	private final int FILLOPAQUE = 1;
	private final int FILLFORECOLOR = 2;
	private final int FILLBACKCOLOR = 3;
	private final int FILLGRADIENTMODE = 4;
	private final int FILLGRADIENTANGLE = 5;
	private final int FILLGRADIENTOFFSETRATIOX = 6;
	private final int FILLGRADIENTOFFSETRATIOY = 7;
	private final int LINESYMBOLID = 8;
	private final int LINECOLOR = 9;
	private final int LINEWIDTH = 10;
	private final int MARKERSYMBOLID = 11;
	// private final int MARKERCOLOR =12;
	private final int MARKERSIZE = 12;
	private final int MARKERANGLE = 13;

	private boolean isRefreshAtOnce;
	public boolean isResetLayerProperty = false;
	private String layerName;
	
	private Dimension dimension = new Dimension(120, 23);
	private ItemListener panelFillComboBoxListener = new PanelFillComboBoxListener();
	private ItemListener panelLineComboBoxListener = new PanelLineComboBoxListener();
	private ItemListener panelMarkerComboBoxListener = new PanelMarkerComboBoxLitener();
	private PropertyChangeListener layerPropertyChangeListener = new LayerPropertyChangeListener();
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			isResetLayerProperty = false;
		}
	};

	public ThemeCustomContainer(DatasetVector datasetVector, ThemeCustom themeCustom, Layer layer) {
		this.datasetVector = datasetVector;
		this.themeCustom = new ThemeCustom(themeCustom);
		this.map = initCurrentTheme(datasetVector, layer);
		initComponents();
		initResources();
		registActionListener();
	}

	public ThemeCustomContainer(Layer layer) {
		this.themeCustomLayer = layer;
		this.layerName = layer.getName();
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.themeCustom = new ThemeCustom((ThemeCustom) layer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
		initComponents();
		initResources();
		registActionListener();
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
		this.comboBoxFillSymbol = new JComboBox<String>();
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
		if (ThemeUtil.isRegion(datasetVector)) {
			setPanelFillState(true);
		}else {
			setPanelFillState(false);
		}
		initComboBoxFillStyle();
		initComboBoxFillOpaque();
		initComboBoxFillForeColor();
		initComboBoxFillBackColor();
		initComboBoxFillGradientMode();
		initComboBoxFillGradientAngle();
		initComboBoxFillGradientOffsetRatioX();
		initComboBoxFillGradientOffsetRatioY();
		this.panelFill.setLayout(new GridBagLayout());
		this.panelFill.add(labelFillStyle,                  new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1));
		this.panelFill.add(comboBoxFillSymbol,              new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillOpaque,                 new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelFill.add(comboBoxFillOpaque,              new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillForeColor,              new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelFill.add(comboBoxFillForeColor,           new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillBackColor,              new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelFill.add(comboBoxFillBackColor,           new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillGradientMode,           new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelFill.add(comboBoxFillGradientMode,        new GridBagConstraintsHelper(1, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillGradientAngle,          new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelFill.add(comboBoxFillGradientAngle,       new GridBagConstraintsHelper(1, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillGradientOffsetRatioX,   new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelFill.add(comboBoxFillGradientOffsetRatioX,new GridBagConstraintsHelper(1, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelFill.add(labelFillGradientOffsetRatioY,   new GridBagConstraintsHelper(0, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelFill.add(comboBoxFillGradientOffsetRatioY,new GridBagConstraintsHelper(1, 7, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void setPanelFillState(boolean isRegion) {
		this.labelFillStyle.setEnabled(isRegion);
		this.comboBoxFillSymbol.setEnabled(isRegion);
		this.labelFillOpaque.setEnabled(isRegion);
		this.comboBoxFillOpaque.setEnabled(isRegion);
		this.labelFillForeColor.setEnabled(isRegion);
		this.comboBoxFillForeColor.setEnabled(isRegion);
		this.labelFillBackColor.setEnabled(isRegion);
		this.comboBoxFillBackColor.setEnabled(isRegion);
		this.labelFillGradientMode.setEnabled(isRegion);
		this.comboBoxFillGradientMode.setEnabled(isRegion);
		this.labelFillGradientAngle.setEnabled(isRegion);
		this.comboBoxFillGradientAngle.setEnabled(isRegion);
		this.labelFillGradientOffsetRatioX.setEnabled(isRegion);
		this.comboBoxFillGradientOffsetRatioX.setEnabled(isRegion);
		this.labelFillGradientOffsetRatioY.setEnabled(isRegion);
		this.comboBoxFillGradientOffsetRatioY.setEnabled(isRegion);
	}

	private void initComboBoxFillGradientOffsetRatioY() {
		comboBoxLists[FILLGRADIENTOFFSETRATIOY] = new ArrayList<String>();
		this.comboBoxFillGradientOffsetRatioY.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxFillGradientOffsetRatioY, themeCustom.getFillGradientOffsetRatioYExpression(), datasetVector, themeCustomLayer
				.getDisplayFilter().getJoinItems(), comboBoxLists[FILLGRADIENTOFFSETRATIOY], true, false);
	}

	private void initComboBoxFillGradientOffsetRatioX() {
		comboBoxLists[FILLGRADIENTOFFSETRATIOX] = new ArrayList<String>();
		this.comboBoxFillGradientOffsetRatioX.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxFillGradientOffsetRatioX, themeCustom.getFillGradientOffsetRatioXExpression(), datasetVector, themeCustomLayer
				.getDisplayFilter().getJoinItems(), comboBoxLists[FILLGRADIENTOFFSETRATIOX], true, false);
	}

	private void initComboBoxFillGradientAngle() {
		comboBoxLists[FILLGRADIENTANGLE] = new ArrayList<String>();
		this.comboBoxFillGradientAngle.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxFillGradientAngle, themeCustom.getFillGradientAngleExpression(), datasetVector, themeCustomLayer.getDisplayFilter()
				.getJoinItems(), comboBoxLists[FILLGRADIENTANGLE], true, false);
	}

	private void initComboBoxFillGradientMode() {
		comboBoxLists[FILLGRADIENTMODE] = new ArrayList<String>();
		this.comboBoxFillGradientMode.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxFillGradientMode, themeCustom.getFillGradientModeExpression(), datasetVector, themeCustomLayer.getDisplayFilter()
				.getJoinItems(), comboBoxLists[FILLGRADIENTMODE], true, false);
	}

	private void initComboBoxFillBackColor() {
		comboBoxLists[FILLBACKCOLOR] = new ArrayList<String>();
		this.comboBoxFillBackColor.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxFillBackColor, themeCustom.getFillBackColorExpression(), datasetVector, themeCustomLayer.getDisplayFilter()
				.getJoinItems(), comboBoxLists[FILLBACKCOLOR], true, false);
	}

	private void initComboBoxFillForeColor() {
		comboBoxLists[FILLFORECOLOR] = new ArrayList<String>();
		this.comboBoxFillForeColor.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxFillForeColor, themeCustom.getFillForeColorExpression(), datasetVector, themeCustomLayer.getDisplayFilter()
				.getJoinItems(), comboBoxLists[FILLFORECOLOR], true, false);
	}

	private void initComboBoxFillOpaque() {
		comboBoxLists[FILLOPAQUE] = new ArrayList<String>();
		this.comboBoxFillOpaque.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxFillOpaque, themeCustom.getFillOpaqueRateExpression(), datasetVector,
				themeCustomLayer.getDisplayFilter().getJoinItems(), comboBoxLists[FILLOPAQUE], true, false);
	}

	private void initComboBoxFillStyle() {
		comboBoxLists[FILLSYMBOLID] = new ArrayList<String>();
		this.comboBoxFillSymbol.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxFillSymbol, themeCustom.getFillSymbolIDExpression(), datasetVector, themeCustomLayer.getDisplayFilter().getJoinItems(),
				comboBoxLists[FILLSYMBOLID], true, false);
	}

	private void initPanelLine() {
		this.panelLine = new JPanel();
		this.labelLineSymbol = new JLabel();
		this.comboBoxLineSymbol = new JComboBox<String>();
		this.labelLineColor = new JLabel();
		this.comboBoxLineColor = new JComboBox<String>();
		this.labelLineWidth = new JLabel();
		this.comboBoxLineWidth = new JComboBox<String>();
		initComboBoxLineSymbol();
		initComboBoxLineColor();
		initComboBoxLineWidth();
		if (ThemeUtil.isRegion(datasetVector) || ThemeUtil.isLine(datasetVector)) {
			setPanelLineState(true);
		} else {
			setPanelLineState(false);
		}
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

	private void setPanelLineState(boolean isLine) {
		this.labelLineSymbol.setEnabled(isLine);
		this.comboBoxLineSymbol.setEnabled(isLine);
		this.labelLineColor.setEnabled(isLine);
		this.comboBoxLineColor.setEnabled(isLine);
		this.labelLineWidth.setEnabled(isLine);
		this.comboBoxLineWidth.setEnabled(isLine);
	}

	private void initComboBoxLineWidth() {
		comboBoxLists[LINEWIDTH] = new ArrayList<String>();
		this.comboBoxLineWidth.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxLineWidth, themeCustom.getLineWidthExpression(), datasetVector, themeCustomLayer.getDisplayFilter().getJoinItems(),
				comboBoxLists[LINEWIDTH], true, false);
	}

	private void initComboBoxLineColor() {
		comboBoxLists[LINECOLOR] = new ArrayList<String>();
		this.comboBoxLineColor.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxLineColor, themeCustom.getLineColorExpression(), datasetVector, themeCustomLayer.getDisplayFilter().getJoinItems(),
				comboBoxLists[LINECOLOR], true, false);
	}

	private void initComboBoxLineSymbol() {
		comboBoxLists[LINESYMBOLID] = new ArrayList<String>();
		this.comboBoxLineSymbol.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxLineSymbol, themeCustom.getLineSymbolIDExpression(), datasetVector, themeCustomLayer.getDisplayFilter().getJoinItems(),
				comboBoxLists[LINESYMBOLID], true, false);
	}

	private void initPanelMarker() {
		this.panelMarker = new JPanel();
		this.labelMarkerSymbol = new JLabel();
		this.comboBoxMarkerSymbol = new JComboBox<String>();
		// this.labelMarkerColor = new JLabel();
		// this.comboBoxMarkerColor = new JComboBox<String>();
		this.labelMarkerSize = new JLabel();
		this.comboBoxMarkerSize = new JComboBox<String>();
		this.labelMarkerAngle = new JLabel();
		this.comboBoxMarkerAngle = new JComboBox<String>();
		initComboBoxMarkerSymbol();
		initComboBoxMarkerColor();
		initComboBoxMarkerAngle();
		if (ThemeUtil.isPoint(datasetVector)) {
			setPanelMarkerState(true);
		} else {
			setPanelMarkerState(false);
		}
		//@formatter:off
		this.panelMarker.setLayout(new GridBagLayout());
		this.panelMarker.add(labelMarkerSymbol,   new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelMarker.add(comboBoxMarkerSymbol,new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
//		this.panelMarker.add(labelMarkerColor,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
//		this.panelMarker.add(comboBoxMarkerColor, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelMarker.add(labelMarkerSize,     new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelMarker.add(comboBoxMarkerSize,  new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelMarker.add(labelMarkerAngle,    new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		this.panelMarker.add(comboBoxMarkerAngle, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void setPanelMarkerState(boolean isMarker) {
		this.labelMarkerSymbol.setEnabled(isMarker);
		this.comboBoxMarkerSymbol.setEnabled(isMarker);
		this.labelMarkerSize.setEnabled(isMarker);
		this.comboBoxMarkerSize.setEnabled(isMarker);
		this.labelMarkerAngle.setEnabled(isMarker);
		this.comboBoxMarkerAngle.setEnabled(isMarker);
	}

	private void initComboBoxMarkerAngle() {
		comboBoxLists[MARKERANGLE] = new ArrayList<String>();
		this.comboBoxMarkerAngle.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxMarkerAngle, themeCustom.getMarkerAngleExpression(), datasetVector, themeCustomLayer.getDisplayFilter().getJoinItems(),
				comboBoxLists[MARKERANGLE], true, false);
	}

	private void initComboBoxMarkerColor() {
		comboBoxLists[MARKERSIZE] = new ArrayList<String>();
		this.comboBoxMarkerSize.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxMarkerSize, themeCustom.getMarkerSizeExpression(), datasetVector, themeCustomLayer.getDisplayFilter().getJoinItems(),
				comboBoxLists[MARKERSIZE], true, false);
	}

	private void initComboBoxMarkerSymbol() {
		comboBoxLists[MARKERSYMBOLID] = new ArrayList<String>();
		this.comboBoxMarkerSymbol.setPreferredSize(dimension);
		ThemeUtil.initComboBox(comboBoxMarkerSymbol, themeCustom.getMarkerSymbolIDExpression(), datasetVector, themeCustomLayer.getDisplayFilter()
				.getJoinItems(), comboBoxLists[MARKERSYMBOLID], true, false);
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
		// this.labelMarkerColor.setText(MapViewProperties.getString("String_ThemeCustom_MarkerColor"));
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
		this.comboBoxFillSymbol.addItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillOpaque.addItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillForeColor.addItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillBackColor.addItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillGradientMode.addItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillGradientAngle.addItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillGradientOffsetRatioX.addItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillGradientOffsetRatioY.addItemListener(this.panelFillComboBoxListener);
		this.comboBoxLineSymbol.addItemListener(this.panelLineComboBoxListener );
		this.comboBoxLineColor.addItemListener(this.panelLineComboBoxListener);
		this.comboBoxLineWidth.addItemListener(this.panelLineComboBoxListener);
		this.comboBoxMarkerSymbol.addItemListener(this.panelMarkerComboBoxListener);
		this.comboBoxMarkerSize.addItemListener(this.panelMarkerComboBoxListener);
		this.comboBoxMarkerAngle.addItemListener(this.panelMarkerComboBoxListener);
		this.comboBoxFillSymbol.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxFillOpaque.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxFillForeColor.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxFillBackColor.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxFillGradientMode.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxFillGradientAngle.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxFillGradientOffsetRatioX.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxFillGradientOffsetRatioY.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxLineSymbol.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxLineColor.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxLineWidth.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxMarkerSymbol.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxMarkerSize.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxMarkerAngle.getComponent(0).addMouseListener(this.mouseAdapter);
		this.layersTree.addPropertyChangeListener("LayerPropertyChanged", this.layerPropertyChangeListener);
	}
	@Override
	public void unregistActionListener() {
		this.comboBoxFillSymbol.removeItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillOpaque.removeItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillForeColor.removeItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillBackColor.removeItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillGradientMode.removeItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillGradientAngle.removeItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillGradientOffsetRatioX.removeItemListener(this.panelFillComboBoxListener);
		this.comboBoxFillGradientOffsetRatioY.removeItemListener(this.panelFillComboBoxListener);
		this.comboBoxLineSymbol.removeItemListener(this.panelLineComboBoxListener );
		this.comboBoxLineColor.removeItemListener(this.panelLineComboBoxListener);
		this.comboBoxLineWidth.removeItemListener(this.panelLineComboBoxListener);
		this.comboBoxMarkerSymbol.removeItemListener(this.panelMarkerComboBoxListener);
		this.comboBoxMarkerSize.removeItemListener(this.panelMarkerComboBoxListener);
		this.comboBoxMarkerAngle.removeItemListener(this.panelMarkerComboBoxListener);
		this.comboBoxFillSymbol.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxFillOpaque.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxFillForeColor.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxFillBackColor.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxFillGradientMode.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxFillGradientAngle.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxFillGradientOffsetRatioX.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxFillGradientOffsetRatioY.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxLineSymbol.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxLineColor.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxLineWidth.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxMarkerSymbol.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxMarkerSize.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxMarkerAngle.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.layersTree.removePropertyChangeListener("LayerPropertyChanged", this.layerPropertyChangeListener);
	}
	class PanelFillComboBoxListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (isResetLayerProperty) {
				return;
			}
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxFillSymbol) {
					String fillSymbolExpression = themeCustom.getFillSymbolIDExpression();
					resetExpression(FILLSYMBOLID, fillSymbolExpression);
				}
				if (e.getSource()==comboBoxFillOpaque) {
					String fillOpaqueExpression = themeCustom.getFillOpaqueRateExpression();
					resetExpression(FILLOPAQUE, fillOpaqueExpression);
				}
				if (e.getSource()== comboBoxFillForeColor) {
					String fillForeColorExpression = themeCustom.getFillForeColorExpression();
					resetExpression(FILLFORECOLOR, fillForeColorExpression);
				}
				if (e.getSource()== comboBoxFillBackColor) {
					String fillBackColorExpression = themeCustom.getFillBackColorExpression();
					resetExpression(FILLBACKCOLOR, fillBackColorExpression);
				}
				if (e.getSource()== comboBoxFillGradientMode) {
					String fillGradientModeExpression = themeCustom.getFillGradientModeExpression();
					resetExpression(FILLGRADIENTMODE, fillGradientModeExpression);
				}
				if (e.getSource()== comboBoxFillGradientAngle) {
					String fillGradientAngleExpression = themeCustom.getFillGradientAngleExpression();
					resetExpression(FILLGRADIENTANGLE, fillGradientAngleExpression);
				}
				if (e.getSource()== comboBoxFillGradientOffsetRatioX) {
					String fillGradientOffsetXExpression = themeCustom.getFillGradientOffsetRatioXExpression();
					resetExpression(FILLGRADIENTOFFSETRATIOX, fillGradientOffsetXExpression);
				}
				if (e.getSource()== comboBoxFillGradientOffsetRatioY) {
					String fillGradientOffsetYExpression = themeCustom.getFillGradientOffsetRatioYExpression();
					resetExpression(FILLGRADIENTOFFSETRATIOY, fillGradientOffsetYExpression);
				}
			}
		}
	}

	class PanelLineComboBoxListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (isResetLayerProperty) {
				return;
			}
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxLineSymbol) {
					String lineSymbolExpression = themeCustom.getLineSymbolIDExpression();
					resetExpression(LINESYMBOLID, lineSymbolExpression);
				}
				if (e.getSource() == comboBoxLineColor) {
					String lineColorExpression = themeCustom.getLineColorExpression();
					resetExpression(LINECOLOR, lineColorExpression);
				}
				if (e.getSource() == comboBoxLineWidth) {
					String lineWidthExpression = themeCustom.getLineWidthExpression();
					resetExpression(LINEWIDTH, lineWidthExpression);
				}
			}
		}
		
	}
	
	class PanelMarkerComboBoxLitener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (isResetLayerProperty) {
				return;
			}
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxMarkerSymbol) {
					String markerSymbolExpression = themeCustom.getMarkerSymbolIDExpression();
					resetExpression(MARKERSYMBOLID, markerSymbolExpression);
				}
				if (e.getSource() == comboBoxMarkerSize) {
					String markerSizeExpression = themeCustom.getMarkerSizeExpression();
					resetExpression(MARKERSIZE, markerSizeExpression);
				}
				if (e.getSource() == comboBoxMarkerAngle) {
					String markerAngleExpression = themeCustom.getMarkerAngleExpression();
					resetExpression(MARKERANGLE, markerAngleExpression);
				}
			}
		}
	}
	
	class LayerPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if (null != themeCustomLayer && !themeCustomLayer.isDisposed() && ((Layer) e.getNewValue()).equals(themeCustomLayer)) {
				isResetLayerProperty  = true;
				initComboBoxFillStyle();
				initComboBoxFillOpaque();
				initComboBoxFillForeColor();
				initComboBoxFillBackColor();
				initComboBoxFillGradientMode();
				initComboBoxFillGradientAngle();
				initComboBoxFillGradientOffsetRatioX();
				initComboBoxFillGradientOffsetRatioY();
				initComboBoxLineSymbol();
				initComboBoxLineColor();
				initComboBoxLineWidth();
				initComboBoxMarkerSymbol();
				initComboBoxMarkerColor();
				initComboBoxMarkerAngle();
			}
		}

	}

	
	public void resetExpression(int type, String expression) {
		if (!comboBoxLists[type].contains(expression)) {
			expression = expression.substring(expression.indexOf(".") + 1, expression.length());
		}
		JComboBox<String> comboBoxExpression = null;
		if (type == FILLSYMBOLID) {
			comboBoxExpression = this.comboBoxFillSymbol;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == FILLOPAQUE) {
			comboBoxExpression = this.comboBoxFillOpaque;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == FILLFORECOLOR) {
			comboBoxExpression = this.comboBoxFillForeColor;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == FILLBACKCOLOR) {
			comboBoxExpression = this.comboBoxFillBackColor;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == FILLGRADIENTMODE) {
			comboBoxExpression = this.comboBoxFillGradientMode;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == FILLGRADIENTANGLE) {
			comboBoxExpression = this.comboBoxFillGradientAngle;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == FILLGRADIENTOFFSETRATIOX) {
			comboBoxExpression = this.comboBoxFillGradientOffsetRatioX;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == FILLGRADIENTOFFSETRATIOY) {
			comboBoxExpression = this.comboBoxFillGradientOffsetRatioY;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == LINESYMBOLID) {
			comboBoxExpression = this.comboBoxLineSymbol;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == LINECOLOR) {
			comboBoxExpression = this.comboBoxLineColor;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == LINEWIDTH) {
			comboBoxExpression = this.comboBoxLineWidth;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == MARKERSYMBOLID) {
			comboBoxExpression = this.comboBoxMarkerSymbol;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == MARKERSIZE) {
			comboBoxExpression = this.comboBoxMarkerSize;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}
		if (type == MARKERANGLE) {
			comboBoxExpression = this.comboBoxMarkerAngle;
			resetThemeInfo(type, expression, comboBoxExpression);
			return;
		}

	}

	private void resetThemeInfo(int type, String expression, JComboBox<String> comboBoxExpression) {
		Dataset[] datasets = ThemeUtil.getDatasets(themeCustomLayer, datasetVector);
		boolean itemChangeState = ThemeUtil.getSqlExpression(comboBoxExpression, datasets, comboBoxLists[type], expression, true);
		if (itemChangeState) {
			// 如果sql表达式中修改了选项
			expression = comboBoxExpression.getSelectedItem().toString();
			switch (type) {
			case FILLSYMBOLID:
				themeCustom.setFillSymbolIDExpression(expression);//修改填充风格表达式
				refreshAtOnce();
				break;
			case FILLOPAQUE:
				themeCustom.setFillOpaqueRateExpression(expression);//修改透明度表达式
				refreshAtOnce();
				break;
			case FILLFORECOLOR:
				themeCustom.setFillForeColorExpression(expression);//修改前景色表达式
				refreshAtOnce();
				break;
			case FILLBACKCOLOR:
				themeCustom.setFillBackColorExpression(expression);//修改背景色表达式
				refreshAtOnce();
				break;
			case FILLGRADIENTMODE:
				themeCustom.setFillGradientModeExpression(expression);//修改渐变模式表达式
				refreshAtOnce();
				break;
			case FILLGRADIENTANGLE:
				themeCustom.setFillGradientAngleExpression(expression);//修改渐变角度表达式
				refreshAtOnce();
				break;
			case FILLGRADIENTOFFSETRATIOX:
				themeCustom.setFillGradientOffsetRatioXExpression(expression);//修改渐变水平偏移量表达式
				refreshAtOnce();
				break;
			case FILLGRADIENTOFFSETRATIOY:
				themeCustom.setFillGradientOffsetRatioYExpression(expression);//修改渐变垂直偏移量表达式
				refreshAtOnce();
				break;
			case LINESYMBOLID:
				themeCustom.setLineSymbolIDExpression(expression);//修改线风格表达式
				refreshAtOnce();
				break;
			case LINECOLOR:
				themeCustom.setLineColorExpression(expression);//修改线颜色表达式
				refreshAtOnce();
				break;
			case LINEWIDTH:
				themeCustom.setLineWidthExpression(expression);//修改线宽表达式
				refreshAtOnce();
				break;
			case MARKERSYMBOLID:
				themeCustom.setMarkerSymbolIDExpression(expression);//修改符号风格表达式
				refreshAtOnce();
				break;
			case MARKERSIZE:
				themeCustom.setMarkerSizeExpression(expression);//修改符号大小表达式
				refreshAtOnce();
				break;
			case MARKERANGLE:
				themeCustom.setMarkerAngleExpression(expression);//修改符号偏移角度表达式
				refreshAtOnce();
				break;
			default:
				break;
			}
		}
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
