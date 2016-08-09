package com.supermap.desktop.ui.controls;

import com.supermap.data.Enum;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;

public class ReadTextStyleTemplateXML {
	private ReadTextStyleTemplateXML(){
		//工具类，不提供构造函数
	}
	
	public static TextStyle getTextStyleFromXmlFile(File file) {
		TextStyle result = new TextStyle();
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			
			Node nodeTextStyle = document.getChildNodes().item(0);
			if (nodeTextStyle.getNodeName().equals(TextStyleTemplateTags.TEXT_STYLE.toString())) {
				NodeList nodeList = nodeTextStyle.getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node child = nodeList.item(i);
					if (child instanceof Element) {
						Element childElement = (Element) child;
						String childElementStr = childElement.getNodeName();
						Text textNode = (Text) childElement.getFirstChild();
						String data = "";
						if (textNode != null && textNode.getData() != null) {
							// 调用trim会保证移除真实数据旁边的空白，比如说xml的作者将数据单独放在一行
							data = textNode.getData().trim();
						}
						
						if (childElementStr.equals(TextStyleTemplateTags.FACE_NAME.toString())) {
							result.setFontName(data);
						} else if (childElementStr.equals(TextStyleTemplateTags.ALIGN.toString())) {
							// 把得到的字符串大写一下
							Enum textAlignment = Enum.parse(TextAlignment.class, data.toUpperCase());
							result.setAlignment((TextAlignment) textAlignment);
						} else if (childElementStr.equals(TextStyleTemplateTags.FORE_COLOR.toString())) {
							result.setForeColor(parseColor(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.BACK_COLOR.toString())) {
							result.setBackColor(parseColor(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.BACK_OPAQUE.toString())) {
							result.setBackOpaque(Boolean.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.HEIGHT.toString())) {
							result.setFontHeight(Double.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.WIDTH.toString())) {
							result.setFontWidth(Double.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.WEIGHT.toString())) {
							result.setWeight(Integer.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.IS_FIXED_SIZE.toString())) {
							result.setSizeFixed(Boolean.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.FIXED_SIZE.toString())) {
							continue;
						} else if (childElementStr.equals(TextStyleTemplateTags.ANGLE.toString())) {
							result.setRotation(Double.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.BOLD.toString())) {
							result.setBold(Boolean.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.ITALIC.toString())) {
							result.setItalic(Boolean.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.ITALIC_ANGLE.toString())) {
							result.setItalicAngle(Double.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.STRIKE_OUT.toString())) {
							result.setStrikeout(Boolean.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.UNDERLINE.toString())) {
							result.setUnderline(Boolean.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.SHADOW.toString())) {
							result.setShadow(Boolean.valueOf(data));
						} else if (childElementStr.equals(TextStyleTemplateTags.HALO.toString())) {
							// TODO
							continue;
						} else if (childElementStr.equals(TextStyleTemplateTags.TEXT3D_OPAQUE.toString())) {
							continue;
						} else if (childElementStr.equals(TextStyleTemplateTags.TEXT3D_SCALE.toString())) {
							continue;
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}
	
	
	/**
	 * 根据传入的字符串得到一个Color对象，
	 * @param data
	 * 			字符串的形式为：RGB(0,0,0)
	 * @return
	 */
	private static Color parseColor(String data) {
		String temp = data.substring("RGB(".length());
		// trim()一下，忽略掉空白
		temp = temp.trim(); 
		temp = temp.substring(0, temp.length() - 1);
		String[] rgb = temp.split(",");
		String r = rgb[0].trim();
		String g = rgb[1].trim();
		String b = rgb[2].trim();
		return new Color(Integer.valueOf(r), Integer.valueOf(g), 
				Integer.valueOf(b));
	}
	
}

/**
 * <sml:TextStyle> 
 * <sml:FaceName>宋体</sml:FaceName> 
 * <sml:Align>TopLeft</sml:Align>
 * <sml:ForeColor>RGB(77,77,77)</sml:ForeColor> 
 * <sml:BackColor>RGB(0,0,0)</sml:BackColor>
 * <sml:BackOpaque>FALSE</sml:BackOpaque> 
 * <sml:Height>2.5</sml:Height>
 * <sml:Width>0</sml:Width> 
 * <sml:Weight>400</sml:Weight>
 * <sml:IsFixedSize>TRUE</sml:IsFixedSize>
 * <sml:FixedSize>2.5</sml:FixedSize>
 * <sml:Angle>0</sml:Angle> 
 * <sml:Bold>FALSE</sml:Bold> 
 * <sml:Italic>FALSE</sml:Italic>
 * <sml:ItalicAngle>0.00</sml:ItalicAngle> 
 * <sml:StrikeOut>FALSE</sml:StrikeOut>
 * <sml:Underline>FALSE</sml:Underline> 
 * <sml:Shadow>FALSE</sml:Shadow>
 * <sml:Halo>FALSE</sml:Halo> 
 * <sml:Text3DOpaque>100</sml:Text3DOpaque>
 * <sml:Text3DScale>1.00</sml:Text3DScale> 
 * </sml:TextStyle>
 */
class TextStyleTemplateTags {
	
	private final String tags;
	
	private TextStyleTemplateTags(String tags) {
		this.tags = tags;
	}
	@Override
	public String toString() {
		return tags;
	}
	
	public static final TextStyleTemplateTags TEXT_STYLE = new TextStyleTemplateTags("sml:TextStyle");
	public static final TextStyleTemplateTags FACE_NAME = new TextStyleTemplateTags("sml:FaceName");
	public static final TextStyleTemplateTags ALIGN = new TextStyleTemplateTags("sml:Align");
	public static final TextStyleTemplateTags FORE_COLOR = new TextStyleTemplateTags("sml:ForeColor");
	public static final TextStyleTemplateTags BACK_COLOR = new TextStyleTemplateTags("sml:BackColor");
	public static final TextStyleTemplateTags BACK_OPAQUE = new TextStyleTemplateTags("sml:BackOpaque");
	public static final TextStyleTemplateTags HEIGHT = new TextStyleTemplateTags("sml:Height");
	public static final TextStyleTemplateTags WIDTH = new TextStyleTemplateTags("sml:Width");
	public static final TextStyleTemplateTags WEIGHT = new TextStyleTemplateTags("sml:Weight");
	public static final TextStyleTemplateTags IS_FIXED_SIZE = new TextStyleTemplateTags("sml:IsFixedSize");
	public static final TextStyleTemplateTags FIXED_SIZE = new TextStyleTemplateTags("sml:FixedSize");
	public static final TextStyleTemplateTags ANGLE = new TextStyleTemplateTags("sml:Angle");
	public static final TextStyleTemplateTags BOLD = new TextStyleTemplateTags("sml:Bold");
	public static final TextStyleTemplateTags ITALIC = new TextStyleTemplateTags("sml:Italic");
	public static final TextStyleTemplateTags ITALIC_ANGLE = new TextStyleTemplateTags("sml:ItalicAngle");
	public static final TextStyleTemplateTags STRIKE_OUT = new TextStyleTemplateTags("sml:StrikeOut");
	public static final TextStyleTemplateTags UNDERLINE = new TextStyleTemplateTags("sml:Underline");
	public static final TextStyleTemplateTags SHADOW = new TextStyleTemplateTags("sml:Shadow");
	public static final TextStyleTemplateTags HALO = new TextStyleTemplateTags("sml:Halo");
	public static final TextStyleTemplateTags TEXT3D_OPAQUE = new TextStyleTemplateTags("sml:Text3DOpaque");
	public static final TextStyleTemplateTags TEXT3D_SCALE = new TextStyleTemplateTags("sml:Text3DScale");
}
