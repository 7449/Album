package com.album.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.album.util.FileUtils;
import com.album.util.SingleMediaScanner;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private UCrop.Options dayOptions;
    private UCrop.Options nightOptions;
    private ArrayList<AlbumModel> list;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_day_album).setOnClickListener(this);
        findViewById(R.id.btn_night_album).setOnClickListener(this);
        findViewById(R.id.btn_open_camera).setOnClickListener(this);

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
                Toast.makeText(getApplicationContext(), "进来选中的图片个数：" + list.size(), Toast.LENGTH_SHORT).show();
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
                                .setAlbumBottomFinderTextBackground(R.drawable.selector_btn)
//                                .setAlbumBottomPreviewTextBackground(R.drawable.selector_btn)
                                .setAlbumBottomSelectTextBackground(R.drawable.selector_btn)
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
            case R.id.btn_open_camera:
                // fragment  activity 直接传递 this ， 内部会自己处理
                int i = AlbumTool.openCamera(this, imagePath = Uri.fromFile(FileUtils.getCameraFile(this, null)));

                // -1 没有权限
                // 0 打开成功
                // 1 打开相机失败，估计设备没有安装相关软件

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
        } else if (resultCode == UCrop.RESULT_ERROR) {
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AlbumConstant.ITEM_CAMERA:
                    new SingleMediaScanner(this, FileUtils.getScannerFile(imagePath.getPath()),
                            new SingleMediaScanner.SingleScannerListener() {
                                @Override
                                public void onScanStart() {

                                }

                                @Override
                                public void onScanCompleted() {

                                }
                            });
                    UCrop.of(Uri.fromFile(new File(imagePath.getPath())), imagePath = Uri.fromFile(FileUtils.getCameraFile(this, null)))
                            .withOptions(new UCrop.Options())
                            .start(this);
                    break;
                case UCrop.REQUEST_CROP:
                    Toast.makeText(getApplicationContext(), "直接打开相机裁剪path:" + imagePath.getPath(), Toast.LENGTH_SHORT).show();
                    new SingleMediaScanner(this, FileUtils.getScannerFile(imagePath.getPath()),
                            new SingleMediaScanner.SingleScannerListener() {
                                @Override
                                public void onScanStart() {

                                }

                                @Override
                                public void onScanCompleted() {

                                }
                            });
                    break;
            }
        }
    }

    /**
     * @see com.album.ui.widget.SimpleAlbumListener
     */
    private static class MainAlbumListener implements AlbumListener {

        private Context context;
        private List<AlbumModel> list = null;

        void toast(String s) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }

        MainAlbumListener(Context context, ArrayList<AlbumModel> list) {
            this.context = context.getApplicationContext();
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
