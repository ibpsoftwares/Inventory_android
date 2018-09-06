package com.kftsoftwares.inventorymanagment;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kftsoftwares.inventorymanagment.model.ProductModel;

import java.util.ArrayList;

public class ProductDescription extends AppCompatActivity {

    ArrayList<ProductModel> mProductModel;
    ArrayList<String> mImages;
    LinearLayout mPager_dots;
    ViewPager mViewPager;
    private ImageView[] ivArrayDotsPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        mProductModel = new ArrayList<>();
        mImages = new ArrayList<>();
        TextView productName = findViewById(R.id.productName);
        TextView productCode = findViewById(R.id.productCode);
        TextView productDes = findViewById(R.id.productDetail);
        mViewPager = findViewById(R.id.viewPager);
        mPager_dots = findViewById(R.id.pager_dots);

        Bundle b = getIntent().getExtras();

        if (b != null) {
           /* if (b.getSerializable("data")!=null)
            {

                mProductModel.addAll((Collection<? extends ProductModel>) b.getSerializable("data"));
            }*/

            if (b.getString("image1") != null || !b.getString("image1").equalsIgnoreCase("null")) {
                mImages.add(b.getString("image1"));
            }
            if (b.getString("image2") != null || !b.getString("image1").equalsIgnoreCase("null")) {
                mImages.add(b.getString("image2"));

            }
            if (b.getString("image3") != null || !b.getString("image1").equalsIgnoreCase("null")) {
                mImages.add(b.getString("image3"));

            }
            if (b.getString("productname") != null || !b.getString("image1").equalsIgnoreCase("null")) {

                productName.setText(b.getString("productname"));

            }
            if (b.getString("productcode") != null || !b.getString("image1").equalsIgnoreCase("null")) {
                productCode.setText(b.getString("productcode"));

            }
            if (b.getString("productdes") != null || !b.getString("image1").equalsIgnoreCase("null")) {
                productDes.setText(b.getString("productdes"));

            }
            if (b.getString("invoice_image") != null || !b.getString("image1").equalsIgnoreCase("null")) {

            }

            ViewPagerAdapterForSingleProduct viewPagerAdapterForSingleProduct = new ViewPagerAdapterForSingleProduct(ProductDescription.this, mImages);
            mViewPager.setAdapter(viewPagerAdapterForSingleProduct);
            setupPagerIndidcatorDots();

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < ivArrayDotsPager.length; i++) {
                        ivArrayDotsPager[i].setBackground(ContextCompat.getDrawable(ProductDescription.this, R.mipmap.slide_white));
                    }
                    ivArrayDotsPager[position].setBackground(ContextCompat.getDrawable(ProductDescription.this, R.mipmap.slide_red));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


            Log.e("", String.valueOf(mImages));
        }

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setupPagerIndidcatorDots() {
        mPager_dots.removeAllViews();
        ivArrayDotsPager = new ImageView[mImages.size()];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(ProductDescription.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
            params.setMargins(10, 10, 10, 10);
            ivArrayDotsPager[i].setLayoutParams(params);
            //   ivArrayDotsPager[i].setBackground(ContextCompat.getDrawable(this,R.drawable.un_select));
            //ivArrayDotsPager[i].setAlpha(0.4f);
            ivArrayDotsPager[i].setBackground(ContextCompat.getDrawable(ProductDescription.this, R.mipmap.slide_white));
            ivArrayDotsPager[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivArrayDotsPager[i].setClickable(true);
            ivArrayDotsPager[i].setFocusable(true);
            ivArrayDotsPager[i].setDuplicateParentStateEnabled(true);
            final int val = i;
            ivArrayDotsPager[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(val);
                    view.setAlpha(1);
                }
            });

            mPager_dots.addView(ivArrayDotsPager[i]);
            mPager_dots.bringToFront();
        }

        ivArrayDotsPager[0].setBackground(ContextCompat.getDrawable(ProductDescription.this, R.mipmap.slide_red));

    }
}
