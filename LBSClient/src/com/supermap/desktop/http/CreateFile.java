package com.supermap.desktop.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.supermap.desktop.Application;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * <b>function:</b> 单线程上传文件
 * 
 */
@SuppressWarnings("deprecation")
public class CreateFile {

	// 待上传文件信息
	private FileInfo uploadInfo;

	// 上传是否完成
	private boolean isCreated = false;
	//
	private boolean isFailed;

	private static final int BUFF_LENGTH = 1024 * 8;

	public CreateFile(FileInfo downloadInfo) throws IOException {
		super();
		this.uploadInfo = downloadInfo;
	}

	public CreateFile() {
		super();
	}

	@SuppressWarnings({ "deprecation", "resource" })
	// 创建文件
	public String createFile() {
		String locationURL = "";
		try {
			// 第一步创建一个空文件

			String webFile = this.uploadInfo.getUrl();

			if (!webFile.endsWith("/")) {
				webFile += "/";
			}
			String fileName = URLEncoder.encode( this.uploadInfo.getFileName(), "UTF-8" );
			webFile = MessageFormat.format("{0}{1}?user.name=root&op=CREATE", webFile, fileName);
			HttpClient client = new DefaultHttpClient();
			// 发送http请求，没有自动重定向，也没有发送文件数据（即只是创建一个虚拟文件）
			HttpPut requestPut = new HttpPut(webFile);

			HttpResponse response = client.execute(requestPut);
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
				requestPut = new HttpPut(locationURL);
				String fullPath = this.uploadInfo.getFilePath();
				if (!fullPath.endsWith("\\")) {
					fullPath += "\\";
				}
				fullPath += this.uploadInfo.getFileName();
				response = new DefaultHttpClient().execute(requestPut);
				if (response != null && response.getStatusLine().getStatusCode() == 201) {
					Application.getActiveApplication().getOutput().output("HDFS file create success");
					// 文件创建成功后利用Append命令在文件中追加内容
				} else {

				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return locationURL;
	}

	public InputStream getFileStatus(String url, String fileName) {
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String webFile = String.format("%s%s?user.name=root&op=GETFILESTATUS", url, fileName);
		InputStream inputStream = null;
		URL nowURL;
		try {
			nowURL = new URL(webFile);
			HttpURLConnection connection = (HttpURLConnection) nowURL.openConnection();
			connection.setConnectTimeout(3000);
			// 设置读取数据超时时间为3000ms
			connection.setReadTimeout(3000);
			setHeader(connection);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			inputStream = connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	// 创建文件
	public void appendFile() {
		try {
			String locationURL = "";

			String webFile = this.uploadInfo.getUrl();

			if (!webFile.endsWith("/")) {
				webFile += "/";
			}
			webFile = String.format("%s%s?user.name=root&op=APPEND", webFile, this.uploadInfo.getFileName());
			HttpPost requestPost = new HttpPost(webFile);
			HttpResponse response = new DefaultHttpClient().execute(requestPost);
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
					Header locationHeader = response.getFirstHeader("Location");
					if (locationHeader == null) {
						Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_UnlandFailed"));
						return;
					} else {
						// 获取登陆成功之后跳转链接
						locationURL = locationHeader.getValue();
					}
				}
			}

			if (!StringUtilities.isNullOrEmptyString(locationURL)) {
				requestPost = new HttpPost(locationURL);
				String fullPath = this.uploadInfo.getFilePath();
				if (!fullPath.endsWith("\\")) {
					fullPath += "\\";
				}
				fullPath += this.uploadInfo.getFileName();
				// 上传文件
				File file = new File(fullPath);

				FileEntity fileEntity = new FileEntity(file);
				requestPost.setEntity(fileEntity);
				response = new DefaultHttpClient().execute(requestPost);
				if (response != null && response.getStatusLine().getStatusCode() == 200) {
					Application.getActiveApplication().getOutput()
							.output("File:\"" + fullPath + "\"" + LBSClientProperties.getString("String_UploadEndString"));
					isCreated = true;
				} else {
					Application.getActiveApplication().getOutput()
							.output("File: \"" + fullPath + "\"" + LBSClientProperties.getString("String_UploadEndFailed"));
					isFailed = true;
				}
			}
			// byte[] bytes2 = org.apache.commons.compress.utils.IOUtils.toByteArray(response.getEntity().getContent());
			//
			// System.out.println("response enttiy " + new String(bytes2, "utf-8"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void createDir(String url, String name) {
		try {
			// 创建目录
			String webFile = url;
			if (!webFile.endsWith("/")) {
				webFile += "/";
			}
			webFile = String.format("%s%s?user.name=root&op=MKDIRS", webFile, name);
			HttpPut requestPut = new HttpPut(webFile);
			HttpResponse response = new DefaultHttpClient().execute(requestPut);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				Application.getActiveApplication().getOutput().output(MessageFormat.format(LBSClientProperties.getString("String_MakeDirectorySuccess"), name));
				CommonUtilities.getActiveLBSControl().refresh();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void renameFile(String url, String name, String newName, boolean isDir) {
		try {
			// 重命名文件
			String webFile = url;
			String rootPath = webFile.substring(webFile.lastIndexOf("/v1") + 3, webFile.lastIndexOf("/"));
			if (!rootPath.endsWith("/")) {
				rootPath += "/";
			}
			if (!webFile.endsWith("/")) {
				webFile += "/";
			}
			webFile = String.format("%s%s?user.name=root&op=RENAME&destination=%s", webFile, name, rootPath + newName);
			HttpPut requestPut = new HttpPut(webFile);
			HttpResponse response = new DefaultHttpClient().execute(requestPut);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				if (isDir) {
					Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(LBSClientProperties.getString("String_RenameDirSuccess"), name, newName));
				} else {
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(LBSClientProperties.getString("String_RenameFileSuccess"), name, newName));
				}
				CommonUtilities.getActiveLBSControl().refresh();
			}
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

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	public boolean isFailed() {
		return isFailed;
	}

	public void setFailed(boolean isFailed) {
		this.isFailed = isFailed;
	}

}
