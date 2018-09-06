package com.kftsoftwares.inventorymanagment.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.kftsoftwares.inventorymanagment.util.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.inventorymanagment.util.Constants.EMAIL;
import static com.kftsoftwares.inventorymanagment.util.Constants.FORGET_PASSWORD;
import static com.kftsoftwares.inventorymanagment.util.Constants.ID;
import static com.kftsoftwares.inventorymanagment.util.Constants.LOGIN;
import static com.kftsoftwares.inventorymanagment.util.Constants.NAME;

public class ForgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Button loginButton = findViewById(R.id.loginButton);
        final EditText input_email = findViewById(R.id.input_email);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_email != null && input_email.getText().toString().equalsIgnoreCase("")) {
                     Toast.makeText(ForgetPassword.this, "Please enter email", Toast.LENGTH_SHORT).show();

                } else if (input_email != null && !Patterns.EMAIL_ADDRESS.matcher(input_email.getText().toString()).matches()) {
                    Toast.makeText(ForgetPassword.this, "Please enter correct email", Toast.LENGTH_SHORT).show();

                }
                else {

                    forgetPassword(input_email.getText().toString());
                }
            }
        });
        ImageView imageBack=findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void forgetPassword(final String email) {
        String tag_string_req = "string_req";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                FORGET_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(ForgetPassword.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {

                        Toast.makeText(ForgetPassword.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ForgetPassword.this, "Request Timeout", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(ForgetPassword.this, "Please Check Your Credentials", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(ForgetPassword.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(ForgetPassword.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(ForgetPassword.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail",email);
                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
