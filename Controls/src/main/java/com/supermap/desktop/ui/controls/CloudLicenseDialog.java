package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.ui.controls.button.SmButton;
import sun.net.www.http.HttpClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * Created by xie on 2016/12/20.
 */
public class CloudLicenseDialog extends SmDialog {
    private JPanel panelCloudImage;
    private JPanel panelUserImage;
    private JLabel labelUserName;
    private JLabel labelPassWord;
    private JTextField textFieldUserName;
    private JPasswordField fieldPassWord;
    private JLabel labelRegister;
    private JLabel labelFindPassword;
    private JCheckBox checkBoxSavePassword;
    private JCheckBox checkBoxAutoLogin;
    private SmButton buttonLogin;
    private JButton buttonClose;
    private static final String REGIST_URL = "https://sso.supermap.com/register?service=http://www.supermapol.com";
    private static final String PASSWORD_URL = "https://sso.supermap.com/v101/cas/password?service=http://itest.supermapol.com";

    private MouseListener registListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            connect(REGIST_URL);
        }
    };
    private MouseListener findPasswordListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            connect(PASSWORD_URL);
        }
    };
    private ActionListener closeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeEvents();
            CloudLicenseDialog.this.dispose();
        }
    };

    private void connect(String url) {
        URI uri = URI.create(url);
        Desktop dp = Desktop.getDesktop();
        if (dp.isSupported(Desktop.Action.BROWSE)) {
            try {
                dp.browse(uri);// 获取系统默认浏览器打开网页
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private ActionListener loginListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = textFieldUserName.getText();
            String passWord = String.valueOf(fieldPassWord.getPassword());
            try {
                login(userName, passWord);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    };

    private void login(String userName, String passWord) {
        String urlStr = "http://www.supermapol.com/shiro-cas";
        try {
            URL url = new URL(urlStr);
            HttpClient client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CloudLicenseDialog() {
        init();
    }

    private void init() {
        initComponents();
        initLayout();
        initResources();
        registEvents();
        this.setSize(new Dimension(480, 330));
        this.setLocationRelativeTo(null);
    }

    private void initResources() {
        this.panelCloudImage.add(new JLabel(ControlsResources.getIcon("/controlsresources/Image_CloudClient.png")));
        this.panelUserImage.add(new JLabel(ControlsResources.getIcon("/controlsresources/Image_Regist.png")));

        this.setTitle(ControlsProperties.getString("String_UserManage_Login"));
        this.labelUserName.setText(ControlsProperties.getString("String_FormLogin_UserName"));
        this.labelPassWord.setText(ControlsProperties.getString("String_FormLogin_Password"));
        this.labelRegister.setText("<html><font color='blue' size='24px'><a href=" + "\"" + REGIST_URL + "\"" + " target=\"_blank\">" + ControlsProperties.getString("String_FormLogin_Register") + "</a></font></html>");
        this.labelFindPassword.setText("<html><font color='blue' size='24px'><a href=" + "\"" + PASSWORD_URL + "\"" + " target=\"_blank\">" + ControlsProperties.getString("String_FormLogin_GetBack") + "</a></font></html>");
        this.checkBoxAutoLogin.setText(ControlsProperties.getString("String_FormLogin_AutoLogin"));
        this.checkBoxSavePassword.setText(ControlsProperties.getString("String_FormLogin_SavePassword"));
        this.buttonLogin.setText(ControlsProperties.getString("String_UserManage_Login"));
    }

    private void registEvents() {
        removeEvents();
        this.labelRegister.addMouseListener(this.registListener);
        this.labelFindPassword.addMouseListener(this.findPasswordListener);
        this.buttonClose.addActionListener(this.closeListener);
        this.buttonLogin.addActionListener(this.loginListener);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                CloudLicenseDialog.this.dispose();
            }
        });
    }

    private void removeEvents() {
        this.labelRegister.removeMouseListener(this.registListener);
        this.labelFindPassword.removeMouseListener(this.findPasswordListener);
        this.buttonClose.removeActionListener(this.closeListener);
        this.buttonLogin.removeActionListener(this.loginListener);
    }

    private void initLayout() {
        this.setLayout(new GridBagLayout());

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonLogin, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
        panelButton.add(this.buttonClose, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));

        this.add(panelCloudImage, new GridBagConstraintsHelper(0, 0, 6, 3).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(panelUserImage, new GridBagConstraintsHelper(0, 3, 1, 3).setInsets(10, 0, 0, 0).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(labelUserName, new GridBagConstraintsHelper(1, 3, 1, 1).setInsets(10, 5, 10, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(textFieldUserName, new GridBagConstraintsHelper(2, 3, 2, 1).setInsets(10, 0, 10, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(labelRegister, new GridBagConstraintsHelper(4, 3, 1, 1).setInsets(10, 5, 5, 16).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(labelPassWord, new GridBagConstraintsHelper(1, 4, 1, 1).setInsets(0, 5, 0, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(fieldPassWord, new GridBagConstraintsHelper(2, 4, 2, 1).setInsets(0, 0, 0, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(labelFindPassword, new GridBagConstraintsHelper(4, 4, 1, 1).setInsets(0, 5, 0, 16).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(checkBoxSavePassword, new GridBagConstraintsHelper(2, 5, 1, 1).setInsets(0, 5, 5, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(checkBoxAutoLogin, new GridBagConstraintsHelper(3, 5, 1, 1).setInsets(0, 0, 5, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(panelButton, new GridBagConstraintsHelper(0, 6, 5, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
        this.labelRegister.setPreferredSize(new Dimension(100, 23));
        this.labelFindPassword.setPreferredSize(new Dimension(100, 23));
    }


    private void initComponents() {
        this.panelCloudImage = new JPanel();
        this.panelUserImage = new JPanel();
        this.labelUserName = new JLabel();
        this.labelPassWord = new JLabel();
        this.textFieldUserName = new JTextField();
        this.fieldPassWord = new JPasswordField();
        this.labelRegister = new JLabel();
        this.labelFindPassword = new JLabel();
        this.checkBoxSavePassword = new JCheckBox();
        this.checkBoxAutoLogin = new JCheckBox();
        this.buttonLogin = new SmButton();
        this.buttonClose = ComponentFactory.createButtonClose();
        this.checkBoxSavePassword.setSelected(true);
//        this.buttonLogin.setEnabled(false);
    }

}

