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
import com.supermap.desktop.ui.docking.SplitWindow;
import com.supermap.desktop.ui.docking.TabWindow;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.*;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;
import com.supermap.ui.MapControl;
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
    //    private JButton buttonImage;
//    private JButton buttonRemoveImage;
    private JButton buttonOk;
    private JPanel panelButton;
    private JLabel labelTitle;
    //    private List formList = new ArrayList();
    private List formMapList = new ArrayList();
    private List formTabularList = new ArrayList();
    public List<IForm> selectList = new ArrayList();
    private static JPopupMenuBind popupMenubind;


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
            selectList.clear();
            addFormList();
            splitTabWindow();
            JPopupMenuBind.this.setVisible(false);
            showView();
        }
    };
    private SplitWindow splitWindow;
    private MouseListener mapControlMouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                bindMapCenter(formMapList.size(), e);
            }
        }

//        @Override
//        public void mouseEntered(MouseEvent e) {
        //想要实现鼠标进入时激活窗口，方便数据集拖拽响应，但是刷新太慢，关闭
//            String name = ((MapControl) e.getSource()).getMap().getName();
//            IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
//            int size = formManager.getCount();
//            for (int i = 0; i < size; i++) {
//                IForm form = formManager.get(i);
//                if (name.equals(form.getText())) {
//                    Application.getActiveApplication().setActiveForm(form);
//                }
//            }
////            Application.getActiveApplication().setActiveForm();
//        }

        @Override
        public void mouseExited(MouseEvent e) {
            int size = formMapList.size();
            for (int j = 0; j < size; j++) {
                IForm formMap = (IForm) formMapList.get(j);
                Map map;
                if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl()) {
                    map = ((IFormMap) formMap).getMapControl().getMap();
                    MapUtilities.clearTrackingObjects(map, TAG_MOVE);
                    map.refreshTrackingLayer();
                }
            }
        }
    };
    private MouseWheelListener mapControlMouseWheelListener = new MouseWheelListener() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            bindMapCenterAndScale(formMapList.size(), ((MapControl) e.getSource()).getMap());
        }
    };
    private MouseMotionListener mapControlMouseMotionListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            bindMapsMousePosition(formMapList.size(), e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            bindMapsMousePosition(formMapList.size(), e);
        }
    };
    private MapDrawingListener mapDrawingListener = new MapDrawingListener() {
        @Override
        public void mapDrawing(MapDrawingEvent e) {
            bindMapCenterAndScale(formMapList.size(), (Map) e.getSource());
        }
    };
    private GeometrySelectChangedListener geoMetrySelectChangeListener;
    private MapDrawnListener mapDrawnListener = new MapDrawnListener() {
        @Override
        public void mapDrawn(MapDrawnEvent mapDrawnEvent) {
            mapDrawnEvent.getMap().refresh();
        }
    };

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
        final int size = formMapList.size();
        // 如果有多个同名的FormMap，关联时实现选择集关联
        //如果有多个同名但不同类型的Form,FormMap之间直接实现选择集关联，
        //FormMap和FormTabular之间关联实现关联浏览属性表

        for (int i = 0; i < size; i++) {
            IForm temp = (IForm) formMapList.get(i);
            if (temp instanceof IFormMap) {
                final MapControl mapControl = ((IFormMap) temp).getMapControl();
                mapControl.addMouseListener(this.mapControlMouseListener);
                mapControl.addMouseWheelListener(this.mapControlMouseWheelListener);
                mapControl.addMouseMotionListener(this.mapControlMouseMotionListener);
                mapControl.getMap().addDrawingListener(this.mapDrawingListener);
                mapControl.getMap().addDrawnListener(this.mapDrawnListener);
                this.geoMetrySelectChangeListener = new LocalSelectChangedListener(mapControl.getMap());
                mapControl.addGeometrySelectChangedListener(this.geoMetrySelectChangeListener);
            }
        }
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        removeBind();
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        removeBind();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        removeBind();
    }


    class LocalSelectChangedListener implements GeometrySelectChangedListener {
        private Map map;

        public LocalSelectChangedListener(Map map) {
            this.map = map;
        }

        @Override
        public void geometrySelectChanged(GeometrySelectChangedEvent geometrySelectChangedEvent) {
            bindMapsSelection(formMapList.size(), map);
        }
    }

    private void bindMapsSelection(int size, Map map) {
        for (int j = 0; j < size; j++) {
            IForm formMap = (IForm) formMapList.get(j);
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

    public void dispose() {
        if (null != formMapList) {
            removeBind();
            this.formMapList = null;
        }
        if (null != formTabularList) {
            this.formTabularList = null;
        }
    }

    private void removeBind() {
        final int size = formMapList.size();
        for (int i = 0; i < size; i++) {
            final MapControl mapControl = ((IFormMap) formMapList.get(i)).getMapControl();
            if (null != mapControl) {
                mapControl.removeMouseListener(this.mapControlMouseListener);
                mapControl.removeMouseWheelListener(this.mapControlMouseWheelListener);
                mapControl.removeMouseMotionListener(this.mapControlMouseMotionListener);
                mapControl.getMap().removeDrawingListener(this.mapDrawingListener);
                mapControl.getMap().removeDrawnListener(this.mapDrawnListener);
                mapControl.removeGeometrySelectChangedListener(this.geoMetrySelectChangeListener);
            }
        }
    }

    private void bindMapsMousePosition(int size, MouseEvent e) {
        Map sourceMap = ((MapControl) e.getSource()).getMap();
        Map currentMap = sourceMap;
        currentMap.getTrackingLayer().clear();
        currentMap.refreshTrackingLayer();
        Point2Ds points = new Point2Ds();
        for (int j = 0; j < size; j++) {
            IForm formMap = (IForm) formMapList.get(j);
            Map map;
            if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && !e.getSource().equals(((IFormMap) formMap).getMapControl())) {
                map = ((IFormMap) formMap).getMapControl().getMap();
                if (!sourceMap.getPrjCoordSys().equals(map.getPrjCoordSys())) {
                    points.clear();
                    points.add(map.pixelToLogical(e.getPoint()));
                    CoordSysTranslator.convert(points, currentMap.getPrjCoordSys(), map.getPrjCoordSys(), new CoordSysTransParameter(), CoordSysTransMethod.MTH_COORDINATE_FRAME);
                }
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
            IForm formMap = (IForm) formMapList.get(j);
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
        Map sourceMap = ((MapControl) e.getSource()).getMap();
        Point2D center = sourceMap.getCenter();
        Point2Ds points = new Point2Ds();
        for (int j = 0; j < size; j++) {
            IForm formMap = (IForm) formMapList.get(j);
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
        int formMapSize = formMapList.size();
        int formTabularSize = formTabularList.size();
        SplitWindow splitWindow = null;
        TabWindow formMapSplitWindow = null;
        TabWindow formTabularTabWindow = null;
        TabWindow tabWindow = ((DockbarManager) (Application.getActiveApplication().getMainFrame()).getDockbarManager()).getChildFormsWindow();
        if (formTabularSize > 0) {
            splitWindow = tabWindow.split((DockingWindow) formTabularList.get(0), Direction.DOWN, 0.7f);
            formTabularTabWindow = (TabWindow) splitWindow.getChildWindow(splitWindow.getChildWindowCount() - 1);
            for (int i = 1; i < formTabularSize; i++) {
                formTabularTabWindow.addTab((DockingWindow) formTabularList.get(i));
            }
        }
        if (formMapSize > 0 && null != splitWindow) {
            formMapSplitWindow = (TabWindow) splitWindow.getChildWindow(0);
            while (formMapSize > 1) {
                formMapSplitWindow.split((DockingWindow) formMapList.get(formMapSize - 1), Direction.RIGHT, (float) (1 - 1.0 / formMapSize));
                formMapSize--;

            }
        } else if (formMapSize > 0 && null == splitWindow) {
            while (formMapSize > 1) {
                if (tabWindow.getChildWindowCount() > 0) {
                    this.splitWindow = tabWindow.split((DockingWindow) formMapList.get(formMapSize - 1), Direction.RIGHT, (float) (1 - 1.0 / formMapSize));
                    formMapSize--;
                } else {
                    break;
                }
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
//        this.buttonImage.addActionListener(this.imageListener);
//        this.buttonRemoveImage.addActionListener(this.removeImageListener);
        this.buttonOk.addActionListener(this.okListener);
        this.listForms.addMouseListener(this.listFormsMouseListener);
    }

    public void removeEvents() {
        this.buttonSelectAll.removeActionListener(this.selectAllListener);
        this.buttonSelectInverse.removeActionListener(this.selectInverseListener);
//        this.buttonImage.removeActionListener(this.imageListener);
//        this.buttonRemoveImage.removeActionListener(this.removeImageListener);
        this.buttonOk.removeActionListener(this.okListener);
        this.listForms.removeMouseListener(this.listFormsMouseListener);
    }

    private void initLayout() {
        this.removeAll();
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
