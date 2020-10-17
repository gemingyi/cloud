package com.example.platformgenerator;

import java.io.Serializable;

/**
 * 模块信息
 * 
 * @author ckwen
 *
 */
public class ModuleInfo implements Serializable {

	private static final long serialVersionUID = 2023240168877809160L;

	private String projectName;

	private String packageInfo;

	private String tablePrefix;

	public ModuleInfo() {
		super();
	}

	public ModuleInfo(String projectName, String packageInfo, String tablePrefix) {
		super();
		this.projectName = projectName;
		this.packageInfo = packageInfo;
		this.tablePrefix = tablePrefix;
	}

	public static ModuleInfo instance(String projectName, String packageInfo, String tablePrefix) {
		return new ModuleInfo(projectName, packageInfo, tablePrefix);
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(String packageInfo) {
		this.packageInfo = packageInfo;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	@Override
	public String toString() {
		return "ModuleInfo [projectName=" + projectName + ", packageInfo=" + packageInfo + ", tablePrefix="
				+ tablePrefix + "]";
	}

}
