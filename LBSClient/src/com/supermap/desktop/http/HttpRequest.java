package com.supermap.desktop.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.supermap.desktop.Application;
import com.supermap.desktop.lbsclient.LBSClientProperties;

public class HttpRequest {
	static private final Log LOG = LogFactory
			.getLog(HttpRequest.class);
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String getHttpString(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			InputStream inputStream = getHttpStream(url, param);
			
			// 定义 BufferedReader输入流来读取URL的响应
			if (inputStream != null) {
				in = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_ConnectException"));
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
//		try {
//			result = new String(result.getBytes("iso-8859-1"), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} 
		return result;
	}
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static InputStream getHttpStream(String url, String param) {
		InputStream inputStream = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Accept-Encoding", "utf-8");
			connection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			connection.setConnectTimeout(3000);
			connection.setDoInput(true);
			// 建立实际的连接
			connection.connect();
			
//			// 获取所有响应头字段
//			Map<String, List<String>> map = connection.getHeaderFields();
//			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				Application.getActiveApplication().getOutput().output(key + "--->" + map.get(key));				
//			}
			
			inputStream = connection.getInputStream();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_ConnectException"));
			e.printStackTrace();
		}
		return inputStream;
	}

	public static Boolean saveFileToDisk(String urlPath, String param, String localPath) {		
		Boolean result = false;
		
		Application.getActiveApplication().getOutput().output("Start fetch stream.");
		InputStream inputStream = null;
		while (inputStream == null) {
			inputStream = getHttpStream(urlPath, param);
		}
		
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
			if (inputStream != null) {
				Application.getActiveApplication().getOutput().output("Fetch stream finished.");
				Application.getActiveApplication().getOutput().output("Start save file.");
				fileOutputStream = new FileOutputStream(localPath);
				while ((len = inputStream.read(data)) != -1) {
					fileOutputStream.write(data, 0, len);
				}
				Application.getActiveApplication().getOutput().output("save file finished.");
				
				result = true;
			}			
		} catch (IOException e) {
			Application.getActiveApplication().getOutput().output("Save file exception！");
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Application.getActiveApplication().getOutput().output(e);
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					Application.getActiveApplication().getOutput().output(e);
				}
			}
		}
		
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @param params
	 *            填写的url的参数
	 * @param encode
	 *            字节编码
	 * @return
	 */
	public static String sendPostMessage(Map<String, String> params, String encode) {
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("username", "admin");
		// params.put("password", "123");
		// String result = http_Post.sendPostMessage(params, "utf-8");
		
		// 作为StringBuffer初始化的字符串
		StringBuffer buffer = new StringBuffer();
		try {
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					// 完成转码操作
					buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode))
							.append("&");
				}
				buffer.deleteCharAt(buffer.length() - 1);
			}
			// System.out.println(buffer.toString());
			// 删除掉最有一个&

			System.out.println("-->>" + buffer.toString());

			// 请求服务器端的url
			String PATH = "http://192.168.1.125:8080/myhttp/servlet/LoginAction";
			URL url = new URL(PATH);

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(3000);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);// 表示从服务器获取数据
			urlConnection.setDoOutput(true);// 表示向服务器写数据
			// 获得上传信息的字节大小以及长度
			byte[] mydata = buffer.toString().getBytes();
			// 表示设置请求体的类型是文本类型
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Content-Length", String.valueOf(mydata.length));
			// 获得输出流,向服务器输出数据
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();
			// 获得服务器响应的结果和状态码
			int responseCode = urlConnection.getResponseCode();
			if (responseCode == 200) {
				return changeInputStream(urlConnection.getInputStream(), encode);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将一个输入流转换成指定编码的字符串
	 * 
	 * @param inputStream
	 * @param encode
	 * @return
	 */
	private static String changeInputStream(InputStream inputStream, String encode) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}
				result = new String(outputStream.toByteArray(), encode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	static public String sendDELETE(String restfulUrl,
			Map<String, String> params) {
		HttpClient client = new DefaultHttpClient();
		HttpDelete requestDelete = new HttpDelete(restfulUrl);
		requestDelete
				.setHeader("Accept",
						"application/json, application/xml, text/html, text/*, image/*, */*");
		String result = "";
		try {
			HttpResponse httpResponse = client.execute(requestDelete);
			if (httpResponse == null)
				return result;
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				result = EntityUtils.toString(httpResponse.getEntity());
		} catch (IOException ignore) {
			ignore.printStackTrace();
			if (LOG.isErrorEnabled())
				LOG.error(ignore);
		} finally {
			if (client != null)
				client.getConnectionManager().shutdown();
		}
		return result;
	}

	static public String sendPUT(String restfulUrl, Map<String, String> params) {
		HttpClient client = new DefaultHttpClient();
		HttpPut requestPut = new HttpPut(restfulUrl);
		requestPut
				.setHeader("Accept",
						"application/json, application/xml, text/html, text/*, image/*, */*");
		final List<BasicNameValuePair> putData = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			putData.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		if (params != null && !params.isEmpty()) {
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(putData,
						HTTP.UTF_8);
				requestPut.setEntity(entity);
			} catch (UnsupportedEncodingException ignore) {
				ignore.printStackTrace();
			}
		}
		String result = "";
		try {
			HttpResponse httpResponse = client.execute(requestPut);
			if (httpResponse == null)
				return result;
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				result = EntityUtils.toString(httpResponse.getEntity());
		} catch (IOException ignore) {
			ignore.printStackTrace();
			if (LOG.isErrorEnabled())
				LOG.error(ignore);
		} finally {
			if (client != null)
				client.getConnectionManager().shutdown();
		}
		return result.toString();
	}
}