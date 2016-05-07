package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.DialogResult;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
			initComboBox();
		}
	};

	private ColorsCellRenderer colorsCellRenderer;

	private int colorsCount;

	private transient ActionListener customColorsListener;

	private transient Colors customColors;

	private static final String defaultColorName = "DefaultGrid";

	private int selectCount;


	/**
	 * 构造函数
	 */
	public ColorsComboBox() {
		super();
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				((BasicComboBoxUI) ColorsComboBox.this.getUI()).paintCurrentValue(getGraphics(), getBounds(), true);
			}
		});
		colorsCellRenderer = new ColorsCellRenderer(this);
		this.setRenderer(colorsCellRenderer);
		colorsCount = 32;
		customColorsListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getSelectedIndex() != -1 && getSelectedIndex() == getItemCount() - 1) {
					final ColorSchemeEditorDialog dialog = new ColorSchemeEditorDialog(customColors);
					dialog.setVisible(true);
					DialogResult dialogResult = dialog.getResult();
					if (dialogResult.equals(DialogResult.APPLY)) {
						customColors = dialog.getResultColors();
						removeItemAt(getItemCount() - 1);
						addItem(customColors);
						setSelectedItem(customColors);
					}
					dialog.dispose();
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
	 * 返回一个单元格中要生成渐变色的颜色总数
	 *
	 * @return
	 */
	public int getColorsCount() {
		return colorsCount;
	}

	/**
	 * 设置一个单元格中要生成渐变色的颜色总数
	 *
	 * @param count
	 */
	public void setColorsCount(int count) {
		colorsCount = count;
		customColors = Colors.makeGradient(count, ColorGradientType.RAINBOW, false);
	}


	/**
	 * 根据xml配置文件来初始化JComboBox
	 */
	private void initComboBox() {
		try {
			this.removeAllItems();
			ColorSchemeManager colorSchemeManager = ColorSchemeManager.getColorSchemeManager();
			java.util.List<ColorScheme> colorSchemeList = colorSchemeManager.getColorSchemeList();
			for (int i = 0; i < colorSchemeList.size(); i++) {
				if (colorSchemeList.get(i).getName().equals(defaultColorName)) {
					selectCount = i;
				}
				this.addItem(colorSchemeList.get(i).getColors());
			}
			customColors = Colors.makeGradient(getColorsCount(), ColorGradientType.RAINBOW, false);
			this.addItem(customColors);
			this.addActionListener(customColorsListener);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	public void dispose() {
		ColorSchemeManager.getColorSchemeManager().removeColorSchemeManagerChangedListener(colorSchemeManagerChangedListener);
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
