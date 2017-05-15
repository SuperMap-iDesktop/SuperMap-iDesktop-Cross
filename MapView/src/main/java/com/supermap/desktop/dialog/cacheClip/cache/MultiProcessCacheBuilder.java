package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.mapping.Map;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiProcessCacheBuilder {

    /**
     * 启用多进程进行测试. TODO: 能够使用多进程切图
     *
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("please input args: workspaceFile mapName taskList cacheRoot processCount[options] mergeCount[options]");
            return;
        }

        String workspaceFile = args[0];
        String mapName = args[1];
        String taskListFile = args[2];
        String cacheRoot = args[3];

        // 默认为3个进程
        int processCount = 3;
        if (args.length > 4) {
            processCount = Integer.valueOf(args[4]);
        }

        int mergeCount = 1;
        if (args.length > 5) {
            mergeCount = Integer.valueOf(args[5]);
        }

        File dir = new File(new File(taskListFile).getParent() + "/done");
        if (!dir.exists()) {
            dir.mkdir();
        }

        if (taskListFile.toLowerCase().endsWith(".sci")) {
            // 子进程启动单一任务，为子进程时taskListFile 为sci文件，而不是任务清单文件
            Run(workspaceFile, mapName, taskListFile, cacheRoot);
        } else {
            // 读取文件，考虑到文件比较大，读到一行，就执行一个
            // 默认不写日志文件，多进程的时候再写文件
            LogWriter.setWriteToFile(true);
            // 写出参数信息，以便后面查看日志进行分析
            LogWriter log = LogWriter.getInstance();

            InetAddress ia = InetAddress.getLocalHost();
            String localname = ia.getHostName();
            String localip = ia.getHostAddress();


            log.writelog(String.format("Run Workspace:%s Map:%s Tasks:%s CacheRoot%s processCount:%d mergeCount:%d, at computer:%s, ip:%s", workspaceFile, mapName, taskListFile, cacheRoot, processCount, mergeCount, localname, localip));

            File tasks = new File(taskListFile);
            BufferedReader taskReader = new BufferedReader(new InputStreamReader(new FileInputStream(tasks), "UTF-8"));
            String sciFile = null;
            String folder = tasks.getParent();

            // 读到内存中，方便重复遍历
            ArrayList<String> allLines = new ArrayList<String>();
            String line;
            while ((line = taskReader.readLine()) != null) {
                allLines.add(line);
            }
            taskReader.close();

            // 进程数小于2时，使用当前进程切图
            if (processCount < 2) {
                RunAllCurrentProc(allLines, folder, workspaceFile, mapName, cacheRoot);
            } else {
                RunAllMultiProc(allLines, folder, workspaceFile, mapName, cacheRoot, processCount, mergeCount);
            }
        }
    }

    public static void RunBySubProc(List<String> allTasks, String sciFolder, String workspaceFile, String mapName, String cacheRoot, int processCount, int mergeCount) throws Exception {
        LogWriter log = LogWriter.getInstance();
        //java options
        RuntimeMXBean runtimeMBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMBean.getInputArguments();
        // 启动子进程，不能有输出，如果有输出会出现因为output满了，而挂起的现象
        List<SubprocessThread> threaList = Collections.synchronizedList(new ArrayList<SubprocessThread>());
        int merged = 0;
        StringBuffer buffer = new StringBuffer();
        int taskSize = allTasks.size();
        int lastIndex = taskSize - 1;
        for (int i = 0; i < taskSize; i++) {
            String sciFile = allTasks.get(i);
            sciFile = sciFolder + "/" + sciFile;
            File sci = new File(sciFile);

            if (!sci.exists()) {
                LogWriter.getInstance().writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciFile));
                continue;
            }
            buffer.append(";");
            buffer.append(sciFile);
            merged++;
            // 如果为最后一个一个任务了，不够mereCount也需要执行，否则continue进行添加任务
            if (merged < mergeCount && i != lastIndex) {
                continue;
            }

            // 合并完任务了，开始启动子进程
            ArrayList<String> arguments = new ArrayList<String>();
            arguments.add("java");
            arguments.addAll(jvmArgs);
            arguments.add("-cp");
            arguments.add(System.getProperty("java.class.path"));
            arguments.add(MultiProcessCacheBuilder.class.getName());
            arguments.add(workspaceFile);
            arguments.add(mapName);
            arguments.add(buffer.substring(1));
            arguments.add(cacheRoot);

            buffer = new StringBuffer();
            merged = 0;

            SubprocessThread thread = new SubprocessThread(arguments);
            threaList.add(thread);
            thread.start();

            // 等待一个任务结束，启动新的任务
            while (threaList.size() >= processCount) {
                //判断time out，以便kill掉不挂起的进程
                int tSize = threaList.size();
                for (int index = tSize - 1; index > -1; index--) {
                    SubprocessThread t = threaList.get(index);
                    t.timeout();
                    if (t.isExit) {
                        threaList.remove(index);
                    }
                }
                Thread.sleep(1000);
                log.flush();
            }
        }
        log.flush();


        // 等待最后结束
        while (threaList.size() > 0) {
            //判断time out，以便kill掉不挂起的进程
            int tSize = threaList.size();
            for (int index = tSize - 1; index > -1; index--) {
                SubprocessThread t = threaList.get(index);
                t.timeout();
                if (t.isExit) {
                    threaList.remove(index);
                }
            }
            Thread.sleep(1000);
            log.flush();
        }
        log.flush();
    }

    public static void RunAllMultiProc(List<String> allTasks, String sciFolder, String workspaceFile, String mapName, String cacheRoot, int processCount, int mergeCount) throws Exception {

        // 启动子进程，但是进程数不能同时大于进程个数
        LogWriter log = LogWriter.getInstance();
        long start = System.currentTimeMillis();

        // 所有任务开始启动
        RunBySubProc(allTasks, sciFolder, workspaceFile, mapName, cacheRoot, processCount, mergeCount);

        // 重试执行此前失败的任务，重试后仍失败，则最后通过日志的方式输出
        ArrayList<String> tryAgainTasks = new ArrayList<String>();
        for (String task : allTasks) {
            String sciFile = sciFolder + "/" + task;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                continue;
            }
            tryAgainTasks.add(task);
            LogWriter.getInstance().writelog(String.format("the failure sciFile: %s try again", sciFile));
        }

        // 在线程过多的时候，容易引起UDB崩溃，默认最少合并4个任务
        int newMergeCount = tryAgainTasks.size() / processCount;
        if (newMergeCount < 4) {
            newMergeCount = 4;
        }
        // 处理第一次失败的任务,这里不合并任务
        RunBySubProc(tryAgainTasks, sciFolder, workspaceFile, mapName, cacheRoot, processCount, newMergeCount);

        // 第二次执行失败，通过日志通报出去
        for (String task : tryAgainTasks) {
            String sciFile = sciFolder + "/" + task;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                continue;
            }
            LogWriter.getInstance().writelog(String.format("sciFile: %s fail.", sciFile));
        }

        long cost = System.currentTimeMillis() - start;
        String lastLog = String.format("sci File count:%d, cost(ms):%d", allTasks.size(), cost);
        log.writelog(lastLog);
        log.flush();
        log.closs();
        System.out.println(lastLog);
    }

    public static void RunAllCurrentProc(List<String> allTasks, String sciFolder, String workspaceFile, String mapName, String cacheRoot) {

        for (String sciFile : allTasks) {
            sciFile = sciFolder + "/" + sciFile;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                LogWriter.getInstance().writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciFile));
                continue;
            }
            Run(workspaceFile, mapName, sciFile, cacheRoot);
        }

        // 重试执行此前失败的任务，重试后仍失败，则最后通过日志的方式输出
        ArrayList<String> tryAgainTasks = new ArrayList<String>();
        for (String task : allTasks) {
            String sciFile = sciFolder + "/" + task;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                continue;
            }
            tryAgainTasks.add(task);
            LogWriter.getInstance().writelog(String.format("the failure sciFile: %s try again", sciFile));
            Run(workspaceFile, mapName, sciFile, cacheRoot);
        }

        // 如果还有失败的，则通过日志写出
        for (String task : tryAgainTasks) {
            String sciFile = sciFolder + "/" + task;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                continue;
            }
            LogWriter.getInstance().writelog(String.format("sciFile: %s fail.", sciFile));
        }
    }

    public static void Run(String workspaceFile, String mapName, String sciFiles, String targetRoot) {

        long start = System.currentTimeMillis();
        LogWriter log = LogWriter.getInstance();
        String pid = LogWriter.getPID();

        // sciFile 支持多个文件
        String[] sciFileArray = sciFiles.split(";");
        Workspace wk = new Workspace();
        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo(workspaceFile);
        wk.open(info);
        Map map = new Map(wk);
        map.open(mapName);
        log.writelog(String.format("start sciCount:%d , PID:%s", sciFileArray.length, pid));
        log.writelog(String.format("init PID:%s, cost(ms):%d", LogWriter.getPID(), System.currentTimeMillis() - start));

        for (String sciFile : sciFileArray) {
            long oneStart = System.currentTimeMillis();
            File sci = new File(sciFile);
            if (!sci.exists()) {
                LogWriter.getInstance().writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciFile));
                continue;
            }
            // 添加空白是的对齐，容易后续通过excel进行日志处理
            MapCacheBuilder builder = new MapCacheBuilder();
            // 把sci放后面，可以使用sci的HashCode
            builder.setMap(map);
            builder.fromConfigFile(sciFile);
            builder.setOutputFolder(targetRoot);
            // 不记录resumable信息
            builder.resumable(false);

            boolean result = builder.buildWithoutConfigFile();
            // 将sci文件移到done目录下
            String doneDir = (sci.getParent() + "/done");

            if (result) {
                sci.renameTo(new File(doneDir, sci.getName()));
            }
            builder.dispose();
            long end = System.currentTimeMillis();
            log.writelog(String.format("%s %s done,PID:%s, cost(ms):%d, done", sciFile, String.valueOf(result), LogWriter.getPID(), end - oneStart));
            log.flush();
        }

        // TODO: 是否需要考虑打印整个进程的平均耗时

        map.close();
        map.dispose();
        wk.dispose();

    }

}
