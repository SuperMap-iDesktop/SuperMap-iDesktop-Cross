package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDataEntry;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.WorkflowView.FormWorkflow;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/7/19.
 */
public class CtrlActionImportWorkflow extends CtrlAction {
	public CtrlActionImportWorkflow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		String moduleName = "CtrlActionImportWorkflow";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			SmFileChoose.addNewNode(SmFileChoose.createFileFilter(ProcessProperties.getString("String_ProcessFile"), "xml"),
					CommonProperties.getString("String_DefaultFilePath"), ProcessProperties.getString("String_ImportWorkFLowFile"),
					moduleName, "OpenOne");
		}
		SmFileChoose fileChoose = new SmFileChoose(moduleName);
		if (fileChoose.showDefaultDialog() == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChoose.getFilePath();
			if (!StringUtilities.isNullOrEmpty(filePath) && new File(filePath).exists()) {
				Document document = XmlUtilities.getDocument(filePath);

				ArrayList<String> names = new ArrayList<>();
				ArrayList<IDataEntry<String>> workflows = Application.getActiveApplication().getWorkflowEntries();
				for (IDataEntry<String> workflow : workflows) {
					names.add(workflow.getKey());
				}

				IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
				for (int i = 0; i < formManager.getCount(); i++) {
					if (formManager.get(i) instanceof FormWorkflow) {
						names.add(formManager.get(i).getText());
					}
				}

				FormWorkflow formWorkflow = FormWorkflow.serializeFrom(XmlUtilities.nodeToString(document));
				String name = StringUtilities.getUniqueName(formWorkflow.getText(), names);
				formWorkflow.setText(name);
				formWorkflow.setNeedSave(true);
				Application.getActiveApplication().getMainFrame().getFormManager().add(formWorkflow);
			}
		}
	}
}
