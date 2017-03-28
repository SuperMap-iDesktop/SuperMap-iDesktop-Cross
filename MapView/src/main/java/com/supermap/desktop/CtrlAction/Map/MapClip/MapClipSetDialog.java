package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.*;

/**
 * @author YuanR 2017.3.28
 *         地图裁剪统一设置Dialog
 */
public class MapClipSetDialog extends SmDialog {

	private JCheckBox checkBoxAimDataDatasource;
	private DatasourceComboBox aimDataDatasourceComboBox;
	private JCheckBox checkBoxClipMode;
	private JRadioButton insideRegion;
	private JRadioButton outsideRegion;
	private JCheckBox parClip;
	private JCheckBox eraseClipRegionCheckBox;
	//	private JCheckBox exactClippingCheckBox;
	private PanelButton panelButton;

	private MapClipJTable mapClipJTable;

	public static final int DEFAULT_PREFERREDSIZE_GAP = 10;

	private ItemListener checkBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource().equals(checkBoxAimDataDatasource)) {
				aimDataDatasourceComboBox.setEnabled(checkBoxAimDataDatasource.isSelected());
			} else if (e.getSource().equals(checkBoxClipMode)) {
				insideRegion.setEnabled(checkBoxClipMode.isSelected());
				outsideRegion.setEnabled(checkBoxClipMode.isSelected());
			}
		}
	};

	public MapClipSetDialog(MapClipJTable mapClipJTable) {
		this.mapClipJTable = mapClipJTable;
		initComponent();
		initLayout();
		initResources();
		initListeners();

		this.pack();
		this.setSize(new Dimension(450, this.getPreferredSize().height));
		this.setLocationRelativeTo(null);
	}

	private void initComponent() {
		this.checkBoxAimDataDatasource = new JCheckBox();
		this.aimDataDatasourceComboBox = new DatasourceComboBox();
		this.aimDataDatasourceComboBox.setEnabled(false);
		this.checkBoxClipMode = new JCheckBox();
		this.insideRegion = new JRadioButton();
		this.insideRegion.setEnabled(false);
		this.outsideRegion = new JRadioButton();
		this.outsideRegion.setEnabled(false);

		ButtonGroup mapClipButtonGroup = new ButtonGroup();
		mapClipButtonGroup.add(this.insideRegion);
		mapClipButtonGroup.add(this.outsideRegion);
		this.parClip = new JCheckBox();
		this.eraseClipRegionCheckBox = new JCheckBox();
//		this.exactClippingCheckBox = new JCheckBox();
		this.panelButton = new PanelButton();

	}

	private void initLayout() {
		JPanel mainPanel = new JPanel();
		GroupLayout panelLayout = new GroupLayout(mainPanel);
		panelLayout.setAutoCreateContainerGaps(true);
		panelLayout.setAutoCreateGaps(true);
		mainPanel.setLayout(panelLayout);

		//@formatter:off
		panelLayout.setHorizontalGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(this.parClip)
				.addGroup(panelLayout.createSequentialGroup()
						.addComponent(this.checkBoxAimDataDatasource).addGap(30)
						.addComponent(this.aimDataDatasourceComboBox))
				.addGroup(panelLayout.createSequentialGroup()
						.addComponent(this.checkBoxClipMode).addGap(40)
						.addComponent(this.insideRegion).addGap(30)
						.addComponent(this.outsideRegion))
				.addComponent(this.eraseClipRegionCheckBox));
//				.addComponent(this.exactClippingCheckBox));
		panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()
				.addComponent(this.parClip).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.checkBoxAimDataDatasource)
						.addComponent(this.aimDataDatasourceComboBox,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.checkBoxClipMode)
						.addComponent(this.insideRegion)
						.addComponent(this.outsideRegion)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addComponent(this.eraseClipRegionCheckBox).addGap(DEFAULT_PREFERREDSIZE_GAP)
//				.addComponent(this.exactClippingCheckBox)
				.addGap(0,0,Short.MAX_VALUE));

		//@formatter:on
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(this.panelButton, BorderLayout.SOUTH);
	}

	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_MapClip_GiveSameValue"));
		this.checkBoxAimDataDatasource.setText(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.checkBoxClipMode.setText(MapViewProperties.getString("String_MapClip_ClipTypeD"));
		this.insideRegion.setText(MapViewProperties.getString("String_MapClip_InsideRegion"));
		this.outsideRegion.setText(MapViewProperties.getString("String_MapClip_OutsideRegion"));

		this.parClip.setText(MapViewProperties.getString("String_MapClip_IsClip"));
		this.eraseClipRegionCheckBox.setText(MapViewProperties.getString("String_MapClip_EraseCheck"));
//		this.exactClippingCheckBox.setText(MapViewProperties.getString("String_MapClip_Image_ExactClip"));
	}

	private void initListeners() {
		removeEvents();
		this.checkBoxAimDataDatasource.addItemListener(checkBoxListener);
		this.checkBoxClipMode.addItemListener(checkBoxListener);
		this.panelButton.getButtonOk().addActionListener(this.OKListener);
		this.panelButton.getButtonCancel().addActionListener(this.cancelListener);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				removeEvents();
			}
		});
	}

	private void removeEvents() {
		this.checkBoxAimDataDatasource.removeItemListener(checkBoxListener);
		this.checkBoxClipMode.removeItemListener(checkBoxListener);
		this.panelButton.getButtonOk().removeActionListener(this.OKListener);
		this.panelButton.getButtonCancel().removeActionListener(this.cancelListener);
	}

	/**
	 * 确定按钮事件
	 */
	private ActionListener OKListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Boolean clip = false;
				Datasource targetDatasource = null;
				String clipType = null;
				String erase = MapViewProperties.getString("String_MapClip_No");
//			Boolean exactclip = false;
				// 获得选中的行
				int[] selectrows = mapClipJTable.getSelectedRows();
				int size = selectrows.length;

				//是否参与裁剪
				if (parClip.isSelected()) {
					clip = true;
				}
				// 是否设置目标数据源
				if (checkBoxAimDataDatasource.isSelected()) {
					targetDatasource = (Datasource) aimDataDatasourceComboBox.getSelectedItem();
				}
				//是否设置裁剪方式
				if (checkBoxClipMode.isSelected()) {
					if (insideRegion.isSelected()) {
						clipType = MapViewProperties.getString("String_MapClip_In");
					} else if (outsideRegion.isSelected()) {
						clipType = MapViewProperties.getString("String_MapClip_Out");
					}
				}
				//矢量数据集是否擦除裁剪区域
				if (eraseClipRegionCheckBox.isSelected()) {
					erase = MapViewProperties.getString("String_MapClip_Yes");
				}

				// 根据设置的统一值，对JTable数据进行统一修改
				// 对目标数据源列进行修改
				if (null != targetDatasource) {
					for (int i = 0; i < size; i++) {
						mapClipJTable.getModel().setValueAt(targetDatasource, selectrows[i], COLUMN_INDEX_AIMDATASOURCE);
					}
				}
				// 对裁剪类型进行统一修改
				if (null != clipType) {
					for (int i = 0; i < size; i++) {
						mapClipJTable.getModel().setValueAt(clipType, selectrows[i], COLUMN_INDEX_CLIPTYPE);
					}
				}
				// 对是否参与裁剪、是否擦除进行统一修改
				for (int i = 0; i < size; i++) {
					mapClipJTable.getModel().setValueAt(clip, selectrows[i], COLUMN_INDEX_ISCLIP);
					mapClipJTable.getModel().setValueAt(erase, selectrows[i], COLUMN_INDEX_ERASE);
				}
			} catch (Exception e1) {
				Application.getActiveApplication().getOutput().output(e1);
			} finally {
				MapClipSetDialog.this.dispose();
			}
		}
	};


	/**
	 * 取消按钮事件
	 */
	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			removeEvents();
			MapClipSetDialog.this.dispose();
		}
	};
}
