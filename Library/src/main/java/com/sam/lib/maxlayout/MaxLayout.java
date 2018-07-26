package com.sam.lib.maxlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

public class MaxLayout extends FrameLayout {

    private static final float DEFAULT_MAX_RATIO = 1f;
    private static final float DEFAULT_MAX_VALUE = 0f;

    private float mMaxHeight = DEFAULT_MAX_VALUE;// 优先级低
    private float mRatHeight = DEFAULT_MAX_RATIO;// 优先级高

    private float mMaxWidth = DEFAULT_MAX_VALUE;// 优先级低
    private float mRatWidth = DEFAULT_MAX_RATIO;// 优先级高

    public MaxLayout(Context context) {
        super(context);
        init(context);
    }

    public MaxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    public MaxLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxLayout);
        final int count = a.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MaxLayout_ml_HeightRatio) {
                mRatHeight = a.getFloat(attr, DEFAULT_MAX_RATIO);
            } else if (attr == R.styleable.MaxLayout_ml_HeightDimen) {
                mMaxHeight = a.getDimension(attr, DEFAULT_MAX_VALUE);
            } else if (attr == R.styleable.MaxLayout_ml_WidthRatio) {
                mRatWidth = a.getFloat(attr, DEFAULT_MAX_RATIO);
            } else if (attr == R.styleable.MaxLayout_ml_WidthDimen) {
                mMaxWidth = a.getDimension(attr, DEFAULT_MAX_VALUE);
            }
        }
        a.recycle();

    }

    private void init(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (mMaxHeight <= 0) {
            mMaxHeight = mRatHeight * (float) dm.heightPixels;
        } else {
            mMaxHeight = Math.min(mMaxHeight, mRatHeight * (float) dm.heightPixels);
        }
        if (mMaxWidth <= 0) {
            mMaxWidth = mRatWidth * (float) dm.widthPixels;
        } else {
            mMaxWidth = Math.min(mMaxWidth, mRatWidth * (float) dm.widthPixels);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(getMeasureSpec((int) mMaxWidth, widthMeasureSpec), getMeasureSpec((int) mMaxHeight, heightMeasureSpec));
    }

    private int getMeasureSpec(int max, int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            size = size <= max ? size : max;
        }

        if (mode == MeasureSpec.UNSPECIFIED) {
            size = size <= max ? size : max;
        }
        if (mode == MeasureSpec.AT_MOST) {
            size = size <= max ? size : max;
        }
        return MeasureSpec.makeMeasureSpec(size, mode);
    }

}