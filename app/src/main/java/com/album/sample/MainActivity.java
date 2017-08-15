package com.album.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.album.Album;
import com.album.AlbumConfig;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private AlbumConfig albumConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_radio_image).setOnClickListener(this);
        albumConfig = new AlbumConfig()
                .setHideCamera(true)
                .setAlbumToolbarBackground(R.color.colorAlbumBlack)
                .setAlbumStatusBarColor(R.color.colorAlbumBlack)
                .setAlbumToolbarIcon(R.drawable.ic_action_preview_check)
                .setAlbumToolbarTextColor(R.color.colorAlbumPrimary);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_radio_image:
                Album.getInstance().setConfig(albumConfig).start(this);
                break;
        }
    }
}
