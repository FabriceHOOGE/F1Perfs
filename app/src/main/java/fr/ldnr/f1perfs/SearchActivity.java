package fr.ldnr.f1perfs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by dfour on 19/04/2017.
 */

public class SearchActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
                startActivity(new Intent(this, NewTime.class));
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
