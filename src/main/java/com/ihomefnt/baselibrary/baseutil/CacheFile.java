package com.ihomefnt.baselibrary.baseutil;

/**
 * cache file structure, remember the file content and the
 * update time
 */
public class CacheFile {
    private String updateTime;
    private String fileContent;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
