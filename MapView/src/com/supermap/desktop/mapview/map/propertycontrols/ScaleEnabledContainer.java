package com.supermap.desktop.mapview.map.propertycontrols;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.table.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import com.supermap.desktop.*;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.mapview.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.Map;

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
	private List<ScaleDisplay> scaleDisplays;
	private JPanel panelButton;
	private final String urlStr = "/com/supermap/desktop/coreresources/ToolBar/";
	private final Color selectColor = new Color(185, 214, 255);
	private Map map;
	private MouseAdapter localMouseAdapter;
	private ActionListener panelButtonAction;
	private double[] scales;
	private MapBoundsPropertyControl control;
	private String[] title = { MapViewProperties.getString("String_Index"), MapViewProperties.getString("String_Scales") };
	private DecimalFormat format = new DecimalFormat("#.############");

	private TableModelListener tableModelListener = new TableModelListener() {

		@Override
		public void tableChanged(TableModelEvent e) {
			tableModelListener(e);
		}
	};
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
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
		this.localMouseAdapter = new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
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
					for (int i = selectRow.length - 1; i >= 0; i--) {
						scaleDisplays.remove(selectRow[i]);
					}
					getTable();
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
		this.buttonSelectAll.addMouseListener(this.localMouseAdapter);
		this.buttonInvertSelect.addMouseListener(this.localMouseAdapter);
		this.buttonDelete.addMouseListener(this.localMouseAdapter);
		this.buttonImport.addMouseListener(this.localMouseAdapter);
		this.buttonExport.addMouseListener(this.localMouseAdapter);
		this.buttonOk.addActionListener(this.panelButtonAction);
		this.buttonCancel.addActionListener(this.panelButtonAction);
		this.table.addMouseListener(this.mouseAdapter);
	}

	private void removeRepeatStr(List<ScaleDisplay> scaleDisplays) {
		int count = scaleDisplays.size();
		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				if (scaleDisplays.get(i).getScale().equals(scaleDisplays.get(j).getScale())) {
					scaleDisplays.remove(i);
					count--;
				}
			}
		}
	}

	private double[] sort(List<ScaleDisplay> scaleDisplays) {
		double[] needList = new double[scaleDisplays.size()];
		for (int i = 0; i < scaleDisplays.size(); i++) {
			try {
				needList[i] = new ScaleModel(scaleDisplays.get(i).getScale()).getScale();
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
				scaleDisplays.add(new ScaleDisplay(new ScaleModel(needList[i]).getScaleCaption()));
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
		String oldScale = scaleDisplays.get(selectRow).getScale();
		String selectScale = table.getValueAt(selectRow, 1).toString();
		if (scaleIsRight(selectScale) && selectScale.contains(":")) {
			setTableCell(selectRow, selectScale);
		}
		if (scaleIsRight(selectScale) && !selectScale.contains(":")) {
			setTableCell(selectRow, "1:" + selectScale);
		}
		if (!scaleIsRight(selectScale)) {
			setTableCell(selectRow, oldScale);
			Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_ErrorInput"));
		}
	}

	private void setTableCell(int selectRow, String selectScale) {
		scaleDisplays.get(selectRow).setScale(selectScale);
		getTable();
		return;
	}

	private boolean scaleIsRight(String scale) {
		boolean scaleIsRight = false;
		if (StringUtilties.isNumber(scale)) {
			scaleIsRight = true;
		}
		if (scale.contains(":") && scale.split(":").length == 2) {
			String[] scaleList = scale.split(":");
			if (scaleList[0].equals("1") && StringUtilties.isNumber(scaleList[1])) {
				scaleIsRight = true;
			}
		}
		return scaleIsRight;
	}

	private void unRegistEvents() {
		this.buttonSelectAll.removeMouseListener(this.localMouseAdapter);
		this.buttonInvertSelect.removeMouseListener(this.localMouseAdapter);
		this.buttonDelete.removeMouseListener(this.localMouseAdapter);
		this.buttonImport.removeMouseListener(this.localMouseAdapter);
		this.buttonExport.removeMouseListener(this.localMouseAdapter);
		this.buttonOk.removeActionListener(this.panelButtonAction);
		this.buttonCancel.removeActionListener(this.panelButtonAction);
		this.table.removeMouseListener(this.mouseAdapter);
	}

	protected void importXml(String string) {
		try {
			String filePath = getFilePath(string, false);
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String tempstr = "";
			while ((tempstr = br.readLine()) != null) {
				if (tempstr.contains("<Scale>")) {
					tempstr = tempstr.substring(tempstr.indexOf(">") + 1, tempstr.lastIndexOf("<"));
					if (!haveScale(scaleDisplays, tempstr)) {
						scaleDisplays.add(new ScaleDisplay(tempstr));
					}
				}
			}
			sort(scaleDisplays);
			getTable();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean haveScale(List<ScaleDisplay> tempScaleDisplays, String tempstr) {
		boolean haveScale = false;
		for (int i = 0; i < tempScaleDisplays.size(); i++) {
			if (tempScaleDisplays.get(i).getScale().equals(tempstr)) {
				haveScale = true;
			}
		}
		return haveScale;
	}

	private String getFilePath(String module, boolean isOutport) {
		String filePath = "";
		String title = CommonProperties.getString("String_ToolBar_Import");
		if (isOutport) {
			title = CommonProperties.getString("String_Button_Export");
		}
		if (!SmFileChoose.isModuleExist(module)) {
			String fileFilter = SmFileChoose.createFileFilter(MapViewProperties.getString("String_ScaleFile"), "xml");
			SmFileChoose.addNewNode(fileFilter, MapViewProperties.getString("String_ScaleFile"), title, module, "OpenMany");
		}
		SmFileChoose fileChoose = new SmFileChoose(module);
		if (isOutport) {
			fileChoose.setSelectedFile(new File(MapViewProperties.getString("String_Scales") + ".xml"));
		}
		int state = fileChoose.showDefaultDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			filePath = fileChoose.getFilePath();
		}
		return filePath;
	}

	protected void exportXml(String module) {
		createXml(getFilePath(module, true));
	}

	private void createXml(String filename) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element scales = document.createElement("Scales");
			scales.setAttribute("xmlns", "http://www.supermap.com.cn/desktop");
			scales.setAttribute("version", "8.0.x");
			document.appendChild(scales);
			removeRepeatStr(scaleDisplays);
			sort(scaleDisplays);
			for (int i = 0; i < scaleDisplays.size(); i++) {
				Element scale = document.createElement("Scale");
				String scaleCaption = scaleDisplays.get(i).getScale();
				scale.appendChild(document.createTextNode(scaleCaption));
				scale.setNodeValue(scaleCaption);
				scales.appendChild(scale);
			}
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			if (StringUtilties.isNullOrEmpty(filename)) {
				return;
			}
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
				parseFileToXML(transformer, source, file);
			} else if (JOptionPane.OK_OPTION == new SmOptionPane().showConfirmDialog(MapViewProperties.getString("String_RenameFile_Message"))) {
				parseFileToXML(transformer, source, file);
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
		this.getContentPane().add(this.toolbar,     new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5));
		this.getContentPane().add(this.scrollPane,  new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setFill(GridBagConstraints.BOTH).setWeight(1, 4));
		this.getContentPane().add(this.panelButton, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.EAST).setInsets(0));
		//@formatter:on
	}

	private void initPanelButton() {
		this.buttonOk = new SmButton();
		this.buttonCancel = new SmButton();
		this.panelButton = new JPanel();
		//@formatter:off
		this.panelButton.setLayout(new GridBagLayout());
		this.panelButton.add(this.buttonOk,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(10,0,10,10));
		this.panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(10,0,10,10));
		//@formatter:on
	}

	private void initScrollPane() {
		this.scaleDisplays = new ArrayList<ScaleEnabledContainer.ScaleDisplay>();
		this.scrollPane = new JScrollPane();
		this.table = new JTable();
		getTable();
		this.scrollPane.setViewportView(this.table);
	}

	private void initToolBar() {
		this.toolbar = new JToolBar();
		this.toolbar.setFloatable(false);
		this.buttonSelectAll = new JButton(new ImageIcon(ScaleEnabledContainer.class.getResource(urlStr + "Image_ToolButton_SelectAll.png")));
		this.buttonInvertSelect = new JButton(new ImageIcon(ScaleEnabledContainer.class.getResource(urlStr + "Image_ToolButton_SelectInverse.png")));
		this.buttonDelete = new JButton(new ImageIcon(ScaleEnabledContainer.class.getResource(urlStr + "Image_ToolButton_Delete.png")));
		this.buttonImport = new JButton(new ImageIcon(ScaleEnabledContainer.class.getResource(urlStr + "Image_ToolButton_Import.png")));
		this.buttonExport = new JButton(new ImageIcon(ScaleEnabledContainer.class.getResource(urlStr + "Image_ToolButton_Export.png")));
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
		if (selectRow > 0 && selectRow + 1 != table.getRowCount()) {
			// 有选中项或者选中项不是0
			String scaleNext = table.getValueAt(selectRow + 1, 1).toString();
			String scaleNow = table.getValueAt(selectRow, 1).toString();
			double scaleNextD = Double.parseDouble(scaleNext.split(":")[1]);
			double scaleNowD = Double.parseDouble(scaleNow.split(":")[1]);
			ScaleDisplay scaleInsert = new ScaleDisplay("1:" + String.valueOf(format.format((scaleNextD + scaleNowD) / 2)));
			this.scaleDisplays.add(selectRow + 1, scaleInsert);
			getTable();
			this.table.addRowSelectionInterval(selectRow + 1, selectRow + 1);
			checkButtonState();
			return;
		}
		if (table.getRowCount() == 0) {
			// 表中没有数据时
			double scale = map.getScale();
			String scaleCaption = new ScaleModel(format.format(scale)).getScaleCaption();
			ScaleDisplay scaleDisplay = new ScaleDisplay(scaleCaption);
			this.scaleDisplays.add(scaleDisplay);
			getTable();
			checkButtonState();
			return;
		}

		if (selectRow == table.getRowCount() - 1 || table.getRowCount() > 0 && selectRow < 0) {
			// 没有选中项，但是表中有数据时
			String scaleEnd = table.getValueAt(table.getRowCount() - 1, 1).toString();
			double scaleEndD = Double.parseDouble(scaleEnd.split(":")[1]);
			ScaleDisplay scaleLast = new ScaleDisplay("1:" + String.valueOf(format.format(scaleEndD / 2)));
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
			this.labelAddScale = new JLabel(new ImageIcon(ScaleEnabledContainer.class.getResource(urlStr + "Image_ToolButton_AddScale.png")));
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
									ScaleDisplay scaleDisplay = new ScaleDisplay(new ScaleModel(map.getScale()).getScaleCaption());
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
					if ((new ScaleModel(map.getScale()).getScaleCaption()).equals(scaleDisplays.get(i).getScale())) {
						exist = true;
					}
				}
			}
			return exist;
		}

		private void initToolBarComponents() {
			this.addScale = new DataCell(MapViewProperties.getString("String_AddScale"), new ImageIcon(ScaleEnabledContainer.class.getResource(urlStr
					+ "Image_ToolButton_AddScale.png")));
			this.addDefaultScale = new DataCell(MapViewProperties.getString("String_AddCurrentScale"), new ImageIcon(
					ScaleEnabledContainer.class.getResource(urlStr + "Image_ToolButton_DefaultScale.png")));
			this.setLayout(new GridBagLayout());
			this.add(this.addScale, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST));
			this.add(this.addDefaultScale, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST));
		}

	}

	class ScaleDisplay {
		String scale;

		public ScaleDisplay(String scale) {
			this.scale = scale;
		}

		public String getScale() {
			return scale;
		}

		public void setScale(String scale) {
			this.scale = scale;
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
			for (int i = 0; i < scales.length; i++) {
				ScaleDisplay tempdDisplay = new ScaleDisplay(new ScaleModel(scales[i]).getScaleCaption());
				scaleDisplays.add(tempdDisplay);
			}
		}
		getTable();
	}

	private void getTable() {
		int row = scaleDisplays.size();
		this.table.setModel(new LocalDefualTableModel(new Object[row][2], title));
		for (int i = 0; i < row; i++) {
			this.table.setValueAt(i + 1, i, 0);
			this.table.setValueAt(scaleDisplays.get(i).getScale(), i, 1);
		}
		this.table.getColumn(MapViewProperties.getString("String_Index")).setMaxWidth(40);
		this.table.getModel().removeTableModelListener(this.tableModelListener);
		this.table.getModel().addTableModelListener(this.tableModelListener);
	}

}
