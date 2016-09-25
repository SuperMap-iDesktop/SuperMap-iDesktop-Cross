package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.XmlUtilities;

import javax.swing.*;
import java.io.File;
import java.text.MessageFormat;

/**
 * @author XiaJT
 */
public class CtrlActionTransformInput extends CtrlAction {
	public CtrlActionTransformInput(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormTransformation) {
			String moduleName = "TransformInput";
			if (!SmFileChoose.isModuleExist(moduleName)) {
				String fileFilters = SmFileChoose.createFileFilter(DataEditorProperties.getString("String_TransformationFileFilter"), "drfu");
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"), DataEditorProperties.getString("String_ImportTransformationFile")
						, moduleName, "OpenOne");
			}
			SmFileChoose fileChoose = new SmFileChoose(moduleName);
			if (fileChoose.showDefaultDialog() == JFileChooser.APPROVE_OPTION) {
				File file = fileChoose.getSelectedFile();
				try {
					if (!((IFormTransformation) activeForm).fromXml(XmlUtilities.getDocument(file.getPath()))) {
						Application.getActiveApplication().getOutput().output(DataEditorProperties.getString("String_ImportTransformationFileFailed"));
					}
				} catch (Exception e) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(DataEditorProperties.getString("String_ImportTransformationFileFailed"), file.getAbsolutePath()));
				}
			}
		}
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getActiveForm() instanceof IFormTransformation;
	}
}
