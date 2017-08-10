package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessOverlayAnalyst;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.AbstractProcessLoader;
import com.supermap.desktop.process.loader.IProcessDescriptor;
import com.supermap.desktop.utilities.OverlayAnalystType;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/8/7.
 */
public class OverlayProcessLoader extends AbstractProcessLoader {
	public OverlayProcessLoader(IProcessDescriptor descriptor) {
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
		if (MetaKeys.OVERLAY_ANALYST_CLIP.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.CLIP);
		} else if (MetaKeys.OVERLAY_ANALYST_UNION.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.UNION);
		} else if (MetaKeys.OVERLAY_ANALYST_ERASE.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.ERASE);
		} else if (MetaKeys.OVERLAY_ANALYST_IDENTITY.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.IDENTITY);
		} else if (MetaKeys.OVERLAY_ANALYST_INTERSECT.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.INTERSECT);
		} else if (MetaKeys.OVERLAY_ANALYST_UPDATE.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.UPDATE);
		} else if (MetaKeys.OVERLAY_ANALYST_XOR.equals(getProcessDescriptor().getKey())) {
			result = new MetaProcessOverlayAnalyst(OverlayAnalystType.XOR);
		}
		return result;
	}
}
