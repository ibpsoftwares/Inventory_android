package com.kftsoftwares.inventorymanagment.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.crashlytics.android.Crashlytics;
import com.kftsoftwares.inventorymanagment.util.EditText;
import com.kftsoftwares.inventorymanagment.R;
import com.kftsoftwares.inventorymanagment.util.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static com.kftsoftwares.inventorymanagment.util.Constants.EMAIL;
import static com.kftsoftwares.inventorymanagment.util.Constants.ID;
import static com.kftsoftwares.inventorymanagment.util.Constants.LOGIN;
import static com.kftsoftwares.inventorymanagment.util.Constants.MyPREFERENCES;
import static com.kftsoftwares.inventorymanagment.util.Constants.NAME;
import static com.kftsoftwares.inventorymanagment.util.Constants.TYPE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    EditText mInput_email, mInput_password;

    SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        mInput_email = findViewById(R.id.input_email);
        mInput_password = findViewById(R.id.input_password);
        Button loginButton = findViewById(R.id.loginButton);
        mSharedPref = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInput_email != null && mInput_email.getText().toString().equalsIgnoreCase("")) {
                     Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();

                } else if (mInput_email != null && !Patterns.EMAIL_ADDRESS.matcher(mInput_email.getText().toString()).matches()) {
                    Toast.makeText(LoginActivity.this, "Please enter correct email", Toast.LENGTH_SHORT).show();

                } else if (mInput_password != null && mInput_password.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    loginApi();
                }
            }
        });

        TextView forgetPassord = findViewById(R.id.forgetPassord);
        forgetPassord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });
    }


    //---------------------LOGIN API----------------------//
    private void loginApi() {

        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        SharedPreferences.Editor editor = mSharedPref.edit();
                        editor.putString(ID, data.getString("userid"));
                        editor.putString(NAME, data.getString("username"));
                        editor.putString(EMAIL, data.getString("email"));
                        editor.putString(TYPE, data.getString("user_type"));
                        editor.apply();
                        editor.commit();
                        Toast.makeText(LoginActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        if(data.getString("user_type").equalsIgnoreCase("admin")) {

                            startActivity(new Intent(LoginActivity.this, ViewProducts.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(LoginActivity.this, SearchByDate.class));
                            finish();
                        }
                    } else {

                        Toast.makeText(LoginActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginActivity.this, "Request Timeout", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(LoginActivity.this, "Please Check Your Credentials", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(LoginActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", mInput_email.getText().toString());
                params.put("password", mInput_password.getText().toString());
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}

