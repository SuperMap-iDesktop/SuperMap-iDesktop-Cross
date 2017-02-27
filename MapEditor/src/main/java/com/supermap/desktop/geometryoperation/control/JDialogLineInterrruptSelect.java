package com.supermap.desktop.geometryoperation.control;

import com.supermap.data.Recordset;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author huqing
 */
public class JDialogLineInterrruptSelect extends SmDialog{
	private JButton buttonOK= ComponentFactory.createButtonOK();
	private JButton buttonCancel=ComponentFactory.createButtonCancel();
	private JPanel panelWindow=new JPanel();
	private JScrollPane tableScrollpane=new JScrollPane();
	private JTable lineInterruptSelectTable=null;
	private JPanel panelButtons=null;
	private Recordset recordset=null;
	private Object data[][]=null;
	private ArrayList<Integer> idsArrayList=null;
	private final static  int COLOMN_COUT=4;

	//private Object[] tableHeadTitles={"参与打断","对象","SMID","对象所在图层"};
	private Object[] tableHeadTitles={MapEditorProperties.getString("String_Interrupt"),
			MapEditorProperties.getString("String_Object"),
			"SMID",
			MapEditorProperties.getString("String_LayersOfObject")};
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == JDialogLineInterrruptSelect.this.buttonOK) {
				JDialogLineInterrruptSelect.this.setDialogResult(DialogResult.OK);
				JDialogLineInterrruptSelect.this.dispose();
			} else if (e.getSource() == JDialogLineInterrruptSelect.this.buttonCancel) {
				JDialogLineInterrruptSelect.this.dispose();
			}
		}
	};
	private KeyListener KeyListener=new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar()==KeyEvent.VK_ENTER){
				JDialogLineInterrruptSelect.this.setDialogResult(DialogResult.OK);
				JDialogLineInterrruptSelect.this.dispose();
			}else if(e.getKeyChar()== KeyEvent.VK_ESCAPE){
				JDialogLineInterrruptSelect.this.dispose();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	};
	public  JDialogLineInterrruptSelect(Recordset recordset,ArrayList<Integer> idsArrayListList){
		//查询的结果
		this.recordset=recordset;
		this.idsArrayList=idsArrayListList;
		InitComponents();
		unRegisterEvents();
		registerEvents();
	}
	private void InitComponents(){
		this.buttonOK.setSelected(true);
		this.setTitle(MapEditorProperties.getString("String_DialogSelectedTitle"));
		this.setSize(500, 250);
		setLocationRelativeTo(null);

		//table内容的model
		this.data=this.getData();
		LineInterruptSelectTableModel tableModel=new LineInterruptSelectTableModel(this.recordset,tableHeadTitles);
		this.lineInterruptSelectTable=new JTable(tableModel);
		//设置线段选择的 鼠标监听
		this.lineInterruptSelectTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowIndex =lineInterruptSelectTable.getSelectedRow();
				int colomnIndex=lineInterruptSelectTable.getSelectedColumn();
				if(colomnIndex==0){
					lineInterruptSelectTable.setValueAt(!(Boolean)lineInterruptSelectTable.getValueAt(rowIndex,colomnIndex),rowIndex,colomnIndex);
				}
			}
		});

		//关闭自适应，根据拉伸table也拉伸
		this.lineInterruptSelectTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//设置行高
		this.lineInterruptSelectTable.setRowHeight(23);
		this.tableScrollpane.getViewport().setView(this.lineInterruptSelectTable);
		this.setLayout(new GridBagLayout());
		//设置渲染
		this.lineInterruptSelectTable.setDefaultRenderer(JLabel.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel tempLabel=(JLabel)value;
				tempLabel.setOpaque(true);
				tempLabel.setBackground(isSelected ? new Color(51, 153, 255) : Color.white);
				tempLabel.setForeground(isSelected? Color.white:Color.black);
				tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
				return tempLabel;

			}
		});
		this.lineInterruptSelectTable.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(this.lineInterruptSelectTable));

		this.panelWindow.setLayout(new BorderLayout());
		this.panelWindow.add(this.tableScrollpane,BorderLayout.CENTER);

		this.panelButtons=new JPanel();
		this.panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(2, 0, 2, 10));
		panelButtons.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setInsets(2, 0, 2, 10));
		this.getContentPane().add(this.tableScrollpane,new GridBagConstraintsHelper(0,0,1,1).setFill(GridBagConstraints.BOTH).setWeight(1,1).setInsets(4,10,0,10));
		this.getContentPane().add(this.panelButtons,new GridBagConstraintsHelper(0,1,1,1).setWeight(0,0).setAnchor(GridBagConstraints.EAST).setInsets(4,20,4,0));
	}
	private void registerEvents(){
		this.buttonOK.addActionListener(actionListener);
		this.buttonOK.addKeyListener(KeyListener);
		this.buttonCancel.addKeyListener(KeyListener);
		this.buttonCancel.addActionListener(actionListener);
	}
	private void unRegisterEvents(){
		this.buttonOK.removeKeyListener(KeyListener);
		this.buttonOK.removeActionListener(actionListener);
		this.buttonCancel.removeKeyListener(KeyListener);
		this.buttonCancel.removeActionListener(actionListener);
	}
	//获取当前对话框填充到table中的数据
	private Object[][] getData(){
		Object data[][]=new Object[this.idsArrayList.size()][JDialogLineInterrruptSelect.COLOMN_COUT];
		this.recordset.moveFirst();
		for(int i=0;i<this.idsArrayList.size();i++){
			recordset.seekID(idsArrayList.get(i));
			data[i][0] = true;
			data[i][1]=new JLabel("第"+(i+1)+"个对象");
			data[i][2]=new JLabel(this.recordset.getID()+"");
			data[i][3]=new JLabel(this.recordset.getDataset().getName()+"@"+
					this.recordset.getDataset().getDatasource().getAlias());
		}
//		for(int i=0;i<this.recordset.getRecordCount();i++){
//			data[i][0] = true;
//			data[i][1]=new JLabel("第"+(i+1)+"个对象");
//			data[i][2]=new JLabel(this.recordset.getID()+"");
//			data[i][3]=new JLabel(this.recordset.getDataset().getName()+"@"+
//					this.recordset.getDataset().getDatasource().getAlias());
//			this.recordset.moveNext();
//		}
		return data;
	}
	//选择最终要被打断的线
	public ArrayList<Integer>getSelectedLineIds(){
		ArrayList<Integer> ids=new ArrayList<>();
		this.recordset.moveFirst();
		for(int i=0;i<JDialogLineInterrruptSelect.this.idsArrayList.size();i++){
			if(this.lineInterruptSelectTable.getValueAt(i,0).equals(true)){
				ids.add(recordset.getID());
			}
			this.recordset.moveNext();
		}
		return ids;
	}
	//表模型
	class LineInterruptSelectTableModel extends DefaultTableModel {
		private Recordset recordset=null;
		LineInterruptSelectTableModel(Recordset recordset,Object[] tableTitle){
			super(JDialogLineInterrruptSelect.this.data,tableTitle);
			this.recordset=recordset;
		}
		@Override
		public int getRowCount() {
			return JDialogLineInterrruptSelect.this.data !=null? JDialogLineInterrruptSelect.this.data.length:0;
		}
		@Override
		public int getColumnCount() {
			return JDialogLineInterrruptSelect.this.data!=null? JDialogLineInterrruptSelect.this.data[0].length:0;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
				//return JDialogLineInterrruptSelect.this.lineInterruptSelectTable.getModel().getValueAt(rowIndex,columnIndex);
				return JDialogLineInterrruptSelect.this.data[rowIndex][columnIndex];

		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if(columnIndex==0)
				return Boolean.class;
			if(columnIndex==1)
				return JLabel.class;
			if(columnIndex==2)
				return JLabel.class;
			if(columnIndex==3)
				return JLabel.class;
			return null;
			//return super.getColumnClass(columnIndex);
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			super.setValueAt(aValue, row, column);
			JDialogLineInterrruptSelect.this.data[row][column]=(Boolean)aValue;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public void selectAllORNull(boolean isSelected){
			if(JDialogLineInterrruptSelect.this.data!=null){
				for(int i=0;i<this.getRowCount();i++){
					this.setValueAt(isSelected,i,0);
				}
			}
		}
	}
	//表头渲染期器
	public class CheckHeaderCellRenderer implements TableCellRenderer {
		LineInterruptSelectTableModel tableModel;
		JTableHeader tableHeader;
		final JCheckBox selectBox;

		public CheckHeaderCellRenderer(JTable table) {
			this.tableModel = (LineInterruptSelectTableModel)table.getModel();
			this.tableHeader = table.getTableHeader();
			selectBox = new JCheckBox(tableModel.getColumnName(0));
			selectBox.setSelected(true);
			tableHeader.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() > 0) {
						//获得选中列
						int selectColumn = tableHeader.columnAtPoint(e.getPoint());
						if (selectColumn == 0) {
							boolean value = !selectBox.isSelected();
							selectBox.setSelected(value);
							tableModel.selectAllORNull(value);
							tableHeader.repaint();
						}
					}
				}
			});
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
		                                               boolean isSelected, boolean hasFocus, int row, int column) {
			String valueStr = (String) value;
			JLabel label = new JLabel(valueStr);
			label.setHorizontalAlignment(SwingConstants.CENTER); // 表头标签剧中
			selectBox.setHorizontalAlignment(SwingConstants.CENTER);// 表头标签剧中
			selectBox.setBorderPainted(true);
			JComponent component = (column == 0) ? selectBox : label;

			component.setForeground(tableHeader.getForeground());
			component.setBackground(tableHeader.getBackground());
			component.setFont(tableHeader.getFont());
			component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

			return component;
		}

	}

}
