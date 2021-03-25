package com.example.complain_box;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Complain_raised_by_companions extends AppCompatActivity {
    RecyclerView rcv;
    ArrayAdapterComplaints adapter;
    RecyclerView.LayoutManager mgr;
    ArrayList<String> complaint;
    ArrayList<String> subject;
    ArrayList<String> emp_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_raised_by_companions);
        rcv=findViewById(R.id.rcv);
        complaint=new ArrayList<>();
        subject=new ArrayList<>();
        emp_name=new ArrayList<>();
        subject.add("water problem");
        complaint.add("water is not coming whiufe whheehwo eiheh eioheh eoihe ejdeifhh ehhhhhhhh whhhhhhhh wjjj");
        emp_name.add("Riased by Raghav Partani");
        subject.add("water problem");
        complaint.add("water is not coming whiufe whheehwo eiheh eioheh eoihe ej");
        emp_name.add("Riased by Rajua aps");
        subject.add("water problem");
        complaint.add("water is not coming whiufe whheehwo eiheh eioheh eoihe ejwater is not coming whiufe whheehwo eiheh eioheh eoihe ej");
        emp_name.add("Riased by Raghav Partani");
        mgr = new LinearLayoutManager(this);

        rcv.setLayoutManager(mgr);
        adapter=new ArrayAdapterComplaints(this,complaint,emp_name,subject);
        rcv.setAdapter(adapter);

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
}