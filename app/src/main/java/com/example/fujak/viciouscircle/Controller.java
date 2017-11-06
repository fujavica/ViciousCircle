package com.example.fujak.viciouscircle;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Fujak on 11/6/2017.
 */

public class Controller extends TimerTask {
    GameView screen;
    public Controller(GameView c){
        screen = c;
    }


    @Override
    public void run() {
        screen.moveObstacles();
        screen.postInvalidate();
       /* try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}