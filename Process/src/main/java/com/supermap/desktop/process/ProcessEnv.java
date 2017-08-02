package com.supermap.desktop.process;

import com.supermap.desktop.process.loader.DefaultProcessLoader;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.parameter.ipls.ParameterClassBundleNode;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储到工作空间里以如下固定格式存储
 * <Desktop>
 * <NET></NET>
 * <Cross></Cross>
 * </Desktop>
 * Created by highsad on 2017/7/19.
 */
public class ProcessEnv {
	public static final ProcessEnv INSTANCE = new ProcessEnv();
	private ArrayList<ParameterClassBundleNode> packages = new ArrayList<>();
	private Map<String, IProcessLoader> processLoaderMap = new ConcurrentHashMap<>();

	private ProcessEnv() {

	}

	public ArrayList<ParameterClassBundleNode> getParamsUIPackage() {
		return this.packages;
	}

	public void addParametersUIPackage(String packageName, String bundleName) {
		if (!isExisted(packageName, bundleName)) {
			this.packages.add(new ParameterClassBundleNode(packageName, bundleName));
		}
	}

	public boolean isExisted(String packageName, String bundleName) {
		boolean isExisted = false;

		for (int i = 0; i < this.packages.size(); i++) {
			ParameterClassBundleNode pack = this.packages.get(i);
			if (pack.getBundleName().equals(bundleName) && pack.getPackageName().equals(packageName)) {
				isExisted = true;
				break;
			}
		}
		return isExisted;
	}

	public void registerProcessLoader(String className, IProcessLoader processLoader) {
		if (!StringUtilities.isNullOrEmpty(className) && !this.processLoaderMap.containsKey(className)) {
			this.processLoaderMap.put(className, processLoader);
		}
	}

	public IProcessLoader getProcessLoader(String className) {
		return this.processLoaderMap.containsKey(className) ? this.processLoaderMap.get(className) : DefaultProcessLoader.INSTANCE;
	}
}
