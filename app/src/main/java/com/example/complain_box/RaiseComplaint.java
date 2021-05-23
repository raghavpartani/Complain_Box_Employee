package com.example.complain_box;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complain_box.internetcheck.InternetCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RaiseComplaint extends AppCompatActivity {

    EditText sub,details,customcategory;
    Spinner category;
    ArrayList <String>a;
    ArrayAdapter<String>arrayAdapter;
    Button raise;
    ProgressDialog pd;

    String url="https://complainbox2000.000webhostapp.com/raise_a_complaint.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_a_compalint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        details=findViewById(R.id.description);
        sub=findViewById(R.id.subject);
        category=findViewById(R.id.category);
        raise=findViewById(R.id.raise);
        customcategory=findViewById(R.id.customcategory);
        customcategory.setEnabled(false);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String email = sh.getString("email", "");
        final String company = sh.getString("company", "");
        final String name = sh.getString("name", "");
        final String emp_id = sh.getString("emp_id", "");

        //Toast.makeText(this, ""+company+name+email+emp_id, Toast.LENGTH_SHORT).show();
        a=new ArrayList<>();
        a.add("Category");
        a.add("Misconduct");
        a.add("Machinery");
        a.add("Other");

        arrayAdapter=new ArrayAdapter<String>(RaiseComplaint.this,android.R.layout.simple_expandable_list_item_1,a);
               category.setAdapter(arrayAdapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(category.getSelectedItem().toString().trim().equals("Other")){
                    customcategory.setEnabled(true);
                }
                else {
                    customcategory.setEnabled(false);
                    customcategory.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     raise.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             InternetCheck internetCheck=new InternetCheck();
             boolean b=internetCheck.checkConnection(RaiseComplaint.this);

             if(category.getSelectedItem().toString().trim().equals("Category")){
                 Toast.makeText(RaiseComplaint.this, "Please select a category", Toast.LENGTH_SHORT).show();
             }

             else if(category.getSelectedItem().toString().trim().equals("Other")){
                 if(customcategory.getText().toString().trim().equals("")){
                     customcategory.setError("Category is missing");
                 }
             }
             else if(details.getText().toString().trim().equals("")){
                 details.setError("Description is missing");
             }
             else if(sub.getText().toString().trim().equals("")){
                 sub.setError("Subject is missing");
             }
             else if(sub.getText().toString().length()<10){
                 sub.setError("Subject can't be too small");
             }
             else if(details.getText().toString().length()<15){
                 details.setError("Desription can't be too small");
             }
             else if(b) {
                 raise();
            }
             else{
                 Toast.makeText(RaiseComplaint.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
             }
         }

         private void raise() {
             raise.setEnabled(false);
             pd = new ProgressDialog(RaiseComplaint.this, R.style.MyAlertDialogStyle);
             pd.setTitle("Connecting Server");
             pd.setMessage("loading...");
             pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             pd.show();
             StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     pd.dismiss();
                     if(response.trim().equals("Raised Successfully")) {
                         Toast.makeText(RaiseComplaint.this, "" + response, Toast.LENGTH_SHORT).show();
                         finish();
                     }
                     else {

                         raise.setEnabled(true);
                         Toast.makeText(RaiseComplaint.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                     }
                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {

                     raise.setEnabled(true);
                     pd.dismiss();
                     Toast.makeText(RaiseComplaint.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                 }
             }){
                 @Nullable
                 @Override
                 protected Map<String, String> getParams() throws AuthFailureError {
                     Map<String, String> map = new HashMap<>();
                     map.put("email", email);
                     map.put("name", name);
                     map.put("company",company);
                     map.put("emp_id",emp_id);
                     map.put("subject", sub.getText().toString().trim());
                     map.put("description",details.getText().toString().trim());
                     if(category.getSelectedItem().toString().trim().equals("Other")){
                         map.put("category",customcategory.getText().toString().trim());
                     }
                     else {
                         map.put("category",category.getSelectedItem().toString().trim());
                     }
                     return map;
                 }
             };
             RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
             mque.add(stringRequest);
         }
     });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}