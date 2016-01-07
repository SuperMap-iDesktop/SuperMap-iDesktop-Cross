package com.supermap.desktop.ui.controls;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.supermap.desktop.Application;

/**
 * 颜色配置文件类
 * 
 * @author xuzw
 *
 */
class ColorScheme {

	private ArrayList<Color> colors;

	private int fileType;

	private int version;

	private String name;

	private String author;

	private String description;

	private ColorSystem colorSystem;

	private IntervalColorBuildMethod intervalColorBuildMethod;

	private int keyColorCount;

	private int intervalColorCount;

	/**
	 * 构造函数
	 */
	public ColorScheme() {
		colors = new ArrayList<Color>();
	}

	/**
	 * 获取颜色数组
	 * 
	 * @return
	 */
	public ArrayList<Color> getColors() {
		return colors;
	}

	/**
	 * 设置颜色数组
	 * 
	 * @param colors
	 */
	public void setColors(ArrayList<Color> colors) {
		this.colors = (ArrayList<Color>) colors.clone();
	}

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
		this.name = name;
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
		this.description = description;
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
		return keyColorCount;
	}

	/**
	 * 设置关键色个数
	 * 
	 * @param count
	 */
	public void setKeyColorCount(int count) {
		keyColorCount = count;
	}

	/**
	 * 根据XML文件构建颜色集合，文件样式参见范例
	 * 
	 * @param xmlFile
	 * @return
	 */
	public boolean fromXML(File xmlFile) {
		if (!xmlFile.exists()) {
			return false;
		}
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		Document document = null;

		boolean booleanResult = false;
		// 存放颜色数据结果
		ArrayList<Color> colorList = new ArrayList<Color>();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(xmlFile);

			// XML文件根节点，ColorScheme节点
			Node nodeColorScheme = document.getChildNodes().item(0);
			Element elementColorScheme = (Element) nodeColorScheme;
			// 读取文件头信息，FileHeader节点
			Node nodeFileHeader = elementColorScheme.getElementsByTagName(ColorSchemeTags.FILE_HEADER).item(0);
			NodeList nodeListFileHeader = nodeFileHeader.getChildNodes();

			for (int i = 0; i < nodeListFileHeader.getLength(); i++) {
				Node child = nodeListFileHeader.item(i);
				if (child instanceof Element) {
					Element childElement = (Element) child;
					String childElementName = childElement.getNodeName();
					Text textNode = (Text) childElement.getFirstChild();
					if (textNode != null && textNode.getData() != null) {
						// 调用trim会保证移除真实数据旁边的空白，比如说xml的作者将数据单独放在一行
						String value = textNode.getData().trim();
						if (childElementName.equals(ColorSchemeTags.FILE_TYPE)) {
							// 文件类型信息，FileType节点
							this.setFileType(Integer.valueOf(value));
						} else if (childElementName.equals(ColorSchemeTags.VERSION)) {
							// 版本信息，Version节点
							this.setVersion(Integer.valueOf(value));
						} else if (childElementName.equals(ColorSchemeTags.NAME)) {
							// 名称，Name节点
							this.setName(value);
						} else if (childElementName.equals(ColorSchemeTags.AUTHOR)) {
							// 作者，Author节点
							this.setAuthor(value);
						} else if (childElementName.equals(ColorSchemeTags.DESCRIPTION)) {
							// 描述，Description节点
							this.setDescription(value);
						} else if (childElementName.equals(ColorSchemeTags.COLORSYSTEM)) {
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
						} else if (childElementName.equals(ColorSchemeTags.INTERVAL_COLOR_BUILD_METHOD)) {
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
						} else if (childElementName.equals(ColorSchemeTags.KEY_COLOR_COUNT)) {
							// 关键色个数，KeyColorCount节点
							this.setKeyColorCount(Integer.valueOf(value));
						} else if (childElementName.equals(ColorSchemeTags.INTERVAL_COLOR_COUNT)) {
							// 两个关键色直接插值颜色数目，IntervalColorCount节点
							this.setIntervalColorCount(Integer.valueOf(value));
						}
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
						if (childElementName.indexOf(ColorSchemeTags.KEY_COLOR) != -1) {
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
						if (childElementName.indexOf(ColorSchemeTags.KEY_COLOR) != -1) {
							float h = Float.parseFloat(getValueFromNode(childElement, ColorSchemeTags.HUE));
							float s = Float.parseFloat(getValueFromNode(childElement, ColorSchemeTags.SATURATION));
							float b = Float.parseFloat(getValueFromNode(childElement, ColorSchemeTags.VALUE));

							colorList.add(Color.getHSBColor(h, s, b));
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
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
		CS_UNKNOWN, // 未知颜色系统
		CS_RGB, // r = red(0-255), g = green(0-255), b = blue(0-255)

		CS_CMYK, // A color mode made up of cyan (C), magenta (M), yellow
		// (Y), and black (K).
		// CMYK printing produces true blacks and a wide tonal range. In the
		// CMYK color mode, color values are expressed as percentages;
		// therefore, a value of 100 for an ink means that the ink is applied at
		// full saturation.
		// c = cyan(0-100), m = magenta(0-100), y = yellow(0-100), k =
		// blank(0-100),

		CS_HLS, // h = hue(0-360), l = lightness(0-100), S = saturation(0-100)

		CS_HSB, // A color model that defines three components: hue, saturation,
		// and brightness.
		// Hue determines color (yellow, orange, red, and so on); brightness
		// determines perceived intensity (lighter or darker color); and
		// saturation determines color depth (from dull to intense).
		// h = hue(0-360), s = saturation(0-100), b = brightness(0-100)
		CS_LAB, // A color model that contains a luminance (or lightness)
		// component (L) and
		// two chromatic components: "a" (green to red) and "b" (blue to
		// yellow).
		CS_CMY, // A color mode made up of cyan (C), magenta (M), and yellow
		// (Y).
		// This mode is used in the three-color printing process.
		CS_YIQ, CS_GRAY
	}

	protected enum IntervalColorBuildMethod // 两个关键色之间的中间色产生的方式
	{
		ICBM_GRADIENT, // 渐变方式
		ICBM_RANDOM, // 随即产生
	}
}
