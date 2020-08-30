package com.gallery.scan.annotation;

import androidx.annotation.StringDef;

import com.gallery.scan.args.Columns;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        Columns.SIZE,
        Columns.PARENT,
        Columns.MIME_TYPE,
        Columns.DISPLAY_NAME,
        Columns.MEDIA_TYPE,
        Columns.DATE_ADDED,
        Columns.DATE_MODIFIED,
        Columns.BUCKET_ID,
        Columns.BUCKET_DISPLAY_NAME
})
public @interface SortFieldDef {
}
