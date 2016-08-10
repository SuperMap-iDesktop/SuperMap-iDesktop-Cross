package com.supermap.desktop.http.download;

import com.supermap.desktop.http.LogUtils;
import com.supermap.desktop.http.SaveItemFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

 
/**
 * <b>function:</b> 单线程下载文件
 * @author xie
 */
public class DownloadFile extends Thread {
    
    //下载文件url
    private String url;
    //下载文件起始位置  
    private long startPos;
    //下载文件结束位置
    private long endPos;
    //下载文件的长度
    private long length;
    //线程id
    private int threadId;
    
    //下载是否完成
    private boolean isDownloadOver = false;
 
    private SaveItemFile itemFile;
    
    private boolean isStop = false;
    // 计算下载速度
    private long speed;
    
    private static final int BUFF_LENGTH = 1024 * 8;
    
    /**
     * @param url 下载文件url
     * @param name 文件名称
     * @param startPos 下载文件起点
     * @param endPos 下载文件结束点
     * @param threadId 线程id
     * @throws IOException
     */
    public DownloadFile(String url, String name, long startPos, long endPos, long length, int threadId) throws IOException {
        super();
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.length = length;
        this.threadId = threadId;
        //分块下载写入文件内容
        this.itemFile = new SaveItemFile(name, startPos);
    } 
    
    @Override
    public void run() {
    	 //以下两个变量用来统计传输速度
        long timeSpanSeconds = 0;
        long range = 0;
        speed = 0L;
        long startTime = System.currentTimeMillis();
        while (endPos > startPos && !isDownloadOver && !isStop) {
            try {
            	URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                // 设置连接超时时间为10000ms
                connection.setConnectTimeout(30000);
                // 设置读取数据超时时间为10000ms
                connection.setReadTimeout(30000);                
                setHeader(connection);
    			
                //获取文件输入流，读取文件内容
                InputStream inputStream = connection.getInputStream();
                while (inputStream == null) {
                	inputStream = connection.getInputStream();
                }
                
                if (inputStream != null) { 
                	byte[] buff = new byte[BUFF_LENGTH];
                    int size = -1;
                    LogUtils.log("#start#Thread: " + threadId + ", startPos: " + startPos + ", endPos: " + endPos);
                    while ((size = inputStream.read(buff)) > 0 && startPos < endPos && !isDownloadOver) {
                        //写入文件内容，返回最后写入的长度
                        startPos += itemFile.write(buff, 0, size);
                        range += size;
                        timeSpanSeconds += System.currentTimeMillis() - startTime;
                        if (timeSpanSeconds > 1000)
                        {
                            speed = (range * 1000) / timeSpanSeconds;
                            timeSpanSeconds = 0;
                            range = 0;
                            startTime = System.currentTimeMillis();
                        }
                    }
                    LogUtils.log("#over#Thread: " + threadId + ", startPos: " + startPos + ", endPos: " + endPos);
                    LogUtils.log("Thread " + threadId + " is execute over!");
                    this.isDownloadOver = true;
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
        
        if (endPos < startPos && !isDownloadOver) {
            LogUtils.log("Thread " + threadId  + " startPos > endPos, not need download file !");
            this.isDownloadOver = true;
        }
        
        if (endPos == startPos && !isDownloadOver) {
            LogUtils.log("Thread " + threadId  + " startPos = endPos, not need download file !");
            this.isDownloadOver = true;
        }
    }
    
    /**
     * <b>function:</b> 打印下载文件头部信息
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
    
    public boolean isDownloadOver() {
        return isDownloadOver;
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
    
    public void stopDownload(){
    	this.isStop = true;
    }

	public long getSpeed() {
		return speed;
	}
    
}
