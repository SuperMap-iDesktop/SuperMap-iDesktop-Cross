package com.supermap.desktop.controls;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import com.supermap.desktop.ui.*;

public class ControlServiceTracker<S, T> extends ServiceTracker<S, T> {
	public ControlServiceTracker(BundleContext context) {
		super(context, UICommonToolkit.class.getName(), null);
	}

	@Override
	public Object addingService(ServiceReference reference) {
		return super.addingService(reference);

	}

	@Override
	public void removedService(ServiceReference<S> reference, T service) {
		super.removedService(reference, service);

	}
}
