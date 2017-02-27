package com.supermap.desktop.dialog;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.messagebus.NewMessageBus;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.ui.lbs.impl.WebHDFS;
import com.supermap.desktop.ui.lbs.params.*;
import com.supermap.desktop.ui.lbs.ui.JDialogHDFSFiles;
import com.supermap.ui.Action;
import com.supermap.ui.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by xie on 2017/1/11.
 */
public class JDialogHeatMap extends SmDialog {
	private JLabel labelCacheType;
	private JComboBox comboBoxCacheType;

	private JPanel panelBounds;
	private JLabel labelBoundsLeft;
	private JTextField textBoundsLeft;
	private JLabel labelBoundsBottom;
	private JTextField textBoundsBottom;
	private JLabel labelBoundsRight;
	private JTextField textBoundsRight;
	private JLabel labelBoundsTop;
	private JTextField textBoundsTop;
	private JButton buttonDrawBounds;
	private JButton buttonInputBrowser;

	private JLabel labelCacheLevel;
	private JTextField textFieldCacheLevel;
	private JLabel labelXYIndex;
	private JTextField textFieldXYIndex;
	private JLabel labelFileInputPath;
	private JTextField textFieldFileInputPath;
	private JLabel labelCacheName;
	private JTextField textFieldCacheName;
	private JLabel labelDatabaseType;
	private JComboBox comboBoxDatabaseType;
	private JLabel labelServiceAddress;
	private JTextField textFieldServiceAddress;
	private JLabel labelDatabase;
	private JTextField textFieldDatabase;
	private JLabel labelVersion;
	private JTextField textFieldVersion;
	private JButton buttonOK;
	private JButton buttonCancel;
	private ActionListener heatMapListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			qureyInfo();
		}
	};
	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			removeEvents();
			JDialogHeatMap.this.dispose();
		}
	};
	private ActionListener drawBoundsListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetBounds();
		}
	};
	private ActionListener inputBrowserListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonInputBrowser();
		}
	};

	public JDialogHeatMap(JFrame parent) {
		super(parent, true);
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initResources();
		registEvents();
	}

	private void initComponents() {
		this.labelCacheType = new JLabel();
		this.comboBoxCacheType = new JComboBox();
		this.comboBoxCacheType.addItem("Heatmap");
		this.buttonInputBrowser = new JButton();
		this.panelBounds = new JPanel();
		this.labelBoundsLeft = new JLabel();
		this.textBoundsLeft = new JTextField("-74.050");
		this.labelBoundsBottom = new JLabel();
		this.textBoundsBottom = new JTextField("40.650");
		this.labelBoundsRight = new JLabel();
		this.textBoundsRight = new JTextField("-73.850");
		this.labelBoundsTop = new JLabel();
		this.textBoundsTop = new JTextField("40.850");
		this.buttonDrawBounds = new JButton();
		if (Application.getActiveApplication().getActiveForm() == null
				|| !(Application.getActiveApplication().getActiveForm() instanceof IFormMap)) {
			buttonDrawBounds.setEnabled(false);
		}
		this.labelCacheLevel = new JLabel();
		this.textFieldCacheLevel = new JTextField("1");
		this.labelXYIndex = new JLabel();
		this.textFieldXYIndex = new JTextField("10,11");
		this.labelFileInputPath = new JLabel();
		this.textFieldFileInputPath = new JTextField(WebHDFS.getHDFSFilePath());
		this.labelCacheName = new JLabel();
		this.textFieldCacheName = new JTextField("test1_heat");
		this.labelDatabaseType = new JLabel();
		this.comboBoxDatabaseType = new JComboBox();
		this.comboBoxDatabaseType.addItem("MongoDB");
		this.labelServiceAddress = new JLabel();
		this.textFieldServiceAddress = new JTextField("192.168.15.245:27017");
		this.labelDatabase = new JLabel();
		this.textFieldDatabase = new JTextField("test");
		this.labelVersion = new JLabel();
		this.textFieldVersion = new JTextField("V1");
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.getRootPane().setDefaultButton(this.buttonOK);
		setComboBoxTheme(this.comboBoxCacheType);
		setComboBoxTheme(this.comboBoxDatabaseType);
		this.setSize(680, 510);
		this.setLocationRelativeTo(null);
	}

	private void setComboBoxTheme(JComboBox comboBox) {
		comboBox.setEditable(true);
		((JTextField) comboBox.getEditor().getEditorComponent()).setEditable(false);
	}

	private void initLayout() {
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));
		this.panelBounds.setLayout(new GridBagLayout());
		this.panelBounds.add(this.labelBoundsLeft, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsLeft, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsBottom, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsBottom, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsRight, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsRight, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsTop, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 10, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsTop, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 10, 0).setWeight(1, 0));
		this.panelBounds.add(this.buttonDrawBounds, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 5, 10, 10).setWeight(0, 0));


		this.setLayout(new GridBagLayout());
		this.add(this.labelFileInputPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldFileInputPath, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.add(this.buttonInputBrowser, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 5, 0, 10));
		this.add(this.labelCacheType, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.comboBoxCacheType, new GridBagConstraintsHelper(1, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.panelBounds, new GridBagConstraintsHelper(0, 2, 4, 4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(10, 10, 0, 10).setWeight(1, 0));
		this.add(this.labelXYIndex, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldXYIndex, new GridBagConstraintsHelper(1, 6, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelCacheLevel, new GridBagConstraintsHelper(0, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldCacheLevel, new GridBagConstraintsHelper(1, 7, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelCacheName, new GridBagConstraintsHelper(0, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldCacheName, new GridBagConstraintsHelper(1, 8, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelDatabaseType, new GridBagConstraintsHelper(0, 9, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.comboBoxDatabaseType, new GridBagConstraintsHelper(1, 9, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelServiceAddress, new GridBagConstraintsHelper(0, 10, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldServiceAddress, new GridBagConstraintsHelper(1, 10, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelDatabase, new GridBagConstraintsHelper(0, 11, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldDatabase, new GridBagConstraintsHelper(1, 11, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelVersion, new GridBagConstraintsHelper(0, 12, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 10, 0).setWeight(0, 0));
		this.add(this.textFieldVersion, new GridBagConstraintsHelper(1, 12, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 10, 10).setWeight(1, 0));

		this.add(new JPanel(), new GridBagConstraintsHelper(0, 13, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(0, 1));
		this.add(panelButton, new GridBagConstraintsHelper(0, 14, 4, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));

		this.panelBounds.setLayout(new GridBagLayout());
		this.panelBounds.add(this.labelBoundsLeft, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsLeft, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsBottom, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsBottom, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsRight, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsRight, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsTop, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 10, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsTop, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 10, 0).setWeight(1, 0));
		this.panelBounds.add(this.buttonDrawBounds, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 5, 10, 0).setWeight(0, 0));
	}

	private void initResources() {
		this.labelXYIndex.setText(LBSClientProperties.getString("String_XYIndex"));
		this.labelCacheType.setText(LBSClientProperties.getString("String_CacheType"));
		this.labelCacheLevel.setText(LBSClientProperties.getString("String_CacheLevel"));
		this.buttonDrawBounds.setText(LBSClientProperties.getString("String_DrawBounds"));
		this.buttonDrawBounds.setToolTipText(LBSClientProperties.getString("String_DrawBounds"));
		this.labelFileInputPath.setText(LBSClientProperties.getString(""));
		this.labelCacheName.setText(LBSClientProperties.getString("String_CacheName"));
		this.labelDatabaseType.setText(LBSClientProperties.getString("String_DatabaseType"));
		this.labelServiceAddress.setText(LBSClientProperties.getString("String_ServiceAddress"));
		this.labelDatabase.setText(LBSClientProperties.getString("String_Database"));
		this.labelVersion.setText(LBSClientProperties.getString("String_Version"));
		this.labelBoundsLeft.setText(LBSClientProperties.getString("String_Left"));
		this.labelBoundsBottom.setText(LBSClientProperties.getString("String_Bottom"));
		this.labelBoundsRight.setText(LBSClientProperties.getString("String_Right"));
		this.labelBoundsTop.setText(LBSClientProperties.getString("String_Top"));
		this.setTitle(LBSClientProperties.getString("String_HeatMap"));
		this.buttonInputBrowser.setText(LBSClientProperties.getString("String_Browser"));
		this.buttonInputBrowser.setToolTipText(LBSClientProperties.getString("String_Browser"));
		this.panelBounds.setBorder(new TitledBorder(null, LBSClientProperties.getString("String_CacheBounds"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
	}

	private void registEvents() {
		removeEvents();
		this.buttonOK.addActionListener(this.heatMapListener);
		this.buttonCancel.addActionListener(this.cancelListener);
		this.buttonDrawBounds.addActionListener(this.drawBoundsListener);
		this.buttonInputBrowser.addActionListener(this.inputBrowserListener);
	}

	private void buttonInputBrowser() {
		JDialogHDFSFiles hdfsFiles = new JDialogHDFSFiles();
		if (hdfsFiles.showDialog() == DialogResult.OK) {
			textFieldFileInputPath.setText(WebHDFS.getResultHDFSFilePath());
		}
	}

	private transient MouseListener controlMouseListener = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			MapControl control = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			if (e.getButton() == MouseEvent.BUTTON3) {
				control.removeMouseListener(this);
				exitEdit();
			}
		}
	};

	private void exitEdit() {
		MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		activeMapControl.setAction(Action.SELECT2);
		activeMapControl.setTrackMode(TrackMode.EDIT);

		this.setVisible(true);
	}

	private transient TrackedListener trackedListener = new TrackedListener() {

		@Override
		public void tracked(TrackedEvent arg0) {
			abstractTracked(arg0);
		}
	};

	private void abstractTracked(TrackedEvent arg0) {
		if (arg0.getGeometry() != null) {

			Rectangle2D rectangle = arg0.getGeometry().getBounds().clone();
			this.textBoundsLeft.setText(String.format("%f", rectangle.getLeft()));
			this.textBoundsBottom.setText(String.format("%f", rectangle.getBottom()));
			this.textBoundsRight.setText(String.format("%f", rectangle.getRight()));
			this.textBoundsTop.setText(String.format("%f", rectangle.getTop()));

			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().removeTrackedListener(trackedListener);
			exitEdit();
		} else {
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().addMouseListener(controlMouseListener);
		}
	}

	private void resetBounds() {
		final MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		activeMapControl.setTrackMode(TrackMode.TRACK);
		activeMapControl.setAction(com.supermap.ui.Action.CREATERECTANGLE);
		activeMapControl.addMouseListener(controlMouseListener);
		activeMapControl.addTrackedListener(trackedListener);

		this.setVisible(false);
	}

	private void qureyInfo() {
		IServerService service = new IServerServiceImpl();
		BuildCacheJobSetting setting = new BuildCacheJobSetting();

		FileInputDataSetting input = new FileInputDataSetting();
		input.filePath = textFieldFileInputPath.getText();

		MongoDBOutputsetting output = new MongoDBOutputsetting();
		output.cacheName = textFieldCacheName.getText();
		output.cacheType = (String) comboBoxDatabaseType.getSelectedItem();
		output.serverAdresses[0] = textFieldServiceAddress.getText();
		output.database = textFieldDatabase.getText();
		output.version = textFieldVersion.getText();

		BuildCacheDrawingSetting drawing = new BuildCacheDrawingSetting();
		String bounds = textBoundsLeft.getText() + "," + textBoundsBottom.getText() + "," + textBoundsRight.getText() + "," + textBoundsTop.getText();
		drawing.imageType = (String) comboBoxCacheType.getSelectedItem();
		drawing.bounds = bounds;
		drawing.level = textFieldCacheLevel.getText();
		drawing.xyIndex = textFieldXYIndex.getText();
		setting.input = input;
		setting.output = output;
		setting.drawing = drawing;
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		JobResultResponse response = service.query(setting);
		if (null != response) {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			NewMessageBus.producer(response);
			dispose();
		}
	}

	private void removeEvents() {
		this.buttonOK.removeActionListener(this.heatMapListener);
		this.buttonCancel.removeActionListener(this.cancelListener);
		this.buttonDrawBounds.removeActionListener(this.drawBoundsListener);
		this.buttonInputBrowser.removeActionListener(this.inputBrowserListener);
	}
}
