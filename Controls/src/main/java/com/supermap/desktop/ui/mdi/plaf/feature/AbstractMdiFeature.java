package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by highsad on 2016/8/30.
 */
public abstract class AbstractMdiFeature implements IMdiFeature {

	private int x = 0;
	private int y = 0;
	private IMdiFeature parent;
	private MdiGroup group;
	private IMdiFeature currentFeature;

	public AbstractMdiFeature(MdiGroup group, IMdiFeature parent) {
		this.group = group;
		this.parent = parent;
	}

	@Override
	public IMdiFeature getParent() {
		return this.parent;
	}

	@Override
	public MdiGroup getGroup() {
		return this.group;
	}

	@Override
	public abstract List<IMdiFeature> getFeatures();

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public abstract int getHeight();

	@Override
	public abstract int getWidth();

	public void repaint() {
		getGroup().repaint(getX(), getY(), getWidth(), getHeight());
	}

	/**
	 * 进行布局
	 * 调用 layout 之前需按需求决定是否调用 validate
	 * 子元素的布局由根决定，因此需要在自身布局改变之后依次调整子元素的布局
	 */
	@Override
	public void layout() {
		layouting();

		if (getFeatures() != null) {
			for (int i = 0; i < getFeatures().size(); i++) {
				getFeatures().get(i).layout();
			}
		}
	}

	protected void layouting() {
		// 进行子元素的布局
	}

	/**
	 * 验证基础属性（width、height 等）
	 */
	@Override
	public void validate() {

		// 整个 MdiGroup 的布局元素是树型组织结构，宽高等属性是由下往上逆推的过程，因此需要先验证子元素的基础属性
		if (getFeatures() != null) {
			for (int i = 0; i < getFeatures().size(); i++) {
				getFeatures().get(i).validate();
			}
		}

		validating();
	}

	protected void validating() {
		// 验证基础属性（width、height 等）
	}

	@Override
	public boolean isContains(Point point) {
		Rectangle rectangle = new Rectangle(getX(), getY(), getWidth(), getHeight());
		return rectangle.contains(point);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (getFeatures() == null) {
			return;
		}

		Point point = getGroup().getMousePosition();

		// 向包含鼠标位置的 feature 转发 keyTyped 事件
		for (int i = 0; i < getFeatures().size(); i++) {
			IMdiFeature feature = getFeatures().get(i);
			if (feature.isContains(point)) {
				feature.keyTyped(e);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (getFeatures() == null) {
			return;
		}

		Point point = getGroup().getMousePosition();

		// 向包含鼠标位置的 feature 转发 keyPressed 事件
		for (int i = 0; i < getFeatures().size(); i++) {
			IMdiFeature feature = getFeatures().get(i);
			if (feature.isContains(point)) {
				feature.keyPressed(e);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (getFeatures() == null) {
			return;
		}

		Point point = getGroup().getMousePosition();

		// 向包含鼠标位置的 feature 转发 keyReleased 事件
		for (int i = 0; i < getFeatures().size(); i++) {
			IMdiFeature feature = getFeatures().get(i);
			if (feature.isContains(point)) {
				feature.keyReleased(e);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (getFeatures() == null) {
			return;
		}

		// 向包含鼠标位置的 feature 转发 mouseClicked 事件
		for (int i = 0; i < getFeatures().size(); i++) {
			IMdiFeature feature = getFeatures().get(i);
			if (feature.isContains(e.getPoint())) {
				feature.mouseClicked(e);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (getFeatures() == null) {
			return;
		}

		// 向包含鼠标位置的 feature 转发 mousePressed 事件
		for (int i = 0; i < getFeatures().size(); i++) {
			IMdiFeature feature = getFeatures().get(i);
			if (feature.isContains(e.getPoint())) {
				feature.mousePressed(e);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (getFeatures() == null) {
			return;
		}

		// 向包含鼠标位置的 feature 转发 mouseReleased 事件
		for (int i = 0; i < getFeatures().size(); i++) {
			IMdiFeature feature = getFeatures().get(i);
			if (feature.isContains(e.getPoint())) {
				feature.mouseReleased(e);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// 子 feature 按需实现自己的 mouseEntered
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// 子 feature 按需实现自己的 mouseExited

		if (this.currentFeature != null) {
			this.currentFeature.mouseExited(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// MdiGroup 内部 feature 不支持 drag，什么都不做
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (getFeatures() == null) {
			return;
		}

		for (int i = 0; i < getFeatures().size(); i++) {
			IMdiFeature feature = getFeatures().get(i);
			if (feature.isContains(e.getPoint())) {
				if (this.currentFeature != feature) {

					// 当有新的 feature 捕获到鼠标，并且之前捕获到鼠标的 oldFeature 不为空，则转发 oldFeature 的 mouseExited
					if (this.currentFeature != null) {
						this.currentFeature.mouseExited(e);
					}

					// 当有新的 feature 捕获到鼠标，则转发 newFeature 的 mouseEntered
					feature.mouseEntered(e);
					this.currentFeature = feature;
				}

				// 有 feature 捕获到鼠标，则转发 feature.mouseMoved
				this.currentFeature.mouseMoved(e);

				// 类似 GridLayout，MdiGroup 在界面上被划分为不同的小格子，每个小格子负责各自的绘制，这个小格子就是 feature，feature 与 feature 之间不重叠不压盖，
				// 因此一个 feature 捕获到鼠标，其他的 feature 也就无法捕获鼠标，因此一个 feature 捕获到鼠标之后直接结束循环。
				break;
			}
		}
	}

	/**
	 * 子类如果有自己的实现，需在方法最后调用 super.paint
	 *
	 * @param graphics
	 */
	@Override
	public void paint(Graphics graphics) {
		if (getFeatures() != null && graphics != null) {
			for (int i = 0; i < getFeatures().size(); i++) {
				getFeatures().get(i).paint(graphics);
			}
		}
	}
}
