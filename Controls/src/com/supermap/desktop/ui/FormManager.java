package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.dialog.DialogSaveChildForms;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.DockingWindowAdapter;
import com.supermap.desktop.ui.docking.FloatingWindow;
import com.supermap.desktop.ui.docking.OperationAbortedException;
import com.supermap.desktop.ui.docking.RootWindow;
import com.supermap.desktop.ui.docking.TabWindow;
import com.supermap.desktop.ui.docking.View;
import com.supermap.desktop.ui.docking.event.WindowClosingEvent;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.HashMap;

public class FormManager implements IFormManager {
	private IFormMain mainForm = null;
	private TabWindow childWindowsContainer = null;
	private WindowType activatedChildFormType = WindowType.UNKNOWN;
	private IForm activeForm;
	private EventListenerList listenerList = new EventListenerList();
	private ArrayList<IForm> childForms = new ArrayList<IForm>();
	private DockingWindowAdapter dockingWindowAdapter = new DockingWindowAdapter() {
		@Override
		public void windowShown(DockingWindow window) {
			// 触发子窗体各自的实现方法
			if (window instanceof IForm) {
				childWindowActived(window);
				((IForm) window).windowShown();
			}
		}

		@Override
		public void windowHidden(DockingWindow window) {
			childWindowHidden();

			// 触发子窗体各自的实现方法
			if (window instanceof IForm) {
				((IForm) window).windowHidden();
			}
		}

		@Override
		public void viewFocusChanged(View previouslyFocusedView, View focusedView) {
			childWindowFocusChanged(focusedView);
		}

		@Override
		public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow) {
			childWindowAdded();
		}

		@Override
		public void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow) {
			childWindowRemoved(removedWindow);
		}

		@Override
		public void windowClosing(WindowClosingEvent evt) throws OperationAbortedException {
			childWindowClosing(evt);
		}

		@Override
		public void windowClosed(DockingWindow window) {
			childWindowClosed(window);
		}

		@Override
		public void windowUndocking(DockingWindow window) throws OperationAbortedException {
			childWindowUndocking();
		}

		@Override
		public void windowUndocked(DockingWindow window) {
			childWindowUndocked();
		}

		@Override
		public void windowDocking(DockingWindow window) throws OperationAbortedException {
			childWindowDocking();
		}

		@Override
		public void windowDocked(DockingWindow window) {
			childWindowDocked();
		}

		@Override
		public void windowMinimized(DockingWindow window) {
			childWindowMinimized();
		}

		@Override
		public void windowMaximized(DockingWindow window) {
			childWindowMaximized();
		}

		@Override
		public void windowRestored(DockingWindow window) {
			childWindowRestored();
		}

		@Override
		public void windowMaximizing(DockingWindow window) throws OperationAbortedException {
			childWindowMaximizing();
		}

		@Override
		public void windowMinimizing(DockingWindow window) throws OperationAbortedException {
			childWindowMinimizing();
		}

		@Override
		public void windowRestoring(DockingWindow window) throws OperationAbortedException {
			childWindowRestoring();
		}
	};

	public FormManager(IFormMain mainForm) {
		this.setMainForm(mainForm);
	}

	private IForm[] getMdiChildren() {
		return this.childForms.toArray(new IForm[this.childForms.size()]);
	}

	public TabWindow getChildWindowsContainer() {
		return this.childWindowsContainer;
	}

	public void setRootContainer(RootWindow rootWindow) {
		rootWindow.addListener(this.dockingWindowAdapter);
	}

	public void setChildWindowsContainer(TabWindow childWindowsContainer) {
		this.childWindowsContainer = childWindowsContainer;
	}

	@Override
	public IForm get(int index) {
		IForm result = null;
		try {
			result = this.childForms.get(index);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public void add(IForm form) {

		try {
			this.childForms.add(form);
			View childWindow = (View) form;
			this.getChildWindowsContainer().addTab(childWindow);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void add(int index, IForm form) {
		try {
			this.childForms.add(index, form);
			View childWindow = (View) form;
			this.getChildWindowsContainer().addTab(childWindow, index);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

	}

	@Override
	public int getCount() {
		return this.childForms.size();
	}

	@Override
	public IForm getActiveForm() {
		return this.activeForm;
	}

	@Override
	public void setActiveForm(IForm form) {
		try {
			IForm oldActiveForm = this.activeForm;
			this.activeForm = form;

			if (this.activeForm != oldActiveForm) {
				if (oldActiveForm != null) {
					oldActiveForm.deactived();
				}
				if (this.activeForm != null) {
					this.activeForm.actived();
				}
				ToolbarUtilties.updataToolbarsState();
				fireActiveFormChanged(new ActiveFormChangedEvent(this, oldActiveForm, form));
			}

			// 选中子窗体
			int index = this.childWindowsContainer.getChildWindowIndex((DockingWindow) form);
			if (index >= 0) {
				this.childWindowsContainer.setSelectedTab(index);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void resetActiveForm() {
		fireActiveFormChanged(new ActiveFormChangedEvent(this, null, this.activeForm));
	}

	@Override
	public void showChildForm(IForm childForm) {
		try {
			// 查找窗体，如果窗体不存在，就添加到TabWindow中
			int index = -1;
			for (int i = 0; i < this.childWindowsContainer.getChildWindowCount(); i++) {
				if (this.childWindowsContainer.getChildWindow(i).equals(childForm)) {
					index = i;
					break;
				}
			}

			if (index == -1) {
				// 将子窗体加入标签容器显示
				add(childForm);
			}

			this.setActiveForm(childForm);

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean close(IForm form) {
		boolean result = false;
		try {
			this.childForms.remove(form);
			form.clean();
			((DockingWindow) form).close();
			((DockingWindow) form).removeAll();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public boolean closeAll() {
		boolean result = false;
		try {
			for (int i = this.childForms.size() - 1; i >= 0; i--) {
				close(this.childForms.get(i));
			}
			result = true;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public boolean closeAll(boolean isSave) {
		return closeForms(getMdiChildren(), isSave);
	}

	/**
	 * 保存子窗口时是否进行提示
	 *
	 * @param notify 如果为true 则会根据窗体的具体修改情况弹出子窗体统一管理窗体，进行统一设置，否则直接进行保存。
	 * @return 是否执行完了保存操作，包括用户主动取消的保存
	 */
	@Override
	public boolean saveAll(boolean notify) {
		return saveForms(getMdiChildren(), notify);
	}

	/**
	 * 关闭指定的子窗口
	 *
	 * @param forms
	 * @param isSave
	 * @return
	 */
	public boolean closeForms(IForm[] forms, boolean isSave) {
		boolean result = true;

		try {
			if (isSave) {
				result = this.saveForms(forms, GlobalParameters.isShowFormClosingInfo());
			} else {
				for (int i = 0; i < forms.length; i++) {
					forms[i].setNeedSave(false);
				}
			}

			if (result) {
				HashMap<IForm, Boolean> formsNeedSaveStatus = new HashMap<IForm, Boolean>();

				for (IForm child : forms) {
					formsNeedSaveStatus.put(child, child.isNeedSave());
					child.setNeedSave(false);
				}
				result = closeForms(forms);

				for (IForm child : forms) {
					child.setNeedSave(formsNeedSaveStatus.get(child));
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/**
	 * 关闭指定的子窗口
	 *
	 * @param forms
	 * @return
	 */
	public boolean closeForms(IForm[] forms) {
		boolean result = false;

		try {
			for (int i = forms.length - 1; i >= 0; i--) {
				close(forms[i]);
			}
			result = true;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	/**
	 * 保存指定的子窗口
	 *
	 * @param forms
	 * @param notify 如果为true 则会根据窗体的具体修改情况弹出子窗体统一管理窗体，进行统一设置，否则直接进行保存。
	 * @return 无论是否保存，都返回 true，取消操作返回 false
	 */
	public boolean saveForms(IForm[] forms, boolean notify) {
		boolean result = false;

		try {
			if (notify) {
				ArrayList<IForm> canSaveForms = new ArrayList<IForm>();
				for (IForm child : forms) {
					boolean canSaved = false;
					if (child instanceof IFormMap) {
						canSaved = ((IFormMap) child).getMapControl().getMap().isModified();
					} else if (child instanceof IFormScene) {
						// 场景没有实现，始终需要保存
						canSaved = true;
					} else if (child instanceof IFormLayout) {
						canSaved = ((IFormLayout) child).getMapLayoutControl().getMapLayout().isModified();
					}

					if (canSaved) {
						canSaveForms.add(child);
					}
				}

				if (!canSaveForms.isEmpty()) {
					DialogSaveChildForms saveChildForms = new DialogSaveChildForms();
					saveChildForms.setAllForms(canSaveForms.toArray(new IForm[canSaveForms.size()]));
					DialogResult dialogResult = saveChildForms.showDialog();
					boolean saveLayer3DKML = saveChildForms.isSaveLayer3DKML();

					// 保存被选择的窗口
					if (dialogResult == DialogResult.YES) {

						IForm[] selectedForms = saveChildForms.getSelectedForms();
						for (IForm form : selectedForms) {
							if (form instanceof IFormMap) {
								if (Application.getActiveApplication().getWorkspace().getMaps().indexOf(form.getText()) >= 0) {
									form.save();
								} else {
									form.save(false, true);
								}
							} else if (form instanceof IFormScene) {
								if (saveLayer3DKML) {
									form.saveFormInfos();
								}

								if (Application.getActiveApplication().getWorkspace().getScenes().indexOf(form.getText()) >= 0) {
									form.save();
								} else {
									form.save(false, true);
								}
							} else if (form instanceof IFormLayout) {
								if (Application.getActiveApplication().getWorkspace().getLayouts().indexOf(form.getText()) >= 0) {
									form.save();
								} else {
									form.save(false, true);
								}
							} else {
								form.save();
							}
						}

						result = true;
					} else if (dialogResult == DialogResult.NO) {
						result = true;

						for (IForm form : saveChildForms.getSelectedForms()) {
							if (saveLayer3DKML && form instanceof IFormScene) {
								form.saveFormInfos();
							}
						}
					} else {
						result = false;
					}
				} else {
					result = true;
				}
			} else {
				result = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	public void raise_FormCreated(Object obj) {
		// 默认实现，后续进行初始化操作
	}

	@Override
	public void addActiveFormChangedListener(ActiveFormChangedListener listener) {
		this.listenerList.add(ActiveFormChangedListener.class, listener);
	}

	@Override
	public void removeActiveFormChangedListener(ActiveFormChangedListener listener) {
		this.listenerList.remove(ActiveFormChangedListener.class, listener);
	}

	protected void fireActiveFormChanged(ActiveFormChangedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActiveFormChangedListener.class) {
				((ActiveFormChangedListener) listeners[i + 1]).activeFormChanged(e);
			}
		}
	}

	private void childWindowActived(DockingWindow window) {
		try {
			setActiveForm((IForm) window);
			refreshMenusAndToolbars((IForm) window);

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void refreshMenusAndToolbars(IForm window) {
		// 获取之前激活的窗口的类型
		WindowType beforeType = this.activatedChildFormType;
		if (window != null) {
			this.activatedChildFormType = window.getWindowType();
		} else {
			this.activatedChildFormType = WindowType.UNKNOWN;
		}

		// 如果窗口类型不一致，刷新子选项卡和工具条
		if (beforeType != this.activatedChildFormType) {
			final FrameMenuManager frameMenuManager = (FrameMenuManager) Application.getActiveApplication().getMainFrame().getFrameMenuManager();
			final ToolbarManager toolbarManager = (ToolbarManager) Application.getActiveApplication().getMainFrame().getToolbarManager();

			boolean needRefersh = false;
			// 如果之前存在子窗口，则需要移除原来的子菜单和工具条
			if (beforeType != WindowType.UNKNOWN) {
				// 移除原子窗体的子菜单
				frameMenuManager.removeChildMenu(beforeType);
				// 移除原子窗体的子工具条
				toolbarManager.removeChildToolbar(beforeType);

				needRefersh = true;
			}

			// 如果切换后存在子窗口，则需要添加子菜单和工具条
			if (!this.activatedChildFormType.equals(WindowType.UNKNOWN)) {
				// 激活新子窗体的子菜单
				frameMenuManager.loadChildMenu(activatedChildFormType);
				// 激活新子窗体的子工具条
				toolbarManager.loadChildToolbar(activatedChildFormType);

				needRefersh = true;
			}

			if (needRefersh) {
				frameMenuManager.getMenuBar().updateUI();
				toolbarManager.getToolbarsContainer().repaint();
			}
		}
	}

	/**
	 * 当没有新的窗口激活，移出 Menus 和 Toolbars
	 */
	private void childWindowHidden() {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowFocusChanged(View focusedView) {
		try {
			if (focusedView != null && focusedView instanceof IForm && focusedView != this.activeForm) {
				setActiveForm((IForm) focusedView);
				refreshMenusAndToolbars((IForm) focusedView);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void childWindowAdded() {
		// 默认实现后续进行初始化操作
	}

	private void childWindowRemoved(DockingWindow removedWindow) {
		try {
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 窗口关闭响应事件
	 *
	 * @param evt
	 * @throws OperationAbortedException
	 */
	private void childWindowClosing(WindowClosingEvent evt) throws OperationAbortedException {
		try {
			if (evt.getWindow() instanceof IForm) {
				if (GlobalParameters.isShowFormClosingInfo()) {
					IForm form = (IForm) evt.getWindow();
					boolean isNeedSave = false;
					String message = "";

					if (form instanceof IFormMap) {
						// 地图 修改过才提示
						if (((IFormMap) form).getMapControl().getMap().isModified()) {
							isNeedSave = true;
							message = String.format(ControlsProperties.getString("String_SaveMapPrompt"), form.getText());
						}
					} else if (form instanceof IFormScene) {
						// 场景 组件不支持，始终提示
						isNeedSave = true;
						message = String.format(ControlsProperties.getString("String_SaveScenePrompt"), form.getText());
					} else if (form instanceof IFormLayout && ((IFormLayout) form).getMapLayoutControl().getMapLayout().isModified()) {
						isNeedSave = true;
						message = String.format(ControlsProperties.getString("String_SaveLayoutPrompt"), form.getText());
					}
					if (isNeedSave) {
						int result = UICommonToolkit.showConfirmDialogWithCancel(message);
						if (result == JOptionPane.YES_OPTION) {
							form.save();
							form.clean();
						} else if (result == JOptionPane.NO_OPTION) {
							// 不保存，直接关闭
							form.clean();
						} else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
							// 取消关闭操作
							evt.setCancel(true);
						}
					}
				}
			} else if (evt.getWindow() instanceof FloatingWindow && evt.getWindow().getChildWindowCount() > 0) {
				ArrayList<IForm> closingForms = new ArrayList<IForm>();
				DockingWindow dockingWindow = evt.getWindow().getChildWindow(0);

				if (dockingWindow instanceof TabWindow) {
					TabWindow tabWindow = (TabWindow) dockingWindow;

					for (int i = 0; i < tabWindow.getChildWindowCount(); i++) {
						DockingWindow childWindow = tabWindow.getChildWindow(i);
						if (childWindow instanceof IForm) {
							closingForms.add((IForm) childWindow);
						}
					}

					if (!closingForms.isEmpty() && !saveForms(closingForms.toArray(new IForm[closingForms.size()]), true)) {
						// 取消关闭操作
						evt.setCancel(true);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void childWindowClosed(DockingWindow window) {
		try {
			// DockingWindow 结构很奇特，以下代码用来判断是否需要移除一个子窗口
			if (window instanceof IForm) {
				this.childForms.remove(window);
			} else if (window instanceof FloatingWindow && window.getChildWindowCount() > 0) {
				DockingWindow dockingWindow = window.getChildWindow(0);
				if (dockingWindow instanceof TabWindow) {
					TabWindow tabWindow = (TabWindow) dockingWindow;
					for (int i = 0; i < tabWindow.getChildWindowCount(); i++) {
						DockingWindow childWindow = tabWindow.getChildWindow(i);
						if (childWindow instanceof IForm) {
							this.childForms.remove(childWindow);
						}
					}
				}
			}

			// 子窗口集合不包含 activeForm，表明之前移除了 activeForm，那么就需要切换另一个 activeForm 了
			if (!this.childForms.contains(this.activeForm)) {
				if (!childForms.isEmpty()) {
					this.setActiveForm(this.childForms.get(0));
				} else {
					// 为空时置空当前活动窗体
					this.setActiveForm(null);
				}
			}
			refreshMenusAndToolbars(getActiveForm());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void childWindowUndocking() throws OperationAbortedException {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowUndocked() {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowDocking() throws OperationAbortedException {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowDocked() {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowMinimized() {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowMaximized() {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowRestored() {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowMaximizing() throws OperationAbortedException {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowMinimizing() throws OperationAbortedException {
		// 默认实现，后续进行初始化操作
	}

	private void childWindowRestoring() throws OperationAbortedException {
		// 默认实现，后续进行初始化操作
	}

	public IFormMain getMainForm() {
		return mainForm;
	}

	public void setMainForm(IFormMain mainForm) {
		this.mainForm = mainForm;
	}

	@Override
	public boolean isContain(IFormMap formMap) {
		return childForms.contains(formMap);
	}
}
