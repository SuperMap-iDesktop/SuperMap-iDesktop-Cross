package com.supermap.desktop.controls.utilties;

import com.supermap.desktop.ui.controls.NodeDataType;

public class NodeDataTypeUtilties {
	public static boolean isNodeDataset(NodeDataType type) {
		return type == NodeDataType.DATASET_GRID || type == NodeDataType.DATASET_GRID_COLLECTION || type == NodeDataType.DATASET_GRID_COLLECTION_ITEM
				|| type == NodeDataType.DATASET_IMAGE || type == NodeDataType.DATASET_IMAGE_COLLECTION || type == NodeDataType.DATASET_IMAGE_COLLECTION_ITEM
				|| type == NodeDataType.DATASET_RELATION_SHIP || type == NodeDataType.DATASET_TOPOLOGY || type == NodeDataType.DATASET_VECTOR;
	}

	private NodeDataTypeUtilties() {
		// 工具类不提供构造函数
	}
}
