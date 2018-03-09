package com.pj2.pacman;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.BitmapFactory;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by xk on 2016/12/24.
 */

public class GameView extends View implements Runnable {
    public static Thread UIThread;
    public static Thread CONThread;
    public static boolean istrue;
    public static GameView gameView;
    private int Ox = 0;
    private int Oy = 0;
    private final static int handOx = 1064;
    private final static int handOy = 513;
    private final static int handR = 100;
    public  Hero hero;
    private int screenW , screenH;
    private Bitmap mapBit;
    Movie tmp;
    Movie tmp0;
    int dur;
    int dur0;
    Movie picture;
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        for (int i = 0; i < 9; i++) {
            canvas.drawBitmap(mapBit, Ox + Map.iconX[i], Oy + Map.iconY[i] + 24, null);
        }
        long  cur = System.currentTimeMillis();
        tmp0.setTime((int) (cur%dur0));
        tmp.setTime((int) (cur%dur));
        for (Bean bean:Bean.beans){
            if (!bean.eaten){
                if (bean.type==Bean.BIG){
                    for (int i = 0; i < 9; i++) {
                        tmp0.draw(canvas, bean.x + Ox + Map.iconX[i], bean.y + Oy + Map.iconY[i] + 12);
                    }
                }else{
                    for (int i = 0; i < 9; i++) {
                        tmp.draw(canvas, bean.x + Ox + Map.iconX[i], bean.y + Oy + Map.iconY[i] + 12);
                    }
                }
            }
        }
        hero.picture.setTime((int) (cur%hero.picture.duration()));
        hero.picture.draw(canvas,screenW/2,screenH/2);
        if (hero.sleepTime!=0){
            picture.setTime(3000-15*hero.sleepTime);
            picture.draw(canvas,screenW/2-275,screenH/2-200);
        }
        for (Enemy enemy:Enemy.enemies) {
            enemy.picture.setTime((int) (cur%enemy.picture.duration()));
            for (int i = 0; i < 9; i++) {
                enemy.picture.draw(canvas, enemy.x + Ox + Map.iconX[i], enemy.y + Oy + Map.iconY[i]);
            }
        }
    }

    private void init(){
        Chart.loadChart();
        picture = Movie.decodeStream(this.getResources().openRawResource(Creature.getResID("p_3stimer2")));
        gameView = this;
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenW = wm.getDefaultDisplay().getWidth();
        screenH = wm.getDefaultDisplay().getHeight();
        Map.view = this;
        if (!Pause.pause){
            Pacman.newGame(this);
        }
        Pause.pause = false;
        Map map = Pacman.map;
        hero = Pacman.hero;
        mapBit = map.backBitMap;
        tmp = Bean.pic;
        tmp0 = Bean.picB;
        dur = tmp.duration();
        dur0 = tmp0.duration();
        istrue = true;
    }
    Context myContext;
    public GameView(Context context) throws IOException {
        super(context);
        myContext = context;
        this.setBackgroundColor(Color.rgb(47, 60, 42));
        init();
    }


    @Override
    public void run() {
        while (istrue) {
            long start = System.currentTimeMillis();
            logic();
            this.postInvalidate();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 25) {
                    Thread.sleep(25 - (end - start));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        UIThread.interrupt();
    }

    /**
     * 触屏事件监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float dx = x- handOx;
        float dy = y- handOy;
        if (dx-dy<0){
            hero.direction = dx+dy>0?Creature.DOWN:Creature.LEFT;
        }else {
            hero.direction = dx+dy>0?Creature.RIGHT:Creature.UP;
        }
        if (event.getAction()==MotionEvent.ACTION_UP){
            Message message = new Message();
            message.obj = event;
            message.arg1 = GameActivity.HIDE_HAND;
            GameActivity.mHandler.sendMessageDelayed(message,0);
            return true;
        }
        Message message = new Message();
        message.obj = event;
        message.arg1 = GameActivity.UPDATE_HAND;
        GameActivity.mHandler.sendMessageDelayed(message,0);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 游戏逻辑
     */
    private void logic() {
        Ox = screenW/2-hero.x;
        Oy = screenH/2-hero.y;
        for (int i = 0; i < Enemy.size; i++) {
            Enemy.enemies.get(i).move();
        }
    }
}
