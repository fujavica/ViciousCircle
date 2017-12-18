package com.example.fujak.viciouscircle;

import android.content.Context;

import com.example.fujak.viciouscircle.GameView;

/**
 * Created by Fujak on 18.12.2017.
 */

public class breakThread extends Thread{
    GameView screen;
        breakThread(GameView c){
            screen = c;

        }
        public void run() {
            try {
                Thread.sleep(3000);
                screen.restartLevel();
            }
            catch(InterruptedException v) {
            }
        }
}
