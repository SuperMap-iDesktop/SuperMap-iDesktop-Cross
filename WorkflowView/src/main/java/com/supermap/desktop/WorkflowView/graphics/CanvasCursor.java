package com.supermap.desktop.WorkflowView.graphics;

import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.utilities.CursorUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by highsad on 2017/3/31.
 */
public class CanvasCursor {
	private final static String CONNECTING_PATH = "/processresources/Cursor_Connecting.png";
	private final static String ITEMDRAGGED_PATH = "/processresources/Cursor_ItemDragged.png";
	private final static String TRANSLATION_PATH = "/processresources/Cursor_Translation.png";

	public final static Image IMAGE_CONNECTING = ((ImageIcon) ProcessResources.getIcon(CONNECTING_PATH)).getImage();
	public final static Image IMAGE_ITEMDRAGGED = ((ImageIcon) ProcessResources.getIcon(ITEMDRAGGED_PATH)).getImage();
	public final static Image IMAGE_TRANSLATION = ((ImageIcon) ProcessResources.getIcon(TRANSLATION_PATH)).getImage();

	public static void setConnectingCursor(GraphCanvas canvas) {
		CursorUtilities.setCustomCursor(canvas, IMAGE_CONNECTING, "Cursor_Connecting");
	}

	public static void setItemDraggedCursor(GraphCanvas canvas) {
		CursorUtilities.setCustomCursor(canvas, IMAGE_ITEMDRAGGED, "Cursor_ItemDragged");
	}

	public static void setTranslationCursor(GraphCanvas canvas) {
		CursorUtilities.setCustomCursor(canvas, IMAGE_TRANSLATION, "Cursor_Translation");
	}

	public static void resetCursor(GraphCanvas canvas) {
		CursorUtilities.setDefaultCursor(canvas);
	}
}
