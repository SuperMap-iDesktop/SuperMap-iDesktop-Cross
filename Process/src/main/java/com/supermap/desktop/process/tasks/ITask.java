package com.supermap.desktop.process.tasks;


import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.tasks.callable.UpdateProgressCallable;

import java.util.concurrent.CancellationException;

/**
 * Created by xie on 2016/11/28.
 */
public interface ITask {

    /**
     * 获取流程
     *
     * @return
     */
    IProcess getProcess();

    /**
     * 获取操作是否已取消
     *
     * @return
     */
    boolean isCancel();

    /**
     * 设置是否取消操作
     *
     * @param isCancel
     */
    void setCancel(boolean isCancel);

    /**
     * 更新进度信息
     *
     * @param percent    进度
     * @param remainTime 剩余时间
     * @param message    进度信息
     */
    void updateProgress(int percent, String remainTime, String message) throws CancellationException;

    void doWork(final UpdateProgressCallable doWork);

}
