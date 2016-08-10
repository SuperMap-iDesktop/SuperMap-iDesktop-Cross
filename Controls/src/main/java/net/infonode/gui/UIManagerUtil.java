/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */


// $Id: UIManagerUtil.java,v 1.6 2005/12/04 13:46:04 jesper Exp $
package net.infonode.gui;

import net.infonode.gui.border.BorderUtil;
import net.infonode.util.ColorUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.6 $
 */
public class UIManagerUtil {
  private UIManagerUtil() {
  }

  public static Insets getInsets(String key) {
    return InsetsUtil.copy(UIManager.getInsets(key));
  }

  public static Insets getInsets(String key, Insets insets) {
    Insets nowInsets = getInsets(key);
    return nowInsets == null ? insets : nowInsets;
  }

  public static Insets getInsets(String key, String defaultKey) {
    Insets insets = getInsets(key);

    if (insets != null)
      return insets;

    insets = getInsets(defaultKey);
    return insets == null ? new Insets(0, 0, 0, 0) : insets;
  }

  public static Color getColor(String key) {
    return ColorUtil.copy(UIManager.getColor(key));
  }

  public static Color getColor(String key, String defaultKey) {
    return getColor(key, defaultKey, Color.BLACK);
  }

  public static Color getColor(String key, String defaultKey, Color defaultColor) {
    Color color = getColor(key);

    if (color != null)
      return color;

    color = getColor(defaultKey);
    return color == null ? defaultColor : color;
  }

  public static Border getBorder(String key) {
    return BorderUtil.copy(UIManager.getBorder(key));
  }

  public static Border getBorder(String key, String defaultKey) {
    Border border = getBorder(key);

    if (border != null)
      return border;

    border = getBorder(defaultKey);
    return border == null ? BorderFactory.createEmptyBorder() : border;
  }

  public static Font getFont(String key) {
    Font font = UIManager.getFont(key);
    if (font == null)
      font = new JLabel().getFont();
    return FontUtil.copy(font);
  }

  public static Font getFont(String key, String defaultKey) {
    Font font = getFont(key);

    if (font != null)
      return font;

    font = getFont(defaultKey);
    return font == null ? new Font("Dialog", 0, 11) : font;
  }

  public static Color getColor(String key, Color defaultColor) {
    Color color = getColor(key);
    return color == null ? defaultColor : color;
  }
}
