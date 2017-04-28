package com.supermap.desktop.process.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.Application;
import com.supermap.desktop.Plugin;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.process.graphics.graphs.decorators.IDecorator;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/1/20.
 */
public abstract class AbstractGraph implements IGraph {
	private GraphCanvas canvas;
	protected Shape shape;
	private ConcurrentHashMap<String, IDecorator> decorators = new ConcurrentHashMap<>();
	private EventListenerList listenerList = new EventListenerList();

	private AbstractGraph() {
		// 反射用的
	}

	public AbstractGraph(GraphCanvas canvas, Shape shape) {
		// canvas的初始化在添加到画布的时候由画布设值
		this.canvas = canvas;
		this.shape = shape;
	}

	public Shape getShape() {
		return this.shape;
	}

	@Override
	public Rectangle getBounds() {
		if (this.shape != null) {
			return this.shape.getBounds();
		} else {
			return null;
		}
	}

	@Override
	public Rectangle getTotalBounds() {
		Rectangle totalBounds = getBounds();
		if (!GraphicsUtil.isRegionValid(totalBounds)) {
			return null;
		}

		for (String key :
				this.decorators.keySet()) {
			IDecorator decorator = this.decorators.get(key);
			Rectangle decoratorBounds = decorator.getBounds();

			if (GraphicsUtil.isRegionValid(decoratorBounds)) {
				totalBounds = totalBounds.union(decoratorBounds);
			}
		}
		return totalBounds;
	}

	@Override
	public Point getLocation() {
		if (this.shape != null) {
			return this.shape.getBounds().getLocation();
		} else {
			return null;
		}
	}

	@Override
	public Point getCenter() {
		if (this.shape != null) {
			double x = this.shape.getBounds().getX();
			double y = this.shape.getBounds().getY();
			double width = this.shape.getBounds().getWidth();
			double height = this.shape.getBounds().getHeight();
			Point center = new Point();
			center.setLocation(x + width / 2, y + height / 2);
			return center;
		} else {
			return null;
		}
	}

	@Override
	public int getWidth() {
		if (this.shape != null) {
			return this.shape.getBounds().width;
		} else {
			return -1;
		}
	}

	@Override
	public int getHeight() {
		if (this.shape != null) {
			return this.shape.getBounds().height;
		} else {
			return -1;
		}
	}

	@Override
	public void setLocation(Point point) {
		Point oldLocation = getShape().getBounds().getLocation();

		if (!oldLocation.equals(point)) {
			applyLocation(point);
			fireGraphBoundsChanged(new GraphBoundsChangedEvent(this, oldLocation, point));
		}
	}

	protected abstract void applyLocation(Point point);

	@Override
	public void setSize(int width, int height) {
		int oldWidth = getShape().getBounds().width;
		int oldHeight = getShape().getBounds().height;

		if (oldWidth != width || oldHeight != height) {
			applySize(width, height);
			fireGraphBoundsChanged(new GraphBoundsChangedEvent(this, oldWidth, width, oldHeight, height));
		}
	}

	protected abstract void applySize(int width, int height);

	@Override
	public boolean contains(Point point) {
		boolean result = this.shape.contains(point);

		if (!result) {
			for (String key :
					this.decorators.keySet()) {
				result = this.decorators.get(key).contains(point);
				if (result) {
					break;
				}
			}
		}
		return result;
	}

	@Override
	public void setCanvas(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public IDecorator[] getDecorators() {
		Set<Map.Entry<String, IDecorator>> entrySet = this.decorators.entrySet();
		IDecorator[] result = new IDecorator[entrySet.size()];
		entrySet.toArray(result);
		return result;
	}

	@Override
	public int getDecoratorSize() {
		return this.decorators.size();
	}

	@Override
	public IDecorator getDecorator(String key) {
		return this.decorators.get(key);
	}

	@Override
	public void addDecorator(String key, IDecorator decorator) {
		if (StringUtilities.isNullOrEmpty(key) || decorator == null) {
			return;
		}

		this.decorators.put(key, decorator);
		decorator.decorate(this);
	}

	@Override
	public void removeDecorator(String key) {
		if (this.decorators.containsKey(key)) {
			this.decorators.get(key).undecorate();
			this.decorators.remove(key);
		}
	}

	@Override
	public void removeDecorator(IDecorator decorator) {
		if (decorator == null) {
			return;
		}

		for (String key :
				this.decorators.keySet()) {
			if (this.decorators.get(key) == decorator) {
				this.decorators.remove(key);
				break;
			}
		}
		decorator.undecorate();
	}

	@Override
	public boolean isDecoratedBy(String key) {
		return this.decorators.containsKey(key);
	}

	@Override
	public boolean isDecoratedBy(IDecorator decorator) {
		return decorator != null && this.decorators.contains(decorator);
	}

	@Override
	public void addGraphBoundsChangedListener(GraphBoundsChangedListener listener) {
		this.listenerList.add(GraphBoundsChangedListener.class, listener);
	}

	@Override
	public void removeGraghBoundsChangedListener(GraphBoundsChangedListener listener) {
		this.listenerList.remove(GraphBoundsChangedListener.class, listener);
	}

	protected void fireGraphBoundsChanged(GraphBoundsChangedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphBoundsChangedListener.class) {
				((GraphBoundsChangedListener) listeners[i + 1]).graghBoundsChanged(e);
			}
		}
	}

	public final String toXml() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("class", this.getClass().getName());
		toXmlHook(jsonObject);
		return jsonObject.toString();
	}

	protected void toXmlHook(JSONObject jsonObject) {

	}


	@Override
	public final IGraph formXml(JSONObject xml) {
		formXmlHook(xml);
		return this;
	}

	@Override
	public void paint(Graphics g) {
		onPaint(g);

		for (String key :
				this.decorators.keySet()) {
			this.decorators.get(key).paint(g);
		}
	}

	protected abstract void onPaint(Graphics g);

	protected void formXmlHook(JSONObject xml) {

	}

	public static IGraph formXmlFile(String xml) {
		IGraph result = null;
		JSONObject jsonObject = JSONObject.parseObject(xml);
		String clazzName = (String) jsonObject.get("class");
		Plugin process = Application.getActiveApplication().getPluginManager().getBundle("SuperMap.Desktop.Process");
		try {
			Class<?> aClass = process.getBundle().loadClass(clazzName);
			Constructor<?> constructor = aClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			result = ((IGraph) constructor.newInstance());
			constructor.setAccessible(false);
			result.formXml(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
