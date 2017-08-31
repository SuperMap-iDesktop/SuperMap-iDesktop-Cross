package com.supermap.desktop.implement.UserDefineType;

import com.supermap.data.conversion.ImportSettingCSV;
import com.supermap.data.conversion.ImportSteppedEvent;
import com.supermap.data.conversion.ImportSteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.XlsUtilities;

import java.util.Vector;

/**
 * Created by xie on 2017/8/29.
 */
public class ImportSettingExcel extends ImportSettingCSV {
	private Vector<ImportSteppedListener> steppedListeners;

	public ImportSettingExcel() {

	}

	public UserDefineImportResult[] run() {
		UserDefineImportResult[] result = null;
		try {
			XlsUtilities.importSettingExcel = this;
			if (this.getSourceFilePath().endsWith("xls")) {
				result = XlsUtilities.importXlsFile(this.getTargetDatasource(), this.getSourceFilePath(), this.getFirstRowIsField());
			} else {
				result = XlsUtilities.importXlsxFile(this.getTargetDatasource(), this.getSourceFilePath(), this.getFirstRowIsField());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public void addImportSteppedListener(ImportSteppedListener listener) {
		if (null == steppedListeners) {
			steppedListeners = new Vector();
		}
		if (null != listener && !steppedListeners.contains(listener)) {
			steppedListeners.add(listener);
		}
	}

	public void removeImportSteppedListener(ImportSteppedListener listener) {
		if (null != listener && steppedListeners.contains(listener)) {
			steppedListeners.remove(listener);
		}
	}

	public void fireStepped(ImportSteppedEvent event) {
		if (null != steppedListeners) {
			Vector listeners = this.steppedListeners;
			for (int i = 0, size = listeners.size(); i < size; i++) {
				((ImportSteppedListener) listeners.get(i)).stepped(event);
			}
		}
	}
}
