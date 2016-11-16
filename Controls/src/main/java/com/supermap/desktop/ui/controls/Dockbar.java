package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.ui.XMLDockbar;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;

import java.awt.*;

/**
 * iDesktop java 第一个版本，DockBar 不做关闭桌面保存的功能。 DockBar 的实现依赖于所用的开源库，IDockBar 所定义的属性 并非 DockBar 本身的属性，而是由 DockBar 与 DockBar 的 Parent 所处状态来共同决定的，因此在本类中，IDockBar
 * 的具体实现 不能简单的从自身进行获取，目前仅简单的直接读取 XMLDockbar 的值，但是 XMLDockbar 的值仅仅只是桌面初始化配置文件得到的， 并不能表示 DockBar 的当前状态，因此后续版本做保存的时候， 需要重新设计一下 DockBar 的结构。 当前版本如果尝试通过 DockBar
 * 的实例在代码中设置一些本身并不支持的属性，暂时抛出 UnsupportedOperationException 的异常。
 *
 * @author wuxb
 */
public class Dockbar extends View implements IDockbar {
	private static final long serialVersionUID = -741186257818181105L;

	private static final int SHOWN = 0;
	private static final int HIDDEN = 1;
	private static final int FOCUS_CHANGED = 2;
	private static final int ADDED = 3;
	private static final int REMOVED = 4;
	private static final int CLOSING = 5;
	private static final int CLOSED = 6;
	private static final int UNDOCKING = 7;
	private static final int UNDOCKED = 8;
	private static final int DOCKING = 9;
	private static final int DOCKED = 10;
	private static final int MINIMIZED = 11;
	private static final int MAXIMIZED = 12;
	private static final int RESTORED = 13;
	private static final int MAXIMIZING = 14;
	private static final int MINIMIZING = 15;
	private static final int RESTORING = 16;

	private transient XMLDockbar xmlDockbar = null;
	// 因为重写了 isVisible，Dockbar 的框架会使用这个方法来显示或者隐藏内部控件，之前在 Removed 和 Closed 中设置了 xmlDockbar.setVisible(false)，而
	// 进行 Undock 之类的操作同样会触发 Removed，具体是 Undocking-Hidden-Removed-Added-Undocked，导致内容控件消失。
	// 因为 Dockbar 本身没有 visible 这种属性，所以就需要在各种 Dockbar 的事件中自行实现，而实现过程中需要考虑某一个行为是由什么行为导致的，
	// 所以暂时定义这样一个变量来记录当前 Action，具体实现日后再说。
	private int currentAction = -1;

	// dockbar 内嵌的用户自定义控件
	private Component innerComponent;

	public int getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(int currentAction) {
		this.currentAction = currentAction;
	}

	public Dockbar(final XMLDockbar xmlDockbar) {
		super(xmlDockbar.getID(), xmlDockbar.getLabel());
		this.xmlDockbar = xmlDockbar;
		initialize();
//		this.getWindowProperties().setMaximizeEnabled(false);
//		this.getWindowProperties().setMinimizeEnabled(false);
//		this.addListener(new SetDockingWindowListener());
	}

	public boolean initialize() {
		if (this.xmlDockbar == null) {
			return false;
		}

		this.setVisible(xmlDockbar.getVisible());
		this.innerComponent = xmlDockbar.CreateComponent();
		getContentPane().add(this.innerComponent);
		return true;
	}

	@Override
	public boolean isVisible() {
		return this.xmlDockbar.getVisible();
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.xmlDockbar.setVisible(isVisible);
	}

	@Override
	public Component getInnerComponent() {
		return getContentPane().getComponent(0);
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
		setActive(true);
	}
}
