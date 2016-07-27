package com.supermap.desktop.http.download;

/**
 * <b>function:</b> 下载文件信息类
 * 
 * @author hoojo
 * @createDate 2011-9-21 下午05:14:58
 * @file DownloadInfo.java
 * @package com.hoo.entity
 * @project MultiThreadDownLoad
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FileInfo {
	// 下载文件url
	private String url;
	// 下载文件名称
	private String fileName;
	// 下载文件路径
	private String filePath;
	// file size
	private long fileSize;
	// 分成多少段下载， 每一段用一个线程完成下载
	private int splitter;
	// 分成多少段下载， 每一段用一个线程完成下载
	private Boolean isHDFSFile;

	// 下载文件默认保存路径
	private final static String FILE_PATH = "C:/temp";
	// 默认分块数、线程数
	private final static int SPLITTER_NUM = 5;

	public FileInfo() {
		super();
	}

	/**
	 * @param url
	 *            下载地址
	 */
	public FileInfo(String url) {
		this(url, SPLITTER_NUM);
	}

	/**
	 * @param url
	 *            下载地址url
	 * @param splitter
	 *            分成多少段或是多少个线程下载
	 */
	public FileInfo(String url, int splitter) {
		this(url, null, null, -1, splitter, false);
	}

	/***
	 * @param url
	 *            地址
	 * @param fileName
	 *            文件名称
	 * @param filePath
	 *            文件路径
	 * @param fileSize
	 *            file size
	 * @param splitter
	 *            分成多少段或是多少个线程下载
	 ** @param isHDFSFile
	 *            是否为HDFS文件
	 */
	public FileInfo(String url, String fileName, String filePath, long fileSize, int splitter, Boolean isHDFSFile) {
		super();
		if (url == null || "".equals(url)) {
			throw new RuntimeException("url is not null!");
		}
		
		this.url = url;
		this.fileName = (fileName == null || "".equals(fileName)) ? getFileName(url) : fileName;
		this.filePath = (filePath == null || "".equals(filePath)) ? FILE_PATH : filePath;
		this.fileSize = fileSize;
		this.splitter = (splitter < 1) ? SPLITTER_NUM : splitter;
		this.isHDFSFile = isHDFSFile;
	}

	/**
	 * <b>function:</b> 通过url获得文件名称
	 * 
	 * @author hoojo
	 * @createDate 2011-9-30 下午05:00:00
	 * @param url
	 * @return
	 */
	private String getFileName(String url) {
		return url.substring(url.lastIndexOf("/") + 1, url.length());
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (url == null || "".equals(url)) {
			throw new RuntimeException("url is not null!");
		}
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = (fileName == null || "".equals(fileName)) ? getFileName(url) : fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = (filePath == null || "".equals(filePath)) ? FILE_PATH : filePath;
	}

	public long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getSplitter() {
		return splitter;
	}

	public void setSplitter(int splitter) {
		this.splitter = (splitter < 1) ? SPLITTER_NUM : splitter;
	}

	public Boolean isHDFSFile() {
		return this.isHDFSFile;
	}
	
	public void setHDFSFile(Boolean isHDFSFile) {
		this.isHDFSFile = isHDFSFile;
	}
	
	@Override
	public String toString() {
		return this.url + "#" + this.fileName + "#" + this.filePath + "#" + this.splitter;
	}
	
	@Override
	public boolean equals(Object obj) {		
		Boolean result = true;
        if (obj instanceof FileInfo) {
        	FileInfo downloadInfo = (FileInfo) obj;
            if (!this.url.equals(downloadInfo.getUrl())) {
            	result = false;
            }
            
            if (result && !this.fileName.equals(downloadInfo.getFileName())) {
            	result = false;
            }
            
            if (result && !this.filePath.equals(downloadInfo.getFilePath())) {
            	result = false;
            }
            
            if (result && !(this.fileSize == downloadInfo.getFileSize())) {
            	result = false;
            }
            
            if (result && !(this.splitter == downloadInfo.getSplitter())) {
            	result = false;
            }
            
            if (result && !this.isHDFSFile.equals(downloadInfo.isHDFSFile())) {
            	result = false;
            }
        }
        return result;
    }
        
	@Override
    public int hashCode() {
        return 1;            
    }
}