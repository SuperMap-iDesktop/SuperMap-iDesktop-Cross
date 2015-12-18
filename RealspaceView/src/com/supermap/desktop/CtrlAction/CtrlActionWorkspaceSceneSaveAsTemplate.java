package com.supermap.desktop.CtrlAction;

import java.text.MessageFormat;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.realspaceview.RealspaceViewProperties;
import com.supermap.desktop.realspaceview.utilties.SaveAsSceneTemplateUtilties;

public class CtrlActionWorkspaceSceneSaveAsTemplate extends CtrlAction{

	public CtrlActionWorkspaceSceneSaveAsTemplate(IBaseItem caller,
																																																																																																																																																																	IForm formClass) {
		super(caller, formClass);
	}
	
	@Override
	public void run(){
		String sceneXml = SaveAsSceneTemplateUtilties.getActiveSceneXml();
		String message = RealspaceViewProperties.getString("String_SavaAsSceneTempalteFailed");
		if(sceneXml != null && sceneXml.length() > 0){
			String sceneTemplatePath = SaveAsSceneTemplateUtilties.saveAsSceneTemplate(sceneXml);
			if(sceneTemplatePath != null && sceneTemplatePath.length() > 0){
				message = MessageFormat.format(RealspaceViewProperties.getString("String_SavaAsTemplateSuccessful"), sceneTemplatePath);
			}
		}
		Application.getActiveApplication().getOutput().output(message);
	}
	
	@Override
	public boolean enable(){
		return true;
	}
}
