package com.example.complain_box;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    TextView signin;
    Button signup;
    Spinner company;
    ArrayAdapter<String>arrayAdapter;
    ArrayList<String>a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signin=findViewById(R.id.login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        signup=findViewById(R.id.signup);
        company=findViewById(R.id.company);
        a=new ArrayList<>();
        company.setPrompt("Title");
        a.add("Company Name");
        a.add("tcs");
        a.add("wipro");
        a.add("infosys");
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,a);
        company.setAdapter(arrayAdapter);
   }
}