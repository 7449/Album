package com.album.model;

/**
 * by y on 14/08/2017.
 */

public class FinderModel {
    private String dirName;
    private String thumbnailsPath;
    private boolean isSelect;

    public FinderModel(String dirName, String thumbnailsPath, boolean isSelect) {
        this.dirName = dirName;
        this.thumbnailsPath = thumbnailsPath;
        this.isSelect = isSelect;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getThumbnailsPath() {
        return thumbnailsPath;
    }

    public void setThumbnailsPath(String thumbnailsPath) {
        this.thumbnailsPath = thumbnailsPath;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
