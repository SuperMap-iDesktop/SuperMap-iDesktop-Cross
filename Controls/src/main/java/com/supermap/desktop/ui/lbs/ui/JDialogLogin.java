package com.supermap.desktop.ui.lbs.ui;

import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.lbs.IServerServiceImpl;
import com.supermap.desktop.lbs.Interface.IServerService;
import com.supermap.desktop.lbs.params.IServerLoginInfo;
import com.supermap.desktop.utilities.StringUtilities;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by xie on 2017/1/6.
 */
public class JDialogLogin extends SmDialog {

    private final String LOCALHOST = "localhost";
    private JRadioButton radioButtonLocalHost;
    private JRadioButton radioButtonRemoteHost;
    private JLabel labelServer;
    private JTextField textFieldHost;
    private JLabel labelColon;
    private JTextField textFieldPort;

    private JLabel labelUserName;
    private JLabel labelPassword;
    private JTextField textFieldUserName;
    private JPasswordField textFieldPassword;
    private SmButton buttonLogin;
    private JButton buttonClose;
    private JLabel labelWorning;
    private JCheckBox checkBoxSaveLoginInfo;
    private String ipAddr = "";

    private ActionListener loginListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            loginToIserver();
        }

    };
    private ActionListener closeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeEvents();
            saveLoginInfo();
            dispose();
        }
    };
    private ActionListener localHostListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ipAddr = textFieldHost.getText();
            textFieldHost.setText(LOCALHOST);
            textFieldHost.setEnabled(false);
        }
    };
    private ActionListener remoteHostListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            textFieldHost.setText(ipAddr);
            textFieldHost.setEnabled(true);
            if (checkBoxSaveLoginInfo.isSelected()) {
                IServerLoginInfo.remoteHost = radioButtonRemoteHost.isSelected();
            }
        }
    };
    private ActionListener saveLoginInfoListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveLoginInfo();
        }
    };

    private void saveLoginInfo() {
        if (checkBoxSaveLoginInfo.isSelected()) {
            IServerLoginInfo.ipAddr = textFieldHost.getText();
            IServerLoginInfo.port = textFieldPort.getText();
            IServerLoginInfo.username = textFieldUserName.getText();
            IServerLoginInfo.password = textFieldPassword.getText();
            IServerLoginInfo.saveLoginInfo = true;
        } else {
            IServerLoginInfo.ipAddr = "";
            IServerLoginInfo.port = "";
            IServerLoginInfo.username = "";
            IServerLoginInfo.password = "";
            IServerLoginInfo.saveLoginInfo = false;
        }
        IServerLoginInfo.remoteHost = radioButtonRemoteHost.isSelected();
    }

    private void loginToIserver() {
        String username = textFieldUserName.getText();
        String password = textFieldPassword.getText();
        IServerService service = new IServerServiceImpl();
        if (!StringUtilities.isNullOrEmpty(textFieldHost.getText()) && !StringUtilities.isNullOrEmpty(textFieldPort.getText())) {
            IServerLoginInfo.ipAddr = textFieldHost.getText();
            IServerLoginInfo.port = textFieldPort.getText();
        }
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        CloseableHttpClient client = service.login(username, password);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (IServerLoginInfo.error) {
            return;
        }
        if (null == client) {
            labelWorning.setForeground(Color.red);
            labelWorning.setText(CommonProperties.getString("String_PermissionCheckFailed"));
        } else {
            IServerLoginInfo.client = client;
            IServerLoginInfo.login = true;
            if (IServerLoginInfo.saveLoginInfo) {
                IServerLoginInfo.username = username;
                IServerLoginInfo.password = password;
            }
            dialogResult = DialogResult.OK;
            dispose();
        }
    }

    public JDialogLogin() {

    }

    public DialogResult showDialog() {
        init();
        return dialogResult;
    }

    private void init() {
        initComponents();
        initResources();
        registEvents();
        setSize(new Dimension(450, 356));
        this.getRootPane().setDefaultButton(this.buttonLogin);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registEvents() {
        removeEvents();
        this.buttonLogin.addActionListener(loginListener);
        this.buttonClose.addActionListener(closeListener);
        this.radioButtonLocalHost.addActionListener(localHostListener);
        this.radioButtonRemoteHost.addActionListener(remoteHostListener);
        this.checkBoxSaveLoginInfo.addActionListener(saveLoginInfoListener);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                saveLoginInfo();
                removeEvents();
            }
        });
    }

    private void removeEvents() {
        this.buttonLogin.removeActionListener(loginListener);
        this.buttonClose.removeActionListener(closeListener);
        this.radioButtonLocalHost.removeActionListener(localHostListener);
        this.radioButtonRemoteHost.removeActionListener(remoteHostListener);
    }

    private void initResources() {
        this.setTitle(CoreProperties.getString("String_LoginToIserver"));
        this.buttonLogin.setText(CoreProperties.getString("String_UserManage_Login"));
        this.radioButtonLocalHost.setText(CoreProperties.getString("String_LocalHost"));
        this.radioButtonRemoteHost.setText(CoreProperties.getString("String_RemoteHost"));
        this.labelServer.setText(CoreProperties.getString("String_Server"));
        this.labelUserName.setText(CoreProperties.getString("String_FormLogin_UserName"));
        this.labelPassword.setText(CoreProperties.getString("String_FormLogin_Password"));
        this.checkBoxSaveLoginInfo.setText(CoreProperties.getString("String_SaveLoginInfo"));
        this.textFieldPort.setText("8090");
    }

    private void initComponents() {
        // 服务器设置面板
        JPanel panelService = new JPanel();
        panelService.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString("String_Services")));
        this.radioButtonLocalHost = new JRadioButton();
        this.radioButtonRemoteHost = new JRadioButton();
        this.labelServer = new JLabel();
        this.textFieldHost = new JTextField(LOCALHOST);
        this.labelColon = new JLabel(":");
        this.textFieldPort = new JTextField();
        this.textFieldPort.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.radioButtonLocalHost);
        buttonGroup.add(this.radioButtonRemoteHost);
        panelService.setLayout(new GridBagLayout());
        this.radioButtonLocalHost.setSelected(true);
        this.textFieldHost.setEnabled(false);
        this.checkBoxSaveLoginInfo = new JCheckBox();

        panelService.add(this.radioButtonLocalHost, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(10, 10, 0, 0));
        panelService.add(this.radioButtonRemoteHost, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(10, 5, 0, 0));
        panelService.add(this.labelServer, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 10, 0));
        panelService.add(this.textFieldHost, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 5, 10, 0));
        panelService.add(this.labelColon, new GridBagConstraintsHelper(2, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 10, 0));
        panelService.add(this.textFieldPort, new GridBagConstraintsHelper(3, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 5, 10, 10));

        // 用户设置面板
        JPanel panelUser = new JPanel();
        panelUser.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString("String_Admin")));
        this.labelUserName = new JLabel("UserName:");
        this.textFieldUserName = new JTextField();
        this.textFieldUserName.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
        this.labelPassword = new JLabel("Password:");
        this.textFieldPassword = new JPasswordField();
        this.textFieldPassword.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
        this.labelWorning = new JLabel(" ");
        boolean saveInfo = IServerLoginInfo.saveLoginInfo;
        if (saveInfo) {
            this.checkBoxSaveLoginInfo.setSelected(saveInfo);
            boolean remoteHost = IServerLoginInfo.remoteHost;
            this.radioButtonRemoteHost.setSelected(remoteHost);
            if (remoteHost) {
                this.textFieldHost.setText(IServerLoginInfo.ipAddr);
                this.textFieldHost.setEnabled(true);
            }
            this.textFieldUserName.setText(IServerLoginInfo.username);
            this.textFieldPassword.setText(IServerLoginInfo.password);

        }
        panelUser.setLayout(new GridBagLayout());
        panelUser.add(this.labelWorning, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(10, 5, 0, 10));
        panelUser.add(this.labelUserName, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(10, 10, 10, 0));
        panelUser.add(this.textFieldUserName, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(10, 5, 10, 10));
        panelUser.add(this.labelPassword, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(0, 10, 10, 0));
        panelUser.add(this.textFieldPassword, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 10, 10));
        panelUser.add(this.checkBoxSaveLoginInfo, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(0, 5, 10, 10));

        // 主面板
        JPanel panelMain = new JPanel();
        setContentPane(panelMain);
        this.buttonLogin = new SmButton();
        this.buttonClose = ComponentFactory.createButtonClose();

        setLayout(new GridBagLayout());
        panelMain.add(panelService, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setInsets(10, 10, 0, 10));
        panelMain.add(panelUser, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
        panelMain.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(0, 1));
        panelMain.add(this.buttonLogin, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 10, 0));
        panelMain.add(this.buttonClose, new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 10, 10));
    }

}
