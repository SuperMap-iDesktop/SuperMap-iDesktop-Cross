package com.supermap.desktop.newtheme.themeLabel;

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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatasetVector datasetVector;
	private ThemeLabel themeLabel;
	private Layer themeLabelLayer;
	private Map map;
	
	private boolean isRefreshAtOnce;
	private String layerName;
	

	public ThemeLabelComplicatedContainer(DatasetVector dataset,ThemeLabel themeLabel,Layer layer){
		this.datasetVector = dataset;
		this.themeLabel = new ThemeLabel(themeLabel);
		this.map = initCurrentTheme(datasetVector, layer);
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
