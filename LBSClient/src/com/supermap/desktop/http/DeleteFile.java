package com.supermap.desktop.http;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.text.MessageFormat;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.supermap.desktop.Application;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;

/**
 * <b>function:</b> 单线程下载文件
 * 
 * @author hoojo
 * @createDate 2011-9-22 下午02:55:10
 * @file DownloadFile.java
 * @package com.hoo.download
 * @project MultiThreadDownLoad
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class DeleteFile extends Thread {

	private boolean isDeleted;
	// 文件url
	private String url;
	// 文件名称
	private String fileName;

	private boolean isDirectory;

	public DeleteFile(String url, String fileName, boolean isDirctory) throws IOException {
		super();
		this.url = url;
		this.fileName = fileName;
		this.isDirectory = isDirctory;
	}

	@SuppressWarnings({ "deprecation", "resource" })
	@Override
	public void run() {
		this.deleteFile();
	}

	// 删除文件
	private synchronized void deleteFile() {
		try {
			String webFile = this.url;
			String locationURL = "";
			if (!webFile.endsWith("/")) {
				webFile += "/";
			}
			webFile = String.format("%s%s?user.name=root&op=DELETE", webFile, this.fileName);
			HttpClient client = new DefaultHttpClient();
			HttpDelete requestPut = new HttpDelete(webFile);

			HttpResponse response = client.execute(requestPut);
			if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (isDirectory) {
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(LBSClientProperties.getString("String_DeleteDirSuccess"), this.fileName));
				} else {
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(LBSClientProperties.getString("String_DeleteFileSuccess"), this.fileName));
				}
				// 刷新大数据展示列表
				CommonUtilities.getActiveLBSControl().refresh();
				isDeleted = true;
			} else {
				if (isDirectory) {
					Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(LBSClientProperties.getString("String_DeleteDirFailed"), this.fileName));
				} else {
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(LBSClientProperties.getString("String_DeleteFileFailed"), this.fileName));
				}
				// 刷新大数据展示列表
				CommonUtilities.getActiveLBSControl().refresh();
				isDeleted = false;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * <b>function:</b> 设置URLConnection的头部信息，伪装请求信息
	 * 
	 * @author hoojo
	 * @createDate 2011-9-28 下午05:29:43
	 * @param con
	 */
	public static void setHeader(URLConnection conn) {
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// conn.setRequestProperty("User-Agent",
		// "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "utf-8");
		conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("connnection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since", "Fri, 02 Jan 2009 17:00:05 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-conntrol", "max-age=0");
		conn.setRequestProperty("Referer", "http://www.baidu.com");
		// conn.setRequestProperty("accept", "*/*");
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
