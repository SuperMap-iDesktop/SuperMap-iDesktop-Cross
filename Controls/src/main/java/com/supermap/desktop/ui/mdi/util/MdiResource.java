package com.supermap.desktop.ui.mdi.util;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class MdiResource {

	private static final String ICON_CLOSE = "/mdi/icon_close.png";
	private static final String ICON_CLOSE_DISABLE = "/mdi/icon_close_disable.png";
	private static final String ICON_CLOSE_ACTIVE = "/mdi/icon_close_active.png";
	private static final String ICON_FLOAT = "/mdi/icon_float.png";
	private static final String ICON_FLOAT_DISABLE = "/mdi/icon_float_disable.png";
	private static final String ICON_FLOAT_ACTIVE = "/mdi/icon_float_active.png";
	private static final String ICON_LIST = "/mdi/icon_list.png";
	private static final String ICON_LIST_DISABLE = "/mdi/icon_list_disable.png";
	private static final String ICON_LIST_ACTIVE = "/mdi/icon_list_active.png";
	private static final String ICON_NEXT = "/mdi/icon_next.png";
	private static final String ICON_NEXT_DISABLE = "/mdi/icon_next_disable.png";
	private static final String ICON_NEXT_ACTIVE = "/mdi/icon_next_active.png";
	private static final String ICON_PRE = "/mdi/icon_pre.png";
	private static final String ICON_PRE_DISBALE = "/mdi/icon_pre_disable.png";
	private static final String ICON_PRE_ACTIVE = "/mdi/icon_pre_active.png";
	private static final String ICON_FILE = "/mdi/icon_file.png";
	private static final String ICON_ACTION = "/mdi/icon_action.png";
	private static final String ICON_ACTION_DISABLE = "/mdi/icon_action_disable.png";
	private static final String ICON_ACTION_ACTIVE = "/mdi/icon_action_active.png";

	public static final String CLOSE = "ICON_CLOSE";
	public static final String CLOSE_DISABLE = "ICON_CLOSE_DISABLE";
	public static final String CLOSE_ACTIVE = "ICON_CLOSE_ACTIVE ";
	public static final String FLOAT = "ICON_FLOAT";
	public static final String FLOAT_DISABLE = "ICON_FLOAT_DISABLE";
	public static final String FLOAT_ACTIVE = "ICON_FLOAT_ACTIVE";
	public static final String LIST = "ICON_LIST";
	public static final String LIST_DISABLE = "ICON_LIST_DISABLE";
	public static final String LIST_ACTIVE = "ICON_LIST_ACTIVE";
	public static final String NEXT = "ICON_NEXT";
	public static final String NEXT_DISABLE = "ICON_NEXT_DISABLE";
	public static final String NEXT_ACTIVE = "ICON_NEXT_ACTIVE";
	public static final String PRE = "ICON_PRE";
	public static final String PRE_DISABLE = "ICON_PRE_DISABLE";
	public static final String PRE_ACTIVE = "ICON_PRE_ACTIVE";
	public static final String FILE = "ICON_FILE";
	public static final String ACTION = "ICON_ACION";
	public static final String ACTION_DISABLE = "ICON_ACION_DISABLE";
	public static final String ACTION_ACTIVE = "ICON_ACION_ACTIVE";

	private static Map<String, ImageIcon> iconMap;
	private static Map<String, Dimension> iconSizeMap;

	private static Map<String, String> defaultIconMap;

	static {
		iconMap = new HashMap<String, ImageIcon>();
		iconSizeMap = new HashMap<String, Dimension>();
		initDefaults();
	}

	private static void initDefaults() {
		defaultIconMap = new HashMap<>();
		defaultIconMap.put(CLOSE, ICON_CLOSE);
		defaultIconMap.put(CLOSE_DISABLE, ICON_CLOSE_DISABLE);
		defaultIconMap.put(CLOSE_ACTIVE, ICON_CLOSE_ACTIVE);
		defaultIconMap.put(FLOAT, ICON_FLOAT);
		defaultIconMap.put(FLOAT_DISABLE, ICON_FLOAT_DISABLE);
		defaultIconMap.put(FLOAT_ACTIVE, ICON_FLOAT_ACTIVE);
		defaultIconMap.put(LIST, ICON_LIST);
		defaultIconMap.put(LIST_DISABLE, ICON_LIST_DISABLE);
		defaultIconMap.put(LIST_ACTIVE, ICON_LIST_ACTIVE);
		defaultIconMap.put(NEXT, ICON_NEXT);
		defaultIconMap.put(NEXT_DISABLE, ICON_NEXT_DISABLE);
		defaultIconMap.put(NEXT_ACTIVE, ICON_NEXT_ACTIVE);
		defaultIconMap.put(PRE, ICON_PRE);
		defaultIconMap.put(PRE_DISABLE, ICON_PRE_DISBALE);
		defaultIconMap.put(PRE_ACTIVE, ICON_PRE_ACTIVE);
		defaultIconMap.put(FILE, ICON_FILE);
		defaultIconMap.put(ACTION, ICON_ACTION);
		defaultIconMap.put(ACTION_DISABLE, ICON_ACTION_DISABLE);
		defaultIconMap.put(ACTION_ACTIVE, ICON_ACTION_ACTIVE);

		putIcon(CLOSE, ResourceUtil.getIcon(defaultIconMap.get(CLOSE)));
		putIcon(CLOSE_DISABLE, ResourceUtil.getIcon(defaultIconMap.get(CLOSE_DISABLE)));
		putIcon(CLOSE_ACTIVE, ResourceUtil.getIcon(defaultIconMap.get(CLOSE_ACTIVE)));
		putIcon(FLOAT, ResourceUtil.getIcon(defaultIconMap.get(FLOAT)));
		putIcon(FLOAT_DISABLE, ResourceUtil.getIcon(defaultIconMap.get(FLOAT_DISABLE)));
		putIcon(FLOAT_ACTIVE, ResourceUtil.getIcon(defaultIconMap.get(FLOAT_ACTIVE)));
		putIcon(LIST, ResourceUtil.getIcon(defaultIconMap.get(LIST)));
		putIcon(LIST_DISABLE, ResourceUtil.getIcon(defaultIconMap.get(LIST_DISABLE)));
		putIcon(LIST_ACTIVE, ResourceUtil.getIcon(defaultIconMap.get(LIST_ACTIVE)));
		putIcon(NEXT, ResourceUtil.getIcon(defaultIconMap.get(NEXT)));
		putIcon(NEXT_DISABLE, ResourceUtil.getIcon(defaultIconMap.get(NEXT_DISABLE)));
		putIcon(NEXT_ACTIVE, ResourceUtil.getIcon(defaultIconMap.get(NEXT_ACTIVE)));
		putIcon(PRE, ResourceUtil.getIcon(defaultIconMap.get(PRE)));
		putIcon(PRE_DISABLE, ResourceUtil.getIcon(defaultIconMap.get(PRE_DISABLE)));
		putIcon(PRE_ACTIVE, ResourceUtil.getIcon(defaultIconMap.get(PRE_ACTIVE)));
		putIcon(FILE, ResourceUtil.getIcon(defaultIconMap.get(FILE)));
		putIcon(ACTION, ResourceUtil.getIcon(defaultIconMap.get(ACTION)));
		putIcon(ACTION_DISABLE, ResourceUtil.getIcon(defaultIconMap.get(ACTION_DISABLE)));
		putIcon(ACTION_ACTIVE, ResourceUtil.getIcon(defaultIconMap.get(ACTION_ACTIVE)));
	}

	public static void putIcon(String name, ImageIcon icon) {
		if (isNullOrEmpty(name)) {
			return;
		}

		String trimed = name.trim();
		if (iconMap.containsKey(trimed)) {
			if (iconMap.get(trimed) != icon) {
				iconSizeMap.put(trimed, new Dimension(icon.getIconWidth(), icon.getIconHeight()));
			}
		} else {
			iconMap.put(trimed, icon);
			iconSizeMap.put(trimed, new Dimension(icon.getIconWidth(), icon.getIconHeight()));

		}
	}

	public static void putIconSize(String name, int width, int height) {
		if (isNullOrEmpty(name)) {
			return;
		}

		String trimed = name.trim();
		if (!iconMap.containsKey(trimed)) {
			return;
		}

		iconSizeMap.put(trimed, new Dimension(width, height));
	}

	/**
	 * 重置 Image 的大小为默认值
	 */
	public static void resetSizeDefaults() {
		for (String name : iconMap.keySet()) {
			ImageIcon icon = iconMap.get(name);
			iconSizeMap.put(name, new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		}
	}

	/**
	 * 重置 MdiResource 为默认
	 */
	public static void resetDefaults() {
		for (String name : iconMap.keySet()) {
			if (!defaultIconMap.containsKey(name)) {
				iconMap.remove(name);
				iconSizeMap.remove(name);
			}
		}
		resetSizeDefaults();
	}

	public static void removeIcon(String name) {
		if (isNullOrEmpty(name)) {
			return;
		}

		String trimed = name.trim();
		if (iconMap.containsKey(trimed)) {
			iconMap.remove(trimed);
			iconSizeMap.remove(trimed);

		}
	}

	public static ImageIcon getIcon(String name) {
		if (isNullOrEmpty(name)) {
			return null;
		}

		String trimed = name.trim();
		if (!iconMap.containsKey(trimed)) {
			return null;
		}

		return iconMap.get(trimed);
	}

	public static Dimension getIconSize(String name) {
		if (isNullOrEmpty(name)) {
			return null;
		}

		String trimed = name.trim();
		if (!iconMap.containsKey(trimed)) {
			return null;
		}

		return iconSizeMap.get(trimed);
	}

	private static boolean isNullOrEmpty(String str) {
		return str == null || str.trim() == "";
	}
}
