package com.example.complain_box;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArrayAdapterComplaints extends RecyclerView.Adapter<ArrayAdapterComplaints.Myholder> {
    Context context;
    ArrayList<String> arrayList_subject;
    ArrayList<String> arrayList_emp_name;
    ArrayList<String> arrayList_complaint;
    ArrayList<String> arrayList_complaint_id;
    ArrayList<String> arrayList_upvoted_by_you_or_not;

    public ArrayAdapterComplaints(Context context, ArrayList<String> complaint, ArrayList<String> emp_name, ArrayList<String> subject,ArrayList<String> complaint_id,ArrayList<String> upvoted_by_you_or_not) {
        this.context=context;
        this.arrayList_complaint=complaint;
        this.arrayList_subject=subject;
        this.arrayList_emp_name=emp_name;
        this.arrayList_complaint_id=complaint_id;
        this.arrayList_upvoted_by_you_or_not=upvoted_by_you_or_not;
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
                    if(s){
                    like.setColorFilter(context.getResources().getColor(R.color.grey));
                    s=false;
                    }
                    else {
                        Toast.makeText(context, "upvoted", Toast.LENGTH_SHORT).show();
                        like.setColorFilter(context.getResources().getColor(R.color.primaryTextColor));
                        s=true;
                    }
                }
            });

        }
    }
}
