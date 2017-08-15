package com.album.sample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.yalantis.ucrop.UCrop;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private UCrop.Options dayOptions;
    private UCrop.Options nightOptions;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_day_album:
                Album.getInstance().setOptions(dayOptions).setConfig(new AlbumConfig()).start(this);
                break;
            case R.id.btn_night_album:
                Album.getInstance().setOptions(nightOptions).setConfig(new AlbumConfig(AlbumConstant.TYPE_NIGHT).setRadio(true)).start(this);
                break;
        }
    }
}
