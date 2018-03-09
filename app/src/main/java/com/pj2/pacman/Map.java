package com.pj2.pacman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Rect;
import android.view.View;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Scanner;

class Map {
    Cell[][] background;
    Bitmap backBitMap;
    static View view;
    final static int SIDE = 64;

    static int width;
    static int height;

    //9个image实现unlimited地图
    static int[] iconX;
    static int[] iconY;
    //分别代表：上，右，下，左，左上，右上，右下，左下，左，上，右，下
    private final int[] dX = {0, 1, 0, -1, -1, 1, 1, -1, -1, 0, 1, 0};
    private final int[] dY = {-1, 0, 1, 0, -1, -1, 1, 1, 0, -1, 0, 1};

    void loadMap(InputStream inputStream) {
        try {
            Scanner input = new Scanner(inputStream);
            height = input.nextInt();
            width = input.nextInt();
            background = new Cell[height][width];

            iconX = new int[9];
            iconY = new int[9];
            for (int i = 0; i < 9; i++) {
                iconX[i] = Creature.DIR_X[i] * SIDE * width;
                iconY[i] = Creature.DIR_Y[i] * SIDE * height;
            }

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int thisType = input.nextInt();
                    background[i][j] = new Cell(SIDE * j, SIDE * i, (thisType == Cell.WALL ? Cell.WALL : Cell.ROAD));
                    if (thisType == Bean.SMALL) new Bean(SIDE * j, SIDE * i, Bean.SMALL);
                    if (thisType == Bean.BIG) new Bean(SIDE * j, SIDE * i, Bean.BIG);
                }
            }
            Bean.pic = Movie.decodeStream(view.getResources().openRawResource(Creature.getResID("p_gold_3")));
            Bean.picB = Movie.decodeStream(view.getResources().openRawResource(Creature.getResID("p_gold_2")));
            Pacman.hero = new Hero(input.nextInt() * Map.SIDE, input.nextInt() * Map.SIDE);
            int enemySize = input.nextInt();
            for (int i = 0; i < enemySize; i++) {
                new Enemy(input.nextInt() * SIDE, input.nextInt() * SIDE, input.nextInt());
            }

            input.close();
            loadImage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadImage() {
        String type1;
        int type2;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (background[i][j].type == Cell.WALL) {
                    type1 = "p_";
                    type2 = 0;
                    for (int d = 0; d < 4; d++) {
                        type1 += background[outOfIndex(i + dY[d], false)][outOfIndex(j + dX[d], true)].type;
                    }
                    for (int d = 4; d < 8; d++) {
                        type2 += Math.pow(10, 7 - d) *
                                background[outOfIndex(i + dY[d], false)][outOfIndex(j + dX[d], true)].type *
                                background[outOfIndex(i + dY[d + 4], false)][outOfIndex(j + dX[d + 4], true)].type *
                                background[outOfIndex(i + dY[d - 4], false)][outOfIndex(j + dX[d - 4], true)].type;
                    }
                    background[i][j].picture = getRes(type1 + "_" + type2);
                } else background[i][j].picture = getRes("p_road");
            }
        }
        Bitmap[] bitmaps = new Bitmap[height];
        for (int i = 0; i < height; i++){
            bitmaps[i] = background[i][0].picture;
            for (int j = 1; j < width; j++){
                bitmaps[i] = mergeBitmap_LR(bitmaps[i],background[i][j].picture);
            }
        }
        backBitMap = bitmaps[0];
        for (int i = 1; i < height; i++){
            backBitMap = mergeBitmap_TB(backBitMap,bitmaps[i]);

        }
    }

    //越界修正
    private int outOfIndex(int pos, boolean hv) { //true for x, false for y
        if (pos < 0 || pos >= (hv? width: height))
            pos -= (int)Math.signum(pos) * (hv? width: height);
        return pos;
    }

    static Bitmap getRes(String name) {
        Class drawable = R.drawable.class;
        Field field;
        int resID = 0;
        try {
            field = drawable.getField(name);
            resID = field.getInt(field.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeResource(view.getResources(),resID);
    }

    private static Bitmap mergeBitmap_LR(Bitmap leftBitmap, Bitmap rightBitmap) {

        int height = leftBitmap.getHeight();

        int width = leftBitmap.getWidth() + rightBitmap.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Rect leftRect = new Rect(0, 0, leftBitmap.getWidth(), leftBitmap.getHeight());
        Rect rightRect  = new Rect(0, 0, rightBitmap.getWidth(), rightBitmap.getHeight());

        Rect rightRectT  = new Rect(leftBitmap.getWidth(), 0, width, height);

        canvas.drawBitmap(leftBitmap, leftRect, leftRect, null);
        canvas.drawBitmap(rightBitmap, rightRect, rightRectT, null);
        return bitmap;
    }

    private static Bitmap mergeBitmap_TB(Bitmap topBitmap, Bitmap bottomBitmap) {

        int width = topBitmap.getWidth();

        int height = topBitmap.getHeight() + bottomBitmap.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Rect topRect = new Rect(0, 0, topBitmap.getWidth(), topBitmap.getHeight());
        Rect bottomRect  = new Rect(0, 0, bottomBitmap.getWidth(), bottomBitmap.getHeight());

        Rect bottomRectT  = new Rect(0, topBitmap.getHeight(), width, height);

        canvas.drawBitmap(topBitmap, topRect, topRect, null);
        canvas.drawBitmap(bottomBitmap, bottomRect, bottomRectT, null);
        return bitmap;
    }
    /**
    private static Bitmap merge9Bitmap(Bitmap bitmap) {

        int width = bitmap.getWidth()*3;
        int height = bitmap.getHeight()*3;
        int rawWidth = bitmap.getWidth();
        int rawHeight = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(bitmap,0,rawHeight, null);
        canvas.drawBitmap(bitmap);
        return bitmap;
    }**/

}
