package com.supermap.desktop.mapview.layer.propertymodel;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.utilties.StringUtilities;
import com.supermap.mapping.Layer;

import javax.swing.event.EventListenerList;
import java.util.HashMap;
import java.util.Set;

// @formatter:off
/**
 * 派生类需自行写代码实现属性可用状态的检测以及可用状态改变的事件发送过程
 * 派生类需自行写代码实现属性改变的检测以及属性值改变的事件发送过程
 * 
 * @author highsad
 *
 */
// @formatter:on
public abstract class LayerPropertyModel implements Cloneable {

	private Layer[] layers;
	private IFormMap formMap;
	private EventListenerList listenerList = new EventListenerList();
	protected HashMap<String, Boolean> propertyEnabled = new HashMap<String, Boolean>();

	public LayerPropertyModel() {

	}

	public LayerPropertyModel(Layer[] layers, IFormMap formMap) {
		this.layers = layers;
		this.formMap = formMap;
	}

	/**
	 * 获取封装的图层集合
	 *
	 * @return
	 */
	public Layer[] getLayers() {
		return layers;
	}

	/**
	 * 获取绑定的地图
	 *
	 * @return
	 */
	public IFormMap getFormMap() {
		return formMap;
	}

	/**
	 * 属性值更改之后，需要做对应的刷新操作，比如刷新地图显示等
	 */
	public final void refresh() {
		if (this.formMap != null && this.formMap.getMapControl() != null && this.formMap.getMapControl().getMap() != null) {
			this.formMap.getMapControl().getMap().refresh();
		}
	}

	/**
	 * 获取属性名称集合
	 *
	 * @return
	 */
	public Set<String> getPropertyNames() {
		return this.propertyEnabled.keySet();
	}

	/**
	 * 获取指定名称的属性是否可用
	 *
	 * @param propertyName
	 * @return
	 */
	public final boolean isPropertyEnabled(String propertyName) {
		return this.propertyEnabled.get(propertyName);
	}

	/**
	 * 将属性值应用到图层上
	 *
	 * @return
	 */
	public final boolean apply() {
		boolean result = false;
		try {
			if (this.layers != null && this.layers.length > 0) {
				for (Layer layer : layers) {
					if (!layer.isDisposed()) {
						apply(layer);
					}
				}
			}
			result = true;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final LayerPropertyModel clone() {
		LayerPropertyModel result = null;

		try {
			result = this.getClass().newInstance();
			result.layers = this.getLayers();
			result.formMap = this.getFormMap();
			result.setProperties(this);
			result.propertyEnabled = (HashMap<String, Boolean>) this.propertyEnabled.clone();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/**
	 * 添加对属性可用状态改变的监听
	 *
	 * @param listener
	 */
	public final void addPropertyEnabledChangeListener(PropertyEnabledChangeListener listener) {
		this.listenerList.add(PropertyEnabledChangeListener.class, listener);
	}

	/**
	 * 移除对属性可用状态改变的监听
	 *
	 * @param listener
	 */
	public final void removePropertyEnabledChangeListener(PropertyEnabledChangeListener listener) {
		this.listenerList.remove(PropertyEnabledChangeListener.class, listener);
	}

	/**
	 * 使用指定 model 的数据设置自己的属性 子类必须重写这个方法
	 *
	 * @param model
	 */
	public abstract void setProperties(LayerPropertyModel model);

	/**
	 * 清理资源、移除对图层的监听等
	 */
	public void clear() {
		// 子类根据需要实现这个方法
	}

	/**
	 * 子类必须重写这个方法
	 *
	 * @param model
	 * @return
	 */
	public boolean equals(LayerPropertyModel model) {
		return this.layers == model.getLayers() && this.formMap == model.getFormMap();
	}

	/**
	 * 在指定的图层上应用属性设置 子类必须重写这个方法
	 *
	 * @param layer
	 */
	protected abstract void apply(Layer layer);

	protected final void checkPropertyEnabled(String propertyName, boolean enabled) throws InvalidPropertyException {
		if (StringUtilities.isNullOrEmpty(propertyName)) {
			throw new InvalidPropertyException();
		}

		if (!propertyEnabled.containsKey(propertyName)) {
			throw new InvalidPropertyException("property is not exist.");
		}

		if (propertyEnabled.get(propertyName) != enabled) {
			propertyEnabled.put(propertyName, enabled);
			firePropertyEnabledChange(new PropertyEnabledChangeEvent(this, propertyName, enabled));
		}
	}

	/**
	 * 发送属性可用状态改变的事件
	 *
	 * @param e
	 */
	protected void firePropertyEnabledChange(PropertyEnabledChangeEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PropertyEnabledChangeListener.class) {
				((PropertyEnabledChangeListener) listeners[i + 1]).propertyEnabeldChange(e);
			}
		}
	}

}
