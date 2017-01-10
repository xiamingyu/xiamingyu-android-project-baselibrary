/**
 * Copyright (C) 2006-2014 Tuniu All rights reserved
 */
package com.ihomefnt.baselibrary.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.ihomefnt.baselibrary.R;
import com.ihomefnt.baselibrary.baseutil.DeviceUtils;
import com.ihomefnt.baselibrary.baseutil.LoggerUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;

public abstract class PicassoUtilDelegate {

    private static final String LOG_TAG = PicassoUtilDelegate.class.getSimpleName();

    public static void loadImage(Context context, String url, ImageView target) {
        if (url != null) {
            if (url.contains("qiniucdn.com") && url.contains("?")) {
                url = url + "/imageMogr2/format/webp";
            } else if (url.contains("qiniucdn.com")) {
                url = url + "?imageMogr2/format/webp";
            }
            loadImageWithRatio(context, url,target, DeviceUtils.getDisplayWidth(context));
        } else {
            loadImage(context, url, target, R.drawable.default_img);
        }
    }


    /**
     * load from local file
     *
     * @param context
     * @param file
     * @param transformation
     * @param target
     */
    public static void loadImage(Context context, File file, Transformation transformation, ImageView target) {
        Picasso picasso = Picasso.with(context);
        try {
            RequestCreator creator = picasso.load(file);
            if (transformation != null) {
                creator.transform(transformation);
            }
            if (target != null) {
                creator.into(target);
            }
        } catch (Exception e) {
            LoggerUtil.w(LOG_TAG, "picasso load image exception", e);
        }
    }

    /**
     * load from url
     *
     * @param context
     * @param url
     * @param transformation
     * @param target
     */
    public static void loadImage(Context context, String url, Transformation transformation, ImageView target) {
        Picasso picasso = Picasso.with(context);
        try {
            RequestCreator creator = picasso.load(url);
            if (transformation != null) {
                creator.transform(transformation);
            }
            if (target != null) {
                creator.into(target);
            }
        } catch (Exception e) {
            LoggerUtil.w(LOG_TAG, "picasso load image exception", e);
        }
    }

    public static void loadImageWithRatio(Context context, String url, ImageView target, final int width) {
        Picasso picasso = Picasso.with(context);
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {

                int targetWidth = width;

                if (source.getWidth() == 0) {
                    return source;
                }

                //如果图片小于设置的宽度，则返回原图
                if (source.getWidth() < targetWidth) {
                    return source;
                } else {
                    //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                    int targetHeight = (int) (targetWidth * aspectRatio);
                    if (targetHeight != 0 && targetWidth != 0) {
                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                        if (result != source) {
                            // Same bitmap is returned if sizes are the same
                            source.recycle();
                        }
                        return result;
                    } else {
                        return source;
                    }
                }
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";

            }
        };

        try {
            RequestCreator creator = picasso.load(url);
            if (transformation != null) {
                creator.transform(transformation);
            }
            if (target != null) {
                creator.into(target);
            }
        } catch (Exception e) {
            LoggerUtil.w(LOG_TAG, "picasso load image exception", e);
        }
    }

    public static void loadImageWithNoFade(Context context, String url, Transformation transformation, int placeholderRes, ImageView target) {
        Picasso picasso = Picasso.with(context);
        try {
            RequestCreator creator = picasso.load(url);
            if (transformation != null) {
                creator.transform(transformation);
            }
            creator.noFade();
            creator.placeholder(placeholderRes);
            if (target != null) {
                creator.into(target);
            }
        } catch (Exception e) {
            LoggerUtil.w(LOG_TAG, "picasso load image exception", e);
        }
    }

    /**
     * @param context
     * @param url
     * @param transformation
     * @param target
     * @param width          调整的图片宽度
     * @param height         调整的图片高度
     */
    public static void loadImage(Context context, String url, Transformation transformation, ImageView target, int width, int height) {
        Picasso picasso = Picasso.with(context);
        RequestCreator creator;
        try {
            if (url != null) {
                creator = picasso.load(url).resize(width, height).centerCrop();
            } else {
                creator = picasso.load(R.drawable.default_img).resize(width, height).centerCrop();
            }
            if (transformation != null) {
                creator.transform(transformation);
            }
            if (target != null) {
                creator.into(target);
            }
        } catch (Exception e) {
            LoggerUtil.w(LOG_TAG, "picasso load image exception", e);
        }
    }

    /**
     * @param context
     * @param url
     * @param transformation
     * @param target
     * @param width          调整的图片宽度
     * @param height         调整的图片高度
     * @param defaultImage   加载失败时默认图片
     */
    public static void loadImage(Context context, String url, Transformation transformation, ImageView target, int width, int height, Drawable defaultImage) {
        if (target != null) {
            target.setImageDrawable(defaultImage);
        }
        Picasso picasso = Picasso.with(context);
        try {
            RequestCreator creator = picasso.load(url).resize(width, height).centerCrop();
            if (transformation != null) {
                creator.transform(transformation);
            }
            if (target != null) {
                creator.into(target);
            }
        } catch (Exception e) {
            if (target != null) {
                target.setImageDrawable(defaultImage);
            }
            LoggerUtil.w(LOG_TAG, "picasso load image exception", e);
        }
    }

    public static void loadImage(Context context, String url, ImageView target, int resId) {
        if (target != null && url != null) {
            PicassoUtilDelegate.loadImage(context, url, target);
        } else {
            target.setImageResource(resId);
        }
    }


    /**
     * 产生圆角图片
     *
     * @param context
     * @param url
     * @param transformation
     * @param target
     * @param isRound
     */
    public static void loadImage(Context context, String url, Transformation transformation, ImageView target, boolean isRound) {
        Picasso picasso = Picasso.with(context);
        try {
            RequestCreator creator = picasso.load(url).fit();
            if (transformation != null) {
                creator.transform(transformation);
            }
            if (target != null) {
                creator.into(target);
            }
        } catch (Exception e) {
            LoggerUtil.w(LOG_TAG, "picasso load image exception", e);
        }
    }
}
