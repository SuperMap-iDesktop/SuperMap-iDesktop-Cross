package com.supermap.desktop.http.upload;

import java.io.*;
import java.net.*;

import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.supermap.desktop.Application;
import com.supermap.desktop.http.LogUtils;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.StringUtilities;

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
	// 本地文件
	private File localFile;
	// 上传文件起始位置
	private long startPos;
	// 上传文件结束位置
	private long endPos;
	// 上传文件的长度
	private long length;
	// 线程id
	private int threadId;

	private OutputStream outputStream;

	private FileInputStream stream;

	private InputStream inputStream;

	private boolean isUploadOver;

	private boolean isStop;

	// 上传速度
	private long speed;

	private static int BUFF_LENGTH = 1024 * 256;
	
	private int startLength;

	/**
	 * @param url
	 *            上传文件url
	 * @param localFile
	 *            上传文件
	 * @param startPos
	 *            上传文件起点
	 * @param endPos
	 *            上传文件结束点
	 * @param threadId
	 *            线程id
	 * @throws IOException
	 */
	public UploadFile(String url, File localFile, long startPos, long endPos, long length, int threadId) {
		this.url = url;
		this.localFile = localFile;
		this.startPos = startPos;
		this.endPos = endPos;
		this.length = length;
		// 上传通过Append实现，只能支持单线程
		this.threadId = threadId;
		this.startLength = (int) startPos; 
	}

	@Override
	public void run() {
		try {
			int size = -1;
			stream = new FileInputStream(localFile);
			//跳过startLength开始读取文件，保证文件上传长度正确
			stream.skip(startLength);
			byte[] buff = new byte[BUFF_LENGTH];
			// 以下两个变量用来统计传输速度
			long timeSpanSeconds = 0;
			long range = 0;
			speed = 0L;
			long startTime = System.currentTimeMillis();
			while ((size = stream.read(buff, 0, BUFF_LENGTH)) > 0 && startPos < endPos && !isStop) {
				String locationURL = "";
				HttpPost requestPost = new HttpPost(url);
				HttpResponse response = new DefaultHttpClient().execute(requestPost);
				if (response != null) {
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
						Header locationHeader = response.getFirstHeader("Location");
						if (locationHeader == null) {
							Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_UnlandFailed"));
						} else {
							// 获取登陆成功之后跳转链接
							locationURL = locationHeader.getValue();
						}
					}
				}

				if (!StringUtilities.isNullOrEmptyString(locationURL)) {
					// 利用post请求往服务器上上传内容
					URL nowURL = new URL(locationURL);
					HttpURLConnection connection = (HttpURLConnection) nowURL.openConnection();
					connection.setConnectTimeout(3000);
					// 设置读取数据超时时间为3000ms
					connection.setReadTimeout(3000);
					setHeader(connection);
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					outputStream = connection.getOutputStream();
					while (outputStream == null) {
						outputStream = connection.getOutputStream();
					}
					if (outputStream != null) {
						// 将文件写入字节流中
						outputStream.write(buff, 0, size);
						outputStream.flush();
						startPos += size;
						range += size;
						timeSpanSeconds += System.currentTimeMillis() - startTime;
						if (timeSpanSeconds > 1000) {
							speed = (range * 1000) / timeSpanSeconds;
							timeSpanSeconds = 0;
							range = 0;
							startTime = System.currentTimeMillis();
						}
						inputStream = connection.getInputStream();
						// 写入文件到远程服务器
						inputStream.read(buff);
					}
				}
			}
			if (endPos <= startPos) {
				outputStream.close();
				stream.close();
				inputStream.close();
				isUploadOver = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		if (endPos < startPos && !isUploadOver) {
			LogUtils.log("Thread " + threadId + " startPos > endPos, not need upload file !");
			this.isUploadOver = true;
		}

		if (endPos == startPos && !isUploadOver) {
			LogUtils.log("Thread " + threadId + " startPos = endPos, not need upload file !");
			this.isUploadOver = true;
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
		conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
		conn.setRequestProperty("Referer", "http://www.baidu.com");
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

	public long getSpeed() {
		return speed;
	}

	public void setSpeed(long speed) {
		this.speed = speed;
	}

	public void stopUpload() {
		this.isStop = true;
	}
}
