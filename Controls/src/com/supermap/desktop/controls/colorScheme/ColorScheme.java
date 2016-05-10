package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.ICloneable;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilties.ColorsUtilties;
import com.supermap.desktop.utilties.FileUtilties;
import com.supermap.desktop.utilties.PathUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 颜色配置文件类
 *
 * @author xuzw
 */
public class ColorScheme implements ICloneable {

	private List<Color> colors;

	private int fileType;

	private int version;

	private String name;

	private String author;

	private String description;

	private ColorSystem colorSystem;

	private IntervalColorBuildMethod intervalColorBuildMethod;

	/**
	 * 这个变量好像没用到
	 */
	private int keyColorCount;

	private int intervalColorCount;

	private String colorSchemePath;


	/**
	 * 构造函数
	 */
	public ColorScheme() {
		colors = new ArrayList<>();
		name = "NewColorScheme";
		author = "SuperMap";
		description = "Custom";
		intervalColorCount = 32;
		colorSystem = ColorSystem.CS_RGB;
		intervalColorBuildMethod = IntervalColorBuildMethod.ICBM_GRADIENT;
	}

	@Override
	public ColorScheme clone() {
		ColorScheme clone = null;
		try {
			clone = ((ColorScheme) super.clone());
		} catch (CloneNotSupportedException e) {
			Application.getActiveApplication().getOutput().output(e);
			return null;
		}
		List<Color> copy = new LinkedList<>();
		copy.addAll(this.colors);
		clone.setColors(copy);
		clone.setFileType(this.fileType);
		clone.setVersion(this.version);
		clone.setName(this.name);
		clone.setAuthor(this.author);
		clone.setDescription(this.description);
		clone.setColorSystem(this.colorSystem);
		clone.setIntervalColorBuildMethod(this.intervalColorBuildMethod);
		clone.setKeyColorCount(keyColorCount);
		clone.setIntervalColorCount(this.intervalColorCount);
		return clone;
	}

	private void setColors(List<Color> colors) {
		this.colors = colors;
	}

	public ColorScheme(ColorScheme colorScheme) {
		this.colors = colorScheme.colors;
		this.fileType = version;
	}

	/**
	 * 获取颜色数组
	 *
	 * @return
	 */
	public List<Color> getColorsList() {
		return colors;
	}

	public Colors getColors() {
		Colors colors = null;
		Color[] gradientColors = new Color[this.colors.size()];
		this.colors.toArray(gradientColors);
		int intervalColorCount = this.getIntervalColorCount();
		ColorScheme.IntervalColorBuildMethod method = this.getIntervalColorBuildMethod();
		if (method.equals(ColorScheme.IntervalColorBuildMethod.ICBM_GRADIENT)) {
			colors = Colors.makeGradient(intervalColorCount + this.getKeyColorCount(), gradientColors);
		} else if (method.equals(ColorScheme.IntervalColorBuildMethod.ICBM_RANDOM)) {
			colors = ColorsUtilties.buildRandom(intervalColorCount + this.getKeyColorCount(), gradientColors);
		}
		return colors;
	}


//	/**
//	 * 设置颜色数组
//	 *
//	 * @param colors
//	 */
//	public void setColors(ArrayList<Color> colors) {
//		this.colors = (ArrayList<Color>) colors.clone();
//	}

	/**
	 * 获取文件类型
	 *
	 * @return
	 */
	public int getFileType() {
		return fileType;
	}

	/**
	 * 设置文件类型
	 *
	 * @param type
	 */
	public void setFileType(int type) {
		fileType = type;
	}

	/**
	 * 获取版本
	 *
	 * @return
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * 设置版本
	 *
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * 获取名字
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名字
	 *
	 * @param name
	 */
	public void setName(String name) {
		if (name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
	}

	/**
	 * 获取作者
	 *
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 设置作者
	 *
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * 获取描述
	 *
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		if (description == null) {
			this.description = "";
		} else {
			this.description = description;
		}
	}

	/**
	 * 获取两个关键色之间的中间色的数目
	 *
	 * @return
	 */
	public int getIntervalColorCount() {
		return intervalColorCount;
	}

	/**
	 * 设置两个关键色之间的中间色的数目
	 *
	 * @param colorCount
	 */
	public void setIntervalColorCount(int colorCount) {
		intervalColorCount = colorCount;
	}

	/**
	 * 获取颜色方案系统类型
	 *
	 * @return
	 */
	public ColorSystem getColorSystem() {
		return colorSystem;
	}

	/**
	 * 设置颜色方案系统类型
	 *
	 * @param colorSystem
	 */
	public void setColorSystem(ColorSystem colorSystem) {
		this.colorSystem = colorSystem;
	}

	/**
	 * 获取两个关键色之间的中间色产生的方式
	 *
	 * @return
	 */
	public IntervalColorBuildMethod getIntervalColorBuildMethod() {
		return intervalColorBuildMethod;
	}

	/**
	 * 设置两个关键色之间的中间色产生的方式
	 *
	 * @param buildMethod
	 */
	public void setIntervalColorBuildMethod(IntervalColorBuildMethod buildMethod) {
		intervalColorBuildMethod = buildMethod;
	}

	/**
	 * 获取关键色个数
	 *
	 * @return
	 */
	public int getKeyColorCount() {
		if (colors == null) {
			return 0;
		}
		return colors.size();
	}

	/**
	 * 设置关键色个数
	 *
	 * @param count
	 */
	public void setKeyColorCount(int count) {
		keyColorCount = count;
	}

	public String getColorSchemePath() {
		if (StringUtilties.isNullOrEmpty(colorSchemePath)) {
			colorSchemePath = getDefaultFilePath(PathUtilties.getFullPathName(ControlsProperties.getString("String_ColorSchemeCustomDirectory"), true));
			save();
		}
		return colorSchemePath;
	}

	public void save() {
		if (colorSchemePath == null) {
			colorSchemePath = getDefaultFilePath(PathUtilties.getFullPathName(ControlsProperties.getString("String_ColorSchemeCustomDirectory"), true));
		}
		saveAsFilePath(colorSchemePath);
	}

	public void saveAsDirectories(String directories) {
		String defaultFilePath = getDefaultFilePath(directories);
		saveAsFilePath(defaultFilePath);
	}

	public void saveAsFilePath(String defaultFilePath) {
		try {
			File file = new File(defaultFilePath);
			if (file.exists()) {
				FileUtilties.delete(file);
			}
			if (file.createNewFile()) {
				FileUtilties.writeToFile(file, this.toXML());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private String getDefaultFilePath(String customDirectory) {
		if (!customDirectory.endsWith(File.separator) && !customDirectory.endsWith("//")) {
			customDirectory = customDirectory + File.separator;
		}
		if (!new File(customDirectory).exists()) {
			new File(customDirectory).mkdirs();
		}
		boolean isExist = true;
		int i = -1;
		while (isExist) {
			i++;
			isExist = new File(customDirectory + getFileName(i)).exists();
		}
		return customDirectory + getFileName(i);
	}

	private String getFileName(int i) {
		if (i == 0) {
			return "ColorScheme.SCS";
		}
		return "ColorScheme" + i + ".SCS";
	}

	public void setColorSchemePath(String colorSchemePath) {
		this.colorSchemePath = colorSchemePath;
		try {
			if (colorSchemePath == null || !new File(colorSchemePath).exists()) {
				save();
			}
		} catch (Exception ignored) {

		}
	}

	private String toXML() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<ColorScheme>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("  <FileHeader>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <FileType>");
		stringBuilder.append(fileType);
		stringBuilder.append("</FileType>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <Version>");
		stringBuilder.append(version);
		stringBuilder.append("</Version>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <Name>");
		stringBuilder.append(name);
		stringBuilder.append("</Name>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <Author>");
		stringBuilder.append(author);
		stringBuilder.append("</Author>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <Description>");
		if (description == null) {
			description = "";
		}
		stringBuilder.append(description);
		stringBuilder.append("</Description>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <ColorSystem>");
		stringBuilder.append(this.getColorSystem().getValue());
		stringBuilder.append("</ColorSystem>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <IntervalColorBuildMethod>");
		stringBuilder.append(this.getIntervalColorBuildMethod().getValue());
		stringBuilder.append("</IntervalColorBuildMethod>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <KeyColorCount>");
		stringBuilder.append(this.getKeyColorCount());
		stringBuilder.append("</KeyColorCount>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("    <IntervalColorCount>");
		stringBuilder.append(this.getIntervalColorCount());
		stringBuilder.append("</IntervalColorCount>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("  </FileHeader>");
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append("  <DataBlock>").append(System.lineSeparator());

		if (colors != null) {
			for (int i = 0; i < colors.size(); i++) {
				Color color = colors.get(i);
				stringBuilder.append("    <KeyColor").append(i + 1).append(">").append(System.lineSeparator());
				stringBuilder.append("      <Name></Name>").append(System.lineSeparator());
				stringBuilder.append("      <ColorSystem>");
				stringBuilder.append(this.getColorSystem().toString());
				stringBuilder.append("</ColorSystem>").append(System.lineSeparator());
				if (this.getColorSystem() == ColorSystem.CS_RGB) {
					stringBuilder.append("      <Red>").append(color.getRed()).append("</Red>").append(System.lineSeparator());
					stringBuilder.append("      <Green>").append(color.getGreen()).append("</Green>").append(System.lineSeparator());
					stringBuilder.append("      <Blue>").append(color.getBlue()).append("</Blue>").append(System.lineSeparator());
				} else if (this.getColorSystem() == ColorSystem.CS_HSB) {
					float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
					stringBuilder.append("      <Hue>").append(hsb[0]).append("</Hue>").append(System.lineSeparator());
					stringBuilder.append("      <Saturation>").append(hsb[1]).append("</Saturation>").append(System.lineSeparator());
					stringBuilder.append("      <Value>").append(hsb[2]).append("</Value>").append(System.lineSeparator());
				}
				stringBuilder.append("    </KeyColor").append(i + 1).append(">").append(System.lineSeparator());
			}
		}
		stringBuilder.append("  </DataBlock>").append(System.lineSeparator());
		stringBuilder.append("</ColorScheme>");
		return stringBuilder.toString();
	}

	public boolean fromXML(File xmlFile) {
		this.colorSchemePath = xmlFile.getAbsolutePath();
		boolean result = false;
		if (!xmlFile.exists()) {
			return false;
		}
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		Document document = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(xmlFile);
			Node nodeColorScheme = document.getChildNodes().item(0);
			result = fromXML(nodeColorScheme);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			result = false;
		}
		return result;
	}

	/**
	 * 根据XML文件构建颜色集合，文件样式参见范例
	 *
	 * @return
	 */
	public boolean fromXML(Node nodeColorScheme) {
		// XML文件根节点，ColorScheme节点
		Element elementColorScheme = (Element) nodeColorScheme;
		// 读取文件头信息，FileHeader节点
		Node nodeFileHeader = elementColorScheme.getElementsByTagName(ColorSchemeTags.FILE_HEADER).item(0);
		NodeList nodeListFileHeader = nodeFileHeader.getChildNodes();

		boolean booleanResult = false;
		// 存放颜色数据结果
		ArrayList<Color> colorList = new ArrayList<Color>();
		try {


			for (int i = 0; i < nodeListFileHeader.getLength(); i++) {
				Node child = nodeListFileHeader.item(i);
				if (child instanceof Element) {
					Element childElement = (Element) child;
					String childElementName = childElement.getNodeName();
					Text textNode = (Text) childElement.getFirstChild();
					// 调用trim会保证移除真实数据旁边的空白，比如说xml的作者将数据单独放在一行
					String value = null;
					if (textNode != null && textNode.getData() != null) {
						value = textNode.getData().trim();
					} else {
						value = "";
					}
					switch (childElementName) {
						case ColorSchemeTags.FILE_TYPE:
							// 文件类型信息，FileType节点
							this.setFileType(Integer.valueOf(value));
							break;
						case ColorSchemeTags.VERSION:
							// 版本信息，Version节点
							this.setVersion(Integer.valueOf(value));
							break;
						case ColorSchemeTags.NAME:
							// 名称，Name节点
							this.setName(value);
							break;
						case ColorSchemeTags.AUTHOR:
							// 作者，Author节点
							this.setAuthor(value);
							break;
						case ColorSchemeTags.DESCRIPTION:
							// 描述，Description节点
							this.setDescription(value);
							break;
						case ColorSchemeTags.COLORSYSTEM:
							// 颜色系统，ColorSystem节点
							int colorSystemValue = Integer.parseInt(value);
							ColorSystem colorSystemTemp = null;
							switch (colorSystemValue) {
								case 1:
									colorSystemTemp = ColorSystem.CS_RGB;
									break;
								case 2:
									colorSystemTemp = ColorSystem.CS_CMYK;
									break;
								case 3:
									colorSystemTemp = ColorSystem.CS_HLS;
									break;
								case 4:
									colorSystemTemp = ColorSystem.CS_HSB;
									break;
								case 5:
									colorSystemTemp = ColorSystem.CS_LAB;
									break;
								case 6:
									colorSystemTemp = ColorSystem.CS_CMY;
									break;
								case 7:
									colorSystemTemp = ColorSystem.CS_YIQ;
									break;
								case 8:
									colorSystemTemp = ColorSystem.CS_GRAY;
									break;
								default:
									colorSystemTemp = ColorSystem.CS_UNKNOWN;
									break;
							}
							this.setColorSystem(colorSystemTemp);
							break;
						case ColorSchemeTags.INTERVAL_COLOR_BUILD_METHOD:
							// 差值方法，IntervalColorBuildMethod节点
							int methodValue = Integer.parseInt(value);
							IntervalColorBuildMethod buildMethod = IntervalColorBuildMethod.ICBM_GRADIENT;
							switch (methodValue) {
								case 0:
									buildMethod = IntervalColorBuildMethod.ICBM_GRADIENT;
									break;
								case 1:
									buildMethod = IntervalColorBuildMethod.ICBM_RANDOM;
									break;
								default:
									buildMethod = IntervalColorBuildMethod.ICBM_GRADIENT;
									break;
							}
							this.setIntervalColorBuildMethod(buildMethod);
							break;
						case ColorSchemeTags.KEY_COLOR_COUNT:
							// 关键色个数，KeyColorCount节点
							this.setKeyColorCount(Integer.valueOf(value));
							break;
						case ColorSchemeTags.INTERVAL_COLOR_COUNT:
							// 两个关键色直接插值颜色数目，IntervalColorCount节点
							this.setIntervalColorCount(Integer.valueOf(value));
							break;
					}
				}
			}
			// 读取关键色数据
			Node nodeDataBlock = elementColorScheme.getElementsByTagName(ColorSchemeTags.DATA_BLOCK).item(0);
			NodeList nodeListDataBlock = nodeDataBlock.getChildNodes();
			if (this.getColorSystem().equals(ColorSystem.CS_RGB)) {
				for (int i = 0; i < nodeListDataBlock.getLength(); i++) {
					Node child = nodeListDataBlock.item(i);
					if (child instanceof Element) {
						Element childElement = (Element) child;
						String childElementName = childElement.getNodeName();
						if (childElementName.contains(ColorSchemeTags.KEY_COLOR)) {
							int r = Integer.parseInt(getValueFromNode(childElement, ColorSchemeTags.RED));
							int g = Integer.parseInt(getValueFromNode(childElement, ColorSchemeTags.GREEN));
							int b = Integer.parseInt(getValueFromNode(childElement, ColorSchemeTags.BLUE));
							colorList.add(new Color(r, g, b));
						}
					}
				}
			} else if (this.getColorSystem().equals(ColorSystem.CS_HSB)) {
				for (int i = 0; i < nodeListDataBlock.getLength(); i++) {
					Node child = nodeListDataBlock.item(i);
					if (child instanceof Element) {
						Element childElement = (Element) child;
						String childElementName = childElement.getNodeName();
						if (childElementName.contains(ColorSchemeTags.KEY_COLOR)) {
							float h = Float.parseFloat(getValueFromNode(childElement, ColorSchemeTags.HUE));
							float s = Float.parseFloat(getValueFromNode(childElement, ColorSchemeTags.SATURATION));
							float b = Float.parseFloat(getValueFromNode(childElement, ColorSchemeTags.VALUE));

							colorList.add(Color.getHSBColor(h, s, b));
						}
					}
				}
			}
		} catch (Exception ex) {
			if (!StringUtilties.isNullOrEmpty(this.colorSchemePath)) {
				String message = MessageFormat.format(ControlsProperties.getString("String_ColorSchemeBreak"), this.colorSchemePath);
				Application.getActiveApplication().getOutput().output(message);
			}
		}

		if (!colorList.isEmpty()) {
			colors = colorList;
			booleanResult = true;
		}
		return booleanResult;
	}

	/**
	 * 根据节点和标签名获取该节点的值
	 *
	 * @param element
	 * @param colorSchemeTags
	 * @return
	 */
	protected static String getValueFromNode(Element element, String colorSchemeTags) {
		String result = "";
		Node temp = element.getElementsByTagName(colorSchemeTags).item(0);
		if (temp instanceof Element) {
			Text textNode = (Text) temp.getFirstChild();
			if (textNode != null && textNode.getData() != null) {
				result = textNode.getData().trim();
			}
		}
		return result;
	}

	public void setColorList(List colorList) {
		this.colors = colorList;
	}

	/*
	 * 文件范例
	 * 
	 * <?xml version="1.0" encoding="utf-8" ?> <ColorScheme> <FileHeader> <FileType>678370</FileType> <Version>60000</Version> <Name /> <Author /> <Description
	 * /> <ColorSystem>1</ColorSystem> <IntervalColorBuildMethod>0</IntervalColorBuildMethod> <KeyColorCount>5</KeyColorCount>
	 * <IntervalColorCount>64</IntervalColorCount> </FileHeader> <DataBlock> <KeyColor1> <Name>Color [A=255, R=251, G=233, B=205]</Name>
	 * <ColorSystem>1</ColorSystem> <Red>251</Red> <Green>233</Green> <Blue>205</Blue> </KeyColor1> <KeyColor2> <Name>Color [A=255, R=211, G=139, B=90]</Name>
	 * <ColorSystem>1</ColorSystem> <Red>211</Red> <Green>139</Green> <Blue>90</Blue> </KeyColor2> </DataBlock> </ColorScheme>
	 */
	static class ColorSchemeTags {
		public static final String COLOR_SCHEME = "ColorScheme";
		// FileHeader 用到的标签
		public static final String FILE_HEADER = "FileHeader";
		public static final String FILE_TYPE = "FileType";
		public static final String VERSION = "Version";
		public static final String NAME = "Name";
		public static final String AUTHOR = "Author";
		public static final String DESCRIPTION = "Description";

		public static final String COLORSYSTEM = "ColorSystem";
		public static final String INTERVAL_COLOR_BUILD_METHOD = "IntervalColorBuildMethod";

		// 帮助系统需要使用的节点
		public static final String KEY_COLOR_COUNT = "KeyColorCount";
		public static final String INTERVAL_COLOR_COUNT = "IntervalColorCount";

		// DataBlock部分
		public static final String DATA_BLOCK = "DataBlock";
		public static final String KEY_COLOR = "KeyColor";

		// RGB颜色模型
		public static final String RED = "Red";
		public static final String GREEN = "Green";
		public static final String BLUE = "Blue";

		// HSV颜色模型
		public static final String HUE = "Hue";
		public static final String SATURATION = "Saturation";
		public static final String VALUE = "Value";

		private ColorSchemeTags() {
			// 工具类不提供构造函数
		}
	}

	protected enum ColorSystem {
		CS_UNKNOWN(0), // 未知颜色系统
		CS_RGB(1), // r = red(0-255), g = green(0-255), b = blue(0-255)

		CS_CMYK(2), // A color mode made up of cyan (C), magenta (M), yellow
		// (Y), and black (K).
		// CMYK printing produces true blacks and a wide tonal range. In the
		// CMYK color mode, color values are expressed as percentages;
		// therefore, a value of 100 for an ink means that the ink is applied at
		// full saturation.
		// c = cyan(0-100), m = magenta(0-100), y = yellow(0-100), k =
		// blank(0-100),

		CS_HLS(3), // h = hue(0-360), l = lightness(0-100), S = saturation(0-100)

		CS_HSB(4), // A color model that defines three components: hue, saturation,
		// and brightness.
		// Hue determines color (yellow, orange, red, and so on); brightness
		// determines perceived intensity (lighter or darker color); and
		// saturation determines color depth (from dull to intense).
		// h = hue(0-360), s = saturation(0-100), b = brightness(0-100)
		CS_LAB(5), // A color model that contains a luminance (or lightness)
		// component (L) and
		// two chromatic components: "a" (green to red) and "b" (blue to
		// yellow).
		CS_CMY(6), // A color mode made up of cyan (C), magenta (M), and yellow
		// (Y).
		// This mode is used in the three-color printing process.
		CS_YIQ(7), CS_GRAY(8);
		private int value;

		ColorSystem(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum IntervalColorBuildMethod {
		// 两个关键色之间的中间色产生的方式
		ICBM_GRADIENT(0), // 渐变方式
		ICBM_RANDOM(1); // 随即产生

		private int value;

		IntervalColorBuildMethod(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			if (this == ICBM_RANDOM) {
				return ControlsProperties.getString("String_ColorRandom");
			} else if (this == ICBM_GRADIENT) {
				return ControlsProperties.getString("String_ColorGradient");
			}
			return "";
		}

		public static IntervalColorBuildMethod getMethod(String string) {
			if (string.equals(ControlsProperties.getString("String_ColorRandom"))) {
				return ICBM_RANDOM;
			}
			return ICBM_GRADIENT;
		}
	}

	public static JLabel getColorsLabel(Colors colors, int imageWidth, int imageHeight) {
		JLabel label = new JLabel();
		label.setOpaque(true);
		if (imageWidth <= 0) {
			imageWidth = 270;
		}
		if (imageHeight <= 0) {
			imageHeight = 23;
		}
		label.setPreferredSize(new Dimension(imageWidth, imageHeight));
		BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

		// 根据当前渲染单元格的宽度和颜色数计算出每个颜色应当渲染的步长
		if (colors != null) {
			int colorsCount = colors.getCount();
			int step = imageWidth / colorsCount;
			for (int i = 0; i < colorsCount; i++) {
				graphics.setColor(colors.get(i));
				graphics.fillRect(step * i, 0, step * (i + 1), imageHeight);
			}
		}
		label.setIcon(new ImageIcon(bufferedImage));

		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		return label;
	}

}
