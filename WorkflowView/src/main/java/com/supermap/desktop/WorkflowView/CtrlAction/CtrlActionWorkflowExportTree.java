package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDataEntry;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

/**
 * @author XiaJT
 */
public class CtrlActionWorkflowExportTree extends CtrlAction {
	public CtrlActionWorkflowExportTree(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();
		IDataEntry<String> workflowEntry = (IDataEntry<String>) ((TreeNodeData) treeNode.getUserObject()).getData();
		if (workflowEntry == null) {
			return;
		}
		String moduleName = "CtrlActionWorkflowExport";
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
				FileUtilities.writeToFile(filePath, workflowEntry.getValue());
			}
		}
	}

	@Override
	public boolean enable() {
		return UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionCount() == 1;
	}
}
