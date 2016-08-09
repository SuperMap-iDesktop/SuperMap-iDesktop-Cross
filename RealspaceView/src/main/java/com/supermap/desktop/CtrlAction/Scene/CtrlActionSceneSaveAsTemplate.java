package com.supermap.desktop.CtrlAction.Scene;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.realspaceview.RealspaceViewProperties;
import com.supermap.desktop.realspaceview.utilties.SaveAsSceneTemplateUtilties;
import com.supermap.realspace.Scene;

import java.text.MessageFormat;

public class CtrlActionSceneSaveAsTemplate extends CtrlAction{

	public CtrlActionSceneSaveAsTemplate(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run(){
		Scene scene = ((IFormScene)Application.getActiveApplication().getActiveForm()).getSceneControl().getScene();
		String message = RealspaceViewProperties.getString("String_SavaAsSceneTempalteFailed");
		if(scene != null){
			String mapTemplatePath = SaveAsSceneTemplateUtilties.saveAsSceneTemplate(scene.toXML());
			if(mapTemplatePath != null && mapTemplatePath.length() > 0){
				message = MessageFormat.format(RealspaceViewProperties.getString("String_SavaAsTemplateSuccessful"), mapTemplatePath);
			}
		}
		Application.getActiveApplication().getOutput().output(message);
	}
	
	@Override
	public boolean enable(){
		return true;
	}
	
}
