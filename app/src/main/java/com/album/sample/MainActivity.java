package com.album.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.AlbumListener;
import com.album.model.AlbumModel;
import com.album.util.AlbumTool;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private UCrop.Options dayOptions;
    private UCrop.Options nightOptions;
    private ArrayList<AlbumModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_day_album).setOnClickListener(this);
        findViewById(R.id.btn_night_album).setOnClickListener(this);

        dayOptions = new UCrop.Options();
        dayOptions.setToolbarTitle("DayTheme");
        dayOptions.setToolbarColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundDay));
        dayOptions.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAlbumStatusBarColorDay));
        dayOptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundDay));

        nightOptions = new UCrop.Options();
        nightOptions.setToolbarTitle("NightTheme");
        nightOptions.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarTextColorNight));
        nightOptions.setToolbarColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundNight));
        nightOptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundNight));
        nightOptions.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAlbumStatusBarColorNight));
        list = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_day_album:
                AlbumTool.log(list.size());
                Album
                        .getInstance()
                        .setAlbumModels(list)
                        .setAlbumImageLoader(new SimpleFrescoAlbumImageLoader())
                        .setAlbumListener(new MainAlbumListener(this, list))
                        .setOptions(dayOptions)
                        .setConfig(new AlbumConfig()
                                .setCameraCrop(false)
                                .setPermissionsDeniedFinish(false)
                                .setPreviewFinishRefresh(true)
                                .setAlbumContentItemCheckBoxDrawable(R.drawable.simple_selector_album_item_check)
                                .setFrescoImageLoader(true)  // 通知 Album 图片加载框架使用的是 Fresco
                                .setPreviewBackRefresh(true))
                        .start(this);
                break;
            case R.id.btn_night_album:
                Album
                        .getInstance()
                        .setAlbumListener(new MainAlbumListener(this, null))
                        .setAlbumImageLoader(new SimpleGlide4xAlbumImageLoader())
                        .setOptions(nightOptions)
                        .setConfig(new AlbumConfig(AlbumConstant.TYPE_NIGHT)
                                .setRadio(true)
                                .setCrop(true).setCameraPath(Environment.getExternalStorageDirectory().getPath() + "/" + "DCIM/Album")
                                .setuCropPath(Environment.getExternalStorageDirectory().getPath() + "/" + "DCIM" + "/" + "uCrop")
                                .setPermissionsDeniedFinish(true)
                                .setPreviewFinishRefresh(true)
                                .setPreviewBackRefresh(true)
                                .setCameraCrop(true))
                        .start(this);
                break;
        }
    }


    /**
     * @see com.album.ui.widget.SimpleAlbumListener
     */
    private class MainAlbumListener implements AlbumListener {

        private Context context;
        private List<AlbumModel> list = null;

        void toast(String s) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }

        MainAlbumListener(Context context, ArrayList<AlbumModel> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public void onAlbumActivityFinish() {
            toast("图片页面 finish");
        }

        @Override
        public void onAlbumPermissionsDenied(int type) {
            toast("权限被拒");
        }

        @Override
        public void onAlbumFragmentNull() {
            toast("fragment null,应该不会触发");
        }

        @Override
        public void onAlbumPreviewFileNull() {
            toast("预览ViewPager滑动时，使用者在后台删除了图片之后再回到预览界面滑动时会触发");
        }

        @Override
        public void onAlbumFinderNull() {
            toast("图片文件夹目录为空");
        }

        @Override
        public void onAlbumBottomPreviewNull() {
            toast("选择预览时没有选中图片");
        }

        @Override
        public void onAlbumBottomSelectNull() {
            toast("多选选择时没有选中图片");
        }

        @Override
        public void onAlbumFragmentFileNull() {
            toast("使用者在后台删除了图片之后再点击该图片时会触发");
        }

        @Override
        public void onAlbumPreviewSelectNull() {
            toast("预览界面，没有多选照片");
        }

        @Override
        public void onAlbumCheckBoxFileNull() {
            toast("使用者在后台删除了图片之后再选择该图片时会触发");
        }

        @Override
        public void onAlbumFragmentCropCanceled() {
            toast("取消裁剪");
        }

        @Override
        public void onAlbumFragmentCameraCanceled() {
            toast("取消拍照");
        }

        @Override
        public void onAlbumFragmentUCropError(@Nullable Throwable data) {
            toast("裁剪异常:" + data.toString());
        }

        @Override
        public void onAlbumResources(@NonNull List<AlbumModel> list) {
            toast("返回的图片数据" + list.size());
            for (AlbumModel albumModel : list) {
                AlbumTool.log(albumModel.getPath());
            }
            if (this.list != null) {
                this.list.clear();
                this.list.addAll(list);
            }
        }

        @Override
        public void onAlbumUCropResources(@Nullable File scannerFile) {
            toast("返回的裁剪File" + scannerFile);
        }

        @Override
        public void onAlbumMaxCount() {
            toast("多选最大值");
        }

        @Override
        public void onAlbumActivityBackPressed() {
            toast("图片页 back 返回");
        }

        @Override
        public void onAlbumOpenCameraError() {
            toast("没有检测到相机");
        }
    }

}
