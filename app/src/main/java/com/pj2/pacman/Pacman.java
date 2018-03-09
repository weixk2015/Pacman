package com.pj2.pacman;

import android.os.Message;
import android.view.View;

class Pacman {
    static Map map;
    static Hero hero;
    static int life;
    static int score;
    static boolean isPause;

    static void newGame(View view) {
        life = 3;
        score = 0;
        isPause = true;
        Enemy.size = 0;
        Enemy.enemies.clear();
        Bean.beans.clear();
        map = new Map();
        map.loadMap(view.getResources().openRawResource(R.raw.given));
    }

    static void countDown() {
        isPause = true;
        //count down
        isPause = false;
    }

    static void judge() {
        for (Enemy enemy: Enemy.enemies) {
            if (enemy.sleep) continue;
            if ((enemy.x - hero.x) * (enemy.x - hero.x) +
                    (enemy.y - hero.y) * (enemy.y - hero.y) < Map.SIDE * Map.SIDE / 2) {
                if (enemy.type < 2) {
                    life --;
                    if (life > 0) {
                        for (Enemy enemy1: Enemy.enemies) enemy1.backToLife();
                        hero.backToLife();
                        countDown();
                    }
                    Message message = new Message();
                    message.arg1 = GameActivity.UPDATE_LIVE;
                    message.arg2 = life;
                    GameActivity.mHandler.sendMessageDelayed(message,0);
                }
                else {
                    enemy.backToLife();
                    score += 100;
                    Message message = new Message();
                    message.arg1 = GameActivity.UPDATE_SCORE;
                    message.arg2 = score;
                    GameActivity.mHandler.sendMessageDelayed(message,0);
                }
            }
        }
        if (life <= 0) {
            for (Enemy enemy1: Enemy.enemies) enemy1.sleep = true;
            hero.sleep = true;
            //go to fail
            Message message = new Message();
            message.arg1 = GameActivity.HANDLE_DEAD;
            message.arg2 = score;
            GameActivity.mHandler.sendMessageDelayed(message,0);
        }
    }
    
    static void eat() {
        boolean allEat = true;
        for (Bean bean: Bean.beans) {
            if ((!bean.eaten) && (bean.x - hero.x) * (bean.x - hero.x) +
                    (bean.y - hero.y) * (bean.y - hero.y) < Map.SIDE * Map.SIDE / 4) {
                bean.eaten = true;
                score += 10;
                if (bean.type == Bean.BIG) {
                    bean.strong();
                    score += 90;
                }
                Message message = new Message();
                message.arg1 = GameActivity.UPDATE_SCORE;
                message.arg2 = score;
                GameActivity.mHandler.sendMessageDelayed(message,0);

            }
            if (!bean.eaten) allEat = false;
        }
        if (allEat) {
            score += 300 * life;
            for (Enemy enemy1: Enemy.enemies) enemy1.sleep = true;
            hero.sleep = true;
           // go to win
            Message message = new Message();
            message.arg2 = score;
            message.arg1 = GameActivity.HANDLE_WIN;
            GameView.CONThread.interrupt();
            GameView.CONThread.interrupt();
            GameActivity.mHandler.sendMessageDelayed(message,0);

        }
    }

    static void gamePause() {
        if (isPause) {
            isPause = false;
        }
        else {
            isPause = true;
        }
    }

}
