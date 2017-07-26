package com.supermap.desktop.implement.UserDefineType;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfos;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/3/28.
 * Parse gps file
 */
public class GPXAnalytic {
	private static final int GPX_ITEM = 0;

	public static boolean isGPXType(DatasetVector dataset) {
		FieldInfos infos = dataset.getFieldInfos();
		int size = infos.getCount();
		boolean hasEle = false;
		boolean hasTime = false;
		boolean hasLon = false;
		boolean hasLat = false;
		for (int i = 0; i < size; i++) {
			if ("SmX".equals(infos.get(i).getName())) {
				hasLon = true;
			}
			if ("SmY".equals(infos.get(i).getName())) {
				hasLat = true;
			}
			if ("ele".equals(infos.get(i).getName())) {
				hasEle = true;
			}
			if ("time".equals(infos.get(i).getName())) {
				hasTime = true;
			}
		}
		return hasLon && hasLat && hasEle && hasTime;
	}

	/**
	 * Parse gps file to GPXBean list
	 *
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public List<GPXBean> parseXml(String filepath) throws IOException {
		List<GPXBean> result = null;
		File file = new File(filepath);
		if (file.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(file);
				if (null != document) {
					result = new CopyOnWriteArrayList<>();
					Node gpxNode = document.getChildNodes().item(GPX_ITEM);
					Node trkNode = null;
					int gpxNodeSize = gpxNode.getChildNodes().getLength();
					for (int i = 0; i < gpxNodeSize; i++) {
						if ("trk".equals(gpxNode.getChildNodes().item(i).getNodeName())) {
							trkNode = gpxNode.getChildNodes().item(i);
							break;
						}
					}
					if (null != trkNode) {
						Node trksegNode = null;
						int trkNodeSize = trkNode.getChildNodes().getLength();
						for (int i = 0; i < trkNodeSize; i++) {
							if ("trkseg".equals(trkNode.getChildNodes().item(i).getNodeName())) {
								trksegNode = trkNode.getChildNodes().item(i);
								break;
							}
						}
						if (null != trksegNode) {
							int trksegNodeSize = trksegNode.getChildNodes().getLength();
							GPXBean bean = null;
							for (int i = 0; i < trksegNodeSize; i++) {
								if ("trkpt".equals(trksegNode.getChildNodes().item(i).getNodeName())) {
									Node trkptNode = trksegNode.getChildNodes().item(i);
									bean = new GPXBean();
									NamedNodeMap trrs = trkptNode.getAttributes();
									if (null != trrs && trrs.getLength() > 0) {
										bean.setLat(Double.parseDouble(trrs.getNamedItem("lat").getNodeValue()));
										bean.setLon(Double.parseDouble(trrs.getNamedItem("lon").getNodeValue()));
									}
									for (int j = 0; j < trkptNode.getChildNodes().getLength(); j++) {
										if ("ele".equals(trkptNode.getChildNodes().item(j).getNodeName())) {
											bean.setEle(Float.parseFloat(trkptNode.getChildNodes().item(j).getTextContent()));
										}
										if ("time".equals(trkptNode.getChildNodes().item(j).getNodeName())) {
											bean.setTime(trkptNode.getChildNodes().item(j).getTextContent());
										}
									}
									if (null != bean) {
										result.add(bean);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {

			}
		}
		return result;
	}

}
