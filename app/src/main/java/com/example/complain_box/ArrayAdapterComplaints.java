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
    public ArrayAdapterComplaints(Context context, ArrayList<String> complaint, ArrayList<String> emp_name, ArrayList<String> subject) {
        this.context=context;
        this.arrayList_complaint=complaint;
        this.arrayList_subject=subject;
        this.arrayList_emp_name=emp_name;
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
    }

    @Override
    public int getItemCount() {
        return arrayList_complaint.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView complaint;TextView subject;TextView emp_name;
        Button accept,reject;
        public Myholder(@NonNull View itemView) {
            super(itemView);

            complaint=itemView.findViewById(R.id.complaint);
            subject=itemView.findViewById(R.id.subject);
            emp_name=itemView.findViewById(R.id.raised_by);
            final ImageView like=itemView.findViewById(R.id.like);
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
