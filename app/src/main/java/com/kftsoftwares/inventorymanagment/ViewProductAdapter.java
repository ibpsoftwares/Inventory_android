package com.kftsoftwares.inventorymanagment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.kftsoftwares.inventorymanagment.model.ProductModel;

import java.util.ArrayList;


public class ViewProductAdapter extends RecyclerView.Adapter<ViewProductAdapter.MyViewHolder> {

    private Context mContext;

    private ArrayList<ProductModel> mArrayList;
    private Deleter_Edit_Interface mInterface;
    private int mType;


    public ViewProductAdapter(Context mContext, ArrayList<ProductModel> arrayList, Deleter_Edit_Interface deleter_edit_interface) {
        this.mContext = mContext;
        mArrayList = arrayList;
        mInterface = deleter_edit_interface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_single_new, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int positions) {


        holder.productName.setText(mArrayList.get(positions).getName());
        if (mArrayList!=null && mArrayList.size()>0) {
          //  if (!mArrayList.get(positions).isValueSet())
           // {


                if (mArrayList.get(positions).getImage().equalsIgnoreCase("") || mArrayList.get(positions).getImage().equalsIgnoreCase("null")) {

                    if (mArrayList.get(positions).getImage2().equalsIgnoreCase("") || mArrayList.get(positions).getImage2().equalsIgnoreCase("null")) {
                        holder.productImage.setImageResource(R.mipmap.placeholder);

                    } else {

                        Glide.with(mContext)
                                .load(mArrayList.get(positions).getImage2())
                                .placeholder(R.mipmap.placeholder)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        holder.productImage.setImageResource(R.mipmap.placeholder);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                        holder.productImage.setImageDrawable(resource);

                                        return false;
                                    }
                                })
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<GlideDrawable>() {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        holder.productImage.setImageDrawable(resource);

                                    }
                                });
                    }
                } else {
                    Glide.with(mContext)

                            .load(mArrayList.get(positions).getImage())
                            .placeholder(R.mipmap.placeholder)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                    holder.productImage.setImageResource(R.mipmap.placeholder);

                                    if (mArrayList.get(positions).getImage2().equalsIgnoreCase("") || mArrayList.get(positions).getImage2().equalsIgnoreCase("null")) {
                                        holder.productImage.setImageResource(R.mipmap.placeholder);
                                    } else {

                                        Glide.with(mContext)
                                                .load(mArrayList.get(positions).getImage2())
                                                .placeholder(R.mipmap.placeholder)
                                                .listener(new RequestListener<String, GlideDrawable>() {
                                                    @Override
                                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                        holder.productImage.setImageResource(R.mipmap.placeholder);
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                                        holder.productImage.setImageDrawable(resource);
                                                        return false;
                                                    }
                                                })
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(new SimpleTarget<GlideDrawable>() {
                                                    @Override
                                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                        holder.productImage.setImageDrawable(resource);

                                                    }
                                                });
                                    }
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                    holder.productImage.setImageDrawable(resource);

                                    return false;
                                }
                            })
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new SimpleTarget<GlideDrawable>() {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    holder.productImage.setImageDrawable(resource);

                                }
                            });
                }

                mArrayList.get(positions).setValueSet(true);
       // }
      //  else {

          //      Log.e("","Already Set");

        //    }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDescription.class);
                intent.putExtra("image1", mArrayList.get(positions).getImage());
                intent.putExtra("image2", mArrayList.get(positions).getImage2());
                intent.putExtra("image3", mArrayList.get(positions).getImage3());
                intent.putExtra("productname", mArrayList.get(positions).getName());
                intent.putExtra("productcode", mArrayList.get(positions).getProduct_code());
                intent.putExtra("productdes", mArrayList.get(positions).getDescription());
                intent.putExtra("invoice_image", mArrayList.get(positions).getInvoice_image());
                mContext.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.method(0, mArrayList.get(positions).getProductID());
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  mInterface.method(1,mArrayList.get(positions).getProductID());
                mInterface.ArrayList(mArrayList, positions);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        ImageView productImage, delete, edit;

        private MyViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productName);
            productImage = view.findViewById(R.id.productImage);
            delete = view.findViewById(R.id.imageHeart);
            edit = view.findViewById(R.id.edit);

        }
    }

    public void updateData(ArrayList<ProductModel> rrayList) {
        if (mArrayList != null) {
            mArrayList.clear();
        }
        mArrayList.addAll(rrayList);
        notifyDataSetChanged();
    }

    public void addUpdateData(ArrayList<ProductModel> rrayList) {

        mArrayList.addAll(rrayList);
    }

    // Add at the top of the list.

    public void addArticleFirst(ArrayList<ProductModel> list) {
        if (mArrayList != null) {
            mArrayList.clear();
        }
        mArrayList.addAll(0, list);
        notifyDataSetChanged();
    }

    // Add at the end of the list.

    public void addArticleLast(ArrayList<ProductModel> list) {
        mArrayList.addAll(list);
       // notifyDataSetChanged();
    }

}
