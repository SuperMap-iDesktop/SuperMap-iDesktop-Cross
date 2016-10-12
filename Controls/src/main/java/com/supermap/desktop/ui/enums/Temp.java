package com.supermap.desktop.ui.enums;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * Created by Administrator on 2016/10/12.
 */
public class Temp extends JFrame {
    class CheckHeaderCellRenderer implements TableCellRenderer {
        CheckTableModle tableModel;
        JTableHeader tableHeader;
        final JCheckBox selectBox;

        public CheckHeaderCellRenderer(JTable table) {
            this.tableModel = (CheckTableModle) table.getModel();
            this.tableHeader = table.getTableHeader();
            selectBox = new JCheckBox(tableModel.getColumnName(0));
            selectBox.setSelected(false);
            tableHeader.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 0) {
                        //获得选中列
                        int selectColumn = tableHeader.columnAtPoint(e.getPoint());
                        if (selectColumn == 0) {
                            boolean value = !selectBox.isSelected();
                            selectBox.setSelected(value);
                            tableModel.selectAllOrNull(value);
                            tableHeader.repaint();
                        }
                    }
                }
            });
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            // TODO Auto-generated method stub
            String valueStr = (String) value;
            JLabel label = new JLabel(valueStr);
            label.setHorizontalAlignment(SwingConstants.CENTER); // 表头标签居中
            selectBox.setHorizontalAlignment(SwingConstants.CENTER);// 表头标签居中
            selectBox.setBorderPainted(true);
            JComponent component = (column == 0) ? selectBox : label;

            component.setForeground(tableHeader.getForeground());
            component.setBackground(tableHeader.getBackground());
            component.setFont(tableHeader.getFont());
            component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

            return component;
        }

    }

    class CheckTableModle extends DefaultTableModel {

        public CheckTableModle(Vector data, Vector columnNames) {
            super(data, columnNames);
        }

        // /**
        // * 根据类型返回显示控件
        // * 布尔类型返回显示checkbox
        // */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public void selectAllOrNull(boolean value) {
            for (int i = 0; i < getRowCount(); i++) {
                this.setValueAt(value, i, 0);
            }
        }

    }


    private JPanel contentPane;
    private JTable table;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Temp frame = new Temp();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Temp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("CheckBox Table");
        this.setPreferredSize(new Dimension(400, 300));
        // setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        initTable();
        pack();
    }

    private void initTable() {
        Vector headerNames = new Vector();
        headerNames.add("列选择");
        headerNames.add("姓名");
        headerNames.add("年龄");
        Vector data = this.getData();
        CheckTableModle tableModel = new CheckTableModle(data, headerNames);
        table.setModel(tableModel);
        table.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(table));
    }

    /**
     * 获得数据
     *
     * @return
     */
    private Vector getData() {
        Vector data = new Vector();
        Vector rowVector1 = new Vector();
        rowVector1.add(false);
        rowVector1.add("Benson");
        rowVector1.add("25");

        Vector rowVector2 = new Vector();
        rowVector2.add(false);
        rowVector2.add("Laura");
        rowVector2.add("26");

        Vector rowVector3 = new Vector();
        rowVector3.add(false);
        rowVector3.add("YOYO");
        rowVector3.add("1");

        data.add(rowVector1);
        data.add(rowVector2);
        data.add(rowVector3);

        return data;
    }
}
