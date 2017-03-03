package com.supermap.desktop.ui.controls.ProviderLabel;

import com.supermap.desktop.controls.utilities.ControlsResources;

import javax.swing.*;

/**
 * Created by lixiaoyao on 2017/3/3.
 */
public class WarningOrHelpProvider extends JLabel {

    public WarningOrHelpProvider(String tipText, boolean isWaringOrHelp) {
        if (isWaringOrHelp) {
            this.setIcon(ControlsResources.getIcon("/controlsresources/Icon_Warning.png"));
        } else {
            this.setIcon(ControlsResources.getIcon("/controlsresources/Icon_Help.png"));
        }
        this.setToolTipText(tipText);
    }

}
