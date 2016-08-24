package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.SymbolDialogFactory;
import com.supermap.desktop.dialog.symbolDialogs.ISymbolApply;
import com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols.*;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.ColorSelectionPanel;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.PathUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * Created by xie on 2016/8/12.
 * Used for CAD style when color or symbol change;
 */
public class CADStylePanel extends JPanel implements ICADStylePanel {
    private JLabel labelImage;
    private JLabel labelName;
    private JLabel labelArraw;
    //    private JPanel panelColorDisplay;
    private int cadType;
    private int alignment;

    private final Color COLOR_SYSTEM_SELECTED = new Color(185, 214, 244);
    private Color COLOR_SYSTEM_DEFAULT;
    private Color selectedColor;
    private Color defualtColor = new Color(255, 0, 255);
    private String defulatPath;
    private EditHistory editHistory;

    public static final int MARKER_TYPE = 1;
    public static final int LINE_TYPE = 2;
    public static final int FILL_TYPE = 3;
    public static final int HORIZONTAL = 4;
    public static final int VERTICAL = 5;
    private Recordset recordset;
    private JPanelSymbols panelSymbols;
    private JPopupMenu popupMenuVertical;
    private JPanel panelMore;

    private SymbolSelectedChangedListener popuVerticalListener = new SymbolSelectedChangedListener() {

        @Override
        public void SymbolSelectedChangedEvent(Symbol symbol) {
            resetSymbol(popupMenuVertical, symbol);
        }

        @Override
        public void SymbolSelectedDoubleClicked() {
            // Do nothing
        }
    };


    public CADStylePanel(int cadType, int alignment) {
        super();
        this.cadType = cadType;
        this.alignment = alignment;
        initComponents();
        COLOR_SYSTEM_DEFAULT = this.getBackground();
        initResources();
        registEvents();
        editHistory = MapUtilities.getMapControl().getEditHistory();
    }

    private void registEvents() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                CADStylePanel.this.setBackground(COLOR_SYSTEM_SELECTED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                CADStylePanel.this.setBackground(CADStylePanel.this.COLOR_SYSTEM_DEFAULT);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                    switch (alignment) {
                        case HORIZONTAL:
                            setHorizontalPopupmenu(e);
                            break;
                        case VERTICAL:
                            resetRecordsetSymbol(e);
                            break;
                        default:
                            break;
                    }
                }
            }

        });
    }

    private void setHorizontalPopupmenu(MouseEvent e) {
        final JPopupMenu popupMenu = new JPopupMenu();
        ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
        popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
        colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
        popupMenu.show(e.getComponent(), 0, (int) (e.getComponent().getBounds().getHeight()));
        colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                selectedColor = (Color) evt.getNewValue();
                recordset.moveFirst();
                while (!recordset.isEOF()) {
                    editHistory.add(EditType.MODIFY, recordset, true);
                    recordset.edit();
                    Geometry tempGeometry = recordset.getGeometry();
                    GeoStyle geoStyle = new GeoStyle();
                    if (null != geoStyle) {
                        geoStyle = tempGeometry.getStyle();
                    }
                    if (cadType == MARKER_TYPE) {
                        // 修改点符号
                        geoStyle.setLineColor(selectedColor);
                    } else if (cadType == LINE_TYPE) {
                        // 修改线符号
                        geoStyle.setLineColor(selectedColor);
                    } else if (cadType == FILL_TYPE) {
                        // 修改面符号
                        geoStyle.setFillForeColor(selectedColor);
                    }
                    tempGeometry.setStyle(geoStyle);
                    recordset.setGeometry(tempGeometry);
                    tempGeometry.dispose();
                    recordset.update();
                    recordset.moveNext();
                }
                editHistory.batchEnd();
                popupMenu.setVisible(false);
                MapUtilities.getActiveMap().refresh();
                if (null != getImage(defulatPath)) {
                    labelImage.setIcon(new ImageIcon(getImage(defulatPath)));
                }
            }
        });
    }

    private JPanel getPanelSymbols(JPanel panelSymbols) {
        JPanel panelSymbol = new JPanel();
        panelMore = new JPanel();
        JLabel buttonMoreImage = new JLabel(ControlsResources.getIcon("/controlsresources/Image_SymbolDictionary.png"));
        JLabel labelTitle = new JLabel(ControlsProperties.getString("String_SymbolLibraryManager"));
        panelMore.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelMore.add(buttonMoreImage);
        panelMore.add(labelTitle);
        JScrollPane jScrollPane = new JScrollPane(panelSymbols);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panelSymbol.setLayout(new GridBagLayout());
        panelSymbol.add(jScrollPane, new GridBagConstraintsHelper(0, 0, 1, 2).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 2));
        panelSymbol.add(panelMore, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0));
        panelMore.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetRecordsetGeoStyle();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panelMore.setBackground(CADStylePanel.this.COLOR_SYSTEM_SELECTED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panelMore.setBackground(CADStylePanel.this.COLOR_SYSTEM_DEFAULT);
            }
        });
        return panelSymbol;
    }

    private void resetGeoStyle(Recordset tempRecordset, GeoStyle newGeoStyle) {
        tempRecordset.moveFirst();
        while (!tempRecordset.isEOF()) {
            editHistory.add(EditType.MODIFY, tempRecordset, true);
            if (!tempRecordset.isReadOnly()) {
                tempRecordset.edit();
                Geometry tempGeometry = tempRecordset.getGeometry();
                tempGeometry.setStyle(newGeoStyle);
                tempRecordset.setGeometry(tempGeometry);
                tempGeometry.dispose();
                tempRecordset.update();
                tempRecordset.moveNext();
            }
        }
        editHistory.batchEnd();
        popupMenuVertical.setVisible(false);
        panelSymbols.removeSymbolSelectedChangedListener(this.popuVerticalListener);
        MapUtilities.getActiveMap().refresh();
    }

    private void resetRecordsetGeoStyle() {
        SymbolType symbolType = null;
        GeoStyle beforeGeoStyle = new GeoStyle();
        if (cadType == MARKER_TYPE) {
            symbolType = SymbolType.MARKER;
        } else if (cadType == LINE_TYPE) {
            symbolType = SymbolType.LINE;
        } else {
            symbolType = SymbolType.FILL;
        }
        recordset.moveFirst();
        beforeGeoStyle = recordset.getGeometry().getStyle().clone();
        GeoStyle geostyle = changeGeoStyle(beforeGeoStyle, symbolType, new ISymbolApply() {
            @Override
            public void apply(GeoStyle geoStyle) {
                if (MapUtilities.getActiveMap().findSelection(true).length > 0) {
                    resetGeoStyle(MapUtilities.getActiveMap().findSelection(true)[0].toRecordset(), geoStyle);
                }
            }
        });
        if (geostyle != null) {
            if (MapUtilities.getActiveMap().findSelection(true).length > 0) {
                resetGeoStyle(MapUtilities.getActiveMap().findSelection(true)[0].toRecordset(), geostyle);
            }
        }
    }

    private GeoStyle changeGeoStyle(GeoStyle beforeStyle, SymbolType symbolType, ISymbolApply symbolApply) {
        GeoStyle result = null;
        SymbolDialog symbolDialog = null;
        try {
            CursorUtilities.setWaitCursor();
            symbolDialog = SymbolDialogFactory.getSymbolDialog(symbolType);
            DialogResult dialogResult = symbolDialog.showDialog(beforeStyle, symbolApply);
            if (dialogResult == DialogResult.OK) {
                result = symbolDialog.getCurrentGeoStyle();
                panelMore.removeMouseListener(null);
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        } finally {
            CursorUtilities.setDefaultCursor();
        }
        return result;
    }

    private void resetRecordsetSymbol(MouseEvent e) {
        popupMenuVertical = new JPopupMenu();
        Resources resources = Application.getActiveApplication().getWorkspace().getResources();
        if (cadType == MARKER_TYPE) {
            panelSymbols = new JPanelSymbolsPoint();
            popupMenuVertical.setPreferredSize(new Dimension(400, 300));
            panelSymbols.setSymbolGroup(resources, resources.getMarkerLibrary().getRootGroup());
        } else if (cadType == LINE_TYPE) {
            panelSymbols = new JPanelSymbolsLine();
            panelSymbols.setSymbolGroup(resources, resources.getLineLibrary().getRootGroup());
            popupMenuVertical.setPreferredSize(new Dimension(460, 300));
        } else if (cadType == FILL_TYPE) {
            panelSymbols = new JPanelSymbolsFill();
            panelSymbols.setSymbolGroup(resources, resources.getFillLibrary().getRootGroup());
            popupMenuVertical.setPreferredSize(new Dimension(460, 300));
        }
        panelSymbols.setGeoStyle(new GeoStyle());
        popupMenuVertical.add(getPanelSymbols(panelSymbols));
        popupMenuVertical.show(e.getComponent(), 0, (int) e.getComponent().getBounds().getHeight());
        panelSymbols.addSymbolSelectedChangedListener(this.popuVerticalListener);
    }

    private void resetSymbol(JPopupMenu popupMenuVertical, Symbol symbol) {
        recordset.moveFirst();
        while (!recordset.isEOF()) {
            editHistory.add(EditType.MODIFY, recordset, true);
            if (!recordset.isReadOnly()) {
                recordset.edit();
                Geometry tempGeometry = recordset.getGeometry();
                GeoStyle geoStyle = new GeoStyle();
                if (null != tempGeometry.getStyle()) {
                    geoStyle = tempGeometry.getStyle();
                }
                if (cadType == MARKER_TYPE) {
                    // 修改点符号
                    geoStyle.setMarkerSymbolID(((SymbolMarker) symbol).getID());
                } else if (cadType == LINE_TYPE) {
                    // 修改线符号
                    geoStyle.setLineSymbolID(((SymbolLine) symbol).getID());
                } else if (cadType == FILL_TYPE) {
                    // 修改面符号
                    geoStyle.setFillSymbolID(((SymbolFill) symbol).getID());
                }
                tempGeometry.setStyle(geoStyle);
                recordset.setGeometry(tempGeometry);
                tempGeometry.dispose();
                recordset.update();
                recordset.moveNext();
            }
        }
        editHistory.batchEnd();
        popupMenuVertical.setVisible(false);
        MapUtilities.getActiveMap().refresh();
    }


    private void initResources() {
        switch (cadType) {
            case MARKER_TYPE:
                if (alignment == HORIZONTAL) {
                    this.labelImage.setIcon(new ImageIcon(PathUtilities.getFullPathName("../Resources/MapView/Toolbar/LayerStyle/PointStyle/MarkerColor.png", false)));
                } else {
                    this.labelImage.setIcon(new ImageIcon(PathUtilities.getFullPathName("../Resources/MapView/Toolbar/LayerStyle/PointStyle/MarkerSymbol.png", false)));
                    this.labelName.setText(MapEditorProperties.getString("String_Point"));
                }
                break;
            case LINE_TYPE:
                if (alignment == HORIZONTAL) {
                    this.labelImage.setIcon(new ImageIcon(PathUtilities.getFullPathName("../Resources/MapView/Toolbar/LayerStyle/LineStyle/LineColor.png", false)));
                } else {
                    this.labelImage.setIcon(new ImageIcon(PathUtilities.getFullPathName("../Resources/MapView/Toolbar/LayerStyle/LineStyle/LineSymbol.png", false)));
                    this.labelName.setText(MapEditorProperties.getString("String_Line"));
                }
                break;
            case FILL_TYPE:
                if (alignment == HORIZONTAL) {
                    this.labelImage.setIcon(new ImageIcon(PathUtilities.getFullPathName("../Resources/MapView/Toolbar/LayerStyle/FillStyle/ForeColor.png", false)));
                } else {
                    this.labelImage.setIcon(new ImageIcon(PathUtilities.getFullPathName("../Resources/MapView/Toolbar/LayerStyle/FillStyle/FillSymbol.png", false)));
                    this.labelName.setText(MapEditorProperties.getString("String_Fill"));
                }
                break;
            default:
                break;
        }
        defulatPath = labelImage.getIcon().toString();
    }

    private void initComponents() {
//        this.panelColorDisplay = new JPanel();
//        this.panelColorDisplay.setBackground(Color.gray);
//        this.panelColorDisplay.setPreferredSize(new Dimension(15, 5));
        this.labelImage = new JLabel();
        this.labelName = new JLabel();
        this.labelArraw = new JLabel();
        this.labelArraw.setIcon(new MetalComboBoxIcon());
        this.setLayout(new GridBagLayout());
        switch (alignment) {
            case HORIZONTAL:
                initHorizaltalLayout();
                break;
            case VERTICAL:
                initVerticalLayout();
            default:
                break;
        }

    }

    private void initHorizaltalLayout() {
        this.add(this.labelImage, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(1).setWeight(1, 1));
//        this.add(this.panelColorDisplay, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(1).setWeight(1, 1));
        this.add(this.labelArraw, new GridBagConstraintsHelper(1, 0, 1, 2).setAnchor(GridBagConstraints.CENTER).setInsets(1).setWeight(1, 1));
    }

    private void initVerticalLayout() {
        this.add(this.labelImage, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(1).setWeight(1, 1));
        this.add(this.labelName, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(1).setWeight(1, 1));
        this.add(this.labelArraw, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(1).setWeight(1, 1));
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    /**
     * 修改图片颜色并获取新的图片
     *
     * @param image
     * @return
     * @throws Exception
     */
    public Image getImage(String image) {
        BufferedImage imageNew = null;
        try {
            File file = new File(image);
            BufferedImage bi = null;
            bi = ImageIO.read(file);
            int width = bi.getWidth();
            int height = bi.getHeight();
            int[] imageArrayOne = new int[width * height];
            imageArrayOne = bi.getRGB(0, 0, width, height, imageArrayOne, 0, width);
            for (int i = 0; i < imageArrayOne.length; i++) {
                if (imageArrayOne[i] == defualtColor.getRGB()) {
                    imageArrayOne[i] = selectedColor.getRGB();
                }
            }
            imageNew = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            imageNew.setRGB(0, 0, width, height, imageArrayOne, 0, width);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageNew;
    }


    public void setEnable(boolean enabled) {
        this.labelImage.setEnabled(enabled);
        this.labelArraw.setEnabled(enabled);
        if (alignment == VERTICAL) {
            this.labelName.setEnabled(enabled);
        }
    }

    @Override
    public void setRecordset(Recordset recordset) {
        this.recordset = recordset;
    }

}
