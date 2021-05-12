package com.example.complain_box.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complain_box.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrayAdapterComplaints extends RecyclerView.Adapter<ArrayAdapterComplaints.Myholder> {
    Context context;
    ArrayList<String> arrayList_subject;
    ArrayList<String> arrayList_emp_name;
    ArrayList<String> arrayList_complaint;
    ArrayList<String> arrayList_complaint_id;
    ArrayList<String> arrayList_upvoted_by_you_or_not;

    String email;

    public ArrayAdapterComplaints(Context context, ArrayList<String> complaint, ArrayList<String> emp_name, ArrayList<String> subject,ArrayList<String> complaint_id,ArrayList<String> upvoted_by_you_or_not,String email) {
        this.context=context;
        this.arrayList_complaint=complaint;
        this.arrayList_subject=subject;
        this.arrayList_emp_name=emp_name;
        this.arrayList_complaint_id=complaint_id;
        this.arrayList_upvoted_by_you_or_not=upvoted_by_you_or_not;
        this.email=email;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.layout_complaint_raised_by_others,parent,false);
        ArrayAdapterComplaints.Myholder holder = new ArrayAdapterComplaints.Myholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.complaint.setText(arrayList_complaint.get(position));
        holder.emp_name.setText(arrayList_emp_name.get(position));
        holder.subject.setText(arrayList_subject.get(position));
        holder.complaint_id.setText(arrayList_complaint_id.get(position));
        holder.upvotedbyyouornot.setText(arrayList_upvoted_by_you_or_not.get(position));
        if(arrayList_upvoted_by_you_or_not.get(position).equals("yes")){
            holder.like.setColorFilter(context.getResources().getColor(R.color.primaryTextColor));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList_complaint.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView complaint;TextView subject;TextView emp_name;TextView complaint_id;TextView upvotedbyyouornot;
        ImageView like;

        String url="https://complainbox2000.000webhostapp.com/upvote.php";
        public Myholder(@NonNull View itemView) {
            super(itemView);
            complaint_id=itemView.findViewById(R.id.complaint_id);
            complaint=itemView.findViewById(R.id.complaint);
            subject=itemView.findViewById(R.id.subject);
            upvotedbyyouornot=itemView.findViewById(R.id.upvotedbyyouornot);
            emp_name=itemView.findViewById(R.id.raised_by);
            like=itemView.findViewById(R.id.like);
            like.setColorFilter(context.getResources().getColor(R.color.grey));

            like.setOnClickListener(new View.OnClickListener() {
                boolean s=false;
                 public void onClick(View v) {
                    if(upvotedbyyouornot.getText().toString().equals("yes")){
                        s=true;
                    }
                    if(s){


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.trim().equals("ok")){

                                }else if(response.trim().equals("Error")){
                                    Toast.makeText(context, "something went wrong . please try again later", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(context, "something went wrong . please try again later", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "something went wrong . please try again later", Toast.LENGTH_SHORT).show();

                            }
                        }){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> map=new HashMap<>();
                                map.put("complaint_id",complaint_id.getText().toString().trim());
                                map.put("status","1");
                                map.put("email",email);
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                          //Toast.makeText(context, "downvote", Toast.LENGTH_SHORT).show();
                          upvotedbyyouornot.setText("no");
                          like.setColorFilter(context.getResources().getColor(R.color.grey));
                          s=false;
                    }
                    else {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.trim().equals("ok")){
                                    Toast.makeText(context, "upvoted", Toast.LENGTH_SHORT).show();

                                }else if(response.trim().equals("Error")){

                                    Toast.makeText(context, "something went wrong . please try again later", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(context, "something went wrong . please try again later", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "something went wrong . please try again later", Toast.LENGTH_SHORT).show();

                            }
                        }){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> map=new HashMap<>();
                                map.put("complaint_id",complaint_id.getText().toString().trim());
                                map.put("status","0");
                                map.put("email",email);
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);

                        //Toast.makeText(context, "upvote", Toast.LENGTH_SHORT).show();
                        upvotedbyyouornot.setText("yes");
                        like.setColorFilter(context.getResources().getColor(R.color.primaryTextColor));
                        s=true;
                    }
                }
            });
            }
    }


}
