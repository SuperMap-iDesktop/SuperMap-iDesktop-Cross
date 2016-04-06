package com.supermap.desktop.dialog.symbolDialogs.symbolTrees;

import com.supermap.data.Resources;
import com.supermap.data.SymbolLibrary;
import com.supermap.data.SymbolType;
import com.supermap.desktop.utilties.LogUtilties;

/**
 * 符号工厂类
 * 根据指定的资源和符号类型得到对应的符号库
 *
 * @author XiaJt
 */
public class SymbolFactory {
	private SymbolFactory() {
		// you cannot do it
	}

	public static SymbolLibrary getSymbolLibrary(Resources resources, SymbolType symbolType) {
		if (symbolType == SymbolType.MARKER || symbolType == SymbolType.MARKER3D) {
			return resources.getMarkerLibrary();
		} else if (symbolType == SymbolType.LINE || symbolType == SymbolType.PIPENODE) {
			return resources.getLineLibrary();
		} else if (symbolType == SymbolType.FILL) {
			return resources.getFillLibrary();
		}
		LogUtilties.debug("unSupport symbol type :" + symbolType.name());
		return null;
	}
}
