package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 带文件选择按钮的textField
 *
 * @author xie
 */
public class FileChooserControl extends JComponent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JTextField textEditor;

    private String result;

    private JButton buttonDelete;

    private JButton button;

    public FileChooserControl() {
        this(null);
    }

    // 初始化带有默认文件路径的文件选择控件
    public FileChooserControl(String filePath) {
        initCompanent();
        setComponentName();
        setText(filePath);
        textEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textEditorValueChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textEditorValueChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textEditorValueChanged();
            }
        });
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setText("");
            }
        });
        textEditorValueChanged();
    }

    private void textEditorValueChanged() {
        String text = textEditor.getText();
//		if (buttonDelete.isVisible() != StringUtilities.isNullOrEmpty(text)) {
        this.buttonDelete.setEnabled(!StringUtilities.isNullOrEmpty(text));
//		}

    }


    public void setText(String filePath) {
        this.textEditor.setText(filePath);
    }


    @Override
    public void setEnabled(boolean flag) {
        this.textEditor.setEnabled(flag);
        this.buttonDelete.setEnabled(flag);
        this.button.setEnabled(flag);
    }

    // 初始化控件信息
    public void initCompanent() {
        this.textEditor = new JTextField(40);
        this.textEditor.setBackground(Color.white);
        this.textEditor.setAutoscrolls(true);
        this.textEditor.setHorizontalAlignment(JTextField.LEFT);

        Dimension buttonDimension = new Dimension(23, 23);
        this.button = new JButton();
        this.button.setPreferredSize(buttonDimension);
        this.button.setIcon(ControlsResources.getIcon("/controlsresources/Image_DatasetGroup_Normal.png"));
        this.button.setToolTipText(ControlsProperties.getString("String_Select"));
        this.button.setBorder(BorderFactory.createEtchedBorder(1));
        this.button.setContentAreaFilled(false);
        this.button.setBorderPainted(false);
        this.button.setFocusPainted(false);
        this.button.setFocusable(false);

        this.buttonDelete = new JButton();
        this.buttonDelete.setPreferredSize(buttonDimension);
        this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete_16.png"));
        this.buttonDelete.setToolTipText(CommonProperties.getString(CommonProperties.Delete));
        this.buttonDelete.setBorder(BorderFactory.createEtchedBorder(1));
        this.buttonDelete.setBorderPainted(false);
        this.buttonDelete.setContentAreaFilled(false);
        this.setLayout(new GridBagLayout());
        this.add(this.textEditor, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(2, 0));
        this.add(this.buttonDelete, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0).setInsets(0, 5, 0, 0));
        this.add(this.button, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
    }

    private void setComponentName() {
        ComponentUIUtilities.setName(this.textEditor, "FileChooserControl_textEditor");
        ComponentUIUtilities.setName(this.buttonDelete, "FileChooserControl_buttonDelete");
        ComponentUIUtilities.setName(this.button, "FileChooserControl_button");
    }

    public void setIcon(ImageIcon image) {
        this.button.setIcon(image);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public JButton getButton() {
        return button;
    }

    public JTextField getEditor() {
        return textEditor;
    }
}

