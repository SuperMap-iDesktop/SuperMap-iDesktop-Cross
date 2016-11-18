package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiGroupUIProperties;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiPagesUIProperties;

import java.awt.*;
import java.util.List;

/**
 * Created by highsad on 2016/8/30.
 */
public class MdiPagesFeature extends AbstractMdiFeature {


	public MdiPagesFeature(MdiGroup group, IMdiFeature parent) {
		super(group, parent);
	}

	public static Insets getInsets() {
		return MdiPagesUIProperties.INSETS;
	}

	@Override
	public List<IMdiFeature> getFeatures() {
		return null;
	}

	@Override
	public void paint(Graphics graphics) {

		// 由于像素渲染，绘制边框的时候，上下左右的边框填充在转角的地方是有相交重叠部分的，因此，在进行边框绘制的时候，采用如下原则。
		// 左右边框完全绘制，上下边框绘制剩余部分，避免转角压盖
		paintBorderLeftEdge(graphics);
		paintBorderBottomEdge(graphics);
		paintBorderRightEdge(graphics);
		paintBorderTopEdge(graphics);
		super.paint(graphics);
	}

	/**
	 * 绘制左边框
	 *
	 * @param graphics
	 */
	private void paintBorderLeftEdge(Graphics graphics) {

		// 绘制外边线
		graphics.setColor(MdiPagesUIProperties.COLOR_BORDER_LINE);
		graphics.fillRect(getX(), getY(), 1, getHeight());

		// 填充边框内部
		graphics.setColor(MdiPagesUIProperties.COLOR_BORDER_FILL_ACTIVE);
		graphics.fillRect(getX() + 1, getY(), MdiPagesUIProperties.BORDER_WIDTH, getHeight());
	}

	/**
	 * 绘制下边框
	 *
	 * @param graphics
	 */
	private void paintBorderBottomEdge(Graphics graphics) {

		// 绘制外边框
		graphics.setColor(MdiPagesUIProperties.COLOR_BORDER_LINE);
		graphics.fillRect(getX() + 1, getY() + getHeight() - 1, getWidth() - 2, 1);

		// 填充边框内部
		graphics.setColor(MdiPagesUIProperties.COLOR_BORDER_FILL_ACTIVE);
		graphics.fillRect(getX() + 1 + MdiPagesUIProperties.BORDER_WIDTH,
				getY() + getHeight() - 1 - MdiPagesUIProperties.BORDER_WIDTH,
				getWidth() - 2 - MdiPagesUIProperties.BORDER_WIDTH * 2,
				MdiPagesUIProperties.BORDER_WIDTH);
	}

	/**
	 * 绘制右边框
	 *
	 * @param graphics
	 */
	private void paintBorderRightEdge(Graphics graphics) {

		// 绘制外边线
		graphics.setColor(MdiPagesUIProperties.COLOR_BORDER_LINE);
		// getX() + getWidth() - 1 是因为 drawLine 往坐标轴方向渲染一个像素
		graphics.fillRect(getX() + getWidth() - 1, getY(), 1, getHeight());

		// 填充边框内部
		graphics.setColor(MdiPagesUIProperties.COLOR_BORDER_FILL_ACTIVE);
		graphics.fillRect(getX() + getWidth() - MdiPagesUIProperties.BORDER_WIDTH - 1, getY() + 1, MdiPagesUIProperties.BORDER_WIDTH, getHeight() - 2);
	}

	/**
	 * 绘制上边框
	 *
	 * @param graphics
	 */
	private void paintBorderTopEdge(Graphics graphics) {

		// 绘制外边线
		graphics.setColor(MdiPagesUIProperties.COLOR_BORDER_LINE);
		// getX() +1 是左边框绘制占用，getX() + getWidth() -1 是右边框绘制占用
		graphics.fillRect(getX() + 1, getY(), getWidth() - 2, 1);

		// 填充边框内部
		graphics.setColor(MdiPagesUIProperties.COLOR_BORDER_FILL_ACTIVE);
		graphics.fillRect(getX() + 1 + MdiPagesUIProperties.BORDER_WIDTH,
				getY() + 1,
				getWidth() - 2 - MdiPagesUIProperties.BORDER_WIDTH * 2,
				MdiPagesUIProperties.BORDER_WIDTH);
	}

	@Override
	public int getHeight() {
		return getGroup().getHeight() - getY() - MdiGroupUIProperties.INSETS.bottom;
	}

	@Override
	public int getWidth() {
		return getGroup().getWidth() - getX() - MdiGroupUIProperties.INSETS.right;
	}

	public static MdiPagesFeature instance(MdiGroup group, IMdiFeature parent) {
		if (group != null && parent != null) {
			return new MdiPagesFeature(group, parent);
		}
		return null;
	}
}
