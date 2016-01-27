package com.supermap.desktop.newtheme;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ColorsComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeGraph;

/**
 * @author Administrator 统计专题图实现类
 */
public class ThemeGraphContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel panelProperty = new JPanel();
	private JPanel panelAdvance = new JPanel();
	
	private JLabel labelColorStyle = new JLabel();
	private JLabel labelGraphType = new JLabel();
	private JLabel labelMethod = new JLabel();
	private ColorsComboBox comboboxColor = new ColorsComboBox();
	private JComboBox<String> comboBoxGraphType = new JComboBox<String>();
	private JComboBox<String> comboBoxMethod = new JComboBox<String>();
	private JScrollPane scollPane = new JScrollPane();
	private JTable tableGraphInfo = new JTable();
	private JToolBar toolbar = new JToolBar();
	private JButton buttonDelete = new JButton();
	private JButton buttonAdd = new JButton();
	private JButton buttonStyle = new JButton();
	private JButton buttonMoveToFrist = new JButton();
	private JButton buttonMoveToForward = new JButton();
	private JButton buttonMoveToNext = new JButton();
	private JButton buttonMoveToLast = new JButton();
	
	private JTabbedPane tabbedPaneInfo = new JTabbedPane();
	
	private ThemeGraph themeGraph;
	private boolean isRefreshAtOnce;

	public ThemeGraphContainer() {
		
	}

	@Override
	public Theme getCurrentTheme() {
		return this.themeGraph;
	}

	private void initComponent() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.panelProperty.setLayout(new GridBagLayout());
		this.add(tabbedPaneInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		initPanelProperty();
	}

	private void initPanelProperty() {
		this.panelProperty.setLayout(new GridBagLayout());
		initToolbar();
		//@formatter:off
		this.panelProperty.add(this.labelColorStyle,   new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 0).setWeight(20, 0).setIpad(60, 0));
		this.panelProperty.add(this.comboboxColor,     new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelGraphType,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(60, 0));
		this.panelProperty.add(this.comboBoxGraphType, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelMethod,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(60, 0));
		this.panelProperty.add(this.comboBoxMethod,    new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.toolbar,           new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(100, 0));
		this.panelProperty.add(this.scollPane,         new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(100, 3));
		this.tableGraphInfo.setRowSelectionInterval(0, 0);
		this.scollPane.setViewportView(this.tableGraphInfo);		
		//@formatter:on
	}

	private void initToolbar() {
		this.toolbar.setFloatable(false);
		this.toolbar.add(this.buttonDelete);
		this.toolbar.add(this.buttonAdd);
		this.toolbar.addSeparator();
		this.toolbar.add(this.buttonStyle);
		this.toolbar.addSeparator();
		this.toolbar.add(this.buttonMoveToFrist);
		this.toolbar.add(this.buttonMoveToForward);
		this.toolbar.add(this.buttonMoveToNext);
		this.toolbar.add(this.buttonMoveToLast);
		this.buttonDelete.setIcon(new ImageIcon(ThemeUniqueContainer.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonAdd.setIcon(InternalImageIconFactory.ADD_ITEM);
		this.buttonStyle.setIcon(InternalImageIconFactory.REGION_STYLE);
	}

	@Override
	void registActionListener() {

	}

	@Override
	public void unregistActionListener() {

	}

	@Override
	void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	@Override
	void refreshMapAndLayer() {

	}

}
