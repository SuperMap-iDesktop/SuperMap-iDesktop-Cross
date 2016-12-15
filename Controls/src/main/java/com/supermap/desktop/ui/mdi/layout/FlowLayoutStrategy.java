package com.supermap.desktop.ui.mdi.layout;

import com.supermap.desktop.ui.mdi.IMdiContainer;
import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPane;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by highsad on 2016/12/14.
 */
public class FlowLayoutStrategy extends SplitLayoutStrategy {

	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	private static final double DEFAULT_DIVIDER_PROPORTION = 0.5;

	private int layoutMode = HORIZONTAL;
	private Map<JSplitPane, Double> dividerProportions = new HashMap<>();

	private FlowLayoutStrategy(MdiPane container) {
		this(container, HORIZONTAL);
	}

	private FlowLayoutStrategy(MdiPane container, int layoutMode) {
		super(container);
		this.layoutMode = layoutMode;
	}

	public static FlowLayoutStrategy instance(MdiPane container) {
		return FlowLayoutStrategy.instance(container, HORIZONTAL);
	}

	public static FlowLayoutStrategy instance(MdiPane container, int layoutMode) {
		if (container == null) {
			return null;
		}

		return new FlowLayoutStrategy(container, layoutMode);
	}

	public void setLayoutMode(int layoutMode) {
		if (this.layoutMode != layoutMode) {
			this.layoutMode = layoutMode;

			MdiGroup[] groups = getContainer().getGroups();
			for (int i = 0; i < groups.length; i++) {
				MdiGroup group = groups[i];
				setLayoutMode(getSplits().get(group), this.layoutMode);
			}
		}
	}

	@Override
	public void addGroup(MdiGroup group) {
		super.addGroup(group);

		JSplitPane split = getSplits().get(group);
		JSplitPane preSplit = findPreSplit(group);

		if (preSplit != null) {
			setNextSplit(preSplit, split);
		} else {
			((MdiPane) getContainer()).add(split, BorderLayout.CENTER);
			resetDividerSize(split);
			((MdiPane) getContainer()).revalidate();
		}
		adjustDividerProportion();
	}

	@Override
	public void removeGroup(MdiGroup group) {
		JSplitPane split = getSplits().get(group);
		JSplitPane preSplit = findPreSplit(group);
		JSplitPane nextSplit = findNextSplit(group);

		if (preSplit != null) {
			setNextSplit(preSplit, nextSplit);
		} else {

			// 没有上级 split 了，就把自己从 MdiPane 中移除
			((MdiPane) getContainer()).remove(split);
			if (nextSplit != null) {
				((MdiPane) getContainer()).add(nextSplit, BorderLayout.CENTER);
			}
		}
		super.removeGroup(group);
	}

	private JSplitPane findPreSplit(MdiGroup group) {
		int index = getContainer().indexOf(group);
		MdiGroup preGroup = index > 0 ? getContainer().getGroup(index - 1) : null;
		return preGroup == null ? null : getSplits().get(preGroup);
	}

	private JSplitPane findNextSplit(MdiGroup group) {
		int index = getContainer().indexOf(group);
		MdiGroup nextGroup = index > 0 && index + 1 < getContainer().getGroupCount() ? getContainer().getGroup(index + 1) : null;
		return nextGroup == null ? null : getSplits().get(nextGroup);
	}

	@Override
	public void layoutGroups() {
		MdiGroup[] groups = container.getGroups();
		if (groups != null && groups.length > 0) {
			for (int i = 0; i < groups.length; i++) {
				addGroup(groups[i]);
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		this.dividerProportions.clear();
		this.container.removeAll();
	}

	@Override
	protected JSplitPane createSplit(MdiGroup group) {
		final JSplitPane split = super.createSplit(group);
		split.setOrientation(getOrientaition(this.layoutMode));
		setDividerProportion(split, DEFAULT_DIVIDER_PROPORTION);
		split.setLeftComponent(group);
		resetDividerSize(split);

		if (split.getUI() instanceof BasicSplitPaneUI) {
			BasicSplitPaneDivider divider = ((BasicSplitPaneUI) split.getUI()).getDivider();
			divider.setBorder(null);

			divider.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// 当 Didiver 位置改变之后，记录一下 proportion，切换 LayoutMode 之后需要使用该值
					setDividerProportion(split, getDividerProportion(split));
				}
			});
		}
		return split;
	}

	private void resetDividerSize(JSplitPane splitPane) {
		splitPane.setDividerSize(splitPane.getRightComponent() == null ? 0 : 3);
	}

	private int getOrientaition(int layoutMode) {
		int orientation = JSplitPane.HORIZONTAL_SPLIT;
		if (layoutMode == HORIZONTAL || layoutMode < 1) {
			orientation = JSplitPane.HORIZONTAL_SPLIT;
		} else if (layoutMode == VERTICAL || layoutMode > 2) {
			orientation = JSplitPane.VERTICAL_SPLIT;
		}
		return orientation;
	}

	private void setDividerProportion(JSplitPane split, double dividerProportion) {
		if (split == null) {
			return;
		}

		this.dividerProportions.put(split, dividerProportion);
		split.setResizeWeight(dividerProportion);
	}

	private double getDividerProportion(JSplitPane splitPane) {
		if (splitPane == null) {
			return DEFAULT_DIVIDER_PROPORTION;
		}

		double proportion;
		int location = splitPane.getDividerLocation();
		if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
			proportion = (double) location / (splitPane.getHeight() - splitPane.getDividerSize());
		} else {
			proportion = (double) location / (splitPane.getWidth() - splitPane.getDividerSize());
		}
		return proportion;
	}

	private void setLayoutMode(JSplitPane split, int layoutMode) {
		split.setOrientation(getOrientaition(layoutMode));
		split.setDividerLocation(this.dividerProportions.get(split));
		split.setResizeWeight(this.dividerProportions.get(split));
		split.doLayout();
	}

	private void setNextSplit(JSplitPane split, JSplitPane nextSplit) {
		if (split == null) {
			return;
		}

		if (nextSplit != null) {
			split.setRightComponent(nextSplit);
		} else {
			split.setRightComponent(null);
		}
		resetDividerSize(split);
	}

	/**
	 * 调整 DividerProportion，为其计算一个合适的 DividerProportion
	 */
	private void adjustDividerProportion() {
		for (int i = 0; i < getContainer().getGroupCount(); i++) {
			setDividerProportion(getSplits().get(getContainer().getGroup(i)), 1d / (getContainer().getGroupCount() - i));
		}
	}
}
