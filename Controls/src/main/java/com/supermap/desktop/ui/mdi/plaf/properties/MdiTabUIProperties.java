package com.supermap.desktop.ui.mdi.plaf.properties;

import java.awt.*;

/**
 * Created by highsad on 2016/9/20.
 */
public class MdiTabUIProperties {
	public static Insets INSETS = new Insets(4, 4, 4, 8); // 竖直方向总是保持居中，上下间距设置仅影响 Tab 高度
	public static Insets ACTIVE_INSETS = new Insets(1, 1, 1, 1); // 激活状态下的 Tab 向外缩放的距离
	public static int CONTENT_GAP = 4; // tab 上的 icon 与 icon之间、icon 与文本之间的间距
	public static Color FORECOLOR = Color.BLACK;
	public static Color BORDER_COLOR = Color.LIGHT_GRAY;
	public static Color BACKCOLOR_NORMAL = new Color(219, 234, 254, 255);
	public static Color BACKCOLOR_ACTIVE = new Color(248, 208, 120);
}
