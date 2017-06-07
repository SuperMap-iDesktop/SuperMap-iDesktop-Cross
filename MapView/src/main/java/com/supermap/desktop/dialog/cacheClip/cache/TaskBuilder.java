package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.*;
import com.supermap.data.processing.CacheWriter;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.mapview.MapViewProperties;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Clip task 128*128
 * Created by xie on 2017/5/16.
 */
public class TaskBuilder {

	public final static int BundleSize = 128;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static boolean buildTask(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length < 2) {
			System.out.println("input style: sciFile targetFolder listCount[option]");
			return false;
		}
		String sciFile = args[0];
		String targetFolder = args[1] + "/task";
		int listCount = 1;
		if (args.length > 2) {
			listCount = Integer.valueOf(args[2]);
		}
		// create     0    1    2
		// sci        yes  yes  no
		// udb        no   yes  yes
		int canudb = 0;
		if (args.length > 3) {
			canudb = Integer.valueOf(args[3]);
		}

		File fileSci = new File(sciFile);
		if (!fileSci.exists()) {
			System.out.println(sciFile + " not exist!");
			return false;
		}

		Workspace workspace = null;
		Datasource ds = null;

		if (canudb > 0) {
			String datasourceName = "check";
			String datasourcePath = args[1] + "\\check\\" + datasourceName + ".udb";
			datasourcePath = datasourcePath.replaceAll("/", "\\\\");
			File datasourceFile = new File(datasourcePath);
			if (datasourceFile.exists()) {
				SmOptionPane smOptionPane = new SmOptionPane();
				smOptionPane.showConfirmDialog(MessageFormat.format(MapViewProperties.getString("String_Message_UDBExist"), datasourcePath));
				return false;
			} else {
				datasourceFile.getParentFile().mkdirs();
			}
			DatasourceConnectionInfo info = new DatasourceConnectionInfo();
			info.setServer(datasourcePath);
			info.setAlias(datasourceName);
			info.setEngineType(EngineType.UDB);

			workspace = new Workspace();
			ds = workspace.getDatasources().create(info);
		}

		CacheWriter writer = new CacheWriter();
		writer.FromConfigFile(sciFile);

		// Create task path
		File file = new File(targetFolder);
		if (!file.exists()) {
			file.mkdir();
		}

		String name = targetFolder + "\\allTask.list";
		File allTask = new File(name);
		OutputStreamWriter taskListWriter = null;

		if (!allTask.exists()) {
			allTask.createNewFile();
		}
		taskListWriter = new OutputStreamWriter(new FileOutputStream(allTask), "UTF-8");

		HashMap<Double, String> allScaleCaptions = new HashMap<Double, String>(writer.getCacheScaleCaptions());

		Rectangle2D bounds = writer.getCacheBounds();
		Rectangle2D indexBounds = writer.getIndexBounds();

		Set<Double> scales = allScaleCaptions.keySet();
		ArrayList<Double> scaleList = new ArrayList<Double>();
		scaleList.addAll(scales);

		Collections.sort(scaleList);
		int length = 9;
		for (double scale : scaleList) {
			double tileResolution = writer.getTileResolustion(scale);
			HashMap<Double, String> newScaleCaptions = new HashMap<Double, String>();

			String caption = String.valueOf(Math.round(1 / scale));
			if (caption.length() < length) {
				int addSize = length - caption.length();
				for (int i = 0; i < addSize; i++) {
					caption = "0" + caption;
				}
			}
			//Set caption for new sci file
			newScaleCaptions.put(scale, allScaleCaptions.get(scale));
			writer.setCacheScaleCaptions(newScaleCaptions);

			Recordset recordset = null;
			Recordset.BatchEditor editor = null;
			if (canudb > 0) {
				//Create dataset and it's field
				String datasetName = "L" + caption + "_S" + Math.round(1 / scale);
				DatasetVectorInfo datasetInfo = new DatasetVectorInfo(datasetName, DatasetType.REGION);
				DatasetVector dataset = ds.getDatasets().create(datasetInfo);

				FieldInfo fieldInfoGroup = new FieldInfo("tiletype", FieldType.INT32);
				FieldInfo fieldInfoRow = new FieldInfo("tilerow", FieldType.INT32);
				FieldInfo fieldInfoCol = new FieldInfo("tilecol", FieldType.INT32);
				FieldInfo fieldInfotype = new FieldInfo("errortype", FieldType.INT32);
				FieldInfo fieldInfodesc = new FieldInfo("errordesc", FieldType.TEXT);
				dataset.getFieldInfos().add(fieldInfoGroup);
				dataset.getFieldInfos().add(fieldInfoRow);
				dataset.getFieldInfos().add(fieldInfoCol);
				dataset.getFieldInfos().add(fieldInfotype);
				dataset.getFieldInfos().add(fieldInfodesc);

				recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
				editor = recordset.getBatch();
				editor.setMaxRecordCount(10);
				editor.begin();
			}

			//Calculate a scope,0.004 as one pixel
			double left = (bounds.getLeft() - indexBounds.getLeft()) / tileResolution + 0.00000001;
			int startColumnIndex = (int) Math.floor(left);
			double top = -(bounds.getTop() - indexBounds.getTop()) / tileResolution + 0.00000001;
			int startRowIndex = (int) Math.floor(top);
			double right = (bounds.getRight() - indexBounds.getLeft()) / tileResolution + 0.00000001;
			int endColumnIndex = (int) Math.floor(right);
			if ((right - endColumnIndex) < 0.004) {
				endColumnIndex--;
			}
			double bottom = -(bounds.getBottom() - indexBounds.getTop()) / tileResolution + 0.00000001;
			int endRowIndex = (int) Math.floor(bottom);
			if ((bottom - endRowIndex) < 0.004) {
				endRowIndex--;
			}

			for (int row = startRowIndex; row <= endRowIndex; ) {
				int bigRow = row / 128;
				int currentRowEnd = Math.min(endRowIndex, (bigRow + 1) * 128 - 1);

				for (int col = startColumnIndex; col <= endColumnIndex; ) {
					int bigCol = col / 128;
					int currentColEnd = Math.min(endColumnIndex, (bigCol + 1) * 128 - 1);

					double boundLeft = indexBounds.getLeft() + col * tileResolution;
					if (boundLeft < bounds.getLeft()) {
						boundLeft = bounds.getLeft();
					}
					double boundRight = indexBounds.getLeft() + (currentColEnd + 1) * tileResolution;
					if (boundRight > bounds.getRight()) {
						boundRight = bounds.getRight();
					}
					double boundTop = indexBounds.getTop() - row * tileResolution;
					if (boundTop > bounds.getTop()) {
						boundTop = bounds.getTop();
					}
					double boundBottom = indexBounds.getTop() - (currentRowEnd + 1) * tileResolution;
					if (boundBottom < bounds.getBottom()) {
						boundBottom = bounds.getBottom();
					}

					if (((boundRight - boundLeft) > 0.004) && (Math.abs((boundBottom - boundTop)) > 0.004)) {

						if (canudb < 2) {
							Rectangle2D taskBounds = new Rectangle2D(boundLeft, boundBottom, boundRight, boundTop);
							writer.setCacheBounds(taskBounds);
							// Reset cf file's name as same as sci file's name
							String newSciName = String.format("%s/L%s_R%d_C%d.sci", targetFolder, caption, bigRow, bigCol);
							wirteOneSci(writer, newSciName, taskListWriter);
						}

						if (canudb > 0) {
							// Create solid in dataset as needed
							Point2Ds points = new Point2Ds();
							points.add(new Point2D(boundLeft, boundBottom));
							points.add(new Point2D(boundLeft, boundTop));
							points.add(new Point2D(boundRight, boundTop));
							points.add(new Point2D(boundRight, boundBottom));
							GeoRegion region = new GeoRegion(points);
							HashMap<String, Object> fieldInfo = new HashMap<String, Object>();
							fieldInfo.put("tiletype", 1);
							fieldInfo.put("tilerow", bigRow);
							fieldInfo.put("tilecol", bigCol);

							recordset.addNew(region, fieldInfo);
						}
					}

					col = currentColEnd + 1;
				}
				row = currentRowEnd + 1;
			}

			if (canudb > 0) {
				editor.update();

				recordset.close();
				recordset.dispose();
			}

			//Recovery scale info and bounds info
			writer.setCacheScaleCaptions(allScaleCaptions);
			writer.setCacheBounds(bounds);

		}
		taskListWriter.close();

		if (canudb > 0) {
			ds.close();
			workspace.close();
			workspace.dispose();
		}

		if (canudb < 2) {
			// Ues the equal division method to split allTask.list to several sub list file
			splitList(name, listCount);
		}
		return true;
	}

	public static void splitList(String allListFileName, int splitCount) throws Exception {
		if (splitCount < 2) {
			return;
		}

		File tasks = new File(allListFileName);
		BufferedReader taskReader = new BufferedReader(new InputStreamReader(new FileInputStream(tasks), "UTF-8"));
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		while ((line = taskReader.readLine()) != null) {
			lines.add(line);
		}
		taskReader.close();

		int oneFileCount = lines.size() / splitCount;
		int j = 0;
		String name = tasks.getParent() + "/" + tasks.getName().replaceAll(".list", "");

		for (int i = 0; i < splitCount; i++) {
			//Create .list file to record sci file
			File newOne = new File(name + "_" + i + ".list");
			OutputStreamWriter newOneWriter = new OutputStreamWriter(new FileOutputStream(newOne), "UTF-8");
			if (((lines.size() - j) * 1.0 / oneFileCount) < 2) {
				oneFileCount = lines.size() - j;
			}

			for (int k = 0; k < oneFileCount; k++) {
				newOneWriter.write(lines.get(j) + "\n");
				j++;
			}
			newOneWriter.close();
		}

	}

	public static void wirteOneSci(CacheWriter writer, String sciName, OutputStreamWriter listWriter) throws Exception {
		//Create if sci file exist
		boolean sciTrue = writer.ToConfigFile(sciName);
		if (sciTrue) {
			listWriter.write(new File(sciName).getName() + "\n");
		}
	}
}
