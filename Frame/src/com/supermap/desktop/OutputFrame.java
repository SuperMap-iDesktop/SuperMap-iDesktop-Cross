package com.supermap.desktop;

import javax.swing.JTextArea;

import com.supermap.desktop.Interface.IOutput;
import com.supermap.desktop.enums.InfoType;

public class OutputFrame /*extends JTextArea*/ implements IOutput {
	
	private JTextArea jTextArea = null;
	public JTextArea getTextArea() {
		return this.jTextArea;
	}
	
	public void setTextArea(JTextArea textArea) {
		this.jTextArea = textArea;
	}
	
	public OutputFrame() {
		this.jTextArea = new JTextArea();
		this.jTextArea.setEditable(false);
		Application.getActiveApplication().setOutput(this);
	}

	@Override
	public String getLineText(int index) {
		return null;
	}

	@Override
	public int getLineCount() {
		return 0;
	}

	@Override
	public int getMaxLineCount() {
		return 0;
	}

	@Override
	public void setMaxLineCount(int maxCount) {
		// nothing
	}

	@Override
	public Boolean getIsWordWrapped() {
		return true;
	}

	@Override
	public void setIsWordWrapped(Boolean isWordWrapped) {
		// nothing
	}

	@Override
	public Boolean getIsTimePrefixAdded() {
		return true;
	}

	@Override
	public void setIsTimePrefixAdded(Boolean isTimePrefixAdded) {
		// nothing
	}

	@Override
	public String getTimePrefixFormat() {
		return null;
	}

	@Override
	public void setTimePrefixFormat(String timePrefixFormat) {
		// nothing
	}

	@Override
	public void output(String message) {
		try {
			String oldMessage = this.jTextArea.getText();
			if ( oldMessage.length() > 0) {
				this.jTextArea.setText(oldMessage + "\r\n" + message);
			} else {
				this.jTextArea.setText(message);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
	
	@Override
	public void output(Exception exception) {
		try {
			output(exception.getMessage(), InfoType.Exception);
			StackTraceElement[] elements = exception.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				output(elements[i].toString(), InfoType.Exception);
			}
			
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void output(String message, InfoType type) {
		try {
			if (type == InfoType.Information) {
				this.jTextArea.setText(message + "\r\n");
			}
			else {
				this.jTextArea.setText(message + "\r\n");
			}			
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void clearOutput() {
		try {
			this.jTextArea.setText("");			
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean canCopy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void copy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canClear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
