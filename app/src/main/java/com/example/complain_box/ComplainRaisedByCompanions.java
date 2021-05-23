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
import com.example.complain_box.modelclass.Complaints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComplainRaisedByCompanions extends AppCompatActivity {
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
          startActivity(new Intent(ComplainRaisedByCompanions.this, RaiseComplaint.class));
    }

    public void my_complaints(MenuItem item) {
        startActivity(new Intent(ComplainRaisedByCompanions.this,MyComplaints.class));
    }

    public void logout(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("company","");
        myEdit.putString("name","");
        myEdit.putString("email","");
        myEdit.putString("emp_id", "");
        myEdit.commit();
        startActivity(new Intent(this, Login.class));
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
        boolean b=internetCheck.checkConnection(ComplainRaisedByCompanions.this);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String company = sh.getString("company", "");
        final String email = sh.getString("email", "");

        if(b) {
            getComplaints(email,company);
            mgr = new LinearLayoutManager(this);
            rcv.setLayoutManager(mgr);
            adapter=new ArrayAdapterComplaints(this,complaint,emp_name,subject,complaint_id,upvoted_by_you_or_not,email);

            rcv.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getComplaints(final String email, final String company) {
        String url="https://complainbox2000.000webhostapp.com/complaint_by_companion.php";
        pd = new ProgressDialog(ComplainRaisedByCompanions.this, R.style.MyAlertDialogStyle);
        pd.setTitle("Connecting Server");
        pd.setMessage("loading...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                if(response.trim().equals("")){
                    Toast.makeText(ComplainRaisedByCompanions.this, "No-one had raised any complaints yet", Toast.LENGTH_SHORT).show();
                }
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Complaints complaints=new Complaints();

                        complaints.setSubject(jsonObject.getString("subject"));
                        complaints.setEmp_name(jsonObject.getString("name"));
                        complaints.setDescription(jsonObject.getString("description"));
                        complaints.setComplaint_id(jsonObject.getString("complaint_id"));
                        complaints.setUpvotedbyyouornot(jsonObject.getString("yesorno"));

                        complaint.add(complaints.getDescription());
                        complaint_id.add(complaints.getComplaint_id());
                        subject.add(complaints.getSubject());
                        upvoted_by_you_or_not.add(complaints.getUpvotedbyyouornot());
                        emp_name.add("Raised by "+complaints.getEmp_name());
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
                Toast.makeText(ComplainRaisedByCompanions.this, "something went wrong", Toast.LENGTH_SHORT).show();
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


    }
}