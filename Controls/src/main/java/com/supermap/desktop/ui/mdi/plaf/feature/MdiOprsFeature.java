package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.action.IMdiAction;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiOprsUIProperties;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MdiGroup 右上角功能区
 * 功能区的宽度计算方式为 各 Icon 的宽度 + Icon 与 Icon 之间的距离 + 功能区与左边 Tabs 区域的距离 + 功能区与右边的间距
 * 功能区的高度与 tabs 的高度一致，图标与功能下边距距离一致
 * Created by highsad on 2016/8/30.
 */
public class MdiOprsFeature extends AbstractMdiFeature {

	private int width = 0;
	private int height = 0;
	private List<IMdiFeature> features = new ArrayList<>();

	public MdiOprsFeature(MdiGroup group, IMdiFeature parent) {
		super(group, parent);
		IMdiAction[] groupFunctions = group.getGroupActions();
		for (int i = 0; i < groupFunctions.length; i++) {
			IMdiFeature feature = FunctionFeature.instance(groupFunctions[i], group, this);
			this.features.add(feature);
			this.width += feature.getWidth();
			this.height = Math.max(this.height, feature.getHeight());
		}

		// group 功能区的宽度，各 Icon 的宽度 + Icon 与 Icon 之间的距离 + 功能区与左边 Tabs 区域的距离 + 功能区与右边的间距
		this.width += getIconGap() * (this.features.size() - 1) + getInsets().left + getInsets().right;
	}

	/**
	 * 获取功能区内 icon 与 icon 之间的间距
	 *
	 * @return
	 */
	public static int getIconGap() {
		return MdiOprsUIProperties.ICON_GAP;
	}

	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	public static Insets getInsets() {
		return MdiOprsUIProperties.INSETS;
	}

	@Override
	public List<IMdiFeature> getFeatures() {
		return this.features;
	}

	@Override
	public void layouting() {

		// 计算各 feature 的 X 和 Y
		// 各 feature 按索引顺序从右往左排列
		int x = getX() + MdiOprsUIProperties.INSETS.left;
		for (int i = this.features.size() - 1; i >= 0; i--) {
			IMdiFeature feature = this.features.get(i);
			feature.setX(x);
			feature.setY(getY() + getHeight() - feature.getHeight() - MdiOprsUIProperties.INSETS.bottom);
			x += feature.getWidth() + MdiOprsUIProperties.ICON_GAP;
		}
	}

	public static MdiOprsFeature instance(MdiGroup group, IMdiFeature parent) {
		if (group != null && parent != null) {
			return new MdiOprsFeature(group, parent);
		}
		return null;
	}
}
