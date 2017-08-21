package com.album.ui.view;

import com.album.model.AlbumModel;

import java.util.ArrayList;

/**
 * by y on 15/08/2017.
 */

public interface PreviewMethodActivityView {
    void initBundle();

    void initViewPager(ArrayList<AlbumModel> albumModels);

    void setTitles(int page, int imageSize);

    void setCount(int count, int size);

    void checkBoxClick();

    void isRefreshAlbumUI(boolean isRefresh, boolean isFinish);
}
