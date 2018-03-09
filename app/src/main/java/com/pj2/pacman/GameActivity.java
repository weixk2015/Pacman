package com.pj2.pacman;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.io.IOException;

import static android.view.View.INVISIBLE;
import static com.pj2.pacman.GameView.CONThread;
import static com.pj2.pacman.GameView.UIThread;

/**
 * Created by xk on 2016/12/24.
 */

public class GameActivity extends Activity {
    static int READY_START = 7;
    final static int HANDLE_WIN = 6;
    final static int HANDLE_DEAD = 5;
    final static int UPDATE_LIVE = 4;
    final static int UPDATE_SCORE = 3;
    final static int UPDATE_HAND = 1;
    final static int HIDE_HAND = 2;
    private final static int handOx = 1051;
    private final static int handOy = 500;
    private final static int handR = 100;
    private static handView hand;
    private static Context context;
    private static LinearLayout scoreView;
    private static LinearLayout liveView;
    private static int liveID;
    static Activity activity;
    static Movie picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        //显示自定义的SurfaceView 视图
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ViewGroup.LayoutParams scoreP = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 60
        );
        ViewGroup.LayoutParams liveP = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 60
        );
        ViewGroup.LayoutParams handP = new ViewGroup.LayoutParams(
                75, 75
        );
        try {
            context = this;
            addContentView(new GameView(this),p);
            LayoutInflater inflater = LayoutInflater.from(this);
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.unchangeview, null);
            addContentView(view,p);
            hand = new handView(this);
            addContentView(hand,handP);
            scoreView = new LinearLayout(this);
            scoreView.setX(700);
            scoreView.setPadding(0,20,0,0);
            liveView = new LinearLayout(this);
            liveView.setGravity(Gravity.LEFT);
            liveView.setOrientation(LinearLayout.HORIZONTAL);
            liveView.setPadding(0,20,0,0);
            liveView.setX(410);
            liveID = Creature.getResID("p_hero"+3+"p");
            for (int i = 0;i<3;i++){
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(liveID);
                liveView.addView(imageView,40,40);
            }
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(Creature.getResID("p_score"+1+"p"));
            scoreView.addView(imageView,40,40);
            addContentView(liveView,liveP);
            addContentView(scoreView,scoreP);
            @SuppressLint("InflateParams") View view1 = inflater.inflate(R.layout.pause_button, null);
            addContentView(view1,p);
            Button button = (Button) view1.findViewById(R.id.pause_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivities(new Intent[]{
                            new Intent(GameActivity.this,Pause.class)
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case UPDATE_HAND: {
                    MotionEvent event = (MotionEvent) msg.obj;
                    float x = event.getX();
                    float y = event.getY();
                    float dx = x- handOx;
                    float dy = y- handOy;
                    float distance = (float)Math.sqrt((dx*dx+dy*dy));
                    float handX;
                    float handY;
                    GameActivity.mHandler.sendEmptyMessageDelayed(GameActivity.UPDATE_HAND,0);
                    if (distance<=handR){
                        handX = x;
                        handY = y;
                    }else {
                        float sin = dy/distance;
                        handY = handOy + sin *handR;
                        float cos = dx/distance;
                        handX = handOx + cos *handR;
                    }
                    hand.setX(handX-37.5f);
                    hand.setY(handY-37.5f);
                    if (hand.getVisibility()==INVISIBLE)
                        hand.setVisibility(ImageView.VISIBLE);
                    break;
                }
                case HIDE_HAND:{
                    hand.setVisibility(INVISIBLE);
                    break;
                }
                case UPDATE_SCORE:{
                    scoreView.removeAllViews();
                    int score = msg.arg2;
                    String sscore = String.valueOf(score);
                    int len = sscore.length();
                    for(int i = 0;i<len;i++){
                        ImageView imageView = new ImageView(context);
                        imageView.setImageResource(Creature.getResID("p_score"+(sscore.charAt(i)-'0'+1)+"p"));
                        scoreView.addView(imageView,40,40);
                    }
                    break;
                }
                case UPDATE_LIVE:{
                    liveView.removeAllViews();
                    for (int i = 0;i<msg.arg2;i++){
                        ImageView imageView = new ImageView(context);
                        imageView.setImageResource(liveID);
                        liveView.addView(imageView,40,40);
                    }
                    break;
                }
                case HANDLE_DEAD:{
                    context.startActivities(new Intent[]{
                            new Intent(context,LoseActivity.class)
                    });
                    break;
                }
                case HANDLE_WIN:{
                    int score = msg.arg2;
                    for (Chart chart:Chart.charts
                            ) {
                        System.out.println(chart.name + "\n" + chart.score);
                    }
                    if (Chart.charts.size()<8||Chart.charts.get(7).score<score){
                        context.startActivities(new Intent[]{
                                new Intent(context,AddToChart.class)
                        });
                    }else {
                        context.startActivities(new Intent[]{
                                new Intent(context,WinActivity.class)
                        });
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    @Override
    public void onPause(){
        super.onPause();  // Always call the superclass method first
        GameView.istrue = false;
    }
    @Override
    public void onBackPressed() {
        startActivities(new Intent[]{
                new Intent(GameActivity.this,Pause.class)
        });
    }
    @Override
    public void onResume(){
        super.onPause();  // Always call the superclass method first
        GameView.istrue = true;
        UIThread = new Thread(GameView.gameView);
        UIThread.start();
        CONThread = new Thread(){
            @Override
            public void run() {
                while (GameView.istrue) {
                    long start = System.currentTimeMillis();
                    Pacman.hero.move();
                    long end = System.currentTimeMillis();
                    try {
                        if (end - start < 15) {
                            Thread.sleep(15 - (end - start));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CONThread.interrupt();
            }
        };
        CONThread.start();
    }
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
