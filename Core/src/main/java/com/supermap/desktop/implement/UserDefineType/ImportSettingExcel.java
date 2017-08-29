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
	private Vector steppedListeners;

	public ImportSettingExcel() {

	}

	public UserDefineImportResult[] run() {
		UserDefineImportResult[] result = null;
		try {
			XlsUtilities.importSettingExcel = this;
			XlsUtilities.importXlsFile(this.getTargetDatasource(), this.getSourceFilePath(), this.getFirstRowIsField());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public void addSteppedListener(ImportSteppedListener listener) {
		if (null == steppedListeners) {
			steppedListeners = new Vector();
		}
		if (null != listener) {
			steppedListeners.add(steppedListeners);
		}
	}

	public void removeSteppedListener(ImportSteppedListener listener) {
		if (null != listener && steppedListeners.contains(listener)) {
			steppedListeners.remove(listener);
		}
	}

	public void fireStepped(ImportSteppedEvent event) {
		if (null != steppedListeners) {
			Vector listeners = this.steppedListeners;
			for (int i = 0, size = steppedListeners.size(); i < size; i++) {
				((ImportSteppedListener) steppedListeners.get(i)).stepped(event);
			}
		}
	}
}
