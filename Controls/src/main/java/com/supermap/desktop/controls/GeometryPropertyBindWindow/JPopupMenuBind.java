package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.*;
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
import com.supermap.desktop.ui.docking.TabWindow;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.*;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;
import com.supermap.ui.MapControl;
import net.infonode.util.Direction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 2016/11/10.
 */
public class JPopupMenuBind extends JPopupMenu {
    private static final int MARKET_WIDTH = 128;
    private static final String TAG_MOVE = "MOVE";
    private JScrollPane scrollPane;
    private JList listForms;
    private JButton buttonSelectAll;
    private JButton buttonSelectInverse;
    //    private JButton buttonImage;
//    private JButton buttonRemoveImage;
    private JButton buttonOk;
    private JPanel panelButton;
    private JLabel labelTitle;
    private IForm activeForm;
    private List formList = new ArrayList();
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
            splitTabWindow();
            JPopupMenuBind.this.setVisible(false);
            showView();
        }
    };

    private void addFormList() {
        IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
        DefaultListModel model = (DefaultListModel) listForms.getModel();
        int size = model.getSize();
        for (int i = 0; i < size; i++) {
            CheckableItem item = (CheckableItem) model.getElementAt(i);
            if (item.isSelected()) {
                IForm form = formManager.get(i);
                if (form instanceof IFormMap && !form.getText().equals(activeForm.getText())) {
                    ((IFormMap) form).getMapControl().setAction(Action.PAN);
                }
                formList.add(form);
            }
        }
    }

    private void showView() {
        final int size = formList.size();
        // 如果有多个同名的FormMap，关联时实现选择集关联
        //如果有多个同名但不同类型的Form,FormMap之间直接实现选择集关联，
        //FormMap和FormTabular之间关联实现关联浏览属性表
        for (int i = 0; i < size; i++) {
            IForm temp = (IForm) formList.get(i);
            if (temp instanceof IFormMap) {
                final MapControl mapControl = ((IFormMap) temp).getMapControl();

                mapControl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            bindMapCenter(size, e);
                        }
                    }
                });
                mapControl.addMouseWheelListener(new MouseWheelListener() {
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        bindMapCenterAndScale(size, ((MapControl) e.getSource()).getMap());
                    }
                });
                mapControl.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        bindMapsMousePosition(size, e);
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        bindMapsMousePosition(size, e);
                    }
                });
                mapControl.getMap().addDrawingListener(new MapDrawingListener() {
                    @Override
                    public void mapDrawing(MapDrawingEvent e) {
                        bindMapCenterAndScale(size, (Map) e.getSource());
                    }
                });
                mapControl.addGeometrySelectChangedListener(new GeometrySelectChangedListener() {
                    @Override
                    public void geometrySelectChanged(GeometrySelectChangedEvent geometrySelectChangedEvent) {
                        bindMapsSelection(size, mapControl.getMap());
                    }
                });
            }
        }
    }

    private void bindMapsSelection(int size, Map map) {
        for (int j = 0; j < size; j++) {
            IForm formMap = (IForm) formList.get(j);
            if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() &&
                    !map.equals(((IFormMap) formMap).getMapControl().getMap()) && includeSameLayer(((IFormMap) formMap).getMapControl().getMap(), map)) {
                Map sourceMap = ((IFormMap) formMap).getMapControl().getMap();
                Map targetMap = map;
                Layers sourceLayers = sourceMap.getLayers();
                Layers targetLayers = targetMap.getLayers();
                int sourceLayesSize = sourceLayers.getCount();
                int targetLaysersSize = targetLayers.getCount();
                for (int i = 0; i < sourceLayesSize; i++) {
                    for (int k = 0; k < targetLaysersSize; k++) {
                        Layer sourceLayer = sourceLayers.get(i);
                        Layer targetLayer = targetLayers.get(k);
                        if (sourceLayer.getDataset().equals(targetLayer.getDataset())) {
                            sourceLayer.setSelection(targetLayer.getSelection());
                        }
                    }
                }
            }
        }
    }

    private void bindMapsMousePosition(int size, MouseEvent e) {
        Map currentMap = ((MapControl) e.getSource()).getMap();
        currentMap.getTrackingLayer().clear();
        currentMap.refreshTrackingLayer();
        for (int j = 0; j < size; j++) {
            IForm formMap = (IForm) formList.get(j);
            Map map;
            if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && !e.getSource().equals(((IFormMap) formMap).getMapControl())) {
                map = ((IFormMap) formMap).getMapControl().getMap();
                MapUtilities.clearTrackingObjects(map, TAG_MOVE);
                Geometry tempGeometry = getTrackingGeometry(map.pixelToMap(e.getPoint()), Color.gray);
                map.getTrackingLayer().add(tempGeometry, TAG_MOVE);
                tempGeometry.dispose();
                map.refreshTrackingLayer();
            }
        }
    }

    //region 绘制跟踪层图像模拟光标
    private Geometry getTrackingGeometry(Point2D point, Color color) {
        GeoCompound geoCompound = new GeoCompound();
        GeoPoint geoPoint = new GeoPoint(point);
        geoPoint.setStyle(getPointStyle(color));
        geoCompound.addPart(geoPoint);
        return geoCompound;
    }

    private GeoStyle getPointStyle(Color color) {
        GeoStyle style = new GeoStyle();
        style.setSymbolMarker(getCrossMarket(color));
        style.setMarkerSize(new Size2D(10, 10));
        return style;
    }

    private SymbolMarker getCrossMarket(Color color) {
        SymbolMarker sm = new SymbolMarker();

        Rectangle2D rect = new Rectangle2D(0, 0, MARKET_WIDTH, MARKET_WIDTH);
        GeoCompound compound = new GeoCompound();
        try {
            int start = 0;
            int end = MARKET_WIDTH - start;
            Point2Ds pnts = new Point2Ds();
            pnts.add(new Point2D(start, MARKET_WIDTH / 2));
            pnts.add(new Point2D(end, MARKET_WIDTH / 2));
            GeoLine line = new GeoLine(pnts);
            GeoStyle lineStyle = new GeoStyle();
            lineStyle.setLineColor(color);
            lineStyle.setLineWidth(5);
            line.setStyle(lineStyle);
            compound.addPart(line);

            pnts = new Point2Ds();
            pnts.add(new Point2D(MARKET_WIDTH / 2, start));
            pnts.add(new Point2D(MARKET_WIDTH / 2, end));
            line = new GeoLine(pnts);
            line.setStyle(lineStyle);
            compound.addPart(line);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        sm.fromGeometry(compound, rect);
        return sm;
    }

    private void bindMapCenterAndScale(int size, Map sourceMap) {
        Point2D center = sourceMap.getCenter();
        double scale = sourceMap.getScale();
        for (int j = 0; j < size; j++) {
            IForm formMap = (IForm) formList.get(j);
            Map map;
            if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && !sourceMap.equals(((IFormMap) formMap).getMapControl().getMap())) {
                map = ((IFormMap) formMap).getMapControl().getMap();
                if (null != center && scale - 0.0 > 0) {
                    map.setCenter(center);
                    map.setScale(scale);
                    map.refresh();
                }
            } else if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && sourceMap.equals(((IFormMap) formMap).getMapControl().getMap())) {
                map = ((IFormMap) formMap).getMapControl().getMap();
                center = map.getCenter();
                scale = map.getScale();
            }
        }
    }

    private void bindMapCenter(int size, MouseEvent e) {
        Point2D center = ((MapControl) e.getSource()).getMap().getCenter();
        for (int j = 0; j < size; j++) {
            IForm formMap = (IForm) formList.get(j);
            Map map;
            if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && !e.getSource().equals(((IFormMap) formMap).getMapControl())) {
                map = ((IFormMap) formMap).getMapControl().getMap();
                if (null != center) {
                    map.setCenter(center);
                    map.refresh();
                }
            }
        }
    }

    private boolean includeSameLayer(Map sourceMap, Map targetMap) {
        boolean result = false;
        Layers sourceLayers = sourceMap.getLayers();
        Layers targetLayers = targetMap.getLayers();
        int sourceLayesSize = sourceLayers.getCount();
        int targetLaysersSize = targetLayers.getCount();
        for (int i = 0; i < sourceLayesSize; i++) {
            for (int k = 0; k < targetLaysersSize; k++) {
                Layer sourceLayer = sourceLayers.get(i);
                Layer targetLayer = targetLayers.get(k);
                if (sourceLayer.getDataset().equals(targetLayer.getDataset())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private void splitTabWindow() {
        TabWindow tabWindow;
        int size = formList.size();
        while (size > 1) {
            tabWindow = ((DockbarManager) (Application.getActiveApplication().getMainFrame()).getDockbarManager()).getChildFormsWindow();
            if (tabWindow.getChildWindowCount() > 0) {
                tabWindow.split((DockingWindow) formList.get(size - 1), Direction.RIGHT, (float) (1 - 1.0 / size));
                size--;
            } else {
                break;
            }
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

    public JPopupMenuBind() {
        super();
        initComponents();
        initLayout();
        registEvents();
        initResources();
        this.setPopupSize(480, 270);
    }

    private void repaintListItem(int i) {
        Rectangle rect = listForms.getCellBounds(i, i);
        listForms.repaint(rect);
    }

    private void initComponents() {
        this.scrollPane = new JScrollPane();
        this.listForms = new JList();
        this.buttonSelectAll = ComponentFactory.createButtonSelectAll();
        this.buttonSelectInverse = ComponentFactory.createButtonSelectInverse();
//        this.buttonImage = new SmButton();
//        this.buttonRemoveImage = new SmButton();
//        this.buttonRemoveImage.setEnabled(false);
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
        activeForm = Application.getActiveApplication().getActiveForm();
        int size = formManager.getCount();
        int listSize = formList.size();
        for (int i = 0; i < size; i++) {
            IForm form = formManager.get(i);
            CheckableItem item = new CheckableItem();
            item.setStr(form.getText());
            item.setForm(form);
            for (int j = 0; j < listSize; j++) {
                if (form.equals(formList.get(j))) {
                    item.setSelected(true);
                } else {
                    item.setSelected(false);
                }
            }
            listModel.addElement(item);
        }
    }

    private void registEvents() {
        removeEvents();
        this.buttonSelectAll.addActionListener(this.selectAllListener);
        this.buttonSelectInverse.addActionListener(this.selectInverseListener);
//        this.buttonImage.addActionListener(this.imageListener);
//        this.buttonRemoveImage.addActionListener(this.removeImageListener);
        this.buttonOk.addActionListener(this.okListener);
        this.listForms.addMouseListener(this.listFormsMouseListener);
    }

    private void removeEvents() {
        this.buttonSelectAll.removeActionListener(this.selectAllListener);
        this.buttonSelectInverse.removeActionListener(this.selectInverseListener);
//        this.buttonImage.removeActionListener(this.imageListener);
//        this.buttonRemoveImage.removeActionListener(this.removeImageListener);
        this.buttonOk.removeActionListener(this.okListener);
        this.listForms.removeMouseListener(this.listFormsMouseListener);
    }

    private void initLayout() {
        this.panelButton.setLayout(new GridBagLayout());
        this.panelButton.add(this.buttonSelectAll, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelButton.add(this.buttonSelectInverse, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
//        this.panelButton.add(this.buttonImage, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
//        this.panelButton.add(this.buttonRemoveImage, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelButton.add(this.buttonOk, new GridBagConstraintsHelper(4, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.setLayout(new GridBagLayout());
        this.add(this.labelTitle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        this.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 1, 5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(this.panelButton, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0));
        this.scrollPane.setViewportView(listForms);
    }

    private void initResources() {
//        this.buttonImage.setText(CoreProperties.getString("String_Icon"));
//        this.buttonRemoveImage.setText(CoreProperties.getString("String_ClearImage"));
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
