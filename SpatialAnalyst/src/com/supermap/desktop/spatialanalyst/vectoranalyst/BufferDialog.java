package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private LocalActionListener localActionListener = new LocalActionListener();
	private PanelButton panelButton;
	private JPanel panelBuffer;
	public final static Dimension DEFAULT_WINDOWS_BUFFER_LINE_DIMENSION = new Dimension(620, 470);
	public final static Dimension DEFAULT_WINDOWS_BUFFER_POINTORREGION_DIMENSION = new Dimension(620, 350);
	public final static Dimension DEFAULT_LINUX_BUFFER_LINE_DIMENSION = new Dimension(670, 470);
	public final static Dimension DEFAULT_LINUX_BUFFER_POINTORREGION_DIMENSION = new Dimension(670, 370);
	private DoSome some = new DoSome() {
		@Override
		public void doSome(boolean enable) {
			panelButton.getButtonOk().setEnabled(enable);
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

		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();

		if (activeDatasets.length > 0
				&& (activeDatasets[0].getType() == DatasetType.POINT || activeDatasets[0].getType() == DatasetType.POINT3D
						|| activeDatasets[0].getType() == DatasetType.REGION || activeDatasets[0].getType() == DatasetType.REGION3D)) {

			this.panelBufferType = new PanelPointOrRegionAnalyst();
			setSize(getPointPanelDimension());
			this.radioButtonPointOrRegion.setSelected(true);
			((PanelPointOrRegionAnalyst) panelBufferType).setSome(some);
		} else {
			this.panelBufferType = new PanelLineBufferAnalyst();
			setSize(getLinePanelDimension());
			this.radioButtonLine.setSelected(true);
			((PanelLineBufferAnalyst) panelBufferType).setSome(some);
		}
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
		
		

		GroupLayout layout = new GroupLayout(this.panelBuffer);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		//@formatter:off
		this.panelBuffer.setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.panelDataType)
				.addComponent(this.panelBufferType)));
        //设置panelBufferType布局竖直方向不拉伸
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(true,this.panelDataType).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(this.panelBufferType,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(10, Short.MAX_VALUE));
		
		//@formatter:on

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.panelBuffer, BorderLayout.CENTER);
		this.getContentPane().add(this.panelButton, BorderLayout.SOUTH);
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
			((PanelLineBufferAnalyst) panelBufferType).addListener();
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
				setSize(getLinePanelDimension());
				((PanelLineBufferAnalyst) panelBufferType).setSome(some);

			} else if (e.getSource() == panelButton.getButtonOk()) {
				try {
					if (panelBufferType instanceof PanelPointOrRegionAnalyst) {
						flag = ((PanelPointOrRegionAnalyst) panelBufferType).createCurrentBuffer();
						((PanelPointOrRegionAnalyst) panelBufferType).addListener();
						if (!((PanelPointOrRegionAnalyst) panelBufferType).isButtonEnabled()) {
							flag = false;
							JOptionPane.showMessageDialog(BufferDialog.this, SpatialAnalystProperties.getString("String_Dataset_Not_Null"));
						}

					} else if (panelBufferType instanceof PanelLineBufferAnalyst) {
						flag = ((PanelLineBufferAnalyst) panelBufferType).CreateCurrentBuffer();
						((PanelLineBufferAnalyst) panelBufferType).addListener();
						if (!((PanelLineBufferAnalyst) panelBufferType).isButtonEnabled()) {
							flag = false;
							JOptionPane.showMessageDialog(BufferDialog.this, SpatialAnalystProperties.getString("String_Dataset_Not_Null"));
						}
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
