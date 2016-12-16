package com.supermap.desktop.ui.mdi.plaf.feature;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.plaf.properties.MdiGroupUIProperties;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 绘图区域的布局以 Feature contain Features 的形式进行管理，每个 subFeature 各自负责内部各种子构建（contain Features）的布局以及绘制逻辑。
 * 整个 MdiGroup 的绘图区域划分为三个 features: MdiPagesFeatures（MdiPage 绘图区域）、MdiTabsFeature（标签页绘图区域）、MdiOprsFeatures（右上角功能区绘图区域），
 * 同时在 MdiGroup 内部维护 Insets 等属性，用以处理子构建（subFeature）与子构建（subFeature）、子构建（subFeature）与边框等的位置关系。
 * 任何一个 Feature 基本布局管理思路均与 MdiGroupFeature 一致。
 * ********************特殊说明******************************************
 * MdiGroup 以功能区的宽度为基准来计算 Tabs 区域的可用宽度，而功能区的高度则以 Tabs 区域的高度作为计算基准。
 * ********************************************************************
 * Created by highsad on 2016/8/30.
 */
public class MdiGroupFeature extends AbstractMdiFeature {

	private MdiGroup group;
	private List<IMdiFeature> features;
	private MdiPagesFeature pagesFeature;
	private MdiTabsFeature tabsFeature;
	private MdiOprsFeature oprsFeature;

	private MdiGroupFeature(MdiGroup group, IMdiFeature parent) {
		super(group, parent);
		this.group = group;
		this.features = new ArrayList<>();

		// 初始化主要构图区域
		this.pagesFeature = MdiPagesFeature.instance(this.group, this);
		this.tabsFeature = MdiTabsFeature.instance(this.group, this);
		this.oprsFeature = MdiOprsFeature.instance(this.group, this);
		this.pagesFeature.setTabsFeature(this.tabsFeature);
		initFeatures();
	}

	public static Insets getInsets() {
		return MdiGroupUIProperties.INSETS;
	}

	public void initFeatures() {
		this.features.add(this.pagesFeature);
		this.features.add(this.tabsFeature);
		this.features.add(this.oprsFeature);
	}

	public MdiPagesFeature getPagesFeature() {
		return pagesFeature;
	}

	public MdiTabsFeature getTabsFeature() {
		return tabsFeature;
	}

	public MdiOprsFeature getOprsFeature() {
		return oprsFeature;
	}

	@Override
	public List<IMdiFeature> getFeatures() {
		return this.features;
	}

	@Override
	public int getHeight() {
		return this.group.getHeight();
	}

	@Override
	public int getWidth() {
		return this.group.getWidth();
	}

	@Override
	protected void layouting() {
		if (getGroup().getPageCount() - getGroup().getFloatingPageCount() > 0) {

			// 比较 TabsFeature 与 OprsFeature 的高度，取最大值，用以计算 pages 的坐标
			int maxHeight = Math.max(this.tabsFeature.getHeight(), this.oprsFeature.getHeight());

			// 计算 PagesFeature 的左上角坐标
			this.pagesFeature.setX(getInsets().left);
			this.pagesFeature.setY(getInsets().top + maxHeight);

			// 计算 Tabs Feature 的左上角坐标值
			this.tabsFeature.setX(getInsets().left);
			this.tabsFeature.setY(getInsets().top + maxHeight - this.tabsFeature.getHeight());

			// 计算 Oprs Feature 的左上角坐标值
			this.oprsFeature.setX(this.tabsFeature.getX() + this.tabsFeature.getWidth());
			this.oprsFeature.setY(getInsets().top + maxHeight - this.oprsFeature.getHeight());
		}
	}

	@Override
	protected void validating() {
		if (getGroup().getPageCount() - getGroup().getFloatingPageCount() == 0) {
			// Group 没有 tab 页了，什么都不做
		} else {
			if (this.features.size() == 0) {
				initFeatures();
			}

			// 初始化 Tabs 的宽度，Tabs Feature 的宽度计算方式为 getWidth - Insets.left - Insets.right - OprsFeature.width
			this.tabsFeature.setWidth(getWidth() - getInsets().left - getInsets().right - this.oprsFeature.getWidth());
		}
	}

	@Override
	public void paint(Graphics graphics) {
		if (getGroup().getPageCount() - getGroup().getFloatingPageCount() == 0) {
			// group 没有 Tab 页了，就不再绘制了
		} else {
			for (int i = 0; i < this.features.size(); i++) {
				this.features.get(i).paint(graphics);
			}
		}
	}

	public static MdiGroupFeature instance(MdiGroup group) {
		if (group != null) {
			return new MdiGroupFeature(group, null);
		}
		return null;
	}
}
