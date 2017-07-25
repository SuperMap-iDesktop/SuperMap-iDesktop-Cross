package com.supermap.desktop.CtrlAction;

import com.supermap.Interface.ILBSTask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.callable.UploadPropressCallable;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.lbs.FileInfo;
import com.supermap.desktop.ui.lbs.HDFSDefine;
import com.supermap.desktop.ui.lbs.WebHDFS;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.CursorUtilities;

import javax.swing.*;
import java.io.File;
import java.text.MessageFormat;

public class CtrlActionUpload extends CtrlAction {

    public CtrlActionUpload(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        try {
            IFormLBSControl control = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
            String modelName = "HDFSFileUpload";
            if (!SmFileChoose.isModuleExist(modelName)) {
                SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), CommonProperties.getString("String_SelectFile"),
                        modelName, "OpenOne");
            }
            SmFileChoose smFileChoose = new SmFileChoose(modelName);
            smFileChoose.setAcceptAllFileFilterUsed(true);
            int state = smFileChoose.showDefaultDialog();
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = new File(smFileChoose.getFilePath());

                FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();

                if (file.exists() && fileManagerContainer != null) {
                    String webPath = control.getURL();
                    if (!webPath.endsWith("/")) {
                        webPath += "/";
                    }
                    String webURL = webPath + file.getName();
                    HDFSDefine[] nowDefines = WebHDFS.listDirectory(webURL, "", false);
                    if (nowDefines.length > 0) {
                        //文件已经存在，提示是否覆盖
                        if (UICommonToolkit.showConfirmDialog(MessageFormat.format(CoreProperties.getString("String_FieldExist"), file.getName())) == JOptionPane.OK_OPTION) {
                            upload(file, fileManagerContainer, webPath);
                        }
                    } else {
                        upload(file, fileManagerContainer, webPath);
                    }
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
            CursorUtilities.setDefaultCursor();
        }
    }

    private void upload(File file, FileManagerContainer fileManagerContainer, String webPath) {
        FileInfo uploadInfo = new FileInfo(webPath, file.getName(), "", file.getParentFile().getPath(), file.length(), 1, false);
        ITaskFactory taskFactory = TaskFactory.getInstance();
        ILBSTask task = taskFactory.getTask(TaskEnum.UPLOADTASK, uploadInfo);
        UploadPropressCallable uploadProgressCallable = new UploadPropressCallable(uploadInfo, true);
        task.doWork(uploadProgressCallable);
        fileManagerContainer.addItem(task);
    }

    @Override
    public boolean enable() {
        boolean enable = false;
        if (null != Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm()
                && Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm() instanceof IFormLBSControl) {
            enable = true;
        }
        return enable;
    }
}
