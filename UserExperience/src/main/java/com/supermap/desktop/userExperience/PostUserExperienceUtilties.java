package com.supermap.desktop.userExperience;

import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.desktop.utilities.NetworkUtilties;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by SillyB on 2017/7/12.
 */
public class PostUserExperienceUtilties {
	private static final String postLogsIP = "log.supermap.com";


	public static boolean postFile(FileLocker fileLocker) {
		if (NetworkUtilties.ping(postLogsIP)) {
			try {
				String ip = NetworkUtilties.getIpAddress();
				String macAddress = NetworkUtilties.getMacAddress();
				RandomAccessFile randomAccessFile = fileLocker.getRandomAccessFile();

				if (randomAccessFile.length() > 0) {
					randomAccessFile.seek(0);
					while (randomAccessFile.length() != randomAccessFile.getFilePointer()) {
						String line = randomAccessFile.readLine();
						line = line.replaceAll("\\{IP\\}", ip);
						line = line.replaceAll("\\{MACADDRESS\\}", macAddress);
						// TODO: 2017/7/12
					}
				}
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
				return false;
			}
			return true;
		}
		return false;

	}


}
