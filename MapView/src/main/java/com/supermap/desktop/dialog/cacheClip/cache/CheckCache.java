package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.*;
import com.supermap.data.processing.CacheWriter;
import com.supermap.data.processing.CompactFile;
import com.supermap.data.processing.StorageType;
import com.supermap.tilestorage.TileContent;
import com.supermap.tilestorage.TileStorageConnection;
import com.supermap.tilestorage.TileStorageManager;
import com.supermap.tilestorage.TileStorageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/5/27.
 */
public class CheckCache {

	private boolean error2udb;
	private Geometry boundaryRegion;
	private boolean boundaryCheck;
	private String errorFileName;
	private String sciFilePath;
	private BufferedWriter errorWriter = null;
	private int sciLength;
	private static final int INDEX_CACHEROOT = 0;
	private static final int INDEX_SCIPATH = 1;
	//	private static final int INDEX_MERGETASKCOUNT = 2;
	private static final int INDEX_ERROR2UDB = 3;
	private static final int INDEX_BOUNDARYREGION = 4;

	public void startProcess(int processCount, String[] params) {
		try {
//			if (0 == processCount) {
//				main(params);
//			} else {
//				for (int i = 0; i < processCount; i++) {
//					CacheUtilities.startProcess(params, getClass().getName(), LogWriter.CHECK_CACEH);
//					Thread.sleep(2000);
//				}
//			}
			if (processCount > 0) {
				for (int i = 0; i < processCount; i++) {
					CacheUtilities.startProcess(params, getClass().getName(), LogWriter.CHECK_CACEH);
					Thread.sleep(2000);
				}
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

	}

	public static void main(String[] args) {

		if (args.length <= 0) {
			System.out.println("Need params");
		} else {
			if (args.length < 3 || args[0].equalsIgnoreCase("--help")) {
				System.out.println("CacheCheck arguments instructions:");
				System.out.println("cacheroot scipath processcount mergetaskcount mongoconninfo");
				return;
			}
			CheckCache cacheCheck = new CheckCache();
			cacheCheck.checkCache(args);
		}
	}

	private void checkCache(String[] params) {
		String cacheRoot = params[INDEX_CACHEROOT];
		String scipath = params[INDEX_SCIPATH];
//		int mergeTaskCount = 0;
//		if (params.length > 2) {
//			mergeTaskCount = Integer.valueOf(params[INDEX_MERGETASKCOUNT]);
//		}
		if (params.length > 3) {
			this.error2udb = Boolean.valueOf(params[INDEX_ERROR2UDB]);
		}
		if (params.length > 4) {
			this.boundaryRegion = Toolkit.GeoJsonToGemetry(params[INDEX_BOUNDARYREGION]);
		}

		ArrayList<String> sciNames = getSciFileList(scipath);

		if (sciNames.size() == 0) {
			System.out.println("sci file not found!");
			return;
		}
//		if (this.error2udb) {
//			File scifile = new File(sciNames.get(0));
//			String checkPath = CacheUtilities.replacePath(scifile.getParentFile().getParent(), "check");
//			String datasourcePath = CacheUtilities.replacePath(checkPath, "check.udb");
//			File datasourceFile = new File(datasourcePath);
//			if (!datasourceFile.exists()) {
//				System.out.println(datasourcePath + " not exists!");
//				return;
//			}
//		}
		this.boundaryCheck = boundaryRegion != null;
		CacheWriter writer = new CacheWriter();
		writer.FromConfigFile(sciNames.get(0));
//		double anchorLeft = writer.getIndexBounds().getLeft();
//		double anchorTop = writer.getIndexBounds().getTop();
//		int tileSize = writer.getTileSize().value();
		//Get sci files from sci directory
		File sciPath = new File(scipath);
		String parentPath = null;
		if (sciPath.exists()) {
			do {
				//Recalculate sci file length
				String[] sciFileNames = sciPath.list(CacheUtilities.getFilter());
				sciLength = sciFileNames.length;

				File checkingDir = null;
				if (sciLength > 0) {
					File sci = new File(CacheUtilities.replacePath(scipath, sciFileNames[0]));
					checkingDir = new File(CacheUtilities.replacePath(sci.getParentFile().getParent(), "doing"));
					if (!checkingDir.exists()) {
						checkingDir.mkdir();
					}
				}
				CopyOnWriteArrayList<String> doingSciNames = new CopyOnWriteArrayList<>();
				//Now give mergeSciCount sci files to every process if sciLength>mergeSciCount
				int mergeSciCount = 3;
				if (sciLength > mergeSciCount) {
					//First step:Move mergeSciCount sci to doing directory
					int success = 0;
					for (int i = 0; success < mergeSciCount; i++) {
						if (checkingSci(CacheUtilities.replacePath(scipath, sciFileNames[sciLength - 1 - i]), checkingDir, doingSciNames)) {
							success++;
						}
					}

				} else {
					//First step:Move last sci file to doing directory
					for (int i = sciLength - 1; i >= 0; i--) {
						checkingSci(CacheUtilities.replacePath(scipath, sciFileNames[i]), checkingDir, doingSciNames);
					}
				}
				//Second step:get sci file from doing dir and build cache
				for (int i = 0; i < doingSciNames.size(); i++) {
					String sciName = doingSciNames.get(i);
					check(cacheRoot, sciName);
				}
			} while (sciLength != 0);
//			if (this.error2udb && null != parentPath) {
//				this.error2Udb(anchorLeft, anchorTop, tileSize, parentPath);
//			}
		}
	}

	private boolean checkingSci(String sciFileName, File doingDir, CopyOnWriteArrayList<String> doingSciNames) {
		String sciName = sciFileName;
		File sci = new File(sciName);
		boolean renameSuccess = sci.renameTo(new File(doingDir, sci.getName()));
		if (renameSuccess) {
			doingSciNames.add(CacheUtilities.replacePath(doingDir.getAbsolutePath(), sci.getName()));
		}
		return renameSuccess;
	}

	public void check(String cacheRoot, String sciFile) {
		LogWriter log = LogWriter.getInstance(LogWriter.CHECK_CACEH);
		File file = new File(sciFile);
		if (!file.exists()) {
			return;
		}
		sciFilePath = file.getName();
		errorWriter = null;
//		MapCacheFile cacheFile = new MapCacheFile();
//		cacheFile.open(sciFile);
		CacheWriter cacheFile = new CacheWriter();
		cacheFile.FromConfigFile(sciFile);
		String pathSeparator = "\\";
		if (CacheUtilities.isLinux()) {
			pathSeparator = "/";
		}
		cacheRoot = cacheRoot + pathSeparator +
				cacheFile.parseTileFormat() + "_" +
				cacheFile.getTileSize().value() + "_" +
				cacheFile.getHashCode();

		TileStorageManager manager = null;
		if (cacheFile.getStorageType() == StorageType.MongoDB) {
			String[] strInfo = cacheFile.getMongoConnectionInfo();
			if (strInfo == null || strInfo.length < 3) {
				log.writelog("error: mongo connection info!");
				return;
			}
			String server = strInfo[0];
			String database = strInfo[1];
			String cacheName = strInfo[2];

			TileStorageConnection connection = new TileStorageConnection();
			connection.setStorageType(TileStorageType.MONGOV2);
			connection.setServer(server);
			connection.setDatabase(database);
			connection.setName(cacheName);

			manager = new TileStorageManager();
			if (!manager.open(connection)) {
				log.writelog("error: mongo open failed!");
				return;
			}
		}

		boolean result = true;
		for (Double scale : cacheFile.getCacheScaleCaptions().keySet()) {
			String caption = cacheFile.getCacheScaleCaptions().get(scale);
			String errFileName = file.getName().replaceAll(".sci", "");
			errFileName += "(L" + caption + "_S" + Math.round(1 / scale) + ").err";
			errorFileName = file.getParentFile().getParent() + pathSeparator + "temp" + pathSeparator + errFileName;

			double reolustion = getResolution(scale, cacheFile.getPrjCoordSys(), cacheFile.getDPI());

			double left = ((cacheFile.getCacheBounds().getLeft() - cacheFile.getIndexBounds().getLeft()) / reolustion) / cacheFile.getTileSize().value();
			double top = ((cacheFile.getIndexBounds().getTop() - cacheFile.getCacheBounds().getTop()) / reolustion) / cacheFile.getTileSize().value();
			double right = ((cacheFile.getCacheBounds().getRight() - cacheFile.getIndexBounds().getLeft()) / reolustion) / cacheFile.getTileSize().value();
			double bottom = ((cacheFile.getIndexBounds().getTop() - cacheFile.getCacheBounds().getBottom()) / reolustion) / cacheFile.getTileSize().value();

			int tileLeft = (int) left;
			int tileTop = (int) top;

			int tileRight = (int) right;
			if ((right - tileRight) < 0.0001) {
				tileRight--;
			}
			int tileBottom = (int) bottom;
			if ((bottom - tileBottom) < 0.0001) {
				tileBottom--;
			}

			if (cacheFile.getStorageType() == StorageType.Original) {

			} else if (cacheFile.getStorageType() == StorageType.Compact) {
				result = result && checkCompactCache(log, cacheRoot, caption, tileLeft, tileTop, tileRight, tileBottom, reolustion, cacheFile);
			} else if (cacheFile.getStorageType() == StorageType.MongoDB) {
				result = result && checkMongoCache(log, manager, Integer.valueOf(caption), tileLeft, tileTop, tileRight, tileBottom, reolustion, cacheFile);
			}

			if (errorWriter != null) {
				try {
					errorWriter.flush();
					errorWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				errorWriter = null;
			}
		}

		File filesci = new File(sciFile);
		String checkDir = (CacheUtilities.replacePath(filesci.getParentFile().getParent(), "checked"));
		if (!result) {
			checkDir = (CacheUtilities.replacePath(filesci.getParentFile().getParent(), "Task"));
		}
		filesci.renameTo(new File(checkDir, filesci.getName()));

		if (manager != null) {
			manager.close();
		}
	}

	//Check compact cache
	public boolean checkCompactCache(LogWriter log, String cacheRoot, String caption, int left, int top, int right, int bottom, double reolustion, CacheWriter cacheFile) {

		boolean isWithin = false;
		if (boundaryCheck) {
			Rectangle2D cacheBounds = cacheFile.getCacheBounds();
			Point2Ds points = new Point2Ds();
			points.add(new Point2D(cacheBounds.getLeft(), cacheBounds.getBottom()));
			points.add(new Point2D(cacheBounds.getLeft(), cacheBounds.getTop()));
			points.add(new Point2D(cacheBounds.getRight(), cacheBounds.getTop()));
			points.add(new Point2D(cacheBounds.getRight(), cacheBounds.getBottom()));
			GeoRegion region = new GeoRegion(points);

			isWithin = Geometrist.isWithin(region, boundaryRegion);
		}
		double anchorLeft = cacheFile.getIndexBounds().getLeft();
		double anchorTop = cacheFile.getIndexBounds().getTop();
		int tileSize = cacheFile.getTileSize().value();

		boolean result = true;
		for (int row = top; row <= bottom; ) {
			int bigRow = row / 128;
			int currentRow = Math.min(bottom, (bigRow + 1) * 128 - 1);

			for (int col = left; col <= right; ) {
				int bigCol = col / 128;
				int currentCol = Math.min(right, (bigCol + 1) * 128 - 1);

				String cfPath = "";
				if (CacheUtilities.isWindows()) {
					cfPath = cacheRoot + "\\" + caption + "\\" + bigRow + "\\" + bigCol + ".cf";
				} else {
					cfPath = cacheRoot + "/" + caption + "/" + bigRow + "/" + bigCol + ".cf";
				}
				File cfFile = new File(cfPath);

				//long starttime = System.nanoTime();

				if (!cfFile.exists()) {
					result = false;
					log.writelog("lost:" + cfPath);
					this.writError("lost," + reolustion + "," + bigRow + "," + bigCol + "," + sciFilePath);
				} else {
					CompactFile compactFile = new CompactFile();
					if (compactFile.Open(cfPath, "") != 0) {
						result = false;
						log.writelog("failure:" + cfPath);
						this.writError("failure," + reolustion + "," + bigRow + "," + bigCol + "," + sciFilePath);
					} else {
						byte[] data = null;
						for (int tempRow = row; tempRow <= currentRow; tempRow++) {
							for (int tempCol = col; tempCol <= currentCol; tempCol++) {

								data = compactFile.getAt(tempRow % 128, tempCol % 128);
								if (data == null || data.length == 0) {
									result = false;
									log.writelog("missing:" + caption + "," + tempRow + "," + tempCol + "," + sciFilePath);
									this.writError("missing," + reolustion + "," + tempRow + "," + tempCol + "," + sciFilePath);
								} else if (data.length > 16) {
									try {
										ByteArrayInputStream byteStream = new ByteArrayInputStream(data, 12, data.length - 12);
										//MemoryCacheImageInputStream tileStream = new MemoryCacheImageInputStream(byteStream);
										BufferedImage image = null;
										try {
											image = ImageIO.read(byteStream);
										} catch (IOException e) {
											result = false;
											log.writelog("error:" + caption + "," + tempRow + "," + tempCol + "," + sciFilePath);
											this.writError("error," + reolustion + "," + tempRow + "," + tempCol + "," + sciFilePath);
											byteStream.close();
											continue;
										}

										boolean tileIsWithin = isWithin;
										if (boundaryCheck && !isWithin) {

											double boundLeft = anchorLeft + reolustion * col * tileSize;
											double boundRight = boundLeft + reolustion * tileSize;
											double boundTop = anchorTop - reolustion * row * tileSize;
											double boundBottom = boundTop - reolustion * tileSize;

											Point2Ds points = new Point2Ds();
											points.add(new Point2D(boundLeft, boundBottom));
											points.add(new Point2D(boundLeft, boundTop));
											points.add(new Point2D(boundRight, boundTop));
											points.add(new Point2D(boundRight, boundBottom));
											GeoRegion tileRegion = new GeoRegion(points);

											tileIsWithin = Geometrist.isWithin(tileRegion, boundaryRegion);
										}

										if (boundaryCheck) {
											if (tileIsWithin && isBlockWhite(image)) {
												result = false;
												log.writelog("white:" + caption + "," + tempRow + "," + tempCol + "," + sciFilePath);
												this.writError("white," + reolustion + "," + tempRow + "," + tempCol + "," + sciFilePath);
											}
										} else {
											if (isSolidWhite(image)) {
												result = false;
												log.writelog("white:" + caption + "," + tempRow + "," + tempCol + "," + sciFilePath);
												this.writError("white," + reolustion + "," + tempRow + "," + tempCol + "," + sciFilePath);
											}
										}

										byteStream.close();
									} catch (IOException e) {
									}
								}
							}
						}
						compactFile.Close();
					}
				}

				log.flush();

				col = currentCol + 1;
			}
			row = currentRow + 1;
		}
		return result;
	}

	//Get resolution of scale
	public double getResolution(double scale, PrjCoordSys prjCoordSys, double cacheDPI) {
		double unitRatio = 0; //unit:0.1mm/(prj unit)
		if (prjCoordSys.getCoordUnit().equals(Unit.DEGREE)) {
			unitRatio = prjCoordSys.getGeoCoordSys().getGeoDatum()
					.getGeoSpheroid().getAxis()
					* (prjCoordSys.getCoordUnit().value() - 1000000000)
					* 10000
					* 3.1415926535897932384626433833
					/ (180.0 * 1745329);
		} else {
			unitRatio = (double) prjCoordSys.getCoordUnit().value();
		}

		double resolution = 1 / (scale * cacheDPI / 254); //unit:0.1mm/pixel
		resolution = resolution / unitRatio; //unit:(pry unit)/pixel
		return resolution;
	}

	//Check mongo type cache
	public boolean checkMongoCache(LogWriter log, TileStorageManager manager, int level, int left, int top, int right, int bottom, double reolustion, CacheWriter cacheFile) {

		boolean isWithin = false;
		if (boundaryCheck) {
			Rectangle2D cacheBounds = cacheFile.getCacheBounds();
			Point2Ds points = new Point2Ds();
			points.add(new Point2D(cacheBounds.getLeft(), cacheBounds.getBottom()));
			points.add(new Point2D(cacheBounds.getLeft(), cacheBounds.getTop()));
			points.add(new Point2D(cacheBounds.getRight(), cacheBounds.getTop()));
			points.add(new Point2D(cacheBounds.getRight(), cacheBounds.getBottom()));
			GeoRegion region = new GeoRegion(points);

			isWithin = Geometrist.isWithin(region, boundaryRegion);
		}
		double anchorLeft = cacheFile.getIndexBounds().getLeft();
		double anchorTop = cacheFile.getIndexBounds().getTop();
		int tileSize = cacheFile.getTileSize().value();

		boolean result = true;
		byte[] data = null;
		TileContent content = null;
		for (int row = top; row <= bottom; row++) {
			for (int col = left; col <= right; col++) {
				//TileContentInfo info = new TileContentInfo(level,row,col,"");
				//content = manager.loadTile(info);
				content = manager.loadTile(level - 1, row, col);
				data = null;
				if (content != null) {
					data = content.getData();
					String key = content.getKey();
					content.dispose();
					if (!key.isEmpty()) {
						continue;
					}
				}

				if (data == null || data.length == 0) {
					result = false;
					log.writelog("missing:" + level + "," + row + "," + col + "," + sciFilePath);
					this.writError("missing," + reolustion + "," + row + "," + col + "," + sciFilePath);
				} else if (data.length > 16) {
					try {
						ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
						//MemoryCacheImageInputStream tileStream = new MemoryCacheImageInputStream(byteStream);
						BufferedImage image = null;
						try {
							image = ImageIO.read(byteStream);
						} catch (IOException e) {
							result = false;
							log.writelog("error:" + level + "," + row + "," + col + "," + sciFilePath);
							this.writError("error," + reolustion + "," + row + "," + col + "," + sciFilePath);
							byteStream.close();
							continue;
						}

						boolean tileIsWithin = isWithin;
						if (boundaryCheck && !isWithin) {

							double boundLeft = anchorLeft + reolustion * col * tileSize;
							double boundRight = boundLeft + reolustion * tileSize;
							double boundTop = anchorTop - reolustion * row * tileSize;
							double boundBottom = boundTop - reolustion * tileSize;

							Point2Ds points = new Point2Ds();
							points.add(new Point2D(boundLeft, boundBottom));
							points.add(new Point2D(boundLeft, boundTop));
							points.add(new Point2D(boundRight, boundTop));
							points.add(new Point2D(boundRight, boundBottom));
							GeoRegion tileRegion = new GeoRegion(points);

							tileIsWithin = Geometrist.isWithin(tileRegion, boundaryRegion);
						}

						if (boundaryCheck) {
							if (tileIsWithin && isBlockWhite(image)) {
								result = false;
								log.writelog("white:" + level + "," + row + "," + col + "," + sciFilePath);
								this.writError("white," + reolustion + "," + row + "," + col + "," + sciFilePath);
							}
						} else {
							if (isSolidWhite(image)) {
								result = false;
								log.writelog("white:" + level + "," + row + "," + col + "," + sciFilePath);
								this.writError("white," + reolustion + "," + row + "," + col + "," + sciFilePath);
							}
						}

						byteStream.close();
					} catch (IOException e) {
					}
				} else {
					//System.out.println(row + " " + col);
				}
			}
		}
		return result;
	}


	public boolean isSolidWhite(BufferedImage image) {
		byte[] data = ((DataBufferByte) image.getData().getDataBuffer()).getData();
		byte value = -1;
		for (int i = 0; i < data.length; i++) {
			if (data[i] != value) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate image's block is white
	 *
	 * @param image
	 * @return
	 */
	public boolean isBlockWhite(BufferedImage image) {
		byte[] data = ((DataBufferByte) image.getData().getDataBuffer()).getData();
		byte value = -1;
		int whiteStart = 0, index = 0, whiteCount = 0, h, w;
		int widthByte = data.length / image.getHeight();
		for (h = 0; h < image.getHeight(); h++) {
			index = h * widthByte;
			for (w = 0; w < widthByte; w++) {
				if (data[index + w] == value) {
					if (whiteStart == -1) {
						whiteStart = w;
					} else if ((w - whiteStart) > 128) {
						whiteCount++;
						break;
					}
				} else {
					whiteStart = -1;
				}
			}
			if (whiteCount > 10) {
				return true;
			}
		}
		return false;
	}


	private void writError(String error) {

		if (!error2udb) {
			return;
		}

		if (errorWriter == null) {
			try {
				File file = new File(errorFileName);
				if (file.exists()) {
					file.delete();
				} else {
					file.getParentFile().mkdirs();
				}
				errorWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorFileName), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			errorWriter.write(error);
			errorWriter.newLine();
			errorWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getSciFileList(String scipath) {
		String checkDir = "";
		ArrayList<String> sciNames = new ArrayList<String>();
		if (scipath.endsWith(".list")) {
			File tasksFile = new File(scipath);
			String taskDir = tasksFile.getParent();
			BufferedReader taskReader;
			try {
				taskReader = new BufferedReader(new InputStreamReader(new FileInputStream(tasksFile), "UTF-8"));

				String line;
				while ((line = taskReader.readLine()) != null) {
					sciNames.add(CacheUtilities.replacePath(taskDir, line));
				}
				taskReader.close();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			checkDir = CacheUtilities.replacePath(tasksFile.getParentFile().getParent(), "checked");
		} else if (scipath.contains("&")) {
			String[] sciFileArray = scipath.split("&");
			for (String onesci : sciFileArray) {
				sciNames.add(onesci);
			}

			if (sciNames.size() > 0) {
				File sciFile = new File(sciNames.get(0));
				checkDir = CacheUtilities.replacePath(sciFile.getParentFile().getParent(), "checked");
			}
		} else if (scipath.endsWith(".sci")) {
			sciNames.add(scipath);

			File sciFile = new File(scipath);
			checkDir = CacheUtilities.replacePath(sciFile.getParentFile().getParent(), "checked");
		} else {
			File sciFile = new File(scipath);
			if (sciFile.isDirectory()) {
				String[] sciFiles = sciFile.list(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return name.endsWith(".sci");
					}
				});
				for (String name : sciFiles) {
					sciNames.add(CacheUtilities.replacePath(scipath, name));
				}

				checkDir = CacheUtilities.replacePath(sciFile.getParent(), "checked");
			}
		}

		File scicheck = new File(checkDir);
		if (!scicheck.exists()) {
			scicheck.mkdir();
		}
		File scierror = new File(CacheUtilities.replacePath(scicheck.getParent(), "Task"));
		if (!scierror.exists()) {
			scierror.mkdir();
		}

		return sciNames;
	}

	public void error2Udb(double anchorLeft, double anchorTop, int tileSize, String parentName) {
		String tempPath = CacheUtilities.replacePath(parentName, "temp");
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			return;
		}
		String[] errorNames = tempFile.list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".err");
			}
		});

		if (errorNames.length <= 0) {
			System.out.println("no error file!");
			return;
		}
		String checkPath = CacheUtilities.replacePath(parentName, "check");
		String udbPath = CacheUtilities.replacePath(checkPath, "check.udb");
		tempFile = new File(udbPath);
		if (!tempFile.exists()) {
			System.out.println("not found cache.udb file!");
			return;
		}
		DatasourceConnectionInfo info = new DatasourceConnectionInfo();
		info.setServer(udbPath);
		info.setAlias("check");
		info.setEngineType(EngineType.UDB);

		Workspace workspace = new Workspace();
		Datasource ds = workspace.getDatasources().open(info);

		for (String name : errorNames) {

			int indexFirst = name.indexOf("_");
			int indexSecond = name.indexOf("_", indexFirst + 1);
			int indexThreed = name.indexOf("(");
			int indexFour = name.indexOf(")");
			String caption = name.substring(1, indexFirst);
			int bigRow = Integer.valueOf(name.substring(indexFirst + 2, indexSecond));
			int bigCol = Integer.valueOf(name.substring(indexSecond + 2, indexThreed));
			String datasetName = name.substring(indexThreed + 1, indexFour);

			ArrayList<String> addLines = new ArrayList<String>();
			ArrayList<String> modifyLines = new ArrayList<String>();

			File file = new File(CacheUtilities.replacePath(tempPath, name));
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

				String readline;
				while ((readline = reader.readLine()) != null) {
					if (readline.startsWith("lost") || readline.startsWith("failure")) {
						modifyLines.add(readline);
					} else {
						addLines.add(readline);
					}
				}

				reader.close();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			DatasetVector dataset = (DatasetVector) ds.getDatasets().get(datasetName);

			for (String line : modifyLines) {
				String[] elements = line.split(",");
				if (elements.length <= 0) {
					continue;
				}
				//double reolustion = Double.valueOf(elements[1]);
				int row = Integer.valueOf(elements[2]);
				int col = Integer.valueOf(elements[3]);
				int errortype = elements[0].startsWith("lo") ? 3 : 4;

				String queryStr = "tiletype = 1 and tilerow = " + row + " and tilecol = " + col;
				Recordset recordset = dataset.query(queryStr, CursorType.DYNAMIC);
				recordset.edit();
				recordset.setFieldValue("errortype", errortype);
				recordset.setFieldValue("errordesc", elements[0]);
				recordset.update();
				recordset.close();
				recordset.dispose();
			}

			if (addLines.size() > 0) {
				Recordset recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
				recordset.getBatch().setMaxRecordCount(10);
				recordset.getBatch().begin();

				for (String line : addLines) {
					String[] elements = line.split(",");
					if (elements.length <= 0) {
						continue;
					}
					double reolustion = Double.valueOf(elements[1]);
					int row = Integer.valueOf(elements[2]);
					int col = Integer.valueOf(elements[3]);
					int errortype = 5;
					if (elements[0].startsWith("m")) {
						errortype = 1;
					} else if (elements[0].startsWith("w")) {
						errortype = 2;
					}

					double boundLeft = anchorLeft + reolustion * col * tileSize;
					double boundRight = boundLeft + reolustion * tileSize;
					double boundTop = anchorTop - reolustion * row * tileSize;
					double boundBottom = boundTop - reolustion * tileSize;

					HashMap<String, Object> fieldInfo = new HashMap<String, Object>();
					fieldInfo.put("tiletype", 2);
					fieldInfo.put("tilerow", row);
					fieldInfo.put("tilecol", col);
					fieldInfo.put("errortype", errortype);
					fieldInfo.put("errordesc", elements[0]);

					//Add recordset to dataset
					Point2Ds points = new Point2Ds();
					points.add(new Point2D(boundLeft, boundBottom));
					points.add(new Point2D(boundLeft, boundTop));
					points.add(new Point2D(boundRight, boundTop));
					points.add(new Point2D(boundRight, boundBottom));
					GeoRegion region = new GeoRegion(points);

					recordset.addNew(region, fieldInfo);
				}

				recordset.getBatch().update();
				recordset.close();
				recordset.dispose();
			}
			file.delete();
		}

		ds.close();
		workspace.close();
		workspace.dispose();
	}
}
