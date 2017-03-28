package com.supermap.desktop.ui.controls.ProviderLabel;

import com.supermap.desktop.controls.utilities.ControlsResources;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lixiaoyao on 2017/3/3.
 */
public class WarningOrHelpProvider extends JLabel {
    private String tipText="";
    public WarningOrHelpProvider(String tipText, boolean isWaringOrHelp) {
        this.tipText=tipText;
        if (isWaringOrHelp) {
            this.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
        } else {
            this.setIcon(ControlsResources.getIcon("/controlsresources/Icon_Help.png"));
        }
        this.setToolTipText(this.tipText);
        this.setPreferredSize(new Dimension(23,23));
    }

    public void showWarning(){
        this.setText("");
        this.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
        this.setToolTipText(this.tipText);
    }

    public void showWarning(String currentShowWarningInformation){
        this.setText("");
        this.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/warning.png"));
        this.setToolTipText(currentShowWarningInformation);
    }

    public void hideWarning(){
        this.setText(" ");
        this.setIcon(null);
    }
}
