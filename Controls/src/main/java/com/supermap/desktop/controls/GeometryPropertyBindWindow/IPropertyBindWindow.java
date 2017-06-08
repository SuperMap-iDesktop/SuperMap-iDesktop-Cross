package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

public interface IPropertyBindWindow {
    void registEvents();

    void removeEvents();

    IBindWindow getBindWindow();

    void setBindWindow(IBindWindow bindWindow, MapControl mapControl);

    IBindProperty getBindProperty();

    void setBindProperty(IBindProperty bindProperty);

    void setFormMap(IFormMap formMap);
}
