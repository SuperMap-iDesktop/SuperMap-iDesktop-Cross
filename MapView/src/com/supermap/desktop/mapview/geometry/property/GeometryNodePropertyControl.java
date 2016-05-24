package com.supermap.desktop.mapview.geometry.property;

import com.supermap.data.Recordset;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CoreProperties;

/**
 * @author XiaJT
 */
public class GeometryNodePropertyControl extends AbstractPropertyControl {
	private Recordset recordset = null;

	public GeometryNodePropertyControl(Recordset recordset) {
		super(CoreProperties.getString("String_NodeInfo"));
		this.recordset = recordset;
		init();
	}

	private void init() {
		initComponent();
	}

	private void initComponent() {

	}

	@Override
	public void refreshData() {
		initComponentStates();
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.GEOMETRY_NODE;
	}

	public void setRecordset(Recordset recordset) {
		if (this.recordset != null && !this.recordset.isClosed()) {
			this.recordset.dispose();
		}
		this.recordset = recordset;
		initComponentStates();
	}

	private void initComponentStates() {

	}
}
