package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.ui.MapControl;

public class BufferDialog extends SmDialog {
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
	private DatasetComboBox datasetComboBox;
	private final static Dimension DEFAULT_BUFFER_LINE_DIMENSION = new Dimension(575, 435);
	private final static Dimension DEFAULT_BUFFER_POINTORREGION_DIMENSION = new Dimension(575, 332);

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

	public BufferDialog() {
		super();
		initPanelBufferBasic();
		setBufferFactory();
		initBufferButtonOk();
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void initPanelBufferBasic() {
		setTitle(SpatialAnalystProperties.getString("String_SingleBufferAnalysis_Capital"));
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath selectedPath = workspaceTree.getSelectionPath();
		if (selectedPath != null) {
			if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
				TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
				if (nodeData.getData() instanceof Datasource) {
					getPanelLineBuffer();
				} else if (nodeData.getData() instanceof Dataset) {
					Dataset selectedDataset = (Dataset) nodeData.getData();
					if (selectedDataset.getType() == DatasetType.POINT || selectedDataset.getType() == DatasetType.POINT3D
							|| selectedDataset.getType() == DatasetType.REGION || selectedDataset.getType() == DatasetType.REGION3D) {
						this.panelBufferBasic = new PanelPointOrRegionAnalyst();
						setSize(DEFAULT_BUFFER_POINTORREGION_DIMENSION);
						this.radioButtonPointOrRegion.setSelected(true);
					} else {
						getPanelLineBuffer();
					}
				} else {
					getPanelLineBuffer();
				}
			}
		} else {
			getPanelLineBuffer();
		}

	}

	private void getPanelLineBuffer() {
		this.panelBufferBasic = new PanelLineBufferAnalyst();
		setSize(DEFAULT_BUFFER_LINE_DIMENSION);
		this.radioButtonLine.setSelected(true);
	}

	private void initBufferButtonOk() {
		if (panelBufferBasic instanceof PanelLineBufferAnalyst) {
			this.datasetComboBox = ((PanelLineBufferAnalyst) panelBufferBasic).getPanelBufferData().getComboBoxBufferDataDataset();
		} else if (panelBufferBasic instanceof PanelPointOrRegionAnalyst) {
			this.datasetComboBox = ((PanelPointOrRegionAnalyst) panelBufferBasic).getPanelBufferData().getComboBoxBufferDataDataset();
		}
		panelButton.getButtonOk().setEnabled(datasetComboBox.getSelectedDataset() != null);
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

         
          GridBagLayout gridBagConstraints = new GridBagLayout();
          this.getContentPane().setLayout(gridBagConstraints);
         
          this.setLayout(new BorderLayout());
          this.add(panelDataType, BorderLayout.NORTH);
          this.add(panelBufferBasic, BorderLayout.CENTER);
          this.add(panelButton, BorderLayout.SOUTH);
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
         
          if (panelBufferBasic instanceof PanelLineBufferAnalyst) {
               ((PanelLineBufferAnalyst)panelBufferBasic).addListener();
          }else if (panelBufferBasic instanceof PanelPointOrRegionAnalyst) {
			((PanelPointOrRegionAnalyst)panelBufferBasic).addListener();
		}
     }
    
     private void removeRegisterEvent(){
          this.radioButtonLine.removeActionListener(this.localActionListener);
          this.radioButtonPointOrRegion.removeActionListener(this.localActionListener);
     }
     
 
    
     class LocalActionListener implements ActionListener{
          private boolean flag;
         
          @Override
          public void actionPerformed(ActionEvent e) {
               if (e.getSource() == radioButtonPointOrRegion) {
                    BufferDialog.this.getContentPane().remove(panelBufferBasic);
                    panelBufferBasic = new PanelPointOrRegionAnalyst();
                    BufferDialog.this.getContentPane().add(panelBufferBasic);
                    ((PanelPointOrRegionAnalyst) panelBufferBasic).addListener();
                    ((PanelPointOrRegionAnalyst) panelBufferBasic).setSome(new DoSome() {
						@Override
						
						public void doSome() {
							 panelButton.getButtonOk().setEnabled(((PanelPointOrRegionAnalyst) panelBufferBasic).isButtonOKEnabled()&&((PanelPointOrRegionAnalyst) panelBufferBasic).isEnabled());
						}
					});
                    setSize(DEFAULT_BUFFER_POINTORREGION_DIMENSION);
               } else if (e.getSource() == radioButtonLine) {
                    BufferDialog.this.getContentPane().remove(panelBufferBasic);
                    panelBufferBasic = new PanelLineBufferAnalyst();
                    BufferDialog.this.getContentPane().add(panelBufferBasic);
                    setSize(DEFAULT_BUFFER_LINE_DIMENSION);
                    
                    ((PanelLineBufferAnalyst) panelBufferBasic).addListener();
                    ((PanelLineBufferAnalyst) panelBufferBasic).setSome(new DoSome() {
						@Override
						public void doSome() {
							 panelButton.getButtonOk().setEnabled(((PanelLineBufferAnalyst) panelBufferBasic).isButtonOkEnabled()&&((PanelLineBufferAnalyst) panelBufferBasic).isEnabled());
						}
					});
                    
               }else if (e.getSource() == panelButton.getButtonOk()) {
                    try {
						if(panelBufferBasic instanceof PanelPointOrRegionAnalyst){
						     flag = ((PanelPointOrRegionAnalyst)panelBufferBasic).createCurrentBuffer();
						}else if (panelBufferBasic instanceof  PanelLineBufferAnalyst) {
						     flag = ((PanelLineBufferAnalyst)panelBufferBasic).CreateCurrentBuffer();
						}
					} catch (Exception e1) {
						BufferDialog.this.dispose();
					}
                    if(flag==true){
                    BufferDialog.this.dispose();
                    }
               }else if (e.getSource()==panelButton.getButtonCancel()) {
                    BufferDialog.this.dispose();
               }
          }
     }
}
