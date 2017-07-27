package com.supermap.desktop.http.upload;

import com.supermap.desktop.lbs.FileInfo;


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
public class TestUploadMain {
 
    public static void main(String[] args) {
    	String webPath = "http://192.168.12.103:50070/webhdfs/v1/tmp/";
    	FileInfo fileInfo = new FileInfo(webPath);
    	String fileName = "test1.java";
    	String localFilePath = "F:/temp/";
    	int threadCount = 3;
    	
		UploadUtils.upload(fileInfo, localFilePath, fileName, threadCount, true);		
    }
}