package com.supermap.desktop.process.parameter.ipls;

/**
 * @author XiaJT
 */
public class ParameterClassBundleNode {
	private String packageName;
	private String bundleName;

	public ParameterClassBundleNode(String packageName, String bundleName) {
		this.packageName = packageName;
		this.bundleName = bundleName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
}
