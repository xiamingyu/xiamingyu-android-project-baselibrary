package com.ihomefnt.baselibrary.layouthelp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import com.ihomefnt.baselibrary.R;
import com.ihomefnt.baselibrary.baseutil.DeviceUtils;

/**
 * Created by XiaMingYu
 */
public class RatioImageView extends ImageView {
	private static final String LOG_TAG = RatioImageView.class.getSimpleName();

    int ratioHeight;
    int ratioWidth;
    int ratioType;
    int length;

    public RatioImageView(Context context) {
        super(context);
        initProperty(null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperty(attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initProperty(attrs);
    }


    private void initProperty(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RatioImageView);

        DisplayMetrics metrics = DeviceUtils.getDisplayInfo(getContext());

        ratioHeight = a.getInt(R.styleable.RatioImageView_ratioHeight, 9);
        ratioWidth = a.getInt(R.styleable.RatioImageView_ratioWidth, 16);
        ratioType = a.getInt(R.styleable.RatioImageView_ratioType, 0);
        length = a.getInt(R.styleable.RatioImageView_length, metrics.widthPixels);

        a.recycle();
    }


    public void setLength(int length) {
    	this.length = length;
    }

    public void setRatioHeight(int ratioHeight) {
        this.ratioHeight = ratioHeight;
    }

    public void setRatioWidth(int ratioWidth) {
        this.ratioWidth = ratioWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics metrics = DeviceUtils.getDisplayInfo(getContext());
        /**
         * make to 16:9 ratio
         */
        int width = 0;
        int height = 0;
        /**
         * change dip to px
         */
        if (ratioType == 0) {
            width = length;
            height = width * ratioHeight / ratioWidth;
        } else {
            height = length;
            width = height * ratioWidth / ratioHeight;
        }
        
//        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
//                MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
        setMeasuredDimension(width, height);
    }
}
