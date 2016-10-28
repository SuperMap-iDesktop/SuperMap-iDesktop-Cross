package com.supermap.desktop.CtrlAction.Geometry;

import com.supermap.data.GeoCompound;
import com.supermap.data.SymbolMarker;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.SymbolUtilties;
import com.supermap.mapping.Selection;

/**
 * @author XiaJT
 */
public class CtrlActionGeometryToSymbol extends CtrlAction {
	public CtrlActionGeometryToSymbol(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		GeoCompound geoCompound = new GeoCompound();
		if (activeForm instanceof IFormMap) {
			Selection[] selections = ((IFormMap) activeForm).getMapControl().getMap().findSelection(true);
			if (selections != null && selections.length > 0) {
				JDialogGeometryToSymbol jDialogGeometryToSymbol = new JDialogGeometryToSymbol();
				if (jDialogGeometryToSymbol.showDialog() == DialogResult.OK) {
					for (Selection selection : selections) {
						geoCompound.addPart(selection.toRecordset().getGeometry());
					}
					SymbolMarker symbolMarker = new SymbolMarker();
					symbolMarker.fromGeometry(geoCompound, geoCompound.getBounds());
					symbolMarker.setName(jDialogGeometryToSymbol.getSymbolName());
					SymbolUtilties.addMarkerSymbol(symbolMarker, jDialogGeometryToSymbol.getSymbolGroup());
				}
			}
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormMap) {
			if (((IFormMap) activeForm).getSelectedCount() > 0) {
				return true;
			}
		}
		return false;
	}
}
