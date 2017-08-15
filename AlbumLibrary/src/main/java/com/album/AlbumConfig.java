package com.album;

/**
 * by y on 14/08/2017.
 */

public class AlbumConfig {


    private boolean hideCamera = false;
    //////////////  album toolbar //////
    private int albumStatusBarColor = R.color.colorAlbumStatusBarColorDay;
    private int albumToolbarBackground = R.color.colorAlbumToolbarBackgroundDay;
    private int albumToolbarIcon = R.drawable.ic_arrow_back_day;
    private int albumToolbarTextColor = R.color.colorAlbumToolbarTextColorDay;
    private int albumToolbarText = R.string.album_name;
    private float albumToolbarElevation = 6f;
    ////////////// album bottom view //////
    private int albumBottomViewBackground = R.color.colorAlbumBottomViewBackgroundDay;
    private int albumBottomFinderTextSize = 16;
    private int albumBottomFinderTextColor = R.color.colorAlbumBottomFinderTextColorDay;
    private int albumBottomFinderTextDrawable = R.drawable.ic_action_album_finder_day;
    private int albumBottomPreViewText = R.string.album_preview;
    private int albumBottomPreViewTextSize = 16;
    private int albumBottomPreViewTextColor = R.color.colorAlbumBottomPreViewTextColorDay;
    private int albumBottomSelectText = R.string.album_select;
    private int albumBottomSelectTextSize = 16;
    private int albumBottomSelectTextColor = R.color.colorAlbumBottomSelectTextColorDay;
    ///////////// album list popup window /////
    private int albumListPopupWidth = 600;
    private int albumListPopupHorizontalOffset = 20;
    private int albumListPopupVerticalOffset = 80;
    private int albumListPopupItemBackground = R.color.colorAlbumListPopupItemBackgroundDay;
    private int albumListPopupItemTextColor = R.color.colorAlbumListPopupItemTextColorDay;


    public AlbumConfig() {
    }

    public AlbumConfig(int type) {
        switch (type) {
            case AlbumConstant.TYPE_NIGHT:
                albumStatusBarColor = R.color.colorAlbumStatusBarColorNight;
                albumToolbarBackground = R.color.colorAlbumToolbarBackgroundNight;
                albumToolbarIcon = R.drawable.ic_arrow_back_night;
                albumToolbarTextColor = R.color.colorAlbumToolbarTextColorNight;
                albumToolbarText = R.string.album_name;
                albumToolbarElevation = 6f;

                albumBottomViewBackground = R.color.colorAlbumBottomViewBackgroundNight;
                albumBottomFinderTextSize = 16;
                albumBottomFinderTextColor = R.color.colorAlbumBottomFinderTextColorNight;
                albumBottomFinderTextDrawable = R.drawable.ic_action_album_finder_night;
                albumBottomPreViewText = R.string.album_preview;
                albumBottomPreViewTextSize = 16;
                albumBottomPreViewTextColor = R.color.colorAlbumBottomPreViewTextColorNight;
                albumBottomSelectText = R.string.album_select;
                albumBottomSelectTextSize = 16;
                albumBottomSelectTextColor = R.color.colorAlbumBottomSelectTextColorNight;

                albumListPopupWidth = 600;
                albumListPopupHorizontalOffset = 20;
                albumListPopupVerticalOffset = 80;
                albumListPopupItemTextColor = R.color.colorAlbumListPopupItemTextColorNight;
                albumListPopupItemBackground = R.color.colorAlbumListPopupItemBackgroundNight;
                break;
            default:
                throw new RuntimeException("type");
        }
    }

    public boolean isHideCamera() {
        return hideCamera;
    }

    public AlbumConfig setHideCamera(boolean hideCamera) {
        this.hideCamera = hideCamera;
        return this;
    }

    public int getAlbumToolbarBackground() {
        return albumToolbarBackground;
    }

    public AlbumConfig setAlbumToolbarBackground(int albumToolbarBackground) {
        this.albumToolbarBackground = albumToolbarBackground;
        return this;
    }

    public int getAlbumToolbarIcon() {
        return albumToolbarIcon;
    }

    public AlbumConfig setAlbumToolbarIcon(int albumToolbarIcon) {
        this.albumToolbarIcon = albumToolbarIcon;
        return this;
    }

    public int getAlbumStatusBarColor() {
        return albumStatusBarColor;
    }

    public AlbumConfig setAlbumStatusBarColor(int albumStatusBarColor) {
        this.albumStatusBarColor = albumStatusBarColor;
        return this;
    }

    public int getAlbumToolbarTextColor() {
        return albumToolbarTextColor;
    }

    public AlbumConfig setAlbumToolbarTextColor(int albumToolbarTextColor) {
        this.albumToolbarTextColor = albumToolbarTextColor;
        return this;
    }

    public int getAlbumToolbarText() {
        return albumToolbarText;
    }

    public AlbumConfig setAlbumToolbarText(int albumToolbarText) {
        this.albumToolbarText = albumToolbarText;
        return this;
    }

    public float getAlbumToolbarElevation() {
        return albumToolbarElevation;
    }

    public AlbumConfig setAlbumToolbarElevation(float albumToolbarElevation) {
        this.albumToolbarElevation = albumToolbarElevation;
        return this;
    }

    public int getAlbumBottomViewBackground() {
        return albumBottomViewBackground;
    }

    public AlbumConfig setAlbumBottomViewBackground(int albumBottomViewBackground) {
        this.albumBottomViewBackground = albumBottomViewBackground;
        return this;
    }

    public int getAlbumBottomFinderTextSize() {
        return albumBottomFinderTextSize;
    }

    public AlbumConfig setAlbumBottomFinderTextSize(int albumBottomFinderTextSize) {
        this.albumBottomFinderTextSize = albumBottomFinderTextSize;
        return this;
    }

    public int getAlbumBottomFinderTextColor() {
        return albumBottomFinderTextColor;
    }

    public AlbumConfig setAlbumBottomFinderTextColor(int albumBottomFinderTextColor) {
        this.albumBottomFinderTextColor = albumBottomFinderTextColor;
        return this;
    }

    public int getAlbumBottomPreViewText() {
        return albumBottomPreViewText;
    }

    public AlbumConfig setAlbumBottomPreViewText(int albumBottomPreViewText) {
        this.albumBottomPreViewText = albumBottomPreViewText;
        return this;
    }

    public int getAlbumBottomPreViewTextSize() {
        return albumBottomPreViewTextSize;
    }

    public AlbumConfig setAlbumBottomPreViewTextSize(int albumBottomPreViewTextSize) {
        this.albumBottomPreViewTextSize = albumBottomPreViewTextSize;
        return this;
    }

    public int getAlbumBottomPreViewTextColor() {
        return albumBottomPreViewTextColor;
    }

    public AlbumConfig setAlbumBottomPreViewTextColor(int albumBottomPreViewTextColor) {
        this.albumBottomPreViewTextColor = albumBottomPreViewTextColor;
        return this;
    }

    public int getAlbumBottomSelectText() {
        return albumBottomSelectText;
    }

    public AlbumConfig setAlbumBottomSelectText(int albumBottomSelectText) {
        this.albumBottomSelectText = albumBottomSelectText;
        return this;
    }

    public int getAlbumBottomSelectTextSize() {
        return albumBottomSelectTextSize;
    }

    public AlbumConfig setAlbumBottomSelectTextSize(int albumBottomSelectTextSize) {
        this.albumBottomSelectTextSize = albumBottomSelectTextSize;
        return this;
    }

    public int getAlbumBottomSelectTextColor() {
        return albumBottomSelectTextColor;
    }

    public AlbumConfig setAlbumBottomSelectTextColor(int albumBottomSelectTextColor) {
        this.albumBottomSelectTextColor = albumBottomSelectTextColor;
        return this;
    }

    public int getAlbumListPopupWidth() {
        return albumListPopupWidth;
    }

    public AlbumConfig setAlbumListPopupWidth(int albumListPopupWidth) {
        this.albumListPopupWidth = albumListPopupWidth;
        return this;
    }

    public int getAlbumListPopupHorizontalOffset() {
        return albumListPopupHorizontalOffset;
    }

    public AlbumConfig setAlbumListPopupHorizontalOffset(int albumListPopupHorizontalOffset) {
        this.albumListPopupHorizontalOffset = albumListPopupHorizontalOffset;
        return this;
    }

    public int getAlbumListPopupVerticalOffset() {
        return albumListPopupVerticalOffset;
    }

    public AlbumConfig setAlbumListPopupVerticalOffset(int albumListPopupVerticalOffset) {
        this.albumListPopupVerticalOffset = albumListPopupVerticalOffset;
        return this;
    }

    public int getAlbumBottomFinderTextDrawable() {
        return albumBottomFinderTextDrawable;
    }

    public AlbumConfig setAlbumBottomFinderTextDrawable(int albumBottomFinderTextDrawable) {
        this.albumBottomFinderTextDrawable = albumBottomFinderTextDrawable;
        return this;
    }

    public int getAlbumListPopupItemBackground() {
        return albumListPopupItemBackground;
    }

    public AlbumConfig setAlbumListPopupItemBackground(int albumListPopupItemBackground) {
        this.albumListPopupItemBackground = albumListPopupItemBackground;
        return this;
    }

    public int getAlbumListPopupItemTextColor() {
        return albumListPopupItemTextColor;
    }

    public AlbumConfig setAlbumListPopupItemTextColor(int albumListPopupItemTextColor) {
        this.albumListPopupItemTextColor = albumListPopupItemTextColor;
        return this;
    }
}
