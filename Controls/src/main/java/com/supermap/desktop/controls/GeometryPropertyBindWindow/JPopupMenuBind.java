package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.SplitWindow;
import com.supermap.desktop.ui.docking.TabWindow;
import net.infonode.util.Direction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 2016/11/10.
 */
public class JPopupMenuBind extends JPopupMenu implements PopupMenuListener {
    private static final int MARKET_WIDTH = 128;
    private static final String TAG_MOVE = "MOVE";
    private JScrollPane scrollPane;
    private JList listForms;
    private JButton buttonSelectAll;
    private JButton buttonSelectInverse;
    private JButton buttonOk;
    private JPanel panelButton;
    private JLabel labelTitle;
    private List formMapList = new ArrayList();
    private List formTabularList = new ArrayList();
    public static List<IForm> selectList = new ArrayList();
    private BindHandler handler = new BindHandler();
    private static JPopupMenuBind popupMenubind;
    private boolean formMapsOnly = false;
    private boolean formTabularsOnly = false;
    private boolean formMapsAndFormTabulars = false;


    private ActionListener selectAllListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int size = listForms.getModel().getSize();
            for (int i = 0; i < size; i++) {
                CheckableItem item = (CheckableItem) listForms.getModel().getElementAt(i);
                item.setSelected(true);
                repaintListItem(i);
            }
        }
    };

    private ActionListener selectInverseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int size = listForms.getModel().getSize();
            for (int i = 0; i < size; i++) {
                CheckableItem item = (CheckableItem) listForms.getModel().getElementAt(i);
                item.setSelected(!item.isSelected());
                repaintListItem(i);
            }
        }
    };
    private ActionListener imageListener;
    private ActionListener removeImageListener;
    private ActionListener okListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addFormList();
            showView();
            splitTabWindow();
            JPopupMenuBind.this.setVisible(false);
        }
    };
    private SplitWindow splitWindow;

    private void addFormList() {
        formMapList.clear();
        formTabularList.clear();
        IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
        DefaultListModel model = (DefaultListModel) listForms.getModel();
        int size = model.getSize();
        for (int i = 0; i < size; i++) {
            CheckableItem item = (CheckableItem) model.getElementAt(i);
            if (item.isSelected()) {
                IForm form = formManager.get(i);
                if (form instanceof IFormMap) {
                    formMapList.add(form);
                } else if (form instanceof IFormTabular) {
                    formTabularList.add(form);
                }
                selectList.add(form);
            }
        }
    }

    private void showView() {
        final int formMapSize = formMapList.size();
        int formSize = selectList.size();
        int formTabularSize = formTabularList.size();
        //只有属性表
        handler.setFormMapList(formMapList);
        handler.setFormTabularList(formTabularList);
        handler.setFormsList(selectList);
        if (formSize == formMapSize) {
            formMapsOnly = true;
            handler.bindFormMaps();
        } else if (formTabularSize == formSize) {
            formTabularsOnly = true;
            handler.bindFormTabulars();
        } else {
            formMapsAndFormTabulars = true;
            handler.bindFormMapsAndFormTabulars();
        }
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        removeBind();
    }

    public void removeBind() {
        if (formMapsOnly) {
            handler.removeFormMapsBind();
        } else if (formTabularsOnly) {
            handler.removeFormTabularsBind();
        } else if (formMapsAndFormTabulars) {
            handler.removeFormMapsAndFormTabularsBind();
        }
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        formMapsOnly = false;
        formTabularsOnly = false;
        formMapsAndFormTabulars = false;
        removeBind();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        removeBind();
    }


    public void dispose() {
        if (null != labelTitle) {
            labelTitle = null;
        }
        if (null != listForms) {
            listForms = null;
        }
        if (null != buttonOk) {
            buttonOk = null;
        }
        if (null != buttonSelectAll) {
            buttonSelectAll = null;
        }
        if (null != buttonSelectInverse) {
            buttonSelectInverse = null;
        }
        if (null != panelButton) {
            panelButton = null;
        }
        if (null != scrollPane) {
            scrollPane = null;
        }
        if (null != formMapList) {
            formMapList = null;
        }
        if (null != formTabularList) {
            formTabularList = null;
        }
    }

    private void splitTabWindow() {
        int formMapSize = formMapList.size();
        int formTabularSize = formTabularList.size();
        TabWindow formMapSplitWindow = null;
        TabWindow formTabularTabWindow = null;
        TabWindow tabWindow = ((DockbarManager) (Application.getActiveApplication().getMainFrame()).getDockbarManager()).getChildFormsWindow();
        if (formMapsOnly) {
            while (formMapSize > 1) {
                if (tabWindow.getChildWindowCount() > 0) {
                    tabWindow.split((DockingWindow) formMapList.get(formMapSize - 1), Direction.RIGHT, (float) (1 - 1.0 / formMapSize));
                    formMapSize--;
                } else {
                    break;
                }
            }
        } else if (formTabularsOnly) {
            while (formTabularSize > 1) {
                if (tabWindow.getChildWindowCount() > 0) {
                    tabWindow.split((DockingWindow) formTabularList.get(formTabularSize - 1), Direction.DOWN, (float) (1 - 1.0 / formTabularSize));
                    formTabularSize--;
                } else {
                    break;
                }
            }
        } else if (formMapsAndFormTabulars) {
            //有地图和属性表时

            this.splitWindow = tabWindow.split((DockingWindow) formTabularList.get(0), Direction.DOWN, 0.7f);
            formMapSplitWindow = (TabWindow) splitWindow.getChildWindow(0);
            while (formMapSize > 1) {
                if (formMapSplitWindow.getChildWindowCount() > 0) {
                    formMapSplitWindow.split((DockingWindow) formMapList.get(formMapSize - 1), Direction.RIGHT, (float) (1 - 1.0 / formMapSize));
                    formMapSize--;
                } else {
                    break;
                }
            }
            formTabularTabWindow = (TabWindow) splitWindow.getChildWindow(splitWindow.getChildWindowCount() - 1);
            for (int i = 1; i < formTabularSize; i++) {
                formTabularTabWindow.addTab((DockingWindow) formTabularList.get(i));
            }
        }
        if (formMapSize > 0) {
            Application.getActiveApplication().setActiveForm((IForm) formMapList.get(0));
        }
    }

    private MouseListener listFormsMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int index = listForms.locationToIndex(e.getPoint());
            CheckableItem item = (CheckableItem) listForms.getModel().getElementAt(index);
            item.setSelected(!item.isSelected());
            repaintListItem(index);
        }
    };

    public static JPopupMenuBind instance() {
        if (null == popupMenubind) {
            popupMenubind = new JPopupMenuBind();
        }
        return popupMenubind;
    }

    public void init() {
        initComponents();
        initLayout();
        registEvents();
        initResources();
        addPopupMenuListener(this);
        this.setPopupSize(480, 270);
    }

    private void repaintListItem(int i) {
        Rectangle rect = listForms.getCellBounds(i, i);
        listForms.repaint(rect);
    }

    private void initComponents() {
        if (null == formMapList) {
            formMapList = new ArrayList();
        }
        if (null == formTabularList) {
            formTabularList = new ArrayList();
        }
        this.scrollPane = new JScrollPane();
        this.listForms = new JList();
        this.buttonSelectAll = ComponentFactory.createButtonSelectAll();
        this.buttonSelectInverse = ComponentFactory.createButtonSelectInverse();
        this.buttonOk = ComponentFactory.createButtonOK();
        this.labelTitle = new JLabel();
        this.panelButton = new JPanel();
        initListForms();
    }

    private void initListForms() {
        DefaultListModel listModel = new DefaultListModel();
        this.listForms = new JList();
        this.listForms.setModel(listModel);
        addItemToList(listModel);
        this.listForms.setCellRenderer(new CheckListRenderer());
        this.listForms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listForms.setBorder(new EmptyBorder(0, 4, 0, 0));
    }

    private void addItemToList(DefaultListModel listModel) {
        IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
        int size = formManager.getCount();
        int listSize = selectList.size();
        for (int i = 0; i < size; i++) {
            IForm form = formManager.get(i);
            CheckableItem item = new CheckableItem();
            item.setStr(form.getText());
            item.setForm(form);
            for (int j = 0; j < listSize; j++) {
                if (form.equals(selectList.get(j))) {
                    item.setSelected(true);
                    break;
                }
            }
            listModel.addElement(item);
        }
    }

    private void registEvents() {
        removeEvents();
        this.buttonSelectAll.addActionListener(this.selectAllListener);
        this.buttonSelectInverse.addActionListener(this.selectInverseListener);
        this.buttonOk.addActionListener(this.okListener);
        this.listForms.addMouseListener(this.listFormsMouseListener);
    }

    public void removeEvents() {
        if (null != buttonSelectInverse && null != buttonSelectAll && null != buttonOk && null != listForms) {
            this.buttonSelectAll.removeActionListener(this.selectAllListener);
            this.buttonSelectInverse.removeActionListener(this.selectInverseListener);
            this.buttonOk.removeActionListener(this.okListener);
            this.listForms.removeMouseListener(this.listFormsMouseListener);
        }
    }

    private void initLayout() {
        this.removeAll();
        this.panelButton.setLayout(new GridBagLayout());
        this.panelButton.add(this.buttonSelectAll, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelButton.add(this.buttonSelectInverse, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelButton.add(this.buttonOk, new GridBagConstraintsHelper(4, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.setLayout(new GridBagLayout());
        this.add(this.labelTitle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        this.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 1, 5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(this.panelButton, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0));
        this.scrollPane.setViewportView(listForms);
    }

    private void initResources() {
        this.labelTitle.setText(CoreProperties.getString("String_Bind"));
    }

    class CheckListRenderer implements ListCellRenderer<Object> {

        public CheckListRenderer() {
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            setEnabled(list.isEnabled());
            return this.initRenderPanel(list, value);
        }

        private JPanel initRenderPanel(JList list, Object value) {
            JPanel panelContent = new JPanel();
            JCheckBox checkBox = new JCheckBox();
            DataCell dataCell = new DataCell();
            if (value instanceof CheckableItem) {
                checkBox.setSelected(((CheckableItem) value).isSelected());
                if (((CheckableItem) value).getForm() instanceof IFormMap) {
                    dataCell.initDataImage(new ImageIcon(InternalImageIconFactory.MAPS.getImage()), value.toString());
                } else if (((CheckableItem) value).getForm() instanceof IFormTabular) {
                    String path = CommonToolkit.DatasetImageWrap.getImageIconPath(DatasetType.TABULAR);
                    URL url = JPopupMenuBind.class.getResource(path);
                    dataCell.initDataImage(new ImageIcon(url), value.toString());
                }
            }

            panelContent.setLayout(new GridBagLayout());
            panelContent.setLayout(new FlowLayout(FlowLayout.LEFT));
            panelContent.add(checkBox);
            panelContent.add(dataCell);
            dataCell.setFont(list.getFont());
            setComponentTheme(panelContent);
            setComponentTheme(checkBox);
            setComponentTheme(dataCell);
            return panelContent;
        }

        private void setComponentTheme(JComponent component) {
            component.setBackground(UIManager.getColor("List.textBackground"));
            component.setForeground(UIManager.getColor("List.textForeground"));
        }
    }

    class CheckableItem {
        private String str;
        private boolean isSelected;
        private IForm form;

        public CheckableItem() {
        }

        public IForm getForm() {
            return form;
        }

        public void setForm(IForm form) {
            this.form = form;
        }

        public void setSelected(boolean b) {
            this.isSelected = b;
        }

        public boolean isSelected() {
            return this.isSelected;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public String toString() {
            return str;
        }
    }

}
