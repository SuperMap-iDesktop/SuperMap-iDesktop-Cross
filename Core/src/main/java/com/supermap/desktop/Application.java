package com.supermap.desktop;

import com.supermap.data.*;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.event.*;
import com.supermap.desktop.implement.Output;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.event.EventListenerList;
import java.util.*;

/**
 * 应用程序类，实现启动主窗口、插件管理和代码段编译执行等功能。
 */
public class Application {
	private final static String NODE_ROOT_NAME = "Desktop";
	private final static String NODE_CROSS_NAME = "Cross";
	private final static String NODE_WORKFLOWS_NAME = "Workflows";

	private IFormMain formMain = null;
	private ISplashForm formSplash = null;
	private Workspace workspace = null;
	private IOutput output = new Output();
	private WorkEnvironmentManager workEnvironmentManager = null;
	private PluginManager pluginManager = null;
	private ArrayList<Datasource> activeDatasources = new ArrayList<Datasource>();
	private ArrayList<Dataset> activeDatasets = new ArrayList<Dataset>();
	private ArrayList<IDataEntry<String>> workflowEntries = new ArrayList<>();

	private EventListenerList eventListenerList = new EventListenerList();
	private ArrayList<FormLoadedListener> formLoadedListeners = new ArrayList<>();
	private ArrayList<FormActivatedListener> formActivatedListeners = new ArrayList<FormActivatedListener>();
	private ArrayList<WorkflowsChangedListener> workflowsChangedListeners = new ArrayList<>();

	private WorkflowInitListener workflowInitListener;
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
						resetWorkflows();
					} catch (Exception e) {

					}
				}
			});
			workspace.addClosingListener(new WorkspaceClosingListener() {
				@Override
				public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
					for (int i = workflowEntries.size() - 1; i >= 0; i--) {
						removeWorkflowFromTree(workflowEntries.get(i));
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

	public ArrayList<IDataEntry<String>> getWorkflowEntries() {
		return this.workflowEntries;
	}

	public IDataEntry<String> getWorkflowEntry(String workflowName) {
		for (int i = 0; i < this.workflowEntries.size(); i++) {
			if (this.workflowEntries.get(i).getKey().equals(workflowName)) {
				return this.workflowEntries.get(i);
			}
		}
		return null;
	}

	private String[] getWorkflowNames() {
		String[] names = new String[this.workflowEntries.size()];

		for (int i = 0; i < this.workflowEntries.size(); i++) {
			names[i] = this.workflowEntries.get(i).getKey();
		}
		return names;
	}

	public void resetWorkflows() {
		Document desktopInfoDoc = getDesktopInfoDoc(this.workspace);
		Element workflowsNode = XmlUtilities.findElementNodeByName(desktopInfoDoc, "Workflows");
		Element[] workflowEntries = XmlUtilities.getChildElementNodesByName(workflowsNode, "WorkflowEntry");
		for (Element element : workflowEntries) {
			String workflowName = element.getAttribute("Name");
			this.workflowEntries.add(new StringEntry(workflowName, XmlUtilities.nodeToString(element)));
		}
		fireWorkflowsChanged(new WorkflowsChangedEvent(WorkflowsChangedEvent.RE_BUILD, this.workflowEntries.toArray(new IDataEntry[this.workflowEntries.size()])));
	}

	private IWorkflow fireWorkflowInitListener(Element element) {
		return workflowInitListener.init(element);
	}

	public void addWorkflow(String workflowName, String workflowValue) {
		IDataEntry<String> workflowEntry = new StringEntry(workflowName, workflowValue);
		this.workflowEntries.add(workflowEntry);
		addWorkflowInWorkspace(workflowEntry);
		addWorkflowInTree(workflowEntry);
	}

	private void addWorkflowInWorkspace(IDataEntry<String> workflowEntry) {
		Document desktopInfoDoc = getDesktopInfoDoc(this.workspace);
		Element workflowsNode = XmlUtilities.findElementNodeByName(desktopInfoDoc, "Workflows");

		Document tempDoc = XmlUtilities.stringToDocument(workflowEntry.getValue());
		Element tempWorkflowEntryNode = XmlUtilities.getChildElementNodeByName(tempDoc, "WorkflowEntry");
		if (tempWorkflowEntryNode == null) {
			return;
		}

		Element workflowEntryNode = (Element) desktopInfoDoc.importNode(tempWorkflowEntryNode, true);
		workflowsNode.appendChild(workflowEntryNode);
		this.workspace.setDesktopInfo(XmlUtilities.nodeToString(desktopInfoDoc));
	}

	private Document getDesktopInfoDoc(Workspace workspace) {
		if (workspace == null) {
			throw new NullPointerException();
		}

		boolean needInit = false;
		Document document = XmlUtilities.stringToDocument(workspace.getDesktopInfo());
		if (document == null) {
			document = XmlUtilities.getEmptyDocument();
			needInit = true;
		}

		Element desktopNode = XmlUtilities.getChildElementNodeByName(document, NODE_ROOT_NAME);
		if (desktopNode == null) {
			desktopNode = document.createElement(NODE_ROOT_NAME);
			document.appendChild(desktopNode);
			needInit = true;
		}

		Element crossNode = XmlUtilities.getChildElementNodeByName(desktopNode, NODE_CROSS_NAME);
		if (crossNode == null) {
			crossNode = document.createElement(NODE_CROSS_NAME);
			desktopNode.appendChild(crossNode);
			needInit = true;
		}

		Element workflowsNode = XmlUtilities.getChildElementNodeByName(crossNode, NODE_WORKFLOWS_NAME);
		if (workflowsNode == null) {
			workflowsNode = document.createElement(NODE_WORKFLOWS_NAME);
			crossNode.appendChild(workflowsNode);
			needInit = true;
		}

		if (needInit) {
			workspace.setDesktopInfo(XmlUtilities.nodeToString(document));
		}
		return document;
	}


	private void addWorkflowInTree(IDataEntry<String> workflowEntry) {
		fireWorkflowsChanged(new WorkflowsChangedEvent(WorkflowsChangedEvent.ADD, workflowEntry));
	}

	public void addWorkflow(int index, String workflowName, String workflowValue) {
		IDataEntry<String> workflowEntry = new StringEntry(workflowName, workflowValue);
		this.workflowEntries.add(index, workflowEntry);
		fireWorkflowsChanged(new WorkflowsChangedEvent(WorkflowsChangedEvent.ADD, workflowEntry));
	}

	public void removeWorkflow(String workflowName) {
		IDataEntry<String> workflowEntry = getWorkflowEntry(workflowName);

		if (workflowEntry != null) {
			removeWorkflowFromWorkspace(workflowName);
			removeWorkflowFromTree(workflowEntry);
		}
	}

	private void removeWorkflowFromTree(IDataEntry<String> workflowEntry) {
//		removeWorkflowEntry(workflowEntry);
		this.workflowEntries.remove(workflowEntry);
		fireWorkflowsChanged(new WorkflowsChangedEvent(WorkflowsChangedEvent.DELETE, workflowEntry));
	}

//	private void removeWorkflowEntry(String name) {
//		for (int i = 0; i < this.workflowEntries.size(); i++) {
//			IDataEntry dataEntry = this.workflowEntries.get(i);
//			if (dataEntry.getKey().equals(name)) {
//				this.workflowEntries.remove(i);
//				break;
//			}
//		}
//	}

	public void modifyWorkflowName(String oldName, String newName) {
		if (StringUtilities.isNullOrEmpty(oldName) || StringUtilities.isNullOrEmpty(newName)) {
			return;
		}

		if (StringUtilities.stringEquals(oldName, newName, false)) {
			return;
		}

		IDataEntry<String> workflowEntry = getWorkflowEntry(oldName);
		if (workflowEntry == null) {
			return;
		}

		// 修改内存对象的 name
		workflowEntry.setKey(newName);

		// 修改保存到工作空间的字符串
		Document desktopInfoDoc = getDesktopInfoDoc(this.workspace);
		Element workflowEntryNode = null;

		Element workflowsNode = XmlUtilities.findElementNodeByName(desktopInfoDoc, "Workflows");
		Element[] workflowsArray = XmlUtilities.getChildElementNodesByName(workflowsNode, "WorkflowEntry");
		for (Element element : workflowsArray) {
			if (element.getAttribute("Name").equals(oldName)) {
				workflowEntryNode = element;
				break;
			}
		}

		if (workflowEntryNode != null) {
			workflowEntryNode.setAttribute("Name", newName);
			Element workflowNode = XmlUtilities.getChildElementNodeByName(workflowEntryNode, "Workflow");
			workflowNode.setAttribute("Name", newName);

			workflowEntry.setValue(XmlUtilities.nodeToString(workflowEntryNode));
			this.workspace.setDesktopInfo(XmlUtilities.nodeToString(desktopInfoDoc));
		}
	}

	private void removeWorkflowFromWorkspace(String workflowName) {
		Document desktopInfoDoc = getDesktopInfoDoc(this.workspace);
		Element workflowsNode = XmlUtilities.findElementNodeByName(desktopInfoDoc, "Workflows");
		Element[] workflowsArray = XmlUtilities.getChildElementNodesByName(workflowsNode, "WorkflowEntry");
		for (Element element : workflowsArray) {
			if (element.getAttribute("name").equals(workflowName)) {
				workflowsNode.removeChild(element);
				break;
			}
		}
		String s = XmlUtilities.nodeToString(desktopInfoDoc);
		workspace.setDesktopInfo(s);
	}

	private void fireWorkflowsChanged(WorkflowsChangedEvent workflowsChangedEvent) {
		for (WorkflowsChangedListener workFlowsChangedListener : workflowsChangedListeners) {
			workFlowsChangedListener.workFlowsChanged(workflowsChangedEvent);
		}
	}

	public void addWorkflowsChangedListener(WorkflowsChangedListener workflowsChangedListener) {
		if (!workflowsChangedListeners.contains(workflowsChangedListener)) {
			workflowsChangedListeners.add(workflowsChangedListener);
		}
	}

	public void removeWorkflowsChangedListener(WorkflowsChangedListener workflowsChangedListener) {
		workflowsChangedListeners.remove(workflowsChangedListener);
	}

	public WorkflowInitListener getWorkflowInitListener() {
		return workflowInitListener;
	}

	public void setWorkflowInitListener(WorkflowInitListener workflowInitListener) {
		this.workflowInitListener = workflowInitListener;
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
