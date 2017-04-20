package fr.ldnr.f1perfs;

import android.content.Intent;
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

public class ChartActivity extends AppCompatActivity {

    LinearLayout layout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Deserialisation du fichier xml
        layout = (LinearLayout) LinearLayout.inflate(this, R.layout.activity_chart, null);

        setTableResult();

        setContentView(layout);

    }

    public void setTableResult()
    {
        TableLayout table = (TableLayout) layout.findViewById(R.id.chart_table);
        TableRow row = new TableRow(this);
        TextView txt = new TextView(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,2);
        txt.setLayoutParams(lp);
        txt.setPadding(10, 10, 10, 10);
        txt.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txt.setTextColor(ContextCompat.getColor(this, R.color.textColor));
        txt.setText("Aucun résultat trouvé !");
        row.addView(txt);
        table.addView(row);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart, menu);
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
