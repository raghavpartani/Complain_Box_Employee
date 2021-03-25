package com.example.complain_box;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

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
        upvote.add("5 upvotes");
        subject.add("water problem");
        complaint.add("water is not coming whiufe whheehwo eiheh eioheh eoihe ejdeifhh ehhhhhhhh whhhhhhhh wjjj");
        status.add("Status=Rejected");
        upvote.add("1 upvote");
        subject.add("water problem");

        complaint.add("water is not coming whiufe whheehwo eiheh eioheh eoihe ej");
        status.add("Status=Pending");
        upvote.add("3 upvotes");
        subject.add("water problem");
        complaint.add("water is not coming whiufe whheehwo eiheh eioheh eoihe ejwater is not coming whiufe whheehwo eiheh eioheh eoihe ej");
        status.add("Status=Resolved");
        mgr = new LinearLayoutManager(this);

        rcv.setLayoutManager(mgr);
        adapter=new ArrayAdapterMyComplaints(this,complaint,status,upvote,subject);
        rcv.setAdapter(adapter);

    }
}