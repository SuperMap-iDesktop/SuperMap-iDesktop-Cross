package com.supermap.desktop.geometryoperation;

import java.util.HashMap;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;

public class EditManager {
	private HashMap<IFormMap, EditEnvironment> maps = new HashMap<>();

	public EditManager() {

	}

	/**
	 * 获取与 ActiveForm 绑定的 GeometryEdit 实例
	 * 
	 * @param formMap
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
			// 下一阶段的优化是完善子窗口的事件，提供关闭事件等，通过事件机制来处理清理的问题
			// @formatter:on
			clear();
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
				this.maps.get(formMap).clear();
				this.maps.remove(formMap);
			}
		}
	}
}
