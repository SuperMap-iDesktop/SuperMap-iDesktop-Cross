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

import com.supermap.desktop.Application;
import com.supermap.desktop.http.LogUtils;
import com.supermap.desktop.http.SaveItemFile;
import com.supermap.desktop.lbsclient.LBSClientProperties;

 
/**
 * <b>function:</b> 单线程下载文件
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
    
    //上传文件url
    private String url;
    //下载文件起始位置  
    private long startPos;
    //下载文件结束位置
    private long endPos;
    //下载文件的长度
    private long length;
    //线程id
    private int threadId;    
    //待上传的本地文件
    private String fileName;
    
    //下载是否完成
    private boolean isUploadOver = false;
 
    private SaveItemFile itemFile;
    
    private static final int BUFF_LENGTH = 1024 * 8;
    
    /**
     * @param url 下载文件url
     * @param name 文件名称
     * @param startPos 下载文件起点
     * @param endPos 下载文件结束点
     * @param threadId 线程id
     * @throws IOException
     */
	public UploadFile(String url, String name, long startPos, long endPos,
			long length, int threadId) throws IOException {
		super();
		this.url = url;
		this.startPos = startPos;
		this.endPos = endPos;
		this.length = length;
		this.threadId = threadId;
		// 分块下载写入文件内容
		this.itemFile = new SaveItemFile(name, startPos);
	}

	public void uploadFile(String fileName) {
		try {
			// 换行符
			final String newLine = "\r\n";
			final String boundaryPrefix = "--";
			// 定义数据分隔线
			String BOUNDARY = "========7d4a6d158c9";
			// 服务器的域名
			URL url = new URL(this.url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置为POST情
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求头参数
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// 上传文件
			File file = new File(fileName);
			// 数据输入流,用于读取文件数据
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			byte[] bufferOut = new byte[1024];
			int bytes = 0;
			// 每次读1KB数据,并且将文件数据写入到输出流中
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			// 最后添加换行
			out.write(newLine.getBytes());
			in.close();
			// 定义最后数据分隔线，即--加上BOUNDARY再加上--。
			byte[] end_data = (newLine + boundaryPrefix + BOUNDARY
					+ boundaryPrefix + newLine).getBytes();
			// 写上结尾标识
			out.write(end_data);
			out.flush();
			out.close();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(LBSClientProperties.getString("String_POSTException"));
			e.printStackTrace();
		}
	}
    
    @Override
    public void run() {
        while (endPos > startPos && !isUploadOver) {
            try {
            	URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                // 设置连接超时时间为10000ms
                connection.setConnectTimeout(3000);
                // 设置读取数据超时时间为10000ms
                connection.setReadTimeout(3000);
                
//                setHeader(connection);

                // 换行符
    			final String newLine = "\r\n";
    			final String boundaryPrefix = "--";
    			// 定义数据分隔线
    			String BOUNDARY = "========7d4a6d158c9";
    			
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    			// 设置为POST情
    			conn.setRequestMethod("POST");
    			// 发送POST请求必须设置如下两行
    			conn.setDoOutput(true);
    			conn.setDoInput(true);
    			conn.setUseCaches(false);
    			// 设置请求头参数
    			conn.setRequestProperty("connection", "Keep-Alive");
    			conn.setRequestProperty("Charsert", "UTF-8");
    			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
    			
    			OutputStream outputStream = null;
                while (outputStream == null) {
                	outputStream = new DataOutputStream(conn.getOutputStream());
                }
                
    			// 上传文件
                if (outputStream != null) {
        			File file = new File(this.fileName);
        			// 数据输入流,用于读取文件数据
        			DataInputStream in = new DataInputStream(new FileInputStream(file));
        			byte[] buff = new byte[BUFF_LENGTH];
        			int size = 0;
        			LogUtils.log("#start#Thread: " + threadId + ", startPos: " + startPos + ", endPos: " + endPos);
        			// 每次读1KB数据,并且将文件数据写入到输出流中
        			while ((size = in.read(buff)) != -1) {
        				//上传文件内容，返回最后上传的长度                        		
        				outputStream.write(buff, 0, size);
        				startPos += size;        		
        			}
    				LogUtils.log("#over#Thread: " + threadId + ", startPos: " + startPos + ", endPos: " + endPos);
                    LogUtils.log("Thread " + threadId + " is execute over!");
                    this.isUploadOver = true;
        			in.close();
        			outputStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (itemFile != null) {
                        itemFile.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (endPos < startPos && !isUploadOver) {
            LogUtils.log("Thread " + threadId  + " startPos > endPos, not need download file !");
            this.isUploadOver = true;
        }
        
        if (endPos == startPos && !isUploadOver) {
            LogUtils.log("Thread " + threadId  + " startPos = endPos, not need download file !");
            this.isUploadOver = true;
        }
    }
    
    /**
     * <b>function:</b> 打印下载文件头部信息
     * @author hoojo
     * @createDate 2011-9-22 下午05:44:35
     * @param conn HttpURLConnection
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
     * @author hoojo
     * @createDate 2011-9-28 下午05:29:43
     * @param con
     */
    public static void setHeader(URLConnection conn) {
    	conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
        conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
        conn.setRequestProperty("Accept-Encoding", "utf-8");
        conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        conn.setRequestProperty("Keep-Alive", "300");
        conn.setRequestProperty("connnection", "keep-alive");
        conn.setRequestProperty("If-Modified-Since", "Fri, 02 Jan 2009 17:00:05 GMT");
        conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
        conn.setRequestProperty("Cache-conntrol", "max-age=0");
        conn.setRequestProperty("Referer", "http://www.baidu.com");
//        conn.setRequestProperty("accept", "*/*");
    }
    
    public boolean isUploadOver() {
        return isUploadOver;
    }
    
    public long getStartPos() {
        return startPos;
    }
 
    public long getEndPos() {
        return endPos;
    }
 
    public long getLength() {
        return length;
    }
}
