/**
 * Copyright (C) 2006-2014 Tuniu All rights reserved
 */
package com.ihomefnt.baselibrary.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.ihomefnt.baselibrary.baseutil.LoggerUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

public class PicassoUtilForTarget extends PicassoUtilDelegate implements Target {

    private static final String LOG_TAG = PicassoUtilDelegate.class.getSimpleName();
    private static PicassoTargetListener mPicassoTargetListener;

    /**
     * 使用target缓存图片
     *
     * @param context
     * @param url      图片地址
     * @param listener 获取bitmap的监听
     */
    public static void saveImageCacheByTarget(Context context, String url, PicassoTargetListener listener) {
        mPicassoTargetListener = listener;
        Picasso picasso = Picasso.with(context);
        try {
            RequestCreator creator = picasso.load(url);
            creator.into(PicassoUtilForTarget.class.newInstance());
        } catch (Exception e) {
            LoggerUtil.w(LOG_TAG, "picasso load image exception");
        }
    }

    /**
     * 使用target缓存图片
     *
     * @param context 图片地址
     * @param url
     */
    public static void saveImageCacheByTarget(Context context, String url) {
        saveImageCacheByTarget(context, url, null);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        if (mPicassoTargetListener != null) {
            mPicassoTargetListener.onBitmapLoaded(bitmap);
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        if (mPicassoTargetListener != null) {
            mPicassoTargetListener.onBitmapLoadFail();
        }
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }

    public interface PicassoTargetListener {
        void onBitmapLoaded(Bitmap bitmap);

        void onBitmapLoadFail();
    }
}
