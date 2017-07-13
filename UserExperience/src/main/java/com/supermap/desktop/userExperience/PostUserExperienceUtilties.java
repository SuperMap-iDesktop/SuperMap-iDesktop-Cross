package com.supermap.desktop.userExperience;

import com.alibaba.fastjson.JSONArray;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.desktop.utilities.NetworkUtilties;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by SillyB on 2017/7/12.
 */
public class PostUserExperienceUtilties {
	private static final String dataItemPostLogsUrl = "http://log.supermap.com/v1/ilog/services/data/es/1";
	private static final String postLogsIP = "log.supermap.com";


	public static boolean postFile(FileLocker fileLocker) {
		if (NetworkUtilties.ping(postLogsIP)) {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			try {
				HttpPost httpPost = new HttpPost(dataItemPostLogsUrl);

				String ip = NetworkUtilties.getIpAddress();
				String macAddress = NetworkUtilties.getMacAddress();
				RandomAccessFile randomAccessFile = fileLocker.getRandomAccessFile();

				if (randomAccessFile.length() > 0) {
					randomAccessFile.seek(0);
					byte[] bytes = new byte[((int) randomAccessFile.length())];
					randomAccessFile.read(bytes);
					String value = new String(bytes, "UTF-8");
					String[] lines = value.split(System.getProperty("line.separator"));
					JSONArray array = new JSONArray();
					for (String line : lines) {
						line = line.replace("{IP}", ip);
						line = line.replace("{MACADDRESS}", macAddress);
						array.add(line);
						if (array.size() == 50) {
							StringEntity stringEntity = new StringEntity(array.toJSONString(), ContentType.APPLICATION_JSON);
							httpPost.setEntity(stringEntity);
							CloseableHttpResponse response = httpClient.execute(httpPost);
							array.clear();
						}
					}
					if (array.size() != 0) {
						StringEntity stringEntity = new StringEntity(array.toJSONString(), ContentType.APPLICATION_JSON);
						httpPost.setEntity(stringEntity);
						CloseableHttpResponse response = httpClient.execute(httpPost);
					}
				}
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
				return false;
			} finally {
				try {
					httpClient.close();
				} catch (IOException e) {
					Application.getActiveApplication().getOutput().output(e);
				}
			}
			return true;
		}
		return false;

	}


}
