package com.pj2.pacman;

import android.graphics.Bitmap;
import android.graphics.Movie;

import java.util.ArrayList;

class Bean {
    static ArrayList<Bean> beans = new ArrayList<>();

    final static int SMALL = 2;
    final static int BIG = 3;
    static Movie pic;
    static Movie picB;
    int x;
    int y;
    int type;
    boolean eaten;

    Bean(int x, int y, int newType) {
        this.x = x;
        this.y = y;
        type = newType;
        eaten = false;
        beans.add(this);
    }

    void strong() {
        for (Enemy enemy: Enemy.enemies) {
            enemy.time = Enemy.WEAK_TIME;
            if (enemy.type < 2) enemy.type += 3;
            enemy.speed = Enemy.WEAK_SPEED;
            enemy.picture = Movie.decodeStream(Map.view.getResources().openRawResource(Creature.getResID("p_" + enemy.type + "" + enemy.direction)));
        }
    }
}