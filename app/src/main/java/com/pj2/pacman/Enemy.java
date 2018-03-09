package com.pj2.pacman;

import android.graphics.Movie;

import java.util.ArrayList;
import java.util.Random;

class Enemy extends Creature {
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static int size;
    
    final private static int SLOW_TYPE = 1;
    //final static int FAST_TYPE = 0;
    final private static int SLOW_SPEED = 4;
    final private static int FAST_SPEED = 2 * SLOW_SPEED;
    final static int WEAK_SPEED = 1;
    
    final private static int FPS = 40;
    final private static int SLEEP_TIME = FPS * 3;
    final private static int BLIND_TIME = FPS * 3;
    final static int WEAK_TIME = ((FPS * 5) / Map.SIDE) * Map.SIDE;
    final private static int WARN_TIME = FPS * 2;

    Movie picture;
    int type;
    private int newDir;
    private boolean cantMove = false;
    int time;

    Enemy(int x, int y, int newType) {
        super(x, y);
        type = newType;
        speed = type == SLOW_TYPE ? SLOW_SPEED: FAST_SPEED;
        time = BLIND_TIME;
        sleepTime = SLEEP_TIME;
        direction = randomDir();
        newDir = direction;
        enemies.add(this);
        size++;
        picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_" + type + "2")));
    }

    @Override
    void backToLife() {
        super.backToLife();
        if (type > 2) type -= 3;
        sleepTime = SLEEP_TIME;
        time = BLIND_TIME;
        speed = type == SLOW_TYPE ? SLOW_SPEED: FAST_SPEED;
        picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_" + type + "2")));
        direction = randomDir();
        newDir = direction;
        time = BLIND_TIME;
    }

    @Override
    void move() {
        if (sleep) {
            sleepTime--;
            if (sleepTime == 0) sleep = false;
            return;
        }
        if (cantMove) return;
        if (time >= 0) {
            if (type > 2 && time > 0 && time < WARN_TIME) {
                //图标闪动
                if (time % 10 == 0) {
                    picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_" + (type - 3) + "" + direction)));
                }
                else if (time % 10 == 5) {
                    picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_" + type + "" + direction)));
                }
            }
            if (time == 0 && type > 2) {
                if (x % FAST_SPEED != 0) setPos(FAST_SPEED * (x / FAST_SPEED), y);
                if (y % FAST_SPEED != 0) setPos(x, FAST_SPEED * (y / FAST_SPEED));
                type -= 3;
                speed = type == SLOW_TYPE ? SLOW_SPEED: FAST_SPEED;
                picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_" + type + "" + direction)));
            }
            time--;
        }
        if (x % Map.SIDE == 0 && y % Map.SIDE == 0) {
            if (time < 0 && x == Pacman.hero.x && y > Pacman.hero.y && canMove(UP, x, y))
                newDir = UP;
            else if (time < 0 && x == Pacman.hero.x && y < Pacman.hero.y && canMove(DOWN, x, y))
                newDir = DOWN;
            else if (time < 0 && y == Pacman.hero.y && x > Pacman.hero.x && canMove(LEFT, x, y))
                newDir = LEFT;
            else if (time < 0 && y == Pacman.hero.y && x < Pacman.hero.x && canMove(RIGHT, x, y))
                newDir = RIGHT;
            else if (type > 2 && x == Pacman.hero.x && y > Pacman.hero.y && canMove(DOWN, x, y))
                newDir = DOWN;
            else if (type > 2 && x == Pacman.hero.x && y < Pacman.hero.y && canMove(UP, x, y))
                newDir = UP;
            else if (type > 2 && y == Pacman.hero.y && x > Pacman.hero.x && canMove(RIGHT, x, y))
                newDir = RIGHT;
            else if (type > 2 && y == Pacman.hero.y && x < Pacman.hero.x && canMove(LEFT, x, y))
                newDir = LEFT;
            else if (canMove((direction + 1) % 4, x, y) || canMove((direction + 3) % 4, x, y)) {
                do {
                    newDir = randomDir();
                } while (!canMove(newDir, x, y) || newDir == (direction + 2) % 4);
            }
            else if (canMove(direction, x, y)) newDir = direction;
            else if (canMove((direction + 2) % 4, x, y)) newDir = (direction + 2) % 4;
            else {
                cantMove = true;
                return;
            }
        }
        if (direction != newDir) {
            direction = newDir;
            picture = Movie.decodeStream(Map.view.getResources().openRawResource(getResID("p_" + type + "" + direction)));
        }
        changePos(speed, direction);
    }

    private int randomDir() {
        return (new Random()).nextInt(4);
    }

}
