package fr.ldnr.f1perfs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dfour on 19/04/2017.
 */

public class SearchActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //selon l'orientation l'application utilise le bon layout.
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            setContentView(R.layout.activity_search_h);
            Log.i("NewTimeActivity", "onCreate : landscape");
        }else
        {
            setContentView(R.layout.activity_search);
        }
    }


    //methode à utiliser pour la recherche en base de données
    public void searchTime(View View) {
        AutoCompleteTextView actvSearchEvent = (AutoCompleteTextView) findViewById(R.id.search_search_event);
        TextView tvSearchTime = (TextView) findViewById(R.id.search_search_time);
        EditText etSearchMinuteMin = (EditText) findViewById(R.id.search_search_minute_min);
        EditText etSearchSecondeMin = (EditText) findViewById(R.id.search_search_seconde_min);
        EditText etSearchMinuteMax = (EditText) findViewById(R.id.search_search_minute_max);
        EditText etSearchSecondeMax = (EditText) findViewById(R.id.search_search_seconde_max);
        AutoCompleteTextView actvSearchPilot = (AutoCompleteTextView) findViewById(R.id.search_searchpilot);
        AutoCompleteTextView actvSearchTrack = (AutoCompleteTextView) findViewById(R.id.search_searchtrack);
        Button btSearchSearch = (Button) findViewById(R.id.search_search);

        Log.i("SearchActivity", "Evenement: " + actvSearchEvent.getText() + "|| Temps: " + tvSearchTime.getText() + "||MinuteMin: " + etSearchMinuteMin.getText() + "||" +
                "|| SecondeMin: " + etSearchSecondeMin.getText() + "||MinuteMax: " + etSearchMinuteMax.getText() + "||" +
                "|| SecondeMax: " + etSearchSecondeMax.getText() + "|| Pilote: " + actvSearchPilot.getText() + "|| Circuit: " + actvSearchTrack.getText());

        //conversion données saisies en milliseconde
        int minutemin, minutemax;
        minutemin = Validator.timeToMillisecond(etSearchMinuteMin.getText().toString(), etSearchSecondeMin.getText().toString(), "0");
        minutemax = Validator.timeToMillisecond(etSearchMinuteMax.getText().toString(), etSearchSecondeMax.getText().toString(), "0");

        //envoi des données
        Intent intent = new Intent(SearchActivity.this, ChartActivity.class);
        intent.putExtra("event", actvSearchEvent.getText().toString());
        intent.putExtra("pilot", actvSearchPilot.getText().toString());
        intent.putExtra("track", actvSearchTrack.getText().toString());
        intent.putExtra("minutemin", minutemin);
        intent.putExtra("minutemax", minutemax);

        setResult(0, intent);

        startActivityForResult(intent, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
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
