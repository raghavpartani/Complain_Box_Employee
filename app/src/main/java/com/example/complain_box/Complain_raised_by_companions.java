package com.example.complain_box;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complain_box.adapter.ArrayAdapterComplaints;
import com.example.complain_box.internetcheck.InternetCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Complain_raised_by_companions extends AppCompatActivity {
    RecyclerView rcv;
    ArrayAdapterComplaints adapter;
    RecyclerView.LayoutManager mgr;
    ArrayList<String> complaint;
    ArrayList<String> subject;
    ArrayList<String> emp_name;
    ArrayList<String> complaint_id;
    ArrayList<String> upvoted_by_you_or_not;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_raised_by_companions);
        rcv=findViewById(R.id.rcv);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public void raise_a_complaint(MenuItem item) {
          startActivity(new Intent(Complain_raised_by_companions.this,raise_a_compalint.class));
    }

    public void my_complaints(MenuItem item) {
        startActivity(new Intent(Complain_raised_by_companions.this,MyComplaints.class));
    }

    public void logout(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("company","");
        myEdit.putString("name","");
        myEdit.putString("email","");
        myEdit.putString("emp_id", "");
        myEdit.commit();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        complaint=new ArrayList<>();
        subject=new ArrayList<>();
        emp_name=new ArrayList<>();
        complaint_id=new ArrayList<>();

        upvoted_by_you_or_not=new ArrayList<>();
        InternetCheck internetCheck=new InternetCheck();
        boolean b=internetCheck.checkConnection(Complain_raised_by_companions.this);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String company = sh.getString("company", "");
        final String email = sh.getString("email", "");

        if(b) {
            String url="https://complainbox2000.000webhostapp.com/complaint_by_companion.php";
            pd = new ProgressDialog(Complain_raised_by_companions.this, R.style.MyAlertDialogStyle);
            pd.setTitle("Connecting Server");
            pd.setMessage("loading...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    if(response.trim().equals("")){
                        Toast.makeText(Complain_raised_by_companions.this, "No-one had raised any complaints yet", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name1 = null;
                            String complaint_id1 = null;
                            String subject1 = null;
                            String description1 = null;

                            String upvoted_by_you_or_not1 = null;
                            subject1 = jsonObject.getString("subject");
                            name1 = jsonObject.getString("name");
                            description1=jsonObject.getString("description");
                            complaint_id1=jsonObject.getString("complaint_id");

                            upvoted_by_you_or_not1=jsonObject.getString("yesorno");

                            complaint.add(description1);
                            complaint_id.add(complaint_id1);
                            subject.add(subject1);
                            upvoted_by_you_or_not.add(upvoted_by_you_or_not1);
                            emp_name.add("Raised by "+name1);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(Complain_raised_by_companions.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("company", company);
                    map.put("email", email);
                    return map;
                }
            };
            RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
            mque.add(stringRequest);

            mgr = new LinearLayoutManager(this);

            rcv.setLayoutManager(mgr);
            adapter=new ArrayAdapterComplaints(this,complaint,emp_name,subject,complaint_id,upvoted_by_you_or_not,email);

            rcv.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}