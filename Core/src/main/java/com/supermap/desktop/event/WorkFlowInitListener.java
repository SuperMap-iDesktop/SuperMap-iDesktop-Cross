package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IWorkFlow;
import org.w3c.dom.Element;

/**
 * @author XiaJT
 */
public abstract class WorkFlowInitListener {
	public abstract IWorkFlow init(Element element);
}
