package com.supermap.desktop.Interface;

import java.awt.Component;

public interface IPopupMenu extends IMenu {

    /**
     * Displays the popup menu at the position x,y in the coordinate
     * space of the component invoker.
     *
     * @param invoker the component in whose space the popup menu is to appear
     * @param x the x coordinate in invoker's coordinate space at which 
     * the popup menu is to be displayed
     * @param y the y coordinate in invoker's coordinate space at which 
     * the popup menu is to be displayed
     */
    void show(Component invoker, int x, int y);
}
