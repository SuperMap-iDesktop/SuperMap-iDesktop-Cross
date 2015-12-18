package com.supermap.desktop.ui.controls;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.utilties.PathUtilties;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * 颜色下拉选择器
 * 
 * @author xuzw
 * 
 */
public class ColorsComboBox extends JComboBox {
	private static final long serialVersionUID = 1L;

	private ColorsCellRenderer colorsCellRenderer;

	private int colorsCount;

	private transient ActionListener customColorsListener;

	private transient Colors customColors;

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
		this.setSelectedIndex(21);
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
			// 依据base库来找Bin
			String tempPath = "../Templates/ColorScheme/Basic/";
			String path = PathUtilties.getFullPathName(tempPath, true);
			File file = new File(path);
			String[] fileNames = file.list();
			ColorScheme colorScheme = new ColorScheme();
			for (int i = 0; i < fileNames.length; i++) {
				File temp = new File(path + fileNames[i]);
				boolean b = colorScheme.fromXML(temp);
				if (b) {
					ArrayList<Color> list = colorScheme.getColors();
					Color[] gradientColors = new Color[list.size()];
					list.toArray(gradientColors);
					int intervalColorCount = colorScheme.getIntervalColorCount();
					if (intervalColorCount < 2) {
						intervalColorCount = 16;
					}
					Colors colors = null;
					ColorScheme.IntervalColorBuildMethod method = colorScheme.getIntervalColorBuildMethod();
					if (method.equals(ColorScheme.IntervalColorBuildMethod.ICBM_GRADIENT)) {
						colors = Colors.makeGradient(intervalColorCount + colorScheme.getKeyColorCount(), gradientColors);
					} else if (method.equals(ColorScheme.IntervalColorBuildMethod.ICBM_RANDOM)) {
						colors = buildRandom(intervalColorCount + colorScheme.getKeyColorCount(), gradientColors);
					}
					this.addItem(colors);
				}
			}
			customColors = Colors.makeGradient(getColorsCount(), ColorGradientType.RAINBOW, false);
			this.addItem(customColors);
			this.addActionListener(customColorsListener);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 生成随机色，照.net桌面写了一部分内部计算量还是很大的
	 * 
	 * @param colorCount
	 * @param keyColors
	 * @return
	 */
	private static Colors buildRandom(int colorCount, Color[] keyColors) {
		Colors colors = new Colors();
		// 要产生的颜色比关键色还要少，太可怜了
		// 就不再进行随机色生成了，直接将随机色扔出去算了
		if (colorCount <= keyColors.length) {
			for (int index = 0; index < colorCount; index++) {
				colors.add(keyColors[index]);
			}
		} else {
			// 按照关键色计算每段需要生成多少随机色
			int colorCountPerSection = (int) (Math.floor(colorCount * 1.0 / (keyColors.length - 1)));
			int colorCountLastSection = colorCount - colorCountPerSection * (keyColors.length - 1);
			for (int index = 0; index < (keyColors.length - 1); index++) {
				Color[] temp = { keyColors[index], keyColors[index + 1] };
				colors.addRange(Colors.makeGradient(colorCountPerSection, temp).toArray());
			}
			if (colorCountLastSection > 0) {
				Color[] temp = { keyColors[keyColors.length - 2], keyColors[keyColors.length - 1] };
				colors.addRange(Colors.makeGradient(colorCountLastSection, temp).toArray());
			}
		}

		return colors;
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
