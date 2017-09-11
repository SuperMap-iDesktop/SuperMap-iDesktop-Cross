package com.supermap.desktop.process.core;

/**
 * @author XiaJT
 */
public class ReadyEvent<E> {
	private boolean isOutputMessage;
	private E sourceData;

	public ReadyEvent(E sourceData, boolean isOutputMessage) {
		this.sourceData = sourceData;
		this.isOutputMessage = isOutputMessage;
	}

	public boolean isOutputMessage() {
		return isOutputMessage;
	}

	public void setOutputMessage(boolean outputMessage) {
		isOutputMessage = outputMessage;
	}

	public E getSourceData() {
		return sourceData;
	}

	public void setSourceData(E sourceData) {
		this.sourceData = sourceData;
	}
}
