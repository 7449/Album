package com.gallery.scan.annotation;

import androidx.annotation.IntDef;

import com.gallery.scan.ScanType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({ScanType.IMAGE, ScanType.VIDEO, ScanType.MIX, ScanType.NONE})
public @interface ScanTypeDef {
}