package fr.ldnr.f1perfs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class NewTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_time);

        //Gestion de l'aide à la saisie dans le champs du nom du circuit
        AutoCompleteTextView actvRace = (AutoCompleteTextView)findViewById(R.id.newtime_race_name);
        String[] circuit = {"Bowser Castle","Plain Donut","Rainbow Road","Plain Donut2","Plain Donut3","Plain Donut4","Plain Donut5","Plain Donut6","Plain Donut7","Plain Donut8"};
        ArrayAdapter<String> aaRace = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,circuit);
        actvRace.setAdapter(aaRace);


    }


    public void saveNewTime(View view) {
        EditText etEvent = (EditText)findViewById(R.id.newtime_eventname);
        EditText etRace = (EditText)findViewById(R.id.newtime_race_name);
        EditText etPilot = (EditText)findViewById(R.id.newtime_pilot_name);
        EditText etMinute = (EditText)findViewById(R.id.newtime_minutes);
        EditText etSecond = (EditText)findViewById(R.id.newtime_secondes);
        EditText etMilliSecond = (EditText)findViewById(R.id.newtime_millisecondes);

        Log.i("NewTimeActivity","Evènement: " + etEvent.getText() + "|| Course: " + etRace.getText() + "|| Pilote: " + etPilot.getText());
        Log.i("NewTimeActivity","Temps enregistré : "+etMinute.getText() + ":" + etSecond.getText() + "." + etMilliSecond.getText());
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
