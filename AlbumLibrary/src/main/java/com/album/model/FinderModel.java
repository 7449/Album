package com.album.model;

/**
 * by y on 14/08/2017.
 */

public class FinderModel {
    private String dirName;
    private String thumbnailsPath;
    private String bucketId;
    private int count;

    public FinderModel(String dirName, String thumbnailsPath, String bucketId, int count) {
        this.dirName = dirName;
        this.thumbnailsPath = thumbnailsPath;
        this.bucketId = bucketId;
        this.count = count;
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

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
