package com.supermap.desktop.utilities;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class BracketMatchUtilties {

	private static final BracketData[] BRACKET_DATAS = new BracketData[]{
//			new BracketData('\"', '\"'),
//			new BracketData('\'', '\''),
			new BracketData('(', ')'),
			new BracketData('{', '}'),
			new BracketData('[', ']'),
			new BracketData('<', '>')
	};

	private StringBuilder matchString;
	private boolean isMatch;
	private ArrayList<BracketData> bracketDatas = new ArrayList<>();

	public BracketMatchUtilties(String matchString) {
		this.matchString = new StringBuilder(matchString);
		bracketDatas = getBracketDatas(matchString);
		isMatch = bracketDatas.size() == 0;
	}

	public boolean appendString(String matchString) {
		checkIsMatch(matchString, bracketDatas);
		this.matchString.append(matchString);
		return bracketDatas.size() == 0;
	}

	private static void checkIsMatch(String matchString, ArrayList<BracketData> bracketDatas) {
		for (int i = 0; i < matchString.length(); i++) {
			char currentString = matchString.charAt(i);
			if (bracketDatas.size() > 0) {
				BracketData bracketData = bracketDatas.get(bracketDatas.size() - 1);
				if (bracketData.right == currentString) {
					bracketDatas.remove(bracketDatas.size() - 1);
				} else if (bracketData.left == currentString) {
					bracketDatas.add(bracketData.clone());
				}
			} else {
				for (BracketData bracketData : BRACKET_DATAS) {
					if (bracketData.left == currentString) {
						bracketDatas.add(bracketData.clone());
						break;
					}
				}
			}
		}
	}

	public boolean isMatch() {
		return isMatch;
	}

	public static boolean isBracketComplete(String columnTmp) {
		ArrayList<BracketData> bracketDatas = getBracketDatas(columnTmp);
		return bracketDatas.size() == 0;
	}

	private static ArrayList<BracketData> getBracketDatas(String columnTmp) {
		ArrayList<BracketData> bracketDatas = new ArrayList<>();
		checkIsMatch(columnTmp, bracketDatas);
		return bracketDatas;
	}

	public String getMatchString() {
		return matchString.toString();
	}

	public void clear() {
		matchString = new StringBuilder();
		isMatch = true;
		bracketDatas = new ArrayList<>();
	}
}
