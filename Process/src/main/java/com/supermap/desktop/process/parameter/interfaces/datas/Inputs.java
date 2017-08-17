package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.parameter.events.ValueProviderBindEvent;
import com.supermap.desktop.process.parameter.events.ValueProviderBindListener;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/3/1.
 */
public class Inputs {
	private IProcess process;
	private ConcurrentHashMap<String, InputData> datas = new ConcurrentHashMap<>();
	private ArrayList<ValueProviderBindListener> valueProviderBindListeners = new ArrayList<>();

	public Inputs(IProcess process) {
		this.process = process;
	}

	public IParameters getParameters() {
		return this.process.getParameters();
	}

	public boolean isContains(String name) {
		if (StringUtilities.isNullOrEmpty(name) || !this.datas.containsKey(name)) {
			return false;
		}

		return this.datas.containsKey(name);
	}

	public InputData getData(String name) {
		if (StringUtilities.isNullOrEmpty(name) || !this.datas.containsKey(name)) {
			return null;
		}

		return this.datas.get(name);
	}

	public void add(InputData data) {
		if (data == null) {
			return;
		}

		if (!this.datas.containsKey(data.getName())) {
			this.datas.put(data.getName(), data);
		}
	}

	public void addData(String name, Type type) {
		if (StringUtilities.isNullOrEmpty(name)) {
			return;
		}

		addData(new InputData(name, type));
	}

	public void addData(String name, String text, Type type) {
		if (StringUtilities.isNullOrEmpty(name)) {
			return;
		}

		addData(new InputData(name, text, type));
	}

	public void addData(InputData data) {
		if (data == null) {
			return;
		}

		if (!this.datas.containsKey(data.getName())) {
			this.datas.put(data.getName(), data);
		}
	}

	public InputData[] getDatas() {
		ArrayList<InputData> result = new ArrayList<>();

		for (String name : this.datas.keySet()) {
			result.add(this.datas.get(name));
		}
		return result.toArray(new InputData[this.datas.size()]);
	}

	public InputData[] getDatas(Type type) {
		ArrayList<InputData> result = new ArrayList<>();
		for (String name : this.datas.keySet()) {
			InputData data = this.datas.get(name);
			if (data.getType().intersects(type)) {
				result.add(data);
			}
		}
		return result.toArray(new InputData[result.size()]);
	}

	public int getCount() {
		return this.datas.size();
	}

	public void bind(String name, IValueProvider valueProvider) {
		if (this.datas.containsKey(name)) {
			this.datas.get(name).bind(valueProvider);
			fireValueProviderBind(new ValueProviderBindEvent(this.datas.get(name)));
		}
	}

	private void fireValueProviderBind(ValueProviderBindEvent event) {
		for (ValueProviderBindListener valueProviderBindListener : valueProviderBindListeners) {
			valueProviderBindListener.valueBind(event);
		}
	}

	public void addValueProviderBindListener(ValueProviderBindListener valueProviderBindListener) {
		valueProviderBindListeners.add(valueProviderBindListener);
	}

	public void removeValueProviderBindListener(ValueProviderBindListener valueProviderBindListener) {
		valueProviderBindListeners.remove(valueProviderBindListener);
	}

	public String getBindedInput(IValueProvider valueProvider) {
		for (String key :
				this.datas.keySet()) {
			if (this.datas.get(key).isBind(valueProvider)) {
				return key;
			}
		}

		return null;
	}

	public void unbind(String name) {
		if (this.datas.containsKey(name)) {
			this.datas.get(name).unbind();
			ValueProviderBindEvent event = new ValueProviderBindEvent(this.datas.get(name));
			event.setType(ValueProviderBindEvent.UNBIND);
			fireValueProviderBind(event);
		}
	}
}
