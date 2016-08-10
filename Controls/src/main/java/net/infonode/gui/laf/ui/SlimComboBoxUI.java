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

// $Id: SlimComboBoxUI.java,v 1.6 2005/02/16 11:28:11 jesper Exp $
package net.infonode.gui.laf.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.6 $
 */
public class SlimComboBoxUI extends MetalComboBoxUI {
	private static Border FOCUS_BORDER;
	private static Border NORMAL_BORDER;

	public static ComponentUI createUI() {
		return new SlimComboBoxUI();
	}

	public static Border getFocusBorder(){
		if(SlimComboBoxUI.FOCUS_BORDER == null){
			SlimComboBoxUI.FOCUS_BORDER = new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(0, 3, 0, 3));
		}
		return SlimComboBoxUI.FOCUS_BORDER;
	}
	
	public static Border getNormalBorder(){
		if(SlimComboBoxUI.NORMAL_BORDER == null){
			SlimComboBoxUI.NORMAL_BORDER = new EmptyBorder(1, 4, 1, 4);
		}
		return SlimComboBoxUI.NORMAL_BORDER;
	}
	
	public static void setFocusBorder(Border focusBorder ){
		SlimComboBoxUI.FOCUS_BORDER = focusBorder;
	}
	public static void setNormalBorder(Border normalBorder ){
		SlimComboBoxUI.NORMAL_BORDER = normalBorder;
	}
	@Override
	protected ListCellRenderer createRenderer() {
		return new BasicComboBoxRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				label.setBorder(index == -1 ? noFocusBorder : cellHasFocus ? getFocusBorder() : getNormalBorder());
				return label;
			}
		};
	}

}
