package fr.ldnr.f1perfs;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewTimeActivity extends AppCompatActivity {

    private DBOpenHelper dboh;
    private String sEvent, sTrack, sPilot;
    private int iTime;
    private boolean isValidForm = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //selon l'orientation l'application utilise le bon layout.
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            setContentView(R.layout.activity_new_time_h);
            Log.i("NewTimeActivity", "onCreate : landscape");
        }else
        {
            setContentView(R.layout.activity_new_time);
        }

        //Création de l'objet pour accéder à la base de données
        dboh = new DBOpenHelper(this);
        setAutoCompleteEditText();

    }

    //cette fonction permet de mettre en place l'autocomplétion sur le champs "nom du circuit"
    private void setAutoCompleteEditText()
    {
        //Déclaration des variables pour la requete en base de données
        String[] select = new String[]{"track"};
        String[] where = new String[]{};
        String[] values = new String[]{};
        String orderBy = null;
        String limit = null;
        //recupération de l'objet de l'autocomplétion
        AutoCompleteTextView actvRace = (AutoCompleteTextView)findViewById(R.id.newtime_race_name);

        //Récupération des données pour le champs en autocomplétion
        ArrayList<ArrayList<String>> allTrack = dboh.selectRecord(select,where,values,orderBy,limit,true);
        //création de l'adapter qui sera associé au champs
        ArrayAdapter<String> aaRace = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,allTrack.get(0));
        //Association de l'adapter et du champs à autocompléter
        actvRace.setAdapter(aaRace);
    }

    @Override
    public int getRequestedOrientation() {
        return super.getRequestedOrientation();
    }

    public void saveNewTime(View view) {
        //Permet de savoir si l'insertion des données a reussi
        boolean validInsert = false;

        this.setsEvent(((EditText)findViewById(R.id.newtime_eventname)).getText().toString());
        this.setsPilot(((EditText)findViewById(R.id.newtime_pilot_name)).getText().toString());
        this.setsTrack(((EditText)findViewById(R.id.newtime_race_name)).getText().toString());
        this.setiTime(((EditText)findViewById(R.id.newtime_minutes)).getText().toString(),
                ((EditText)findViewById(R.id.newtime_secondes)).getText().toString(),
                ((EditText)findViewById(R.id.newtime_millisecondes)).getText().toString());

        //Si les informations sont valides on insert les données
        if(this.isValidForm)
        {
            validInsert = dboh.insertRecord(sEvent, iTime,sTrack,sPilot);
            //Si l'insertion a échoué, un log d'erreur est émis
            //Et un message de type Toast est envoyé à l'utilisateur.
            if(!validInsert)
            {
                Log.e("NewTimeActivity", "saveNewTime: error during new time insertion!!!");
                Toast.makeText(this,R.string.newtime_record_fail,Toast.LENGTH_LONG).show();
            }else
            {
                //Si l'insertion à réussi l'utilisateur est alerté par un message de type Toast
                //Le formulaire est réinitialisé
                Toast.makeText(this,R.string.newtime_record_successful,Toast.LENGTH_LONG).show();
                //Mise a zéro du formulaire
                this.resetForm();
                //Mise à zéro des variables membres
                this.setsPilot("");
                this.setsTrack("");
                this.setsEvent("");
                this.setiTime("0","0","0");
                //Mise à jour de l'aide à la saisi sur le nom du circuit.
                setAutoCompleteEditText();
            }
        }else
        {
            //On averti l'utilisateur que les données saisies ne sont pas valides
            Toast.makeText(this,R.string.newtime_invalid_info,Toast.LENGTH_LONG).show();
        }
        this.isValidForm = true;
    }

    private void resetForm()
    {
        ((EditText)findViewById(R.id.newtime_eventname)).setText("");
        ((EditText)findViewById(R.id.newtime_pilot_name)).setText("");
        ((EditText)findViewById(R.id.newtime_race_name)).setText("");
        ((EditText)findViewById(R.id.newtime_minutes)).setText("");
        ((EditText)findViewById(R.id.newtime_secondes)).setText("");
        ((EditText)findViewById(R.id.newtime_millisecondes)).setText("");
    }

    public String getsEvent()
    {
        return sEvent;
    }

    public String getsTrack()
    {
        return sTrack;
    }

    public String getsPilot()
    {
        return sPilot;
    }

    public int getsTime()
    {
        return iTime;
    }

    public void setsEvent(String event)
    {
        this.sEvent = event;
        this.isValidForm =  this.isValidForm && Validator.isValidNewTimeEvent(event);
    }

    public void setsTrack(String track)
    {
        this.sTrack = track;
        this.isValidForm = this.isValidForm && Validator.isValidNewTimeRace(track);
    }

    public void setsPilot(String pilot)
    {
        this.sPilot = pilot;
        this.isValidForm = this.isValidForm && Validator.isValidNewTimePilot(pilot);
    }

    public void setiTime(String minute, String second, String millisecond)
    {
        this.iTime = Validator.timeToMillisecond(minute, second, millisecond);
        this.isValidForm = this.isValidForm && Validator.isValidNewTimeLapTime(minute, second, millisecond);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newtime, menu);
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
