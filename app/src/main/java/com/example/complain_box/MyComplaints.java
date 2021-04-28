package com.example.complain_box;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyComplaints extends AppCompatActivity {
    RecyclerView rcv;
    ArrayAdapterMyComplaints adapter;
    RecyclerView.LayoutManager mgr;
    ArrayList<String> subject;
    ArrayList<String> complaint;
    ArrayList<String> status;
    ArrayList<String> upvote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaints);
        rcv=findViewById(R.id.rcv);
        complaint=new ArrayList<>();
        upvote=new ArrayList<>();
        status=new ArrayList<>();
        subject=new ArrayList<>();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String email = sh.getString("email", "");

        String url="https://complainbox2000.000webhostapp.com/my_complaints.php?email="+email;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MyComplaints.this, "hii"+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String status1 = null;
                        String upvote1 = null;
                        String subject1 = null;
                        String description1 = null;
                        status1 = jsonObject.getString("status");
                        subject1 = jsonObject.getString("subject");
                        upvote1 = jsonObject.getString("upvote");
                        description1=jsonObject.getString("description");

                        complaint.add(description1);
                        if(status1.trim().equals("0")){
                            status.add("Status : Pending");
                        }else if(status1.trim().equals("1")){
                            status.add("Status : Approved");
                        }else if(status1.trim().equals("2")){
                            status.add("Status : Rejected");
                        }else if(status1.trim().equals("3")){
                            status.add("Status : Resolved");
                        }
                        upvote.add(upvote1+" Upvotes");
                        subject.add(subject1);
                        adapter.notifyDataSetChanged();
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
        mgr = new LinearLayoutManager(this);

        rcv.setLayoutManager(mgr);
        adapter=new ArrayAdapterMyComplaints(this,complaint,status,upvote,subject);
        rcv.setAdapter(adapter);

    }
}