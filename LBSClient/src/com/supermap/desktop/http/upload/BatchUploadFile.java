package com.supermap.desktop.http.upload;

import java.io.IOException;
import java.util.Random;

import com.supermap.desktop.Application;
import com.supermap.desktop.http.CreateFile;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.utilities.CommonUtilities;

/**
 * <b>function:</b> 分批量下载文件
 * 
 * @author hoojo
 * @createDate 2011-9-22 下午05:51:54
 * @file BatchDownloadFile.java
 * @package com.hoo.download
 * @project MultiThreadDownLoad
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class BatchUploadFile extends Thread {
	// 下载文件信息
	private FileInfo downloadInfo;
	// 子线程下载
	// private UploadFile[] fileItems;
	// 文件长度
	// 是否第一个文件
	private boolean first = true;

	// 是否停止下载

	public BatchUploadFile(FileInfo downloadInfo) {
		this.downloadInfo = downloadInfo;
	}

	@Override
	public void run() {
		// 首次上传，创建文件
			CreateFile createFile = null;
			// 创建指定个数单线程下载对象，每个线程独立完成指定块内容的下载
			if (this.downloadInfo.isHDFSFile()) {
				try {
					createFile = new CreateFile(this.downloadInfo);
					createFile.start();
					boolean isCreated = false;
					while (!isCreated&&!createFile.isFailed()) {
						Thread.sleep(1000);
						UploadUtils.fireSteppedEvent(this, downloadInfo, getRandomProgress(), 0);
						isCreated = createFile.isCreated();
						// 上传失败或上传结束
						if (isCreated||createFile.isFailed()) {
							UploadUtils.fireSteppedEvent(this, downloadInfo, 100, 0);
						}
						
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Step2：用要被写入的文件数据，提交另一个HTTP PUT请求到上边返回的Header中的location的URL。
		}

	}

	private int getRandomProgress() {
		Random random = new Random(System.currentTimeMillis());
		return random.nextInt(100);
	}

}