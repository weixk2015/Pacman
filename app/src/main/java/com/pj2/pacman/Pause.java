package com.pj2.pacman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Pause extends AppCompatActivity {
    public static boolean pause = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View view1 = inflater.inflate(R.layout.activity_pause, null);
        Button button = (Button) view1.findViewById(R.id.backToGame);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause = true;finish();
            }
        });
        Button button1 = (Button) view1.findViewById(R.id.restart);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.activity.finish();
                pause = false;
                finish();
                Intent intent = new Intent(Pause.this , GameActivity.class);
                startActivity(intent);
            }
        });
        Button button2 = (Button) view1.findViewById(R.id.main_menu);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.activity.finish();
                finish();
                Intent intent = new Intent(Pause.this , MainMenu.class);
                startActivity(intent);
            }
        });
        Button button3 = (Button) view1.findViewById(R.id.help_button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pause.this , HelpActivity.class);
                startActivity(intent);
            }
        });
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        addContentView(view1,p);
    }
    @Override
    public void onBackPressed() {
        pause = true;finish();
    }
}
