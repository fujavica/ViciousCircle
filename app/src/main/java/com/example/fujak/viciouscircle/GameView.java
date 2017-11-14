package com.example.fujak.viciouscircle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Fujak on 11/5/2017.
 */

public class GameView extends View {

    private int width;
    private int height;
    Context context;
    int tickRate = 10;
    Paint obstaclePaint;
    Paint bigCirclePaint;
    Paint smallCirclePaint;
    Timer timer;
    ArrayList<Obstacle> obstacles;
    Circle circle;
    int bigRadius;
    int smallRadius = 35;
    int circleCenter_x;
    float circleCenter_y;
    double alpha = 0;
    double beta = Math.PI;
    float circle_x;
    float circle_y;

    float circle2_x;
    float circle2_y;

    double angularVelocity = 0.04;
    double upper_difference;
    double left_difference;

    Rotation rotation = Rotation.STATIONARY;

    MediaPlayer mediaPlayer;
    boolean paused = false;
    int level = 1;

    public GameView(Context context) {
        super(context);
        init(context);
        // this.setOnTouchListener(this);
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resID = context.getResources().getIdentifier("level" + level,"raw",context.getPackageName());
        mediaPlayer = MediaPlayer.create(context, resID);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        obstacles = DataManager.loadLevel();

        circle = new Circle(200);
        obstaclePaint = new Paint();
        obstaclePaint.setColor(Color.rgb(255, 255, 255));
        //obstaclePaint.setColor(Color.rgb(100, 200, 100));

        bigCirclePaint = new Paint();
        bigCirclePaint.setColor(Color.rgb(50, 50, 50));
        bigCirclePaint.setStyle(Paint.Style.STROKE);
        bigCirclePaint.setStrokeWidth(8);

        smallCirclePaint = new Paint();
        smallCirclePaint.setColor(Color.rgb(250, 50, 250));

        mediaPlayer.start();

        timer = new Timer();
        timer.schedule(new Controller(this), tickRate, tickRate);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("Size change","Size changed: from " + oldw + " " + oldh + " to " + w + " " + h);

        width = w;
        height = h;

        circleCenter_x = width / 2;
        circleCenter_y = (float) (height - (height / 6));
        bigRadius = width / 4 ;
        circle_x = circleCenter_x - bigRadius;
        circle2_x = circleCenter_x + bigRadius;
        circle_y = circleCenter_y;
        circle2_y = circleCenter_y;
        super.onSizeChanged(w, h, oldw, oldh);
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle(circleCenter_x, circleCenter_y , bigRadius ,bigCirclePaint);
        canvas.drawCircle(circle_x  , circle_y, smallRadius,smallCirclePaint);
        canvas.drawCircle(circle2_x  , circle2_y, smallRadius,smallCirclePaint);

        for (Obstacle o : obstacles) {
            canvas.drawRect(o.posx, o.posy, o.posx + o.width, o.posy+ o.height,obstaclePaint);
        }
    }



    public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_UP) return true;

        //Activity host = (Activity) this.getContext();
        //TextView text = (TextView) host.findViewById(R.id.text);
        // text.setText("Congratz!");

    if(event.getAction() == MotionEvent.ACTION_UP)
    {
        rotation = Rotation.STATIONARY;
        return true;
    }

        double x = event.getX();
        if (x > width / 2)
            rotation = Rotation.CLOCKWISE;
        else
            rotation = Rotation.COUNTERCLOCKWISE;


        return true;
    }


    public void moveObstacles(){
        obstacles = Obstacle.iLikeToMoveItMoveIt(obstacles);
    }

    public void moveCircles()
    {
        if(rotation == Rotation.STATIONARY) {
            return;
        }

        if(rotation == Rotation.CLOCKWISE)
        {
            alpha += angularVelocity;
            beta += angularVelocity;
        }
        else
        {
            alpha -= angularVelocity;
            beta -= angularVelocity;
        }
        circle_x = (float) (bigRadius*Math.cos(alpha) + circleCenter_x);
        circle_y = (float) (bigRadius*Math.sin(alpha) + circleCenter_y);
        circle2_x = (float) (bigRadius*Math.cos(beta) + circleCenter_x);
        circle2_y = (float) (bigRadius*Math.sin(beta) + circleCenter_y);
    }

    public boolean detectCollisions()
    {

        for(Obstacle o : obstacles)
        {
            //if(o.posy + o.height < circle_y - smallRadius && o.posy >  circle_y + smallRadius)

            upper_difference = circle_y - smallRadius  - o.posy;
            if(upper_difference <  o.height && upper_difference > - (smallRadius*2)) // Y match?
            {
             left_difference = circle_x - smallRadius - o.posx;
                if(left_difference <  o.width && left_difference > - (smallRadius*2)) // X match?
                {
                    gameOver(); //Collision with circle 1
                }

            }
            upper_difference = circle2_y - smallRadius  - o.posy;
            if(upper_difference <  o.height && upper_difference > - (smallRadius*2)) // Y match?
            {
                left_difference = circle2_x - smallRadius - o.posx;
                if(left_difference <  o.width && left_difference > - (smallRadius*2)) // X match too?
                {
                    gameOver(); //Collision with circle 2
                }

            }

        }
        return false;
    }

    public void gameOver()
    {
        timer.cancel();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, R.raw.gameover);
        mediaPlayer.start();
        end();

    }

    public void end()
    {
       // mediaPlayer.release();
       // mediaPlayer = null;
    }

    @Override
    public void onDetachedFromWindow() {
        mediaPlayer.release();
        mediaPlayer = null;
        timer.cancel();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility){
        if (visibility == View.VISIBLE){
            if(paused){
                timer = new Timer();
                timer.schedule(new Controller(this), tickRate, tickRate);
                mediaPlayer.start();
            }
        }
        else
        {
            mediaPlayer.pause();
            timer.cancel();
            paused = true;
        }
    }


}

