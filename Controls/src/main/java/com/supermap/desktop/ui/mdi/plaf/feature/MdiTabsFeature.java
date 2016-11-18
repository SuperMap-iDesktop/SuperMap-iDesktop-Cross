package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiTabsUIProperties;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tabs 区域的宽度由 MdiGroup 的宽度与 MdiOps 的宽度共同决定，开放设置
 * Tabs 区域的高度由 Tab 的 icon、text、activate state 决定
 * Created by highsad on 2016/8/30.
 */
public class MdiTabsFeature extends AbstractMdiFeature {

	private int tabGap = 2; // tab 与 tab 之间的间距
	private int width = 0;
	private List<IMdiFeature> features = new ArrayList<>();
	private int firstVisibleTabIndex = 0; // 当前 tabs 第一个可显示的 tab
	private int lastVisibleTabIndex = 0; // 当前 tabs 最后一个可显示的 tab
	private int height = 0;

	public MdiTabsFeature(MdiGroup group, IMdiFeature parent) {
		super(group, parent);
	}

	public static int getTabGap() {
		return MdiTabsUIProperties.TAB_GAP;
	}

	public static Insets getInsets() {
		return MdiTabsUIProperties.INSETS;
	}

	@Override
	public List<IMdiFeature> getFeatures() {
		return this.features;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
	}

	/**
	 * 取 Tab 激活状态下的最大高度
	 *
	 * @return
	 */
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * Tabs 区域的宽度由 group 的宽度、group Insets.left、group Insets.right、Oprs 的宽度共同决定，开放设置
	 *
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	protected void validating() {
		this.features.clear();
		for (int i = 0; i < getGroup().getPageCount(); i++) {
			IMdiFeature feature = MdiTabFeature.instance(getGroup(), getGroup().getPageAt(i), this);
			this.features.add(feature);
			this.height = Math.max(this.height, feature.getHeight());
		}
	}

	/**
	 * 计算 maxVisibleTabCount 的时候，默认有一个激活状态的 Tab
	 */
	@Override
	public void layouting() {
		layoutingVisibleIndex();
		layoutingFeaturesRect();
	}

	/**
	 * 计算整个 Tabs 区域可以显示的 Tab 起止索引
	 * Tab 的文本不同导致 Tab 的宽度不同，每一次重绘都需要根据当前视图重新计算
	 */
	private void layoutingVisibleIndex() {
		int effectiveWidth = getEffectiveWidth();

		if (getGroup() != null && this.features.size() > 0 && effectiveWidth > 0) {
			this.lastVisibleTabIndex = this.features.size() - 1;
			int sum = 0;

			// 从 firstIndex 往后遍历计算宽度，直至所有的 Features 摆放完毕或者 sum 总宽度超过 effectiveWidth
			for (int i = this.firstVisibleTabIndex; i < this.features.size(); i++) {
				IMdiFeature childFeature = this.features.get(i);
				sum += sum == 0 ? childFeature.getWidth() : childFeature.getWidth() + this.tabGap;

				if (sum > effectiveWidth) {
					this.lastVisibleTabIndex = i - 1;
					break;
				}
			}

			// 如果 tabs 区域可以摆放的下，那么就全部显示出来。
			// 如果 lastVisibleTabIndex 已经是最后一个了，那么就说明可用的 tabs 区域还有发挥空间，就从 startIndex 往前继续运算
			if (this.lastVisibleTabIndex == this.features.size() && this.firstVisibleTabIndex > 0) {
				for (int i = this.firstVisibleTabIndex - 1; i >= 0; i--) {
					IMdiFeature childFeature = this.features.get(i);
					sum += sum == 0 ? childFeature.getWidth() : childFeature.getWidth() + this.tabGap;

					if (sum > effectiveWidth && i < this.firstVisibleTabIndex) {
						this.firstVisibleTabIndex = i + 1;
						break;
					}
				}
			}
		} else {
			this.firstVisibleTabIndex = 0;
			this.lastVisibleTabIndex = 0;
		}
	}

	/**
	 * 计算每一个 Feature 的 bounds
	 */
	private void layoutingFeaturesRect() {
		if (this.features.size() > 0) {
			// 初始化所有 Tab 为不可见
			for (int i = 0; i < this.features.size(); i++) {
				MdiTabFeature feature = (MdiTabFeature) this.features.get(i);
				feature.setX(0);
				feature.setY(0);
				feature.setHidden(true);
			}

			int x = getX() + this.getInsets().left;
			for (int i = this.firstVisibleTabIndex; i < this.lastVisibleTabIndex + 1; i++) {
				MdiTabFeature childFeature = (MdiTabFeature) this.features.get(i);
				int y = childFeature.getPage().isActive() ? getY() + getInsets().top : getY() + getInsets().top + MdiTabFeature.getActiveInsets().top + MdiTabFeature.getActiveInsets().bottom;
				childFeature.setX(x);
				childFeature.setY(y);
				childFeature.setHidden(false);
				x += childFeature.getWidth() + getTabGap();
			}
		}
	}

	public boolean canForward() {
		return this.firstVisibleTabIndex > 0;
	}

	public boolean canBackward() {
		return this.lastVisibleTabIndex < getGroup().getPageCount() - 1;
	}

	public void forward() {
		if (canForward()) {
			this.firstVisibleTabIndex--;
			validate();
			layout();
			repaint();
		}
	}

	public void backward() {
		if (canBackward()) {
			this.firstVisibleTabIndex++;
			validate();
			layout();
			repaint();
		}
	}

	/**
	 * 获取有效宽度 width - insets.left - insets.right
	 *
	 * @return
	 */
	private int getEffectiveWidth() {
		return this.width > getInsets().left + getInsets().right ? this.width - getInsets().left - getInsets().right : 0;
	}


	public static MdiTabsFeature instance(MdiGroup group, IMdiFeature parent) {
		if (group != null && parent != null) {
			return new MdiTabsFeature(group, parent);
		}
		return null;
	}
}
