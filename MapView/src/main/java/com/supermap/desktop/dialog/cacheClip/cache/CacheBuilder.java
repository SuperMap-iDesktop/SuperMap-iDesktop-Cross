package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.utilities.Convert;
import com.supermap.mapping.Map;

import java.io.*;
import java.util.ArrayList;

public class CacheBuilder {

    /**
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < 4) {
            System.out.println("please input argements:");
            System.out.println("workspacefile mapname scilist cacheroot prpcesscount mergetaskcount");
            return;
        }

        String workspaceFile = args[0];
        String mapName = args[1];
        String sciList = args[2];
        String cacheRoot = args[3];
        int processCount = 0;
        if (args.length > 4) {
            processCount = Integer.valueOf(args[4]);
        }
        int mergeTaskCount = 4;
        if (args.length > 5) {
            mergeTaskCount = Integer.valueOf(args[5]);
        }

        ArrayList<String> allsciFiles = new ArrayList<String>();
        if (sciList.endsWith(".list")) {
            File tasksFile = new File(sciList);
            String taskDir = tasksFile.getParent();
            BufferedReader taskReader;
            try {
                taskReader = new BufferedReader(new InputStreamReader(new FileInputStream(tasksFile), "UTF-8"));

                String line;
                while ((line = taskReader.readLine()) != null) {
                    allsciFiles.add(taskDir + "/" + line);
                }
                taskReader.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (sciList.contains("&")) {
            String[] sciFileArray = sciList.split("&");
            for (int i = 0; i < sciFileArray.length; i++) {
                allsciFiles.add(sciFileArray[i]);
            }
        } else if (sciList.endsWith(".sci")) {
            allsciFiles.add(sciList);
        } else {
            File sciFile = new File(sciList);
            if (sciFile.isDirectory()) {
                String[] sciFiles = sciFile.list(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        return name.endsWith(".sci");
                    }
                });
                // sort by name
                ArrayList<Integer> scales = new ArrayList();

                for (String name : sciFiles) {
                    String scale = name.substring(1, name.indexOf("_"));
                    Integer scaleInt = Convert.toInteger(scale);
                    if (!scales.contains(scaleInt))
                        scales.add(scaleInt);
                }
                Integer[] scaleArray = scales.toArray(new Integer[scales.size()]);
                sort(scaleArray);
                for (Integer scale : scaleArray) {
                    for (String name : sciFiles) {
                        if (name.contains(String.valueOf(scale)) && !allsciFiles.contains(sciList + "/" + name)) {
                            allsciFiles.add(sciList + "/" + name);
                        }
                    }
                }
            }
        }

        if (allsciFiles.size() == 0) {
            System.out.println("sci files is empty!");
            return;
        }

//        if (processCount > 0) {
        LogWriter.setWriteToFile(true);
//        }

        CacheBuilder cacheBuilder = new CacheBuilder();
        cacheBuilder.build(workspaceFile, mapName, allsciFiles, cacheRoot, processCount, mergeTaskCount);
    }

    private static void sort(Integer[] scaleArray) {
        for (int i = 0; i < scaleArray.length; i++) {
            for (int j = i + 1; j < scaleArray.length; j++) {
                if (scaleArray[i] > scaleArray[j]) {
                    Integer temp = scaleArray[i];
                    scaleArray[i] = scaleArray[j];
                    scaleArray[j] = temp;
                }
            }
        }
    }

    private int sciIndex = 0;

    private void build(String workspaceFile, String mapName, ArrayList<String> sciList, String cacheRoot, int processCount, int mergeTaskCount) {
        if (processCount > 0) {
            File sciFile = new File(sciList.get(0));
            if (!sciFile.isFile()) {
                System.out.println("args \"" + sciFile + "\" not file!");
                return;
            }
            File scidone = new File(sciFile.getParentFile().getParent() + "/build");
            if (!scidone.exists()) {
                scidone.mkdir();
            }

            sciIndex = 0;
            final String argsWorkspace = workspaceFile;
            final String argsMapName = mapName;
            final ArrayList<String> argsSciList = sciList;
            final String argsCacheRoot = cacheRoot;
            final int argsMergeTaskCount = mergeTaskCount;

            MultiProcessManager processManager = new MultiProcessManager(this, processCount);
            processManager.addProcessBuildingListener(new ProcessBuildingListener() {
                @Override
                public void ProcessBuilding(ProcessBuildEvent event) {

                    if (sciIndex < argsSciList.size()) {
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append(argsSciList.get(sciIndex));
                        sciIndex++;
                        if (argsMergeTaskCount > 1) {
                            for (int i = 1; i < argsMergeTaskCount; i++) {
                                if (sciIndex >= argsSciList.size()) {
                                    break;
                                }
                                strBuilder.append("&");
                                strBuilder.append(argsSciList.get(sciIndex));
                                sciIndex++;
                            }
                        }
                        String sciname = strBuilder.toString();

                        ArrayList<String> argsList = new ArrayList<String>();
                        argsList.add(argsWorkspace);
                        argsList.add(argsMapName);
                        argsList.add(sciname);
                        argsList.add(argsCacheRoot);
                        argsList.add("0");
                        argsList.add("0");
                        event.setActive(true);
                        event.setArgs(argsList);
                    }
                }
            });

            processManager.Run();
        } else {
            this.buildCache(workspaceFile, mapName, sciList, cacheRoot);
        }
    }

    public void buildCache(String workspaceFile, String mapName, ArrayList<String> sciFiles, String targetRoot) {

        final long start = System.currentTimeMillis();
        LogWriter log = LogWriter.getInstance();
        String pid = LogWriter.getPID();

        Workspace wk = new Workspace();
        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo(workspaceFile);
        wk.open(info);
        Map map = new Map(wk);
        map.open(mapName);
        log.writelog(String.format("start sciCount:%d , PID:%s", sciFiles.size(), pid));
        log.writelog(String.format("init PID:%s, cost(ms):%d", LogWriter.getPID(), System.currentTimeMillis() - start));

        for (String sciFile : sciFiles) {
            long oneStart = System.currentTimeMillis();
            File sci = new File(sciFile);
            if (!sci.exists()) {
                LogWriter.getInstance().writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciFile));
                continue;
            }
            MapCacheBuilder builder = new MapCacheBuilder();
            builder.setMap(map);
            builder.fromConfigFile(sciFile);
            builder.setOutputFolder(targetRoot);
            builder.resumable(false);
            String scaleInfo = "1:" + String.valueOf((int) (1 / builder.getOutputScales()[0]));
            final String finalScaleInfo = scaleInfo;

            boolean result = builder.buildWithoutConfigFile();
            builder.dispose();

            if (result) {
                File doneDir = new File(sci.getParentFile().getParent() + "/build");
                if (!doneDir.exists()) {
                    doneDir.mkdir();
                }
                sci.renameTo(new File(doneDir, sci.getName()));
            }

            long end = System.currentTimeMillis();
            log.writelog(String.format("%s %s done,PID:%s, cost(ms):%d, done", sciFile, String.valueOf(result), LogWriter.getPID(), end - oneStart));
            log.flush();
        }

        map.close();
        map.dispose();
        wk.close();
        wk.dispose();

    }

}
