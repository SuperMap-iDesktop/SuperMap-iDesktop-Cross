package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.Dataset;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.HashMap;

/**
 * Created by lixiaoyao on 2017/4/14.
 */
public class AvailableDatasetName {
    private HashMap<String, String> origionDatasetNameAndNewDatasetName;

    public AvailableDatasetName() {
        this.origionDatasetNameAndNewDatasetName = new HashMap<>();
    }

    public String getAvailableDatasetName(Dataset dataset) {
        String origionDatasetName = dataset.getName();
        String newName = origionDatasetName;
        if (this.origionDatasetNameAndNewDatasetName.containsKey(origionDatasetName)) {
            newName = this.origionDatasetNameAndNewDatasetName.get(origionDatasetName);
        } else {
            while (!dataset.getDatasource().getDatasets().isAvailableDatasetName(newName)) {
                if (newName.indexOf("_") != -1) {
                    int index = newName.lastIndexOf("_");
                    if (StringUtilities.isNumber(newName.substring(index + 1, newName.length()))) {
                        newName = getNotRepeatDatasetName(Integer.valueOf(newName.substring(index + 1, newName.length())) + 1, newName.substring(0, index + 1));
                    } else if (index == newName.length() - 1) {
                        newName = getNotRepeatDatasetName(1, newName);
                    } else {
                        newName = getNotRepeatDatasetName(1, newName + "_");
                    }
                } else {
                    newName = getNotRepeatDatasetName(1, newName + "_");
                }
            }
            this.origionDatasetNameAndNewDatasetName.put(origionDatasetName, newName);
        }
        return newName;
    }

    /**
     * 判断当前新的数据集名称是是否跟之前生成的名称相同，如果相同则进行处理，直到不相同为止
     * @param num
     * @param prePart
     * @return
     */
    private String getNotRepeatDatasetName(Integer num, String prePart) {
        String newName = prePart + num.toString();
        while (this.origionDatasetNameAndNewDatasetName.containsValue(newName)) {
            num = num + 1;
            newName = prePart + num.toString();
        }
        return newName;
    }
}
