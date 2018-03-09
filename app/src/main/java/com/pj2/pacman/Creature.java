package com.pj2.pacman;

import java.lang.reflect.Field;

abstract class Creature {
    int x;
    int y;
    private int x_birth;
    private int y_birth;
    int direction;
    int speed;
    boolean sleep;
    int sleepTime;
    
    //direction array
    //0-8分别表示：上，右，下，左， 左上，右上，右下，左下，原地
    final static int UP = 0;
    final static int RIGHT = 1;
    final static int DOWN = 2;
    final static int LEFT = 3;
    final static int[] DIR_X = {0, 1, 0, -1, -1, 1, 1, -1, 0};
    final static int[] DIR_Y = {-1, 0, 1, 0, -1, -1, 1, 1, 0};
    //0-3分别表示：左上角上，右上角右，右下角下，左下角左
    private final static int[] DIR_X1 = {0, Map.SIDE, Map.SIDE - 1, -1};
    private final static int[] DIR_Y1 = {-1, 0, Map.SIDE, Map.SIDE - 1};
    //0-3分别表示：右上角上，右下角右，左下角下，左上角左
    private final static int[] DIR_X2 = {Map.SIDE - 1, Map.SIDE, 0, -1};
    private final static int[] DIR_Y2 = {-1, Map.SIDE - 1, Map.SIDE, 0};

    Creature(int x, int y) {
        x_birth = x;
        y_birth = y;
        setPos(x, y);
        sleep = true;
        direction = DOWN; //起始向下
    }

    //移动
    abstract void move();

    //复活
    void backToLife() {
        setPos(x_birth, y_birth);
        sleep = true;
        direction = DOWN;
    }

    //判断是否可动
    boolean canMove(int dir, int x, int y) {
        if (dir == -1) return false;
        int xOnDir1 = outOfIndex(x + DIR_X1[dir], true);
        int yOnDir1 = outOfIndex(y + DIR_Y1[dir], false);
        int xOnDir2 = outOfIndex(x + DIR_X2[dir], true);
        int yOnDir2 = outOfIndex(y + DIR_Y2[dir], false);
        boolean canMove1 = (Cell.typeOf(xOnDir1, yOnDir1) == Cell.ROAD);
        boolean canMove2 = (Cell.typeOf(xOnDir2, yOnDir2) == Cell.ROAD);
        return (canMove1 && canMove2);
    }

    //越界修正
    private int outOfIndex(int pos, boolean hv) { //true for x, false for y
        if (pos < 0 || pos >= Map.SIDE * (hv? Map.width: Map.height))
            pos -= (int)Math.signum(pos) * Map.SIDE * (hv? Map.width: Map.height);
        return pos;
    }

    void setPos(int x, int y) {
        this.x = outOfIndex(x, true);
        this.y = outOfIndex(y, false);
    }
    
    void changePos(int speed, int dir) {
        x = outOfIndex(x + speed * DIR_X[dir], true);
        y = outOfIndex(y + speed * DIR_Y[dir], false);
    }

    static int getResID(String name) {
        Class drawable = R.drawable.class;
        Field field = null;
        int resID = 0;
        try {
            field = drawable.getField(name);
            resID = field.getInt(field.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resID;
    }
}
