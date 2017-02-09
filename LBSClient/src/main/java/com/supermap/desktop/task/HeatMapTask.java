package com.supermap.desktop.task;

import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;

import javax.swing.*;
import java.util.concurrent.CancellationException;

/**
 * Created by xie on 2017/1/11.
 */
public class HeatMapTask extends Task {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public HeatMapTask() {
        super();
        progressBar.setString("");
        buttonRun.setVisible(false);
        labelTitle.setText(LBSClientProperties.getString("String_HeatMapInfo"));
        labelLogo.setIcon(CommonUtilities.getImageIcon("HeatMap.png"));
    }

    @Override
    public void updateProgress(final int percent, final String remainTime, String message) throws CancellationException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setValue(percent);
                labelStatus.setText("");
                if (percent == 100) {
                    labelTitle.setText(LBSClientProperties.getString("String_HeatMapFinished"));
                }
            }
        });
    }
}
