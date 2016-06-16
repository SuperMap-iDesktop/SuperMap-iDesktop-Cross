package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.data.Enum;
import com.supermap.data.GeoCoordSys;
import com.supermap.data.GeoCoordSysType;
import com.supermap.data.GeoDatumType;
import com.supermap.data.GeoPrimeMeridianType;
import com.supermap.data.GeoSpheroidType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.comboBox.JSearchComboBox;
import com.supermap.desktop.ui.controls.comboBox.SearchItemValueGetter;
import com.supermap.desktop.utilties.EnumComparator;
import com.supermap.desktop.utilties.PrjCoordSysTypeUtilties;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

/**
 * @author XiaJT
 */
public class JPanelGeoCoordSys extends JPanel {

	private JLabel labelType = new JLabel();
	private JSearchComboBox<GeoCoordSysType> comboBoxType = new JSearchComboBox<>();

	// 大地参考系
	private JPanel panelGeoDatum = new JPanel();
	private JLabel labelGeoDatumType = new JLabel();
	private JSearchComboBox<GeoDatumType> comboBoxGeoDatumType = new JSearchComboBox<>();

	// 椭球参数
	private JPanel panelGeoSpheroid = new JPanel();
	private JLabel labelGeoSpheroidType = new JLabel();
	private JSearchComboBox<GeoSpheroidType> comboBoxGeoSpheroidType = new JSearchComboBox<>();
	private JLabel labelAxis = new JLabel();
	private SmTextFieldLegit textFieldAxis = new SmTextFieldLegit();
	private JLabel labelFlatten = new JLabel();
	private SmTextFieldLegit textFieldFlatten = new SmTextFieldLegit();

	// 中央经线
	private JPanel panelCentralMeridian = new JPanel();
	private JLabel labelCentralMeridianType = new JLabel();
	private JSearchComboBox<GeoPrimeMeridianType> comboBoxCentralMeridianType = new JSearchComboBox<>();
	private JLabel labelLongitude = new JLabel();
	private SmTextFieldLegit textFieldLongitude = new SmTextFieldLegit();

	private GeoCoordSys geoCoordSys = new GeoCoordSys();
	// 加锁防止事件循环触发
	private boolean lock = false;
	private boolean lockGeo = false;
	private boolean lockAxis = false;
	private boolean lockCenter = false;
	private ListCellRenderer<Enum> renderer;

	public JPanelGeoCoordSys() {
		initComponents();
		addListeners();
		initLayout();
		initResources();
		initComponentStates();
	}

	private void initComponents() {
		//region 类型
		SearchItemValueGetter<Enum> searchItemValueGetter = PrjCoordSysSettingsUtilties.getSearchItemValueGetter();
		comboBoxType.setSearchItemValueGetter(searchItemValueGetter);
		Enum[] enums = Enum.getEnums(GeoCoordSysType.class);

		Arrays.sort(enums, 0, enums.length, new EnumComparator());
		for (Enum anEnum : enums) {
			if (anEnum instanceof GeoCoordSysType) {
				comboBoxType.addItem((GeoCoordSysType) anEnum);
			}
		}
		renderer = PrjCoordSysSettingsUtilties.getEnumComboBoxItemRender();
		comboBoxType.setRenderer(renderer);
		//endregion

		comboBoxType.setEditable(true);
		comboBoxGeoDatumType.setEditable(true);
		comboBoxGeoSpheroidType.setEditable(true);
		comboBoxCentralMeridianType.setEditable(true);

		//region 大地参考系类型
		Enum[] enumsGeoDatum = Enum.getEnums(GeoDatumType.class);
		comboBoxGeoDatumType.setSearchItemValueGetter(searchItemValueGetter);
		Arrays.sort(enumsGeoDatum, 0, enumsGeoDatum.length, new EnumComparator());

		for (Enum anEnum : enumsGeoDatum) {
			if (anEnum instanceof GeoDatumType) {
				comboBoxGeoDatumType.addItem((GeoDatumType) anEnum);
			}
		}
		comboBoxGeoDatumType.setRenderer(renderer);
		//endregion

		//region 椭球参数类型
		Enum[] enumsGeoSpheroid = Enum.getEnums(GeoSpheroidType.class);
		comboBoxGeoSpheroidType.setSearchItemValueGetter(searchItemValueGetter);
		Arrays.sort(enumsGeoSpheroid, 0, enumsGeoSpheroid.length, new EnumComparator());
		for (Enum anEnum : enumsGeoSpheroid) {
			if (anEnum instanceof GeoSpheroidType) {
				comboBoxGeoSpheroidType.addItem((GeoSpheroidType) anEnum);
			}
		}
		comboBoxGeoSpheroidType.setRenderer(renderer);
		//endregion

		//region 赤道半径
		textFieldAxis.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilties.isNullOrEmpty(textFieldValue) || textFieldValue.contains("d")) {
					return false;
				}
				try {
					double value = Double.valueOf(textFieldValue);
					return axisValueChanged(value);
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		//endregion

		//region 扁率
		textFieldFlatten.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilties.isNullOrEmpty(textFieldValue) || textFieldValue.contains("d")) {
					return false;
				}
				try {
					double value = Double.valueOf(textFieldValue);
					return flattenValueChanged(value);
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		//endregion
		//region 中央经线
		Enum[] enumsCenter = Enum.getEnums(GeoPrimeMeridianType.class);
		comboBoxCentralMeridianType.setSearchItemValueGetter(searchItemValueGetter);
		for (Enum anEnum : enumsCenter) {
			if (anEnum instanceof GeoPrimeMeridianType)
				comboBoxCentralMeridianType.addItem((GeoPrimeMeridianType) anEnum);
		}
		comboBoxCentralMeridianType.setRenderer(renderer);
		//endregion
		//region 经度
		textFieldLongitude.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilties.isNullOrEmpty(textFieldValue) || textFieldValue.contains("d")) {
					return false;
				}
				try {
					double value = Double.valueOf(textFieldValue);
					return longitudeValueChanged(value);
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		//endregion
	}

	private boolean longitudeValueChanged(double value) {
		if (value < -180 || value > 180) {
			return false;
		}
		if (!textFieldLongitude.getText().equals(textFieldLongitude.getBackUpValue())) {
			if (geoCoordSys.getGeoPrimeMeridian().getType() != GeoPrimeMeridianType.PRIMEMERIDIAN_USER_DEFINED) {
				String name = geoCoordSys.getGeoPrimeMeridian().getName();
				geoCoordSys.getGeoPrimeMeridian().setType(GeoPrimeMeridianType.PRIMEMERIDIAN_USER_DEFINED);
				geoCoordSys.getGeoPrimeMeridian().setName(name);
			}
			geoCoordSys.getGeoPrimeMeridian().setLongitudeValue(value);

		}
		return true;
	}

	private boolean flattenValueChanged(double value) {
		if (value < 0 || value > 1) {
			return false;
		}
		if (!textFieldFlatten.getText().equals(textFieldFlatten.getBackUpValue())) {
			if (geoCoordSys.getGeoDatum().getGeoSpheroid().getType() != GeoSpheroidType.SPHEROID_USER_DEFINED) {
				String name = geoCoordSys.getGeoDatum().getGeoSpheroid().getName();
				geoCoordSys.getGeoDatum().getGeoSpheroid().setType(GeoSpheroidType.SPHEROID_USER_DEFINED);
				geoCoordSys.getGeoDatum().getGeoSpheroid().setName(name);
			}
			geoCoordSys.getGeoDatum().getGeoSpheroid().setFlatten(value);
		}
		return true;
	}

	private boolean axisValueChanged(double value) {
		if (value < 5000000 || value > 10000000) {
			return false;
		}
		if (!textFieldAxis.getText().equals(textFieldAxis.getBackUpValue())) {
//			if (!lockAxis) {
//				comboBoxGeoSpheroidType.setSelectedItem(GeoSpheroidType.SPHEROID_USER_DEFINED);
//			}
			if (geoCoordSys.getGeoDatum().getGeoSpheroid().getType() != GeoSpheroidType.SPHEROID_USER_DEFINED) {
				String name = geoCoordSys.getGeoDatum().getGeoSpheroid().getName();
				geoCoordSys.getGeoDatum().getGeoSpheroid().setType(GeoSpheroidType.SPHEROID_USER_DEFINED);
				geoCoordSys.getGeoDatum().getGeoSpheroid().setName(name);
			}
			geoCoordSys.getGeoDatum().getGeoSpheroid().setAxis(value);

		}
		return true;
	}

	private void addListeners() {
		comboBoxType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && comboBoxType.getSelectedItem() != null) {
					if (lock) {
						return;
					}
					Object selectedItem = comboBoxType.getSelectedItem();
					if (selectedItem instanceof GeoCoordSysType) {
						geoCoordSys.setType((GeoCoordSysType) selectedItem);
						lock = true;
						comboBoxGeoDatumType.setSelectedItem(geoCoordSys.getGeoDatum().getType());
						comboBoxCentralMeridianType.setSelectedItem(geoCoordSys.getGeoPrimeMeridian().getType());
						comboBoxType.setSelectedItem(PrjCoordSysTypeUtilties.getDescribe(((GeoCoordSysType) selectedItem).name()));
						geoCoordSys.setType(GeoCoordSysType.GCS_USER_DEFINE);
						geoCoordSys.setName(PrjCoordSysTypeUtilties.getDescribe(((GeoCoordSysType) selectedItem).name()));
						lock = false;
					} else {
						if (StringUtilties.isNullOrEmptyString(selectedItem)) {
							return;
						}
						if (selectedItem instanceof String) {
							geoCoordSys.setType(GeoCoordSysType.GCS_USER_DEFINE);
							geoCoordSys.setName((String) selectedItem);
						}
					}
					firePropertyChange("GeoCoordSysType", "", "");
				}
			}
		});
		comboBoxGeoDatumType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (lockGeo) {
					return;
				}
				Object selectedItem = comboBoxGeoDatumType.getSelectedItem();
				if (e.getStateChange() == ItemEvent.SELECTED && selectedItem != null) {

					if (selectedItem instanceof GeoDatumType) {
						if (geoCoordSys.getType() != GeoCoordSysType.GCS_USER_DEFINE) {
							geoCoordSys.setType(GeoCoordSysType.GCS_USER_DEFINE);
						}
						geoCoordSys.getGeoDatum().setType((GeoDatumType) selectedItem);
						lockGeo = true;
						comboBoxGeoSpheroidType.setSelectedItem(geoCoordSys.getGeoDatum().getGeoSpheroid().getType());
						comboBoxGeoDatumType.setSelectedItem(PrjCoordSysTypeUtilties.getDescribe(((GeoDatumType) selectedItem).name()));
						geoCoordSys.getGeoDatum().setType(GeoDatumType.DATUM_USER_DEFINED);
						geoCoordSys.getGeoDatum().setName(PrjCoordSysTypeUtilties.getDescribe(((GeoDatumType) selectedItem).name()));
						lockGeo = false;
					} else {
						if (StringUtilties.isNullOrEmptyString(selectedItem)) {
							return;
						}
						geoCoordSys.getGeoDatum().setType(GeoDatumType.DATUM_USER_DEFINED);
						if (selectedItem instanceof String) {
							geoCoordSys.setName((String) selectedItem);
						}
					}
				}
			}
		});

		comboBoxGeoSpheroidType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (lockAxis) {
					return;
				}
				Object selectedItem = comboBoxGeoSpheroidType.getSelectedItem();
				if (e.getStateChange() == ItemEvent.SELECTED && selectedItem != null) {
					if (selectedItem instanceof GeoSpheroidType) {
						geoCoordSys.getGeoDatum().getGeoSpheroid().setType((GeoSpheroidType) selectedItem);
						lockAxis = true;
						textFieldAxis.setText(String.valueOf(geoCoordSys.getGeoDatum().getGeoSpheroid().getAxis()));
						textFieldFlatten.setText(String.valueOf(geoCoordSys.getGeoDatum().getGeoSpheroid().getFlatten()));
						comboBoxGeoSpheroidType.setSelectedItem(PrjCoordSysTypeUtilties.getDescribe(((GeoSpheroidType) selectedItem).name()));
						geoCoordSys.getGeoDatum().getGeoSpheroid().setType(GeoSpheroidType.SPHEROID_USER_DEFINED);
						geoCoordSys.getGeoDatum().getGeoSpheroid().setName(PrjCoordSysTypeUtilties.getDescribe(((GeoSpheroidType) selectedItem).name()));
						lockAxis = false;
					} else {
						if (StringUtilties.isNullOrEmptyString(selectedItem)) {
							return;
						}
						geoCoordSys.getGeoDatum().getGeoSpheroid().setType(GeoSpheroidType.SPHEROID_USER_DEFINED);
						geoCoordSys.getGeoDatum().getGeoSpheroid().setName((String) selectedItem);
					}
				}
			}
		});

		comboBoxCentralMeridianType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {

				if (lockCenter) {
					return;
				}
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object selectedItem = comboBoxCentralMeridianType.getSelectedItem();

					if (selectedItem instanceof GeoPrimeMeridianType) {
						if (geoCoordSys.getType() != GeoCoordSysType.GCS_USER_DEFINE) {
							geoCoordSys.setType(GeoCoordSysType.GCS_USER_DEFINE);
						}
						geoCoordSys.getGeoPrimeMeridian().setType((GeoPrimeMeridianType) selectedItem);
						lockCenter = true;
						textFieldLongitude.setText(String.valueOf(geoCoordSys.getGeoPrimeMeridian().getLongitudeValue()));
						comboBoxCentralMeridianType.setSelectedItem(PrjCoordSysTypeUtilties.getDescribe(((GeoPrimeMeridianType) selectedItem).name()));
						geoCoordSys.getGeoPrimeMeridian().setType(GeoPrimeMeridianType.PRIMEMERIDIAN_USER_DEFINED);
						geoCoordSys.getGeoPrimeMeridian().setName(PrjCoordSysTypeUtilties.getDescribe(((GeoPrimeMeridianType) selectedItem).name()));

						lockCenter = false;
					} else {
						if (StringUtilties.isNullOrEmptyString(selectedItem)) {
							return;
						}
						geoCoordSys.getGeoPrimeMeridian().setType(GeoPrimeMeridianType.PRIMEMERIDIAN_USER_DEFINED);
						geoCoordSys.getGeoPrimeMeridian().setName((String) selectedItem);
					}

				}
			}
		});
	}

	//region 初始化布局
	private void initLayout() {
		initPanelGeoDatum();
		initPanelCentralMeridian();
		this.setLayout(new GridBagLayout());
		this.add(labelType, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setFill(GridBagConstraints.NONE).setIpad(50, 0).setInsets(10, 10, 0, 0));
		this.add(comboBoxType, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(2, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 20));
		this.add(panelGeoDatum, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(2, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));
		this.add(panelCentralMeridian, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(2, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 10, 10));
	}


	private void initPanelGeoDatum() {
		// 大地参考系
		initPanelGeoSpheroid();
		panelGeoDatum.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GeoDatum")));
		panelGeoDatum.setLayout(new GridBagLayout());
		panelGeoDatum.add(labelGeoDatumType, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(5, 5, 0, 0).setIpad(39, 0));
		panelGeoDatum.add(comboBoxGeoDatumType, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(5, 5, 0, 5));
		panelGeoDatum.add(panelGeoSpheroid, new GridBagConstraintsHelper(0, 1, 2, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(5, 0, 0, 0));
	}

	private void initPanelGeoSpheroid() {
		// 椭球参数
		panelGeoSpheroid.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GeoSpheroid")));
		panelGeoSpheroid.setLayout(new GridBagLayout());
		panelGeoSpheroid.add(labelGeoSpheroidType, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setIpad(32, 0).setInsets(5, 5, 0, 0));
		panelGeoSpheroid.add(comboBoxGeoSpheroidType, new GridBagConstraintsHelper(1, 0, 3, 1).setWeight(2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));

		panelGeoSpheroid.add(labelAxis, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0));
		panelGeoSpheroid.add(textFieldAxis, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
		panelGeoSpheroid.add(labelFlatten, new GridBagConstraintsHelper(2, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 0).setIpad(30, 0));
		panelGeoSpheroid.add(textFieldFlatten, new GridBagConstraintsHelper(3, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
	}

	private void initPanelCentralMeridian() {
		// 中央经线
		panelCentralMeridian.setBorder(BorderFactory.createTitledBorder(CoreProperties.getString("String_PrjParameter_CenterMeridian")));
		panelCentralMeridian.setLayout(new GridBagLayout());
		panelCentralMeridian.add(labelCentralMeridianType, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setIpad(39, 0).setInsets(5, 5, 0, 0));
		panelCentralMeridian.add(comboBoxCentralMeridianType, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(5, 5, 0, 5));

		panelCentralMeridian.add(labelLongitude, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(5, 5, 0, 0));
		panelCentralMeridian.add(textFieldLongitude, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(5, 5, 0, 5));
	}

	//endregion

	private void initResources() {
		labelType.setText(ControlsProperties.getString("String_Label_Type"));
		labelCentralMeridianType.setText(ControlsProperties.getString("String_Label_Type"));
		labelGeoDatumType.setText(ControlsProperties.getString("String_Label_Type"));
		labelGeoSpheroidType.setText(ControlsProperties.getString("String_Label_Type"));
		labelAxis.setText(ControlsProperties.getString("String_Axis"));
		labelFlatten.setText(ControlsProperties.getString("String_Flatten"));
		labelLongitude.setText(ControlsProperties.getString("String_Label_LongitudeValue"));
	}

	private void initComponentStates() {
		lock = true;
		lockGeo = true;
		lockAxis = true;
		lockCenter = true;
		comboBoxType.setSelectedItem(this.geoCoordSys.getName());
		comboBoxGeoDatumType.setSelectedItem(this.geoCoordSys.getGeoDatum().getType());
		comboBoxGeoSpheroidType.setSelectedItem(this.geoCoordSys.getGeoDatum().getGeoSpheroid().getName());
		textFieldAxis.setText(String.valueOf(this.geoCoordSys.getGeoDatum().getGeoSpheroid().getAxis()));
		textFieldFlatten.setText(String.valueOf(this.geoCoordSys.getGeoDatum().getGeoSpheroid().getFlatten()));

		comboBoxCentralMeridianType.setSelectedItem(this.geoCoordSys.getGeoPrimeMeridian().getName());
		textFieldLongitude.setText(String.valueOf(this.geoCoordSys.getGeoPrimeMeridian().getLongitudeValue()));


		lock = false;
		lockGeo = false;
		lockAxis = false;
		lockCenter = false;
	}

	public GeoCoordSys getGeoCoordSys() {
		return geoCoordSys.clone();
	}

	public void setGeoCoordSys(GeoCoordSys geoCoordSys) {
		if (this.geoCoordSys != null) {
			this.geoCoordSys.dispose();
		}
		this.geoCoordSys = geoCoordSys.clone();
		initComponentStates();
	}

	public void dispose() {
		if (this.geoCoordSys != null) {
			this.geoCoordSys.dispose();
		}
	}
}
