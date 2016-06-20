package com.supermap.desktop.http;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.supermap.desktop.Application;

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
public class CreateFile extends Thread {

	// 上传文件url
	private String url;
	// 待上传的本地文件名称
	private String fileName;
	// 待上传的本地文件路径
	private String filePath;

	// 下载是否完成
	private boolean isCreated = false;

	private static final int BUFF_LENGTH = 1024 * 8;

	/**
	 * @param url
	 *            下载文件url
	 * @param name
	 *            文件名称
	 * @param startPos
	 *            下载文件起点
	 * @param endPos
	 *            下载文件结束点
	 * @param threadId
	 *            线程id
	 * @throws IOException
	 */
	public CreateFile(String url, String filePath, String fileName) throws IOException {
		super();
		this.url = url;
		this.filePath = filePath;
		this.fileName = fileName;
	}

	@SuppressWarnings({ "deprecation", "resource" })
	@Override
	public void run() {
		this.createFile();
//		this.createDir();
//		this.renameFile();
	}
	
	// 创建文件
	private void createFile() {
		try {			
			String webFile = this.url;
			String locationURL = "";
			if (!webFile.endsWith("/")) {
				webFile += "/";
			}
			webFile = String.format("%s%s?user.name=root&op=CREATE", webFile, this.fileName);
			HttpClient client = new DefaultHttpClient();
			HttpPut requestPut = new HttpPut(webFile);
	        
			HttpResponse response = client.execute(requestPut);
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
					Header locationHeader = response.getFirstHeader("Location");
					if (locationHeader == null) {
						System.out.println("登陆不成功，请稍后再试!");
						return;
					} else {
						// 获取登陆成功之后跳转链接
						locationURL = locationHeader.getValue();					
					}
				}
//				Header headers[] = response.getAllHeaders();
//				for (int i = 0; i < headers.length; i++) {
//					System.out.println(headers[i].getName() + "   "
//							+ headers[i].getValue());
//				}				
			}
			
			if (!"".equals(locationURL)) {
				requestPut = new HttpPut(locationURL);
				
				String fullPath = this.filePath;
				if (!fullPath.endsWith("/")) {
					fullPath += "/";
				}
				fullPath += this.fileName;
				File file = new File(fullPath);
				FileEntity fileEntity = new FileEntity(file);
				requestPut.setEntity( fileEntity );
//		        HttpParams params = client.getParams();
//		        params.setParameter( CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1 );
//		        params.setParameter( CoreConnectionPNames.SO_TIMEOUT, new Integer( 15000 ) );
//		        params.setParameter( CoreConnectionPNames.CONNECTION_TIMEOUT, new Integer( 15000 ) );	
				HttpClient allocateClient = new DefaultHttpClient();
				response = allocateClient.execute(requestPut);				
				if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Application.getActiveApplication().getOutput().output("file \"" + fullPath + "\" upload finished.");
				} else {
					Application.getActiveApplication().getOutput().output("file \"" + fullPath + "\" upload failed.");
				}
			}

			System.out.println(response.getStatusLine());
			byte[] bytes2 = org.apache.commons.compress.utils.IOUtils
					.toByteArray(response.getEntity().getContent());

			System.out.println("response enttiy " + new String(bytes2, "utf-8"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	private void createDir() {
		try {
			// 创建目录
			String webFile = this.url;
			String locationURL = "";
			if (!webFile.endsWith("/")) {
				webFile += "/";
			}
			webFile = String.format("%s%s?user.name=root&op=MKDIRS", webFile, "dir4");
			HttpClient client = new DefaultHttpClient();
			HttpPut requestPut = new HttpPut(webFile);
			HttpResponse response = client.execute(requestPut);
			if (response != null) {
				System.out.println(response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
					Header locationHeader = response.getFirstHeader("Location");
					if (locationHeader == null) {
						System.out.println("登陆不成功，请稍后再试!");
						return;
					} else {
						// 获取登陆成功之后跳转链接
						locationURL = locationHeader.getValue();					
					}
				}
				Header headers[] = response.getAllHeaders();
				for (int i = 0; i < headers.length; i++) {
					System.out.println(headers[i].getName() + "   "
							+ headers[i].getValue());
				}				
			}
			
			if (!"".equals(locationURL)) {
				requestPut = new HttpPut(locationURL);
				response = client.execute(requestPut);
				
				if (response != null) {
					
				}
			}

			System.out.println(response.getStatusLine());
			byte[] bytes = org.apache.commons.compress.utils.IOUtils
					.toByteArray(response.getEntity().getContent());

			System.out.println("response enttiy " + new String(bytes, "utf-8"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	private void renameFile() {
		try {
			// 重命名文件
			String webFile = this.url;
			String locationURL = "";
			if (!webFile.endsWith("/")) {
				webFile += "/";
			}
			webFile = String.format("%s%s?user.name=root&op=RENAME&destination=%s", webFile, "test0.csv", "test1.csv");
			HttpClient client = new DefaultHttpClient();
			HttpPut requestPut = new HttpPut(webFile);
			HttpResponse response = client.execute(requestPut);
			if (response != null) {
				System.out.println(response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
					Header locationHeader = response.getFirstHeader("Location");
					if (locationHeader == null) {
						System.out.println("登陆不成功，请稍后再试!");
						return;
					} else {
						// 获取登陆成功之后跳转链接
						locationURL = locationHeader.getValue();					
					}
				}
				Header headers[] = response.getAllHeaders();
				for (int i = 0; i < headers.length; i++) {
					System.out.println(headers[i].getName() + "   "
							+ headers[i].getValue());
				}				
			}
			
			if (!"".equals(locationURL)) {
				requestPut = new HttpPut(locationURL);
				response = client.execute(requestPut);
				
				if (response != null) {
					
				}
			}

			System.out.println(response.getStatusLine());
			byte[] bytes = org.apache.commons.compress.utils.IOUtils
					.toByteArray(response.getEntity().getContent());

			System.out.println("response enttiy " + new String(bytes, "utf-8"));			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * <b>function:</b> 打印下载文件头部信息
	 * 
	 * @author hoojo
	 * @createDate 2011-9-22 下午05:44:35
	 * @param conn
	 *            HttpURLConnection
	 */
	public static void printHeader(URLConnection conn) {
		int i = 1;
		while (true) {
			String header = conn.getHeaderFieldKey(i);
			i++;
			if (header != null) {
				LogUtils.info(header + ":" + conn.getHeaderField(i));
			} else {
				break;
			}
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
		conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// conn.setRequestProperty("User-Agent",
		// "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "utf-8");
		conn.setRequestProperty("Accept-Charset",
				"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("connnection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since",
				"Fri, 02 Jan 2009 17:00:05 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-conntrol", "max-age=0");
		conn.setRequestProperty("Referer", "http://www.baidu.com");
		// conn.setRequestProperty("accept", "*/*");
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}
}
