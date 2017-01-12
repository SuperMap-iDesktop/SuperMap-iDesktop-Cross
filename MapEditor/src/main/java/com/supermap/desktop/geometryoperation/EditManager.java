package com.supermap.desktop.geometryoperation;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.event.FormClosedEvent;
import com.supermap.desktop.event.FormClosedListener;
import com.supermap.desktop.event.FormClosingEvent;
import com.supermap.desktop.event.FormClosingListener;

import java.util.HashMap;

public class EditManager {
	private HashMap<IFormMap, EditEnvironment> maps = new HashMap<>();
	FormClosingListener formClosingListener = new FormClosingListener() {
		@Override
		public void formClosing(FormClosingEvent e) {
			try {
				if (e.getForm() instanceof IFormMap) {
					IFormMap formMap = (IFormMap) e.getForm();
					if (EditManager.this.maps.containsKey(formMap)) {
						EditEnvironment environment = EditManager.this.maps.get(formMap);
						environment.preClear();
					}
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	};

	FormClosedListener formClosedListener = new FormClosedListener() {
		@Override
		public void formClosed(FormClosedEvent e) {
			try {
				if (e.getForm() instanceof IFormMap) {
					IFormMap formMap = (IFormMap) e.getForm();
					if (EditManager.this.maps.containsKey(formMap)) {
						EditEnvironment environment = EditManager.this.maps.get(formMap);
						environment.clear();
						remove(formMap);
					}
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	};

	public EditManager() {

	}

	/**
	 * 获取与 ActiveForm 绑定的 GeometryEdit 实例
	 *
	 * @return
	 */
	public EditEnvironment instance() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();

		if (activeForm instanceof IFormMap) {
			return instance((IFormMap) activeForm);
		}
		return null;
	}

	/**
	 * 获取与指定 formMap 绑定的 GeometryEdit 实例
	 *
	 * @param formMap
	 * @return
	 */
	public EditEnvironment instance(IFormMap formMap) {
		EditEnvironment edit = null;

		if (this.maps.containsKey(formMap)) {
			edit = this.maps.get(formMap);
		} else if (formMap != null) {
			// @formatter:off
			// 获取一个新的 EditEnvironment 之前先清理一下失效的、已关闭的 form。
			// @formatter:on
			clear();

			formMap.addFormClosingListener(this.formClosingListener);
			formMap.addFormClosedListener(this.formClosedListener);
			edit = EditEnvironment.createInstance(formMap);
			this.maps.put(formMap, edit);
		}
		return edit;
	}

	public void removeInstance(IFormMap formMap) {
		if (this.maps.containsKey(formMap)) {
			this.maps.remove(formMap);
		}
	}

	private void clear() {
		for (IFormMap formMap : this.maps.keySet()) {
			if (formMap == null || formMap.isClosed()) {
				remove(formMap);
			}
		}
	}

	private void remove(IFormMap formMap) {
		if (formMap != null && this.maps.containsKey(formMap)) {
			formMap.removeFormClosingListener(this.formClosingListener);
			formMap.removeFormClosedListener(this.formClosedListener);
			this.maps.remove(formMap);
		}
	}
}
