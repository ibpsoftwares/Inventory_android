package com.kftsoftwares.inventorymanagment.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by apple on 20/02/18.
 */

public class TextViewLight extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TextView";

    public TextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewLight(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/robotolight.ttf");
        setTypeface(tf);
    }
}
