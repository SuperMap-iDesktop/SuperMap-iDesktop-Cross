package com.supermap.desktop.ui.lbs;

/**
 * 文件信息类
 * 
 * @author
 *
 */
public class FileInfo {
	// 文件url
	private String url;
	// 文件名称
	private String fileName;
	// 真实名称
	private String realName;
	// 文件路径
	private String filePath;
	// file size
	private long fileSize;
	// 分成多少段， 每一段用一个线程完成
	private int splitter;
	// 分成多少段， 每一段用一个线程完成
	private Boolean isHDFSFile;

	// 文件默认保存路径
	private final static String FILE_PATH = "C:/temp";
	// 默认分块数、线程数
	private final static int SPLITTER_NUM = 5;

	public FileInfo() {
		super();
	}

	/**
	 * @param url
	 *            地址
	 */
	public FileInfo(String url) {
		this(url, SPLITTER_NUM);
	}

	/**
	 * @param url
	 *            地址url
	 * @param splitter
	 *            分成多少段或是多少个线程
	 */
	public FileInfo(String url, int splitter) {
		this(url, null, null, null, -1, splitter, false);
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
	 *            分成多少段或是多少个线程
	 ** @param isHDFSFile
	 *            是否为HDFS文件
	 */
	public FileInfo(String url, String fileName, String realName, String filePath, long fileSize, int splitter, Boolean isHDFSFile) {
		super();
		if (url == null || "".equals(url)) {
			throw new RuntimeException("url is not null!");
		}

		this.url = url;
		this.realName = realName;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}