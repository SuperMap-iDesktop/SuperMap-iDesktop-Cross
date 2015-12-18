package com.supermap.desktop;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import com.supermap.desktop.ui.FormBase;
import com.supermap.desktop.utilties.PathUtilties;

/**
 *
 * @author Administrator
 */
public class MainFrame extends FormBase implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new form MainFrame
	 */
	public MainFrame() {

		this.setSize(1000, 640);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setText("SuperMap iDesktop Cross 8C");

		// 换成自己的图标：
		String path = PathUtilties.getRootPathName();
		String[] paths = new String[2];
		paths[0] = path;
		paths[1] = "../Resources/Frame";
		path = PathUtilties.combinePath(paths, true);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		ArrayList<Image> images = new ArrayList<Image>();
		images.add(toolkit.createImage(path + "iDesktop_Cross_16.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_24.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_32.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_64.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_128.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_256.png"));
		images.add(toolkit.createImage(path + "iDesktop_Cross_512.png"));
		this.setIconImages(images);
		this.setVisible(true);
		this.addWindowListener(this);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// nothing
 }

	@Override
	public void windowClosing(WindowEvent e) {

		// 先把窗口关闭状态恢复成默认状态：收到关闭消息即退出
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		boolean result = CommonToolkit.WorkspaceWrap.closeWorkspace();
		if (!result) {
			// 取消关闭操作
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// nothing
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// nothing

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// nothing

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// nothing

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// nothing

	}

}
