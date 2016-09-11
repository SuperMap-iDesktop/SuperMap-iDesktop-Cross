package com.supermap.desktop.tabularview;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormTabular;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.utilities.TabularUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.swing.*;
import java.awt.*;

public class TabularViewActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === TabularView!!");

		TabularViewActivator.setContext(bundleContext);

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.TabularView", bundleContext.getBundle());

		CommonToolkit.FormWrap.addNewWindowListener(new NewWindowListener() {
			@Override
			public void newWindow(NewWindowEvent evt) {
				newWindowEvent(evt);
			}
		});
	}

	private void newWindowEvent(NewWindowEvent evt) {
		try {
			WindowType type = evt.getNewWindowType();

			if (type == WindowType.TABULAR) {
				IFormTabular formTabular = showTabular(evt.getNewWindowName());
				evt.setNewWindow(formTabular);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public IFormTabular showTabular(String name) {
		String newName = name;
		JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
		mainFrame.setCursor(Cursor.WAIT_CURSOR);
		IFormTabular formTabular = null;
		try {
			IForm form = CommonToolkit.FormWrap.getForm(newName, WindowType.TABULAR);
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

			if (form == null) {
				if (newName == null || newName.length() == 0)
					newName = TabularUtilities.getTabularName();

				formTabular = new FormTabular(newName);
				if (formManager.getActiveForm() != null) {
					formManager.setActiveForm(formTabular);
				}
				formManager.showChildForm(formTabular);
			} else {
				formTabular = (IFormTabular) form;
				formManager.setActiveForm(formTabular);
			}

			if (formTabular != null) {
				formTabular.setRecordset(openActiveDataset());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		mainFrame.setCursor(Cursor.DEFAULT_CURSOR);

		return formTabular;
	}

	private Recordset openActiveDataset() {
		boolean flag = false;
		Recordset recordset = null;
		try {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			if (datasets != null && datasets.length > 0) {
				for (Dataset selectedDataset : datasets) {
					if (selectedDataset instanceof DatasetVector) {
						// 先打开数据集
						if (!selectedDataset.isOpen() && !selectedDataset.open()) {
							// 数据集打开失败
							flag = true;
						}
						if (!flag) {
							recordset = ((DatasetVector) selectedDataset).getRecordset(false, CursorType.DYNAMIC);
						}
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return recordset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		TabularViewActivator.setContext(null);
		System.out.println("Goodbye SuperMap === Tabular!!");
	}

	public static void setContext(BundleContext context) {
		TabularViewActivator.CONTEXT = context;
	}
}
