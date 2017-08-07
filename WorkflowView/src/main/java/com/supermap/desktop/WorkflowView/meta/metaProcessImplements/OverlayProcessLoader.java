package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.IProcessDescriptor;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.utilities.OverlayAnalystType;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/8/7.
 */
public class OverlayProcessLoader implements IProcessLoader {
	@Override
	public IProcess loadProcess(IProcessDescriptor descriptor) {
		if (descriptor == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(descriptor.getClassName())) {
			return null;
		}

		IProcess result = null;
		if (MetaKeys.OVERLAY_ANALYST_CLIP.equals(descriptor.getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.CLIP);
		} else if (MetaKeys.OVERLAY_ANALYST_UNION.equals(descriptor.getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.UNION);
		} else if (MetaKeys.OVERLAY_ANALYST_ERASE.equals(descriptor.getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.ERASE);
		} else if (MetaKeys.OVERLAY_ANALYST_IDENTITY.equals(descriptor.getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.IDENTITY);
		} else if (MetaKeys.OVERLAY_ANALYST_INTERSECT.equals(descriptor.getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.INTERSECT);
		} else if (MetaKeys.OVERLAY_ANALYST_UPDATE.equals(descriptor.getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.UPDATE);
		} else if (MetaKeys.OVERLAY_ANALYST_XOR.equals(descriptor.getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.XOR);
		}
		return result;
	}
}
