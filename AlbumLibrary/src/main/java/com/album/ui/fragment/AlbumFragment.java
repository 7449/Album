package com.album.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.album.Album;
import com.album.AlbumCameraListener;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.R;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.presenter.impl.AlbumPresenterImpl;
import com.album.ui.activity.PreviewActivity;
import com.album.ui.adapter.AlbumAdapter;
import com.album.ui.annotation.AlbumResultType;
import com.album.ui.view.AlbumMethodFragmentView;
import com.album.ui.view.AlbumView;
import com.album.ui.widget.LoadMoreRecyclerView;
import com.album.ui.widget.SimpleGridDivider;
import com.album.util.AlbumTool;
import com.album.util.FileUtils;
import com.album.util.PermissionUtils;
import com.album.util.SingleMediaScanner;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * by y on 14/08/2017.
 */

public class AlbumFragment extends Fragment implements
        AlbumView,
        AlbumMethodFragmentView,
        AlbumAdapter.OnItemClickListener,
        SingleMediaScanner.SingleScannerListener, LoadMoreRecyclerView.LoadMoreListener {

    private Activity albumActivity;

    private LoadMoreRecyclerView recyclerView;
    private ProgressBar progressBar;
    private AlbumAdapter albumAdapter;
    private AppCompatImageView emptyView;
    private AlbumPresenterImpl albumPresenter;

    private Uri uCropImagePath = null;
    private Uri imagePath = null;
    private SingleMediaScanner singleMediaScanner = null;
    private ArrayList<FinderModel> finderModels = null;
    private ArrayList<AlbumModel> multipleAlbumModel = null;
    private AlbumConfig albumConfig = null;
    private String bucketId = null;
    private int page = 0;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        bucketId = savedInstanceState.getString(AlbumConstant.TYPE_ALBUM_STATE_BUCKET_ID);
        multipleAlbumModel = savedInstanceState.getParcelableArrayList(AlbumConstant.TYPE_ALBUM_STATE_SELECT);
        imagePath = savedInstanceState.getParcelable(AlbumConstant.TYPE_ALBUM_STATE_URI_PATH);
        uCropImagePath = savedInstanceState.getParcelable(AlbumConstant.TYPE_ALBUM_STATE_CROP_URI_PATH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        albumConfig = Album.getInstance().getConfig();
        View inflate = inflater.inflate(R.layout.fragment_album, container, false);
        inflate.findViewById(R.id.album_content_view).setBackgroundColor(ContextCompat.getColor(inflate.getContext(), albumConfig.getAlbumContentViewBackground()));
        recyclerView = (LoadMoreRecyclerView) inflate.findViewById(R.id.album_recyclerView);
        progressBar = (ProgressBar) inflate.findViewById(R.id.album_progress);
        emptyView = (AppCompatImageView) inflate.findViewById(R.id.album_empty);
        albumPresenter = new AlbumPresenterImpl(this, albumConfig.isVideo());
        albumActivity = getActivity();

        Drawable drawable = ContextCompat.getDrawable(albumActivity, albumConfig.getAlbumContentEmptyDrawable());
        drawable.setColorFilter(ContextCompat.getColor(albumActivity, albumConfig.getAlbumContentEmptyDrawableColor()), PorterDuff.Mode.SRC_ATOP);
        emptyView.setImageDrawable(drawable);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Album.getInstance().getEmptyClickListener() != null) {
                    if (Album.getInstance().getEmptyClickListener().click(v)) {
                        openCamera();
                    }
                }
            }
        });

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        finderModels = new ArrayList<>();
        initRecyclerView();
        onScanAlbum(bucketId, false, false);
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(albumActivity, albumConfig.getSpanCount()));
        recyclerView.setLoadingListener(this);
        recyclerView.addItemDecoration(new SimpleGridDivider(albumConfig.getDividerWidth()));
        albumAdapter = new AlbumAdapter(new ArrayList<AlbumModel>(), AlbumTool.getImageViewWidth(albumActivity, albumConfig.getSpanCount()));
        albumAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(albumAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case AlbumConstant.TYPE_PREVIEW_CODE:
                    onResultPreview(data.getExtras());
                    break;
                case UCrop.REQUEST_CROP:
                    Album.getInstance().getAlbumListener().onAlbumFragmentCropCanceled();
                    break;
                case AlbumConstant.ITEM_CAMERA:
                    Album.getInstance().getAlbumListener().onAlbumFragmentCameraCanceled();
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Album.getInstance().getAlbumListener().onAlbumFragmentUCropError(UCrop.getError(data));
            albumActivity.finish();
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        String customizePath = extras.getString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY);
                        if (!TextUtils.isEmpty(customizePath)) {
                            imagePath = Uri.fromFile(new File(customizePath));
                            refreshMedia(AlbumConstant.TYPE_RESULT_CAMERA);
                            if (albumConfig.isCameraCrop()) {
                                openUCrop(imagePath.getPath(), uCropImagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity, albumConfig.getuCropPath(), albumConfig.isVideo())));
                            }
                        }
                    }
                    break;
                case AlbumConstant.ITEM_CAMERA:
                    refreshMedia(AlbumConstant.TYPE_RESULT_CAMERA);
                    if (albumConfig.isCameraCrop()) {
                        openUCrop(imagePath.getPath(), uCropImagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity, albumConfig.getuCropPath(), albumConfig.isVideo())));
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    Album.getInstance().getAlbumListener().onAlbumUCropResources(FileUtils.getScannerFile(uCropImagePath.getPath()));
                    refreshMedia(AlbumConstant.TYPE_RESULT_CROP);
                    albumActivity.finish();
                    break;
                case AlbumConstant.TYPE_PREVIEW_CODE:
                    onResultPreview(data.getExtras());
                    break;
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disconnectMediaScanner();
    }

    @Override
    public void showProgress() {
        if (progressBar != null && progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void scanSuccess(ArrayList<AlbumModel> albumModels) {
        if (emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(bucketId) && !albumConfig.isHideCamera() && page == 0 && albumModels != null && !albumModels.isEmpty()) {
            albumModels.add(0, new AlbumModel(null, null, AlbumConstant.CAMERA, 0, false));
        }
        albumAdapter.addAll(albumModels);
        if (page == 0 && !albumConfig.isRadio()) {
            ArrayList<AlbumModel> selectModel = Album.getInstance().getAlbumModels();
            if (selectModel != null && !selectModel.isEmpty() && albumModels != null && !albumModels.isEmpty()) {
                albumPresenter.firstMergeModel(albumModels, selectModel);
                albumAdapter.setMultiplePreviewList(selectModel);
            }
        }
        ++page;
    }

    @Override
    public void finderModel(ArrayList<FinderModel> list) {
        finderModels.clear();
        finderModels.addAll(list);
    }

    @Override
    public ArrayList<AlbumModel> getSelectModel() {
        if (multipleAlbumModel != null) {
            albumAdapter.setMultiplePreviewList(multipleAlbumModel);
        }
        return albumAdapter.getMultiplePreviewList();
    }

    @Override
    public Activity getAlbumActivity() {
        return albumActivity;
    }

    @Override
    public void onAlbumNoMore() {
        if (page == 0) {
            emptyView.setVisibility(View.VISIBLE);
            Album.getInstance().getAlbumListener().onAlbumEmpty();
        }
        Album.getInstance().getAlbumListener().onAlbumNoMore();
    }

    @Override
    public void resultSuccess(AlbumModel albumModel) {
        if (albumModel == null) {
            Album.getInstance().getAlbumListener().onAlbumResultCameraError();
        } else {
            albumAdapter.getAlbumList().add(1, albumModel);
            albumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position, AlbumModel albumModel) {
        if (position == 0 && TextUtils.equals(albumModel.getPath(), AlbumConstant.CAMERA)) {
            if (PermissionUtils.camera(albumActivity)) {
                openCamera();
            }
            return;
        }
        if (!FileUtils.isFile(albumModel.getPath())) {
            Album.getInstance().getAlbumListener().onAlbumFragmentFileNull();
            return;
        }
        if (albumConfig.isVideo()) {
            try {
                Intent openVideo = new Intent(Intent.ACTION_VIEW);
                openVideo.setDataAndType(Uri.parse(albumModel.getPath()), AlbumConstant.VIDEO_PLAY_TYPE);
                startActivity(openVideo);
            } catch (Exception e) {
                Album.getInstance().getAlbumListener().onVideoPlayError();
            }
            return;
        }
        if (albumConfig.isRadio()) {
            if (albumConfig.isCrop()) {
                openUCrop(albumModel.getPath(), uCropImagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity, albumConfig.getuCropPath(), albumConfig.isVideo())));
            } else {
                List<AlbumModel> list = new ArrayList<>();
                list.add(albumModel);
                Album.getInstance().getAlbumListener().onAlbumResources(list);
                albumActivity.finish();
            }
            return;
        }
        Bundle bundle = new Bundle();
        ArrayList<AlbumModel> multiplePreviewList = albumAdapter.getMultiplePreviewList();
        bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, multiplePreviewList);
        bundle.putInt(AlbumConstant.PREVIEW_POSITION_KEY, position);
        bundle.putString(AlbumConstant.PREVIEW_BUCKET_ID, bucketId);
        startActivityForResult(new Intent(albumActivity, PreviewActivity.class).putExtras(bundle), AlbumConstant.TYPE_PREVIEW_CODE);
    }

    @Override
    public void onScanStart() {
    }

    @Override
    public void onScanCompleted(@AlbumResultType int type) {
        if (type == AlbumConstant.TYPE_RESULT_CROP) {
            return;
        }
        onScanAlbum(bucketId, false, true);
    }


    @Override
    public void onScanAlbum(final String bucketId, boolean isFinder, boolean result) {
        if (isFinder && albumAdapter != null) {
            page = 0;
            albumAdapter.removeAll();
        }
        this.bucketId = bucketId;
        if (PermissionUtils.storage(albumActivity)) {
            if (result && !albumAdapter.getAlbumList().isEmpty()) {
                albumPresenter.resultScan(imagePath.getPath());
                return;
            }
            albumPresenter.scan(bucketId, page, albumConfig.getCount());
        }
    }

    @Override
    public void disconnectMediaScanner() {
        if (singleMediaScanner != null) {
            singleMediaScanner.disconnect();
            singleMediaScanner = null;
        }
    }

    @Override
    public void openCamera() {
        AlbumCameraListener albumCameraListener = Album.getInstance().getAlbumCameraListener();
        if (albumCameraListener != null) {
            albumCameraListener.startCamera(this);
            return;
        }
        imagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity, albumConfig.getCameraPath(), albumConfig.isVideo()));
        int i = AlbumTool.openCamera(this, imagePath, albumConfig.isVideo());
        if (i == 1) {
            Album.getInstance().getAlbumListener().onAlbumOpenCameraError();
        }
    }

    @Override
    public void openUCrop(String path, Uri uri) {
        UCrop.of(Uri.fromFile(new File(path)), uri)
                .withOptions(Album.getInstance().getOptions())
                .start(albumActivity, this);
    }


    @Override
    public void refreshMedia(@AlbumResultType int type) {
        disconnectMediaScanner();
        singleMediaScanner = new SingleMediaScanner(albumActivity,
                FileUtils.getScannerFile(type == AlbumConstant.TYPE_RESULT_CAMERA ?
                        imagePath.getPath() :
                        uCropImagePath.getPath()),
                AlbumFragment.this, type);
    }

    @Override
    public List<FinderModel> getFinderModel() {
        return finderModels;
    }

    @Override
    public void multiplePreview() {
        ArrayList<AlbumModel> albumModels = albumAdapter.getMultiplePreviewList();
        if (albumModels == null || albumModels.isEmpty()) {
            Album.getInstance().getAlbumListener().onAlbumBottomPreviewNull();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, albumModels);
        bundle.putString(AlbumConstant.PREVIEW_BUCKET_ID, AlbumConstant.PREVIEW_BUTTON_KEY);
        startActivityForResult(new Intent(albumActivity, PreviewActivity.class).putExtras(bundle), AlbumConstant.TYPE_PREVIEW_CODE);
    }

    @Override
    public void multipleSelect() {
        List<AlbumModel> albumModels = albumAdapter.getMultiplePreviewList();
        if (albumModels == null || albumModels.isEmpty()) {
            Album.getInstance().getAlbumListener().onAlbumBottomSelectNull();
            return;
        }
        Album.getInstance().getAlbumListener().onAlbumResources(albumModels);
        albumActivity.finish();
    }

    @Override
    public void onResultPreview(Bundle bundle) {
        ArrayList<AlbumModel> previewAlbumModel = bundle.getParcelableArrayList(AlbumConstant.PREVIEW_KEY);
        boolean isRefreshUI = bundle.getBoolean(AlbumConstant.PREVIEW_REFRESH_UI, true);
        boolean isFinish = bundle.getBoolean(AlbumConstant.PREVIEW_FINISH, false);
        if (isFinish) {
            albumActivity.finish();
            return;
        }
        if (previewAlbumModel == null) {
            return;
        }
        multipleAlbumModel = previewAlbumModel;
        if (!isRefreshUI) {
            return;
        }
        albumPresenter.mergeModel(albumAdapter.getAlbumList(), previewAlbumModel);
        albumAdapter.setMultiplePreviewList(previewAlbumModel);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AlbumConstant.TYPE_ALBUM_STATE_SELECT, albumAdapter.getMultiplePreviewList());
        outState.putString(AlbumConstant.TYPE_ALBUM_STATE_BUCKET_ID, bucketId);
        outState.putParcelable(AlbumConstant.TYPE_ALBUM_STATE_URI_PATH, imagePath);
        outState.putParcelable(AlbumConstant.TYPE_ALBUM_STATE_CROP_URI_PATH, uCropImagePath);
    }


    @Override
    public void onLoadMore() {
        if (PermissionUtils.storage(albumActivity) && !albumPresenter.isScan()) {
            albumPresenter.scan(bucketId, page, albumConfig.getCount());
        }
    }
}
