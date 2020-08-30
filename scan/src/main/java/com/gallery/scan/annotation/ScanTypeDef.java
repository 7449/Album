package com.gallery.scan.annotation;

import androidx.annotation.IntDef;

import com.gallery.scan.types.ScanType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({ScanType.NONE, ScanType.MIX, ScanType.IMAGE, ScanType.VIDEO, ScanType.AUDIO})
public @interface ScanTypeDef {
}