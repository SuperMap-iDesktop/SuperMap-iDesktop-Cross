package com.supermap.desktop.controls.colorScheme;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.utilties.FileUtilities;
import com.supermap.desktop.utilties.PathUtilities;
import com.supermap.desktop.utilties.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class ColorSchemeManager {
	private static ColorSchemeManager colorSchemeManager;
	private List<ColorSchemeManagerChangedListener> colorSchemeManagerChangedListeners;
	private List<ColorScheme> colorSchemeList;
	private List<ColorScheme> defaultColorSchemeList;

	private int version;

	public List<ColorScheme> getColorSchemeList() {
		return colorSchemeList;
	}

	public void setColorSchemeList(List<ColorScheme> colorSchemeList) {
		this.colorSchemeList = colorSchemeList;
		fireColorSchemeManagerChanged();

	}

	public List<ColorScheme> getDefaultColorSchemeList() {
		return defaultColorSchemeList;
	}

	public static ColorSchemeManager getColorSchemeManager() {
		if (colorSchemeManager == null) {
			colorSchemeManager = new ColorSchemeManager();
		}
		return colorSchemeManager;
	}

	private ColorSchemeManager() {
		init();
	}

	private void init() {
		colorSchemeManagerChangedListeners = new ArrayList<>();
		colorSchemeList = new ArrayList<>();
		defaultColorSchemeList = new ArrayList<>();
		version = 60000;
		initDefaultColorSchemes(defaultColorSchemeList);
		if (!new File(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeBasicDirectory"), true)).exists()) {
			new File(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeBasicDirectory"), true)).mkdirs();
		}
		if (!new File(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeCustomDirectory"), true)).exists()) {
			new File(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeCustomDirectory"), true)).mkdirs();
		}
		if (new File(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeManagerFilePath"), false)).exists()) {
			formXML(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeManagerFilePath"), false));
		}
		if (colorSchemeList.size() == 0) {
			initDefaultColorSchemes(colorSchemeList);
		}
	}

	private void formXML(String filePath) {
		colorSchemeList.clear();
		Document document = XmlUtilities.getDocument(filePath);
		NodeList childNodes = document.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (TAG_FILE_HEADER.equals(childNodes.item(i).getNodeName())) {
					// 文件头
					String versionValue = childNodes.item(i).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
					version = Integer.valueOf(versionValue);
				} else if (TAG_DATA_BLOCK.equals(childNodes.item(i).getNodeName())) {
					// colors
					NodeList childNodesColors = childNodes.item(i).getChildNodes();
					for (int j = 0; j < childNodesColors.getLength(); j++) {
						if (childNodesColors.item(j).getNodeType() == Node.ELEMENT_NODE) {
							String colorFilePath = childNodesColors.item(j).getChildNodes().item(0).getNodeValue();
							if (new File(colorFilePath).exists()) {
								ColorScheme colorScheme = new ColorScheme();
								if (colorScheme.fromXML(new File(colorFilePath), false)) {
									colorScheme.setColorSchemePath(colorFilePath);
									colorSchemeList.add(colorScheme);
								}
							}
						}
					}
				}
			}
		}
	}

	public void save() {
		FileUtilities.writeToFile(new File(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeManagerFilePath"), false)), toXML(colorSchemeList));
	}

	private String toXML(List<ColorScheme> colorSchemeList) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>").append(System.lineSeparator());
		stringBuilder.append("<ColorSchemeManager>");

		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(" <FileHeader>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("  <Version>");
		stringBuilder.append(version);
		stringBuilder.append("</Version>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("  <ColorSchemeCount>");
		stringBuilder.append(colorSchemeList.size());
		stringBuilder.append("</ColorSchemeCount>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(" </FileHeader>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(" <DataBlock>");
		stringBuilder.append(System.lineSeparator());

		for (int i = 0; i < colorSchemeList.size(); i++) {
			stringBuilder.append("  <ColorScheme").append(i).append(">");
			stringBuilder.append(colorSchemeList.get(i).getColorSchemePath());
			stringBuilder.append("</ColorScheme").append(i).append(">").append(System.lineSeparator());
		}
		stringBuilder.append(" </DataBlock>").append(System.lineSeparator());
		stringBuilder.append("</ColorSchemeManager>");
		return stringBuilder.toString();
	}

	public void initDefaultColorSchemes(List<ColorScheme> schemeList) {
		InputStream in = this.getClass().getResourceAsStream("/com/supermap/desktop/controlsresources/ColorScheme/DefaultColorScheme.xml");
		String baseDirectory = PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeBasicDirectory"), true);
		if (in != null) {
			Document defaultColorScheme = XmlUtilities.getDocument(in);
			NodeList childNodes = defaultColorScheme.getChildNodes().item(0).getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					ColorScheme colorScheme = new ColorScheme();
					if (colorScheme.fromXML(childNodes.item(i))) {
						schemeList.add(colorScheme);
					}
					colorScheme.setColorSchemePath(baseDirectory + colorScheme.getName() + ".scs");
				}
			}
		}
	}

	private void fireColorSchemeManagerChanged() {
		save();
		ColorSchemeManagerChangedEvent colorSchemeManagerChangedEvent = new ColorSchemeManagerChangedEvent(colorSchemeList);
		for (int i = colorSchemeManagerChangedListeners.size() - 1; i >= 0; i--) {
			colorSchemeManagerChangedListeners.get(i).colorSchemeManagerChanged(colorSchemeManagerChangedEvent);
		}
	}

	public void addColorSchemeManagerChangedListener(ColorSchemeManagerChangedListener colorSchemeManagerChangedListener) {
		if (!colorSchemeManagerChangedListeners.contains(colorSchemeManagerChangedListener)) {
			colorSchemeManagerChangedListeners.add(colorSchemeManagerChangedListener);
		}
	}

	public void removeColorSchemeManagerChangedListener(ColorSchemeManagerChangedListener colorSchemeManagerChangedListener) {
		colorSchemeManagerChangedListeners.remove(colorSchemeManagerChangedListener);
	}


	private final String TAG_FILE_HEADER = "FileHeader";
	private final String TAG_DATA_BLOCK = "DataBlock";


	public void addColorScheme(ColorScheme colorScheme) {
		this.colorSchemeList.add(colorScheme.clone());
		fireColorSchemeManagerChanged();
	}
}
