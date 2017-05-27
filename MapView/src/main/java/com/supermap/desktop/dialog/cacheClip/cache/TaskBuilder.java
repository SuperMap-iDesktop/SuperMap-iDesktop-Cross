package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.*;
import com.supermap.data.processing.CacheWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Clip task 128*128
 * Created by xie on 2017/5/16.
 */
public class TaskBuilder {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("input style: sciFile targetFolder listCount[option]");
			return;
		}
		String sciFile = args[0];
		String targetFolder = args[1] + "/task";
		int listCount = 1;
		if (args.length > 2) {
			listCount = Integer.valueOf(args[2]);
		}
		File fileSci = new File(sciFile);
		if (!fileSci.exists()) {
			System.out.println(sciFile + " not exist!");
			return;
		}
		String datasourceName = "check";
		String datasourcePath = args[1] + "/check/" + datasourceName + ".udb";
		File datasourceFile = new File(datasourcePath);
		if (datasourceFile.exists()) {
			System.out.println(datasourcePath + " is exists!");
			return;
		} else {
			datasourceFile.getParentFile().mkdirs();
		}
		DatasourceConnectionInfo info = new DatasourceConnectionInfo();
		info.setServer(datasourcePath);
		info.setAlias(datasourceName);
		info.setEngineType(EngineType.UDB);
		Workspace workspace = new Workspace();
		Datasource ds = workspace.getDatasources().create(info);
		CacheWriter writer = new CacheWriter();
		writer.FromConfigFile(sciFile);
		File file = new File(targetFolder);
		if (!file.exists()) {
			file.mkdir();
		}
		String name = targetFolder + "/allTask.list";
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
			HashMap<Double, String> newScaleCaptions = new HashMap();
			String tempCaption = String.valueOf((int) (1 / scale));
			String caption = "";
			if (tempCaption.length() < length) {
				int addSize = length - tempCaption.length();
				for (int i = 0; i < addSize; i++) {
					caption = "0" + tempCaption;
				}
			}
			//Set caption for new sci file
			newScaleCaptions.put(scale, allScaleCaptions.get(scale));
			writer.setCacheScaleCaptions(newScaleCaptions);
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
			Recordset recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
			Recordset.BatchEditor editor = recordset.getBatch();
			editor.setMaxRecordCount(10);
			editor.begin();
			int startColumnIndex = (int) Math.floor(((bounds.getLeft() - indexBounds.getLeft()) / tileResolution));
			int startRowIndex = (int) Math.floor(-((bounds.getTop() - indexBounds.getTop()) / tileResolution));
			int endColumnIndex = (int) Math.floor(((bounds.getRight() - indexBounds.getLeft()) / tileResolution));
			int endRowIndex = (int) Math.floor(-((bounds.getBottom() - indexBounds.getTop()) / tileResolution));
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
					Rectangle2D taskBounds = new Rectangle2D(boundLeft, boundBottom, boundRight, boundTop);
					writer.setCacheBounds(taskBounds);
					String newSciName = String.format("%s/L%s_R%d_C%d.sci", targetFolder, caption, bigRow, bigCol);
					wirteOneSci(writer, newSciName, taskListWriter);
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
					col = currentColEnd + 1;
				}
				row = currentRowEnd + 1;
			}
			editor.update();
			recordset.close();
			recordset.dispose();
			writer.setCacheScaleCaptions(allScaleCaptions);
			writer.setCacheBounds(bounds);
		}
		taskListWriter.close();
		ds.close();
		workspace.close();
		workspace.dispose();
		splitList(name, listCount);
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
		boolean sciTrue = writer.ToConfigFile(sciName);
		if (sciTrue) {
			listWriter.write(new File(sciName).getName() + "\n");
		}
	}
}
