package com.supermap.desktop;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.Interface.IOutput;
import com.supermap.desktop.Interface.ISplashForm;
import com.supermap.desktop.event.ActiveDatasetsChangeEvent;
import com.supermap.desktop.event.ActiveDatasetsChangeListener;
import com.supermap.desktop.event.ActiveDatasourcesChangeEvent;
import com.supermap.desktop.event.ActiveDatasourcesChangeListener;
import com.supermap.desktop.implement.Output;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * 应用程序类，实现启动主窗口、插件管理和代码段编译执行等功能。
 */
public class Application {

	private IFormMain formMain = null;
	private ISplashForm formSplash = null;
	private Workspace workspace = null;
	private IOutput output = new Output();
	private WorkEnvironmentManager workEnvironmentManager = null;
	private PluginManager pluginManager = null;
	private ArrayList<Datasource> activeDatasources = new ArrayList<Datasource>();
	private ArrayList<Dataset> activeDatasets = new ArrayList<Dataset>();

	private EventListenerList eventListenerList = new EventListenerList();

	/**
	 * classVar1 documentation comment
	 */
	private static Application activeApplication;

	public Application() {

		Application.setActiveApplication(this);

		Application.getActiveApplication().initialize();
	}

	/** classVar1 documentation comment */

	/**
	 * Get the MainFrame
	 */
	public IFormMain getMainFrame() {
		return this.formMain;
	}

	/**
	 * Set the MainFrame
	 */
	public void setMainFrame(IFormMain formMain) {
		this.formMain = formMain;
	}

	/**
	 * Get the SplashForm
	 */
	public ISplashForm getSplashForm() {
		return this.formSplash;
	}

	/**
	 * Set the SplashForm
	 */
	public void setSplashForm(ISplashForm formSplash) {
		this.formSplash = formSplash;
	}

	/**
	 * 获取应用程序的输出信息对象。
	 */
	public IOutput getOutput() {
		return this.output;
	}

	/**
	 * 设置应用程序的输出信息对象。
	 */
	public void setOutput(IOutput output) {
		this.output = output;
	}

	/**
	 * Get the Workspace
	 */
	public Workspace getWorkspace() {
		return this.workspace;
	}

	/**
	 * Set the Workspace
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public Datasource[] getActiveDatasources() {
		return this.activeDatasources.toArray(new Datasource[this.activeDatasources.size()]);
	}

	public void setActiveDatasources(Datasource[] activeDatasources) {
		Datasource[] old = getActiveDatasources();
		this.activeDatasources.clear();
		if (activeDatasources != null) {
			Collections.addAll(this.activeDatasources, activeDatasources);
		}
		fireActiveDatasourcesChange(new ActiveDatasourcesChangeEvent(this, old, getActiveDatasources()));
	}

	public Dataset[] getActiveDatasets() {
		return this.activeDatasets.toArray(new Dataset[this.activeDatasets.size()]);
	}

	public void setActiveDatasets(Dataset[] activeDatasets) {
		Dataset[] old = getActiveDatasets();
		this.activeDatasets.clear();
		if (activeDatasets != null) {
			Collections.addAll(this.activeDatasets, activeDatasets);
		}
		fireActiveDatasetsChange(new ActiveDatasetsChangeEvent(this, old, getActiveDatasets()));
	}

	/**
	 * get PluginManager
	 */
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

	/**
	 * 设置程序的默认工作环境，注意需要在 ActiveApplication.Initialize() 之前设置
	 */
	public WorkEnvironmentManager getWorkEnvironmentManager() {
		return this.workEnvironmentManager;
	}

	/**
	 * Get the Application
	 */
	public static Application getActiveApplication() {
		if (Application.activeApplication == null) {
			Application.activeApplication = new Application();
		}
		return Application.activeApplication;
	}

	/**
	 * Set the Application
	 */
	public static void setActiveApplication(Application activeApplication) {
		Application.activeApplication = activeApplication;
	}

	public IForm getActiveForm() {
		return this.getMainFrame().getFormManager().getActiveForm();
	}

	public void setActiveForm(IForm form) {
		this.getMainFrame().getFormManager().setActiveForm(form);
	}

	/**
	 * 重新发送一次
	 */
	public void resetActiveForm() {
		this.getMainFrame().getFormManager().resetActiveForm();
	}

	// 临时做的CtrlAction的管理
	private HashMap<String, HashMap<String, ICtrlAction>> ctrlActions = new HashMap<String, HashMap<String, ICtrlAction>>();

	public ICtrlAction getCtrlAction(String bundleName, String key) {
		ICtrlAction ctrlAction = null;
		try {
			HashMap<String, ICtrlAction> bundleCtrlActions = this.ctrlActions.get(bundleName);
			if (bundleCtrlActions.containsKey(key)) {
				ctrlAction = bundleCtrlActions.get(key);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return ctrlAction;
	}

	public void setCtrlAction(String bundleName, String key, ICtrlAction ctrlAction) {
		try {
			HashMap<String, ICtrlAction> bundleCtrlActions = null;
			if (this.ctrlActions.containsKey(bundleName)) {
				bundleCtrlActions = this.ctrlActions.get(bundleName);
			} else {
				bundleCtrlActions = new HashMap<String, ICtrlAction>();
				this.ctrlActions.put(bundleName, bundleCtrlActions);
			}

			bundleCtrlActions.put(key, ctrlAction);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	// 临时做的CtrlAction的管理

	/**
	 * Method doSomething documentation comment…
	 */
	public void initialize() {
		try {
			this.workspace = new Workspace();
			this.pluginManager = new PluginManager();
			this.workEnvironmentManager = new WorkEnvironmentManager();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void addActiveDatasourceChangedListener(ActiveDatasourcesChangeListener listener) {
		eventListenerList.add(ActiveDatasourcesChangeListener.class, listener);
	}

	public void removeActiveDatasourceChangedListener(ActiveDatasourcesChangeListener listener) {
		eventListenerList.remove(ActiveDatasourcesChangeListener.class, listener);
	}

	public void addActiveDatasetChangedListener(ActiveDatasetsChangeListener listener) {
		eventListenerList.add(ActiveDatasetsChangeListener.class, listener);
	}

	public void removeActiveDatasetChanagedListener(ActiveDatasetsChangeListener listener) {
		eventListenerList.remove(ActiveDatasetsChangeListener.class, listener);
	}

	protected void fireActiveDatasourcesChange(ActiveDatasourcesChangeEvent e) {
		Object[] listeners = eventListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActiveDatasourcesChangeListener.class) {
				((ActiveDatasourcesChangeListener) listeners[i + 1]).activeDatasourcesChange(e);
			}
		}
	}

	protected void fireActiveDatasetsChange(ActiveDatasetsChangeEvent e) {
		Object[] listeners = eventListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActiveDatasetsChangeListener.class) {
				((ActiveDatasetsChangeListener) listeners[i + 1]).activeDatasetsChange(e);
			}
		}
	}
}
