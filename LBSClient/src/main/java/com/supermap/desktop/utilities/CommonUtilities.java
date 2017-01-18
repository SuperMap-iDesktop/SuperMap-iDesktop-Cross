package com.supermap.desktop.utilities;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.dialog.JDialogTaskManager;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.TaskManagerContainer;
import com.supermap.desktop.http.callable.DownloadProgressCallable;
import com.supermap.desktop.http.callable.UploadPropressCallable;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.ui.controls.DialogResult;

import javax.swing.*;
import java.util.List;

/**
 * LBSClient项目公用方法类
 *
 * @author xie
 */
public class CommonUtilities {
    private static final String FILE_MANAGER_CONTROL_CLASS = "com.supermap.desktop.http.FileManagerContainer";
    private static final String TASK_MANAGER_CONTROL_CLASS = "com.supermap.desktop.http.TaskManagerContainer";

    /**
     * 获取显示进度条ManagerContainer,并激活显示dockbar
     *
     * @return
     */
    public static FileManagerContainer getFileManagerContainer() {

        FileManagerContainer fileManagerContainer = null;
        IDockbar dockbarPropertyContainer;
        dockbarPropertyContainer = getDockBar(FILE_MANAGER_CONTROL_CLASS);
        if (dockbarPropertyContainer != null) {
            fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer.getInnerComponent();
            dockbarPropertyContainer.setVisible(true);
            dockbarPropertyContainer.active();
        }
        return fileManagerContainer;
    }

    /**
     * 获取任务管理界面，并激活dockbar
     *
     * @return
     */
    public static TaskManagerContainer getTaskManagerContainer() {
        TaskManagerContainer taskManagerContainer = null;
        IDockbar dockbarPropertyContainer;
        dockbarPropertyContainer = getDockBar(TASK_MANAGER_CONTROL_CLASS);
        if (dockbarPropertyContainer != null) {
            taskManagerContainer = (TaskManagerContainer) dockbarPropertyContainer.getInnerComponent();
            dockbarPropertyContainer.setVisible(true);
            dockbarPropertyContainer.active();
        }
        return taskManagerContainer;
    }

    /**
     * 获取要显示的docbar
     *
     * @return
     */
    public static IDockbar getDockBar(String name) {
        IDockbar dockbarPropertyContainer = null;
        try {
            dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(name));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return dockbarPropertyContainer;
    }

    /**
     * 删除任务项
     */
    public static void removeItem(ITask task) {
        IDockbar dockbarPropertyContainer = CommonUtilities.getDockBar(FILE_MANAGER_CONTROL_CLASS);
        if (null != dockbarPropertyContainer) {
            FileManagerContainer fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer.getInnerComponent();
            fileManagerContainer.removeItem(task);
        }
    }

    public static IFormLBSControl getActiveLBSControl() {
        IFormLBSControl result = null;
        if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
            result = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
        }
        return result;
    }

    /**
     * 获取图片资源
     *
     * @param path
     * @return
     */
    public static ImageIcon getImageIcon(String path) {
        ImageIcon imageIcon = null;
        try {
            if (!StringUtilities.isNullOrEmptyString(path)) {
                imageIcon = new ImageIcon(CommonUtilities.class.getResource("/lbsresources/Toolbar/" + path));
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return imageIcon;
    }

    /**
     * 添加下载任务
     *
     * @param downloadInfo
     */
    public static void addDownLoadTask(FileInfo downloadInfo) {
        FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();

        if (fileManagerContainer != null) {
            ITaskFactory taskFactory = TaskFactory.getInstance();
            ITask task = taskFactory.getTask(TaskEnum.DOWNLOADTASK, downloadInfo);
            DownloadProgressCallable uploadProgressCallable = new DownloadProgressCallable(downloadInfo, true);
            task.doWork(uploadProgressCallable);
            fileManagerContainer.addItem(task);
        }
    }

    /**
     * 判断任务恢复界面是否打开过
     */
    public static IFormLBSControl getFormHDFSManager() {
        IFormLBSControl result = null;
        int lbsFormCount = 0;
        int formCount = Application.getActiveApplication().getMainFrame().getFormManager().getCount();
        for (int i = 0; i < formCount; i++) {
            if (Application.getActiveApplication().getMainFrame().getFormManager().get(i) instanceof IFormLBSControl) {
                result = (IFormLBSControl) Application.getActiveApplication().getMainFrame().getFormManager().get(i);
                break;
            }
        }
        return result;
    }

    /**
     * 判断任务恢复界面是否打开过
     */
    public static boolean isTaskManagerOpened() {
        boolean result = false;
        int lbsFormCount = 0;
        int formCount = Application.getActiveApplication().getMainFrame().getFormManager().getCount();
        for (int i = 0; i < formCount; i++) {
            if (Application.getActiveApplication().getMainFrame().getFormManager().get(i) instanceof IFormLBSControl) {
                lbsFormCount++;
            }
        }
        if (lbsFormCount >= 1) {
            result = true;
        }
        return result;
    }

    /**
     * 恢复任务
     */
    public static void recoverTask() {
        FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();
        if (null != fileManagerContainer) {
            ITaskFactory taskFactory = TaskFactory.getInstance();
            List<String> downloadTaskPropertyLists = ManagerXMLParser.getTaskPropertyList(TaskEnum.DOWNLOADTASK);
            List<String> uploadTaskPropertyLists = ManagerXMLParser.getTaskPropertyList(TaskEnum.UPLOADTASK);
            JDialogTaskManager taskManager = JDialogTaskManager.getInstance();
            taskManager.setDownloadTaskCount(downloadTaskPropertyLists.size());
            taskManager.setUploadTaskCount(uploadTaskPropertyLists.size());
            if (taskManager.showDialog().equals(DialogResult.OK) && taskManager.isRecoverTask()) {
                if (taskManager.isRecoverDownLoadTask()) {
                    for (String downloadAttris : downloadTaskPropertyLists) {
                        String[] attriArrayForDownload = downloadAttris.split(",");
                        FileInfo downloadInfo = new FileInfo(attriArrayForDownload[0], attriArrayForDownload[1], attriArrayForDownload[2],
                                attriArrayForDownload[3], Long.parseLong(attriArrayForDownload[4]), 1, true);
                        ITask downloadTask = taskFactory.getTask(TaskEnum.DOWNLOADTASK, downloadInfo);
                        DownloadProgressCallable downloadProgressCallable = new DownloadProgressCallable(downloadInfo, false);
                        downloadTask.doWork(downloadProgressCallable);
                        fileManagerContainer.addItem(downloadTask);
                    }
                } else {
                    ManagerXMLParser.removeAllChildTasks(TaskEnum.DOWNLOADTASK);
                }
                if (taskManager.isrecoverUploadTask()) {
                    for (String uploadAttris : uploadTaskPropertyLists) {
                        String[] attriArrayForUpload = uploadAttris.split(",");
                        FileInfo uploadInfo = new FileInfo(attriArrayForUpload[0], attriArrayForUpload[1], "", attriArrayForUpload[2],
                                Long.parseLong(attriArrayForUpload[3]), 1, true);
                        ITask uploadTask = taskFactory.getTask(TaskEnum.UPLOADTASK, uploadInfo);
                        UploadPropressCallable downloadProgressCallable = new UploadPropressCallable(uploadInfo, false);
                        uploadTask.doWork(downloadProgressCallable);
                        fileManagerContainer.addItem(uploadTask);
                    }
                } else {
                    ManagerXMLParser.removeAllChildTasks(TaskEnum.UPLOADTASK);
                }
            } else {
                ManagerXMLParser.removeAllTasks();
            }
        }
    }

}
