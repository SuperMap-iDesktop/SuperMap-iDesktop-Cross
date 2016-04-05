package com.supermap.desktop.utilties;

import com.supermap.data.Point2D;
import com.supermap.desktop.Application;
import com.supermap.layout.MapLayout;
import com.supermap.mapping.Map;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class FontUtilties {

	// 经验值
	private final static double EXPERIENCE = 2.83;

	/**
	 * 字号大小转化为字体对象实际保存的字高。
	 *
	 * @param fontSize
	 * @param mapObject
	 * @param isSizeFixed
	 * @return
	 */

	public static double fontSizeToMapHeight(double fontSize, Object mapObject, boolean isSizeFixed) {
		double mapHeight = 0;
		try {
			mapHeight = fontSize / EXPERIENCE;
			if (isSizeFixed) {
				mapHeight = fontSize / EXPERIENCE;
			} else {
				double fontHeight = fontSize / EXPERIENCE;
				Point2D logicalPntEnd = new Point2D(fontHeight, fontHeight);
				Point2D logicalPntStart = new Point2D(0, 0);
				Point2D pointEnd = new Point2D();
				Point2D pointStart = new Point2D();

				if (mapObject instanceof Map) {
					Map map = (Map) mapObject;
					pointEnd = map.logicalToMap(logicalPntEnd);
					pointStart = map.logicalToMap(logicalPntStart);
					mapHeight = Math.abs(pointEnd.getY() - pointStart.getY());
				}

				if (mapObject instanceof MapLayout) {
					MapLayout mapLayout = (MapLayout) mapObject;
					pointEnd = mapLayout.logicalToLayout(logicalPntEnd);
					pointStart = mapLayout.logicalToLayout(logicalPntStart);
					mapHeight = Math.abs(pointEnd.getY() - pointStart.getY());
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return mapHeight;
	}

	/**
	 * 字体对象实际保存的字高转化为字号大小。
	 *
	 * @param mapHeight
	 * @param mapObject
	 * @param isSizeFixed
	 * @return
	 */
	public static double mapHeightToFontSize(double mapHeight, Object mapObject, boolean isSizeFixed) {
		double fontSize = 0;
		try {
			fontSize = mapHeight * EXPERIENCE;
			if (isSizeFixed) {
				fontSize = mapHeight * EXPERIENCE;
			} else {
				Double fontHeight = mapHeight * EXPERIENCE;
				Point2D pointEnd = new Point2D(fontHeight, fontHeight);
				Point2D pointStart = new Point2D(0, 0);
				Point2D logicalPntEnd = new Point2D();
				Point2D logicalPntStart = new Point2D();

				if (mapObject instanceof Map) {
					Map map = (Map) mapObject;
					logicalPntEnd = map.mapToLogical(pointEnd);
					logicalPntStart = map.mapToLogical(pointStart);
					fontSize = Math.abs(logicalPntEnd.getY() - logicalPntStart.getY());
				}

				if (mapObject instanceof MapLayout) {
					MapLayout mapLayout = (MapLayout) mapObject;
					logicalPntEnd = mapLayout.layoutToLogical(pointEnd);
					logicalPntStart = mapLayout.layoutToLogical(pointStart);
					fontSize = Math.abs(logicalPntEnd.getY() - logicalPntStart.getY());
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return fontSize;
	}

	/**
	 * 字体对象实际保存的字宽（还需要乘以10作为mapWidth）转化为显示的字宽
	 *
	 * @param mapWidth
	 * @param mapObject
	 * @param isSizeFixed
	 * @return
	 */
	public static double mapWidthToFontWidth(double mapWidth, Object mapObject, boolean isSizeFixed) {
		double fontWidth = 0;
		try {
			fontWidth = mapWidth;
			if (!isSizeFixed) {
				Point2D pointEnd = new Point2D(mapWidth, mapWidth);
				Point2D pointStart = new Point2D(0, 0);
				Point2D logicalPntEnd = new Point2D();
				Point2D logicalPntStart = new Point2D();

				if (mapObject instanceof Map) {
					Map map = (Map) mapObject;
					logicalPntEnd = map.mapToLogical(pointEnd);
					logicalPntStart = map.mapToLogical(pointStart);
					fontWidth = Math.abs(logicalPntEnd.getX() - logicalPntStart.getX());
				}

				if (mapObject instanceof MapLayout) {
					MapLayout mapLayout = (MapLayout) mapObject;
					logicalPntEnd = mapLayout.layoutToLogical(pointEnd);
					logicalPntStart = mapLayout.layoutToLogical(pointStart);
					fontWidth = Math.abs(logicalPntEnd.getX() - logicalPntStart.getX());
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return fontWidth;
	}

	/**
	 * 显示的字宽转化为字体对象实际保存的字宽（还需要除以10）
	 *
	 * @param fontWidth
	 * @param mapObject
	 * @param isSizeFixed
	 * @return
	 */
	public static double fontWidthToMapWidth(double fontWidth, Object mapObject, boolean isSizeFixed) {
		double mapWidth = 0;
		try {
			mapWidth = fontWidth;
			if (!isSizeFixed) {
				Point2D logicalPntEnd = new Point2D(fontWidth, fontWidth);
				Point2D logicalPntStart = new Point2D(0, 0);
				Point2D pointEnd = new Point2D();
				Point2D pointStart = new Point2D();

				if (mapObject instanceof Map) {
					Map map = (Map) mapObject;
					pointEnd = map.logicalToMap(logicalPntEnd);
					pointStart = map.logicalToMap(logicalPntStart);
					mapWidth = Math.abs(pointEnd.getX() - pointStart.getX());
				}

				if (mapObject instanceof MapLayout) {
					MapLayout mapLayout = (MapLayout) mapObject;
					pointEnd = mapLayout.logicalToLayout(logicalPntEnd);
					pointStart = mapLayout.logicalToLayout(logicalPntStart);
					mapWidth = Math.abs(pointEnd.getX() - pointStart.getX());
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return mapWidth;
	}

	private static AffineTransform atf = new AffineTransform();

	private static FontRenderContext frc = new FontRenderContext(atf, true, true);

	/**
	 * 获得字符串在指定字体下的高度
	 *
	 * @param str  需要计算字符串
	 * @param font 字体
	 * @return 高度
	 */
	public static int getStringHeight(String str, Font font) {
		if (str == null || str.isEmpty() || font == null) {
			return 0;
		}
		return (int) font.getStringBounds(str, frc).getHeight();

	}

	/**
	 * 获得字符串在指定字体下的宽度
	 *
	 * @param str  需要计算字符串
	 * @param font 字体
	 * @return 宽度
	 */
	public static int getStringWidth(String str, Font font) {
		if (str == null || str.isEmpty() || font == null) {
			return 0;
		}
		return (int) font.getStringBounds(str, frc).getWidth();
	}
}
