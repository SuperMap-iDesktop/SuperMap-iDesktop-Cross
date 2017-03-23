package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.desktop.Application;
import com.supermap.desktop.ScaleModel;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Map;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 固定比例尺控件
 *
 * @author xie
 */
public class ScaleEnabledContainer extends SmDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JToolBar toolbar;
    private JButton buttonSelectAll;
    private JButton buttonInvertSelect;
    private JButton buttonDelete;
    private JButton buttonImport;
    private JButton buttonExport;
    private JButton buttonOk;
    private JButton buttonCancel;
    private JScrollPane scrollPane;
    private JTable table;
    private List<String> scaleDisplays;
    private JPanel panelButton;
    private final String urlStr = "/coreresources/ToolBar/";
    private final Color selectColor = new Color(185, 214, 255);
    private Map map;
    private ActionListener localActionListener;
    private ActionListener panelButtonAction;
    private double[] scales;
    private MapBoundsPropertyControl control;
    private String[] title = {MapViewProperties.getString("String_Index"), MapViewProperties.getString("String_Scales")};
    private DecimalFormat format = new DecimalFormat("#.############");

    private TableModelListener tableModelListener = new TableModelListener() {

        @Override
        public void tableChanged(TableModelEvent e) {
            tableModelListener(e);
        }
    };
    private MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseReleased(MouseEvent e) {
            checkButtonState();
        }
    };

    public ScaleEnabledContainer() {
        initComponents();
        initResources();
        registEvents();
        checkButtonState();
        addFocusTraversalPolicyList();
        setModal(false);
    }

    public void init(MapBoundsPropertyControl control, Map map) {
        this.control = control;
        this.map = map;
        setVisible(true);
    }

    private void addFocusTraversalPolicyList() {
        this.componentList.add(this.buttonOk);
        this.componentList.add(this.buttonCancel);
        this.setFocusTraversalPolicy(this.policy);
        this.getRootPane().setDefaultButton(this.buttonOk);
    }

    private void registEvents() {
        this.localActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == buttonSelectAll) {
                    table.setRowSelectionInterval(0, table.getRowCount() - 1);
                    checkButtonState();
                    return;
                }
                if (e.getSource() == buttonInvertSelect) {
                    selectInvert(table);
                    checkButtonState();
                    return;
                }
                if (e.getSource() == buttonDelete) {
                    int[] selectRow = table.getSelectedRows();
                    List<String> removeInfo = new ArrayList<String>();
                    for (int i = selectRow.length - 1; i >= 0; i--) {
                        removeInfo.add((String) table.getValueAt(selectRow[i], 1));
                    }
                    scaleDisplays.removeAll(removeInfo);
                    getTable();
                    if (table.getRowCount() > 0) {
                        table.addRowSelectionInterval(0, 0);
                    }
                    checkButtonState();
                    return;
                }
                if (e.getSource() == buttonImport) {
                    importXml("ImportScales");
                    checkButtonState();
                    return;
                }
                if (e.getSource() == buttonExport) {
                    exportXml("ExportScales");
                    checkButtonState();
                    return;
                }
            }
        };
        this.panelButtonAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == buttonOk) {
                    dialogResult = DialogResult.OK;
                    if (scaleDisplays.size() > 0) {
                        removeRepeatStr(scaleDisplays);
                        scales = sort(scaleDisplays);
                        setPropertyControlFlag(true);
                    } else {
                        scales = new double[0];
                        setPropertyControlFlag(false);
                    }
                    MapBoundsPropertyControl.visibleScales = scales;
                    control.verify();
                    dispose();
                }
                if (e.getSource() == buttonCancel) {
                    dispose();
                }
            }

            private void setPropertyControlFlag(boolean flag) {
                MapBoundsPropertyControl.isVisibleScalesEnabled = flag;
                control.getCheckBoxIsVisibleScalesEnabled().setSelected(flag);
            }
        };
        unRegistEvents();
        this.buttonSelectAll.addActionListener(this.localActionListener);
        this.buttonInvertSelect.addActionListener(this.localActionListener);
        this.buttonDelete.addActionListener(this.localActionListener);
        this.buttonImport.addActionListener(this.localActionListener);
        this.buttonExport.addActionListener(this.localActionListener);
        this.buttonOk.addActionListener(this.panelButtonAction);
        this.buttonCancel.addActionListener(this.panelButtonAction);
        this.table.addMouseListener(this.mouseAdapter);
    }

    private void removeRepeatStr(List<String> scaleDisplays) {
        int count = scaleDisplays.size();
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (scaleDisplays.get(i).equals(scaleDisplays.get(j))) {
                    scaleDisplays.remove(scaleDisplays.get(i));
                    count -= 1;
                }
            }
        }
    }

    private double[] sort(List<String> scaleDisplays) {
        double[] needList = new double[scaleDisplays.size()];
        for (int i = 0; i < scaleDisplays.size(); i++) {
            try {
                needList[i] = new ScaleModel(scaleDisplays.get(i)).getScale();
            } catch (InvalidScaleException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < needList.length; i++) {
            for (int j = i + 1; j < needList.length; j++) {
                if (Double.compare(needList[i], needList[j]) > 0) {
                    double tempDouble = needList[i];
                    needList[i] = needList[j];
                    needList[j] = tempDouble;
                }
            }
        }
        scaleDisplays.clear();
        for (int i = 0; i < needList.length; i++) {
            try {
                scaleDisplays.add(new ScaleModel(needList[i]).getScaleCaption());
            } catch (InvalidScaleException e) {
                e.printStackTrace();
            }
        }
        return needList;
    }

    private void tableModelListener(TableModelEvent e) {
        checkButtonState();
        int selectRow = e.getFirstRow();
        if (selectRow > scaleDisplays.size()) {
            return;
        }
        String oldScale = scaleDisplays.get(selectRow);
        String selectScale = table.getValueAt(selectRow, 1).toString();

        if (!ScaleModel.isLegitScaleString(selectScale)) {
            setTableCell(selectRow, oldScale);
            Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_ErrorInput"));
            return;
        }
        if (ScaleModel.isLegitScaleString(selectScale) && selectScale.contains(":")) {
            setTableCell(selectRow, selectScale);
            return;
        }
        if (!selectScale.contains(":") && ScaleModel.isLegitScaleString("1:" + selectScale)) {
            setTableCell(selectRow, "1:" + selectScale);
            return;
        }
    }

    private void setTableCell(int selectRow, String selectScale) {
        scaleDisplays.set(selectRow, "1:" + DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(selectScale.split(":")[1])));
        getTable();
    }

    private void unRegistEvents() {
        this.buttonSelectAll.removeActionListener(this.localActionListener);
        this.buttonInvertSelect.removeActionListener(this.localActionListener);
        this.buttonDelete.removeActionListener(this.localActionListener);
        this.buttonImport.removeActionListener(this.localActionListener);
        this.buttonExport.removeActionListener(this.localActionListener);
        this.buttonOk.removeActionListener(this.panelButtonAction);
        this.buttonCancel.removeActionListener(this.panelButtonAction);
        this.table.removeMouseListener(this.mouseAdapter);
    }

    protected void importXml(String string) {
        try {
            String filePath = getFilePathForImport();
            if (!StringUtilities.isNullOrEmpty(filePath)) {
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String tempstr = "";
                while ((tempstr = br.readLine()) != null) {
                    if (tempstr.contains("<Scale>")) {
                        tempstr = tempstr.substring(tempstr.indexOf(">") + 1, tempstr.lastIndexOf("<"));
                        if (!haveScale(scaleDisplays, tempstr)) {
                            scaleDisplays.add(tempstr);
                        }
                    }
                }
                sort(scaleDisplays);
                getTable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean haveScale(List<String> tempScaleDisplays, String tempstr) {
        boolean haveScale = false;
        for (int i = 0; i < tempScaleDisplays.size(); i++) {
            if (tempScaleDisplays.get(i).equals(tempstr)) {
                haveScale = true;
            }
        }
        return haveScale;
    }

    private String getFilePathForImport() {
        String filePath = "";
        String title = CommonProperties.getString("String_ToolBar_Import");
        if (!SmFileChoose.isModuleExist("ImportScale")) {
            String fileFilter = SmFileChoose.createFileFilter(MapViewProperties.getString("String_ScaleFile"), "xml");
            SmFileChoose.addNewNode(fileFilter, MapViewProperties.getString("String_ScaleFile"), title, "ImportScale", "OpenOne");
        }
        SmFileChoose fileChoose = new SmFileChoose("ImportScale");
        int stateTemp = fileChoose.showDefaultDialog();
        if (stateTemp == JFileChooser.APPROVE_OPTION) {
            filePath = fileChoose.getFilePath();
        }
        return filePath;
    }

    private String getFilePathForExport() {
        String filePath = "";
        if (!SmFileChoose.isModuleExist("ExportScale")) {
            String fileFilter = SmFileChoose.createFileFilter(MapViewProperties.getString("String_ScaleFile"), "xml");
            SmFileChoose.addNewNode(fileFilter, MapViewProperties.getString("String_ScaleFile"), CommonProperties.getString("String_ToolBar_Export"),
                    "ExportScale", "GetDirectories");
        }
        SmFileChoose tempfileChooser = new SmFileChoose("ExportScale");
        tempfileChooser.setSelectedFile(new File(MapViewProperties.getString("String_Scales") + ".xml"));
        int state = tempfileChooser.showSaveDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) {
            filePath = tempfileChooser.getFilePath();
        }
        return filePath;
    }

    protected void exportXml(String module) {
        createXml(getFilePathForExport());
    }

    private void createXml(String filename) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element scales = document.createElement("Scales");
            scales.setAttribute("xmlns", "http://www.supermap.com.cn/desktop");
            scales.setAttribute("version", "8.1.x");
            document.appendChild(scales);
            List<String> tempList = new ArrayList<String>(scaleDisplays);
            removeRepeatStr(tempList);
            sort(tempList);
            for (int i = 0; i < tempList.size(); i++) {
                Element scale = document.createElement("Scale");
                String scaleCaption = tempList.get(i);
                scale.appendChild(document.createTextNode(scaleCaption));
                scale.setNodeValue(scaleCaption);
                scales.appendChild(scale);
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            if (StringUtilities.isNullOrEmpty(filename)) {
                return;
            }
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
                parseFileToXML(transformer, source, file);
            } else if (JOptionPane.OK_OPTION == new SmOptionPane().showConfirmDialog(MapViewProperties.getString("String_RenameFile_Message"))) {
                parseFileToXML(transformer, source, file);
            }
            if (file.exists() && file.length() > 0) {
                Application.getActiveApplication().getOutput()
                        .output(MessageFormat.format(MapViewProperties.getString("String_ExportScale_Scucess_Info"), file.getPath()));
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseFileToXML(Transformer transformer, DOMSource source, File file) throws FileNotFoundException, TransformerException {
        PrintWriter pw = new PrintWriter(file);
        StreamResult streamResult = new StreamResult(pw);
        transformer.transform(source, streamResult);
    }

    /**
     * 反选
     *
     * @param table
     */
    private static void selectInvert(JTable table) {
        try {
            int[] temp = table.getSelectedRows();
            ArrayList<Integer> selectedRows = new ArrayList<Integer>();
            for (int index = 0; index < temp.length; index++) {
                selectedRows.add(temp[index]);
            }

            ListSelectionModel selectionModel = table.getSelectionModel();
            selectionModel.clearSelection();
            for (int index = 0; index < table.getRowCount(); index++) {
                if (!selectedRows.contains(index)) {
                    selectionModel.addSelectionInterval(index, index);
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    private void checkButtonState() {
        if (this.table.getRowCount() > 0) {
            resetButtonState(true);
        } else {
            resetButtonState(false);
        }
        if (this.table.getSelectedRow() >= 0) {
            this.buttonDelete.setEnabled(true);
        } else {
            this.buttonDelete.setEnabled(false);
        }
    }

    private void resetButtonState(boolean enable) {
        this.buttonSelectAll.setEnabled(enable);
        this.buttonInvertSelect.setEnabled(enable);
        this.buttonExport.setEnabled(enable);
    }

    private void initResources() {
        this.setTitle(MapViewProperties.getString("String_SetScaleFixed"));
        this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
        this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
        this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
        this.buttonInvertSelect.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
        this.buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
        this.buttonImport.setToolTipText(CommonProperties.getString("String_ToolBar_Import"));
        this.buttonExport.setToolTipText(CommonProperties.getString("String_ToolBar_Export"));
    }

    private void initComponents() {
        initToolBar();
        initScrollPane();
        initPanelButton();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        this.setLocation((screenSize.width - frameSize.width) / 2 - 200, (screenSize.height - frameSize.height) / 2 - 140);
        this.setSize(500, 400);
        initContentPane();
    }

    private void initContentPane() {
        //@formatter:off
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().add(this.toolbar, new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5));
        this.getContentPane().add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.BOTH).setWeight(1, 4));
        this.getContentPane().add(this.panelButton, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.EAST).setInsets(0));
        //@formatter:on
    }

    private void initPanelButton() {
        this.buttonOk = new SmButton();
        this.buttonCancel = new SmButton();
        this.panelButton = new JPanel();
        //@formatter:off
        this.panelButton.setLayout(new GridBagLayout());
        this.panelButton.add(this.buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(10, 0, 10, 10));
        this.panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(10, 0, 10, 10));
        //@formatter:on
    }

    private void initScrollPane() {
        this.scaleDisplays = new ArrayList<String>();
        this.scrollPane = new JScrollPane();
        this.table = new JTable();
        getTable();
        this.scrollPane.setViewportView(this.table);
    }

    private void initToolBar() {
        this.toolbar = new JToolBar();
        this.toolbar.setFloatable(false);
        this.buttonSelectAll = new JButton(CoreResources.getIcon(urlStr + "Image_ToolButton_SelectAll.png"));
        this.buttonInvertSelect = new JButton(CoreResources.getIcon(urlStr + "Image_ToolButton_SelectInverse.png"));
        this.buttonDelete = new JButton(CoreResources.getIcon(urlStr + "Image_ToolButton_Delete.png"));
        this.buttonImport = new JButton(CoreResources.getIcon(urlStr + "Image_ToolButton_Import.png"));
        this.buttonExport = new JButton(CoreResources.getIcon(urlStr + "Image_ToolButton_Export.png"));
        AddScalePanel addScalePanel = new AddScalePanel();
        this.toolbar.add(addScalePanel);
        this.toolbar.addSeparator();
        this.toolbar.add(this.buttonSelectAll);
        this.toolbar.add(this.buttonInvertSelect);
        this.toolbar.add(this.buttonDelete);
        this.toolbar.addSeparator();
        this.toolbar.add(this.buttonImport);
        this.toolbar.add(this.buttonExport);
    }

    private void addScaleCaption() throws InvalidScaleException {
        int selectRow = table.getSelectedRow();
        if (selectRow >= 0 && selectRow + 1 != table.getRowCount()) {
            // 有选中项或者选中项不是0
            String scaleNext = table.getValueAt(selectRow + 1, 1).toString();
            String scaleNow = table.getValueAt(selectRow, 1).toString();
            if (scaleNext.contains(",")) {
                scaleNext = scaleNext.replaceAll(",","");
            }
            if (scaleNow.contains(",")) {
                scaleNow = scaleNow.replaceAll(",","");
            }
            double scaleNextD = Double.parseDouble(scaleNext.split(":")[1]);
            double scaleNowD = Double.parseDouble(scaleNow.split(":")[1]);
            String scaleInsert = "1:" + DoubleUtilities.getFormatString((scaleNextD + scaleNowD) / 2);
            this.scaleDisplays.add(selectRow + 1, scaleInsert);
            getTable();
            this.table.addRowSelectionInterval(selectRow + 1, selectRow + 1);
            checkButtonState();
            return;
        }
        if (table.getRowCount() == 0) {
            // 表中没有数据时
            double scale = map.getScale();
            String scaleCaption = new ScaleModel(scale).getScaleCaption();
            String scaleDisplay = new String(scaleCaption);
            this.scaleDisplays.add(scaleDisplay);
            getTable();
            checkButtonState();
            return;
        }

        if (selectRow == table.getRowCount() - 1 || table.getRowCount() > 0 && selectRow < 0) {
            // 没有选中项，但是表中有数据时
            String scaleEnd = table.getValueAt(table.getRowCount() - 1, 1).toString();
            double scaleEndD = DoubleUtilities.stringToValue(scaleEnd.split(":")[1]);
            String scaleLast = "1:" + DoubleUtilities.getFormatString(scaleEndD / 2);
            this.scaleDisplays.add(scaleLast);
            getTable();
            checkButtonState();
            return;
        }
    }

    class AddScalePanel extends JPanel {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private JLabel labelAddScale;
        private JLabel labelArraw;
        private MouseAdapter mouseAdpter;

        public AddScalePanel() {
            initAddScalePanel();
            registAddScalePanelEvents();
        }

        private void registAddScalePanelEvents() {

            this.mouseAdpter = new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(selectColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(ScaleEnabledContainer.this.getBackground());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getSource() == labelAddScale) {
                        try {
                            addScaleCaption();
                        } catch (InvalidScaleException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        JPopupMenu popupMenuAddScale = new ToolBarPopuMenu();
                        popupMenuAddScale.show(AddScalePanel.this, 0, getHeight());
                    }
                }

            };
            this.labelAddScale.addMouseListener(this.mouseAdpter);
            this.labelArraw.addMouseListener(this.mouseAdpter);
        }

        private void initAddScalePanel() {
            this.labelAddScale = new JLabel(CoreResources.getIcon(urlStr + "Image_ToolButton_AddScale.png"));
            this.labelArraw = new JLabel(new MetalComboBoxIcon());
            this.labelAddScale.setToolTipText(MapViewProperties.getString("String_AddScale"));
            this.labelArraw.setToolTipText(MapViewProperties.getString("String_AddScale"));
            this.setLayout(new GridBagLayout());
            this.add(this.labelAddScale, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST));
            this.add(this.labelArraw, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setIpad(2, 0));
        }
    }

    class ToolBarPopuMenu extends JPopupMenu {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private DataCell addScale;
        private DataCell addDefaultScale;
        private MouseAdapter mouseAdpter;

        public ToolBarPopuMenu() {
            initToolBarComponents();
            registToolBarPopuMenuEvents();
        }

        private void registToolBarPopuMenuEvents() {
            this.mouseAdpter = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    ((JPanel) e.getSource()).setBackground(selectColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    ((JPanel) e.getSource()).setBackground(ScaleEnabledContainer.this.getBackground());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getSource() == addScale) {
                        try {
                            addScaleCaption();
                        } catch (InvalidScaleException e1) {
                            e1.printStackTrace();
                        }
                        ToolBarPopuMenu.this.setVisible(false);
                    } else {
                        try {
                            if (scaleExist()) {
                                table.setRowSelectionInterval(0, 0);
                            } else {
                                try {
                                    String scaleDisplay = new String(new ScaleModel(map.getScale()).getScaleCaption());
                                    scaleDisplays.add(scaleDisplay);
                                    getTable();
                                } catch (InvalidScaleException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        } catch (InvalidScaleException e1) {
                            e1.printStackTrace();
                        }
                        checkButtonState();
                        ToolBarPopuMenu.this.setVisible(false);
                    }
                }

            };
            this.addScale.addMouseListener(this.mouseAdpter);
            this.addDefaultScale.addMouseListener(this.mouseAdpter);
        }

        protected boolean scaleExist() throws InvalidScaleException {
            boolean exist = false;
            if (scaleDisplays.size() > 0) {
                for (int i = 0; i < scaleDisplays.size(); i++) {
                    if ((new ScaleModel(map.getScale()).getScaleCaption()).equals(scaleDisplays.get(i))) {
                        exist = true;
                    }
                }
            }
            return exist;
        }

        private void initToolBarComponents() {
            this.addScale = new DataCell(MapViewProperties.getString("String_AddScale"), CoreResources.getIcon(urlStr
                    + "Image_ToolButton_AddScale.png"));
            this.addDefaultScale = new DataCell(MapViewProperties.getString("String_AddCurrentScale"), CoreResources.getIcon(urlStr + "Image_ToolButton_DefaultScale.png"));
            this.setLayout(new GridBagLayout());
            this.add(this.addScale, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST));
            this.add(this.addDefaultScale, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST));
        }

    }

    class LocalDefualTableModel extends DefaultTableModel {
        private static final long serialVersionUID = 1L;

        public LocalDefualTableModel(Object[][] obj, String[] name) {
            super(obj, title);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }
    }

    public double[] getScales() {
        return scales;
    }

    public void setScales(double[] scales) throws InvalidScaleException {
        this.scales = scales;
        scaleDisplays.clear();
        if (scales.length > 0) {
            for (double scale : scales) {
                String tempDisplay = new ScaleModel(scale).getScaleCaption();
                scaleDisplays.add(tempDisplay);
            }
        }
        getTable();
    }

    private void getTable() {
        int row = scaleDisplays.size();
        this.table.setModel(new LocalDefualTableModel(new Object[row][2], title));
        for (int i = 0; i < row; i++) {
            this.table.setValueAt(i + 1, i, 0);
            this.table.setValueAt(scaleDisplays.get(i), i, 1);
        }
        this.table.getColumn(MapViewProperties.getString("String_Index")).setMaxWidth(40);
        this.table.getModel().removeTableModelListener(this.tableModelListener);
        this.table.getModel().addTableModelListener(this.tableModelListener);
    }

}
