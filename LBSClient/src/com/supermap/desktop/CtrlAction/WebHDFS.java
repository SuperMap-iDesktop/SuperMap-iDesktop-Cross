package com.supermap.desktop.CtrlAction;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.supermap.desktop.Application;
import com.supermap.desktop.http.HttpRequest;

public class WebHDFS {

	public static String webURL = "http://192.168.14.1:50070/webhdfs/v1/data/NY_trip_data/";
	public static String webFile = "trip_data_1.csv";
	public static String outputURL = "http://192.168.14.1:50070/webhdfs/v1/output/";

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
		serverPath = serverPath.replace("http", "hdfs");
		serverPath = serverPath.replace("50070/webhdfs/v1", "9000");
		serverPath += webFile;
		return serverPath;
	}

	public static String getHDFSOutputDirectry() {
		String directry = outputURL;
		directry = directry.replace("50070/webhdfs/v1", "9000");
		directry = directry.replace("http", "hdfs");
		if (!directry.endsWith("/")) {
			directry += "/";
		}
		return directry;
	}

	public static String urlToHDFS(String url) {
		String hdfs = url.replace("50070/webhdfs/v1", "9000");
		hdfs = hdfs.replace("http", "hdfs");
		return hdfs;
	}

	public static String hdfsToURL(String hdfs) {
		String url = hdfs.replace("9000", "50070/webhdfs/v1");
		url = url.replace("hdfs://", "http://");
		return url;
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

	public static HDFSDefine getFileStatus(String urlPath) {
		HDFSDefine define = null;

		String result = HttpRequest.getHttpString(urlPath, "op=GETFILESTATUS");
		String[] temps = result.split("\"|,|:|\\[|\\]|\\{|\\}|\\\r|\\\n");
		ArrayList<String> results = new ArrayList<String>();
		for (String temp : temps) {
			if (!temp.trim().equals("")) {
				results.add(temp.trim());
			}
		}

		int itemsCount = 26;
		// 开始：最前面一个节点 {"FileStatus":[
		// 结尾：]}}
		results.remove(0);
		// "accessTime":0,"blockSize":0,"childrenNum":24,"fileId":16386,"group":"supergroup","length":0,"modificationTime":1461949836347,
		// "owner":"root","pathSuffix":"data","permission":"755","replication":0,"storagePolicy":0,"type":"DIRECTORY"
		String permission = "", owner = "", group = "", length = "", replication = "", blockSize = "", pathSuffix = "", type = "";
		for (int i = 0; i < results.size(); i += itemsCount) {
			for (int j = 0; j < itemsCount; j += 2) {
				if (results.get(i + j).equalsIgnoreCase("permission")) {
					permission = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("owner")) {
					owner = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("group")) {
					group = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("length")) {
					length = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("replication")) {
					replication = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("blockSize")) {
					blockSize = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("pathSuffix")) {
					pathSuffix = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("type")) {
					type = results.get(i + j + 1);
				}
			}

			Boolean isDir = false;
			if (type.equalsIgnoreCase("DIRECTORY")) {
				isDir = true;
			}

			define = (new WebHDFS()).getHDFSDefine(permission, owner, group, length, replication, blockSize, pathSuffix, isDir);
		}

		return define;
	}

	public static HDFSDefine[] listDirectory(String urlPath, String childFolder, Boolean isFolderOnly) {
		ArrayList<HDFSDefine> defines = new ArrayList<HDFSDefine>();
		try {
			int itemsCount = 26;
			if (!urlPath.endsWith("/")) {
				urlPath += "/";
			}

			if (!"".equals(childFolder)) {
				if (!childFolder.endsWith("/")) {
					childFolder += "/";
				}

				if (childFolder.startsWith("/")) {
					childFolder.substring(1, childFolder.length() - 1);
				}

				urlPath += childFolder;
			}

			String result = WebHDFS.getFileList(urlPath);
			String[] temps = result.split("\"|,|:|\\[|\\]|\\{|\\}|\\\r|\\\n");
			ArrayList<String> results = new ArrayList<String>();
			for (String temp : temps) {
				if (!temp.trim().equals("")) {
					results.add(temp.trim());
				}
			}

			// 开始：最前面两个节点 {"FileStatuses":{"FileStatus":[
			// 结尾：]}}
			results.remove(1);
			results.remove(0);
			// "accessTime":0,"blockSize":0,"childrenNum":24,"fileId":16386,"group":"supergroup","length":0,"modificationTime":1461949836347,
			// "owner":"root","pathSuffix":"data","permission":"755","replication":0,"storagePolicy":0,"type":"DIRECTORY"
			String permission = "", owner = "", group = "", length = "", replication = "", blockSize = "", pathSuffix = "", type = "";
			for (int i = 0; i < results.size(); i += itemsCount) {
				for (int j = 0; j < itemsCount; j += 2) {
					if (results.get(i + j).equalsIgnoreCase("permission")) {
						permission = results.get(i + j + 1);
					} else if (results.get(i + j).equalsIgnoreCase("owner")) {
						owner = results.get(i + j + 1);
					} else if (results.get(i + j).equalsIgnoreCase("group")) {
						group = results.get(i + j + 1);
					} else if (results.get(i + j).equalsIgnoreCase("length")) {
						length = results.get(i + j + 1);
					} else if (results.get(i + j).equalsIgnoreCase("replication")) {
						replication = results.get(i + j + 1);
					} else if (results.get(i + j).equalsIgnoreCase("blockSize")) {
						blockSize = results.get(i + j + 1);
					} else if (results.get(i + j).equalsIgnoreCase("pathSuffix")) {
						pathSuffix = results.get(i + j + 1);
					} else if (results.get(i + j).equalsIgnoreCase("type")) {
						type = results.get(i + j + 1);
					}
				}

				Boolean isDir = false;
				if (type.equalsIgnoreCase("DIRECTORY")) {
					isDir = true;
				}

				if (!isFolderOnly || (isFolderOnly && isDir) ) {
					HDFSDefine hdfsDefine = (new WebHDFS()).getHDFSDefine(permission, owner, group, length, replication, blockSize, pathSuffix, isDir);
					defines.add(hdfsDefine);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return defines.toArray(new HDFSDefine[defines.size()]);
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
