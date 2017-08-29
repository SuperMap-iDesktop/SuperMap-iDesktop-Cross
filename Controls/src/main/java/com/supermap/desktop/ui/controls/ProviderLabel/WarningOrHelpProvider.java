package com.supermap.desktop.ui.controls.ProviderLabel;

import com.supermap.desktop.controls.utilities.ControlsResources;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lixiaoyao on 2017/3/3.
 */
public class WarningOrHelpProvider extends JLabel {
    private String tipText = "";

	public static final boolean WARING = true;
	public static final boolean HELP = false;


	public WarningOrHelpProvider(String tipText, boolean type) {
		this.tipText = tipText;
		if (type) {
			this.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
        } else {
            this.setIcon(ControlsResources.getIcon("/controlsresources/Icon_Help.png"));
        }
        this.setToolTipText(this.tipText);
        this.setPreferredSize(new Dimension(23, 23));
    }

    /**
     * 有时候需要隐藏警告信息，并且不影响控件布局，特殊情况下需要显示警告信息，此时显示的警告信息是初始化控件的默认信息
     */
    public void showWarning() {
        this.setText("");
        this.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
        this.setToolTipText(this.tipText);
    }

    /**
     * 有时候需要隐藏警告信息，并且不影响控件布局，特殊情况下需要显示警告信息，此时显示的警告信息为传进来的警告信息
     */
    public void showWarning(String currentShowWarningInformation) {
        this.setText("");
        this.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
        this.setToolTipText(currentShowWarningInformation);
    }

    /**
     * 特殊情况需要隐藏警告信息，并且不影响控件布局
     */
    public void hideWarning() {
        this.setText("");
        this.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/clarity.png"));
    }
}
