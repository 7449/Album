package com.album;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.album.model.AlbumModel;

import java.io.File;
import java.util.List;

/**
 * by y on 18/08/2017.
 */

public interface AlbumListener {
    void onAlbumActivityFinish();

    void onAlbumPermissionsDenied(int type);

    void onAlbumFragmentNull();

    void onAlbumPreviewFileNull();

    void onAlbumFinderNull();

    void onAlbumFragmentResultNull();

    void onAlbumBottomPreviewNull();

    void onAlbumBottomSelectNull();

    void onAlbumFragmentFileNull();

    void onAlbumPreviewSelectNull();

    void onAlbumFragmentCropCanceled();

    void onAlbumFragmentCameraCanceled();

    void onAlbumFragmentUCropError(@Nullable Intent data);

    void onAlbumResources(@NonNull List<AlbumModel> list);

    void onAlbumUCropResources(@Nullable File scannerFile);

    void onAlbumMaxCount();

    void onAlbumActivityBackPressed();
}
