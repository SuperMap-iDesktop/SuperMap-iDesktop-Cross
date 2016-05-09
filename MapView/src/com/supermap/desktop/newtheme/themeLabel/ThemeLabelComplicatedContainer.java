package com.supermap.desktop.newtheme.themeLabel;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

/**
 * 标签复合风格专题图
 * @author xie
 *
 */
public class ThemeLabelComplicatedContainer extends ThemeChangePanel{
	
	private static final long serialVersionUID = 1L;
	private DatasetVector datasetVector;
	private ThemeLabel themeLabel;
	private Layer themeLabelLayer;
	private Map map;
	private JTabbedPane tabbedPane;
	private ThemeLabelPropertyPanel labelPropertyPanel;
	private JPanel panelStyle;
	private ThemeLabelAdvancePanel labelAdvancePanel;
	
	private JLabel labelSeparatorMode;
	private JLabel labelSeparator;
	private JLabel labelSeparatNumber;
	private JLabel labelDefualtStyle;
	private JComboBox<String> comboBoxSeparatorMode;//标签分割方法
	private JTextField textFieldSeparator;//分割符
	private JTextField textFieldSeparatNumber;//分割段数
	private JButton buttonDefualtStyle;//默认风格
	private JTable tableComplicated;
	
	private boolean isRefreshAtOnce;
	private String layerName;
	

	public ThemeLabelComplicatedContainer(DatasetVector dataset,ThemeLabel themeLabel,Layer layer){
		this.datasetVector = dataset;
		this.themeLabel = new ThemeLabel(themeLabel);
		this.map = initCurrentTheme(datasetVector, layer);
		initComponents();
	}

	public ThemeLabelComplicatedContainer(Layer layer){
		this.themeLabelLayer = layer;
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.themeLabel = new ThemeLabel((ThemeLabel) layer.getTheme());
		this.layerName = layer.getName();
		this.map = ThemeGuideFactory.getMapControl().getMap();
	}

	private void initComponents() {
		this.labelPropertyPanel = new ThemeLabelPropertyPanel(themeLabelLayer);
		this.labelAdvancePanel = new ThemeLabelAdvancePanel(themeLabelLayer);
		this.panelStyle = new JPanel();
		initPanelStyle();
	}
	
	private void initPanelStyle() {
		// TODO Auto-generated method stub
		
	}

	private Map initCurrentTheme(DatasetVector datasetVector, Layer themeGraduatedLayer) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeLabelLayer = mapControl.getMap().getLayers().add(datasetVector, themeLabel, true);
			// 复制关联表信息到新图层中
			this.themeLabelLayer.setDisplayFilter(themeGraduatedLayer.getDisplayFilter());
			this.layerName = this.themeLabelLayer.getName();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
			mapControl.getMap().refresh();
		}
		return mapControl.getMap();
	}


	@Override
	public Theme getCurrentTheme() {
		return themeLabel;
	}

	@Override
	public Layer getCurrentLayer() {
		return themeLabelLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeLabelLayer = layer;
	}

	@Override
	public void registActionListener() {
		
	}

	@Override
	public void unregistActionListener() {
		
	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		
	}

	@Override
	public void refreshMapAndLayer() {
		
		
	}

}
