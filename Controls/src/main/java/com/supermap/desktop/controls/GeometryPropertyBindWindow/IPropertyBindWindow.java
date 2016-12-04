package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;

public interface IPropertyBindWindow {
    void registEvents();

    void removeEvents();

    IBindWindow getBindWindow();

    void setBindWindow(IBindWindow bindWindow, Layer activeLayer);

    IBindProperty getBindProperty();

    void setBindProperty(IBindProperty bindProperty);

//    void setSplitWindow(SplitWindow splitWindow);

    void setFormMap(IFormMap formMap);
}
