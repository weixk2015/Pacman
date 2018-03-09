package com.pj2.pacman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by xk on 2016/12/29.
 */

public class HelpActivity extends Activity {
    static int cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cur = 1;
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View view1 = inflater.inflate(R.layout.help_layout, null);
//        Button button = (Button) view1.findViewById(R.id.exit);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        Button button1 = (Button) view1.findViewById(R.id.to_left);
        final ImageView imageView = (ImageView)view1.findViewById(R.id.help_image);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cur>1){
                    cur--;
                    imageView.setImageResource(Creature.getResID("p_help"+cur));
                }
            }
        });
        Button button2 = (Button) view1.findViewById(R.id.to_right);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cur<3){
                    cur++;
                    imageView.setImageResource(Creature.getResID("p_help"+cur));
                }
            }
        });
        Button button3 = (Button) view1.findViewById(R.id.backToGame);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setContentView(view1);
    }
}
