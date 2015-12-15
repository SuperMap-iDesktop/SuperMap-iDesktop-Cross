package com.supermap.desktop.enums;

/**
 * 指定打开工作空间的结果。
 * @author Huchenpu
 *
 */
public enum OpenWorkspaceResult {
    /**
     * 打开工作空间成功。
     */
    SUCCESSED,

    /**
     * 由于未知错误，导致打开工作空间失败。 
     */
    FAILED_UNKNOW,

    /**
     * 由于许可原因，导致打开工作空间失败。
     */
    FAILED_LICENSE_WRONG,

    /**
     * 由于文件原因，比如文件占用、文件损坏等，导致打开工作空间失败。
     */
    FAILED_FILE_WRONG,

    /**
     * 由于密码不正确，导致打开工作空间失败。
     */
    FAILED_PASSWORD_WRONG,

    /**
     * 由于工作空间版本不正确，导致打开工作空间失败。
     */
    FAILED_VERSION_WRONG,

    /**
     * 由于用户取消操作，导致打开工作空间失败。
     */
    FAILED_CANCEL,
}
