package com.album.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.util.PermissionUtils;
import com.album.util.StatusBarUtil;

/**
 * by y on 14/08/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected AlbumConfig albumConfig = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        albumConfig = Album.getInstance().getConfig();
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(ContextCompat.getColor(this, albumConfig.getAlbumStatusBarColor()), getWindow());
        setContentView(getLayoutId());
        initView();
        initTitle();
        if (PermissionUtils.storage(this)) {
            permissionsGranted(AlbumConstant.TYPE_ALBUM);
        }
        initCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AlbumConstant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(AlbumConstant.TYPE_ALBUM);
                } else {
                    permissionsGranted(AlbumConstant.TYPE_ALBUM);
                }
                break;
            case AlbumConstant.CAMERA_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(AlbumConstant.TYPE_CAMERA);
                } else {
                    permissionsGranted(AlbumConstant.TYPE_CAMERA);
                }
                break;
        }
    }

    protected abstract void initCreate(@Nullable Bundle savedInstanceState);

    protected abstract void initView();


    protected abstract void initTitle();

    @LayoutRes
    protected abstract int getLayoutId();


    protected abstract void permissionsDenied(int type);

    protected abstract void permissionsGranted(int type);
}
