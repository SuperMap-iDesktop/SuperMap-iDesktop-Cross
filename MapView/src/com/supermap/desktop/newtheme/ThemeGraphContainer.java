package com.supermap.desktop.newtheme;

import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeGraph;

/**
 * @author Administrator
 * 统计专题图实现类
 */
public class ThemeGraphContainer extends ThemeChangePanel{

	private ThemeGraph themeGraph;
	private boolean isRefreshAtOnce;
	
	@Override
	public Theme getCurrentTheme() {
		
		return null;
	}

	private void initComponent(){
		
	}
	
	@Override
	void registActionListener() {
		
	}

	@Override
	public void unregistActionListener() {
		
	}

	@Override
	void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	@Override
	void refreshMapAndLayer() {
		// TODO Auto-generated method stub
		
	}
	
}
