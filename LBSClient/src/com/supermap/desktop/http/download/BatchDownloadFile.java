package com.supermap.desktop.http.download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.supermap.desktop.Application;
import com.supermap.desktop.http.LogUtils;
import com.supermap.desktop.lbsclient.LBSClientProperties;

/**
 * <b>function:</b> 分批量下载文件
 * 
 * @author:xie
 */
public class BatchDownloadFile extends Thread {
	// 下载文件信息
	private FileInfo downloadInfo;
	// 一组开始下载位置
	private long[] startPos;
	// 一组结束下载位置
	private long[] endPos;
	// 一组原始文件大小
	private long[] length;
	// 休眠时间
	private static final int SLEEP_SECONDS = 1000;
	// 子线程下载
	private DownloadFile[] fileItems;
	// 文件长度
	private long fileSize;
	// 是否第一个文件
	private boolean first = true;
	// 是否停止下载
	private boolean stop = false;
	// 临时文件信息
	private File tempFile;
	// 下载速度
	private long speed;

	public BatchDownloadFile(FileInfo downloadInfo) {
		this.downloadInfo = downloadInfo;
		String tempPath = this.downloadInfo.getFilePath() + File.separator + downloadInfo.getFileName() + ".position";
		tempFile = new File(tempPath);
		// 如果存在读入点位置的文件
		if (tempFile.exists()) {
			first = false;
			// 就直接读取内容
			try {
				readPosInfo();
				// 文件长度直接取自最后一段的结束位置
				fileSize = endPos[endPos.length - 1];
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 数组的长度就要分成多少段的数量
			startPos = new long[downloadInfo.getSplitter()];
			endPos = new long[downloadInfo.getSplitter()];
			length = new long[downloadInfo.getSplitter()];
		}
	}

	@Override
	public void run() {
		// 首次下载，获取下载文件长度
		if (first && null != downloadInfo) {
			// 如果是HDFS文件，直接从 downloadInfo 中获取文件长度
			if (!downloadInfo.isHDFSFile()) {
				fileSize = this.getFileSize();// 获取文件长度
			} else {
				fileSize = downloadInfo.getFileSize();
			}

			if (fileSize == -1) {
				LogUtils.log("file size is know!");
				stop = true;
			} else if (fileSize == -2) {
				LogUtils.log("read file length is error!");
				stop = true;
			} else if (fileSize > 0) {
				/**
				 * eg start: 1, 3, 5, 7, 9 end: 3, 5, 7, 9, length
				 */
				for (int i = 0, len = startPos.length; i < len; i++) {
					long size = i * (fileSize / len);
					startPos[i] = size;

					// 设置最后一个结束点的位置
					if (i == len - 1) {
						endPos[i] = fileSize;
					} else {
						size = (i + 1) * (fileSize / len);
						endPos[i] = size;
					}
					length[i] = endPos[i] - startPos[i];
					LogUtils.log("start-end Position[" + i + "]: " + startPos[i] + "-" + endPos[i]);
				}
			} else {
				LogUtils.log("get file size is error, download is stop!");
				stop = true;
			}
		}

		// 子线程开始下载
		if (!stop) {
			// 创建单线程下载对象数组
			fileItems = new DownloadFile[startPos.length];

			for (int i = 0; i < startPos.length; i++) {
				try {
					// 创建指定个数单线程下载对象，每个线程独立完成指定块内容的下载
					String fileName = URLEncoder.encode(this.downloadInfo.getFileName(), "UTF-8");
					String url = this.downloadInfo.getUrl() + fileName;
					if (this.downloadInfo.isHDFSFile()) {
						url = String.format("%s?op=OPEN&offset=%d&length=%d", url, startPos[i], endPos[i] - startPos[i]);
					}

					fileItems[i] = new DownloadFile(url, this.downloadInfo.getFilePath() + File.separator + downloadInfo.getFileName(), startPos[i], endPos[i],
							length[i], i);
					fileItems[i].start();// 启动线程，开始下载
					LogUtils.log("Thread: " + i + ", startPos: " + startPos[i] + ", endPos: " + endPos[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 循环写入下载文件长度信息
			Boolean isFinished = false;

			while (!stop && !isFinished) {
				try {
					LogUtils.log("downloading……");
					writePosInfo();
					getSpeed();
					DownloadUtils.fireSteppedEvent(this, downloadInfo, this.getDownloadProcess(), this.getRemainTime());
					isFinished = true;
					Thread.sleep(SLEEP_SECONDS);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				try {
					isFinished = this.isFinished();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 下载完成了删除进度文件,删除任务
				if (isFinished) {
					this.tempFile.delete();
					DownloadUtils.fireSteppedEvent(this, downloadInfo, 100, 0);
					Application.getActiveApplication().getOutput().output(this.downloadInfo + LBSClientProperties.getString("String_DownLoadFinished"));
				}
			}
			LogUtils.info("Download task is finished!");
		}
	}

	private void getSpeed() {
		speed = 0;
		for (int i = 0; i < fileItems.length; i++) {
			speed += fileItems[i].getSpeed();
		}
		speed = speed / fileItems.length;
	}

	/**
	 * 获取文件的下载完成的大小
	 * 
	 */
	public long getFinishedSize() throws IOException {
		long finished = 0;
		try {
			if (fileItems != null) {
				for (int i = 0; i < fileItems.length; i++) {
					if (fileItems[i] != null) {
						finished += fileItems[i].getLength() - (fileItems[i].getEndPos() - fileItems[i].getStartPos());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return finished;
	}

	/**
	 * 获取文件的下载进度
	 * 
	 */
	public int getDownloadProcess() throws IOException {
		int process = 0;
		try {
			long finished = this.getFinishedSize();
			if (this.fileSize != 0) {
				process = (int) (finished * 100 / this.fileSize);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return process;
	}

	/**
	 * 获取文件的剩余下载时间
	 * 
	 */
	public int getRemainTime() throws IOException {
		int remainTime = 0;
		try {
			long finished = this.getFinishedSize();
			remainTime = (int) ((this.fileSize - finished) / (1024 * 500));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return remainTime;
	}

	/**
	 * 获取文件的下载信息
	 * 
	 */
	public String getDownloadInformation() throws IOException {
		String information = "";
		try {
			information = String.format("%s/%s", BatchDownloadFile.convertFileSize(this.getFinishedSize()), BatchDownloadFile.convertFileSize(this.fileSize));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return information;
	}

	public static String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}

	/**
	 * 获取文件的下载进度
	 * 
	 */
	public Boolean isFinished() throws IOException {
		Boolean result = true;
		try {
			if (fileItems != null) {
				for (int i = 0; i < fileItems.length; i++) {
					if (fileItems[i] != null && !fileItems[i].isDownloadOver()) {
						result = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 停止下载
	 * 
	 */
	public void stopDownload() throws IOException {
		this.stop = true;
		for (int i = 0; i < fileItems.length; i++) {
			fileItems[i].stopDownload();
		}
	}

	/**
	 * 继续下载
	 * 
	 */
	public void resumeDownload() {
		try {
			this.stop = false;
			readPosInfo();
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 将写入点数据保存在临时文件中
	 * 
	 */
	private void writePosInfo() throws IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile));
		dos.writeInt(startPos.length);
		for (int i = 0; i < startPos.length; i++) {
			dos.writeLong(fileItems[i].getStartPos());
			dos.writeLong(fileItems[i].getEndPos());
			dos.writeLong(fileItems[i].getLength());
			// LogUtils.info("[" + fileItem[i].getStartPos() + "#" + fileItem[i].getEndPos() + "]");
		}
		dos.close();
	}

	/**
	 * <b>function:</b>读取写入点的位置信息
	 * 
	 */
	private void readPosInfo() throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(tempFile));
		int startPosLength = dis.readInt();
		startPos = new long[startPosLength];
		endPos = new long[startPosLength];
		length = new long[startPosLength];
		for (int i = 0; i < startPosLength; i++) {
			startPos[i] = dis.readLong();
			endPos[i] = dis.readLong();
			length[i] = dis.readLong();
		}
		dis.close();
	}

	/**
	 * <b>function:</b> 获取下载文件的长度
	 * 
	 * @author hoojo
	 * @createDate 2011-9-26 下午12:15:08
	 * @return
	 */
	private int getFileSize() {
		int fileLength = -1;
		try {
			URL url = new URL(this.downloadInfo.getUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			DownloadFile.setHeader(conn);

			int stateCode = conn.getResponseCode();
			// 判断http status是否为HTTP/1.1 206 Partial Content或者200 OK
			if (stateCode != HttpURLConnection.HTTP_OK && stateCode != HttpURLConnection.HTTP_PARTIAL) {
				LogUtils.log("Error Code: " + stateCode);
				return -2;
			} else if (stateCode >= 400) {
				LogUtils.log("Error Code: " + stateCode);
				return -2;
			} else {
				// 获取长度
				fileLength = conn.getContentLength();
				LogUtils.log("FileLength: " + fileLength);
			}

			DownloadFile.printHeader(conn);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileLength;
	}

	public FileInfo getDownloadInfo() {
		return downloadInfo;
	}

	public void setDownloadInfo(FileInfo downloadInfo) {
		this.downloadInfo = downloadInfo;
	}

}