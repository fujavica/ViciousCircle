package com.example.fujak.viciouscircle;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class menu extends Activity implements View.OnClickListener{
    MediaPlayer mediaPlayer;//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        int resID = getResources().getIdentifier("intro","raw",getPackageName());
        mediaPlayer = MediaPlayer.create(this, resID);
        //mediaPlayer.start();
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
        mediaPlayer.release();
        switch(view.getId())
        {

            case R.id.singleplayer:
            {
                Intent intent = new Intent(this, LevelOverview.class);
                //startActivityForResult(intent,i);
                startActivity(intent);
                break;
            }

            case R.id.multiplayer:
            {
                Intent intent = new Intent(this, Multiplayer.class);
                //startActivityForResult(intent,i);
                startActivity(intent);
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
