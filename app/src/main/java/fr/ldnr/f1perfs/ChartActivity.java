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

import static android.widget.TableRow.*;

public class ChartActivity extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    LinearLayout layout = null;
    String[] pilots;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Connexion à la BDD (et éventuellement création)
        dbOpenHelper = new DBOpenHelper(this);

        // Deserialisation du fichier xml
        layout = (LinearLayout) LinearLayout.inflate(this, R.layout.activity_chart, null);

        fillTableResult();

        generateChart();

        setContentView(layout);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String event = data.getStringExtra("event");
        String pilot = data.getStringExtra("pilot");
        String track = data.getStringExtra("track");

        int min = data.getIntExtra("minutemin",0);
        int max = data.getIntExtra("minutemax",0);

        Log.i("TEST","\nEvent : " + event + "\nPilot : " + pilot + "\nTrack : " + track + "\nMin : " + min + "\nMax : " + max);

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
                new DataPoint(0, 100),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setTitle("Track 1");
        graph.addSeries(series);

        // Courbe 2 TEST
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 5),
                new DataPoint(1, 2),
                new DataPoint(2, 3),
                new DataPoint(3, 9),
                new DataPoint(4, 0)
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

                for(int j = 0;j < queryResult.size();j++)
                    row.addView(setTableResult(queryResult.get(j).get(i)));

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
        String[] where = new String[]{}; // Pas de WHERE
        String[] values = new String[]{}; // Pas de WHERE
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
