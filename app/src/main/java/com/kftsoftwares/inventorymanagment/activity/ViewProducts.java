package com.kftsoftwares.inventorymanagment.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.kftsoftwares.inventorymanagment.Deleter_Edit_Interface;
import com.kftsoftwares.inventorymanagment.model.ProductModel;
import com.kftsoftwares.inventorymanagment.R;
import com.kftsoftwares.inventorymanagment.ViewProductAdapter;
import com.kftsoftwares.inventorymanagment.util.AppController;
import com.kftsoftwares.inventorymanagment.util.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static com.kftsoftwares.inventorymanagment.util.Constants.DELETE_PRODUCT;
import static com.kftsoftwares.inventorymanagment.util.Constants.SEARCHPRODUCTS;
import static com.kftsoftwares.inventorymanagment.util.Constants.VIEWPRODUCTS;

public class ViewProducts extends AppCompatActivity implements Deleter_Edit_Interface {


    ProgressBar mProgressBar;
    private boolean mLoadMore;
    private boolean mLoading;
    private int mPageSize = 0;
    private ArrayList<ProductModel> mProductModelList;
    ViewProductAdapter mViewProductAdapter;
    EditText mSearchEditText;
    RecyclerView mRecyclerView;

    @Override
    protected void onResume() {
        super.onResume();

        mViewProductAdapter = new ViewProductAdapter(ViewProducts.this, new ArrayList<ProductModel>(), this);
        mRecyclerView.setAdapter(mViewProductAdapter);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        if (mSearchEditText.getText().toString().equalsIgnoreCase("") || mSearchEditText.getText().toString().equalsIgnoreCase(" ")) {
            getViewProducts(false, 0);
            if (mProductModelList != null) {
                mProductModelList.clear();
            }
        } else {
            mLoading = true;
            mLoadMore = false;
            getSearchProduct(mSearchEditText.getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);


        TextView add = findViewById(R.id.addProduct);
        TextView viewArchives = findViewById(R.id.viewArchives);

        mRecyclerView = findViewById(R.id.recyclerView);
        mSearchEditText = findViewById(R.id.searchEditBox);
        ImageButton searchImageButton = findViewById(R.id.searchImageButton);

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mSearchEditText.getText().toString().equalsIgnoreCase("") || mSearchEditText.getText().toString().equalsIgnoreCase(" ")) {
                    getViewProducts(false, 0);
                    if (mProductModelList != null) {
                        mProductModelList.clear();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSearchEditText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (mSearchEditText.getText().toString().equalsIgnoreCase("") || mSearchEditText.getText().toString().equalsIgnoreCase(" ")) {
                                getViewProducts(false, 0);
                                if (mProductModelList != null) {
                                    mProductModelList.clear();
                                }
                            } else {
                                mLoading = true;
                                mLoadMore = false;
                                getSearchProduct(mSearchEditText.getText().toString());
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        mProductModelList = new ArrayList<>();
        final GridLayoutManager mLayoutManager =
                new GridLayoutManager(ViewProducts.this, 2);


        mRecyclerView.setLayoutManager(mLayoutManager);

        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(2);

        mRecyclerView.addItemDecoration(spacesItemDecoration);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int totalwidth = width / 2;


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProducts.this, AddProduct.class));
                hideKeyBoard();
            }
        });

        viewArchives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProducts.this, ViewArchive.class));
                hideKeyBoard();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (mLoadMore && !mLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= mPageSize) {
                        mLoading = true;
                        mLoadMore = false;
                        mPageSize++;
                        getViewProducts(true, mPageSize);

                    }
                }
            }
        });
      //  getViewProducts(false, 0);

        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mSearchEditText.getText().toString().equalsIgnoreCase("") || mSearchEditText.getText().toString().equalsIgnoreCase(" ")) {
                    getViewProducts(false, 0);
                    if (mProductModelList != null) {
                        mProductModelList.clear();
                    }
                } else {
                    mLoading = true;
                    mLoadMore = false;
                    getSearchProduct(mSearchEditText.getText().toString());
                }
            }
        });


    }

    private void getSearchProduct(final String name) {

        hideKeyBoard();
        mViewProductAdapter = new ViewProductAdapter(ViewProducts.this, new ArrayList<ProductModel>(), this);
        mRecyclerView.setAdapter(mViewProductAdapter);

        if (mProductModelList != null) {
            mProductModelList.clear();
        }
        String tag_string_req = "string_req";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                SEARCHPRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
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

                            Glide.with(ViewProducts.this)
                                    .load(jsonObject1.getString("image"));
                            mProductModelList.add(productModel);
                        }


                        mViewProductAdapter.updateData(mProductModelList);
                    } else {
                        Toast.makeText(ViewProducts.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ViewProducts.this, "Request Timeout", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(ViewProducts.this, "Please Check Your Credentials", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(ViewProducts.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(ViewProducts.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(ViewProducts.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getViewProducts(final boolean value, final int count) {

        mPageSize = count;

        if (count == 0) {
            mProductModelList.clear();

        } else {

            mProductModelList.clear();
        }

        hideKeyBoard();

        String tag_string_req = "string_req";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        if (value) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {


            pDialog.show();
        }


        StringRequest strReq = new StringRequest(Request.Method.POST,
                VIEWPRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (pDialog.isShowing()) {
                    pDialog.cancel();
                    pDialog.dismiss();
                } else if (mProgressBar.getVisibility() == View.VISIBLE) {
                    mProgressBar.setVisibility(View.GONE);
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String string = jsonObject.getString("status");
                    if (string.equalsIgnoreCase("false")) {

                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        mLoadMore = true;
                        mLoading = false;
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

                            Glide.with(ViewProducts.this)
                                    .load(jsonObject1.getString("image"));
                            mProductModelList.add(productModel);
                        }


                        if (mPageSize == 0) {
                            mViewProductAdapter.addArticleFirst(mProductModelList);
                        } else {
                            mViewProductAdapter.addArticleLast(mProductModelList);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (value) {
                    mProgressBar.setVisibility(View.GONE);
                } else {


                    pDialog.cancel();
                    pDialog.dismiss();
                }


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Toast.makeText(ViewProducts.this, "Request Timeout", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(ViewProducts.this, "Please Check Your Credentials", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(ViewProducts.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(ViewProducts.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(ViewProducts.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", String.valueOf(mPageSize));
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void method(int type, String id) {

        //if 0 delete else edit
        if (type == 0) {
            showDeleteDialog(ViewProducts.this, id);
        } else {

            Intent intent = new Intent(ViewProducts.this, AddProduct.class);
            intent.putExtra("id", id);
            intent.putExtra("value", "edit");
            startActivity(intent);

        }

    }

    @Override
    public void ArrayList(ArrayList<ProductModel> productModels, int position) {

        Intent intent = new Intent(ViewProducts.this, AddProduct.class);
        Bundle b = new Bundle();
        b.putSerializable("list", productModels);
        b.putInt("position", position);
        b.putString("value", "edit");
        intent.putExtra("bundle", b);
        startActivity(intent);

    }

    private void deleteProduct(final String id, final String reason) {

        hideKeyBoard();

        String tag_string_req = "string_req";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                DELETE_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (mSearchEditText.getText().toString().equalsIgnoreCase("") || mSearchEditText.getText().toString().equalsIgnoreCase(" ")) {
                        if (mProductModelList != null) {
                            mProductModelList.clear();
                        }
                        getViewProducts(false, 0);

                    } else {
                        mLoading = true;
                        mLoadMore = false;
                        getSearchProduct(mSearchEditText.getText().toString());
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
                    Toast.makeText(ViewProducts.this, "Request Timeout", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(ViewProducts.this, "Please Check Your Credentials", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(ViewProducts.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(ViewProducts.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(ViewProducts.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("reason", reason);
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void showDeleteDialog(Activity activity, final String id) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_dialog);
        Button close = dialog.findViewById(R.id.close);
        final EditText text = dialog.findViewById(R.id.text);
        Button give = dialog.findViewById(R.id.save);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
                hideKeyBoard();
            }
        });
        give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (text.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(ViewProducts.this, "Enter Reason", Toast.LENGTH_SHORT).show();
                } else {
                    deleteProduct(id, text.getText().toString());
                    hideKeyBoard();

                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });
        dialog.show();

    }
}
