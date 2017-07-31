package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Charset;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.properties.CharsetProperties;

import java.util.ArrayList;

/**
 * Created by hanyz on 2017/4/28.
 */
public class ParameterCharset extends ParameterComboBox {

	public ParameterCharset() {
		this(ProcessProperties.getString("String_LabelCharset"));//"字符集:"
	}

	public ParameterCharset(String describe) {
		super(describe);
		setItems(this.createCharsetNode().toArray(new ParameterDataNode[createCharsetNode().size()]));
	}

	private ArrayList<ParameterDataNode> createCharsetNode() {
		ArrayList<ParameterDataNode> parameterDataNodes = new ArrayList<>();
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.ANSI), Charset.ANSI));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.ARABIC), Charset.ARABIC));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.BALTIC), Charset.BALTIC));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.CHINESEBIG5), Charset.CHINESEBIG5));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.CYRILLIC), Charset.CYRILLIC));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.DEFAULT), Charset.DEFAULT));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.EASTEUROPE), Charset.EASTEUROPE));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.GB18030), Charset.GB18030));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.GREEK), Charset.GREEK));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.HANGEUL), Charset.HANGEUL));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.HEBREW), Charset.HEBREW));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.JOHAB), Charset.JOHAB));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.KOREAN), Charset.KOREAN));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.MAC), Charset.MAC));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.OEM), Charset.OEM));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.RUSSIAN), Charset.RUSSIAN));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.SHIFTJIS), Charset.SHIFTJIS));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.SYMBOL), Charset.SYMBOL));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.THAI), Charset.THAI));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.TURKISH), Charset.TURKISH));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.UNICODE), Charset.UNICODE));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.UTF7), Charset.UTF7));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.UTF8), Charset.UTF8));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.VIETNAMESE), Charset.VIETNAMESE));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.WINDOWS1252), Charset.WINDOWS1252));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.XIA5), Charset.XIA5));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.XIA5GERMAN), Charset.XIA5GERMAN));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.XIA5NORWEGIAN), Charset.XIA5NORWEGIAN));
		parameterDataNodes.add(new ParameterDataNode(CharsetProperties.getString(CharsetProperties.XIA5SWEDISH), Charset.XIA5SWEDISH));

		return parameterDataNodes;
	}


}
