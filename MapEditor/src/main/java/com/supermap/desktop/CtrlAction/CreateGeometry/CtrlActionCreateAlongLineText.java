package com.supermap.desktop.CtrlAction.CreateGeometry;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoCardinal;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoText;
import com.supermap.data.TextPart;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.CADStyle.DefaultTextStyle;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.ActionChangedListener;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CtrlActionCreateAlongLineText extends ActionCreateBase {
	private static final double DEFAULT_FONT_PIXEL_HEIGHT = 23;

	private TrackedListener trackedListener = new TrackedListener() {

		@Override
		public void tracked(TrackedEvent arg0) {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

			GeoCompound geoCompound = (GeoCompound) arg0.getGeometry();
			GeoText geoText = (GeoText) geoCompound.getPart(0);

			// 输入同时在地图上显示
			JDialogCreateAlongText jDialogCreateAlongText = new JDialogCreateAlongText(formMap, geoCompound);
			if (jDialogCreateAlongText.showDialog() == DialogResult.OK) {
				String text = jDialogCreateAlongText.getText();

				geoText.getTextStyle().setSizeFixed(false);
//				String activeMapName=((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap().getName();
//				geoText.setTextStyle(DefaultTextStyle.getDefaultGeoStyle(activeMapName).clone());
				// DEFAULT_FONT_PIXEL_HEIGHT 是一个经验值，使得不固定大小的时候，最后绘制到地图上的文本大小与输入的时候基本一致
				geoText.getTextStyle().setFontHeight(DEFAULT_FONT_PIXEL_HEIGHT * MapUtilities.pixelLength(formMap.getMapControl()));
				TextPart textPart = new TextPart();
				textPart.setText(text);
				geoText.addPart(textPart);
			}

		}
	};
	private ActionChangedListener actionChangedListener = new ActionChangedListener() {

		@Override
		public void actionChanged(ActionChangedEvent arg0) {
			abstractActionChangedListener(arg0);
		}

	};

	private void abstractActionChangedListener(ActionChangedEvent arg0) {
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

		if (arg0.getOldAction() == Action.CREATE_ALONG_LINE_TEXT) {

			// 绘制过程中，按住中键会切换为漫游，此时不希望结束绘制
			if (arg0.getNewAction() != Action.PAN) {
				formMap.getMapControl().removeActionChangedListener(actionChangedListener);
				formMap.getMapControl().removeTrackedListener(trackedListener);
			}
		} else if (arg0.getOldAction() == Action.PAN && arg0.getNewAction() != Action.CREATE_ALONG_LINE_TEXT) {

			// 在漫游状态，改变为其他 Action，触发这个事件，表明在绘制中进行的漫游，如果切换为CREATE_ALONG_LINE_TEXT 之外的 Action，那么就结束绘制
			formMap.getMapControl().removeActionChangedListener(actionChangedListener);
			formMap.getMapControl().removeTrackedListener(trackedListener);
		} else if (arg0.getNewAction() == Action.CREATE_ALONG_LINE_TEXT) {
			formMap.getMapControl().addTrackedListener(trackedListener);
		}
	}

	public CtrlActionCreateAlongLineText(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action getAction() {
		return Action.CREATE_ALONG_LINE_TEXT;
	}

	@Override
	public void run() {
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		formMap.getMapControl().addActionChangedListener(this.actionChangedListener);
		formMap.getMapControl().setAction(Action.CREATE_ALONG_LINE_TEXT);
	}

	@Override
	public boolean isSupportDatasetType(DatasetType datasetType) {
		return DatasetType.TEXT == datasetType || DatasetType.CAD == datasetType;
	}

	private class JDialogCreateAlongText extends SmDialog {

		private IFormMap formMap;
		private final String CreateAlongLineTextTracing = "CreateAlongLineTextTracing";
		private JLabel labelDescribe;
		private JTextField textFieldText;
		private JPanel panelButton;
		private SmButton buttonOk;
		private SmButton buttonCancel;
		private GeoLine geoLine;

		public JDialogCreateAlongText(IFormMap formMap, GeoCompound geoCompound) {
			super();
			this.formMap = formMap;
			GeoCompound clone = geoCompound.clone();
			geoLine = ((GeoCardinal) clone.getPart(1)).convertToLine(10);

			initComponents();
			initListeners();
			initLayout();
			initResources();
			this.componentList.add(this.buttonOk);
			this.componentList.add(this.buttonCancel);
			this.setFocusTraversalPolicy(policy);
			this.getRootPane().setDefaultButton(this.buttonCancel);
		}

		private void initComponents() {
			labelDescribe = new JLabel();
			textFieldText = new JTextField();
			panelButton = new JPanel();
			buttonOk = new SmButton();
			buttonCancel = new SmButton();
			this.setSize((int) (360 * SystemPropertyUtilities.getSystemSizeRate()), (int) (120 * SystemPropertyUtilities.getSystemSizeRate()));
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.buttonOk.setEnabled(false);
			getRootPane().setDefaultButton(this.buttonOk);
		}

		private void initListeners() {
			this.textFieldText.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					updateText();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					updateText();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					updateText();
				}
			});
			this.buttonOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonOkClicked();
				}
			});

			this.buttonCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					dispose();
				}
			});
		}

		private void buttonOkClicked() {
			dialogResult = DialogResult.OK;
			dispose();
		}

		private void updateText() {
			if (StringUtilities.isNullOrEmpty(getText().trim())) {
				buttonOk.setEnabled(false);
				removeTrackingObject();
			} else {
				buttonOk.setEnabled(true);
				this.getRootPane().setDefaultButton(buttonOk);
				GeoText geoText = GeoText.makeAlongLineText(getText(), geoLine);

//				String activeMapName=((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap().getName();
//				geoText.setTextStyle(DefaultTextStyle.getDefaultGeoStyle(activeMapName).clone());
				geoText.getTextStyle().setFontName("");
				geoText.getTextStyle().setSizeFixed(false);
				// DEFAULT_FONT_PIXEL_HEIGHT 是一个经验值，使得不固定大小的时候，最后绘制到地图上的文本大小与输入的时候基本一致
				geoText.getTextStyle().setFontHeight(DEFAULT_FONT_PIXEL_HEIGHT * MapUtilities.pixelLength(formMap.getMapControl()));

				for (int i = 0; i < formMap.getMapControl().getMap().getTrackingLayer().getCount(); i++) {
					if (CreateAlongLineTextTracing.equals(formMap.getMapControl().getMap().getTrackingLayer().getTag(i))) {
						formMap.getMapControl().getMap().getTrackingLayer().get(i).dispose();
						formMap.getMapControl().getMap().getTrackingLayer().set(i, geoText);
						refreshTrackingLayer();
						return;
					}
				}
				formMap.getMapControl().getMap().getTrackingLayer().add(geoText, CreateAlongLineTextTracing);
				refreshTrackingLayer();
			}
		}

		private void refreshTrackingLayer() {
			TrackMode trackMode = formMap.getMapControl().getTrackMode();
			if (trackMode == TrackMode.TRACK) {
				formMap.getMapControl().getMap().refreshTrackingLayer();
			} else {
				formMap.getMapControl().setTrackMode(TrackMode.TRACK);
				formMap.getMapControl().getMap().refreshTrackingLayer();
				formMap.getMapControl().setTrackMode(trackMode);
			}
		}

		private void initLayout() {
			panelButton.setLayout(new GridBagLayout());
			panelButton.add(buttonOk,
					new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(99, 1));
			panelButton.add(buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST)
					.setWeight(1, 1));

			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			panel.add(labelDescribe,
					new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
			panel.add(textFieldText,
					new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
			panel.add(panelButton,
					new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
			this.setLayout(new GridBagLayout());
			this.add(panel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setInsets(10).setAnchor(GridBagConstraints.CENTER)
					.setWeight(1, 1));
		}

		private void initResources() {
			this.setTitle(MapEditorProperties.getString("String_AlongLineTitle"));
			labelDescribe.setText((MapEditorProperties.getString("String_AlongLineText")));
			buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
			buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
		}

		public String getText() {
			return textFieldText.getText();
		}

		@Override
		public void dispose() {
			removeTrackingObject();
			geoLine.dispose();
			super.dispose();
		}

		private void removeTrackingObject() {
			for (int i = formMap.getMapControl().getMap().getTrackingLayer().getCount() - 1; i >= 0; i--) {
				if (CreateAlongLineTextTracing.equals(formMap.getMapControl().getMap().getTrackingLayer().getTag(i))) {
					formMap.getMapControl().getMap().getTrackingLayer().remove(i);
				}
			}
			refreshTrackingLayer();
		}

	}
}
