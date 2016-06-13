package com.supermap.desktop.utilties;

/**
 * @author XiaJT
 */
public class PrjCoordSysTypeUtilties {
	private PrjCoordSysTypeUtilties() {

	}

	public static String getDescribe(String value) {
		String[] split = value.split("_");
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < split.length; i++) {
			if (split[i].length() <= 0) {
				continue;
			}
			builder.append(split[i].charAt(0));
			builder.append(split[i].substring(1, split[i].length()).toLowerCase());
		}
		String s = builder.toString();
		return s.equals("DealulPiscului1970StereoEalulPiscului1970Stereo70") ? "DealulPiscului1970Stereo70" : s;
	}
}
