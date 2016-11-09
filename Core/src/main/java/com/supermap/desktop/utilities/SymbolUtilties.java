package com.supermap.desktop.utilities;

import com.supermap.data.SymbolGroup;
import com.supermap.data.SymbolMarker;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.enums.SymbolChangeType;
import com.supermap.desktop.event.SymbolChangedEvent;
import com.supermap.desktop.event.SymbolChangedListener;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class SymbolUtilties {

	public static ArrayList<SymbolChangedListener> symbolChangedListeners = new ArrayList<>();

	public static int addMarkerSymbol(SymbolMarker symbolMarker) {
		return addMarkerSymbol(symbolMarker, Application.getActiveApplication().getWorkspace().getResources().getMarkerLibrary().getRootGroup());
	}

	public static int addMarkerSymbol(SymbolMarker symbolMarker, SymbolGroup symbolGroup) {
		int id = Application.getActiveApplication().getWorkspace().getResources().getMarkerLibrary().add(symbolMarker);
		if (symbolGroup != Application.getActiveApplication().getWorkspace().getResources().getMarkerLibrary().getRootGroup()) {
			Application.getActiveApplication().getWorkspace().getResources().getMarkerLibrary().moveTo(id, symbolGroup);
		}
		fireSymbolChangedListener(new SymbolChangedEvent(SymbolType.MARKER, id, SymbolChangeType.Add, symbolGroup));
		return id;
	}

	private static void fireSymbolChangedListener(SymbolChangedEvent symbolChangedEvent) {
		for (SymbolChangedListener symbolChangedListener : symbolChangedListeners) {
			symbolChangedListener.symbolChanged(symbolChangedEvent);
		}
	}

	public static void addSymbolChangedListener(SymbolChangedListener symbolChangedListener) {
		if (!symbolChangedListeners.contains(symbolChangedListener)) {
			symbolChangedListeners.add(symbolChangedListener);
		}
	}

	public static void removeSymbolChangedListener(SymbolChangedListener symbolChangedListener) {
		symbolChangedListeners.remove(symbolChangedListener);
	}
}
