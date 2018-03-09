package com.pj2.pacman;

import android.graphics.Movie;
import android.os.Message;

class Hero extends Creature {
    final private int FPS = 66;
    final private int SLEEP_TIME = FPS * 3;

    private int cur_dir;
    Movie picture;

    Hero(int x, int y) {
        super(x, y);
        sleepTime = SLEEP_TIME;
        speed = 4;
        picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_hero3")));
    }

    @Override
    void backToLife() {
        super.backToLife();
        sleepTime = SLEEP_TIME;
        picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_hero3")));
        cur_dir = DOWN;
    }

    @Override
    void move() {
        if (sleep) {
            sleepTime--;
            if (sleepTime == 0) sleep = false;
            return;
        }
        if (canMove(direction, x, y) && cur_dir != direction) {
            cur_dir = direction;
            picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_hero" + (direction + 1))));
            direction = -1;
        }
        if (canMove(cur_dir, x, y)) {
            changePos(speed, cur_dir);
        }
        Pacman.judge();
        Pacman.eat();
    }

}
