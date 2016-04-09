package com.supermap.desktop.geometryoperation;

import java.util.HashMap;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;

public class GeometryEditManager {
	private HashMap<IFormMap, GeometryEditEnv> maps = new HashMap<>();

	public GeometryEditManager() {

	}

	/**
	 * 获取与 ActiveForm 绑定的 GeometryEdit 实例
	 * 
	 * @param formMap
	 * @return
	 */
	public GeometryEditEnv instance() {
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
	public GeometryEditEnv instance(IFormMap formMap) {
		GeometryEditEnv edit = null;

		if (this.maps.containsKey(formMap)) {
			edit = this.maps.get(formMap);
		} else if (formMap != null) {
			edit = GeometryEditEnv.createInstance(formMap);
			this.maps.put(formMap, edit);
		}
		return edit;
	}

	public void removeInstance(IFormMap formMap) {
		if (this.maps.containsKey(formMap)) {
			this.maps.remove(formMap);
		}
	}
}
