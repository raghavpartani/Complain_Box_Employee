package com.example.complain_box;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class raise_a_compalint extends AppCompatActivity {

    EditText sub,details;
    Spinner category;
    ArrayList <String>a;
    ArrayAdapter<String>arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_a_compalint);
        details=findViewById(R.id.detailsofcompalint);
        sub=findViewById(R.id.subject);
        category=findViewById(R.id.category);
        a=new ArrayList<>();
        a.add("Category");
        a.add("Misconduct");
        a.add("Machinery");
        a.add("Other");
        arrayAdapter=new ArrayAdapter<String>(raise_a_compalint.this,android.R.layout.simple_expandable_list_item_1,a);
               category.setAdapter(arrayAdapter);
    }
}