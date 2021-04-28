package com.example.complain_box;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView signup;
    Button login;

    EditText email,password;
    String url="https://complainbox2000.000webhostapp.com/login_emp.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        email=findViewById(R.id.edtEmail);
        password=findViewById(R.id.editTextPassword);

        signup=findViewById(R.id.signup);
        login=findViewById(R.id.btnlogin);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emp_id = sh.getString("emp_id", "");

        if(emp_id.trim().equals("")){

        }
        else {
            startActivity(new Intent(MainActivity.this,Complain_raised_by_companions.class));
            finish();
        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().equals("")){
                    email.setError("Email is missing");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password is missing");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Please Enter Valid Email.");
                }else if (password.length() < 6) {
                    password.setError("Please Enter Minimum 6 Char.");
                }
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.trim().equals("Invalid email or password")){
                                Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                                try {
                                    JSONArray jsonArray=new JSONArray(response);
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String status=jsonObject.getString("status");
                                    if(status.trim().equals("0")){
                                        Toast.makeText(MainActivity.this, "pending", Toast.LENGTH_SHORT).show();
                                    }else if(status.trim().equals("2")){
                                        Toast.makeText(MainActivity.this, "rejected", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(status.trim().equals("1")){
                                        Toast.makeText(MainActivity.this, "approved", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this,Complain_raised_by_companions.class));
                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putString("company", jsonObject.getString("company"));
                                        myEdit.putString("name", jsonObject.getString("name"));
                                        myEdit.putString("email", jsonObject.getString("email"));
                                        myEdit.putString("emp_id", jsonObject.getString("emp_id"));
                                        myEdit.commit();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("email", email.getText().toString().trim());
                            map.put("password", password.getText().toString().trim());
                            return map;
                        }
                    };
                    RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
                    mque.add(stringRequest);

                }

            }
        });
    }
}