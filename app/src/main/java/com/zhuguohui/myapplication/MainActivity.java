package com.zhuguohui.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Toast;

import com.zhuguohui.myapplication.stack.StackLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StackLayout stackView=findViewById(R.id.stackView);
        MyStackAdapter myStackAdapter = new MyStackAdapter();
        stackView.setAdapter(myStackAdapter);
        mockDatas(myStackAdapter);
    }

    private void mockDatas(MyStackAdapter myStackAdapter) {
        List<String> datas=new ArrayList<>();
        for(int i=0;i<20;i++){
            datas.add(i+"");
        }
        myStackAdapter.addData(datas);

    }
}