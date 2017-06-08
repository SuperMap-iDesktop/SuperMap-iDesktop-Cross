package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.plaf.MdiGroupUtilities;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiTabUIProperties;
import sun.swing.SwingUtilities2;

import java.awt.*;
import java.util.List;

/**
 * Created by highsad on 2016/8/30.
 */
public class TextFeature extends AbstractMdiFeature {

	private String text = "";
	private int height = 0;
	private int width = 0;
	private int fontDescent = 0; // 字符绘制时，从基线往下超过的距离，比如字符 g，就会这样。

	public TextFeature(String text, MdiGroup group, IMdiFeature parent) {
		super(group, parent);
		setText(text == null ? "Null" : text);
	}

	@Override
	public List<IMdiFeature> getFeatures() {
		return null;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.setFont(getGroup().getFont());
		graphics.setColor(MdiTabUIProperties.FORECOLOR);

		// 字符绘制时，坐标点指定的是基线的位置，而实际上我们希望指定的坐标点是整个字符块最下边的位置，因此使用 fontDescent 做个处理
		graphics.drawString(this.text, getX(), getY() + getHeight() - this.fontDescent);
	}

	@Override
	public void validating() {
		this.height = MdiGroupUtilities.getFontHeight(getGroup());
		this.width = SwingUtilities2.stringWidth(getGroup(), getGroup().getFontMetrics(getGroup().getFont()), this.text);
		this.fontDescent = getGroup().getFontMetrics(getGroup().getFont()).getDescent();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		validating();
	}

	public static TextFeature instance(String text, MdiGroup group, IMdiFeature parent) {
		if (group != null && parent != null) {
			return new TextFeature(text, group, parent);
		}
		return null;
	}
}
