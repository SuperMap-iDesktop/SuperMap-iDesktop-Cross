package com.supermap.desktop.GeometryPropertyBindWindow;

import com.supermap.data.Dataset;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.ui.docking.SplitWindow;

public interface IPropertyBindWindow {
	public void registEvents();

	public void removeEvents();

	public IBindWindow getBindWindow();

	public void setBindWindow(IBindWindow bindWindow,Dataset dataset);

	public IBindProperty getBindProperty();

	public void setBindProperty(IBindProperty bindProperty);

	public void setSplitWindow(SplitWindow splitWindow);

	public void setFormMap(IFormMap formMap);
}
