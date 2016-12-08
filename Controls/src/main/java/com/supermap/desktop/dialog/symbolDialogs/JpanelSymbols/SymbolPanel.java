package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.Geometry;
import com.supermap.data.Resources;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.FontUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

/**
 * @author XiaJt
 */
public abstract class SymbolPanel extends JPanel {

	protected Symbol symbol;
	protected int symbolID;
	protected String symbolName;
	protected Resources resources;


	private Dimension labelDimension;

	private int fontSize;
	private JLabel labelIcon;
	private JLabel labelName;

	/**
	 * 用于普通符号
	 *
	 * @param symbol    符号
	 * @param resources 资源
	 */
	public SymbolPanel(Symbol symbol, Resources resources) {
		this.resources = resources;
		this.symbol = symbol;
		this.symbolID = symbol.getID();
		this.symbolName = symbol.getName();
	}

	/**
	 * 用于系统符号
	 *
	 * @param id        符号id
	 * @param resources 资源
	 */
	public SymbolPanel(int id, Resources resources) {
		this.symbolID = id;
		this.symbol = null;
		this.symbolName = "System " + id;
		this.resources = resources;
	}

	protected void initSize() {
		labelDimension = new Dimension(getIconWidth() + 12, getIconHeight() + 12);
		fontSize = getIconWidth() + 40;
		labelName = new JLabel();
		labelName.setText(getFitText(labelName.getFont()));
		labelName.setBackground(Color.WHITE);
		labelName.setHorizontalTextPosition(JLabel.CENTER);
	}

	protected void init(BufferedImage bufferedImage) {
		ImageIcon icon = new ImageIcon(bufferedImage);
		labelIcon = new JLabel("", icon, JLabel.CENTER);
		labelIcon.setMinimumSize(labelDimension);
		labelIcon.setMaximumSize(labelDimension);
		labelIcon.setPreferredSize(labelDimension);
		labelIcon.setBackground(Color.WHITE);

		this.setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		this.add(labelIcon, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.NORTH));
		this.add(labelName, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.NORTH).setIpad(0, 2));
		this.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_SymbolPanelToolTips"), java.lang.String.valueOf(symbolID), symbolName));
	}


	private String getFitText(Font font) {
		int line = 1;
		String result = symbolName;
		if (FontUtilities.getStringWidth(symbolName, font) > fontSize) {
			StringBuilder builder = new StringBuilder();
			int fontCount = 0;
			builder.append("<html>");
			for (int i = 0; i < symbolName.length(); i++) {
				char c = symbolName.charAt(i);
				if (fontCount > fontSize) {
					builder.append(c);
				} else {
					int width = FontUtilities.getStringWidth(String.valueOf(c), font);
					if (width + fontCount > fontSize) {
						line++;
						builder.append("<br>");
						fontCount = 0;
					}
					builder.append(c);
					fontCount += width;
				}
			}
			builder.append("</html>");
			result = builder.toString();
		}
		Dimension preferredSize = new Dimension(fontSize + 20, (int) (labelDimension.getHeight() + line * (FontUtilities.getStringHeight(CommonProperties.getString(CommonProperties.OK), font) + 5)));
		this.setMinimumSize(preferredSize);
		this.setPreferredSize(preferredSize);
		this.setMaximumSize(preferredSize);
		return result;
	}

	protected Geometry getPaintGeometry() {
		return SymbolPanelGeometryFactory.getPaintGeometry(getSymbolType());
	}

	protected abstract SymbolType getSymbolType();

	@Override
	public int getBaseline(int width, int height) {
		return 0;
	}

	@Override
	public BaselineResizeBehavior getBaselineResizeBehavior() {
		return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
	}

	public void setSelected() {
		if (labelIcon != null && labelName != null) {
			this.labelIcon.setBorder(BorderFactory.createLineBorder(new Color(52, 154, 255), 3));
//		this.labelIcon.setOpaque(true);
//		this.labelIcon.setForeground(new Color(181, 255, 255));
			this.labelName.setOpaque(true);
			this.labelIcon.setOpaque(true);

//		this.labelIcon.setBackground(new Color(52, 154, 255));
			this.labelName.setBackground(new Color(52, 154, 255));
			this.labelName.setForeground(Color.WHITE);
		}
	}

	public void setUnselected() {
		if (labelIcon != null && labelName != null) {
			this.labelIcon.setBorder(BorderFactory.createEmptyBorder());
			this.labelName.setOpaque(false);
//		this.labelIcon.setOpaque(false);
//		this.labelIcon.setBackground(Color.white);
			this.labelName.setBackground(Color.white);
			this.labelName.setForeground(Color.BLACK);
		}
	}

	public int getSymbolID() {
		return symbolID;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public String getSymbolName() {
		return symbolName;
	}

	// 宽高修改之后要对应修改SymbolPanelGeometryFactory里面创建对象的值
	protected int getIconWidth() {
		return 60;
	}

	protected int getIconHeight() {
		return 60;
	}
}
