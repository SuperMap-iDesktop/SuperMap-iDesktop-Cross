package com.supermap.desktop.ui.controls;

import com.supermap.data.GeoStyle;
import com.supermap.data.GeoStyle3D;
import com.supermap.data.Resources;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3DSettingVector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 符号选择器对话框
 * 
 * @author xuzw
 *
 */
public class SymbolDialog extends SmDialog {

	private static final long serialVersionUID = 1L;

	private transient Resources resources;

	private transient SymbolType fieldSymbolType;

	// 用于存放用户之前的GeoStyle
	private transient GeoStyle preStyle;

	// 操作过程中被赋值的GeoStyle，该GeoStyle经过用户一系列的赋值之后返回给用户
	private transient GeoStyle activeStyle;

	public DialogResult getDialogResult() {
		return dialogResult;
	}

	// 符号选择器的最终结果，默认是CANCEL
	private transient DialogResult dialogResult = DialogResult.CANCEL;

	private JPanel jPanelButton;

	private SmButton jButtonOk;

	private SmButton jButtonCancel;

	private SmButton jButtonApply;

	private transient SymbolPanel symbolPanel;

	private boolean applyEnable = false;

	/**
	 * 默认构造函数
	 */
	public SymbolDialog() {
		super();
		this.setModal(true);
		this.componentList.add(this.jButtonOk);
		this.componentList.add(this.jButtonCancel);
		this.componentList.add(this.jButtonApply);
		this.setFocusTraversalPolicy(policy);
	}

	public SymbolDialog(JDialog owner) {
		super(owner);
		this.setModal(true);
		this.componentList.add(this.jButtonOk);
		this.componentList.add(this.jButtonCancel);
		this.componentList.add(this.jButtonApply);
		this.setFocusTraversalPolicy(policy);
	}

	/**
	 * 控制应用按钮是否可用，默认不可用
	 * 
	 * @param applyEnable
	 *            应用按钮是否可用
	 */
	public void setApplyEnable(boolean applyEnable) {
		this.applyEnable = applyEnable;
	}

	/**
	 * 显示符号选择器
	 * 
	 * @param resources
	 *            资源
	 * @param geoStyle
	 *            用户传入的GeoStyle
	 * @param symbolType
	 *            符号选择器类型
	 * @return 返回用户对当前选择器的操作，确定按钮表示OK，取消按钮表示CANCLE
	 */
	public DialogResult showDialog(Resources resources, GeoStyle geoStyle, SymbolType symbolType) {
		// preStyle用于以后添加应用后点击取消的回滚数据操作
		preStyle = geoStyle.clone();
		this.activeStyle = geoStyle;
		this.resources = resources;
		this.fieldSymbolType = symbolType;

		if (fieldSymbolType.equals(SymbolType.MARKER)) {
			this.setSize(715, 530);
			initialize();
		} else if (fieldSymbolType.equals(SymbolType.LINE)) {
			this.setSize(720, 530);
			initialize();
		} else if (fieldSymbolType.equals(SymbolType.FILL)) {
			this.setSize(700, 530);
			initialize();
		}

		symbolPanel = new SymbolPanel(resources, geoStyle.clone(), symbolType);
		this.setLayout(new GridBagLayout());
		this.add(symbolPanel, new GridBagConstraintsHelper(0, 0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.add(getButtonPanel(), new GridBagConstraintsHelper(0, 1).setInsets(10, 0, 10, 0).setWeight(1, 0).setAnchor(GridBagConstraints.EAST));
		this.getRootPane().setDefaultButton(jButtonOk);
		this.setVisible(true);
		return dialogResult;
	}

	/**
	 * 获取用户经过一系列操作后最终的GeoStyle
	 * 
	 * @return
	 */
	public GeoStyle getStyle() {
		GeoStyle geoStyle = null;
		if (dialogResult.equals(DialogResult.CANCEL)) {
			geoStyle = preStyle;
		} else if (dialogResult.equals(DialogResult.OK)) {
			geoStyle = activeStyle;
		}
		return geoStyle;
	}

	/**
	 * 返回用户设置的资源
	 * 
	 * @return
	 */
	public Resources getResources() {
		return resources;
	}

	/**
	 * 返回用户设置的符号类型
	 * 
	 * @return
	 */
	public SymbolType getSymbolType() {
		return fieldSymbolType;
	}

	/**
	 * 初始化面板
	 */
	private void initialize() {
		this.setTitle(ControlsProperties.getString("String_Title_SymbolDialog"));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = this.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonOkClicekd() {
		try {
			activeStyle = symbolPanel.getStyle();
			dialogResult = DialogResult.OK;
			setVisible(false);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void buttonCancelClicked() {
		// 回滚到最初状态
		dialogResult = DialogResult.CANCEL;
		setVisible(false);
	}

	/**
	 * 获取按钮面板
	 * 
	 * @return
	 */
	private JPanel getButtonPanel() {
		try {
			if (jPanelButton == null) {
				jPanelButton = new JPanel();
				jPanelButton.setLayout(new GridBagLayout());
				jButtonOk = new SmButton();
				jButtonOk.setText(CommonProperties.getString("String_Button_OK"));
				jPanelButton.add(jButtonOk, new GridBagConstraintsHelper(0, 0).setAnchor(GridBagConstraints.EAST).setWeight(0, 1).setInsets(0, 0, 0, 10));
				jButtonOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						buttonOkClicekd();
					}

				});

				jButtonCancel = new SmButton();
				jButtonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
				jPanelButton.add(jButtonCancel, new GridBagConstraintsHelper(1, 0).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setInsets(0, 0, 0, 10));
				jButtonCancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						buttonCancelClicked();
					}

				});

				jButtonApply = new SmButton();
				jButtonApply.setText(CommonProperties.getString(CommonProperties.Apply));
				jPanelButton.add(jButtonApply, new GridBagConstraintsHelper(2, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 10));
				jButtonApply.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						abstractJbuttonApplyLisenter();
					}

				});

			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return jPanelButton;
	}

	private void abstractJbuttonApplyLisenter() {
		if (!applyEnable) {
			return;
		}
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormMap) {
			Layer[] layers = ((IFormMap) activeForm).getActiveLayers();
			for (Layer layer : layers) {
				LayerSettingVector layerSetting = (LayerSettingVector) layer.getAdditionalSetting();
				if (null != layerSetting) {
					layerSetting.setStyle(symbolPanel.getStyle());
				}
			}
			((IFormMap) activeForm).getMapControl().getMap().refresh();
		} else if (activeForm instanceof IFormScene) {
			Layer3D[] layer3Ds = ((IFormScene) activeForm).getActiveLayer3Ds();
			for (Layer3D layer3D : layer3Ds) {
				Layer3DSettingVector layerSetting = (Layer3DSettingVector) ((Layer3DDataset) layer3D).getAdditionalSetting();
				layerSetting.setStyle(getGeoStyle3D(symbolPanel.getStyle()));
			}
		}
	}

	private GeoStyle3D getGeoStyle3D(GeoStyle style) {
		GeoStyle3D style3D = new GeoStyle3D();
		style3D.setFillBackColor(style.getFillBackColor());
		style3D.setFillForeColor(style.getFillForeColor());
		style3D.setFillGradientAngle(style.getFillGradientAngle());
		style3D.setFillGradientMode(style.getFillGradientMode());
		style3D.setFillSymbolID(style.getFillSymbolID());
		style3D.setLineColor(style.getLineColor());
		style3D.setLineSymbolID(style.getLineSymbolID());
		style3D.setLineWidth(style.getLineWidth());
		style3D.setMarker3DRotateX(style.getMarkerAngle());
		style3D.setMarker3DRotateY(style.getMarkerAngle());
		style3D.setMarker3DRotateZ(style.getMarkerAngle());
		style3D.setMarkerSize(style.getMarkerSize().getHeight());
		style3D.setMarkerSymbolID(style.getMarkerSymbolID());
		return style3D;
	}

	@Override
	public void escapePressed() {
		buttonCancelClicked();
	}

	@Override
	public void enterPressed() {
		if (this.getRootPane().getDefaultButton() == this.jButtonOk) {
			buttonOkClicekd();
		}
		if (this.getRootPane().getDefaultButton()==this.jButtonCancel) {
			buttonCancelClicked();
		}
		if (this.getRootPane().getDefaultButton()==this.jButtonApply) {
			abstractJbuttonApplyLisenter();
		}
	}
}
