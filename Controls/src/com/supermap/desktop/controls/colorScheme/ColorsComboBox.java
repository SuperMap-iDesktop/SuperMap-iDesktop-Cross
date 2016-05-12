package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilties.ColorsUtilties;
import com.supermap.desktop.ui.controls.DialogResult;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
			Colors selectedItem = ((Colors) getSelectedItem());
			initComboBox();
			if (selectedItem != null) {
				if (selectedItem == customColors) {
					setSelectedIndex(getItemCount() - 2);
				} else {
					for (int i = 0; i < getItemCount(); i++) {
						if (ColorsUtilties.isEqualsColors((Colors) getItemAt(i), selectedItem)) {
							setSelectedIndex(i);
							return;
						}
					}
					if (selectedIndex < getItemCount() - 1) {
						setSelectedIndex(selectedIndex);
					}
				}
			}
		}
	};

	private ColorsCellRenderer colorsCellRenderer;

	private transient ActionListener customColorsListener;

	private transient Colors customColors;

	private static final String defaultColorName = "DefaultGrid";

	private int selectCount;


	/**
	 * 构造函数
	 */
	public ColorsComboBox() {
		super();
		colorsCellRenderer = new ColorsCellRenderer(this);
		this.setRenderer(colorsCellRenderer);
		customColorsListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getSelectedIndex() != -1 && getSelectedIndex() == getItemCount() - 1) {
					ColorSchemeEditorDialog dialog = new ColorSchemeEditorDialog();
					if (dialog.showDialog() == DialogResult.OK) {
						ColorSchemeManager.getColorSchemeManager().addColorScheme(dialog.getColorScheme());
						setSelectedIndex(getItemCount() - 2);
					}
				}
			}

		};

		ColorSchemeManager.getColorSchemeManager().addColorSchemeManagerChangedListener(colorSchemeManagerChangedListener);
		initComboBox();
		this.setMaximumRowCount(20);
		this.setSelectedIndex(selectCount);
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
			this.removeActionListener(customColorsListener);
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
			this.addActionListener(customColorsListener);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
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
		int imageHeight = this.getPreferredSize().height;
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
