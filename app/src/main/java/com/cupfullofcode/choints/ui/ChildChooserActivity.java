package com.cupfullofcode.choints.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cupfullofcode.choints.R;
import com.cupfullofcode.choints.domain.ChildRepository;
import com.cupfullofcode.choints.infrastructure.SQLiteChildRepository;
import com.cupfullofcode.choints.infrastructure.SQLiteHelper;


public class ChildChooserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        ChildRepository childRepository = new SQLiteChildRepository(dbHelper);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_chooser);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ChildChooserFragment(childRepository))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child_chooser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
