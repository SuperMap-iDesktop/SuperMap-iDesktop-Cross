package com.supermap.desktop.geometry.property.geoText;

import javax.swing.JPanel;

import com.supermap.data.Recordset;
import com.supermap.desktop.enums.TextStyleType;
import com.supermap.desktop.ui.controls.textStyle.TextStyleChangeListener;

public interface IGeoTextProperty {
	
	JPanel getPanel();
	
	public void reset();
	
	public void apply(Recordset recordset);
	
	public void enabled(boolean enabled);
	
	public void addGeoTextChangeListener(GeoInfoChangeListener l);

	public void removeGeoTextChangeListener(GeoInfoChangeListener l);

	void fireGeoTextChanged(boolean isModified);
}
