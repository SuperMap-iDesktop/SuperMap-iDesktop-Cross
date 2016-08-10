package com.supermap.desktop.core;

import com.supermap.desktop.Application;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

@SuppressWarnings("rawtypes")
public class CoreServiceTracker extends ServiceTracker {
	public CoreServiceTracker(BundleContext context) {
		super(context, Application.class.getName(), null);
	}


	public Object addingService(ServiceReference reference) {
		return super.addingService(reference);

	}

	public void removedService(ServiceReference reference, Object service) {
		super.removedService(reference, service);

	}
}
