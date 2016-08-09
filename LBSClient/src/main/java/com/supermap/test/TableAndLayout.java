package com.supermap.test;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableAndLayout extends JDialog {
	
	private JLabel labelServerURL;
	private JTextField textServerURL;
	private JButton buttonBrowser;
	
	private JTable table1;
	private JTable table2;
	
	private JLabel labelStatus;
	private JButton buttonOK;
	private JButton buttonCancel;
	
	public static void main(String[] args) {
		TableAndLayout dialog = new TableAndLayout();
		dialog.setVisible(true);
	}
	
	public TableAndLayout() {
		initializeComponents();
	}
	
	public void initializeComponents() {
		this.setSize(900, 600);
		this.setLocation(400, 300);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.labelServerURL = new JLabel("服务器地址:");
		this.textServerURL = new JTextField("webURL");
		this.buttonBrowser = new JButton("浏览");
		
		this.table1 = new JTable(new Table1Model(
				new String[]{"区域编号","区域名称","区域坐标","总卡数","记录时间"}, 
				new Object[][]{{"区域编号","区域名称","区域坐标","总卡数","记录时间"}, {"区域编号","区域名称","区域坐标","总卡数","记录时间"}} 
				));
		this.table2 = new JTable(new Table2Model());

		this.labelStatus = new JLabel("服务器链接状态 : Connected.");
		this.buttonOK = new SmButton(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel = new SmButton(CommonProperties.getString("String_Button_Cancel"));
		
		GroupLayout gLayout = new GroupLayout(this.getContentPane());
		gLayout.setAutoCreateContainerGaps(true);
		gLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(gLayout);		

		JScrollPane scrollPaneLeft = new JScrollPane(this.table1);
		
		JSplitPane splitPane = new JSplitPane();// 创建一个分割容器类
		splitPane.setOneTouchExpandable(true);// 让分割线显示出箭头
		splitPane.setContinuousLayout(true);// 操作箭头，重绘图形
		splitPane.setPreferredSize(new Dimension(100, 200));
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);// 设置分割线方向
		splitPane.setLeftComponent(scrollPaneLeft);
		splitPane.setRightComponent(this.table2);
		splitPane.setDividerSize(10);
		splitPane.setDividerLocation(200);// 设置分割线位于中央
		
		// @formatter:off
		gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(gLayout.createSequentialGroup().addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.buttonBrowser, 32, 32, 32))
				.addComponent(splitPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gLayout.createSequentialGroup().addComponent(this.labelStatus)
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK, 75, 75, 75)
						.addComponent(this.buttonCancel, 75, 75, 75)));
		gLayout.setVerticalGroup(gLayout.createSequentialGroup()
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonBrowser, 23, 23, 23))
				.addComponent(splitPane, gLayout.PREFERRED_SIZE, gLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelStatus)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
	}
	
	public class Table1Model extends AbstractTableModel {
				
//		String[] names = new String[]{"区域编号","区域名称","区域坐标","总卡数","记录时间"};
//		Object[][] objects = new Object[][]{{null,null,null,null,null}};
//		jTable1.setModel(new javax.swing.table.DefaultTableModel(
//		object, string));
		
		/**
		 * 列标题
		 */
		protected ArrayList<String> columnNames;
		
		/**
		 * 数据内容列表
		 */
		protected transient ArrayList<ArrayList<Object>> contents;
		
		/**
		 * 表格行后台数据列表
		 */
		protected transient ArrayList<Object> rowTagContents;

		
		/**
		 * 构造函数。
		 */
		public Table1Model() {
			// 列头序列
			this.columnNames = new ArrayList<String>();
			// 数据序列
			this.contents = new ArrayList<ArrayList<Object>>();
			this.rowTagContents = new ArrayList<Object>();
		}

		/**
		 * 构造函数。
		 * 
		 * @param columnNames 指定列头
		 */
		public Table1Model(String[] columnNames) {
			this();

			for (String columnName : columnNames) {
				// 初始化列头
				this.columnNames.add(columnName);
			}			
		}

		/**
		 * 构造函数。
		 * 
		 * @param datas 数据
		 * @param columnNames 列头
		 * @throws Exception 抛出数据数不正确的异常
		 */
		public Table1Model(String[] columnNames, Object[][] datas) {
			this(columnNames);
			// 刷新数据
			try {
				for (Object[] data : datas) {
					// 将数据逐行添加到列表中
					addRow(data);
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		
		@Override
		public int getRowCount() {
			return this.contents.size();
		}

		@Override
		public int getColumnCount() {
			return this.columnNames.size();
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int col) {
			return columnNames.get(col);
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return contents.get(rowIndex).get(columnIndex);
		}
		
		public void addRow(Object[] row) {
			ArrayList<Object> values = new ArrayList<Object>();
			for (Object value : row) {
				values.add(value);
			}
			contents.add(values);
		}
		
	}
	
	public class Table2Model extends AbstractTableModel {

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
