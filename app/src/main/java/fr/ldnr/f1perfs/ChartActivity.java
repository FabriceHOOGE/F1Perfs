package fr.ldnr.f1perfs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.GraphViewXML;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import static android.widget.TableRow.*;

public class ChartActivity extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    LinearLayout layout = null;
    String[] pilots;
    String event, pilot, track;
    int min, max;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Connexion à la BDD (et éventuellement création)
        dbOpenHelper = new DBOpenHelper(this);

        // Deserialisation du fichier xml
        layout = (LinearLayout) LinearLayout.inflate(this, R.layout.activity_chart, null);

        event = getIntent().getStringExtra("event");
        pilot = getIntent().getStringExtra("pilot");
        track = getIntent().getStringExtra("track");

        min = getIntent().getIntExtra("minutemin",0);
        max = getIntent().getIntExtra("minutemax",0);

        fillTableResult();

        generateChart();

        setContentView(layout);


    }

    /**
     *   Génération graphique
     *
     *
     **/
    public void generateChart()
    {
        // Récupération graph
        GraphView graph = (GraphView) layout.findViewById(R.id.graph);

        // Stylisation graph
        graph.setTitleColor(ContextCompat.getColor(this, R.color.textColor));
        graph.getGridLabelRenderer().setGridColor(ContextCompat.getColor(this, R.color.textColor));
        graph.getGridLabelRenderer().setVerticalLabelsColor(ContextCompat.getColor(this, R.color.textColor));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(ContextCompat.getColor(this, R.color.textColor));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(ContextCompat.getColor(this, R.color.textColor));
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(ContextCompat.getColor(this, R.color.textColor));
        graph.getGridLabelRenderer().setVerticalAxisTitle(getResources().getString(R.string.chart_vTitle));

        // Valeurs axe vertical
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setYAxisBoundsManual(true);

        // Valeurs axe horizontal (noms pilotes)
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(pilots);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        // Courbe 1 TEST
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 9.55),
                new DataPoint(1, 9.25),
                new DataPoint(2, 8.30),
                new DataPoint(3, 9),
                new DataPoint(4, 9.1)
        });
        series.setTitle("Track 1");
        graph.addSeries(series);

        // Courbe 2 TEST
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 4.6),
                new DataPoint(1, 4.7),
                new DataPoint(2, 4.5),
                new DataPoint(3, 4.6),
                new DataPoint(4, 4.9)
        });
        series2.setTitle("Track 2");
        // Couleur courbe 2
        series2.setColor(Color.RED);

        // GetSecondScale apparemment obligatoire
        graph.getSecondScale().addSeries(series2);
        // Obligé de rentrer manuellement les valeurs axe vertical 2
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(10);
        // Couleur axe vertical 2
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);

        // Courbe 3 TEST
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 8),
                new DataPoint(1, 2.4),
                new DataPoint(2, 4),
                new DataPoint(3, 8.5),
                new DataPoint(4, 2.9)
        });
        series3.setTitle("Track 3");
        // Couleur courbe 2
        series3.setColor(Color.GREEN);

        // GetSecondScale apparemment obligatoire
        graph.getSecondScale().addSeries(series3);


    }




    /**
    *   Génération cellule du tableau
     *
     * @param content
     *            = contenu de la cellule
    *
     **/
    public TextView setTableResult(String content)
    {
        TextView txt = new TextView(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,2);
        txt.setLayoutParams(lp);
        txt.setPadding(10, 10, 10, 10);
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(ContextCompat.getColor(this, R.color.textColor));
        txt.setText(content);
        return txt;
    }



    /**
     *   Remplissage du tableau
     *
     **/
    public void fillTableResult()
    {
        ArrayList<ArrayList<String>> queryResult = getSearchResult();
        TableLayout table = (TableLayout) layout.findViewById(R.id.chart_table);

        if(dbOpenHelper.isCorrect(queryResult))
        {
            // Récupération noms des pilotes pour le graph
            ArrayList<String> tmpArr = new ArrayList<>();
            for(int i = 0;i < queryResult.get(3).size();i++)
                if(!tmpArr.contains(queryResult.get(3).get(i)))
                    tmpArr.add(queryResult.get(3).get(i));

            pilots = tmpArr.toArray(new String[0]);

            for(int i = 0;i < queryResult.get(0).size();i++)
            {
                TableRow row = new TableRow(this);

                row.addView(setTableResult(queryResult.get(0).get(i)));
                row.addView(setTableResult(Validator.millisecondToTime(Integer.parseInt(queryResult.get(1).get(i)))));
                row.addView(setTableResult(queryResult.get(2).get(i)));
                row.addView(setTableResult(queryResult.get(3).get(i)));

                table.addView(row);
            }
        }
        else
        {
            TableRow row = new TableRow(this);
            row.addView(setTableResult("Aucun résultat trouvé !"));
            table.addView(row);
        }
    }



    /**
     *   Récupération résultat de la recherche
     *
     **/
    public ArrayList<ArrayList<String>> getSearchResult()
    {
        // Déclaration du SELECT à effectuer
        String[] select = new String[]{"event", "time", "track", "pilot"}; // SELECT pilot, time

        String[] where;
        String[] values;

        if((event == null || event.trim().isEmpty()) && (track == null || track.trim().isEmpty()) && (pilot == null || pilot.trim().isEmpty()) && min == 0 && max == 0)
        {
            where = new String[]{}; // Pas de WHERE
            values = new String[]{}; // Pas de WHERE
        }
        else
        {
            List<String> tmpWhere = new ArrayList<>();
            List<String> tmpValues = new ArrayList<>();
            if(event != null){tmpWhere.add("event");tmpValues.add(event);}
            if(track != null){tmpWhere.add("track");tmpValues.add(track);}
            if(pilot != null){tmpWhere.add("pilot");tmpValues.add(pilot);}

            where = tmpWhere.toArray(new String[0]);
            values = tmpValues.toArray(new String[0]);
        }



        String orderBy = "time"; // ORDER BY time
        String limit = "6"; // LIMIT 6
        Boolean distinct = false; // Pas de DISTINCT

        // Fonction qui envoie le SELECT et retourne un tableau
        return dbOpenHelper.selectRecord(select, where, values, orderBy, limit, distinct);
    }




    /**
     *   Génération menu
     *
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart, menu);
        return true;
    }


    /**
     *   Evènement on click sur les éléments du menu
     *
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.menu_record:
                startActivity(new Intent(this, NewTimeActivity.class));
                return true;

            case R.id.menu_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;

            case R.id.menu_backHome:
                startActivity(new Intent(this, StartActivity.class));
                return true;

            default:
                return false;
        }

    }
}
