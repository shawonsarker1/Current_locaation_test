package com.techdecode.currentlocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_login extends AppCompatActivity {
    Button button;
    EditText etEmail,etPassword;

    private String email,password;
    private String URL="http://192.168.7.41/startech_Hrm/login1.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        email=password=" ";
        etEmail=findViewById(R.id.edittext_username);
        etPassword=findViewById(R.id.edittext_password);
        button=(Button)findViewById(R.id.button_login);


    }
    public void login1(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (etEmail.getText().toString().length() == 0 || etPassword.getText().toString().length() == 0) {

            if (etEmail.getText().toString().length() == 0)
                etEmail.setError("Username is required!");

            else if (etPassword.getText().toString().length() == 0)
                etPassword.setError("Password is required!");

        } else {
            if (!email.equals(" ") && !password.equals(" ")) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Success")) {

                            Intent dp = new Intent(MainActivity_login.this, MainActivity.class);
                            dp.putExtra("username",email);
                            startActivity(dp);

                        } else if (response.equals("Data failed")) {

                            Toast.makeText(MainActivity_login.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this, "Invalid Login ID/Password", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(MainActivity.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<String, String>();
                        data.put("email", email);
                        data.put("password", password);
                        return data;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }
    }

}