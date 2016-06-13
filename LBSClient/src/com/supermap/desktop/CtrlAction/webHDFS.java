package com.supermap.desktop.CtrlAction;

import javax.swing.SwingUtilities;

public class webHDFS {
	
	public static String webURL = "http://192.168.12.103:50070/webhdfs/v1/data/";
	public static String webFile = "mobile10w.csv"; // mobile0426095637.csv"
	
	public HDFSDefine getHDFSDefine(String permission, String owner, String group, String size, String replication,
			String blockSize, String name, Boolean isDir) {
		
		HDFSDefine define = new HDFSDefine(permission, owner, group, size, replication,
				blockSize, name, isDir);
		return define;
	}
	
	public static String getHDFSFileURL() {
		String serverPath = webURL;
//		serverPath = serverPath.replace("webhdfs/v1/", "");
//		serverPath = serverPath.replace("http", "hdfs");
		serverPath += webFile;
		return serverPath;
	}
	
	public static String getHDFSFilePath() {
		String serverPath = webURL;
		serverPath = serverPath.replace("webhdfs/v1/", "");
		serverPath = serverPath.replace("http", "hdfs");
		serverPath += webFile;
		return serverPath;
	}
	
	public static String getFileList(String urlPath) {
		return HttpRequest.getHttpString(urlPath, "op=LISTSTATUS");
	}
	
	private static Boolean getFileResult = false;
	public static Boolean getFile(String urlPath, String localPath) {
		getFileResult = false;
		// 需要实现断点续传
		// 获取文件总大小，计算给出进度
		// 提供界面，给出默认名字，允许用户改名字
		getFileResult = HttpRequest.saveFileToDisk(urlPath, "op=OPEN&offset=0&length=1024", localPath);		
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {		
			}
		});
		return getFileResult;
	}

	/**
	 * describe a HDFS file
	 *
	 * @author huchenpu
	 */
 	public class HDFSDefine {
		private String fullPath = "";
		String permission = "", owner = "", group = "", length = "", replication = "", blockSize = "", pathSuffix = "";
		Boolean isDir = false;

		public HDFSDefine(String permission, String owner, String group, String size, String replication,
				String blockSize, String name, Boolean isDir) {
			this.permission = permission;
			this.owner = owner;
			this.group = group;
			this.length = size;
			this.replication = replication;
			this.blockSize = blockSize;
			this.pathSuffix = name;
			this.isDir = isDir;
		}
		
		public String getPermission() {
			return permission;
		}

		public void setPermission(String permission) {
			this.permission = permission;
		}	
		
		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}	
		
		public String getGroup() {
			return this.group;
		}

		public void setGroup(String group) {
			this.group = group;
		}	
		
		public String getSize() {
			return this.length;
		}

		public void setSize(String length) {
			this.length = length;
		}	
		
		public String getReplication() {
			return this.replication;
		}

		public void setReplication(String replication) {
			this.replication = replication;
		}	
		
		public String getBlockSize() {
			return this.blockSize;
		}

		public void setBlockSize(String blockSize) {
			this.blockSize = blockSize;
		}	
		
		public String getName() {
			return this.pathSuffix;
		}

		public void setName(String pathSuffix) {
			this.pathSuffix = pathSuffix;
		}	
		
		public Boolean isDir() {
			return this.isDir;
		}

		public void setIsDir(Boolean isDir) {
			this.isDir = isDir;
		}	

		public String getFullPath() {
			return fullPath;
		}

		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}		

		@Override
		public String toString() {
			return this.fullPath;
		}
	}
}
