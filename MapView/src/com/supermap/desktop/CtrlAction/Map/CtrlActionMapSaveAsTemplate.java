package com.supermap.desktop.CtrlAction.Map;

import java.text.MessageFormat;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.utilties.SaveAsMapTemplateUtilties;
import com.supermap.mapping.Map;

public class CtrlActionMapSaveAsTemplate extends CtrlAction{

	public CtrlActionMapSaveAsTemplate(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run(){
		Map map = ((IFormMap)Application.getActiveApplication().getActiveForm()).getMapControl().getMap();
		String message = MapViewProperties.getString("String_SavaAsTemplateFailed");
		if(map != null){
			String mapTemplatePath = SaveAsMapTemplateUtilties.saveAsMapTemplate(map.toXML());
			if(mapTemplatePath != null && mapTemplatePath.length() > 0){
				message = MessageFormat.format(MapViewProperties.getString("String_SavaAsTemplateSuccessful"), mapTemplatePath);
			}
		}
		Application.getActiveApplication().getOutput().output(message);
	}
	
	@Override
	public boolean enable(){
		return true;
	}
}
