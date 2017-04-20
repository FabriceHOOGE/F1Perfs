package fr.ldnr.f1perfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        exempleInsertSelectBDD();

    }

    private void getLastRecord()
    {
        // La classe construction de la BDD
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this);


    }

    private void exempleInsertSelectBDD()
    {
        // La classe construction de la BDD
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this);

        // Insert + Mssg Confirmation OU Erreur
        if(dbOpenHelper.insertRecord("Session", "00:00.000", "Nürburgring", "toto tata"))
            Toast.makeText(this, getResources().getString(R.string.dbo_insertWin), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, getResources().getString(R.string.dbo_insertError), Toast.LENGTH_LONG).show();

        // Sélection du pilote et du temps, depuis la table Perfs (par défaut), où event == "Session"
        // Trié par ordre décroissant en fonction du pilote, en limitant la rechercher à 10 résultats à partir du 5ème
        
        // Déclaration du SELECT à effectuer
        String[] select = new String[]{"pilot", "time"}; // SELECT pilot, time
        String[] where = new String[]{"event"}; // WHERE event=?
        String[] values = new String[]{"Session"}; // WHERE event='Session'
        String orderBy = "pilot DESC"; // ORDER BY pilot DESC
        String limit = "5, 10"; // LIMIT 5, 10 (10 lignes à partir de la n°5)

        // Fonction qui envoie le SELECT et retourne un tableau
        ArrayList<ArrayList<String>> queryResult = dbOpenHelper.selectRecord(select, where, values, orderBy, limit);

        // Parcours du tableau multidimension correspondant au SELECT : indice 0 -> pilot, indice 1 -> time
        for (int i = 0; i < queryResult.get(0).size(); i++)
        {
            Log.i("N°" + i, queryResult.get(0).get(i) + " -> " + queryResult.get(1).get(i));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

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
