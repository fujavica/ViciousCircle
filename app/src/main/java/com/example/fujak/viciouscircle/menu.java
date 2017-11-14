package com.example.fujak.viciouscircle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class menu extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        LinearLayout btn_single = (LinearLayout) findViewById(R.id.singleplayer);
        btn_single.setOnClickListener(this);
        LinearLayout btn_multi = (LinearLayout) findViewById(R.id.multiplayer);
        btn_multi.setOnClickListener(this);
        LinearLayout btn_highscore = (LinearLayout) findViewById(R.id.highscore);
        btn_highscore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.singleplayer:
            {
                Intent intent = new Intent(this, MainActivity.class);
                //startActivityForResult(intent,i);
                startActivity(intent);
                break;
            }

            case R.id.multiplayer:
            {
                break;
            }

            case R.id.highscore:
            {
                Intent intent = new Intent(this, HighscoreActivity.class);
                //startActivityForResult(intent,i);
                startActivity(intent);
                break;
            }
        }
}
}
