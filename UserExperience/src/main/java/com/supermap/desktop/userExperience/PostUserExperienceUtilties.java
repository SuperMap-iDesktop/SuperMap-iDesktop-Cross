package com.supermap.desktop.userExperience;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
		boolean result = true;
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
					int count = 0;
					JSONArray array = new JSONArray();
					for (String line : lines) {
						line = line.replace("{IP}", ip);
						line = line.replace("{MACADDRESS}", macAddress);
						array.add(JSONObject.parseObject(line));

						count++;
						if (count == 49) {
							StringEntity stringEntity = new StringEntity(array.toJSONString(), ContentType.APPLICATION_JSON);
							array.clear();
							httpPost.setEntity(stringEntity);
							CloseableHttpResponse response = httpClient.execute(httpPost);
							count = 0;
							if (response.getStatusLine().getStatusCode() != 200) {
								result = false;
							}
							response.close();
						}
					}
					if (count != 0) {
						StringEntity stringEntity = new StringEntity(array.toJSONString(), ContentType.APPLICATION_JSON);
						httpPost.setEntity(stringEntity);
						CloseableHttpResponse response = httpClient.execute(httpPost);
						if (response.getStatusLine().getStatusCode() != 200) {
							result = false;
						}
						response.close();
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
			return result;
		}
		return false;
	}
}
