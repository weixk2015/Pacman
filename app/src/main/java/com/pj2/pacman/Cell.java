package com.pj2.pacman;

import android.graphics.Bitmap;

class Cell {
    static final int ROAD = 0;
    static final int WALL = 1;

    int x;
    int y;
    int type;
    Bitmap picture;

    Cell(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }



    static int typeOf(int x, int y) {
        //找到含点(x,y)的Cell，返回地形
        return Pacman.map.background[y / Map.SIDE][x / Map.SIDE].type;
    }

}
