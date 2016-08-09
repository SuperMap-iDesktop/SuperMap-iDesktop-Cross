package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.data.Enum;
import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.comboBox.JSearchComboBox;
import com.supermap.desktop.ui.controls.comboBox.SearchItemValueGetter;
import com.supermap.desktop.utilities.EnumComparator;
import com.supermap.desktop.utilities.PrjCoordSysTypeUtilities;
import com.supermap.desktop.utilities.StringUtilties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author XiaJT
 */
public class JDialogUserDefinePrjProjection extends SmDialog {
	private final ActionListener buttonOkListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dialogResult = DialogResult.OK;
			dispose();
		}
	};
	private final ActionListener buttonCalcelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dialogResult = DialogResult.CANCEL;
			dispose();
		}
	};
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JLabel labelName = new JLabel();
	private JSearchComboBox<PrjCoordSysType> comboBoxName = new JSearchComboBox<>();

	// 投影坐标系
	private JPanel panelPrjCoordSys = new JPanel();
	private JLabel labelCoordType = new JLabel();
	private JSearchComboBox<ProjectionType> comboBoxCoordType = new JSearchComboBox<>();

	private JLabel labelCoordSysUnit = new JLabel();
	private JSearchComboBox<Unit> comboBoxCoordSysUnit = new JSearchComboBox<>();
	private Unit[] units = new Unit[]{Unit.KILOMETER, Unit.METER, Unit.DECIMETER, Unit.CENTIMETER, Unit.MILIMETER, Unit.MILE, Unit.YARD, Unit.FOOT, Unit.INCH};
	private DecimalFormat df = new DecimalFormat("0.######################");

	// 投影参数
	private JPanel panelPrjCoordSysParameters = new JPanel();

	// 按钮
	private JPanel panelButtons = new JPanel();
	private SmButton buttonOK = new SmButton();
	private SmButton buttonCancel = new SmButton();

	private JPanelGeoCoordSys panelGeoCoordSys = new JPanelGeoCoordSys();

	private JLabel labelParameterFormat = new JLabel();
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton radioButtonAngle = new JRadioButton();
	private JRadioButton radioButtonAMS = new JRadioButton();

	// 中央经线
	private JLabel labelCentralMeridian = new JLabel();
	private JPanelFormat panelCentralMeridian = new JPanelFormat();
	// 水平偏移
	private JLabel labelFalseEasting = new JLabel();
	private SmTextFieldLegit textFieldFalseEasting = new SmTextFieldLegit();

	// 原点纬线
	private JLabel labelCentralParallel = new JLabel();
	private JPanelFormat panelCentralParallel = new JPanelFormat();
	// 垂直偏移
	private JLabel labelFalseNorthing = new JLabel();
	private SmTextFieldLegit textFieldFalseNorthing = new SmTextFieldLegit();

	// 比例因子
	private JLabel labelScaleFactor = new JLabel();
	private SmTextFieldLegit textFieldScaleFactor = new SmTextFieldLegit();

	// 第一标准纬线
	private JLabel labelStandardParallel1 = new JLabel();
	private JPanelFormat panelStandardParallel1 = new JPanelFormat();
	// 第二标准纬线
	private JLabel labelStandardParallel2 = new JLabel();
	private JPanelFormat panelStandardParallel2 = new JPanelFormat();

	// 第一点的经度
	private JLabel labelFirstPointLongitude = new JLabel();
	private JPanelFormat panelFirstPointLongitude = new JPanelFormat();

	// 第2点的经度
	private JLabel labelSecondPointLongitude = new JLabel();
	private JPanelFormat panelSecondPointLongitude = new JPanelFormat();

	// 方位角
	private JLabel labelAzimuth = new JLabel();
	private JPanelFormat panelAzimuth = new JPanelFormat();
	private final ItemListener radioAMSListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			boolean mode = JPanelFormat.angle;
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mode = JPanelFormat.ANGLE_M_S;
			}
			panelCentralMeridian.setMode(mode);
			panelCentralParallel.setMode(mode);
			panelStandardParallel1.setMode(mode);
			panelStandardParallel2.setMode(mode);
			panelFirstPointLongitude.setMode(mode);
			panelSecondPointLongitude.setMode(mode);
			panelAzimuth.setMode(mode);
			repaint();
		}
	};

	private PrjCoordSys prjCoordSys;
	private boolean lock = false;
	private ISmTextFieldLegit fieldLegit;
	private Dimension labelPreferredSize = new Dimension(20, 23);
	private ItemListener comboBoxUnitListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				prjCoordSys.setCoordUnit((Unit) comboBoxCoordSysUnit.getSelectedItem());
			}
		}
	};
	private ItemListener comboBoxNameListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (lock) {
				return;
			}
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Object selectedItem = comboBoxName.getSelectedItem();
				if (selectedItem != null && !(selectedItem instanceof String) && selectedItem != PrjCoordSysType.PCS_USER_DEFINED) {
					String describe = PrjCoordSysTypeUtilities.getDescribe(((PrjCoordSysType) selectedItem).name());
					prjCoordSys.setType((PrjCoordSysType) selectedItem);
					lock = true;
					comboBoxCoordType.setSelectedItem(prjCoordSys.getProjection().getType());
					panelGeoCoordSys.setGeoCoordSys(prjCoordSys.getGeoCoordSys());
					comboBoxCoordSysUnit.setSelectedItem(prjCoordSys.getCoordUnit());
					resetProjectionTypeValues();
					prjCoordSys.setType(PrjCoordSysType.PCS_USER_DEFINED);
					prjCoordSys.setName(describe);
					comboBoxName.setSelectedItem(describe);
					lock = false;
				} else {
					if (StringUtilties.isNullOrEmptyString(selectedItem)) {
						return;
					}
					prjCoordSys.setName(selectedItem instanceof String ? (String) selectedItem : ((PrjCoordSysType) selectedItem).name());
				}
			}
		}
	};
	private ItemListener comboBoxCoordTypeListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				prjCoordSys.getProjection().setType((ProjectionType) comboBoxCoordType.getSelectedItem());
			}
		}
	};

	public JDialogUserDefinePrjProjection() {
		super();
		this.setTitle(ControlsProperties.getString("String_UserDefined_PrjCoordSys"));
		prjCoordSys = new PrjCoordSys();
		componentList.add(buttonOK);
		componentList.add(buttonCancel);
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initResources();
		addListeners();
		initComponentState();
	}

	private void initComponents() {

		setSize(500, 550);
		setLocationRelativeTo(null);
		textFieldScaleFactor.setPreferredSize(new Dimension(50, 23));
		textFieldFalseNorthing.setPreferredSize(new Dimension(50, 23));
		textFieldFalseEasting.setPreferredSize(new Dimension(50, 23));
		buttonGroup.add(radioButtonAMS);
		buttonGroup.add(radioButtonAngle);

		// region 名称
		Enum[] enums = Enum.getEnums(PrjCoordSysType.class);
		SearchItemValueGetter<Enum> searchItemValueGetter = PrjCoordSysSettingsUtilties.getSearchItemValueGetter();
		comboBoxName.setSearchItemValueGetter(searchItemValueGetter);
		Arrays.sort(enums, 0, enums.length, new EnumComparator());
		for (Enum anEnum : enums) {
			if (anEnum instanceof PrjCoordSysType && anEnum != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE && anEnum != PrjCoordSysType.PCS_NON_EARTH && anEnum != PrjCoordSysType.PCS_USER_DEFINED) {
				comboBoxName.addItem((PrjCoordSysType) anEnum);
			}
		}

		comboBoxName.setRenderer(new MyEnumCellRender(comboBoxName));
		comboBoxName.setEditable(true);
		// endregion

		// region 投影方式
		Enum[] enums1 = Enum.getEnums(ProjectionType.class);
		comboBoxCoordType.setSearchItemValueGetter(searchItemValueGetter);
		Arrays.sort(enums1, 0, enums1.length, new EnumComparator());
		for (Enum anEnum : enums1) {
			if (anEnum instanceof ProjectionType && anEnum != PrjCoordSysType.PCS_USER_DEFINED) {
				comboBoxCoordType.addItem((ProjectionType) anEnum);
			}
		}

		comboBoxCoordType.setRenderer(new MyEnumCellRender(comboBoxCoordType));

		// endregion

		// region 单位
		comboBoxCoordSysUnit.setSearchItemValueGetter(searchItemValueGetter);
		for (Unit unit : units) {
			comboBoxCoordSysUnit.addItem(unit);
		}
		comboBoxCoordSysUnit.setRenderer(new ListCellRenderer<Unit>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Unit> list, Unit value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel jLabel = new JLabel();
				jLabel.setOpaque(true);
				jLabel.setPreferredSize(labelPreferredSize);
				jLabel.setText(LengthUnit.convertForm(value).toString());
				if (isSelected) {
					jLabel.setBackground(list.getSelectionBackground());
				} else {
					jLabel.setBackground(list.getBackground());
				}
				return jLabel;
			}
		});
		comboBoxCoordSysUnit.setMaximumRowCount(units.length);
		// endregion

		fieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilties.isNullOrEmpty(textFieldValue) || textFieldValue.contains("d")) {
					return false;
				}
				try {
					Double aDouble = Double.valueOf(textFieldValue);
					if (aDouble < -100000000000d || aDouble > 100000000000d) {
						return false;
					}
				} catch (Exception e) {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		};
		textFieldFalseNorthing.setSmTextFieldLegit(fieldLegit);
		textFieldFalseEasting.setSmTextFieldLegit(fieldLegit);
		textFieldScaleFactor.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilties.isNullOrEmpty(textFieldValue) || textFieldValue.contains("d")) {
					return false;
				}
				try {
					Double aDouble = Double.valueOf(textFieldValue);
					if (aDouble < 0d || aDouble > 1d) {
						return false;
					}
				} catch (Exception e) {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
	}

	// region 初始化布局
	private void initLayout() {
		initTabbedPane();
		initPanelButtons();
		this.setLayout(new GridBagLayout());
		this.add(labelName, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0)
				.setWeight(0, 0));
		this.add(comboBoxName,
				new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 0, 10)
						.setWeight(1, 0));
		this.add(tabbedPane,
				new GridBagConstraintsHelper(0, 1, 2, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10)
						.setWeight(1, 1));
		this.add(panelButtons,
				new GridBagConstraintsHelper(0, 2, 2, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 10, 10)
						.setWeight(1, 0));
	}

	private void initTabbedPane() {
		initPanelPrjcoordSys();
		initPanelPrjCoordSysParameters();
		tabbedPane.add(ControlsProperties.getString("String_PrjCoorSys"), panelPrjCoordSys);
		tabbedPane.add(ControlsProperties.getString("String_PrjCoordSysParameters"), panelPrjCoordSysParameters);
	}

	/**
	 * 初始化投影坐标系系统
	 */
	private void initPanelPrjcoordSys() {
		panelGeoCoordSys.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GeoCoordSys")));
		panelPrjCoordSys.setLayout(new GridBagLayout());
		panelPrjCoordSys.add(labelCoordType, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setIpad(36, 0).setAnchor(GridBagConstraints.WEST)
				.setInsets(10, 10, 0, 0));
		panelPrjCoordSys.add(
				comboBoxCoordType,
				new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER)
						.setInsets(10, 5, 0, 10));

		panelPrjCoordSys.add(labelCoordSysUnit,
				new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelPrjCoordSys.add(
				comboBoxCoordSysUnit,
				new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER)
						.setInsets(10, 5, 0, 10));

		panelPrjCoordSys.add(
				panelGeoCoordSys,
				new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.NORTH)
						.setInsets(5, 10, 0, 10));
		panelPrjCoordSys.add(
				new JPanel(),
				new GridBagConstraintsHelper(0, 3, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
						.setInsets(5, 10, 10, 10));
	}

	private void initPanelPrjCoordSysParameters() {
		panelPrjCoordSysParameters.setLayout(new GridBagLayout());
		panelPrjCoordSysParameters.add(labelParameterFormat, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST)
				.setInsets(10, 10, 0, 0));
		panelPrjCoordSysParameters.add(radioButtonAngle, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0)
				.setWeight(1, 0));
		panelPrjCoordSysParameters.add(radioButtonAMS, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0));
		panelPrjCoordSysParameters.add(new Panel(), new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10)
				.setWeight(1, 0));

		panelPrjCoordSysParameters.add(labelCentralMeridian, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST)
				.setInsets(10, 10, 0, 0));
		panelPrjCoordSysParameters.add(panelCentralMeridian, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0)
				.setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelPrjCoordSysParameters.add(labelFalseEasting, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0));
		panelPrjCoordSysParameters.add(
				textFieldFalseEasting,
				new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10).setWeight(1, 0)
						.setFill(GridBagConstraints.HORIZONTAL));

		panelPrjCoordSysParameters.add(labelCentralParallel, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST)
				.setInsets(10, 10, 0, 0));
		panelPrjCoordSysParameters.add(panelCentralParallel, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0)
				.setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelPrjCoordSysParameters.add(labelFalseNorthing, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0));
		panelPrjCoordSysParameters.add(
				textFieldFalseNorthing,
				new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10).setWeight(1, 0)
						.setFill(GridBagConstraints.HORIZONTAL));

		panelPrjCoordSysParameters.add(new JPanel(), new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0)
				.setWeight(1, 0));
		panelPrjCoordSysParameters.add(labelScaleFactor, new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0));
		panelPrjCoordSysParameters.add(textFieldScaleFactor, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST)
				.setInsets(10, 5, 0, 10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));

		panelPrjCoordSysParameters.add(labelStandardParallel1,
				new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelPrjCoordSysParameters.add(
				panelStandardParallel1,
				new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0).setWeight(1, 0)
						.setFill(GridBagConstraints.HORIZONTAL));
		panelPrjCoordSysParameters.add(new Panel(), new GridBagConstraintsHelper(3, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10)
				.setWeight(1, 0));

		panelPrjCoordSysParameters.add(labelStandardParallel2,
				new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelPrjCoordSysParameters.add(
				panelStandardParallel2,
				new GridBagConstraintsHelper(1, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0).setWeight(1, 0)
						.setFill(GridBagConstraints.HORIZONTAL));
		panelPrjCoordSysParameters.add(new Panel(), new GridBagConstraintsHelper(3, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10)
				.setWeight(1, 0));

		panelPrjCoordSysParameters.add(labelFirstPointLongitude,
				new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelPrjCoordSysParameters.add(
				panelFirstPointLongitude,
				new GridBagConstraintsHelper(1, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0).setWeight(1, 0)
						.setFill(GridBagConstraints.HORIZONTAL));
		panelPrjCoordSysParameters.add(new Panel(), new GridBagConstraintsHelper(3, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10)
				.setWeight(1, 0));

		panelPrjCoordSysParameters.add(labelSecondPointLongitude,
				new GridBagConstraintsHelper(0, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelPrjCoordSysParameters.add(
				panelSecondPointLongitude,
				new GridBagConstraintsHelper(1, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0).setWeight(1, 0)
						.setFill(GridBagConstraints.HORIZONTAL));
		panelPrjCoordSysParameters.add(new Panel(), new GridBagConstraintsHelper(3, 7, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10)
				.setWeight(1, 0));

		panelPrjCoordSysParameters.add(labelAzimuth, new GridBagConstraintsHelper(0, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		panelPrjCoordSysParameters.add(panelAzimuth, new GridBagConstraintsHelper(1, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 0)
				.setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelPrjCoordSysParameters.add(new Panel(), new GridBagConstraintsHelper(3, 8, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10)
				.setWeight(1, 0));

		panelPrjCoordSysParameters.add(new JPanel(),
				new GridBagConstraintsHelper(0, 9, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(2, 1));
	}

	private void initPanelButtons() {
		panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 0, 5));
		panelButtons.add(buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST));
	}

	// endregion

	private void initResources() {
		labelName.setText(ControlsProperties.getString("String_Label_Name"));
		labelCoordType.setText(ControlsProperties.getString("String_ProjectionType"));
		labelCoordSysUnit.setText(ControlsProperties.getString("String_CoorSysUnit"));

		labelParameterFormat.setText(ControlsProperties.getString("String_ParameterFormat"));
		radioButtonAngle.setText(ControlsProperties.getString("String_angle"));
		radioButtonAMS.setText(ControlsProperties.getString("String_DMSFormat"));
		labelCentralMeridian.setText(ControlsProperties.getString("String_CentralMeridian"));
		labelFalseEasting.setText(ControlsProperties.getString("String_FalseEasting"));
		labelCentralParallel.setText(ControlsProperties.getString("String_CentralParallel"));
		labelFalseNorthing.setText(ControlsProperties.getString("String_FalseNorthing"));

		labelScaleFactor.setText(ControlsProperties.getString("String_ScaleFactor"));
		labelStandardParallel1.setText(ControlsProperties.getString("String_StandardParallel1"));
		labelStandardParallel2.setText(ControlsProperties.getString("String_StandardParallel2"));
		labelFirstPointLongitude.setText(ControlsProperties.getString("String_FirstPointLongitude"));
		labelSecondPointLongitude.setText(ControlsProperties.getString("String_SecondPointLongitude"));
		labelAzimuth.setText(ControlsProperties.getString("String_Label_Azimuth"));

		buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void addListeners() {
		buttonOK.addActionListener(buttonOkListener);
		buttonCancel.addActionListener(buttonCalcelListener);
		radioButtonAMS.addItemListener(radioAMSListener);
		comboBoxName.addItemListener(comboBoxNameListener);
		comboBoxCoordType.addItemListener(comboBoxCoordTypeListener);
		comboBoxCoordSysUnit.addItemListener(comboBoxUnitListener);
	}

	private void removeListeners() {
		buttonOK.removeActionListener(buttonOkListener);
		buttonCancel.removeActionListener(buttonCalcelListener);
		radioButtonAMS.removeItemListener(radioAMSListener);
		comboBoxName.removeItemListener(comboBoxNameListener);
		comboBoxCoordType.removeItemListener(comboBoxCoordTypeListener);
		comboBoxCoordSysUnit.removeItemListener(comboBoxUnitListener);
	}

	private void resetProjectionTypeValues() {
		panelCentralMeridian.setValue(prjCoordSys.getPrjParameter().getCentralMeridian());
		panelCentralParallel.setValue(prjCoordSys.getPrjParameter().getCentralParallel());
		panelStandardParallel1.setValue(prjCoordSys.getPrjParameter().getStandardParallel1());
		panelStandardParallel2.setValue(prjCoordSys.getPrjParameter().getStandardParallel2());
		panelFirstPointLongitude.setValue(prjCoordSys.getPrjParameter().getFirstPointLongitude());
		panelSecondPointLongitude.setValue(prjCoordSys.getPrjParameter().getSecondPointLongitude());
		panelAzimuth.setValue(prjCoordSys.getPrjParameter().getAzimuth());
		textFieldFalseEasting.setText(df.format(prjCoordSys.getPrjParameter().getFalseEasting()));
		textFieldFalseNorthing.setText(df.format(prjCoordSys.getPrjParameter().getFalseNorthing()));
		textFieldScaleFactor.setText(df.format(prjCoordSys.getPrjParameter().getScaleFactor()));
	}

	private void initComponentState() {
		radioButtonAngle.setSelected(true);
	}

	public PrjCoordSys getPrjCoordSys() {
		prjCoordSys.setGeoCoordSys(panelGeoCoordSys.getGeoCoordSys());
		prjCoordSys.getPrjParameter().setCentralMeridian(panelCentralMeridian.getValue());
		prjCoordSys.getPrjParameter().setCentralParallel(panelCentralParallel.getValue());
		prjCoordSys.getPrjParameter().setStandardParallel1(panelStandardParallel1.getValue());
		prjCoordSys.getPrjParameter().setStandardParallel2(panelStandardParallel2.getValue());
		prjCoordSys.getPrjParameter().setFirstPointLongitude(panelFirstPointLongitude.getValue());
		prjCoordSys.getPrjParameter().setSecondPointLongitude(panelSecondPointLongitude.getValue());
		prjCoordSys.getPrjParameter().setAzimuth(panelAzimuth.getValue());
		prjCoordSys.getPrjParameter().setFalseEasting(Double.valueOf(textFieldFalseEasting.getText()));
		prjCoordSys.getPrjParameter().setFalseNorthing(Double.valueOf(textFieldFalseNorthing.getText()));
		prjCoordSys.getPrjParameter().setScaleFactor(Double.valueOf(textFieldScaleFactor.getText()));

		return prjCoordSys.clone();
	}

	public void setPrjCoordSys(PrjCoordSys prjCoordSys) {
		if (this.prjCoordSys != null) {
			this.prjCoordSys.dispose();
		}
		this.prjCoordSys = prjCoordSys.clone();
		this.prjCoordSys.setType(PrjCoordSysType.PCS_USER_DEFINED);
		this.prjCoordSys.setName(prjCoordSys.getName());
		panelGeoCoordSys.setGeoCoordSys(this.prjCoordSys.getGeoCoordSys());
		lock = true;
		comboBoxName.setSelectedItem(prjCoordSys.getName());
		comboBoxCoordSysUnit.setSelectedItem(prjCoordSys.getCoordUnit());
		comboBoxCoordType.setSelectedItem(prjCoordSys.getProjection().getType());
		resetProjectionTypeValues();
		lock = false;
	}

	public void clean() {
		panelGeoCoordSys.dispose();
		removeListeners();
		if (this.prjCoordSys != null) {
			this.prjCoordSys.dispose();
		}
	}

}
