package com.supermap.desktop.ui.controls;

import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import com.supermap.desktop.properties.CommonProperties;

/**
 * 字体控件
 * 
 * @author XiaJT
 *
 */
public class SmJComboBoxFontChoose extends JComboBox {

	/**
	 * 默认字体风格
	 */
	public static final int DEFAULT_FONT_STYLE = Font.PLAIN;
	/**
	 * 默认字体大小
	 */
	public static final int DEFAULT_FONT_SIZE = 16;

	/**
	 * 当字体不支持显示时使用默认使用的字体
	 */
	private String defaultUseFont;
	/**
	 * 字体的风格
	 */
	private int fontStyle;
	/**
	 * 字体的大小
	 */
	private int fontSize;
	/**
	 * 字体的历史记录，添加在最上方(未实现)
	 */
	private String[] historyFonts;

	/**
	 * 创建使用默认值的字体控件
	 */
	public SmJComboBoxFontChoose() {
		super();
		this.defaultUseFont = CommonProperties.getString("String_DefaultUseFont");
		this.fontStyle = DEFAULT_FONT_STYLE;
		this.fontSize = DEFAULT_FONT_SIZE;
		init();

	}

	/**
	 * 创建带历史字体的字体控件
	 * 
	 * @param historyFonts 历史字体
	 */
	public SmJComboBoxFontChoose(String[] historyFonts) {
		super();
		this.historyFonts = historyFonts;
		this.defaultUseFont = CommonProperties.getString("String_DefaultUseFont");
		this.fontStyle = DEFAULT_FONT_STYLE;
		this.fontSize = DEFAULT_FONT_SIZE;
		init();

	}

	/**
	 * 创建使用自定义字体风格的字体控件
	 * 
	 * @param defaultUseFont 默认字体
	 * @param fontStyle 字体风格
	 * @param fontSize 字体大小
	 */
	public SmJComboBoxFontChoose(String defaultUseFont, int fontStyle, int fontSize) {
		super();
		this.defaultUseFont = defaultUseFont;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
		init();
	}

	/**
	 * 创建带历史字体且使用自定义字体风格的字体控件
	 * 
	 * @param historyFonts 历史字体
	 * @param defaultUseFont 默认字体
	 * @param fontStyle 字体风格
	 * @param fontSize 字体大小
	 */
	public SmJComboBoxFontChoose(String[] historyFonts, String defaultUseFont, int fontStyle, int fontSize) {
		super();
		this.historyFonts = historyFonts;
		this.defaultUseFont = defaultUseFont;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
		init();
	}

	/**
	 * 字体控件初始化
	 */
	public void init() {
		// 获取当前系统支持的字体列表
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontTypeFaceStr = ge.getAvailableFontFamilyNames();

		if (historyFonts != null && historyFonts.length > 0) {
			String[] allFonts = new String[historyFonts.length + 1 + fontTypeFaceStr.length];
			System.arraycopy(historyFonts, 0, allFonts, 0, historyFonts.length);
			allFonts[historyFonts.length] = "----------";
			System.arraycopy(fontTypeFaceStr, 0, allFonts, historyFonts.length + 1, fontTypeFaceStr.length);
			this.setModel(new DefaultComboBoxModel(allFonts));
		} else {
			this.setModel(new DefaultComboBoxModel(fontTypeFaceStr));
		}
		// 设置渲染器
		this.setRenderer(new FontCellRenderer());
	}

	/**
	 * JComboBoxFontChoose渲染器，设置每一项为它表示的字体
	 * 
	 * @author XiaJT
	 *
	 */
	class FontCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			String fontName = (String) value;
			Font defaultFont = new Font(defaultUseFont, fontStyle, fontSize);
			// 字体不支持显示，使用默认的字体
			if ("Marlett".equals(fontName) || "MT Extra".equals(fontName) || "Symbol".equals(fontName) || "Webdings".equals(fontName)
					|| "Wingdings".equals(fontName)) {
				label.setFont(defaultFont);
			} else {
				Font font = new Font((String) value, fontStyle, fontSize);
				label.setFont(font);
			}
			return (Component) label;
		}
	}
}