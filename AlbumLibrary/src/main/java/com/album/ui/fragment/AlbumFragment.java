package com.album.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.R;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.presenter.AlbumPresenter;
import com.album.presenter.impl.AlbumPresenterImpl;
import com.album.ui.activity.AlbumActivity;
import com.album.ui.activity.PreviewActivity;
import com.album.ui.adapter.AlbumAdapter;
import com.album.ui.view.AlbumMethodFragmentView;
import com.album.ui.view.AlbumView;
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
        SingleMediaScanner.SingleScannerListener {

    private AlbumActivity albumActivity;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AlbumAdapter albumAdapter;
    private AlbumPresenter albumPresenter;
    private FrameLayout albumContentView;

    private Uri imagePath;
    private SingleMediaScanner singleMediaScanner;
    private ArrayList<FinderModel> finderModels = null;

    private AlbumConfig albumConfig = null;
    private String bucketId = null;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        albumConfig = Album.getInstance().getConfig();
        View inflate = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) inflate.findViewById(R.id.progress);
        albumContentView = (FrameLayout) inflate.findViewById(R.id.album_content_view);
        albumPresenter = new AlbumPresenterImpl(this);
        albumActivity = (AlbumActivity) getActivity();
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        albumContentView.setBackgroundColor(ContextCompat.getColor(albumActivity, albumConfig.getAlbumContentViewBackground()));
        initRecyclerView();
        onScanAlbum(null);
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(albumActivity, albumConfig.getSpanCount()));
        albumAdapter = new AlbumAdapter(null);
        albumAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(albumAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case AlbumConstant.TYPE_PREVIEW_CODE:
                    if (data == null) {
                        Album.getInstance().getAlbumListener().onAlbumFragmentResultNull();
                        return;
                    }
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
            Album.getInstance().getAlbumListener().onAlbumFragmentUCropError(data);
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AlbumConstant.ITEM_CAMERA:
                    refreshMedia();
                    if (albumConfig.isCameraCrop()) {
                        openUCrop(imagePath.getPath(), imagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity)));
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    Album.getInstance().getAlbumListener().onAlbumUCropResources(FileUtils.getScannerFile(imagePath.getPath()));
                    refreshMedia();
                    albumActivity.finish();
                    break;
                case AlbumConstant.TYPE_PREVIEW_CODE:
                    if (data == null) {
                        Album.getInstance().getAlbumListener().onAlbumFragmentResultNull();
                        return;
                    }
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
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void scanSuccess(ArrayList<AlbumModel> albumModels) {
        albumAdapter.addAll(albumModels);
    }

    @Override
    public void finderModel(ArrayList<FinderModel> list) {
        if (finderModels == null) {
            finderModels = new ArrayList<>();
        }
        finderModels.clear();
        if (list != null) {
            finderModels.addAll(list);
        }
    }

    @Override
    public ArrayList<AlbumModel> getSelectModel() {
        return albumAdapter.getMultiplePreviewList();
    }

    @Override
    public void onItemClick(View view, int position, AlbumModel albumModel) {
        if (position == 0 && TextUtils.equals(albumModel.getPath(), AlbumConstant.CAMERA)) {
            openCamera();
            return;
        }
        if (!FileUtils.isFile(albumModel.getPath())) {
            Album.getInstance().getAlbumListener().onAlbumFragmentFileNull();
            return;
        }
        if (albumConfig.isRadio()) {
            if (albumConfig.isCrop()) {
                openUCrop(albumModel.getPath(), imagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity)));
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
    public void onScanCompleted() {
        onScanAlbum(null);
    }


    @Override
    public void onScanAlbum(final String bucketId) {
        this.bucketId = bucketId;
        if (PermissionUtils.storage(albumActivity)) {
            albumActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    albumPresenter.scan(albumActivity.getContentResolver(), albumConfig.isHideCamera(), bucketId);
                }
            });
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
        imagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity));
        AlbumTool.openCamera(this, imagePath);
    }

    @Override
    public void openUCrop(String path, Uri uri) {
        UCrop.of(Uri.fromFile(new File(path)), uri)
                .withOptions(Album.getInstance().getOptions())
                .start(albumActivity, this);
    }

    @Override
    public void refreshMedia() {
        disconnectMediaScanner();
        singleMediaScanner = new SingleMediaScanner(albumActivity, FileUtils.getScannerFile(imagePath.getPath()), this);
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
            Album.getInstance().getAlbumListener().onAlbumFragmentResultNull();
            return;
        }
        if (!isRefreshUI) {
            return;
        }
        albumPresenter.mergeModel(albumAdapter.getAlbumList(), previewAlbumModel);
        albumAdapter.setMultiplePreviewList(previewAlbumModel);
    }


}
