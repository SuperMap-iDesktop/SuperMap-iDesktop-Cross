package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessInterpolator;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.AbstractProcessLoader;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.Map;

/**
 * Created by highsad on 2017/8/9.
 */
public class InterpolatorProcessLoader extends AbstractProcessLoader {
	public InterpolatorProcessLoader(Map<String, String> properties, String index) {
		super(properties, index);
	}

	@Override
	public IProcess loadProcess() {
		if (getProperties() == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(getClassName())) {
			return null;
		}

		IProcess result = null;
		if (MetaKeys.INTERPOLATOR_IDW.equals(getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.IDW);
		} else if (MetaKeys.INTERPOLATOR_RBF.equals(getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.RBF);
		} else if (MetaKeys.INTERPOLATOR_UniversalKRIGING.equals(getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.UniversalKRIGING);
		} else if (MetaKeys.INTERPOLATOR_SimpleKRIGING.equals(getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.SimpleKRIGING);
		} else if (MetaKeys.INTERPOLATOR_KRIGING.equals(getKey())) {
			result = new MetaProcessInterpolator(InterpolationAlgorithmType.KRIGING);
		}
		return result;
	}
}
