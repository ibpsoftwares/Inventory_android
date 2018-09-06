package com.kftsoftwares.inventorymanagment.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by apple on 20/02/18.
 */

public class TextViewLightItilic extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TextView";

    public TextViewLightItilic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TextViewLightItilic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewLightItilic(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/robotolightitalic.ttf");
        setTypeface(tf);
    }
}
