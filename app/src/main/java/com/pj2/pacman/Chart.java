package com.pj2.pacman;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Chart {

    String name;
    int score;
    private static final String file = "chart.cht";
    static ArrayList<Chart> charts = new ArrayList<>();

    Chart(String name, int score) {
        this.name = name;
        this.score = score;
        if (charts.size() > 8) charts.remove(8);
        charts.add(this);
    }

    public static void loadChart() {
        try {
            charts.clear();
            FileInputStream fis = MainMenu.activity.openFileInput(file);
            Scanner input = new Scanner(fis);
            while (input.hasNext()) {
                new Chart(input.next(), input.nextInt());
            }
            input.close();
            for (Chart chart:charts
                    ) {
                System.out.println(chart.name + "\n" + chart.score);
            }
            //charts.forEach(chart -> System.out.println(chart.name + " " + chart.score + " "));
        }
        catch (Exception ignored) {

        }
    }

    public static void saveChart() {
        for (Chart chart:charts
                ) {
            System.out.println(chart.name + "\n" + chart.score);
        }
        Collections.sort(charts,new sortByScore());
        try {
            FileOutputStream fos = MainMenu.activity.openFileOutput(file, Context.MODE_PRIVATE);
            PrintWriter output = new PrintWriter(fos);
            for (Chart chart:charts
                 ) {
                output.println(chart.name + "\n" + chart.score);
            }
            output.close();
        }
        catch (Exception ignored) {

        }
    }

    private static class sortByScore implements Comparator<Chart> {
        @Override
        public int compare(Chart chart1, Chart chart2) {
            if (chart1.score == chart2.score) return 0;
            return chart1.score > chart2.score ? -1 : 1;
        }
    }
}
