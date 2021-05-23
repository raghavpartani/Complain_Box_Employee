package com.example.complain_box;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complain_box.internetcheck.InternetCheck;
import com.example.complain_box.modelclass.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView signup;
    Button login;
    ProgressDialog pd;
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
            startActivity(new Intent(Login.this, ComplainRaisedByCompanions.class));
            finish();
        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetCheck internetCheck=new InternetCheck();
                boolean b=internetCheck.checkConnection(Login.this);

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
                }else if(!b){
                    Toast.makeText(Login.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    login();
                }
            }

            private void login() {
                pd = new ProgressDialog(Login.this, R.style.MyAlertDialogStyle);
                pd.setTitle("Connecting Server");
                pd.setMessage("loading...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        if(response.trim().equals("Invalid email or password")){
                            Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                        }
                        else if(response.trim().equals("")){
                            Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            //Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                String status=jsonObject.getString("status");
                                if(status.trim().equals("0")){
                                    Toast.makeText(Login.this, "the owner of the company need to accept your request then only you can login", Toast.LENGTH_SHORT).show();
                                }else if(status.trim().equals("2")){
                                    Toast.makeText(Login.this, "the owner of the company need to rejected your account", Toast.LENGTH_SHORT).show();
                                }
                                else if(status.trim().equals("1")){
                                    Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, ComplainRaisedByCompanions.class));

                                    Employee employee=null;
                                    employee=new Employee();
                                    employee.setCompany_name(jsonObject.getString("company"));
                                    employee.setEmp_name(jsonObject.getString("name"));
                                    employee.setEmp_id(jsonObject.getString("emp_id"));
                                    employee.setEmp_email(jsonObject.getString("email"));

                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                    myEdit.putString("company", employee.getCompany_name());
                                    myEdit.putString("name",employee.getEmp_name());
                                    myEdit.putString("email",employee.getEmp_email());
                                    myEdit.putString("emp_id", employee.getEmp_id());

                                    myEdit.commit();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Toast.makeText(Login.this, "Something went wrong try again later", Toast.LENGTH_SHORT).show();
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
        });
    }
}