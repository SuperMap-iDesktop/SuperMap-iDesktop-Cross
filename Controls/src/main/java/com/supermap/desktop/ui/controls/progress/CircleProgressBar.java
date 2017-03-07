package com.supermap.desktop.ui.controls.progress;

import com.supermap.desktop.controls.ControlsProperties;

import javax.swing.*;
import java.awt.*;

/**
 * Created by xie on 2017/3/7.
 * Circle progress bar paint with graphics
 */
public class CircleProgressBar extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * mini progress
     */
    private int minimumProgress;

    /**
     * max progress
     */
    private int maximumProgress;

    /**
     * current progress
     */
    private int progress;

    /**
     * background color for circle progress bar
     */
    private Color backgroundColor;

    /**
     * fore ground color for circle progress bar
     */
    private Color foregroundColor;

    /**
     * center digital color
     */
    private Color digitalColor;


    public CircleProgressBar() {
        setMinimumProgress(0);
        setMaximumProgress(100);
        setProgress(0);
        setBackgroundColor(new Color(209, 206, 200));
        setForegroundColor(Color.red);
        setDigitalColor(Color.red);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2d = (Graphics2D) g;
        // set the rendering hint
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;
        int fontSize = 0;
        if (getWidth() >= getHeight()) {
            x = (getWidth() - getHeight()) / 2 + 25;
            y = 25;
            width = getHeight() - 50;
            height = getHeight() - 50;
            fontSize = getWidth() / 10;
        } else {
            x = 25;
            y = (getHeight() - getWidth()) / 2 + 25;
            width = getWidth() - 50;
            height = getWidth() - 50;
            fontSize = getHeight() / 10;
        }
        graphics2d.setStroke(new BasicStroke(5.0f));
        graphics2d.setColor(backgroundColor);
        graphics2d.drawArc(x, y, width, height, 0, 360);
        graphics2d.setColor(foregroundColor);
        graphics2d.drawArc(x, y, width, height, 90, -(int) (360 * ((progress * 1.0) / (getMaximumProgress() - getMinimumProgress()))));
        graphics2d.setFont(new Font(ControlsProperties.getString("String_Boldface"), Font.BOLD, fontSize));
        FontMetrics fontMetrics = graphics2d.getFontMetrics();
        int digitalWidth = fontMetrics.stringWidth(progress + "%");
        int digitalAscent = fontMetrics.getAscent();
        graphics2d.setColor(digitalColor);
        graphics2d.drawString(progress + "%", getWidth() / 2 - digitalWidth / 2, getHeight() / 2 + digitalAscent / 2);
    }

    /**
     * @return
     */
    public int getMinimumProgress() {
        return minimumProgress;
    }

    /**
     * @param minimumProgress
     */
    public void setMinimumProgress(int minimumProgress) {
        if (minimumProgress <= getMaximumProgress()) {
            this.minimumProgress = minimumProgress;
        }
    }

    /**
     *
     * @return maximumProgress
     */
    public int getMaximumProgress() {
        return maximumProgress;
    }

    /**
     * @param maximumProgress
     */
    public void setMaximumProgress(int maximumProgress) {
        if (maximumProgress >= getMinimumProgress()) {
            this.maximumProgress = maximumProgress;
        }
    }

    /**
     * @return progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress >= getMinimumProgress() && progress <= getMaximumProgress()) {
            this.progress = progress;
            this.repaint();
        }
    }

    /**
     * @return backgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor。
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.repaint();
    }

    /**
     * @return foregroundColor
     */
    public Color getForegroundColor() {
        return foregroundColor;
    }

    /**
     * @param foregroundColor
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        this.repaint();
    }

    /**
     * @return digitalColor
     */
    public Color getDigitalColor() {
        return digitalColor;
    }

    /**
     * @param digitalColor
     */
    public void setDigitalColor(Color digitalColor) {
        this.digitalColor = digitalColor;
        this.repaint();
    }

}
