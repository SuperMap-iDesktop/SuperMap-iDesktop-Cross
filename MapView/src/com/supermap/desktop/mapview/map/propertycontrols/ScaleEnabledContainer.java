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
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.table.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import com.supermap.desktop.*;
import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.mapview.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;

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
	private double scale = 0.0;
	private MouseAdapter localMouseAdapter;
	private ScaleModel scaleModel;
	private ActionListener panelButtonAction;

	public ScaleEnabledContainer(Double scale) {
		this.scale = scale;
		try {
			this.scaleModel = new ScaleModel(scale);
		} catch (InvalidScaleException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		initComponents();
		initResources();
		registEvents();
		checkButtonState();
		addFocusTraversalPolicyList();
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
				}
				if (e.getSource() == buttonInvertSelect) {
					selectInvert(table);
				}
				if (e.getSource() == buttonDelete) {
					int[] selectRow = table.getSelectedRows();
					if (selectRow.length < table.getRowCount()) {
						((ScaleDisplayModel) table.getModel()).removeRows(selectRow);
						table.addRowSelectionInterval(0, 0);
					} else {
						int[] tempRow = new int[table.getRowCount()];
						for (int i = 0; i < table.getRowCount(); i++) {
							tempRow[i] = i;
						}
						((ScaleDisplayModel) table.getModel()).removeRows(tempRow);
					}
				}
				if (e.getSource() == buttonImport) {
					importXml("ImportScales");
				}
				if (e.getSource() == buttonExport) {
					exportXml("ExportScales");
				}
				checkButtonState();
			}
		};
		this.panelButtonAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==buttonOk) {
					dialogResult = DialogResult.OK;
					if (scaleDisplays.size()>0) {
						try {
							scale = new ScaleModel(scaleDisplays.get(0).getScale()).getScale();
						} catch (InvalidScaleException ex) {
							ex.printStackTrace();
						}
					}
					dispose();
				}
				if (e.getSource()==buttonCancel) {
					dispose();
				}
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
	}

	private void unRegistEvents() {
		this.buttonSelectAll.removeMouseListener(this.localMouseAdapter);
		this.buttonInvertSelect.removeMouseListener(this.localMouseAdapter);
		this.buttonDelete.removeMouseListener(this.localMouseAdapter);
		this.buttonImport.removeMouseListener(this.localMouseAdapter);
		this.buttonExport.removeMouseListener(this.localMouseAdapter);
		this.buttonOk.removeActionListener(this.panelButtonAction);
		this.buttonCancel.removeActionListener(this.panelButtonAction);
	}

	protected void importXml(String string) {
		try {
			String filePath = getFilePath(string);
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String tempstr = "";
			while ((tempstr = br.readLine()) != null) {
				if (tempstr.contains("<Scale>")) {
					tempstr = tempstr.substring(tempstr.indexOf(">") + 1, tempstr.lastIndexOf("<"));
					int count = table.getRowCount() + 1;
					if (count > 1) {
						count = Integer.valueOf(table.getValueAt(count - 2, 0).toString()) + 1;
					}
					ScaleDisplay scaleDisplay = new ScaleDisplay(count, tempstr);
					((ScaleDisplayModel) table.getModel()).addRow(scaleDisplay);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getFilePath(String module) {
		String filePath = "";
		if (!SmFileChoose.isModuleExist(module)) {
			String fileFilter = SmFileChoose.createFileFilter(MapViewProperties.getString("String_ScaleFile"), "xml");
			SmFileChoose.addNewNode(fileFilter, MapViewProperties.getString("String_ScaleFile"), CommonProperties.getString("String_ToolBar_Import"), module,
					"OpenMany");
		}
		SmFileChoose fileChoose = new SmFileChoose(module);
		fileChoose.setSelectedFile(new File(MapViewProperties.getString("String_Scales") + ".xml"));
		int state = fileChoose.showDefaultDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			filePath = fileChoose.getFilePath();
		}
		return filePath;
	}

	protected void exportXml(String module) {
		createXml(getFilePath(module));
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
			for (int i = 0; i < table.getRowCount(); i++) {
				Element scale = document.createElement("Scale");
				String scaleCaption = table.getValueAt(i, 1).toString();
				scale.appendChild(document.createTextNode(scaleCaption));
				scale.setNodeValue(scaleCaption);
				scales.appendChild(scale);
			}
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			PrintWriter pw = new PrintWriter(file);
			StreamResult streamResult = new StreamResult(pw);
			transformer.transform(source, streamResult);
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
	}

	private void resetButtonState(boolean enable) {
		this.buttonSelectAll.setEnabled(enable);
		this.buttonInvertSelect.setEnabled(enable);
		this.buttonDelete.setEnabled(enable);
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
		this.table = new JTable(new ScaleDisplayModel());
		this.table.getColumn(MapViewProperties.getString("String_Index")).setPreferredWidth(20);
		this.table.repaint();
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

	private void addScaleCaption() {
		int index = table.getRowCount() + 1;
		String scaleCaption = scaleModel.getScaleCaption();
		if (index > 1) {
			try {
				index = Integer.valueOf(table.getValueAt(table.getRowCount() - 1, 0).toString()) + 1;
				double tempScale = scale * Math.pow(2, index - 1);
				scaleCaption = new ScaleModel(tempScale).getScaleCaption();
			} catch (InvalidScaleException e1) {
				e1.printStackTrace();
			}
		}
		ScaleDisplay scaleDisplay = new ScaleDisplay(index, scaleCaption);
		((ScaleDisplayModel) table.getModel()).addRow(scaleDisplay);
		checkButtonState();
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
						addScaleCaption();
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
						addScaleCaption();
					} else {
						if (table.getRowCount() > 0) {
							table.setRowSelectionInterval(0, 0);
						} else {
							ScaleDisplay scaleDisplay = new ScaleDisplay(1, scaleModel.getScaleCaption());
							((ScaleDisplayModel) table.getModel()).addRow(scaleDisplay);
						}
						checkButtonState();
					}
				}

			};
			this.addScale.addMouseListener(this.mouseAdpter);
			this.addDefaultScale.addMouseListener(this.mouseAdpter);
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
		int index;
		String scale;

		public ScaleDisplay(int index, String scale) {
			this.index = index;
			this.scale = scale;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getScale() {
			return scale;
		}

		public void setScale(String scale) {
			this.scale = scale;
		}

	}

	class ScaleDisplayModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] title = { MapViewProperties.getString("String_Index"), MapViewProperties.getString("String_Scales") };

		public ScaleDisplayModel() {
			super();
		}

		@Override
		public int getRowCount() {
			return scaleDisplays.size();
		}

		@Override
		public int getColumnCount() {
			return title.length;
		}

		public void addRow(ScaleDisplay scaleDisplay) {
			scaleDisplays.add(scaleDisplay);
			fireTableRowsInserted(0, getRowCount());
		}

		public void removeRow(int i) {
			scaleDisplays.remove(i);
			fireTableRowsDeleted(0, getRowCount());
		}

		public void removeRows(int[] rows) {
			ArrayList<ScaleDisplay> removeInfo = new ArrayList<ScaleDisplay>();
			if (rows.length > 0) {
				for (int i = 0; i < rows.length; i++) {
					removeInfo.add(scaleDisplays.get(rows[i]));
				}
				scaleDisplays.removeAll(removeInfo);
				fireTableRowsDeleted(0, getRowCount());
			}
		}

		public void updateRows(List<ScaleDisplay> tempScaleDisplay) {
			scaleDisplays = (ArrayList<ScaleDisplay>) tempScaleDisplay;
			fireTableRowsUpdated(0, getRowCount());
		}

		@Override
		public String getColumnName(int columnIndex) {
			return title[columnIndex];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 1) {
				return true;
			}
			return false;
		}

		// 得到某行的数据
		public ScaleDisplay getTagValueAt(int tag) {
			return scaleDisplays.get(tag);
		}

		// 得到选中的所有行的数据
		public List<ScaleDisplay> getTagValueAt(int[] tag) {
			ArrayList<ScaleDisplay> result = new ArrayList<ScaleDisplay>();
			for (int i = 0; i < tag.length; i++) {
				result.add(scaleDisplays.get(i));
			}
			return result;
		}

		// 在表格中填充数据
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			ScaleDisplay scaleDisplay = scaleDisplays.get(rowIndex);
			if (0 == columnIndex) {
				return scaleDisplay.getIndex();
			}
			if (1 == columnIndex) {
				return scaleDisplay.getScale();
			}
			return "";
		}

	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public List<ScaleDisplay> getScaleDisplays() {
		return scaleDisplays;
	}

	public void setScaleDisplays(List<ScaleDisplay> scaleDisplays) {
		this.scaleDisplays = scaleDisplays;
	}

}
