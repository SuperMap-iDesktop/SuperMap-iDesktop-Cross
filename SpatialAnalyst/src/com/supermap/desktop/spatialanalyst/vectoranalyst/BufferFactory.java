package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.ui.MapControl;

public class BufferFactory extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelDataType;
	private JRadioButton radioButtonPointOrRegion = new JRadioButton("PointOrRegion");
	private JRadioButton radioButtonLine = new JRadioButton("Line");
	private JPanel panelDataType;
	private JPanel panelBufferBasic;
	private JPanel panelButtonGroup;
	private ButtonGroup buttonGroup;
	private MapControl mapControl;
	private LocalActionListener localActionListener = new LocalActionListener();
	private PanelButton panelButton;

	public JPanel getPanelBuffer() {
		return panelBufferBasic;
	}

	public void setPanelBuffer(JPanel panelBuffer) {

		this.panelBufferBasic = panelBuffer;
	}

	public JPanel getPanelBufferBasic() {
		return this.panelBufferBasic;
	}

	public void setPanelBufferBasic(JPanel panelBufferBasic) {
		this.panelBufferBasic = panelBufferBasic;
	}

	public BufferFactory() {
		super();
		initPanelBufferBasic();
		setBufferFactory();
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void initPanelBufferBasic() {
		setTitle(SpatialAnalystProperties.getString("String_SingleBufferAnalysis_Capital"));
		if (Application.getActiveApplication().getActiveForm() != null) {
			if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
				mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
				DatasetType datasetType = mapControl.getMap().getLayers().get(0).getDataset().getType();
				if (datasetType == DatasetType.POINT || datasetType == DatasetType.POINT3D || datasetType == DatasetType.REGION
						|| datasetType == DatasetType.REGION3D) {
					this.panelBufferBasic = new PanelPointOrRegionAnalyst();
					setSize(575, 332);
					this.radioButtonPointOrRegion.setSelected(true);

				} else {
					this.panelBufferBasic = new PanelLineBufferAnalyst();
					setSize(575, 435);
					this.radioButtonLine.setSelected(true);
				}
			} else {
				this.panelBufferBasic = new PanelLineBufferAnalyst();
				setSize(575, 435);
				this.radioButtonLine.setSelected(true);
			}
		} else {
			WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
			TreePath selectedPath = workspaceTree.getSelectionPath();
			if (selectedPath != null) {
				if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
					TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
					if (nodeData.getData() instanceof Datasource) {
						this.panelBufferBasic = new PanelLineBufferAnalyst();
						setSize(575, 435);
						this.radioButtonLine.setSelected(true);

					} else if (nodeData.getData() instanceof Dataset) {
						Dataset selectedDataset = (Dataset) nodeData.getData();
						if (selectedDataset.getType() == DatasetType.POINT || selectedDataset.getType() == DatasetType.POINT3D
								|| selectedDataset.getType() == DatasetType.REGION || selectedDataset.getType() == DatasetType.REGION3D) {
							this.panelBufferBasic = new PanelPointOrRegionAnalyst();
							setSize(575, 332);
							this.radioButtonPointOrRegion.setSelected(true);
						} else {
							this.panelBufferBasic = new PanelLineBufferAnalyst();
							setSize(575, 435);
							this.radioButtonLine.setSelected(true);
						}
					} else {
						this.panelBufferBasic = new PanelLineBufferAnalyst();
						setSize(575, 435);
						this.radioButtonLine.setSelected(true);
					}
				}
			} else {
				this.panelBufferBasic = new PanelLineBufferAnalyst();
				setSize(575, 435);
				this.radioButtonLine.setSelected(true);
			}
		}

	}

	private void setBufferFactory() {
		removeRegisterEvent();
		initComponent();
		initResources();
		registerEvent();
	}

	private void initComponent() {
		this.labelDataType = new JLabel("DataType");
		this.panelDataType = new JPanel();
		this.panelButton = new PanelButton();
		this.panelButtonGroup = new JPanel();
		this.buttonGroup = new ButtonGroup();
		this.buttonGroup.add(this.radioButtonPointOrRegion);
		this.buttonGroup.add(this.radioButtonLine);

		GroupLayout panelButtonGroupLayout = new GroupLayout(this.panelButtonGroup);
		this.panelButtonGroup.setLayout(panelButtonGroupLayout);

		//@formatter:off
		panelButtonGroupLayout.setHorizontalGroup(panelButtonGroupLayout.createSequentialGroup()
				.addComponent(this.radioButtonPointOrRegion).addGap(30)
				.addComponent(this.radioButtonLine));
		panelButtonGroupLayout.setVerticalGroup(panelButtonGroupLayout.createSequentialGroup()
				.addGroup(panelButtonGroupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonPointOrRegion)
						.addComponent(this.radioButtonLine)));
		
	   //@formatter:on

		GroupLayout panelDataTypeLayout = new GroupLayout(this.panelDataType);
		this.panelDataType.setLayout(panelDataTypeLayout);

		//@formatter:off
		panelDataTypeLayout.setHorizontalGroup(panelDataTypeLayout.createSequentialGroup()
				.addGap(20)
				.addComponent(this.labelDataType).addGap(30)
				.addComponent(this.panelButtonGroup));
		
		panelDataTypeLayout.setVerticalGroup(panelDataTypeLayout.createSequentialGroup()
				.addGap(10)
				.addGroup(panelDataTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDataType)
						.addComponent(this.panelButtonGroup)));

		this.setLayout(new BorderLayout());
		this.add(this.panelDataType, BorderLayout.NORTH);
		this.add(this.panelBufferBasic,BorderLayout.CENTER);
		this.add(this.panelButton,BorderLayout.SOUTH);
		// 添加分割线
//		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelDataType, panelBufferBasic);
//		this.add(splitPane);
	}

	private void initResources() {
		this.labelDataType.setText(SpatialAnalystProperties.getString("String_BufferAnalysis_DataType"));
		this.radioButtonLine.setText(SpatialAnalystProperties.getString("String_BufferAnalysis_Line"));
		this.radioButtonPointOrRegion.setText(SpatialAnalystProperties.getString("String_BufferAnalysis_PointAndRegion"));
	}
	

	private void registerEvent() {
		this.radioButtonPointOrRegion.addActionListener(this.localActionListener);
		this.radioButtonLine.addActionListener(this.localActionListener);
		this.panelButton.getButtonOk().addActionListener(this.localActionListener);
		this.panelButton.getButtonCancel().addActionListener(this.localActionListener);
	}
	
	private void removeRegisterEvent(){
		this.radioButtonLine.removeActionListener(this.localActionListener);
		this.radioButtonPointOrRegion.removeActionListener(this.localActionListener);
	}
	
	class LocalActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == radioButtonPointOrRegion) {
				BufferFactory.this.getContentPane().remove(panelBufferBasic);
				panelBufferBasic = new PanelPointOrRegionAnalyst();
				setSize(575, 332);
				BufferFactory.this.getContentPane().add(panelBufferBasic);
//				panelButton.getButtonOk().setEnabled(((PanelPointOrRegionAnalyst) panelBufferBasic).isButtonEnabled());
			} else if (e.getSource() == radioButtonLine) {
				BufferFactory.this.getContentPane().remove(panelBufferBasic);
				panelBufferBasic = new PanelLineBufferAnalyst();
				setSize(575, 435);
				BufferFactory.this.getContentPane().add(panelBufferBasic);
//				panelButton.getButtonOk().setEnabled(((PanelPointOrRegionAnalyst) panelBufferBasic).isButtonEnabled());
			}else if (e.getSource() == panelButton.getButtonOk()) {
				 if(panelBufferBasic instanceof PanelPointOrRegionAnalyst){
					 ((PanelPointOrRegionAnalyst)panelBufferBasic).CreateCurrentBuffer();
				 }else if (panelBufferBasic instanceof  PanelLineBufferAnalyst) {
					((PanelLineBufferAnalyst)panelBufferBasic).CreateCurrentBuffer();
				}
				 BufferFactory.this.dispose();
			}else if (e.getSource()==panelButton.getButtonCancel()) {
				BufferFactory.this.dispose();
			}
		}
	}
}