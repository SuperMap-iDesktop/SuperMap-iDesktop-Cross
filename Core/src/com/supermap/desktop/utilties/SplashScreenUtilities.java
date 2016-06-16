package com.supermap.desktop.utilties;

import java.awt.*;

/**
 * 启动界面输出加载信息
 */
public class SplashScreenUtilities {

	private static SplashScreenUtilities splashScreenUtiltiesInstance;
	private static int modelCount = 0;// 根据模块总数来显示进度条
	private static int currentCount = 0;
	private float screenWidth;
	private float screenHeight;
	private SplashScreen splashScreen;

	private float fontY;
	private Graphics2D graphics2D;
	private static final Color fontColor = new Color(78, 135, 237);
	private static final Color progressColor = new Color(93, 203, 183);

	private static final float DEFAULT_FONT_SIZE = 16.0f;

	private SplashScreenUtilities() {
		splashScreen = SplashScreen.getSplashScreen();
		if (splashScreen != null) {
			Dimension bounds = splashScreen.getSize();
			screenWidth = (float) bounds.getWidth();
			screenHeight = (float) bounds.getHeight();
			fontY = (float) (screenHeight * 0.925);
			graphics2D = splashScreen.createGraphics();
			graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, DEFAULT_FONT_SIZE));
			graphics2D.setComposite(AlphaComposite.Clear);
		}
	}

	/**
	 * 更新当前显示的信息
	 *
	 * @param text 需要显示的文字信息
	 */
	public void update(String text) {
		++currentCount;
		if (text == null) {
			return;
		}
		// 清空
		graphics2D.setComposite(AlphaComposite.Clear);
		graphics2D.fillRect(0, 0, ((int) splashScreen.getSize().getWidth()), ((int) splashScreen.getSize().getHeight()));
		// 显示文字
		graphics2D.setPaintMode();
		graphics2D.setColor(fontColor);
		graphics2D.drawString(text, 5, fontY);
		//显示进度条
		graphics2D.setColor(progressColor);
		int width;
		if (currentCount >= modelCount) {
			width = (int) screenWidth;
		} else {
			width = (int) (((float) (currentCount + 1) / (float) (modelCount + 1)) * screenWidth);
		}
		int height = (int) (screenHeight * 0.045);
		graphics2D.fillRect(0, (int) (screenHeight - height), width, height - 5);

		graphics2D.setColor(Color.WHITE);
		graphics2D.fillRect(width, (int) (screenHeight - height), (int) (screenWidth - width), height - 5);
//		graphics2D.setColor(Color.BLACK);
//		graphics2D.drawRect(0,(int) (screenHeight - height), (int) screenWidth -1, height -5);
		splashScreen.update();
	}

	public static SplashScreenUtilities getSplashScreenUtiltiesInstance() {
		if (SplashScreen.getSplashScreen() == null) {
			return null;
		}
		if (splashScreenUtiltiesInstance == null) {
			synchronized (SplashScreenUtilities.class) {
				if (splashScreenUtiltiesInstance == null) {
					splashScreenUtiltiesInstance = new SplashScreenUtilities();
				}
			}
		}
		return splashScreenUtiltiesInstance;
	}

	public static void setBundleCount(int count) {
		SplashScreenUtilities.modelCount = count;
	}

	public static void resetCurrentCount() {
		SplashScreenUtilities.currentCount = 1;
	}
}
