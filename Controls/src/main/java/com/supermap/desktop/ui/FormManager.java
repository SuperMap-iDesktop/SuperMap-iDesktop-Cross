package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dialog.DialogSaveChildForms;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.event.CancellationEvent;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.events.*;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.HashMap;

public class FormManager extends MdiGroup implements IFormManager {
	private WindowType activatedChildFormType = WindowType.UNKNOWN;
	private IForm activeForm;
	private EventListenerList listenerList = new EventListenerList();
	private ArrayList<IForm> childForms = new ArrayList<IForm>();
	private MdiGroup mdiGroup;
	private PageActivatedListener pageActivatedListener = new PageActivatedListener() {

		@Override
		public void pageActivated(PageActivatedEvent event) {
			if (event.getOldActivedPage() != null && event.getOldActivedPage().getComponent() instanceof FormBaseChild) {
				((FormBaseChild) event.getOldActivedPage().getComponent()).deactived();
			}

			if (event.getActivedPage() != null && event.getActivedPage().getComponent() instanceof FormBaseChild) {
				((FormBaseChild) event.getActivedPage().getComponent()).actived();
			}
			ToolbarUIUtilities.updataToolbarsState();
			fireActiveFormChanged(new ActiveFormChangedEvent(this, (IForm) event.getOldActivedPage().getComponent(), (IForm) event.getActivedPage().getComponent()));
			refreshMenusAndToolbars((IForm) event.getActivedPage().getComponent());
		}
	};

	private PageClosingListener pageClosingListener = new PageClosingListener() {
		@Override
		public void pageRemoving(PageClosingEvent e) {
			if (e.getPage() != null && e.getPage().getComponent() instanceof FormBaseChild) {
				CancellationEvent cancellation = new CancellationEvent(e.getPage().getComponent(), false);
				((FormBaseChild) e.getPage().getComponent()).formClosing(cancellation);
				e.setCancel(cancellation.isCancel());
			}
		}
	};

	private PageClosedListener pageClosedListener = new PageClosedListener() {
		@Override
		public void pageRemoved(PageClosedEvent e) {
			if (e.getPage() != null && e.getPage().getComponent() instanceof FormBaseChild) {
				((FormBaseChild) e.getPage().getComponent()).windowClosed();
			}
		}
	};

	public MdiGroup getContentPane() {
		return this.mdiGroup;
	}

	public FormManager() {
		super(null);
		this.mdiGroup = new MdiGroup(null);
		addPageActivatedListener(this.pageActivatedListener);
		addPageClosingListener(this.pageClosingListener);
		addPageClosedListener(this.pageClosedListener);
	}

	private IForm[] getMdiChildren() {
		return this.childForms.toArray(new IForm[this.childForms.size()]);
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
		add(form, getPageCount());
	}

	@Override
	public void add(IForm form, int index) {
		try {
			if (form instanceof FormBaseChild) {
				MdiPage page = MdiPage.createMdiPage((FormBaseChild) form, ((FormBaseChild) form).getText(), true, false);
				addPage(page, index);
			} else {
				Application.getActiveApplication().getOutput().output("error.");
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

	}

	@Override
	public int getCount() {
		return getPageCount();
	}

	@Override
	public IForm getActiveForm() {
		return (FormBaseChild) getActivePage().getComponent();
	}

	@Override
	public void setActiveForm(IForm form) {
		activePage((FormBaseChild) form);
	}

	@Override
	public void resetActiveForm() {
		fireActiveFormChanged(new ActiveFormChangedEvent(this, null, this.activeForm));
	}

	@Override
	public void showChildForm(IForm childForm) {
		try {
			if (!(childForm instanceof FormBaseChild)) {
				return;
			}

			MdiPage page = getPage((FormBaseChild) childForm);
			if (page == null) {
				page = addPage((FormBaseChild) childForm);
			} else {
				activePage(page);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean close(IForm form) {
		boolean isClosed = false;
		if (form instanceof FormBaseChild) {
			isClosed = super.close((FormBaseChild) form);
		}
		return isClosed;
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
				result = this.saveForms(forms, true);
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
			if (notify && GlobalParameters.isShowFormClosingInfo()) {
				ArrayList<IForm> canSaveForms = new ArrayList<IForm>();
				for (IForm child : forms) {
					boolean canSaved = false;
					if (child.getWindowType() == WindowType.MAP) {
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

	@Override
	public boolean isContain(IForm form) {
		if (form instanceof FormBaseChild) {
			return getPage((FormBaseChild) form) != null;
		} else {
			return false;
		}
	}
}
