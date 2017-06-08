package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.action.IMdiAction;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiTabUIProperties;
import com.supermap.desktop.ui.mdi.util.MdiResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by highsad on 2016/8/30.
 */
class MdiTabFeature extends AbstractMdiFeature {

	private MdiPage page;
	private boolean isHidden = true; // 是否隐藏。Tabs 区域不一定能显示所有的 tab，毕竟大小有限
	private int heightNormal = 0; // 非激活状态的高度
	private List<IMdiFeature> features = new ArrayList<>();
	private IMdiFeature iconFeature;
	private IMdiFeature textFeature;

	private MdiTabFeature(MdiGroup group, MdiPage page, IMdiFeature parent) {
		super(group, parent);
		this.page = page;

		IMdiAction[] pageFunctions = group.getPageActions();
		for (int i = 0; i < pageFunctions.length; i++) {
			this.features.add(FunctionFeature.instance(pageFunctions[i], group, this.page, this));
		}

		this.iconFeature = IconFeature.instance(MdiResource.getIcon(MdiResource.FILE), group, this);
		this.textFeature = TextFeature.instance(page.getTitle(), group, this);
		this.features.add(this.textFeature);
		this.features.add(this.iconFeature);

		// 获取非激活状态的高度
		for (int i = 0; i < this.features.size(); i++) {
			this.heightNormal = Math.max(this.heightNormal, this.features.get(i).getHeight());
		}
		this.heightNormal += MdiTabUIProperties.INSETS.top + MdiTabUIProperties.INSETS.bottom;
		initListener();
	}

	private void initListener() {
		this.page.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(MdiPage.TITLE_PROPERTY)) {
					((TextFeature) textFeature).setText(page.getTitle());
					getParent().layout();
					getParent().repaint();
				}
			}
		});
	}

	public MdiPage getPage() {
		return page;
	}

	public static Insets getInsets() {
		return MdiTabUIProperties.INSETS;
	}

	public static Insets getActiveInsets() {
		return MdiTabUIProperties.ACTIVE_INSETS;
	}

	public static int getContentGap() {
		return MdiTabUIProperties.CONTENT_GAP;
	}

	@Override
	public List<IMdiFeature> getFeatures() {
		return this.features;
	}

	/**
	 * 获取 Tabs 的高度
	 *
	 * @return
	 */
	@Override
	public int getHeight() {
		return this.page.isActive() ? this.heightNormal + getActiveInsets().top + getActiveInsets().bottom : this.heightNormal;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean hidden) {
		isHidden = hidden;
	}

	@Override
	public int getWidth() {
		return this.page.isFloating() ? 0 : getWidth(this.page.isActive());
	}

	private int getWidth(boolean isActive) {
		int width = getContentGap() * (this.features.size() - 1) + getInsets().left + getInsets().right;

		for (int i = 0; i < this.features.size(); i++) {
			width += this.features.get(i).getWidth();
		}
		return isActive ? width + getActiveInsets().left + getActiveInsets().right : width;
	}

	@Override
	public void paint(Graphics graphics) {
		if (this.isHidden) {
			return;
		}

		// 绘制边框
		graphics.setColor(MdiTabUIProperties.BORDER_COLOR);
		// drawRect 实际渲染了 width + 1 和 height +1 的区域
		if (this.getPage().isActive()) {
			graphics.fillRect(getX(), getY(), getWidth(), 1);
			graphics.fillRect(getX(), getY(), 1, getHeight());
			graphics.fillRect(getX() + getWidth(), getY(), 1, getHeight());
		} else {
			graphics.drawRect(getX(), getY(), getWidth(), getHeight());
		}

		// 填充底色
		graphics.setColor(this.getGroup().isFocused() && this.page.isActive() ? MdiTabUIProperties.BACKCOLOR_ACTIVE : MdiTabUIProperties.BACKCOLOR_NORMAL);
		graphics.fillRect(getX() + 1, getY() + 1, getWidth() - 1, getHeight() - 1);
		super.paint(graphics);
	}

	@Override
	public boolean isContains(Point point) {
		return !this.isHidden && super.isContains(point);
	}

	@Override
	public void layouting() {

		// 计算各 feature 的 X、Y
		// features 按索引从右往左排列
		int x = getX() + MdiTabUIProperties.INSETS.left;
		for (int i = this.features.size() - 1; i >= 0; i--) {
			IMdiFeature feature = this.features.get(i);
			feature.setX(x);
			feature.setY(getY() + (getHeight() - feature.getHeight()) / 2);
			x += feature.getWidth() + MdiTabUIProperties.CONTENT_GAP;
		}
	}

	static MdiTabFeature instance(MdiGroup group, MdiPage page, IMdiFeature parent) {
		if (group != null && parent != null) {
			return new MdiTabFeature(group, page, parent);
		}
		return null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (this.iconFeature.isContains(e.getPoint()) || this.textFeature.isContains(e.getPoint())) {
				this.page.active();
			} else {
				boolean isChildPressed = false; // 子 feature 是否被点击
				for (int i = 0; i < this.features.size(); i++) {
					IMdiFeature feature = this.features.get(i);
					if (feature.isContains(e.getPoint())) {
						feature.mousePressed(e);
						isChildPressed = true;
					}
				}

				// 没有点击子 feature，就激活当前选项卡
				if (!isChildPressed) {
					this.page.active();
				}
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			// TODO 右键菜单，留空
		} else if (SwingUtilities.isMiddleMouseButton(e)) {
			if (this.iconFeature.isContains(e.getPoint()) || this.textFeature.isContains(e.getPoint())) {
				this.page.close();
			}
		}
	}
}
