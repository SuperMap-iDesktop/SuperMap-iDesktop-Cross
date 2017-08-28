package com.supermap.desktop.utilities;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.properties.CoreProperties;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ClipBoardUtilties {

	public static void setBounds(Rectangle2D bounds) {
		String clipBoardTextLeft = CoreProperties.getString("String_LabelLeft") + String.valueOf(bounds.getLeft());
		String clipBoardTextBottom = CoreProperties.getString("String_LabelBottom") + String.valueOf(bounds.getBottom());
		String clipBoardTextRight = CoreProperties.getString("String_LabelRight") + String.valueOf(bounds.getRight());
		String clipBoardTextTop = CoreProperties.getString("String_LabelTop") + String.valueOf(bounds.getTop());
		setSysClipboardText(clipBoardTextLeft + "," + clipBoardTextBottom + "," + clipBoardTextRight + "," + clipBoardTextTop);
	}

	public static Rectangle2D getBounds() {
		String clipBoard = getSysClipboardText();
		// 判断剪切板中是否包含“左：”“下：”“右：”“上：”等“分隔符”
		String left = CoreProperties.getString("String_LabelLeft");
		String bottom = CoreProperties.getString("String_LabelBottom");
		String right = CoreProperties.getString("String_LabelRight");
		String top = CoreProperties.getString("String_LabelTop");

		if (clipBoard.contains(left) && clipBoard.contains(bottom) && clipBoard.contains(right) && clipBoard.contains(top)) {
			String clipBoardLeft = clipBoard.substring(clipBoard.indexOf(CoreProperties.getString("String_LabelLeft")), clipBoard.indexOf(CoreProperties.getString("String_LabelBottom")));
			String clipBoardBottom = clipBoard.substring(clipBoard.indexOf(CoreProperties.getString("String_LabelBottom")), clipBoard.indexOf(CoreProperties.getString("String_LabelRight")));
			String clipBoardRight = clipBoard.substring(clipBoard.indexOf(CoreProperties.getString("String_LabelRight")), clipBoard.indexOf(CoreProperties.getString("String_LabelTop")));
			String clipBoardTop = clipBoard.substring(clipBoard.indexOf(CoreProperties.getString("String_LabelTop")));

			clipBoardLeft = (clipBoardLeft.replace(left, "")).replace(",", "");
			clipBoardBottom = (clipBoardBottom.replace(bottom, "")).replace(",", "");
			clipBoardRight = (clipBoardRight.replace(right, "")).replace(",", "");
			clipBoardTop = (clipBoardTop.replace(top, "")).replace(",", "");
			if (StringUtilities.isNumber(clipBoardLeft) && StringUtilities.isNumber(clipBoardBottom) && StringUtilities.isNumber(clipBoardRight) && StringUtilities.isNumber(clipBoardTop)) {
				return new Rectangle2D(DoubleUtilities.stringToValue(clipBoardLeft), DoubleUtilities.stringToValue(clipBoardBottom), DoubleUtilities.stringToValue(clipBoardRight), DoubleUtilities.stringToValue(clipBoardTop));
			}
		}
		return null;
	}

	/**
	 * 调用windows的剪贴板
	 * 获得系统剪贴板内
	 * yuanR 2017.3.24
	 *
	 * @return
	 */
	public static String getSysClipboardText() {
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipTf = sysClip.getContents(null);
		if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				String ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 调用windows的剪贴板
	 * yuanR
	 *
	 * @param coypText
	 */
	public static void setSysClipboardText(String coypText) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable Text = new StringSelection(coypText);
		clip.setContents(Text, null);
	}
}
