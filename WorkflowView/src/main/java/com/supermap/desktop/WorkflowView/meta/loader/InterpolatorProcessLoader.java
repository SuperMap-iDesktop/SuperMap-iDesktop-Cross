package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessInterpolator;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.AbstractProcessLoader;
import com.supermap.desktop.process.loader.IProcessDescriptor;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/8/9.
 */
public class InterpolatorProcessLoader extends AbstractProcessLoader {
	public InterpolatorProcessLoader(IProcessDescriptor descriptor) {
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

		IProcess result = null;
		if (MetaKeys.INTERPOLATOR_IDW.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.IDW);
		} else if (MetaKeys.INTERPOLATOR_RBF.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.RBF);
		} else if (MetaKeys.INTERPOLATOR_UniversalKRIGING.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.UniversalKRIGING);
		} else if (MetaKeys.INTERPOLATOR_SimpleKRIGING.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.SimpleKRIGING);
		} else if (MetaKeys.INTERPOLATOR_KRIGING.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.KRIGING);
		}
		return result;
	}
}
