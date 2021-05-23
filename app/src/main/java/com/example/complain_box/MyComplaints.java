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
import com.example.complain_box.modelclass.Complaints;

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

        InternetCheck internetCheck = new InternetCheck();
        boolean b = internetCheck.checkConnection(this);

        if (b) {
            getComplaints();
            mgr = new LinearLayoutManager(this);
            rcv.setLayoutManager(mgr);
            adapter = new ArrayAdapterMyComplaints(this, complaint, status, upvote, subject);
            rcv.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getComplaints() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String email = sh.getString("email", "");
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
                            Complaints complaints=new Complaints();
                            complaints.setStatus(jsonObject.getString("status"));
                            complaints.setSubject(jsonObject.getString("subject"));
                            complaints.setUpvote(jsonObject.getString("upvote"));
                            complaints.setDescription(jsonObject.getString("description"));

                            complaint.add(complaints.getDescription());
                            if (complaints.getStatus().trim().equals("0")) {
                                status.add("Status : Pending");
                            } else if (complaints.getStatus().trim().equals("1")) {
                                status.add("Status : Approved");
                            } else if (complaints.getStatus().trim().equals("2")) {
                                status.add("Status : Rejected");
                            } else if (complaints.getStatus().trim().equals("3")) {
                                status.add("Status : Resolved");
                            }
                            upvote.add(complaints.getUpvote()+ " Upvotes");
                            subject.add(complaints.getSubject());
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
