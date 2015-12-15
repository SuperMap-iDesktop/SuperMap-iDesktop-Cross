package com.supermap.desktop.mapview.layer.propertycontrols;

import javax.swing.JPanel;

import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.layer.propertymodel.LayerPropertyModel;
import com.supermap.desktop.mapview.layer.propertymodel.PropertyEnabledChangeEvent;
import com.supermap.desktop.mapview.layer.propertymodel.PropertyEnabledChangeListener;
import com.supermap.desktop.ui.UICommonToolkit;

// @formatter:off
/**
 * 图层的属性值改变之后，需要同步刷新对应控件的显示内容，这里面有几点需要考虑
 * 1：当前处于即时刷新状态，图层属性值发生改变，同步更新面板内容。
 * 2：当前处于非即时刷新状态，图层属性值发生改变，由于这种状态下，图层属性值与面板显示的内容可以不一致（在面板上做过操作），因此就需要考虑
 * 如果发生改变的值与控件显示的内容是否做同步，以及应用状态是否持续可用的问题。这里处理为，非即时刷新的时候，不做同步。
 * 3：目前图层属性上只有 Caption、可编辑、可选择、可见、可捕捉 这5个属性开放了事件监听值改变，因此只同步这部分，其他属性的改变需要调整结构才行，
 * 这在之后重构的时候再行考虑。
 * 4：在面板上修改应用会导致图层属性值的改变，而图层属性值的改变又会导致刷新面板上控件显示的值，这里面的逻辑关系很绕，需要谨慎处理，尽量保持单线，不要相互影响。
 * 5：目前的实现方式是，在 Container 里注册事件，然后重置面板内容
 * 
 * @author highsad
 *
 */
// @formatter:on
public abstract class AbstractLayerPropertyControl extends JPanel implements ILayerProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isChanged = false;
	private boolean isAutoApply = true;

	private transient LayerPropertyModel layerPropertyModel; // 原属性
	private transient LayerPropertyModel modifiedLayerPropertyModel; // 过程中修改的属性
	private transient PropertyEnabledChangeListener enabledChangeListener = new PropertyEnabledChangeListener() {

		@Override
		public void propertyEnabeldChange(PropertyEnabledChangeEvent e) {
			setControlEnabled(e.getPropertyName(), e.getEnabled());
		}
	};

	public AbstractLayerPropertyControl() {
		initializeComponents();
		initializeResources();
	}

	public LayerPropertyModel getLayerPropertyModel() {
		return this.layerPropertyModel;
	}

	public final void setLayerPropertyModel(LayerPropertyModel layerPropertyModel) {
		if (this.modifiedLayerPropertyModel != null) {
			this.modifiedLayerPropertyModel.removePropertyEnabledChangeListener(enabledChangeListener);
			this.modifiedLayerPropertyModel.clear();
			this.layerPropertyModel.clear();
		}

		this.layerPropertyModel = layerPropertyModel;
		if (layerPropertyModel != null) {
			this.modifiedLayerPropertyModel = layerPropertyModel.clone();
			this.modifiedLayerPropertyModel.addPropertyEnabledChangeListener(enabledChangeListener);
		}
		unregisterEvents();
		setControlEnabled();
		fillComponents();
		registerEvents();
	}

	@Override
	public final boolean isChanged() {
		return this.isChanged;
	}

	@Override
	public final boolean isAutoApply() {
		return this.isAutoApply;
	}

	@Override
	public final void setAutoApply(boolean autoApply) {
		this.isAutoApply = autoApply;
		if (this.isAutoApply) {
			apply();
		}
	}

	@Override
	public final void addLayerPropertyChangedListener(ChangedListener listener) {
		listenerList.add(ChangedListener.class, listener);
	}

	@Override
	public final void removeLayerPropertyChangedListener(ChangedListener listener) {
		listenerList.remove(ChangedListener.class, listener);
	}

	@Override
	public final boolean apply() {
		boolean result = true;

		try {
			LayerPropertyContainer.setResetFlag(false);
			getLayerPropertyModel().setProperties(getModifiedLayerPropertyModel());
			getLayerPropertyModel().apply();
			getLayerPropertyModel().refresh();
			this.isChanged = false;
		} catch (Exception e) {
			result = false;
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			LayerPropertyContainer.setResetFlag(true);
		}
		return result;
	}

	protected void fireLayerPropertyChanged(ChangedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangedListener.class) {
				((ChangedListener) listeners[i + 1]).changed(e);
			}
		}
	}

	protected LayerPropertyModel getModifiedLayerPropertyModel() {
		return this.modifiedLayerPropertyModel;
	}

	protected final void checkChanged() {
		if (this.isAutoApply()) {
			apply();

			// 当图层树没有焦点时，在其他地方更改了可显示、可编辑等，图层树上对应节点不会刷新。应用编辑之后，刷新图层树。
			UICommonToolkit.getLayersManager().getLayersTree().updateUI();
		} else {
			int state = getLayerPropertyModel().equals(getModifiedLayerPropertyModel()) ? ChangedEvent.UNCHANGED : ChangedEvent.CHANGED;
			fireLayerPropertyChanged(new ChangedEvent(this, state));
		}
	}

	/**
	 * 初始化控件，该构造的构造，该加载的加载，该布局的布局，子类必须重写这个方法
	 */
	protected abstract void initializeComponents();

	/**
	 * 资源化，子类必须重写这个方法
	 */
	protected abstract void initializeResources();

	/**
	 * 往控件里填充数据，子类必须重写这个方法
	 */
	protected abstract void fillComponents();

	/**
	 * 注册事件，子类必须重写这个方法 由于在基类的构造函数调用这个方法， 为了避免基类调用的时候子类的 Listener 还没有构造的情况，需要使用单例模式 在子类的方法实现里构造对应的 Listener
	 */
	protected abstract void registerEvents();

	/**
	 * 注销事件，子类必须重写这个方法
	 */
	protected abstract void unregisterEvents();

	/**
	 * 当属性可用状态发生改变时，设置对应控件的可用状态
	 * 
	 * @param propertyName
	 * @param enabled
	 */
	protected abstract void setControlEnabled(String propertyName, boolean enabled);

	private void setControlEnabled() {
		for (String propertyName : this.modifiedLayerPropertyModel.getPropertyNames()) {
			setControlEnabled(propertyName, this.modifiedLayerPropertyModel.isPropertyEnabled(propertyName));
		}
	}
}
