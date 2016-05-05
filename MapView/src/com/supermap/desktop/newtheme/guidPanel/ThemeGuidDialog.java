package com.supermap.desktop.newtheme.guidPanel;

import com.supermap.data.DatasetType;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CommonListCellRenderer;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemeGuidDialog extends SmDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel panelContent = new JPanel();
	private SmButton buttonOk = new SmButton("OK");
	private SmButton buttonCancel = new SmButton("Cancel");
	private JList<Object> listContent = new JList<Object>();
	private ThemeLabelDecorator labelUniform;
	private ThemeLabelDecorator labelRange;
	private JPanel panel;
	private JPanel contentPanel = new JPanel();

	private boolean isUniform = true;
	private boolean isRange = false;
	private boolean isCadType = false;
	private boolean isGridType = false;

	private DatasetType activeDatasetType = ThemeUtil.getActiveLayer().getDataset().getType();

	private transient LocalMouseListener localMouseListener = new LocalMouseListener();
	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalListSelectionListener listSelectionListener = new LocalListSelectionListener();

	public ThemeGuidDialog(JFrame owner, boolean flag, boolean isCadType, boolean isGridType) {
		super(owner, true);
		this.isCadType = isCadType;
		this.isGridType = isGridType;
		initComponents();
		initResources();
		registListener();
		this.componentList.add(this.buttonOk);
		this.componentList.add(this.buttonCancel);
		this.setFocusTraversalPolicy(policy);
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		setSize(560, 400);
		setLocationRelativeTo(null);
		this.panelContent.setBorder(new EmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPane = new JScrollPane();
		if (this.isCadType) {
			this.panel = newLabelThemePanel();
		} else if (isGridType) {
			this.panel = new GridUniqueThemePanel(this);
		} else {
			this.panel = new UniqueThemePanel(this);
		}
		this.panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.panel.setBackground(Color.WHITE);

		addListItem();
		listContent.setCellRenderer(new ListCellRenderer<Object>() {

			@Override
			public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				((DataCell) value).setSelected(isSelected);
				return (DataCell) value;
			}
		});
		scrollPane.setViewportView(listContent);
		this.buttonOk.setActionCommand("OK");
		getRootPane().setDefaultButton(buttonOk);
		this.buttonCancel.setActionCommand("Cancel");

		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 10, 10));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 10, 10));
		//@formatter:off
		getContentPane().setLayout(new GridBagLayout());
		this.contentPanel.setLayout(new GridBagLayout());
		this.contentPanel.add(scrollPane, new GridBagConstraintsHelper(0, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(3).setWeight(0, 1).setIpad(30, 0));
		this.contentPanel.add(this.panel, new GridBagConstraintsHelper(3, 0, 7, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(3).setWeight(1, 1));
		
		getContentPane().add(contentPanel,new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(3).setWeight(1, 3));
		getContentPane().add(panelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
		//@formatter:on
		getRootPane().setDefaultButton(this.buttonOk);
	}

	/**
	 * 添加可用的专题图项
	 */
	private void addListItem() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		DataCell uniqueDataCell = new DataCell(MapViewProperties.getString("String_Theme_Unique"), InternalImageIconFactory.THEMEGUIDE_UNIQUE);
		DataCell rangeDataCell = new DataCell(MapViewProperties.getString("String_Theme_Range"), InternalImageIconFactory.THEMEGUIDE_RANGE);
		DataCell labelDataCell = new DataCell(MapViewProperties.getString("String_ThemeLabel"), InternalImageIconFactory.THEMEGUIDE_UNIFORM);
		DataCell gridUniqueCell = new DataCell(MapViewProperties.getString("String_ThemeGrid_Unique"), InternalImageIconFactory.THEMEGUIDE_GRIDUNIQUE);
		DataCell gridRangeCell = new DataCell(MapViewProperties.getString("String_ThemeGrid_Range"), InternalImageIconFactory.THEMEGUIDE_GRIDRANGE);
		DataCell graphCell = new DataCell(MapViewProperties.getString("String_Theme_Graph"), InternalImageIconFactory.THEMEGUIDE_GRAPH);
		DataCell graduatedSymbolCell = new DataCell(MapViewProperties.getString("String_Theme_GraduatedSymbol"),
				InternalImageIconFactory.THEMEGUIDE_GRADUATEDSYMBOL);
		DataCell dotDensityCell = new DataCell(MapViewProperties.getString("String_Theme_DotDensity"), InternalImageIconFactory.THEMEGUIDE_DOTDENSITY);
		if (isCadType) {
			listModel.addElement(labelDataCell);
		}else if(isGridType) {
			listModel.addElement(gridUniqueCell);
			listModel.addElement(gridRangeCell);
		}else{
			listModel.addElement(uniqueDataCell);
			listModel.addElement(rangeDataCell);
			listModel.addElement(labelDataCell);
			listModel.addElement(graphCell);
			listModel.addElement(graduatedSymbolCell);
			if (activeDatasetType == DatasetType.REGION || activeDatasetType == DatasetType.REGION3D) {
				listModel.addElement(dotDensityCell);
			}
		}
		this.listContent.setModel(listModel);
		this.listContent.setSelectedIndex(0);
		this.listContent.setCellRenderer(new CommonListCellRenderer());
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_ThemeGuide"));
		this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
	}

	/**
	 * 注册事件
	 */
	private void registListener() {
		this.buttonOk.addActionListener(actionListener);
		this.buttonCancel.addActionListener(actionListener);
		this.listContent.addListSelectionListener(listSelectionListener);
	}

	/**
	 * 注销事件
	 */
	private void unregistListener() {
		this.buttonOk.removeActionListener(actionListener);
		this.listContent.removeListSelectionListener(listSelectionListener);
	}

	/**
	 * 选中左边的专题图类型时修改右边的界面
	 * 
	 * @param selectRow
	 *            选中的专题图类型对应的行数
	 */
	protected void replaceCurrentPanelByListSelection(int selectRow) {
		switch (selectRow) {
		case 0:
			if (null != getRightPanel()) {
				replaceRightPanel(new UniqueThemePanel(this));
			}
			break;
		case 1:
			if (null != getRightPanel()) {
				replaceRightPanel(new RangeThemePanel(this));
			}
			break;
		case 2:
			if (null != getRightPanel()) {
				replaceRightPanel(newLabelThemePanel());
			}
			break;
		case 3:
			if (null != getRightPanel()) {
				replaceRightPanel(new GraphThemePanel(this));
			}
			break;
		case 4:
			if (null != getRightPanel()) {
				replaceRightPanel(new GraduatedSymbolThemePanel(this));
			}
			break;
		case 5:
			if (null != getRightPanel()&&(activeDatasetType == DatasetType.REGION || activeDatasetType == DatasetType.REGION3D)) {
				replaceRightPanel(new DotDensityThemePanel(this));
			}
			break;
		default:
			break;
		}
	}

	private void replaceRightPanel(JPanel panel) {
		this.contentPanel.remove(getRightPanel());
		this.contentPanel.add(panel, new GridBagConstraintsHelper(3, 0, 7, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(3)
				.setWeight(1, 1));
		this.contentPanel.updateUI();
	}

	/**
	 * 新建创建标签专题图向导面板
	 * 
	 * @return
	 */
	private LabelThemePanel newLabelThemePanel() {
		LabelThemePanel labelThemePanel = new LabelThemePanel();
		this.labelRange = labelThemePanel.getLabelRangeTheme();
		this.labelUniform = labelThemePanel.getLabelUniformTheme();
		this.labelRange.addMouseListener(this.localMouseListener);
		this.labelUniform.addMouseListener(this.localMouseListener);
		return labelThemePanel;
	}

	/**
	 * 得到右边当前界面
	 * 
	 * @return
	 */
	private JPanel getRightPanel() {
		return (JPanel) this.contentPanel.getComponent(1);
	}

	class LocalListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int selectRow = ThemeGuidDialog.this.listContent.getSelectedIndex();
			if (!isCadType && !isGridType) {
				replaceCurrentPanelByListSelection(selectRow);
			} else if (isGridType) {
				replaceGridPanelByListSelection(selectRow);
			}
		}
	}

	private void replaceGridPanelByListSelection(int selectRow) {
		switch (selectRow) {
		case 0:
			if (null != getRightPanel()) {
				replaceRightPanel(new GridUniqueThemePanel(this));
			}
			break;
		case 1:
			if (null != getRightPanel()) {
				replaceRightPanel(new GridRangeThemePanel(this));
			}
			break;
		default:
			break;
		}
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if ("OK".equals(e.getActionCommand())) {
				okButtonPressed();
			} else if ("Cancel".equals(e.getActionCommand())) {
				cancelButtonPressed();
			}
		}
	}

	private void cancelButtonPressed() {
		unregistListener();
		ThemeGuidDialog.this.buttonCancel.removeActionListener(actionListener);
		ThemeGuidDialog.this.dispose();
	}

	private void okButtonPressed() {
		int selectRow = listContent.getSelectedIndex();
		boolean success = false;
		if (!isCadType && !isGridType) {
			// 创建专题图dockBar
			createThemeContainer();
		} else if (isGridType && 0 == selectRow) {
			// 栅格单值专题图
			success = ThemeGuideFactory.buildGridUniqueTheme();
			if (success) {
				ThemeGuidDialog.this.dispose();
			}
		} else if (isGridType && 1 == selectRow) {
			// 栅格分段专题图
			success = ThemeGuideFactory.buildGridRangeTheme();
			if (success) {
				ThemeGuidDialog.this.dispose();
			}
		} else {
			// 标签统一风格专题图
			if (isUniform) {
				ThemeGuideFactory.buildLabelUniformTheme(ThemeUtil.getActiveLayer());
				ThemeGuidDialog.this.dispose();
			} else if (isRange) {
				ThemeGuideFactory.buildLabelRangeTheme(ThemeUtil.getActiveLayer());
				if (success) {
					ThemeGuidDialog.this.dispose();
				}
			}
		}
	}

	private void createThemeContainer() {
		int clickCount = listContent.getSelectedIndex();
		boolean buildSuccessed = false;
		switch (clickCount) {
		case 0:
			// 单值专题图
			buildSuccessed = ThemeGuideFactory.buildUniqueTheme(ThemeUtil.getActiveLayer());
			if (buildSuccessed) {
				ThemeGuidDialog.this.dispose();
			}
			break;
		case 1:
			// 分段专题图
			buildSuccessed = ThemeGuideFactory.buildRangeTheme(ThemeUtil.getActiveLayer());
			if (buildSuccessed) {
				ThemeGuidDialog.this.dispose();
			}
			break;
		case 2:
			// 标签统一风格专题图
			if (isUniform) {
				ThemeGuideFactory.buildLabelUniformTheme(ThemeUtil.getActiveLayer());
				ThemeGuidDialog.this.dispose();
			} else if (isRange) {
				buildSuccessed = ThemeGuideFactory.buildLabelRangeTheme(ThemeUtil.getActiveLayer());
				if (buildSuccessed) {
					ThemeGuidDialog.this.dispose();
				}
			}
			break;
		case 3:
			// 统计专题图
			buildSuccessed = ThemeGuideFactory.buildGraphTheme(ThemeUtil.getActiveLayer());
			if (buildSuccessed) {
				ThemeGuidDialog.this.dispose();
			}
			break;
		case 4:
			// 等级符号专题图
			buildSuccessed = ThemeGuideFactory.buildGraduatedSymbolTheme(ThemeUtil.getActiveLayer());
			if (buildSuccessed) {
				ThemeGuidDialog.this.dispose();
			}
			break;
		case 5:
			if (activeDatasetType == DatasetType.REGION || activeDatasetType == DatasetType.REGION3D) {
				// 点密度专题图
				buildSuccessed = ThemeGuideFactory.buildDotDensityTheme(ThemeUtil.getActiveLayer());
				if (buildSuccessed) {
					ThemeGuidDialog.this.dispose();
				}
			} else {
				// 自定义专题图
			}
			break;
		case 6:

		default:
			break;
		}
	}

	class LocalMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getSource() == labelUniform) {
				// 统一风格标签专题图
				if (1 == e.getClickCount()) {
					labelUniform.selected(true);
					labelRange.selected(false);
					isUniform = true;
					isRange = false;
				} else if (2 == e.getClickCount()) {
					// 新建标签统一风格专题图
					ThemeGuideFactory.buildLabelUniformTheme(ThemeUtil.getActiveLayer());
					ThemeGuidDialog.this.dispose();
					unregistListener();
				}
			} else {
				// 分段风格标签专题图
				if (1 == e.getClickCount()) {
					labelUniform.selected(false);
					labelRange.selected(true);
					isUniform = false;
					isRange = true;
				} else if (2 == e.getClickCount()) {
					// 新建分段风格标签专题图
					ThemeGuideFactory.buildLabelRangeTheme(ThemeUtil.getActiveLayer());
					ThemeGuidDialog.this.dispose();
					unregistListener();
				}
			}

		}

	}

}
