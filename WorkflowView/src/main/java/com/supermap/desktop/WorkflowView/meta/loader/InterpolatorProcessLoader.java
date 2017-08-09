package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessInterpolator;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.IProcessDescriptor;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/8/9.
 */
public class InterpolatorProcessLoader implements IProcessLoader {
	@Override
	public IProcess loadProcess(IProcessDescriptor descriptor) {
		if (descriptor == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(descriptor.getClassName())) {
			return null;
		}

		IProcess result = null;
		if (MetaKeys.INTERPOLATOR_IDW.equals(descriptor.getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.IDW);
		} else if (MetaKeys.INTERPOLATOR_RBF.equals(descriptor.getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.RBF);
		} else if (MetaKeys.INTERPOLATOR_UniversalKRIGING.equals(descriptor.getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.UniversalKRIGING);
		} else if (MetaKeys.INTERPOLATOR_SimpleKRIGING.equals(descriptor.getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.SimpleKRIGING);
		} else if (MetaKeys.INTERPOLATOR_KRIGING.equals(descriptor.getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.KRIGING);
		}
		return result;
	}
}
