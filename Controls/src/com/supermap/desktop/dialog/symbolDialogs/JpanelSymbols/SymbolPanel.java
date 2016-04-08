package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.Geometry;
import com.supermap.data.Resources;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.FontUtilties;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author XiaJt
 */
public abstract class SymbolPanel extends JPanel {

	protected Symbol symbol;
	protected int symbolID;
	protected String symbolName;
	protected Resources resources;
	protected int width = 48;
	protected int height = 48;

	private Dimension labelDimension = new Dimension(60, 60);

	private static final int fontSize = 100;

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


	protected void init(BufferedImage bufferedImage) {
		ImageIcon icon = new ImageIcon(bufferedImage);
		JLabel label = new JLabel("", icon, JLabel.CENTER);
		label.setMinimumSize(labelDimension);
		label.setMaximumSize(labelDimension);
		label.setPreferredSize(labelDimension);
		label.setBackground(Color.WHITE);
		JLabel labelName = new JLabel(getFitText(label.getFont()));
		labelName.setBackground(Color.WHITE);
		labelName.setHorizontalTextPosition(JLabel.CENTER);

		this.setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		this.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.NORTH));
		this.add(labelName, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.NORTH));
	}


	private String getFitText(Font font) {
		int line = 1;
		String result = symbolName;
		if (FontUtilties.getStringWidth(symbolName, font) > fontSize) {
			StringBuilder builder = new StringBuilder();
			int fontCount = 0;
			builder.append("<html>");
			for (int i = 0; i < symbolName.length(); i++) {
				char c = symbolName.charAt(i);
				if (fontCount > fontSize) {
					builder.append(c);
				} else {
					int width = FontUtilties.getStringWidth(String.valueOf(c), font);
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
		Dimension preferredSize = new Dimension(fontSize, 60 + line * (FontUtilties.getStringHeight(CommonProperties.getString(CommonProperties.OK), font) + 2));
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

}
