package com.album.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.album.AlbumConstant;

/**
 * by y on 17/08/2017.
 */
public class AlbumTool {
    private AlbumTool() {
    }

    public static boolean hasL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void log(Object o) {
        if (o != null) {
            Log.d(AlbumConstant.TAG, o.toString());
        }
    }

    public static Drawable getDrawable(Context context, int id, int color) {
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public static void openCamera(Fragment activity, Uri cameraUri) {
        if (PermissionUtils.camera(activity.getActivity())) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            } else {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, cameraUri.getPath());
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, AlbumConstant.ITEM_CAMERA);
                Uri uri = activity.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            activity.startActivityForResult(intent, AlbumConstant.ITEM_CAMERA);
        }
    }


    public static void setStatusBarColor(@ColorInt int color, Window window) {
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(color);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    mChildView.setFitsSystemWindows(true);
                }
                View statusBarView = new View(window.getContext());
                int statusBarHeight = 0;
                Resources res = window.getContext().getResources();
                int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = res.getDimensionPixelSize(resourceId);
                }
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
                params.gravity = Gravity.TOP;
                statusBarView.setLayoutParams(params);
                statusBarView.setBackgroundColor(color);
                mContentView.addView(statusBarView);
            }
        }
    }
}
