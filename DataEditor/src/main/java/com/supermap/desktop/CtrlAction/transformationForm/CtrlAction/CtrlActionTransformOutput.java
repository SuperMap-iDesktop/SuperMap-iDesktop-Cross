package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.FormTransformationTableModel;
import com.supermap.desktop.CtrlAction.transformationForm.beans.FormTransformationSubFormType;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.FileUtilities;

import javax.swing.*;
import java.io.File;
import java.text.MessageFormat;

/**
 * @author XiaJT
 */
public class CtrlActionTransformOutput extends CtrlAction {
	public CtrlActionTransformOutput(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormTransformation) {
			String moduleName = "TransformOutput";
			if (!SmFileChoose.isModuleExist(moduleName)) {
				String fileFilters = SmFileChoose.createFileFilter(DataEditorProperties.getString("String_TransformationFileFilter"), "drfu");
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"), DataEditorProperties.getString("String_ExportTransformationFile")
						, moduleName, "SaveOne");
			}
			SmFileChoose fileChoose = new SmFileChoose(moduleName);
			fileChoose.setSelectedFile(new File(DataEditorProperties.getString("String_TransformationFileFilter")));
			if (fileChoose.showDefaultDialog() == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChoose.getFilePath());
				String value = ((IFormTransformation) activeForm).toXml();
				if (value == null) {
					Application.getActiveApplication().getOutput().output(DataEditorProperties.getString("String_ExportTransformationFileFailed"));
				}
				if (file.isFile() && file.exists()) {
					file.delete();
				}
				if (FileUtilities.writeToFile(file, value)) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(DataEditorProperties.getString("String_ExportTransformationFileSuccess"), fileChoose.getFilePath()));
				} else {
					Application.getActiveApplication().getOutput().output(DataEditorProperties.getString("String_ExportTransformationFileFailed"));
				}
			}
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (!(activeForm instanceof IFormTransformation)) {
			return false;
		}
		FormTransformationTableModel model = (FormTransformationTableModel) ((IFormTransformation) activeForm).getTable().getModel();
		if (model.getEnableRowCount() <= 0 || model.getEnablePointCount(FormTransformationSubFormType.Target) != model.getEnablePointCount(FormTransformationSubFormType.Reference)) {
			return false;
		}
		return true;
	}
}
