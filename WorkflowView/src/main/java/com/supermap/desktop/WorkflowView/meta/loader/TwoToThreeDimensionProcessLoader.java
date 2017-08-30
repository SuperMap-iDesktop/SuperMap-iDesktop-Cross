package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.data.DatasetType;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion.MetaProcess2DTo3D;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.AbstractProcessLoader;
import com.supermap.desktop.process.loader.IProcessDescriptor;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created By Chens on 2017/8/12 0012
 */
public class TwoToThreeDimensionProcessLoader extends AbstractProcessLoader {
	private final static String TWO_TO_THREE_DIMENSION = "2DTo3D";

	public TwoToThreeDimensionProcessLoader(IProcessDescriptor descriptor) {
		super(descriptor);
	}

	@Override
	public IProcess loadProcess() {
		if (getProcessDescriptor() == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(getProcessDescriptor().getClassName())) {
			return null;
		}

		DatasetType type = null;
		switch (getProcessDescriptor().getKey()) {
			case MetaKeys.CONVERSION_POINT2D_TO_3D:
				type = DatasetType.POINT;
				break;
			case MetaKeys.CONVERSION_LINE2D_TO_3D:
				type = DatasetType.LINE;
				break;
			case MetaKeys.CONVERSION_REGION2D_TO_3D:
				type = DatasetType.REGION;
				break;
		}
		return new MetaProcess2DTo3D(type);
	}
}
