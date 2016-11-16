package com.supermap.desktop.CtrlAction.Geometry;

import com.supermap.data.GeoCompound;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.SymbolMarker;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.mapeditor.PluginEnvironment;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.SymbolUtilties;
import com.supermap.mapping.Selection;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author XiaJT
 */
public class CtrlActionGeometryToSymbol extends CtrlAction {
	public CtrlActionGeometryToSymbol(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		supportGeometryTypeList = Arrays.asList(supportGeometryType);
	}

	private static final GeometryType[] supportGeometryType = new GeometryType[]{
			GeometryType.GEOPOINT,
			GeometryType.GEOLINE,
			GeometryType.GEOREGION,
			GeometryType.GEOTEXT,
			GeometryType.GEOARC,
			GeometryType.GEOCIRCLE,
			GeometryType.GEOBOX,
			GeometryType.GEOCOMPOUND,
			GeometryType.GEOELLIPTICARC,
			GeometryType.GEOLINEM,
			GeometryType.GEOROUNDRECTANGLE,
			GeometryType.GEOPIE
	};
	private static List<GeometryType> supportGeometryTypeList;


	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		Geometry geometry;
		if (activeForm instanceof IFormMap) {
			Selection[] selections = ((IFormMap) activeForm).getMapControl().getMap().findSelection(true);
			if (selections != null && selections.length > 0) {
				JDialogGeometryToSymbol jDialogGeometryToSymbol = new JDialogGeometryToSymbol();
				if (jDialogGeometryToSymbol.showDialog() == DialogResult.OK) {
					if (selections.length == 1) {
						geometry = selections[0].toRecordset().getGeometry();
					} else {
						GeoCompound geoCompound = new GeoCompound();
						for (Selection selection : selections) {

							Geometry tempGeometry = selection.toRecordset().getGeometry();
							if (supportGeometryTypeList.contains(tempGeometry.getType())) {
								geoCompound.addPart(tempGeometry);
							}
						}
						geometry = geoCompound;
					}
					SymbolMarker symbolMarker = new SymbolMarker();
					symbolMarker.fromGeometry(geometry, geometry.getBounds());
					symbolMarker.setName(jDialogGeometryToSymbol.getSymbolName());
					int id = SymbolUtilties.addMarkerSymbol(symbolMarker, jDialogGeometryToSymbol.getSymbolGroup());
					if (id != -1) {
						Application.getActiveApplication().getOutput().output(MessageFormat.format(MapEditorProperties.getString("String_GeometryToSymbolSuccess"), jDialogGeometryToSymbol.getSymbolName(), jDialogGeometryToSymbol.getSymbolGroup().getName(), String.valueOf(id)));
					} else {
						Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryToSymbolFailed"));
					}
				}
			}
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormMap && PluginEnvironment.getGeometryEditManager().instance().getEditProperties().getSelectedGeometryCount() > 0) {
			List<GeometryType> editableSelectedGeometryTypes = PluginEnvironment.getGeometryEditManager().instance().getEditProperties().getEditableSelectedGeometryTypes();
			for (GeometryType editableSelectedGeometryType : editableSelectedGeometryTypes) {
				if (!supportGeometryTypeList.contains(editableSelectedGeometryType)) {
					return false;
				}
			}
		}
		return true;
	}
}
