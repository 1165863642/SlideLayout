package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> dataLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        listView = findViewById(R.id.lv);

        dataLists = new ArrayList<>();
        //data模拟
        dataLists.add("data1");
        dataLists.add("data2");
        dataLists.add("data3");


        SlideAdapter adapter = new SlideAdapter(this, dataLists);
        listView.setAdapter(adapter);
    }
}
