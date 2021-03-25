package com.example.complain_box;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArrayAdapterMyComplaints extends RecyclerView.Adapter<ArrayAdapterMyComplaints.Myholder> {
    Context context;
    ArrayList<String> arrayList_upvotes;
    ArrayList<String> arrayList_status;
    ArrayList<String> arrayList_complaint;
    ArrayList<String> arrayList_subject;
    public ArrayAdapterMyComplaints(Context context, ArrayList<String> complaint, ArrayList<String> status, ArrayList<String> upvotes,ArrayList<String> subject) {
        this.context=context;
        this.arrayList_complaint=complaint;
        this.arrayList_upvotes=upvotes;
        this.arrayList_status=status;
        this.arrayList_subject=subject;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.my_complaint_view,parent,false);
        ArrayAdapterMyComplaints.Myholder holder = new ArrayAdapterMyComplaints.Myholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.complaint.setText(arrayList_complaint.get(position));
        holder.status.setText(arrayList_status.get(position));
        holder.upvotes.setText(arrayList_upvotes.get(position));
        holder.subject.setText(arrayList_subject.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList_complaint.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView complaint;TextView upvotes;TextView status;TextView subject;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            complaint=itemView.findViewById(R.id.complaint);
            upvotes=itemView.findViewById(R.id.upvote);
            status=itemView.findViewById(R.id.status);
            subject=itemView.findViewById(R.id.subject);
        }
    }
}
