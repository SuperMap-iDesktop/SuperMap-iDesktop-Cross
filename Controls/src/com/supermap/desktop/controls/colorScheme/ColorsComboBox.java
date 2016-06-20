package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ColorsUIUtilities;
import com.supermap.desktop.ui.controls.DialogResult;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 颜色下拉选择器
 *
 * @author xuzw
 */
public class ColorsComboBox extends JComboBox {
	private static final long serialVersionUID = 1L;
	private final ColorSchemeManagerChangedListener colorSchemeManagerChangedListener = new ColorSchemeManagerChangedListener() {
		@Override
		public void colorSchemeManagerChanged(ColorSchemeManagerChangedEvent colorSchemeManagerChangedEvent) {
			int selectedIndex = getSelectedIndex();
			int itemCount = getItemCount();
			Colors selectedItem = ((Colors) getSelectedItem());
			initComboBox();
			if (selectedItem != null) {
				if (itemCount - 1 == selectedIndex) {
					setSelectedIndex(getItemCount() - 2);
				} else {
					ItemListener[] itemListeners = ColorsComboBox.this.getItemListeners();
					removeItemListeners(itemListeners);
					for (int i = 0; i < getItemCount(); i++) {
						if (ColorsUIUtilities.isEqualsColors((Colors) getItemAt(i), selectedItem)) {
							setSelectedIndex(i);
							addItemListeners(itemListeners);
							return;
						}
					}
					if (selectedIndex < getItemCount() - 1) {
						setSelectedIndex(selectedIndex);
					}
					addItemListeners(itemListeners);
				}
			}
		}
	};

	private ColorsCellRenderer colorsCellRenderer;


	private transient Colors customColors;

	private static final String defaultColorName = "DefaultGrid";

	private int selectCount;
	private boolean isAddItem = false;
	private boolean isSelectedUserDefineItem = false;
	private int lastSelectedIndex;


	/**
	 * 构造函数
	 */
	public ColorsComboBox() {
		super();
		colorsCellRenderer = new ColorsCellRenderer(this);
		this.setRenderer(colorsCellRenderer);

		ColorSchemeManager.getColorSchemeManager().addColorSchemeManagerChangedListener(colorSchemeManagerChangedListener);
		initComboBox();
		this.setMaximumRowCount(20);
		this.setSelectedIndex(selectCount);
	}

	private void showUserDefineDialog() {
		ColorSchemeEditorDialog dialog = new ColorSchemeEditorDialog();
		if (dialog.showDialog() == DialogResult.OK) {
			ColorSchemeManager.getColorSchemeManager().addColorScheme(dialog.getColorScheme());
			setSelectedIndex(getItemCount() - 2);
		}
	}

	/**
	 * 返回渲染单元格的渲染大小
	 *
	 * @return
	 */
	public Dimension getColorRendererSize() {
		return colorsCellRenderer.getPreferredSize();
	}

	/**
	 * 设置渲染单元格的渲染大小
	 *
	 * @param dimension
	 */
	public void setColorRendererSize(Dimension dimension) {
		colorsCellRenderer.setPreferredSize(dimension);
	}


	/**
	 * 根据xml配置文件来初始化JComboBox
	 */
	private void initComboBox() {
		try {
			this.removeAllItems();
			ItemListener[] itemListeners = this.getItemListeners();
			removeItemListeners(itemListeners);
			ColorSchemeManager colorSchemeManager = ColorSchemeManager.getColorSchemeManager();
			java.util.List<ColorScheme> colorSchemeList = colorSchemeManager.getColorSchemeList();
			for (int i = 0; i < colorSchemeList.size(); i++) {
				if (colorSchemeList.get(i).getName().equals(defaultColorName)) {
					selectCount = i;
				}
				this.addItem(colorSchemeList.get(i).getColors());
			}
			customColors = Colors.makeGradient(32, ColorGradientType.RAINBOW, false);
			this.addItem(customColors);
			addItemListeners(itemListeners);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void addItemListeners(ItemListener[] itemListeners) {
		for (ItemListener itemListener : itemListeners) {
			this.addItemListener(itemListener);
		}
	}

	private void removeItemListeners(ItemListener[] itemListeners) {
		for (ItemListener itemListener : itemListeners) {
			this.removeItemListener(itemListener);
		}
	}

	public void dispose() {
		removeColorChangedListener();
	}

	/**
	 * 移除颜色方案改变时的监听事件，使用完一定要记得移除
	 */
	public void removeColorChangedListener() {
		ColorSchemeManager.getColorSchemeManager().removeColorSchemeManagerChangedListener(colorSchemeManagerChangedListener);
	}

	/**
	 * 添加颜色方案改变时的监听事件
	 */
	public void addColorChangedListener() {
		removeColorChangedListener();
		ColorSchemeManager.getColorSchemeManager().addColorSchemeManagerChangedListener(colorSchemeManagerChangedListener);
	}

	@Override
	public void addItem(Object item) {
		isAddItem = true;
		super.addItem(item);
		isAddItem = false;
	}

	@Override
	protected void fireItemStateChanged(ItemEvent e) {
		if (isSelectedUserDefineItem) {
			// 选中最后一项时置空，这个时候不发送改变事件
			return;
		}
		if (isAddItem || e.getStateChange() == ItemEvent.DESELECTED || e.getItem() != getItemAt(getItemCount() - 1)) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				lastSelectedIndex = getSelectedIndex();
			}
			super.fireItemStateChanged(e);
		} else {
			isSelectedUserDefineItem = true;
			if (getItemCount() > lastSelectedIndex) {
				this.setSelectedIndex(lastSelectedIndex);
			} else {
				this.setSelectedIndex(-1);
			}
			this.setPopupVisible(false);
			isSelectedUserDefineItem = false;
			showUserDefineDialog();
		}
	}
}

class ColorsCellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;
	private final ColorsComboBox colorsComboBox;

	public ColorsCellRenderer(ColorsComboBox colorsComboBox) {
		this.colorsComboBox = colorsComboBox;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Colors colors = (Colors) value;
		int imageWidth = colorsComboBox.getWidth() - colorsComboBox.getComponent(0).getWidth();
		int imageHeight = 24;
		JLabel colorsLabel = ColorScheme.getColorsLabel(colors, imageWidth, imageHeight);
		if (index == list.getModel().getSize() - 1) {
			colorsLabel.setText(ControlsProperties.getString("String_CustomColor"));
		} else {
			colorsLabel.setText("");
		}
		if (isSelected) {
			colorsLabel.setBorder(new LineBorder(Color.black, 1, false));
		} else {
			colorsLabel.setBorder(new LineBorder(Color.white, 1, false));
		}
		return colorsLabel;
	}
}
