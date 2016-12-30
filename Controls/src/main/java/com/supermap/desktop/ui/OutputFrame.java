package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.Interface.IOutput;
import com.supermap.desktop.enums.InfoType;
import com.supermap.desktop.ui.controls.Dockbar;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.utilities.LogUtilities;
import org.flexdock.docking.DockingManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OutputFrame extends JScrollPane implements IOutput {

	private static final long serialVersionUID = 1L;
	private transient boolean isShowTime = true;
	private JTextArea textArea = new JTextArea();
	private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

	public JTextArea getTextArea() {
		return this.textArea;
	}

	private JPopupMenu outputPopupMenu = null;

	/**
	 * 获取输出窗口的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getOutputPopupMenu() {
		return this.outputPopupMenu;
	}

	private transient MouseListener Output_MouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int buttonType = e.getButton();
			int clickCount = e.getClickCount();

			if (buttonType == MouseEvent.BUTTON3 && clickCount == 1) {
				getOutputPopupMenu().show((Component) textArea, (int) e.getPoint().getX(), (int) e.getPoint().getY());
			}
		}
	};

	public OutputFrame() {
		this.setViewportView(this.textArea);
		this.textArea.setEditable(false);
		this.textArea.addMouseListener(Output_MouseListener);

		if (Application.getActiveApplication().getMainFrame() != null) {
			IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
			this.outputPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.Output.ContextMenu");
		}
	}

	@Override
	public String getLineText(int index) {
		return this.textArea.getText();
	}

	@Override
	public int getLineCount() {
		return this.textArea.getRows();
	}

	@Override
	public boolean canCopy() {
		boolean result = true;
		String selectedText = this.textArea.getSelectedText();
		if (selectedText == null || "".equals(selectedText)) {
			result = false;
		}
		return result;
	}

	@Override
	public void copy() {
		this.textArea.copy();
	}

	@Override
	public boolean canClear() {
		boolean result = true;
		String text = this.textArea.getText();
		if (text == null || "".equals(text)) {
			result = false;
		}
		return result;
	}

	@Override
	public void clear() {
		this.textArea.setText("");
	}

	@Override
	public int getMaxLineCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLineCount(int maxCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean getIsWordWrapped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsWordWrapped(Boolean isWordWrapped) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean getIsTimePrefixAdded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsTimePrefixAdded(Boolean isTimePrefixAdded) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTimePrefixFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTimePrefixFormat(String timePrefixFormat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void output(String message) {
		if (GlobalParameters.isLogInformation()) {
			output(message, true);
		}

	}

	@Override
	public void output(Exception exception) {
		try {
			if (!GlobalParameters.isLogException()) {
				return;
			}
			LogUtilities.error(exception.getMessage(), exception);
			output(exception.getMessage(), InfoType.Exception);
			StackTraceElement[] elements = exception.getStackTrace();
			for (StackTraceElement element : elements) {
				output(element.toString(), InfoType.Exception);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void output(Throwable e) {
		if (!GlobalParameters.isLogException()) {
			return;
		}
		output(e.getMessage(), InfoType.Exception);
		StackTraceElement[] elements = e.getStackTrace();
		for (StackTraceElement element : elements) {
			output(element.toString(), InfoType.Exception);
		}
	}

	/**
	 * 输出信息，判断是否打印log日志
	 *
	 * @param message
	 * @param isOutputLog
	 */
	private void output(String message, boolean isOutputLog) {
		if (isOutputLog) {
			LogUtilities.outPut(message);
		}
		if (!isShowing()) {
			IFormMain formMain = Application.getActiveApplication().getMainFrame();
			IDockbar outputDockBar = ((DockbarManager) formMain.getDockbarManager())
					.getOutputFrame();
			if (outputDockBar != null) {
				//setvisible注释后输出框口的停靠位置不对，不注释每次弹出了两次（不是很明显）
				outputDockBar.setVisible(true);
				DockingManager.display(((Dockbar) outputDockBar).getView());
			}
		}
		String messageTemp = message;
		try {
			if (isShowTime) {
				messageTemp = "[" + this.df.format(new Date()) + "] " + messageTemp;
			}

			if (this.textArea.getText().length() > 0) {
				textArea.append(System.lineSeparator());
			}
			textArea.append(messageTemp);
			textArea.setCaretPosition(textArea.getDocument().getLength());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void timeShowStateChange() {
		isShowTime = !isShowTime;
	}

	@Override
	public void output(String message, InfoType type) {
		try {
			if (type == InfoType.Information) {
				if (GlobalParameters.isLogInformation()) {
					output(message, true);
				}
			} else {
				if (GlobalParameters.isLogException()) {
					output(message, false);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void clearOutput() {
		try {
			this.textArea.setText("");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);

		}
	}

	/**
	 * 是否显示时间
	 *
	 * @return
	 */
	public boolean isShowTime() {
		return isShowTime;
	}

}
