package com.example.complain_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complain_box.internetcheck.InternetCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    TextView signin;
    Button signup;
    Spinner company;
    ImageView imageView_icon;
    ArrayAdapter<String>arrayAdapter;
    ArrayList<String>a;
    ProgressDialog pd;

    EditText email,name,emp_id,password;
    String url1="https://complainbox2000.000webhostapp.com/signup_employee.php";
    String url="https://complainbox2000.000webhostapp.com/company_list.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        InternetCheck internetCheck=new InternetCheck();
        boolean b=internetCheck.checkConnection(this);

        if(!b){
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
        imageView_icon = findViewById(R.id.show_pass_btn);
        signin=findViewById(R.id.login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        signup=findViewById(R.id.signup);
        company=findViewById(R.id.company);

        email=findViewById(R.id.edt_emp_email);
        name=findViewById(R.id.edt_emp_name);
        emp_id=findViewById(R.id.edt_emp_id);
        password=findViewById(R.id.edt_emp_password);

        a=new ArrayList<>();
        a.add("Select Company");
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = null;
                        name = jsonObject.getString("name");
                        a.add(name);
                        arrayAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
        mque.add(stringRequest);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,a);
        company.setAdapter(arrayAdapter);
        imageView_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    imageView_icon.setImageResource(R.drawable.hide_password);

                    //Show Password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    imageView_icon.setImageResource(R.drawable.show_password);

                    //Hide Password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetCheck internetCheck=new InternetCheck();
                boolean b=internetCheck.checkConnection(SignUp.this);

                if(company.getSelectedItem().toString().equals("Select Company")){
                    Toast.makeText(SignUp.this, "Please select company name", Toast.LENGTH_SHORT).show();
                }
                else if(emp_id.getText().toString().trim().equals("")){
                    emp_id.setError("Emp id is missing");
                }
                else if(name.getText().toString().trim().equals("")){
                    name.setError("Name is missing");
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("Email is missing");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password is missing");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password is missing");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Please Enter Valid Email.");
                }
                else if (password.length() < 6) {
                    password.setError("Please Enter Minimum 6 Char.");
                }
                else if(!b){
                    Toast.makeText(SignUp.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    pd = new ProgressDialog(SignUp.this, R.style.MyAlertDialogStyle);
                    pd.setTitle("Connecting Server");
                    pd.setMessage("loading...");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            if(response.trim().equals("Registration Successful But it need to be approved by the owner of the company admin")) {
                                Toast.makeText(SignUp.this, "Registration Successful But it need to be approved by the owner of the company", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else if(response.trim().equals("Email already registered with us")){
                                Toast.makeText(SignUp.this, ""+response, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("email", email.getText().toString().trim());
                            map.put("password", password.getText().toString().trim());
                            map.put("company", company.getSelectedItem().toString().trim());
                            map.put("name", name.getText().toString().trim());
                            map.put("emp_id", emp_id.getText().toString().trim());
                            return map;
                        }
                    };
                    RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
                    mque.add(stringRequest);
                    // Intent intent=new Intent(MainActivity.this,Pending_Complaint.class);
                    //startActivity(intent);

                }

            }
        });

    }
}