package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.DialogResult;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * 颜色下拉选择器
 *
 * @author xuzw
 */
public class ColorsComboBox extends JComboBox {
	private static final long serialVersionUID = 1L;

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
		colorsCellRenderer = new ColorsCellRenderer();
		this.setRenderer(colorsCellRenderer);
		colorsCount = 32;
		customColorsListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getSelectedIndex() == getItemCount() - 1) {
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


}

class ColorsCellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;

	public ColorsCellRenderer() {
		setOpaque(true);
		this.setPreferredSize(new Dimension(270, 20));
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Colors colors = (Colors) value;
		int imageWidth = this.getPreferredSize().width;
		int imageHeight = this.getPreferredSize().height;
		BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

		// 根据当前渲染单元格的宽度和颜色数计算出每个颜色应当渲染的步长
		if (colors != null) {
			int colorsCount = colors.getCount();
			int step = imageWidth / colorsCount;
			for (int i = 0; i < colorsCount; i++) {
				graphics.setColor(colors.get(i));
				graphics.fillRect(step * i, 0, step * (i + 1), imageHeight);
			}
		}
		this.setIcon(new ImageIcon(bufferedImage));

		// 最后一项为自定义颜色，其余的不需要文本标签
		if (index == list.getModel().getSize() - 1) {
			this.setText(ControlsProperties.getString("String_CustomColor"));
		} else {
			this.setText("");
		}
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		if (isSelected) {
			this.setBorder(new LineBorder(Color.black, 1, false));
		} else {
			this.setBorder(new LineBorder(Color.white, 1, false));
		}
		return this;
	}
}
