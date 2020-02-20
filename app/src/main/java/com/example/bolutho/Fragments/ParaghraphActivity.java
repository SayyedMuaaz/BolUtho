package com.example.bolutho.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bolutho.R;

public class ParaghraphActivity extends AppCompatActivity {
    private TextView full_Paragraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paraghraph);
        Intent intent=getIntent();
        String detail=intent.getStringExtra("detail");
        full_Paragraph=findViewById(R.id.full_paragraph);
        full_Paragraph.setText(detail);
    }
}
