package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.Colors;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chens on 2017/1/11 0011.
 */
public class ColorsWithKeysTableModel extends DefaultTableModel {
    private static final String[] columnNames = new String[]{
            CommonProperties.getString(CommonProperties.Index),
            ControlsProperties.getString("String_Column_Color"),
            ControlsProperties.getString("String_ColumnValue")
    };

    private final Color defaultColor = new Color(255, 220, 251);
    private List<ColorNode> colorNodes;

    public void setColorNodes(Color[] colors, double[] keys) {
        colorNodes = new ArrayList<>();
        for (int i = 0; i < colors.length; i++) {
            colorNodes.add(new ColorNode(colors[i], keys[i]));
        }
    }

    public void addColorNodes(Color[] colors,double[] keys){
        for (int i = 0; i < colors.length; i++) {
            this.colorNodes.add(new ColorNode(colors[i], keys[i]));
        }
    }

    public List<ColorNode> getColorNodes() {
        return colorNodes;
    }

    public void setColors(List<Color> colors) {
        for (int i = 0; i < colors.size(); i++) {
            colorNodes.get(i).setColor(colors.get(i));
        }
    }

    public void setColors(Colors colors) {
        Color[] colors1= colors.toArray();
        for (int i = 0; i < colors1.length; i++) {
            colorNodes.get(i).setColor(colors1[i]);
        }
    }

    public void setKeys(List<Double> keys) {
        for (int i = 0; i < keys.size(); i++) {
            colorNodes.get(i).setKey(keys.get(i));
        }
    }

    public void setKeys(double[] keys) {
        for (int i = 0; i < keys.length; i++) {
            colorNodes.get(i).setKey(keys[i]);
        }
    }

    public List<Color> getColors() {
        List<Color> colors = new ArrayList<Color>();
        for (ColorNode colorNode:colorNodes) {
            colors.add(colorNode.getColor());
        }
        return colors;
    }

    public List<Double> getKeys() {
        List<Double> keys = new ArrayList<Double>();
        for (ColorNode colorNode:colorNodes) {
            keys.add(colorNode.getKey());
        }
        return keys;
    }

    public double getLastKey() {
        return colorNodes.get(colorNodes.size() - 1).getKey();
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return row + 1;
            case 1:
                return colorNodes.get(row).getColor();
            case 2:
                return colorNodes.get(row).getKey();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        switch (column) {
            case 0:
                throw new UnsupportedOperationException();
            case 1:
                colorNodes.get(row).setColor((Color) aValue);
                break;
            case 2:
                colorNodes.get(row).setKey((double) aValue);
                break;
        }
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column >= 1;
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Color.class;
            case 2:
                return Double.class;
        }
        return null;
    }

    @Override
    public int getRowCount() {
        if (colorNodes == null) {
            return 0;
        }
        return colorNodes.size();
    }

    public void moveToTop(int... rows) {
        for (int i = 0; i < rows.length; i++) {
            moveTo(rows[i], i);
        }
        fireTableDataChanged();
    }

    public void moveUp(int... rows) {
        for (int row : rows) {
            moveTo(row, row - 1);
        }
        fireTableDataChanged();
    }

    public void moveDown(int... rows) {
        for (int i = rows.length - 1; i >= 0; i--) {
            moveTo(rows[i], rows[i] + 1);
        }
        fireTableDataChanged();
    }

    public void moveToBottom(int... rows) {
        int count = colorNodes.size();
        for (int i = rows.length - 1; i >= 0; i--) {
            moveTo(rows[i], i + count - rows.length);
        }
        fireTableDataChanged();
    }

    public void add(Color color) {
        int size = colorNodes.size();
        double key;
        if (size == 0) {
            //列表为空时直接加0
            key = 0;
        } else if (size == 1) {
            //列表只有一个记录时，步长为1，得到追加的值
            key = colorNodes.get(0).getKey() + 1;
        } else {
            //从最后两个值等差递增得到第三个值
            key = colorNodes.get(size - 1).getKey() * 2 - colorNodes.get(size - 2).getKey();
        }
        colorNodes.add(new ColorNode(color, key));
        fireTableDataChanged();
    }

    public void insert(int selectedRow, Color color) {
        if (selectedRow == colorNodes.size() - 1) {
            add(color);
        } else {
            double key = (colorNodes.get(selectedRow).getKey() + colorNodes.get(selectedRow + 1).getKey()) / 2;
            colorNodes.add(selectedRow + 1, new ColorNode(color, key));
        }
        fireTableDataChanged();
    }

    private void moveTo(int beforeRow, int resultRow) {
        if (beforeRow > resultRow) {
            for (int i = beforeRow; i > resultRow; i--) {
                swap(i, i - 1);
            }
        } else if (beforeRow < resultRow) {
            for (int i = beforeRow; i < resultRow; i++) {
                swap(i, i + 1);
            }
        }
    }

    private void swap(int i, int i1) {
        //只交换颜色，实现的目的是不同段值颜色交换，因而段值无需也不能交换
        Color color = colorNodes.get(i).getColor();
        colorNodes.get(i).setColor(colorNodes.get(i1).getColor());
        colorNodes.get(i1).setColor(color);
    }

    public void removeRow(int[] selectedRow) {
        for (int i = selectedRow.length - 1; i >= 0; i--) {
            colorNodes.remove(selectedRow[i]);
        }
        fireTableDataChanged();
    }

    public void colorInvert(int[] selectedRow) {
        for (int i = 0; i < selectedRow.length / 2; i++) {
            swap(i, selectedRow.length - 1 - i);
        }
        fireTableDataChanged();
    }

    public void colorInvert() {
        for (int i = 0; i < colorNodes.size() / 2; i++) {
            swap(i, colorNodes.size() - 1 - i);
        }
        fireTableDataChanged();
    }

    protected class ColorNode{
        private Color color;
        private double key;

        public ColorNode(Color color, double key) {
            this.color = color;
            this.key = key;
        }

        public Color getColor() {
            return color;
        }

        public double getKey() {
            return key;
        }

        public void setKey(double key) {
            this.key = key;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }
}
