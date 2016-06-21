package com.supermap.desktop.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
class ToolBarContainer extends Container {
	// 包内使用，暂不开放
	private int index = -1;
	private static java.util.List<ToolBarContainer> lists = new ArrayList<>();

	private ToolBarContainer(int index) {
		super();
		this.index = index;
		this.setLayout(new ToolBarLayout(FlowLayout.LEADING, 0, 5));
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				ToolBarContainer.this.getParent().doLayout();
			}
		});
	}

	public int getIndex() {
		return index;
	}

	public static ToolBarContainer getToolBarContainer(int index) {
		for (ToolBarContainer list : lists) {
			if (list.getIndex() == index) {
				return list;
			}
		}
		ToolBarContainer toolBarContainer = new ToolBarContainer(index);
		lists.add(toolBarContainer);
		return toolBarContainer;
	}

	public static java.util.List<ToolBarContainer> getContainerS() {
		return lists;
	}

	class ToolBarLayout extends FlowLayout {
		/**
		 * Constructs a new <code>WrapLayout</code> with a left
		 * alignment and a default 5-unit horizontal and vertical gap.
		 */
		public ToolBarLayout() {
			super();
		}

		/**
		 * Constructs a new <code>FlowLayout</code> with the specified
		 * alignment and a default 5-unit horizontal and vertical gap.
		 * The value of the alignment argument must be one of
		 * <code>WrapLayout</code>, <code>WrapLayout</code>,
		 * or <code>WrapLayout</code>.
		 *
		 * @param align the alignment value
		 */
		public ToolBarLayout(int align) {
			super(align);
		}

		/**
		 * Creates a new flow layout manager with the indicated alignment
		 * and the indicated horizontal and vertical gaps.
		 * <p/>
		 * The value of the alignment argument must be one of
		 * <code>WrapLayout</code>, <code>WrapLayout</code>,
		 * or <code>WrapLayout</code>.
		 *
		 * @param align the alignment value
		 * @param hgap  the horizontal gap between components
		 * @param vgap  the vertical gap between components
		 */
		public ToolBarLayout(int align, int hgap, int vgap) {
			super(align, hgap, vgap);
		}

		/**
		 * Returns the preferred dimensions for this layout given the
		 * <i>visible</i> components in the specified target container.
		 *
		 * @param target the component which needs to be laid out
		 * @return the preferred dimensions to lay out the
		 * subcomponents of the specified container
		 */
		@Override
		public Dimension preferredLayoutSize(Container target) {
			return layoutSize(target, true);
		}

		/**
		 * Returns the minimum dimensions needed to layout the <i>visible</i>
		 * components contained in the specified target container.
		 *
		 * @param target the component which needs to be laid out
		 * @return the minimum dimensions to lay out the
		 * subcomponents of the specified container
		 */
		@Override
		public Dimension minimumLayoutSize(Container target) {
			Dimension minimum = layoutSize(target, true);
			minimum.width -= (getHgap() + 1);
			return minimum;
		}

		/**
		 * Returns the minimum or preferred dimension needed to layout the target
		 * container.
		 *
		 * @param target    target to get layout size for
		 * @param preferred should preferred size be calculated
		 * @return the dimension to layout the target container
		 */
		private Dimension layoutSize(Container target, boolean preferred) {
			synchronized (target.getTreeLock()) {
				//  Each row must fit with the width allocated to the containter.
				//  When the container width = 0, the preferred width of the container
				//  has not yet been calculated so lets ask for the maximum.

				int targetWidth = target.getSize().width;
				Container container = target;

				while (container.getSize().width == 0 && container.getParent() != null) {
					container = container.getParent();
				}

				targetWidth = container.getSize().width;

				if (targetWidth == 0)
					targetWidth = Integer.MAX_VALUE;

				int hgap = getHgap();
				int vgap = getVgap();
				Insets insets = target.getInsets();
				int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
				int maxWidth = targetWidth - horizontalInsetsAndGap;

				//  Fit components into the allowed width

				Dimension dim = new Dimension(0, 0);
				int rowWidth = 0;
				int rowHeight = 0;

				int nmembers = target.getComponentCount();

				for (int i = 0; i < nmembers; i++) {
					Component m = target.getComponent(i);

					if (m.isVisible()) {

						Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

						//  Can't add the component to current row. Start a new row.

						if (rowWidth + m.getMinimumSize().getWidth() > maxWidth) {
							addRow(dim, rowWidth, rowHeight);
							rowWidth = 0;
							rowHeight = 0;
						}
						//  Add a horizontal gap for all components after the first

						if (rowWidth != 0) {
							rowWidth += hgap;
						}

						rowWidth += d.width;
						rowHeight = Math.max(rowHeight, d.height);
					}
				}

				addRow(dim, rowWidth, rowHeight);

				dim.width += horizontalInsetsAndGap;
				dim.height += insets.top + insets.bottom + vgap * 2;

				//	When using a scroll pane or the DecoratedLookAndFeel we need to
				//  make sure the preferred size is less than the size of the
				//  target containter so shrinking the container size works
				//  correctly. Removing the horizontal gap is an easy way to do this.

				Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);

				if (scrollPane != null && target.isValid()) {
					dim.width -= (hgap + 1);
				}

				return dim;
			}
		}

		/*
		 *  A new row has been completed. Use the dimensions of this row
		 *  to update the preferred size for the container.
		 *
		 *  @param dim update the width and height when appropriate
		 *  @param rowWidth the width of the row to add
		 *  @param rowHeight the height of the row to add
		 */
		private void addRow(Dimension dim, int rowWidth, int rowHeight) {
			dim.width = Math.max(dim.width, rowWidth);

			if (dim.height > 0) {
				dim.height += getVgap();
			}

			dim.height += rowHeight;
		}

		@Override
		public void layoutContainer(Container target) {
			synchronized (target.getTreeLock()) {
				Insets insets = target.getInsets();
				int maxwidth = target.getWidth() - (insets.left + insets.right + getHgap() * 2);
				int nmembers = target.getComponentCount();
				int x = 0, y = insets.top + getVgap();
				int rowh = 0, start = 0;

				boolean ltr = target.getComponentOrientation().isLeftToRight();

				boolean useBaseline = getAlignOnBaseline();
				int[] ascent = null;
				int[] descent = null;

				if (useBaseline) {
					ascent = new int[nmembers];
					descent = new int[nmembers];
				}

				for (int i = 0; i < nmembers; i++) {
					Component m = target.getComponent(i);
					if (m.isVisible()) {
						Dimension d = m.getPreferredSize();
						m.setSize(d.width, d.height);

						if (useBaseline) {
							int baseline = m.getBaseline(d.width, d.height);
							if (baseline >= 0) {
								ascent[i] = baseline;
								descent[i] = d.height - baseline;
							} else {
								ascent[i] = -1;
							}
						}
						if ((x + d.width) <= maxwidth) {
							if (x > 0) {
								x += getHgap();
							}
							x += d.width;
							rowh = Math.max(rowh, d.height);
						} else {
							// 大小不够时不换行改为缩短尺寸
							if (x + m.getMinimumSize().getWidth() < maxwidth) {
								m.setSize(maxwidth - x, d.height);
								x += d.width;
								rowh = Math.max(rowh, d.height);
							} else {
								if (m.getPreferredSize().getWidth() > maxwidth) {
									// 换行前先做判断
									m.setSize(maxwidth, d.height);
								}
								rowh = moveComponents(target, insets.left + getHgap(), y,
										maxwidth - x, rowh, start, i, ltr,
										useBaseline, ascent, descent);
								x = d.width;
								y += getVgap() + rowh;
								rowh = d.height;
								start = i;
							}
						}
					}
				}
				moveComponents(target, insets.left + getHgap(), y, maxwidth - x, rowh,
						start, nmembers, ltr, useBaseline, ascent, descent);
			}
		}

		private int moveComponents(Container target, int x, int y, int width, int height,
		                           int rowStart, int rowEnd, boolean ltr,
		                           boolean useBaseline, int[] ascent,
		                           int[] descent) {
			switch (this.getAlignment()) {
				case LEFT:
					x += ltr ? 0 : width;
					break;
				case CENTER:
					x += width / 2;
					break;
				case RIGHT:
					x += ltr ? width : 0;
					break;
				case LEADING:
					break;
				case TRAILING:
					x += width;
					break;
			}
			int maxAscent = 0;
			int nonbaselineHeight = 0;
			int baselineOffset = 0;
			if (useBaseline) {
				int maxDescent = 0;
				for (int i = rowStart; i < rowEnd; i++) {
					Component m = target.getComponent(i);
					if (m.isVisible()) {
						if (ascent[i] >= 0) {
							maxAscent = Math.max(maxAscent, ascent[i]);
							maxDescent = Math.max(maxDescent, descent[i]);
						} else {
							nonbaselineHeight = Math.max(m.getHeight(),
									nonbaselineHeight);
						}
					}
				}
				height = Math.max(maxAscent + maxDescent, nonbaselineHeight);
				baselineOffset = (height - maxAscent - maxDescent) / 2;
			}
			for (int i = rowStart; i < rowEnd; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					int cy;
					if (useBaseline && ascent[i] >= 0) {
						cy = y + baselineOffset + maxAscent - ascent[i];
					} else {
						cy = y + (height - m.getHeight()) / 2;
					}
					if (ltr) {
						m.setLocation(x, cy);
					} else {
						m.setLocation(target.getWidth() - x - m.getWidth(), cy);
					}
					x += m.getWidth() + getHgap();
				}
			}
			return height;
		}
	}
}