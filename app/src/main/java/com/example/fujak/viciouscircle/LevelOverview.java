package com.example.fujak.viciouscircle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class LevelOverview extends Activity implements AdapterView.OnItemClickListener{
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ListView lv;
    ListView lv2;
    List<String> my_level_strings;
    List<String> my_level_strings2;
    int progressLevel;
    int maxLevel = 1;
    int pickedLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_overview);
        lv = findViewById(R.id.levelView);
        lv2 = findViewById(R.id.levelView2);

        SharedPreferences sharedPref = getSharedPreferences("com.example.fujak.viciouscircle", MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        progressLevel = sharedPref.getInt("progress", 1);

        my_level_strings = new ArrayList<String>();
        my_level_strings2 = new ArrayList<String>();
        while(this.getResources().getIdentifier("level" + maxLevel,"raw", this.getPackageName()) != 0)
        {
            if(progressLevel >= maxLevel)my_level_strings.add("Level " + maxLevel);
            else    my_level_strings2.add("Level " + maxLevel);
            maxLevel++;
        }
        maxLevel--;

        adapter = new ArrayAdapter<String>(
                this,
                R.layout.my_level_item,
                my_level_strings);
        adapter2 = new ArrayAdapter<String>(
                this,
                R.layout.my_level_item2,
                my_level_strings2);
        lv.setAdapter(adapter);
        lv2.setAdapter(adapter2);
        lv.setDividerHeight(10);
        lv2.setDividerHeight(10);
        lv.setOnItemClickListener(this);
       // lv2.setOnItemClickListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("level", i+1);
        pickedLevel = i+1;
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(pickedLevel == progressLevel && resultCode == RESULT_OK && pickedLevel < maxLevel){
            progressLevel++;
            my_level_strings.add("Level " + progressLevel);
            my_level_strings2.remove(0);

            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();

            SharedPreferences sharedPref = getSharedPreferences("com.example.fujak.viciouscircle", MODE_PRIVATE);
            Bundle extras = getIntent().getExtras();

            sharedPref = this.getSharedPreferences(
                    "com.example.fujak.viciouscircle", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();


            int newLevel = pickedLevel + 1;
            editor.putInt("progress", newLevel);
            editor.commit();
        }
    }
}
