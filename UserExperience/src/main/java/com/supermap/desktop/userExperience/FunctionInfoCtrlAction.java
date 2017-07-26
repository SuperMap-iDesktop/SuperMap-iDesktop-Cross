package com.supermap.desktop.userExperience;

import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.event.DesktopRuntimeEvent;
import com.supermap.desktop.implement.ControlButton;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.implement.SmButtonDropdown;
import com.supermap.desktop.implement.SmCtrlActionButton;
import com.supermap.desktop.implement.SmMenu;
import com.supermap.desktop.implement.SmMenuItem;
import com.supermap.desktop.implement.SmPopupMenu;
import com.supermap.desktop.implement.SmToolbar;
import com.supermap.desktop.interfaces.UserExperienceBean;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author XiaJT
 */
public class FunctionInfoCtrlAction implements FunctionInfo, UserExperienceBean, JSONAware {

	private String caption;
	private String ctrlActionName;
	private String pluginName;
	private long totalTime;
	private String functionGrade0;
	private String functionGrade1;
	private String functionGrade2;
	private String functionGrade3;
	private long executeDateTime;
	private boolean isFinished;
	private String message;
	private String stackTrace;
	private boolean isException;
	private long currentMemory;
	private long memoryIncrement;
	private long memoryTotal = Runtime.getRuntime().totalMemory();
	private CtrlAction ctrlAction;

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public FunctionInfoCtrlAction(String action) throws ParseException {
		isException = true;
		JSONObject parse = null;
		parse = JSONObject.parseObject(action);
		caption = parse.getString("Caption");
		ctrlActionName = parse.getString("CtrlActionName");
		executeDateTime = parse.getLong("ExecuteDateTime");
		functionGrade0 = parse.getString("FunctionGrade0");
		functionGrade1 = parse.getString("FunctionGrade1");
		functionGrade2 = parse.getString("FunctionGrade2");
		functionGrade3 = parse.getString("FunctionGrade3");
		isFinished = false;
		memoryIncrement = 0;
		memoryTotal = parse.getLong("MemoryTotal");
		message = parse.getString("Message");
		pluginName = parse.getString("PluginName");
		stackTrace = parse.getString("StackTrace");
		totalTime = 0;
	}


	public void initCtrlAction(CtrlAction ctrlAction) {
		this.ctrlAction = ctrlAction;
		caption = ctrlAction.getCaller().getText();
		if(StringUtilities.isNullOrEmpty(caption) && ctrlAction.getCaller() instanceof Component){
			caption = ((Component)ctrlAction.getCaller()).getName();
		}
		ctrlActionName = ctrlAction.getClass().getName();
		isException = false;
		pluginName = ctrlAction.getClass().getClassLoader().toString();
		executeDateTime = System.currentTimeMillis();
		isFinished = false;
		message = "";
		stackTrace = "";
		if (ctrlAction.getCaller() != null) {
			getFunctionGrade(ctrlAction.getCaller());
		}
		currentMemory = memoryTotal - Runtime.getRuntime().freeMemory();
	}

	public FunctionInfoCtrlAction(DesktopRuntimeEvent event) {
		if (event.getCurrentObject() instanceof CtrlAction) {
			initCtrlAction(((CtrlAction) event.getCurrentObject()));
		}
	}

	private void getFunctionGrade(IBaseItem caller) {
		if (caller instanceof SmMenuItem) {
			Container parent = ((SmMenuItem) caller).getParent();
			if (parent instanceof SmPopupMenu) {
				// 右键菜单
				functionGrade0 = UserExperienceProperties.getString("String_ContextMenu");
				functionGrade1 = ((SmPopupMenu) parent).getText();
			} else if (parent instanceof JPopupMenu) {
				Component invoker = ((JPopupMenu) parent).getInvoker();
				if (invoker instanceof ControlButton) {
					// 下拉菜单
					functionGrade0 = UserExperienceProperties.getString("String_ToolBar_ButtonDropdown");
					functionGrade1 = ((SmButtonDropdown) invoker.getParent()).getText();
					functionGrade3 = ((SmToolbar) invoker.getParent().getParent()).getText();
				} else if (invoker instanceof SmMenu) {
					functionGrade0 = UserExperienceProperties.getString("String_FrameMenu");
					functionGrade1 = ((SmMenu) invoker).getText();
					Container superParent = invoker.getParent();
					if (superParent instanceof JPopupMenu && ((JPopupMenu) superParent).getInvoker() instanceof SmMenu) {
						functionGrade0 = UserExperienceProperties.getString("String_FrameChildMenu");
						SmMenu menu = (SmMenu) ((JPopupMenu) superParent).getInvoker();
						functionGrade2 = menu.getText();
					}
				}
			}
		} else if (caller instanceof SmCtrlActionButton) {
			// 工具条
			functionGrade0 = UserExperienceProperties.getString("String_ToolBar");
			functionGrade1 = ((SmCtrlActionButton) caller).getParent().getName();
		} else if (caller instanceof SmButtonDropdown) {
			//  工具条
			functionGrade0 = UserExperienceProperties.getString("String_ToolBar");
			functionGrade1 = ((SmButtonDropdown) caller).getParent().getName();
		}
	}

	public void finished() {
		if (!isFinished) {
			isFinished = true;
			totalTime = System.currentTimeMillis() - executeDateTime;
			memoryIncrement = memoryTotal - Runtime.getRuntime().freeMemory() - currentMemory;

		}
	}

	public FunctionInfoCtrlAction(Exception exception) {
		isException = true;
		ctrlActionName = "";
		caption = "";
		executeDateTime = 0;
		isFinished = true;
		message = exception.getMessage();
		for (int i = 0; i < 3; i++) {
			stackTrace += exception.getStackTrace()[i].toString();
		}

	}

	@Override
	public String getJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Caption", caption);
		jsonObject.put("CtrlActionName", ctrlActionName);
		jsonObject.put("ExecuteDateTime", executeDateTime);
		jsonObject.put("FunctionGrade0", functionGrade0);
		jsonObject.put("FunctionGrade1", functionGrade1);
		jsonObject.put("FunctionGrade2", functionGrade2);
		jsonObject.put("FunctionGrade3", functionGrade3);
//		jsonObject.put("GDIIncrement", );
//		jsonObject.put("GDITotal", );
		jsonObject.put("IsException", isException);
		jsonObject.put("IsFinished", isFinished);
		jsonObject.put("MemoryIncrement", memoryIncrement);
		jsonObject.put("MemoryTotal", memoryTotal);
		jsonObject.put("Message", message);
		jsonObject.put("PluginName", pluginName);
//		jsonObject.put("ProcessCount", );
		jsonObject.put("StackTrace", stackTrace);
		jsonObject.put("TotalTime", totalTime);
//		jsonObject.put("UserObjectIncerment", );
//		jsonObject.put("UserObjectTotal", );
		return jsonObject.toString();
	}

	public CtrlAction getCtrlAction() {
		return ctrlAction;
	}

	@Override
	public String toJSONString() {
		return getJson();
	}
}
