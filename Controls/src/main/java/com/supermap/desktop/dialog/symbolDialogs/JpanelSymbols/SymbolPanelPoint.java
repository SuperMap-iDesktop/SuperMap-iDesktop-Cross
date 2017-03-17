package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.*;
import com.supermap.desktop.ui.controls.InternalToolkitControl;
import com.supermap.desktop.ui.controls.UIEnvironment;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author XiaJt
 */
public class SymbolPanelPoint extends SymbolPanel {
    private boolean isPaint = false;
    private static final Object lock = new Object();

    public SymbolPanelPoint(Symbol symbol, Resources resources) {
        super(symbol, resources);
        initSize();

    }

    public SymbolPanelPoint(int id, Resources resources) {
        super(id, resources);
        this.symbolName = "System " + (id + 1);
        initSize();

    }

    @Override
    protected SymbolType getSymbolType() {
        return SymbolType.MARKER;
    }

    private BufferedImage getSymbolMarkerBuffedImage(int width, int height, SymbolMarker symbolMarker, GeoPoint geoPoint) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Point point = symbolMarker.getOrigin();
        int x = point.x * width / UIEnvironment.symbolPointMax;
        int y = point.y * height / UIEnvironment.symbolPointMax;
        geoPoint.setX(x);
        geoPoint.setY(y);
        geoPoint.getStyle().setMarkerSymbolID(symbolMarker.getID());
        //Modify by xie,some image marker could not display all,So move this set.
//		if (SymbolMarkerType.getSymbolMarkerType(symbolMarker) != SymbolMarkerType.Vector) {
//			geoPoint.getStyle().setMarkerSize(new Size2D(0, 1));
//		}
        InternalToolkitControl.internalDraw(geoPoint, resources, bufferedImage.getGraphics());

        return bufferedImage;
    }

    @Override
    protected int getIconWidth() {
        return 40;
    }

    @Override
    protected int getIconHeight() {
        return 40;
    }


    @Override
    public void paint(final Graphics g) {
        if (isPaint) {
            super.paint(g);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        if (isPaint) {
                            return;
                        }
                        if (symbol != null) {
                            BufferedImage bufferedImage;
                            Geometry paintGeometry = getPaintGeometry();

                            // 二维点符号与二三维点符号分别处理
                            if (symbol instanceof SymbolMarker) {
                                bufferedImage = getSymbolMarkerBuffedImage(getIconWidth(), getIconHeight(), (SymbolMarker) symbol, ((GeoPoint) paintGeometry));
                            } else {
                                bufferedImage = ((SymbolMarker3D) symbol).getThumbnail();
                            }
                            init(bufferedImage);
                        } else {
                            Geometry paintGeometry = getPaintGeometry();
                            BufferedImage bufferedImage = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                            paintGeometry.getStyle().setMarkerSymbolID(symbolID);
                            InternalToolkitControl.internalDraw(paintGeometry, resources, bufferedImage.getGraphics());
                            init(bufferedImage);
                        }
                        isPaint = true;
                        revalidate();
                        repaint();
                    }
                }
            });
        }
    }
}
