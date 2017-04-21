package fr.ldnr.f1perfs;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import static android.widget.TableRow.*;

public class ChartActivity extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    LinearLayout layout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Connexion à la BDD (et éventuellement création)
        dbOpenHelper = new DBOpenHelper(this);

        // Deserialisation du fichier xml
        layout = (LinearLayout) LinearLayout.inflate(this, R.layout.activity_chart, null);

        fillTableResult();


        float[] values = new float[] { 2.0f,1.5f, 2.5f, 1.0f , 3.0f };
        String[] verlabels = new String[] { "great", "ok", "bad" };
        String[] horlabels = new String[] { "today", "tomorrow", "next week", "next month" };
        ChartView chartView = new ChartView(this, values, "GraphViewDemo",horlabels, verlabels, ChartView.LINE);

        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.chart_layout);

        setContentView(chartView);


        //setContentView(layout);

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
        String orderBy = null; // Pas d'ORDER BY
        String limit = null; // Pas de LIMIT
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

            /*case R.id.menu_record:
                startActivity(new Intent(this, NewTimeActivity.class));
                return true;*/

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
