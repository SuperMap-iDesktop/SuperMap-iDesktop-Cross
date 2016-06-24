package com.supermap.desktop.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class SmTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	private Vector<Boolean> closable;
	private Color colorNorth = new Color(255, 255, 255);
	private Color colorSouth = new Color(255, 255, 255);
	private JPanel parentPanel = null;

	/**
	 * 鏋勶拷?鏂规硶
	 */
	public SmTabbedPane(JPanel parentPanel) {
		super();
		this.parentPanel = parentPanel;
		initialize();
	}

	private void initialize() {
		closable = new Vector<Boolean>(0);
		setUI(new SmTabbedPaneUI(this.parentPanel));
	}

	/**
	 * 鍔犲叆缁勪欢
	 * 
	 * @param title 鏍囬
	 * @param icon 鍥炬爣
	 * @param component 缁勪欢
	 * @param tip 鎻愮ず淇℃伅
	 * @param closabel 鏄惁鍙叧锟�?
	 */
	@Override
	public void addTab(String title, Component component) {
		ImageIcon icon = new ImageIcon();
		addTab(title, icon, component, title);
		this.closable.add(true);
	}

	/**
	 * 绉婚櫎缁勪欢
	 * 
	 * @param index 缁勪欢搴忓彿
	 */
	public void removeTab(int index) {
		if (index == -1)
			return;
		super.removeTabAt(index);
		closable.remove(index);
	}

	/**
	 * UI瀹炵幇锟�?
	 * 
	 * @author Sun
	 */
	class SmTabbedPaneUI extends BasicTabbedPaneUI {
		private Rectangle[] closeRects = new Rectangle[0];
		private int nowIndex = -1;
		JPanel parentPanel = null;

		public SmTabbedPaneUI(JPanel parentPanel) {
			super();
			this.parentPanel = parentPanel;
			initialize();
		}

		private void initialize() {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						for (int i = 0; i < getTabCount(); i++) {
							if (closeRects[i].contains(e.getPoint()) && closable.get(i)) {
								removeTab(i);
							}
						}
					} else if (e.getButton() == MouseEvent.BUTTON2) {
						// 默认实现
					}
				}
			});
		}

		@Override
		protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
			Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);
			if (tabPlacement != TOP || selectedIndex < 0 || (selRect.y + selRect.height + 1 < y) || (selRect.x < x || selRect.x > x + w)) {
				g.drawLine(x, y, x + w - 2, y);
			} else {
				g.drawLine(x, y, selRect.x - 1, y);
				if (selRect.x + selRect.width < x + w - 2) {
					g.drawLine(selRect.x + selRect.width, y, x + w - 2, y);
				} else {
					g.drawLine(x + w - 2, y, x + w - 2, y);
				}
			}
		}

		@Override
		protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
			Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);
			if (tabPlacement != LEFT || selectedIndex < 0 || (selRect.x + selRect.width + 1 < x) || (selRect.y < y || selRect.y > y + h)) {
				g.drawLine(x, y, x, y + h - 2);
			} else {
				g.drawLine(x, y, x, selRect.y - 1);
				if (selRect.y + selRect.height < y + h - 2) {
					g.drawLine(x, selRect.y + selRect.height, x, y + h - 2);
				}
			}
		}

		@Override
		protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
			Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);
			if (tabPlacement != BOTTOM || selectedIndex < 0 || (selRect.y - 1 > h) || (selRect.x < x || selRect.x > x + w)) {
				g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
			} else {
				g.drawLine(x, y + h - 1, selRect.x - 1, y + h - 1);
				if (selRect.x + selRect.width < x + w - 2) {
					g.drawLine(selRect.x + selRect.width, y + h - 1, x + w - 1, y + h - 1);
				}
			}
		}

		@Override
		protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
			Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);
			if (tabPlacement != RIGHT || selectedIndex < 0 || (selRect.x - 1 > w) || (selRect.y < y || selRect.y > y + h)) {
				g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
			} else {
				g.drawLine(x + w - 1, y, x + w - 1, selRect.y - 1);
				if (selRect.y + selRect.height < y + h - 2) {
					g.drawLine(x + w - 1, selRect.y + selRect.height, x + w - 1, y + h - 2);
				}
			}
		}

		@Override
		protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
			super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
			if (closable.get(tabIndex) && tabIndex == getSelectedIndex()) {
				paintCloseIcon(g, tabIndex, tabIndex == nowIndex);
			}
		}

		@Override
		protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
			g.setColor(Color.GRAY);
			switch (tabPlacement) {
			case LEFT:
				setLeft(g, x, y, w, h);
				break;
			case RIGHT:
				setRight(g, x, y, w, h);
				break;
			case BOTTOM:
				setBottom(g, x, y, w, h);
				break;
			case TOP:
			default:
				setDefault(g, x, y, w, h);
			}
		}

		private void setDefault(Graphics g, int x, int y, int w, int h) {
			g.drawLine(x, y, x, y + h - 1);
			g.drawLine(x, y, x + w - 1, y);
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
		}

		private void setBottom(Graphics g, int x, int y, int w, int h) {
			g.drawLine(x, y, x, y + h - 1);
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
			g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
		}

		private void setRight(Graphics g, int x, int y, int w, int h) {
			g.drawLine(x, y, x + w - 1, y);
			g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
		}

		private void setLeft(Graphics g, int x, int y, int w, int h) {
			g.drawLine(x, y, x, y + h - 1);
			g.drawLine(x, y, x + w - 1, y);
			g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
		}

		@Override
		protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect,
				boolean isSelected) {
			// 默认实现，后续进行初始化操作

		}

		@Override
		protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
			Graphics2D g2d = (Graphics2D) g;
			switch (tabPlacement) {
			case LEFT:
				setCaseLeft(g, x, y, w, h, isSelected, g2d);
				break;
			case RIGHT:
				setCaseRight(g, x, y, w, h, isSelected, g2d);
				break;
			case BOTTOM:
				setCaseBottom(g, x, y, w, h, isSelected, g2d);
				break;
			case TOP:
			default:
				setCaseDefault(x, y, w, h, isSelected, g2d);
			}
		}

		private void setCaseDefault(int x, int y, int w, int h, boolean isSelected, Graphics2D g2d) {
			GradientPaint gradient;
			if (isSelected) {
				gradient = new GradientPaint(x + 1, y, colorNorth, x + 1, y + h, colorSouth, true);
			} else {
				gradient = new GradientPaint(x + 1, y, Color.LIGHT_GRAY, x + 1, y + h, Color.WHITE, true);
			}
			g2d.setPaint(gradient);
			g2d.fillRect(x + 1, y + 1, w - 2, h - 1);
		}

		private void setCaseBottom(Graphics g, int x, int y, int w, int h, boolean isSelected, Graphics2D g2d) {
			GradientPaint gradient;
			if (isSelected) {
				gradient = new GradientPaint(x + 1, y + h, colorNorth, x + 1, y, colorSouth, true);
			} else {
				gradient = new GradientPaint(x + 1, y + h, Color.LIGHT_GRAY, x + 1, y, Color.WHITE, true);
			}
			g2d.setPaint(gradient);
			g.fillRect(x + 1, y, w - 2, h - 1);
		}

		private void setCaseLeft(Graphics g, int x, int y, int w, int h, boolean isSelected, Graphics2D g2d) {
			GradientPaint gradient;
			if (isSelected) {
				gradient = new GradientPaint(x + 1, y, colorNorth, x + w, y, colorSouth, true);
			} else {
				gradient = new GradientPaint(x + 1, y, Color.LIGHT_GRAY, x + w, y, Color.WHITE, true);
			}
			g2d.setPaint(gradient);
			g.fillRect(x + 1, y + 1, w - 1, h - 2);
		}

		private void setCaseRight(Graphics g, int x, int y, int w, int h, boolean isSelected, Graphics2D g2d) {
			GradientPaint gradient;
			if (isSelected) {
				gradient = new GradientPaint(x + w, y, colorNorth, x + 1, y, colorSouth, true);
			} else {
				gradient = new GradientPaint(x + w, y, Color.LIGHT_GRAY, x + 1, y, Color.WHITE, true);
			}
			g2d.setPaint(gradient);
			g.fillRect(x, y + 1, w - 1, h - 2);
		}

		private void paintCloseIcon(Graphics g, int tabIndex, boolean entered) {
			Rectangle rect = closeRects[tabIndex];
			int x = rect.x;
			int y = rect.y;
			int[] xs = { x, x + 2, x + 4, x + 5, x + 7, x + 9, x + 9, x + 7, x + 7, x + 9, x + 9, x + 7, x + 5, x + 4, x + 2, x, x, x + 2, x + 2, x };
			int[] ys = { y, y, y + 2, y + 2, y, y, y + 2, y + 4, y + 5, y + 7, y + 9, y + 9, y + 7, y + 7, y + 9, y + 9, y + 7, y + 5, y + 4, y + 2 };
			if (entered) {
				g.setColor(new Color(252, 160, 160));
			} else {
				g.setColor(Color.WHITE);
			}
			g.fillPolygon(xs, ys, 20);
			g.setColor(Color.DARK_GRAY);
			g.drawPolygon(xs, ys, 20);
		}

		@Override
		protected void layoutLabel(int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon, Rectangle tabRect, Rectangle iconRect,
				Rectangle textRect, boolean isSelected) {
			textRect.x = textRect.y = iconRect.x = iconRect.y = 0;
			View v = getTextViewForTab(tabIndex);
			if (v != null) {
				tabPane.putClientProperty("html", v);
			}
			SwingUtilities.layoutCompoundLabel((JComponent) tabPane, metrics, title, icon, SwingUtilities.CENTER, SwingUtilities.CENTER, SwingUtilities.CENTER,
					SwingUtilities.TRAILING, tabRect, iconRect, textRect, textIconGap);
			tabPane.putClientProperty("html", null);
		}

		@Override
		protected LayoutManager createLayoutManager() {
			return new JTabbedPaneLayout();
		}

		@Override
		protected void assureRectsCreated(int tabCount) {
			super.assureRectsCreated(tabCount);
			int rectArrayLen = closeRects.length;
			if (tabCount != rectArrayLen) {
				Rectangle[] tempRectArray = new Rectangle[tabCount];
				System.arraycopy(closeRects, 0, tempRectArray, 0, Math.min(rectArrayLen, tabCount));
				closeRects = tempRectArray;
				for (int rectIndex = rectArrayLen; rectIndex < tabCount; rectIndex++) {
					closeRects[rectIndex] = new Rectangle();
				}
			}
		}

		class JTabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
			@Override
			protected void calculateTabRects(int tabPlacement, int tabCount) {
				FontMetrics metrics = getFontMetrics();
				Dimension size = tabPane.getSize();
				Insets insets = tabPane.getInsets();
				Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
				int fontHeight = metrics.getHeight();
				int selectedIndex = tabPane.getSelectedIndex();
				int tabRunOverlay;
				int i, j;
				int x, y;
				int returnAt;
				boolean verticalTabRuns = tabPlacement == LEFT || tabPlacement == RIGHT;

				switch (tabPlacement) {
				case LEFT:
					maxTabWidth = calculateMaxTabWidth(tabPlacement) + 20;
					x = insets.left + tabAreaInsets.left;
					y = insets.top + tabAreaInsets.top;
					returnAt = size.height - (insets.bottom + tabAreaInsets.bottom);
					break;
				case RIGHT:
					maxTabWidth = calculateMaxTabWidth(tabPlacement) + 20;
					x = size.width - insets.right - tabAreaInsets.right - maxTabWidth;
					y = insets.top + tabAreaInsets.top;
					returnAt = size.height - (insets.bottom + tabAreaInsets.bottom);
					break;
				case BOTTOM:
					maxTabHeight = calculateMaxTabHeight(tabPlacement);
					x = insets.left + tabAreaInsets.left;
					y = size.height - insets.bottom - tabAreaInsets.bottom - maxTabHeight;
					returnAt = size.width - (insets.right + tabAreaInsets.right);
					break;
				case TOP:
				default:
					maxTabHeight = calculateMaxTabHeight(tabPlacement);
					x = insets.left + tabAreaInsets.left;
					y = insets.top + tabAreaInsets.top;
					returnAt = size.width - (insets.right + tabAreaInsets.right);
					break;
				}

				tabRunOverlay = getTabRunOverlay(tabPlacement);
				runCount = 0;
				selectedRun = -1;
				if (tabCount == 0) {
					return;
				}
				selectedRun = 0;
				runCount = 1;
				Rectangle rect;
				for (i = 0; i < tabCount; i++) {
					rect = rects[i];
					if (!verticalTabRuns) {
						// Tabs on TOP or BOTTOM....
						if (i > 0) {
							rect.x = rects[i - 1].x + rects[i - 1].width;
						} else {
							tabRuns[0] = 0;
							runCount = 1;
							maxTabWidth = 0;
							rect.x = x;
						}
						rect.width = calculateTabWidth(tabPlacement, i, metrics) + 20;
						maxTabWidth = Math.max(maxTabWidth, rect.width);
						if (rect.x != 2 + insets.left && rect.x + rect.width > returnAt) {
							if (runCount > tabRuns.length - 1) {
								expandTabRunsArray();
							}
							tabRuns[runCount] = i;
							runCount++;
							rect.x = x;
						}
						rect.y = y;
						rect.height = maxTabHeight/* - 2 */;

					} else {
						// Tabs on LEFT or RIGHT...
						if (i > 0) {
							rect.y = rects[i - 1].y + rects[i - 1].height;
						} else {
							tabRuns[0] = 0;
							runCount = 1;
							maxTabHeight = 0;
							rect.y = y;
						}
						rect.height = calculateTabHeight(tabPlacement, i, fontHeight);
						maxTabHeight = Math.max(maxTabHeight, rect.height);
						if (rect.y != 2 + insets.top && rect.y + rect.height > returnAt) {
							if (runCount > tabRuns.length - 1) {
								expandTabRunsArray();
							}
							tabRuns[runCount] = i;
							runCount++;
							rect.y = y;
						}
						rect.x = x;
						rect.width = maxTabWidth/* - 2 */;
					}
					if (i == selectedIndex) {
						selectedRun = runCount - 1;
					}
				}
				if (runCount > 1) {
					normalizeTabRuns(tabPlacement, tabCount, verticalTabRuns ? y : x, returnAt);
					selectedRun = getRunForTab(tabCount, selectedIndex);
					if (shouldRotateTabRuns(tabPlacement)) {
						rotateTabRuns(tabPlacement, selectedRun);
					}
				}
				for (i = runCount - 1; i >= 0; i--) {
					int start = tabRuns[i];
					int next = tabRuns[i == (runCount - 1) ? 0 : i + 1];
					int end = next != 0 ? next - 1 : tabCount - 1;
					if (!verticalTabRuns) {
						for (j = start; j <= end; j++) {
							rect = rects[j];
							rect.y = y;
							rect.x += getTabRunIndent(tabPlacement, i);
						}
						if (shouldPadTabRun(tabPlacement, i)) {
							padTabRun(tabPlacement, start, end, returnAt);
						}
						if (tabPlacement == BOTTOM) {
							y -= (maxTabHeight - tabRunOverlay);
						} else {
							y += (maxTabHeight - tabRunOverlay);
						}
					} else {
						for (j = start; j <= end; j++) {
							rect = rects[j];
							rect.x = x;
							rect.y += getTabRunIndent(tabPlacement, i);
						}
						if (shouldPadTabRun(tabPlacement, i)) {
							padTabRun(tabPlacement, start, end, returnAt);
						}
						if (tabPlacement == RIGHT) {
							x -= (maxTabWidth - tabRunOverlay);
						} else {
							x += (maxTabWidth - tabRunOverlay);
						}
					}
				}
				for (i = 0; i < tabCount; i++) {
					closeRects[i].x = rects[i].x + rects[i].width - 14;
					closeRects[i].y = rects[i].y + 6;
					closeRects[i].width = 10;
					closeRects[i].height = 10;
				}
			}
		}
	}
}