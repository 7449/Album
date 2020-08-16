package com.gallery.scan.annotation;

import androidx.annotation.StringDef;

import com.gallery.scan.Sort;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({Sort.ASC, Sort.DESC})
public @interface SortDef {
}