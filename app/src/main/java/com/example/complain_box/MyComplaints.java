package com.example.complain_box;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complain_box.adapter.ArrayAdapterMyComplaints;
import com.example.complain_box.internetcheck.InternetCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyComplaints extends AppCompatActivity {
    RecyclerView rcv;
    ArrayAdapterMyComplaints adapter;
    RecyclerView.LayoutManager mgr;
    ArrayList<String> subject;
    ArrayList<String> complaint;
    ArrayList<String> status;
    ArrayList<String> upvote;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaints);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcv = findViewById(R.id.rcv);
        complaint = new ArrayList<>();
        upvote = new ArrayList<>();
        status = new ArrayList<>();
        subject = new ArrayList<>();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String email = sh.getString("email", "");
        InternetCheck internetCheck = new InternetCheck();
        boolean b = internetCheck.checkConnection(this);

        if (b) {
            String url = "https://complainbox2000.000webhostapp.com/my_complaints.php?email=" + email;
            pd = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            pd.setTitle("Connecting Server");
            pd.setMessage("loading...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    if(response.trim().equals("")){
                        Toast.makeText(MyComplaints.this, "you had not raised any complaints yet", Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String status1 = null;
                                String upvote1 = null;
                                String subject1 = null;
                                String description1 = null;
                                status1 = jsonObject.getString("status");
                                subject1 = jsonObject.getString("subject");
                                upvote1 = jsonObject.getString("upvote");
                                description1 = jsonObject.getString("description");

                                complaint.add(description1);
                                if (status1.trim().equals("0")) {
                                    status.add("Status : Pending");
                                } else if (status1.trim().equals("1")) {
                                    status.add("Status : Approved");
                                } else if (status1.trim().equals("2")) {
                                    status.add("Status : Rejected");
                                } else if (status1.trim().equals("3")) {
                                    status.add("Status : Resolved");
                                }
                                upvote.add(upvote1 + " Upvotes");
                                subject.add(subject1);
                                adapter.notifyDataSetChanged();
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
                    Toast.makeText(MyComplaints.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
            mque.add(stringRequest);
            mgr = new LinearLayoutManager(this);

            rcv.setLayoutManager(mgr);
            adapter = new ArrayAdapterMyComplaints(this, complaint, status, upvote, subject);
            rcv.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
