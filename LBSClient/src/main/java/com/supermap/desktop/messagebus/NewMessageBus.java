package com.supermap.desktop.messagebus;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IResponse;
import com.supermap.desktop.Interface.IServerService;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.impl.IServerServiceImpl;
import com.supermap.desktop.params.KernelDensityJobResponse;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.utilities.CommonUtilities;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by xie on 2017/1/10.
 * 新的线程管理，通信类
 */
public class NewMessageBus {
    private static volatile ITask task;
    private static volatile ArrayList<IResponse> tasks = null;

    public static void producer(IResponse response) {
        try {
            if (null == tasks) {
                tasks = new ArrayList<>();
            }
            tasks.add(response);
            FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();
            ITaskFactory taskFactory = TaskFactory.getInstance();
            if (response instanceof KernelDensityJobResponse) {
                task = taskFactory.getTask(TaskEnum.KERNELDENSITYTASK, null);
                addTask(fileManagerContainer, task);
            }
            ExecutorService eService = Executors.newCachedThreadPool(new ThreadFactory() {

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("newMessagebus" + UUID.randomUUID());
                    return thread;
                }
            });
            eService.submit(new MessageBusConsumer());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addTask(FileManagerContainer fileManagerContainer, ITask task) throws InterruptedException {
        if (fileManagerContainer != null) {
            fileManagerContainer.addItem(task);
        }
    }

    public static class MessageBusConsumer implements Runnable, ExceptionListener {
        @Override
        public void run() {
            try {
                while (tasks.size() != 0) {
                    for (IResponse response : tasks) {
                        if (response instanceof KernelDensityJobResponse) {
                            IServerService serverService = new IServerServiceImpl();
                            String result = serverService.query(((KernelDensityJobResponse) response).newResourceLocation);
                            if (result.contains("FINISHED")) {
                                tasks.remove(response);
                                task.updateProgress(100, "", "");
                            } else {
                                updateProgress();
                                Thread.sleep(100);
                            }
                        }
                    }
                }

            } catch (InterruptedException exception) {
                Application.getActiveApplication().getOutput().output(exception);
            }
        }

        @Override
        public void onException(JMSException exception) {
            Application.getActiveApplication().getOutput().output(exception);
        }
    }

    private static void updateProgress() throws InterruptedException {
        Thread.sleep(1000);
        task.updateProgress(getRandomProgress(), "", "");
    }

    private static int getRandomProgress() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(100);
    }
}
