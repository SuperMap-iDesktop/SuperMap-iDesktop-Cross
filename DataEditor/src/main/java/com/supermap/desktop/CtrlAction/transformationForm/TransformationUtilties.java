package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.TransformationMode;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationTableDataBean;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @author XiaJT
 */
public class TransformationUtilties {

	private TransformationUtilties() {

	}

	public static String getXmlString(TransformationMode transformationMode, List<TransformationTableDataBean> beans) {
		Document emptyDocument = XmlUtilities.getEmptyDocument();
		if (emptyDocument != null) {
			Element root = emptyDocument.createElement("SuperMapTransformation");
			emptyDocument.appendChild(root);
			root.setAttribute("xmlns:sml", "http://www.supermap.com.cn/sml");
			root.setAttribute("version", "20100630");
			root.setAttribute("Description", "UGC settiong file create by SuperMap UGC 6.0");

			Element mode = emptyDocument.createElement("sml:TransformationMode");
			mode.setNodeValue(String.valueOf(transformationMode.value()));
			root.appendChild(mode);
			for (TransformationTableDataBean bean : beans) {
				Element controlPoint = emptyDocument.createElement("sml:ContrlPoint");
				controlPoint.setAttribute("isEnable", String.valueOf(bean.isSelected()));
				if (bean.getPointOriginal() != null) {
					Element originalPointX = emptyDocument.createElement("sml:OriginalPointX");
					originalPointX.appendChild(emptyDocument.createTextNode(DoubleUtilities.toString(bean.getPointOriginal().getX(), 10)));
					controlPoint.appendChild(originalPointX);

					Element originalPointY = emptyDocument.createElement("sml:OriginalPointY");
					originalPointY.appendChild(emptyDocument.createTextNode(DoubleUtilities.toString(bean.getPointOriginal().getY(), 10)));
					controlPoint.appendChild(originalPointY);
				}
				if (bean.getPointRefer() != null) {
					Element targetPointX = emptyDocument.createElement("sml:TargetPointX");
					targetPointX.appendChild(emptyDocument.createTextNode(DoubleUtilities.toString(bean.getPointRefer().getX(), 10)));
					controlPoint.appendChild(targetPointX);

					Element targetPointY = emptyDocument.createElement("sml:TargetPointY");
					targetPointY.appendChild(emptyDocument.createTextNode(DoubleUtilities.toString(bean.getPointRefer().getY(), 10)));
					controlPoint.appendChild(targetPointY);
				}
				root.appendChild(controlPoint);
			}
		}

		return XmlUtilities.nodeToString(emptyDocument, "UTF-8");
	}

}
