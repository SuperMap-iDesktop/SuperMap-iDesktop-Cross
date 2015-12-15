package com.supermap.desktop.newtheme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.supermap.data.DatasetVector;
import com.supermap.data.Enum;
import com.supermap.data.FieldInfo;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextStyle;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.ColorSelectButton;
import com.supermap.desktop.ui.controls.FontComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextStyleContainer;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.ThemeLabel;
import com.supermap.ui.MapControl;

import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

public class ThemeLabelUniformContainer extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane = new JTabbedPane();
	private transient ThemeLabelPropertyPanel panelProperty;
	private transient ThemeLabelAdvancePanel panelAdvance;

	private transient DatasetVector datasetVector;
	private transient Map map;
	private transient ThemeLabel themeLabel;
	private transient Layer themeLabelLayer;
	private transient TextStyle textStyle;

	private boolean isRefreshAtOnece = true;

	public ThemeLabelUniformContainer(DatasetVector datasetVector, ThemeLabel themeLabel) {
		this.datasetVector = datasetVector;
		this.themeLabel = themeLabel;
		this.map = initCurrentTheme(datasetVector);
		initComponents();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public ThemeLabelUniformContainer(Layer layer) {
		this.themeLabelLayer = layer;
		this.themeLabel = (ThemeLabel) layer.getTheme();
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.textStyle = ((ThemeLabel) themeLabelLayer.getTheme()).getUniformStyle();
		initComponents();
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		this.setLayout(new GridBagLayout());
		this.panelProperty = new ThemeLabelPropertyPanel(themeLabelLayer);
		this.panelAdvance = new ThemeLabelAdvancePanel(themeLabelLayer);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Style"), new TextStyleContainer(textStyle, map));
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Advanced"), this.panelAdvance);
		this.tabbedPane.setSelectedIndex(1);
		this.add(this.tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH)
				.setWeight(1, 1));
	}

	/**
	 * 初始化单值专题图
	 * 
	 * @param dataset
	 * @return
	 */
	private Map initCurrentTheme(DatasetVector dataset) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeLabelLayer = mapControl.getMap().getLayers().add(dataset, themeLabel, true);
			FieldInfo fieldInfo = datasetVector.getFieldInfos().get(0);
			String item = datasetVector.getName() + "." + fieldInfo.getName();
			((ThemeLabel) themeLabelLayer.getTheme()).setLabelExpression(item);
			textStyle = ((ThemeLabel) themeLabelLayer.getTheme()).getUniformStyle();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
			mapControl.getMap().refresh();
		}
		return mapControl.getMap();
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

}
