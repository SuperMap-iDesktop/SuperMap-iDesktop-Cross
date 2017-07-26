package com.supermap.desktop.ui.lbs;

/**
 * describe a HDFS file
 *
 * @author huchenpu
 */
public class HDFSDefine {
    private String fullPath = "";
    String permission = "", owner = "", group = "", length = "", replication = "", blockSize = "", pathSuffix = "";
    Boolean isDir = false;

    public HDFSDefine(String permission, String owner, String group, String size, String replication, String blockSize, String name, Boolean isDir) {
        this.permission = permission;
        this.owner = owner;
        this.group = group;
        this.length = size;
        this.replication = replication;
        this.blockSize = blockSize;
        this.pathSuffix = name;
        this.isDir = isDir;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSize() {
        return this.length;
    }

    public void setSize(String length) {
        this.length = length;
    }

    public String getReplication() {
        return this.replication;
    }

    public void setReplication(String replication) {
        this.replication = replication;
    }

    public String getBlockSize() {
        return this.blockSize;
    }

    public void setBlockSize(String blockSize) {
        this.blockSize = blockSize;
    }

    public String getName() {
//			try {
//				this.pathSuffix = URLEncoder.encode(pathSuffix, "GBK");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
        return this.pathSuffix;
    }

    public void setName(String pathSuffix) {
        this.pathSuffix = pathSuffix;
    }

    public Boolean isDir() {
        return this.isDir;
    }

    public void setIsDir(Boolean isDir) {
        this.isDir = isDir;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    @Override
    public String toString() {
        return this.fullPath;
    }
}
