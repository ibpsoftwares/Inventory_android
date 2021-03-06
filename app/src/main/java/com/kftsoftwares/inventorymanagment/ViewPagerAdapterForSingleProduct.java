package com.kftsoftwares.inventorymanagment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kftsoftwares.inventorymanagment.activity.ZoomActivity;

import java.util.ArrayList;

/**
 * Created by apple on 20/02/18.
 */

public class ViewPagerAdapterForSingleProduct extends PagerAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<String> mArry;

    public ViewPagerAdapterForSingleProduct(Context mContext, ArrayList<String> mList) {
        this.mContext = mContext;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mArry = mList;
    }

    @Override
    public int getCount() {
        return mArry.size();

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

       final ImageView imageView = itemView.findViewById(R.id.imageView);

        final ProgressBar progressBar = itemView.findViewById(R.id.progress_Bar);
        // imageView.setImageResource(mImages[position]);
        Glide.with(mContext).load(mArry.get(position))
                .crossFade()
                .fitCenter()
                .dontTransform()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageResource(R.mipmap.placeholder);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        container.addView(itemView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ZoomActivity.class);
                i.putExtra("image",mArry.get(position));
                i.putExtra("position",position);
                mContext.startActivity(i);
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

}
