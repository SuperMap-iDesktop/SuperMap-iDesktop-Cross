package com.supermap.desktop.ui.controls;

import com.supermap.data.GeoStyle;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 多选时的选择面板
 */
public class JDialogSymbolsChange extends SmDialog {

	//region 变量定义
	private SymbolType symbolType;

	private PanelSymbolSet panelPoint;
	private PanelSymbolSet panelLine;
	private PanelSymbolSet panelFill;
	private JPanel panelButton;

	private JButton buttonNext;
	private JButton buttonCancle;

	private List<GeoStyle> geoStylesBeforeList;
	private SymbolDialog symbolDialog;

	private boolean[] symbolEnables = new boolean[16];
	private ActionListener actionListenerCancle = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialogSymbolsChange.this.setDialogResult(DialogResult.CANCEL);
			JDialogSymbolsChange.this.clean();
		}
	};
	private ActionListener checkButtonNextState = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonNext.setEnabled(panelPoint.isAnySelected() || panelLine.isAnySelected() || panelFill.isAnySelected());
		}
	};

	private ActionListener actionListenerOK = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (showSymbolDialog() == DialogResult.OK) {
				changeSymbolMarket(JDialogSymbolsChange.this.symbolDialog.getStyle());
				JDialogSymbolsChange.this.setDialogResult(DialogResult.OK);
				JDialogSymbolsChange.this.clean();
			}
		}
	};

	private SymbolDialog getSymbolDialogs() {
		if (symbolDialog == null) {
			symbolDialog = new SymbolDialog();
		}
		return symbolDialog;
	}

	private DialogResult showSymbolDialog() {
		return getSymbolDialogs().showDialog(Application.getActiveApplication().getWorkspace().getResources(), geoStylesBeforeList.get(0), this.symbolType);
	}
	//endregion

	public JDialogSymbolsChange(SymbolType symbolType, List<GeoStyle> geoStylesBeforeList) {
		this.symbolType = symbolType;
		if (geoStylesBeforeList == null || geoStylesBeforeList.size() <= 0) {
			Application.getActiveApplication().getOutput().output("Are you kidding me? geoStyles is empty.");
		}
		this.geoStylesBeforeList = geoStylesBeforeList;
		initComponents();
		initResources();
		initLayout();
		registListeners();
		initPanelEnabled();
		this.setSize(new Dimension(600, 400));
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				clean();
			}
		});
	}

	private void getSymbolEnables() {
		int i = 0;
		boolean[] panelPointCheckBoxValues = panelPoint.getCheckBoxValues();
		for (; i < panelPointCheckBoxValues.length; i++) {
			symbolEnables[i] = panelPointCheckBoxValues[i];
		}
		boolean[] panelLineCheckBoxValues = panelLine.getCheckBoxValues();
		for (int j = 0; j < panelLineCheckBoxValues.length; j++, i++) {
			symbolEnables[i] = panelLineCheckBoxValues[j];
		}
		boolean[] panelFillCheckBoxValues = panelFill.getCheckBoxValues();
		for (int j = 0; j < panelFillCheckBoxValues.length; j++, i++) {
			symbolEnables[i] = panelFillCheckBoxValues[j];
		}
	}

	private void changeSymbolMarket(GeoStyle geoStyleAfter) {
		getSymbolEnables();

		//region 点面板
		if (symbolEnables[0]) {
			// 符号类型
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setMarkerSymbolID(geoStyleAfter.getMarkerSymbolID());
			}
		}
		if (symbolEnables[1]) {
			// 符号大小
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setMarkerSize(geoStyleAfter.getMarkerSize());
			}

		}
		if (symbolEnables[2]) {
			// 符号颜色
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setLineColor(geoStyle.getLineColor());
			}
		}
		if (symbolEnables[3]) {
			// 旋转角度
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setMarkerAngle(geoStyleAfter.getMarkerAngle());
			}
		}
		if (symbolEnables[4]) {
			// 透明度
			// TODO 没有设置透明度的地方，先屏蔽
//			for (GeoStyle geoStyle : geoStylesBeforeList) {
//				geoStyle.;
//			}
		}
		//endregion

		//region 线面板
		if (symbolEnables[5]) {
			// 线型
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setLineSymbolID(geoStyleAfter.getLineSymbolID());
			}
		}
		if (symbolEnables[6]) {
			// 线宽度
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setLineWidth(geoStyleAfter.getLineWidth());
			}
		}
		if (symbolEnables[7]) {
			// 线颜色
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setLineColor(geoStyleAfter.getLineColor());
			}
		}
		//endregion

		if (symbolEnables[8]) {
			// 填充类型
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setFillSymbolID(geoStyleAfter.getFillSymbolID());
			}
		}
		if (symbolEnables[9]) {
			// 背景色
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setFillBackColor(geoStyleAfter.getFillBackColor());
			}
		}
		if (symbolEnables[10]) {
			// 前景色
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setFillForeColor(geoStyleAfter.getFillForeColor());
			}
		}
		if (symbolEnables[11]) {
			// 透明度
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setFillBackOpaque(geoStyleAfter.getFillBackOpaque());
				geoStyle.setFillOpaqueRate(geoStyleAfter.getFillOpaqueRate());
			}
		}
		if (symbolEnables[12]) {
			// 渐变类型
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setFillGradientMode(geoStyleAfter.getFillGradientMode());
			}
		}
		if (symbolEnables[13]) {
			// 渐变角度
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setFillGradientAngle(geoStyleAfter.getFillGradientAngle());
			}
		}
		if (symbolEnables[14]) {
			// 渐变中心X偏移
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setFillGradientOffsetRatioX(geoStyleAfter.getFillGradientOffsetRatioX());
			}
		}
		if (symbolEnables[15]) {
			// 渐变中心Y偏移
			for (GeoStyle geoStyle : geoStylesBeforeList) {
				geoStyle.setFillGradientOffsetRatioY(geoStyle.getFillGradientOffsetRatioY());
			}
		}
	}

	private void initPanelEnabled() {

		this.buttonNext.setEnabled(false);

		if (symbolType == SymbolType.MARKER) {
			panelPoint.setCheckBoxsEnable(true);
			panelLine.setCheckBoxsEnable(false);
			panelFill.setCheckBoxsEnable(false);
		} else if (symbolType == SymbolType.LINE) {
			panelPoint.setCheckBoxsEnable(false);
			panelLine.setCheckBoxsEnable(true);
			panelFill.setCheckBoxsEnable(false);
		} else if (symbolType == SymbolType.FILL) {
			panelPoint.setCheckBoxsEnable(false);
			panelLine.setCheckBoxsEnable(true);
			panelFill.setCheckBoxsEnable(true);
		} else {
			// 3D符号不支持
			panelPoint.setCheckBoxsEnable(false);
			panelLine.setCheckBoxsEnable(false);
			panelFill.setCheckBoxsEnable(false);

		}
	}


	private void initComponents() {
		panelButton = new JPanel();
		panelPoint = new PanelSymbolSet(5);
		panelLine = new PanelSymbolSet(3);
		panelFill = new PanelSymbolSet(8);
		buttonNext = new JButton("Next");
		buttonCancle = new JButton("Cancle");
	}

	private void initResources() {
		this.setTitle(getResources("String_ModifyThemeItemStyle"));

		panelPoint.setBorder(BorderFactory.createTitledBorder(getResources("String_SymbolStyle")));
		panelPoint.initResources(new String[]{getResources("String_MarkType"), getResources("String_MarkSize"), getResources("String_MarkColor"),
				getResources("String_RotationAngle"), getResources("String_Transparency")});

		panelLine.setBorder(BorderFactory.createTitledBorder(getResources("String_LineStyle")));
		panelLine.initResources(new String[]{getResources("String_LineType"), getResources("String_LineWidth"), getResources("String_LineColor")});

		panelFill.setBorder(BorderFactory.createTitledBorder(getResources("String_FillStyle")));
		panelFill.initResources(new String[]{getResources("String_FillType"), getResources("String_BackColor"), getResources("String_ForColor"),
				getResources("String_Transparency"), getResources("String_GradientType"), getResources("String_GradientAngle"),
				getResources("String_GradientOffXCheck"), getResources("String_GradientOffYCheck")});

		buttonNext.setText(CommonProperties.getString(CommonProperties.Next));
		buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initLayout() {
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonNext, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 0, 5));
		panelButton.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));

		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.add(panelPoint, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		panelCenter.add(panelLine, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		panelCenter.add(panelFill, new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		panelCenter.add(panelButton, new GridBagConstraintsHelper(0, 3, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0));

		this.setLayout(new GridBagLayout());
		this.add(panelCenter, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(10).setWeight(1, 1));
	}

	private void registListeners() {
		panelPoint.addActionListeners(checkButtonNextState);
		panelLine.addActionListeners(checkButtonNextState);
		panelFill.addActionListeners(checkButtonNextState);

		buttonNext.addActionListener(actionListenerOK);
		buttonCancle.addActionListener(actionListenerCancle);
	}

	private void clean() {
		this.unRegistListeners();
		this.dispose();
	}

	private void unRegistListeners() {
		panelPoint.removeActionListeners(checkButtonNextState);
		panelLine.removeActionListeners(checkButtonNextState);
		panelFill.removeActionListeners(checkButtonNextState);

		buttonNext.removeActionListener(actionListenerOK);
		buttonCancle.removeActionListener(actionListenerCancle);
	}

	private String getResources(String s) {
		return CoreProperties.getString(s);
	}

	class PanelSymbolSet extends JPanel {

		List<JCheckBox> checkBoxList = new ArrayList();
		private int checkBoxCount;

		public PanelSymbolSet(int checkBoxCount) {
			this.checkBoxCount = checkBoxCount;
			this.initParams();
			this.initLayout();
		}

		private void initParams() {
			Dimension perfersize = new Dimension(150, 23);

			for (int i = 0; i < checkBoxCount; i++) {

				JCheckBox checkBox = new JCheckBox("CheckBox");
				checkBox.setPreferredSize(perfersize);
				checkBoxList.add(checkBox);
			}
		}

		public boolean[] getCheckBoxValues() {
			boolean[] checkBoxValues = new boolean[checkBoxCount];
			for (int i = 0; i < checkBoxCount; i++) {
				checkBoxValues[i] = checkBoxList.get(i).isSelected();
			}
			return checkBoxValues;
		}

		public boolean isAnySelected() {
			boolean isSelected = false;
			for (int i = 0; i < checkBoxCount; i++) {
				if (checkBoxList.get(i).isSelected()) {
					isSelected = true;
					break;
				}
			}
			return isSelected;
		}

		public void addActionListeners(ActionListener actionListener) {
			for (int i = 0; i < checkBoxCount; i++) {
				checkBoxList.get(i).addActionListener(actionListener);
			}
		}

		public void removeActionListeners(ActionListener actionListener) {
			for (int i = 0; i < checkBoxCount; i++) {
				checkBoxList.get(i).removeActionListener(actionListener);
			}
		}

		public void setCheckBoxsEnable(boolean isEnable) {
			for (int i = 0; i < checkBoxCount; i++) {
				checkBoxList.get(i).setEnabled(isEnable);
			}
		}

		public void initResources(String[] resources) {
			for (int i = 0; i < checkBoxCount; i++) {
				checkBoxList.get(i).setText(resources[i]);
			}
		}

		private void initLayout() {
			int x = 0, y = 0;
			this.setLayout(new GridBagLayout());
			for (int i = 0; i < checkBoxCount; i++) {
				GridBagConstraintsHelper gridBagConstraintsHelper = new GridBagConstraintsHelper(x, y, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(0);
				this.add(checkBoxList.get(i), gridBagConstraintsHelper);
				x++;
				if (x == 3) {
					x = 0;
					y++;
				}
			}
		}
	}
}
