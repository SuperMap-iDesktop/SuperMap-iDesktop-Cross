package com.supermap.desktop.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import javax.swing.event.EventListenerList;

import org.apache.http.HttpStatus;

import com.supermap.desktop.Application;
import com.supermap.desktop.core.FileSize;
import com.supermap.desktop.core.FileSizeType;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.StringUtilities;

public class HttpPostFile {
	private static final String POST = "POST";
	private static final int DEFAULT_TIMEOUT = 30000000; // 500分钟
	private static final int BUFFER_SIZE = 5120;
	private static final int DEFAULT_CHUNK_SIZE = 1024;

	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";

	private String url = "";
	private String fileName = "";
	private String boundary = "";
	private boolean isCancel = false;
	private Proxy proxy = null;

	private EventListenerList listenerList = new EventListenerList();

	public HttpPostFile(String url) throws UnsupportedEncodingException {
		this(url, "");
	}

	public HttpPostFile(String url, String fileName) throws UnsupportedEncodingException {

		// 很重要，必须要进行转码，请求以及请求体中不能直接识别空格等特殊字符
		this(url, fileName, URLEncoder.encode("------------------------" + new Date().toString(), "UTF-8"));
	}

	/**
	 * @param url
	 *            上传 api
	 * @param fileName
	 *            文件名（不带后缀）
	 * @param boundary
	 *            分隔符
	 */
	public HttpPostFile(String url, String fileName, String boundary) {
		this(url, fileName, boundary, "", -1);
	}

	/**
	 * @param url
	 *            上传
	 * @param fileName
	 *            文件名（不带后缀）
	 * @param boundary
	 *            分隔符
	 * @param hostName
	 *            代理主机
	 * @param port
	 *            代理端口
	 */
	public HttpPostFile(String url, String fileName, String boundary, String hostName, int port) {
		this.url = url;
		this.fileName = fileName;
		this.boundary = boundary;
		if (!StringUtilities.isNullOrEmpty(hostName) && port >= 0) {
			this.proxy = new Proxy(Type.HTTP, new InetSocketAddress(hostName, port));
		}
	}

	/**
	 * @param file
	 * @return
	 */
	public String post(File file) {
		String result = "";
		FileInputStream fileInputStream = null;
		BufferedReader responseReader = null;

		try {
			long totalSize = file.length();
			if (StringUtilities.isNullOrEmpty(this.fileName)) {
				this.fileName = FileUtilities.getFileNameWithoutExtension(file);
			}

			// 这会用到请求体里，作为换行符使用，协议规定的请求体应该是平台无关的，因此换行符也是指定的，否则 Linux 上的换行符会导致请求正文解析失败
			String lineSeparator = "\r\n";
			StringBuilder prePostData = new StringBuilder();
			prePostData.append("--" + boundary); // 起始边界符
			prePostData.append(lineSeparator); // 另起一行
			prePostData.append("Content-Disposition:form-data;name=\"file\";filename=\"");
			prePostData.append(this.fileName + ".zip\"");
			prePostData.append(lineSeparator); // 另起一行
			prePostData.append("Content-Type:application/octet-stream");
			prePostData.append(lineSeparator);// 另起一行
			prePostData.append(lineSeparator);// 空一行
			// ----这里就填写二进制数据----
			// 然后是结束边界符
			String endBoundary = lineSeparator + "--" + boundary + "--" + lineSeparator;

			// 初始化Http请求
			URL url = new URL(this.url);
			HttpURLConnection connection = this.proxy == null ? (HttpURLConnection) url.openConnection() : (HttpURLConnection) url.openConnection(this.proxy);
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// 分块传输，很重要，必须要进行设置，否则大文件时会内存溢出
			connection.setChunkedStreamingMode(DEFAULT_CHUNK_SIZE);
			connection.setRequestMethod(POST);
			connection.setConnectTimeout(DEFAULT_TIMEOUT);
			connection.setRequestProperty(HEADER_CONTENT_TYPE, "multipart/form-data;boundary=" + boundary);
			connection.setRequestProperty(HEADER_CONTENT_LENGTH, String.valueOf(totalSize + prePostData.length() + endBoundary.length()));
			connection.connect();
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

			long uploadOffset = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			// 写入起始边界符
			outputStream.writeBytes(prePostData.toString());

			// 写入数据
			fileInputStream = new FileInputStream(file);

			FileSize totalFileSize = new FileSize(totalSize, FileSizeType.BYTE);
			// 每一个时间片的起点
			// 将上传过程分为不同的时间片段，用来计算上传速度，剩余时间等
			Date startTime = new Date();
			long segment = 0;

			while (uploadOffset < totalSize) {
				if (this.isCancel) {
					break;
				}

				int realReadSize = fileInputStream.read(buffer, 0, BUFFER_SIZE);
				outputStream.write(buffer, 0, realReadSize);
				uploadOffset += realReadSize;
				segment += realReadSize;
				outputStream.flush();

				Date currentDate = new Date();
				if (currentDate.getTime() - startTime.getTime() > 1000) {
					try {
						FileSize uploadedFileSize = new FileSize(uploadOffset, FileSizeType.BYTE);
						// 还剩下多少没有
						FileSize restFileSize = new FileSize(totalSize - uploadOffset, FileSizeType.BYTE);
						// 每一个时间片内传输的数据量
						FileSize segmentFileSize = new FileSize(segment, FileSizeType.BYTE);
						// 传输速度
						FileSize speed = FileSize.divide(segmentFileSize, (currentDate.getTime() - startTime.getTime()) / 1000d);
						// 剩余时间
						int remainTime = new Double(FileSize.divide(restFileSize, speed)).intValue();

						HttpPostEvent event = new HttpPostEvent(this, totalFileSize, uploadedFileSize, speed, remainTime);
						fireHttpPost(event);

						// 如果取消了，结束上传
						if (event.isCancel()) {
							this.isCancel = true;
						}
					} finally {
						segment = 0;
						startTime = new Date();
					}
				}
			}
			// 写入结束边界符
			outputStream.writeBytes(endBoundary);
			outputStream.flush();
			// 关闭请求流
			outputStream.close();
			fireHttpPost(new HttpPostEvent(this, totalFileSize, totalFileSize, FileSize.ZERO, 0));

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpStatus.SC_OK || responseCode == HttpStatus.SC_CREATED || responseCode == HttpStatus.SC_ACCEPTED) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					result += line;
					result += System.lineSeparator();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (responseReader != null) {
					responseReader.close();
				}
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		return result;
	}

	public void addHttpPostListener(HttpPostListener listener) {
		this.listenerList.add(HttpPostListener.class, listener);
	}

	public void removeHttpPostListener(HttpPostListener listener) {
		this.listenerList.remove(HttpPostListener.class, listener);
	}

	protected void fireHttpPost(HttpPostEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == HttpPostListener.class) {
				((HttpPostListener) listeners[i + 1]).httpPost(e);
			}
		}
		this.isCancel = e.isCancel();
	}
}
