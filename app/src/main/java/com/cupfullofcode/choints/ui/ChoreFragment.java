package com.cupfullofcode.choints.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.cupfullofcode.choints.R;
import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.Chore;
import com.cupfullofcode.choints.infrastructure.SQLiteChoreRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChoreFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SQLiteChoreRepository choresRepository = null;
    private Child child = null;
    private ArrayAdapter<Chore> choresAdapter = null;

    @Override
    public void onPause() {
        choresRepository.close();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        populateChoresList();
    }

    @Override
    public void onResume() {
        choresRepository = new SQLiteChoreRepository(getActivity());
        try {
            choresRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_chore) {
            addNewChore();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choresRepository = new SQLiteChoreRepository(getActivity());
        setHasOptionsMenu(true);

        try {
            choresRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras == null) return;

        this.child = (Child) extras.getSerializable("child");
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ChoreFragment newInstance(int sectionNumber) {
        ChoreFragment fragment = new ChoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public ChoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_detail_chore, container, false);

        choresAdapter = new ArrayAdapter<Chore>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList<Chore>()
        );

        ListView choresListView = (ListView) rootView.findViewById(R.id.listView_chores);
        choresListView.setAdapter(choresAdapter);

        return rootView;
    }

    protected void populateChoresList() {
        List<Chore> chores = choresRepository.chores(child);
        choresAdapter.setNotifyOnChange(true);

        choresAdapter.clear();
        for (Chore chore : chores) {
            choresAdapter.add(chore);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_chore, menu);
    }

    protected void addNewChore() {
        LayoutInflater inflater = getLayoutInflater(null);
        final View view;
        view = inflater.inflate(R.layout.alert_add_chore, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(view);
        alert.setTitle(R.string.add_chore);
        alert.setMessage(R.string.add_chore_message);

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText descriptionEditText = (EditText) view.findViewById(R.id.chore_description);
                EditText pointsEditText = (EditText) view.findViewById(R.id.chore_points);

                String description = descriptionEditText.getText().toString();
                long points = Integer.parseInt(pointsEditText.getText().toString());

                choresRepository.add(description, points, child);
                populateChoresList();
            }
        });

        alert.show();
    }
}
