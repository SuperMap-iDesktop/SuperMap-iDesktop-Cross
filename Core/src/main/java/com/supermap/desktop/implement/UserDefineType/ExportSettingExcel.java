package com.supermap.desktop.implement.UserDefineType;

import com.supermap.data.DatasetVector;
import com.supermap.data.conversion.ExportSettingCSV;
import com.supermap.data.conversion.ExportSteppedEvent;
import com.supermap.data.conversion.ExportSteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.XlsUtilities;

import java.util.Vector;

/**
 * Created by xie on 2017/8/30.
 */
public class ExportSettingExcel extends ExportSettingCSV {
	private Vector<ExportSteppedListener> steppedListeners;

	public UserDefineExportResult run() {
		UserDefineExportResult result = null;
		try {
			XlsUtilities.exportSettingExcel = this;
			result = XlsUtilities.exportXlsxFile(((DatasetVector) this.getSourceData()), this.getTargetFilePath(), this.getIsExportFieldName());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public void addExportSteppedListener(ExportSteppedListener listener) {
		if (null == steppedListeners) {
			steppedListeners = new Vector<>();
		}
		if (null != listener && !steppedListeners.contains(listener)) {
			steppedListeners.add(listener);
		}
	}

	public void removeExportSteppedListener(ExportSteppedListener listener) {
		if (null != listener && steppedListeners.contains(listener)) {
			steppedListeners.remove(listener);
		}
	}

	public void fireStepped(ExportSteppedEvent event) {
		if (null != steppedListeners) {
			Vector<ExportSteppedListener> listeners = this.steppedListeners;
			for (int i = 0, size = listeners.size(); i < size; i++) {
				((ExportSteppedListener) listeners.get(i)).stepped(event);
			}
		}
	}

}
