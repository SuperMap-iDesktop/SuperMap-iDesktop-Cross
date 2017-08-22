package com.supermap.desktop.lbs;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WebHDFS {

    //	public static String webURL = "http://192.168.14.1:50070/webhdfs/v1/data/NY_trip_data/";
//	public static String webFile = "trip_data_1.csv";
//	public static String outputURL = "http://192.168.14.1:50070/webhdfs/v1/output/";
//	public static String defaultURL = "http://192.168.14.1:50070/webhdfs/v1/";
    public static String webURL = "http://192.168.20.189:50070/webhdfs/v1/data/";
    public static String url = "http://localhost:50070/webhdfs/v1/data/";
    public static String defaultURL = "http://192.168.20.189:50070/webhdfs/v1/";
    public static String outputURL = "http://192.168.20.189:50070/webhdfs/v1/data/";
    public static String webFile = "newyork_taxi_2013-01_147k.csv";
    public static String resultURL ="";

    public HDFSDefine getHDFSDefine(String permission, String owner, String group, String size, String replication, String blockSize, String name, Boolean isDir) {

        HDFSDefine define = new HDFSDefine(permission, owner, group, size, replication, blockSize, name, isDir);
        return define;
    }

    public static String getHDFSFileURL() {
        String serverPath = webURL;
        // serverPath = serverPath.replace("webhdfs/v1/", "");
        // serverPath = serverPath.replace("http", "hdfs");
//        serverPath += webFile;
        return serverPath;
    }
    public static String getResultHDFSFilePath() {
        String serverPath = resultURL;
        serverPath = serverPath.replace("http", "hdfs");
        serverPath = serverPath.replace("50070/webhdfs/v1", "9000");
        return serverPath;
    }
    public static String getHDFSFilePath() {
        String serverPath = url;
        serverPath = serverPath.replace("http", "hdfs");
        serverPath = serverPath.replace("50070/webhdfs/v1", "9000");
        serverPath += webFile;
        return serverPath;
    }

    public static String getHDFSOutputDirectry() {
        String directry = outputURL;
        directry = directry.replace("50070/webhdfs/v1", "9000");
        directry = directry.replace("http", "hdfs");
        if (!directry.endsWith("/")) {
            directry += "/";
        }
        return directry;
    }

    public static String urlToHDFS(String url) {
        String hdfs = url.replace("50070/webhdfs/v1", "9000");
        hdfs = hdfs.replace("http", "hdfs");
        return hdfs;
    }

    public static String hdfsToURL(String hdfs) {
        String url = hdfs.replace("9000", "50070/webhdfs/v1");
        url = url.replace("hdfs://", "http://");
        return url;
    }

    public static String getFileList(String urlPath) {
        return HttpRequest.getHttpString(urlPath, "op=LISTSTATUS");
    }

    private static Boolean getFileResult = false;

    public static Boolean getFile(String urlPath, String localPath) {
        getFileResult = false;
        // 需要实现断点续传
        // 获取文件总大小，计算给出进度
        // 提供界面，给出默认名字，允许用户改名字
        getFileResult = HttpRequest.saveFileToDisk(urlPath, "op=OPEN&offset=0&length=1024", localPath);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });
        return getFileResult;
    }

    private static String getHttpString(InputStream stream) {
        String result = "";
        BufferedReader in = null;
        try {
            InputStream inputStream = stream;

            // 定义 BufferedReader输入流来读取URL的响应
            if (inputStream != null) {
                in = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_ConnectException"));
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static HDFSDefine getFileStatus(String url, String name) {
        HDFSDefine define = null;
        CreateFile file = new CreateFile();
        String result = getHttpString(file.getFileStatus(url, name));
        define = parseStringToDefine(define, result);

        return define;
    }

    private static HDFSDefine parseStringToDefine(HDFSDefine define, String result) {
        String[] temps = result.split("\"|,|:|\\[|\\]|\\{|\\}|\\\r|\\\n");
        ArrayList<String> results = new ArrayList<String>();
        for (String temp : temps) {
            if (!temp.trim().equals("")) {
                results.add(temp.trim());
            }
        }

        int itemsCount = 26;
        // 开始：最前面一个节点 {"FileStatus":[
        // 结尾：]}}
        results.remove(0);
        // "accessTime":0,"blockSize":0,"childrenNum":24,"fileId":16386,"group":"supergroup","length":0,"modificationTime":1461949836347,
        // "owner":"root","pathSuffix":"data","permission":"755","replication":0,"storagePolicy":0,"type":"DIRECTORY"
        String permission = "", owner = "", group = "", length = "", replication = "", blockSize = "", pathSuffix = "", type = "";
        for (int i = 0; i < results.size(); i += itemsCount) {
            for (int j = 0; j < itemsCount; j += 2) {
                if (results.get(i + j).equalsIgnoreCase("permission")) {
                    permission = results.get(i + j + 1);
                } else if (results.get(i + j).equalsIgnoreCase("owner")) {
                    owner = results.get(i + j + 1);
                } else if (results.get(i + j).equalsIgnoreCase("group")) {
                    group = results.get(i + j + 1);
                } else if (results.get(i + j).equalsIgnoreCase("length")) {
                    length = results.get(i + j + 1);
                } else if (results.get(i + j).equalsIgnoreCase("replication")) {
                    replication = results.get(i + j + 1);
                } else if (results.get(i + j).equalsIgnoreCase("blockSize")) {
                    blockSize = results.get(i + j + 1);
                } else if (results.get(i + j).equalsIgnoreCase("pathSuffix")) {
                    pathSuffix = results.get(i + j + 1);
                } else if (results.get(i + j).equalsIgnoreCase("type")) {
                    type = results.get(i + j + 1);
                }
            }

            Boolean isDir = false;
            if (type.equalsIgnoreCase("DIRECTORY")) {
                isDir = true;
            }

            define = (new WebHDFS()).getHDFSDefine(permission, owner, group, length, replication, blockSize, pathSuffix, isDir);
        }
        return define;
    }

    public static HDFSDefine getFileStatus(String urlPath) {
        HDFSDefine define = null;

        String result = HttpRequest.getHttpString(urlPath, "op=GETFILESTATUS");
        define = parseStringToDefine(define, result);

        return define;
    }

    public static HDFSDefine[] listDirectory(String urlPath, String childFolder, Boolean isFolderOnly) {
        ArrayList<HDFSDefine> defines = new ArrayList<HDFSDefine>();
        try {
            int itemsCount = 26;
            if (!urlPath.endsWith("/")) {
                urlPath += "/";
            }

            if (!"".equals(childFolder)) {
                if (!childFolder.endsWith("/")) {
                    childFolder += "/";
                }

                if (childFolder.startsWith("/")) {
                    childFolder.substring(1, childFolder.length() - 1);
                }

                urlPath += childFolder;
            }

            String result = WebHDFS.getFileList(urlPath);
            String[] temps = result.split("\"|,|:|\\[|\\]|\\{|\\}|\\\r|\\\n");
            ArrayList<String> results = new ArrayList<String>();
            for (String temp : temps) {
                if (!temp.trim().equals("")) {
                    results.add(temp.trim());
                }
            }

            // 开始：最前面两个节点 {"FileStatuses":{"FileStatus":[
            // 结尾：]}}
            results.remove(1);
            results.remove(0);
            // "accessTime":0,"blockSize":0,"childrenNum":24,"fileId":16386,"group":"supergroup","length":0,"modificationTime":1461949836347,
            // "owner":"root","pathSuffix":"data","permission":"755","replication":0,"storagePolicy":0,"type":"DIRECTORY"
            String permission = "", owner = "", group = "", length = "", replication = "", blockSize = "", pathSuffix = "", type = "";
            for (int i = 0; i < results.size(); i += itemsCount) {
                for (int j = 0; j < itemsCount; j += 2) {
                    if (results.get(i + j).equalsIgnoreCase("permission")) {
                        permission = results.get(i + j + 1);
                    } else if (results.get(i + j).equalsIgnoreCase("owner")) {
                        owner = results.get(i + j + 1);
                    } else if (results.get(i + j).equalsIgnoreCase("group")) {
                        group = results.get(i + j + 1);
                    } else if (results.get(i + j).equalsIgnoreCase("length")) {
                        length = results.get(i + j + 1);
                    } else if (results.get(i + j).equalsIgnoreCase("replication")) {
                        replication = results.get(i + j + 1);
                    } else if (results.get(i + j).equalsIgnoreCase("blockSize")) {
                        blockSize = results.get(i + j + 1);
                    } else if (results.get(i + j).equalsIgnoreCase("pathSuffix")) {
                        pathSuffix = results.get(i + j + 1);
                    } else if (results.get(i + j).equalsIgnoreCase("type")) {
                        type = results.get(i + j + 1);
                    }
                }

                Boolean isDir = false;
                if (type.equalsIgnoreCase("DIRECTORY")) {
                    isDir = true;
                }

                if (!isFolderOnly || (isFolderOnly && isDir)) {
                    HDFSDefine hdfsDefine = (new WebHDFS()).getHDFSDefine(permission, owner, group, length, replication, blockSize, pathSuffix, isDir);
                    defines.add(hdfsDefine);
                }
            }
        } catch (Exception ex) {

        }

        return defines.toArray(new HDFSDefine[defines.size()]);
    }
}
