package com.example.fujak.viciouscircle;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.bluetooth.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    Timer timer;
    MediaPlayer mediaPlayer;

    TextView title;
    BluetoothAdapter blueAdapter;
    BluetoothChatService bs;
    ArrayAdapter<String> pairedDevicesArrayAdapter;
    public int chosenLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        chosenLevel = extras.getInt("level",1);

        setContentView(R.layout.activity_main);

    }

    public void levelCompleted(){

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}


