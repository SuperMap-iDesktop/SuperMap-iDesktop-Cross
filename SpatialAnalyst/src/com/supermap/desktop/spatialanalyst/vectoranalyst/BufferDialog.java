package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilties.SystemPropertyUtilties;
import com.supermap.mapping.Layer;
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
	private JPanel panelBufferType;
	private JPanel panelButtonGroup;
	private ButtonGroup buttonGroup;
	private MapControl mapControl;
	private PanelButton panelButton;
	private JPanel panelBuffer;
	public final static Dimension DEFAULT_WINDOWS_BUFFER_LINE_DIMENSION = new Dimension(620, 470);
	public final static Dimension DEFAULT_WINDOWS_BUFFER_POINTORREGION_DIMENSION = new Dimension(620, 350);
	public final static Dimension DEFAULT_LINUX_BUFFER_POINTORREGION_DIMENSION = new Dimension(720, 365);
	public final static Dimension DEFAULT_LINUX_BUFFER_LINE_DIMENSION = new Dimension(720, 480);
	private LocalActionListener localActionListener = new LocalActionListener();
	private DoSome some = new DoSome() {
		@Override
		public void doSome(boolean isArcSegmentNumSuitable, boolean isComboBoxDatasetNotNull1, boolean isRadiusNumSuitable) {
			panelButton.getButtonOk().setEnabled(isArcSegmentNumSuitable && isComboBoxDatasetNotNull1 && isRadiusNumSuitable);
		}
	};

	public BufferDialog() {
		super();
		initPanelBufferBasic();
		setBufferDialog();
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void initPanelBufferBasic() {
		this.setTitle(SpatialAnalystProperties.getString("String_SingleBufferAnalysis_Capital"));
		int layersCount = 0;
		// 打开地图时，如果选中点面或线数据集时，初始化打开界面为对应的选中缓冲区类型界面，如果选中的数据类型没有点，面，线，网络等类型时，默认打开线缓冲区界面
		if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			this.mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			layersCount = this.mapControl.getMap().getLayers().getCount();
			if (layersCount > 0) {
				for (int i = 0; i < layersCount; i++) {
					Layer[] activeLayer = new Layer[layersCount];
					activeLayer[i] = mapControl.getMap().getLayers().get(i);
					if (activeLayer[i].getSelection() != null && activeLayer[i].getSelection().getCount() != 0) {
						if (activeLayer[i].getDataset().getType() == DatasetType.POINT || activeLayer[i].getDataset().getType() == DatasetType.POINT3D
								|| activeLayer[i].getDataset().getType() == DatasetType.REGION || activeLayer[i].getDataset().getType() == DatasetType.REGION3D) {
							getPointorRegionType();
							return;
						} else if (activeLayer[i].getDataset().getType() == DatasetType.LINE || activeLayer[i].getDataset().getType() == DatasetType.LINE3D
								|| activeLayer[i].getDataset().getType() == DatasetType.NETWORK
								|| activeLayer[i].getDataset().getType() == DatasetType.NETWORK3D) {
							getLineType();
							return;
						}
					}
				}
			}
		}

		// 没有打开地图时，当选中数据集节点，如果为点，面类型时，打开点面缓冲区界面，选中其他节点打开线缓冲区界面

		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath selectedPath = workspaceTree.getSelectionPath();
		if (selectedPath != null) {
			if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
				TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
				if (nodeData.getData() instanceof Dataset) {
					Dataset selectedDataset = (Dataset) nodeData.getData();
					if (selectedDataset.getType() == DatasetType.POINT || selectedDataset.getType() == DatasetType.POINT3D
							|| selectedDataset.getType() == DatasetType.REGION || selectedDataset.getType() == DatasetType.REGION3D) {
						getPointorRegionType();
						return;
					}
				}
			}
		}
		getLineType();
	}

	private void getPointorRegionType() {
		this.panelBufferType = new PanelPointOrRegionAnalyst();
		setSize(getPointPanelDimension());
		this.radioButtonPointOrRegion.setSelected(true);
		((PanelPointOrRegionAnalyst) panelBufferType).setSome(some);

	}

	private void getLineType() {
		this.panelBufferType = new PanelLineBufferAnalyst();
		setSize(getLinePanelDimension());
		this.radioButtonLine.setSelected(true);
		((PanelLineBufferAnalyst) panelBufferType).setSome(some);
	}

	private void setBufferDialog() {
		removeRegisterEvent();
		initComponent();
		initResources();
		registerEvent();
	}

	private void initComponent() {
		this.labelDataType = new JLabel("DataType");
		this.panelDataType = new JPanel();
		this.panelButton = new PanelButton();
		this.panelBuffer = new JPanel();
		this.panelButtonGroup = new JPanel();
		this.buttonGroup = new ButtonGroup();
		this.buttonGroup.add(this.radioButtonPointOrRegion);
		this.buttonGroup.add(this.radioButtonLine);

		setPanelGroupButtonLayout();
		setPanelDataTypeLayout();
		setPanelBufferLayout();

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.panelBuffer, BorderLayout.CENTER);
		this.getContentPane().add(this.panelButton, BorderLayout.SOUTH);
	}

	private void setPanelGroupButtonLayout() {
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
	}

	private void setPanelDataTypeLayout() {
		GroupLayout panelDataTypeLayout = new GroupLayout(this.panelDataType);
		panelDataTypeLayout.setAutoCreateGaps(true);
		this.panelDataType.setLayout(panelDataTypeLayout);

		//@formatter:off
            panelDataTypeLayout.setHorizontalGroup(panelDataTypeLayout.createSequentialGroup()
            		.addGap(10)
                      .addComponent(this.labelDataType).addGap(30)
                      .addComponent(this.panelButtonGroup));
           
            panelDataTypeLayout.setVerticalGroup(panelDataTypeLayout.createSequentialGroup()
                      .addGroup(panelDataTypeLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(this.labelDataType)
                                .addComponent(this.panelButtonGroup)));
       //@formatter:on
	}

	private void setPanelBufferLayout() {
		GroupLayout layout = new GroupLayout(this.panelBuffer);
		this.panelBuffer.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		//@formatter:off
		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.panelDataType)
				.addComponent(this.panelBufferType)));
        //设置panelBufferType布局竖直方向不拉伸
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(true,this.panelDataType).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(this.panelBufferType,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(10, Short.MAX_VALUE));
		
		//@formatter:on
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

		if (panelBufferType instanceof PanelLineBufferAnalyst) {
			((PanelLineBufferAnalyst) panelBufferType).addButtonListener();
		} else if (panelBufferType instanceof PanelPointOrRegionAnalyst) {
			((PanelPointOrRegionAnalyst) panelBufferType).addListener();
		}
	}

	private void removeRegisterEvent() {
		this.radioButtonLine.removeActionListener(this.localActionListener);
		this.radioButtonPointOrRegion.removeActionListener(this.localActionListener);
	}

	private Dimension getLinePanelDimension() {
		if (SystemPropertyUtilties.isWindows()) {
			return BufferDialog.DEFAULT_WINDOWS_BUFFER_LINE_DIMENSION;
		} else {
			return BufferDialog.DEFAULT_LINUX_BUFFER_LINE_DIMENSION;
		}
	}

	private Dimension getPointPanelDimension() {
		if (SystemPropertyUtilties.isWindows()) {
			return BufferDialog.DEFAULT_WINDOWS_BUFFER_POINTORREGION_DIMENSION;
		} else {
			return BufferDialog.DEFAULT_LINUX_BUFFER_POINTORREGION_DIMENSION;
		}
	}

	class LocalActionListener implements ActionListener {
		private boolean flag;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == radioButtonPointOrRegion) {
				if (panelBufferType instanceof PanelPointOrRegionAnalyst) {
					((PanelPointOrRegionAnalyst) panelBufferType).setSome(null);
				} else {
					((PanelLineBufferAnalyst) panelBufferType).setSome(null);
				}
				BufferDialog.this.getContentPane().removeAll();
				panelBufferType = new PanelPointOrRegionAnalyst();
				setBufferDialog();
				((PanelPointOrRegionAnalyst) panelBufferType).setSome(some);
				setSize(getPointPanelDimension());
			} else if (e.getSource() == radioButtonLine) {
				if (panelBufferType instanceof PanelPointOrRegionAnalyst) {
					((PanelPointOrRegionAnalyst) panelBufferType).setSome(null);
				} else {
					((PanelLineBufferAnalyst) panelBufferType).setSome(null);
				}
				BufferDialog.this.getContentPane().removeAll();
				panelBufferType = new PanelLineBufferAnalyst();
				setBufferDialog();
				((PanelLineBufferAnalyst) panelBufferType).setSome(some);
				setSize(getLinePanelDimension());
			} else if (e.getSource() == panelButton.getButtonOk()) {
				try {
					if (panelBufferType instanceof PanelPointOrRegionAnalyst) {
						flag = ((PanelPointOrRegionAnalyst) panelBufferType).createCurrentBuffer();

					} else if (panelBufferType instanceof PanelLineBufferAnalyst) {
						flag = ((PanelLineBufferAnalyst) panelBufferType).CreateCurrentBuffer();
					}
				} catch (Exception e1) {
					BufferDialog.this.dispose();
				}
				if (flag) {
					BufferDialog.this.dispose();
				}
			} else if (e.getSource() == panelButton.getButtonCancel()) {
				BufferDialog.this.dispose();
			}
		}
	}
}
