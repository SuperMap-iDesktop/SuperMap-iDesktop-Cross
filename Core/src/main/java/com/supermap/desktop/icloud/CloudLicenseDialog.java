package com.supermap.desktop.icloud;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.icloud.api.LicenseService;
import com.supermap.desktop.icloud.commontypes.ApplyFormalLicenseResponse;
import com.supermap.desktop.icloud.commontypes.ApplyTrialLicenseResponse;
import com.supermap.desktop.icloud.commontypes.LicenseId;
import com.supermap.desktop.icloud.commontypes.ProductType;
import com.supermap.desktop.icloud.impl.LicenseServiceFactory;
import com.supermap.desktop.icloud.online.AuthenticationException;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.PathUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;

/**
 * Created by xie on 2016/12/20.
 */
public class CloudLicenseDialog extends JDialog {
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
    private JButton buttonLogin;
    private JButton buttonClose;
    private LicenseService licenseService;
    private ApplyFormalLicenseResponse formalLicenseResponse;//用于归还的正式许可信息
    private ApplyTrialLicenseResponse trialLicenseResponse;//用于归还的试用许可信息
    private static final String REGIST_URL = "https://sso.supermap.com/register?service=http://www.supermapol.com";
    private static final String PASSWORD_URL = "https://sso.supermap.com/v101/cas/password?service=http://itest.supermapol.com";
    private static final String TOKEN_PATH = PathUtilities.getFullPathName("../Configuration/token", false);
    private static final int USER_NAME = 0;
    private static final int PASS_WORD = 1;
    private static final int SAVE_TOKEN = 2;
    private static final int AUTO_LOGIN = 3;
    public static final int DIALOGRESULT_OK = 4;
    public static final int DIALOGRESULT_CANCEL = 5;

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
    private int dialogResult;
    private LicenseId licenseId;

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
            login();
        }
    };

    private void login() {
        String userName = textFieldUserName.getText();
        String passWord = String.valueOf(fieldPassWord.getPassword());
        try {
            licenseService = LicenseServiceFactory.create(userName, passWord, ProductType.IDESKTOP);
            licenseId = LicenseManager.getFormalLicenseId(licenseService);
            if (null != licenseId) {
                //有正式许可id，则申请正式许可
                formalLicenseResponse = LicenseManager.applyFormalLicense(licenseService, licenseId);
                dialogResult = DIALOGRESULT_OK;
                saveToken();
            } else {
                //没有正式许可id,则申请试用许可
                trialLicenseResponse = LicenseManager.applyTrialLicense(licenseService);
                dialogResult =DIALOGRESULT_OK;
                saveToken();
            }
        } catch (AuthenticationException e1) {
            JOptionPaneUtilities.showMessageDialog(CommonProperties.getString("String_PermissionCheckFailed"));
            dialogResult = DIALOGRESULT_CANCEL;
        } finally {
            removeEvents();
            dispose();
        }
    }

    private void saveToken() {
        FileOutputStream outPutStream = null;
        String userName = textFieldUserName.getText() + ",";
        String password = String.copyValueOf(fieldPassWord.getPassword());
        String saveToken = checkBoxSavePassword.isSelected() ? "true" : "false";
        String autoLogin = checkBoxAutoLogin.isSelected() ? "true" : "false";
        try {
            outPutStream = new FileOutputStream(TOKEN_PATH);
            outPutStream.write(userName.getBytes());
            outPutStream.write(password.getBytes());
            outPutStream.write(",".getBytes());
            outPutStream.write(saveToken.getBytes());
            outPutStream.write(",".getBytes());
            outPutStream.write(autoLogin.getBytes());
        } catch (FileNotFoundException e) {
            Application.getActiveApplication().getOutput().output(e);
        } catch (IOException e) {
            Application.getActiveApplication().getOutput().output(e);
        } finally {
            try {
                outPutStream.close();
            } catch (IOException e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        }

    }

    /**
     * // 显示对话框，不过滤字段类型
     *
     * @param
     */
    public int showDialog() {
        init();
        this.setVisible(true);
        return dialogResult;
    }

    public CloudLicenseDialog() {
        super();
        setModal(true);
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
        this.panelCloudImage.add(new JLabel(CoreResources.getIcon("/coreresources/Image_CloudClient.png")));
        this.panelUserImage.add(new JLabel(CoreResources.getIcon("/coreresources/Image_Regist.png")));

        this.setTitle(CoreProperties.getString("String_UserManage_Login"));
        this.labelUserName.setText(CoreProperties.getString("String_FormLogin_UserName"));
        this.labelPassWord.setText(CoreProperties.getString("String_FormLogin_Password"));
        this.labelRegister.setText("<html><font color='blue' size='24px'><a href=" + "\"" + REGIST_URL + "\"" + " target=\"_blank\">" + CoreProperties.getString("String_FormLogin_Register") + "</a></font></html>");
        this.labelFindPassword.setText("<html><font color='blue' size='24px'><a href=" + "\"" + PASSWORD_URL + "\"" + " target=\"_blank\">" + CoreProperties.getString("String_FormLogin_GetBack") + "</a></font></html>");
        this.checkBoxAutoLogin.setText(CoreProperties.getString("String_FormLogin_AutoLogin"));
        this.checkBoxSavePassword.setText(CoreProperties.getString("String_FormLogin_SavePassword"));
        this.buttonLogin.setText(CoreProperties.getString("String_UserManage_Login"));
        this.buttonClose.setText(CoreProperties.getString("String_Close"));
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
        this.buttonLogin = new JButton();
        this.buttonClose = new JButton();
        initToken();
    }

    private void initToken() {
        FileReader reader = null;
        try {
            File file = new File(TOKEN_PATH);
            if (file.exists()) {
                reader = new FileReader(file);
                char[] charArray = new char[(int) file.length()];
                reader.read(charArray);
                String[] token = String.valueOf(charArray).split(",");
                if (null != token && token.length == 4) {
                    boolean savePassword = "true".equalsIgnoreCase(token[SAVE_TOKEN]);
                    boolean autoLogin = "true".equalsIgnoreCase(token[AUTO_LOGIN]);
                    checkBoxSavePassword.setSelected(savePassword);
                    checkBoxAutoLogin.setSelected(autoLogin);
                    if (savePassword) {
                        textFieldUserName.setText(token[USER_NAME]);
                        fieldPassWord.setText(token[PASS_WORD]);
                    }
                    this.buttonLogin.setEnabled(!autoLogin);
                    if (autoLogin) {
                        login();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            Application.getActiveApplication().getOutput().output(e);
        } catch (IOException e) {
            Application.getActiveApplication().getOutput().output(e);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        }
    }

    public LicenseId getLicenseId() {
        return licenseId;
    }

    public LicenseService getLicenseService() {
        return licenseService;
    }

    public ApplyFormalLicenseResponse getFormalLicenseResponse() {
        return formalLicenseResponse;
    }

    public ApplyTrialLicenseResponse getTrialLicenseResponse() {
        return trialLicenseResponse;
    }
}

