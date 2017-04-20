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

    DBOpenHelper dboh;



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

    private void setAutoCompleteEditText()
    {
        AutoCompleteTextView actvRace = (AutoCompleteTextView)findViewById(R.id.newtime_race_name);
        String[] select = new String[]{"track"};
        String[] where = new String[]{};
        String[] values = new String[]{};
        String orderBy = null;
        String limit = null;

        ArrayList<ArrayList<String>> allTrack = dboh.selectRecord(select,where,values,orderBy,limit,true);

        //String[] track = {"Bowser Castle","Plain Donut","Rainbow Road","Plain Donut2","Plain Donut3","Plain Donut4","Plain Donut5","Plain Donut6","Plain Donut7","Plain Donut8"};
        ArrayAdapter<String> aaRace = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,allTrack.get(0));
        actvRace.setAdapter(aaRace);
    }

    @Override
    public int getRequestedOrientation() {
        return super.getRequestedOrientation();
    }

    public void saveNewTime(View view) {
        //Permet de savoir si l'insertion des données a reussi
        boolean validInsert = false;
        //permet de savoir si les informations saisies sont correctes
        boolean validEntry = true;
        //Permet de stocker les inforamtions du formulaire
        String sEvent, sTrack, sPilot, sMinute, sSecond, sMillisecond, sTime;


        //Récupération des éléments de champs du formulaire
        EditText etEvent = (EditText)findViewById(R.id.newtime_eventname);
        EditText etTrack = (EditText)findViewById(R.id.newtime_race_name);
        EditText etPilot = (EditText)findViewById(R.id.newtime_pilot_name);
        EditText etMinute = (EditText)findViewById(R.id.newtime_minutes);
        EditText etSecond = (EditText)findViewById(R.id.newtime_secondes);
        EditText etMilliSecond = (EditText)findViewById(R.id.newtime_millisecondes);

        //A partir des éléments du formulaire, les variable de type String sont initialisées
        sEvent = etEvent.getText().toString();
        sTrack = etTrack.getText().toString();
        sPilot = etPilot.getText().toString();
        sMinute = etMinute.getText().toString();
        sSecond = etSecond.getText().toString();
        sMillisecond = etMilliSecond.getText().toString();
        sTime = formatTime(sMinute,sSecond,sMillisecond);

        //On valide si les informations entrées correspondent aux caractéristiques attendues
        //Grace à la classe Validator
        validEntry = validEntry && Validator.isValidNewTimeEvent(sEvent);
        validEntry = validEntry && Validator.isValidNewTimeRace(sTrack);
        validEntry = validEntry && Validator.isValidNewTimePilot(sPilot);
        validEntry = validEntry && Validator.isValidNewTimeLapTime(sMinute,sSecond,sMillisecond);

        Log.i("NewTimeActivity","Evènement: " + sEvent + "|| temps: | " + sTime + " |" + sMinute +  "|" + sMillisecond + "|" + sSecond);

        //Si les informations sont valides on insert les données
        if(validEntry)
        {
            validInsert = dboh.insertRecord(sEvent, sTime,sTrack,sPilot);
            //Si l'insertion a échoué, un log d'erreur est émis/
            //Et un message de type Toast est envoyé à l'utilisateur.
            if(!validInsert)
            {
                Log.e("NewTimeActivity", "saveNewTime: error during new time insertion!!!");
                Toast.makeText(this,"test échouée",Toast.LENGTH_LONG).show();
            }else
            {
                //Si l'insertion à réussi l'utilisateur est alerté par un message de type Toast
                //Le formulaire est réinitialisé
                Toast.makeText(this,"test reussi",Toast.LENGTH_LONG).show();
                etEvent.setText("");
                etTrack.setText("");
                etPilot.setText("");
                etMinute.setText("");
                etSecond.setText("");
                etMilliSecond.setText("");

                //Mise à jour de l'aide à la saisi sur le nom du circuit.
                setAutoCompleteEditText();
            }
        }else
        {
            Toast.makeText(this,"test invalide",Toast.LENGTH_LONG).show();
        }
    }

    private String formatTime(String minute,String second, String millisecond)
    {
        String result = "";
        if(minute.length()<2)
            result = result +"0";
        result = result + minute+":";

        if(second.length()<2)
            result=result + "0";
        result = result + second +".";

        if(millisecond.length()<3)
        {
            if(millisecond.length()<2)
                result = result+"0";
            result = result+"0";
        }
        result = result+millisecond;
        return result;
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
