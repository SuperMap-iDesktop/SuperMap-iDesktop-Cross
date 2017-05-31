package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IWorkFlow;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormWorkflow;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.io.File;

/**
 * @author XiaJT
 */
public class CtrlActionProcessExportForm extends CtrlAction {
	public CtrlActionProcessExportForm(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IWorkFlow workflow;
		workflow = ((FormWorkflow) Application.getActiveApplication().getActiveForm()).getWorkFlow();
		if (workflow == null) {
			return;
		}
		String moduleName = "CtrlActionProcessExport";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			SmFileChoose.addNewNode(SmFileChoose.createFileFilter(ProcessProperties.getString("String_ProcessFile"), "xml"),
					CommonProperties.getString("String_DefaultFilePath"), ProcessProperties.getString("String_ImportWorkFLowFile"),
					moduleName, "SaveOne");
		}
		SmFileChoose fileChoose = new SmFileChoose(moduleName);
		fileChoose.setSelectedFile(new File("ProcessTemplate.xml"));
		if (fileChoose.showDefaultDialog() == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChoose.getFilePath();
			if (!StringUtilities.isNullOrEmpty(filePath)) {
				FileUtilities.writeToFile(filePath, workflow.getMatrixXml());
			}
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
