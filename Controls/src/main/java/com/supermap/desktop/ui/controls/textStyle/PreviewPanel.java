package com.supermap.desktop.ui.controls.textStyle;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.utilities.FontUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.ui.Action;
import com.supermap.ui.InteractionMode;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 文本预览面板
 *
 * @author xie
 */
public class PreviewPanel extends JPanel implements IPreview {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private MapControl mapControl;
    private Geometry geoText;

    public PreviewPanel(Geometry geoText) {
        this.geoText = geoText;
        initPanelPreView();
    }

    @Override
    public void refresh(String text, TextStyle tempTextStyle, double rotation) {
        // 地图全幅显示
        double newFontHeight = FontUtilities.mapHeightToFontSize(tempTextStyle.getFontHeight(), MapUtilities.getActiveMap(), tempTextStyle.isSizeFixed()) * 10;
        if (!tempTextStyle.isSizeFixed() && newFontHeight - 0.0 > 0) {
            tempTextStyle.setFontHeight(newFontHeight);
        }
        mapControl.setInteractionMode(InteractionMode.CUSTOMALL);
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        MapControl.Cursors.setArrow(cursor);
        Geometry sourceGeoText = null;
        GeoPoint geoPoint = new GeoPoint();
        GeoStyle style = new GeoStyle();
        style.setLineColor(Color.red);
        style.setMarkerSize(new Size2D(3, 3));
        geoPoint.setStyle(style);
        Geometry tempGeoText = null;
        if (geoText instanceof GeoText) {
            sourceGeoText = geoText.clone();
            tempGeoText = getPreViewGeoText(text, rotation);
            ((GeoText) tempGeoText).setTextStyle(tempTextStyle);
            geoPoint.setX(tempGeoText.getInnerPoint().getX());
            geoPoint.setY(tempGeoText.getInnerPoint().getY());
        }
        if (geoText instanceof GeoText3D) {
            sourceGeoText = geoText.clone();
            tempGeoText = getPreViewGeoText3D(text, ((GeoText3D) sourceGeoText).getPart(0).getAnchorPoint());
            ((GeoText3D) tempGeoText).setTextStyle(tempTextStyle);
            geoPoint.setX(tempGeoText.getInnerPoint().getX());
            geoPoint.setY(tempGeoText.getInnerPoint().getY());
        }
        if (mapControl.getMap().getTrackingLayer().getCount() > 0 && null != mapControl.getMap().getTrackingLayer().get(0)
                && null != mapControl.getMap().getTrackingLayer().get(1)) {
            mapControl.getMap().getTrackingLayer().set(0, tempGeoText);
            mapControl.getMap().getTrackingLayer().set(1, geoPoint);

        } else {
            mapControl.getMap().getTrackingLayer().add(tempGeoText, "GeoText");
            mapControl.getMap().getTrackingLayer().add(geoPoint, "GeoPoint");
        }
        mapControl.getMap().refreshTrackingLayer();
    }

    @Override
    public double getFontHeight() {
        if (geoText instanceof GeoText) {
            return ((GeoText) geoText).getTextStyle().getFontHeight();
        } else if (geoText instanceof GeoText3D) {
            return ((GeoText3D) geoText).getTextStyle().getFontHeight();
        }
        return 0.0;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    /**
     * 获取预览面板
     *
     * @return
     */
    protected void initPanelPreView() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                ControlsProperties.getString("String_Preview"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.BLUE));
        this.add(initPreViewMapControl(), BorderLayout.CENTER);
    }

    /**
     * 获取预览的MapControl
     *
     * @return
     */
    protected MapControl initPreViewMapControl() {
        if (mapControl == null) {
            mapControl = new MapControl();
            mapControl.getMap().viewEntire();
            mapControl.getMap().setCenter(new Point2D(0, 0));
            mapControl.getMap().setViewBounds(new Rectangle2D(new Point2D(0, 0), new Point2D(200, 200)));
            // 设置为系统默认光标
            mapControl.setAction(Action.NULL);
            String text = "";
            if (geoText instanceof GeoText && ((GeoText) geoText).getPartCount() > 0) {
                text = ((GeoText) geoText).getPart(0).getText();
                double rotation = 0.0;
                if (((GeoText) geoText).getPartCount() > 0) {
                    rotation = ((GeoText) geoText).getPart(0).getRotation();
                }
                refresh(text, ((GeoText) geoText).getTextStyle(), rotation);
            }
            if (geoText instanceof GeoText3D && ((GeoText3D) geoText).getPartCount() > 0) {
                text = ((GeoText3D) geoText).getPart(0).getText();
                refresh(text, ((GeoText3D) geoText).getTextStyle(), 0.0);
            }
        }
        return mapControl;
    }

    private GeoText3D getPreViewGeoText3D(String text, Point3D anchorPoint) {
        GeoText3D preViewGeoText = new GeoText3D();
        preViewGeoText.setEmpty();
        TextPart3D textPart = new TextPart3D(text, anchorPoint);
        preViewGeoText.addPart(textPart);
        return preViewGeoText;
    }

    /**
     * 获取要预览的GeoText
     *
     * @return
     */
    protected GeoText getPreViewGeoText(String currentText, double rotation) {
        GeoText preViewGeoText = new GeoText();
        preViewGeoText.setEmpty();
        TextPart textPart = new TextPart(currentText, mapControl.getMap().getCenter(), rotation);
        preViewGeoText.addPart(textPart);
        return preViewGeoText;
    }

    @Override
    public void enabled(boolean enabled) {
        this.mapControl.setEnabled(enabled);
        this.setEnabled(enabled);
    }

    @Override
    public void removeEvents() {
        // TODO

    }

}
