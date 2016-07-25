package com.supermap.desktop.http.upload;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;

import com.supermap.desktop.Application;
import com.supermap.desktop.http.LogUtils;
import com.supermap.desktop.http.SaveItemFile;
import com.supermap.desktop.lbsclient.LBSClientProperties;

/**
 * <b>function:</b> 单线程上传文件
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
public class UploadFile extends Thread {

	// 上传文件url
	private String url;
	// 本地文件路径
	private String localPath;
	// 上传文件起始位置
	private long startPos;
	// 上传文件结束位置
	private long endPos;
	// 上传文件的长度
	private long length;
	// 线程id
	private int threadId;

	private OutputStream outputStream;

	private InputStream inputStream;

	private boolean isUploadOver;

	// 上传速度
	private long speed;

	private static int BUFF_LENGTH = 1024 * 256;

	/**
	 * @param url
	 *            上传文件url
	 * @param name
	 *            文件名称
	 * @param startPos
	 *            上传文件起点
	 * @param endPos
	 *            上传文件结束点
	 * @param threadId
	 *            线程id
	 * @throws IOException
	 */
	public UploadFile(String url, String localPath, long startPos, long endPos, long length, int threadId) {
		this.url = url;
		this.startPos = startPos;
		this.endPos = endPos;
		this.length = length;
		// 上传通过Append实现，只能支持单线程
		this.threadId = threadId;
		this.localPath = localPath;
	}
	
	@Override
	public void run() {
		if (endPos > startPos) {
			// 要上传的文件，每次上传4k
			while (endPos > startPos && !isUploadOver) {
				try {
					URL url = new URL(this.url);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					// 设置连接超时时间为10000ms
					connection.setConnectTimeout(3000);
					// 设置读取数据超时时间为10000ms
					connection.setReadTimeout(3000);
					setHeader(connection);
					connection.setDoOutput(true);
					// //获取文件输出流，读取文件内容
//					inputStream = connection.getInputStream();
					outputStream = connection.getOutputStream();
					while (outputStream == null) {
						outputStream = connection.getOutputStream();
					}

					if (outputStream != null) {
						byte[] buff = new byte[BUFF_LENGTH];
						while (startPos < endPos && !isUploadOver) {
							// 写入文件内容，返回最后写入的长度
							outputStream.write(buff, 0, BUFF_LENGTH);
							outputStream.flush();
							startPos += BUFF_LENGTH;
							endPos += BUFF_LENGTH;
						}
						this.isUploadOver = true;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
//						inputStream.close();
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			if (endPos < startPos && !isUploadOver) {
				Application.getActiveApplication().getOutput().output("Thread " + threadId + " startPos > endPos, not need download file !");
				this.isUploadOver = true;
			}

			if (endPos == startPos && !isUploadOver) {
				Application.getActiveApplication().getOutput().output("Thread " + threadId + " startPos = endPos, not need download file !");
				this.isUploadOver = true;
			}
		}
	}

	private void setHeader(URLConnection conn) {
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "utf-8");
		conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("connnection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since", "Fri, 22 Jan 2016 12:00:00 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-conntrol", "max-age=0");
		conn.setRequestProperty("Referer", "http://www.baidu.com");
		conn.setRequestProperty("doOutput", "true");
	}

	public long getLength() {
		return length;
	}

	public long getEndPos() {
		return endPos;
	}

	public long getStartPos() {
		return startPos;
	}

	public boolean isUploadOver() {
		return isUploadOver;
	}
		
}
