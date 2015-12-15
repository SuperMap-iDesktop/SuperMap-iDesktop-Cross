package com.supermap.desktop.datatopology.CtrlAction;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.ui.controls.SmDialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogTopoAdvance extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFieldFilterExpression;
	private JTextField textFieldOvershootsTolerance = new JTextField();
	private JTextField textFieldUndershootsTolerance = new JTextField();
	private JTextField textFieldVertexTorance = new JTextField();
	private JLabel labelOvershootsTolerance = new JLabel("String_Label_OvershootsTolerance");
	private JLabel labelUndershootsTolerance = new JLabel("String_Label_UndershootsTolerance");
	private JLabel labelVertexTolerance = new JLabel("String_Label_VertexTolerance");
	private JLabel labelFilterExpression = new JLabel("String_FilterExpression");
	private JLabel labelNotCutting = new JLabel("String_NotCutting");
	private JPanel panelLinesIntersected = new JPanel();
	private JPanel panelToleranceSetting = new JPanel();
	private JButton buttonMore = new JButton("...");
	private JButton buttonSure = new JButton("String_Button_OK");
	private JButton buttonQuite = new JButton("String_Button_Cancel");
	private transient TopologyProcessingOptions topologyProcessingOptions = new TopologyProcessingOptions();
	private DatasetComboBox comboBoxNotCutting;
	private transient DatasetVector targetDataset;
	private transient Datasource datasource;
	private SQLExpressionDialog sqlExpressionDialog;

	/**
	 * @wbp.parser.constructor
	 */
	public JDialogTopoAdvance(JDialog owner, boolean model, Datasource datasource) {
		super(owner, model);
		this.datasource = datasource;
		setLocationRelativeTo(owner);
		initComponents();
		initResources();
	}

	public JDialogTopoAdvance(JDialog owner, boolean model, TopologyProcessingOptions topologyProcessingOptions, DatasetVector targetDataset,
			Datasource datasource) {
		super(owner, model);
		setLocationRelativeTo(owner);
		this.datasource = datasource;
		this.topologyProcessingOptions = topologyProcessingOptions;
		this.targetDataset = targetDataset;
		initComponents();
		initResources();
	}

	private void initResources() {
		setTitle(DataTopologyProperties.getString("String_Form_AdvanceSettings"));
		labelOvershootsTolerance.setText(DataTopologyProperties.getString("String_Label_OvershootsTolerance"));
		labelUndershootsTolerance.setText(DataTopologyProperties.getString("String_Label_UndershootsTolerance"));
		labelVertexTolerance.setText(DataTopologyProperties.getString("String_Label_VertexTolerance"));
		labelFilterExpression.setText(DataTopologyProperties.getString("String_FilterExpression"));
		labelNotCutting.setText(DataTopologyProperties.getString("String_NotCutting"));
		buttonSure.setText(CommonProperties.getString("String_Button_OK"));
		buttonQuite.setText(CommonProperties.getString("String_Button_Cancel"));
		panelLinesIntersected.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), DataTopologyProperties
				.getString("String_LinesIntersected"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelToleranceSetting.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), DataTopologyProperties
				.getString("String_GroupBox_ToleranceSetting"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}

	private void initComponents() {
		initComboBoxItem();
		setSize(320, 320);
		//@formatter:off
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING,groupLayout.createSequentialGroup()
								.addComponent(buttonSure)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(buttonQuite))
						.addComponent(panelToleranceSetting, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
						.addComponent(panelLinesIntersected, GroupLayout.PREFERRED_SIZE, 270, Short.MAX_VALUE))
						.addGap(26)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(panelLinesIntersected)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelToleranceSetting)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(buttonSure)
								.addComponent(buttonQuite))
						.addContainerGap(13, Short.MAX_VALUE)));
		//@formatter:on
		textFieldOvershootsTolerance.setColumns(10);
		textFieldUndershootsTolerance.setColumns(10);
		textFieldVertexTorance.setColumns(10);
		//@formatter:off
		GroupLayout gl_panelToleranceSetting = new GroupLayout(panelToleranceSetting);
		gl_panelToleranceSetting.setHorizontalGroup(gl_panelToleranceSetting.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelToleranceSetting.createSequentialGroup()
						.addGroup(gl_panelToleranceSetting.createParallelGroup(Alignment.TRAILING)
								.addComponent(labelOvershootsTolerance, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
								.addComponent(labelUndershootsTolerance, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
								.addComponent(labelVertexTolerance, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelToleranceSetting.createParallelGroup(Alignment.TRAILING)
								.addComponent(textFieldOvershootsTolerance, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panelToleranceSetting.createSequentialGroup()
										.addComponent(textFieldUndershootsTolerance,GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panelToleranceSetting.createSequentialGroup()
										.addComponent(textFieldVertexTorance,GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)))));
		gl_panelToleranceSetting.setVerticalGroup(gl_panelToleranceSetting.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelToleranceSetting.createSequentialGroup()
						.addGroup(gl_panelToleranceSetting.createParallelGroup(Alignment.LEADING)
								.addComponent(labelOvershootsTolerance)
								.addComponent(textFieldOvershootsTolerance))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelToleranceSetting.createParallelGroup(Alignment.LEADING)
								.addComponent(labelUndershootsTolerance)
								.addComponent(textFieldUndershootsTolerance))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelToleranceSetting.createParallelGroup(Alignment.LEADING)
								.addComponent(labelVertexTolerance)
								.addComponent(textFieldVertexTorance))
						.addContainerGap()));
		/**
		 * labelOvershootsTolerance textFieldOvershootsTolerance 
		 * labelUndershootsTolerance textFieldUndershootsTolerance 
		 * labelVertexTolerance textFieldVertexTorance
		 */
		gl_panelToleranceSetting.setAutoCreateContainerGaps(true);
		gl_panelToleranceSetting.setAutoCreateGaps(true);
		panelToleranceSetting.setLayout(gl_panelToleranceSetting);

		textFieldFilterExpression = new JTextField();
		textFieldFilterExpression.setColumns(10);

		GroupLayout gl_panelLinesIntersected = new GroupLayout(panelLinesIntersected);
		gl_panelLinesIntersected.setHorizontalGroup(gl_panelLinesIntersected.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLinesIntersected.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panelLinesIntersected.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panelLinesIntersected.createSequentialGroup()
										.addComponent(labelFilterExpression)
										.addComponent(textFieldFilterExpression, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(buttonMore, 0, 0, Short.MAX_VALUE))
								.addGroup(gl_panelLinesIntersected.createSequentialGroup()
										.addComponent(labelNotCutting)
										.addComponent(comboBoxNotCutting, 0, 181, Short.MAX_VALUE)))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_panelLinesIntersected.setVerticalGroup(gl_panelLinesIntersected.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLinesIntersected.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panelLinesIntersected.createParallelGroup(Alignment.BASELINE)
								.addComponent(labelFilterExpression)
								.addComponent(textFieldFilterExpression, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
								.addComponent(buttonMore))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelLinesIntersected.createParallelGroup(Alignment.BASELINE)
								.addComponent(labelNotCutting)
								.addComponent(comboBoxNotCutting, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panelLinesIntersected.setLayout(gl_panelLinesIntersected);
		getContentPane().setLayout(groupLayout);
		//formatter:on
		buttonSure.addActionListener(new CommonButtonListener());
		buttonQuite.addActionListener(new CommonButtonListener());
		buttonMore.addActionListener(new CommonButtonListener());
	}

	private void initComboBoxItem() {
		try {
			if (null != targetDataset) {
				comboBoxNotCutting = new DatasetComboBox(new Dataset[0]);
				for (int i = 0; i < targetDataset.getDatasource().getDatasets().getCount(); i++) {
					Dataset tempDataset = targetDataset.getDatasource().getDatasets().get(i);
					if (tempDataset.getType() == DatasetType.POINT) {
						String path = CommonToolkit.DatasetImageWrap.getImageIconPath(tempDataset.getType());
						DataCell cell = new DataCell(path, tempDataset.getName());
						comboBoxNotCutting.addItem(cell);
					}
				}
				this.textFieldOvershootsTolerance.setText(Double.toString(((DatasetVector) targetDataset).getTolerance().getDangle()));
				this.textFieldUndershootsTolerance.setText(Double.toString(((DatasetVector) targetDataset).getTolerance().getExtend()));
				this.textFieldVertexTorance.setText(Double.toString(((DatasetVector) targetDataset).getTolerance().getNodeSnap()));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	class CommonButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			if (buttonSure == c) {
				setTopologyInfo();
				dispose();
			}
			if (buttonQuite == c) {
				dispose();
			}
			if (buttonMore == c) {
				addItemToTextFieldFilterExpression();
			}
		}

		private void addItemToTextFieldFilterExpression() {
			sqlExpressionDialog = new SQLExpressionDialog();
			Dataset[] datasets = new Dataset[1];
			datasets[0] = targetDataset;
			DialogResult dialogResult = sqlExpressionDialog.showDialog("",datasets);
			if (dialogResult == DialogResult.OK) {
				String filter = sqlExpressionDialog.getQueryParameter().getAttributeFilter();
				if (filter != null && !"".equals(filter.trim())) {
					textFieldFilterExpression.setText(filter);
				}
			}
		}
	}

	private void setTopologyInfo() {
		try {
			String arcFilterString = textFieldFilterExpression.getText();
			if (null != arcFilterString && !arcFilterString.isEmpty() && !"".equals(arcFilterString)) {
				topologyProcessingOptions.setArcFilterString(arcFilterString);
			}
			if (0 < comboBoxNotCutting.getItemCount()) {
				String datasetName = comboBoxNotCutting.getSelectItem();
				DatasetVector dataset = (DatasetVector) CommonToolkit.DatasetWrap.getDatasetFromDatasource(datasetName, datasource);
				topologyProcessingOptions.setVertexFilterRecordset(dataset.getRecordset(false, CursorType.STATIC));
			}

			double overshootsTolerance = Double.parseDouble(textFieldOvershootsTolerance.getText());
			topologyProcessingOptions.setOvershootsTolerance(overshootsTolerance);
			double undershootsTolerance = Double.parseDouble(textFieldUndershootsTolerance.getText());
			topologyProcessingOptions.setUndershootsTolerance(undershootsTolerance);
			double vertexTorance = Double.parseDouble(textFieldVertexTorance.getText());
			topologyProcessingOptions.setVertexTolerance(vertexTorance);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
