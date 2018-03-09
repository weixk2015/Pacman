package com.pj2.pacman;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by xk on 2016/12/25.
 */

public class handView extends ImageView {
    private final static int handOx = 1051;
    private final static int handOy = 519;
    private final static int handR = 100;

    public handView(Context context) {
        super(context);
        this.setImageResource(R.drawable.p_handp);
        this.setX(200);
        this.setVisibility(View.INVISIBLE);
    }
}
