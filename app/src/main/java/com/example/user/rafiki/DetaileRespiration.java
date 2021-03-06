package com.example.user.rafiki;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.rafiki.ItemData.Cycle;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.user.rafiki.DetaileCardiaque.Liste_donne;

public class DetaileRespiration extends AppCompatActivity {

    ImageView Etat_Cycle, Txt_Cycle, Cercle;
    TextView Txt_max, Txt_min, Txt_moy;
    SharedPreferences prefs, pref;
    GraphView graph;
    ArrayList<Double> list_poumon = new ArrayList<>();
    MySQLiteOpenHelper helper;
    UserDataSource ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaile_respiration);

        Etat_Cycle = findViewById(R.id.etat_cycle);
        Txt_Cycle = findViewById(R.id.txt_etat);
        Txt_max = findViewById(R.id.chifre_max);
        Txt_min = findViewById(R.id.chiffre_min);
        Txt_moy = findViewById(R.id.chifre_moys);
        Cercle = findViewById(R.id.imageView17);
        graph = (GraphView) findViewById(R.id.graph);

        prefs = getSharedPreferences("Cycle", MODE_PRIVATE);
        pref = getSharedPreferences("Inscription", MODE_PRIVATE);
        helper = new MySQLiteOpenHelper(this, "Utilisateur", null);
        ds = new UserDataSource(helper);
        Test_Donnees();

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(80);

        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);

        graph.getGridLabelRenderer().setHorizontalAxisTitle(getString(R.string.tempsinst));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.CYAN);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.CYAN);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.CYAN);
        graph.getGridLabelRenderer().setGridColor(Color.CYAN);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getViewport().setScrollable(true);

        String fuldate = prefs.getString("Date_Cycle", null);
        if (fuldate != null && ds.getCountCycle(fuldate) > 0) {
            if (Liste_donne.size() > 0) {

                for (Cycle c : Liste_donne) {
                    list_poumon.add(c.getPoumon());
                }
            }
        }


        if (ds.getCountCycle(fuldate) > 0) {

            int x = 0;
            DataPoint[] points = new DataPoint[Liste_donne.size()];
            for (int i = 0; i < points.length; i++) {

                points[i] = new DataPoint(x, Liste_donne.get(i).getPoumon());
                x += 5;
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(7);
            series.setThickness(7);
            series.setColor(Color.CYAN);
            graph.addSeries(series);
        }

        Txt_max.setText(String.valueOf(Collections.max(list_poumon)));
        Txt_min.setText(String.valueOf(Collections.min(list_poumon)));
        Txt_moy.setText(String.valueOf(Math.round(Moyenne_Val(list_poumon))));
    }

    public double Moyenne_Val(ArrayList<Double> list) {
        int total = 0;
        for (double val : list) {
            total += val;
        }
        return total / list.size();
    }

    public void precedant(View view) {
        Intent ite = new Intent(this, DetaileCardiaque.class);
        startActivity(ite);
        overridePendingTransition(R.anim.exit_to_right, R.anim.enter_from_left);
    }

    public void suivant(View view) {
        Intent ite = new Intent(this, DetaileTemperature.class);
        startActivity(ite);
//        overridePendingTransition(R.anim.exit_to_left, R.anim.enter_from_right);
    }

    public void parammetres(View view) {
        Intent ite = new Intent(this, MenuActivity.class);
        startActivity(ite);
    }

    public void Test_Donnees() {
        int Indice = prefs.getInt("Indice", 0);

        if (Indice != 0) {
            switch (Indice) {
                case 1:
                    Etat_Cycle.setImageResource(R.drawable.icon_quotidien);
                    Txt_Cycle.setImageResource(R.drawable.quotidien);
                    Cercle.setVisibility(View.GONE);
                    break;
                case 2:

                    Etat_Cycle.setImageResource(R.drawable.icon_marche);
                    Txt_Cycle.setImageResource(R.drawable.marche);
                    Cercle.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    Etat_Cycle.setImageResource(R.drawable.icone_course);
                    Txt_Cycle.setImageResource(R.drawable.course_a_pied);
                    Cercle.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    Etat_Cycle.setImageResource(R.drawable.icone_cycle);
                    Txt_Cycle.setImageResource(R.drawable.cyclisme);
                    Cercle.setVisibility(View.GONE);
                    break;
                case 5:
                    Etat_Cycle.setImageResource(R.drawable.icon_sommeil);
                    Txt_Cycle.setImageResource(R.drawable.sommeil);
                    Cercle.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void exite(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ite = new Intent(this, DetaileCardiogramme.class);
            startActivity(ite);
        }
        return false;
    }

    public void acueil(View view) {
        Intent ite = new Intent(this, E8.class);
        startActivity(ite);
    }

    public void historique(View view) {
        Intent ite = new Intent(this, HistoriqueActivity.class);
        startActivity(ite);
    }

    public void Cycle(View view) {
        startActivity(new Intent(this, CycleActivity.class));
    }

    public void supprimer(View view) {
        final String fuldate = prefs.getString("Date_Cycle", null);

        if (fuldate != null) {
            AlertDialog.Builder alt = new AlertDialog.Builder(this);
            alt.setTitle(" " + getString(R.string.finir_activity))
                    .setIcon(R.drawable.alert)
                    .setMessage("\n " + getString(R.string.text_supprimer_cycle))
                    .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ds.deleteCycle(fuldate);
                            startActivity(new Intent(getApplicationContext(), ParametresMesures.class));
                        }
                    })
                    .setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
    }
}
