package com.kftsoftwares.inventorymanagment.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.inventorymanagment.R;
import com.kftsoftwares.inventorymanagment.SearchByDateAdapter;
import com.kftsoftwares.inventorymanagment.model.Categories;
import com.kftsoftwares.inventorymanagment.model.ProductModel;
import com.kftsoftwares.inventorymanagment.util.AppController;
import com.kftsoftwares.inventorymanagment.util.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.Request.Method.POST;
import static com.kftsoftwares.inventorymanagment.util.Constants.GET_ALL_CATEGORIES;
import static com.kftsoftwares.inventorymanagment.util.Constants.SEARCH_BY_RANGE;

public class SearchByDate extends AppCompatActivity implements View.OnClickListener {
    TextView mToDate, mFromDate;
    Spinner mCategorySpinner;
    private int day, month, year;
    private Calendar mCalendar;
    TextView mCount;
    private ArrayList<Categories> mCategories;

    private ArrayList<ProductModel> mProductModelList;

    SearchByDateAdapter mViewProductAdapter;
    String mStartDate="" , mEndDate="";
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_date);
        mFromDate = findViewById(R.id.toDate);
        mToDate    = findViewById(R.id.fromDate);
        mCount    = findViewById(R.id.count);
        mCategorySpinner = findViewById(R.id.searchEditBox);
        mRecyclerView = findViewById(R.id.recyclerView);
        mCalendar = new GregorianCalendar();
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        Button searchImageButton = findViewById(R.id.searchImageButton);
        mCategories = new ArrayList<>();
        mProductModelList = new ArrayList<>();
        mToDate.setOnClickListener(this);
        mFromDate.setOnClickListener(this);
        searchImageButton.setOnClickListener(this);
        getSelectorData();
        final GridLayoutManager mLayoutManager =
                new GridLayoutManager(SearchByDate.this, 2);


        mRecyclerView.setLayoutManager(mLayoutManager);

        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(2);

        mRecyclerView.addItemDecoration(spacesItemDecoration);

        mViewProductAdapter = new SearchByDateAdapter(SearchByDate.this, new ArrayList<ProductModel>());
        mRecyclerView.setAdapter(mViewProductAdapter);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

    }

    public void DateDialog(final int i) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {


                Calendar calendar = Calendar.getInstance();
                calendar.set(year1, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH);
                String strDate = format.format(calendar.getTime());
                SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyy", Locale.ENGLISH);
                String strDate1 = format1.format(calendar.getTime());

                if (i == 1) {

                    mToDate.setText(strDate1);
                    mEndDate = strDate;


                } else if (i == 2) {

                    mFromDate.setText(strDate1);
                    mStartDate = strDate;

                }
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(SearchByDate.this, listener, year, month, day);
        dpDialog.show();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.toDate:
                DateDialog(2);
                break;
            case R.id.fromDate:
                DateDialog(1);

                break;

            case R.id.searchImageButton:

                String str = mToDate.getText().toString();
                String str1 = mFromDate.getText().toString();
                if (!mToDate.getText().toString().equals("To Date")) {
                    if (!mFromDate.getText().toString().equals("From Date")) {

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy",Locale.ENGLISH);
                        try {
                            Date  todate = format.parse(mFromDate.getText().toString());
                            Date fromDate = format.parse(mToDate.getText().toString());

                            if (fromDate.before(todate))
                            {
                                Toast.makeText(this, "Select after from date", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                getSearchData();

                            }

                            System.out.println(todate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                    } else {

                        Toast.makeText(this, "Select from date also", Toast.LENGTH_SHORT).show();
                    }


                }
               else if (!mFromDate.getText().toString().equals("From Date")) {

                        Toast.makeText(this, "Select to date also", Toast.LENGTH_SHORT).show();


                }
                else {

                    getSearchData();
                }
                break;

        }

    }

    private void getSearchData() {
        if (mProductModelList!=null)
        {
            mProductModelList.clear();
        }

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                SEARCH_BY_RANGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("products");

                        ProductModel productModel = null;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            productModel = new ProductModel();
                            productModel.setName(jsonObject1.getString("name"));
                            productModel.setProductID(jsonObject1.getString("productID"));
                            productModel.setCategoryID(jsonObject1.getString("categoryID"));
                            productModel.setMain_cat(jsonObject1.getString("main_cat"));
                            productModel.setProduct_code(jsonObject1.getString("product_code"));
                            productModel.setDescription(jsonObject1.getString("description"));
                            productModel.setDepartment(jsonObject1.getString("department"));
                            productModel.setAssigned_name(jsonObject1.getString("assigned_name"));
                            productModel.setProduct_quantity(jsonObject1.getString("product_quantity"));
                            productModel.setLocation_id(jsonObject1.getString("location_id"));

                            productModel.setLocation_name(jsonObject1.getString("location"));

                            productModel.setAssigned_id(jsonObject1.getString("assigned_id"));
                            productModel.setBuynow_price(jsonObject1.getString("buynow_price"));
                            productModel.setAdded_by(jsonObject1.getString("added_by"));
                            productModel.setMake(jsonObject1.getString("make"));
                            productModel.setModel(jsonObject1.getString("model"));
                            productModel.setTeam_leader(jsonObject1.getString("team_leader"));
                            productModel.setImage(jsonObject1.getString("image"));
                            productModel.setImage2(jsonObject1.getString("image2"));
                            productModel.setImage3(jsonObject1.getString("image3"));
                            productModel.setStatus(jsonObject1.getString("status"));
                            productModel.setInvoice_image(jsonObject1.getString("invoice_image"));
                            productModel.setAdditional_info(jsonObject1.getString("additional_info"));
                            productModel.setInvoice_date(jsonObject1.getString("invoice_date"));
                            productModel.setPutin_date(jsonObject1.getString("putin_date"));
                            productModel.setTrash_reason(jsonObject1.getString("trash_reason"));

                            mProductModelList.add(productModel);
                        }

                        mCount.setText("Total Product: "+jsonArray.length());

                        mViewProductAdapter.updateData(mProductModelList);
                    } else {
                        Toast.makeText(SearchByDate.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

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
                    Toast.makeText(SearchByDate.this, "Request Timeout", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(SearchByDate.this, "Please Check Your Credentials", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(SearchByDate.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(SearchByDate.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(SearchByDate.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                    params.put("date1",mStartDate);
                    params.put("date2", mEndDate);
                if (mCategorySpinner.getSelectedItemPosition()>0)
                {
                    params.put("category", mCategories.get(mCategorySpinner.getSelectedItemPosition()).getCategoryID());

                }
                else {
                    params.put("category", "");
                }
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void getSelectorData() {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(POST,
                GET_ALL_CATEGORIES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray categories = jsonObject.getJSONArray("categories");

                    Categories categories12 = new Categories();
                    categories12.setParent("");
                    categories12.setCategoryID("");
                    categories12.setName("--Select--");

                    mCategories.add(categories12);

                    Categories categories1;


                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject jsonObject1 = categories.getJSONObject(i);
                        categories1 = new Categories();
                        categories1.setCategoryID(jsonObject1.getString("categoryID"));
                        categories1.setName(jsonObject1.getString("name"));

                        mCategories.add(categories1);

                    }

                    updateCategorySpinner();
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
                    Toast.makeText(SearchByDate.this, "Request Timeout", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(SearchByDate.this, "Authentication Failure", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(SearchByDate.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(SearchByDate.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(SearchByDate.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

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

    private void updateCategorySpinner() {


        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < mCategories.size(); i++) {
            data.add(mCategories.get(i).getName());
        }

        ArrayAdapter<String> locations1 = new ArrayAdapter<String>(SearchByDate.this, R.layout.spinner_layout, data);

        mCategorySpinner.setAdapter(locations1);


    }



}
