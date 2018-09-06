package com.kftsoftwares.inventorymanagment.activity;

//testing
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.util.ArrayUtils;
import com.kftsoftwares.inventorymanagment.model.Categories;
import com.kftsoftwares.inventorymanagment.model.Location;
import com.kftsoftwares.inventorymanagment.model.ProductModel;
import com.kftsoftwares.inventorymanagment.R;
import com.kftsoftwares.inventorymanagment.util.AppController;
import com.kftsoftwares.inventorymanagment.util.EditText;
import com.kftsoftwares.inventorymanagment.util.SingletonRequestQueue;
import com.kftsoftwares.inventorymanagment.util.Utility;
import com.kftsoftwares.inventorymanagment.util.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static com.android.volley.Request.Method.POST;
import static com.kftsoftwares.inventorymanagment.util.Constants.ADDPRODUCT;
import static com.kftsoftwares.inventorymanagment.util.Constants.EDIT_PRODUCT;
import static com.kftsoftwares.inventorymanagment.util.Constants.GET_SELECTOR_DATA;

public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    EditText mProductName, mProductCode, mProductMake, mProductModel, mProductDescription, mEnterQuantity, mEnterAdditionalDetail, mNameOfEmployee, mEnterEmployeeId;
    Spinner mCategorySpinner, mLocation, mLocationSub, mDepartment, mTeamLeader, mSubCategorySpinner2, mLocationSubCatSpinner3, mSubCategorySpinner;
    TextView mImage1, mImage2, mImage3, mInvoice, mSelectInvoice, mPutInDate, mSelectExpireDate;
    private ArrayList<Leader> mLeader;
    private ArrayList<String> mLeaderName;
    private ArrayList<Location> mLocationArrayList, mLocationArrayList1;
    private ArrayList<Categories> mCategories, mCategories1, mCategories2, mCategories3;
    private LinearLayout mLinerLayoutSubLocation, mLayoutSubCategory, mLayoutSubCategory2, mLayoutSubCategory3;

    private   String mDepartmentArray[] = {"Admin", "Accounts", "IT", "HR"};
    private String mParentCatId, mCatId, mLocationID;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView mImageView;
    private int day, month, year, mUserSelectedImage;
    private Calendar mCalendar;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1001;
    private Drawable mImageDrawable1ForWeb, mImageDrawable2ForWeb, mImageDrawable3ForWeb, mInvoiceDrawableForWeb;
    private String mCurrentDateandTime, mCurrentDateandTimeForWeb;
    private Uri mFilePath;
    private byte pdfarray[];
    private int mCategorySelected = -1;
    private String mValue="", mId;

    private ArrayList<ProductModel> mProductModelList;
    private int mPosition;


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);

        mProductModelList = new ArrayList<>();
        Intent intent = getIntent();

        Bundle b = intent.getBundleExtra("bundle");

        if (b != null) {

            mValue = b.getString("value");
            if (mValue.equalsIgnoreCase("edit")) {

                if (b.getSerializable("list") != null) {

                    mProductModelList.addAll((Collection<? extends ProductModel>) b.getSerializable("list"));
                }

                mPosition = b.getInt("position");

            }
        }

        mProductName = findViewById(R.id.productName);
        mProductCode = findViewById(R.id.productCode);
        mProductMake = findViewById(R.id.productMake);
        mProductModel = findViewById(R.id.productModel);
        mCategorySpinner = findViewById(R.id.categorySpinner);
        mProductDescription = findViewById(R.id.productDesc);
        mEnterQuantity = findViewById(R.id.enterQuantity);
        mImage1 = findViewById(R.id.image1);
        mImage2 = findViewById(R.id.image2);
        mImage3 = findViewById(R.id.image3);
        mInvoice = findViewById(R.id.invoice);
        mSelectInvoice = findViewById(R.id.selectInvoice);
        mPutInDate = findViewById(R.id.putInDate);
        mSelectExpireDate = findViewById(R.id.selectExpireDate);
        mEnterAdditionalDetail = findViewById(R.id.enterAdditionalDetail);
        mLocation = findViewById(R.id.location);
        mDepartment = findViewById(R.id.department);
        mNameOfEmployee = findViewById(R.id.nameOfEmployee);
        mEnterEmployeeId = findViewById(R.id.enterEmployeeId);
        mTeamLeader = findViewById(R.id.teamLeader);
        mLocationSub = findViewById(R.id.locationSub);
        mLayoutSubCategory = findViewById(R.id.layoutSubCategory);
        mLayoutSubCategory2 = findViewById(R.id.layoutSubCategory2);
        mLayoutSubCategory3 = findViewById(R.id.layoutSubCategory3);
        mLinerLayoutSubLocation = findViewById(R.id.layoutSubLocation);
        mSubCategorySpinner = findViewById(R.id.locationSubCat);
        mSubCategorySpinner2 = findViewById(R.id.locationSubCat2);
        mLocationSubCatSpinner3 = findViewById(R.id.locationSubCat3);
        mImageView = findViewById(R.id.testingImage);
        ImageView imageBack = findViewById(R.id.imageBack);
        ScrollView scrollView = findViewById(R.id.scrollView);
        mLeader = new ArrayList<>();
        mLeaderName = new ArrayList<>();
        mLocationArrayList = new ArrayList<>();
        mLocationArrayList1 = new ArrayList<>();
        mCategories1 = new ArrayList<>();
        mCategories = new ArrayList<>();
        mCategories2 = new ArrayList<>();
        mCategories3 = new ArrayList<>();
        getSelectorData();


        mCalendar = new GregorianCalendar();
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.getDefault());
        mCurrentDateandTime = sdf.format(new Date());
        mCurrentDateandTime = mCurrentDateandTime.replace(" ", "");


        mLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (mLocationArrayList.get(position).getChild_String() != null && !mLocationArrayList.get(position).getChild_String().equalsIgnoreCase("NO CHILD")) {
                    ArrayList<String> LocationParent = new ArrayList<>();

                    mLocationSub.setVisibility(View.VISIBLE);
                    mLinerLayoutSubLocation.setVisibility(View.VISIBLE);
                    Location location;

                    if (mLocationArrayList1 != null) {

                        mLocationArrayList1.clear();
                    }

                    try {
                        JSONArray jsonArray = new JSONArray(mLocationArrayList.get(position).getChild_String());

                        for (int j = 0; j < jsonArray.length(); j++) {

                            location = new Location();
                            JSONObject childrenObject = jsonArray.getJSONObject(j);

                            location.setParent(childrenObject.getString("parent"));
                            location.setLid(childrenObject.getString("lid"));
                            location.setLocation(childrenObject.getString("location"));
                            if (childrenObject.has("children")) {

                                location.setChild_String(String.valueOf(childrenObject.getJSONArray("children")));

                            } else {
                                location.setChild_String("NO CHILD");

                            }
                            LocationParent.add(childrenObject.getString("location"));
                            mLocationArrayList1.add(location);
                        }
                        updateLocationSubSpinner();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    mLocationSub.setVisibility(View.GONE);
                    mLinerLayoutSubLocation.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (mCategories1 != null) {
                    mCategories1.clear();
                }

                mCategorySelected = position;
                if (mCategories.get(position).getChildren() != null && !mCategories.get(position).getChildren().equalsIgnoreCase("NO CHILD")) {

                    mSubCategorySpinner.setVisibility(View.VISIBLE);
                    mLayoutSubCategory.setVisibility(View.VISIBLE);

                    mSubCategorySpinner2.setVisibility(View.VISIBLE);
                    mLayoutSubCategory2.setVisibility(View.VISIBLE);


                    mLocationSubCatSpinner3.setVisibility(View.VISIBLE);
                    mLayoutSubCategory3.setVisibility(View.VISIBLE);
                    try {
                        JSONArray jsonArray = new JSONArray(mCategories.get(position).getChildren());

                        Categories categories;
                        for (int j = 0; j < jsonArray.length(); j++) {
                            categories = new Categories();
                            JSONObject childrenObject = jsonArray.getJSONObject(j);

                            categories.setParent(childrenObject.getString("parent"));
                            categories.setCategoryID(childrenObject.getString("categoryID"));
                            categories.setName(childrenObject.getString("name"));
                            if (childrenObject.has("children")) {

                                categories.setChildren(String.valueOf(childrenObject.getJSONArray("children")));

                            } else {
                                categories.setChildren("NO CHILD");

                            }
                            mCategories1.add(categories);
                        }

                        updateSubCategory();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    mSubCategorySpinner.setVisibility(View.GONE);
                    mLayoutSubCategory.setVisibility(View.GONE);

                    mSubCategorySpinner2.setVisibility(View.GONE);
                    mLayoutSubCategory2.setVisibility(View.GONE);

                    mLocationSubCatSpinner3.setVisibility(View.GONE);
                    mLayoutSubCategory3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        mSubCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                if (mCategories2 != null) {
                    mCategories2.clear();
                }
                if (mCategories1.get(position).getChildren() != null && !mCategories1.get(position).getChildren().equalsIgnoreCase("NO CHILD")) {

                    mSubCategorySpinner2.setVisibility(View.VISIBLE);
                    mLayoutSubCategory2.setVisibility(View.VISIBLE);

                    mLocationSubCatSpinner3.setVisibility(View.VISIBLE);
                    mLayoutSubCategory3.setVisibility(View.VISIBLE);

                    try {
                        JSONArray jsonArray = new JSONArray(mCategories1.get(position).getChildren());

                        Categories categories;
                        for (int j = 0; j < jsonArray.length(); j++) {
                            categories = new Categories();
                            JSONObject childrenObject = jsonArray.getJSONObject(j);

                            categories.setParent(childrenObject.getString("parent"));
                            categories.setCategoryID(childrenObject.getString("categoryID"));
                            categories.setName(childrenObject.getString("name"));
                            if (childrenObject.has("children")) {

                                categories.setChildren(String.valueOf(childrenObject.getJSONArray("children")));

                            } else {
                                categories.setChildren("NO CHILD");

                            }
                            mCategories2.add(categories);
                        }

                        updateSubCategory2();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    mSubCategorySpinner2.setVisibility(View.GONE);
                    mLayoutSubCategory2.setVisibility(View.GONE);

                    mLocationSubCatSpinner3.setVisibility(View.GONE);
                    mLayoutSubCategory3.setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        mSubCategorySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                if (mCategories3 != null) {
                    mCategories3.clear();
                }
                if (mCategories2.get(position).getChildren() != null && !mCategories2.get(position).getChildren().equalsIgnoreCase("NO CHILD")) {

                    mLocationSubCatSpinner3.setVisibility(View.VISIBLE);
                    mLayoutSubCategory3.setVisibility(View.VISIBLE);

                    try {
                        JSONArray jsonArray = new JSONArray(mCategories2.get(position).getChildren());

                        Categories categories;
                        for (int j = 0; j < jsonArray.length(); j++) {
                            categories = new Categories();
                            JSONObject childrenObject = jsonArray.getJSONObject(j);

                            categories.setParent(childrenObject.getString("parent"));
                            categories.setCategoryID(childrenObject.getString("categoryID"));
                            categories.setName(childrenObject.getString("name"));
                            if (childrenObject.has("children")) {

                                categories.setChildren(String.valueOf(childrenObject.getJSONArray("children")));

                            } else {
                                categories.setChildren("NO CHILD");

                            }
                            mCategories3.add(categories);
                        }

                        updateSubCategory3();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    mLocationSubCatSpinner3.setVisibility(View.GONE);
                    mLayoutSubCategory3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        ArrayAdapter<String> departmentArray = new ArrayAdapter<String>(AddProduct.this, R.layout.spinner_layout, mDepartmentArray);
        mDepartment.setAdapter(departmentArray);

        final Button addProduct = findViewById(R.id.addProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mLayoutSubCategory.getVisibility() == View.VISIBLE) {

                    if (mLayoutSubCategory2.getVisibility() == View.VISIBLE) {

                        if (mLayoutSubCategory3.getVisibility() == View.VISIBLE) {
                            mParentCatId = mCategories3.get(mLocationSubCatSpinner3.getSelectedItemPosition()).getParent();
                            mCatId = mCategories3.get(mLocationSubCatSpinner3.getSelectedItemPosition()).getCategoryID();


                        } else {

                            mParentCatId = mCategories2.get(mSubCategorySpinner2.getSelectedItemPosition()).getParent();
                            mCatId = mCategories2.get(mSubCategorySpinner2.getSelectedItemPosition()).getCategoryID();


                        }

                    } else {
                        mParentCatId = mCategories1.get(mSubCategorySpinner.getSelectedItemPosition()).getParent();
                        mCatId = mCategories1.get(mSubCategorySpinner.getSelectedItemPosition()).getCategoryID();


                    }


                } else {
                    mParentCatId = mCategories.get(mCategorySpinner.getSelectedItemPosition()).getParent();
                    mCatId = mCategories.get(mCategorySpinner.getSelectedItemPosition()).getCategoryID();
                }

                if (mLinerLayoutSubLocation.getVisibility() == View.VISIBLE) {
                    mLocationID = mLocationArrayList1.get(mLocationSub.getSelectedItemPosition()).getLid();

                } else {
                    mLocationID = mLocationArrayList.get(mLocation.getSelectedItemPosition()).getLid();
                }



                if (mProductName.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(AddProduct.this, "Product Name Field is Required", Toast.LENGTH_SHORT).show();

                }
                else if (mCategorySelected == -1 ||  mCategorySelected==0) {

                    Toast.makeText(AddProduct.this, "Category Field is Required", Toast.LENGTH_SHORT).show();

                }
                else if (mNameOfEmployee.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(AddProduct.this, "Assigned Name Field is Required", Toast.LENGTH_SHORT).show();

                } else if (mEnterEmployeeId.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(AddProduct.this, "Assigned Emp Id Field is Required", Toast.LENGTH_SHORT).show();

                } else {
                    addProductTesting();
                }
            }
        });


        mSelectInvoice.setOnClickListener(this);
        mPutInDate.setOnClickListener(this);
        mSelectExpireDate.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        mImage1.setOnClickListener(this);
        mImage2.setOnClickListener(this);
        mImage3.setOnClickListener(this);
        mInvoice.setOnClickListener(this);


        if (mValue.equalsIgnoreCase("edit"))
        {
            mEnterAdditionalDetail.setText(mProductModelList.get(mPosition).getAdditional_info());
            mNameOfEmployee.setText(mProductModelList.get(mPosition).getAssigned_name());
            mEnterEmployeeId.setText(mProductModelList.get(mPosition).getAssigned_name());
            mProductCode.setText(mProductModelList.get(mPosition).getAssigned_id());
            mProductMake.setText(mProductModelList.get(mPosition).getMake());
            mProductName.setText(mProductModelList.get(mPosition).getName());
            mProductModel.setText(mProductModelList.get(mPosition).getModel());
            mProductDescription.setText(mProductModelList.get(mPosition).getDescription());
            mEnterQuantity.setText(mProductModelList.get(mPosition).getProduct_quantity());
            mSelectInvoice.setText(mProductModelList.get(mPosition).getInvoice_date());
            mPutInDate.setText(mProductModelList.get(mPosition).getPutin_date());
            mPutInDate.setText(mProductModelList.get(mPosition).getPutin_date());
            int index =  ArrayUtils.indexOf(mDepartmentArray,mProductModelList.get(mPosition).getDepartment());

            mDepartment.setSelection(index);


        }

        scrollView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp)
                    {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
    }




    private void updateLocationSubSpinner() {
        ArrayList<String> LocationParent = new ArrayList<>();

        for (int i = 0; i < mLocationArrayList1.size(); i++) {
            LocationParent.add(mLocationArrayList1.get(i).getLocation());
        }

        int locationParentId = 0, locationChildId=0;


        ArrayAdapter<String> locations = new ArrayAdapter<String>(AddProduct.this, R.layout.spinner_layout, LocationParent);

        mLocationSub.setAdapter(locations);

        if (mProductModelList.size()>0) {

            for (int i = 0; i < mLocationArrayList1.size(); i++) {

                if (mLocationArrayList1.get(i).getLid().equalsIgnoreCase(mProductModelList.get(mPosition).getLocation_id())) {

                    for (int j = 0; j < mLocationArrayList.size(); j++) {
                        if (mLocationArrayList.get(j).getLid().equalsIgnoreCase(mLocationArrayList1.get(i).getParent())) {
                            locationParentId = j;

                        }

                    }
                   locationChildId = i;

                } else {

                    for (int j = 0; j < mLocationArrayList.size(); j++) {
                        if (mLocationArrayList.get(j).getLid().equalsIgnoreCase(mProductModelList.get(mPosition).getLocation_id())) {
                            locationParentId = j;

                        }

                    }
                }
            }

            mLocation.setSelection(locationParentId);
            mLocationSub.setSelection(locationChildId);
        }
    }

    private void addProductTesting() {


        /*
         * These variables are for testing...
         */

        String catID = mCatId;
        String productId = mParentCatId;
        String loc = mLocationID;
        String currentDateandTime = mCurrentDateandTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        mCurrentDateandTimeForWeb = sdf.format(new Date());

        if (mValue.equalsIgnoreCase("edit")) {
            uploadEdiBitmap();
        }
        else {
            uploadBitmap();
        }
    }

    private void updateSubCategory() {

        ArrayList<String> LocationParent = new ArrayList<>();

        for (int i = 0; i < mCategories1.size(); i++) {
            LocationParent.add(mCategories1.get(i).getName());
        }

        ArrayAdapter<String> locations = new ArrayAdapter<String>(AddProduct.this, R.layout.spinner_layout, LocationParent);
        mSubCategorySpinner.setAdapter(locations);


        if (mProductModelList.size()>0) {

            int parentID = 0, childID = 0;
            if (!mProductModelList.get(mPosition).getMain_cat().equalsIgnoreCase("0")) {
                mSubCategorySpinner.setVisibility(View.VISIBLE);
                mLayoutSubCategory.setVisibility(View.VISIBLE);

                mSubCategorySpinner2.setVisibility(View.GONE);
                mLayoutSubCategory2.setVisibility(View.GONE);

                mLocationSubCatSpinner3.setVisibility(View.GONE);
                mLayoutSubCategory3.setVisibility(View.GONE);
                for (int i = 0; i < mCategories1.size(); i++) {
                    if (mProductModelList.get(mPosition).getCategoryID().equalsIgnoreCase(mCategories1.get(i).getCategoryID())) {

                        childID =i;
                    }
                }

            } else {
                mSubCategorySpinner.setVisibility(View.GONE);
                mLayoutSubCategory.setVisibility(View.GONE);

                mSubCategorySpinner2.setVisibility(View.GONE);
                mLayoutSubCategory2.setVisibility(View.GONE);

                mLocationSubCatSpinner3.setVisibility(View.GONE);
                mLayoutSubCategory3.setVisibility(View.GONE);
                for (int i = 0; i < mCategories.size(); i++) {
                    if (mProductModelList.get(mPosition).getMain_cat().equalsIgnoreCase(mCategories.get(i).getCategoryID())) {
                      //  mCategorySpinner.setSelection(i);
                    }

                }

            }

            mSubCategorySpinner.setSelection(childID);
        }


    }

    private void updateSubCategory2() {

        ArrayList<String> LocationParent = new ArrayList<>();

        for (int i = 0; i < mCategories2.size(); i++) {
            LocationParent.add(mCategories2.get(i).getName());
        }

        ArrayAdapter<String> locations = new ArrayAdapter<String>(AddProduct.this, R.layout.spinner_layout, LocationParent);
        mSubCategorySpinner2.setAdapter(locations);






    }

    private void updateSubCategory3() {

        ArrayList<String> LocationParent = new ArrayList<>();

        for (int i = 0; i < mCategories3.size(); i++) {
            LocationParent.add(mCategories3.get(i).getName());
        }

        ArrayAdapter<String> locations = new ArrayAdapter<String>(AddProduct.this, R.layout.spinner_layout, LocationParent);
        mLocationSubCatSpinner3.setAdapter(locations);
    }

    private void getSelectorData() {

        if (mCategories != null) {
            mCategories.clear();
        }


        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(POST,
                GET_SELECTOR_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray leader = jsonObject.getJSONArray("leaders");
                    JSONArray location = jsonObject.getJSONArray("location");
                    JSONArray categories = jsonObject.getJSONArray("categories");

                    Location location1 = null;
                    Categories categories12 = new Categories();
                    categories12.setParent("");
                    categories12.setCategoryID("");
                    categories12.setName("--Select--");

                    mCategories.add(categories12);

                    Categories categories1;


                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject jsonObject1 = categories.getJSONObject(i);
                        categories1 = new Categories();
                        categories1.setParent(jsonObject1.getString("parent"));
                        categories1.setCategoryID(jsonObject1.getString("categoryID"));
                        categories1.setName(jsonObject1.getString("name"));
                        if (jsonObject1.has("children")) {

                            categories1.setChildren(String.valueOf(jsonObject1.getJSONArray("children")));

                        } else {
                            categories1.setChildren("NO CHILD");

                        }

                        mCategories.add(categories1);

                    }


                    for (int i = 0; i < location.length(); i++) {
                        JSONObject jsonObject1 = location.getJSONObject(i);
                        location1 = new Location();
                        location1.setParent(jsonObject1.getString("parent"));
                        location1.setLid(jsonObject1.getString("lid"));
                        location1.setLocation(jsonObject1.getString("location"));
                        if (jsonObject1.has("children")) {

                            location1.setChild_String(String.valueOf(jsonObject1.getJSONArray("children")));

                        } else {
                            location1.setChild_String("NO CHILD");

                        }

                        mLocationArrayList.add(location1);

                    }

                    Leader leader1 = null;
                    for (int i = 0; i < leader.length(); i++) {
                        leader1 = new Leader();
                        JSONObject jsonObject1 = leader.getJSONObject(i);
                        leader1.setDate(jsonObject1.getString("date"));
                        leader1.setId(jsonObject1.getString("id"));
                        leader1.setTl_id(jsonObject1.getString("tl_id"));
                        leader1.setTl_name(jsonObject1.getString("tl_name"));
                        mLeaderName.add(jsonObject1.getString("tl_name"));
                        mLeader.add(leader1);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddProduct.this, R.layout.spinner_layout, mLeaderName);

                    mTeamLeader.setAdapter(arrayAdapter);
                    if(mValue.equalsIgnoreCase("edit")) {

                        int teamLeader = mLeaderName.indexOf(mProductModelList.get(mPosition).getTeam_leader());
                        mTeamLeader.setSelection(teamLeader);
                    }
                    updateLocationSpinner();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Toast.makeText(AddProduct.this, "Request Timeout", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(AddProduct.this, "Authentication Failure", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(AddProduct.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(AddProduct.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(AddProduct.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void updateLocationSpinner() {

        ArrayList<String> LocationParent = new ArrayList<>();

        int locationSelection = 0;

        for (int i = 0; i < mLocationArrayList.size(); i++) {
            LocationParent.add(mLocationArrayList.get(i).getLocation());

        }

        ArrayAdapter<String> locations = new ArrayAdapter<String>(AddProduct.this, R.layout.spinner_layout, LocationParent);

        mLocation.setAdapter(locations);


        if (mProductModelList.size()>0) {

            int parentLocation = 0;

            for (int i = 0; i < mLocationArrayList.size(); i++) {

                if (mLocationArrayList.get(i).getLid().equalsIgnoreCase(mProductModelList.get(mPosition).getLocation_id())) {
                    parentLocation=i;

                    }

                 else {
                    mLocationSub.setVisibility(View.GONE);

                }
            }

            mLocation.setSelection(parentLocation);
        }






        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < mCategories.size(); i++) {
            data.add(mCategories.get(i).getName());
        }

        ArrayAdapter<String> locations1 = new ArrayAdapter<String>(AddProduct.this, R.layout.spinner_layout, data);

        mCategorySpinner.setAdapter(locations1);

        if (mProductModelList.size()>0) {

            int parentID=0;
            if (mProductModelList.get(mPosition).getMain_cat().equalsIgnoreCase("0")) {
                mSubCategorySpinner.setVisibility(View.GONE);
                mLayoutSubCategory.setVisibility(View.GONE);

                mSubCategorySpinner2.setVisibility(View.GONE);
                mLayoutSubCategory2.setVisibility(View.GONE);

                mLocationSubCatSpinner3.setVisibility(View.GONE);
                mLayoutSubCategory3.setVisibility(View.GONE);
                for (int i = 0; i < mCategories.size(); i++) {
                    if (mProductModelList.get(mPosition).getCategoryID().equalsIgnoreCase(mCategories.get(i).getCategoryID()))
                    {
                        parentID=i;
                    }

                }


            } else {

                Log.e("","");

                for (int i = 0; i < mCategories.size(); i++) {
                    if (mProductModelList.get(mPosition).getMain_cat().equalsIgnoreCase(mCategories.get(i).getCategoryID()))
                    {
                        parentID=i;
                    }

                }


            }

            mCategorySpinner.setSelection(parentID);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectInvoice:

                DateDialog(1);
                break;

            case R.id.putInDate:
                DateDialog(2);

                break;

            case R.id.selectExpireDate:
                DateDialog(3);

                break;
            case R.id.image1:

                selectImage(1);
                break;
            case R.id.image2:
                selectImage(2);

                break;
            case R.id.image3:
                selectImage(3);

                break;
            case R.id.invoice:
                showFileChooser();

                break;
            case R.id.imageBack:
                finish();

                break;

        }

    }

    class Leader {
        String id, tl_id, tl_name, date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTl_id() {
            return tl_id;
        }

        public void setTl_id(String tl_id) {
            this.tl_id = tl_id;
        }

        public String getTl_name() {
            return tl_name;
        }

        public void setTl_name(String tl_name) {
            this.tl_name = tl_name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    private void uploadBitmap() {
        final ProgressDialog pDialog = new ProgressDialog(AddProduct.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ADDPRODUCT1, new Response.Listener<NetworkResponse>() {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(POST, ADDPRODUCT, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                pDialog.cancel();
                pDialog.dismiss();
                String resultResponse = new String(response.data);

                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        Toast.makeText(AddProduct.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(AddProduct.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                pDialog.cancel();
                pDialog.dismiss();
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("main_cat", mParentCatId);
                params.put("product_code", mProductCode.getText().toString());
                params.put("description", mProductDescription.getText().toString());
                params.put("department", mDepartmentArray[mDepartment.getSelectedItemPosition()]);
                params.put("assigned_name", mNameOfEmployee.getText().toString());
                params.put("product_quantity", mEnterQuantity.getText().toString());
                params.put("location_id", mLocationID);
                params.put("assigned_id", mEnterEmployeeId.getText().toString());
                params.put("addedon", mCurrentDateandTimeForWeb);
                params.put("expire_date", mSelectExpireDate.getText().toString());
                params.put("team_leader", mLeaderName.get(mTeamLeader.getSelectedItemPosition()));
                params.put("added_by", "");
                params.put("make", mProductMake.getText().toString());
                params.put("model", mProductModel.getText().toString());
                params.put("additional_info", mEnterAdditionalDetail.getText().toString());
                params.put("invoice_date", mSelectInvoice.getText().toString());
                params.put("putin_date", mPutInDate.getText().toString());
                params.put("name", mProductName.getText().toString());
                params.put("categoryID", mCatId);

                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+access);
                params.put("Accept", "application/json");

                return params;
            }*/


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                if (mImageDrawable1ForWeb != null) {
                    params.put("image", new DataPart(mCurrentDateandTime + "1.jpg", AppController.getFileDataFromDrawable(AddProduct.this, mImageDrawable1ForWeb), "image/jpeg"));


                }

                if (mImageDrawable2ForWeb != null) {
                    params.put("image2", new DataPart(mCurrentDateandTime + "2.jpg", AppController.getFileDataFromDrawable(AddProduct.this, mImageDrawable2ForWeb), "image/jpeg"));


                }
                if (mImageDrawable3ForWeb != null) {
                    params.put("image3", new DataPart(mCurrentDateandTime + "3.jpg", AppController.getFileDataFromDrawable(AddProduct.this, mImageDrawable3ForWeb), "image/jpeg"));


                }
                if (pdfarray != null) {
                    params.put("invoice_image", new DataPart(mCurrentDateandTime + "4.pdf", pdfarray, "pdf"));


                }

                return params;
            }

        };
        SingletonRequestQueue.getInstance(AddProduct.this).addToRequestQueue(multipartRequest);
    }

    private void uploadEdiBitmap() {
        final ProgressDialog pDialog = new ProgressDialog(AddProduct.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ADDPRODUCT1, new Response.Listener<NetworkResponse>() {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(POST, EDIT_PRODUCT, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                pDialog.cancel();
                pDialog.dismiss();
                String resultResponse = new String(response.data);

                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        Toast.makeText(AddProduct.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(AddProduct.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                pDialog.cancel();
                pDialog.dismiss();
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("main_cat", mParentCatId);
                params.put("product_code", mProductCode.getText().toString());
                params.put("description", mProductDescription.getText().toString());
                params.put("department", mDepartmentArray[mDepartment.getSelectedItemPosition()]);
                params.put("assigned_name", mNameOfEmployee.getText().toString());
                params.put("product_quantity", mEnterQuantity.getText().toString());
                params.put("location_id", mLocationID);
                params.put("assigned_id", mEnterEmployeeId.getText().toString());
                params.put("addedon", mCurrentDateandTimeForWeb);
                params.put("expire_date", mSelectExpireDate.getText().toString());
                params.put("team_leader", mLeaderName.get(mTeamLeader.getSelectedItemPosition()));
                params.put("added_by", "");
                params.put("make", mProductMake.getText().toString());
                params.put("model", mProductModel.getText().toString());
                params.put("additional_info", mEnterAdditionalDetail.getText().toString());
                params.put("invoice_date", mSelectInvoice.getText().toString());
                params.put("putin_date", mPutInDate.getText().toString());
                params.put("name", mProductName.getText().toString());
                params.put("categoryID", mCatId);
                    params.put("id",mProductModelList.get(mPosition).getProductID());

                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+access);
                params.put("Accept", "application/json");

                return params;
            }*/


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                if (mImageDrawable1ForWeb != null) {
                    params.put("image", new DataPart(mCurrentDateandTime + "1.jpg", AppController.getFileDataFromDrawable(AddProduct.this, mImageDrawable1ForWeb), "image/jpeg"));



                }

                if (mImageDrawable2ForWeb != null) {
                    params.put("image2", new DataPart(mCurrentDateandTime + "2.jpg", AppController.getFileDataFromDrawable(AddProduct.this, mImageDrawable2ForWeb), "image/jpeg"));


                }
                if (mImageDrawable3ForWeb != null) {
                    params.put("image3", new DataPart(mCurrentDateandTime + "3.jpg", AppController.getFileDataFromDrawable(AddProduct.this, mImageDrawable3ForWeb), "image/jpeg"));


                }
                if (pdfarray != null) {
                    params.put("invoice_image", new DataPart(mCurrentDateandTime + "4.pdf", pdfarray, "pdf"));


                }

                return params;
            }

        };
        SingletonRequestQueue.getInstance(AddProduct.this).addToRequestQueue(multipartRequest);
    }


    private void selectImage(int i) {

        mUserSelectedImage = i;

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddProduct.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == PICK_PDF_REQUEST) {
                // showFileChooser();
                mFilePath = data.getData();

                // Let's read picked image data - its URI
                Uri uri = data.getData();
                File file = new File(uri.getPath());

                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    byte[] bytesArray = new byte[is.available()];
                    is.read(bytesArray);

                    //write to sdcard
            /*
            File myPdf=new File(Environment.getExternalStorageDirectory(), "myPdf.pdf");
            FileOutputStream fos=new FileOutputStream(myPdf.getPath());
            fos.write(bytesArray);
            fos.close();*/

                    System.out.println(bytesArray.toString());
                    pdfarray = bytesArray;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mUserSelectedImage == 1) {
            mImageDrawable1ForWeb = new BitmapDrawable(getResources(), thumbnail);

            mImage1.setText("image1_" + mCurrentDateandTime + ".jpg");

        } else if (mUserSelectedImage == 2) {
            mImageDrawable2ForWeb = new BitmapDrawable(getResources(), thumbnail);
            mImage2.setText("image2_" + mCurrentDateandTime + ".jpg");


        } else if (mUserSelectedImage == 3) {
            mImageDrawable3ForWeb = new BitmapDrawable(getResources(), thumbnail);

            mImage3.setText("image2_" + mCurrentDateandTime + ".jpg");


        } else if (mUserSelectedImage == 4) {
            mInvoiceDrawableForWeb = new BitmapDrawable(getResources(), thumbnail);

            mSelectInvoice.setText("invoice" + mCurrentDateandTime + ".jpg");


        }
        // Drawable d = new BitmapDrawable(getResources(), thumbnail);

        mImageView.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mUserSelectedImage == 1) {
            mImageDrawable1ForWeb = new BitmapDrawable(getResources(), bm);
            mImage1.setText("image1_" + mCurrentDateandTime + ".jpg");


        } else if (mUserSelectedImage == 2) {
            mImageDrawable2ForWeb = new BitmapDrawable(getResources(), bm);
            mImage2.setText("image2_" + mCurrentDateandTime + ".jpg");


        } else if (mUserSelectedImage == 3) {
            mImageDrawable3ForWeb = new BitmapDrawable(getResources(), bm);
            mImage3.setText("image2_" + mCurrentDateandTime + ".jpg");


        } else if (mUserSelectedImage == 4) {
            mInvoiceDrawableForWeb = new BitmapDrawable(getResources(), bm);
            mSelectInvoice.setText("invoice" + mCurrentDateandTime + ".jpg");


        }


    }


    public void DateDialog(final int i) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (i == 1) {
                    // mDateSelect = true;
                    mSelectInvoice.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

                    //  mStartDateForServer = monthOfYear + "/" + dayOfMonth + "/" + year;

                } else if (i == 2) {
                    // mDateSelect = true;

                    mPutInDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    // mEndDateForServer = monthOfYear + "/" + dayOfMonth + "/" + year;


                } else if (i == 3) {
                    // mDateSelect = true;

                    mSelectExpireDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    // mEndDateForServer = monthOfYear + "/" + dayOfMonth + "/" + year;


                }
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(AddProduct.this, listener, year, month, day);
        dpDialog.show();
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }


}
