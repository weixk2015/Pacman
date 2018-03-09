package com.pj2.pacman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by xk on 2016/12/29.
 */

public class AddToChart extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View view1 = inflater.inflate(R.layout.add_to_chart, null);
        Button button3 = (Button) view1.findViewById(R.id.ok);
        final EditText name = (EditText) view1.findViewById(R.id.name);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Chart(name.getText().toString(),Pacman.score);
                Chart.saveChart();
                finish();
                Intent intent = new Intent(AddToChart.this , WinActivity.class);
                startActivity(intent);
            }
        });
        setContentView(view1);
    }
}
