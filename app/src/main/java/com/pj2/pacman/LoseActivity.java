package com.pj2.pacman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by xk on 2016/12/29.
 */

public class LoseActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View view1 = inflater.inflate(R.layout.lose_layout, null);
        Button button3 = (Button) view1.findViewById(R.id.backToGame);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.activity.finish();
                finish();
                Intent intent = new Intent(LoseActivity.this , MainMenu.class);
                startActivity(intent);
            }
        });
        setContentView(view1);
        TextView score = (TextView) view1.findViewById(R.id.score);
        score.setText(String.valueOf(Pacman.score));
    }
}
