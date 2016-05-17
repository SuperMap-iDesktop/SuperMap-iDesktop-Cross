package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.data.Enum;
import com.supermap.data.GeoCoordSys;
import com.supermap.data.GeoCoordSysType;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Unit;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFieldSearch;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.PathUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.desktop.utilties.XmlUtilties;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

// @formatter:off
/**
 * 先不做自定义投影 投影描述的文件是从 iDesktop .NET 迁移过来的，而 Java版的实现与投影描述文件的结构略有不同。
 * 在配置文件中，分组信息是以 GroupCaption 子节点的形式写在了每一个定义里，它们是同级平行关系，
 * 而在本类的实现中， 分组与子项是上下层级关系。
 * 
 * @author highsad
 *
 */
// @formatter:on
public class JDialogPrjCoordSysSettings extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_PROJECTION_CONFIG_PATH = "/com/supermap/desktop/controlsresources/Projection.xml";
	private static final String DEFAULT_GROUPCAPTION = "Default";

	private JToolBar toolBar;
	private JLabel labelPath;
	private JTextField textFieldPath;
	private TextFieldSearch textFieldSearch;

	private JSplitPane splitPaneMain; // 整个投影选择区域的主面板
	private JTree treePrjCoordSys; // 读取加载投影信息的树，主面板左边区域
	private JSplitPane splitPaneDetails; // 读取加载选中树节点的具体内容，以及选定投影详细信息的面板，主面板右边区域
	private JTable tablePrjCoordSys; // 读取加载选中树节点的具体内容的 Table，主面板右边区域上半区域
	private JTextArea textAreaDetail; // 显示选定投影详细信息，主面板右边区域下半区域

	private SmButton buttonApply;
	private SmButton buttonClose;

	// 平面坐标系定义集合
	private transient CoordSysDefine noneEarth = new CoordSysDefine(CoordSysDefine.NONE_ERRTH, null, ControlsProperties.getString("String_NoneEarth")); // 平面坐标系定义数据
	// 投影坐标系统定义集合
	private transient CoordSysDefine projectionSystem = new CoordSysDefine(CoordSysDefine.PROJECTION_SYSTEM, null,
			ControlsProperties.getString("String_PrjCoorSys")); // 投影坐标系统定义数据
	// 地理坐标系定义集合
	private transient CoordSysDefine geographyCoordinate = new CoordSysDefine(CoordSysDefine.GEOGRAPHY_COORDINATE, null,
			ControlsProperties.getString("String_GeoCoordSys")); // 地理坐标系定义数据
	// 当前选中的坐标系
	private transient CoordSysDefine currentPrjDefine = null;
	private transient PrjCoordSys prjCoordSys = null;

	private String projectionConfigPath = "";
	private transient Document projectionDoc = null;

	private PrjCoordSysTableModel prjModel = new PrjCoordSysTableModel();

	private transient TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (e.getSource() == treePrjCoordSys) {
				treeSelectionChange();
			}
		}
	};
	private transient ListSelectionListener listSelectionListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				tableSelectionChange();
			}
		}
	};
	private transient MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			if ((e.getSource() == tablePrjCoordSys.getParent() || e.getSource() == tablePrjCoordSys) && e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
				tableMouseRightClicked(e);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() != 2) {
				return;
			}

			if (e.getSource() == tablePrjCoordSys) {
				tableMouseDoubleClick(e);
			} else if (e.getSource() == treePrjCoordSys) {
				treeMouseDoubleClick(e);
			}
		}
	};

	private transient ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonApply) {
				buttonApplyClicked();
			} else if (e.getSource() == buttonClose) {
				buttonCloseClicked();
			} else if (e.getSource() == textFieldSearch) {
				textFieldSearchAction();
			}
		}
	};

	private transient DocumentListener documentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldSearchAction();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldSearchAction();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldSearchAction();
		}
	};
	private JPopupMenu tablePopupmenu;

	/**
	 * Create the dialog.
	 */
	public JDialogPrjCoordSysSettings() {
		try {
			initializeComponents();
			initializeResources();
			// 加载投影配置文件
			loadProjectionConfig();
			// 构建平面坐标系定义数据
			buildNoneEarthDefines();
			// 构建投影坐标系统定义数据
			buildProjectionSystemDefines();
			// 构建地理坐标系定义数据
			buildGeographyCoordinateDefines();
			// 构造显示投影系统结构的树
			initializeTreePrjCoordSys();
			registerEvents();
			setControlsEnabled();
			setSize(new Dimension(1100, 600));
			setLocationRelativeTo(null);
			selectRootNode();
			this.componentList.add(buttonApply);
			this.componentList.add(buttonClose);
			this.setFocusTraversalPolicy(policy);
			this.getRootPane().setDefaultButton(buttonClose);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void tableMouseRightClicked(MouseEvent e) {
		if (tablePrjCoordSys.getRowCount() <= 0) {
			return;
		}
		int i = tablePrjCoordSys.rowAtPoint(e.getPoint());
		int[] selectedRows = tablePrjCoordSys.getSelectedRows();

		if (i != -1) {
			// 判断是否已有选中行
			boolean isExist = false;
			for (int selectedRow : selectedRows) {
				if (selectedRow == i) {
					isExist = true;
				}
			}
			if (!isExist) {
				tablePrjCoordSys.setRowSelectionInterval(i, i);
			}
		} else {
			tablePrjCoordSys.setRowSelectionInterval(tablePrjCoordSys.getRowCount() - 1, tablePrjCoordSys.getRowCount() - 1);
		}

		// 弹菜单
		getTablePopupmenu().show(tablePrjCoordSys, e.getX(), e.getY());
		prjModel.getRowData(tablePrjCoordSys.getSelectedRow());
	}

	// 获取选定的投影
	public PrjCoordSys getPrjCoordSys() {
		return this.prjCoordSys;
	}

	/**
	 * 设置选定的投影（目前不支持自定义投影，所以如果是自定义投影，那么就不选择，是默认投影，则选中默认投影）
	 *
	 * @param prj
	 */
	public void setPrjCoordSys(PrjCoordSys prj) {
		this.prjCoordSys = prj;

		if (this.prjCoordSys != null) {
			if (this.prjCoordSys.getType() == PrjCoordSysType.PCS_NON_EARTH) { // 平面坐标系
				this.currentPrjDefine = this.noneEarth.getChildByCoordSysCode(this.prjCoordSys.getCoordUnit().value());
			} else if (this.prjCoordSys.getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) { // 地理坐标系
				GeoCoordSys geoCoordSys = this.prjCoordSys.getGeoCoordSys();
				if (geoCoordSys.getType() != GeoCoordSysType.GCS_USER_DEFINE) {
					this.currentPrjDefine = this.geographyCoordinate.getChildByCoordSysCode(geoCoordSys.getType().value());
				}
			} else { // 投影坐标系统
				if (this.prjCoordSys.getType() != PrjCoordSysType.PCS_USER_DEFINED) {
					this.currentPrjDefine = this.projectionSystem.getChildByCoordSysCode(this.prjCoordSys.getType().value());
				}
			}
		}

		// 获取根节点下与 currentPrjDefine 绑定的子节点
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getNodeByDefine((DefaultMutableTreeNode) this.treePrjCoordSys.getModel().getRoot(),
				this.currentPrjDefine);
		if (node != null) {
			TreePath path = new TreePath(node.getPath());
			// 选中
			this.treePrjCoordSys.setSelectionPath(path);
			// 展开
			this.treePrjCoordSys.expandPath(path);
			// 滚动到可见
			this.treePrjCoordSys.scrollPathToVisible(path);
		}
		this.buttonApply.setEnabled(false);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		this.splitPaneMain.setDividerLocation(0.3);
		this.splitPaneDetails.setDividerLocation(0.7);
	}

	private void initializeComponents() {
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		this.getContentPane().setLayout(groupLayout);
		JToolBar toolBarTemp = createToolBar();
		JPanel centerPanel = createCenterPanel();

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(toolBarTemp, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addComponent(centerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(toolBarTemp, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(centerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE));
		// @formatter:on
	}

	private void initializeResources() {
		this.setTitle(ControlsProperties.getString("String_SetProjection_Caption"));
		this.labelPath.setText(ControlsProperties.getString("String_CoordSys_PathName"));
		this.buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
		this.buttonClose.setText(CommonProperties.getString(CommonProperties.Close));
	}

	private void registerEvents() {
		this.treePrjCoordSys.addTreeSelectionListener(this.treeSelectionListener);
		this.tablePrjCoordSys.getSelectionModel().addListSelectionListener(listSelectionListener);
		this.treePrjCoordSys.addMouseListener(this.mouseAdapter);
		this.tablePrjCoordSys.addMouseListener(this.mouseAdapter);
		this.tablePrjCoordSys.getParent().addMouseListener(this.mouseAdapter);
		this.buttonApply.addActionListener(this.actionListener);
		this.buttonClose.addActionListener(this.actionListener);
		this.textFieldSearch.addActionListener(this.actionListener);
		this.textFieldSearch.getDocument().addDocumentListener(this.documentListener);
	}

	private void unregisterEvents() {
		this.treePrjCoordSys.removeTreeSelectionListener(this.treeSelectionListener);
		this.tablePrjCoordSys.getSelectionModel().removeListSelectionListener(listSelectionListener);
		this.treePrjCoordSys.removeMouseListener(this.mouseAdapter);
		this.tablePrjCoordSys.removeMouseListener(this.mouseAdapter);
		this.tablePrjCoordSys.getParent().removeMouseListener(this.mouseAdapter);
		this.buttonApply.removeActionListener(this.actionListener);
		this.buttonClose.removeActionListener(this.actionListener);
		this.textFieldSearch.removeActionListener(this.actionListener);
		this.textFieldSearch.getDocument().removeDocumentListener(this.documentListener);
	}

	/**
	 * 创建工具条
	 *
	 * @return
	 */
	private JToolBar createToolBar() {
		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);
		this.labelPath = new JLabel("ProjectionPath:");
		this.textFieldPath = new JTextField();
		this.textFieldPath.setEditable(false);
		this.textFieldSearch = new TextFieldSearch();
		textFieldSearch.setPreferredSize(new Dimension(150, 30));
		this.toolBar.add(this.labelPath);
		this.toolBar.addSeparator(new Dimension(5, 5));
		this.toolBar.add(this.textFieldPath);
		this.toolBar.addSeparator(new Dimension(5, 5));
		this.toolBar.add(this.textFieldSearch);
		return this.toolBar;
	}

	/**
	 * 创建工具条之下的内容面板
	 *
	 * @return
	 */
	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel();
		createSplitPaneMain();
		this.buttonApply = new SmButton("Apply");
		this.buttonClose = new SmButton("Close");

		GroupLayout groupLayout = new GroupLayout(centerPanel);
		groupLayout.setAutoCreateGaps(true);
		centerPanel.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(this.splitPaneMain, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonApply)
						.addComponent(this.buttonClose)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.splitPaneMain, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonApply)
						.addComponent(this.buttonClose)));
		// @formatter:on
		this.getRootPane().setDefaultButton(this.buttonApply);
		return centerPanel;
	}

	/**
	 * 创建容纳 Tree 和 Table 的分割面板
	 *
	 * @return
	 */
	private JSplitPane createSplitPaneMain() {
		this.splitPaneMain = new JSplitPane();
		this.splitPaneMain.setContinuousLayout(true);

		JScrollPane scrollPane = new JScrollPane();
		this.treePrjCoordSys = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode(ControlsProperties.getString("String_CoordSystem"))));
		this.treePrjCoordSys.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrollPane.setViewportView(this.treePrjCoordSys);
		this.splitPaneMain.setLeftComponent(scrollPane);
		this.splitPaneMain.setRightComponent(createSplitPaneDetails());
		return this.splitPaneMain;
	}

	/**
	 * 创建容纳 Table 和投影详细信息展示的分割面板
	 *
	 * @return
	 */
	private JSplitPane createSplitPaneDetails() {
		this.splitPaneDetails = new JSplitPane();
		this.splitPaneDetails.setContinuousLayout(true);
		this.splitPaneDetails.setOrientation(JSplitPane.VERTICAL_SPLIT);

		JScrollPane scrollPane = new JScrollPane();
		this.tablePrjCoordSys = new JTable();
		this.tablePrjCoordSys.setRowHeight(this.tablePrjCoordSys.getRowHeight() + 4);
		this.tablePrjCoordSys.setModel(this.prjModel);
		scrollPane.setViewportView(this.tablePrjCoordSys);

		JScrollPane scrollPaneDetail = new JScrollPane();
		this.textAreaDetail = new JTextArea();
		this.textAreaDetail.setEditable(false);
		scrollPaneDetail.setViewportView(this.textAreaDetail);

		this.splitPaneDetails.setTopComponent(scrollPane);
		this.splitPaneDetails.setBottomComponent(scrollPaneDetail);
		return this.splitPaneDetails;
	}

	/**
	 * 加载投影配置文件
	 *
	 * @throws Exception
	 */
	private void loadProjectionConfig() {
		try {
			String startupXml = PathUtilties.getFullPathName(XMLProjectionTag.FILE_STARTUP_XML, false);
			Document startupDoc = XmlUtilties.getDocument(startupXml);
			if (startupDoc != null) {
				NodeList nodeList = startupDoc.getElementsByTagName(XMLProjectionTag.PROJECTION);
				if (nodeList.getLength() > 0) {
					this.projectionConfigPath = nodeList.item(0).getAttributes().getNamedItem(XMLProjectionTag.DEFAULT).getNodeValue();
				}
			}

			if (StringUtilties.isNullOrEmpty(this.projectionConfigPath)) {
				File file = new File("");
				this.projectionConfigPath = PathUtilties.getParentPath(file.getAbsolutePath()) + XMLProjectionTag.PROJECTION_XML;
			}
			loadProjectionConfig(this.projectionConfigPath);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 根据指定路径加载投影配置文件，返回 xml 文档对象
	 *
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private Document loadProjectionConfig(String filePath) {
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				InputStream stream = getClass().getResourceAsStream(DEFAULT_PROJECTION_CONFIG_PATH);
				if (stream != null) {
					this.projectionDoc = XmlUtilties.getDocument(stream);
				} else {
					throw new Exception("Default ProjectionConfig does not exists.");
				}
			} else {
				this.projectionDoc = XmlUtilties.getDocument(filePath);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return this.projectionDoc;
	}

	/**
	 * 构建平面坐标系定义集合
	 */
	private void buildNoneEarthDefines() {
		Enum[] units = Enum.getEnums(Unit.class);

		for (int i = 0; i < units.length; i++) {
			Unit unit = (Unit) units[i];
			CoordSysDefine coordSysDefine = new CoordSysDefine(CoordSysDefine.NONE_ERRTH, this.noneEarth, unit.toString());
			coordSysDefine.setCoordSysCode(unit.value());
		}
	}

	/**
	 * 构建投影坐标系统定义集合
	 */
	private void buildProjectionSystemDefines() {
		NodeList nodes = this.projectionDoc.getElementsByTagName(XMLProjectionTag.PRJCOORDSYS_DEFINE);

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
				createPrjCoordSysDefine(node);
			}
		}
	}

	private CoordSysDefine createPrjCoordSysDefine(Node prjCoordSysNode) {
		CoordSysDefine result = new CoordSysDefine(CoordSysDefine.PROJECTION_SYSTEM);

		NodeList nodes = prjCoordSysNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase(XMLProjectionTag.PRJGROUP_CAPTION)) {
					String groupCaption = node.getTextContent();
					CoordSysDefine parent = this.projectionSystem.getChildByCaption(groupCaption);
					if (parent == null) {
						parent = new CoordSysDefine(CoordSysDefine.PROJECTION_SYSTEM, this.projectionSystem, groupCaption);
					}
					parent.add(result);
				} else if (node.getNodeName().equalsIgnoreCase(XMLProjectionTag.PRJCOORDSYS_CAPTION)) {
					result.setCaption(node.getTextContent());
				} else if (node.getNodeName().equalsIgnoreCase(XMLProjectionTag.PRJCOORDSYS_TYPE)) {
					String prjType = node.getTextContent();
					if (!StringUtilties.isNullOrEmpty(prjType)) {
						result.setCoordSysCode(Integer.valueOf(prjType));
					} else {
						result.setCoordSysCode(CoordSysDefine.USER_DEFINED);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 构建地理坐标系定义集合
	 */
	private void buildGeographyCoordinateDefines() {
		NodeList nodes = this.projectionDoc.getElementsByTagName(XMLProjectionTag.GEOCOORDSYS_DEFINE);

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
				createGeoCoordSysDefine(node);
			}
		}
	}

	private CoordSysDefine createGeoCoordSysDefine(Node geoCoordSysNode) {
		CoordSysDefine result = new CoordSysDefine(CoordSysDefine.GEOGRAPHY_COORDINATE);

		NodeList nodes = geoCoordSysNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase(XMLProjectionTag.GEOGROUP_CATION)) {
					String groupCaption = node.getTextContent();
					CoordSysDefine parent = this.geographyCoordinate.getChildByCaption(groupCaption);
					if (parent == null) {
						parent = new CoordSysDefine(CoordSysDefine.GEOGRAPHY_COORDINATE, this.geographyCoordinate, groupCaption);
					}
					parent.add(result);
				} else if (node.getNodeName().equalsIgnoreCase(XMLProjectionTag.GEOCOORDSYS_CAPTION)) {
					result.setCaption(node.getTextContent());
				} else if (node.getNodeName().equalsIgnoreCase(XMLProjectionTag.GEOCOORDSYS_TYPE)) {
					String geoType = node.getTextContent();
					if (!StringUtilties.isNullOrEmpty(geoType)) {
						result.setCoordSysCode(Integer.valueOf(geoType));
					} else {
						result.setCoordSysCode(CoordSysDefine.USER_DEFINED);
					}
				}
			}
		}

		// 到这一步没有 Parent，说明配置文件中没有 GorupCaption 节点，那么就取 Default 节点
		if (result.getParent() == null) {
			CoordSysDefine parent = this.geographyCoordinate.getChildByCaption(DEFAULT_GROUPCAPTION);
			if (parent == null) {
				parent = new CoordSysDefine(CoordSysDefine.GEOGRAPHY_COORDINATE, this.geographyCoordinate, DEFAULT_GROUPCAPTION);
			}
			parent.add(result);
		}
		return result;
	}

	private void initializeTreePrjCoordSys() {
		DefaultTreeModel treeModel = (DefaultTreeModel) this.treePrjCoordSys.getModel();
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
		rootNode.add(createNode(this.noneEarth));
		rootNode.add(createNode(this.projectionSystem));
		rootNode.add(createNode(this.geographyCoordinate));
		this.treePrjCoordSys.expandPath(new TreePath(rootNode.getPath()));
	}

	private DefaultMutableTreeNode createNode(CoordSysDefine define) {
		DefaultMutableTreeNode result = new DefaultMutableTreeNode(define);
		for (int i = 0; i < define.size(); i++) {
			result.add(createNode(define.get(i)));
		}
		return result;
	}

	private void treeSelectionChange() {
		try {
			// 点击 Expand / Collapse 也会触发 SelectionChange，然而此时 SelectionPath 为空
			if (this.treePrjCoordSys.getSelectionPath() != null) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.treePrjCoordSys.getSelectionPath().getLastPathComponent();

				if (selectedNode.getUserObject() instanceof CoordSysDefine) {
					this.prjModel.setDefine((CoordSysDefine) selectedNode.getUserObject());
					this.currentPrjDefine = (CoordSysDefine) selectedNode.getUserObject();
				} else {
					this.prjModel.setDefine(null);
					this.currentPrjDefine = null;
				}

				// Table 上有可能是搜索结果的 Model，这时候就要重新设置一下 Model
				if (this.tablePrjCoordSys.getModel() != this.prjModel) {
					this.tablePrjCoordSys.setModel(this.prjModel);
				}
				refreshStates();
				setControlsEnabled();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void tableSelectionChange() {
		int row = this.tablePrjCoordSys.getSelectedRow();
		if (row >= 0) {
			AbstractPrjTableModel model = (AbstractPrjTableModel) this.tablePrjCoordSys.getModel();
			this.currentPrjDefine = model.getRowData(row);
		} else {
			this.currentPrjDefine = null;
		}
		refreshStates();
		setControlsEnabled();
	}

	private void tableMouseDoubleClick(MouseEvent e) {
		// 获取鼠标双击位置处的选中行
		int row = this.tablePrjCoordSys.rowAtPoint(e.getPoint());
		if (row < 0 || row != this.tablePrjCoordSys.getSelectedRow()) {
			return;
		}

		AbstractPrjTableModel model = (AbstractPrjTableModel) this.tablePrjCoordSys.getModel();
		CoordSysDefine clickedPrjDefine = model.getRowData(row);
		if (clickedPrjDefine.size() > 0) { // 如果双击的项是集合，那么进入下一层，并设置树的节点选中，触发 TreeSlection
			// 获取树上当前选中的节点（即将设置的节点的父节点）
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.treePrjCoordSys.getSelectionPath().getLastPathComponent();
			// 即将设置选中的节点
			DefaultMutableTreeNode toSelectedNode = (DefaultMutableTreeNode) getNodeByDefine(selectedNode, clickedPrjDefine);
			if (toSelectedNode != null) {
				TreePath nodePath = new TreePath(toSelectedNode.getPath());
				this.treePrjCoordSys.setSelectionPath(nodePath);
				this.treePrjCoordSys.expandPath(nodePath);
			}
		} else { // 如果双击的项没有子项，那么就是具体的投影定义，提示选择应用
			confirmSelected();
		}
	}

	/**
	 * // 如果双击的是叶子节点，那么提示应用，如果双击的是其他节点，没有任何效果
	 *
	 * @param e
	 */
	private void treeMouseDoubleClick(MouseEvent e) {
		// 获取双击位置处的 TreePath
		TreePath clickedPath = this.treePrjCoordSys.getPathForLocation(e.getX(), e.getY());
		// 判断双击的是否是节点
		if (clickedPath != null) {
			DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) clickedPath.getLastPathComponent();
			// 双击的是节点，并且和当前选中的 Define 是同一个节点，就提示是否应用
			if (clickedNode != null && clickedNode.getUserObject() == this.currentPrjDefine) {
				confirmSelected();
			}
		}
	}

	private void textFieldSearchAction() {
		search(this.textFieldSearch.getText());
	}

	private void buttonApplyClicked() {
		applyPrjCoordSys();
	}

	private void buttonCloseClicked() {
		this.dialogResult = DialogResult.CANCEL;
		setVisible(false);
	}

	/**
	 * 获取指定父节点下，与指定 UserData 匹配的子节点
	 *
	 * @param node
	 * @param define
	 * @return
	 */
	private TreeNode getNodeByDefine(DefaultMutableTreeNode node, CoordSysDefine define) {
		TreeNode result = null;

		try {
			if (node != null && define != null) {
				if (node.getUserObject() == define) {
					result = node;
				} else {
					if (node.getChildCount() == 0) {
						result = null;
					} else {
						for (int i = 0; i < node.getChildCount(); i++) {
							DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
							result = getNodeByDefine(childNode, define);
							if (result != null) {
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/**
	 * 确认并选择指定的投影。只有当前投影是叶子节点也就是具体的投影时，才提示是否应用。
	 *
	 * @param selectedDefine
	 */
	private void confirmSelected() {
		if (this.currentPrjDefine != null && this.currentPrjDefine.size() == 0) {
			if (showConfirmMessage() == JOptionPane.YES_OPTION) {
				applyPrjCoordSys();
			} else {
				this.prjCoordSys = null;
			}
		}
	}

	/**
	 * 应用选中的投影
	 */
	private void applyPrjCoordSys() {
		if (this.currentPrjDefine.coordSysType == CoordSysDefine.NONE_ERRTH) {
			this.prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_NON_EARTH);
			this.prjCoordSys.setCoordUnit((Unit) Enum.parse(Unit.class, this.currentPrjDefine.getCoordSysCode()));
		} else if (this.currentPrjDefine.coordSysType == CoordSysDefine.PROJECTION_SYSTEM) {
			this.prjCoordSys = PrjCoordSysSettingsUtilties.getPrjCoordSys(this.currentPrjDefine);
		} else if (this.currentPrjDefine.coordSysType == CoordSysDefine.GEOGRAPHY_COORDINATE) {
			GeoCoordSys geoCoordSys = PrjCoordSysSettingsUtilties.getGeoCoordSys(this.currentPrjDefine);
			this.prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
			this.prjCoordSys.setGeoCoordSys(geoCoordSys);
		} else {
			this.prjCoordSys = null;
		}

		this.dialogResult = DialogResult.OK;
		setVisible(false);
	}

	/**
	 * @return JOptionPane.YES_OPTION / JOptionPane.NO_OPTION / JOptionPane.CLOSED_OPTION
	 */
	private int showConfirmMessage() {
		return UICommonToolkit.showConfirmDialogYesNo(ControlsProperties.getString("String_message_Apply"));
	}

	/**
	 * 当前选中的投影信息发生改变时，刷新一些控件的内容展示
	 */
	private void refreshStates() {
		refreshTextAreaDetails();
		refreshPath();
		toolBar.updateUI();
	}

	/**
	 * 当前选中的投影信息发生改变时，刷新投影信息详情
	 */
	private void refreshTextAreaDetails() {
		this.textAreaDetail.setText(PrjCoordSysSettingsUtilties.getDescription(this.currentPrjDefine));
	}

	private void refreshPath() {
		TreePath path = this.treePrjCoordSys.getSelectionPath();
		if (path != null) {
			this.textFieldPath.setText(path.toString());
		}
	}

	private void setControlsEnabled() {
		this.buttonApply.setEnabled(this.currentPrjDefine != null && this.currentPrjDefine.size() == 0);
	}

	private void search(String pattern) {
		SearchResultModel searchModel = new SearchResultModel();
		// 如果当前选中的投影不为空，就搜索当前选中的投影，否则就搜索所有
		if (this.currentPrjDefine != null) {
			searchDefine(pattern, this.currentPrjDefine, searchModel);
		} else {
			searchDefine(pattern, this.noneEarth, searchModel);
			searchDefine(pattern, this.geographyCoordinate, searchModel);
			searchDefine(pattern, this.projectionSystem, searchModel);
		}

		this.tablePrjCoordSys.setModel(searchModel);
	}

	/**
	 * 根据关键字搜索指定的 Define，并将所有结果添加到 SearchResultModel 中
	 *
	 * @param pattern
	 * @param define
	 * @param searchModel
	 */
	private void searchDefine(String pattern, CoordSysDefine define, SearchResultModel searchModel) {
		CoordSysDefine[] allLeafDefines = null;

		// 如果选中的是最后一级子节点，那么就选择改节点的父节点进行搜索
		if (define.size() == 0) {
			allLeafDefines = define.getParent().getAllLeaves();
		} else {
			allLeafDefines = define.getAllLeaves();
		}

		for (int i = 0; i < allLeafDefines.length; i++) {
			String caption = allLeafDefines[i].getCaption();
			if (caption.toLowerCase().contains(pattern.toLowerCase())) {
				searchModel.add(allLeafDefines[i]);
			}
		}
	}

	public JPopupMenu getTablePopupmenu() {
		if (tablePopupmenu == null) {
			tablePopupmenu = new JPopupMenu();
			JMenuItem menuItem = new JMenuItem(ControlsProperties.getString("String_Button_NewCoordSys"));
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO: 2016/5/16  弹菜单啦！！
				}
			});
			tablePopupmenu.add(menuItem);
		}
		return tablePopupmenu;
	}

	public void setTablePopupmenu(JPopupMenu tablePopupmenu) {
		this.tablePopupmenu = tablePopupmenu;
	}

	/**
	 * 描述一个坐标系
	 *
	 * @author highsad
	 */
	public class CoordSysDefine {
		public static final int USER_DEFINED = -1; // 用户自定义
		public static final int PROJECTION_SYSTEM = -2; // 投影坐标系统
		public static final int NONE_ERRTH = -3; // 平面坐标系
		public static final int GEOGRAPHY_COORDINATE = -4; // 地理坐标系

		private CoordSysDefine parent;
		private String caption = "";
		private int coordSysType = PROJECTION_SYSTEM; // 坐标系类型
		private int coordSysCode = USER_DEFINED; // 默认坐标系代码
		private ArrayList<CoordSysDefine> children = new ArrayList<JDialogPrjCoordSysSettings.CoordSysDefine>();

		public CoordSysDefine(int coordSysType) {
			this.coordSysType = coordSysType;
		}

		public CoordSysDefine(int coordSysType, CoordSysDefine parent) {
			this(coordSysType);
			if (parent != null) {
				parent.add(this);
			}
		}

		public CoordSysDefine(int coordSysType, CoordSysDefine parent, String caption) {
			this(coordSysType, parent);
			this.caption = caption;
		}

		public CoordSysDefine getParent() {
			return this.parent;
		}

		private void setParent(CoordSysDefine parent) {
			this.parent = parent;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public int getCoordSysType() {
			return coordSysType;
		}

		public int getCoordSysCode() {
			return coordSysCode;
		}

		public void setCoordSysCode(int coordSysCode) {
			this.coordSysCode = coordSysCode;
		}

		public CoordSysDefine get(int index) {
			return this.children.get(index);
		}

		public boolean add(CoordSysDefine child) {
			boolean result = false;

			try {
				if (!this.children.contains(child)) {
					result = this.children.add(child);
					child.setParent(this);
				}
			} catch (Exception e) {
				result = false;
			}
			return result;
		}

		public boolean remove(CoordSysDefine child) {
			boolean result = false;

			try {
				if (this.children.contains(child)) {
					result = this.children.remove(child);
					child.setParent(null);
				}
			} catch (Exception e) {
				result = false;
			}
			return result;
		}

		public int size() {
			return this.children.size();
		}

		public CoordSysDefine getChildByCaption(String caption) {
			CoordSysDefine result = null;

			try {
				for (CoordSysDefine coordSysDefine : children) {
					if (coordSysDefine.getCaption().equals(caption)) {
						result = coordSysDefine;
					} else {
						result = coordSysDefine.getChildByCaption(caption);
					}

					if (result != null) {
						break;
					}
				}
			} catch (Exception e) {
				result = null;
			}
			return result;
		}

		public CoordSysDefine getChildByCoordSysCode(int coordSysCode) {
			CoordSysDefine result = null;

			try {
				for (CoordSysDefine coordSysDefine : children) {
					if (coordSysDefine.getCoordSysCode() == coordSysCode) {
						result = coordSysDefine;
					} else {
						result = coordSysDefine.getChildByCoordSysCode(coordSysCode);
					}

					if (result != null) {
						break;
					}
				}
			} catch (Exception e) {
				result = null;
			}
			return result;
		}

		public String getCoordSysTypeDescription() {
			String result = "";
			if (this.coordSysType == PROJECTION_SYSTEM) {
				result = ControlsProperties.getString("String_PrjCoorSys");
			} else if (this.coordSysType == NONE_ERRTH) {
				result = ControlsProperties.getString("String_NoneEarth");
			} else if (this.coordSysType == GEOGRAPHY_COORDINATE) {
				result = ControlsProperties.getString("String_GeoCoordSys");
			}
			return result;
		}

		/**
		 * 获取该投影定义下所有的叶子节点（也就是所有的投影）
		 *
		 * @return
		 */
		public CoordSysDefine[] getAllLeaves() {
			ArrayList<CoordSysDefine> list = new ArrayList<>();

			try {
				if (this.children.isEmpty()) {
					list.add(this);
				} else {
					for (int i = 0; i < this.children.size(); i++) {
						CoordSysDefine[] leaves = this.children.get(i).getAllLeaves();
						for (int leafIndex = 0; leafIndex < leaves.length; leafIndex++) {
							list.add(leaves[leafIndex]);
						}
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			return list.toArray(new CoordSysDefine[list.size()]);
		}

		@Override
		public String toString() {
			return this.caption;
		}
	}

	/**
	 * 投影信息 TableModel 的抽象基类，搜索结果的 Model 和 正常展示的 Model 各自有不同的实现。
	 *
	 * @author highsad
	 */
	private abstract class AbstractPrjTableModel extends AbstractTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		protected static final int CAPTION = 0;
		protected static final int TYPE = 1;
		protected static final int GROUP = 2;

		private transient CoordSysDefine define;

		public AbstractPrjTableModel() {

		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public String getColumnName(int column) {
			if (column == CAPTION) {
				return CommonProperties.getString(CommonProperties.Caption);
			} else if (column == TYPE) {
				return CommonProperties.getString(CommonProperties.Type);
			} else if (column == GROUP) {
				return ControlsProperties.getString("String_BelongToGroup");
			} else {
				return null;
			}
		}

		public abstract CoordSysDefine getRowData(int row);

		public CoordSysDefine getDefine() {
			return define;
		}

		public void setDefine(CoordSysDefine define) {
			this.define = define;
		}
	}

	/**
	 * 选中节点之后，在 Table 上展示对应数据的 Model
	 *
	 * @author highsad
	 */
	private class PrjCoordSysTableModel extends AbstractPrjTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private transient CoordSysDefine define;

		@Override
		public CoordSysDefine getDefine() {
			return this.define;
		}

		public PrjCoordSysTableModel() {
			// do nothing
		}

		@Override
		public void setDefine(CoordSysDefine define) {
			this.define = define;
			fireTableDataChanged();
		}

		@Override
		public int getRowCount() {
			if (this.define == null) {
				return 0;
			}

			// 如果没有子项，则表明是坐标系定义，在列表中展示它本身
			if (this.define.size() == 0) {
				return 1;
			} else {
				return this.define.size();
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (this.define == null) {
				return null;
			}

			CoordSysDefine item = this.define;
			if (this.define.size() > 0) {
				item = this.define.get(rowIndex);
			}

			if (columnIndex == CAPTION) {
				return item.getCaption();
			} else if (columnIndex == TYPE) {
				return item.getCoordSysTypeDescription();
			} else if (columnIndex == GROUP) {
				return item.getParent().getCaption();
			} else {
				return null;
			}
		}

		@Override
		public CoordSysDefine getRowData(int row) {
			CoordSysDefine result = null;

			try {
				if (this.define == null) {
					return null;
				}

				if (this.define.size() == 0 && row == 0) {
					result = this.define;
				} else if (this.define.size() > 0 && 0 <= row && row < this.define.size()) {
					result = this.define.get(row);
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			return result;
		}
	}

	/**
	 * 在 Table 上展示搜索结果的 Model
	 *
	 * @author highsad
	 */
	private class SearchResultModel extends AbstractPrjTableModel {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private ArrayList<CoordSysDefine> defines = new ArrayList<>();

		public void add(CoordSysDefine define) {
			if (!this.defines.contains(define)) {
				this.defines.add(define);
			}
		}

		@Override
		public int getRowCount() {
			return this.defines.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (this.defines == null) {
				return null;
			}

			CoordSysDefine item = null;
			if (!this.defines.isEmpty()) {
				item = this.defines.get(rowIndex);
				if (columnIndex == CAPTION) {
					return item.getCaption();
				} else if (columnIndex == TYPE) {
					return item.getCoordSysTypeDescription();
				} else if (columnIndex == GROUP) {
					return item.getParent().getCaption();
				} else {
					return null;
				}
			}
			return null;
		}

		@Override
		public CoordSysDefine getRowData(int row) {
			CoordSysDefine result = null;

			try {
				if (!this.defines.isEmpty() && 0 <= row && row < this.defines.size()) {
					result = this.defines.get(row);
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			return result;
		}
	}

	public void selectRootNode() {
		if (this.treePrjCoordSys.getRowCount() > 0) {
			treePrjCoordSys.setSelectionRow(0);
		}
	}

}
