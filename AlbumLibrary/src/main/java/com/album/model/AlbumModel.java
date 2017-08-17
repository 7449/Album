package com.album.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * by y on 14/08/2017.
 */

public class AlbumModel implements Parcelable {
    public static final Creator<AlbumModel> CREATOR = new Creator<AlbumModel>() {
        @Override
        public AlbumModel createFromParcel(Parcel in) {
            return new AlbumModel(in);
        }

        @Override
        public AlbumModel[] newArray(int size) {
            return new AlbumModel[size];
        }
    };
    private String dirPath;
    private String dirName;
    private String path;
    private boolean isCheck;

    public AlbumModel(String dirPath, String dirName, String path, boolean isCheck) {
        this.dirPath = dirPath;
        this.dirName = dirName;
        this.path = path;
        this.isCheck = isCheck;
    }

    protected AlbumModel(Parcel in) {
        dirPath = in.readString();
        dirName = in.readString();
        path = in.readString();
        isCheck = in.readByte() != 0;
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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dirPath);
        dest.writeString(dirName);
        dest.writeString(path);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumModel that = (AlbumModel) o;

        if (isCheck != that.isCheck) return false;
        if (dirPath != null ? !dirPath.equals(that.dirPath) : that.dirPath != null) return false;
        if (dirName != null ? !dirName.equals(that.dirName) : that.dirName != null) return false;
        return path != null ? path.equals(that.path) : that.path == null;

    }

    @Override
    public int hashCode() {
        int result = dirPath != null ? dirPath.hashCode() : 0;
        result = 31 * result + (dirName != null ? dirName.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (isCheck ? 1 : 0);
        return result;
    }
}
