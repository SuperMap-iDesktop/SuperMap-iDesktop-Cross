package com.supermap.desktop.dialog.cacheClip.cache;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;


public class MultiProcessManager {

    transient Vector m_ProcessBuildingListeners;

    public synchronized void addProcessBuildingListener(ProcessBuildingListener l) {
        if (m_ProcessBuildingListeners == null) {
            m_ProcessBuildingListeners = new Vector();
        }
        if (!m_ProcessBuildingListeners.contains(l)) {
            m_ProcessBuildingListeners.insertElementAt(l, 0);
        }
    }

    public synchronized void removeProcessBuildingListener(ProcessBuildingListener l) {
        if (m_ProcessBuildingListeners != null && m_ProcessBuildingListeners.contains(l)) {
            m_ProcessBuildingListeners.remove(l);
        }
    }

    protected void fireProcessBuilding(ProcessBuildEvent event) {
        if (m_ProcessBuildingListeners != null) {
            Vector listeners = m_ProcessBuildingListeners;
            int count = listeners.size();
            for (int i = count - 1; i >= 0; i--) {
                ((ProcessBuildingListener) listeners.elementAt(i)).ProcessBuilding(event);
            }
        }
    }

    private int m_processCount;
    private Object m_classObject;
    private int m_timeout;

    public MultiProcessManager(Object object, int count) {
        m_processCount = count;
        m_classObject = object;
        m_timeout = 5 * 60 * 1000;
    }

    public int getProcessCount() {
        return m_processCount;
    }

    public void setProcessCount(int count) {
        m_processCount = count;
    }

    public int getTimeout() {
        return m_timeout;
    }

    public void setTimeout(int timeout) {
        m_timeout = timeout;
    }

    public void Run() {
        if (m_processCount == 0) {
            System.out.println("ProcessCount is zero!");
            return;
        }
        System.out.println(m_processCount);
        SubprocessThread.TimeOutMS = m_timeout;

        RuntimeMXBean runtimeMBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMBean.getInputArguments();
        String className = m_classObject.getClass().getName();
        List<SubprocessThread> threaList = Collections.synchronizedList(new ArrayList<SubprocessThread>());

        while (true) {

            if (threaList.size() < m_processCount) {

                ProcessBuildEvent event = new ProcessBuildEvent(this);
                fireProcessBuilding(event);

                //System.out.println(event.getArgs());

                if (event.getActive()) {
                    ArrayList<String> arguments = new ArrayList<String>();
                    arguments.add("java");
                    //arguments.addAll(jvmArgs);
                    arguments.add("-cp");
                    String projectPath = System.getProperty("user.dir");
                    projectPath = projectPath.replace("\\", "/");
                    String jarPath = ".;" + projectPath + "/bin/com.supermap.data.jar;" + projectPath + "/bin/com.supermap.mapping.jar;" + projectPath + "/bin/com.supermap.tilestorage.jar;"+ projectPath + "/bin/com.supermap.data.processing.jar;" + projectPath + "/bundles/idesktop_bundles/MapView.jar";
                    arguments.add(jarPath);
                    arguments.add(className);
                    if (event.getArgs().size() > 0) {
                        for (int i = 0; i < event.getArgs().size(); i++) {
                            if (!event.getArgs().get(i).isEmpty()) {
                                arguments.add(event.getArgs().get(i));
                            }
                        }
                    }

                    SubprocessThread thread = new SubprocessThread(arguments);
                    threaList.add(thread);
                    thread.start();
                } else {
                    if (threaList.size() == 0) {
                        break;
                    }
                }
            }

            for (int index = threaList.size() - 1; index > -1; index--) {
                SubprocessThread t = threaList.get(index);
                t.timeout();
                if (t.isExit) {
                    threaList.remove(index);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
