package com.supermap.desktop.core;

import com.supermap.desktop.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.awt.*;

public class CoreActivator implements BundleActivator {

	ServiceRegistration<?> registration;
	CoreServiceTracker serviceTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Hello SuperMap === Core!!");


//		SplashScreen splash = SplashScreen.getSplashScreen();
//		Rectangle bounds = splash.getBounds();
//		double x = bounds.getX();
//		double y = bounds.getY();
//		double height = bounds.getHeight();
//		double width = bounds.getWidth();
//
//		int startRow = (int) (x + height / 3);
//		int startColumn = (int) (y + width / 3);
//
//
//		Graphics2D g = splash.createGraphics();
//		g.setComposite(AlphaComposite.Clear);
//		g.fillRect(startRow, startColumn, 280, 40);
//		g.setPaintMode();
//		g.setColor(Color.RED);
//		g.drawString("Core Starting", startRow+10, startColumn+10);
//		g.fillRect(startRow,startColumn,50,20);
//		splash.update();
//		Thread.sleep(1000);
//
//		g.setComposite(AlphaComposite.Clear);
//		g.fillRect(startRow, startColumn, 280, 40);
//		g.setPaintMode();
//		g.setColor(Color.RED);
//		g.drawString("Core stoped", startRow+10, startColumn+10);
//      g.fillRect(startRow,startColumn+20,50,20);
//		splash.update();
//		Thread.sleep(1000);
//		g.dispose();


		// 不知道为什么，core会被加载两次，暂时先这么处理
		if (Application.getActiveApplication() == null || Application.getActiveApplication().getPluginManager().getBundle("SuperMap.Desktop.Core") == null) {
			this.serviceTracker = new CoreServiceTracker(context);
			this.serviceTracker.open();
			this.registration = context.registerService(Application.class.getName(), new Application(), null);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Goodbye SuperMap === Core!!");

		this.serviceTracker.close();
		this.registration.unregister();
	}

}
