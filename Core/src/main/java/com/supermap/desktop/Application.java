package com.supermap.desktop;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Resources;
import com.supermap.data.SymbolGroup;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceClosingEvent;
import com.supermap.data.WorkspaceClosingListener;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.Interface.IOutput;
import com.supermap.desktop.Interface.ISplashForm;
import com.supermap.desktop.Interface.IWorkFlow;
import com.supermap.desktop.event.ActiveDatasetsChangeEvent;
import com.supermap.desktop.event.ActiveDatasetsChangeListener;
import com.supermap.desktop.event.ActiveDatasourcesChangeEvent;
import com.supermap.desktop.event.ActiveDatasourcesChangeListener;
import com.supermap.desktop.event.FormActivatedListener;
import com.supermap.desktop.event.FormLoadedListener;
import com.supermap.desktop.event.ResourcesChangedEvent;
import com.supermap.desktop.event.ResourcesChangedListener;
import com.supermap.desktop.event.WorkFlowChangedEvent;
import com.supermap.desktop.event.WorkFlowInitListener;
import com.supermap.desktop.event.WorkFlowsChangedListener;
import com.supermap.desktop.implement.Output;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Vector;

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
	private ArrayList<IWorkFlow> workFlows = new ArrayList<>();

	private EventListenerList eventListenerList = new EventListenerList();
	private ArrayList<FormLoadedListener> formLoadedListeners = new ArrayList<>();
	private ArrayList<FormActivatedListener> formActivatedListeners = new ArrayList<FormActivatedListener>();
	private ArrayList<WorkFlowsChangedListener> workFlowsChangedListeners = new ArrayList<>();

	private WorkFlowInitListener workFlowInitListener;
	private static final String splitLine = "____________________";
	private Vector<ResourcesChangedListener> resourcesChangedListeners = new Vector<>();

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
		final IFormMain formMainCopy = formMain;
		this.formMain = formMain;
		formMain.addFormLoadedListener(new FormLoadedListener() {
			@Override
			public void loadFinish(EventObject object) {
				fireFormLoadedEvent(object);//自动化使用，请勿删除
				formMainCopy.removeFormLoadedListener(this);
			}
		});
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


//	/**
//	 * Set the Workspace
//	 */
//	public void setWorkspace(Workspace workspace) {
//		this.workspace = workspace;
//	}

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
			workspace.addOpenedListener(new WorkspaceOpenedListener() {
				@Override
				public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {
					try {
						resetWorkFlows();
					} catch (Exception e) {
						// TODO 工作空间开放自定义存储之后，就不再需要存到 workspace 的 description 里了
					}
				}
			});
			workspace.addClosingListener(new WorkspaceClosingListener() {
				@Override
				public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
					for (int i = workFlows.size() - 1; i >= 0; i--) {
						removeWorkFlowFormTree(workFlows.get(i));
					}
				}
			});
			this.pluginManager = new PluginManager();
			this.workEnvironmentManager = new WorkEnvironmentManager();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	//自动化使用，请勿删除
	public void addFormLoadedListener(FormLoadedListener listener) {
		formLoadedListeners.add(listener);
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

	//自动化使用，请勿删除
	protected void fireFormLoadedEvent(EventObject eventObject) {
		for (int i = formLoadedListeners.size() - 1; i >= 0; i--) {
			formLoadedListeners.get(i).loadFinish(eventObject);
		}
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

	public ArrayList<IWorkFlow> getWorkFlows() {
		return workFlows;
	}

	public void resetWorkFlows() {
		String description = workspace.getDescription();
		String sourceDescription = "";
		//region 等一个接口
		String[] split = description.split(splitLine);
		if (split.length == 2) {
			sourceDescription = split[0];
			description = split[1];
		} else {
			description = "";
		}
		//endregion

		Document document = null;
		try {
			document = XmlUtilities.stringToDocument(description);
		} catch (Exception e) {
			// ignore
		}
		if (StringUtilities.isNullOrEmpty(description) || document == null) {
			String s = initWorkFlowXml();

			//region 等一个接口
			s = sourceDescription + splitLine + s;
			//endregion

			workspace.setDescription(s);
			return;
		} else {
			Node root = document.getChildNodes().item(0);
			Node workFlows = XmlUtilities.getChildElementNodeByName(root, "WorkFlows");
			Element[] workFlow = XmlUtilities.getChildElementNodesByName(workFlows, "WorkFlow");
			for (Element element : workFlow) {
				this.workFlows.add(fireWorkFlowInitListener(element));
			}
		}
		fireWorkFlowsChanged(new WorkFlowChangedEvent(WorkFlowChangedEvent.RE_BUILD, workFlows.toArray(new IWorkFlow[workFlows.size()])));
	}

	private IWorkFlow fireWorkFlowInitListener(Element element) {
		return workFlowInitListener.init(element);
	}

	public void addWorkFlow(IWorkFlow workFlow) {
		addWorkFlowInWorkspace(workFlow);
		addWorkFlowInTree(workFlow);
	}

	private void addWorkFlowInWorkspace(IWorkFlow workFlow) {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		String description = workspace.getDescription();

		String[] split = description.split(splitLine);

		if (split.length == 2) {
			description = split[1];
		}

		if (StringUtilities.isNullOrEmpty(description)) {
			description = initWorkFlowXml();
			workspace.setDescription(description);
		}
		Document document = XmlUtilities.stringToDocument(description);
		Node root = document.getChildNodes().item(0);
		Node workFlows = XmlUtilities.getChildElementNodeByName(root, "WorkFlows");
		Element workFlowNode = document.createElement("WorkFlow");
		workFlowNode.setAttribute("name", workFlow.getName());
		workFlowNode.setAttribute("value", workFlow.getMatrixXml());
		workFlows.appendChild(workFlowNode);
		String s = XmlUtilities.nodeToString(document, "UTF-8");

		//region 等一个接口
		s = workspace.getDescription().split(splitLine)[0] + splitLine + s;
		//endregion

		workspace.setDescription(s);
	}

	private String initWorkFlowXml() {
		String description;
		Document document = XmlUtilities.getEmptyDocument();
		Element root = XmlUtilities.createRoot(document, "root");
		Element workFlows = document.createElement("WorkFlows");
		root.appendChild(workFlows);
		description = XmlUtilities.nodeToString(document, "UTF-8");
		return description;
	}

	private void addWorkFlowInTree(IWorkFlow workFlow) {
		this.workFlows.add(workFlow);
		fireWorkFlowsChanged(new WorkFlowChangedEvent(WorkFlowChangedEvent.ADD, workFlow));
	}

	public void addWorkFlow(int index, IWorkFlow workFlow) {
		workFlows.add(index, workFlow);
		fireWorkFlowsChanged(new WorkFlowChangedEvent(WorkFlowChangedEvent.ADD, workFlow));
	}

	public void removeWorkFlow(IWorkFlow workFlow) {
		removeWorkFlowFormWorkspace(workFlow);
		removeWorkFlowFormTree(workFlow);
	}

	private void removeWorkFlowFormTree(IWorkFlow workFlow) {
		this.workFlows.remove(workFlow);
		fireWorkFlowsChanged(new WorkFlowChangedEvent(WorkFlowChangedEvent.DELETE, workFlow));
	}

	private void removeWorkFlowFormWorkspace(IWorkFlow workFlow) {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		String description = workspace.getDescription();
		description = description.split(splitLine)[1];
		Document document = XmlUtilities.stringToDocument(description);
		Node root = document.getChildNodes().item(0);
		Node workFlows = XmlUtilities.getChildElementNodeByName(root, "WorkFlows");
		Element[] workFlowsArray = XmlUtilities.getChildElementNodesByName(workFlows, "WorkFlow");
		for (Element element : workFlowsArray) {
			if (element.getAttribute("name").equals(workFlow.getName())) {
				workFlows.removeChild(element);
				break;
			}
		}
		String s = XmlUtilities.nodeToString(document, "UTF-8");

		//region 等一个接口 菩罰世諳若多侄羯冥世殿僧僧死怯栗冥陀侄曳栗多罰道瑟知冥摩一侄所婆皤若顛朋皤沙諳老倒諳多朋侄婆實
		s = workspace.getDescription().split(splitLine)[0] + splitLine + s;
		//endregion

		workspace.setDescription(s);
	}

	private void fireWorkFlowsChanged(WorkFlowChangedEvent workFlowChangedEvent) {
		for (WorkFlowsChangedListener workFlowsChangedListener : workFlowsChangedListeners) {
			workFlowsChangedListener.workFlowsChanged(workFlowChangedEvent);
		}
	}

	public void addWorkFlowChangedListener(WorkFlowsChangedListener workFlowsChangedListener) {
		if (!workFlowsChangedListeners.contains(workFlowsChangedListener)) {
			workFlowsChangedListeners.add(workFlowsChangedListener);
		}
	}

	public void removeWorkFlowChangedListener(WorkFlowsChangedListener workFlowsChangedListener) {
		workFlowsChangedListeners.remove(workFlowsChangedListener);
	}

	public WorkFlowInitListener getWorkFlowInitListener() {
		return workFlowInitListener;
	}

	public void setWorkFlowInitListener(WorkFlowInitListener workFlowInitListener) {
		this.workFlowInitListener = workFlowInitListener;
	}

	public void setWorkspaceDescribe(String description) {
		workspace.setDescription(description + splitLine + workspace.getDescription().split(splitLine)[1]);
	}

	public String getWorkspaceDescribe() {
		return workspace.getDescription().split(splitLine)[0];
	}

	public void setResourcesInfo(Resources currentResources, SymbolGroup currentSymbolGroup) {
		fireResourcesChanged(new ResourcesChangedEvent(currentResources, currentSymbolGroup));
	}

	private void fireResourcesChanged(ResourcesChangedEvent resourcesChangedEvent) {
		Vector<ResourcesChangedListener> listeners = resourcesChangedListeners;
		for (ResourcesChangedListener listener : listeners) {
			listener.resourcesChanged(resourcesChangedEvent);
		}
	}

	public void addResourcesChangedListener(ResourcesChangedListener resourcesChangeListener) {
		if (null != resourcesChangeListener && !resourcesChangedListeners.contains(resourcesChangeListener))
			resourcesChangedListeners.add(resourcesChangeListener);
	}

	public void removeResourcesChangedListener(ResourcesChangedListener resourcesChangeListener) {
		if (null != resourcesChangeListener && resourcesChangedListeners.contains(resourcesChangeListener))
			resourcesChangedListeners.remove(resourcesChangeListener);
	}
}
