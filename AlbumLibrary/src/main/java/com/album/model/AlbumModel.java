package com.album.model;

import java.io.Serializable;

/**
 * by y on 14/08/2017.
 */

public class AlbumModel implements Serializable {
    private String dirPath;
    private String dirName;
    private String path;

    public AlbumModel(String dirPath, String dirName, String path) {
        this.dirPath = dirPath;
        this.dirName = dirName;
        this.path = path;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
