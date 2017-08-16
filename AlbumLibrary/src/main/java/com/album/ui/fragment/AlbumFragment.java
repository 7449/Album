package com.album.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
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
import com.album.util.CameraUtil;
import com.album.util.FileUtils;
import com.album.util.PermissionUtils;
import com.album.util.SingleMediaScanner;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.Serializable;
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
    private List<FinderModel> finderModels = null;
    private ArrayMap<String, List<AlbumModel>> arrayMap = null;
    private String key = AlbumConstant.ALL_ALBUM_NAME;

    private AlbumConfig albumConfig = null;

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
        onScanAlbum();
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), albumConfig.getSpanCount()));
        albumAdapter = new AlbumAdapter(null);
        albumAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(albumAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
        } else if (resultCode == UCrop.RESULT_ERROR) {
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AlbumConstant.ITEM_CAMERA:
                case UCrop.REQUEST_CROP:
                    disconnectMediaScanner();
                    singleMediaScanner = new SingleMediaScanner(albumActivity, FileUtils.getScannerFile(imagePath.getPath()), this);
                    break;
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (arrayMap != null) {
            arrayMap.clear();
        }
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
    public void scanSuccess(ArrayMap<String, List<AlbumModel>> maps) {
        arrayMap = maps;
        updateUI(AlbumConstant.ALL_ALBUM_NAME);
    }


    @Override
    public void finderModel(List<FinderModel> list) {
        if (finderModels == null) {
            finderModels = new ArrayList<>();
        } else {
            finderModels.clear();
        }
        if (list != null) {
            finderModels.addAll(list);
        }
    }

    @Override
    public void onItemClick(View view, int position, AlbumModel albumModel) {
        if (position == 0 && TextUtils.equals(albumModel.getPath(), AlbumConstant.CAMERA)) {
            openCamera();
        } else {
            if (FileUtils.isFile(albumModel.getPath())) {
                if (albumConfig.isRadio()) {
                    UCrop.of(Uri.fromFile(new File(albumModel.getPath())), imagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity)))
                            .withOptions(Album.getInstance().getOptions())
                            .start(albumActivity, this);
                } else {
                    Bundle bundle = new Bundle();
                    List<AlbumModel> albumModels = arrayMap.get(key);
                    bundle.putSerializable(AlbumConstant.PREVIEW_KEY, (Serializable) albumModels);
                    bundle.putInt(AlbumConstant.PREVIEW_POSITION_KEY, position);
                    bundle.putString(AlbumConstant.PREVIEW_POSITION_KEY, key);
                    Intent intent = new Intent(albumActivity, PreviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            } else {
                //onScanAlbum();
            }
        }
    }

    @Override
    public void onScanCompleted() {
        onScanAlbum();
    }


    @Override
    public void onScanAlbum() {
        if (PermissionUtils.storage(albumActivity)) {
            albumActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    albumPresenter.scan(albumActivity.getContentResolver(), albumConfig.isHideCamera());
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
        CameraUtil.openCamera(this, imagePath);
    }

    @Override
    public List<FinderModel> getFinderModel() {
        return finderModels;
    }

    @Override
    public void updateUI(String key) {
        this.key = key;
        if (arrayMap == null) {
            return;
        }
        List<AlbumModel> albumModels = arrayMap.get(key);
        // cache
        if (albumModels == null) {
            return;
        }
        albumAdapter.addAll(albumModels);
    }
}
