package com.pj2.pacman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
    static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        LayoutInflater inflater = LayoutInflater.from(this);
        Chart.loadChart();
        @SuppressLint("InflateParams") View view1 = inflater.inflate(R.layout.activity_main_menu, null);
        Button button = (Button) view1.findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pause.pause = false;
                finish();
                Intent intent = new Intent(MainMenu.this , GameActivity.class);
                startActivity(intent);
            }
        });
        Button button1 = (Button) view1.findViewById(R.id.exit);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button button2 = (Button) view1.findViewById(R.id.help);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this , HelpActivity.class);
                startActivity(intent);
            }
        });
        Button button3 = (Button) view1.findViewById(R.id.chart);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this , ChartActivity.class);
                startActivity(intent);
            }
        });
        setContentView(view1);
    }
}
