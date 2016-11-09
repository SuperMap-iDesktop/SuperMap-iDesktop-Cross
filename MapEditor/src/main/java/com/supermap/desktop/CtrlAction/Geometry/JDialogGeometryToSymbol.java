package com.supermap.desktop.CtrlAction.Geometry;

import com.supermap.data.SymbolGroup;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.symbolDialogs.symbolTrees.SymbolGroupTree;
import com.supermap.desktop.dialog.symbolDialogs.symbolTrees.SymbolGroupTreeNode;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TreeComboBox;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author XiaJT
 */
public class JDialogGeometryToSymbol extends SmDialog {
	private JLabel labelName = new JLabel();
	private JTextField textFieldName = new JTextField();
	private SmButton buttonOk = new SmButton();
	private SmButton buttonCancle = new SmButton();
	private JLabel labelSymbolGroup = new JLabel();
	private TreeComboBox treeComboBox = new TreeComboBox();
	private SymbolGroupTree jTree = new SymbolGroupTree(Application.getActiveApplication().getWorkspace().getResources().getMarkerLibrary().getRootGroup());

	public JDialogGeometryToSymbol() {
		super();
		init();
		this.setTitle(MapEditorProperties.getString("String_GeometryToSymbol"));
		this.setSize(300, 150);
		this.setLocationRelativeTo(null);
	}

	private void init() {
		initComponent();
		initLayout();
		initResources();
		initListeners();
		initComponentStates();
	}

	private void initComponent() {
		jTree.setEditable(false);
		treeComboBox = new TreeComboBox(jTree);
		jTree.setShowsRootHandles(false);
		treeComboBox.setSelectedItem(new TreePath(jTree.getModel().getRoot()));
		labelSymbolGroup = new JLabel();

	}

	private void initLayout() {
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));
		panelButton.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 0, 0));

		this.setLayout(new GridBagLayout());
		this.add(labelSymbolGroup, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0));
		this.add(treeComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));

		this.add(labelName, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		this.add(textFieldName, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));

		this.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));

		this.add(panelButton, new GridBagConstraintsHelper(0, 3, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 10, 10));
	}

	private void initResources() {
		labelSymbolGroup.setText(MapEditorProperties.getString("String_SymbolGroup"));
		labelName.setText(ControlsProperties.getString("String_Label_SymbolName"));
		buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initListeners() {
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.OK;
				dispose();
			}
		});
		buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				dispose();
			}
		});
	}

	private void initComponentStates() {
		textFieldName.setText("NewSymbol");
	}


	public String getSymbolName() {
		return textFieldName.getText();
	}

	public SymbolGroup getSymbolGroup() {
		return ((SymbolGroupTreeNode) jTree.getLastSelectedPathComponent()).getCurrentGroup();
	}
}
