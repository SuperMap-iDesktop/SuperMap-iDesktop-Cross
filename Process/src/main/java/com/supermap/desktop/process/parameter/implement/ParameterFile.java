package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XiaJT
 */
public class ParameterFile extends AbstractParameter implements ISelectionParameter {

	@ParameterField(name = "value")
	private String selectedPath;
	private String describe;
	private Map<String, Vector<String>> filtesMap = new ConcurrentHashMap<>();
	private String moduleName;
	private String title;
	private String moduleType;

	private int fileSelectionMode = 0;

	public ParameterFile(String describe) {
		this.describe = describe;
	}

	public ParameterFile() {
		this("");
	}

	@Override
	public String getType() {
		return ParameterType.FILE;
	}

	@Override
	public void setSelectedItem(Object value) {
		String oldValue = this.selectedPath;
		this.selectedPath = (String) value;
//		if (value instanceof File) {
//			selectedFile = (File) value;
//		} else if (value instanceof String && new File((String) value).exists()) {
//			selectedFile = new File((String) value);
//		}
		firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, selectedPath));
	}


	@Override
	public Object getSelectedItem() {
		return selectedPath;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterFile setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	public int getFilterCount() {
		return this.filtesMap.size();
	}

	public Set<String> getFilterNames() {
		return this.filtesMap.keySet();
	}

	public String[] getExtensions(String filterName) {
		if (this.filtesMap.containsKey(filterName)) {
			Vector<String> extensions = this.filtesMap.get(filterName);
			return extensions.toArray(new String[extensions.size()]);
		}
		return null;
	}

	public void addExtension(String filterName, String extension) {
		if (StringUtilities.isNullOrEmpty(filterName)) {
			return;
		}

		if (!this.filtesMap.containsKey(filterName)) {
			this.filtesMap.put(filterName, new Vector<String>());
		}

		Vector<String> extensions = this.filtesMap.get(filterName);
		if (!extensions.contains(extension)) {
			extensions.add(extension);
		}
	}

	public String getFilters() {
		if (this.filtesMap.size() == 0) {
			return null;
		}

		Vector<String> filters = new Vector<>();
		for (String filterName :
				this.filtesMap.keySet()) {
			Vector<String> extensions = this.filtesMap.get(filterName);

			if (extensions != null && extensions.size() > 0) {
				filters.add(FileUtilities.buildFileFilter(filterName, extensions.toArray(new String[extensions.size()])));
			}
		}
		return FileUtilities.buildFileFilters(filters.toArray(new String[filters.size()]));
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTitle() {
		return title;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public void setFileSelectionMode(int fileSelectionMode) {
		this.fileSelectionMode = fileSelectionMode;
	}

	public int getFileSelectionMode() {
		return fileSelectionMode;
	}

	@Override
	public void dispose() {

	}
}
