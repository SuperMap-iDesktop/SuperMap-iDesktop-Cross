package com.supermap.desktop.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DictionaryImpl implements Dictionary {

	private List fWords = new ArrayList(Arrays.asList("osgi", "eclipse", "equinox"));
	private String fLanguage = "en_US";

	@Override
	public String getLanguage() {
		return fLanguage;
	}

	@Override
	public boolean check(String word) {
		return fWords.contains(word);
	}

	@Override
	public String toString() {
		return fLanguage;
	}

}
