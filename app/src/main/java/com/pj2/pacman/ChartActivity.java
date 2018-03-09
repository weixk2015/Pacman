package com.pj2.pacman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by xk on 2016/12/29.
 */

public class ChartActivity extends Activity {


    /**
     * Created by xk on 2016/12/29.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Chart.loadChart();
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View view1 = inflater.inflate(R.layout.charts_layout, null);
        Button button3 = (Button) view1.findViewById(R.id.backToGame);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addContentView(view1,p);
        int i = 0;
        for (Chart chart:Chart.charts
                ) {
            TextView textView = new TextView(this);
            textView.setText(chart.name + "\t\t\t" + chart.score);
            textView.setX(360);
            if (i < 3)
                textView.setY(90+i*66);
            else
                textView.setY(270+(i-3)*50);
            textView.setTextColor(Color.WHITE);
            addContentView(textView,p);
            if (i<3){
                textView.setTextScaleX(2);
            }
            i++;
        }

    }


}
