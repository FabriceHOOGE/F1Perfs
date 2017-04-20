package fr.ldnr.f1perfs;

import android.app.Activity;
import android.content.Intent;
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

/**
 * Created by dfour on 19/04/2017.
 */

public class SearchActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    //methode à utiliser pour la recherche en base de données
    public void searchActivity(View View) {
        AutoCompleteTextView actvSearchEvent = (AutoCompleteTextView) findViewById(R.id.search_search_event);
        TextView tvSearchTimeM = (TextView) findViewById(R.id.search_search_timeM);
        EditText etSearchMinute = (EditText) findViewById(R.id.search_search_minute);
        TextView tvSearchTimeS = (TextView) findViewById(R.id.search_search_timeS);
        EditText etSearchSeconds = (EditText) findViewById(R.id.search_search_seconds);
        AutoCompleteTextView actvSearchPilot = (AutoCompleteTextView) findViewById(R.id.search_searchpilot);
        AutoCompleteTextView actvSearchTrack = (AutoCompleteTextView) findViewById(R.id.search_searchtrack);
        Button btSearchSearch = (Button) findViewById(R.id.search_search);

        Log.i("SearchActivity", "Evenement: "+ actvSearchEvent.getText()+ "|| Temps: "+ tvSearchTimeM.getText()+"||Minute: "+etSearchMinute.getText()+"||" +
                "|| Temps: "+tvSearchTimeS.getText()+"|| Secondes: "+etSearchSeconds.getText()+"|| Pilote: "+actvSearchPilot.getText()+"|| Circuit: "+actvSearchTrack.getText());
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
