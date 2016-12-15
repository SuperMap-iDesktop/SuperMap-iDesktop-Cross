package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.ui.XMLDockbar;
import org.apache.log4j.Hierarchy;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.view.View;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * iDesktop java 第一个版本，DockBar 不做关闭桌面保存的功能。 DockBar 的实现依赖于所用的开源库，IDockBar 所定义的属性 并非 DockBar 本身的属性，而是由 DockBar 与 DockBar 的 Parent 所处状态来共同决定的，因此在本类中，IDockBar
 * 的具体实现 不能简单的从自身进行获取，目前仅简单的直接读取 XMLDockbar 的值，但是 XMLDockbar 的值仅仅只是桌面初始化配置文件得到的， 并不能表示 DockBar 的当前状态，因此后续版本做保存的时候， 需要重新设计一下 DockBar 的结构。 当前版本如果尝试通过 DockBar
 * 的实例在代码中设置一些本身并不支持的属性，暂时抛出 UnsupportedOperationException 的异常。
 *
 * @author wuxb
 */
public class Dockbar implements IDockbar {
	private static final long serialVersionUID = -741186257818181105L;
	private static final int RESTORING = 16;

	private transient XMLDockbar xmlDockbar = null;
	// 因为重写了 isVisible，Dockbar 的框架会使用这个方法来显示或者隐藏内部控件，之前在 Removed 和 Closed 中设置了 xmlDockbar.setVisible(false)，而
	// 进行 Undock 之类的操作同样会触发 Removed，具体是 Undocking-Hidden-Removed-Added-Undocked，导致内容控件消失。
	// 因为 Dockbar 本身没有 visible 这种属性，所以就需要在各种 Dockbar 的事件中自行实现，而实现过程中需要考虑某一个行为是由什么行为导致的，
	// 所以暂时定义这样一个变量来记录当前 Action，具体实现日后再说。
	private int currentAction = -1;

	private View view;
	// dockbar 内嵌的用户自定义控件
	private Component innerComponent;

	public int getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(int currentAction) {
		this.currentAction = currentAction;
	}

	public Dockbar(final XMLDockbar xmlDockbar) {
		this.xmlDockbar = xmlDockbar;
		initialize();
	}

	public boolean initialize() {
		try {
			if (this.xmlDockbar == null) {
				return false;
			}

			this.view = new View(this.xmlDockbar.getID(), this.xmlDockbar.getTitle(), this.xmlDockbar.getTitle());
			this.view.addAction(DockingConstants.CLOSE_ACTION);
			this.view.addAction(DockingConstants.PIN_ACTION);
//		this.setVisible(xmlDockbar.getVisible());

			JPanel panel = new JPanel();
			panel.setBorder(new LineBorder(Color.GRAY, 1));
			panel.setLayout(new BorderLayout());
			panel.setLayout(new BorderLayout());
			this.view.setContentPane(panel);
			this.innerComponent = xmlDockbar.CreateComponent();
			panel.add(this.innerComponent);

			this.view.addDockingListener(new DockingListener() {
				@Override
				public void dockingComplete(DockingEvent evt) {

				}

				@Override
				public void dockingCanceled(DockingEvent evt) {

				}

				@Override
				public void dragStarted(DockingEvent evt) {

				}

				@Override
				public void dropStarted(DockingEvent evt) {

				}

				@Override
				public void undockingComplete(DockingEvent evt) {

				}

				@Override
				public void undockingStarted(DockingEvent evt) {

				}
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	public View getView() {
		return this.view;
	}

	public String getID() {
		return this.xmlDockbar.getID();
	}

	@Override
	public boolean isVisible() {
		return this.xmlDockbar.getVisible();
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.xmlDockbar.setVisible(isVisible);
		((DockbarManager) Application.getActiveApplication().getMainFrame().getDockbarManager()).display(this, isVisible);
	}

	@Override
	public Component getInnerComponent() {
		return this.innerComponent;
	}

	@Override
	public String getTitle() {
		return this.xmlDockbar.getTitle();
	}

	@Override
	public void setTitle(String title) {
		this.xmlDockbar.setTitle(title);
		this.view.setTitle(title);
	}

	@Override
	public boolean isActive() {
		return this.view.isActive();
	}

	@Override
	public PluginInfo getPluginInfo() {
		return this.xmlDockbar.getPluginInfo();
	}

	@Override
	public String getComponentName() {
		return this.innerComponent.getClass().getName();
	}

	@Override
	public void active() {
		this.view.setActive(true);
	}
}
