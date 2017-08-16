package com.supermap.desktop.utilities;

import com.supermap.data.BlockSizeOption;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.BlockSizeOptionProperties;

/**
 * Created by yuanR on 2017/8/15 0015.
 */
public class BlockSizeOptionUtilities {
	private BlockSizeOptionUtilities() {
		// 工具类不提供构造函数
	}

	public static String toString(BlockSizeOption data) {
		String result = "";

		try {
			if (data == BlockSizeOption.BS_64) {
				result = BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_64);
			} else if (data == BlockSizeOption.BS_128) {
				result = BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_128);
			} else if (data == BlockSizeOption.BS_256) {
				result = BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_256);
			} else if (data == BlockSizeOption.BS_512) {
				result = BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_512);
			} else if (data == BlockSizeOption.BS_1024) {
				result = BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_1024);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static BlockSizeOption valueOf(String text) {
		BlockSizeOption result = BlockSizeOption.BS_64;

		try {
			if (text.equalsIgnoreCase(BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_64))) {
				result = BlockSizeOption.BS_64;
			} else if (text.equalsIgnoreCase(BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_128))) {
				result = BlockSizeOption.BS_128;
			} else if (text.equalsIgnoreCase(BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_256))) {
				result = BlockSizeOption.BS_256;
			} else if (text.equalsIgnoreCase(BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_512))) {
				result = BlockSizeOption.BS_512;
			} else if (text.equalsIgnoreCase(BlockSizeOptionProperties.getString(BlockSizeOptionProperties.String_BlockSizeOption_BS_1024))) {
				result = BlockSizeOption.BS_1024;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
