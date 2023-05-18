package com.zhuguohui.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_like).setOnClickListener(v->{
            Toast.makeText(this,"点赞成功",Toast.LENGTH_SHORT).show();
        });
    }
}