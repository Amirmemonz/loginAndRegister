package com.androiddeft.loginandregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText etLoginUsername, etLoginPassword;
    Button btnLogin,btnregister;
    final String Login_Url = "https://newtrending.website/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnregister = (Button) findViewById(R.id.btnLoginRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        final String username = etLoginUsername.getText().toString();
        final String password = etLoginPassword.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Login_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { ;
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equalsIgnoreCase("success")) {
                                Toast.makeText(getApplicationContext(),"Login Successfully", Toast.LENGTH_SHORT).show();
                                JSONObject userJson = obj.getJSONObject("username");
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email")
                                );
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),obj.getString("message") , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Response Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName", username);
                params.put("password", password);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
