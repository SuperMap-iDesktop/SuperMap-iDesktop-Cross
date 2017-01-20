package com.supermap.desktop.event;

import java.awt.*;
import java.util.EventObject;

/**
 * Created by Chens on 2017/1/20 0020.
 */
public class ColorTableChangeEvent extends EventObject {
    private double[] keys;
    private Color[] colors;


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ColorTableChangeEvent(Object source,double[] keys,Color[] colors) {
        super(source);
        this.keys = keys;
        this.colors = colors;
    }

    public double[] getKeys() {
        return keys;
    }

    public Color[] getColors() {
        return colors;
    }
}
