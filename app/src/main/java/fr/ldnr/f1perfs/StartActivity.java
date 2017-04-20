package fr.ldnr.f1perfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    // La classe connexion de la BDD
    DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Connexion à la BDD (et éventuellement création)
        dbOpenHelper = new DBOpenHelper(this);

        // Insertion multiple pour test
        /*exempleInsertSelectBDD("Entraînement","01:52.244","Paul Ricard","John McFly");
        exempleInsertSelectBDD("Compétition","04:47.877","Le Mans","Fabrice Hooges");
        exempleInsertSelectBDD("Session découverte","01:44.558","Paul Ricard","Kévin Diez");
        exempleInsertSelectBDD("Pour le fun","01:13.445","Abu Dhabi","John McFly");
        exempleInsertSelectBDD("Compétition","03:52.276","Le Mans","David Fournier");
        exempleInsertSelectBDD("Championnat","59:38.133","Karting Indoor","David Fournier");
        exempleInsertSelectBDD("Entraînement","59:38.276","Karting Indoor","Fabrice Hooges");
        exempleInsertSelectBDD("Compétition","59:39.566","Karting Indoor","Kévin Diez");
        exempleInsertSelectBDD("Session découverte","00:12.001","Paul Ricard","Le STIG");
        exempleInsertSelectBDD("Pour le fun","00:42.963","Abu Dhabi","John Rambo");
        exempleInsertSelectBDD("Compétition","02:23.752","Le périph de Toulouse","Rocky Balboa");*/

        // Récupération du dernier enregistrement
        getLastRecord();

        // Suppression de la BDD
        // deleteDB();
    }


    /*
    * Récupération du dernier enregistrement et affichage de celui-ci sur la page d'accueil
    */
    private void getLastRecord()
    {
        // Récupération des TextView à MAJ
        TextView tvTime = (TextView) findViewById(R.id.start_txtTime);
        TextView tvTrack = (TextView) findViewById(R.id.start_txtTrack);
        TextView tvPilot = (TextView) findViewById(R.id.start_txtPilot);

        // Déclaration du SELECT à effectuer
        String[] select = new String[]{"time", "track", "pilot"}; // SELECT time, track, pilot
        String[] where = new String[]{}; // Pas de WHERE
        String[] values = new String[]{}; // Pas de WHERE = pas de valeurs à chercher
        String orderBy = "_id DESC"; // ORDER BY _id DESC
        String limit = "1"; // LIMIT 1
        Boolean distinct = false; // Pas de DISTINCT

        // Fonction qui envoie le SELECT et retourne un tableau
        ArrayList<ArrayList<String>> queryResult = dbOpenHelper.selectRecord(select, where, values, orderBy, limit, distinct);

        // MAJ des TextView s'il y a bien un résultat : indice 0 -> time, 1 -> track, 2 -> pilot
        if(queryResult.get(0).size() == 1 && queryResult.get(1).size() == 1 && queryResult.get(2).size() == 1)
        {
            tvTime.setText(queryResult.get(0).get(0));
            tvTrack.setText(queryResult.get(1).get(0));
            tvPilot.setText(queryResult.get(2).get(0));
        }
    }

    /*
    * Suppression de la base de données
    */
    private void deleteDB()
    {
        this.deleteDatabase(DBOpenHelper.Constants.DATABASE_NAME);
        Toast.makeText(this, getResources().getString(R.string.start_toastDeleteDB), Toast.LENGTH_LONG);
    }

    /*
    * Exemple d'utilisation des méthodes Insert et Select
    */
    private void exempleInsertSelectBDD(String event, String time, String track, String pilot)
    {
        // Insert + Mssg Confirmation OU Erreur
        if(dbOpenHelper.insertRecord(event, time, track, pilot))
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
        Boolean distinct = false; // Pas de DISTINCT

        // Fonction qui envoie le SELECT et retourne un tableau
        ArrayList<ArrayList<String>> queryResult = dbOpenHelper.selectRecord(select, where, values, orderBy, limit, distinct);

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
