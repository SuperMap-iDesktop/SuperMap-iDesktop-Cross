package com.supermap.desktop.http;

/**
 * <b>function:</b> 下载测试
 * @author hoojo
 * @createDate 2011-9-23 下午05:49:46
 * @file TestDownloadMain.java
 * @package com.hoo.download
 * @project MultiThreadDownLoad
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TestDownloadMain {
 
    public static void main(String[] args) {

    	String webPath = "http://192.168.12.103:50070/webhdfs/v1/data/test10W.csv";
    	String localFileName = "test10W.csv";
    	String localFilePath = "F:/temp";
    	long fileSize = 12190249;
//    	fileSize = 1024;
    	int threadCount = 3;
    	
		DownloadUtils.download(webPath, localFileName,  localFilePath, fileSize, threadCount, true);		
    }
}