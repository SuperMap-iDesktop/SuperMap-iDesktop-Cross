package com.supermap.desktop.newtheme;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CommonListCellRenderer;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
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
	private JButton buttonOk = new JButton("OK");
	private JButton buttonCancel = new JButton("Cancel");
	private JList<Object> listContent = new JList<Object>();
	private transient GroupLayout gl_panelContent = new GroupLayout(panelContent);
	private JLabel labelUniform = new JLabel();
	private JLabel labelRange = new JLabel();
	private JPanel panel;

	private boolean isUniform = true;
	private boolean isRange = false;
	private boolean isCadType = false;
	private boolean isGridType = false;

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
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		setSize(477, 385);
		setLocationRelativeTo(null);
		this.panelContent.setBorder(new EmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPane = new JScrollPane();
		if (isCadType) {
			panel = newLabelThemePanel();
		} else if (isGridType) {
			panel = new GridUniqueThemePanel(this);
		} else {
			panel = new UniqueThemePanel(this);
		}
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBackground(Color.WHITE);
		gl_panelContent.setHorizontalGroup(
				gl_panelContent.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelContent.createSequentialGroup()
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGap(20)
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
								.addContainerGap())
				);
		gl_panelContent.setVerticalGroup(
				gl_panelContent.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelContent.createSequentialGroup()
								.addGap(2)
								.addGroup(gl_panelContent.createParallelGroup(Alignment.LEADING)
										.addComponent(panel, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)))
				);
		addListItem();
		scrollPane.setViewportView(listContent);
		this.panelContent.setLayout(gl_panelContent);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonOk.setActionCommand("OK");
		buttonPane.add(buttonOk);
		getRootPane().setDefaultButton(buttonOk);
		buttonCancel.setActionCommand("Cancel");
		buttonPane.add(buttonCancel);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panelContent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 459, GroupLayout.PREFERRED_SIZE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup()
						.addComponent(panelContent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		getContentPane().setLayout(groupLayout);
	}

	/**
	 * 添加可用的专题图项
	 */
	private void addListItem() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		DataCell uniqueDataCell = new DataCell();
		uniqueDataCell.initDataImage(InternalImageIconFactory.THEMEGUIDE_UNIQUE, MapViewProperties.getString("String_Theme_Unique"));
		DataCell rangeDataCell = new DataCell();
		rangeDataCell.initDataImage(InternalImageIconFactory.THEMEGUIDE_RANGE, MapViewProperties.getString("String_Theme_Range"));
		DataCell labelDataCell = new DataCell();
		labelDataCell.initDataImage(InternalImageIconFactory.THEMEGUIDE_UNIFORM, MapViewProperties.getString("String_ThemeLabel"));
		DataCell gridUniqueCell = new DataCell();
		gridUniqueCell.initDataImage(InternalImageIconFactory.THEMEGUIDE_GRIDUNIQUE, MapViewProperties.getString("String_ThemeGrid_Unique"));
		DataCell gridRangeCell = new DataCell();
		gridRangeCell.initDataImage(InternalImageIconFactory.THEMEGUIDE_GRIDRANGE, MapViewProperties.getString("String_ThemeGrid_Range"));
		if (isCadType) {
			listModel.addElement(labelDataCell);
		} else if (isGridType) {
			listModel.addElement(gridUniqueCell);
			listModel.addElement(gridRangeCell);
		} else {
			listModel.addElement(uniqueDataCell);
			listModel.addElement(rangeDataCell);
			listModel.addElement(labelDataCell);
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
	 * @param selectRow 选中的专题图类型对应的行数
	 */
	protected void replaceCurrentPanelByListSelection(int selectRow) {
		switch (selectRow) {
		case 0:
			if (null != getRightPanel()) {
				this.gl_panelContent.replace(getRightPanel(), new UniqueThemePanel(this));
			}
			break;
		case 1:
			if (null != getRightPanel()) {
				this.gl_panelContent.replace(getRightPanel(), new RangeThemePanel(this));
			}
			break;
		case 2:
			if (null != getRightPanel()) {
				this.gl_panelContent.replace(getRightPanel(), newLabelThemePanel());
			}
			break;

		default:
			break;
		}
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
		return (JPanel) this.panelContent.getComponent(1);
	}

	class LocalListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int selectRow = ThemeGuidDialog.this.listContent.getSelectedIndex();
			if (!isCadType && !isGridType) {
				replaceCurrentPanelByListSelection(selectRow);
			}else if(isGridType) {
				replaceGridPanelByListSelection(selectRow);
			}
		}
	}
	
	private void replaceGridPanelByListSelection(int selectRow) {
		switch (selectRow) {
		case 0:
			if (null != getRightPanel()) {
				this.gl_panelContent.replace(getRightPanel(), new GridUniqueThemePanel(this));
			}
			break;
		case 1:
			if (null != getRightPanel()) {
				this.gl_panelContent.replace(getRightPanel(), new GridRangeThemePanel(this));
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
				int selectRow = listContent.getSelectedIndex();
				if (!isCadType && !isGridType) {
					// 创建专题图dockBar
					createThemeContainer();
				} else if (isGridType && 0 == selectRow) {
					// 栅格单值专题图
					ThemeGuideFactory.buildGridUniqueTheme();
					ThemeGuidDialog.this.dispose();
				} else if (isGridType && 1 == selectRow) {
					// 栅格分段专题图
					ThemeGuideFactory.buildGridRangeTheme();
					ThemeGuidDialog.this.dispose();
				} else {
					// 标签统一风格专题图
					if (isUniform) {
						ThemeGuideFactory.buildLabelUnformTheme();
						ThemeGuidDialog.this.dispose();
					} else if (isRange) {
						ThemeGuideFactory.buildLabelRangeTheme();
						ThemeGuidDialog.this.dispose();
					}
				}
			} else if ("Cancel".equals(e.getActionCommand())) {
				unregistListener();
				ThemeGuidDialog.this.buttonCancel.removeActionListener(actionListener);
				ThemeGuidDialog.this.dispose();
			}
		}

		private void createThemeContainer() {
			int clickCount = listContent.getSelectedIndex();
			if (0 == clickCount) {
				// 单值专题图
				ThemeGuideFactory.buildUniqueTheme();
				ThemeGuidDialog.this.dispose();
			} else if (1 == clickCount) {
				// 分段专题图
				ThemeGuideFactory.buildRangeTheme();
				ThemeGuidDialog.this.dispose();
			} else if (2 == clickCount) {
				// 标签统一风格专题图
				if (isUniform) {
					ThemeGuideFactory.buildLabelUnformTheme();
					ThemeGuidDialog.this.dispose();
				} else if (isRange) {
					ThemeGuideFactory.buildLabelRangeTheme();
					ThemeGuidDialog.this.dispose();
				}
			}
		}

	}

	class LocalMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getSource() == labelUniform) {
				// 统一风格标签专题图
				if (1 == e.getClickCount()) {
					labelUniform.setOpaque(true);
					labelUniform.setBackground(Color.gray);
					labelRange.setBackground(Color.white);
					isUniform = true;
					isRange = false;
				} else if (2 == e.getClickCount()) {
					// 新建标签统一风格专题图
					ThemeGuideFactory.buildLabelUnformTheme();
					ThemeGuidDialog.this.dispose();
					unregistListener();
				}
			} else {
				// 分段风格标签专题图
				if (1 == e.getClickCount()) {
					labelUniform.setBackground(Color.white);
					labelRange.setOpaque(true);
					labelRange.setBackground(Color.gray);
					isUniform = false;
					isRange = true;
				} else if (2 == e.getClickCount()) {
					// 新建分段风格标签专题图
					ThemeGuideFactory.buildLabelRangeTheme();
					ThemeGuidDialog.this.dispose();
					unregistListener();
				}
			}

		}

	}

}
