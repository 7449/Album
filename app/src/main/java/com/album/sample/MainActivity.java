package com.album.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;

public class MainActivity extends AppCompatActivity implements OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_day_album).setOnClickListener(this);
        findViewById(R.id.btn_night_album).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_day_album:
                Album.getInstance().setConfig(new AlbumConfig()).start(this);
                break;
            case R.id.btn_night_album:
                Album.getInstance().setConfig(new AlbumConfig(AlbumConstant.TYPE_NIGHT)).start(this);
                break;
        }
    }
}
