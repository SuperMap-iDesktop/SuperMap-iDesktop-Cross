package com.supermap.desktop.process.dataconversion;

import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/4/7.
 * Disused class
 */
public class ImportParameters extends DefaultParameters {
    private CopyOnWriteArrayList<IParameter> importParameters;

    public ImportParameters() {
        super();
    }

    public void toArray() {
        if (null == importParameters) {
            importParameters = new CopyOnWriteArrayList<>();
        }
        CopyOnWriteArrayList<IParameter> tempParameterArray = new CopyOnWriteArrayList<>(parameters);
        importParameters.addAll(tempParameterArray);
    }

    public void addParameter(IParameter parameter) {
        importParameters.add(parameter);
    }

    public void resetPanel() {
        if (panel != null && panel instanceof JPanel) {
            panel.removeAll();
            for (int i = 0; i < importParameters.size(); i++) {
                if (importParameters.get(i) instanceof JPanel) {
                    panel.add((JPanel) importParameters.get(i).getParameterPanel().getPanel(), new GridBagConstraintsHelper(0, i, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));
                }
            }
            panel.add(new JPanel(), new GridBagConstraintsHelper(0, parameters.length, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
            parameterPanel.setPanel(panel);
        }
    }
}
