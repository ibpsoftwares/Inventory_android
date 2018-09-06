package com.kftsoftwares.inventorymanagment.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.Crashlytics;
import com.kftsoftwares.inventorymanagment.R;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class ZoomActivity extends AppCompatActivity {
    String mImageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getString("image") != null) {
                mImageData = b.getString("image");
            }

        }

        final ImageView zoomInZoomOut = findViewById(R.id.zoomImage);
        Glide.with(this).load(mImageData)
                .crossFade()
                .fitCenter()
                .dontTransform()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //  progressBar.setVisibility(View.GONE);
                        zoomInZoomOut.setImageResource(R.mipmap.placeholder);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //   progressBar.setVisibility(View.GONE);

                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(zoomInZoomOut);


        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
