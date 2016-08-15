package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.desktop.Application;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.ColorSelectionPanel;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.PathUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by xie on 2016/8/12.
 */
public class CADStylePanel extends JPanel {
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

    public static final int POINT_TYPE = 1;
    public static final int LINE_TYPE = 2;
    public static final int FILL_TYPE = 3;
    public static final int HORIZONTAL = 4;
    public static final int VERTICAL = 5;


    public CADStylePanel(int cadType, int alignment) {
        super();
        this.cadType = cadType;
        this.alignment = alignment;
        initComponents();
        COLOR_SYSTEM_DEFAULT = this.getBackground();
        initResources();
        registEvents();
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
                            ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
                            Rectangle Rectangle = e.getComponent().getBounds();
                            final JPopupMenu popupMenu = new JPopupMenu();
                            popupMenu.setBorderPainted(false);
                            popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
                            colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
                            popupMenu.show(e.getComponent(), 0, (int) (Rectangle.getHeight()));
                            colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
                                @Override
                                public void propertyChange(PropertyChangeEvent evt) {
                                    selectedColor = (Color) evt.getNewValue();
//                                    panelColorDisplay.setBackground(selectedColor);
//                                    panelColorDisplay.updateUI();
                                    System.out.println(labelImage.getIcon().toString());
                                    if (null != getImage(defulatPath)) {
                                        labelImage.setIcon(new ImageIcon(getImage(defulatPath)));
                                    }
                                    popupMenu.setVisible(false);
                                }
                            });
                            break;
                        case VERTICAL:
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }


    private void initResources() {
        switch (cadType) {
            case POINT_TYPE:
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
            int[] rgb = new int[3];
            File file = new File(image);
            BufferedImage bi = null;
            bi = ImageIO.read(file);
            int width = bi.getWidth();
            int height = bi.getHeight();
            int[] imageArrayOne = new int[width * height];
            imageArrayOne = bi.getRGB(0,0,width,height,imageArrayOne,0,width);
            for (int i=0;i<imageArrayOne.length;i++){
                if (imageArrayOne[i]==defualtColor.getRGB()){
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
        this.labelName.setEnabled(enabled);
    }
}
